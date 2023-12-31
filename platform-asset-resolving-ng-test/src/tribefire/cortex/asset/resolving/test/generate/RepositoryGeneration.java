// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.cortex.asset.resolving.test.generate;

import java.io.File;

import com.braintribe.devrock.repolet.generator.RepositoryGenerations;
import com.braintribe.utils.FileTools;

public class RepositoryGeneration {
	public static void main(String[] args) {
		try {
			File configurationFile = new File("res/repo-src/repository-skeleton-generation.yml");
			File targetFolder = new File("res/repo");
			FileTools.deleteDirectoryRecursively(targetFolder);
			RepositoryGenerations.generateFromYaml(configurationFile, targetFolder);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
