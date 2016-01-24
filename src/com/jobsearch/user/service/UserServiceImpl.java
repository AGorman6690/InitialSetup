package com.jobsearch.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.jobsearch.category.service.Category;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.App;
import com.jobsearch.model.Item;
import com.jobsearch.model.Profile;
import com.jobsearch.user.repository.UserRepository;

@Service
public class UserServiceImpl {

	@Autowired
	UserRepository repository;

	public JobSearchUser createUser(JobSearchUser user) {
		return repository.createUser(user);
	}

	public void addCategory(int userId, int categoryId) {

		// Verify the usercategory table does not already contain this item
		if (repository.getUserCatergories(userId, categoryId).size() <= 0) {
			repository.addUserCategory(userId, categoryId);
		}
	}

	public void deleteCategory(int userId, int categoryId) {
		repository.deleteUserCategory(userId, categoryId);

	}

	// public void setUsersCats(JobSearchUser user, String[] cats, App app) {
	// repository.setUserCats(user, cats, app);
	// }

	public void setUserByEmail(JobSearchUser user) {
		repository.getUserByEmail(user);

	}

	public void setUsersId(JobSearchUser user) {
		repository.setUsersId(user);

	}

	public void exportUsersCats(JobSearchUser user) {
		repository.exportUsersCats(user);

	}

	// public void exportJobCategory(DataBaseItem item) {
	// repository.exportJobCategory(item);
	//
	// }

	public JobSearchUser getUserByEmail(String emailAddress) {
		return repository.getUserByEmail(emailAddress);

	}

	// public ArrayList<Category> getCategoriesForJob(Job job) {
	// return repository.getCategoriesForJob(job);
	// }

	public List<JobSearchUser> getUsers(int categoryId, int profileIdNotToInclude) {
		return repository.getUsers(categoryId, profileIdNotToInclude);
	}

	public JobSearchUser getUser(int userId) {
		return repository.getUser(userId);
	}

	public List<JobSearchUser> getApplicants(int jobId) {
		return repository.getApplicants(jobId);
	}

	public List<JobSearchUser> getEmployees(int jobId) {
		return repository.getEmpolyees(jobId);
	}

	public void hireApplicant(int userId, int jobId) {
		repository.hireApplicant(userId, jobId);

	}

	public Profile getProfile(int profileId) {
		return repository.getProfile(profileId);
	}

	public List<Profile> getProfiles() {
		return repository.getProfiles();
	}

	public List<JobSearchUser> getEmployeesByCategory(int categoryId) {
		return repository.getEmployeesByCategory(categoryId);
	}

}
