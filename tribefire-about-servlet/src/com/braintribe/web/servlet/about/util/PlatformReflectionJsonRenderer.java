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
package com.braintribe.web.servlet.about.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.model.platformreflection.PlatformReflection;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.web.servlet.TypedVelocityContext;
import com.braintribe.web.servlet.about.AboutServlet;

/**
 * Convenience tool to render a platform reflection JSON (maybe received from a customer?)
 * as HTML. based on the Velocity template used in the About page.
 * @author roman
 */
public class PlatformReflectionJsonRenderer {

	public static void main(String[] args) throws Exception {

		if (args == null || args.length < 2 || StringTools.isBlank(args[0]) || StringTools.isBlank(args[1])) {
			System.err.println("Please provide a path to the JSON file and a path to the HTML file to be written.");
			System.exit(1);
		}

		File jsonFile = new File(args[0]);
		if (!jsonFile.exists() || !jsonFile.isFile()) {
			System.err.println("The file "+jsonFile.getAbsolutePath()+" does not exist or is not a file.");
		}

		PlatformReflection pr = null;
		try (InputStream in = new BufferedInputStream(new FileInputStream(jsonFile))) {
			JsonStreamMarshaller marshaller = new JsonStreamMarshaller();
			pr = (PlatformReflection) marshaller.unmarshall(in);
		}
		Map<String,PlatformReflection> sysInfoMap = Collections.synchronizedMap(new TreeMap<>());
		sysInfoMap.put("JSON File", pr);
		
		VelocityEngine engine = com.braintribe.utils.velocity.VelocityTools.newResourceLoaderVelocityEngine(true);
		Template template = engine.getTemplate("com/braintribe/web/servlet/about/templates/tfSystemInformation.html.vm");
		
		TypedVelocityContext context = new TypedVelocityContext();
		context.setType(AboutServlet.TYPE_SYSINFO);
		context.put("sysInfoMap", sysInfoMap);
		context.put("current_year", new GregorianCalendar().get(Calendar.YEAR));
		context.put("tribefireRuntime", TribefireRuntime.class);


		File outputFile = new File(args[1]);
		File outputDir = outputFile.getParentFile();
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
			template.merge(context, writer);
		}

	}
}
