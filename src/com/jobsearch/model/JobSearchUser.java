package com.jobsearch.model;

import java.util.ArrayList;
import java.util.List;

public class JobSearchUser {

	private int userId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private List<Category> categories;
	private Profile profile;
	private int profileId;
	
	
	
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
