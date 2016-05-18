package com.jobsearch.job.service;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.jobsearch.application.service.Application;
import com.jobsearch.category.service.Category;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;

public class Job {

	private int id;
	private int isActive;
	private int userId;
	private int openings;
	private String jobName;
	private String description;
	private Category category;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private float lat;
	private float lng;
	private Date startDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	private String stringStartTime;
	private String stringEndTime;
	private Double distanceFromFilterLocation;
	private List<Integer> categoryIds;
	private List<Category> categories;
	private List<JobSearchUser> employees;
	private List<JobSearchUser> applicants;
	private List<Application> applications;
	private List<Question> questions;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
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

	public void setDistanceFromFilterLocation(Double distanceFromFilterLocation) {
		this.distanceFromFilterLocation = distanceFromFilterLocation;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
		this.openings = openings;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public List<JobSearchUser> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<JobSearchUser> list) {
		this.applicants = list;
	}

	public List<JobSearchUser> getEmployees() {
		return employees;
	}

	public void setEmployees(List<JobSearchUser> employees) {
		this.employees = employees;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	// public List<Category> getCategories() {
	// return categories;
	// }
	// public void setCategories(List<Category> list) {
	// this.categories = list;
	// }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Double getDistanceFromFilterLocation() {
		return distanceFromFilterLocation;
	}

	public void setDistanceFromFilterLocation(double distanceFromFilterLocation) {
		this.distanceFromFilterLocation = distanceFromFilterLocation;
	}

	// @Override
	// public int compareTo(Job another) {
	// // TODO Auto-generated method stub
	// if
	// (this.getDistanceFromFilterLocation()<another.getDistanceFromFilterLocation()){
	// return -1;
	// }else{
	// return 1;
	// }
	// }

}
