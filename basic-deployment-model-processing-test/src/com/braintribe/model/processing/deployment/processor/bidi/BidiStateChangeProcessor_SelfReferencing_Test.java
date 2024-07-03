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
package com.braintribe.model.processing.deployment.processor.bidi;

import java.util.ArrayList;

import org.junit.Test;

import com.braintribe.model.processing.deployment.processor.bidi.data.Folder;
import com.braintribe.utils.junit.assertions.BtAssertions;

/**
 * 
 */
public class BidiStateChangeProcessor_SelfReferencing_Test extends AbstractBidiScpTests {

	// ####################################
	// ## . . . . Simple tests . . . . . ##
	// ####################################

	@Test
	public void testSetParentFolder() throws Exception {
		apply(session -> {
			Folder parent = newFolder(session, "ParentFolder");
			Folder subFolder = newFolder(session, "SubFolder");
			subFolder.setParent(parent);
		});

		Folder parent = folderByName("ParentFolder");
		Folder subFolder = folderByName("SubFolder");

		BtAssertions.assertThat(parent.getSubFolders()).contains(subFolder);
		BtAssertions.assertThat(subFolder.getParent()).isEqualTo(parent);
	}

	@Test
	public void testSetSubFolderFolder() throws Exception {
		apply(session -> {
			Folder parent = newFolder(session, "ParentFolder");
			Folder subFolder = newFolder(session, "SubFolder");

			parent.setSubFolders(new ArrayList<Folder>());
			parent.getSubFolders().add(subFolder);
		});

		Folder parent = folderByName("ParentFolder");
		Folder subFolder = folderByName("SubFolder");

		BtAssertions.assertThat(parent.getSubFolders()).contains(subFolder);
		BtAssertions.assertThat(subFolder.getParent()).isEqualTo(parent);
	}

	@Test
	public void testRemoveParentFolder() throws Exception {
		// Preparation
		apply(session -> {
			Folder parent = newFolder(session, "ParentFolder");
			Folder subFolder = newFolder(session, "SubFolder");

			parent.setSubFolders(new ArrayList<Folder>());
			parent.getSubFolders().add(subFolder);
		});

		// Actual test
		apply(session -> {
			Folder subFolder = folderByName("SubFolder", session);
			subFolder.setParent(null);
		});

		Folder parent = folderByName("ParentFolder");
		Folder subFolder = folderByName("SubFolder");

		BtAssertions.assertThat(subFolder.getParent()).isNull();
		BtAssertions.assertThat(subFolder).isNotIn(parent.getSubFolders());
	}

	@Test
	public void testRemoveSubFolder() throws Exception {
		// Preparation
		apply(session -> {
			Folder parent = newFolder(session, "ParentFolder");
			Folder subFolder1 = newFolder(session, "SubFolder1");
			Folder subFolder2 = newFolder(session, "SubFolder2");
			Folder subFolder3 = newFolder(session, "SubFolder3");

			parent.setSubFolders(new ArrayList<Folder>());
			parent.getSubFolders().add(subFolder1);
			parent.getSubFolders().add(subFolder2);
			parent.getSubFolders().add(subFolder3);

		});

		// Actual test
		apply(session -> {
			Folder subFolder2 = folderByName("SubFolder2", session);
			Folder parent = folderByName("ParentFolder", session);

			parent.getSubFolders().remove(subFolder2);
		});

		Folder parent = folderByName("ParentFolder");
		Folder subFolder2 = folderByName("SubFolder2");

		BtAssertions.assertThat(parent.getSubFolders()).hasSize(2);
		BtAssertions.assertThat(subFolder2).isNotIn(parent.getSubFolders());
		BtAssertions.assertThat(subFolder2.getParent()).isNull();
	}

}
