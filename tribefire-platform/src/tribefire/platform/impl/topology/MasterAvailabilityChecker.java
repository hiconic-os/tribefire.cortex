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
package tribefire.platform.impl.topology;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.date.NanoClock;

public class MasterAvailabilityChecker implements Supplier<Boolean> {

	private static final Logger logger = Logger.getLogger(MasterAvailabilityChecker.class);
	
	private Instant availabilityCheckStart = null;
	private Instant availabilityCheckStopped = null;

	private InstanceId localInstanceId;
	
	@Override
	public Boolean get() {
		
		if (availabilityCheckStopped != null) {
			return true;
		}
		String checkApproved = TribefireRuntime.getProperty("TRIBEFIRE_TOMCAT_AVAILABILITY_CHECK", "true");
		if (!checkApproved.equalsIgnoreCase("true")) {
			logger.debug(() -> "Checking the availability is disabled.");
			availabilityCheckStopped = NanoClock.INSTANCE.instant();
			return true;
		}
		if (availabilityCheckStart == null) {
			availabilityCheckStart = NanoClock.INSTANCE.instant();
		} else {
			Instant now = NanoClock.INSTANCE.instant();
			Duration duration = Duration.between(availabilityCheckStart, now);
			long minutes = duration.toMinutes();
			if (minutes > 2) {
				logger.debug(() -> "Checking the availability has been tried for "+StringTools.prettyPrintDuration(duration, true, ChronoUnit.MILLIS)+". Disabling the check now.");
				availabilityCheckStopped = now;
				return true;
			}
		}
		String appId = localInstanceId.getApplicationId();
		if (appId != null && appId.equals(TribefireConstants.TRIBEFIRE_SERVICES_APPLICATION_ID)) {
			String servicesUrl = TribefireRuntime.getServicesUrl();
			if (!StringTools.isBlank(servicesUrl)) {
				String rpcUrl = servicesUrl.endsWith("/") ? servicesUrl + "rpc" : servicesUrl + "/rpc";

				logger.debug(() -> "Running in the master cartridge. Checking availability of: "+rpcUrl);
				
				DefaultHttpClientProvider clientProvider = new DefaultHttpClientProvider();
				clientProvider.setSocketTimeout(5000);
				try (CloseableHttpClient client = clientProvider.provideHttpClient()) {
					
					HttpGet get = new HttpGet(rpcUrl);
					try (CloseableHttpResponse response = client.execute(get)) {
						
						StatusLine statusLine = response.getStatusLine();
						int statusCode = statusLine.getStatusCode();
						logger.debug(() -> "Received status code: "+statusCode+" from "+rpcUrl);
						// We received a status.... regardless of the status, it is actually available
						availabilityCheckStopped = NanoClock.INSTANCE.instant();
						return true;
						
					} catch(SocketTimeoutException ste) {
						logger.debug(() -> "Received a timeout when trying to connect to "+rpcUrl);
						return false;
					} catch(SocketException se) {
						logger.debug(() -> "Received an error when trying to connect to "+rpcUrl);
						return false;						
					}

					
				} catch(Exception e) {
					logger.info(() -> "Could not verify the reachability of "+servicesUrl, e);
				}

			} else {
				logger.debug(() -> "Running in the master cartridge. Could not check availability because the services URL is not set.");
			}
		} else {
			logger.debug(() -> "The current instance is not the master.");
		}
		
		logger.debug(() -> "As we could not check the availability, we will proceed with the heartbeat.");
		availabilityCheckStopped = NanoClock.INSTANCE.instant();
		return true;
	}

	@Required
	@Configurable
	public void setLocalInstanceId(InstanceId localInstanceId) {
		this.localInstanceId = localInstanceId;
	}

}
