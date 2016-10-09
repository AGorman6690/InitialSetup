package com.jobsearch.job.service;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonIgnoreProperties({ "millisecondsDate" })
public class WorkDay {
	
	@JsonIgnore public String millisecondsDate;
	
	@JsonProperty("date")
	Date date;
	
	@JsonProperty("stringDate")
	String stringDate;
	
	@JsonProperty("stringStartTime")
	String stringStartTime;
	
	@JsonProperty("stringEndTime")
	String stringEndTime;
	
//	@JsonProperty("millisecondsDate")
//	private int millisecondsDate;
//
	public String getMillisecondsDate() {
		return millisecondsDate;
	}

	public void setMillisecondsDate(String millisecondsDate) {
		this.millisecondsDate = millisecondsDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStringDate() {
		return stringDate;
	}

	public void setStringDate(String stringDate) {
		this.stringDate = stringDate;
	}

	public String getStringStartTime() {
		return stringStartTime;
	}

	public void setStringStartTime(String stringStartTime) {
		this.stringStartTime = stringStartTime;
	}

	public String getStringEndTime() {
		return stringEndTime;
	}

	public void setStringEndTime(String stringEndTime) {
		this.stringEndTime = stringEndTime;
	}
	
	

}
