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
package com.braintribe.web.api.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.braintribe.exception.Exceptions;
import com.braintribe.logging.Logger;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.StringTools;

/**
 * Utility methods that are mainly used by {@link com.braintribe.web.api.ApplicationLoader} and {@link com.braintribe.web.api.listener.ForcedLibs}.
 */
public class AppTools {

	private static final Logger logger = Logger.getLogger(AppTools.class);

	protected static String loadUrlContent(URL url) throws IOException {
		try (InputStream is = new BufferedInputStream(url.openStream())) {
			String content = IOTools.slurp(is, "UTF-8").trim();
			return content;
		}
	}

	public static List<String> getContentLines(URL url) throws IOException {
		List<String> result = new ArrayList<String>();
		String content = loadUrlContent(url);
		if (content != null && content.length() > 0) {
			content.lines().forEach(line -> {
				line = line.trim();
				if (line.length() > 0 && !line.startsWith("#")) {
					result.add(line);
				}
			});
		}
		return result;
	}

	public static List<URL> parseIndexFromURL(URL url) throws IOException {
		List<URL> result = new ArrayList<URL>();
		List<String> lines = getContentLines(url);
		for (String line : lines) {
			if (line.toLowerCase().endsWith(".jar")) {
				try {
					URL jarUrl = new URI(line).toURL();
					logger.debug("Adding JAR resource " + jarUrl);
					result.add(jarUrl);
				} catch (MalformedURLException | URISyntaxException e) {
					throw Exceptions.unchecked(e, "Not a valid URL " + line);
				}
			} else {
				logger.debug("Ignoring non-JAR file " + line);
			}
		}
		return result;
	}

	public static void deployJarFilesToClasses(ServletContext servletContext, List<URL> jarUrls) {
		String realPath = servletContext.getRealPath("/WEB-INF");
		if (logger.isDebugEnabled())
			logger.debug("deployJarFilesToClasses: Using real path " + realPath);
		if (realPath == null) {
			return;
		}

		File realPathFile = new File(realPath);
		File classesDir = new File(realPathFile, "classes");
		if (!classesDir.exists()) {
			classesDir.mkdir();
		} else {
			cleanUp(jarUrls, classesDir);
		}
		unjarJarFiles(jarUrls, classesDir);
	}

	protected static void unjarJarFiles(List<URL> jarUrls, File classesDir) {
		boolean debug = logger.isDebugEnabled();

		if (debug) {
			logger.debug("Extracting JARs " + jarUrls + " to " + classesDir.getAbsolutePath());
		}

		// Note: In most cases, the platform will be started for once in a Docker container
		// Hence, most likely the classes have not yet been extracted at this point.
		// So, it makes sense to create a thread pool in any case.
		int count = 0;
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (URL url : jarUrls) {
				String indexName = getIndexName(url);
				File indexFile = new File(classesDir, indexName);
				if (!indexFile.exists()) {
					if (debug)
						logger.debug("Extracting JAR " + url);

					executor.submit(() -> unjar(url, classesDir));
					count++;

					if (debug)
						logger.debug("Done extracting JAR " + url);
				} else {
					if (debug)
						logger.debug("Index file " + indexFile.getAbsolutePath() + " already exists. Not extracting " + url);
				}
			}
		}
		if (debug) {
			logger.debug("Extracted " + count + " JAR files into " + classesDir.getAbsolutePath());
		}
	}

	protected static String getIndexName(URL url) {
		if (url == null) {
			return null;
		}
		String jarName = FileTools.getName(url.toString());
		String rawName = FileTools.getNameWithoutExtension(jarName);
		String indexName = rawName + ".index";
		return indexName;
	}

	protected static void cleanUp(List<URL> activatedJarUrls, File classesDir) {

		boolean debug = logger.isDebugEnabled();

		final List<String> activatedIndexFiles = new ArrayList<String>();
		for (URL url : activatedJarUrls) {
			String indexName = getIndexName(url);
			activatedIndexFiles.add(indexName);
		}

		if (debug)
			logger.debug("These are the activated forced libs: " + activatedIndexFiles);

		File[] surplusIndexFiles = classesDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".index")) {
					if (activatedIndexFiles.contains(name)) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			}
		});
		if (surplusIndexFiles != null && surplusIndexFiles.length > 0) {
			if (debug)
				logger.debug("Cleaning up: " + StringTools.<File> join(",", surplusIndexFiles));
			for (File surplusIndexFile : surplusIndexFiles) {
				try {
					if (debug)
						logger.debug("Cleaning up: " + surplusIndexFile);
					List<String> lines = getContentLines(surplusIndexFile.toURI().toURL());
					for (String line : lines) {
						File file = new File(classesDir, line);
						if (file.exists() && file.isFile()) {
							file.delete();
						}
					}
					surplusIndexFile.delete();
				} catch (IOException e) {
					logger.error("Could not process file " + surplusIndexFile.getAbsolutePath(), e);
				}
			}
			if (debug)
				logger.debug("Cleaning up empty folders.");
			FileTools.cleanEmptyFolders(classesDir, false);
			if (debug)
				logger.debug("Done cleaning up empty folders.");
		} else {
			if (debug)
				logger.debug("No JAR had to be cleaned up.");
		}
	}

	protected static void unjar(URL jarUrl, File targetDir) {
		try (JarInputStream zin = new JarInputStream(jarUrl.openStream())) {
			List<String> entries = new ArrayList<>();
			ZipEntry zipEntry = null;

			while ((zipEntry = zin.getNextEntry()) != null) {
				String slashedPathName = zipEntry.getName();
				entries.add(slashedPathName);

				File targetFile = new File(targetDir, slashedPathName);

				if (zipEntry.isDirectory()) {
					// create directory because it maybe empty and it would be an information loss otherwise
					targetFile.mkdirs();
				} else {
					targetFile.getParentFile().mkdirs();

					try (OutputStream out = new FileOutputStream(targetFile)) {
						IOTools.transferBytes(zin, out, IOTools.BUFFER_SUPPLIER_8K);
					}
				}

				zin.closeEntry();
			}

			String jarName = FileTools.getName(jarUrl.toString());
			String rawName = FileTools.getNameWithoutExtension(jarName);
			String indexName = rawName + ".index";
			File indexFile = new File(targetDir, indexName);
			StringBuilder sb = new StringBuilder();
			for (String entry : entries) {
				sb.append(entry);
				sb.append('\n');
			}
			IOTools.spit(indexFile, sb.toString(), "UTF-8", false);

		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Error while unpacking jar: " + jarUrl + " to " + targetDir.getAbsolutePath());
		}
	}

	public static String getContextPath(ServletContextEvent sce) {
		String contextPath = "unknown";
		if (sce != null) {
			ServletContext ctx = sce.getServletContext();
			if (ctx != null) {
				contextPath = ctx.getContextPath();
			} else {
				contextPath = sce.toString();
			}
		}
		return contextPath;
	}
}
