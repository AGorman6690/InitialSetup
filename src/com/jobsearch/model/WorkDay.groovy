package com.jobsearch.model

import java.awt.TexturePaintContext.Int;
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
	
	WorkDay(){
		
	}
	WorkDay(String stringDate){
		this.setStringDate(stringDate);
	}
}
