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
package com.braintribe.model.processing.securityservice.basic;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.common.attribute.common.Waypoint;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InternalError;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.security.reason.Forbidden;
import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.gm.model.security.reason.InvalidSession;
import com.braintribe.gm.model.security.reason.SessionExpired;
import com.braintribe.gm.model.security.reason.SessionNotFound;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.securityservice.api.DeletedSessionInfo;
import com.braintribe.model.processing.securityservice.api.UserSessionService;
import com.braintribe.model.processing.securityservice.api.exceptions.SecurityServiceError;
import com.braintribe.model.processing.securityservice.basic.user.UserInternalService;
import com.braintribe.model.processing.securityservice.basic.user.UserInternalServiceImpl;
import com.braintribe.model.processing.securityservice.basic.verification.UserSessionAccessVerificationExpert;
import com.braintribe.model.processing.securityservice.impl.Roles;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.common.context.UserSessionAspect;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.transaction.Transaction;
import com.braintribe.model.security.service.config.OpenUserSessionConfiguration;
import com.braintribe.model.security.service.config.OpenUserSessionEntryPoint;
import com.braintribe.model.securityservice.AuthenticateCredentials;
import com.braintribe.model.securityservice.AuthenticateCredentialsResponse;
import com.braintribe.model.securityservice.AuthenticatedUser;
import com.braintribe.model.securityservice.AuthenticatedUserSession;
import com.braintribe.model.securityservice.GetCurrentUser;
import com.braintribe.model.securityservice.Logout;
import com.braintribe.model.securityservice.LogoutSession;
import com.braintribe.model.securityservice.OpenUserSession;
import com.braintribe.model.securityservice.OpenUserSessionResponse;
import com.braintribe.model.securityservice.SecurityRequest;
import com.braintribe.model.securityservice.ValidateUserSession;
import com.braintribe.model.securityservice.credentials.AbstractUserIdentificationCredentials;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.model.securityservice.credentials.ExistingSessionCredentials;
import com.braintribe.model.securityservice.credentials.TokenWithUserNameCredentials;
import com.braintribe.model.securityservice.credentials.identification.EmailIdentification;
import com.braintribe.model.securityservice.credentials.identification.UserIdentification;
import com.braintribe.model.securityservice.credentials.identification.UserNameIdentification;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.time.TimeSpan;
import com.braintribe.model.user.User;
import com.braintribe.model.user.statistics.UserStatistics;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.utils.lcd.LazyInitialized;

public class SecurityServiceProcessor extends AbstractDispatchingServiceProcessor<SecurityRequest, Object> {

	private static final Logger log = Logger.getLogger(SecurityServiceProcessor.class);

	private UserSessionService userSessionService;
	private Evaluator<ServiceRequest> evaluator;
	private List<UserSessionAccessVerificationExpert> userSessionAccessVerificationExperts;

	private boolean enableUserStatistics;
	private Supplier<PersistenceGmSession> userStatisticsGmSessionProvider;
	private TimeSpan sessionMaxIdleTime;
	private TimeSpan sessionMaxAge;
	private final CredentialsHasher credentialsHasher = new CredentialsHasher();
	private Supplier<PersistenceGmSession> authGmSessionProvider;
	private OpenUserSessionConfiguration openUserSessionConfiguration = OpenUserSessionConfiguration.T.create();
	private LazyInitialized<Map<String, OpenUserSessionEntryPoint>> entryPointsByWaypoint = new LazyInitialized<Map<String,OpenUserSessionEntryPoint>>(this::indexEntryPointsByWaypoint);

	/**
	 * <p>
	 * Sets the Provider of {@link PersistenceGmSession}(s) used by this expert for fetching standard {@link User} information.
	 * 
	 * @param authGmSessionProvider
	 *            The Provider of {@link PersistenceGmSession}(s) used by this expert for fetching standard {@link User} information.
	 */
	@Required
	@Configurable
	public void setAuthGmSessionProvider(Supplier<PersistenceGmSession> authGmSessionProvider) {
		this.authGmSessionProvider = authGmSessionProvider;
	}

	@Required
	@Configurable
	public void setEvaluator(Evaluator<ServiceRequest> evaluator) {
		this.evaluator = evaluator;
	}

	@Required
	@Configurable
	public void setUserSessionService(UserSessionService userSessionService) {
		this.userSessionService = userSessionService;
	}
	
	@Configurable
	public void setOpenUserSessionConfiguration(OpenUserSessionConfiguration openUserSessionConfiguration) {
		this.openUserSessionConfiguration = openUserSessionConfiguration;
	}

	@Configurable
	public void setEnableUserStatistics(boolean enableUserStatistics) {
		this.enableUserStatistics = enableUserStatistics;
	}

	@Configurable
	public void setUserStatisticsGmSessionProvider(Supplier<PersistenceGmSession> userStatisticsGmSessionProvider) {
		this.userStatisticsGmSessionProvider = userStatisticsGmSessionProvider;
	}

	/**
	 * <p>
	 * Sets the max idle time, as {@link TimeSpan}, to be set to the {@link UserSession} objects created by this authentication expert
	 * 
	 * @param sessionMaxIdleTime
	 *            The max idle time to be set to the {@link UserSession} objects created by this authentication expert
	 */
	@Configurable
	public void setSessionMaxIdleTime(TimeSpan sessionMaxIdleTime) {
		this.sessionMaxIdleTime = sessionMaxIdleTime;
	}

	/**
	 * <p>
	 * Sets the max age, as {@link TimeSpan}, to assist the generation of expiry dates for the {@link UserSession} objects created by this
	 * authentication expert
	 * 
	 * @param sessionMaxAge
	 *            Sets the max age to base the expiry dates for the {@link UserSession} objects created by this authentication expert
	 */
	@Configurable
	public void setSessionMaxAge(TimeSpan sessionMaxAge) {
		this.sessionMaxAge = sessionMaxAge;
	}

	@Override
	protected void configureDispatching(DispatchConfiguration<SecurityRequest, Object> dispatching) {
		dispatching.registerReasoned(OpenUserSession.T, this::openUserSession);
		dispatching.registerReasoned(ValidateUserSession.T, this::validateUserSession);
		dispatching.register(GetCurrentUser.T, this::getCurrentUser);
		dispatching.register(Logout.T, this::logout);
		dispatching.register(LogoutSession.T, this::logoutSession);
	}
	
	private Map<String, OpenUserSessionEntryPoint> indexEntryPointsByWaypoint() {
		Map<String, OpenUserSessionEntryPoint> index = new HashMap<>();
		
		for (OpenUserSessionEntryPoint entryPoint: openUserSessionConfiguration.getEntryPoints()) {
			for (String waypoint: entryPoint.getActivationWaypoints()) {
				index.put(waypoint, entryPoint);
			}
		}
		
		return index;
	}

	public UserInternalService getUserInternalService() {
		return new UserInternalServiceImpl(authGmSessionProvider.get());
	}

	private boolean logoutSession(ServiceRequestContext context, LogoutSession request) {
		return logout(request.getSessionId());
	}

	private boolean logout(ServiceRequestContext context, Logout request) {
		return logout(context.getRequestorSessionId());
	}

	private boolean logout(String sessionId) {
		if (sessionId == null)
			return false;

		Maybe<DeletedSessionInfo> deletedUserSessionMaybe = userSessionService.deleteUserSession(sessionId);

		deletedUserSessionMaybe //
				.ifSatisfied(this::collectStatisticsUponLogout) //
				.ifUnsatisfied(r -> log.debug("Could logout session '" + sessionId + "': " + r.stringify()));

		return deletedUserSessionMaybe.isSatisfied();
	}

	private Maybe<OpenUserSessionResponse> openUserSession(ServiceRequestContext requestContext, OpenUserSession openUserSession) {
		Credentials credentials = openUserSession.getCredentials();

		if (credentials == null)
			return Reasons.build(InvalidArgument.T).text("OpenUserSession.credentials must not be null").toMaybe();

		String acquirationKey = null;

		if (credentials.acquirationSupportive()) {
			// The idea behind acquiring is that we don't open a new session but use an existing one in some cases
			// E.g. when the client just always sends the same token (3rd party system)
			// But when the requester address is different, i.e. it is the same user on a different device, we want a new session
			acquirationKey = credentialsHasher.hash(credentials, m -> m.put("requestorAddress", requestContext.getRequestorAddress()));

			Maybe<UserSession> acquiredUserSessionMaybe = acquireUserSession(requestContext, acquirationKey);

			if (acquiredUserSessionMaybe.isSatisfied()) {
				return acquiredUserSessionMaybe.map(us -> createResponseFrom(us, true));
			}

			// TODO: rethink the responsibility for UserSession transcription and therefore the responsibility of acquiration
			// blocking
			if (acquiredUserSessionMaybe.isUnsatisfiedBy(InvalidCredentials.T)) {
				// In this case the credentials are blocked via the acquiration mechanism and a reauthentication is not possible
				return acquiredUserSessionMaybe.whyUnsatisfied().asMaybe();
			} else if (!acquiredUserSessionMaybe.isUnsatisfiedBy(SessionNotFound.T)) {
				log.debug("Error while finding session via acquiration key: " + acquiredUserSessionMaybe.whyUnsatisfied().stringify());
			}
		}

		AuthenticateCredentials authenticateCredentials = AuthenticateCredentials.T.create();
		authenticateCredentials.setProperties(openUserSession.getProperties());
		authenticateCredentials.setCredentials(openUserSession.getCredentials());

		Maybe<? extends AuthenticateCredentialsResponse> maybe = authenticateCredentials.eval(evaluator).getReasoned();

		if (maybe.isUnsatisfied())
			return Maybe.empty(maybe.whyUnsatisfied());
		
		AuthenticateCredentialsResponse authenticatedCredentialsResponse = maybe.get();
		
		if (authenticatedCredentialsResponse instanceof AuthenticatedUserSession authenticatedUserSession) {
			return Maybe.complete(createResponseFrom(authenticatedUserSession.getUserSession(), true));
		}
		
		Reason authorizationFailure = checkAuthorization(requestContext, authenticatedCredentialsResponse);
		
		if (authorizationFailure != null)
			return authorizationFailure.asMaybe();

		return buildUserSession(requestContext, openUserSession, authenticatedCredentialsResponse, acquirationKey) //
				.map(us -> createResponseFrom(us, false));
	}

	private Reason checkAuthorization(ServiceRequestContext requestContext, AuthenticateCredentialsResponse authenticatedCredentialsResponse) {
		Set<String> effectiveRoles = getEffectiveRoles(authenticatedCredentialsResponse);
		
		if (hasRequiredRoles(requestContext, effectiveRoles))
			return null;
		
		
		return Reasons.build(Forbidden.T).text("Insufficient rights to to open a UserSession this way").toReason();
	}

	private boolean hasRequiredRoles(ServiceRequestContext requestContext, Set<String> effectiveRoles) {
		String waypoint = requestContext.findOrNull(Waypoint.class);
		
		if (waypoint == null)
			return true;
		
		OpenUserSessionEntryPoint entryPoint = entryPointsByWaypoint.get().get(waypoint);
		
		if (entryPoint == null)
			return true;
		
		return checkAuthorization(effectiveRoles, entryPoint.getAllowedRoles(), entryPoint.getForbiddenRoles());
	}
	
	/**
     * Checks whether a user with the given roles is authorized,
     * based on an optional whitelist (includedRoles) and blacklist (excludedRoles).
     *
     * @param effectiveRoles The roles currently assigned to the user
     * @param allowedRoles The roles from which at least one must be present (if not empty)
     * @param forbiddenRoles The roles from which none must be present (if not empty)
     * @return true if authorized; false if access should be denied
     */
    public static boolean checkAuthorization(Set<String> effectiveRoles, Set<String> allowedRoles, Set<String> forbiddenRoles) {
        // Check blacklist (if present)
        if (!forbiddenRoles.isEmpty()) {
            for (String role : effectiveRoles) {
                if (forbiddenRoles.contains(role)) {
                    return false; // Access denied
                }
            }
        }

        // Check whitelist (if present)
        if (!allowedRoles.isEmpty()) {
            for (String role : effectiveRoles) {
                if (allowedRoles.contains(role)) {
                    return true; // Access granted
                }
            }
            return false; // No matching allowed role found
        }

        // No exclusions matched, no inclusion rules defined → access granted
        return true;
    }

	private Set<String> getEffectiveRoles(AuthenticateCredentialsResponse authenticatedCredentialsResponse) {
		if (authenticatedCredentialsResponse instanceof AuthenticatedUser) {
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) authenticatedCredentialsResponse;
			return Roles.userEffectiveRoles(authenticatedUser.getUser());
		} else if (authenticatedCredentialsResponse instanceof AuthenticatedUserSession) {
			AuthenticatedUserSession authenticatedUserSession = (AuthenticatedUserSession) authenticatedCredentialsResponse;
			return authenticatedUserSession.getUserSession().getEffectiveRoles();
		} else {
			return Collections.emptySet();
		}
	}

	private Maybe<UserSession> acquireUserSession(ServiceRequestContext requestContext, String acquirationKey) {
		return userSessionService.findUserSessionByAcquirationKey(acquirationKey) //
				.flatMap(s -> validateUserSession(requestContext, s)) //
				.ifSatisfied(this::touchUserSession);
	}

	private OpenUserSessionResponse createResponseFrom(UserSession userSession, boolean reused) {
		OpenUserSessionResponse response = OpenUserSessionResponse.T.create();
		response.setUserSession(userSession);
		response.setReused(reused);
		return response;
	}

	private Maybe<UserSession> buildUserSession(ServiceRequestContext context, OpenUserSession openUserSession,
			AuthenticateCredentialsResponse authenticatedCredentialsResponse, String acquirationKey) {
		if (authenticatedCredentialsResponse instanceof AuthenticatedUser) {
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) authenticatedCredentialsResponse;

			log.trace(() -> "Creating session for client from IP: " + context.getRequestorAddress());

			User user = authenticatedUser.getUser();

			if (authenticatedUser.getEnsureUserPersistence()) {
				Reason error = getUserInternalService().ensureUser(user);
				String uuid = UUID.randomUUID().toString();
				String msg = "Error while ensuring User persistence (tracebackId=" + uuid + ")";
				if (error != null) {
					log.error(msg + ": " + error.stringify());
					return Reasons.build(InternalError.T).text(msg).toMaybe();
				}
			}

			// TODO: should the expiry date influenced from the outside via OpenUserSession.expiryDate or should this only be
			// controlled by Credentials/Authentication
			//@formatter:off
			Maybe<UserSession> userSessionMaybe = new BasicUserSessionBuilder(userSessionService, sessionMaxIdleTime, sessionMaxAge)
				.requestContext(context)
				.request(openUserSession)
				.acquirationKey(acquirationKey)
				.expiryDate(authenticatedUser.getExpiryDate())
				.addProperties(authenticatedUser.getProperties())
				.blocksAuthenticationAfterLogout(authenticatedUser.getInvalidateCredentialsOnLogout())
				.buildFor(user);
			//@formatter:on

			boolean satisfied = userSessionMaybe.isSatisfied();

			this.logAuthentication(context, openUserSession, satisfied);

			if (!satisfied)
				return Maybe.empty(userSessionMaybe.whyUnsatisfied());

			collectStatisticsUponLogin(userSessionMaybe.get());

			return userSessionMaybe;

		} else {
			return Reasons.build(InternalError.T)
					.text("Unsupported AuthenticateCredentialsResponse type: " + authenticatedCredentialsResponse.entityType().getTypeSignature())
					.toMaybe();
		}
	}

	protected void logAuthentication(ServiceRequestContext requestContext, OpenUserSession openUserSession, boolean successful) {
		if (!log.isDebugEnabled()) {
			return;
		}

		try {
			Credentials credentials = openUserSession.getCredentials();

			String type = null;
			String userId = null;
			if (credentials instanceof AbstractUserIdentificationCredentials) {
				type = "user";
				AbstractUserIdentificationCredentials upc = (AbstractUserIdentificationCredentials) credentials;
				UserIdentification userIdentification = upc.getUserIdentification();
				if (userIdentification instanceof UserNameIdentification) {
					userId = ((UserNameIdentification) userIdentification).getUserName();
				} else if (userIdentification instanceof EmailIdentification) {
					userId = ((EmailIdentification) userIdentification).getEmail();
				}
			} else if (credentials instanceof TokenWithUserNameCredentials) {
				type = "user";
				TokenWithUserNameCredentials tuc = (TokenWithUserNameCredentials) credentials;
				userId = tuc.getUserName();
			} else if (credentials instanceof ExistingSessionCredentials) {
				if (successful) {
					// Well, a session has been verified. No point in logging that.
					return;
				}
				type = "session";
				ExistingSessionCredentials esc = (ExistingSessionCredentials) credentials;
				userId = esc.getExistingSessionId();
			} else {
				log.trace(() -> "Not logging authentication of credentials " + credentials + " with success: " + successful);
				return;
			}

			if (userId != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("Authentication of ");
				sb.append(type);
				sb.append(" '");
				sb.append(userId);
				sb.append("' from '");

				String address = requestContext != null ? requestContext.getRequestorAddress() : null;
				sb.append(address);
				sb.append("' ");

				if (successful) {
					sb.append("succeeded");
				} else {
					sb.append("failed");
				}
				log.debug(sb.toString());
			}

		} catch (Exception e) {
			log.debug(() -> "Could not log an authentication attempt", e);
		}
	}

	private Maybe<UserSession> validateUserSession(ServiceRequestContext requestContext, ValidateUserSession request) {
		Maybe<UserSession> userSessionMaybe = userSessionService.findUserSession(request.getSessionId());

		return userSessionMaybe //
				.flatMap(s -> validateUserSession(requestContext, s)) //
				.ifSatisfied(this::touchUserSession);
	}

	private void touchUserSession(UserSession userSession) {
		Date lastAccessedDate = new Date();
		Date fixedExpiryDate = userSession.getFixedExpiryDate();

		TimeSpan maxIdleTime = userSession.getMaxIdleTime();

		Date expiryDate = calculateExpiryDate(lastAccessedDate, maxIdleTime, fixedExpiryDate);

		String sessionId = userSession.getSessionId();
		Thread.ofVirtual().name("Touch session").start(() -> userSessionService.touchUserSession(sessionId, lastAccessedDate, expiryDate));
	}

	protected Date calculateExpiryDate(Date pivot, TimeSpan span, Date fixedExpiryDate) {
		if (span == null) {
			return fixedExpiryDate;
		}
		Date expiryDate = new Date(pivot.getTime() + span.toLongMillies());

		if (fixedExpiryDate != null && fixedExpiryDate.before(expiryDate))
			return fixedExpiryDate;

		return expiryDate;
	}

	private Maybe<UserSession> validateUserSession(ServiceRequestContext requestContext, UserSession userSession) {
		log.trace(() -> "Validating user session: " + userSession);

		Date expiryDate = userSession.getExpiryDate();

		if (expiryDate != null) {
			Date now = new Date();

			if (now.after(expiryDate)) {
				return Reasons.build(SessionExpired.T).text("User session '" + userSession.getId() + "' has expired.").toMaybe();
			}
		}

		LazyInitialized<Reason> verifyReason = new LazyInitialized<>(
				() -> Reasons.build(InvalidSession.T).text("User session '" + userSession.getId() + "' is invalid.").toReason());

		if (userSessionAccessVerificationExperts != null && !userSessionAccessVerificationExperts.isEmpty()) {
			for (UserSessionAccessVerificationExpert expert : userSessionAccessVerificationExperts) {
				Reason reason = expert.verifyUserSessionAccess(requestContext, userSession);

				if (reason != null)
					verifyReason.get().getReasons().add(reason);
			}
		}

		if (verifyReason.isInitialized()) {
			log.debug(verifyReason.get().stringify());
			return Reasons.build(InvalidSession.T).text("User session '" + userSession.getId() + "' is invalid.").toMaybe();
		}

		return Maybe.complete(userSession);
	}

	private User getCurrentUser(ServiceRequestContext requestContext, GetCurrentUser request) {
		return requestContext.findAttribute(UserSessionAspect.class).map(UserSession::getUser).orElse(null);
	}

	private void collectStatisticsUponLogin(UserSession userSession) {
		if (!enableUserStatistics)
			return;

		try {
			PersistenceGmSession gmSession = getUserStatisticsGmSession();

			UserStatistics userStatistics = findUserStatistics(userSession.getUser().getName(), gmSession);
			if (userStatistics == null) {
				// This is an optimistic approach. We prefer not to use a DB-based locking
				// as this would be too much overhead. We do a local locking and if another
				// thread has created such a UserStatistics entity in the meantime, we will
				// re-try the query
				try {
					userStatistics = gmSession.create(UserStatistics.T);
					userStatistics.setId(userSession.getUser().getName());
					userStatistics.setFirstLoginDate(userSession.getCreationDate());
					userStatistics.setSessionsOpened(0);
					userStatistics.setLoggedInTime(0L);
					gmSession.commit();
				} catch (Exception e) {
					rollback(gmSession);
					// Session is tainted, creating a new one
					gmSession = getUserStatisticsGmSession();

					userStatistics = findUserStatistics(userSession.getUser().getName(), gmSession);
					if (userStatistics == null) {
						throw new SecurityServiceError("Could not create a UserStatistics entity for user '" + userSession.getUser().getName()
								+ "' but also did not find an existing one", e);
					}
				}
			}
			Integer sessionsOpened = userStatistics.getSessionsOpened();
			if (sessionsOpened == null) {
				sessionsOpened = 0;
			}
			userStatistics.setSessionsOpened(sessionsOpened + 1);
			Date creationDate = userSession.getCreationDate();
			if (creationDate == null) {
				creationDate = new Date();
			}
			userStatistics.setLastLoginDate(creationDate);

			gmSession.commit();
		} catch (Exception e) {
			log.error(() -> "Collecting user statistics failed upon login", e);
		}
	}

	private void collectStatisticsUponLogout(DeletedSessionInfo deletedSessionInfo) {
		if (!enableUserStatistics)
			return;

		UserSession userSession = deletedSessionInfo.userSession();
		try {
			PersistenceGmSession gmSession = getUserStatisticsGmSession();
			UserStatistics userStatistics = findUserStatistics(userSession.getUser().getName(), gmSession);
			if (userStatistics == null) {
				return;
			}

			long sessionDuration = userSession.getLastAccessedDate().getTime() - userSession.getCreationDate().getTime();
			userStatistics.setLoggedInTime(userStatistics.getLoggedInTime() + sessionDuration);

			gmSession.commit();
		} catch (Exception e) {
			log.error("Collecting user statistics failed upon logout of user session '" + userSession.getSessionId() + "'", e);
		}
	}

	private UserStatistics findUserStatistics(String username, PersistenceGmSession gmSession) {
		return gmSession.query().entity(UserStatistics.T, username).find();
	}

	private PersistenceGmSession getUserStatisticsGmSession() {
		PersistenceGmSession gmSession;
		try {
			gmSession = userStatisticsGmSessionProvider.get();
		} catch (Exception e) {
			throw new SecurityServiceError("Failed to obtain a gm session for accessing the user statistics", e);
		}
		if (gmSession == null) {
			throw new SecurityServiceError("Failed to obtain a gm session for accessing the user statistics");
		}
		return gmSession;
	}

	private void rollback(PersistenceGmSession userStatisticsGmSession) {
		try {
			Transaction t = userStatisticsGmSession.getTransaction();
			t.undo(t.getManipulationsDone().size());
		} catch (Exception e) {
			throw new SecurityServiceError("Failed to rollback the user statistics gm session", e);
		}
	}

	@Configurable
	public void setUserSessionAccessVerificationExperts(List<UserSessionAccessVerificationExpert> userSessionAccessVerificationExperts) {
		this.userSessionAccessVerificationExperts = userSessionAccessVerificationExperts;
	}

}
