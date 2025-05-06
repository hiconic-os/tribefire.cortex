
package tribefire.cortex.messaging.jdbc.wire;

import tribefire.cortex.messaging.jdbc.wire.space.JdbcMessagingModuleSpace;
import tribefire.module.wire.contract.StandardTribefireModuleWireModule;
import tribefire.module.wire.contract.TribefireModuleContract;

public enum JdbcMessagingModuleWireModule implements StandardTribefireModuleWireModule {

	INSTANCE;

	@Override
	public Class<? extends TribefireModuleContract> moduleSpaceClass() {
		return JdbcMessagingModuleSpace.class;
	}

}