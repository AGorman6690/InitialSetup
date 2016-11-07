package com.jobsearch.user.service;

import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.jobsearch.job.service.CompletedJobResponseDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.FailedWageNegotiationDTO;
import com.jobsearch.model.FindEmployeesDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.WageProposal;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.rate.SubmitRatingDTO;
import com.jobsearch.user.rate.SubmitRatingDTOs_Wrapper;
import com.jobsearch.user.repository.UserRepository;
import com.jobsearch.user.web.AvailabilityDTO;
import com.jobsearch.user.web.EditProfileRequestDTO;
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

	@Autowired
	@Qualifier("FindEmployeesResponseVM")
	Template findEmployeesResponseTemplate;

	public JobSearchUser createUser(JobSearchUser user) {

		if (this.isValidEmailRequest(user.getEmailAddress())) {
			user.setPassword(encryptPassword(user.getPassword()));

			JobSearchUser newUser = repository.createUser(user);

			if (newUser.getEmailAddress() != null) {
				mailer.sendMail(user.getEmailAddress(), "email verification",
						"please click the link to verify your email " + hostUrl + "/JobSearch/validateEmail?userId="
								+ newUser.getUserId());
			}
			return newUser;
		} else {
			return null;
		}
	}

	private boolean isValidEmailRequest(String email) {

		JobSearchUser user = repository.getUserByEmail(email);
		if (user == null) {
			return true;
		} else {
			return false;
		}

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

	public void updateAvailability(AvailabilityDTO availabilityDTO) {

		repository.deleteAvailability(availabilityDTO.getUserId());

		for (String date : availabilityDTO.getStringDays()) {
			repository.addAvailability(availabilityDTO.getUserId(), DateUtility.getSqlDate(date));
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
		JobSearchUser user = getUser(SessionContext.getSessionUser(session).getUserId());
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

	public void setEmployeesProfileModel(JobSearchUser employee, Model model) {

		// Availability
		List<String> availableDays = this.getAvailableDays(employee.getUserId());

		// Get the employee's open job applications
		List<ApplicationResponseDTO> openApplicationResponseDtos = applicationService
				.getOpenApplicationResponseDtosByApplicant(employee.getUserId());

		// //Verify the start date for the not-yet-started-jobs is still in the
		// future.
		// jobService.verifyJobStatusForUsersYetToStartJobs(employee.getUserId());

		// Get the employee's hired-for, but not-yet-started jobs
		List<Job> yetToStartJobs = jobService.getYetToStartJobsByEmployee(employee.getUserId());

		// Get the employee's completed jobs
		List<Job> completedJobs = jobService.getCompletedJobsByEmployee(employee.getUserId());

		// Get the employee's active jobs
		List<Job> activeJobs = jobService.getActiveJobsByEmployee(employee.getUserId());

		// Get the failed wage negotiations that the employee has been involved
		// in
		List<FailedWageNegotiationDTO> failedWageNegotiationDtos = applicationService
				.getFailedWageNegotiationsDTOsByUser(employee.getUserId());

		// Set the model attributes
		model.addAttribute("user", employee);
		model.addAttribute("availableDays", availableDays);
		model.addAttribute("failedWageNegotiationDtos", failedWageNegotiationDtos);
		model.addAttribute("yetToStartJobs", yetToStartJobs);
		model.addAttribute("activeJobs", activeJobs);
		model.addAttribute("completedJobs", completedJobs);
		model.addAttribute("openApplicationResponseDtos", openApplicationResponseDtos);

	}

	public void setEmployersProfileModel(JobSearchUser employer, Model model) {

		// Get the employer's yet-to-start jobs
		List<Job> yetToStartJobs = jobService.getYetToStartJobsByEmployer(employer.getUserId());
		List<JobDTO> yetToStartJobs_Dtos = jobService.getYetToStartJobsByEmployer_Dto(employer.getUserId());

		// Get the employer's active jobs
		List<Job> activeJobs = jobService.getActiveJobsByEmployer(employer.getUserId());

		// Get the employer's completed jobs
		List<CompletedJobResponseDTO> completedJobs = jobService.getCompletedJobsByEmployer(employer.getUserId());

		// Get the failed wage negotiations that the employer has been involved
		// in
		List<JobDTO> activeJobsWithFailedWageNegotiations = jobService
				.getJobsWithFailedWageNegotiations(employer.getUserId(), yetToStartJobs);

		// //Run the failed wage negotiations velocity template
		String vtFailedWageNegotiations = applicationService
				.getFailedWageNegotiationsVelocityTemplate(activeJobsWithFailedWageNegotiations);

		// Run a velocity template to render the yet-to-start-jobs table
		// String vtYetToStartJobs =
		// jobService.getEmployerProfileJobTableTemplate(employer,
		// yetToStartJobs, false);
		String vtYetToStartJobs = jobService.getEmployersJobsYetTemplate(employer, yetToStartJobs_Dtos, false);

		// Run a velocity template to render the active-jobs table
		String vtActiveJobs = jobService.getEmployerProfileJobTableTemplate(employer, activeJobs, true);

		model.addAttribute("vtFailedWageNegotiations", vtFailedWageNegotiations);
		model.addAttribute("vtYetToStartJobs", vtYetToStartJobs);
		model.addAttribute("vtActiveJobs", vtActiveJobs);
		// model.addAttribute("activeJobs", activeJobs);
		model.addAttribute("completedJobs", completedJobs);

		// When the profile is requested and presented to the user,
		// all the applications' "HasBeenViewed" property, for the user's active
		// jobs,
		// will be set to true.

		// *********************************************************************
		// *********************************************************************
		// On second thought, this should be set to zero when the user clicks
		// and views
		// the new applicants
		applicationService.setJobsApplicationsHasBeenViewed(yetToStartJobs, 1);
		applicationService.setJobsApplicationsHasBeenViewed(activeJobs, 1);
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

	public boolean isLoggedIn(HttpSession session) {

		// *************************************
		// *************************************
		// This is hackish.
		// Sometimes this user session attribute is not null...
		// That is why the email is verified not to be null after the
		// user is apparently "not" null...
		// *************************************
		// *************************************
		JobSearchUser user = SessionContext.getSessionUser(session);
		if (user == null) {
			return false;
		} else {
			if (user.getEmailAddress() == null) {
				return false;
			} else {
				return true;
			}

		}
	}

	public void setModel_WorkHistoryByUser(Model model, int userId) {
<<<<<<< HEAD
		
	
		List<CompletedJobResponseDTO> completedJobDtos = jobService.
											getCompletedJobResponseDtosByEmployee(userId);
		
		model.addAttribute("completedJobDtos", completedJobDtos);
		model.addAttribute("userId_employee", userId);
		
	}
=======

		List<CompletedJobResponseDTO> completedJobDtos = jobService.getCompletedJobResponseDtosByEmployee(userId);
		model.addAttribute("completedJobDtos", completedJobDtos);
>>>>>>> bf9c9a6c360a24a6b78679a3831fabd86140de1f

	}

}
