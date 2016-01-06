package com.jobsearch.model;

import java.util.ArrayList;
import java.util.List;

public class App {
	private List<JobSearchUser> users;
	private List<Category> categories;
	private JobSearchUser user;
	private ArrayList<Profile> profiles;
	
	public void setUser(JobSearchUser user){
		this.user = user;
	}
	
	public JobSearchUser getUser(){
		return this.user;
	}
	
	public void setUsers(List<JobSearchUser> users){
		this.users = users;
	}
	
	public ArrayList<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(ArrayList<Profile> profiles) {
		this.profiles = profiles;
	}

	public List<JobSearchUser> getUsers(){
		return this.users;
	}
	
	public void setCategories(List<Category> categories){
		this.categories = categories;
	}
	
	public List<Category> getCategories(){
		return this.categories;
	}
	
	public void addCategory(Category category){
		this.categories.add(category);
	}

	public Category getCategoryByName(String strCat) {

		for(Category cat: this.categories){ 
			if(cat.getName().matches(strCat)){
				return cat;
			}
		}
		
		return null;
	}
	
	public Category getCategoryById(int strId) {

		for(Category cat: this.categories){ 
			if(cat.getId() == strId){
				return cat;
			}
		}
		
		return null;
	}

	public Profile getProfileById(int profileId) {
		for(Profile profile : this.profiles){
			if(profile.getId() == profileId){
				return profile;
			}
		}
		
		return null;
	}
	
}
