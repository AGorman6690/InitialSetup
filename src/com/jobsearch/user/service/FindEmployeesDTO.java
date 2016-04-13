package com.jobsearch.user.service;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FindEmployeesDTO {
	
	@JsonProperty
	private List<Date> availableDates;
	
	@JsonProperty
	private List<String> stringAvailableDates;
	
	

	public List<String> getStringAvailableDates() {
		return stringAvailableDates;
	}

	public void setStringAvailableDates(List<String> stringAvailableDates) {
		this.stringAvailableDates = stringAvailableDates;
	}

	public List<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}
	

}
