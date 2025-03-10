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
package com.braintribe.model.processing.exchange.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.notification.HasNotifications;
import com.braintribe.model.notification.Level;
import com.braintribe.model.notification.Notification;
import com.braintribe.model.processing.notification.api.builder.Notifications;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.managed.NotFoundException;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;

public abstract class ServiceBase {
	
	private Logger logger = Logger.getLogger(this.getClass());
	private List<Notification> notifications = new ArrayList<>();
	
	protected GmMetaModel findModel(PersistenceGmSession session, String modelName, boolean required) {
		EntityQuery query = EntityQueryBuilder.from(GmMetaModel.class).where().property("name").eq(modelName).done();
		GmMetaModel model = session.query().entities(query).unique();
		if (model == null && required) {
			throw new NotFoundException("Could not find required model: "+modelName);
		}
		return model;
	}
	
	protected IncrementalAccess findAccess(PersistenceGmSession session, String externalId) {
		EntityQuery query = EntityQueryBuilder.from(IncrementalAccess.class).where().property(IncrementalAccess.externalId).eq(externalId).done();
		return session.query().entities(query).unique();
	}

	protected AccessAspect findAspect(PersistenceGmSession session, String externalId) {
		EntityQuery query = EntityQueryBuilder.from(AccessAspect.class).where().property(AccessAspect.externalId).eq(externalId).done();
		return session.query().entities(query).unique();
	}


	

	
	protected void notifyInfo(String message) {
		notifyInfo(message, null);
	}
	
	protected void notifyInfo(String message, Logger logger) {
		notify(message, Level.INFO, logger);
	}
	
	protected void notifyWarning(String message) {
		notifyWarning(message, null);
	}

	protected void notifyWarning(String message, Logger logger) {
		notify(message, Level.WARNING, logger);
	}

	
	protected void notifyError(String message) {
		notifyError(message, null);
	}
	
	protected void notifyError(String message, Logger logger) {
		notify(message, Level.ERROR, logger);
	}

	protected void notify(String message, Level level, Logger logger) {
		if (logger != null)
			logger.debug(message);
		
		notify(message, level, notifications);
	}

	protected void addNotifications(List<Notification> notifications) {
		this.notifications.addAll(notifications);
	}
	protected List<Notification> getNotifications() {
		return notifications;
	}
	
	protected <T extends HasNotifications> T  createConfirmationResponse(String message, Level level, EntityType<T> responseType) {
		return createResponse(message, level, responseType, true);
	}
	
	protected <T extends HasNotifications> T createResponse(String message, EntityType<T> responseType) {
		return createResponse(message, Level.INFO, responseType);
	}
	protected <T extends HasNotifications> T createResponse(String message, Level level, EntityType<T> responseType) {
		return createResponse(message, level, responseType, false);
	}
	
	protected <T extends HasNotifications> T createResponse(String message, Level level, EntityType<T> responseType, boolean confirmationRequired) {
		T response = responseType.create();

		if (confirmationRequired) {
			response.setNotifications(
					Notifications.build()
						.add()
							.message()
								.confirmationRequired()
								.message(message)
								.level(level)
							.close()
						.close()
					.list()
					);
		} else {
			notify(message, level, logger);
		}
		
		
		Collections.reverse(notifications);
		response.getNotifications().addAll(notifications);
		return response;
		
	}	
	
	public static void notify(String message, Level level, List<Notification> notifications) {
		notifications.addAll(
				Notifications.build()
					.add()
						.message().level(level).message(message).close()
					.close()
				.list());
	}
	
	public static  <T extends HasNotifications> T createResponse(String message, Level level, boolean confirmationRequired, EntityType<T> responseType) {
		return createResponse(message, level, confirmationRequired, responseType, Collections.emptyList());
	}
	public static  <T extends HasNotifications> T createResponse(String message, Level level, boolean confirmationRequired, EntityType<T> responseType, List<Notification> notifications) {
		T response = responseType.create();

		if (confirmationRequired) {
			response.setNotifications(
					Notifications.build()
						.add()
							.message()
								.confirmationRequired()
								.message(message)
								.level(level)
							.close()
						.close()
					.list()
					);
		} else {
			notify(message, level, notifications);
		}
		
		
		Collections.reverse(notifications);
		response.getNotifications().addAll(notifications);
		return response;
		
	}	
}
