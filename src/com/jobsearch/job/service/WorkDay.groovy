package com.jobsearch.job.service

import java.sql.Date;
import java.time.LocalDate

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class WorkDay {
	
	@JsonProperty("millisecondsDate")
	String millisecondsDate

//	@JsonProperty("date")
//	Date date
	
	@JsonProperty("date")
	LocalDate date
	
	@JsonProperty("dateId")
	int dateId
	
	@JsonProperty("stringDate")
	String stringDate

	@JsonProperty("stringStartTime")
	String stringStartTime

	@JsonProperty("stringEndTime")
	String stringEndTime
}
