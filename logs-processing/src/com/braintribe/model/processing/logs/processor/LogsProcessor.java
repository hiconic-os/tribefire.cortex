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
package com.braintribe.model.processing.logs.processor;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang.StringEscapeUtils;

import com.braintribe.cfg.Required;
import com.braintribe.common.lcd.Numbers;
import com.braintribe.logging.Logger;
import com.braintribe.model.logging.LogLevel;
import com.braintribe.model.logs.request.DownloadSelectedLogFiles;
import com.braintribe.model.logs.request.FileInfo;
import com.braintribe.model.logs.request.GetLogContent;
import com.braintribe.model.logs.request.GetLogFiles;
import com.braintribe.model.logs.request.GetLogLevel;
import com.braintribe.model.logs.request.GetLogLevelResponse;
import com.braintribe.model.logs.request.GetLogs;
import com.braintribe.model.logs.request.ListLogFiles;
import com.braintribe.model.logs.request.LogContent;
import com.braintribe.model.logs.request.LogFileBundle;
import com.braintribe.model.logs.request.LogFiles;
import com.braintribe.model.logs.request.LogFilesList;
import com.braintribe.model.logs.request.Logs;
import com.braintribe.model.logs.request.LogsRequest;
import com.braintribe.model.logs.request.LogsResponse;
import com.braintribe.model.logs.request.SetLogLevel;
import com.braintribe.model.logs.request.SetLogLevelResponse;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.bootstrapping.jmx.TribefireRuntimeMBean;
import com.braintribe.model.processing.bootstrapping.jmx.TribefireRuntimeMBeanTools;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.weaving.impl.dispatch.DispatchingServiceProcessor;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.utils.Base64;
import com.braintribe.utils.DateTools;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.OsTools;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.collection.api.MultiMap;
import com.braintribe.utils.collection.impl.ComparatorBasedNavigableMultiMap;

public class LogsProcessor extends DispatchingServiceProcessor<LogsRequest, LogsResponse> {

	private static Logger logger = Logger.getLogger(LogsProcessor.class);

	protected static final String tribefireRuntimeMBeanPrefix = "com.braintribe.tribefire:type=TribefireRuntime,name=";

	public static final String LOG_LOCATION = "com.braintribe.logging.juli.handlers.FileHandler.directory";

	// protected File logFolder;
	private Supplier<String> userNameProvider;
	private InstanceId localInstanceId;

	private static final int newLineLength = "\r\n".length();

	private final ReentrantLock logFileCacheLock = new ReentrantLock();
	private Map<String, File> knownLogFiles = new HashMap<>();
	private MultiMap<String, File> knownLogFilesPerKey = null;
	private long logFileCacheLastRefresh = -1L;

	// @formatter:off
	@Required public void setUserNameProvider(Supplier<String> userNameProvider) { this.userNameProvider = userNameProvider; }
	@Required public void setLocalInstanceId(InstanceId localInstanceId) { this.localInstanceId = localInstanceId; }
	// @formatter:on

	@SuppressWarnings("unused")
	public GetLogLevelResponse getLogLevel(ServiceRequestContext context, GetLogLevel request) throws Exception {

		String applicationId = localInstanceId.getApplicationId();

		TribefireRuntimeMBean runtime = TribefireRuntimeMBeanTools.getTribefireCartridgeRuntime(applicationId);
		String logLevel = runtime.getProperty(TribefireRuntime.ENVIRONMENT_LOG_LEVEL);

		GetLogLevelResponse response = GetLogLevelResponse.T.create();
		if (!StringTools.isBlank(logLevel)) {
			response.setLogLevel(LogLevel.valueOf(logLevel));
		}
		response.setNodeId(localInstanceId.getNodeId());
		return response;
	}

	@SuppressWarnings("unused")
	public SetLogLevelResponse setLogLevel(ServiceRequestContext context, SetLogLevel request) throws Exception {

		String paramLogLevel = request.getLogLevel();

		String cartridgeName = localInstanceId.getApplicationId();
		final LogLevel logLevel;

		try {
			logLevel = LogLevel.valueOf(paramLogLevel);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Could not interpret the log level value: " + paramLogLevel, e);
		}

		// Change log level for cartridge
		TribefireRuntimeMBean tribefireCartrigdeRuntime = TribefireRuntimeMBeanTools.getTribefireCartridgeRuntime(cartridgeName);
		if (tribefireCartrigdeRuntime != null) {
			logger.debug(() -> "Setting loglevel " + logLevel + " on " + cartridgeName);

			tribefireCartrigdeRuntime.setProperty(TribefireRuntime.ENVIRONMENT_LOG_LEVEL, logLevel.name());
		} else {
			logger.info(() -> "Could not find TribefireRuntimeMBean for " + cartridgeName);
		}

		SetLogLevelResponse response = SetLogLevelResponse.T.create();
		return response;

	}

	protected Set<String> getCartridgeNames() {
		Set<String> cartridgeNames = new HashSet<String>();

		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName mBeanQueryName = new ObjectName(String.format("%s*", tribefireRuntimeMBeanPrefix));

			for (ObjectName mBeanName : mbs.queryNames(mBeanQueryName, null)) {
				if (mbs.isInstanceOf(mBeanName, TribefireRuntimeMBean.class.getName())) {
					String name = mBeanName.getKeyProperty("name");
					if (!StringTools.isBlank(name)) {
						cartridgeNames.add(name);
					}
				}
			}
		} catch (MalformedObjectNameException | InstanceNotFoundException e) {
			logger.error(String.format("Invalid TribefireRuntime MBean-Prefix: %s", tribefireRuntimeMBeanPrefix), e);
		}

		if (cartridgeNames.contains("master")) {
			cartridgeNames.remove("tribefire-services");
		}

		return cartridgeNames;
	}

	@SuppressWarnings("unused")
	public Logs getLog(ServiceRequestContext context, GetLogs request) throws Exception {

		loadLogFiles();
		Map<String, File> knownLogFilesRef = knownLogFiles;

		Logs logs = Logs.T.create();

		String fileName = request.getFilename();
		if (fileName == null || fileName.trim().length() == 0) {
			fileName = "*";
		}

		StringTokenizer tokenizer = new StringTokenizer(fileName, ".*", true);
		StringBuilder builder = new StringBuilder();
		boolean foundJoker = false;

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.equals("*")) {
				foundJoker = true;
				builder.append(".*");
			} else if (token.equals(".") == false) {
				String quoted = Pattern.quote(token);
				builder.append(quoted);
			}
		}

		if (foundJoker == false) {
			if (fileName.contains("/") || fileName.contains("\\")) {
				throw new Exception("no paths allowed");
			} else {
				File file = knownLogFilesRef.get(fileName);
				if (file != null && file.exists()) {
					this.setFile(logs, file, "text/plain", request.getBase64EncodedResponse());
				} else {
					throw new FileNotFoundException("Could not find file " + fileName);
				}
			}
		} else {
			Date from = request.getFromDate();
			Date to = request.getToDate();

			Collection<File> logFiles = filterFiles(from, to, builder.toString());
			int logFilesCount = logFiles.size();
			int top = request.getTop();
			logFilesCount = Math.min(logFilesCount, top);
			if (logFilesCount <= 0) {
				logFilesCount = Integer.MAX_VALUE;
			}

			String dateStr = DateTools.encode(new Date(), DateTools.TERSE_DATETIME_FORMAT_2);

			String downloadFilenamePrefix = fileName;
			if (downloadFilenamePrefix.endsWith(".*")) {
				downloadFilenamePrefix = downloadFilenamePrefix.substring(0, downloadFilenamePrefix.length() - 2);
			}
			if (downloadFilenamePrefix.equals("*")) {
				downloadFilenamePrefix = "all";
			}
			downloadFilenamePrefix = FileTools.replaceIllegalCharactersInFileName(downloadFilenamePrefix, "");

			String name = String.format("%s-logs-%s.zip", downloadFilenamePrefix, dateStr);

			this.setZippedFile(logs, name, logFiles, logFilesCount, request.getBase64EncodedResponse());
		}

		return logs;
	}

	private Collection<File> filterFiles(Date from, Date to, String filenamePattern) {
		Pattern pattern = !StringTools.isBlank(filenamePattern) ? Pattern.compile(filenamePattern) : null;
		List<File> result = new ArrayList<>();
		MultiMap<String, File> knownLogFilesPerKeyRef = knownLogFilesPerKey;

		for (String key : knownLogFilesPerKeyRef.keySet()) {
			boolean acceptKey = true;
			if (pattern != null) {
				Matcher matcher = pattern.matcher(key);
				acceptKey = matcher.matches();
			}
			if (acceptKey) {
				Collection<File> filesPerKey = knownLogFilesPerKeyRef.getAll(key);
				for (File file : filesPerKey) {

					if (!file.exists()) {
						continue;
					}

					boolean acceptDate = true;
					if (from != null || to != null) {
						Date fileDate = new Date(file.lastModified());
						if (from != null && from.compareTo(fileDate) > 0) {
							acceptDate = false;
						}
						if (to != null && to.compareTo(fileDate) < 0) {
							acceptDate = false;
						}
					}

					if (acceptDate) {
						result.add(file);
					}
				}

			}
		}
		return result;
	}

	private void setFile(Logs logs, File file, String mimeType, boolean base64EncodedResponse) {
		if (base64EncodedResponse) {
			try (InputStream fis = new BufferedInputStream(new FileInputStream(file));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Base64.OutputStream out = new Base64.OutputStream(baos)) {
				IOTools.transferBytes(fis, out);
				out.close();
				baos.close();

				logs.setFilename(file.getName());
				logs.setMimeType(mimeType);
				logs.setBase64EncodedResource(baos.toString(StandardCharsets.UTF_8));
			} catch (IOException ioe) {
				throw new UncheckedIOException("Could not package log file: " + ioe.getMessage(), ioe);
			}
		} else {

			Resource resource = Resource.createTransient(() -> new FileInputStream(file));
			resource.setName(file.getName());
			resource.setMimeType(mimeType);
			resource.setFileSize(file.length());

			try {
				BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				if (attrs != null) {
					FileTime creationTime = attrs.creationTime();
					if (creationTime != null) {
						GregorianCalendar atime = new GregorianCalendar();
						atime.setTimeInMillis(creationTime.toMillis());
						resource.setCreated(atime.getTime());
					}
				}
			} catch (IOException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("Could not get the creation time of file " + file.getAbsolutePath(), e);
				}
			}

			if (OsTools.isUnixSystem()) {
				try {
					PosixFileAttributes attrs = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
					if (attrs != null) {
						UserPrincipal owner = attrs.owner();
						if (owner != null) {
							resource.setCreator(owner.getName());
						}
					}
				} catch (UnsupportedOperationException e) {
					// ignore
					
				} catch (Exception e) {
					logger.debug(() -> "Could not get the owner of file " + file.getAbsolutePath(), e);
				}
			}

			logs.setLog(resource);
			logs.setFilename(file.getName());
		}
	}

	private void setZippedFile(Logs logs, String name, Collection<File> logFiles, int logFilesCount, boolean base64EncodedResponse) {
		if (base64EncodedResponse) {
			try (InputStream fis = new ZippingInputStreamProvider(name, logFiles, logFilesCount).openInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Base64.OutputStream out = new Base64.OutputStream(baos)) {
				IOTools.transferBytes(fis, out);
				out.close();
				baos.close();

				logs.setFilename(name);
				logs.setMimeType("application/zip");
				logs.setBase64EncodedResource(baos.toString(StandardCharsets.UTF_8));
			} catch (IOException ioe) {
				throw new UncheckedIOException("Could not package log file: " + ioe.getMessage(), ioe);
			}

		} else {
			Resource resource = Resource.createTransient(new ZippingInputStreamProvider(name, logFiles, logFilesCount));
			resource.setName(name);
			resource.setMimeType("application/zip");
			resource.setCreated(new Date());
			try {
				resource.setCreator(this.userNameProvider.get());
			} catch (RuntimeException e) {
				logger.debug("Could not get the current user name.", e);
			}

			logs.setLog(resource);
			logs.setFilename(name);
		}
	}

	@SuppressWarnings("unused")
	public LogFiles getLogFiles(ServiceRequestContext context, GetLogFiles request) throws Exception {

		loadLogFiles();
		MultiMap<String, File> knownLogFilesPerKeyRef = knownLogFilesPerKey;

		Date from = request.getFrom();
		Date to = request.getTo();

		LogFiles logFiles = LogFiles.T.create();

		for (String key : knownLogFilesPerKeyRef.keySet()) {
			LogFileBundle bundle = LogFileBundle.T.create();
			bundle.setBundleName(key);

			for (File f : knownLogFilesPerKeyRef.getAll(key)) {

				boolean acceptDate = true;
				if (from != null || to != null) {
					Date fileDate = new Date(f.lastModified());
					if (from != null && from.compareTo(fileDate) > 0) {
						acceptDate = false;
					}
					if (to != null && to.compareTo(fileDate) < 0) {
						acceptDate = false;
					}
				}
				if (acceptDate) {
					bundle.getFileNames().add(f.getName());
				}
			}
			logFiles.getLogBundles().add(bundle);
		}
		return logFiles;
	}

	@SuppressWarnings("unused")
	public LogContent getLogContent(ServiceRequestContext context, GetLogContent request) throws Exception {
		loadLogFiles();
		Map<String, File> knownLogFilesRef = knownLogFiles;

		String logFileName = request.getLogFile();
		long logMark = request.getMark();
		int logLines = request.getLines();

		LogContent logContentResult = LogContent.T.create();

		List<String> logContent = logContentResult.getContent();

		File logFile = knownLogFilesRef.get(logFileName);
		if (logFile != null && logFile.exists()) {

			BasicFileAttributes attr = Files.readAttributes(logFile.toPath(), BasicFileAttributes.class);

			logContentResult.setCreationDate(new Date(attr.creationTime().toMillis()));

			// Read result variables
			try {

				if (logMark < 0) {
					// Mark end of file
					logMark = logFile.length();

					try (ReversedLinesFileReader logFileReader = ReversedLinesFileReader.builder().setFile(logFile).setBufferSize(1024).setCharset("UTF-8").get()) {
						String logLine = null;
						int readLogLines = 0;

						// Read logLine until start is reached or logLines are reached
						while ((logLine = logFileReader.readLine()) != null && readLogLines < logLines) {
							// Add logLine to output
							logContent.add(0, StringEscapeUtils.escapeHtml(logLine));
							readLogLines++;
						}
					}
				} else {
					// Read log file from top starting at mark to bottom
					try (BufferedReader logFileReader = new BufferedReader(
							new InputStreamReader(new FileInputStream(logFile), Charset.forName("UTF-8")))) {
						logFileReader.skip(logMark);
						String logLine = null;
						int readLogLines = 0;

						// Read logLine until end is reached or logLines are reached
						while ((logLine = logFileReader.readLine()) != null && readLogLines < logLines) {
							// Add logLine to output
							logContent.add(StringEscapeUtils.escapeHtml(logLine));
							readLogLines++;

							// Add logLine to logMark
							logMark += logLine.length() + newLineLength;
						}
					}
				}

				logContentResult.setMark(logMark);

			} catch (Exception e) {
				logger.debug(() -> "Error while trying to read content of log file: " + logFileName, e);
			}
		}

		return logContentResult;
	}

	@SuppressWarnings("unused")
	public LogFilesList listLogFiles(ServiceRequestContext context, ListLogFiles request) throws Exception {
		loadLogFiles();
		MultiMap<String, File> knownLogFilesPerKeyRef = knownLogFilesPerKey;

		Date from = request.getFrom();
		Date to = request.getTo();

		LogFilesList logFilesList = LogFilesList.T.create();
		List<FileInfo> fileInfos = logFilesList.getFileInfos();

		for (File f : knownLogFilesPerKeyRef.values()) {

			boolean acceptDate = true;
			if (from != null || to != null) {
				Date fileDate = new Date(f.lastModified());
				if (from != null && from.compareTo(fileDate) > 0) {
					acceptDate = false;
				}
				if (to != null && to.compareTo(fileDate) < 0) {
					acceptDate = false;
				}
			}
			if (acceptDate) {
				FileInfo fileInfo = FileInfo.T.create();
				fileInfo.setName(f.getName());
				fileInfo.setSizeInBytes(f.length());
				fileInfo.setLastModified(new Date(f.lastModified()));
				fileInfos.add(fileInfo);
			}
		}

		return logFilesList;
	}

	@SuppressWarnings("unused")
	public Logs downloadSelectedLogFiles(ServiceRequestContext context, DownloadSelectedLogFiles request) throws Exception {
		loadLogFiles();
		Map<String, File> knownLogFilesRef = knownLogFiles;

		List<String> requestedFilenames = request.getFilenames();
		if (requestedFilenames == null || requestedFilenames.isEmpty())
			throw new IllegalArgumentException("No filenames specified for download.");

		List<File> filesToPackage = new ArrayList<>();
		List<String> notFounds = newList();
		for (String filename : requestedFilenames) {
			if (filename.contains("/") || filename.contains("\\")) {
				throw new IllegalArgumentException("No paths allowed in filename: " + filename);
			}
			File file = knownLogFilesRef.get(filename);
			if (file != null && file.exists())
				filesToPackage.add(file);
			else
				notFounds.add(filename);
		}

		logger.debug("Downloading log files\n\tFound: " + filesToPackage + "\n\t: Not found: " + notFounds);

		Logs logs = Logs.T.create();

		if (!filesToPackage.isEmpty()) {
			String dateStr = DateTools.encode(new Date(), DateTools.TERSE_DATETIME_FORMAT_2);
			String name = String.format("logs-%s.zip", dateStr);
			setZippedFile(logs, name, filesToPackage, filesToPackage.size(), false);
		}

		return logs;
	}

	
	private void loadLogFiles() {
		long now = System.currentTimeMillis();
		if ((now - logFileCacheLastRefresh) < Numbers.MILLISECONDS_PER_SECOND * 10) {
			return;
		}
		logFileCacheLastRefresh = now;
		logFileCacheLock.lock();
		try {
			MultiMap<String, File> logFilesPerKey = new ComparatorBasedNavigableMultiMap<String, File>(Comparator.naturalOrder(), new FileComparator());
			Map<String, File> logFiles = new HashMap<>();

			try {
				Pattern keyPattern = Pattern.compile("^[A-Za-z]+([-_][A-Za-z]+)*");

				Set<File> logDirs = findLogDirs();
				for (File dir : logDirs) {
					for (File file : dir.listFiles(f -> f.isFile())) {
						String key = file.getName();
						if (key.equals(".DS_Store")) {
							// We're on a Mac
							return;
						}
						Matcher keyMatcher = keyPattern.matcher(key);
						if (keyMatcher.find()) {
							key = keyMatcher.group();
						}
						logFilesPerKey.put(key, file);
						logFiles.put(file.getName(), file);
					}
				}

			} catch (Exception e) {
				logger.error("Error while reading logging configuration files. " + e.getMessage(), e);
			}

			knownLogFilesPerKey = logFilesPerKey;
			knownLogFiles = logFiles;

		} finally {
			logFileCacheLock.unlock();
		}
	}

	private Set<File> findLogDirs() {
		try {
			String catalinaBase = TribefireRuntime.getContainerRoot().replaceAll("\\\\", "/");
			String logConfDir = catalinaBase + "/conf";
			Set<File> logDirs = new HashSet<>();
			File logConfFolder = new File(logConfDir);
			for (File logConf : logConfFolder.listFiles(f -> f.getName().contains("logging.properties"))) {
				Properties logProps = new Properties();
				try (InputStream in = new BufferedInputStream(new FileInputStream(logConf))) {
					logProps.load(in);
				}
				String logLocationPath = logProps.getProperty(LOG_LOCATION);
				logLocationPath = logLocationPath.replaceAll("\\$\\{catalina.base\\}", catalinaBase);
				logDirs.add(new File(logLocationPath));
			}
			return logDirs;

		} catch (Exception e) {
			logger.error("Error while reading logging configuration files. " + e.getMessage(), e);
			return Collections.emptySet();
		}
	}}
