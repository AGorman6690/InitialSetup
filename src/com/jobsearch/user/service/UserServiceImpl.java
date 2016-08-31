package com.jobsearch.user.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationResponseDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.email.Mailer;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.service.SubmitJobPostingRequestDTO;
import com.jobsearch.job.service.CompletedJobResponseDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobInfoPostRequestDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.DummyData;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.WageProposal;
import com.jobsearch.user.rate.RatingRequestDTO;
import com.jobsearch.user.rate.RatingRequestDTOs;
import com.jobsearch.user.repository.UserRepository;
import com.jobsearch.user.web.AvailabilityRequestDTO;
import com.jobsearch.user.web.EditProfileRequestDTO;
import com.jobsearch.user.web.FindEmployeesRequestDTO;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;

@Service
public class UserServiceImpl {

	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	@Autowired
	UserRepository repository;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	JobServiceImpl jobService;
	
	@Autowired
	ApplicationServiceImpl applicationService;

	@Autowired
	Mailer mailer;

	@Value("${host.url}")
	private String hostUrl;

	public JobSearchUser createUser(JobSearchUser user) {

		user.setPassword(encryptPassword(user.getPassword()));

		JobSearchUser newUser = repository.createUser(user);

		if (newUser.getEmailAddress() != null) {
			mailer.sendMail(user.getEmailAddress(), "email verification", "please click the link to verify your email "
					+ hostUrl + "/JobSearch/validateEmail?userId=" + newUser.getUserId());
		}
		return newUser;
	}

	private String encryptPassword(String password) {
		 StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
         return encryptor.encryptPassword(password);
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


	

	public List<String> getAvailableDates(int userId) {
		return repository.getAvailableDates(userId);

	}

	public List<Profile> getProfiles() {
		return repository.getProfiles();
	}

	public List<JobSearchUser> getEmployeesByCategory(int categoryId) {
		return repository.getEmployeesByCategory(categoryId);
	}

	public void rateEmployee(RatingRequestDTOs ratingRequestDTOs) {

//		for (RateCriterion rc : ratingRequestDTOs.getRateCriteria()) {
//			repository.updateRating(rc);
//		}
//
//		deleteEndorsements(ratingRequestDTOs.getEmployeeId(), ratingRequestDTOs.getJobId());
//		for (Endorsement endorsement : ratingRequestDTOs.getEndorsements()) {
//			repository.addEndorsement(endorsement);
//		}
//
//		deleteComment(ratingRequestDTOs.getJobId(), ratingRequestDTOs.getEmployeeId());
//		if (ratingRequestDTOs.getComment() != "") {
//			repository.addComment(ratingRequestDTOs);
//		}

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

		// Round to the nearest tenth. 0 is the minimum value.
		return MathUtility.round(repository.getRating(userId), 1, 0);
	}

	public List<Endorsement> getUserEndorsementsByCategory(int userId, List<Category> categories) {
		// NOTE: this does not return ALL endorsements.
		// This consolidates ALL endorsements into each endorsement's category.
		// For instance, if a user has 10 concrete endorsements,
		// only one endorsement object will be created, but the count
		// property will be equal to 10.

		List<Endorsement> endorsements = new ArrayList<Endorsement>();

		for (Category category : categories) {

			Endorsement endorsement = new Endorsement();

			// Get how many endorsements the user has in the particular category
			int endorsementCount = repository.getEndorsementCountByCategory(userId, category.getId());
			endorsement.setCount(endorsementCount);

			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());
			endorsements.add(endorsement);
		}

		return endorsements;
	}

	public List<Endorsement> getUsersEndorsements(int userId) {
		// NOTE: this does not return ALL endorsements.
		// This consolidates ALL endorsements into each endorsement's category.
		// For instance, if a user has 10 concrete endorsements,
		// only one endorsement object will be created, but the count
		// property will be equal to 10.

		List<Endorsement> endorsements = new ArrayList<Endorsement>();

		// Get the category Ids that the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIds(userId);

		for (Integer endorsementCategoryId : endorsementCategoryIds) {

			Category category = categoryService.getCategory(endorsementCategoryId);

			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());

			// Get how many endorsements the user has in the particular category
			int endorsementCount = this.getEndorsementCountByCategory(userId, category.getId());
			endorsement.setCount(endorsementCount);

			endorsements.add(endorsement);
		}

		return endorsements;

	}
	

	public int getEndorsementCountByCategory(int userId, int categoryId) {
		return repository.getEndorsementCountByCategory(userId, categoryId);
	}

	public List<Endorsement> getUsersEndorsementsByJob(int userId, int jobId) {
		

		// NOTE: this does not return ALL endorsements.
		// This consolidates ALL endorsements into each endorsement's category.
		// For instance, if a user has 10 concrete endorsements,
		// only one endorsement object will be created, but the count
		// property will be equal to 10.

		List<Endorsement> endorsements = new ArrayList<Endorsement>();

		//Per the job, get the category Ids that the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIdsByJob(userId, jobId);

		//Create endorsement objects
		for (Integer endorsementCategoryId : endorsementCategoryIds) {

			//Create a category object
			Category category = categoryService.getCategory(endorsementCategoryId);

			//Set the endorsement object's properties
			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());

//			// Get how many endorsements the user has in the particular category
//			// and job
//			int endorsementCount = this.getEndorsementCountByCategoryAndJob(userId, category.getId(), jobId);
//			endorsement.setCount(endorsementCount);

			endorsements.add(endorsement);
		}

		return endorsements;

	}
	
	

	public int getEndorsementCountByCategoryAndJob(int userId, int categoryId, int jobId) {
		
		return repository.getEndorsementCountByCategoryAndJob(userId, categoryId, jobId);
	}

	public String getComment(int jobId, int userId) {

		return repository.getComment(jobId, userId);
	}

	public double getRatingForJob(int userId, int jobId) {

		List<Double> ratingValues = repository.getRatingForJob(userId, jobId);

		double total = 0;
		int count = 0;
		for (Double ratingValue : ratingValues) {
			if (ratingValue >= 0) {
				total += ratingValue;
				count += 1;
			}
		}

		return MathUtility.round(total / count, 1, 0);

	}

	public void updateAvailability(AvailabilityRequestDTO availabilityDTO) {
		// TODO Auto-generated method stub

		repository.deleteAvailability(availabilityDTO.getUserId());

		for (String date : availabilityDTO.getStringDays()) {
			repository.addAvailability(availabilityDTO.getUserId(), DateUtility.getSqlDate(date));
		}

	}

	public void editProfile(EditProfileRequestDTO editProfileRequest) {

		// Edit categories
		categoryService.deleteCategoriesFromUser(editProfileRequest.getUserId());
		for (int categoryId : editProfileRequest.getCategoryIds()) {
			categoryService.addCategoryToUser(editProfileRequest.getUserId(), categoryId);
		}

		// Edit home location
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(editProfileRequest.getHomeCity() + " " + editProfileRequest.getHomeState()
				+ " " + editProfileRequest.getHomeZipCode());
		if (results.length == 1) {
			editProfileRequest.setHomeLat((float) results[0].geometry.location.lat);
			editProfileRequest.setHomeLng((float) results[0].geometry.location.lng);
			this.updateHomeLocation(editProfileRequest);

		}

		if (editProfileRequest.getMaxWorkRadius() > 0) {
			repository.UpdateMaxWorkRadius(editProfileRequest.getUserId(), editProfileRequest.getMaxWorkRadius());
		}

	}

	public void updateHomeLocation(EditProfileRequestDTO editProfileRequest) {
		repository.updateHomeLocation(editProfileRequest);

	}

	public List<JobSearchUser> findEmployees(FindEmployeesRequestDTO findEmployeesRequest) {

		// A valid location must be supplied
		if ((Float) findEmployeesRequest.getLat() != null && (Float) findEmployeesRequest.getLng() != null
				&& findEmployeesRequest.getRadius() > 0) {

			List<JobSearchUser> employees = repository.findEmployees(findEmployeesRequest);
			for (JobSearchUser employee : employees) {
				employee.setCategories(categoryService.getCategoriesByUserId(employee.getUserId()));
				employee.setEndorsements(
						this.getUserEndorsementsByCategory(employee.getUserId(), employee.getCategories()));
				employee.setRating(this.getRating(employee.getUserId()));
				employee.setDistanceFromJob(MathUtility.round(GoogleClient.getDistance(findEmployeesRequest.getLat(),
						findEmployeesRequest.getLng(), employee.getHomeLat(), employee.getHomeLng()), 1, 0));

			}

			return employees;

		}

		return null;
	}

	public void createUsers_DummyData() {

//
//		DummyData dummyData = new DummyData();
//		List<JobSearchUser> dummyUsers = dummyData.getDummyUsers();
//
//		int lastDummyCreationId = 0;
//		try {
//			lastDummyCreationId = repository.getLastDummyCreationId("user");	
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		
//		repository.createUsers_DummyData(dummyUsers, lastDummyCreationId + 1);


	}

	public void createJobs_DummyData() {

//		List<JobSearchUser> dummyEmployers = repository.getEmployers();
//		DummyData dummyData = new DummyData();
//
//		List<JobInfoPostRequestDTO> dummyJobs = dummyData.getDummyJobs(dummyEmployers);
//		int lastDummyCreationId = repository.getLastDummyCreationId("job");
//
//		for (JobInfoPostRequestDTO dummyJob : dummyJobs) {
//			repository.createJob_DummyData(dummyJob, lastDummyCreationId + 1);
//		}

	}

	public void resetPassword(JobSearchUser user) {

		String newPassword = generateRandomPassword();

		String encryptedPassword = encryptPassword(newPassword);

		boolean passwordUpdated = repository.resetPassword(user.getUsername(), encryptedPassword);

		if (passwordUpdated) {
			mailer.sendMail(user.getUsername(), "Labor Vault password reset",
					"Your new password for labor vault is " + newPassword + "\n");
		}

	}

	public static String generateRandomPassword() {
		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

		String pw = "";
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (RANDOM.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}

	public void updatePassword(String password, String email) {

		String encryptedPassword  = encryptPassword(password);

		repository.updatePassword(encryptedPassword, email);
	}

	public void hireApplicant(int wageProposalId) {
		
		//Get the wage proposal
		WageProposal wageProposal = applicationService.getWageProposal(wageProposalId);
		
		//Get the application
		Application application = applicationService.getApplication(wageProposal.getApplicationId());
				
		//Update the application's status to hired
		applicationService.updateApplicationStatus(application.getApplicationId(), 3);
		
		//Hire the applicant 
		this.hireApplicant(application.getUserId(), application.getJobId());
		
	}

	public void setEmployeesProfileModel(JobSearchUser employee, Model model) {
		

		//Get the employee's open job applications
		List<ApplicationResponseDTO> openApplicationResponseDtos = 
							applicationService.getOpenApplicationResponseDtosByApplicant(employee.getUserId());
		
		//Get the employee's hired-for, but not-yet-started jobs
		List<Job> yetToStartJobs = jobService.getYetToStartJobsByEmployee(employee.getUserId());
		
		//Get the employee's completed jobs
		List<Job> completedJobs = jobService.getCompletedJobsByEmployee(employee.getUserId());
		
		//Get the employee's active jobs
		List<Job> activeJobs = jobService.getActiveJobsByEmployee(employee.getUserId());
		
		//Get the failed wage negotiations that the employee has been involved in 
		List<ApplicationResponseDTO> failedWageNegotiations =
							applicationService.getFailedWageNegotiations(employee.getUserId());
		
		//Set the model attributes
		model.addAttribute("failedWageNegotiations", failedWageNegotiations);
		model.addAttribute("yetToStartJobs", yetToStartJobs);
		model.addAttribute("activeJobs", activeJobs);
		model.addAttribute("completedJobs", completedJobs);
		model.addAttribute("openApplicationResponseDtos", openApplicationResponseDtos);
		
	}

	public void setEmployersProfileModel(JobSearchUser employer, Model model) {
		
		//Get the employer's yet-to-start jobs
		List<Job>  yetToStartJobs = jobService.getYetToStartJobsByEmployer(employer.getUserId());
		
		//Get the employer's active jobs
		List<Job> activeJobs = jobService.getActiveJobsByEmployer(employer.getUserId());
		
		//Get the employer's completed jobs
		List<CompletedJobResponseDTO> completedJobs = jobService.getCompletedJobsByEmployer(employer.getUserId());
		
		
		model.addAttribute("yetToStartJobs", yetToStartJobs);
		model.addAttribute("activeJobs", activeJobs);
		model.addAttribute("completedJobs", completedJobs);
		
		//When the profile is requested and presented to the user,
		//all the applications' "HasBeenViewed" property, for the user's active jobs,
		//will be set to true.
		
		//*********************************************************************			
		//*********************************************************************
		//On second thought, this should be set to zero when the user clicks and views
		//the new applicants
		applicationService.setJobsApplicationsHasBeenViewed(activeJobs, 1);
		//*********************************************************************			
		//*********************************************************************
		
	}


}
