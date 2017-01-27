package com.jobsearch.user.web

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class AvailabilityDTO {
	int userId

	@JsonProperty
	List<String> stringDays

//	@JsonProperty
//	List<Date> days
}
