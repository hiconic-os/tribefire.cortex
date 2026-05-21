// ============================================================================
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
package com.braintribe.model.logs.request;

import java.util.List;

import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * Request to download specific log files by name. Returns the requested files packaged as a zip resource.
 */
public interface DownloadSelectedLogFiles extends LogsRequest {

	EntityType<DownloadSelectedLogFiles> T = EntityTypes.T(DownloadSelectedLogFiles.class);

	/** The list of log file names to download. */
	List<String> getFilenames();
	void setFilenames(List<String> filenames);

	@Override
	EvalContext<? extends Logs> eval(Evaluator<ServiceRequest> evaluator);
}
