package com.jobsearch.job.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator.OfDouble;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmployeeSearch;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Question;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
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
		
		// **************************************************************
		// **************************************************************
		// In order to alert all applicants of this job, 
		// a flag on the job should be set
		// **************************************************************
		// **************************************************************

		for(WorkDay workDay : workDays){
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
			workDay.setDateId(this.getDateId(workDay.getDate().toString()));
			repository.addWorkDay(jobId, workDay);
		}

	}

	public int getDateId(String date) {
		return repository.getDateId(date);
	}

	
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
		List<Job> jobsCompleted = getJobs_byStatusAndByEmployer(userId, Job.STATUS_PAST);
		
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


	public List<Job> getJobs_byStatusAndByEmployer(int userId, Integer status) {
		
		return repository.getJobsByStatusAndByEmployer(userId, status);
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
			jobDto.setRatingDto(userService.getRatingDtoByUserAndJob(userService.getUser(userId), job.getId()));
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
		jobDto.setWorkDayDtos(getWorkDayDtos(jobId));
		jobDto.setCategories(categoryService.getCategoriesByJobId(jobId));
		jobDto.setSkillsRequired(this.getSkills_ByType(jobId, Skill.TYPE_REQUIRED_JOB_POSTING));
		jobDto.setSkillsDesired(this.getSkills_ByType(jobId, Skill.TYPE_DESIRED_JOB_POSTING));
		jobDto.setDurationDays(this.getDuration(jobId));

		jobDto.setDaysUntilStart(DateUtility.getTimeSpan(job.getStartDate_local(),
												job.getStartTime_local(),
									job.getEndDate_local(), job.getEndTime_local(), DateUtility.TimeSpanUnit.Days));
		
		jobDto.setAreAllTimesTheSame(areAllTimesTheSame(jobDto.getWorkDays()));
		setCalendarInitData(jobDto, jobDto.getWorkDays());
		
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
		jobDto.setCountApplications_total(
				applicationService.getCountApplications_total(job.getId()));		
		
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
				
				setCalendarInitData(jobDto, getWorkDays(job.getId()));
				
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
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
//		JobSearchUserDTO userDto = new JobSearchUserDTO();

		JobDTO jobDto = this.getJobDTO_DisplayJobInfo(jobId);
		for( WorkDayDto workDayDto : jobDto.getWorkDayDtos()){
			setWorkDayDto_conflictingEmployment(workDayDto, sessionUser.getUserId());
		}
		
		jobDto.setRatingValue_overall(userService.getRating(jobDto.getJob().getUserId()));
//		JobSearchUserDTO userDto_employer = new JobSearchUserDTO();
//		userDto_employer.setUser(userService.getUser(jobDto.getJob().getUserId()));
//		userDto_employer.setJobDtos_jobsCompleted(getJobDtos_JobsCompleted_Employer(
//				userDto_employer.getUser().getUserId()));
//		userDto_employer.setRatingValue_overall(userService.getRating(userDto_employer.getUser().getUserId()));
//		userDto_employer.setRatingDto(userService.getRatingDto_byUser(userDto_employer.getUser()));
//		
//		for( JobDTO jobDto_complete : userDto_employer.getJobDtos_jobsCompleted() ){
//			jobDto_complete.setRatingValue_overall(userService.getRating_byJobAndUser(
//					jobDto_complete.getJob().getId(), userDto_employer.getUser().getUserId()));
//			
//			jobDto_complete.setComments(getCommentsGivenToUser_byJob(
//					userDto_employer.getUser().getUserId(), jobDto_complete.getJob().getId()));
//		}
		
		userService.setModel_getRatings_byUser(model, jobDto.getJob().getUserId());
		

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

//			userDto.setRatingDto(userService.getRatingDtoByUserAndJob(sessionUser, jobId));
			
			break;			
	
		
		}	
		
		model.addAttribute("json_work_day_dtos", JSON.stringify(jobDto.getWorkDayDtos()));
		model.addAttribute("context", context);
		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
//		model.addAttribute("userDto", userDto);
//		model.addAttribute("user", sessionUser);

				
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
//		JobSearchUserDTO userDto = new JobSearchUserDTO();

		switch (context) {
		case "waiting":

			jobDto.setQuestions(applicationService.getQuestions(jobDto.getJob().getId()));
			jobDto.setApplicationDtos(applicationService.getApplicationDtos_ByJob_OpenApplications(jobId, session));
			jobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
			
			jobDto.setWorkDayDtos(getWorkDayDtos(jobId));
						
			applicationService.updateIsNew(jobDto.getJob(), 0);
			applicationService.updateWageProposalsStatus_ToViewedButNoActionTaken(
					jobDto.getJob().getId());
			
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
		
		model.addAttribute("json_work_day_dtos", JSON.stringify(getWorkDayDtos(jobId)));
		model.addAttribute("data_pageInit", data_pageInit);
		model.addAttribute("context", context);
//		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobDto", jobDto);
//		model.addAttribute("user", sessionUser);
//		model.addAttribute("userDto", userDto);
				
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

	public List<JobDTO> getJobDtos_employment_currentAndFuture(int userId_employee) {

		List<Job> jobs = this.getJobs_ByEmployeeAndJobStatuses(userId_employee, 
													Arrays.asList(Job.STATUS_PRESENT, Job.STATUS_FUTURE));
		
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();
		
		for(Job job : jobs){
			
			JobDTO jobDto = new JobDTO();
			
			jobDto.setJob(job);
			jobDto.setWorkDays(this.getWorkDays(job.getId()));
			
			Application application = applicationService.getApplication(job.getId(), userId_employee);
			EmploymentProposalDTO currentProposal = applicationService.getCurrentEmploymentProposal(
					application.getApplicationId());
			jobDto.setWorkDayDtos(getWorkDayDtos_byProposal(currentProposal));
			
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
				
			model.addAttribute("job", job);
			model.addAttribute("employer", employer);			
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
			
			List<JobSearchUser> employees = userService.getEmployeesByJob(jobId);
			
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

	public void setWorkDayDates(List<WorkDay> workDays) {

		for(WorkDay workDay : workDays){
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
		}
		
	}

	public void setModel_PreviewJobPost(Model model, JobDTO jobDto) {
		
		this.setLatAndLag(jobDto);
		this.formatAddress(jobDto.getJob());
		
		this.setWorkDayDates(jobDto.getWorkDays());
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
		
		model.addAttribute("json_work_day_dtos", JSON.stringify(jobDto.getWorkDayDtos()));
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
			Integer count_unavailableDays = repository.getCount_unavailableDays_ByUserAndWorkDays(userId, workDays); 
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

	public void setModel_getApplicants_byJobAndDate(Model model, HttpSession session, int jobId, String dateString) {
		
		if(verificationService.didSessionUserPostJob(session, jobId)){
			
			JobDTO jobDto = getJobDTO_DisplayJobInfo(jobId);
			
			List<Application> applications = applicationService.getApplications_byJobAndDate(jobId, dateString);		
			List<ApplicationDTO> applicationDtos = applicationService.getApplicationDtos(applications, session);
			jobDto.setApplicationDtos(applicationDtos);
			
			List<JobSearchUser> applicants_whoAreAvailableButDidNotApplyForDate = userService.getApplicants_whoAreAvailableButDidNotApplyForDate(jobId, dateString);
			List<ApplicationDTO> applicationDtos_applicantsWhoAreAvailableButDidNotApplyForDate =
												applicationService.getApplicationDtos_byApplicants(jobId, applicants_whoAreAvailableButDidNotApplyForDate, session);
			
			List<JobSearchUser> users_whoAreAvailableButHaveNotApplied = userService.getUsers_whoAreAvailableButHaveNotApplied(jobId, dateString);
			

			model.addAttribute("date", LocalDate.parse(dateString).format(DateTimeFormatter.ofPattern("E MMM d, y")));
			model.addAttribute("jobDto", jobDto);
			model.addAttribute("applicationDtos_applicantsWhoAreAvailableButDidNotApplyForDate",
					applicationDtos_applicantsWhoAreAvailableButDidNotApplyForDate);
			model.addAttribute("count_userswhoAreAvailableButHaveNotApplied",
					users_whoAreAvailableButHaveNotApplied.size());
		}

	}

	public List<WorkDayDto> getWorkDayDtos_byProposal(EmploymentProposalDTO proposal) {
		
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
			setWorkDayDto_conflictingEmployment(workDayDto, application.getUserId());
			
			// Conflicting applications
			workDayDto.setApplicationDtos_conflictingApplications(
					applicationService.getApplicationDtos_Conflicting(
							application.getUserId(), proposal.getApplicationId(), Arrays.asList(workDay)));
			workDayDtos.add(workDayDto);
		}
		return workDayDtos;
	}

	public Job getConflictingEmployment_byUserAndWorkDay(int userId, int workDayId) {
		
		return repository.getConflictingEmployment_byUserAndWorkDay(userId, workDayId);
	}

	public void setWorkDayDto_conflictingEmployment(WorkDayDto workDayDto, int userId ){
	
		workDayDto.setJob_conflictingEmployment(getConflictingEmployment_byUserAndWorkDay(
				userId, workDayDto.getWorkDay().getDateId()));
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
			
			List<JobSearchUser> users_employees = userService.getEmployeesByJob(jobId);
			if(users_employees != null){
				model.addAttribute("json_work_day_dtos",	JSON.stringify(getWorkDayDtos(jobId)));
				model.addAttribute("jobId", jobId);
				model.addAttribute("users_employees", users_employees);
			}else isValidRequest = false;

		}else isValidRequest = false;
		
		return isValidRequest;
	}

	public void replaceEmployee(HttpSession session, int jobId, int userId) {
		
		if(verificationService.didSessionUserPostJob(session, jobId)){
			repository.replaceEmployee(jobId, userId);
		}
		
	}

	public void editJob_workDays(HttpSession session, EditJobDto editJobDto) {
		
		if ( verificationService.didSessionUserPostJob(session, editJobDto.getJobId()) &&
				areValidWorkDays(editJobDto.getNewWorkDays()) ){
			
			for(WorkDay new_workDay : editJobDto.getNewWorkDays()){
				new_workDay.setDate(LocalDate.parse(new_workDay.getStringDate()));
			}
			
			
			List<WorkDay> workDays_toDelete = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toAdd = new ArrayList<WorkDay>();
			List<WorkDay> workDays_toEdit = new ArrayList<WorkDay>();
			
			List<WorkDay> current_workDays = getWorkDays(editJobDto.getJobId());
			
			// Delete or edit current work days
			for(WorkDay current_workDay : current_workDays){
				
				boolean deleteCurrentWorkDay = true;
				for(WorkDay new_workDay : editJobDto.getNewWorkDays()){
					
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
			for(WorkDay new_workDay : editJobDto.getNewWorkDays()){			
				
				boolean addNewWorkDay = true;
				for(WorkDay current_workDay : current_workDays){
					
					if(new_workDay.getDate().equals(current_workDay.getDate())){
						addNewWorkDay = false;
						break;
					}
				}
				
				if(addNewWorkDay) workDays_toAdd.add(new_workDay);
			}	

			
		
			this.addWorkDays(editJobDto.getJobId(), workDays_toAdd);
			this.deleteWorkDays(editJobDto.getJobId(), workDays_toDelete, editJobDto.getProposalDto());
			this.updateWorkDayTimes(editJobDto.getJobId(), workDays_toEdit);
			
			
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

	public void deleteWorkDays(int jobId, List<WorkDay> workDays_toDelete,
									EmploymentProposalDTO proposalDto_withExpirationTime) {
		
		if(verificationService.isListPopulated(workDays_toDelete)){
		
			Job job = getJob(jobId);
			
			// Affected applications that are open (not accepted)
			List<Application> affectedApplications = 
					applicationService.getApplications_byJobAndAtLeastOneWorkDay(jobId, workDays_toDelete);
			
			for(Application affectedApplication : affectedApplications){
				EmploymentProposalDTO currentProposal = applicationService.getCurrentEmploymentProposal(
						affectedApplication.getApplicationId());
					
				applicationService.updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);
				
//				repository.deleteProposedWorkDays(workDays_toDelete);			
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
				applicationService.updateProposalFlag(acceptedProposal,
						EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);
				
				// Insert new proposal
				EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
				newProposal.setApplicationId(application.getApplicationId());
				newProposal.setAmount(acceptedProposal.getAmount());
				newProposal.setProposedByUserId(job.getUserId());
				newProposal.setProposedToUserId(application.getUserId());
				newProposal.setStatus(EmploymentProposalDTO.STATUS_PENDING_APPLICANT_APPROVAL);
				newProposal.setEmployerAcceptedDate(LocalDateTime.now());				
				newProposal.setExpirationDate(applicationService
						.getExpirationDate(newProposal.getEmployerAcceptedDate(),
								proposalDto_withExpirationTime));
				newProposal.setDateStrings_proposedDates(
						removeConflictingWorkDays(acceptedProposal.getDateStrings_proposedDates(),
								workDays_toDelete));
				
				applicationService.insertEmploymentProposal(newProposal);
				
				// Undo this employment record
				applicationService.openApplication(application.getApplicationId());
				applicationService.deleteEmployment(application.getUserId(), application.getJobId());
			}
			
			repository.deleteProposedWorkDays(workDays_toDelete);		
			repository.deleteWorkDays(workDays_toDelete);
		}
	}

	public boolean setModel_editJob_removeWorkDays_preProcess(Model model, HttpSession session, int jobId,
			List<String> dateStrings_remove) {
		
		if(verificationService.didSessionUserPostJob(session, jobId)){
			
			List<WorkDay> workDays = getWorkDays_byJobAndDateStrings(jobId, dateStrings_remove);
			
			List<Application> affectedApplications_employees = applicationService.
					getAcceptedApplications_byJobAndAtLeastOneWorkDay(jobId, workDays);
			
			List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
						
			for(Application application : affectedApplications_employees){
				ApplicationDTO applicationDto = new ApplicationDTO();
				applicationDto.setApplication(application);
				applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));
				applicationDtos.add(applicationDto);
			}
			
			model.addAttribute("applicationDtos", applicationDtos);
			return true;
			
		}
		
		return false;
	}

	public List<WorkDay> getWorkDays_byJobAndDateStrings(int jobId, List<String> dateStrings) {
		
		if(verificationService.isListPopulated(dateStrings)){
			return repository.getWorkDays_byJobAndDateStrings(jobId, dateStrings);
		}else return null;
	}

	public void setModel_findEmployeesByJob(Model model, HttpSession session, int jobId) {
		
		if(verificationService.didSessionUserPostJob(session, jobId)){
			model.addAttribute("job", getJob(jobId));
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
	
}
