package com.jobsearch.job.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.maps.model.GeocodingResult;
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
	@Qualifier("FilterJobsVM")
	Template filterJobsTemplate;

	public void addPosting(SubmitJobPostingRequestDTO postingDto) {

		for (JobInfoPostRequestDTO jobDto : postingDto.getJobs()) {
			//***********************************************************************************
			//Should we verify the address on the client side?  I'm thinking so... 
			//Why go all the way to the server
			//when the same logic can be handled in the browser?
			//***********************************************************************************
			
			//Build the job's full address		
			String address = jobDto.getStreetAddress() + " " + jobDto.getCity() + " " + jobDto.getState() + " "
					+ jobDto.getZipCode();

			GoogleClient maps = new GoogleClient();
			GeocodingResult[] results = maps.getLatAndLng(address);

			if (results.length == 1) {

				// Convert strings to sql Date objects
				jobDto.setStartDate(DateUtility.getSqlDate(jobDto.getStringStartDate()));
				jobDto.setEndDate(DateUtility.getSqlDate(jobDto.getStringEndDate()));

				// Convert strings to sql Time objects
				jobDto.setStartTime(java.sql.Time.valueOf(jobDto.getStringStartTime()));
				jobDto.setEndTime(java.sql.Time.valueOf(jobDto.getStringEndTime()));

				// Address this idea later.
				// Should we format the user's address and city per the data from Google maps?
				// This would correct spelling errors, maintain consistent letter casing, etc.
				// ***************************************************************
				// jobDto.setZipCode(GoogleClient.getAddressComponent(results[0].addressComponents,
				// "POSTAL_CODE"));
				// ***************************************************************

				jobDto.setLat((float) results[0].geometry.location.lat);
				jobDto.setLng((float) results[0].geometry.location.lng);
				
				jobDto.setQuestions(getQuestionsFromPostingDto(jobDto.selectedQuestionIds, postingDto.getQuestions()));

				repository.addJob(jobDto);
			} else if (results.length == 0) {
				// invalid address
			} else if (results.length > 1) {
				// ambiguous address
			}
		}
	}

	private List<Question> getQuestionsFromPostingDto(List<Integer> selectedQuestionIds, List<Question> postingDtoQuestions) {

		List<Question> questions = new ArrayList<Question>();
		
		for(int selectedQuestionId : selectedQuestionIds){			
			//Get question
			for(Question postingDtoQuestion : postingDtoQuestions){
				if(postingDtoQuestion.getQuestionId() == selectedQuestionId){
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
		}

		return activeJobs;

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

		List<Job> completedJobs = repository.getCompletedJobsByEmployee(userId);

		List<CompletedJobResponseDTO> completedJobDtos = new ArrayList<CompletedJobResponseDTO>();
		for (Job job : completedJobs) {
			CompletedJobResponseDTO completedJobDto = new CompletedJobResponseDTO();
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
			completedJobDto.setJob(job);
			completedJobDto.setComment(userService.getComment(job.getId(), userId));
			completedJobDto.setRating(userService.getRatingForJob(userId, job.getId()));
			;
			completedJobDto.setEndorsements(userService.getUsersEndorsementsByJob(userId, job.getId()));

			completedJobDtos.add(completedJobDto);
		}

		return completedJobDtos;
	}

	public Job getJob(int jobId) {
		// TODO Auto-generated method stub
		Job job = repository.getJob(jobId);

		// Get job categories
		job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

		// Set job questions
		job.setQuestions(applicationService.getQuestions(job.getId()));

		// Get job applicants
		job.setApplicants(userService.getApplicants(jobId));

		// Set each applicant's rating, application, and endorsements only for
		// the job's categories
		for (JobSearchUser applicant : job.getApplicants()) {
			// applicant.setRatings(userService.getRatings(applicant.getUserId()));
			applicant.setRating(userService.getRating(applicant.getUserId()));
			applicant.setApplication(applicationService.getApplication(jobId, applicant.getUserId()));
			applicant.setEndorsements(
					userService.getUserEndorsementsByCategory(applicant.getUserId(), job.getCategories()));
			applicant.setAnswers(applicationService.getAnswers(job.getQuestions(), applicant.getUserId()));
		}

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
	
	public String getFilterdJobsResponseHtml(List<Job> filteredJobs, FilterJobRequestDTO request){
		
		return getFilterJobsTemplate(filteredJobs, request);
	}

	public List<Job> getFilteredJobs(FilterJobRequestDTO filter) {
		// TODO Auto-generated method stub
		
		//************************************************************
		//************************************************************
		//The user's requested locations should be cached somewhere with 
		//either cookies or a global array on the client side.
		//This would put less load on the Google API quota.
		//Or a table can be created that stores the lat/lng for all requested
		//city/states/zip code
		//************************************************************
		//************************************************************
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(filter.fromAddress);

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
		if (filter.categoryIds[0] == -1)
			filter.categoryIds = null;
		if (filter.getStringEndTime().equals(FilterJobRequestDTO.ZERO_TIME))
			filter.endTime = null;
		if (filter.getStringStartTime().equals(FilterJobRequestDTO.ZERO_TIME))
			filter.startTime = null;
		if (filter.getWorkingDays().get(0).matches("-1"))
			filter.setWorkingDays(null);

		// Get the filtered jobs
		List<Job> filteredJobs = repository.getFilteredJobs(filter);

		// For each filtered job, calculate the distance between the user's
		// specified filter lat/lng
		if (filteredJobs != null) {
			for (Job job : filteredJobs) {
				job.setDistanceFromFilterLocation(
						MathUtility.round(
								GoogleClient.getDistance(filter.getLat(), filter.getLng(), job.getLat(), job.getLng()),
								1, 0));
				job.setCategories(categoryService.getCategoriesByJobId(job.getId()));

			}
			return filteredJobs;
		} else {
			return null;
		}

		// }else {
		// //the address is ambiguous
		// return null;
		// }
	}
	
	public String getFilterJobsTemplate(List<Job> filteredJobs, FilterJobRequestDTO request){
		
		StringWriter writer = new StringWriter();
		
		final VelocityContext context = new VelocityContext();
		
		context.put("request", request);
		context.put("jobs", filteredJobs);
		
		filterJobsTemplate.merge(context, writer);
		
		return writer.toString();
		
	}

}
