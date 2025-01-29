package com.braintribe.web.servlet.auth.aspect;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.model.service.api.ServiceRequest;

public interface AuthHttpRequestSupplier {

	Optional<HttpServletRequest> getFor(ServiceRequest request);

	Collection<HttpServletRequest> getAll();
}
