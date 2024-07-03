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
package com.braintribe.model.processing.platformreflection.tf;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.packaging.Dependency;
import com.braintribe.model.packaging.Packaging;

/**
 * Best effort packaging supplier that requires a path to the WEB-INF/lib folder
 * 
 */
public class FileBasedPackagingProvider implements Supplier<Packaging> {

	private static Logger logger = Logger.getLogger(FileBasedPackagingProvider.class);
	
	private File libFolder;
	
	@Override
	public Packaging get() {
		if (libFolder == null) {
			logger.trace(() -> "No lib folder configured.");
			return null;
		}
		
		Packaging packaging = Packaging.T.create();
		packaging.setTimestamp(new Date());
		List<Dependency> dependencies = packaging.getDependencies();
		
		File[] files = libFolder.listFiles(f -> f.isFile() && f.getName().toLowerCase().endsWith(".jar"));
		
		if (files != null) {
			Arrays.sort(files);
			for (File f : files) {
				
				Dependency dep = Dependency.T.create();
				
				String name = f.getName();
				//Get rid of extension
				name = name.substring(0, name.length()-4);
				
				int index = name.lastIndexOf("-");
				if (index > 0 && index < (name.length()-1)) {

					String artifactId = name.substring(0, index);
					String version = name.substring(index+1);
					
					dep.setArtifactId(artifactId);
					dep.setVersion(version);
					
				} else {
					dep.setArtifactId(name);
				}
				
				dependencies.add(dep);				
			}
		}
		
		
		return packaging;
	}

	@Required
	public void setLibFolder(File libFolder) {
		if (libFolder == null) {
			logger.warn("The library folder is set to null.");
			return;
		}
		if (libFolder.exists() && libFolder.isDirectory()) {
			logger.info(() -> "Accepting library folder: "+libFolder.getAbsolutePath());
			this.libFolder = libFolder;
		} else {
			logger.info(() -> "Not accepting library folder: "+libFolder.getAbsolutePath());
		}
	}


}
