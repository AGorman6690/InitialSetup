package com.jobsearch.job.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.GeocodingResult;

public class FilterJobsDTO {

	@JsonProperty("jobs")
	List<Job> jobs;
	
	@JsonProperty("distanceFromLat")
	float distanceFromLat;
	
	@JsonProperty("distanceFromLng")
	float distanceFromLng;
	
	@JsonProperty("radius")
	int radius;
	
	

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
