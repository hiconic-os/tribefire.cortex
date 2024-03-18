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

import com.braintribe.codec.marshaller.bin.Bin2Marshaller;
import com.braintribe.transport.messaging.api.MessagingConnectionProvider;
import com.braintribe.transport.messaging.api.MessagingContext;

public class TestMessagingConnectionProvider {
	
	public static final TestMessagingConnectionProvider instance = new TestMessagingConnectionProvider();
	
	private final BlockingQueueMessagingConnectionProvider messagingConnectionProvider;
	
	private TestMessagingConnectionProvider() {
		messagingConnectionProvider = getMessagingConnectionProvider();
	}
	
	public MessagingConnectionProvider<?> get() {
		return messagingConnectionProvider;
	}
	
	private BlockingQueueMessagingConnectionProvider getMessagingConnectionProvider() {
		BlockingQueueMessagingConnectionProvider gmDmbMqConnectionProvider = new BlockingQueueMessagingConnectionProvider();
		gmDmbMqConnectionProvider.setMessagingContext(getMessagingContext());
		
		return gmDmbMqConnectionProvider;
	}
	
	protected MessagingContext getMessagingContext() {
		MessagingContext context = new MessagingContext();
		context.setMarshaller(new Bin2Marshaller());
		return context;
	}
}
