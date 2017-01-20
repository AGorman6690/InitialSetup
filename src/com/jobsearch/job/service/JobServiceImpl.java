package com.jobsearch.job.service;

import java.io.StringWriter;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.hibernate.mapping.Collection;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.model.Answer;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Question;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;
import com.jobsearch.utilities.DateUtility.TimeSpanUnit;


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

	@Autowired
	@Qualifier("FilterJobsVM")
	Template filterJobsTemplate;

	@Autowired
	@Qualifier("EmployerProfileJobTableVM")
	Template vmTemplate_employerProfileJobTable;

	@Autowired
	@Qualifier("JobInformationVM")
	Template vtJobInfo;

	@Autowired
	@Qualifier("EmployerJobsYetToStartVM")
	Template employerJobsYetToStartTemplate;

	@Autowired
	@Qualifier("EmployerJobsInProcessVM")
	Template employerJobsInProcessTemplate;

	@Autowired
	@Qualifier("QuestionsVM")
	Template questionsTemplate;

	@Autowired
	@Qualifier("AnswersVM")
	Template answersTemplate;

	@Autowired
	@Qualifier("QuestionsToAnswerVM")
	Template questionsToAnswer;

	public void addPosting(SubmitJobPostingRequestDTO postingDto, JobSearchUser user) {

		for (Job job : postingDto.getJobs()) {
			// ***********************************************************************************
			// Should we verify the address on the client side? I'm thinking
			// so...
			// Why go all the way to the server
			// when the same logic can be handled in the browser?
			// ***********************************************************************************

			// Build the job's full address
			String address = job.getStreetAddress() + " " + job.getCity() + " " + job.getState() + " "
					+ job.getZipCode();

			GeocodingResult[] results = googleClient.getLatAndLng(address);

			if (results.length == 1) {



//				// Convert strings to sql Date objects
//				jobDto.setStartDate(DateUtility.getSqlDate(jobDto.getStringStartDate()));
//				jobDto.setEndDate(DateUtility.getSqlDate(jobDto.getStringEndDate()));
//
//				// Convert strings to sql Time objects
//				jobDto.setStartTime(java.sql.Time.valueOf(jobDto.getStringStartTime()));
//				jobDto.setEndTime(java.sql.Time.valueOf(jobDto.getStringEndTime()));

				// Address this idea later.
				// Should we format the user's address and city per the data
				// from Google maps?
				// This would correct spelling errors, maintain consistent
				// letter casing, etc.
				// ***************************************************************
				// jobDto.setZipCode(GoogleClient.getAddressComponent(results[0].addressComponents,
				// "POSTAL_CODE"));
				// ***************************************************************

				job.setLat((float) results[0].geometry.location.lat);
				job.setLng((float) results[0].geometry.location.lng);

				job.setQuestions(getQuestionsFromPosting(job.getSelectedQuestionIds(), postingDto.getQuestions()));

				repository.addJob(job, user);

			} else if (results.length == 0) {
				// invalid address
			} else if (results.length > 1) {
				// ambiguous address
			}
		}
	}

	public void addWorkDays(int jobId, List<WorkDay> workDays) {

		for(WorkDay workDay : workDays){
			workDay.setDate(DateUtility.getSqlDate(workDay.getStringDate()));
			repository.addWorkDay(jobId, workDay);
		}

	}

	private List<PostQuestionDTO> getQuestionsFromPosting(List<Integer> selectedQuestionIds, List<PostQuestionDTO> postingDtoQuestions) {

		List<PostQuestionDTO> questions = new ArrayList<PostQuestionDTO>();

		for(int selectedQuestionId : selectedQuestionIds){
			//Get question
			for(PostQuestionDTO postingDtoQuestion : postingDtoQuestions){
				if(postingDtoQuestion.getId() == selectedQuestionId){
					questions.add(postingDtoQuestion);
				}
			}
		}
		return questions;
	}


//	public List<Job> getYetToStartJobsByEmployer(int userId) {
//
//		//Query the database
//		List<Job> yetToStartJobs = repository.getJobsByStatusAndByEmployer(userId, 0);
//
//		//Set the jobs application summary (i.e. applicants and employees)
//		setJobsApplicationSummary(yetToStartJobs);
//
//		return yetToStartJobs;
//	}

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


//	private void setJobsApplicationSummary(List<Job> jobs) {
//
//		for (Job job : jobs) {
//			this.setJobApplicationSummary(job);		}
//
//	}

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

	public List<CompletedJobResponseDTO> getCompletedJobsByEmployer(int userId) {
		// FYI : As of now, this method could return a list of Jobs, not
		// CompletedJobDTOs, and still
		// contain all the information that is currently being rendered on jsp
		// pages.
		// It is returning CompletedJobDTO because the
		// getCompletedJobsBy***Employee*** needs CompltedJobDTOs.
		// Rather than creating two separate list properties in the user class
		// to hold both Job objects
		// and CompltedJobDTO objects, I chose this because in the future
		// perhaps additional completed job info
		// (i.e. past employees, comments, ratings) would also be nice to
		// display on the employer page.

		List<Job> completedJobs = repository.getJobsByStatusAndByEmployer(userId, 2);

		List<CompletedJobResponseDTO> completedJobDtos = new ArrayList<CompletedJobResponseDTO>();
		for (Job job : completedJobs) {
			CompletedJobResponseDTO completedJobDto = new CompletedJobResponseDTO();
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
			completedJobDto.setJob(job);
			completedJobDtos.add(completedJobDto);
		}

		return completedJobDtos;
	}

	public List<CompletedJobResponseDTO> getCompletedJobResponseDtosByEmployee(int userId) {

		//Get completed jobs
		List<Job> completedJobs = getCompletedJobsByEmployee(userId);

		// For each completed job, create a completed job response DTO
		List<CompletedJobResponseDTO> completedJobDtos = new ArrayList<CompletedJobResponseDTO>();
		for (Job job : completedJobs) {
			CompletedJobResponseDTO completedJobDto = new CompletedJobResponseDTO();

			// Set the job's categories
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

			// Set the DTO's properties
			completedJobDto.setJob(job);
			completedJobDto.setComment(userService.getComment(job.getId(), userId));
			completedJobDto.setRating(userService.getRatingForJob(userId, job.getId()));
			completedJobDto.setEndorsements(userService.getUsersEndorsementsByJob(userId, job.getId()));

			// Add the DTO
			completedJobDtos.add(completedJobDto);
		}

		return completedJobDtos;
	}

	public List<Job> getCompletedJobsByEmployee(int userId) {

		return repository.getJobsByStatusByEmployee(userId, 2);
	}

	public Job getJob(int jobId){
		return repository.getJob(jobId);

	}

	public void setPostingInfoForJob(Job job) {
		// This only sets the job properties that relate to the job posting
		// i.e. no applicants, no employees, etc.

		// Set job categories
		job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

		// Set job questions
//		job.setQuestions(applicationService.getQuestions(job.getId()));

	}

	public Job getEmployersJobProfile(int jobId) {
		// This sets almost(?) all of the job's properties

		Job job = repository.getJob(jobId);

		// Set job categories
		job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

		//****************************************************************************************
		//****************************************************************************************
		//Why does the job have both applications and applicants???????
		//Shouldn't applicants be enough?????
		//Each application object has an applicant property.
		//This is redundant.
		//I will address this.
		//UPDATE:
		//This is done this way because the "Status" is a property of the application, not the applicant.
		//I think removing the applicants from the job is the way to go.
		//Check back later.
//		job.setApplications(applicationService.getApplicationsByJob(jobId));
		//****************************************************************************************
		//****************************************************************************************


//		// Set job questions
//		job.setQuestions(applicationService.getQuestions(job.getId()));

		// // Set job applicants
		// job.setApplicants(userService.getApplicants(jobId));

		// Set each applicant's rating, application, and endorsements only for
		// the job's categories
		// for (JobSearchUser applicant : job.getApplicants()) {
		// //
		// applicant.setRatings(userService.getRatings(applicant.getUserId()));
		// applicant.setRating(userService.getRating(applicant.getUserId()));
		// applicant.setApplication(applicationService.getApplication(jobId,
		// applicant.getUserId()));
		// applicant.setEndorsements(
		// userService.getUserEndorsementsByCategory(applicant.getUserId(),
		// job.getCategories()));
		// //
		// applicant.setAnswers(applicationService.getAnswers(job.getQuestions(),
		// applicant.getUserId()));
		// }

		// Get job employees
		job.setEmployees(userService.getEmployeesByJob(jobId));

		// Set each employee's rating, application and endorsements
		for (JobSearchUser employee : job.getEmployees()) {
			// employee.setRatings(userService.getRatings(employee.getUserId()));
//			employee.setRating(userService.getRating(employee.getUserId()));
//			employee.setApplication(applicationService.getApplication(jobId, employee.getUserId()));
//			employee.setEndorsements(
//					userService.getUserEndorsementsByCategory(employee.getUserId(), job.getCategories()));
		}

		// // Set each employee's rating
		// for(JobSearchUser employee : job.getEmployees()){
		// employee.setRatings(userService.getRatings(employee.getUserId()));
		// }

		return job;
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
		
//		SessionContext.updateFilteredJobIds(filteredJobs, session);

		return filteredJobs;

	}


//	public void setDurationForJobs(List<Job> jobs) {
//		for (Job job : jobs){
//			job.setDuration(this.getDuration(job.getId()));
//		}
//		
//	}

	public Integer getDuration(int jobId) {
		return repository.getDuration(jobId);
	}

//	public void setJobsCategories(List<Job> jobs){
//		for (Job job : jobs) {
//			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
//		}
//	}

//	public void setJobsDistanceFromRequest(List<Job> jobs, float fromLat, float fromLng) {
//		for (Job job : jobs) {
//
//			double distance = GoogleClient.getDistance(fromLat, fromLng, job.getLat(), job.getLng());
//			job.setDistanceFromFilterLocation(distance);
//		}
//	}

//		
	public double getDistanceFromRequest(Job job, float fromLat, float fromLng) {
			return GoogleClient.getDistance(fromLat, fromLng, job.getLat(), job.getLng());
	}	
	


	public void setModel_EmployerViewCompletedJob(Model model, int jobId, HttpSession session) {
		
		//Get the job
		JobDTO completedJobDto = new JobDTO();
		completedJobDto.setJob(this.getJob(jobId));

	
		//Verify the job is complete.
		if(completedJobDto.getJob().getStatus() == 2){

			completedJobDto.setEmployeeDtos(userService.getEmployeeDtosByJob(jobId));
			
			completedJobDto.setCategories(categoryService.getCategoriesByJobId(jobId));
			
			boolean haveJobRatingsBeenSubmitted = false ;// this.getHaveJobRatingsBeenSubmitted(jobId);
			
			//Add to model
			model.addAttribute("jobDto", completedJobDto);
			model.addAttribute("haveJobRatingsBeenSubmitted", haveJobRatingsBeenSubmitted);
			
		}
		
	}



	public boolean getHaveJobRatingsBeenSubmitted(int jobId) {
		
		int unsubmittedRatingsCount = repository.getRatingCountByJobAndRatingValue(jobId, -1);
		
		if(unsubmittedRatingsCount == 0) return true;
		else return false;
		
	}

	public int getJobStatus(int jobId) {
		Job job = repository.getJob(jobId);
		return job.getStatus();
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


	public String getEmployersJobsYetTemplate(JobSearchUser employer, List<JobDTO> yetToStartJobs_Dtos, boolean b) {

		StringWriter writer = new StringWriter();

		//Set the context
		final VelocityContext context = new VelocityContext();
		context.put("employer", employer);
		context.put("jobDtos", yetToStartJobs_Dtos);
//		context.put("isActiveJobs", isActiveJobs);
		context.put("mathUtility", MathUtility.class);
		context.put("numberTool", new NumberTool());

		//Run the template
		employerJobsYetToStartTemplate.merge(context, writer);

		//Return the html
		return writer.toString();
	}


	public String getEmployerProfileJobTableTemplate(JobSearchUser employer, List<Job> yetToStartJobs,
														boolean isActiveJobs) {


		StringWriter writer = new StringWriter();

		//Set the context
		final VelocityContext context = new VelocityContext();
		context.put("employer", employer);
		context.put("jobs", yetToStartJobs);
//		context.put("isActiveJobs", isActiveJobs);
		context.put("mathUtility", MathUtility.class);
		context.put("numberTool", new NumberTool());

		//Run the template
//		vmTemplate_employerProfileJobTable.merge(context, writer);
		employerJobsInProcessTemplate.merge(context, writer);

		//Return the html
		return writer.toString();
	}

	public void setModel_EmployerViewJob(Model model, int jobId, HttpSession session) {

		JobSearchUser user = SessionContext.getUser(session);
		
		
		//Get the job
		Job selectedJob = this.getEmployersJobProfile(jobId);
		int hideJobInfoOnLoad;

		if(SessionContext.getUser(session).getUserId() == selectedJob.getUserId()){

			//Get the employees
			List<JobSearchUser> employees = userService.getEmployeesByJob(jobId);
			for(JobSearchUser employee : employees){

				employee.setWage(applicationService.getWage(employee.getUserId(), jobId));				
				employee.setRating(userService.getRating(employee.getUserId()));
			}

			//Get the applications
			List<Application> applications = applicationService.getApplicationsByJob(jobId);

			for(Application application : applications){
				application.setCurrentWageProposal(applicationService.getCurrentWageProposal(application));
			}
			
	//		//Get the failed wage negotiations
			String vtFailedWageNegotiationsByJob = applicationService.
					getFailedWageNegotiationsByJobVelocityTemplate(selectedJob);
			
			
			JobDTO jobDto = new JobDTO();
			jobDto = this.getJobDTO_DisplayJobInfo(jobId);
			
			model.addAttribute("jobDto", jobDto);
			model.addAttribute("applications", applications);
			model.addAttribute("employees", employees);
			model.addAttribute("vtFailedWageNegotiationsByJob", vtFailedWageNegotiationsByJob);

			hideJobInfoOnLoad = 1;
		}
		else{
			hideJobInfoOnLoad = 0;
		}


		List<Question> questions = applicationService.getQuestions(jobId);
		String vtJobInfo = this.getVelocityTemplate_JobInfo(jobId, hideJobInfoOnLoad);


		//Set the model attributes
		model.addAttribute("job", selectedJob);
		model.addAttribute("questions", questions);
		model.addAttribute("vtJobInfo", vtJobInfo);

	}
	
	

	public void setModel_EmployerViewJob_WhenViewingFromEmployeeWorkHistory(Model model,
											HttpSession session, int jobId, int employeeId) {
		
		String vtJobInfo = this.getVelocityTemplate_JobInfo(jobId, 0);
		Application application = applicationService.getApplication(jobId, employeeId);
		application.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(jobId, employeeId));
		
		model.addAttribute("vtJobInfo", vtJobInfo);
		model.addAttribute("questions", application.getQuestions());
		
		
	}

	public Job getJobByApplicationId(int applicationId) {

		return repository.getJobByApplicationId(applicationId);
	}

	public void setModel_EmployeeViewJobFromProfile(Model model, int jobId, int userId) {

//		//Velocity templates
//		String vtJobInformation_EmployeeViewJobFromProfile = this.getVelocityTemplate_JobInfo(jobId, 0);
//		//String vtQuestions = this.getVelocityTemplate_Questions(jobId);
//		String vtAnswers = this.getVelocityTemplate_Answers(jobId, userId);
//
//		model.addAttribute("vtJobInformation", vtJobInformation_EmployeeViewJobFromProfile);
//		model.addAttribute("vtAnswers", vtAnswers);
		//model.addAttribute("vtQuestions", vtQuestions);
		
		
		JobDTO jobDto = new JobDTO();
		jobDto.setJob(this.getJob(jobId));
		
		Application application = applicationService.getApplication(jobId, userId);
		application.setQuestions(applicationService.getQuestionsWithAnswersByJobAndUser(jobId, userId));
		
		model.addAttribute("jobDto", jobDto);
		model.addAttribute("questions", application.getQuestions());


	}



	private String getVelocityTemplate_JobInfo(int jobId, int hideOnOpen) {
		//Job properties
		Job job = this.getJob(jobId);	
		job.setDuration(this.getDuration(jobId));
		List<WorkDay> workDays = this.getWorkDays(jobId);

		List<Category> categories = categoryService.getCategoriesByJobId(jobId);

		StringWriter writer = new StringWriter();

		//Set the context
		final VelocityContext context = new VelocityContext();
		context.put("hideOnOpen", hideOnOpen);
		context.put("job", job);
		context.put("date", new DateTool());
		context.put("categories", categories);
		context.put("workDays", workDays);

		//Run the template
		vtJobInfo.merge(context, writer);

		//Return the html
		return writer.toString();

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

	public void setModel_ApplyForJob(Model model, int jobId, HttpSession session) {


		JobDTO jobDto = new JobDTO();
		jobDto = this.getJobDTO_DisplayJobInfo(jobId);
		
		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobId", jobId);
		model.addAttribute("jobDto", jobDto);

		
		SessionContext.verifyLoggedInUser(session, model);

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

		if(jobDto.getJob() != null){
			
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


	public void UpdateJobStatus(int status, int jobId) {
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

		List<JobDTO> jobDtos = this.getJobDtos_FilteredJobs(jobs, filter, session);
			
		model.addAttribute("jobDtos", jobDtos);
		
		SessionContext.updateFilteredJobIds(session, this.getJobIdsFromJobDTOs(jobDtos));

	}
	
	

	private List<JobDTO> getJobDtos_FilteredJobs(List<Job> jobs, FindJobFilterDTO filter, HttpSession session) {
	
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
		
		List<JobDTO> jobDtos = this.getJobDtos_FilteredJobs(jobs, filter, session);
		
		this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, filter);
				
		session.setAttribute("lastFilterRequest", filter);
		
		SessionContext.setFilteredJobIds(session, this.getJobIdsFromJobDTOs(jobDtos));
	}
	
	public void SetModel_SortFilteredJobs(HttpSession session, Model model, String sortBy, boolean isAscending) {

		
		List<Job> jobs = this.getJobsByIds(SessionContext.getFilteredJobIds(session));

		// *************************************
		// Currently, distance is a property of the job dto
		// One could argue that all these filter properties can be part
		// of the job dto and not the job.
		// As things mature, this can be modified
		// *************************************
		FindJobFilterDTO lastFilterRequest = (FindJobFilterDTO) session.getAttribute("lastFilterRequest");
		List<JobDTO> jobDtos = new ArrayList<JobDTO>();
		if(sortBy.matches("Distance") || sortBy.matches("Duration")){
			jobDtos = this.getJobDtos_FilteredJobs(jobs, lastFilterRequest, session);
			this.sortJobDtos(jobDtos, sortBy, isAscending);
		}
		else{
			this.sortJobs(jobs, sortBy, isAscending);	
			jobDtos = this.getJobDtos_FilteredJobs(jobs, lastFilterRequest, session);
		}
	
		this.setModel_Render_GetJobs_InitialRequest(model, jobDtos, lastFilterRequest);
		
	}
	
	private void setModel_Render_GetJobs_InitialRequest(Model model, List<JobDTO> jobDtos,
		FindJobFilterDTO lastFilterRequest) {

		double maxDistance = this.getMaxDistanceJobFromFilterRequest(jobDtos);
		
		model.addAttribute("filterRequest", lastFilterRequest);
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


	private void sortJobs2(List<Job> filteredJobs, FindJobFilterDTO filter) {
		
		// Sort by start date
		if(filter.getSortBy().matches("StartDate")){
			
			// Ascending
			if (filter.getIsAscending()) {
				java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
					public int compare(Job job1, Job job2){
						return job1.getStartDate_local().compareTo(job2.getStartDate_local());
					}
				});			
			}		
			// Descending
			else{
				java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
					public int compare(Job job1, Job job2){
						return job2.getStartDate_local().compareTo(job1.getStartDate_local());
					}
				});			
			}			
		}
		
		// Sort by end date
		else if(filter.getSortBy().matches("EndDate")){		
			
				
				// Ascending
				if (filter.getIsAscending()) {
					java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
						public int compare(Job job1, Job job2){
							return job1.getEndDate_local().compareTo(job2.getEndDate_local());
						}
					});			
				}		
				// Descending
				else{
					java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
						public int compare(Job job1, Job job2){
							return job2.getEndDate_local().compareTo(job1.getEndDate_local());
						}
					});			
				}			
			}

		// Sort by start time
		if(filter.getSortBy().matches("StartTime")){
			
			// Ascending
			if (filter.getIsAscending()) {
				java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
					public int compare(Job job1, Job job2){
						return job1.getStartTime_local().compareTo(job2.getStartTime_local());
					}
				});			
			}		
			// Descending
			else{
				java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
					public int compare(Job job1, Job job2){
						return job2.getStartTime_local().compareTo(job1.getStartTime_local());
					}
				});			
			}			
		}
		
		// Sort by end time
		if(filter.getSortBy().matches("EndTime")){
			
			// Ascending
			if (filter.getIsAscending()) {
				java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
					public int compare(Job job1, Job job2){
						return job1.getEndTime_local().compareTo(job2.getEndTime_local());
					}
				});			
			}		
			// Descending
			else{
				java.util.Collections.sort(filteredJobs, new Comparator<Job>(){			
					public int compare(Job job1, Job job2){
						return job2.getEndTime_local().compareTo(job1.getEndTime_local());
					}
				});			
			}			
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

//	private List<JobDTO> getJobDtos_FilteredJobs(List<Job> jobs, FindJobFilterDTO filter) {
//		
//		// For each job, set its:
//		// a) categories
//		// b) duration
//		// c) distance from requested filter location
//		
//		List<JobDTO> jobDtos = new ArrayList<JobDTO>();
//		
//		for(Job job : jobs){			
//		
//			JobDTO jobDto = new JobDTO();
//			jobDto.setJob(job);
//			jobDto.setCategories(categoryService.getCategoriesByJobId(job.getId()));			
//			jobDto.setDurationDays(this.getDuration(job.getId()));
//			
//			double distance = this.getDistanceFromRequest(job, filter.getLat(), filter.getLng());
//			distance = MathUtility.round(distance, 1, 0);
//			jobDto.setDistanceFromFilterLocation(distance);			
//			
//			jobDtos.add(jobDto);
//		}
//
//		return jobDtos;
//		
//	}


	

	private List<Job> getJobsByIds(List<Integer> jobIds) {
		
		if(jobIds != null) return repository.getJobsByIds(jobIds);
		else return null;
		
	}

	private JobSearchUserDTO getUserDTO_FindJobs_PageLoad(HttpSession session) {
		
		if(SessionContext.isLoggedIn(session)){
			JobSearchUserDTO userDto = new JobSearchUserDTO();
			
			userDto.setUser((JobSearchUser) session.getAttribute("user"));
			userDto.setSavedFindJobFilters(this.getSavedFindJobFilters(userDto.getUser().getUserId()));

			return userDto;
	
		}
		else return null;
		
	}


	public void setModel_FindJobs_PageLoad(Model model, HttpSession session) {
			
		
		// ****************************************
		// The user does not need to be logged-in in order to search for jobs
		// ****************************************
		if(SessionContext.isLoggedIn(session)){
			JobSearchUserDTO userDto = this.getUserDTO_FindJobs_PageLoad(session);
			session.setAttribute("userDto", userDto);			
		}
		
		model.addAttribute("filterDto", session.getAttribute("lastFilterRequest"));	
	}

	private List<FindJobFilterDTO> getSavedFindJobFilters(int userId) {
		
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
			
		
		case "completed":
			
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





}
