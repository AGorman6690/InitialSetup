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
				
				//Convert the string dates to a java.sql.Date				
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date date = sdf.parse(jobDto.getStringStartDate());
					jobDto.setStartDate(new java.sql.Date(date.getTime()));
					date = sdf.parse(jobDto.getStringEndDate());
					jobDto.setEndDate(new java.sql.Date(date.getTime()));
					
					//Convert strings to sql Time objects
					jobDto.setStartTime(java.sql.Time.valueOf(jobDto.getStringStartTime()));
					jobDto.setEndTime(java.sql.Time.valueOf(jobDto.getStringEndTime()));
					
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
//				jobDto.setZipCode(GoogleClient.getAddressComponent(results[0].addressComponents, "POSTAL_CODE"));
				
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

	public void applyForJob(ApplicationDTO applicationDto) {
		repository.addApplication(applicationDto.getJobId(), applicationDto.getUserId());
		
		//Whether the applicant answered all the questions is handled on the client side.
		//At this point, all questions have a valid answer.
		for(Answer answer : applicationDto.getAnswers()){			
			
			if (answer.getAnswerText() != ""){
				repository.addTextAnswer(answer);
			}else if(answer.getAnswerBoolean() != -1){
				repository.addBooleanAnswer(answer);	
			}else if(answer.getAnswerOptionId() != -1){
				repository.addOptionAnswer(answer, answer.getAnswerOptionId());
			}else if(answer.getAnswerOptionIds().size() > 0){
				for(int answerOptionId : answer.getAnswerOptionIds()){
					repository.addOptionAnswer(answer, answerOptionId);
				}
			}
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
	
	public List<Job> getCompletedJobsByEmployer(int userId) {
		return repository.getCompletedJobsByEmployer(userId);
	}

	public List<CompletedJobDTO> getCompletedJobsByEmployee(int userId) {
		
	
		List<Job> completedJobs = repository.getCompletedJobsByEmployee(userId);
		
		List<CompletedJobDTO> completedJobDtos = new ArrayList<CompletedJobDTO>();
		for(Job job : completedJobs){
			CompletedJobDTO completedJobDto = new CompletedJobDTO();
			completedJobDto.setJob(job);
			completedJobDto.getJob().setCategories(categoryService.getCategoriesByJobId(job.getId()));
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
		job.setQuestions(this.getQuestions(job.getId()));
		
		
		// Get job applicants
		job.setApplicants(userService.getApplicants(jobId));

		// Set each applicant's rating, application, and endorsements only for the job's categories
		for(JobSearchUser applicant : job.getApplicants()){
//			applicant.setRatings(userService.getRatings(applicant.getUserId()));
			applicant.setRating(userService.getRating(applicant.getUserId()));
			applicant.setApplication(applicationService.getApplication(jobId, applicant.getUserId()));
			applicant.setEndorsements(userService.getUserEndorsementsByCategory(applicant.getUserId(), 
											job.getCategories()));
			applicant.setAnswers(this.getAnswers(job.getQuestions(), applicant.getUserId()));
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


	public List<Answer> getAnswers(List<Question> questions, int userId) {
		
		List<Answer> answers = new ArrayList<Answer>();
		
		for(Question question : questions){
			Answer answer;
			
			if(question.getFormatId() == 2 || question.getFormatId() == 3){
				answer = new Answer();
				answer.setAnswers(repository.getAnswers(question.getQuestionId(), userId));
//			}else if(question.getFormatId() == 2){
//				answer = repository.getAnswer(question.getQuestionId(), userId);
//				
			}else{
				answer = repository.getAnswer(question.getQuestionId(), userId);
			}				
			
			answer.setQuestionFormatId(question.getFormatId());
			answers.add(answer);
		}
		return answers;
	}


	private List<Question> getQuestions(int jobId) {
		
		List<Question> questions = repository.getQuestions(jobId);
		for(Question q : questions){
			q.setAnswerOptions(repository.getAnswerOptions(q.getQuestionId()));
		}
		return questions;
	}


	public List<Job> getFilteredJobs(FilterDTO filter) {
		// TODO Auto-generated method stub
		
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(filter.fromAddress);
		
		if (results.length == 1){
			
			filter.setLng((float) results[0].geometry.location.lng);
			filter.setLat((float) results[0].geometry.location.lat);
			
			
			//If parameter is not filtered, set to null
			if(filter.categoryIds[0] ==  -1) filter.categoryIds = null;
			if(filter.getStringEndTime().equals(FilterDTO.ZERO_TIME)) filter.endTime = null;
			if(filter.getStringStartTime().equals(FilterDTO.ZERO_TIME)) filter.startTime = null;

			
			
			int sqlArguementCount = this.getSqlArguementCount(filter); 
	
			//Get the filtered jobs
			List<Job> filteredJobs = repository.getFilteredJobs(filter, sqlArguementCount);
		
			//For each filtered job, calculate the distance between the user's specified filter lat/lng 
			for(Job job : filteredJobs){
				job.setDistanceFromFilterLocation(GoogleClient.getDistance(filter.getLat(), filter.getLng(), job.getLat(), job.getLng()));
				job.setCategories(categoryService.getCategoriesByJobId(job.getId()));
			
			}
			
			


			//Sort the filtered jobs by distance from user's specified lat/lng in ascending order.
//			Collections.sort(filteredJobs, 
//	                (job1, job2) -> job1.getDistanceFromFilterLocation().compareTo(job2.getDistanceFromFilterLocation()));
			return  filteredJobs;
					
		}else {
			//the address is ambiguous
			return null;
		}
	}


	private int getSqlArguementCount(FilterDTO filter) {
		
		//There is at least four arguements for the distance filter
		int count = 4;
		
		
		//Category filter
		if(filter.categoryIds != null){
			count += filter.getCategoryIds().length;
		}		
		
		//Start time filter
		if(filter.startTime != null){
			count += 1;
		}
		
		//End time filter
		if(filter.endTime != null){
			count += 1;
		}
		
		if(filter.getStartDate() != null){
			count += 1;
		}
		
		if(filter.getEndDate() != null){
			count += 1;
		}
		
		
		return count;
	}




}
