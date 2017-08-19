package com.jobsearch.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.dtos.ApplicationDTO;
import com.jobsearch.dtos.CompletedJobDto;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Application;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.repository.JobRepository;
import com.jobsearch.request.AddJobRequest;
import com.jobsearch.request.EditJobRequest;
import com.jobsearch.request.FindEmployeesRequest;
import com.jobsearch.request.FindJobsRequest;
import com.jobsearch.responses.ApplicationProgressResponse;
import com.jobsearch.responses.ApplicationProgressResponse.ApplicationProgressStatus;
import com.jobsearch.responses.FindJobsResponse;
import com.jobsearch.responses.FindJobsResponse.JobDto_findJobsResponse;
import com.jobsearch.responses.GetJobResponse;
import com.jobsearch.responses.MessageResponse;
import com.jobsearch.responses.ValidateAddressResponse;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;
import com.jobsearch.utilities.StringUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class JobServiceImpl {

	@Autowired
	JobRepository repository;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	VerificationServiceImpl verificationService;
	@Autowired
	GoogleClient googleClient;
	@Autowired
	ProposalServiceImpl proposalService;
	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	QuestionServiceImpl questionService;

	public void addJob(AddJobRequest request, HttpSession session) {

		if (StringUtility.isNotBlank(request.getJob().getCity())
				&& StringUtility.isNotBlank(request.getJob().getState())
				&& StringUtility.isNotBlank(request.getJob().getStreetAddress())) {

			if (verificationService.isValidLocation(request.getJob())
					&& workDayService.areValidWorkDays(request.getWorkDays())) {

				
				
				
				// Address was validated in the previous ajax request.
				// Does it need to be validated again at this point??
				// I'm thinking not. This "addJob" is a callback function to the
				// "validate address" function. The user would unable to change
				// the
				// address after is was validated, but before the job was added.
				Coordinate coordinate = GoogleClient.getCoordinate(request.getJob());
				request.getJob().setLat(coordinate.getLatitude());
				request.getJob().setLng(coordinate.getLongitude());
				JobSearchUser user = SessionContext.getUser(session);
				formatAddress(request.getJob());
				repository.addJob(request, user);
			}
		}
	}

	private void formatAddress(Job job) {

		// This is to "clean" the user's input by using the address components
		// that Google provides.
		// This only to make addresses consistent (i.e. proper spelling, capital
		// letters, etc.).

		String route = "";
		String streetNumber = "";
		String zip = "";
		String city = "";
		GeocodingResult result = GoogleClient.getGeocodingResult(job);

		for (AddressComponent addressComponent : result.addressComponents) {

			for (AddressComponentType type : addressComponent.types) {

				if (type.name().matches(type.ROUTE.name())) {
					route = addressComponent.longName;
				} else if (type.name().matches(type.STREET_NUMBER.name())) {
					streetNumber = addressComponent.longName;
				} else if (type.name().matches(type.LOCALITY.name())) {
					city = addressComponent.longName;
				} else if (type.name().matches(type.POSTAL_CODE.name())) {
					zip = addressComponent.longName;
				}
			}
		}

		job.setStreetAddress_formatted(streetNumber + " " + route);
		job.setCity_formatted(city);
		job.setZipCode_formatted(zip);

	}

	public int getDateId(String date) {
		return repository.getDateId(date);
	}

	public List<Job> getJobs_byEmployerAndStatuses(int userId, List<Integer> jobStatuses) {
		return repository.getJobs_byEmployerAndStatuses(userId, jobStatuses);
	}

	public Job getJob(int jobId) {
		return repository.getJob(jobId);
	}

	private List<Job> getJobs_ByFindJobFilter(FindJobsRequest request) {

		// ******************************************************
		// ************************ ******************************
		// Need to validate location.
		// Can it be done client side?
		// Currently the lat and lng are being set in the filter request Dto's
		// constructor
		// ******************************************************
		// ******************************************************
		// Get the filtered jobs
		List<Job> jobs = repository.getJobs_byFindJobsRequest(request);

		return jobs;

	}

	public double getDistanceFromRequest(Job job, float fromLat, float fromLng) {
		return GoogleClient.getDistance(fromLat, fromLng, job.getLat(), job.getLng());
	}

	public Job getJob_ByApplicationId(int applicationId) {
		return repository.getJob_byApplicationId(applicationId);
	}

	public Date getEndDate(int jobId) {
		return repository.getEndDate(jobId);
	}

	public Date getStartDate(int jobId) {
		return repository.getStartDate(jobId);
	}

	public LocalDate getStartLocalDate(int jobId) {
		Date date = repository.getStartDate(jobId);
		return DateUtility.getLocalDate(date);
	}

	public LocalDate getEndLocalDate(int jobId) {
		Date date = repository.getEndDate(jobId);
		return DateUtility.getLocalDate(date);
	}

	public Time getStartTime(int jobId) {
		return repository.getStartTime(jobId);
	}

	public Time getEndTime(int jobId) {
		return repository.getEndTime(jobId);
	}

	public LocalTime getStartLocalTime(int jobId) {
		Time time = repository.getStartTime(jobId);
		return DateUtility.getLocalTime(time);
	}

	public LocalTime getEndLocalTime(int jobId) {
		Time time = repository.getEndTime(jobId);
		return DateUtility.getLocalTime(time);
	}

	private List<Skill> getSkills_ByType(int jobId, Integer type) {
		return repository.getSkills_ByType(jobId, type);
	}

	public void updateJobStatus(int status, int jobId) {

		// ***********************************************************
		// ***********************************************************
		// Job completion will be done automatically.
		// Once that is built, insert the inital ratings there
		if (status == Job.STATUS_PAST) {
			List<WorkDay> workDays = workDayService.getWorkDays(jobId);
			for (WorkDay workDay : workDays) {
				workDayService.updateWorkDay_isComplete(workDay.getWorkDayId(), 1);
			}
		}
		// ***********************************************************
		// ***********************************************************

		repository.updateJobStatus(status, jobId);
	}

	public void setGetJobResponse(Model model, HttpSession session, String context, int jobId) {

		Job job = getJob(jobId);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		List<WorkDay> workDays = workDayService.getWorkDays(jobId);

		GetJobResponse response = new GetJobResponse();
		response.setJob(job);
		// response.setWorkDayDtos(getWorkDayDtos(jobId));
		response.setCountWorkDays(workDays.size());
		response.setJson_workDayDtos(JSON.stringify(workDayService.getWorkDayDtos(jobId)));
		response.setIsPreviewingBeforeSubmittingJobPost(false);
		response.setProfileInfoDto(userService.getProfileInfoDto(userService.getUser(job.getUserId())));
		response.setContext(context);
		response.setSkillsRequired(getSkills_ByType(jobId, Skill.TYPE_REQUIRED_JOB_POSTING));
		response.setSkillsDesired(getSkills_ByType(jobId, Skill.TYPE_DESIRED_JOB_POSTING));
		response.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
		response.setMonthSpan_allWorkDays(DateUtility.getMonthSpan(workDays));
		response.setQuestions(questionService.getQuestionsWithAnswersByJobAndUser(jobId, sessionUser.getUserId()));

		// response.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
		// jobId, sessionUser.getUserId()));

		if (sessionUser != null && sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			response.setApplication(applicationService.getApplication(jobId, sessionUser.getUserId()));
		}

		model.addAttribute("response", response);
	}

	public void setGetJobReponse_forPreviewingJobPost(Model model, AddJobRequest request) {

		GetJobResponse response = new GetJobResponse();
		response.setJob(request.getJob());

		Coordinate coord = GoogleClient.getCoordinate(request.getJob());
		if (coord != null) {
			response.getJob().setLat(coord.getLatitude());
			response.getJob().setLng(coord.getLongitude());
		}

		formatAddress(request.getJob());

		List<String> dates = request.getWorkDays().stream().map(wd -> wd.getStringDate()).collect(Collectors.toList());
		response.setDate_firstWorkDay(DateUtility.getMinimumDate2(dates).toString());
		response.setMonthSpan_allWorkDays(DateUtility.getMonthSpan2(dates));
		response.setSkillsDesired(request.getSkills().stream()
				.filter(s -> s.getType() == Skill.TYPE_DESIRED_JOB_POSTING).collect(Collectors.toList()));
		response.setSkillsRequired(request.getSkills().stream()
				.filter(s -> s.getType() == Skill.TYPE_REQUIRED_JOB_POSTING).collect(Collectors.toList()));
		response.setQuestions(request.getQuestions());

		// for(WorkDay workDay : request.getWorkDays()){
		// workDay.setDate(LocalDate.parse(workDay.getStringDate()));
		// }
		//
		response.setWorkDayDtos(new ArrayList<>());
		for (WorkDay workDay : request.getWorkDays()) {
			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);
			workDayDto.setIsProposed(true); // for css purposes only
			response.getWorkDayDtos().add(workDayDto);
		}

		response.setJson_workDayDtos(JSON.stringify(response.getWorkDayDtos()));
		model.addAttribute("context", "preview-job-post");
		model.addAttribute("response", response);
	}

	public void setApplicationProgressResponse(int jobId, Model model, HttpSession session) {

		if (verificationService.didSessionUserPostJob(session, jobId)) {

			Job job = getJob(jobId);
			List<WorkDay> workDays = workDayService.getWorkDays(jobId);
			JobSearchUser sessionUser = SessionContext.getUser(session);
			LocalDateTime now = LocalDateTime.now();
			List<JobSearchUser> applicants = userService.getUsersWithOpenApplicationToJob(jobId);
			applicants.addAll(userService.getUsersEmployedForJob(jobId));
			
			ApplicationProgressResponse response = new ApplicationProgressResponse();
			response.setJob(job);

			for (JobSearchUser applicant : applicants) {

				Application application = applicationService.getApplication(jobId, applicant.getUserId());
				Proposal currentProposal = proposalService.getCurrentProposal(application.getApplicationId());
				Proposal previousProposal = proposalService.getPreviousProposal(application.getApplicationId(),
						applicant.getUserId());

				ApplicationProgressStatus applicationProgressStatus = new ApplicationProgressStatus();
				applicationProgressStatus.setApplication(application);
				
				
				applicationProgressStatus.setCountJobWorkDays(workDays.size());

				// Current proposal
				applicationProgressStatus.setCurrentProposal(currentProposal);
				applicationProgressStatus.setIsCurrentProposalExpired(
						DateUtility.isDateExpired(currentProposal.getExpirationDate(), now));
				applicationProgressStatus.setIsProposedToSessionUser(
						proposalService.isProposedToUser(currentProposal, sessionUser.getUserId()));
				applicationProgressStatus.setTime_untilEmployerApprovalExpires(
						proposalService.getTime_untilEmployerApprovalExpires(currentProposal.getExpirationDate(), now));
				applicationProgressStatus.setCurrentProposalStatus(proposalService.getCurrentProposalStatus(
						application.getIsOpen(), application.getIsAccepted(), currentProposal.getProposedByUserId(),
						sessionUser.getUserId(), sessionUser.getProfileId()));

				// Previous proposal
				applicationProgressStatus.setPreviousProposal(previousProposal);

				// Applicant
				applicationProgressStatus.setApplicantName(applicant.getFirstName());
				applicationProgressStatus.setApplicantRating(ratingService.getRating(applicant.getUserId()));
				applicationProgressStatus.setApplicantId(applicant.getUserId());

				// Answers
				applicationProgressStatus.setQuestions(
						questionService.getQuestionsWithAnswersByJobAndUser(jobId, applicant.getUserId()));
				applicationProgressStatus.setAnswerOptionIds_Selected(
						applicationService.getAnswerOptionIds_Selected_ByApplicantAndJob(applicant.getUserId(), jobId));

				applicationService.inspectNewness(application);
				proposalService.inspectNewness(currentProposal);

				response.getApplicationProgressStatuses().add(applicationProgressStatus);
			}
			model.addAttribute("response", response);
		}
	}

	public List<Job> getJobs_ByEmployeeAndJobStatuses(int userId_employee, List<Integer> jobStatuses) {

		if (jobStatuses != null) {
			if (jobStatuses.size() > 0) {
				return repository.getJobs_ByEmployeeAndStatuses(userId_employee, jobStatuses);
			}
		}
		return null;
	}

	public void setModel_ViewPostJob(Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);

		List<Job> postedJobs = this.getJobs_ByEmployer(sessionUser.getUserId());
		List<Question> postedQuestions = questionService.getDistinctQuestions_byEmployer(sessionUser.getUserId());

		model.addAttribute("postedJobs", postedJobs);
		model.addAttribute("postedQuestions", postedQuestions);

	}

	private List<Job> getJobs_ByEmployer(int userId) {
		return repository.getJobs_ByEmployer(userId);
	}


	public List<Job> getJobs_needRating_byEmployee(int userId) {

		List<Job> jobs_completed = getJobs_completedByUser(userId);
		if (verificationService.isListPopulated(jobs_completed)) {
			List<Job> jobs_needRating = new ArrayList<Job>();
			for (Job job : jobs_completed) {
				Integer count_nullRatings = ratingService.getCount_nullRatings_givenByUserForJob(job.getId(), userId);
				if (count_nullRatings > 0)
					jobs_needRating.add(job);
			}

			return jobs_needRating;
		} else
			return null;

	}

	public List<Job> getJobs_needRating_byEmployeer(int userId) {
		return repository.getJobs_needRating_byEmployer(userId);
	}

	private List<Job> getJobs_completedByUser(int userId) {
		return repository.getJobs_completedByUser(userId);
	}

	public void addSkills(Integer jobId, List<Skill> skills) {

		for (Skill skill : skills) {
			if (skill.getText() != null) {
				if (skill.getText() != "") {
					if (skill.getType() == Skill.TYPE_DESIRED_JOB_POSTING
							|| skill.getType() == Skill.TYPE_REQUIRED_JOB_POSTING) {

						repository.addSkill(jobId, skill);

					}
				}
			}
		}
	}

	public int getCount_JobsCompleted_ByUser(int userId) {
		return repository.getCount_JobsCompleted_ByUser(userId);
	}

	public Integer getCount_availableDays_ByUserAndWorkDays(int userId, List<String> workDays) {

		if (verificationService.isListPopulated(workDays)) {
			Integer count_unavailableDays = repository.getCount_unavailableDays_ByUserAndWorkDays(userId, workDays);
			return workDays.size() - count_unavailableDays;
		} else
			return -1;
	}

	public String getDate(int dateId) {
		return repository.getDateId(dateId);
	}

	public void setModel_getEmployees_byJobAndDate(Model model, HttpSession session, int jobId,
			List<String> dateStrings) {

		if (verificationService.didSessionUserPostJob(session, jobId)) {
			List<JobSearchUser> users_employees = userService.getEmployees_byJobAndDate(jobId, dateStrings);
			model.addAttribute("users_employees", users_employees);
			if (dateStrings.size() > 1)
				model.addAttribute("isRemovingOneDate", false);
			else
				model.addAttribute("isRemovingOneDate", true);
		}
	}

	public Job getConflictingEmployment_byUserAndWorkDay(int jobId_reference, int userId, int workDayId) {
		return repository.getConflictingEmployment_byUserAndWorkDay(jobId_reference, userId, workDayId);
	}

	public boolean setModel_viewReplaceEmployees(Model model, HttpSession session, int jobId) {

		boolean isValidRequest = true;
		if (verificationService.didSessionUserPostJob(session, jobId)) {

			List<JobSearchUser> users_employees = userService.getUsersEmployedForJob(jobId);
			if (users_employees != null) {
				model.addAttribute("json_work_day_dtos", JSON.stringify(workDayService.getWorkDayDtos(jobId)));
				model.addAttribute("jobId", jobId);
				model.addAttribute("users_employees", users_employees);
			} else
				isValidRequest = false;

		} else
			isValidRequest = false;

		return isValidRequest;
	}

	// public void replaceEmployee(HttpSession session, int jobId, int userId) {
	//
	// if(verificationService.didSessionUserPostJob(session, jobId)){
	// repository.replaceEmployee(jobId, userId);
	// }
	//
	// }

	public void editJobRequest(HttpSession session, EditJobRequest request) {

		if (verificationService.didSessionUserPostJob(session, request.getJobId())
				&& workDayService.areValidWorkDays(request.getNewWorkDays())) {

			for (WorkDay new_workDay : request.getNewWorkDays()) {
				new_workDay.setDate(LocalDate.parse(new_workDay.getStringDate()));
			}

			List<WorkDay> workDays_toDelete = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toAdd = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toEdit = new ArrayList<WorkDay>();
			List<WorkDay> current_workDays = workDayService.getWorkDays(request.getJobId());

			// Delete or edit current work days
			for (WorkDay current_workDay : current_workDays) {
				boolean deleteCurrentWorkDay = true;
				for (WorkDay new_workDay : request.getNewWorkDays()) {
					if (new_workDay.getDate().equals(current_workDay.getDate())) {
						deleteCurrentWorkDay = false;
						if (!new_workDay.getStringStartTime().matches(current_workDay.getStringStartTime())
								|| !new_workDay.getStringEndTime().matches(current_workDay.getStringEndTime())) {

							new_workDay.setWorkDayId(current_workDay.getWorkDayId());
							;
							workDays_toEdit.add(new_workDay);
						}
						break;
					}
				}
				if (deleteCurrentWorkDay)
					workDays_toDelete.add(current_workDay);
			}

			// Add new work days
			for (WorkDay new_workDay : request.getNewWorkDays()) {
				boolean addNewWorkDay = true;
				for (WorkDay current_workDay : current_workDays) {
					if (new_workDay.getDate().equals(current_workDay.getDate())) {
						addNewWorkDay = false;
						break;
					}
				}
				if (addNewWorkDay)
					workDays_toAdd.add(new_workDay);
			}

			workDayService.addWorkDays(request.getJobId(), workDays_toAdd);
			workDayService.deleteWorkDays(request.getJobId(), workDays_toDelete);
			workDayService.updateWorkDayTimes(request.getJobId(), workDays_toEdit);
		}
	}

	public void setModel_findEmployees_byJob(Model model, HttpSession session, int jobId) {

		if (verificationService.didSessionUserPostJob(session, jobId)) {

			Job job = getJob(jobId);
			String address = job.getStreetAddress_formatted() + " " + job.getCity_formatted() + " " + job.getState()
					+ " " + job.getZipCode_formatted();
			Double radius = 25.0;

			FindEmployeesRequest request = new FindEmployeesRequest();
			request.setAddress(address);
			request.setRadius(radius);
			request.setDates(workDayService.getWorkDayDateStrings(jobId));

			userService.setFindEmployeesResponse(model, request);

			model.addAttribute("job", job);
			// model.addAttribute("doShowResultsOnPageLoad", true);
		}
	}
	
		
		
	

	public void inspectJob_isStillAcceptingApplications(int jobId) {

		boolean atLeastOneWorkDayIsNotFilled = false;
		List<WorkDayDto> workDayDtos = workDayService.getWorkDayDtos(jobId);
		for (WorkDayDto workDayDto : workDayDtos) {
			if (workDayDto.getCount_positionsFilled() < workDayDto.getCount_totalPositions()) {
				atLeastOneWorkDayIsNotFilled = true;
				break;
			}
		}

		if (!atLeastOneWorkDayIsNotFilled)
			updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 1);
	}

	private void updateJobFlag(int jobId, String flag, int value) {
		repository.updateJobFlag(jobId, flag, value);
	}

	public void editJob_removeRemainingWorkDays_forUser(int jobId, int userId, HttpSession session) {

		if (verificationService.didSessionUserPostJob(session, jobId)) {

			List<WorkDay> workDays_incomplete = workDayService.getWorkDays_incomplete_byUser(jobId, userId);

			if (workDays_incomplete != null) {
				updateEmploymentFlag(jobId, userId, "WasTerminated", 1);
				updateEmploymentFlag(jobId, userId, "Flag_EmployerTerminatedEmployee", 1);
				Application application = applicationService.getApplication(jobId, userId);
				proposalService.deleteProposedWorkDays(workDays_incomplete, application.getApplicationId());

				updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 0);
			}
		}
	}

	public boolean getIsJobComplete(Integer jobId) {
		List<WorkDay> workDays_incomplete = workDayService.getWorkDays_incomplete(jobId);
		if (workDays_incomplete == null)
			return true;
		else
			return false;
	}

	public boolean getHasJobStarted(Integer jobId) {
		List<WorkDay> workDays_incomplete = workDayService.getWorkDays_incomplete(jobId);
		List<WorkDay> workDays = workDayService.getWorkDays(jobId);

		if (!verificationService.isListPopulated(workDays_incomplete))
			return true;
		else if (workDays_incomplete.size() == workDays.size()) {

			LocalDateTime startTime_firstWorkDay = LocalDateTime.of(workDays.get(0).getDate(),
					LocalTime.parse(workDays.get(0).getStringStartTime()));

			if (startTime_firstWorkDay.isBefore(LocalDateTime.now()))
				return true;
			else
				return false;
		} else
			return true;
	}

	public void leaveJob_employee(HttpSession session, int jobId) {

		// No need to check if the session user is actually an employee of this
		// job.
		// The sql statement will simply not update any flags.
		if (!getIsJobComplete(jobId)) {
			updateEmploymentFlag(jobId, SessionContext.getUser(session).getUserId(), "EmployeeLeftJob", 1);
			updateEmploymentFlag(jobId, SessionContext.getUser(session).getUserId(), "WasTerminated", 1);
			updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 0);
		}

	}

	public void updateEmploymentFlag(int jobId, int userId, String flag, int value) {
		repository.updateEmploymentFlag(jobId, userId, flag, value);

	}

	public void setModel_displayMessage_terminmateEmployee(Model model, HttpSession session, int jobId,
			int userId_emloyee) {

		if (verificationService.didSessionUserPostJob(session, jobId)
				&& verificationService.didUserApplyForJob(jobId, userId_emloyee)) {

			JobSearchUser user_employee = userService.getUser(userId_emloyee);
			model.addAttribute("user_employee", user_employee);
		}
	}

	public List<Job> getJobs_employment_byUserAndDate(int userId, String dateString) {
		return repository.getJobs_employment_byUserAndDate(userId, dateString);
	}

	public List<MessageResponse> getMessagesResponses_jobsTermintedFrom(int userId) {
		List<Job> jobs = repository.getJobs_terminatedFrom_byUser(userId);
		List<MessageResponse> messageResponses = new ArrayList<>();
		for (Job job : jobs) {
			MessageResponse messageResponse = new MessageResponse();
			messageResponse.setJob(job);
			messageResponses.add(messageResponse);
		}
		return messageResponses;
	}

	public List<CompletedJobDto> getCompletedJobDtos(JobSearchUser user) {
		List<Job> completedJobs = getCompletedJobs(user);
		List<CompletedJobDto> completedJobDtos = new ArrayList<>();
		for (Job completedJob : completedJobs) {
			Double rating = ratingService.getRating_byJobAndUser(completedJob.getId(), user.getUserId());
			if (rating != null) {
				CompletedJobDto completedJobDto = new CompletedJobDto();
				completedJobDto.setRating(rating);
				completedJobDto.setJob(completedJob);
				completedJobDto.setComments(
						ratingService.getCommentsGivenToUser_byJob(user.getUserId(), completedJob.getId()));
				completedJobDtos.add(completedJobDto);
			}
		}
		return completedJobDtos;
	}

	public List<Job> getCompletedJobs(JobSearchUser user) {
		List<Job> completedJobs = new ArrayList<>();
		if (user.getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			completedJobs = getJobs_ByEmployeeAndJobStatuses(user.getUserId(), Arrays.asList(Job.STATUS_PAST));
		} else {
			completedJobs = getJobs_byEmployerAndStatuses(user.getUserId(), Arrays.asList(Job.STATUS_PAST));
		}
		return completedJobs;
	}

	public ValidateAddressResponse getValidateAddressResponse(String address) {
		ValidateAddressResponse response = new ValidateAddressResponse();
		if (GoogleClient.isValidAddress(address))
			response.setIsValid(true);
		else
			response.setIsValid(false);
		return response;
	}

	public void setFindJobsResponse(Model model, HttpSession session, FindJobsRequest request) {
		
		Coordinate coord = GoogleClient.getCoordinate(request.getAddress());
		request.setLat(coord.getLatitude());
		request.setLng(coord.getLongitude());
		
		FindJobsResponse response = new FindJobsResponse();
		response.setLatitudeSearched(request.getLat());
		response.setLongitudeSearched(request.getLng());
		

		if(coord != null){
			

			
			List<Job> jobs = getJobs_ByFindJobFilter(request);
			
			
			response.setJobDtos(new ArrayList<>());
		
			if(jobs.size() > 0){

				response.setAppendedJobs(request.getIsAppendingJobs());
			
				for (Job job : jobs) {
	
					List<WorkDay> workDays = workDayService.getWorkDays(job.getId());
					double distance = GoogleClient.getDistance(request.getLat(), request.getLng(), job.getLat(),
							job.getLng());
					distance = MathUtility.round(distance, 1, 0);
					JobSearchUser employer = userService.getUser(job.getUserId());
					
					JobDto_findJobsResponse jobDto = new JobDto_findJobsResponse();
					jobDto.setJob(job);
					jobDto.setWorkDays(workDays);
					jobDto.setDistance(distance);
					jobDto.setEmployerOverallRating(ratingService.getRating(employer.getUserId()));
					jobDto.setEmployerName(employer.getFirstName() + " " + employer.getLastName());
	
					response.getJobDtos().add(jobDto);
				}			
				Double maxDistance = response.getJobDtos().stream()
														  .map(jDto -> jDto.getDistance())
														  .max(Double::compare).get();
				response.setMaxDistance(maxDistance);
			}
			
		}		
		model.addAttribute("response", response);	
	}

}
