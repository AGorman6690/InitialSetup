package com.jobsearch.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.service.Application;
import com.jobsearch.model.AppCatJobUser;
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
	

	public void rateEmployee(int rateCriterionId, int employeeId, int jobId, double value) {
		
		if(repository.hasRating(rateCriterionId, employeeId, jobId)){
			repository.updateRating(rateCriterionId, employeeId, jobId, value);
		}else{
			repository.rateEmployee(rateCriterionId, employeeId, jobId, value);
		}
	}


	public List<RateCriterion> getRatings(int userId, List<RateCriterion> ratingCriteria) {
		return repository.getRatingCriteriaValue(userId, ratingCriteria);
	}

	public List<RateCriterion> getRatingCriteia() {
		return repository.getRatingCriteria();
	}


}
