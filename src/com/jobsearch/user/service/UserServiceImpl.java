package com.jobsearch.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.email.Mailer;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.repository.UserRepository;

@Service
public class UserServiceImpl {

	@Autowired
	UserRepository repository;

	@Autowired
	Mailer mailer;

	public JobSearchUser createUser(JobSearchUser user) {

		JobSearchUser newUser = repository.createUser(user);

		if (newUser.getEmailAddress() != null) {
			mailer.sendMail(user.getEmailAddress(), "email verification",
					"please click the link to verify your email http://localhost:8080/JobSearch/validateEmail?userId="
							+ newUser.getUserId());
		}
		return newUser;
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

	public void rateEmployee(RatingDTO ratingDto) {
		repository.updateRating(ratingDto);
	}

	public List<RateCriterion> getRatings(int userId) {
		
		List<RateCriterion> ratingCriteria = this.getRatingCriteia();
		
		return repository.getRatingCriteriaValue(userId, ratingCriteria);
	}

	public List<RateCriterion> getRatingCriteia() {
		return repository.getRatingCriteria();
	}

	public JobSearchUser validateUser(int userId) {
		return repository.validateUser(userId);
	}

}
