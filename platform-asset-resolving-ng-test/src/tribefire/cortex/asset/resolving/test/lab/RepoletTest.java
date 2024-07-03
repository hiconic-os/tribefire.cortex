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
