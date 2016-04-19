package com.jobsearch.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.email.Mailer;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.GoogleClient;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.repository.UserRepository;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;

@Service
public class UserServiceImpl {

	@Autowired
	UserRepository repository;
	
	@Autowired
	CategoryServiceImpl categoryService;
	
	@Autowired
	JobServiceImpl jobService;

	@Autowired
	Mailer mailer;

	public JobSearchUser createUser(JobSearchUser user) {

		JobSearchUser newUser = repository.createUser(user);

		if (newUser.getEmailAddress() != null) {
			mailer.sendMail(user.getEmailAddress(), "email verification",
					"please click the link to verify your email http://localhost:8080/JobSearch/validateEmail?userId="
							+ newUser.getUserId());
		}
		return newUser;
	}

	public void setUsersId(JobSearchUser user) {
		repository.setUsersId(user);

	}

	public JobSearchUser getUserByEmail(String emailAddress) {
		return repository.getUserByEmail(emailAddress);

	}

	public JobSearchUser getUser(int userId) {
		return repository.getUser(userId);
	}

	public List<JobSearchUser> getApplicants(int jobId) {
		return repository.getApplicants(jobId);
	}


	public List<JobSearchUser> getEmployeesByJob(int jobId) {
		return repository.getEmpolyeesByJob(jobId);
	}

	public void hireApplicant(int userId, int jobId) {
		repository.hireApplicant(userId, jobId);

	}

	public JobSearchUser getProfile(JobSearchUser user) {		
	
		//If employer
		if(user.getProfileId() == 1){
			user.setActiveJobs(jobService.getActiveJobsByUser(user.getUserId()));
			user.setCompletedJobs(jobService.getCompletedJobsByEmployer(user.getUserId()));
		}else{
			
			user.setJobsAppliedTo(jobService.getJobsAppliedTo(user.getUserId()));
			user.setJobsHiredFor(jobService.getJobsHiredFor(user.getUserId()));
			user.setCompletedJobs(jobService.getCompletedJobsByEmployee(user.getUserId()));
			user.setAvailableDates(this.getAvailableDates(user.getUserId()));
		}
		
		user.setCategories(categoryService.getCategoriesByUserId(user.getUserId()));
		
		return user;

	}

	public List<String> getAvailableDates(int userId) {
		return repository.getAvailableDates(userId);
	
	
	}

	public List<Profile> getProfiles() {
		return repository.getProfiles();
	}

	public List<JobSearchUser> getEmployeesByCategory(int categoryId) {
		return repository.getEmployeesByCategory(categoryId);
	}

	public void rateEmployee(RatingDTO ratingDTO) {
		
		for(RateCriterion rc : ratingDTO.getRateCriteria()){
			repository.updateRating(rc);
		}
		
		deleteEndorsements(ratingDTO.getEmployeeId(), ratingDTO.getJobId());
		for(Endorsement endorsement : ratingDTO.getEndorsements()){
			repository.addEndorsement(endorsement);
		}
		
		deleteComment(ratingDTO.getJobId(), ratingDTO.getEmployeeId());
		if(ratingDTO.getComment() != ""){
			repository.addComment(ratingDTO);
		}
		
	}

	public void deleteComment(int jobId, int employeeId) {
		repository.deleteComment(jobId, employeeId);
		
	}

	public void deleteEndorsements(int employeeId, int jobId) {
		repository.deleteEndorsements(employeeId, jobId);
		
	}

	public List<RateCriterion> getRatingCriteia() {
		return repository.getRatingCriteria();
	}

	public JobSearchUser validateUser(int userId) {
		return repository.validateUser(userId);
	}

	public double getRating(int userId) {
		
		List<Double> ratingValues = repository.getRating(userId);		

		double total = 0;
		int count = 0;
		for(double value : ratingValues){			
			if(value >= 0){
				total += value;
				count += 1;
			}			
		}	

		//Round to the nearest tenth. 0 is the minimum value.
		return  MathUtility.round(total / count, 1, 0);
	}
	
	public List<Endorsement> getUserEndorsementsByCategory(int userId, List<Category> categories) {
		//NOTE: this does not return ALL endorsements.
		//This consolidates ALL endorsements into each endorsement's category.
		//For instance, if a user has 10 concrete endorsements,
		//only one endorsement object will be created, but the count
		//property will be equal to 10.
		
		List<Endorsement> endorsements = new ArrayList<Endorsement>();
		
		for(Category category : categories){
			
			Endorsement endorsement = new Endorsement();
			
			//Get how many endorsements the user has in the particular category
			int endorsementCount = repository.getEndorsementCountByCategory(userId, category.getId());
			endorsement.setCount(endorsementCount);
			
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());
			endorsements.add(endorsement);
		}
		
		return  endorsements;
	}
	
	public List<Endorsement> getUsersEndorsements(int userId) {
		//NOTE: this does not return ALL endorsements.
		//This consolidates ALL endorsements into each endorsement's category.
		//For instance, if a user has 10 concrete endorsements,
		//only one endorsement object will be created, but the count
		//property will be equal to 10.
		
		List<Endorsement> endorsements = new ArrayList<Endorsement>();
		
		//Get the category Ids that  the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIds(userId);

		for(Integer endorsementCategoryId : endorsementCategoryIds){			
			
			Category category = categoryService.getCategory(endorsementCategoryId);			
			
			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());
			
			//Get how many endorsements the user has in the particular category
			int endorsementCount = repository.getEndorsementCountByCategory(userId, category.getId());
			endorsement.setCount(endorsementCount);
			
			endorsements.add(endorsement);
		}
		
		return endorsements;

	}
	
	public List<Endorsement> getUsersEndorsementsByJob(int userId, int jobId) {
		
		//NOTE: this does not return ALL endorsements.
		//This consolidates ALL endorsements into each endorsement's category.
		//For instance, if a user has 10 concrete endorsements,
		//only one endorsement object will be created, but the count
		//property will be equal to 10.
		
		List<Endorsement> endorsements = new ArrayList<Endorsement>();
		
		//Get the category Ids that  the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIdsByJob(userId, jobId);

		for(Integer endorsementCategoryId : endorsementCategoryIds){			
			
			Category category = categoryService.getCategory(endorsementCategoryId);			
			
			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());
			
			//Get how many endorsements the user has in the particular category and job
			int endorsementCount = repository.getEndorsementCountByCategoryAndJob(userId, category.getId(), jobId);
			endorsement.setCount(endorsementCount);
			
			endorsements.add(endorsement);
		}
		
		return endorsements;

	}

	
	public String getComment(int jobId, int userId) {
		
		return repository.getComment(jobId, userId);
	}

	public double getRatingForJob(int userId, int jobId) {
			
		List<Double> ratingValues = repository.getRatingForJob(userId, jobId);
		
		double total = 0;
		int count = 0;
		for(Double ratingValue : ratingValues){
			if(ratingValue >= 0){
				total += ratingValue;
				count += 1;
			}			
		}
		
		return MathUtility.round(total / count, 1, 0);		
			
	}

	public void updateAvailability(AvailabilityDTO availabilityDTO) {
		// TODO Auto-generated method stub
		
		repository.deleteAvailability(availabilityDTO.getUserId());
		
		for(String date : availabilityDTO.getStringDays()){
			repository.addAvailability(availabilityDTO.getUserId(), DateUtility.getSqlDate(date));
		}
		
	}
	public void editProfile(EditProfileDTO editProfileDTO) {
		
		//Edit categories
		categoryService.deleteCategoriesFromUser(editProfileDTO.getUserId());
		for(int categoryId : editProfileDTO.getCategoryIds()){
			categoryService.addCategoryToUser(editProfileDTO.getUserId(), categoryId);
		}
		
		
		//Edit home location
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(editProfileDTO.getHomeCity()
												+ " " + editProfileDTO.getHomeCity()
												+ " " + editProfileDTO.getHomeZipCode());
		
		if(results.length == 1){
			editProfileDTO.setHomeLat((float) results[0].geometry.location.lat);
			editProfileDTO.setHomeLng((float) results[0].geometry.location.lng);
			this.updateHomeLocation(editProfileDTO);
					
		}
		
		if(editProfileDTO.getMaxWorkRadius() > 0){
			repository.UpdateMaxWorkRadius(editProfileDTO.getUserId(), editProfileDTO.getMaxWorkRadius());
		}

	}

	public void updateHomeLocation(EditProfileDTO editProfileDTO) {
		repository.updateHomeLocation(editProfileDTO);
		
	}

	public List<JobSearchUser> findEmployees(FindEmployeesDTO findEmployeesDto) {

		//A valid location must be supplied
		if((Float) findEmployeesDto.getLat() != null && (Float) findEmployeesDto.getLng() != null
				&& findEmployeesDto.getRadius() > 0){
			
			List<JobSearchUser> employees = repository.findEmployees(findEmployeesDto);
			for(JobSearchUser employee : employees){
				employee.setCategories(categoryService.getCategoriesByUserId(employee.getUserId()));
				employee.setEndorsements(this.getUserEndorsementsByCategory(employee.getUserId(), employee.getCategories()));
				employee.setRating(this.getRating(employee.getUserId()));
				employee.setDistanceFromJob(MathUtility.round(GoogleClient.getDistance(findEmployeesDto.getLat(),
						findEmployeesDto.getLng(), employee.getHomeLat(), employee.getHomeLng()), 1, 0));

			}
			
			return employees;
			
		}
		
		return null;
	}




}
