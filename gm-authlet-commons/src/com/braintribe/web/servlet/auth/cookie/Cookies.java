package com.braintribe.web.servlet.auth.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface Cookies {
	static Cookie findCookie(HttpServletRequest request, String name) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

}
