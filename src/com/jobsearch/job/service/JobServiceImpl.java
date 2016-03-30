package com.jobsearch.job.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

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
	GoogleClient maps;
	
	public void addJob(List<CreateJobDTO> jobDtos) {
		
		for (CreateJobDTO jobDto : jobDtos){
			
			//Build the job's full address
			String address = jobDto.getStreetAddress() + " " 
								+ jobDto.getCity() + " "
								+ jobDto.getState() + " " 
								+ jobDto.getZipCode();
			
			GeocodingResult[] results = maps.getLatAndLng(address);
	
			//If the address was successfully converted to lat/lng
			if (results.length > 0){
				
				//Convert the string dates to a java.sql.Date				
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date date = sdf.parse(jobDto.getStringStartDate());
					jobDto.setStartDate(new java.sql.Date(date.getTime()));
					date = sdf.parse(jobDto.getStringEndDate());
					jobDto.setEndDate(new java.sql.Date(date.getTime()));
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				jobDto.setLat((float) results[0].geometry.location.lat);
				jobDto.setLng((float) results[0].geometry.location.lng);
				repository.addJob(jobDto);
			}			
		}
}
	

	public void markJobComplete(int jobId) {
		repository.markJobComplete(jobId);
	}

	public void applyForJob(int jobId, int userId) {

		if (!repository.hasAppliedForJob(jobId, userId)) {
			repository.applyForJob(jobId, userId);
		}

	}

	public List<Job> getJobsByCategory(int categoryId) {
		return repository.getJobsByCategory(categoryId);
	}

	public List<Job> getApplicationsByUser(int userId) {
		return repository.getApplicationsByUser(userId);
	}

	public List<Job> getEmploymentByUser(int userId) {
		return repository.getEmploymentByUser(userId);
	}

	public List<Job> getJobsByUser(int userId) {
		return repository.getJobsByUser(userId);
	}


	public List<Job> getJobOffersByUser(int userId) {
		return repository.getJobOffersByUser(userId);
	}

	public List<Job> getActiveJobsByUser(int userId) {
		return repository.getActiveJobsByUser(userId);
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

	public List<Job> getCompletedJobsByUser(int userId) {
		return repository.getCompletedJobsByUser(userId);
	}


	public Job getJob(int jobId) {
		// TODO Auto-generated method stub
		Job job = repository.getJob(jobId);

		// Get job applicants
		job.setApplicants(userService.getApplicants(jobId));

		// Set each applicant's rating
		for(JobSearchUser applicant : job.getApplicants()){
			applicant.setRatings(userService.getRatings(applicant.getUserId()));
			applicant.setApplication(applicationService.getApplication(jobId, applicant.getUserId()));
		}

		// Get job employees
		job.setEmployees(userService.getEmployeesByJob(jobId));

		// Set each employee's rating
		for(JobSearchUser employee : job.getEmployees()){
			employee.setRatings(userService.getRatings(employee.getUserId()));
		}

		return job;
	}


	public List<Job> getFilteredJobs(int radius, String fromAddress, int[] categoryIds) {
		// TODO Auto-generated method stub
		
		GeocodingResult[] results = maps.getLatAndLng(fromAddress);
		
		if (results.length > 0){
			
			double filterLng = results[0].geometry.location.lng;
			double filterLat = results[0].geometry.location.lat;
			
			if(categoryIds[0] ==  -1) categoryIds = null;
			
			//Get the filtered jobs
			List<Job> filteredJobs = repository.getFilteredJobs(radius, filterLat,
					filterLng, categoryIds);
		
			//For each filtered job, calculate the distance between the user's specified filter lat/lng 
			for(Job job : filteredJobs){
				job.setDistanceFromFilterLocation(GoogleClient.getDistance(filterLat, filterLng, job.getLat(), job.getLng()));
			}

			//Sort the filtered jobs by distance from user's specified lat/lng in ascending order.
//			Collections.sort(filteredJobs, 
//	                (job1, job2) -> job1.getDistanceFromFilterLocation().compareTo(job2.getDistanceFromFilterLocation()));
			return  filteredJobs;
					
		}else return null;
	}

}
