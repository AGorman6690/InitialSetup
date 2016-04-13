package com.jobsearch.user.service;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailabilityDTO {
	
	@JsonProperty
	private int userId;
	
	@JsonProperty
	private String[] stringDays;
	
	@JsonProperty
	private List<Date> days;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String[] getStringDays() {
		return stringDays;
	}
	public void setStringDays(String[] stringDays) {
		this.stringDays = stringDays;
	}
	public List<Date> getDays() {
		return days;
	}
	public void setDays(List<Date> days) {
		this.days = days;
	}
	
}
