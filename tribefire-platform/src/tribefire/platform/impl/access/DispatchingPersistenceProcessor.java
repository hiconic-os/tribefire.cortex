package tribefire.platform.impl.access;

import java.util.Set;
import java.util.stream.Collectors;

import com.braintribe.cfg.Required;
import com.braintribe.model.accessapi.CustomPersistenceRequest;
import com.braintribe.model.accessapi.GetModel;
import com.braintribe.model.accessapi.GetPartitions;
import com.braintribe.model.accessapi.ManipulationRequest;
import com.braintribe.model.accessapi.ManipulationResponse;
import com.braintribe.model.accessapi.PersistenceRequest;
import com.braintribe.model.accessapi.QueryAndSelect;
import com.braintribe.model.accessapi.QueryEntities;
import com.braintribe.model.accessapi.QueryProperty;
import com.braintribe.model.accessapi.ReferencesRequest;
import com.braintribe.model.accessapi.ReferencesResponse;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.aop.api.service.AopIncrementalAccess;
import com.braintribe.model.processing.deployment.api.DeployRegistry;
import com.braintribe.model.processing.deployment.api.DeployedUnit;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.query.EntityQueryResult;
import com.braintribe.model.query.PropertyQueryResult;
import com.braintribe.model.query.SelectQueryResult;

public class DispatchingPersistenceProcessor extends AbstractDispatchingServiceProcessor<PersistenceRequest, Object> {

	private DeployRegistry deployRegistry;

	@Required
	public void setDeployRegistry(DeployRegistry deployRegistry) {
		this.deployRegistry = deployRegistry;
	}

	@Override
	protected void configureDispatching(DispatchConfiguration<PersistenceRequest, Object> dispatching) {
		dispatching.register(CustomPersistenceRequest.T, (c, r) -> customReq(c, r));
		dispatching.register(GetModel.T, (c, r) -> getModel(r));
		dispatching.register(GetPartitions.T, (c, r) -> getPartitions(r));
		dispatching.register(ManipulationRequest.T, (c, r) -> applyMan(r));
		dispatching.register(QueryAndSelect.T, (c, r) -> queryAndSelect(r));
		dispatching.register(QueryEntities.T, (c, r) -> queryEntities(r));
		dispatching.register(QueryProperty.T, (c, r) -> queryProperty(r));
		dispatching.register(ReferencesRequest.T, (c, r) -> getReferences(r));
	}

	// @formatter:off
	private Object customReq(ServiceRequestContext c, CustomPersistenceRequest r) { return resolveDelegate(r).processCustomRequest(c, r); }
	private GmMetaModel getModel(GetModel r)                      { return resolveDelegate(r).getMetaModel(); }
	private Set<String> getPartitions(GetPartitions r)            { return resolveDelegate(r).getPartitions();}
	private SelectQueryResult queryAndSelect(QueryAndSelect r)    { return resolveDelegate(r).query(r.getQuery());}
	private EntityQueryResult queryEntities(QueryEntities r)      { return resolveDelegate(r).queryEntities(r.getQuery());}
	private PropertyQueryResult queryProperty(QueryProperty r)    { return resolveDelegate(r).queryProperty(r.getQuery());}
	private ManipulationResponse applyMan(ManipulationRequest r)  { return resolveDelegate(r).applyManipulation(r); }
	private ReferencesResponse getReferences(ReferencesRequest r) { return resolveDelegate(r).getReferences(r); }
	// @formatter:on

	private AopIncrementalAccess resolveDelegate(PersistenceRequest request) {
		String accessId = request.getDomainId();
		if (accessId == null)
			throw new IllegalArgumentException("DomainId not specified for persistence request: " + request);

		DeployedUnit deployedUnit = deployRegistry.resolve(accessId);
		if (deployedUnit == null)
			throw new IllegalArgumentException("No access found for id: " + accessId);

		AopIncrementalAccess access = deployedUnit.findComponent(IncrementalAccess.T);
		if (access == null)
			throw new IllegalArgumentException("Deployable with externalId '" + accessId
					+ "' doesn't seem to be an IncrementalAccess. Found deployable components:" + componentsAsString(deployedUnit));

		return access;
	}

	private String componentsAsString(DeployedUnit deployedUnit) {
		return deployedUnit.getComponents().keySet().stream() //
				.map(EntityType::getShortName) //
				.collect(Collectors.joining(", "));
	}
}
