package com.jobsearch.job.service;

import java.io.StringWriter;
import java.sql.Time;
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


	public List<Job> getYetToStartJobsByEmployer(int userId) {

		//Query the database
		List<Job> yetToStartJobs = repository.getJobsByStatusAndByEmployer(userId, 0);

		//Set the jobs application summary (i.e. applicants and employees)
		setJobsApplicationSummary(yetToStartJobs);

		return yetToStartJobs;
	}

	public List<JobDTO> getYetToStartJobsByEmployer_Dto(int userId) {

		List<JobDTO> jobDtos = new ArrayList<JobDTO>();

		//Query the database
		List<Job> yetToStartJobs = repository.getJobsByStatusAndByEmployer(userId, 0);

		//Set the job Dtos
		for(Job job : yetToStartJobs){
			JobDTO jobDto = new JobDTO();

			setJobApplicationSummary(job);

			jobDto.setJob(job);
			jobDto.setFailedWageNegotiationDtos(applicationService.getFailedWageNegotiationDTOsByJob(job));

			jobDtos.add(jobDto);
		}

		return jobDtos;

	}


	public List<Job> getActiveJobsByEmployer(int userId) {

		//Query the database
		List<Job> activeJobs = repository.getJobsByStatusAndByEmployer(userId, 1);

		//Set the jobs application summary (i.e. applicants and employees)
		setJobsApplicationSummary(activeJobs);

		return activeJobs;

	}


	private void setJobsApplicationSummary(List<Job> jobs) {

		for (Job job : jobs) {
			this.setJobApplicationSummary(job);		}

	}

	public void setJobApplicationSummary(Job job) {
		//*******************************************************
		//*******************************************************
		//I'm beginning to think these properties should not be apart of the Job class
		//but rather they should be part of the JobDTO class...
		//*******************************************************
		//*******************************************************

		job.setCategory(categoryService.getCategoryByJobId(job.getId()));
		job.setApplications(applicationService.getApplicationsByJob(job.getId()));
		job.setEmployees(userService.getEmployeesByJob(job.getId()));
		job.setNewApplicationCount(this.getNewApplicationCount(job.getApplications()));

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


	public List<Job> getFilteredJobs(FilterJobRequestDTO filter, HttpSession session) {



		//******************************************************
		//******************************************************
		//Need to validate location.
		//Can it be done client side?
		//Currently the lat and lng are being set in the filter request Dto's constructor
		//******************************************************
		//******************************************************
		
		
		//If appending jobs, get the job ids that have already been rendered to the user.
		//The already-rendered-job ids are held in the session.
		List<Integer> alreadyLoadedFilteredJobIds = getAlreadyLoadedFilteredJobsIds(filter, session);


		// Get the filtered jobs		
		List<Job> filteredJobs = repository.getFilteredJobs(filter, alreadyLoadedFilteredJobIds);

		// For each filtered job, calculate the distance between the user's
		// specified filter lat/lng
		if (filteredJobs != null) {
			this.setJobsCategories(filteredJobs);
			this.setJobsDistanceFromRequest(filteredJobs, filter.getLat(), filter.getLng());
			this.setDurationForJobs(filteredJobs);
		}
		return filteredJobs;

	}


	public void setDurationForJobs(List<Job> jobs) {
		for (Job job : jobs){
			job.setDuration(this.getDuration(job.getId()));
		}
		
	}

	public Integer getDuration(int jobId) {
		return repository.getDuration(jobId);
	}

	public void setJobsCategories(List<Job> jobs){
		for (Job job : jobs) {
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
		}
	}

	public void setJobsDistanceFromRequest(List<Job> jobs, float fromLat, float fromLng) {
		for (Job job : jobs) {

			double distance = GoogleClient.getDistance(fromLat, fromLng, job.getLat(), job.getLng());
			job.setDistanceFromFilterLocation(distance);
		}
	}

	
	

	public String getVelocityTemplate_FilterJobs(FilterJobRequestDTO request, HttpSession session) {
		
		//Query the database
		List<Job> filteredJobs = new ArrayList<Job>();
		filteredJobs = this.getFilteredJobs(request, session);


		StringWriter writer = new StringWriter();

		// Set the context
		final VelocityContext context = new VelocityContext();
		context.put("request", request);
		context.put("jobs", filteredJobs);
		context.put("date", new DateTool());
		context.put("dateCompare", new ComparisonDateTool());
		context.put("mathUtility", MathUtility.class);
		

		// If the request is to GET filtered jobs, not SORT already-filtered
		// jobs.
		// The max-distance job is used to set the map zoom level.
		// Since the map is not reloaded on sort, this is skipped if the user is
		// sorting.
		if (!request.getIsSortingJobs()) {
			double maxDistance;

			// If any jobs matched the user's filter(s)
			if (filteredJobs.size() > 0) {

				// Get the farthest job from the users requested located.
				Job maxDistanceJob = filteredJobs.stream().max(Comparator.comparing(Job::getDistanceFromFilterLocation))
						.get();
				maxDistance = maxDistanceJob.getDistanceFromFilterLocation();

			} else {
				maxDistance = -1;
			}

			context.put("maxDistance", maxDistance);
		}

		filterJobsTemplate.merge(context, writer);

		return writer.toString();
	}
	

	@SuppressWarnings("unchecked")

	private List<Integer> getAlreadyLoadedFilteredJobsIds(FilterJobRequestDTO request, HttpSession session) {
		
		if(request.getIsAppendingJobs()){				
			return (List<Integer>) session.getAttribute("loadedFilteredJobIds");
		}else{
			return null;
		}
	}


	public String getSortedJobsHTML(FilterJobRequestDTO request, HttpSession session) {

		// Get the filtered job objects that have already been rendered to the
		// user
		@SuppressWarnings("unchecked")
		// List<Integer> filteredJobIds = (List<Integer>)
		// session.getAttribute("loadedFilteredJobIds");
		List<Job> filteredJobs = (List<Job>) session.getAttribute("loadedFilteredJobs");

		// **************************************************
		// For usage of Comparator<T>, refer to:
		// https://www.mkyong.com/java8/java-8-lambda-comparator-example/
		// **************************************************
		Comparator<Job> c = null;

		// The sortBy String is the data-col attribute in the
		// div.sort-jobs-by-container on the FindJobs.jsp
		switch (request.getSortBy()) {
		case "Distance":
			c = (Job j1, Job j2) -> j1.getDistanceFromFilterLocation().compareTo(j2.getDistanceFromFilterLocation());
			break;

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

		case "Duration":
			c = (Job j1, Job j2) -> j1.getDuration().compareTo(j2.getDuration());
			break;
		}

		// If not sorting in ascending order, then reverse the sort.
		if (request.getIsAscending()) {
			filteredJobs.sort(c);
		} else {
			filteredJobs.sort(c.reversed());
		}

			 
		 
//		//From the jobs already rendered to the user, sort them
//		List<Job> sortedJobs = repository.sortJobs(filteredJobIds, request.getSortBy(), 
//				request.getIsAscending());
//		
//		//Set jobs' attributes
//		this.setJobsDistanceFromRequest(sortedJobs, request.getLat(), request.getLng());
//		this.setJobsCategories(sortedJobs);
		
		
		return "";
//		return this.getVelocityTemplate_FilterJobs(filteredJobs, request);
	}
	
	public void setModel_EmployerViewCompletedJob(Model model, int jobId, HttpSession session) {
		
		//Get the job
		Job completedJob = repository.getJob(jobId);
	
		//Verify the job is complete.
		if(completedJob.getStatus() < 2){

		}else{
			
			String vtJobInfo = getVelocityTemplate_JobInfo(completedJob.getId(), 1);
			
			//Set the employees
			completedJob.setEmployees(userService.getEmployeesByJob(jobId));
			
			//Set the categories
			List<Category> categories = categoryService.getCategoriesByJobId(jobId);
//			completedJob.setCategories(categoryService.getCategoriesByJobId(jobId));
			
			boolean haveJobRatingsBeenSubmitted = this.getHaveJobRatingsBeenSubmitted(jobId);
			
			//Add to model
			model.addAttribute("vtJobInfo", vtJobInfo);
			model.addAttribute("job", completedJob);
			model.addAttribute("categories", categories);
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

		JobSearchUser user = SessionContext.getSessionUser(session);
		
		
		//Get the job
		Job selectedJob = this.getEmployersJobProfile(jobId);
		int hideJobInfoOnLoad;

		if(SessionContext.getSessionUser(session).getUserId() == selectedJob.getUserId()){

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
		application.setQuestions(applicationService.getQuestionsByJobAndUser(jobId, employeeId));
		
		model.addAttribute("vtJobInfo", vtJobInfo);
		model.addAttribute("questions", application.getQuestions());
		
		
	}

	public Job getJobByApplicationId(int applicationId) {

		return repository.getJobByApplicationId(applicationId);
	}

	public void setModelForEmployeeViewJobFromProfileJsp(Model model, int jobId, int userId) {

		//Velocity templates
		String vtJobInformation_EmployeeViewJobFromProfile = this.getVelocityTemplate_JobInfo(jobId, 0);
		//String vtQuestions = this.getVelocityTemplate_Questions(jobId);
		String vtAnswers = this.getVelocityTemplate_Answers(jobId, userId);

		model.addAttribute("vtJobInformation", vtJobInformation_EmployeeViewJobFromProfile);
		model.addAttribute("vtAnswers", vtAnswers);
		//model.addAttribute("vtQuestions", vtQuestions);


	}

	private String getVelocityTemplate_Answers(int jobId, int userId) {
		List<Answer> answers = applicationService.getAnswersByJobAndUser(jobId, userId);

		if(answers.size() >0){



			StringWriter writer = new StringWriter();

			//Set the context
			final VelocityContext context = new VelocityContext();
			context.put("answers", answers);

			//Run the template
			questionsTemplate.merge(context, writer);

			//Return the html
			return writer.toString();
		}
		else{
			return "";
		}
	}

	private String getVelocityTemplate_Questions(int jobId){

		List<Question> questions = applicationService.getQuestions(jobId);

		if(questions.size() >0){

			applicationService.setAnswers(questions);

			StringWriter writer = new StringWriter();

			//Set the context
			final VelocityContext context = new VelocityContext();
			context.put("questions", questions);

			//Run the template
			questionsTemplate.merge(context, writer);

			//Return the html
			return writer.toString();
		}
		else{
			return "";
		}

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

	public Time getStartTime(int jobId) {

		return repository.getStartTime(jobId);
	}

	public Time getEndTime(int jobId) {
		return repository.getEndTime(jobId);
	}

	public void setModel_ApplyForJob(Model model, int jobId, HttpSession session) {

		String vtJobInfo = this.getVelocityTemplate_JobInfo(jobId, 0);
		String vtQuestionsToAnswer = this.getVelocityTemplate_QuestionsToAnswer(jobId);
		
		
		JobDTO jobDto = new JobDTO();
		Job job = this.getJob(jobId);
		jobDto.setJob(job);
		jobDto.setWorkDays(this.getWorkDays(jobId));
		jobDto.setQuestions(applicationService.getQuestions(jobId));
		
		this.setJobDtoDuration(jobDto);
		
		
		
		
		List<Category> categories = categoryService.getCategoriesByJobId(jobId);		
		
		model.addAttribute("isLoggedIn", SessionContext.isLoggedIn(session));
		model.addAttribute("jobId", jobId);
		model.addAttribute("vtJobInfo", vtJobInfo);
		model.addAttribute("vtQuestionsToAnswer", vtQuestionsToAnswer);
		model.addAttribute("jobDto", jobDto);
		model.addAttribute("categories", categories);
		
		SessionContext.verifyLoggedInUser(session, model);

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

	private String getVelocityTemplate_QuestionsToAnswer(int jobId) {

		StringWriter writer = new StringWriter();
		final VelocityContext context = new VelocityContext();

		List<Question> questions = applicationService.getQuestions(jobId);

		context.put("questions", questions);
		questionsToAnswer.merge(context, writer);

		return writer.toString();
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








//	public void verifyJobStatusForUsersYetToStartJobs(int userId) {
//
//		//Query the database for the user's yet-to-start jobs
//		List<Job> yetToStartJobs = repository.getJobsByStatusByEmployee(userId, 0);
//
//		//Verify start date and time is still in the future
//		for(Job job : yetToStartJobs){
//			if(job.getStartDate().before(LocalDateTime.now())
//		}
//
//
//
//
//	}

}
