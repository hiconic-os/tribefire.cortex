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
package com.braintribe.transport.messaging.bq;

import com.braintribe.transport.messaging.api.MessagingConnectionProvider;
import com.braintribe.transport.messaging.api.MessagingContext;
import com.braintribe.transport.messaging.api.MessagingException;

/**
 * {@link MessagingConnectionProvider} implementation for providing {@link BqMessagingConnection}(s).
 * 
 * @see MessagingConnectionProvider
 * @see BqMessagingConnection
 */
public class BlockingQueueMessagingConnectionProvider implements MessagingConnectionProvider<BqMessagingConnection> {

    private MessagingContext messagingContext;

	public void setMessagingContext(MessagingContext messagingContext) {
		this.messagingContext = messagingContext;
	}
    
	@Override
	public BqMessagingConnection provideMessagingConnection() throws MessagingException {
		return new BqMessagingConnection(messagingContext);
	}
	
	@Override
	public void close() {
		// no-op, there is nothing to be closed so far in this MessagingConnectionProvider
	}
	
	@Override
	public String toString() {
		return description();
	}

	@Override
	public String description() {
		return "Blocking Queue Messaging";
	}
}
