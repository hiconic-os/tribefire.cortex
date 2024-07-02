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
package tribefire.cortex.deployables.initializer.support.wire.space;

import static com.braintribe.wire.api.util.Lists.list;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.cortex.aspect.FulltextAspect;
import com.braintribe.model.cortex.aspect.IdGeneratorAspect;
import com.braintribe.model.cortex.aspect.SecurityAspect;
import com.braintribe.model.cortex.aspect.StateProcessingAspect;
import com.braintribe.model.cortex.processorrules.BidiPropertyStateChangeProcessorRule;
import com.braintribe.model.cortex.processorrules.MetaDataStateChangeProcessorRule;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.extensiondeployment.StateChangeProcessorRule;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.deployables.initializer.support.wire.contract.DefaultDeployablesContract;
import tribefire.cortex.initializer.support.wire.space.AbstractInitializerSpace;

@Managed
public class DefaultDeployablesSpace extends AbstractInitializerSpace implements DefaultDeployablesContract {

	public static final String DEFAULTDEPLOYABLE_NAMESPACE = "default:deployable/"; 

	@Override
	@Managed
	public List<Deployable> defaultDeployables() {
		List<Deployable> defaultDeployables = new ArrayList<>();
		defaultDeployables.addAll(defaultAspects());
		defaultDeployables.addAll(defaultStateChangeProcessorRules());
		return defaultDeployables;
	}

	@Override
	@Managed
	public List<AccessAspect> defaultAspects() {
		return list(
					stateProcessingAspect(),
					fulltextAspect(),
					securityAspect(),
					idGeneratorAspect()
				);
	}

	@Override
	@Managed
	public List<StateChangeProcessorRule> defaultStateChangeProcessorRules() {
		return list(
					bidiPropertyProcessorRule(),
					metaDataProcessorRule()
				);
	}

	@Override
	@Managed
	public StateProcessingAspect stateProcessingAspect() {
		return lookup(DEFAULTDEPLOYABLE_NAMESPACE + "aspect.stateProcessing.default");
	}

	@Override
	@Managed
	public FulltextAspect fulltextAspect() {
		return lookup(DEFAULTDEPLOYABLE_NAMESPACE + "aspect.fulltext.default");
	}

	@Override
	@Managed
	public SecurityAspect securityAspect() {
		return lookup(DEFAULTDEPLOYABLE_NAMESPACE + "aspect.security.default");
	}

	@Override
	@Managed
	public IdGeneratorAspect idGeneratorAspect() {
		return lookup(DEFAULTDEPLOYABLE_NAMESPACE + "aspect.idGenerator.default");
	}

	@Override
	@Managed
	public BidiPropertyStateChangeProcessorRule bidiPropertyProcessorRule() {
		return lookup(DEFAULTDEPLOYABLE_NAMESPACE + "processorRule.bidiProperty.default");
	}

	@Override
	@Managed
	public MetaDataStateChangeProcessorRule metaDataProcessorRule() {
		return lookup(DEFAULTDEPLOYABLE_NAMESPACE + "processorRule.metaData.default");
	}
}
