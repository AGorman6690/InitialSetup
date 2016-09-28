package com.jobsearch.job.service;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class WorkDay {
	
	@JsonProperty("date")
	Date date;
	
	@JsonProperty("stringDate")
	String stringDate;
	
	@JsonProperty("stringStartTime")
	String stringStartTime;
	
	@JsonProperty("stringENdTime")
	String stringEndTime;

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
