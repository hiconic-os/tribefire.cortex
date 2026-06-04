// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.web.servlet.logs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

import com.braintribe.cfg.InitializationAware;
import com.braintribe.cfg.Required;
import com.braintribe.common.lcd.Numbers;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.logs.request.DownloadSelectedLogFiles;
import com.braintribe.model.logs.request.FileInfo;
import com.braintribe.model.logs.request.ListLogFiles;
import com.braintribe.model.logs.request.LogFilesList;
import com.braintribe.model.logs.request.Logs;
import com.braintribe.model.processing.service.common.FailureCodec;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.model.service.api.MulticastRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Failure;
import com.braintribe.model.service.api.result.MulticastResponse;
import com.braintribe.model.service.api.result.ResponseEnvelope;
import com.braintribe.model.service.api.result.ServiceResult;
import com.braintribe.utils.DateTools;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.stream.CountingOutputStream;
import com.braintribe.utils.stream.api.StreamPipe;
import com.braintribe.utils.stream.api.StreamPipeFactory;
import com.braintribe.web.servlet.BasicTemplateBasedServlet;

public class LogsDownloadServlet extends BasicTemplateBasedServlet implements InitializationAware {

	private static final long serialVersionUID = -1;
	private static Logger logger = Logger.getLogger(LogsDownloadServlet.class);

	private static final String logsTemplateLocation = "com/braintribe/web/servlet/logs/templates/tfLogDownload.html.vm";

	private static final String downloadSelectedPageName = "downloadSelected";

	private Evaluator<ServiceRequest> requestEvaluator;

	private Supplier<String> userSessionIdProvider;
	private StreamPipeFactory streamPipeFactory;

	@Override
	public void postConstruct() {
		// setRefreshFileBasedTemplates(true); // when debugging
		setTemplateLocation(logsTemplateLocation);
	}

	/**
	 * Main entry point to the servlet. The following path are acted upon:
	 * <ul>
	 * <li>/downloadPackage: Provides log files of a selected resources as a ZIP file.</li>
	 * <li>/: Presents the main page of the Logs Download Servlet</li>
	 * </ul>
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo != null) {
			if (pathInfo.equals(String.format("/%s", downloadSelectedPageName))) {
				try {
					downloadSelected(req, resp);
				} catch (Exception e) {
					throw new ServletException("Error while trying to download selected files.", e);
				}
			} else if (pathInfo.equals("/")) {
				String serviceLogsUrl = "../logs-download";
				resp.sendRedirect(serviceLogsUrl);
			} else {
				logger.debug("Unknown path: " + pathInfo);
				super.service(req, resp);
			}
		} else {
			super.service(req, resp);
		}
	}

	@Override
	protected VelocityContext createContext(HttpServletRequest req, HttpServletResponse response) {
		LogDownloadPageParams pageParams = LogDownloadPageParams.parse(req);

		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("filesByNode", listClusterLogFilesGroupedByNode());
		velocityContext.put("groupBy", pageParams.groupBy);
		velocityContext.put("fromDate", pageParams.from);
		velocityContext.put("toDate", pageParams.to);
		velocityContext.put("selectedFiles", pageParams.selectedFiles);

		return velocityContext;
	}

	private Map<String, List<FileInfo>> listClusterLogFilesGroupedByNode() {

		ListLogFiles listLogFiles = ListLogFiles.T.create();
		MulticastRequest mr = MulticastRequest.T.create();
		mr.setServiceRequest(listLogFiles);
		MulticastResponse multicastResponse = null;
		try {
			multicastResponse = mr.eval(requestEvaluator).get();
		} catch (Exception e) {
			logger.info(() -> "Error while trying to list the log files of all nodes", e);
			return null;
		}

		Map<String, List<FileInfo>> result = new TreeMap<>();

		Map<InstanceId, ServiceResult> responses = multicastResponse.getResponses();
		for (Map.Entry<InstanceId, ServiceResult> entry : responses.entrySet()) {
			InstanceId sender = entry.getKey();
			ServiceResult singleResult = entry.getValue();
			ResponseEnvelope asResponse = singleResult.asResponse();
			if (asResponse != null) {
				LogFilesList logFilesList = (LogFilesList) asResponse.getResult();
				List<FileInfo> fileInfos = logFilesList.getFileInfos();
				fileInfos.sort((a, b) -> {
					Date da = a.getLastModified();
					Date db = b.getLastModified();
					if (da == null && db == null)
						return 0;
					if (da == null)
						return 1;
					if (db == null)
						return -1;
					return db.compareTo(da); // newest first
				});
				result.put(sender.getNodeId(), fileInfos);
			} else {
				Failure asFailure = singleResult.asFailure();
				if (asFailure != null) {
					Throwable throwable = FailureCodec.INSTANCE.decode(asFailure);
					logger.debug(() -> "Received failure from " + sender, throwable);
				} else {
					logger.debug(() -> "Received neither result nor a failure from " + sender);
				}
			}
		}

		return result;
	}

	private void downloadSelected(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LogDownloadPageParams pageParams = LogDownloadPageParams.parse(req);

		if (pageParams.selectedFiles.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No files selected.");
			return;
		}

		// Group selected files by node
		Map<String, List<String>> filenamesByNode = new TreeMap<>();
		for (String entry : pageParams.selectedFiles) {
			int sep = entry.indexOf("::");
			if (sep > 0) {
				String node = entry.substring(0, sep);
				String filename = entry.substring(sep + 2);
				filenamesByNode.computeIfAbsent(node, k -> new ArrayList<>()).add(filename);
			}
		}

		logger.debug(() -> "Requesting the following log files from the cluster:\n" + formatFilenamesByNode(filenamesByNode));

		// Send one DownloadLogFiles request per node and collect responses
		String userSessionId = userSessionIdProvider.get();
		Map<String, Resource> resourcesByNode = new TreeMap<>();

		for (Map.Entry<String, List<String>> entry : filenamesByNode.entrySet()) {
			String nodeId = entry.getKey();
			List<String> filenames = entry.getValue();

			DownloadSelectedLogFiles dlSelectedRequest = DownloadSelectedLogFiles.T.create();
			dlSelectedRequest.setFilenames(filenames);

			InstanceId addressee = InstanceId.T.create();
			addressee.setNodeId(nodeId);

			MulticastRequest mr = MulticastRequest.T.create();
			mr.setServiceRequest(dlSelectedRequest);
			mr.setAddressee(addressee);
			mr.setTimeout((long) Numbers.MILLISECONDS_PER_MINUTE);
			mr.setSessionId(userSessionId);

			try {
				MulticastResponse multicastResponse = mr.eval(requestEvaluator).get();

				Map<InstanceId, ServiceResult> responses = multicastResponse.getResponses();
				if (responses.isEmpty()) 
					logger.warn("No response received from node: " + nodeId);
				 else if (responses.size() > 1) 
					logger.warn("Multiple responses received from node: " + nodeId);

				for (Map.Entry<InstanceId, ServiceResult> responseEntry : responses.entrySet()) {
					ServiceResult result = responseEntry.getValue();
					if (result instanceof ResponseEnvelope) {
						Logs logs = (Logs) ((ResponseEnvelope) result).getResult();
						Resource resource = logs.getLog();
						if (resource != null) {
							resourcesByNode.put(nodeId, resource);
						} else {
							logger.warn("Log file [" + logs.getFilename() + "] not received from node: " + nodeId);
						}
					} else if (result instanceof Failure) {
						Throwable throwable = FailureCodec.INSTANCE.decode(result.asFailure());
						logger.error("Received failure from node " + nodeId, throwable);
					}
				}
			} catch (Exception e) {
				logger.error("Error requesting log files from node " + nodeId, e);
			}
		}

		if (resourcesByNode.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not retrieve any log files.");
			return;
		}

		// Package all node responses into a single zip and stream to client
		String packageName = "logs-" + DateTools.encode(new Date(), DateTools.TERSE_DATETIME_FORMAT_2);

		try (StreamPipe pipe = streamPipeFactory.newPipe("download-selected")) {
			long totalSize;

			try (CountingOutputStream cout = new CountingOutputStream(pipe.acquireOutputStream()); ZipOutputStream zos = new ZipOutputStream(cout)) {

				for (Map.Entry<String, Resource> entry : resourcesByNode.entrySet()) {
					String nodeId = entry.getKey();
					Resource resource = entry.getValue();
					String entryPrefix = FileTools.normalizeFilename(nodeId, '_');

					if ("application/zip".equals(resource.getMimeType())) {
						// Unpack the inner zip and re-add entries with a node prefix
						try (ZipInputStream zis = new ZipInputStream(resource.openStream())) {
							ZipEntry innerEntry;
							while ((innerEntry = zis.getNextEntry()) != null) {
								String name = packageName + "/" + entryPrefix + "/" + FileTools.getName(innerEntry.getName());
								zos.putNextEntry(new ZipEntry(name));
								IOTools.pump(zis, zos);
								zos.closeEntry();
							}
						}
					} else {
						// Single file — add directly
						String name = packageName + "/" + entryPrefix + "/" + resource.getName();
						zos.putNextEntry(new ZipEntry(name));
						try (InputStream in = resource.openStream()) {
							IOTools.pump(in, zos, 0xffff);
						}
						zos.closeEntry();
					}
				}

				zos.close();
				cout.close();
				totalSize = cout.getCount();
			}

			resp.setContentType("application/zip");
			resp.setContentLength((int) totalSize);
			resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.zip\"", packageName));

			try (InputStream in = pipe.openInputStream()) {
				IOTools.pump(in, resp.getOutputStream(), 0xffff);
			}
		}
	}

	private String formatFilenamesByNode(Map<String, List<String>> filenamesByNode) {
		return filenamesByNode.entrySet().stream() //
				.map(e -> e.getKey() + ":\n  " + String.join("\n  ", e.getValue())) //
				.collect(Collectors.joining("\n"));
	}

	@Required
	public void setRequestEvaluator(Evaluator<ServiceRequest> requestEvaluator) {
		this.requestEvaluator = requestEvaluator;
	}

	@Required
	public void setCurrentUserSessionIdProvider(Supplier<String> userSessionIdProvider) {
		this.userSessionIdProvider = userSessionIdProvider;
	}
	@Required
	public void setStreamPipeFactory(StreamPipeFactory streamPipeFactory) {
		this.streamPipeFactory = streamPipeFactory;
	}
}
