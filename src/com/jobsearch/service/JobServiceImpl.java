package com.jobsearch.service;

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
import org.hibernate.loader.plan.exec.process.spi.ReturnReader;
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
import com.jobsearch.dtos.CompletedJobDto;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.web.FindJobFilterDTO;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmployeeSearch;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.proposal.service.ProposalServiceImpl;
import com.jobsearch.request.EditJobRequest;
import com.jobsearch.responses.ApplicationProgressResponse;
import com.jobsearch.responses.ApplicationProgressResponse.ApplicationProgressStatus;
import com.jobsearch.responses.GetJobResponse;
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
	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	QuestionServiceImpl questionService;	

	public void addPosting(JobDTO jobDto, HttpSession session) {

		if(	( jobDto.getJob().getCity() != null || jobDto.getJob().getCity() != "" ) &&
			( jobDto.getJob().getState() != null || jobDto.getJob().getState() != "" ) &&
			( jobDto.getJob().getStreetAddress() != null || jobDto.getJob().getStreetAddress() != "" ) ){

			if( verificationService.isValidLocation(jobDto.getJob()) &&
				workDayService.areValidWorkDays(jobDto.getWorkDays())){

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
		jobDto.setWorkDays(workDayService.getWorkDays(jobId));
		jobDto.setWorkDayDtos(workDayService.getWorkDayDtos(jobId));
		jobDto.setCategories(categoryService.getCategoriesByJobId(jobId));
		jobDto.setSkillsRequired(this.getSkills_ByType(jobId, Skill.TYPE_REQUIRED_JOB_POSTING));
		jobDto.setSkillsDesired(this.getSkills_ByType(jobId, Skill.TYPE_DESIRED_JOB_POSTING));

		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(job.getStartDate_local(),
												job.getStartTime_local(),
									job.getEndDate_local(), job.getEndTime_local(), DateUtility.TimeSpanUnit.Days));

		jobDto.setAreAllTimesTheSame(workDayService.areAllTimesTheSame(jobDto.getWorkDays()));

		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));

		return jobDto;
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
			List<WorkDay> workDays = workDayService.getWorkDays(jobId);
			for( WorkDay workDay : workDays){
				workDayService.updateWorkDay_isComplete(workDay.getWorkDayId(), 1);
			}
		}
		// ***********************************************************
		// ***********************************************************

		repository.updateJobStatus(status, jobId);
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

				List<WorkDay> workDays = workDayService.getWorkDays(job.getId());
				jobDto.setWorkDays(workDayService.getWorkDays(job.getId()));

				double distance = GoogleClient.getDistance(filter.getLat(), filter.getLng(), job.getLat(),
						job.getLng());
				distance = MathUtility.round(distance, 1, 0);
				jobDto.setDistanceFromFilterLocation(distance);

				jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
				jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(workDays));
				
				jobDto.getEmployerDto().setUser(userService.getUser(job.getUserId()));
				jobDto.getEmployerDto().setRatingValue_overall(ratingService.getRating(job.getUserId()));

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


	public void setGetJobResponse(Model model, HttpSession session,
			String context, int jobId) {

		
		Job job = getJob(jobId);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		List<WorkDay> workDays = workDayService.getWorkDays(jobId);
		
		GetJobResponse response = new GetJobResponse();
		response.setJob(job);
		//response.setWorkDayDtos(getWorkDayDtos(jobId));
		response.setJson_workDayDtos(JSON.stringify(workDayService.getWorkDayDtos(jobId)));
		response.setIsPreviewingBeforeSubmittingJobPost(false);
		response.setProfileInfoDto(
			userService.getProfileInfoDto(userService.getUser(job.getUserId())));
		response.setContext(context);
		response.setSkillsRequired(getSkills_ByType(jobId, Skill.TYPE_REQUIRED_JOB_POSTING));
		response.setSkillsDesired(getSkills_ByType(jobId, Skill.TYPE_DESIRED_JOB_POSTING));
		response.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
		response.setMonthSpan_allWorkDays(DateUtility.getMonthSpan(workDays));
		response.setQuestions(questionService.getQuestionsWithAnswersByJobAndUser(
				jobId, sessionUser.getUserId()));
		
//		response.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
//				jobId, sessionUser.getUserId()));
		
		if(sessionUser != null && sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE){
			response.setApplication(applicationService.getApplication(jobId, sessionUser.getUserId()));
		}
		
		model.addAttribute("response", response);
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
						ratingService.getRating(applicant.getUserId()));
				applicationProgressStatus.setApplicantId(applicant.getUserId());

				// Answers
				applicationProgressStatus.setQuestions(questionService.getQuestionsWithAnswersByJobAndUser(
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
		List<Question> postedQuestions = questionService.getDistinctQuestions_byEmployer(sessionUser.getUserId());

		model.addAttribute("postedJobs", postedJobs);
		model.addAttribute("postedQuestions", postedQuestions);

	}

	private List<Job> getJobs_ByEmployer(int userId) {
		return repository.getJobs_ByEmployer(userId);
	}

	public JobDTO getJobDto_PreviousPostedJob(HttpSession session, int jobId) {

		JobDTO jobDto = getJobDto_ByEmployer(session, jobId);

		if(jobDto != null){
			jobDto.setQuestions(questionService.getQuestions(jobId));
			return jobDto;
		}
		else return null;
	}


	public List<Job> getJobs_needRating_byEmployee(int userId) {
		
		List<Job> jobs_completed = getJobs_completedByUser(userId);
		if(verificationService.isListPopulated(jobs_completed)){
			List<Job> jobs_needRating = new ArrayList<Job>();
			for(Job job : jobs_completed){
				Integer count_nullRatings = ratingService.getCount_nullRatings_givenByUserForJob(job.getId(), userId);
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

	public String getDate(int dateId) {
		return repository.getDateId(dateId);
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


	public Job getConflictingEmployment_byUserAndWorkDay(int jobId_reference, int userId, int workDayId) {
		return repository.getConflictingEmployment_byUserAndWorkDay(
				jobId_reference,userId, workDayId);	}









	public boolean setModel_viewReplaceEmployees(Model model, HttpSession session, int jobId) {

		boolean isValidRequest = true;
		if(verificationService.didSessionUserPostJob(session, jobId)){

			List<JobSearchUser> users_employees = userService.getUsersEmployedForJob(jobId);
			if(users_employees != null){
				model.addAttribute("json_work_day_dtos",	JSON.stringify(workDayService.getWorkDayDtos(jobId)));
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

	public void editJobRequest(HttpSession session, EditJobRequest request) {
		
		if ( verificationService.didSessionUserPostJob(session, request.getJobId()) &&
				workDayService.areValidWorkDays(request.getNewWorkDays()) ){
			
			for(WorkDay new_workDay : request.getNewWorkDays()){
				new_workDay.setDate(LocalDate.parse(new_workDay.getStringDate()));
			}			
			
			List<WorkDay> workDays_toDelete = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toAdd = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toEdit = new ArrayList<WorkDay>();			
			List<WorkDay> current_workDays = workDayService.getWorkDays(request.getJobId());
			
			// Delete or edit current work days
			for(WorkDay current_workDay : current_workDays){
				boolean deleteCurrentWorkDay = true;
				for(WorkDay new_workDay : request.getNewWorkDays()){
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
			for(WorkDay new_workDay : request.getNewWorkDays()){
				boolean addNewWorkDay = true;
				for(WorkDay current_workDay : current_workDays){
					if(new_workDay.getDate().equals(current_workDay.getDate())){
						addNewWorkDay = false;
						break;
					}
				}
				if(addNewWorkDay) workDays_toAdd.add(new_workDay);
			}	
		
			workDayService.addWorkDays(request.getJobId(), workDays_toAdd);
			workDayService.deleteWorkDays(request.getJobId(), workDays_toDelete);
			workDayService.updateWorkDayTimes(request.getJobId(), workDays_toEdit);
		}		
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
			employeeSearch.setWorkDays(workDayService.getWorkDayDateStrings(jobId));
			
			userService.setModel_findEmployees_results(model, employeeSearch);
			
			model.addAttribute("job", job);			
//			model.addAttribute("doShowResultsOnPageLoad", true);
		}
	}

	public void inspectJob_isStillAcceptingApplications(int jobId) {

		boolean atLeastOneWorkDayIsNotFilled = false;
		List<WorkDayDto> workDayDtos = workDayService.getWorkDayDtos(jobId);
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

			List<WorkDay> workDays_incomplete = workDayService.getWorkDays_incomplete_byUser(jobId, userId);

			if(workDays_incomplete != null){				
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
		if(workDays_incomplete == null) return true;
		else return false;
	}



	public boolean getHasJobStarted(Integer jobId) {
		List<WorkDay> workDays_incomplete = workDayService.getWorkDays_incomplete(jobId);
		List<WorkDay> workDays = workDayService.getWorkDays(jobId);
		
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

	public List<CompletedJobDto> getCompletedJobDtos(JobSearchUser user) {		
		List<Job> completedJobs = getCompletedJobs(user);		
		List<CompletedJobDto> completedJobDtos = new ArrayList<>();
		for (Job completedJob : completedJobs) {						
			Double rating = ratingService.getRating_byJobAndUser(completedJob.getId(),
					user.getUserId());			
			if(rating != null) {
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
			completedJobs = getJobs_ByEmployeeAndJobStatuses(user.getUserId(),
					Arrays.asList(Job.STATUS_PAST));
		} else {
			completedJobs = getJobs_byEmployerAndStatuses(user.getUserId(),
					Arrays.asList(Job.STATUS_PAST));
		}
		return completedJobs;
	}
	
	

	
}
