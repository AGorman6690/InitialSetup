package com.jobsearch.job.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DateTool;
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
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
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

	@Autowired
	@Qualifier("FilterJobsVM")
	Template filterJobsTemplate;

	public void addPosting(SubmitJobPostingRequestDTO postingDto, JobSearchUser user) {

		for (JobInfoPostRequestDTO jobDto : postingDto.getJobs()) {
			// ***********************************************************************************
			// Should we verify the address on the client side? I'm thinking
			// so...
			// Why go all the way to the server
			// when the same logic can be handled in the browser?
			// ***********************************************************************************

			// Build the job's full address
			String address = jobDto.getStreetAddress() + " " + jobDto.getCity() + " " + jobDto.getState() + " "
					+ jobDto.getZipCode();

			GeocodingResult[] results = googleClient.getLatAndLng(address);

			if (results.length == 1) {

				// Convert strings to sql Date objects
				jobDto.setStartDate(DateUtility.getSqlDate(jobDto.getStringStartDate()));
				jobDto.setEndDate(DateUtility.getSqlDate(jobDto.getStringEndDate()));

				// Convert strings to sql Time objects
				jobDto.setStartTime(java.sql.Time.valueOf(jobDto.getStringStartTime()));
				jobDto.setEndTime(java.sql.Time.valueOf(jobDto.getStringEndTime()));

				// Address this idea later.
				// Should we format the user's address and city per the data
				// from Google maps?
				// This would correct spelling errors, maintain consistent
				// letter casing, etc.
				// ***************************************************************
				// jobDto.setZipCode(GoogleClient.getAddressComponent(results[0].addressComponents,
				// "POSTAL_CODE"));
				// ***************************************************************

				jobDto.setLat((float) results[0].geometry.location.lat);
				jobDto.setLng((float) results[0].geometry.location.lng);

				jobDto.setQuestions(
						getQuestionsFromPostingDto(jobDto.getSelectedQuestionIds(), postingDto.getQuestions()));

				repository.addJob(jobDto, user);
			} else if (results.length == 0) {
				// invalid address
			} else if (results.length > 1) {
				// ambiguous address
			}
		}
	}

	private List<Question> getQuestionsFromPostingDto(List<Integer> selectedQuestionIds,
			List<Question> postingDtoQuestions) {

		List<Question> questions = new ArrayList<Question>();

		for (int selectedQuestionId : selectedQuestionIds) {
			// Get question
			for (Question postingDtoQuestion : postingDtoQuestions) {
				if (postingDtoQuestion.getQuestionId() == selectedQuestionId) {
					questions.add(postingDtoQuestion);
				}
			}
		}
		return questions;
	}

	public void markJobComplete(int jobId) {
		repository.markJobComplete(jobId);
	}

	public List<Job> getJobsAppliedTo(int userId) {
		return repository.getJobsAppliedTo(userId);
	}

	public List<Job> getJobsHiredFor(int userId) {
		return repository.getJobsHiredFor(userId);
	}

	public List<Job> getActiveJobsByUser(int userId) {

		List<Job> activeJobs = repository.getActiveJobsByUser(userId);

		for (Job job : activeJobs) {

			job.setCategory(categoryService.getCategoryByJobId(job.getId()));
			job.setApplications(applicationService.getApplicationsByJob(job.getId()));
			job.setEmployees(userService.getEmployeesByJob(job.getId()));
			job.setNewApplicationCount(this.getNewApplicationCount(job.getApplications()));
		}

		return activeJobs;

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

		List<Job> completedJobs = repository.getCompletedJobsByEmployer(userId);

		List<CompletedJobResponseDTO> completedJobDtos = new ArrayList<CompletedJobResponseDTO>();
		for (Job job : completedJobs) {
			CompletedJobResponseDTO completedJobDto = new CompletedJobResponseDTO();
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
			completedJobDto.setJob(job);
			completedJobDtos.add(completedJobDto);
		}

		return completedJobDtos;
	}

	public List<CompletedJobResponseDTO> getCompletedJobsByEmployee(int userId) {

		// Get completed jobs
		List<Job> completedJobs = repository.getCompletedJobsByEmployee(userId);

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

	public Job getJobPostingInfo(int jobId) {
		// This only sets the job properties that relate to the job posting
		// i.e. no applicants, no employees, etc.

		Job job = repository.getJob(jobId);

		// Set job categories
		job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

		// Set job questions
		job.setQuestions(applicationService.getQuestions(job.getId()));

		return job;
	}

	public Job getEmployersJobProfile(int jobId) {
		// This sets almost(?) all of the job's properties

		Job job = repository.getJob(jobId);

		// Set job categories
		job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

		// ****************************************************************************************
		// ****************************************************************************************
		// Why does the job have both applications and applicants???????
		// Shouldn't applicants be enough?????
		// Each application object has an applicant property.
		// This is redundant.
		// I will address this.
		// UPDATE:
		// This is done this way because the "Status" is a property of the
		// application, not the applicant.
		// I think removing the applicants from the job is the way to go.
		// Check back later.
		job.setApplications(applicationService.getApplicationsByJob(jobId));
		// ****************************************************************************************
		// ****************************************************************************************

		// // Set job questions
		job.setQuestions(applicationService.getQuestions(job.getId()));

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
			employee.setRating(userService.getRating(employee.getUserId()));
			employee.setApplication(applicationService.getApplication(jobId, employee.getUserId()));
			employee.setEndorsements(
					userService.getUserEndorsementsByCategory(employee.getUserId(), job.getCategories()));
		}

		// // Set each employee's rating
		// for(JobSearchUser employee : job.getEmployees()){
		// employee.setRatings(userService.getRatings(employee.getUserId()));
		// }

		return job;
	}

	public List<Job> getFilteredJobs(FilterJobRequestDTO filter, List<Integer> alreadyLoadedFilteredJobIds) {
		// TODO Auto-generated method stub

		// ************************************************************
		// ************************************************************
		// The user's requested locations should be cached somewhere with
		// either cookies or a global array on the client side.
		// This would put less load on the Google API quota.
		// Or a table can be created that stores the lat/lng for all requested
		// city/states/zip code
		// ************************************************************
		// ************************************************************
		GeocodingResult[] results = googleClient.getLatAndLng(filter.getFromAddress());

		// **********************************************************************
		// The below condition has been removed because "Woodbury, MN" returns
		// two results.
		// I left it as a reminder for whether we want to handle potential
		// ambiguous responses.
		// **********************************************************************
		// Filter location must return a valid response
		// if (results.length == 1){

		filter.setLng((float) results[0].geometry.location.lng);
		filter.setLat((float) results[0].geometry.location.lat);

		// If parameter is not filtered, set to null
		if (filter.getCategoryIds()[0] == -1)
			filter.setCategoryIds(null);
		if (filter.getStringEndTime() == null)
			filter.setEndTime(null);
		if (filter.getStringStartTime() == null)
			filter.setStartTime(null);
		if (filter.getWorkingDays().get(0).matches("-1"))
			filter.setWorkingDays(null);

		// Get the filtered jobs
		List<Job> filteredJobs = repository.getFilteredJobs(filter, alreadyLoadedFilteredJobIds);

		// For each filtered job, calculate the distance between the user's
		// specified filter lat/lng
		if (filteredJobs != null) {
			this.setJobsCategories(filteredJobs);
			this.setJobsDistanceFromRequest(filteredJobs, filter.getLat(), filter.getLng());
			// for (Job job : filteredJobs) {
			// job.setDistanceFromFilterLocation(
			// MathUtility.round(
			// GoogleClient.getDistance(filter.getLat(), filter.getLng(),
			// job.getLat(), job.getLng()),
			// 1, 0));
			// job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
			//
			// }
			return filteredJobs;
		} else {
			return null;
		}

		// }else {
		// //the address is ambiguous
		// return null;
		// }
	}

	public void setJobsCategories(List<Job> jobs) {
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

	public String getFilterJobsTemplate(List<Job> filteredJobs, FilterJobRequestDTO request) {

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
	public String getFilterdJobsResponseHtml(FilterJobRequestDTO request, HttpSession session, Model model) {

		request.setIsSortingJobs(false);

		// ***************************************************************
		// ***************************************************************
		// The session attributes loadedFilteredJobIds and loadedFilteredJobs
		// are redundant.
		// The loadedFilteredJobIds was created first and is for loading
		// additional filtered jobs, but
		// excluding the already-rendered jobs. The database queries the job
		// table excluding the job
		// ids contained in this session list of job ids. HTML for these jobs is
		// then appended to the
		// existing filtered jobs HTML.
		// The loadedFilteredJobs was created so that when sorting
		// already-rendered jobs, the
		// data base would not have to be queried again. Rather the session list
		// of jobs would be sorted
		// by the requested job attribute and then rendered to the user.
		// It amounts to: have a session list of job ids and re-query the
		// database when the user wants to sort,
		// or have a session list of jobs and sort the session list when the
		// user wants to sort.
		// Essentially: is it cheaper to re-query or to store JOBS, not job ids,
		// in session?
		// It may not even matter, just an FYI.
		// For now both session variables will be used, but I think only one
		// should be kept.
		// ***************************************************************
		// ***************************************************************

		// If appending jobs, get the job ids that have already been rendered to
		// the user.
		// The already-rendered-jobs are held in the session.
		List<Integer> alreadyLoadedFilteredJobIds = new ArrayList<Integer>();
		List<Job> alreadyLoadedFilteredJobs = new ArrayList<Job>();

		if (request.getIsAppendingJobs()) {
			alreadyLoadedFilteredJobIds = (List<Integer>) session.getAttribute("loadedFilteredJobIds");
			alreadyLoadedFilteredJobs = (List<Job>) session.getAttribute("loadedFilteredJobs");
		} else {
			alreadyLoadedFilteredJobIds = null;
			alreadyLoadedFilteredJobs = null;
		}

		// From the request, set the jobs.
		// The already-loaded-jobs (if there are any) will not be queried.
		List<Job> filteredJobs = new ArrayList<Job>();
		filteredJobs = this.getFilteredJobs(request, alreadyLoadedFilteredJobIds);

		// Get the job ids that were just queried
		List<Integer> loadedFilteredJobIds = filteredJobs.stream().map(j -> j.getId()).collect(Collectors.toList());

		// If appending jobs, then add the already-loaded-job-ids to the
		// just-loaded-job-ids
		if (request.getIsAppendingJobs()) {
			if (alreadyLoadedFilteredJobIds != null) {
				loadedFilteredJobIds.addAll(alreadyLoadedFilteredJobIds);
				filteredJobs.addAll(alreadyLoadedFilteredJobs);
			}
		}

		// Update the session variables
		session.setAttribute("loadedFilteredJobIds", loadedFilteredJobIds);
		session.setAttribute("loadedFilteredJobs", filteredJobs);

		// Add jobs ids to the model
		model.addAttribute("loadedFilteredJobIds", loadedFilteredJobIds);

		// Run the velocity template and return the HTML
		return this.getFilterJobsTemplate(filteredJobs, request);

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

		// //From the jobs already rendered to the user, sort them
		// List<Job> sortedJobs = repository.sortJobs(filteredJobIds,
		// request.getSortBy(),
		// request.getIsAscending());
		//
		// //Set jobs' attributes
		// this.setJobsDistanceFromRequest(sortedJobs, request.getLat(),
		// request.getLng());
		// this.setJobsCategories(sortedJobs);

		return this.getFilterJobsTemplate(filteredJobs, request);
	}

	public String getRateEmployeesView(Model model, int jobId) {

		// Get the job
		Job completedJob = repository.getJob(jobId);

		// Verify the job is complete.
		// This is here because the user can edit the hyperlink's "markComplete"
		// value.
		if (completedJob.getIsActive() == 1) {
			return null;
		} else {

			// Set the employees
			completedJob.setEmployees(userService.getEmployeesByJob(jobId));

			// Set the categories
			completedJob.setCategories(categoryService.getCategoriesByJobId(jobId));

			// Add to model
			model.addAttribute("job", completedJob);

			return "RateEmployees";
		}
	}

}
