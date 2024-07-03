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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.braintribe.model.deploymentapi.check.data.CheckBundlesResponse;

import tribefire.cortex.model.check.CheckWeight;

public class CheckBundlesMarshallerTest {

	@Test
	public void testNoResults() throws Exception {
		CheckBundlesResponseHtmlMarshaller marshaller = new CheckBundlesResponseHtmlMarshaller();
		
		CheckBundlesResponse noResults = CbmResultUtils.noResultsOk();
		
		File f = ensureNew("./res/no-results.html");

		FileOutputStream fos = new FileOutputStream(f);
		marshaller.marshall(fos, noResults);
	}
	
	@Test
	public void testFlatView() throws Exception {
		CheckBundlesResponseHtmlMarshaller marshaller = new CheckBundlesResponseHtmlMarshaller();
		
		CheckBundlesResponse flatResults = CbmResultUtils.flatResultsOk();
		
		File f = ensureNew("./res/flat-results.html");

		FileOutputStream fos = new FileOutputStream(f);
		marshaller.marshall(fos, flatResults);
	}
	
	@Test
	public void oneLevelAggregationView() throws Exception {
		CheckBundlesResponseHtmlMarshaller marshaller = new CheckBundlesResponseHtmlMarshaller();
		
		CheckBundlesResponse flatResults = CbmResultUtils.oneLevelAggregationOk();
		
		File f = ensureNew("./res/one-level-aggr.html");
		
		FileOutputStream fos = new FileOutputStream(f);
		marshaller.marshall(fos, flatResults);
	}
	
	@Test
	public void twoLevelAggregationView() throws Exception {
		CheckBundlesResponseHtmlMarshaller marshaller = new CheckBundlesResponseHtmlMarshaller();
		
		CheckBundlesResponse flatResults = CbmResultUtils.twoLevelAggregationOk();
		
		File f = ensureNew("./res/two-level-aggr.html");
		
		FileOutputStream fos = new FileOutputStream(f);
		marshaller.marshall(fos, flatResults);
	}
	
	@Test
	public void threeLevelAggregationView() throws Exception {
		CheckBundlesResponseHtmlMarshaller marshaller = new CheckBundlesResponseHtmlMarshaller();
		
		CheckBundlesResponse flatResults = CbmResultUtils.threeLevelAggregationOk();
		
		File f = ensureNew("./res/three-level-aggr.html");
		
		FileOutputStream fos = new FileOutputStream(f);
		marshaller.marshall(fos, flatResults);
	}
	
	@Test
	public void fourLevelAggregationTwoNodesView() throws Exception {
		CheckBundlesResponseHtmlMarshaller marshaller = new CheckBundlesResponseHtmlMarshaller();
		
		CheckBundlesResponse flatResults = CbmResultUtils.fourLevelAggregationTwoNodesOk();
		
		File f = ensureNew("./res/four-level-two-nodes-aggr.html");
		
		FileOutputStream fos = new FileOutputStream(f);
		marshaller.marshall(fos, flatResults);
	}
	
	@Test
	public void mapToWeightTest()  throws Exception {
		assertTrue(CheckBundlesUtils.mapToWeight(99L) == CheckWeight.under100ms);
		assertTrue(CheckBundlesUtils.mapToWeight(100L) == CheckWeight.under1s);
	}
	
	private File ensureNew(String name) throws IOException {
		File f = new File(name);
		if (f.exists())
			f.delete();

		f.getParentFile().mkdirs();
		f.createNewFile();

		return f;
	}

}
