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
package tribefire.cortex.asset.resolving.test.lab;

import java.io.File;

import com.braintribe.devrock.repolet.launcher.Launcher;

public class RepoletTest {
	public static void main(String[] args) {
		Launcher launcher = Launcher.build().repolet().name("repo").filesystem().filesystem(new File("res/repo")).close().close().done();
		
		launcher.launch();
		
		try {
			System.out.println(launcher.getAssignedPort());
			System.out.println("=====");
		}
		finally {
			launcher.shutdown();
		}
	}
}
