package com.braintribe.web.servlet.auth;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.model.security.service.config.OpenUserSessionEntryPoint;

public interface OpenUserSessionConfigurationProvider {

	String findEntryPointName(HttpServletRequest request);

	OpenUserSessionEntryPoint findEntryPoint(HttpServletRequest request);

	String getCookieName(HttpServletRequest request);

}