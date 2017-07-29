
package com.jobsearch.user.service;

import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.email.Mailer;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.CalendarDay;
import com.jobsearch.model.EmployeeSearch;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.proposal.service.ProposalServiceImpl;
import com.jobsearch.responses.ViewEmployerHomepageResponse;
import com.jobsearch.responses.ViewEmployerHomepageResponse.EmployerHomepageJob;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.rate.SubmitRatingDTO;
import com.jobsearch.user.repository.UserRepository;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.DistanceUtility;
import com.jobsearch.utilities.MathUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

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
	VerificationServiceImpl verificationService;
	@Autowired
	Mailer mailer;
	@Autowired
	ProposalServiceImpl proposalService;

	@Value("${host.url}")
	private String hostUrl;

	public JobSearchUserDTO createUser(JobSearchUser proposedUser) {

		// proposedUser = new JobSearchUser();

		JobSearchUserDTO newUserDto = getNewUserDto(proposedUser);
		newUserDto.setUser(proposedUser);

//		if (!newUserDto.getIsInvalidNewUser()) {

			proposedUser.setPassword(encryptPassword(proposedUser.getPassword()));

			JobSearchUser newUser = repository.createUser(proposedUser);

			mailer.sendMail(proposedUser.getEmailAddress(), "email verification",
					"please click the link to verify your email " + hostUrl + "/JobSearch/email/validate?userId="
							+ newUser.getUserId());

//		}

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
		if (proposedUser.getEmailAddress() == null || proposedUser.getEmailAddress().matches("")) {

			newUserDto.setIsInvalidEmail_format(true);
			newUserDto.setIsInvalidNewUser(true);
		} else {
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

			} catch (AddressException ex) {

				newUserDto.setIsInvalidEmail_format(true);
				newUserDto.setIsInvalidNewUser(true);
			}
		}

		// Matching email
		if (proposedUser.getMatchingEmailAddress() == null
				|| !proposedUser.getEmailAddress().matches(proposedUser.getMatchingEmailAddress())) {

			newUserDto.setIsInvalidMatchingEmail(true);
			newUserDto.setIsInvalidNewUser(true);

		}

		// Password
		if (proposedUser.getPassword() == null
				|| (proposedUser.getPassword().length() < 6 || proposedUser.getPassword().length() > 20)) {

			newUserDto.setIsInvalidPassword(true);
			newUserDto.setIsInvalidNewUser(true);

		}

		// Matching password
		if (proposedUser.getMatchingPassword() == null
				|| !proposedUser.getPassword().matches(proposedUser.getMatchingPassword())) {

			newUserDto.setIsInvalidMatchingPassword(true);
			newUserDto.setIsInvalidNewUser(true);

		}

		// First name
		if (proposedUser.getFirstName() == null || proposedUser.getFirstName().matches("")) {
			newUserDto.setIsInvalidFirstName(true);
			newUserDto.setIsInvalidNewUser(true);
		}

		// Last name
		if (proposedUser.getLastName() == null || proposedUser.getLastName().matches("")) {
			newUserDto.setIsInvalidLastName(true);
			newUserDto.setIsInvalidNewUser(true);
		}

		// Profile Id
		if (proposedUser.getProfileId() != Profile.PROFILE_ID_EMPLOYEE
				&& proposedUser.getProfileId() != Profile.PROFILE_ID_EMPLOYER) {

			newUserDto.setIsInvalidProfile(true);
			newUserDto.setIsInvalidNewUser(true);

		}

		return newUserDto;

	}

	private String encryptPassword(String password) {
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		return encryptor.encryptPassword(password);
	}

	public JobSearchUser getUserByEmail(String emailAddress) {
		return repository.getUserByEmail(emailAddress);

	}

	public JobSearchUser getUser(int userId) {
		return repository.getUser(userId);
	}

	public List<JobSearchUser> getUsersEmployedForJob(int jobId) {
		return repository.getEmployeesByJob(jobId);
	}

	public List<Profile> getProfiles() {
		return repository.getProfiles();
	}

	public void insertRatings(List<SubmitRatingDTO> submitRatingDTOs, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);

		// For each employee's rating
		for (SubmitRatingDTO submitRatingDto : submitRatingDTOs) {

			// Rate criterion
			for (RateCriterion rc : submitRatingDto.getRateCriteria()) {
				rc.setUserId_ratee(submitRatingDto.getUserId_ratee());
				rc.setUserId_rater(sessionUser.getUserId());
				rc.setJobId(submitRatingDto.getJobId());
				repository.updateRating(rc);
			}

			// Comment
			deleteComment(submitRatingDto.getJobId(), submitRatingDto.getUserId_ratee());
			if (submitRatingDto.getCommentString() != "") {
				repository.addComment(submitRatingDto.getUserId_ratee(), submitRatingDto.getJobId(),
						submitRatingDto.getCommentString(), sessionUser.getUserId());
			}

		}

	}

	public void deleteComment(int jobId, int employeeId) {
		repository.deleteComment(jobId, employeeId);

	}

	public List<RateCriterion> getRatingCriteia_toRateEmployee() {
		return repository.getRatingCriteia_toRateEmployee();
	}

	public Double getRating(int userId) {

		// Round to the nearest tenth. 0 is the minimum value.
		return MathUtility.round(repository.getRating(userId), 1, 0);
	}

	public String getComment(int jobId, int userId) {

		return repository.getComment(jobId, userId);
	}

	public void editEmployeeSettings(JobSearchUser user_edited, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);
		user_edited.setUserId(sessionUser.getUserId());

		// Location
		Coordinate coordinate = GoogleClient.getCoordinate(user_edited);
		if (coordinate != null) {
			repository.updateHomeLocation(user_edited, coordinate);
		}

		// Min desired pay
		if (verificationService.isPositiveNumber(user_edited.getMinimumDesiredPay())) {
			repository.updateMinimumDesiredPay(user_edited.getUserId(), user_edited.getMinimumDesiredPay());
		}

		// Work radius
		if (verificationService.isPositiveNumber(user_edited.getMaxWorkRadius())) {
			repository.updateMaxDistanceWillingToWork(user_edited.getUserId(), user_edited.getMaxWorkRadius());
		}

		// About
		if (user_edited.getAbout() == null) {
			repository.updateAbout(user_edited.getUserId(), user_edited.getAbout());
		} else if (sessionUser.getAbout() == null) {
			repository.updateAbout(user_edited.getUserId(), user_edited.getAbout());
		} else if (!sessionUser.getAbout().matches(user_edited.getAbout())) {
			repository.updateAbout(user_edited.getUserId(), user_edited.getAbout());
		}

		this.updateSessionUser(session);
	}

	public void updateSessionUser(HttpSession session) {
		JobSearchUser user = getUser(SessionContext.getUser(session).getUserId());
		session.setAttribute("user", user);
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

	public void insertEmployment(int userId, int jobId) {

		repository.insertEmployment(userId, jobId);

		Application application = applicationService.getApplication(jobId, userId);
		applicationService.updateApplicationFlag(application, "IsAccepted", 1);
		
		insertRatings_toRateEmployer(jobId, userId);
		insertRatings_toRateEmployees(jobId, userId);

		jobService.inspectJob_isStillAcceptingApplications(jobId);

	}

	public void setviewEmployeeHomepageResponse(JobSearchUser employee, Model model, HttpSession session) {
		
		// Refactor: remove "user" from model. use "userDto" instead.

		List<Application> applications = applicationService.getApplications_byUser_openOrAccepted(employee.getUserId());
		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();

		// Applications
		for (Application application : applications) {

			ApplicationDTO applicationDto = new ApplicationDTO();

			applicationDto.setApplication(application);

			// Proposal
			applicationDto.setEmploymentProposalDto(
					applicationService.getCurrentEmploymentProposal(application.getApplicationId()));
			applicationDto.getEmploymentProposalDto().setIsProposedToSessionUser(
					applicationService.getIsProposedToSessionUser(session, applicationDto.getEmploymentProposalDto()));
			applicationDto.setPreviousProposal(applicationService.getPreviousProposal(
					applicationDto.getEmploymentProposalDto().getEmploymentProposalId(),
					application.getApplicationId()));
			
			applicationDto.setCurrentProposalStatus(proposalService.getCurrentProposalStatus(
					application.getIsOpen(),
					application.getIsAccepted(),
					applicationDto.getEmploymentProposalDto().getProposedByUserId(),
					employee.getUserId(),
					employee.getProfileId()));
			
			applicationDto.setMessages(applicationService.getMessages(employee,
					applicationDto.getApplication(),
					applicationDto.getPreviousProposal(),
					applicationDto.getEmploymentProposalDto()));

			// Job dto
			applicationDto.getJobDto().setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(applicationDto.getJobDto().getJob().getId()));
			applicationDto.getJobDto()
					.setMilliseconds_startDate(applicationDto.getJobDto().getJob().getStartDate_local().toEpochDay());
			applicationDto.getJobDto()
					.setMilliseconds_endDate(applicationDto.getJobDto().getJob().getEndDate_local().toEpochDay());

			applicationDto.getJobDto()
					.setDistance(DistanceUtility.getDistance(employee, applicationDto.getJobDto().getJob()));

			// Miscellaneous
			applicationDto.setTime_untilEmployerApprovalExpires(
					applicationService.getTime_untilEmployerApprovalExpires(
							applicationDto.getEmploymentProposalDto().getExpirationDate()));

			applicationDto.getJobDto().setDate_firstWorkDay(DateUtility.getMinimumDate(applicationDto.getJobDto().getWorkDays()).toString());
			applicationDto.getJobDto().setMonths_workDaysSpan(DateUtility.getMonthSpan(applicationDto.getJobDto().getWorkDays()));
			applicationDtos.add(applicationDto);
			
			applicationService.inspectNewness(applicationDto.getApplication());
		}
		model.addAttribute("applicationDtos", applicationDtos);
		
		JobSearchUserDTO userDto = new JobSearchUserDTO();
		userDto.setUser(employee);
		userDto.setCountApplications_open(applications
				.stream().filter(a -> a.getIsAccepted() == 0 && a.getIsOpen() == 1).count());
		userDto.setCountJobs_employment(applications
				.stream().filter(a -> a.getIsAccepted() == 1).count());
		userDto.setCountProposals_waitingOnYou(applicationDtos
				.stream()
				.filter(aDto -> aDto.getApplication().getIsAccepted() == 0)
				.filter(aDto -> aDto.getEmploymentProposalDto()
						.getIsProposedToSessionUser() == true).count());
		userDto.setCountProposals_waitingOnYou_new(applicationDtos
				.stream()
				.filter(aDto -> aDto.getApplication().getIsAccepted() == 0)
				.filter(aDto -> aDto.getEmploymentProposalDto().getIsNew() == 1)
				.count());
		userDto.setCountProposals_waitingOnOther(applicationDtos
				.stream()
				.filter(aDto -> aDto.getApplication().getIsAccepted() == 0)
				.filter(aDto -> aDto.getEmploymentProposalDto()
						.getIsProposedToSessionUser() == false).count());

		
		// Jobs the user was terminated from
		List<Job> jobs_terminated = jobService.getJobs_terminatedFrom_byUser(employee.getUserId());
		model.addAttribute("jobs_terminated", jobs_terminated);
		
		// Applications that were closed due to the employer filling all positions
		List<Application> applications_closedDueToAllPositionsFilled_unacknowledged =
				applicationService.applications_closedDueToAllPositionsFilled_unacknowledged(employee.getUserId());
		List<ApplicationDTO> applicationDtos_closedDueToAllPositionsFilled_unacknowledged =
				new ArrayList<ApplicationDTO>();
		if(verificationService.isListPopulated(applications_closedDueToAllPositionsFilled_unacknowledged)){
			for(Application application : applications_closedDueToAllPositionsFilled_unacknowledged){
				ApplicationDTO applicationDto = new ApplicationDTO();
				applicationDto.setApplication(application);
				applicationDto.getJobDto().setJob(jobService.getJob(application.getJobId()));
				applicationDtos_closedDueToAllPositionsFilled_unacknowledged.add(applicationDto);
			}
		}		
		model.addAttribute("applicationDtos_closedDueToAllPositionsFilled_unacknowledged",
				applicationDtos_closedDueToAllPositionsFilled_unacknowledged);
		
				 
		
		// Ideally this would be updated every time a page is loaded.
		// Since a job becomes one-that-needs-a-rating only after the passage of
		// time,
		// as opposed to a particular event (i.e. a page load), this is the best place to put it for now
		// because
		// I'm assuming this page loads most often.
		List<Job> jobs_needRating = jobService.getJobs_needRating_byEmployee(employee.getUserId());		
		session.setAttribute("jobs_needRating", jobs_needRating);
				
		List<CalendarDay> calendarDays_employmentSummary = getCalendarDays_employmentSummary(employee.getUserId());
		model.addAttribute("calendarDays_employmentSummary", calendarDays_employmentSummary);
		
		int monthSpan_employmentSummaryCalendar =
				DateUtility.getMonthSpan_new(calendarDays_employmentSummary
						.stream()
						.map(cd -> cd.getDate())
						.collect(Collectors.toList()));
		
		model.addAttribute("userDto", userDto);	
		model.addAttribute("user", employee);	
		model.addAttribute("monthSpan_employmentSummaryCalendar", monthSpan_employmentSummaryCalendar);
	}

	private List<CalendarDay> getCalendarDays_employmentSummary(int userId) {
		
		List<CalendarDay> calendarDays = new ArrayList<CalendarDay>();
		
		int saturdayCount = 0;	
		LocalDate day = LocalDate.now();
		
		while(saturdayCount < 2){
			
			CalendarDay calendarDay = new CalendarDay();
			calendarDay.setDate(day);
			
			String dateString = day.format(DateTimeFormatter.ISO_LOCAL_DATE);			
			List<Job> jobs_employment = jobService.getJobs_employment_byUserAndDate(userId, dateString);
			
			calendarDay.setJobDtos(new ArrayList<>());
			for ( Job job : jobs_employment ){				
				JobDTO jobDto = new JobDTO();
				jobDto.setJob(job);
				jobDto.setWorkDayDto(new WorkDayDto());
				jobDto.getWorkDayDto().setWorkDay(jobService.getWorkDay(job.getId(), dateString));
				calendarDay.getJobDtos().add(jobDto);
			}			
			calendarDays.add(calendarDay);
			
			if (day.getDayOfWeek() == DayOfWeek.SATURDAY) saturdayCount += 1;
			day = day.plusDays(1);
		}		
		return calendarDays;
	}

	public List<JobDTO> getJobDtos_ApplicationInvites(int userId) {

		List<ApplicationInvite> applicationInvites = applicationService.getApplicationInvites(userId);

		List<JobDTO> jobDtos_applicationInvites = new ArrayList<JobDTO>();

		for (ApplicationInvite applicationInvite : applicationInvites) {
			JobDTO jobDto = new JobDTO();
			jobDto.setJob(jobService.getJob(applicationInvite.getJobId()));
			jobDto.setApplicationInvite(applicationInvite);
			jobDtos_applicationInvites.add(jobDto);
		}

		return jobDtos_applicationInvites;
	}

	public void setviewEmployerHomepageResponse(JobSearchUser employer, Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);
		LocalDateTime now = LocalDateTime.now();
		ViewEmployerHomepageResponse response = new ViewEmployerHomepageResponse();		
		List<Job> jobs = jobService.getJobs_byEmployerAndStatuses(employer.getUserId(),
				Arrays.asList(Job.STATUS_FUTURE, Job.STATUS_PRESENT));

		for (Job job : jobs) {

			EmployerHomepageJob employerHomepageJob = new EmployerHomepageJob();
			employerHomepageJob.setJob(job);
			
			// Wage Proposals
			List<Proposal> currentProposals = proposalService.getCurrentProposals_byJob(
					job.getId());
			
			employerHomepageJob.setCountWageProposals_sent(
					currentProposals.stream()
									.filter(p -> p.getProposedByUserId() == sessionUser.getUserId())
									.count());

			employerHomepageJob.setCountWageProposals_received(
					currentProposals.stream()
									.filter(p -> p.getProposedToUserId() == sessionUser.getUserId())
									.count());
			
			employerHomepageJob.setCountWageProposals_received_new(
					currentProposals.stream()
						.filter(p -> p.getProposedByUserId() == sessionUser.getUserId())
						.filter(p -> p.getIsNew() == 1)
						.count());		
			
			employerHomepageJob.setCountProposals_expired(
					currentProposals.stream()
						.filter(p -> p.getExpirationDate() != null)
						.filter(p -> p.getExpirationDate().isBefore(now))
						.count());		

			// Applications
			List<Application> applications = applicationService.getApplications_byJob(job.getId());
			employerHomepageJob.setCountApplications_total(
					applications.stream()
								.filter(a -> a.getIsAccepted() == 0)
								.filter(a -> a.getIsOpen() == 1)
								.count());

			employerHomepageJob.setCountApplications_new(
					applications.stream()
						.filter(a -> a.getIsNew() == 1)
						.count());
			
			// Employees
			employerHomepageJob.setCountEmployees_hired(
					applications.stream()
						.filter(a -> a.getIsAccepted() == 1)
						.filter(a -> a.getIsOpen() == 1)
						.count());
			
			// Other job details
//				jobDto.setDaysUntilStart(DateUtility.getTimeSpan(LocalDate.now(), LocalTime.now(),
//						job.getStartDate_local(), job.getStartTime_local(), DateUtility.TimeSpanUnit.Days));

			response.getEmployerHomepageJobs().add(employerHomepageJob);			
		}

		// Ideally this would be updated every time a page is loaded.
		// Since a job becomes one-that-needs-a-rating only after the passage of
		// time,
		// as opposed to a particular event, this is the best place to put it
		// becuase
		// I'm assuming this page loads most often.
		List<Job> jobs_needRating = jobService.getJobs_needRating_byEmployeer(employer.getUserId());		
		session.setAttribute("jobs_needRating", jobs_needRating);
		model.addAttribute("response", response);
	}

	public List<JobSearchUserDTO> getEmployeeDtosByJob(int jobId) {

		// Query the database
		List<JobSearchUser> employees = this.getUsersEmployedForJob(jobId);

		// Create user dtos
		List<JobSearchUserDTO> employeeDtos = new ArrayList<JobSearchUserDTO>();
		for (JobSearchUser employee : employees) {

			JobSearchUserDTO employeeDto = new JobSearchUserDTO();

			employeeDto.setUser(employee);
			employeeDto.setRatingValue_overall(this.getRating(employee.getUserId()));
			Application application = applicationService.getApplication(jobId, employee.getUserId());
			employeeDto.setAcceptedProposal(
					applicationService.getCurrentEmploymentProposal(application.getApplicationId()));
			employeeDto.setTotalPayment(applicationService.getTotalPayment(employeeDto.getAcceptedProposal()));

			employeeDtos.add(employeeDto);
		}

		return employeeDtos;
	}

	public RatingDTO getRatingDto_byUser(JobSearchUser user) {

		RatingDTO ratingDto = new RatingDTO();

		if (user.getProfileId() == Profile.PROFILE_ID_EMPLOYEE)
			ratingDto.setRateCriteria(this.getRatingCriteia_toRateEmployee());
		else
			ratingDto.setRateCriteria(this.getRatingCriteia_toRateEmployer());

		for (RateCriterion rateCriterion : ratingDto.getRateCriteria()) {

			rateCriterion.setValue(
					this.getRatingValue_byCriteriaAndUser(rateCriterion.getRateCriterionId(), user.getUserId()));
			if (rateCriterion.getValue() != null)
				rateCriterion.setStringValue(String.format("%.1f", rateCriterion.getValue()));
		}

		return ratingDto;

	}

	private Double getRatingValue_byCriteriaAndUser(Integer rateCriterionId, int userId) {

		return repository.getRatingValue_byCriteriaAndUser(rateCriterionId, userId);
	}

	public void setModel_Profile(Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);

		if (sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			this.setviewEmployeeHomepageResponse(sessionUser, model, session);
		} else
			this.setviewEmployerHomepageResponse(sessionUser, model, session);

	}

	public void setSession_Login(JobSearchUser user, HttpSession session) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		user = this.getUserByEmail(auth.getName());

		SessionContext.setUser(session, user);
//		List<Job> jobs_needRating = jobService.getJobs_needRating_byEmployee(user.getUserId());		
//		session.setAttribute("jobs_needRating", jobs_needRating);			
	}

	public void setSession_EmailValidation(int userId, HttpSession session) {

		JobSearchUser user = this.getUser(userId);
		SessionContext.setUser(session, user);

	}

	public String getProfileJspName(HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);

		if (sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE)
			return "/employee_profile/Profile_Employee_NEW";
		else
			return "/homepage_employer/Homepage_Employer";

	}

	public JobSearchUserDTO getUserDTO_FindJobs_PageLoad(HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {
			JobSearchUserDTO userDto = new JobSearchUserDTO();

			userDto.setUser((JobSearchUser) session.getAttribute("user"));
			userDto.setSavedFindJobFilters(jobService.getSavedFindJobFilters(userDto.getUser().getUserId()));

			return userDto;

		}
		return null;

	}

	public void setModel_findEmployees_pageLoad(Model model, HttpSession session) {

		if (SessionContext.isLoggedIn(session)) {

			JobSearchUser sessionUser = SessionContext.getUser(session);

			List<Job> jobs_current = jobService.getJobs_byEmployerAndStatuses(sessionUser.getUserId(),
					Arrays.asList(Job.STATUS_FUTURE, Job.STATUS_PRESENT));

			List<JobDTO> jobDtos_current = new ArrayList<JobDTO>();
			for (Job job_current : jobs_current) {

				// ***********************************************
				// This is a bit overkill.
				// The model doesn't need this much info.
				// Address this later.
				JobDTO jobDto_current = jobService.getJobDTO_DisplayJobInfo(job_current.getId());
				// *****************************************************

				jobDtos_current.add(jobDto_current);
			}

			model.addAttribute("jobDtos_current", jobDtos_current);
		}

	}

	public void setModel_findEmployees_results(Model model, EmployeeSearch employeeSearch) {

		List<JobSearchUserDTO> userDtos = new ArrayList<JobSearchUserDTO>();

		if (GoogleClient.isValidAddress(employeeSearch.getAddress())) {
			
			Coordinate coordinate = GoogleClient.getCoordinate(employeeSearch.getAddress());
			employeeSearch.setLat(coordinate.getLatitude());
			employeeSearch.setLng(coordinate.getLongitude());

			List<JobSearchUser> users = this.getUsers_ByFindEmployeesSearch(employeeSearch);
			boolean doShowAvailability = true;
			if(employeeSearch.getWorkDays().size() == 0) doShowAvailability = false; 

			for (JobSearchUser user : users) {
				JobSearchUserDTO userDto = new JobSearchUserDTO();

				userDto.setUser(user);
				userDto.setRatingValue_overall(this.getRating(user.getUserId()));			
				userDto.setCount_jobsCompleted(
						jobService.getCount_JobsCompleted_ByUser(user.getUserId()));

				if(doShowAvailability){
					userDto.setCount_availableDays_perFindEmployeesSearch(
							jobService.getCount_availableDays_ByUserAndWorkDays(user.getUserId(),
									employeeSearch.getWorkDays()));	
				}
				
				userDtos.add(userDto);
			}
			model.addAttribute("employeeSearch", employeeSearch);
			model.addAttribute("doShowAvailability", doShowAvailability);
			model.addAttribute("userDtos", userDtos);
		}

	}

	public List<JobSearchUser> getUsers_ByFindEmployeesSearch(EmployeeSearch employeeSearch) {
		return repository.getUsers_ByFindEmployeesSearch(employeeSearch);
	}

	public List<RateCriterion> getRatingCriteia_toRateEmployer() {

		return repository.getRateCriteria_toRateEmployer();
	}

	public void insertRatings_toRateEmployer(int jobId, int userId_emloyee) {

		List<RateCriterion> rateCriteria = this.getRatingCriteia_toRateEmployer();
		Job job = jobService.getJob(jobId);

		for (RateCriterion rateCriterion : rateCriteria) {
			repository.insertRating(rateCriterion.getRateCriterionId(), job.getUserId(), job.getId(),
					userId_emloyee);
		}
	}

	public void insertRatings_toRateEmployees(int jobId, int userId_employee) {

		List<RateCriterion> rateCriteria = this.getRatingCriteia_toRateEmployee();
		Job job = jobService.getJob(jobId);
		
		for (RateCriterion rateCriterion : rateCriteria) {
			repository.insertRating(rateCriterion.getRateCriterionId(), userId_employee, job.getId(),
					job.getUserId());
		}
	}

	public void setModel_viewCalendar_employee(Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);

		List<Application> applications = applicationService
				.getApplications_byUser_openOrAccepted(sessionUser.getUserId());

		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
		for (Application application : applications) {
			ApplicationDTO applicationDto = new ApplicationDTO();
			applicationDto.setApplication(application);
			applicationDto.getJobDto().setJob(jobService.getJob(application.getJobId()));
			applicationDto.setEmploymentProposalDto(
					applicationService.getCurrentEmploymentProposal(application.getApplicationId()));
			applicationDtos.add(applicationDto);
		}

		model.addAttribute("applicationDtos", applicationDtos);

	}

	public List<JobSearchUser> getApplicants_whoAreAvailableButDidNotApplyForDate(int jobId, String dateString) {

		EmployeeSearch employeeSearch = new EmployeeSearch();

		JobDTO jobDto_findEmployees = new JobDTO();
		jobDto_findEmployees.setJob(jobService.getJob(jobId));
		jobDto_findEmployees.getWorkDays().add((new WorkDay(dateString)));

//		employeeSearch.setJobDto(jobDto_findEmployees);
//		employeeSearch.setJobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays(jobId);

		return getUsers_ByFindEmployeesSearch(employeeSearch);

	}

	public List<JobSearchUser> getUsers_whoAreAvailableButHaveNotApplied(int jobId, String dateString) {

		EmployeeSearch employeeSearch = new EmployeeSearch();

		JobDTO jobDto_findEmployees = new JobDTO();
		jobDto_findEmployees.setJob(jobService.getJob(jobId));
		jobDto_findEmployees.getWorkDays().add((new WorkDay(dateString)));

//		employeeSearch.setJobDto(jobDto_findEmployees);
//		employeeSearch.setJobId_excludeApplicantsOfThisJob(jobId);

		return getUsers_ByFindEmployeesSearch(employeeSearch);
	}

	public Double getRating_byJobAndUser(Integer jobId, int userId) {

		return repository.getRating_byJobAndUser(jobId, userId);
	}

	public void setModel_getRatings_byUser(Model model, int userId) {

		JobSearchUserDTO userDto = new JobSearchUserDTO();
		userDto.setUser(getUser(userId));

		List<Job> jobs_completed = new ArrayList<Job>();
		if (userDto.getUser().getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			jobs_completed = jobService.getJobs_ByEmployeeAndJobStatuses(userId,
					Arrays.asList(Job.STATUS_PAST));
		} else {
			jobs_completed = jobService.getJobs_byEmployerAndStatuses(userId,
					Arrays.asList(Job.STATUS_PAST));
		}

		userDto.setRatingValue_overall(getRating(userDto.getUser().getUserId()));
		userDto.setRatingDto(getRatingDto_byUser(userDto.getUser()));

		userDto.setJobDtos_jobsCompleted(new ArrayList<JobDTO>());
		for (Job job_complete : jobs_completed) {
			JobDTO jobDto = new JobDTO();

			jobDto.setJob(job_complete);
			jobDto.setRatingValue_overall(getRating_byJobAndUser(job_complete.getId(),
					userDto.getUser().getUserId()));

			jobDto.setComments(
					jobService.getCommentsGivenToUser_byJob(userDto.getUser().getUserId(),
							job_complete.getId()));

			if(jobDto.getRatingValue_overall() != null) userDto.getJobDtos_jobsCompleted().add(jobDto);
		}

		boolean userHasEnoughRatingData = false;
		if(userDto.getJobDtos_jobsCompleted() != null &&
				userDto.getJobDtos_jobsCompleted().size() > 0 && 
				userDto.getRatingValue_overall() != null)			
				userHasEnoughRatingData = true;
		
		model.addAttribute("userHasEnoughRatingData", userHasEnoughRatingData);
		model.addAttribute("userDto_ratings", userDto);
	}

	public List<JobSearchUser> getEmployees_byJobAndDate(int jobId, List<String> dateStrings) {
		return repository.getEmployees_byJobAndDate(jobId, dateStrings);
	}

	public void setModel_employeeLeaveJob_confirm(Integer jobId, Model model, HttpSession session) {
		
		if(verificationService.isSessionUserAnEmployee(session, jobId)){
			boolean hasJobStarted = jobService.getHasJobStarted(jobId);
			boolean isJobComplete = jobService.getIsJobComplete(jobId);	
			
			model.addAttribute("jobId", jobId);
			model.addAttribute("hasJobStarted", hasJobStarted);
			model.addAttribute("isJobComplete", isJobComplete);
		}		
	}	

	public JobSearchUser getEmployee(Integer jobId, int userId) {
		return repository.getEmployee(jobId, userId);
	}

	public List<JobSearchUser> getEmployees_whoLeft(boolean departureHasBeenAcknowledged, int jobId) {
		return repository.getEmployees_whoLeft(departureHasBeenAcknowledged, jobId);
	}

	public void acknowledgeEmployeeLeft(int jobId, int userId, HttpSession session) {
		if(verificationService.didSessionUserPostJob(session, jobId)){
			jobService.updateEmploymentFlag(jobId, userId, "Flag_EmployerAcknowledgedEmployeeLeftJob", 1);
		}
		
	}

	public Double getRating_givenByUser(Integer jobId, int userId) {		
		return repository.getRating_givenByUser(jobId, userId);
	}

	public List<JobSearchUser> getEmployees_byJob_completedWork(int jobId) {
		return repository.getEmployees_byJob_completedWork(jobId);
	}

	public Integer getCount_nullRatings_givenByUserForJob(Integer jobId, int userId) {
		return repository.getCount_nullRatings_givenByUserForJob(jobId, userId);
	}

	public List<JobSearchUser> getUsersWithOpenApplicationToJob(int jobId) {
	
		return repository.getApplicants_byJob_openApplicantions(jobId);
	}

	public void setModel_makeOffer_initialize(Model model, int userId, HttpSession session) {
		List<Job> openJobs = jobService.getJobs_byEmployerAndStatuses(
				SessionContext.getUser(session).getUserId(),
				Arrays.asList(Job.STATUS_PRESENT, Job.STATUS_FUTURE));
		
		model.addAttribute("openJobs", openJobs);
		model.addAttribute("user_makeOfferTo", getUser(userId));
	}

	public String getAvailabliltyStatusMessage_forUserAndJob(int userId, int jobId) {
		
		Job job = jobService.getJob(jobId);
		List<String> workDays = jobService.getWorkDayDateStrings(jobId);		
		Integer availableDays = jobService.getCount_availableDays_ByUserAndWorkDays(userId, workDays);
		
		if(verificationService.didUserApplyForJob(jobId, userId)) return "already-applied";
		else if(job.getIsPartialAvailabilityAllowed()){
			if(availableDays > 0) return "available";
			else return "unavailable";
		}else{
			if(availableDays == workDays.size()) return "available";
			else return "unavailable";
		}
				
	}

	public void setModel_makeOffer(Model model, int userId_makeOfferTo, int jobId, HttpSession session) {

		if(!verificationService.didUserApplyForJob(jobId, userId_makeOfferTo) &&
				verificationService.didSessionUserPostJob(session, jobId)){
			
			JobDTO jobDto = new JobDTO();
			jobDto.setJob(jobService.getJob(jobId));
			jobDto.setWorkDayDtos(jobService.getWorkDayDtos(jobId));
			jobDto.setWorkDays(jobService.getWorkDays(jobId));
			jobDto.setDate_firstWorkDay(DateUtility.getMinimumDate(jobDto.getWorkDays()).toString());
			jobDto.setMonths_workDaysSpan(DateUtility.getMonthSpan(jobDto.getWorkDays()));

			model.addAttribute("json_workDayDtos", JSON.stringify(jobDto.getWorkDayDtos()));
			model.addAttribute("jobDto", jobDto);
			model.addAttribute("user_makeOfferTo", getUser(userId_makeOfferTo));
			model.addAttribute("context", "employer-make-initial-offer");
			
		}
		
		
	}
	
	// Refactored
	// ******************************************************************************************
	// ******************************************************************************************	

	public void setViewHomepageResponse(Model model, HttpSession session) {
	
		JobSearchUser sessionUser = SessionContext.getUser(session);

		if (sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			setviewEmployeeHomepageResponse(sessionUser, model, session);
		} else
			setviewEmployerHomepageResponse(sessionUser, model, session);

		
	}


}
