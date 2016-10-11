package com.jobsearch.job.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jobsearch.model.PostQuestionDTO;

public class PostJobDTO {

	@JsonProperty("jobName")
	String jobName;

	@JsonProperty("userId")
	int userId;

	@JsonProperty("categoryIds")
	List<Integer> categoryIds;

	@JsonProperty("openings")
	int openings;

	@JsonProperty("description")
	String description;

	@JsonProperty("streetAddress")
	String streetAddress;

	@JsonProperty("city")
	String city;

	@JsonProperty("state")
	String state;

	@JsonProperty("zipCode")
	String zipCode;

	@JsonProperty("lat")
	float lat;

	@JsonProperty("lng")
	float lng;

//	@JsonProperty("startDate")
//	Date startDate;
//
//	@JsonProperty("endDate")
//	Date endDate;
//
//	@JsonProperty("stringStartDate")
//	String stringStartDate;
//
//	@JsonProperty("stringEndDate")
//	String stringEndDate;
//
//	@JsonProperty("startTime")
//	Time startTime;
//
//	@JsonProperty("endTime")
//	Time endTime;
//
//	@JsonProperty("stringStartTime")
//	String stringStartTime;
//
//	@JsonProperty("stringEndTime")
//	String stringEndTime;

	@JsonProperty("selectedQuestionIds")
	List<Integer> selectedQuestionIds;
	
	@JsonProperty("questions")
	List<PostQuestionDTO> questions;
	
	@JsonProperty("workDays")
	List<WorkDay> workDays;
	
	

	public List<WorkDay> getWorkDays() {
		return workDays;
	}

	public void setWorkDays(List<WorkDay> workDays) {
		this.workDays = workDays;
	}

	public List<PostQuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<PostQuestionDTO> questions) {
		this.questions = questions;
	}

	@JsonProperty("id")
	String id;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
		this.openings = openings;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

//	public Date getStartDate() {
//		return startDate;
//	}
//
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}
//
//	public Date getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//
//	public String getStringStartDate() {
//		return stringStartDate;
//	}
//
//	public void setStringStartDate(String stringStartDate) {
//		this.stringStartDate = stringStartDate;
//	}
//
//	public String getStringEndDate() {
//		return stringEndDate;
//	}
//
//	public void setStringEndDate(String stringEndDate) {
//		this.stringEndDate = stringEndDate;
//	}
//
//	public Time getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(Time startTime) {
//		this.startTime = startTime;
//	}
//
//	public Time getEndTime() {
//		return endTime;
//	}
//
//	public void setEndTime(Time endTime) {
//		this.endTime = endTime;
//	}
//
//	public String getStringStartTime() {
//		return stringStartTime;
//	}
//
//	public void setStringStartTime(String stringStartTime) {
//		this.stringStartTime = stringStartTime;
//	}
//
//	public String getStringEndTime() {
//		return stringEndTime;
//	}
//
//	public void setStringEndTime(String stringEndTime) {
//		this.stringEndTime = stringEndTime;
//	}

	public List<Integer> getSelectedQuestionIds() {
		return selectedQuestionIds;
	}

	public void setSelectedQuestionIds(List<Integer> selectedQuestionIds) {
		this.selectedQuestionIds = selectedQuestionIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
