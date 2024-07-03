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
package tribefire.cortex.check.processing;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

import com.braintribe.utils.lcd.LazyInitialized;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import tribefire.cortex.model.check.CheckWeight;

public class CheckBundleTest {

	public static final MutableDataHolder FLEXMARK_OPTIONS = new MutableDataSet() //
			.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
	
	private LazyInitialized<Parser> markdownParser = new LazyInitialized<>(() -> Parser.builder(FLEXMARK_OPTIONS).build());
	private LazyInitialized<HtmlRenderer> htmlRenderer = new LazyInitialized<>(() -> HtmlRenderer.builder(FLEXMARK_OPTIONS).build());
	
	@Test
	public void testWeightProcessing() {
		
		CheckWeight under1s = CheckWeight.under1s;
		
		CheckWeight under100ms = CheckWeight.under100ms;
		
		assertTrue(under100ms.ordinal() <= under1s.ordinal());
		
	}
	
	@Test
	public void parseMarkdownTable() {

		String input = "Instance | Type | Id | Thread | Duration\r\n" + 
				"--- | --- | --- | --- | ---\r\n" + 
				"```master@tf@Romans-MacBook-Pro.local#200316140759297a0eb7e67d3340af9d``` | SplitPdfJob | 200316141446208904a54e0c2a4d07ad | ConversionJob-32-thread-4 | 31 s 242 ms\r\n" + 
				"`master@tf@Romans-MacBook-Pro.local#200316140759297a0eb7e67d3340af9d` | CreateDocumentJob | 200316141432802abaf63e8ccc4969a3 | ConversionJob-32-thread-4 | 44 s 589 ms";
		Parser parser = markdownParser.get();
		Document document = parser.parse(input);
		
		HtmlRenderer renderer = htmlRenderer.get();
		
		StringWriter writer = new StringWriter();
		renderer.render(document, writer);
		
		String s = writer.toString();
		System.out.println(s);
	}
	
	@Test
	public void parseMarkdownMixed() {
		
		String input = 
				"```\n" +
				"@Managed\r\n" + 
				"private DemoVitalityChecker demoHealthChecker() {\r\n" + 
				"	DemoVitalityChecker bean = create(DemoVitalityChecker.T);\r\n" + 
				"	\r\n" + 
				"	bean.setModule(existingInstances.demoChecksModule());\r\n" + 
				"	bean.setExternalId(\"serviceProcessor.demoHealthChecker\");\r\n" + 
				"	bean.setName(\"Demo Health Checker\");\r\n" + 
				"	\r\n" + 
				"	return bean;\r\n" + 
				"}\n"
				+ "```";
		Parser parser = markdownParser.get();
		Document document = parser.parse(input);
		
		HtmlRenderer renderer = htmlRenderer.get();
		
		StringWriter writer = new StringWriter();
		renderer.render(document, writer);
		
		String s = writer.toString();
		System.out.println(s);
	}

}
