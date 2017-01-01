package com.jobsearch.job.service

import java.sql.Date

import com.fasterxml.jackson.annotation.JsonProperty

class WorkDay {
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
