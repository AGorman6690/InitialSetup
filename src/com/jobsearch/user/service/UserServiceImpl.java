package com.jobsearch.user.service;

import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.email.Mailer;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.FindEmployeesDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.WageProposal;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.rate.SubmitRatingDTO;
import com.jobsearch.user.rate.SubmitRatingDTOs_Wrapper;
import com.jobsearch.user.repository.UserRepository;
import com.jobsearch.user.web.AvailabilityDTO;
import com.jobsearch.user.web.EditProfileRequestDTO;
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

	@Autowired
	@Qualifier("FindEmployeesResponseVM")
	Template findEmployeesResponseTemplate;

	public JobSearchUserDTO createUser(JobSearchUser proposedUser) {

//		proposedUser = new JobSearchUser();
		
		JobSearchUserDTO newUserDto = getNewUserDto(proposedUser);
		newUserDto.setUser(proposedUser);
		
		if(!newUserDto.getIsInvalidNewUser()){			
			
			proposedUser.setPassword(encryptPassword(proposedUser.getPassword()));

			JobSearchUser newUser = repository.createUser(proposedUser);

		
			mailer.sendMail(proposedUser.getEmailAddress(), "email verification",
					"please click the link to verify your email " + hostUrl
					+ "/JobSearch/email/validate?userId=" + newUser.getUserId());
										
		} 
		
		return newUserDto;
	}

	private JobSearchUserDTO getNewUserDto(JobSearchUser proposedUser) {
		
		JobSearchUserDTO newUserDto = new JobSearchUserDTO();
		newUserDto.setIsInvalidEmail_duplicate(false);
		newUserDto.setIsInvalidEmail_format(false);
		newUserDto.setIsInvalidMatchingEmail(false);
		newUserDto.setIsInvalidFirstName(false);
		newUserDto.setIsInvalidLastName(false);
		newUserDto.setIsInvalidMatchingPassword(false);
		newUserDto.setIsInvalidNewUser(false);
		newUserDto.setIsInvalidPassword(false);
		newUserDto.setIsInvalidProfile(false);
		
		// Email address
		if(proposedUser.getEmailAddress() == null || proposedUser.getEmailAddress().matches("")){

			newUserDto.setIsInvalidEmail_format(true);
			newUserDto.setIsInvalidNewUser(true);
		}
		else{
			try {
				
				  // Validate the email is the correct format
			      InternetAddress emailAddr = new InternetAddress(proposedUser.getEmailAddress());
			      emailAddr.validate();
			      
			      
					// Validate the email is not used by another user
					JobSearchUser user = repository.getUserByEmail(proposedUser.getEmailAddress());
					if (user != null) {
	
						newUserDto.setIsInvalidEmail_duplicate(true);
						newUserDto.setIsInvalidNewUser(true);
					}
			      
		    }
			catch (AddressException ex) {

				newUserDto.setIsInvalidEmail_format(true);
			    newUserDto.setIsInvalidNewUser(true);
		    }			
		}
		
		// Matching email
		if(proposedUser.getMatchingEmailAddress() == null ||
				!proposedUser.getEmailAddress().matches(proposedUser.getMatchingEmailAddress())){
			
			newUserDto.setIsInvalidMatchingEmail(true);
			newUserDto.setIsInvalidNewUser(true);
			
		}		
		

		
		// Password
		if(proposedUser.getPassword() == null ||
				(proposedUser.getPassword().length() < 6 || proposedUser.getPassword().length() > 20)){

			newUserDto.setIsInvalidPassword(true);
			newUserDto.setIsInvalidNewUser(true);
			
		}
		
		// Matching password
		if(proposedUser.getMatchingPassword() == null ||
				!proposedUser.getPassword().matches(proposedUser.getMatchingPassword())){
			
			newUserDto.setIsInvalidMatchingPassword(true);
			newUserDto.setIsInvalidNewUser(true);
			
		}
		
		// First name
		if(proposedUser.getFirstName() == null || proposedUser.getFirstName().matches("")){
			newUserDto.setIsInvalidFirstName(true);
			newUserDto.setIsInvalidNewUser(true);
		}
		
		// Last name
		if(proposedUser.getLastName() == null || proposedUser.getLastName().matches("")){
			newUserDto.setIsInvalidLastName(true);
			newUserDto.setIsInvalidNewUser(true);
		}
		
		// Profile Id
		if(proposedUser.getProfileId() != Profile.PROFILE_ID_EMPLOYEE &&
			proposedUser.getProfileId() != Profile.PROFILE_ID_EMPLOYER){
			
			newUserDto.setIsInvalidProfile(true);
			newUserDto.setIsInvalidNewUser(true);
			
		}
	      
		return newUserDto;

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

	public List<JobSearchUser> getApplicantsByJob_SubmittedOrConsidered(int jobId) {
		return repository.getApplicantsByJob_SubmittedOrConsidered(jobId);
	}

	public List<JobSearchUser> getEmployeesByJob(int jobId) {
		return repository.getEmpolyeesByJob(jobId);
	}

	public void hireApplicant(int userId, int jobId) {
		repository.hireApplicant(userId, jobId);

	}

	public List<String> getAvailableDays(int userId) {
		return repository.getAvailableDays(userId);

	}

	public List<Profile> getProfiles() {
		return repository.getProfiles();
	}

	public List<JobSearchUser> getEmployeesByCategory(int categoryId) {
		return repository.getEmployeesByCategory(categoryId);
	}

	public void insertRatings(SubmitRatingDTOs_Wrapper submitRatingDTOs_Wrapper) {

		// For each employee's rating
		for (SubmitRatingDTO submitRatingDto : submitRatingDTOs_Wrapper.getSubmitRatingDtos()) {

			// Rate criterion
			for (RateCriterion rc : submitRatingDto.getRateCriteria()) {
				rc.setEmployeeId(submitRatingDto.getEmployeeId());
				rc.setJobId(submitRatingDTOs_Wrapper.getJobId());
				repository.updateRating(rc);
			}

			// Endorsements
			deleteEndorsements(submitRatingDto.getEmployeeId(), submitRatingDTOs_Wrapper.getJobId());
			for (Integer categoryId : submitRatingDto.getEndorsementCategoryIds()) {
				Endorsement endorsement = new Endorsement(submitRatingDto.getEmployeeId(), categoryId,
						submitRatingDTOs_Wrapper.getJobId());
				repository.addEndorsement(endorsement);
			}

			// Comment
			deleteComment(submitRatingDTOs_Wrapper.getJobId(), submitRatingDto.getEmployeeId());
			if (submitRatingDto.getCommentString() != "") {
				repository.addComment(submitRatingDto.getEmployeeId(), submitRatingDTOs_Wrapper.getJobId(),
						submitRatingDto.getCommentString());
			}

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

		// Round to the nearest tenth. 0 is the minimum value.
		return MathUtility.round(repository.getRating(userId), 1, 0);
	}

	/**
	 * This does not return ALL endorsements. This consolidates ALL endorsements
	 * into each endorsement's category. For instance, if a user has 10 concrete
	 * endorsements, only one endorsement object will be created, but the count
	 * property will be equal to 10.
	 *
	 * @param userId
	 * @param categories
	 * @return
	 */
	public List<Endorsement> getUserEndorsementsByCategory(int userId, List<Category> categories) {
		//

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

	/**
	 * This does not return ALL endorsements. This consolidates ALL endorsements
	 * into each endorsement's category. For instance, if a user has 10 concrete
	 * endorsements, only one endorsement object will be created, but the count
	 * property will be equal to 10.
	 *
	 * @param userId
	 * @return
	 */
	public List<Endorsement> getUsersEndorsements(int userId) {
		List<Endorsement> endorsements = new ArrayList<Endorsement>();

		// Get the category Ids that the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIds(userId);

		for (Integer endorsementCategoryId : endorsementCategoryIds) {

			Category category = categoryService.getCategory(endorsementCategoryId);

			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());

			// Get how many endorsements the user has in the particular category
			int endorsementCount = getEndorsementCountByCategory(userId, category.getId());
			endorsement.setCount(endorsementCount);

			endorsements.add(endorsement);
		}

		return endorsements;

	}

	public int getEndorsementCountByCategory(int userId, int categoryId) {
		return repository.getEndorsementCountByCategory(userId, categoryId);
	}

	/**
	 * This does not return ALL endorsements. This consolidates ALL endorsements
	 * into each endorsement's category. For instance, if a user has 10 concrete
	 * endorsements, only one endorsement object will be created, but the count
	 * property will be equal to 10.
	 *
	 * @param userId
	 * @param jobId
	 * @return
	 */
	public List<Endorsement> getUsersEndorsementsByJob(int userId, int jobId) {

		List<Endorsement> endorsements = new ArrayList<Endorsement>();

		// Per the job, get the category Ids that the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIdsByJob(userId, jobId);

		// Create endorsement objects
		for (Integer endorsementCategoryId : endorsementCategoryIds) {

			// Create a category object
			Category category = categoryService.getCategory(endorsementCategoryId);

			// Set the endorsement object's properties
			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());

			// // Get how many endorsements the user has in the particular
			// category
			// // and job
			// int endorsementCount =
			// this.getEndorsementCountByCategoryAndJob(userId,
			// category.getId(), jobId);
			// endorsement.setCount(endorsementCount);

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

//	public double getRatingForJob(int userId, int jobId) {
//
//		List<Double> ratingValues = repository.getRatingForJob(userId, jobId);
//
//		return calculateRatingForJob(ratingValues);
//
//	}
	
	public double getRatingValueForJob(RatingDTO ratingDto) {

		List<Double> ratingValues = ratingDto.getRateCriteria()
															.stream()
															.map(rc -> rc.getValue())
															.collect(Collectors.toList());

		return calculateRatingForJob(ratingValues);

	}
	
	public double calculateRatingForJob(List<Double> ratingValues){
	
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

	public void updateAvailability(HttpSession session, AvailabilityDTO availabilityDto) {
		
		// *********************************************************************
		// This process is inefficient. Deleting ALL records for the user, regardless
		// if the succeeding insert statement includes these records.
		// Records are deleted only to be re-inserted...
		// This original post could be a potential solution:
		// http://stackoverflow.com/questions/32922038/insert-data-to-table-with-existing-data-then-delete-data-that-was-not-touched-d
		// Revisit this.
		// *********************************************************************		
					
		availabilityDto.setUserId(SessionContext.getUser(session).getUserId());				
		repository.deleteAvailability(availabilityDto.getUserId());	
		
		if(availabilityDto.getStringDays() != null){
			if(availabilityDto.getStringDays().size() > 0){		
				repository.addAvailability(availabilityDto);
			}		
		}


	}

	public void editEmployeeSettings(EditProfileRequestDTO editProfileRequestDto, HttpSession session) {

		// Get the user from the session
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");

		// Set the dto's user id
		editProfileRequestDto.setUserId(user.getUserId());

		// Edit home location
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(editProfileRequestDto.getHomeCity() + " "
				+ editProfileRequestDto.getHomeState() + " " + editProfileRequestDto.getHomeZipCode());

		if (results.length == 1) {
			editProfileRequestDto.setHomeLat((float) results[0].geometry.location.lat);
			editProfileRequestDto.setHomeLng((float) results[0].geometry.location.lng);
			// this.updateHomeLocation(editProfileRequest);
			repository.updateEmployeeSettings(editProfileRequestDto);

			this.updateSessionUser(session);

		}
	}

	public void updateSessionUser(HttpSession session) {
		JobSearchUser user = getUser(SessionContext.getUser(session).getUserId());
		session.setAttribute("user", user);
	}

	public List<JobSearchUser> findEmployees(FindEmployeesDTO findEmployeesDto) {

		findEmployeesDto.setCoordinate(GoogleClient.getCoordinate(findEmployeesDto.getFromAddress()));

		// If the address successfully yielded a coordinate
		if (findEmployeesDto.getCoordinate() != null) {
			List<JobSearchUser> result = new ArrayList<JobSearchUser>();

			// Query the database
			List<JobSearchUser> employees = repository.findEmployees(findEmployeesDto);

			// Set the categories if the user filtered by category
			List<Category> categories = new ArrayList<Category>();
			if (findEmployeesDto.getCategoryIds().size() > 0) {
				categories = categoryService.getCategories(findEmployeesDto.getCategoryIds());
			}

			// Set additional properties for each employee
			for (JobSearchUser employee : employees) {

				// Categories
				// employee.setCategories(categoryService.getCategoriesByUserId(employee.getUserId()));

				// Rating
				employee.setRating(this.getRating(employee.getUserId()));

				// The database query does not filter on rating.
				// This condition is the rating filter.
				if (employee.getRating() >= findEmployeesDto.getRating()) {

					// Set endorsements if the user filtered by category
					if (categories != null) {
						employee.setEndorsements(this.getUserEndorsementsByCategory(employee.getUserId(), categories));
					}

					// Distance from job
					employee.setDistanceFromJob(
							MathUtility.round(GoogleClient.getDistance(findEmployeesDto.getCoordinate().getLatitude(),
									findEmployeesDto.getCoordinate().getLongitude(), employee.getHomeLat(),
									employee.getHomeLng()), 1, 0));

					result.add(employee);

				}

			}

			return result;

		} else {
			return null;
		}

	}

	public void createUsers_DummyData() {

		//
		// DummyData dummyData = new DummyData();
		// List<JobSearchUser> dummyUsers = dummyData.getDummyUsers();
		//
		// int lastDummyCreationId = 0;
		// try {
		// lastDummyCreationId = repository.getLastDummyCreationId("user");
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		//
		//
		// repository.createUsers_DummyData(dummyUsers, lastDummyCreationId +
		// 1);

	}

	public void createJobs_DummyData() {

		// List<JobSearchUser> dummyEmployers = repository.getEmployers();
		// DummyData dummyData = new DummyData();
		//
		// List<JobInfoPostRequestDTO> dummyJobs =
		// dummyData.getDummyJobs(dummyEmployers);
		// int lastDummyCreationId = repository.getLastDummyCreationId("job");
		//
		// for (JobInfoPostRequestDTO dummyJob : dummyJobs) {
		// repository.createJob_DummyData(dummyJob, lastDummyCreationId + 1);
		// }

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

		String encryptedPassword = encryptPassword(password);

		repository.updatePassword(encryptedPassword, email);
	}

	public void hireApplicant(int wageProposalId) {

		// Get the wage proposal
		WageProposal wageProposal = applicationService.getWageProposal(wageProposalId);

		// Get the application
		Application application = applicationService.getApplication(wageProposal.getApplicationId());

		// Update the application's status to hired
		applicationService.updateApplicationStatus(application.getApplicationId(), 3);

		// Hire the applicant
		this.hireApplicant(application.getUserId(), application.getJobId());

	}

	public void setModel_EmployeeProfile(JobSearchUser employee, Model model) {
		
		List<ApplicationDTO> applicationDtos = applicationService.
												getApplicationDtos_ByUserAndApplicationStatus_OpenJobs(employee.getUserId(), 
															Arrays.asList(Application.STATUS_SUBMITTED,
																		Application.STATUS_CONSIDERED,
																		Application.STATUS_ACCEPTED));
		
		int failedApplicationCount = applicationService.getFailedApplicationCount(applicationDtos);
		int openApplicationCount = applicationService.getOpenApplicationCount(applicationDtos);
		

		
		List<Job> jobs_employment = jobService.getJobsByEmployee(employee.getUserId());
		int pastEmploymentCount = jobService.getJobCountByStatus(jobs_employment, 2);
		int currentEmploymentCount = jobService.getJobCountByStatus(jobs_employment, 1);
		int futureEmploymentCount = jobService.getJobCountByStatus(jobs_employment, 0);
		
		
		// *************************************************************
		// Replace getJobsByEmplyee() with this
		// *************************************************************
		List<JobDTO> jobDtos_employment_currentAndFuture = jobService.getJobDtos_Employment_CurrentAndFuture(employee.getUserId());
		
		List<Job> jobs_needRating = jobService.getJobs_NeedRating_FromEmployee(employee.getUserId());
		
		model.addAttribute("jobs_needRating", jobs_needRating);
		model.addAttribute("userId", employee.getUserId());
		model.addAttribute("jobDtos_employment_currentAndFuture", jobDtos_employment_currentAndFuture);
		model.addAttribute("applicationDtos", applicationDtos);
		model.addAttribute("openApplicationCount", openApplicationCount);
		model.addAttribute("failedApplicationCount", failedApplicationCount);
		model.addAttribute("jobs_employment", jobs_employment);
		model.addAttribute("pastEmploymentCount", pastEmploymentCount);
		model.addAttribute("currentEmploymentCount", currentEmploymentCount);
		model.addAttribute("futureEmploymentCount", futureEmploymentCount);

	}

	public void setModel_EmployerProfile(JobSearchUser employer, Model model) {

		List<JobDTO> jobDtos_jobsWaitingToStart = jobService.getJobDtos_JobsWaitingToStart_Employer(employer.getUserId());

		List<JobDTO> jobDtos_jobsInProcess = jobService.getJobDtos_JobsInProcess_Employer(employer.getUserId());

		List<JobDTO> jobDtos_jobsCompleted = jobService.getJobDtos_JobsCompleted_Employer(employer.getUserId());

		
		model.addAttribute("yetToStartJobs_Dtos", jobDtos_jobsWaitingToStart);
		model.addAttribute("jobDtos_jobsInProcess", jobDtos_jobsInProcess);
		model.addAttribute("jobDtos_jobsCompleted", jobDtos_jobsCompleted);
		



		// *********************************************************************
		// *********************************************************************
		// When the profile is requested and presented to the user,
		// all the applications' "HasBeenViewed" property, for the user's active
		// jobs, will be set to true.
		// On second thought, this should be set to true when the user clicks
		// and views the new applicants...
		// Review this.
//		applicationService.setJobsApplicationsHasBeenViewed(yetToStartJobs, 1);
//		applicationService.setJobsApplicationsHasBeenViewed(activeJobs, 1);
		// *********************************************************************
		// *********************************************************************

	}

	public String getFindEmployeesResponseHTML(FindEmployeesDTO findEmployeesDto) {

		// Query the database
		List<JobSearchUser> employees = this.findEmployees(findEmployeesDto);

		StringWriter writer = new StringWriter();

		// Set the context
		final VelocityContext context = new VelocityContext();
		context.put("employees", employees);
		context.put("mathUtility", MathUtility.class);

		findEmployeesResponseTemplate.merge(context, writer);

		return writer.toString();

	}



	public void setModel_WorkHistoryByUser(Model model, int userId) {
		
		
		
		JobSearchUserDTO userDto = new JobSearchUserDTO();
		userDto.setJobDtos_jobsCompleted(jobService.getJobDtos_JobsCompleted_Employee(userId));
		
		model.addAttribute("userDto", userDto);

	}
	
	

	public void setModel_Applicants(Model model, int jobId) {
		
		List<JobSearchUser> applicants = this.getApplicantsByJob_SubmittedOrConsidered(jobId);
		model.addAttribute("applicants", applicants);
		
	}

	public void setModel_WorkHistoryForAllApplicants(Model model, int userId, int jobId) {
		this.setModel_Applicants(model, jobId);
		this.setModel_WorkHistoryByUser(model, userId);
		model.addAttribute("clickedUserId", userId);
	}

	public List<JobSearchUserDTO> getEmployeeDtosByJob(int jobId) {
		
		// Query the database
		List<JobSearchUser> employees = this.getEmployeesByJob(jobId);
		
		// Create user dtos
		List<JobSearchUserDTO> employeeDtos = new ArrayList<JobSearchUserDTO>();		
		for(JobSearchUser employee : employees){
			JobSearchUserDTO employeeDto = new JobSearchUserDTO();
			employeeDto.setUser(employee);
			employeeDto.setRating(this.getRatingDtoByUserAndJob(employee.getUserId(), jobId));
			employeeDto.setWage(applicationService.getWage(employee.getUserId(), jobId));
			
			employeeDtos.add(employeeDto);			
		}
		
		return employeeDtos;
	}

	public RatingDTO getRatingDtoByUserAndJob(int userId, int jobId) {
		
		RatingDTO ratingDto = new RatingDTO();
		
		ratingDto.setRateCriteria(this.getRatingCriteia());		
		
		for(RateCriterion rateCriterion : ratingDto.getRateCriteria()){
			rateCriterion.setValue(this.getRatingValue_ByUserAndJob(rateCriterion.getRateCriterionId(),
																		userId, jobId));
		}
		
		ratingDto.setValue(this.getRatingValueForJob(ratingDto));
		ratingDto.setComment(this.getComment(jobId, userId));
		ratingDto.setEndorsements(this.getUsersEndorsementsByJob(userId, jobId));
		
		return ratingDto;
	}

	private double getRatingValue_ByUserAndJob(int rateCriterionId, int userId, int jobId) {
		
		return repository.getRatingValue_ByUserAndJob(rateCriterionId, userId, jobId);
	}

	public void setModel_Profile(Model model, HttpSession session) {
	
		JobSearchUser sessionUser = SessionContext.getUser(session);

		if (sessionUser.getProfile().getName().equals("Employee")) {
			this.setModel_EmployeeProfile(sessionUser, model);
		}
		else{
			this.setModel_EmployerProfile(sessionUser, model);
		}
		
	}

	public void setSession_Login(JobSearchUser user, HttpSession session) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		user = this.getUserByEmail(auth.getName());
		
		SessionContext.setUser(session, user);
		
//		if(user != null){
////			model.addAttribute("user", user);
//			session.setAttribute("user", user);
//			
//			return true;
//		}
//		else{
//			return false;
//		}
		
	}
	

	public void setSession_EmailValidation(int userId, HttpSession session) {

		JobSearchUser user = this.getUser(userId);
		SessionContext.setUser(session, user);
		
	}

	public String getProfileJspName(HttpSession session) {
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
			
		if (sessionUser.getProfile().getName().equals("Employee")) return "/employee_profile/EmployeeProfile";
		else return "/employer_profile/EmployerProfile";

	}
	

	public JobSearchUserDTO getUserDTO_FindJobs_PageLoad(HttpSession session) {
		
		if(SessionContext.isLoggedIn(session)){
			JobSearchUserDTO userDto = new JobSearchUserDTO();
			
			userDto.setUser((JobSearchUser) session.getAttribute("user"));
			userDto.setSavedFindJobFilters(jobService.getSavedFindJobFilters(userDto.getUser().getUserId()));

			return userDto;
	
		}
		else return null;
		
	}

	public void setModel_Availability(Model model, HttpSession session) {

		JobSearchUserDTO userDto = new JobSearchUserDTO();
		userDto.setUser(SessionContext.getUser(session));
		userDto.setAvailableDays(this.getAvailableDays(userDto.getUser().getUserId()));
		
		model.addAttribute("userDto", userDto);
		
	}



}
