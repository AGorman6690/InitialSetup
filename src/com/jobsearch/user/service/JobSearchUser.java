package com.jobsearch.user.service;

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.application.service.Application;
import com.jobsearch.category.service.Category;
import com.jobsearch.job.service.CompletedJobDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.Answer;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;


public class JobSearchUser {

	private int userId;
	private int profileId;
	private String firstName;
	private String lastName;
	private String username;
	private String emailAddress;
	private String password;
	private String matchingPassword;
	private Profile profile;
	private List<Category> categories;	
	private List<Job> jobs;
	private List<Job> activeJobs;
	private List<Job> jobsAppliedTo;
	private List<Job> jobsHiredFor;
	private List<CompletedJobDTO> completedJobs;
	private List<RateCriterion> ratings;
	private List<Endorsement> endorsements;
	private Application application;
	private double rating;
	private List<Answer> answers;
	private List<String> availableDates;
	private float homeLat;
	private float homeLng;
	private String homeCity;
	private String homeState;
	private String homeZipCode;
	private int maxWorkRadius; //Units = miles
	private double distanceFromJob;
		
	public double getDistanceFromJob() {
		return distanceFromJob;
	}

	public void setDistanceFromJob(double distanceFromJob) {
		this.distanceFromJob = distanceFromJob;
	}

	public float getHomeLat() {
		return homeLat;
	}

	public void setHomeLat(float homeLat) {
		this.homeLat = homeLat;
	}

	public float getHomeLng() {
		return homeLng;
	}

	public void setHomeLng(float homeLng) {
		this.homeLng = homeLng;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getHomeState() {
		return homeState;
	}

	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}

	public String getHomeZipCode() {
		return homeZipCode;
	}

	public void setHomeZipCode(String homeZipCode) {
		this.homeZipCode = homeZipCode;
	}

	public int getMaxWorkRadius() {
		return maxWorkRadius;
	}

	public void setMaxWorkRadius(int maxWorkRadius) {
		this.maxWorkRadius = maxWorkRadius;
	}

	public List<Job> getJobsHiredFor() {
		return jobsHiredFor;
	}

	public void setJobsHiredFor(List<Job> jobsHiredFor) {
		this.jobsHiredFor = jobsHiredFor;
	}

	public List<String> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<String> availableDates) {
		this.availableDates = availableDates;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<CompletedJobDTO> getCompletedJobs() {
		return completedJobs;
	}

	public void setCompletedJobs(List<CompletedJobDTO> completedJobs) {
		this.completedJobs = completedJobs;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public List<Endorsement> getEndorsements() {
		return endorsements;
	}

	public void setEndorsements(List<Endorsement> endorsements) {
		this.endorsements = endorsements;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<RateCriterion> getRatings() {
		return ratings;
	}

	public void setRatings(List<RateCriterion> ratings) {
		this.ratings = ratings;
	}

	public List<Job> getJobsAppliedTo() {
		return jobsAppliedTo;
	}

	public void setJobsAppliedTo(List<Job> jobsAppliedTo) {
		this.jobsAppliedTo = jobsAppliedTo;
	}

	public List<Job> getActiveJobs() {
		return activeJobs;
	}

	public void setActiveJobs(List<Job> activeJobs) {
		this.activeJobs = activeJobs;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = (ArrayList<Job>) jobs;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public void addCategory(Category category){
		
		if(this.categories == null){
			this.categories = new ArrayList<Category>();
		}
		this.categories.add(category);			
	}
	
	public void setCategories(List<Category> categories){
		this.categories = categories;
	}
	
	public List<Category> getCategories(){
		return this.categories;		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Object getProfileIds() {
		// TODO Auto-generated method stub
		return null;
	}

}
