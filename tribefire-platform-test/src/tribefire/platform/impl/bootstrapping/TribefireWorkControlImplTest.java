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
package tribefire.platform.impl.bootstrapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.experimental.categories.Category;

import com.braintribe.testing.category.VerySlow;

@Category(VerySlow.class)
public class TribefireWorkControlImplTest {

	protected static ExecutorService service = Executors.newFixedThreadPool(100);

	@AfterClass
	public static void shutdown() throws Exception {
		service.shutdown();
	}

	//@Test
	public void testWorkPermission() throws Exception {


		int runs = 100;
		Random rnd = new Random();

		for (int k=0; k<runs; ++k) {
			
			TribefireWorkControlImpl.instance.permissionGranted = false;
			
			List<Future<Boolean>> futures = new ArrayList<>();
			
			int count = rnd.nextInt(50);
			for (int i=0; i<count; ++i) {
				Worker w = new Worker(i);
				Future<Boolean> submit = service.submit(w);
				futures.add(submit);
			}

			for (Future<Boolean> future : futures) {
				if (future.isDone()) {
					throw new Exception("One future is done.");
				}
			}
			Thread.sleep(2000L);
			
			TribefireWorkControlImpl.instance.giveWorkPermission();
			
			for (Future<Boolean> future : futures) {
				future.get(1000L, TimeUnit.MILLISECONDS);
			}
			
			System.out.println(""+count+" workers returned successfully true");

		}

		
	}
	
	//@Test
	public void testWorkPermissionWithDelay() throws Exception {


		int runs = 100;
		Random rnd = new Random();

		for (int k=0; k<runs; ++k) {
			
			TribefireWorkControlImpl.instance.permissionGranted = false;
			
			List<Future<Boolean>> futures = new ArrayList<>();
			
			int count = rnd.nextInt(50);
			long delay = rnd.nextInt(2000);
			
			PermissionGiver pg = new PermissionGiver(delay);
			service.submit(pg);
			
			for (int i=0; i<count; ++i) {
				Thread.sleep(50L);
				Worker w = new Worker(i);
				Future<Boolean> submit = service.submit(w);
				futures.add(submit);
			}

			Thread.sleep(2000L);
			
			for (Future<Boolean> future : futures) {
				future.get(1000L, TimeUnit.MILLISECONDS);
			}
			
			System.out.println(""+count+" workers returned successfully true");

		}

		
	}
	

}
