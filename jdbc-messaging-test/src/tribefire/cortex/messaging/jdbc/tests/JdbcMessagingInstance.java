package tribefire.cortex.messaging.jdbc.tests;

import com.braintribe.codec.marshaller.bin.Bin2Marshaller;
import com.braintribe.common.db.BasicDbTestSession;
import com.braintribe.common.db.wire.contract.DbTestDataSourcesContract;
import com.braintribe.transport.messaging.api.MessagingContext;
import com.braintribe.transport.messaging.api.MessagingSession;

import tribefire.cortex.messaging.jdbc.JdbcConnectionProvider;
import tribefire.cortex.messaging.jdbc.JdbcMsgConnection;

/**
 * This requires a running Postgres DB, see {@link DbTestDataSourcesContract#postgres()}
 * 
 * @author peter.gazdik
 */
public class JdbcMessagingInstance {

	public final BasicDbTestSession dbSession;
	public final MessagingContext messagingContex;

	public final JdbcConnectionProvider connectionProvider;
	public final JdbcMsgConnection connection;
	public final MessagingSession session;


	public JdbcMessagingInstance() {
		dbSession = BasicDbTestSession.startDbTest();

		messagingContex = new MessagingContext();
		messagingContex.setMarshaller(new Bin2Marshaller());

		connectionProvider = new JdbcConnectionProvider();
		connectionProvider.setName("test-messaging");
		connectionProvider.setSqlPrefix("hc");
		connectionProvider.setDataSource(dbSession.contract.postgres());
		connectionProvider.setMessagingContext(messagingContex);

		connection = connectionProvider.provideMessagingConnection();

		session = connection.createMessagingSession();
	}

	public void shutDown() {
		connectionProvider.close();
		dbSession.shutdownDbTest();
	}

}
