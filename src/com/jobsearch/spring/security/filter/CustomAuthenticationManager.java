package com.jobsearch.spring.security.filter;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationManager implements AuthenticationProvider {
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		// all the funky AD+DB code
		return null;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return true;
	}
}
