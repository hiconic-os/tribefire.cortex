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
package tribefire.platform.impl.deployment;

import static com.braintribe.model.generic.typecondition.TypeConditions.and;
import static com.braintribe.model.generic.typecondition.TypeConditions.hasCollectionElement;
import static com.braintribe.model.generic.typecondition.TypeConditions.isAssignableTo;
import static com.braintribe.model.generic.typecondition.TypeConditions.isKind;
import static com.braintribe.model.generic.typecondition.TypeConditions.or;
import static com.braintribe.model.generic.typecondition.basic.TypeKind.collectionType;
import static com.braintribe.model.generic.typecondition.basic.TypeKind.entityType;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.braintribe.cfg.Required;
import com.braintribe.execution.generic.ContextualizedFuture;
import com.braintribe.execution.virtual.VirtualThreadExecutorBuilder;
import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.DeployableComponent;
import com.braintribe.model.deployment.HardwiredDeployable;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.pr.criteria.BasicCriterion;
import com.braintribe.model.generic.pr.criteria.CriterionType;
import com.braintribe.model.generic.pr.criteria.PropertyCriterion;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.pr.criteria.matching.StandardMatcher;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.reflection.BaseType;
import com.braintribe.model.generic.reflection.CloningContext;
import com.braintribe.model.generic.reflection.ConfigurableCloningContext;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.SimpleType;
import com.braintribe.model.generic.reflection.StandardCloningContext;
import com.braintribe.model.generic.reflection.StrategyOnCriterionMatch;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.model.generic.session.GmSession;
import com.braintribe.model.meta.data.prompt.Confidential;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.model.processing.deployment.api.DenotationTypeBindings;
import com.braintribe.model.processing.deployment.api.DeployContext;
import com.braintribe.model.processing.deployment.api.DeployedComponent;
import com.braintribe.model.processing.deployment.api.DeployedComponentResolver;
import com.braintribe.model.processing.deployment.api.DeployedUnit;
import com.braintribe.model.processing.deployment.api.DeploymentContext;
import com.braintribe.model.processing.deployment.api.DeploymentException;
import com.braintribe.model.processing.deployment.api.DeploymentScoping;
import com.braintribe.model.processing.deployment.api.DeploymentService;
import com.braintribe.model.processing.deployment.api.MutableDeployRegistry;
import com.braintribe.model.processing.deployment.api.MutableDeploymentContext;
import com.braintribe.model.processing.deployment.api.UndeployContext;
import com.braintribe.model.processing.deployment.api.UndeploymentContext;
import com.braintribe.model.processing.meta.cmd.builders.ModelMdResolver;
import com.braintribe.model.processing.meta.cmd.extended.EntityMdDescriptor;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.thread.api.ThreadContextScoping;
import com.braintribe.utils.lcd.StopWatch;

import tribefire.platform.impl.deployment.ParallelDeploymentStatistics.PromiseStatistics;

/**
 * A {@link DeploymentService} implementation.
 * 
 * @author dirk.scheffler
 */
public class ParallelDeploymentService implements DeploymentService {

	// constants
	private static final Logger log = Logger.getLogger(ParallelDeploymentService.class);

	// configurable
	private DeploymentScoping deploymentScoping;
	private MutableDeployRegistry deployRegistry;
	private DenotationTypeBindings denotationTypeBindings;
	private DeployedComponentResolver deployedComponentResolver;

	// post initialized
	private final Map<String, ContextualizedFuture<?, Deployable>> futures = new ConcurrentHashMap<>();
	private final ReentrantLock deploymentLock = new ReentrantLock();

	private ThreadContextScoping threadContextScoping;

	@Required
	public void setDeploymentScoping(DeploymentScoping deploymentScoping) {
		this.deploymentScoping = deploymentScoping;
	}

	@Required
	public void setDeployRegistry(MutableDeployRegistry deployRegistry) {
		this.deployRegistry = deployRegistry;
	}

	/* I had to change this to a Supplier, rather than the actual value - DeploymentSpace.parallelDeploymentService() */
	@Required
	public void setDenotationTypeBindings(DenotationTypeBindings denotationTypeBindings) {
		this.denotationTypeBindings = denotationTypeBindings;
	}

	@Required
	public void setDeployedComponentResolver(DeployedComponentResolver deployedComponentResolver) {
		this.deployedComponentResolver = deployedComponentResolver;
	}

	@Required
	public void setThreadContextScoping(ThreadContextScoping threadContextScoping) {
		this.threadContextScoping = threadContextScoping;
	}

	@Override
	public void deploy(DeployContext deployContext) throws DeploymentException {
		ParallelDeploymentStatistics stats = new ParallelDeploymentStatistics();
		deploymentLock.lock();
		try {
			stats.stopWatch.intermediate("Monitor");
			s_deploy(deployContext, stats);
			stats.stopWatch.intermediate("Deployment");
		} finally {
			deploymentLock.unlock();
		}
		if (log.isDebugEnabled()) {
			stats.createStatistics();
			log.debug("Deployment of deploy context:\n" + stats);
		}
	}

	class ParallelDeploymentController {

		// TODO this brings dependency to com.braintribe.execution:execution.
		// Thread pool should injected (or rather ThreadPoolFactory that takes params like thread name should be injected)
		private final ExecutorService standardDeploymentThreadPool;
		private final DeployContext context;
		private final ParallelDeploymentStatistics stats;

		public ParallelDeploymentController(int deployableAmount, DeployContext context, ParallelDeploymentStatistics stats) {
			this.context = context;
			this.stats = stats;

			standardDeploymentThreadPool = VirtualThreadExecutorBuilder.newPool().concurrency(Integer.MAX_VALUE).threadNamePrefix("Deployer")
					.description("Deployer").build();
			stats.standardThreadCount = Integer.MAX_VALUE;

			/* Note(RKU): Since we're using virtual threads now, there seems to be no need for 2 different thread pools */
		}

		Future<?> enqueue(Deployable deployable, ParallelDeploymentStatistics stats) {
			Deployer deployer = new Deployer(context, deployable, stats);
			ContextClassLoaderTransfer classLoaderTransfer = new ContextClassLoaderTransfer(deployer);
			Future<?> future = standardDeploymentThreadPool.submit(threadContextScoping.bindContext(classLoaderTransfer));
			return future;
		}

		void waitForFinishedDeployment() {

			stats.waitForFinishedDeploymentStart();

			for (ContextualizedFuture<?, Deployable> f : futures.values()) {
				try {
					f.get();
					context.succeeded(f.getContext());
				} catch (Throwable e) {
					context.failed(f.getContext(), e);
				}
			}

			stats.endOfWaitForFinishedDeploymentStart();

			futures.clear();
		}
	}

	/**
	 * This class is required as it seems that the {@link ThreadContextScoping} is not able to transfer the current class loader context.
	 */
	class ContextClassLoaderTransfer implements Runnable {

		private final Runnable delegate;
		private final ClassLoader classLoader;

		public ContextClassLoaderTransfer(Runnable delegate) {
			this.delegate = delegate;
			this.classLoader = Thread.currentThread().getContextClassLoader();
		}

		@Override
		public void run() {
			ClassLoader classLoaderBackup = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(classLoader);
			try {
				delegate.run();
			} finally {
				Thread.currentThread().setContextClassLoader(classLoaderBackup);
			}
		}

	}

	class Deployer implements Runnable {

		private final DeployContext deployContext;
		private final Deployable deployable;
		private final ParallelDeploymentStatistics stats;

		public Deployer(DeployContext deployContext, Deployable deployable, ParallelDeploymentStatistics stats) {
			this.deployContext = deployContext;
			this.deployable = deployable;
			this.stats = stats;
		}

		@Override
		public void run() {
			BasicDeploymentContext<?, ?> context = new BasicDeploymentContext<>( //
					deployedComponentResolver, resolveDeploymentSession(), deployable, deployContext.areDeployablesFullyFetched());

			PromiseStatistics promiseStats = stats.acquirePromiseStats(deployable);
			promiseStats.executionStarts();

			try {
				deploy(context);
			} finally {
				promiseStats.executionEnded();
				context.onAfterDeployment();
			}

		}

		private PersistenceGmSession resolveDeploymentSession() {
			GmSession deployableSession = deployable.session();
			if (deployableSession instanceof PersistenceGmSession)
				return (PersistenceGmSession) deployableSession;
			else
				return deployContext.session().newEquivalentSession();
		}
	}

	private void s_deploy(DeployContext deployContext, ParallelDeploymentStatistics stats) throws DeploymentException {

		final int size = deployContext.deployables().size();
		stats.deployablesCount = size;

		ParallelDeploymentController controller = new ParallelDeploymentController(size, deployContext, stats);
		stats.stopWatch.intermediate("Controller Init");

		stats.deploymentStarts();
		deployContext.deploymentStarted();

		for (Deployable deployable : deployContext.deployables()) {
			String externalId = deployable.getExternalId();
			if (externalId == null) {
				log.error("No externalId set on deployable of type '" + deployable.entityType().getTypeSignature() + "'. Instance: " + deployable);
			} else {
				Future<?> future = controller.enqueue(deployable, stats);
				futures.put(externalId, new ContextualizedFuture<>(future, deployable));
				deployContext.started(deployable);
			}
		}

		stats.stopWatch.intermediate("Promises Enqueued");

		controller.waitForFinishedDeployment();
		stats.stopWatch.intermediate("Deployment Finish Waiting");
		stats.deploymentFinished();

	}

	@Override
	public void undeploy(UndeployContext undeployContext) throws DeploymentException {
		deploymentLock.lock();
		try {
			s_undeploy(undeployContext);
		} finally {
			deploymentLock.unlock();
		}
	}

	private void s_undeploy(UndeployContext undeployContext) {
		for (Deployable deployable : undeployContext.deployables()) {
			BasicDeploymentContext<?, ?> context = new BasicDeploymentContext<>(deployable);
			try {
				undeploy(context);
				undeployContext.succeeded(deployable);
			} catch (Exception e) {
				undeployContext.failed(deployable, e);
			}
		}
	}

	private void deploy(MutableDeploymentContext<?, ?> context) throws DeploymentException {
		validateContext(context);

		String externalId = context.getDeployable().getExternalId();
		if (!deployRegistry.isDeployed(externalId)) {
			startScope(context);
			try {
				scopedDeploy(context);
				popScope(context);
			} catch (RuntimeException | Error e) {
				endScope(context);
				throw e;
			}
		}
	}

	private void undeploy(MutableDeploymentContext<?, ?> context) throws DeploymentException {
		validateContext(context);

		startScope(context);
		try {
			if (deployRegistry.isDeployed(context.getDeployable().getExternalId())) {
				scopedUndeploy(context);
			}
		} finally {
			endScope(context);
		}
	}

	private void scopedDeploy(MutableDeploymentContext<?, ?> context) throws DeploymentException {
		Deployable deployable = requireNonNull(context.getDeployable(), "deployable");
		String shortDeployableString = shortDeployableString(deployable);

		log.info(() -> "Deploying " + shortDeployableString);
		if (log.isDebugEnabled())
			logDetails(context, deployable, shortDeployableString);

		if (deployable instanceof HardwiredDeployable) {
			log.info(() -> "Ignored local deployment of hardwired deployable: " + shortDeployableString);
			return;
		}

		DeployedUnit deployedUnit = buildLocalUnit(context, shortDeployableString);

		Deployable shallowCopy = makeShallowCopyOf(deployable);
		deployRegistry.register(shallowCopy, deployedUnit);
		log.info(() -> "DEPLOYED  " + shortDeployableString);
	}

	private void logDetails(MutableDeploymentContext<?, ?> context, Deployable deployable, String shortDeployableString) {
		// Profiler shows the cloning takes quite some time; only using it when tracing is active
		if (log.isTraceEnabled()) {
			Deployable printableDeployable = clonePrintable(deployable, context.getSession().getModelAccessory().getMetaData());
			log.debug("Deployable details: " + ((printableDeployable == null) ? shortDeployableString : printableDeployable));
		} else {
			log.debug("Deployable details: " + shortDeployableString);
		}
	}

	// @formatter:off
	private static TraversingCriterion deployableShallowifyingTc = TC.create()
			.conjunction()
				.property()
				.typeCondition(or( //
						and(
								isKind(collectionType),
								hasCollectionElement(isKind(entityType))
						),
						isKind(entityType)
					))
				.negation()
					.pattern()
						.typeCondition(isAssignableTo(Deployable.T))
						.property(Deployable.module)
					.close() // pattern
			.close() // conjunction
		.done();
	// @formatter:on

	private Deployable makeShallowCopyOf(Deployable deployable) {
		CloningContext cc = ConfigurableCloningContext.build().withMatcher(StandardMatcher.create(deployableShallowifyingTc)).done();

		return deployable.clone(cc);
	}

	private void scopedUndeploy(DeploymentContext<?, ?> context) throws DeploymentException {
		Deployable deployable = context.getDeployable();

		DeployedUnit unit = deployRegistry.unregister(deployable);

		unbind(deployable, unit);

		String desc = shortDeployableString(deployable);

		if (unit != null)
			log.info(() -> "Undeployed: " + desc);
		else
			log.debug(() -> "No unit to be undeployed for: " + desc);
	}

	private void unbind(Deployable deployable, DeployedUnit unit) {
		for (DeployedComponent dc : unit.getComponents().values()) {
			ComponentBinder<Deployable, Object> binder = (ComponentBinder<Deployable, Object>) dc.binder();
			if (binder != null)
				binder.unbind(new UndeployContextImpl(deployable, dc));
		}
	}

	static class UndeployContextImpl implements UndeploymentContext<Deployable, Object> {

		private final Deployable deployable;
		private final DeployedComponent deployedComponent;

		public UndeployContextImpl(Deployable deployable, DeployedComponent deployedComponent) {
			this.deployable = deployable;
			this.deployedComponent = deployedComponent;
		}

		@Override
		public Deployable getDeployable() {
			return deployable;
		}

		@Override
		public Object getBoundInstance() {
			return deployedComponent.exposedImplementation();
		}

		@Override
		public Object getSuppliedInstance() {
			return deployedComponent.suppliedImplementation();
		}

	}

	private DeployedUnit buildLocalUnit(MutableDeploymentContext<?, ?> context, String shortDeployableString) throws DeploymentException {
		StopWatch stopWatch = new StopWatch();

		Set<EntityType<? extends Deployable>> componentTypes = resolveComponentTypes(context);
		stopWatch.intermediate("Resolve Component Types");

		Function<MutableDeploymentContext<?, ?>, DeployedUnit> deployedUnitSupplier = denotationTypeBindings
				.resolveDeployedUnitSupplier(context.getDeployable());
		stopWatch.intermediate("Resolve DeployedUnit Supplier");

		DeployedUnit deployedUnit = deployedUnitSupplier.apply(context);

		stopWatch.intermediate("DeployedUnit Creation");

		validateLocalUnitComponents(context.getDeployable(), deployedUnit, componentTypes);

		stopWatch.intermediate("Validation");

		log.debug(() -> "Building local unit " + shortDeployableString + " took: " + stopWatch);
		return deployedUnit;
	}

	private Set<EntityType<? extends Deployable>> resolveComponentTypes(DeploymentContext<?, ?> context) {
		List<EntityMdDescriptor> metadataList = context //
				.getSession() //
				.getModelAccessory() //
				.getMetaData() //
				.entity(context.getDeployable()) //
				.meta(DeployableComponent.T) //
				.listExtended();

		if (metadataList.isEmpty() && log.isWarnEnabled())
			log.warn("No " + DeployableComponent.class.getSimpleName() + " metadata was resolved for "
					+ shortDeployableString(context.getDeployable()));

		return (Set<EntityType<? extends Deployable>>) (Set<?>) metadataList.stream() //
				.map(this::getOwnerTypeSignature) //
				.map(GMF.getTypeReflection()::findEntityType) //
				.filter(t -> t != null && Deployable.T.isAssignableFrom(t)) //
				.collect(Collectors.toSet());
	}

	private String getOwnerTypeSignature(EntityMdDescriptor e) {
		return e.getOwnerTypeInfo().addressedType().getTypeSignature();
	}

	private void startScope(DeploymentContext<?, ?> context) {
		deploymentScoping.push(context);
	}

	private void popScope(DeploymentContext<?, ?> context) {
		try {
			deploymentScoping.pop(context);
		} catch (Exception e) {
			log.error("Failed to pop scope", e);
		}
	}

	private void endScope(DeploymentContext<?, ?> context) {
		popScope(context);

		try {
			deploymentScoping.end(context);
		} catch (Exception e) {
			log.error("Failed to end scope", e);
		}
	}

	private void validateContext(DeploymentContext<?, ?> context) {
		if (context == null)
			throw new IllegalArgumentException(DeploymentContext.class.getName() + " cannot be null");

		if (context.getDeployable() == null)
			throw new IllegalArgumentException(
					"Given " + DeploymentContext.class.getName() + " has no " + Deployable.T.getTypeSignature() + " set: " + context);
	}

	private void validateLocalUnitComponents(Deployable deployable, DeployedUnit deployedUnit,
			Set<EntityType<? extends Deployable>> resolvedComponentTypes) {

		Set<EntityType<? extends Deployable>> currentComponentTypes = deployedUnit.getComponents().keySet();

		Set<String> missingTypes = null;

		if (resolvedComponentTypes.size() == currentComponentTypes.size()) {
			resolvedComponentTypes.removeAll(currentComponentTypes);

			// Current is exactly as resolved. Nothing more to be checked after this.
			if (resolvedComponentTypes.isEmpty())
				return;

			missingTypes = resolvedComponentTypes.stream() //
					.map(GenericModelType::getTypeSignature) //
					.collect(Collectors.toSet());

		} else {
			missingTypes = resolvedComponentTypes.stream() //
					.filter((e) -> !currentComponentTypes.contains(e)) //
					.map(e -> e.getTypeSignature()) //
					.collect(Collectors.toSet());
		}

		if (!missingTypes.isEmpty())
			throw new DeploymentException("Missing binding for type(s): " + String.join(", ", missingTypes) + " while deploying " + desc(deployable));

		Set<String> extraTypes = currentComponentTypes.stream() //
				.filter((e) -> !resolvedComponentTypes.contains(e)) //
				.map(GenericModelType::getTypeSignature) //
				.collect(Collectors.toSet());

		if (!extraTypes.isEmpty())
			log.warn("Deployed unit created for " + desc(deployable) + " contains component types which were not resolved for its deployable type: "
					+ String.join(", ", extraTypes));
	}

	private String desc(Deployable d) {
		return d.shortDescription();
	}

	public void waitForDeployableIfInDeployment(String externalId) {

		ContextualizedFuture<?, Deployable> contextualizedFuture = futures.get(externalId);
		if (contextualizedFuture == null) {
			return;
		}

		try {
			contextualizedFuture.get();
		} catch (Throwable t) {
			return;
		}
	}

	private String shortDeployableString(Deployable deployable) {
		return deployable.entityType().getShortName() + "[" + deployable.getExternalId() + "]";
	}

	private Deployable clonePrintable(Deployable deployable, ModelMdResolver mdResolver) {
		try {
			return BaseType.INSTANCE.clone(new PasswordFilteringCloningContext(mdResolver), deployable, StrategyOnCriterionMatch.skip);

		} catch (Exception e) {
			log.warn("Unable to clone printable version of deployable: " + shortDeployableString(deployable));
			return null;
		}
	}

	private static class PasswordFilteringCloningContext extends StandardCloningContext {

		private final ModelMdResolver mdResolver;

		public PasswordFilteringCloningContext(ModelMdResolver mdResolver) {
			this.mdResolver = mdResolver;
		}

		@Override
		public Object postProcessCloneValue(GenericModelType propertyType, Object clonedValue) {
			if (!(propertyType instanceof SimpleType))
				return super.postProcessCloneValue(propertyType, clonedValue);

			Stack<BasicCriterion> ts = getTraversingStack();

			BasicCriterion peekCriterion = ts.peek();
			if (peekCriterion.criterionType() != CriterionType.PROPERTY)
				return super.postProcessCloneValue(propertyType, clonedValue);

			Stack<Object> os = getObjectStack();
			GenericEntity entity = (GenericEntity) os.get(os.size() - 2);
			if (entity.getId() == null)
				return super.postProcessCloneValue(propertyType, clonedValue);

			PropertyCriterion pc = (PropertyCriterion) peekCriterion;

			boolean confidential = mdResolver.entity(entity).property(pc.getPropertyName()).is(Confidential.T);

			if (confidential)
				return getValueToReplacePassword(propertyType);

			return super.postProcessCloneValue(propertyType, clonedValue);
		}

		public static Object getValueToReplacePassword(GenericModelType pawwordPropertyType) {
			if (pawwordPropertyType.getTypeCode() == TypeCode.stringType)
				return "*****";
			else
				return null;
		}

	}

}
