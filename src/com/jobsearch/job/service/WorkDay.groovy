package com.jobsearch.job.service

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class WorkDay {
	
	@JsonProperty("millisecondsDate")
	String millisecondsDate

	@JsonProperty("date")
	Date date

	@JsonProperty("stringDate")
	String stringDate

	@JsonProperty("stringStartTime")
	String stringStartTime

	@JsonProperty("stringEndTime")
	String stringEndTime
}
