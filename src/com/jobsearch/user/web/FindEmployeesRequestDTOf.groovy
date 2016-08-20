package com.jobsearch.user.web

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class FindEmployeesRequestDTOf {
	@JsonProperty
	List<Date> availableDates

	@JsonProperty
	List<String> stringAvailableDates

	@JsonProperty
	String city

	@JsonProperty
	String state

	@JsonProperty
	String zipCode

	@JsonProperty
	Float lng

	@JsonProperty
	Float lat

	@JsonProperty
	int radius

	@JsonProperty
	List<Integer> categoryIds
}
