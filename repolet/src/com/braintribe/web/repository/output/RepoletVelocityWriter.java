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
package com.braintribe.web.repository.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.braintribe.logging.Logger;

/**
 * Velocity based implementation of {@link RepoletWriter}
 * 
 */
public class RepoletVelocityWriter implements RepoletWriter<Writer> {

	private Template listTemplate;
	private Template notFoundTemplate;

	private static final String listTemplateLocation = "com/braintribe/web/repository/output/templates/list.html.vm";
	private static final String notFoundTemplateLocation = "com/braintribe/web/repository/output/templates/404.html.vm";

	private static final Logger log = Logger.getLogger(RepoletVelocityWriter.class);

	public RepoletVelocityWriter() {
		try {
			loadTemplates();
		} catch (Exception e) {
			log.error("failed to create " + this.getClass() + ": " + e.getMessage(), e);
		}
	}

	public void writeList(String path, Collection<BreadCrumb> breadCrumbs, Collection<String> entries, Writer writer, Map<String, Object> attributes)
			throws IOException {
		loadTemplates();
		VelocityContext context = new VelocityContext();
		context.put("path", path);
		context.put("breadCrumbs", breadCrumbs);
		context.put("entries", entries);
		addAttributesToContext(attributes, context);
		writeList(context, writer);
	}

	public void writeNotFound(String path, boolean printInspectedPaths, Collection<String> inspectedPaths, Writer writer,
			Map<String, Object> attributes) throws IOException {
		VelocityContext context = new VelocityContext();
		context.put("path", path);
		context.put("printInspectedPaths", printInspectedPaths);
		context.put("inspectedPaths", inspectedPaths);
		addAttributesToContext(attributes, context);
		writeNotFound(context, writer);
	}

	private void addAttributesToContext(Map<String, Object> attributes, VelocityContext context) {
		for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
			context.put(attribute.getKey(), attribute.getValue());
		}
	}

	private void writeList(Context context, Writer writer) throws IOException {
		templateMerge(listTemplate, context, writer);
	}

	private void writeNotFound(Context context, Writer writer) throws IOException {
		templateMerge(notFoundTemplate, context, writer);
	}

	private void templateMerge(Template template, Context context, Writer writer) throws IOException {
		try {
			template.merge(context, writer);
		} catch (Exception e) {
			throw new IOException("failed to write to template: " + e.getMessage(), e);
		}
	}

	private VelocityEngine createVelocityEngine() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
		ve.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
		return ve;
	}

	private void loadTemplates() throws IOException {
		VelocityEngine ve = createVelocityEngine();
		try {
			ve.init();
		} catch (Exception e) {
			throw new IOException("failed to initialize velocity engine: " + e.getMessage(), e);
		}
		listTemplate = loadTemplate(ve, listTemplateLocation);
		notFoundTemplate = loadTemplate(ve, notFoundTemplateLocation);
	}

	private Template loadTemplate(VelocityEngine ve, String templateLocation) throws IOException {

		Template template = null;
		try {
			template = ve.getTemplate(templateLocation);
		} catch (Exception e) {
			throw new IOException("failed to load template from [ " + templateLocation + " ]: " + e.getMessage(), e);
		}

		if (template == null)
			throw new IOException("null template loaded from [ " + templateLocation + " ] ");

		return template;
	}

}
