package com.jobsearch.user.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.email.Mailer;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.service.SubmitJobPostingRequestDTO;
import com.jobsearch.job.service.JobInfoPostRequestDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.DummyData;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.user.rate.RatingRequestDTO;
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

	public JobSearchUser getProfile(JobSearchUser user) {

		// If employer
		if (user.getProfileId() == 2) {
			user.setActiveJobs(jobService.getActiveJobsByUser(user.getUserId()));
			user.setCompletedJobs(jobService.getCompletedJobsByEmployer(user.getUserId()));
		} else if (user.getProfileId() == 1) {

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

	public void rateEmployee(RatingRequestDTO ratingDTO) {

		for (RateCriterion rc : ratingDTO.getRateCriteria()) {
			repository.updateRating(rc);
		}

		deleteEndorsements(ratingDTO.getEmployeeId(), ratingDTO.getJobId());
		for (Endorsement endorsement : ratingDTO.getEndorsements()) {
			repository.addEndorsement(endorsement);
		}

		deleteComment(ratingDTO.getJobId(), ratingDTO.getEmployeeId());
		if (ratingDTO.getComment() != "") {
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
			int endorsementCount = repository.getEndorsementCountByCategory(userId, category.getId());
			endorsement.setCount(endorsementCount);

			endorsements.add(endorsement);
		}

		return endorsements;

	}

	public List<Endorsement> getUsersEndorsementsByJob(int userId, int jobId) {

		// NOTE: this does not return ALL endorsements.
		// This consolidates ALL endorsements into each endorsement's category.
		// For instance, if a user has 10 concrete endorsements,
		// only one endorsement object will be created, but the count
		// property will be equal to 10.

		List<Endorsement> endorsements = new ArrayList<Endorsement>();

		// Get the category Ids that the user has endorsements for
		List<Integer> endorsementCategoryIds = repository.getEndorsementCategoryIdsByJob(userId, jobId);

		for (Integer endorsementCategoryId : endorsementCategoryIds) {

			Category category = categoryService.getCategory(endorsementCategoryId);

			Endorsement endorsement = new Endorsement();
			endorsement.setCategoryName(category.getName());
			endorsement.setCategoryId(category.getId());

			// Get how many endorsements the user has in the particular category
			// and job
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
<<<<<<< HEAD

		DummyData dummyData = new DummyData();
		List<JobSearchUser> dummyUsers = dummyData.getDummyUsers();

		int lastDummyCreationId = 0;
		try {
			lastDummyCreationId = repository.getLastDummyCreationId("user");	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		repository.createUsers_DummyData(dummyUsers, lastDummyCreationId + 1);
=======
//
//		DummyData dummyData = new DummyData();
//		List<JobSearchUser> dummyUsers = dummyData.getDummyUsers();
//
//		int lastDummyCreationId = repository.getLastDummyCreationId("user");
//		repository.createUsers_DummyData(dummyUsers, lastDummyCreationId + 1);
>>>>>>> 7f106e60c9eba9611b5f45b6f04ab771c727b47a

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
}
