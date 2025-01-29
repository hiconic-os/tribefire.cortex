package com.braintribe.web.servlet.auth.aspect;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

public interface AuthHttpResponseConfigurer {

	void applyFor(Object response, Consumer<HttpServletResponse> consumer);

	void applyForAll(Consumer<HttpServletResponse> consumer);

}
