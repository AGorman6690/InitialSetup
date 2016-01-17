package com.jobsearch.model;

import java.util.ArrayList;
import java.util.List;


public class JobSearchUser {

	private int userId;
	private int profileId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private Job selectedJob;
	private Profile profile;
	private List<Category> categories;	
	private ArrayList<Job> jobs;
	private ArrayList<Job> activeJobs;
	private ArrayList<Job> appliedToJobs;
	private ArrayList<Job> employment;

	public ArrayList<Job> getEmployment() {
		return employment;
	}

	public void setEmployment(ArrayList<Job> employment) {
		this.employment = employment;
	}

	public ArrayList<Job> getAppliedToJobs() {
		return appliedToJobs;
	}

	public void setAppliedToJobs(ArrayList<Job> appliedToJobs) {
		this.appliedToJobs = appliedToJobs;
	}

	public Job getSelectedJob() {
		return selectedJob;
	}

	public void setSelectedJob(Job selectedJob) {
		this.selectedJob = selectedJob;
	}

	public ArrayList<Job> getActiveJobs() {
		return activeJobs;
	}

	public void setActiveJobs(ArrayList<Job> activeJobs) {
		this.activeJobs = activeJobs;
	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
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

}