package com.jobsearch.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.dtos.ProfileRatingDto;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.repository.RatingRepository;
import com.jobsearch.request.SubmitRatingRequest;
import com.jobsearch.responses.GetRatingsByUserResponse;
import com.jobsearch.responses.rating.ViewRateEmployeesResponse;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.MathUtility;
import com.jobsearch.utilities.VerificationServiceImpl;;

@Service
public class RatingServiceImpl {
	
	@Autowired
	RatingRepository repository;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	VerificationServiceImpl verificationService;
	
	public Double getRating_byJobAndUser(Integer jobId, int userId) {
		return repository.getRating_byJobAndUser(jobId, userId);
	}
	
	public void insertRatings(List<SubmitRatingRequest> requests, HttpSession session) {
		
		// TODO: needs more validation.
		// should verify each rating criteria's value is valid

		JobSearchUser sessionUser = SessionContext.getUser(session);

		// For each employee's rating
		for (SubmitRatingRequest SubmitRatingRequest : requests) {

			// Rate criterion
			for (RateCriterion rc : SubmitRatingRequest.getRateCriteria()) {
				rc.setUserId_ratee(SubmitRatingRequest.getUserId_ratee());
				rc.setUserId_rater(sessionUser.getUserId());
				rc.setJobId(SubmitRatingRequest.getJobId());
				repository.updateRating(rc);
			}

			// Comment
			if (SubmitRatingRequest.getComment() != "") {
				repository.addComment(SubmitRatingRequest.getUserId_ratee(), SubmitRatingRequest.getJobId(),
						SubmitRatingRequest.getComment(), sessionUser.getUserId());
			}

		}

	}
	
	public List<String> getCommentsGivenToUser_byJob(int userId, Integer jobId) {
		return repository.getCommentsGivenToUser_byJob(userId, jobId);
	}
	
	public Double getRating(int userId) {

		// Round to the nearest tenth. 0 is the minimum value.
		return MathUtility.round(repository.getRating(userId), 1, 0);
	}

	public String getComment(int jobId, int userId) {

		return repository.getComment(jobId, userId);
	}
	
	public Double getRating_givenByUser(Integer jobId, int userId) {		
		return repository.getRating_givenByUser(jobId, userId);
	}
	
	public Integer getCount_nullRatings_givenByUserForJob(Integer jobId, int userId) {
		return repository.getCount_nullRatings_givenByUserForJob(jobId, userId);
	}
	
	public void deleteComment(int jobId, int employeeId) {
		repository.deleteComment(jobId, employeeId);

	}
	
	public void insertRatings_toRateEmployer(int jobId, int userId_emloyee) {

		List<RateCriterion> rateCriteria = getRatingCriteia_toRateEmployer();
		Job job = jobService.getJob(jobId);

		for (RateCriterion rateCriterion : rateCriteria) {
			repository.insertRating(rateCriterion.getRateCriterionId(), job.getUserId(), job.getId(),
					userId_emloyee);
		}
	}

	public void insertRatings_toRateEmployees(int jobId, int userId_employee) {

		List<RateCriterion> rateCriteria = getRatingCriteia_toRateEmployee();
		Job job = jobService.getJob(jobId);
		
		for (RateCriterion rateCriterion : rateCriteria) {
			repository.insertRating(rateCriterion.getRateCriterionId(), userId_employee, job.getId(),
					job.getUserId());
		}
	}

	
	public ProfileRatingDto getProfileRatingDto(JobSearchUser user) {

		ProfileRatingDto profileRatingDto = new ProfileRatingDto();
		profileRatingDto.setRateCriteria(getRateCriteria(user));
		for (RateCriterion rateCriterion : profileRatingDto.getRateCriteria()) {
			rateCriterion.setValue(getRatingValue_byCriteriaAndUser(
					rateCriterion.getRateCriterionId(), user.getUserId()));
			if (rateCriterion.getValue() != null)
				rateCriterion.setStringValue(String.format("%.1f", rateCriterion.getValue()));
		}
		profileRatingDto.setOverallRating(getOverallRating(user.getUserId()));
		return profileRatingDto;
	}
	
	private List<RateCriterion> getRateCriteria(JobSearchUser user) {
		if (user.getProfileId() == Profile.PROFILE_ID_EMPLOYEE)
			return getRatingCriteia_toRateEmployee();
		else
			return getRatingCriteia_toRateEmployer();
	}

	public List<RateCriterion> getRatingCriteia_toRateEmployee() {
		return repository.getRatingCriteia_toRateEmployee();
	}
	
	public List<RateCriterion> getRatingCriteia_toRateEmployer() {
		return repository.getRateCriteria_toRateEmployer();
	}
	
	public Double getRatingValue_byCriteriaAndUser(Integer rateCriterionId, int userId) {
		return repository.getRatingValue_byCriteriaAndUser(rateCriterionId, userId);
	}
	
	public Double getOverallRating(int userId) {
		return MathUtility.round(repository.getOverallRating(userId), 1, 0);
	}
	
	public boolean setModel_ViewRateEmployer(int jobId, Model model, HttpSession session) {

		JobSearchUser user = SessionContext.getUser(session);

		if(applicationService.wasUserEmployedForJob(user.getUserId(), jobId)){

			Job job = jobService.getJob(jobId);
			JobSearchUser employer = userService.getUser(job.getUserId());

			model.addAttribute("job", job);
			model.addAttribute("employer", employer);
			return true;
		}
		else return false;

	}


	public void setModel_ViewRateEmployees(int jobId, Model model, HttpSession session) {
		Job job = jobService.getJob(jobId);
		if(verificationService.didSessionUserPostJob(session, job)){	
			ViewRateEmployeesResponse response = new ViewRateEmployeesResponse();
			response.setJob(job);
			response.setEmployees(userService.getEmployees_byJob_completedWork(jobId));
			model.addAttribute("response", response);			
		}
	}
	
	public void setGetRatingByUserResponse(Model model, int userId) {
		JobSearchUser user = userService.getUser(userId);
		GetRatingsByUserResponse response = new GetRatingsByUserResponse();
		response.setProfileInfoDto(userService.getProfileInfoDto(user));
		model.addAttribute("response", response);
		model.addAttribute("isViewingOnesSelf", false);
	}

	public void deleteRatings(int userId, int jobId) {
		repository.deleteRatings(userId, jobId);		
	}

}
