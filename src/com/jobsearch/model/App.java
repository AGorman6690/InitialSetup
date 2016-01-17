package com.jobsearch.model;

import java.util.ArrayList;
import java.util.List;

public class App {
	private List<JobSearchUser> users;
	private List<Category> categories;
	private JobSearchUser user;
	private Job selectedJob;
	private JobSearchUser selectedUser;
	private Category selectedCategory;
	private ArrayList<Profile> profiles;
	private ArrayList<JobSearchUser> usersBySelectedCat;
	//private ArrayList<Category> categoriesBySelectedJob;
//	private ArrayList<Job> jobsBySelectedUser;
//	private ArrayList<Job> jobsBySelectedCat;
	
	public Category getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public JobSearchUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(JobSearchUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Job getSelectedJob() {
		return selectedJob;
	}

	public void setSelectedJob(Job selectedJob) {
		this.selectedJob = selectedJob;
	}

//	public ArrayList<Job> getJobsBySelectedCat() {
//		return jobsBySelectedCat;
//	}
//
//	public void setJobsBySelectedCat(ArrayList<Job> jobsBySelectedCat) {
//		this.jobsBySelectedCat = jobsBySelectedCat;
//	}
//
//	public ArrayList<Job> getJobsBySelectedUser() {
//		return jobsBySelectedUser;
//	}
//
//	public void setJobsBySelectedUser(ArrayList<Job> jobsBySelectedUser) {
//		this.jobsBySelectedUser = jobsBySelectedUser;
//	}

//	public ArrayList<Category> getCategoriesBySelectedJob() {
//		return categoriesBySelectedJob;
//	}
//
//	public void setCategoriesBySelectedJob(ArrayList<Category> categoriesBySelectedJob) {
//		this.categoriesBySelectedJob = categoriesBySelectedJob;
//	}

	public ArrayList<JobSearchUser> getUsersBySelectedCat() {
		return usersBySelectedCat;
	}

	public void setUsersBySelectedCat(ArrayList<JobSearchUser> usersBySelectedCat) {
		this.usersBySelectedCat = usersBySelectedCat;
	}

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

//	public Category getCategoryByName(String strCat) {
//
//		for(Category cat: this.categories){ 
//			if(cat.getName().matches(strCat)){
//				return cat;
//			}
//		}
//		
//		return null;
//	}
//	
//	public Category getCategoryById(int strId) {
//
//		for(Category cat: this.categories){ 
//			if(cat.getId() == strId){
//				return cat;
//			}
//		}
//		
//		return null;
//	}
	
}
