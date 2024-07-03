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
package com.braintribe.testing.internal.tribefire.helpers.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import com.braintribe.codec.marshaller.api.MarshallException;
import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;

abstract class AbstractHttpHelper<H extends HttpUriRequest> {
	private final HttpClient client = HttpClientBuilder.create().build();

	protected final H request;

	private final HttpResponse response;
	private String content;

	AbstractHttpHelper(H request) throws ClientProtocolException, IOException {
		this.request = request;
		
		response = client.execute(request);
		
		assertThat(response).as("No HTTP Response recieved").isNotNull();
	}
	
	public String getContent() throws UnsupportedOperationException, IOException {
		if (content == null) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line + "\n");
			}
			
			content = result.toString();
		}
		
		return content;
	}
	
	public <T> T getUnmarshalledGmObject() throws MarshallException, UnsupportedOperationException, IOException {
		JsonStreamMarshaller jsm = new JsonStreamMarshaller();
		@SuppressWarnings("unchecked")
		T unmarshalled = (T) jsm.unmarshall(new ByteArrayInputStream(getContent().getBytes("UTF-8")));
		return unmarshalled;
	}
	
	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}

}
