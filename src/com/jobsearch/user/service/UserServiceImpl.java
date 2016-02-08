package com.jobsearch.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.model.AppCatJobUser;
import com.jobsearch.model.Application;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.user.repository.UserRepository;

@Service
public class UserServiceImpl {

	@Autowired
	UserRepository repository;

	public JobSearchUser createUser(JobSearchUser user) {
		return repository.createUser(user);
	}

	public void setUsersId(JobSearchUser user) {
		repository.setUsersId(user);

	}

	public JobSearchUser getUserByEmail(String emailAddress) {
		return repository.getUserByEmail(emailAddress);

	}
	

	public void markApplicationViewed(int jobId, int userId) {
		repository.markApplicationViewed(jobId, userId);		
	}

	public JobSearchUser getUser(int userId) {
		return repository.getUser(userId);
	}

	public List<JobSearchUser> getApplicants(int jobId) {
		return repository.getApplicants(jobId);
	}
	
	public List<JobSearchUser> getOfferedApplicantsByJob(int jobId) {
		return repository.getOfferedApplicantsByJob(jobId);
	}

	public List<JobSearchUser> getEmployeesByJob(int jobId) {
		return repository.getEmpolyeesByJob(jobId);
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
	
	public List<RateCriterion> getAppRateCriteria() {
		return repository.getAppRateCriteria();
	}

	public void rateEmployee(int rateCriterionId, int employeeId, int jobId, int value) {
		repository.rateEmployee(rateCriterionId, employeeId, jobId, value);		
	}

	public List<AppCatJobUser> getApplicationsByEmployer(int userId) {
		return repository.getApplicationsByEmployer(userId);
	}

	public void markApplicationAccepted(int jobId, int userId) {
		repository.markApplicationAccepted(jobId, userId);
		
	}

	public List<Application> getApplicationsByJob(int jobId) {
		return repository.getApplicationsByJob(jobId);
	}

}
