package com.jobsearch.model
import java.sql.Timestamp;
import java.time.LocalDate

import org.codehaus.jackson.annotate.JsonProperty


public class WorkDay {
	
	int workDayId 
	
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
	
	Timestamp timestamp_endDate
	
	Integer isComplete
	
	WorkDay(){
		this.date = LocalDate.now()
	}
	WorkDay(String stringDate){
		this.setStringDate(stringDate);		
	}
}
