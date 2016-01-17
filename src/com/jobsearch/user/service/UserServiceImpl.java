package com.jobsearch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.jobsearch.model.App;
import com.jobsearch.model.Category;
import com.jobsearch.model.DataBaseItem;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.repository.FirstRepository;

@Service
public class FirstService {

	@Autowired
	FirstRepository repository;
	
	public void createUser(JobSearchUser user){
		repository.createUser(user);
	}

	public void addCategory(int userId, int categoryId) {
		
		//Verify the usercategory table does not already contain this item
		if(repository.getUserCats(userId, categoryId).size() <= 0){
			repository.addCategory(userId, categoryId);	
		}
	}
	
	public void addJob(String jobName, int userId) {
		repository.addJob(jobName, userId);		
	}

	public void deleteCategory(int userId, int categoryId) {
		repository.deleteCategory(userId, categoryId);
		
	}
	
//	public void setUsersCats(JobSearchUser user, String[] cats, App app) {
//		repository.setUserCats(user, cats, app);		
//	}
	
	public void setUserByEmail(JobSearchUser user) {
		repository.setUserByEmail(user);
		
	}

	public void setUsersId(JobSearchUser user) {
		repository.setUsersId(user);
		
	}

	public void exportUsersCats(JobSearchUser user) {
		repository.exportUsersCats(user);
		
	}
	
//	public void exportJobCategory(DataBaseItem item) {
//		repository.exportJobCategory(item);
//		
//	}

	public JobSearchUser getUserByEmail(String emailAddress) {
		return repository.getUserByEmail(emailAddress);
		
	}

	public ArrayList<Job> getJobs(JobSearchUser user) {
		
		return repository.getJobs(user);
	}
	
	public ArrayList<Job> getJobs(JobSearchUser user, boolean isActive) {
		return repository.getJobs(user, isActive );
	}
	
	public ArrayList<Job> getJobs(int userId, boolean isActive) {
		return repository.getJobs(userId, isActive );
	}

//	public ArrayList<Category> getCategoriesForJob(Job job) {
//		return repository.getCategoriesForJob(job);
//	}

	public ArrayList<JobSearchUser> getUsers(int categoryId, int profileIdNotToInclude) {
		return repository.getUsers(categoryId, profileIdNotToInclude);
	}

	public ArrayList<Category> getCategoriesByJobId(int jobId) {
		return repository.getCategoriesByJobId(jobId);
	}

	public List<Category> getCategories() {
		return repository.getCategories();			
	}
	
	public JobSearchUser getUser(int userId){
		return repository.getUser(userId);
	}

	public void updateJobComplete(int jobId) {
		repository.updateJobComplete(jobId);
		
	}

	public void applyForJob(int jobId, int userId) {
		repository.applyForJob(jobId, userId);
		
	}

	public ArrayList<Job> getJobsBySelectedCat(int categoryId) {
		return repository.getJobsBySelectedCat(categoryId);
	}

	public ArrayList<JobSearchUser> getApplicants(int jobId) {
		return repository.getApplicants(jobId);
	}

	public Job getJob(int jobId) {
		return repository.getJob(jobId);
	}

	public ArrayList<JobSearchUser> getEmployees(int jobId) {
		return repository.getEmpolyees(jobId);
	}

	public void hireApplicant(int userId, int jobId) {
		repository.hireApplicant(userId, jobId);
		
	}

	public ArrayList<Job> getAppliedToJobs(JobSearchUser user, Boolean showOnlyActiveJobs) {
		return repository.getAppliedToJobs(user, showOnlyActiveJobs);
	}

	public List<Category> getCategoriesByUserId(int userId) {
		return repository.getCategoriesByUserId(userId);
	}

	public ArrayList<Job> getEmployment(JobSearchUser user, boolean showOnlyActiveJobs) {
		return repository.getEmployment(user, showOnlyActiveJobs);
	}

	public Profile getProfile(int profileId) {		
		return repository.getProfile(profileId);
	}
	
	public ArrayList<Profile> getProfiles() {		
		return repository.getProfiles();
	}

	public Category getCategory(int categoryId) {
		return repository.getCategory(categoryId);
	}

	public ArrayList<JobSearchUser> getEmployeesByCategory(int categoryId) {		
		return repository.getEmployeesByCategory(categoryId);
	}

	public ArrayList<Job> getJobsByCategory(int categoryId, boolean showOnlyActiveJobs) {
		return repository.getJobsByCategory(categoryId, showOnlyActiveJobs);
	}

	public void addJobCategory(int jobId, int categoryId) {
		repository.addJobCategory(jobId, categoryId);
		
	}

	
}
