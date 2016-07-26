package com.jobsearch.job.service;

import java.util.List;

public class FilterJobResponseDTO {
	
	public String html;
	public float requestedLat;
	public float requestedLng;
	public int requestedRadius;
	public List<Job> jobs;
		
	public float getRequestedLat() {
		return requestedLat;
	}
	public void setRequestedLat(float requestedLat) {
		this.requestedLat = requestedLat;
	}
	public float getRequestedLng() {
		return requestedLng;
	}
	public void setRequestedLng(float requestedLng) {
		this.requestedLng = requestedLng;
	}
	public int getRequestedRadius() {
		return requestedRadius;
	}
	public void setRequestedRadius(int requestedRadius) {
		this.requestedRadius = requestedRadius;
	}
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}

}
