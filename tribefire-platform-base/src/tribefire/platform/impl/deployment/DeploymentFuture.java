package tribefire.platform.impl.deployment;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.braintribe.model.deployment.Deployable;

/**
 * @author peter.gazdik
 */
public class DeploymentFuture {

	public final Deployable deployable;

	// future is initially null, indicating deployment has not even started yet
	// as long as that is the case, the cdl has a value of one
	private volatile Future<?> future;
	private final CountDownLatch futureCdl = new CountDownLatch(1);

	public DeploymentFuture(Deployable deployable) {
		this.deployable = deployable;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
		this.futureCdl.countDown();
	}

	public void waitForDeployment() throws InterruptedException, ExecutionException {
		// wait for future to be set
		futureCdl.await();

		// wait for actual deployment
		future.get();
	}

}
