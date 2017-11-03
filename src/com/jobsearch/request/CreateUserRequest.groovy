package com.jobsearch.request

import com.jobsearch.model.Profile

public class CreateUserRequest {
	String firstName
	String lastName
	String emailAddress
	String confirmEmailAddress
	String password
	String confirmPassword
	Integer profileId
}
