package io.shyftlabs.srms.rest.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import io.shyftlabs.srms.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

public abstract class BaseController {
	@Autowired
	protected HttpServletRequest request;

	private boolean initialized = false;

	private String expectedToken = null;

	private static final Pattern AUTHORZ_HEADER_FORMAT = Pattern.compile("Bearer (.*)");

	private static final String TOKEN_ENV_VAR = "SRMS_TOKEN";


	protected void checkToken() {
		String authorzHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorzHeader == null) {
			throw new UnauthorizedException("Missing credentials");
		}

		Matcher matcher = AUTHORZ_HEADER_FORMAT.matcher(authorzHeader);
		if (!matcher.matches()) {
			throw new UnauthorizedException("Malformed credentials");
		}

		String userToken = matcher.group(1);
		if (!userToken.equals(getExpectedToken())) {
			throw new UnauthorizedException("Invalid credentials");
		}
	}

	private String getExpectedToken() {
		if (!initialized) {
			expectedToken = System.getenv(TOKEN_ENV_VAR);
			initialized = true;
		}
		return expectedToken;
	}

	protected void setExpectedToken(String token) {
		expectedToken = token;
		initialized = true;
	}
}
