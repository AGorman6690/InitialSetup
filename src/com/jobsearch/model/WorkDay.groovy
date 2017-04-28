package com.jobsearch.model
import org.codehaus.jackson.annotate.JsonCreator;
import java.awt.TexturePaintContext.Int;
import java.time.LocalDate

import org.codehaus.jackson.annotate.JsonProperty
import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor

import com.fasterxml.jackson.core.sym.Name;
import com.sun.org.apache.bcel.internal.generic.NEW;;


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
	
	Integer isComplete
	
	WorkDay(){
		this.date = LocalDate.now()
	}
	WorkDay(String stringDate){
		this.setStringDate(stringDate);		
	}
}
