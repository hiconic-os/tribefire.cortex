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
package tribefire.cortex.asset.resolving.ng.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.processing.core.expert.api.DenotationMap;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;

import tribefire.cortex.asset.resolving.ng.api.AssetResolutionContext;
import tribefire.cortex.asset.resolving.ng.api.AssetResolutionContextBuilder;

public class BasicAssetResolutionContext implements AssetResolutionContext, AssetResolutionContextBuilder {

	private ManagedGmSession session = new BasicManagedGmSession();
	private DenotationMap<PlatformAssetNature, List<String>> natureParts;
	private boolean includeDocumentation;
	private boolean selectorFiltering;
	private boolean verboseOutput;
	private boolean runtime;
	private boolean designtime;
	private String stage;
	private final Set<String> tags = new HashSet<>();
	private boolean lenient = false;

	@Override
	public AssetResolutionContextBuilder session(ManagedGmSession session) {
		this.session = session;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder natureParts(DenotationMap<PlatformAssetNature, List<String>> natureParts) {
		this.natureParts = natureParts;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder selectorFiltering(boolean selectorFiltering) {
		this.selectorFiltering = selectorFiltering;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder includeDocumentation(boolean includeDocumentation) {
		this.includeDocumentation = includeDocumentation;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder verboseOutput(boolean verboseOutput) {
		this.verboseOutput = verboseOutput;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder runtime(boolean runtime) {
		this.runtime = runtime;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder designtime(boolean designtime) {
		this.designtime = designtime;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder stage(String stage) {
		this.stage = stage;
		return this;
	}

	@Override
	public AssetResolutionContextBuilder tags(Set<String> tags) {
		this.tags.addAll(tags);
		return this;
	}
	
	@Override
	public AssetResolutionContextBuilder lenient(boolean lenient) {
		this.lenient  = lenient;
		return this;
	}

	@Override
	public AssetResolutionContext done() {
		return this;
	}

	@Override
	public boolean isRuntime() {
		return runtime;
	}

	@Override
	public boolean isDesigntime() {
		return designtime;
	}

	@Override
	public String getStage() {
		return stage;
	}

	@Override
	public Set<String> getTags() {
		return tags;
	}
	
	@Override
	public boolean lenient() {
		return lenient;
	}

	@Override
	public ManagedGmSession session() {
		return session;
	}

	@Override
	public DenotationMap<PlatformAssetNature, List<String>> natureParts() {
		return natureParts;
	}

	@Override
	public boolean selectorFiltering() {
		return selectorFiltering;
	}

	@Override
	public boolean includeDocumentation() {
		return includeDocumentation;
	}

	@Override
	public boolean verboseOutput() {
		return verboseOutput;
	}

}
