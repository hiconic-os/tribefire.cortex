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
package com.braintribe.model.processing.packaging;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.braintribe.cfg.Configurable;
import com.braintribe.codec.dom.genericmodel.GenericModelRootDomCodec;
import com.braintribe.codec.marshaller.stax.StaxMarshaller;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.packaging.Packaging;
import com.braintribe.provider.Holder;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.lcd.StringTools;


public class PackagingProvider implements Supplier<Packaging>{
	
	private Logger logger = Logger.getLogger(PackagingProvider.class);
	
	protected GenericModelRootDomCodec<Object> codec;
	protected GenericModelType rootType;
	protected Supplier<URL> packaginUrlProvider = () -> {
			return PackagingProvider.class.getClassLoader().getResource("packaginginfo/packaging.xml");
	};
	
	@Configurable
	public void setPackaginUrlProvider(Supplier<URL> packaginUrlProvider) {
		this.packaginUrlProvider = packaginUrlProvider;
	}
	
	public void setPackagingUrl(URL packagingUrl) {
		this.packaginUrlProvider = new Holder<URL>(packagingUrl);
	}
	
	public GenericModelRootDomCodec<Object> getCodec() {
		if (this.codec == null) {
			this.codec = new GenericModelRootDomCodec<Object>();
			this.codec.setType(getRootType());
		}
		return this.codec;
	}
	
	protected GenericModelType getRootType() {
		if (this.rootType == null) {
			this.rootType = GMF.getTypeReflection().getType("object");
		}

		return this.rootType;
	}
	@Override
	public Packaging get() throws RuntimeException {

		try {
			URL packagingUrl = packaginUrlProvider.get();
			
			if (packagingUrl == null) {
				logger.warn("No packaging information found.");
				return null;
			}
			
			String packagingContent = null;
			try (InputStream packagingStream = new BufferedInputStream(packagingUrl.openStream())) {
				packagingContent = IOTools.slurp(packagingStream, "UTF-8");
			} catch(Exception e) {
				throw new Exception("Could not read content from URL "+packagingUrl, e);
			}

			if (StringTools.isBlank(packagingContent)) {
				throw new Exception("The content read from URL "+packagingUrl+" is empty.");
			}
			
			boolean tryClassicCodec = true;
			if (packagingContent.contains("<?gm-xml version=\"4\"?>")) {
				tryClassicCodec = false;
			}
			
			Packaging result = null;
			
			if (tryClassicCodec) {
				try {
					ByteArrayInputStream in = new ByteArrayInputStream(packagingContent.getBytes("UTF-8"));
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
					result = (Packaging) getCodec().decode(document);
				} catch(Exception e) {
					if (logger.isTraceEnabled()) logger.trace("Could not parse packaging content from "+packagingUrl+" with classic codec. Trying marshaller...", e);
				}
			}
			
			if (result == null) {
				ByteArrayInputStream in = new ByteArrayInputStream(packagingContent.getBytes("UTF-8"));
				StaxMarshaller marshaller = new StaxMarshaller();
				result = (Packaging) marshaller.unmarshall(in);
			}
			
			return result;
			
		} catch (Exception e) {
			throw new RuntimeException("Error parsing packaging info",e);
		}
		
	}

}
