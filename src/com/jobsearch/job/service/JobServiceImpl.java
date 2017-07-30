package com.jobsearch.job.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.job.web.FindJobFilterDTO;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmployeeSearch;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.proposal.service.ProposalServiceImpl;
import com.jobsearch.responses.ApplicationProgressResponse;
import com.jobsearch.responses.ApplicationProgressResponse.ApplicationProgressStatus;
import com.jobsearch.responses.MessageResponse;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;
import com.jobsearch.utilities.VerificationServiceImpl;


@Service
public class JobServiceImpl {

	@Autowired
	JobRepository repository;
	@Autowired
	CategoryServiceImpl categoryService;
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

	public void addPosting(JobDTO jobDto, HttpSession session) {

		if(	( jobDto.getJob().getCity() != null || jobDto.getJob().getCity() != "" ) &&
			( jobDto.getJob().getState() != null || jobDto.getJob().getState() != "" ) &&
			( jobDto.getJob().getStreetAddress() != null || jobDto.getJob().getStreetAddress() != "" ) ){

			if( verificationService.isValidLocation(jobDto.getJob()) &&
				areValidWorkDays(jobDto.getWorkDays())){

				this.setLatAndLag(jobDto);
				if( jobDto.getJob().getLat() != null &&	jobDto.getJob().getLng() != null ){

					JobSearchUser user = SessionContext.getUser(session);
					formatAddress(jobDto.getJob());
					repository.addJob(jobDto, user);
				}
			}
		}
	}

	private void formatAddress(Job job) {

		// This is to "clean" the user's input by using the address components that Google provides.
		// This only to make addresses consistent (i.e. proper spelling, capital letters, etc.).

		String route = "";
		String streetNumber = "";
		String zip = "";
		String city = "";
		GeocodingResult result = GoogleClient.getGeocodingResult(job);

		for(AddressComponent addressComponent : result.addressComponents){

			for(AddressComponentType type : addressComponent.types){

				if(type.name().matches(type.ROUTE.name())){
					route = addressComponent.longName;
				}else if(type.name().matches(type.STREET_NUMBER.name())){
					streetNumber = addressComponent.longName;
				}else if(type.name().matches(type.LOCALITY.name())){
					city = addressComponent.longName;
				}else if(type.name().matches(type.POSTAL_CODE.name())){
					zip = addressComponent.longName;
				}
			}
		}

		job.setStreetAddress_formatted(streetNumber + " " + route);
		job.setCity_formatted(city);
		job.setZipCode_formatted(zip);

	}

	public void setLatAndLag(JobDTO jobDto) {

		Coordinate coord = GoogleClient.getCoordinate(jobDto.getJob());
		if(coord != null){
			jobDto.getJob().setLat(coord.getLatitude());
			jobDto.getJob().setLng(coord.getLongitude());
		}
	}

	private boolean areValidWorkDays(List<WorkDay> workDays) {

		if(workDays == null) return false;
		else if(workDays.size() == 0) return false;
		else{

			// Validate the string date and times can be parsed to
			// LocalDate and LocalTime objects
			for(WorkDay workDay : workDays){
				try {
					LocalDate.parse(workDay.getStringDate());
					LocalTime startTime = LocalTime.parse(workDay.getStringStartTime());
					LocalTime endTime = LocalTime.parse(workDay.getStringEndTime());
					
					if(endTime.isBefore(startTime)) return false;
					
				} catch (Exception e) {
					return false;
				}

			}
		}

		return true;
	}

	public void addWorkDays(int jobId, List<WorkDay> workDays) {

		// **************************************************************
		// **************************************************************
		// In order to alert all applicants of this job,
		// a flag on the job should be set
		// **************************************************************
		// **************************************************************

		for(WorkDay workDay : workDays){
			
			LocalDateTime localDateTime_endDate = LocalDateTime.of(
					LocalDate.parse(workDay.getStringDate()),
					LocalTime.parse(workDay.getStringEndTime()));
			
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
			workDay.setDateId(this.getDateId(workDay.getDate().toString()));
			workDay.setTimestamp_endDate(Timestamp.valueOf(localDateTime_endDate));
			repository.addWorkDay(jobId, workDay);
		}
	}

	public int getDateId(String date) {
		return repository.getDateId(date);
	}

	public List<Job> getJobs_byEmployerAndStatuses(int userId, List<Integer> jobStatuses) {
		return repository.getJobs_byEmployerAndStatuses(userId, jobStatuses);
	}

	public Job getJob(int jobId){
		return repository.getJob(jobId);
	}

	private List<Job> getJobs_ByFindJobFilter(FindJobFilterDTO filter) {

		//******************************************************
		//************************ ******************************
		//Need to validate location.
		//Can it be done client side?
		//Currently the lat and lng are being set in the filter request Dto's constructor
		//******************************************************
		//******************************************************
		// Get the filtered jobs
		List<Job> filteredJobs = repository.getFilteredJobs(filter);

		return filteredJobs;

	}

	public double getDistanceFromRequest(Job job, float fromLat, float fromLng) {
		return GoogleClient.getDistance(fromLat, fromLng, job.getLat(), job.getLng());
	}


	public Job getJob_ByApplicationId(int applicationId) {
		return repository.getJob_byApplicationId(applicationId);
	}

	public List<WorkDay> getWorkDays(int jobId) {
		return repository.getWorkDays(jobId);
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

	public JobDTO getJobDTO_DisplayJobInfo(int jobId) {

		JobDTO jobDto = new JobDTO();

		Job job = this.getJob(jobId);

		jobDto.setJob(job);
		jobDto.setWorkDays(this.getWorkDays(jobId));
		jobDto.setWorkDayDtos(getWorkDayDtos(jobId));
		jobDto.setCategories(categoryService.getCategoriesByJobId(jobId));
		jobDto.setSkillsRequired(this.getSkills_ByType(jobId, Skill.TYPE_REQUIRED_JOB_POSTING));
		jobDto.setSkillsDesired(this.getSkills_ByType(jobId, Skill.TYPE_DESIRED_JOB_POSTING));

		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(job.getStartDate_local(),
												job.getStartTime_local(),
									job.getEndDate_local(), job.getEndTime_local(), DateUtility.TimeSpanUnit.Days));

		jobDto.setAreAllTimesTheSame(areAllTimesTheSame(jobDto.getWorkDays()));

		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));

		return jobDto;
	}

	public Boolean areAllTimesTheSame(List<WorkDay> workDays) {

		if(verificationService.isListPopulated(workDays)){
			if(workDays.size() > 1){

				LocalTime a_startTime =  LocalTime.parse(workDays.get(0).getStringStartTime());
				LocalTime a_endTime =  LocalTime.parse(workDays.get(0).getStringEndTime());
				for( WorkDay workDay : workDays){
					if(!LocalTime.parse(workDay.getStringStartTime()).equals(a_startTime)){
						return false;
					}
					if(!LocalTime.parse(workDay.getStringEndTime()).equals(a_endTime)){
						return false;
					}
				}

				return true;
			}else return true;
		}else return null;
	}

	private List<Skill> getSkills_ByType(int jobId, Integer type) {
		return repository.getSkills_ByType(jobId, type);
	}

	public void updateJobStatus(int status, int jobId) {

		// ***********************************************************
		// ***********************************************************
		// Job completion will be done automatically.
		// Once that is built, insert the inital ratings there
		if(status == Job.STATUS_PAST){
			List<WorkDay> workDays = getWorkDays(jobId);
			for( WorkDay workDay : workDays){
				updateWorkDay_isComplete(workDay.getWorkDayId(), 1);
			}
		}
		// ***********************************************************
		// ***********************************************************

		repository.updateJobStatus(status, jobId);
	}

	public void updateWorkDay_isComplete(int workDayId, int value) {
		repository.updateWorkDay_isComplete(workDayId, value);

	}

	public List<JobDTO> getMoreJobs(Model model, FindJobFilterDTO filter) {

		// ************************************************************
		// ************************************************************
		// On "Get more jobs", if jobs were previously sorted, should the same
		// sort request be reapplied???
		// I don't see why not...
		// On further inspection, the Sort logic needs to be tidied up.
		// Address this later
		// ************************************************************
		// ************************************************************

		// If getting more jobs, exclude the already-loaded job ids

		// Per the filter request, get the jobs
		List<Job> jobs = getJobs_ByFindJobFilter(filter);
		// Get just-filtered job ids from jobs



		return transformToJobDTOs(jobs, filter);

	}

	public List<JobDTO> getFilteredJobs(Model model, FindJobFilterDTO filter) {

		// Per the filter request, get the jobs
		List<Job> jobs = getJobs_ByFindJobFilter(filter);

		return transformToJobDTOs(jobs, filter);
	}

	public void setModel_SortFilteredJobs(HttpSession session, Model model, String sortBy, boolean isAscending) {

		FindJobFilterDTO lastFilterRequest =  SessionContext.getLastFilterRequest(session);

		lastFilterRequest.setSortBy(sortBy);
		lastFilterRequest.setIsAscending(isAscending);

		List<Job> jobs = this.getJobsByJobIds(SessionContext.getFilteredJobIds(session));
		List<JobDTO> jobDtos = this.getJobDtos_Sorted(jobs, lastFilterRequest);

		this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, lastFilterRequest);

//		SessionContext.setLastFilterRequest(session, lastFilterRequest);

	}

	private void setModel_Render_GetJobs_InitialRequest(Model model, List<JobDTO> jobDtos,
		FindJobFilterDTO lastFilterRequest) {

		double maxDistance = this.getMaxDistanceJobFromFilterRequest(jobDtos);

		model.addAttribute("filterDto", lastFilterRequest);
		model.addAttribute("jobDtos", jobDtos);
		model.addAttribute("maxDistance", maxDistance);

	}

	private List<Integer> getJobIdsFromJobDTOs(List<JobDTO> jobDtos) {

		// Get jobs from job dtos
		List<Job> jobs = jobDtos.stream().map(JobDTO::getJob).collect(Collectors.toList());

		// Get just-filtered job ids from jobs
		List<Integer> jobIds = jobs.stream().map(Job::getId).collect(Collectors.toList());

		return jobIds;
	}

	public void sortJobs(List<Job> filteredJobs, String sortBy, boolean isAscending) {

		// **************************************************
		// For usage of Comparator<T>, refer to:
		// https://www.mkyong.com/java8/java-8-lambda-comparator-example/
		// **************************************************
		Comparator<Job> c = null;

		switch (sortBy) {

			case "StartDate":
				c = (Job j1, Job j2) -> j1.getStartDate().compareTo(j2.getStartDate());
				break;

			case "EndDate":
				c = (Job j1, Job j2) -> j1.getEndDate().compareTo(j2.getEndDate());
				break;

			case "StartTime":
				c = (Job j1, Job j2) -> j1.getStartTime().compareTo(j2.getStartTime());
				break;

			case "EndTime":
				c = (Job j1, Job j2) -> j1.getEndTime().compareTo(j2.getEndTime());
				break;

		}

		if (isAscending) {
			filteredJobs.sort(c);
		} else {
			filteredJobs.sort(c.reversed());
		}

	}

	public void sortJobDtos(List<JobDTO> jobDtos, String sortBy, boolean isAscending) {

		// **************************************************
		// For usage of Comparator<T>, refer to:
		// https://www.mkyong.com/java8/java-8-lambda-comparator-example/
		// **************************************************
		Comparator<JobDTO> c = null;

		switch (sortBy) {

			case "Distance":
				c = (JobDTO j1, JobDTO j2) -> j1.getDistanceFromFilterLocation().compareTo(j2.getDistanceFromFilterLocation());
				break;


			case "Duration":
				c = (JobDTO j1, JobDTO j2) -> j1.getDurationDays().compareTo(j2.getDurationDays());
				break;
			}

		if (isAscending) {
			jobDtos.sort(c);
		} else {
			jobDtos.sort(c.reversed());
		}

	}

	public double getMaxDistanceJobFromFilterRequest(List<JobDTO> jobDtos) {

		// Get the farthest job from the users requested located.
		if(jobDtos != null) {
			if(jobDtos.size() > 0){
				return jobDtos.stream().max(Comparator.comparing(JobDTO::getDistanceFromFilterLocation))
						.get().getDistanceFromFilterLocation();
			}
		}
		return 0;
	}

	private List<Job> getJobsByJobIds(List<Integer> jobIds) {
		if(jobIds != null) return repository.getJobs_byIds(jobIds);
		else return null;
	}

	public List<JobDTO> setModel_FindJobs_PageLoad(Model model, HttpSession session, List<Integer> previousJobIds, FindJobFilterDTO lastFilterRequest) {


//		// ****************************************
//		// The user does not need to be logged-in in order to search for jobs
//		// ****************************************
//
//		// If logged in, set user account details
		if(SessionContext.isLoggedIn(session)){
			JobSearchUserDTO userDto = userService.getUserDTO_FindJobs_PageLoad(session);
			model.addAttribute("userDto", userDto);
		}

		if(previousJobIds != null && lastFilterRequest != null){

			// Query the database
			List<Job> jobs = getJobsByJobIds(previousJobIds);

			// Create job dtos.
			// If necessary, filter the jobs
			List<JobDTO> jobDtos = new ArrayList<JobDTO>();
			if(lastFilterRequest.getSortBy() != null){
				jobDtos = getJobDtos_Sorted(jobs, lastFilterRequest);
			}else{
				jobDtos = transformToJobDTOs(jobs, lastFilterRequest);
			}

			return jobDtos;
		}

		return new ArrayList<>();
	}

	private List<JobDTO> getJobDtos_Sorted(List<Job> jobs, FindJobFilterDTO filterDto) {

		if(filterDto.getSortBy() != null){

			List<JobDTO> jobDtos = new ArrayList<JobDTO>();

			// Distance and duration are properties of the job dto, not the job.
			// Therefore, the dtos need to be created BEFORE they can be sorted.
			// Otherwise, the job objects are sorted first and then the job dtos are
			// created from the sorted job list.
			if(filterDto.getSortBy().matches("Distance") || filterDto.getSortBy().matches("Duration")){
				jobDtos = transformToJobDTOs(jobs, filterDto);
				this.sortJobDtos(jobDtos, filterDto.getSortBy(), filterDto.getIsAscending());
			}else{
				this.sortJobs(jobs, filterDto.getSortBy(), filterDto.getIsAscending());
				jobDtos = this.transformToJobDTOs(jobs, filterDto);
			}

			return jobDtos;
		}
		else return null;
	}

	private List<JobDTO> transformToJobDTOs(List<Job> jobs, FindJobFilterDTO filter) {
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();
		if (CollectionUtils.isNotEmpty(jobs)) {
			for (Job job : jobs) {

				JobDTO jobDto = new JobDTO();
				jobDto.setJob(job);
				jobDto.setCategories(categoryService.getCategoriesByJobId(job.getId()));

				List<WorkDay> workDays = getWorkDays(job.getId());
				jobDto.setWorkDays(getWorkDays(job.getId()));

				double distance = GoogleClient.getDistance(filter.getLat(), filter.getLng(), job.getLat(),
						job.getLng());
				distance = MathUtility.round(distance, 1, 0);
				jobDto.setDistanceFromFilterLocation(distance);

				jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
				jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(workDays));
				
				jobDto.getEmployerDto().setUser(userService.getUser(job.getUserId()));
				jobDto.getEmployerDto().setRatingValue_overall(userService.getRating(job.getUserId()));

				jobDtos.add(jobDto);
			}
		}
		return jobDtos;
	}

	public List<FindJobFilterDTO> getSavedFindJobFilters(int userId) {
		return repository.getSavedFindJobFilters(userId);
	}

	public void saveFindJobFilter(FindJobFilterDTO request, HttpSession session) {

		// *************************************
		// Need to verify the address
		// *************************************

		repository.insertSavedFindJob(request, (JobSearchUser) session.getAttribute("user"));

	}

	public void setModel_LoadFindJobsFilter(int savedFindJobFilterId, Model model, HttpSession session) {

		FindJobFilterDTO filterDto = this.getSavedFindJobFilter(savedFindJobFilterId, session);
		model.addAttribute("filterDto", filterDto);
		
		if(SessionContext.isLoggedIn(session)){
			JobSearchUserDTO userDto = userService.getUserDTO_FindJobs_PageLoad(session);
			model.addAttribute("userDto", userDto);
		}
	}

	private FindJobFilterDTO getSavedFindJobFilter(int savedFindJobFilterId, HttpSession session) {

		FindJobFilterDTO filter = repository.getSavedFindJobFilter(savedFindJobFilterId);

		if(filter != null){

			JobSearchUser sessionUser = SessionContext.getUser(session);

			if(filter.getUserId() == sessionUser.getUserId()){
				return filter;
			}else{
				return null;
			}
		}
		else return null;
	}

	public void setModel_ViewJob_Employee(Model model, HttpSession session,
						String context, int jobId) {

		// **************************************************
		// **************************************************
		// Need to add verification for all contexts
		// **************************************************
		// **************************************************

		Application application = new Application();
		JobSearchUser sessionUser = SessionContext.getUser(session);
		JobDTO jobDto = this.getJobDTO_DisplayJobInfo(jobId);
		// User does not need to be logged in to view job
		if(sessionUser != null){
			application = applicationService.getApplication(jobId, sessionUser.getUserId());
			
			for( WorkDayDto workDayDto : jobDto.getWorkDayDtos()){
				setWorkDayDto_conflictingEmployment(jobId, workDayDto, sessionUser.getUserId());
			
			}
		}



		jobDto.setRatingValue_overall(userService.getRating(jobDto.getJob().getUserId()));
		userService.setModel_getRatings_byUser(model, jobDto.getJob().getUserId());


		switch (context) {
		case "find":

			jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));

			if(sessionUser != null){

				jobDto.setApplication(application);

				if(jobDto.getApplication() == null){
					jobDto.setAvailabilityStatus(this.getAvailabilityStatus(
														jobDto.getJob().getIsPartialAvailabilityAllowed(),
														sessionUser.getUserId(),
														jobDto.getWorkDays()));
				}
			}
			break;

		case "profile-incomplete":

			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
														jobId, sessionUser.getUserId()));
			break;


		case "profile-complete":

			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
					jobId, sessionUser.getUserId()));
			break;
		}

		model.addAttribute("json_work_day_dtos", JSON.stringify(jobDto.getWorkDayDtos()));
		model.addAttribute("context", context);
		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
	}

	public List<String> getCommentsGivenToUser_byJob(int userId, Integer jobId) {
		return repository.getCommentsGivenToUser_byJob(userId, jobId);
	}

	private int getAvailabilityStatus(boolean isPartialAvailabilityAllowed, int userId,
										List<WorkDay> workDays) {

		// ***********************************************************
		// ***********************************************************
		// It has been decided, for the initial release, that employees
		// cannot set their availability.
		// This is a beneficial feature we will include include in the future.
		// ***********************************************************
		// ***********************************************************


		if(verificationService.isListPopulated(workDays)){

//			Integer count_availableDays = this.getCount_availableDays_ByUserAndWorkDays(userId, workDays);
			Integer count_employmentDays = this.getCount_employmentDays_byUserAndWorkDays(userId, workDays);

			// If the user has **NO EMPLOYMENT** on these work days
			if(count_employmentDays <= 0) {

//				if(count_availableDays == 0) return JobDTO.AVAILABILITY_STATUS_NONE_DUE_TO_AVAILABILITY_NOT_SET;
//				else if(count_availableDays == workDays.size()) return JobDTO.AVAILABILITY_STATUS_COMPLETELY;
//				else {
					if(isPartialAvailabilityAllowed) return JobDTO.AVAILABILITY_STATUS_PARTIAL_DUE_TO_AVAILABILITY_NOT_SET;
					else return JobDTO.AVAILABILITY_STATUS_NONE_DUE_TO_AVAILABILITY_NOT_SET;
//				}

			}
			// Else the user has at least one employment day within these work days
			else{

				if(count_employmentDays == workDays.size()) return JobDTO.AVAILABILITY_STATUS_NONE_DUE_TO_EMPLOYMENT;
				else {
					if(isPartialAvailabilityAllowed) return JobDTO.AVAILABILITY_STATUS_PARTIAL_DUE_TO_EMPLOYMENT;
					else return JobDTO.AVAILABILITY_STATUS_NONE_DUE_TO_EMPLOYMENT;
				}
			}
		}
		else return -1;
	}


	public void setModel_ViewJob_Employer(Model model, HttpSession session,
						String context, int jobId, String data_pageInit) {

		// **************************************************
		// **************************************************
		// Need to add verification for all contexts
		// **************************************************
		// **************************************************


		
		JobDTO jobDto = this.getJobDTO_DisplayJobInfo(jobId);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));

		//		JobSearchUserDTO userDto = new JobSearchUserDTO();

		switch (context) {
		case "waiting":

			jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));
			jobDto.setApplicationDtos(applicationService.getApplicationDtos_ByJob_OpenApplications(jobId, session));
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));

			jobDto.setEmployees_whoLeft(userService.getEmployees_whoLeft(false, jobId));

			jobDto.setWorkDayDtos(getWorkDayDtos(jobId));

			applicationService.updateIsNew(jobDto.getJob(), 0);
//			applicationService.updateWageProposalsStatus_ToViewedButNoActionTaken(
//					jobDto.getJob().getId());

			break;

		case "in-process":

			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
														jobId, sessionUser.getUserId()));
			jobDto.setApplicationDtos(applicationService.getApplicationDtos_ByJob_OpenApplications(jobId, session));
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));

			break;

		case "complete":

			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));

			boolean haveJobRatingsBeenSubmitted = false;
			model.addAttribute("haveJobRatingsBeenSubmitted", haveJobRatingsBeenSubmitted);

			break;

		}

		model.addAttribute("json_work_day_dtos", JSON.stringify(getWorkDayDtos(jobId)));
		model.addAttribute("data_pageInit", data_pageInit);
		model.addAttribute("context", context);
		model.addAttribute("jobDto", jobDto);

	}
	

	public void setApplicationProgressResponse(int jobId, Model model, HttpSession session) {
		
		if(verificationService.didSessionUserPostJob(session, jobId)){
			
			ApplicationProgressResponse response = new ApplicationProgressResponse(); 
			response.setJob(getJob(jobId));
			JobSearchUser sessionUser = SessionContext.getUser(session);
			LocalDateTime now = LocalDateTime.now();
					
			List<JobSearchUser> applicants = userService.getUsersWithOpenApplicationToJob(jobId);			
			applicants.addAll(userService.getUsersEmployedForJob(jobId));
			
			for(JobSearchUser applicant : applicants){
				
				Application application = applicationService.getApplication(jobId, applicant.getUserId());
				Proposal currentProposal = proposalService.getCurrentProposal(
						application.getApplicationId());				
				Proposal previousProposal = proposalService.getPreviousProposal(
						application.getApplicationId(), applicant.getUserId());
				
				
				ApplicationProgressStatus applicationProgressStatus = new ApplicationProgressStatus();
				applicationProgressStatus.setApplication(application);
				
				// Current proposal
				applicationProgressStatus.setCurrentProposal(currentProposal);
				applicationProgressStatus.setIsCurrentProposalExpired(
						DateUtility.isDateExpired(currentProposal.getExpirationDate(), now));								
				applicationProgressStatus.setIsProposedToSessionUser(
						proposalService.isProposedToUser(currentProposal, sessionUser.getUserId()));	
				applicationProgressStatus.setTime_untilEmployerApprovalExpires(
						proposalService.getTime_untilEmployerApprovalExpires(
								currentProposal.getExpirationDate(), now));
				applicationProgressStatus.setCurrentProposalStatus(
						proposalService.getCurrentProposalStatus(
								application.getIsOpen(),
								application.getIsAccepted(),
								currentProposal.getProposedByUserId(),
								sessionUser.getUserId(),
								sessionUser.getProfileId()));
				
				// Previous proposal
				applicationProgressStatus.setPreviousProposal(previousProposal);					
				
				// Applicant
				applicationProgressStatus.setApplicantName(applicant.getFirstName());
				applicationProgressStatus.setApplicantRating(
						userService.getRating(applicant.getUserId()));
				applicationProgressStatus.setApplicantId(applicant.getUserId());

				// Answers
				applicationProgressStatus.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
						jobId, applicant.getUserId()));
				applicationProgressStatus.setAnswerOptionIds_Selected(
						applicationService.getAnswerOptionIds_Selected_ByApplicantAndJob(
								applicant.getUserId(), jobId));		
				
				applicationService.inspectNewness(application);
				proposalService.inspectNewness(currentProposal);
				
				response.getApplicationProgressStatuses().add(applicationProgressStatus);				
			}		
			model.addAttribute("response", response);
		}
	}

	public List<WorkDayDto> getWorkDayDtos(int jobId) {

		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();
		List<WorkDay> workDays = getWorkDays(jobId);

		for(WorkDay workDay : workDays){

			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);
			workDayDto.setCount_applicants(applicationService.getCount_applicantsByDay(workDay.getDateId(), jobId));
			workDayDto.setCount_positionsFilled(applicationService.getCount_positionsFilledByDay(workDay.getDateId(), jobId));
			workDayDto.setCount_totalPositions(getJob(jobId).getPositionsPerDay());
			workDayDtos.add(workDayDto);
		}

		return workDayDtos;
	}

	public List<Job> getJobs_ByEmployeeAndJobStatuses(int userId_employee, List<Integer> jobStatuses) {

		if(jobStatuses != null){
			if(jobStatuses.size() > 0){
				return repository.getJobs_ByEmployeeAndStatuses(userId_employee, jobStatuses);
			}
		}
		return null;
	}

	public void setModel_ViewPostJob(Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);

		List<Job> postedJobs = this.getJobs_ByEmployer(sessionUser.getUserId());
		List<Question> postedQuestions = applicationService.getDistinctQuestions_byEmployer(sessionUser.getUserId());

		model.addAttribute("postedJobs", postedJobs);
		model.addAttribute("postedQuestions", postedQuestions);

	}

	private List<Job> getJobs_ByEmployer(int userId) {
		return repository.getJobs_ByEmployer(userId);
	}

	public JobDTO getJobDto_PreviousPostedJob(HttpSession session, int jobId) {

		JobDTO jobDto = getJobDto_ByEmployer(session, jobId);

		if(jobDto != null){
			jobDto.setQuestions(applicationService.getQuestions(jobId));
			return jobDto;
		}
		else return null;
	}

	public Question getQuestion_PreviousPostedQuestion(HttpSession session, int questionId) {

		Question postedQuestion = applicationService.getQuestion(questionId);
		if(verificationService.didSessionUserPostJob(session, postedQuestion.getJobId())){
			return postedQuestion;
		}
		else return null;
	}

	public List<Job> getJobs_needRating_byEmployee(int userId) {
		
		List<Job> jobs_completed = getJobs_completedByUser(userId);
		if(verificationService.isListPopulated(jobs_completed)){
			List<Job> jobs_needRating = new ArrayList<Job>();
			for(Job job : jobs_completed){
				Integer count_nullRatings = userService.getCount_nullRatings_givenByUserForJob(job.getId(), userId);
				if(count_nullRatings > 0) jobs_needRating.add(job);
			}
			
			return jobs_needRating;	
		}else return null;
		
	}
	
	public List<Job> getJobs_needRating_byEmployeer(int userId) {		
		return repository.getJobs_needRating_byEmployer(userId);
	}

	private List<Job> getJobs_completedByUser(int userId) {
	
		return repository.getJobs_completedByUser(userId);

	}

	public boolean setModel_ViewRateEmployer(int jobId, Model model, HttpSession session) {

		JobSearchUser user = SessionContext.getUser(session);

		if(applicationService.wasUserEmployedForJob(user.getUserId(), jobId)){

			Job job = this.getJob(jobId);
			JobSearchUser employer = userService.getUser(this.getJob(jobId).getUserId());

			model.addAttribute("job", job);
			model.addAttribute("employer", employer);
			return true;
		}
		else return false;

	}


	public boolean setModel_ViewRateEmployees(int jobId, Model model, HttpSession session) {

		Job job = this.getJob(jobId);

		if(verificationService.didSessionUserPostJob(session, job)){
			
			List<JobSearchUser> employees = userService.getEmployees_byJob_completedWork(jobId);
			model.addAttribute("job", job);
			model.addAttribute("employees", employees);

			return true;
		}else return false;
	}

	public void addSkills(Integer jobId, List<Skill> skills) {

		for(Skill skill : skills){
			if(skill.getText() != null){
				if(skill.getText() != ""){
					if(skill.getType() == Skill.TYPE_DESIRED_JOB_POSTING ||
							skill.getType() == Skill.TYPE_REQUIRED_JOB_POSTING){

						repository.addSkill(jobId, skill);

					}
				}
			}
		}
	}

	public void setModel_PreviewJobPost(Model model, JobDTO jobDto) {

		this.setLatAndLag(jobDto);
		this.formatAddress(jobDto.getJob());

		for(WorkDay workDay : jobDto.getWorkDays()){
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
		}

		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));

		jobDto.setSkillsDesired(jobDto.getSkills().stream()
				.filter(s -> s.getType() == Skill.TYPE_DESIRED_JOB_POSTING).collect(Collectors.toList()));

		jobDto.setSkillsRequired(jobDto.getSkills().stream()
				.filter(s -> s.getType() == Skill.TYPE_REQUIRED_JOB_POSTING).collect(Collectors.toList()));

		jobDto.setCategories(categoryService.getCategories(jobDto.getCategoryIds()));


		for( WorkDay workDay : jobDto.getWorkDays()){
			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);
			jobDto.getWorkDayDtos().add(workDayDto);
		}

		model.addAttribute("context", "preview-job-post");
		model.addAttribute("json_work_day_dtos", JSON.stringify(jobDto.getWorkDayDtos()));
		model.addAttribute("jobDto", jobDto);

	}

	public JobDTO getJobDto_ByEmployer(HttpSession session, int jobId) {

		if(verificationService.didSessionUserPostJob(session, jobId)){
			return this.getJobDTO_DisplayJobInfo(jobId);
		}
		else return null;
	}

	public int getCount_JobsCompleted_ByUser(int userId) {
		return repository.getCount_JobsCompleted_ByUser(userId);
	}

	public Integer getCount_availableDays_ByUserAndWorkDays(int userId, List<String> workDays) {

		if(verificationService.isListPopulated(workDays)){
			Integer count_unavailableDays =
					repository.getCount_unavailableDays_ByUserAndWorkDays(userId, workDays);
			return 	workDays.size() - count_unavailableDays;
		}
		else return -1;
	}


	public Integer getCount_employmentDays_byUserAndWorkDays(int userId, List<WorkDay> workDays) {

		// *************************************************
		// This only takes into account the user's employment and ignores the days
		// they have or have not explicitly marked as "Available"
		// *************************************************

		if(verificationService.isListPopulated(workDays)){
			return repository.getCount_employmentDays_byUserAndWorkDays(userId, workDays);
		}
		else return -1;
	}

	public String getDate(int dateId) {
		return repository.getDateId(dateId);
	}

	public Integer getWorkDayId(int jobId, int dateId) {
		return repository.getWorkDayId(jobId, dateId);
	}

	public void setModel_getApplicants_byJobAndDate(Model model, HttpSession session, int jobId, String dateString) {

		if(verificationService.didSessionUserPostJob(session, jobId)){

			JobDTO jobDto = getJobDTO_DisplayJobInfo(jobId);

			// Current applicants on the requested day
			List<Application> applications = applicationService.getApplications_byJobAndDate(jobId, dateString);
			List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
			for( Application application : applications){

				ApplicationDTO applicationDto = new ApplicationDTO();
				applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));;

				applicationDtos.add(applicationDto);
			}
			jobDto.setApplicationDtos(applicationDtos);

			// Applicants who are available on the requested day, but did not apply
			List<JobSearchUser> applicants_whoAreAvailableButDidNotApplyForDate = userService.getApplicants_whoAreAvailableButDidNotApplyForDate(jobId, dateString);


			List<JobSearchUser> users_whoAreAvailableButHaveNotApplied = userService.getUsers_whoAreAvailableButHaveNotApplied(jobId, dateString);


			model.addAttribute("date", LocalDate.parse(dateString).format(DateTimeFormatter.ofPattern("E MMM d, y")));
			model.addAttribute("jobDto", jobDto);

			model.addAttribute("applicants_whoAreAvailableButDidNotApplyForDate",
					applicants_whoAreAvailableButDidNotApplyForDate);

			model.addAttribute("count_userswhoAreAvailableButHaveNotApplied",
					users_whoAreAvailableButHaveNotApplied.size());
		}

	}


	public void setModel_getEmployees_byJobAndDate(Model model, HttpSession session, int jobId, List<String> dateStrings) {

		if(verificationService.didSessionUserPostJob(session, jobId)){
			List<JobSearchUser> users_employees = userService.getEmployees_byJobAndDate(jobId, dateStrings);
			model.addAttribute("users_employees", users_employees);
			if(dateStrings.size() > 1) model.addAttribute("isRemovingOneDate", false);
			else model.addAttribute("isRemovingOneDate", true);
		}
	}

	public List<WorkDayDto> getWorkDayDtos_byProposal(EmploymentProposalDTO proposal) {

		// **************************************************************
		// **************************************************************
		// After optimizing the Employee's Profile page load, this was the taxing.
		// In this case, this method added 2 seconds to TTFB.
		// Break this method up, or do away with it entirely.
		// Not every calling method needs ALL this info.
		// **************************************************************
		// **************************************************************

		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();

		Application application = applicationService.getApplication(proposal.getApplicationId());
		Job job = getJob_ByApplicationId(proposal.getApplicationId());
//		List<WorkDay> workDays = getWorkDays_byProposalId(proposal.getEmploymentProposalId());
		List<WorkDay> workDays = getWorkDays(getJob_ByApplicationId(proposal.getApplicationId()).getId());

		for(WorkDay workDay: workDays){
			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);

			// Positions filled
			int positionsFilled = applicationService.getCount_positionsFilledByDay(
					workDay.getDateId(), job.getId());
			if(positionsFilled < job.getPositionsPerDay()) workDayDto.setHasOpenPositions(true);
			else workDayDto.setHasOpenPositions(false);

			// Is proposed
			workDayDto.setIsProposed(applicationService.getIsWorkDayProposed(
					workDay.getWorkDayId(), proposal.getApplicationId()));

			if(workDayDto.getIsProposed() && application.getIsAccepted() == 1)
				workDayDto.setIsAccepted(true);
			else
				workDayDto.setIsAccepted(false);


			// Conflicting employment
			setWorkDayDto_conflictingEmployment(job.getId(), workDayDto, application.getUserId());

			// Conflicting applications
			workDayDto.setApplicationDtos_conflictingApplications(
					applicationService.getApplicationDtos_Conflicting(
							application.getUserId(), proposal.getApplicationId(), Arrays.asList(workDay)));
			workDayDtos.add(workDayDto);
		}
		return workDayDtos;
	}

	public Job getConflictingEmployment_byUserAndWorkDay(int jobId_reference, int userId, int workDayId) {
		return repository.getConflictingEmployment_byUserAndWorkDay(
				jobId_reference,userId, workDayId);	}

	public void setWorkDayDto_conflictingEmployment(int jobId_reference, WorkDayDto workDayDto, int userId ){

		workDayDto.setJob_conflictingEmployment(getConflictingEmployment_byUserAndWorkDay(
				jobId_reference, userId, workDayDto.getWorkDay().getDateId()));
		if(workDayDto.getJob_conflictingEmployment() != null)
			workDayDto.setHasConflictingEmployment(true);
		else workDayDto.setHasConflictingEmployment(false);
	}

	public List<WorkDay> getWorkDays_byProposalId(Integer employmentProposalId) {
		return repository.getWorkDays_byProposalId(employmentProposalId);
	}

	public List<String> removeConflictingWorkDays(List<String> dateStrings_toInspect,
														List<WorkDay> workDays_toInspectAgainst) {

		List<String> dateStrings_conflictsRemoved = new ArrayList<String>();

		for(String dateString_toInspect : dateStrings_toInspect){
			Integer dateId_toInspect = getDateId(dateString_toInspect);

			Boolean isConflicting = false;
			for(WorkDay workDay_toInspectAgainst : workDays_toInspectAgainst){
				if(workDay_toInspectAgainst.getDateId() == dateId_toInspect) {
					isConflicting = true;
					break;
				}
			}

			if(!isConflicting) dateStrings_conflictsRemoved.add(dateString_toInspect);

		}

		return dateStrings_conflictsRemoved;
	}

	public List<String> getWorkDayDateStrings(int jobId) {
		return repository.getWorkDayDateStrings(jobId);
	}

	public boolean setModel_viewReplaceEmployees(Model model, HttpSession session, int jobId) {

		boolean isValidRequest = true;
		if(verificationService.didSessionUserPostJob(session, jobId)){

			List<JobSearchUser> users_employees = userService.getUsersEmployedForJob(jobId);
			if(users_employees != null){
				model.addAttribute("json_work_day_dtos",	JSON.stringify(getWorkDayDtos(jobId)));
				model.addAttribute("jobId", jobId);
				model.addAttribute("users_employees", users_employees);
			}else isValidRequest = false;

		}else isValidRequest = false;

		return isValidRequest;
	}

//	public void replaceEmployee(HttpSession session, int jobId, int userId) {
//		
//		if(verificationService.didSessionUserPostJob(session, jobId)){
//			repository.replaceEmployee(jobId, userId);
//		}
//		
//	}

	public void editJob_workDays(HttpSession session, List<WorkDay> workDays_new, Integer jobId) {
		
		if ( verificationService.didSessionUserPostJob(session, jobId) &&
				areValidWorkDays(workDays_new) ){
			
			for(WorkDay new_workDay : workDays_new){
				new_workDay.setDate(LocalDate.parse(new_workDay.getStringDate()));
			}			
			
			List<WorkDay> workDays_toDelete = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toAdd = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toEdit = new ArrayList<WorkDay>();
			
			List<WorkDay> current_workDays = getWorkDays(jobId);
			
			// Delete or edit current work days
			for(WorkDay current_workDay : current_workDays){

				boolean deleteCurrentWorkDay = true;
				for(WorkDay new_workDay : workDays_new){
					if(new_workDay.getDate().equals(current_workDay.getDate())){
						deleteCurrentWorkDay = false;
						if(!new_workDay.getStringStartTime().matches(current_workDay.getStringStartTime()) ||
								!new_workDay.getStringEndTime().matches(current_workDay.getStringEndTime())	){

							new_workDay.setWorkDayId(current_workDay.getWorkDayId());;
							workDays_toEdit.add(new_workDay);
						}
						break;
					}
				}

				if(deleteCurrentWorkDay) workDays_toDelete.add(current_workDay);
			}

			// Add new work days
			for(WorkDay new_workDay : workDays_new){			

				boolean addNewWorkDay = true;
				for(WorkDay current_workDay : current_workDays){

					if(new_workDay.getDate().equals(current_workDay.getDate())){
						addNewWorkDay = false;
						break;
					}
				}

				if(addNewWorkDay) workDays_toAdd.add(new_workDay);
			}	
		
			this.addWorkDays(jobId, workDays_toAdd);
			this.deleteWorkDays(jobId, workDays_toDelete);
			this.updateWorkDayTimes(jobId, workDays_toEdit);
		}		
	}

	public void updateWorkDayTimes(int jobId, List<WorkDay> workDays) {

		//*************************************************************************
		//*************************************************************************
		// Need to check if the job's current employment is affected by the time change
		//*************************************************************************
		//*************************************************************************

		if(verificationService.isListPopulated(workDays)){

			List<Application> affectedApplications =
					applicationService.getApplications_byJobAndAtLeastOneWorkDay(jobId, workDays);

			for(Application affectedApplication : affectedApplications){
				EmploymentProposalDTO currentProposal = applicationService.getCurrentEmploymentProposal(
						affectedApplication.getApplicationId());


				applicationService.updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED, 1);

			}

			for(WorkDay workDay : workDays){
				repository.updateWorkDay(workDay.getWorkDayId(), workDay.getStringStartTime(),
						workDay.getStringEndTime());
			}
		}
	}

	public void deleteWorkDays(int jobId, List<WorkDay> workDays_toDelete) {
		
		
		if(verificationService.isListPopulated(workDays_toDelete)){

			Job job = getJob(jobId);

//			List<WorkDay> workDays_toDelete = getWorkDays_byJobAndDateStrings(jobId, dateStrings_toDelete);
			
			// Affected applications that are open (not accepted)
			List<Application> affectedApplications =
					applicationService.getApplications_byJobAndAtLeastOneWorkDay(
							jobId, workDays_toDelete);

			for(Application affectedApplication : affectedApplications){
				EmploymentProposalDTO currentProposal = applicationService.getCurrentEmploymentProposal(
						affectedApplication.getApplicationId());

				applicationService.updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);

			}

			// Affected employment
			List<Application> affectedApplications_employees = applicationService.
					getAcceptedApplications_byJobAndAtLeastOneWorkDay(jobId, workDays_toDelete);

			for(Application application : affectedApplications_employees){

				EmploymentProposalDTO acceptedProposal = applicationService.getCurrentEmploymentProposal(
						application.getApplicationId());

				// Update flags
				applicationService.updateProposalFlag(acceptedProposal,
						EmploymentProposalDTO.FLAG_APPLICATION_WAS_REOPENED, 1);

//				applicationService.updateProposalFlag(acceptedProposal,
//						EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);

				// Insert new proposal
				// The proposal will be in the employer's inbox
				EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
				newProposal.setApplicationId(application.getApplicationId());
				newProposal.setAmount(acceptedProposal.getAmount());
				newProposal.setProposedByUserId(application.getUserId());
				newProposal.setProposedToUserId(job.getUserId());
				newProposal.setStatus(EmploymentProposalDTO.STATUS_COUNTERED);
				newProposal.setDateStrings_proposedDates(
						removeConflictingWorkDays(acceptedProposal.getDateStrings_proposedDates(),
								workDays_toDelete));
							
				applicationService.insertEmploymentProposal(newProposal);
				
				// Set flags for the new proposal
				newProposal = applicationService.getCurrentEmploymentProposal(application.getApplicationId());
				applicationService.updateProposalFlag(newProposal, EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);

				// Undo this employment record
				applicationService.openApplication(application.getApplicationId());
				applicationService.updateApplicationFlag(application, "IsAccepted", 0);
				applicationService.deleteEmployment(application.getUserId(), application.getJobId());
			}

			repository.deleteProposedWorkDays(workDays_toDelete);
			repository.deleteWorkDays(workDays_toDelete);
			
//			updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 0);
		}
	}

//	public boolean setModel_editJob_removeWorkDays_preProcess(Model model, HttpSession session, int jobId,
//			List<String> dateStrings_remove) {
//
//		if(verificationService.didSessionUserPostJob(session, jobId)){
//
//			List<WorkDay> workDays = getWorkDays_byJobAndDateStrings(jobId, dateStrings_remove);
//
//			List<Application> affectedApplications_employees = applicationService.
//					getAcceptedApplications_byJobAndAtLeastOneWorkDay(jobId, workDays);
//
//			List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
//
//			for(Application application : affectedApplications_employees){
//				ApplicationDTO applicationDto = new ApplicationDTO();
//				applicationDto.setApplication(application);
//				applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));
//				applicationDtos.add(applicationDto);
//			}
//
//			model.addAttribute("applicationDtos", applicationDtos);
//			return true;
//
//		}
//
//		return false;
//	}

	public List<WorkDay> getWorkDays_byJobAndDateStrings(int jobId, List<String> dateStrings) {

		if(verificationService.isListPopulated(dateStrings)){
			return repository.getWorkDays_byJobAndDateStrings(jobId, dateStrings);
		}else return null;
	}

	public void setModel_findEmployees_byJob(Model model, HttpSession session, int jobId) {

		if(verificationService.didSessionUserPostJob(session, jobId)){
			
			Job job = getJob(jobId);
			String address = job.getStreetAddress_formatted() + " " 
					+ job.getCity_formatted() + " "
					+ job.getState() + " "
					+ job.getZipCode_formatted();
			Double radius = 25.0;
			
			EmployeeSearch employeeSearch = new EmployeeSearch();
			employeeSearch.setAddress(address);
			
			employeeSearch.setRadius(radius);
			employeeSearch.setWorkDays(getWorkDayDateStrings(jobId));
			
			userService.setModel_findEmployees_results(model, employeeSearch);
			
			model.addAttribute("job", job);			
//			model.addAttribute("doShowResultsOnPageLoad", true);
		}
	}

	public void inspectJob_isStillAcceptingApplications(int jobId) {

		boolean atLeastOneWorkDayIsNotFilled = false;
		List<WorkDayDto> workDayDtos = getWorkDayDtos(jobId);
		for( WorkDayDto workDayDto : workDayDtos){
			if( workDayDto.getCount_positionsFilled() < workDayDto.getCount_totalPositions() ){
				atLeastOneWorkDayIsNotFilled = true;
				break;
			}
		}

		if(!atLeastOneWorkDayIsNotFilled)
			updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 1);
	}

	private void updateJobFlag(int jobId, String flag, int value) {
		repository.updateJobFlag(jobId, flag, value);
	}

	public void editJob_removeRemainingWorkDays_forUser(int jobId, int userId, HttpSession session) {

		if(verificationService.didSessionUserPostJob(session, jobId)){

			List<WorkDay> workDays_incomplete = getWorkDays_incomplete_byUser(jobId, userId);

			if(workDays_incomplete != null){				
				updateEmploymentFlag(jobId, userId, "WasTerminated", 1);
				updateEmploymentFlag(jobId, userId, "Flag_EmployerTerminatedEmployee", 1);
				Application application = applicationService.getApplication(jobId, userId);
				applicationService.deleteProposedWorkDays(workDays_incomplete, application.getApplicationId());
				
				updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 0);
			}			
		}		
	}

	private List<WorkDay> getWorkDays_incomplete_byUser(int jobId, int userId) {	
		return repository.getWorkDays_incomplete_byUser(jobId, userId);
	}

	public boolean getIsJobComplete(Integer jobId) {
		List<WorkDay> workDays_incomplete = getWorkDays_incomplete(jobId);
		if(workDays_incomplete == null) return true;
		else return false;
	}

	public List<WorkDay> getWorkDays_incomplete(Integer jobId) {
		return repository.getWorkDays_incomplete(jobId);
	}

	public boolean getHasJobStarted(Integer jobId) {
		List<WorkDay> workDays_incomplete = getWorkDays_incomplete(jobId);
		List<WorkDay> workDays = getWorkDays(jobId);
		
		if(!verificationService.isListPopulated(workDays_incomplete)) return true;
		else if(workDays_incomplete.size() == workDays.size()){
			
			LocalDateTime startTime_firstWorkDay = LocalDateTime.of(
					workDays.get(0).getDate(),
					LocalTime.parse(workDays.get(0).getStringStartTime()));
			
			if(startTime_firstWorkDay.isBefore(LocalDateTime.now())) return true;
			else return false;
		}
		else return true;
	}

	public void leaveJob_employee(HttpSession session, int jobId) {
		
		// No need to check if the session user is actually an employee of this job.
		// The sql statement will simply not update any flags.
		if(!getIsJobComplete(jobId)){
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

		if(verificationService.didSessionUserPostJob(session, jobId) &&
				verificationService.didUserApplyForJob(jobId, userId_emloyee)){
			
			JobSearchUser user_employee = userService.getUser(userId_emloyee);
			model.addAttribute("user_employee", user_employee);			
		}		
	}

	public List<Job> getJobs_employment_byUserAndDate(int userId, String dateString) {
		return repository.getJobs_employment_byUserAndDate(userId, dateString);
	}

	public WorkDay getWorkDay(Integer jobId, String dateString) {
		return repository.getWorkDay(jobId, dateString);
	}

	public List<MessageResponse> getMessagesResponses_jobsTermintedFrom(int userId) {
		List<Job> jobs = repository.getJobs_terminatedFrom_byUser(userId);
		List<MessageResponse> messageResponses = new ArrayList<>();
		for (Job job : jobs){
			MessageResponse messageResponse = new MessageResponse();
			messageResponse.setJob(job);
			messageResponses.add(messageResponse);
		}
		return messageResponses;
	}

	
}
