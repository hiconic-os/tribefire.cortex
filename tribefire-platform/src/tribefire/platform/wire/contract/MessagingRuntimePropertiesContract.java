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
package tribefire.platform.wire.contract;

import java.time.Duration;

import com.braintribe.common.lcd.Numbers;
import com.braintribe.wire.api.annotation.Default;

import tribefire.module.wire.contract.PropertyLookupContract;

public interface MessagingRuntimePropertiesContract extends PropertyLookupContract {

	@Default("" + Numbers.MEBIBYTE)
	long TRIBEFIRE_MESSAGING_TRANSIENT_PERSISTENCE_THRESHOLD();
	
	@Default("PT30M")
	Duration TRIBEFIRE_MESSAGING_TRANSIENT_PERSISTENCE_TTL();
	
	@Default("PT5M")
	Duration TRIBEFIRE_MESSAGING_TRANSIENT_PERSISTENCE_CLEANUP_INTERVAL();
}
