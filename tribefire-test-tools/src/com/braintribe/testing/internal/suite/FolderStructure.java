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
package com.braintribe.testing.internal.suite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.model.folder.Folder;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.workbench.WorkbenchPerspective;

/**
 * for assertion purposes with the types Folder.T and WorkbenchPerspective.T <br>
 * There is a convenience static member factory class {@link Factory} <br>
 * <br>
 * Example Usage
 *
 * <pre>
 * {@code
 * FolderStructure.Factory assertFolderStructure = new FolderStructure.Factory(demoWorkbenchSession);
 *
 * assertFolderStructure.fromWorkbenchPerspective("root")
 * 	.subFolder("root")
 * 		.subFolder("access.demo")
 * 			.subFolder("tribefire")
 * 				.subFolder("demo")
 *				.assertHasSubFolders("persons", "companies")
 *				.siblingFolder("other folder")
 *				.assertHasSubFolders("chillies", "peppers");
 * }
 *
 * @author Neidhart
 *
 */
public class FolderStructure {
	private List<FolderStructure> subFolders = new ArrayList<>();
	private String name;
	private FolderStructure parent;

	public FolderStructure(WorkbenchPerspective perspective) {
		if (perspective == null)
			throw new IllegalArgumentException("All arguments mandatory in constructor");
		
		this.name = perspective.getName();
		addSubFolders(perspective.getFolders());
	}

	public FolderStructure(Folder folder) {
		this.name = folder.getName();
		addSubFolders(folder.getSubFolders());
	}

	private FolderStructure(Folder folder, FolderStructure parent) {
		this(folder);
		this.parent = parent;
	}

	private void addSubFolders(List<Folder> folderList) {

		this.subFolders.addAll(folderList.stream().map(x -> new FolderStructure(x, this)).collect(Collectors.toList()));
	}

	public FolderStructure assertHasSubFolders(String... names) {
		for (String subFolderName : names) {
			for (FolderStructure subFolder : subFolders) {
				if (subFolder.name.equals(subFolderName)) {
					return this;
				}
			}

			throw new IllegalStateException("Subfolder '" + subFolderName + "' not found in folder " + this.name);

		}
		return this;
	}
	
	public void assertHasExactlyTheseSubfoldersAndNoOthers(String... names) {
		assertHasSubFolders(names);
		assert (subFolders.size() == names.length):
			"There were more subfolders found than expected in '" + this.name + "'.";
	}

	@Override
	public String toString() {
		String parentName = "";

		if (parent() != null) {
			parentName = parent().toString();
		}

		return parentName + "/" + this.name;
	}

	public FolderStructure parent() {
		return parent;
	}

	public FolderStructure subFolder(String subFolderName) {
		List<FolderStructure> result = subFolders.stream().filter(folder -> folder.name.equals(subFolderName)).collect(Collectors.toList());

		assert (result.size() == 1) :
			"expected to find exactly one subFolder with name " + subFolderName + " but was disappointed";

		return result.get(0);
	}

	public FolderStructure siblingFolder(String siblingFolderName) {
		return parent().subFolder(siblingFolderName);
	}

	/**
	 * helps creating folder structures from provided workbench access <br>
	 * pass the workbench access session via constructor and use {@link #fromWorkbenchPerspective(String)}
	 *
	 * @author Neidhart
	 *
	 */
	public static class Factory {
		private final PersistenceGmSession workbenchAccessSession;

		public Factory(PersistenceGmSession workbenchAccessSession) {
			this.workbenchAccessSession = workbenchAccessSession;
		}

		/**
		 *
		 * @param name
		 *            name of workbench perspective
		 * @return FolderStructure starting at the respective workbench perspective
		 */
		public FolderStructure fromWorkbenchPerspective(String name) {
			EntityQuery entityQuery = EntityQueryBuilder.from(WorkbenchPerspective.T).where().property("name").eq(name).done();
			WorkbenchPerspective perspective = workbenchAccessSession.query().entities(entityQuery).unique();

			if (perspective == null)
				throw new IllegalStateException("Workbench perspective not found: " + name);
			
			return new FolderStructure(perspective);
		}
	}
}
