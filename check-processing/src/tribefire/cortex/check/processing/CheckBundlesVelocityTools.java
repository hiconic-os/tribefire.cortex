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

import java.io.StringWriter;
import java.util.Arrays;

import com.braintribe.model.deploymentapi.check.data.CheckBundlesResponse;
import com.braintribe.model.deploymentapi.check.data.aggr.CbrAggregatable;
import com.braintribe.model.deploymentapi.check.data.aggr.CbrAggregation;
import com.braintribe.model.time.TimeSpan;
import com.braintribe.model.time.TimeUnit;
import com.braintribe.utils.lcd.LazyInitialized;
import com.braintribe.utils.xml.XmlTools;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import tribefire.cortex.model.check.CheckWeight;

/**
 * Tooling functions which are called from the check bundle velocity template.
 * 
 * @author christina.wilpernig
 */
public class CheckBundlesVelocityTools {

	public static final MutableDataHolder FLEXMARK_OPTIONS = new MutableDataSet() //
			.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
	
	private LazyInitialized<Parser> markdownParser = new LazyInitialized<>(() -> Parser.builder(FLEXMARK_OPTIONS).build());
	private LazyInitialized<HtmlRenderer> htmlRenderer = new LazyInitialized<>(() -> HtmlRenderer.builder(FLEXMARK_OPTIONS).build());

	public String getIdPrefix(CbrAggregation aggregation) {
		switch(aggregation.getKind()) {
		case bundle:
			return "Bundle";
		case coverage:
			return "Coverage";
		case deployable:
			return "Deployable";
		case label:
			return "Label";
		case module:
			return "Module";
		case node:
			return "Node";
		case processor:
			return "Processor";
		case role:
			return "Role";
		case status:
			return "Status";
		case weight:
			return "Weight";
		case effectiveWeight:
			return "Effective Weight";
		default:
			throw new IllegalStateException("Unknown CheckAggregationKind: " + aggregation.getKind());
		}
	}
	
	public String getPrettyWeight(CheckWeight weight) {
		switch(weight) {
			case under100ms:
				return "< 100 ms";
			case under10h:
				return "under 10 h";
			case under10m:
				return "< 10 minutes";
			case under10s:
				return "<10 seconds";
			case under1d:
				return "<1 day";
			case under1h:
				return "<1 hour";
			case under1m:
				return "<1 minute";
			case under1s:
				return "<1 second";
			case under1w:
				return "<1 week";
			case unlimited:
				return "unlimited";
			default:
				throw new IllegalStateException("Unknown CheckWeight: " + weight.toString());
		}
	}
	
	public boolean isResponse(Object object) {
		return (object instanceof CheckBundlesResponse);
	}
	
	public String getDetailsPreview(String details) {
		return details.length() < 50 ? details : details.substring(0, 50) + "...";
	}
	
	public String getIdentification(CbrAggregatable a) {
		return CheckBundlesUtils.getIdentification(a);
	}
	
	public String getPrettyElapsedTime(double elapsedTime) {
		TimeSpan ts = TimeSpan.create(elapsedTime, TimeUnit.milliSecond);
		TimeUnit floor = ts.floorUnit();
		
		if (floor == TimeUnit.milliSecond) {
			ts.setValue(Math.round(elapsedTime));
		}
		
		return ts.formatWithFloorUnitAndSubUnit();
	}
	
	public String parseMarkdown(String markdown) {
		Parser parser = markdownParser.get();
		Document document = parser.parse(markdown);
		
		HtmlRenderer renderer = htmlRenderer.get();
		
		StringWriter writer = new StringWriter();
		renderer.render(document, writer);
		
		return writer.toString();
	}
	
	public String escape(String text) {
		return XmlTools.escape(text);
	}
}
