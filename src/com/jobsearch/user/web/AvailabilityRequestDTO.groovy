package com.jobsearch.user.web

import java.sql.Date
import java.util.List

import com.fasterxml.jackson.annotation.JsonProperty

public class AvailabilityRequestDTO {
	@JsonProperty
	int userId

	@JsonProperty
	String[] stringDays

	@JsonProperty
	List<Date> days
}
