
package com.jobsearch.service;
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

import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.email.Mailer;
import com.jobsearch.google.Coordinate;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Application;
import com.jobsearch.model.CalendarDay;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.CalendarDay.CalendarDayJobDto;
import com.jobsearch.repository.UserRepository;
import com.jobsearch.request.FindEmployeesRequest;
import com.jobsearch.responses.GetProfileCalendarResponse;
import com.jobsearch.responses.InitMakeOfferResponse;
import com.jobsearch.responses.GetProfileCalendarResponse.CalendarApplication;
import com.jobsearch.responses.MessageResponse;
import com.jobsearch.responses.ViewEmployeeHomepageResponse;
import com.jobsearch.responses.ViewEmployeeHomepageResponse.ApplicationProgressStatus;
import com.jobsearch.responses.ViewEmployerHomepageResponse;
import com.jobsearch.responses.CurrentProposalResponse;
import com.jobsearch.responses.FindEmployeesResponse;
import com.jobsearch.responses.GetJobResponse;
import com.jobsearch.responses.FindEmployeesResponse.FindEmployeeUser;
import com.jobsearch.responses.ViewEmployerHomepageResponse.EmployerHomepageJob;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class UserServiceImpl {

	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	@Autowired
	UserRepository repository;
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
	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	WorkDayServiceImpl workDayService;


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
		
		ratingService.insertRatings_toRateEmployer(jobId, userId);
		ratingService.insertRatings_toRateEmployees(jobId, userId);

		jobService.inspectJob_isStillAcceptingApplications(jobId);

	}

	public void setViewEmployeeHomepageResponse(JobSearchUser employee, Model model, HttpSession session) {
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		LocalDateTime now = LocalDateTime.now();
		
		ViewEmployeeHomepageResponse response = new ViewEmployeeHomepageResponse();

		
		
		// User profile info
		response.setProfileInfoDto(getProfileInfoDto(sessionUser));
	
		// Current activity
		List<Application> applications = applicationService.getApplications_byUser_openOrAccepted(
				employee.getUserId());
		for (Application application : applications) {
			

						
			Proposal currentProposal = proposalService.getCurrentProposal(
					application.getApplicationId());				
			Proposal previousProposal = proposalService.getPreviousProposal(application.getApplicationId());
			Job job = jobService.getJob(application.getJobId());
			
			ApplicationProgressStatus applicationProgressStatus = new ApplicationProgressStatus();
			initializeApplicationProgressStatus(applicationProgressStatus, application.getJobId());
			applicationProgressStatus.setApplication(application);

			
			// Current proposal
			applicationProgressStatus.setCurrentProposal(currentProposal);
			applicationProgressStatus.setIsCurrentProposalExpired(
					DateUtility.isDateExpired(currentProposal.getExpirationDate(), now));								
			applicationProgressStatus.setIsProposedToSessionUser(
					proposalService.isProposedToUser(currentProposal, sessionUser.getUserId()));	
			applicationProgressStatus.setTime_untilEmployerApprovalExpires(
					proposalService.getTime_untilEmployerApprovalExpires(
							currentProposal.getExpirationDate(), now));
			applicationProgressStatus.setCurrentProposalStatus(
					proposalService.getCurrentProposalStatus(
							application.getIsOpen(),
							application.getIsAccepted(),
							currentProposal.getProposedByUserId(),
							sessionUser.getUserId(),
							sessionUser.getProfileId()));
			applicationProgressStatus.setTime_untilEmployerApprovalExpires(
					proposalService.getTime_untilEmployerApprovalExpires(
							currentProposal.getExpirationDate(), now));
			
			
			applicationProgressStatus.setPreviousProposal(previousProposal);					
			
			applicationProgressStatus.setMessages(applicationService.getMessages(employee,
					application,
					previousProposal,
					currentProposal));
			
			applicationProgressStatus.setJob(job);
			
			response.getApplicationProgressStatuses().add(applicationProgressStatus);			
			applicationService.inspectNewness(application);		
		}
			

			
		// Proposals and employment counts
		response.setCountJobs_employment(response.getApplicationProgressStatuses()
				.stream()
				.map(aps -> aps.getApplication())
				.filter(a -> a.getIsAccepted() == 1)
				.count());
		
		response.setCountProposals_waitingOnYou(response.getApplicationProgressStatuses()
				.stream()
				.filter(aps -> aps.getApplication().getIsAccepted() == 0)
				.filter(aps -> aps.getIsProposedToSessionUser() == true)
				.count());
		
		response.setCountProposals_waitingOnYou_new(response.getApplicationProgressStatuses()
				.stream()
				.filter(aps -> aps.getApplication().getIsAccepted() == 0)
				.filter(aps -> aps.getIsProposedToSessionUser() == true)
				.map(aps -> aps.getCurrentProposal())
				.filter(p -> p.getIsNew() == 1)
				.count());
		
		response.setCountProposals_waitingOnOther(response.getApplicationProgressStatuses()
				.stream()
				.filter(aps -> aps.getApplication().getIsAccepted() == 0)
				.filter(aps -> aps.getIsProposedToSessionUser() == false)
				.count());		
		
		// Messages
		List<MessageResponse> messageResponses_jobsTerminatedFrom =
				jobService.getMessagesResponses_jobsTermintedFrom(employee.getUserId());		
		List<MessageResponse> messageResponses_applicationsClosedDueToAllPositionsFilled =
				applicationService.getMessagesResponses_applicationsClosedDueToAllPositionsFilled(
						employee.getUserId());
		
		
				
		List<CalendarDay> calendarDays_employmentSummary = getCalendarDays_employmentSummary(employee.getUserId());
		int monthSpan_employmentSummaryCalendar =
				DateUtility.getMonthSpan_new(calendarDays_employmentSummary
						.stream()
						.map(cd -> cd.getDate())
						.collect(Collectors.toList()));
		
		model.addAttribute("messageResponses_jobsTerminatedFrom",
				messageResponses_jobsTerminatedFrom);
		model.addAttribute("messageResponses_applicationsClosedDueToAllPositionsFilled",
				messageResponses_applicationsClosedDueToAllPositionsFilled);
		model.addAttribute("response", response);	
		model.addAttribute("user", employee);	
		model.addAttribute("monthSpan_employmentSummaryCalendar", monthSpan_employmentSummaryCalendar);
		model.addAttribute("calendarDays_employmentSummary", calendarDays_employmentSummary);
		
		
	
		// Ideally this would be updated every time a page is loaded.
		// Since a job becomes one-that-needs-a-rating only after the passage of
		// time,
		// as opposed to a particular event (i.e. a page load), this is the best place to put it for now
		// because
		// I'm assuming this page loads most often.
		List<Job> jobs_needRating = jobService.getJobs_needRating_byEmployee(sessionUser.getUserId());		
		session.setAttribute("jobs_needRating", jobs_needRating);
		model.addAttribute("isViewingOnesSelf", true);
		
		
//		setModel_getRatings_byUser(model, sessionUser.getUserId());		
		model.addAttribute("isViewingOnesSelf", true);
	}

	public void initializeApplicationProgressStatus(ApplicationProgressStatus applicationProgressStatus,
			int jobId) {
		
		// Refactor: this is an awkward place for this.
		// Should this be in a ApplicationProgressStatus constructor???
		// Consolidate the two ApplicationProgressStatus sub classes
		
		Job job = jobService.getJob(jobId);
		List<WorkDay> workDays = workDayService.getWorkDays(jobId);
		applicationProgressStatus.setJob(job);
		applicationProgressStatus.setCountJobWorkDays(workDays.size());
		
	}

	public ProfileInfoDto getProfileInfoDto(JobSearchUser user) {
		ProfileInfoDto profileInfoDto = new ProfileInfoDto();
		profileInfoDto.setUser(user);
		profileInfoDto.setProfileRatingDto(ratingService.getProfileRatingDto(user));
		profileInfoDto.setCompletedJobsDtos(jobService.getCompletedJobDtos(user));
		
		profileInfoDto.setDoesUserHaveEnoughDataToCalculateRating(false);
		if(profileInfoDto.getCompletedJobsDtos().size() > 0 && 
				profileInfoDto.getProfileRatingDto().getOverallRating()!= null){
			profileInfoDto.setDoesUserHaveEnoughDataToCalculateRating(true);
		}
		
		return profileInfoDto;
	}


	public void setViewEmployerHomepageResponse(JobSearchUser employer, Model model, HttpSession session) {

		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		LocalDateTime now = LocalDateTime.now();
		ViewEmployerHomepageResponse response = new ViewEmployerHomepageResponse();		
		List<Job> jobs = jobService.getJobs_byEmployerAndStatuses(employer.getUserId(),
				Arrays.asList(Job.STATUS_FUTURE, Job.STATUS_PRESENT));
		
		// User profile info
		response.setProfileInfoDto(getProfileInfoDto(sessionUser));
		
		
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
						.filter(p -> p.getProposedToUserId() == sessionUser.getUserId())
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
						.count());
			
			// Other job details
//				jobDto.setDaysUntilStart(DateUtility.getTimeSpan(LocalDate.now(), LocalTime.now(),
//						job.getStartDate_local(), job.getStartTime_local(), DateUtility.TimeSpanUnit.Days));

			response.getEmployerHomepageJobs().add(employerHomepageJob);			
		}

		model.addAttribute("response", response);
		
		
		// Ideally this would be updated every time a page is loaded.
		// Since a job becomes one-that-needs-a-rating only after the passage of
		// time,
		// as opposed to a particular event (i.e. a page load), this is the best place to put it for now
		// because
		// I'm assuming this page loads most often.
		List<Job> jobs_needRating = jobService.getJobs_needRating_byEmployeer(sessionUser.getUserId());		
		session.setAttribute("jobs_needRating", jobs_needRating);
		model.addAttribute("isViewingOnesSelf", true);
		
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
				CalendarDayJobDto calendarDayJobDto = new CalendarDayJobDto();
				calendarDayJobDto.setJob(job);
				calendarDayJobDto.setWorkDay(workDayService.getWorkDay(job.getId(), dateString));
				calendarDay.getJobDtos().add(calendarDayJobDto);
			}			
			calendarDays.add(calendarDay);
			
			if (day.getDayOfWeek() == DayOfWeek.SATURDAY) saturdayCount += 1;
			day = day.plusDays(1);
		}		
		return calendarDays;
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

	public void setFindEmployeesResponse(Model model, FindEmployeesRequest request){

		if (GoogleClient.isValidAddress(request.getAddress()) && 
				verificationService.isPositiveNumber(request.getRadius())) {
			
			Coordinate coordinate = GoogleClient.getCoordinate(request.getAddress());
			request.setLat(coordinate.getLatitude());
			request.setLng(coordinate.getLongitude());
			
			FindEmployeesResponse response = new FindEmployeesResponse();
			response.setAddressSearched(request.getAddress());
			response.setRadiusSearched(request.getRadius());
					
			if(verificationService.isListPopulated(request.getDates())){			
				response.setCountDatesSearched(request.getDates().size());
			}
			
			response.setUsers(new ArrayList<>());
			List<JobSearchUser> users = this.getUsers_ByFindEmployeesRequest(request);
			for (JobSearchUser user : users) {
				
				FindEmployeeUser findEmployeeUser = new FindEmployeeUser();

				findEmployeeUser.setUser(user);
				findEmployeeUser.setOverallRating(ratingService.getRating(user.getUserId()));			
				findEmployeeUser.setCountJobsCompleted(
						jobService.getCount_JobsCompleted_ByUser(user.getUserId()));

				if(request.getDates() != null){
					findEmployeeUser.setCountDaysAvailable(
							jobService.getCount_availableDays_ByUserAndWorkDays(user.getUserId(),
									request.getDates()));	
				}
				
				response.getUsers().add(findEmployeeUser);
			}			
			model.addAttribute("response", response);		
		}
	}

	public List<JobSearchUser> getUsers_ByFindEmployeesRequest(FindEmployeesRequest request) {
		return repository.getUsers_byFindEmployeesRequest(request);
	}





	public void setGetProfileCalendarResponse(Model model, HttpSession session) {

		JobSearchUser sessionUser = SessionContext.getUser(session);
		List<Application> applications = applicationService
				.getApplications_byUser_openOrAccepted(sessionUser.getUserId());

		GetProfileCalendarResponse response = new GetProfileCalendarResponse();
		for (Application application : applications) {
			
			Proposal currentProposal = proposalService.getCurrentProposal(application.getApplicationId());
			
			CalendarApplication calendarApplication = new CalendarApplication();
			calendarApplication.setApplication(application);
			calendarApplication.setDates(proposalService.getProposedDates(currentProposal.getProposalId()));
			response.getCalendarApplications().add(calendarApplication);
		}
		model.addAttribute("response", response);
	}


	// Update to use the new models
	// ****************************************************************************	
	// ****************************************************************************	
//	public List<JobSearchUser> getApplicants_whoAreAvailableButDidNotApplyForDate(int jobId, String dateString) {

//		EmployeeSearch employeeSearch = new EmployeeSearch();
//
//		JobDTO jobDto_findEmployees = new JobDTO();
//		jobDto_findEmployees.setJob(jobService.getJob(jobId));
//		jobDto_findEmployees.getWorkDays().add((new WorkDay(dateString)));
//	
//		return getUsers_ByFindEmployeesRequest(employeeSearch);		

//	}

//	public List<JobSearchUser> getUsers_whoAreAvailableButHaveNotApplied(int jobId, String dateString) {


//		EmployeeSearch employeeSearch = new EmployeeSearch();
//
//		JobDTO jobDto_findEmployees = new JobDTO();
//		jobDto_findEmployees.setJob(jobService.getJob(jobId));
//		jobDto_findEmployees.getWorkDays().add((new WorkDay(dateString)));
//
//		return getUsers_ByFindEmployeesRequest(employeeSearch);
		
//	}

// ****************************************************************************
	// ****************************************************************************		



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




	public List<JobSearchUser> getEmployees_byJob_completedWork(int jobId) {
		return repository.getEmployees_byJob_completedWork(jobId);
	}



	public List<JobSearchUser> getUsersWithOpenApplicationToJob(int jobId) {
	
		return repository.getApplicants_byJob_openApplicantions(jobId);
	}

	public void setModel_makeOffer_initialize(Model model, int userId, HttpSession session) {
	
		JobSearchUser sessionUser = SessionContext.getUser(session);
		if(sessionUser == null){
			model.addAttribute("notLoggedIn", true);
		}else{
			List<Job> openJobs = jobService.getJobs_byEmployerAndStatuses(
					sessionUser.getUserId(),
					Arrays.asList(Job.STATUS_PRESENT, Job.STATUS_FUTURE));
			
			model.addAttribute("openJobs", openJobs);
			model.addAttribute("user_makeOfferTo", getUser(userId));		
		}
		

	}

	public String getAvailabliltyStatusMessage_forUserAndJob(int userId, int jobId) {
		
		Job job = jobService.getJob(jobId);
		List<String> workDays = workDayService.getWorkDayDateStrings(jobId);		
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

	public void setInitMakeOfferResponse(Model model, int userId_makeOfferTo, int jobId, HttpSession session) {

		if(!verificationService.didUserApplyForJob(jobId, userId_makeOfferTo) &&
				verificationService.didSessionUserPostJob(session, jobId)){
			
//			JobSearchUser sessionUser = SessionContext.getUser(session);			
//			Proposal proposal = getProposal(proposalId);			
			Job job = jobService.getJob(jobId);			
			List<WorkDay> workDays = workDayService.getWorkDays(job.getId()) ;
			
			CurrentProposalResponse response = new CurrentProposalResponse();
			response.setJob(job);
			response.setProposeToUserId(userId_makeOfferTo);
//			response.setCurrentProposal(proposal);
//			response.setJobWorkDays(workDays);
			response.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
			response.setMonthSpan_allWorkDays(DateUtility.getMonthSpan(workDays));
//			response.setTime_untilEmployerApprovalExpires(
//					getTime_untilEmployerApprovalExpires(proposal.getExpirationDate(), LocalDateTime.now()));
//			response.setTimeUntilStart(DateUtility.getTimeInBetween(
//					LocalDateTime.now(),
//					workDays.get(0).getStringDate(),
//					workDays.get(0).getStringStartTime()));
			
			model.addAttribute("response", response);
//			model.addAttribute("isEmployerMakingFirstOffer", false);
			

			model.addAttribute("context", "employer-make-initial-offer");						
			model.addAttribute("response", response);
			model.addAttribute("json_workDayDtos", JSON.stringify(workDayService.getWorkDayDtos(jobId)));
//			model.addAttribute("user", sessionUser);
//			model.addAttribute("isEmployerMakingFirstOffer", false);
			
		}
		
		
	}
	
	// Refactored
	// ******************************************************************************************
	// ******************************************************************************************	

	public void setViewHomepageResponse(Model model, HttpSession session) {
	
		JobSearchUser sessionUser = SessionContext.getUser(session);

		if (sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE) {
			setViewEmployeeHomepageResponse(sessionUser, model, session);
		} else
			setViewEmployerHomepageResponse(sessionUser, model, session);

		
	}

	public void updateHomeLocation(HttpSession session, String city, String state, String zip) {		
		Coordinate coordinate = GoogleClient.getCoordinate(
				GoogleClient.buildAddress(null, city, state, zip));				
		if (GoogleClient.isValidAddress(coordinate)) {			
			repository.updateHomeLocation(SessionContext.getUser(session).getUserId(), city,
					state, zip, coordinate);
		}		
		updateSessionUser(session);
	}

	public void updateMaxWorkRadius(HttpSession session, Integer maxWorkRadius) {

		if (verificationService.isPositiveNumberOrZero(maxWorkRadius)) {
			repository.updateMaxWorkRadius(
					SessionContext.getUser(session).getUserId(), maxWorkRadius);
		}
		updateSessionUser(session);		
	}

	public void updateAbout(HttpSession session, String about) {
		JobSearchUser sessionUser = SessionContext.getUser(session);
		// About
		if (about == null) {
			repository.updateAbout(sessionUser.getUserId(), about);
		} else if (sessionUser.getAbout() == null) {
			repository.updateAbout(sessionUser.getUserId(), about);
		} else if (!sessionUser.getAbout().matches(about)) {
			repository.updateAbout(sessionUser.getUserId(), about);
		}
		this.updateSessionUser(session);
	}

	public String buildAddress(JobSearchUser user) {
		if(user != null){
			return GoogleClient.buildAddress(null, user.getHomeCity(), user.getHomeState(), user.getHomeZipCode());
		}else return null;
	}


}
