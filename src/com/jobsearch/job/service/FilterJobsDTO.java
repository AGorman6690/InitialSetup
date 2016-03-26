package com.jobsearch.job.service;

import java.sql.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.GeocodingResult;

public class FilterJobsDTO {

	@JsonProperty("jobs")
	List<Job> jobs;
	
	@JsonProperty("fromAddress")
	String fromAddress;
	
	@JsonProperty("distanceFromLat")
	float distanceFromLat;
	
	@JsonProperty("distanceFromLng")
	float distanceFromLng;
	
	@JsonProperty("radius")
	int radius;
	
//	@JsonProperty("startDate")
//	@DateTimeFormat(pattern = "MM/dd/yyyy")
//	DateTime startDate;
//	
//	@JsonProperty("endDate")
//	@DateTimeFormat(pattern = "MM/dd/yyyy")
//	DateTime endDate;
	
	@JsonProperty("categoryIds")
	int[] categoryIds;
	

	public int[] getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(int[] categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@JsonProperty("duration")
	double duration;

//	public DateTime getStartDate() {
//		return startDate;
//	}
//
//	public void setStartDate(DateTime startDate) {
//		this.startDate = startDate;
//	}
//
//	public DateTime getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(DateTime endDate) {
//		this.endDate = endDate;
//	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setDistanceFromLat(float distanceFromLat) {
		this.distanceFromLat = distanceFromLat;
	}

	public void setDistanceFromLng(float distanceFromLng) {
		this.distanceFromLng = distanceFromLng;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public float getDistanceFromLat() {
		return distanceFromLat;
	}

	public void setDistanceFromLat(GeocodingResult geocodingResults[]) {
		this.distanceFromLat = (float) geocodingResults[0].geometry.location.lat;		
	}

	public float getDistanceFromLng() {
		return distanceFromLng;
	}

	public void setDistanceFromLng(GeocodingResult[] geocodingResults) {
		this.distanceFromLng = (float) geocodingResults[0].geometry.location.lng;
	}
	
	
	
	
}
