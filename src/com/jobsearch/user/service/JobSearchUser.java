package com.jobsearch.user.service;

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.application.service.Application;
import com.jobsearch.category.service.Category;
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
	private ArrayList<Job> jobs;
	private ArrayList<Job> activeJobs;
	private ArrayList<Job> appliedToJobs;
	private List<Job> completedJobs;
	private List<RateCriterion> ratings;
	private List<Endorsement> endorsements;
	private Application application;
	private double rating;
	private List<Answer> answers;
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<Job> getCompletedJobs() {
		return completedJobs;
	}

	public void setCompletedJobs(List<Job> completedJobs) {
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

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}

	public ArrayList<Job> getAppliedToJobs() {
		return appliedToJobs;
	}

	public void setAppliedToJobs(ArrayList<Job> appliedToJobs) {
		this.appliedToJobs = appliedToJobs;
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
