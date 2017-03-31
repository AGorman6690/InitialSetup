package com.jobsearch.job.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Question;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

import javafx.print.PrinterJob.JobStatus;


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

	public void addPosting(JobDTO jobDto, HttpSession session) {

		
			// ***********************************************************************************
			// Should we verify the address on the client side? I'm thinking
			// so...
			// Why go all the way to the server
			// when the same logic can be handled in the browser?
			// ***********************************************************************************

		
			this.setLatAndLag(jobDto);


			if( jobDto.getJob().getLat() != null &&
				jobDto.getJob().getLng() != null &&
				areValidWorkDays(jobDto.getWorkDays())){

				JobSearchUser user = SessionContext.getUser(session);
	
				// ***************************************************************
				// Address this idea later.
				// Should we format the user's address and city per the data
				// from Google maps?
				// This would correct spelling errors, maintain consistent
				// letter casing, etc.
				// ***************************************************************
				// jobDto.setZipCode(GoogleClient.getAddressComponent(results[0].addressComponents,
				// "POSTAL_CODE"));
				// ***************************************************************



				repository.addJob(jobDto, user);

			} 
	
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
					LocalTime.parse(workDay.getStringStartTime());
					LocalTime.parse(workDay.getStringEndTime());
				} catch (Exception e) {
					return false;
				}
				
			}
		}
		
		return true;
	}

	public boolean areGeocodingResultsValid(GeocodingResult[] results) {
		if (results.length == 1) {
			return true;
		} else if (results.length == 0) {
			// invalid address
			return false;
		} else { // if (results.length > 1) {
			// if greater than 1, then ambiguous address
			return false;
		}
	}

	public void addWorkDays(int jobId, List<WorkDay> workDays) {

		for(WorkDay workDay : workDays){
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
			workDay.setDateId(this.getDateId(workDay.getDate().toString()));
			repository.addWorkDay(jobId, workDay);
		}

	}

	public int getDateId(String date) {
		return repository.getDateId(date);
	}

//	public List<JobDTO> getJobDtos_JobsWaitingToStart_Employer(int userId) {
//
//		List<JobDTO> jobDtos = new ArrayList<JobDTO>();
//
//		// Query the database
//		List<Job> jobsWaitingToStart = repository.getJobsByStatusAndByEmployer(userId, Job.STATUS_FUTURE);
//		
//		if(jobsWaitingToStart != null){
//			// Set the job Dtos
//			for(Job job : jobsWaitingToStart){
//				
//				JobDTO jobDto = new JobDTO();
//				jobDto = this.getJobDTO_JobWaitingToStart_Employer(job.getId(), userId);
//				jobDtos.add(jobDto);
//			}
//
//			return jobDtos;		
//		}
//
//		return null;
//
//	}
	
	public List<JobDTO> getJobDtos_employerProfile(int userId) {

		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> jobs = this.getJobs_byEmployerAndStatuses(userId, Arrays.asList(Job.STATUS_FUTURE,
																					Job.STATUS_PRESENT));
		if(jobs != null){

			for(Job job : jobs){
				
				JobDTO jobDto = new JobDTO();
				jobDto = this.getJobDTO_employerProfile(job, userId);
				jobDtos.add(jobDto);
			}

			return jobDtos;		
		}

		return null;

	}


	public List<Job> getJobs_byEmployerAndStatuses(int userId, List<Integer> jobStatuses) {
		return repository.getJobs_byEmployerAndStatuses(userId, jobStatuses);
	}

	public List<JobDTO> getJobDtos_JobsInProcess_Employer(int userId) {
		
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> jobsInProcess = repository.getJobsByStatusAndByEmployer(userId, Job.STATUS_PRESENT);
		
		//Set the job Dtos
		if(jobsInProcess != null){
			for(Job job : jobsInProcess){
				
				JobDTO jobDto = new JobDTO();
				jobDto.setJob(job);
	//			jobDto = this.getJobDTO_JobInProcess_Employer(job.getId());
				jobDtos.add(jobDto);
			}		
		
		}	
		
		return jobDtos;
	}
	
	public List<JobDTO> getJobDtos_JobsCompleted_Employer(int userId) {

		
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> jobsCompleted = repository.getJobsByStatusAndByEmployer(userId, Job.STATUS_PAST);
		
		//Set the job Dtos
		if(jobsCompleted != null){
			for(Job job : jobsCompleted){
				
				JobDTO jobDto = new JobDTO();
				jobDto.setJob(job);
				jobDtos.add(jobDto);
			}		
		}
		return jobDtos;

	}

	public int getNewApplicationCount(List<ApplicationDTO> applicationDtos) {
		
		return (int) applicationDtos.stream().filter(a -> a.getApplication()
															.getHasBeenViewed() == 0)
															.count();
	}

	public int getJobCountByCategory(int categoryId) {
		return repository.getJobCountByCategory(categoryId);
	}

	public int getSubJobCount(int categoryId, int count) {

		List<Category> categories;

		// For the categoryId passed as the paramenter, get the categories 1
		// level
		// deep
		categories = categoryService.getSubCategories_CALL_THIS_SOMETHING_DIFFERENT(categoryId);

		// For each category 1 level deep
		for (Category category : categories) {

			// Get its job count
			count += getJobCountByCategory(category.getId());

			// Recursively get the job count for the category 1 level deep
			count = getSubJobCount(category.getId(), count);
		}

		return count;
	}




	public List<JobDTO> getJobDtos_JobsCompleted_Employee(int userId) {
		
		//Get completed jobs
		List<Job> completedJobs = getCompletedJobsByEmployee(userId);
		
		// For each completed job, create a completed job response DTO
		List<JobDTO> jobDtos_jobsCompleted = new ArrayList<JobDTO>();
		
		for (Job job : completedJobs) {
			JobDTO jobDto = new JobDTO();

			jobDto.setJob(job);
			jobDto.setCategories(categoryService.getCategoriesByJobId(job.getId()));
			jobDto.setRatingDto(userService.getRatingDtoByUserAndJob(userId, job.getId()));
			jobDto.setDurationDays(this.getDuration(job.getId()));
			jobDto.setMilliseconds_endDate(job.getEndDate_local().toEpochDay());
			jobDtos_jobsCompleted.add(jobDto);
		}

		return jobDtos_jobsCompleted;		
	}

	public List<Job> getCompletedJobsByEmployee(int userId) {

		return repository.getJobsByStatusByEmployee(userId, 2);
	}

	public Job getJob(int jobId){
		return repository.getJob(jobId);

	}


	public List<Job> getJobs_ByFindJobFilter(FindJobFilterDTO filter, HttpSession session) {



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



	public Integer getDuration(int jobId) {
		return repository.getDuration(jobId);
	}

	public double getDistanceFromRequest(Job job, float fromLat, float fromLng) {
			return GoogleClient.getDistance(fromLat, fromLng, job.getLat(), job.getLng());
	}	

	public boolean getHaveJobRatingsBeenSubmitted(int jobId) {
		
		int unsubmittedRatingsCount = repository.getRatingCountByJobAndRatingValue(jobId, -1);
		
		if(unsubmittedRatingsCount == 0) return true;
		else return false;
		
	}


	public List<Job> getActiveJobsByEmployee(int userId) {

		return repository.getJobsByStatusByEmployee(userId, 1);
	}

	public List<Job> getYetToStartJobsByEmployee(int userId) {

		return repository.getJobsByStatusByEmployee(userId, 0);
	}


	public Job getJob_ByApplicationId(int applicationId) {

		return repository.getJobByApplicationId(applicationId);
	}


	public List<WorkDay> getWorkDays(int jobId) {
		return repository.getWorkDays(jobId);
	}
	

//	public List<WorkDay> getWorkDays(WageProposal wageProposal) {
//		
//		Application application = applicationService.getApplication(wageProposal.getApplicationId());
//		return getWorkDays(application.getJobId());
//	}



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
		jobDto.setCategories(categoryService.getCategoriesByJobId(jobId));
		jobDto.setSkillsRequired(this.getSkills_ByType(jobId, Skill.TYPE_REQUIRED_JOB_POSTING));
		jobDto.setSkillsDesired(this.getSkills_ByType(jobId, Skill.TYPE_DESIRED_JOB_POSTING));
		jobDto.setDurationDays(this.getDuration(jobId));

		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(job.getStartDate_local(),
												job.getStartTime_local(),
									job.getEndDate_local(), job.getEndTime_local(), DateUtility.TimeSpanUnit.Days));
		
		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));
		
		return jobDto;
	}
	

	
	private List<Skill> getSkills_ByType(int jobId, Integer type) {
		return repository.getSkills_ByType(jobId, type);
	}


	public JobDTO getJobDTO_employerProfile(Job job, int userId) {
		
		JobDTO jobDto = new JobDTO();
		
		jobDto.setJob(job);		
		
		// Wage Proposals
		jobDto.setCountWageProposals_sent(
				applicationService.getCountWageProposal_Sent(job.getId(), userId));
		
		jobDto.setCountWageProposals_received(
				applicationService.getCountWageProposal_Received(job.getId(), userId));
		
		jobDto.setCountWageProposals_received_new(
				applicationService.getCountWageProposal_Received_New(job.getId(), userId));
		
		// Applications
		jobDto.setCountApplications_new(
				applicationService.getCountApplications_new(job.getId()));
		
		jobDto.setCountApplications_received(
				applicationService.getCountApplications_received(job.getId()));
		
		jobDto.setCountApplications_declined(
				applicationService.getCountApplications_declined(job.getId()));
		
		// Employees
		jobDto.setCountEmployees_hired(
				applicationService.getCountEmployees_hired(job.getId()));
		
		
		// Other job details
		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(LocalDate.now(), LocalTime.now(), job.getStartDate_local(),
				job.getStartTime_local(), DateUtility.TimeSpanUnit.Days));

		
		return jobDto;
		
	}
//
//	public JobDTO getJobDTO_JobWaitingToStart_Employer(int jobId, int userId) {
//		
//		// ***************************************************
//		// ***************************************************
//		// If only the counts are going to be displayed, and not specific job details,
//		// then set an integer variable on the jobDto.
//		// There's no need to pass a list of objects (wage negotiations, applications, employees)
//		// Review this.
//		// ***************************************************
//		// ***************************************************
//		
//
//		JobDTO jobDto = new JobDTO();
//		
//		Job job = this.getJob(jobId);
//		jobDto.setJob(job);
//		
////		jobDto.setWorkDays(this.getWorkDays(jobId));
//		
////		jobDto.setQuestions(applicationService.getQuestions(jobId));
//		
////		jobDto.setFailedWageNegotiationDtos(applicationService.getFailedWageNegotiationDTOsByJob(job));
//
////		jobDto.setCategories(categoryService.getCategoriesByJobId(job.getId()));
//		
//		jobDto.setCountWageProposals_sent(
//				applicationService.getCountWageProposal_Sent(job.getId(), userId));
//		
//		jobDto.setCountWageProposals_received(
//				applicationService.getCountWageProposal_Received(job.getId(), userId));
//		
//		jobDto.setCountWageProposals_received_new(
//				applicationService.getCountWageProposal_Received_New(job.getId(), userId));
//
//		
//		jobDto.setEmployees(userService.getEmployeesByJob(job.getId()));
//		
//		List<ApplicationDTO> applicationDtos = applicationService.getApplicationDtos_ByJob_OpenApplications(job.getId());
//		jobDto.setApplicationDtos(applicationDtos);
//		jobDto.setNewApplicationCount(this.getNewApplicationCount(applicationDtos));
//
//		
//		System.err.println(jobDto.getJob().getUserId());
//		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(LocalDate.now(), LocalTime.now(), job.getStartDate_local(),
//												job.getStartTime_local(), DateUtility.TimeSpanUnit.Days));
//		
//		return jobDto;
//	}


	public void updateJobStatus(int status, int jobId) {
		
		// ***********************************************************
		// ***********************************************************
		// Job completion will be done automatically.
		// Once that is built, insert the inital ratings there
		if(status == Job.STATUS_PAST){
			userService.insertRatings_toRateEmployer(jobId);
			userService.insertRatings_toRateEmployees(jobId);
		}
		// ***********************************************************
		// ***********************************************************
		
		repository.updateJobStatus(status, jobId);
		
		

	}

	public List<Job> getJobsByEmployee(int employeeUserId) {
		
		return repository.getJobsByEmployee(employeeUserId);
	}

	public int getCount_ByJobsAndStatus(List<Job> jobs, int status) {
		
		if(jobs != null){
			return (int) jobs.stream().filter(j -> j.getStatus() == status).count();
		}
		else{
			return 0; 
		}
		
	}
	
	

	public void setModel_GetMoreJobs(Model model, FindJobFilterDTO filter, HttpSession session) {
		
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
		if(filter.getIsAppendingJobs()){
			filter.setJobIdsToExclude(SessionContext.getFilteredJobIds(session));
		}
		else{
			SessionContext.setFilteredJobIds(session, null);
			filter.setJobIdsToExclude(null);
		}
		
		// Per the filter request, get the jobs
		List<Job> jobs = this.getJobs_ByFindJobFilter(filter, session);

		List<JobDTO> jobDtos = this.getJobDtos_FilteredJobs(jobs, filter);
					
		model.addAttribute("jobDtos", jobDtos);
		
		SessionContext.appendToFilteredJobIds(session, this.getJobIdsFromJobDTOs(jobDtos));

	}
	
	

	public List<JobDTO> getJobDtos_FilteredJobs(List<Job> jobs, FindJobFilterDTO filter) {
	
		// For each job, set its:
		// a) categories
		// b) duration
		// c) distance from requested filter location
		
		if(jobs != null){

			
			List<JobDTO> jobDtos = new ArrayList<JobDTO>();
			
			for(Job job : jobs){			
			
				JobDTO jobDto = new JobDTO(); 
				jobDto.setJob(job);
				jobDto.setCategories(categoryService.getCategoriesByJobId(job.getId()));			
				jobDto.setDurationDays(this.getDuration(job.getId()));
								
				double distance = this.getDistanceFromRequest(job, filter.getLat(), filter.getLng());
				distance = MathUtility.round(distance, 1, 0);
				jobDto.setDistanceFromFilterLocation(distance);			
				
				jobDtos.add(jobDto);
			}

			return jobDtos;			
		}
		else return null;

	
	}

	public void setModel_GetJobs(Model model, FindJobFilterDTO filter, HttpSession session) {
		
		// Per the filter request, get the jobs
		List<Job> jobs = this.getJobs_ByFindJobFilter(filter, session);
		
		// Create job dtos
		List<JobDTO> jobDtos = this.getJobDtos_FilteredJobs(jobs, filter);
		
		// Set model and session
		this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, filter);	
		
		// Update the session variables
		SessionContext.setLastFilterRequest(session, filter);	
		SessionContext.setFilteredJobIds(session, this.getJobIdsFromJobDTOs(jobDtos));
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



	private double getMaxDistanceJobFromFilterRequest(List<JobDTO> jobDtos) {

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
		
		if(jobIds != null) return repository.getJobsByIds(jobIds);
		else return null;
		
	}


	public void setModel_FindJobs_PageLoad(Model model, HttpSession session) {
			
		
		// ****************************************
		// The user does not need to be logged-in in order to search for jobs
		// ****************************************
		
		// If logged in, set user account details
		if(SessionContext.isLoggedIn(session)){
			JobSearchUserDTO userDto = userService.getUserDTO_FindJobs_PageLoad(session);
			session.setAttribute("userDto", userDto);			
		}
		
		// Check if the user had previously loaded jobs
		List<Integer> jobIds_previouslyLoaded = SessionContext.getFilteredJobIds(session);
		
		FindJobFilterDTO lastFilterRequest = SessionContext.getLastFilterRequest(session);

		
		if(jobIds_previouslyLoaded != null && lastFilterRequest != null){
			
			// Query the database
			List<Job> jobs = this.getJobsByJobIds(SessionContext.getFilteredJobIds(session));

			// Create job dtos.
			// If necessary, filter the jobs
			List<JobDTO> jobDtos = new ArrayList<JobDTO>();
			if(lastFilterRequest.getSortBy() != null){
				jobDtos = getJobDtos_Sorted(jobs, lastFilterRequest);	
			}
			else{
				jobDtos = getJobDtos_FilteredJobs(jobs, lastFilterRequest);
			}		
			
			this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, lastFilterRequest);
			
		}
		
		
	}

	private List<JobDTO> getJobDtos_Sorted(List<Job> jobs, FindJobFilterDTO filterDto) {
		
		if(filterDto.getSortBy() != null){
		
		
			List<JobDTO> jobDtos = new ArrayList<JobDTO>();
			
			// Distance and duration are properties of the job dto, not the job.
			// Therefore, the dtos need to be created BEFORE they can be sorted.
			// Otherwise, the job objects are sorted first and then the job dtos are
			// created from the sorted job list. 
			if(filterDto.getSortBy().matches("Distance") || filterDto.getSortBy().matches("Duration")){
				jobDtos = this.getJobDtos_FilteredJobs(jobs, filterDto);
				this.sortJobDtos(jobDtos, filterDto.getSortBy(), filterDto.getIsAscending());
			}
			else{
				this.sortJobs(jobs, filterDto.getSortBy(), filterDto.getIsAscending());	
				jobDtos = this.getJobDtos_FilteredJobs(jobs, filterDto);
			}
			
			return jobDtos;
		}
		else return null;
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
	}

	private FindJobFilterDTO getSavedFindJobFilter(int savedFindJobFilterId, HttpSession session) {
		
		FindJobFilterDTO filter = repository.getSavedFindJobFilter(savedFindJobFilterId);
		
		if(filter != null){
			
			JobSearchUser sessionUser = SessionContext.getUser(session);
			
			if(filter.getUserId() == sessionUser.getUserId()){
				return filter;
			}
			else{
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

		JobDTO jobDto = this.getJobDTO_DisplayJobInfo(jobId);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		JobSearchUserDTO userDto = new JobSearchUserDTO();

		switch (context) {
		case "find":
			
			jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));
			
			if(sessionUser != null){
				
				jobDto.setApplication(applicationService.getApplication(jobId, sessionUser.getUserId()));
				
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

			userDto.setRatingDto(userService.getRatingDtoByUserAndJob(sessionUser.getUserId(), jobId));
			
			break;			
	
		
		}	
		
		model.addAttribute("context", context);
		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
		model.addAttribute("userDto", userDto);

				
	}

	private int getAvailabilityStatus(boolean isPartialAvailabilityAllowed, int userId,
										List<WorkDay> workDays) {
		
		
		if(verificationService.isListPopulated(workDays)){			
			
			Integer count_availableDays = this.getCount_availableDays_ByUserAndWorkDays(userId, workDays);
			Integer count_employmentDays = this.getCount_employmentDays_byUserAndWorkDays(userId, workDays);
			
			// If the user has **NO EMPLOYMENT** on these work days
			if(count_employmentDays <= 0) {
				
				if(count_availableDays == 0) return JobDTO.AVAILABILITY_STATUS_NONE_DUE_TO_AVAILABILITY_NOT_SET;
				else if(count_availableDays == workDays.size()) return JobDTO.AVAILABILITY_STATUS_COMPLETELY;
				else {
					if(isPartialAvailabilityAllowed) return JobDTO.AVAILABILITY_STATUS_PARTIAL_DUE_TO_AVAILABILITY_NOT_SET;
					else return JobDTO.AVAILABILITY_STATUS_NONE_DUE_TO_AVAILABILITY_NOT_SET;
				}
				
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
//		JobSearchUserDTO userDto = new JobSearchUserDTO();

		switch (context) {
		case "waiting":

			jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));
			jobDto.setApplicationDtos(applicationService.getApplicationDtos_ByJob_OpenApplications(jobId, session));
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
						
			applicationService.updateHasBeenViewed(jobDto.getJob(), 1);
			applicationService.updateWageProposalsStatus_ToViewedButNoActionTaken(jobDto.getJob().getId());
			
			break;

		case "in-process":
		
			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
														jobId, sessionUser.getUserId()));		
			jobDto.setApplicationDtos(applicationService.getApplicationDtos_ByJob_OpenApplications(jobId, session));
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
			break;
			
		
		case "complete":
			
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
			
			boolean haveJobRatingsBeenSubmitted = false; // this.getHaveJobRatingsBeenSubmitted(jobId);			
			model.addAttribute("haveJobRatingsBeenSubmitted", haveJobRatingsBeenSubmitted);
			
			break;	
			
		case "work-history":
			
//			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
//					jobId, sessionUser.getUserId()));
//			
//			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
			
			break;
		}	
		
		model.addAttribute("data_pageInit", data_pageInit);
		model.addAttribute("context", context);
//		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
		model.addAttribute("user", sessionUser);
//		model.addAttribute("userDto", userDto);
				
	}

	public List<JobDTO> getJobDtos_employment_currentAndFuture(int userId_employee) {

		List<Job> jobs = this.getJobs_ByEmployeeAndJobStatuses(userId_employee, 
													Arrays.asList(Job.STATUS_PRESENT, Job.STATUS_FUTURE));
		
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();
		
		for(Job job : jobs){
			
			JobDTO jobDto = new JobDTO();
			
			jobDto.setJob(job);
			jobDto.setWorkDays(this.getWorkDays(job.getId()));
			
			jobDtos.add(jobDto);
		}
		
		return jobDtos;
	}

	private List<Job> getJobs_ByEmployeeAndJobStatuses(int userId_employee, List<Integer> jobStatuses) {
		
		if(jobStatuses != null){
			if(jobStatuses.size() > 0){
				return repository.getJobs_ByEmplyeeAndStatuses(userId_employee, jobStatuses);
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
		
		JobDTO jobDto =getJobDTO_DisplayJobInfo(jobId);
				
		if(jobDto.getJob().getUserId() == SessionContext.getUser(session).getUserId()){
			
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


	public List<Job> getJobs_needRating_byUser(int userId) {
		
		return repository.getJobs_needRating_byUser(userId);
	}

	public boolean setModel_ViewRateEmployer(int jobId, Model model, HttpSession session) {
		
		JobSearchUser user = SessionContext.getUser(session);
		
		if(applicationService.wasUserEmployedForJob(user.getUserId(), jobId)){
			
			Job job = this.getJob(jobId);
			JobSearchUser employer = userService.getUser(this.getJob(jobId).getUserId());
			List<RateCriterion> rateCriteria = userService.getRatingCriteia_toRateEmployer() ;
					
			model.addAttribute("job", job);
			model.addAttribute("employer", employer);
			model.addAttribute("rateCriteria", rateCriteria);
			
			return true;
		}
		else return false;
		
	}
	

	public boolean setModel_ViewRateEmployees(int jobId, Model model, HttpSession session) {

		Job job = this.getJob(jobId);
		
		// Verify:
		// 1) The session user posted the job
		// and 2) The job is closed
		if(verificationService.didSessionUserPostJob(session, job) && 
				job.getStatus() == Job.STATUS_PAST){
			
			
			List<RateCriterion> rateCriteria = userService.getRatingCriteia_toRateEmployee();
			List<JobSearchUser> employees = userService.getEmployeesByJob(jobId);
			
			model.addAttribute("job", job);
			model.addAttribute("employees", employees);
			model.addAttribute("rateCriteria", rateCriteria);			
			
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

	public void setWorkDayDates(List<WorkDay> workDays) {

		for(WorkDay workDay : workDays){
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
		}
		
	}

	public void setModel_PreviewJobPost(Model model, JobDTO jobDto) {
		
		this.setLatAndLag(jobDto);
		
		this.setWorkDayDates(jobDto.getWorkDays());
		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));
			
		jobDto.setSkillsDesired(jobDto.getSkills().stream()
				.filter(s -> s.getType() == Skill.TYPE_DESIRED_JOB_POSTING).collect(Collectors.toList()));
	
		jobDto.setSkillsRequired(jobDto.getSkills().stream()
				.filter(s -> s.getType() == Skill.TYPE_REQUIRED_JOB_POSTING).collect(Collectors.toList()));

		jobDto.setCategories(categoryService.getCategories(jobDto.getCategoryIds()));
		model.addAttribute("jobDto", jobDto);
		
	}

	public int getCount_JobsCompleted_ByCategory(int userId, int categoryId) {
		
		return repository.getCount_JobsCompleted_ByCategory(userId, categoryId);
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

	public Integer getCount_availableDays_ByUserAndWorkDays(int userId, List<WorkDay> workDays) {
		
		// *************************************************
		// This takes into account the user's employment and the days
		// they have explicitly marked as "Available".
		// If they have marked the 1st and 2nd as "available", and the
		// work days are the 2nd and 3rd, then their availability count
		// is 1 (the 3rd is not counted, because it was not marked as "available".
		// *************************************************
		
		if(verificationService.isListPopulated(workDays)){
			return repository.getCount_availableDays_ByUserAndWorkDays(userId, workDays);	
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

	public void setCalendarInitData(JobDTO jobDto, List<WorkDay> workDays) {
		
		// ********************************************************
		// Convert all other instances of the below methods to use this method
		// ********************************************************
				
		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(workDays));
		
	}

	public List<String> getDateStrings_UnavailableWorkDays(int userId, List<WorkDay> workDays) {
		
		if(verificationService.isListPopulated(workDays)){			
			return repository.getDateStrings_UnavailableWorkDays(userId, workDays);				
		}else return null;
	}





}
