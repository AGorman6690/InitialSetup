package com.jobsearch.job.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.DateUtility.TimeSpanUnit;
import com.jobsearch.utilities.MathUtility;


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
	GoogleClient googleClient;

	public void addPosting(PostJobDTO postJobDto, HttpSession session) {

		
			// ***********************************************************************************
			// Should we verify the address on the client side? I'm thinking
			// so...
			// Why go all the way to the server
			// when the same logic can be handled in the browser?
			// ***********************************************************************************

			// Build the job's full address
			String address = postJobDto.getStreetAddress() + " " 
							+ postJobDto.getCity() + " " 
							+ postJobDto.getState() + " "
							+ postJobDto.getZipCode();

			GeocodingResult[] results = googleClient.getLatAndLng(address);

			if(areGeocodingResultsValid(results)){

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

				postJobDto.setLat((float) results[0].geometry.location.lat);
				postJobDto.setLng((float) results[0].geometry.location.lng);

				repository.addJob(postJobDto, user);

			} 
	
	}

	public boolean areGeocodingResultsValid(GeocodingResult[] results) {
		if (results.length == 1) {
			return true;
		} else if (results.length == 0) {
			// invalid address
			return false;
		} else { // if (results.length > 1) {
			// ambiguous address
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

	private int getDateId(String date) {
		return repository.getDateId(date);
	}

	public List<JobDTO> getJobDtos_JobsWaitingToStart_Employer(int userId) {

		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> jobsWaitingToStart = repository.getJobsByStatusAndByEmployer(userId, 0);

		//Set the job Dtos
		for(Job job : jobsWaitingToStart){
			
			JobDTO jobDto = new JobDTO();
			jobDto = this.getJobDTO_JobWaitingToStart_Employer(job.getId());
			jobDtos.add(jobDto);
		}

		return jobDtos;

	}


	public List<JobDTO> getJobDtos_JobsInProcess_Employer(int userId) {
		
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> jobsInProcess = repository.getJobsByStatusAndByEmployer(userId, 1);
		
		//Set the job Dtos
		for(Job job : jobsInProcess){
			
			JobDTO jobDto = new JobDTO();
			jobDto.setJob(job);
//			jobDto = this.getJobDTO_JobInProcess_Employer(job.getId());
			jobDtos.add(jobDto);
		}		

		return jobDtos;

	}
	
	public List<JobDTO> getJobDtos_JobsCompleted_Employer(int userId) {

		
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> jobsCompleted = repository.getJobsByStatusAndByEmployer(userId, 2);
		
		//Set the job Dtos
		for(Job job : jobsCompleted){
			
			JobDTO jobDto = new JobDTO();
			jobDto.setJob(job);
			jobDtos.add(jobDto);
		}		

		return jobDtos;

	}

	public int getNewApplicationCount(List<Application> applications) {
		return (int) applications.stream().filter(a -> a.getHasBeenViewed() == 0).count();

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


	public List<Job> getJobsByFindJobFilter(FindJobFilterDTO filter, HttpSession session) {



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

	public List<JobDTO> getJobsWithFailedWageNegotiations(int userId, List<Job> jobs) {

		List<JobDTO> result = new ArrayList<JobDTO>();

		for(Job job : jobs){

			//Create a job dto
			JobDTO jobDto = new JobDTO();

			//Set the dto's job object
			jobDto.setJob(job);

			//Get the failed wage proposals for the job
			jobDto.setFailedWageNegotiationDtos(applicationService.getFailedWageNegotiationDTOsByJob(job));;

			//If there are failed negotiations, add the dto to the result
			if(jobDto.getFailedWageNegotiationDtos().size() > 0){
				result.add(jobDto);
			}

		}


		return result;
	}


	public Job getJobByApplicationId(int applicationId) {

		return repository.getJobByApplicationId(applicationId);
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



	private JobDTO getJobDTO_DisplayJobInfo(int jobId) {
		
		JobDTO jobDto = new JobDTO();
		
		Job job = this.getJob(jobId);
		jobDto.setJob(job);
		jobDto.setWorkDays(this.getWorkDays(jobId));
		jobDto.setCategories(categoryService.getCategoriesByJobId(jobId));
		
		this.setJobDtoDuration(jobDto);
		
		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(job.getStartDate_local(),
												job.getStartTime_local(),
									job.getEndDate_local(), job.getEndTime_local(), DateUtility.TimeSpanUnit.Days));
		
		jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
		jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));
		
		return jobDto;
	}
	
	private JobDTO getJobDTO_JobWaitingToStart_Employer(int jobId) {
		
		JobDTO jobDto = new JobDTO();
		
		Job job = this.getJob(jobId);
		jobDto.setJob(job);
		
		jobDto.setWorkDays(this.getWorkDays(jobId));
		
		jobDto.setQuestions(applicationService.getQuestions(jobId));
		
		jobDto.setFailedWageNegotiationDtos(applicationService.getFailedWageNegotiationDTOsByJob(job));
		
		jobDto.setCategories(categoryService.getCategoriesByJobId(job.getId()));	
		
		jobDto.setEmployees(userService.getEmployeesByJob(job.getId()));
		
		List<Application> applications = applicationService.getApplicationsByJob(job.getId());
		jobDto.setApplications(applications);
		jobDto.setNewApplicationCount(this.getNewApplicationCount(applications));
		
		this.setJobDtoDuration(jobDto);
		
		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(LocalDate.now(), LocalTime.now(), job.getStartDate_local(),
												job.getStartTime_local(), DateUtility.TimeSpanUnit.Days));
		
		return jobDto;
	}
	

	public void setJobDtoDuration(JobDTO jobDto) {

		if(jobDto.getJob() != null &&
			jobDto.getJob().getStartDate() != null &&
			jobDto.getJob().getEndDate() != null &&
			jobDto.getJob().getStartTime() != null &&
			jobDto.getJob().getEndTime() != null){
			
			jobDto.setDurationHours(DateUtility.getTimeSpan(jobDto.getJob().getStartDate(), 
												jobDto.getJob().getStartTime(),
												jobDto.getJob().getEndDate(), 
												jobDto.getJob().getEndTime(), TimeSpanUnit.Hours));
			
			jobDto.setDurationDays(DateUtility.getTimeSpan(jobDto.getJob().getStartDate(), 
													null,
													jobDto.getJob().getEndDate(), 
													null, TimeSpanUnit.Days).intValue());
		}
		
	}


	public void updateJobStatus(int status, int jobId) {
		repository.updateJobStatus(status, jobId);

	}

	public List<Job> getJobsByEmployee(int employeeUserId) {
		
		return repository.getJobsByEmployee(employeeUserId);
	}

	public int getJobCountByStatus(List<Job> jobs, int status) {
		
		if(jobs != null){
			return (int) jobs.stream().filter(j -> j.getStatus() == status).count();
		}
		else{
			return 0; 
		}
		
	}
	
	

	public void setModel_GetMoreJobs(Model model, FindJobFilterDTO filter, HttpSession session) {
	
		// If getting more jobs, exclude the already-loaded job ids
		if(filter.getIsAppendingJobs()){
			filter.setJobIdsToExclude(SessionContext.getFilteredJobIds(session));
		}
		else{
			SessionContext.setFilteredJobIds(session, null);
			filter.setJobIdsToExclude(null);
		}
		
		// Per the filter request, get the jobs
		List<Job> jobs = this.getJobsByFindJobFilter(filter, session);

		List<JobDTO> jobDtos = this.getJobDtos_FilteredJobs(jobs, filter);
			
		model.addAttribute("jobDtos", jobDtos);
		
		SessionContext.appendToFilteredJobIds(session, this.getJobIdsFromJobDTOs(jobDtos));

	}
	
	

	private List<JobDTO> getJobDtos_FilteredJobs(List<Job> jobs, FindJobFilterDTO filter) {
	
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
		List<Job> jobs = this.getJobsByFindJobFilter(filter, session);
		
		// Create job dtos
		List<JobDTO> jobDtos = this.getJobDtos_FilteredJobs(jobs, filter);
		
		// Set model and session
		this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, filter);				
		session.setAttribute("lastFilterRequest", filter);		
		SessionContext.setFilteredJobIds(session, this.getJobIdsFromJobDTOs(jobDtos));
	}
	
	public void setModel_SortFilteredJobs(HttpSession session, Model model, String sortBy, boolean isAscending) {

		FindJobFilterDTO lastFilterRequest =  SessionContext.getLastFilterRequest(session);
		
		List<Job> jobs = this.getJobsByJobIds(SessionContext.getFilteredJobIds(session));
		List<JobDTO> jobDtos = this.getJobDtos_Sorted(jobs, lastFilterRequest);
	
		this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, lastFilterRequest);
		
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
		
		if(jobIds_previouslyLoaded != null){
			
			// Query the database
			List<Job> jobs = this.getJobsByJobIds(SessionContext.getFilteredJobIds(session));

			// Create job dtos.
			// If necessary, filter the jobs
			FindJobFilterDTO lastFilterRequest = SessionContext.getLastFilterRequest(session);
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

	public void setModel_ViewJob_Employee(Model model, HttpSession session, String context, int jobId) {

		// **************************************************
		// **************************************************
		// Need to add verification for all contexts
		// **************************************************
		// **************************************************

		String viewName = null;
		JobDTO jobDto = this.getJobDTO_DisplayJobInfo(jobId);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		JobSearchUserDTO userDto = new JobSearchUserDTO();

		switch (context) {
		case "find":
			
			jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));

			
			break;

			
		case "profile-incomplete":
		
			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
														jobId, sessionUser.getUserId()));	
			
			break;
			
		
		case "profile-complete":
			
			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
					jobId, sessionUser.getUserId()));

			userDto.setRating(userService.getRatingDtoByUserAndJob(sessionUser.getUserId(), jobId));
			
			break;			
	
		
		}	
		
		model.addAttribute("context", context);
		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
		model.addAttribute("userDto", userDto);

				
	}

	public void setModel_ViewJob_Employer(Model model, HttpSession session, String context, int jobId) {

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
			jobDto.setApplications(applicationService.getApplicationsByJob(jobId));
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
			break;

		case "in-process":
		
			jobDto.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(
														jobId, sessionUser.getUserId()));		
			jobDto.setApplications(applicationService.getApplicationsByJob(jobId));
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
		
		model.addAttribute("context", context);
//		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
//		model.addAttribute("userDto", userDto);
				
	}





}
