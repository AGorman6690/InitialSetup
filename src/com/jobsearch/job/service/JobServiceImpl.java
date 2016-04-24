package com.jobsearch.job.service;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.model.Answer;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.GoogleClient;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;

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

	
	public void addJob(List<CreateJobDTO> jobDtos) {
		
		for (CreateJobDTO jobDto : jobDtos){
			
			//Build the job's full address
			String address = jobDto.getStreetAddress() + " " 
								+ jobDto.getCity() + " "
								+ jobDto.getState() + " " 
								+ jobDto.getZipCode();
			
			GoogleClient maps = new GoogleClient();
			GeocodingResult[] results = maps.getLatAndLng(address);
	
			if (results.length == 1){
				 
				//Convert strings to sql Date objects
				jobDto.setStartDate(DateUtility.getSqlDate(jobDto.getStringStartDate()));
				jobDto.setEndDate(DateUtility.getSqlDate(jobDto.getStringEndDate()));
					
				//Convert strings to sql Time objects
				jobDto.setStartTime(java.sql.Time.valueOf(jobDto.getStringStartTime()));
				jobDto.setEndTime(java.sql.Time.valueOf(jobDto.getStringEndTime()));
				
				//Address this idea later
				//***************************************************************
//				jobDto.setZipCode(GoogleClient.getAddressComponent(results[0].addressComponents, "POSTAL_CODE"));
				//***************************************************************
				
				
				jobDto.setLat((float) results[0].geometry.location.lat);
				jobDto.setLng((float) results[0].geometry.location.lng);
				
				repository.addJob(jobDto);
			}else if(results.length == 0){
				//invalid address
			}else if(results.length > 1) {
				//ambiguous address
			}
		}
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
		
		List<Job> activeJobs =  repository.getActiveJobsByUser(userId);
		
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

		// For the categoryId passed as the paramenter, get the categories 1 level
		// deep
		categories = categoryService.getSubCategories(categoryId);

		// For each category 1 level deep
		for (Category category : categories) {

			// Get its job count
			count += getJobCountByCategory(category.getId());

			// Recursively get the job count for the category 1 level deep
			count = getSubJobCount(category.getId(), count);
		}

		return count;
	}
	
	public List<CompletedJobDTO> getCompletedJobsByEmployer(int userId) {
		//FYI : As of now, this method could return a list of Jobs, not CompletedJobDTOs, and still
		//contain all the information that is currently being rendered on jsp pages.
		//It is returning CompletedJobDTO because the getCompletedJobsBy***Employee*** needs CompltedJobDTOs.
		//Rather than creating two separate list properties in the user class to hold both Job objects
		//and CompltedJobDTO objects, I chose this because in the future perhaps additional completed job info
		//(i.e. past employees, comments, ratings) would also be nice to display on the employer page.
		
		List<Job> completedJobs = repository.getCompletedJobsByEmployer(userId);
		
		List<CompletedJobDTO> completedJobDtos = new ArrayList<CompletedJobDTO>();
		for(Job job : completedJobs){
			CompletedJobDTO completedJobDto = new CompletedJobDTO();			
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));			
			completedJobDto.setJob(job);			
			completedJobDtos.add(completedJobDto);
		}
		
		return completedJobDtos;
	}

	public List<CompletedJobDTO> getCompletedJobsByEmployee(int userId) {		
	
		List<Job> completedJobs = repository.getCompletedJobsByEmployee(userId);
		
		List<CompletedJobDTO> completedJobDtos = new ArrayList<CompletedJobDTO>();
		for(Job job : completedJobs){
			CompletedJobDTO completedJobDto = new CompletedJobDTO();
			job.setCategories(categoryService.getCategoriesByJobId(job.getId()));			
			completedJobDto.setJob(job);
			completedJobDto.setComment(userService.getComment(job.getId(), userId));
			completedJobDto.setRating(userService.getRatingForJob(userId, job.getId()));;
			completedJobDto.setEndorsements(userService.getUsersEndorsementsByJob(userId, job.getId()));
			
			completedJobDtos.add(completedJobDto);
		}
		
		return completedJobDtos;
	}


	public Job getJob(int jobId) {
		// TODO Auto-generated method stub
		Job job = repository.getJob(jobId);

		//Get job categories
		job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
		
		//Set job questions
		job.setQuestions(applicationService.getQuestions(job.getId()));
		
		
		// Get job applicants
		job.setApplicants(userService.getApplicants(jobId));

		// Set each applicant's rating, application, and endorsements only for the job's categories
		for(JobSearchUser applicant : job.getApplicants()){
//			applicant.setRatings(userService.getRatings(applicant.getUserId()));
			applicant.setRating(userService.getRating(applicant.getUserId()));
			applicant.setApplication(applicationService.getApplication(jobId, applicant.getUserId()));
			applicant.setEndorsements(userService.getUserEndorsementsByCategory(applicant.getUserId(), 
											job.getCategories()));
			applicant.setAnswers(applicationService.getAnswers(job.getQuestions(), applicant.getUserId()));
		}

		// Get job employees
		job.setEmployees(userService.getEmployeesByJob(jobId));
		
		// Set each employee's rating, application and endorsements		
		for(JobSearchUser employee : job.getEmployees()){
//			employee.setRatings(userService.getRatings(employee.getUserId()));
			employee.setRating(userService.getRating(employee.getUserId()));
			employee.setApplication(applicationService.getApplication(jobId, employee.getUserId()));
			employee.setEndorsements(userService.getUserEndorsementsByCategory(employee.getUserId(), 
										job.getCategories()));
		}

//		// Set each employee's rating
//		for(JobSearchUser employee : job.getEmployees()){
//			employee.setRatings(userService.getRatings(employee.getUserId()));
//		}

		return job;
	}

	public List<Job> getFilteredJobs(FilterDTO filter) {
		// TODO Auto-generated method stub
		
		
//		GoogleClient maps = new GoogleClient();
//		GeocodingResult[] results = maps.getLatAndLng(filter.fromAddress);

		
		//Filter location must return a valid response
//		if (results.length == 1){
			
//			filter.setLng((float) results[0].geometry.location.lng);
//			filter.setLat((float) results[0].geometry.location.lat);
		filter.setLat((float) 45.204523);	
		filter.setLng((float) -93.519745);
			
			
			
			//If parameter is not filtered, set to null
			if(filter.categoryIds[0] ==  -1) filter.categoryIds = null;
			if(filter.getStringEndTime().equals(FilterDTO.ZERO_TIME)) filter.endTime = null;
			if(filter.getStringStartTime().equals(FilterDTO.ZERO_TIME)) filter.startTime = null;
			if(filter.getWorkingDays().get(0).matches("-1")) filter.setWorkingDays(null);

			//Get the filtered jobs
			List<Job> filteredJobs = repository.getFilteredJobs(filter);
		
			//For each filtered job, calculate the distance between the user's specified filter lat/lng 
			if(filteredJobs != null){
				for(Job job : filteredJobs){
					job.setDistanceFromFilterLocation(GoogleClient.getDistance(filter.getLat(), filter.getLng(), job.getLat(), job.getLng()));
					job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
				
				}
				return  filteredJobs;
			}else{
				return null;
			}
			
					
//		}else {
//			//the address is ambiguous
//			return null;
//		}
	}

}
