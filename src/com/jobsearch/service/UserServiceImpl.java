
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.jobsearch.request.CreateUserRequest;
import com.jobsearch.request.FindEmployeesRequest;
import com.jobsearch.request.LoginRequest;
import com.jobsearch.responses.GetProfileCalendarResponse;
import com.jobsearch.responses.InitMakeOfferResponse;
import com.jobsearch.responses.LoginResponse;
import com.jobsearch.responses.GetProfileCalendarResponse.CalendarApplication;
import com.jobsearch.responses.MessageResponse;
import com.jobsearch.responses.UserApplicationStatusResponse;
import com.jobsearch.responses.ViewEmployeeHomepageResponse;
import com.jobsearch.responses.ViewEmployeeHomepageResponse.ApplicationProgressStatus;
import com.jobsearch.responses.ViewEmployerHomepageResponse;
import com.jobsearch.responses.CurrentProposalResponse;
import com.jobsearch.responses.FindEmployeesResponse;
import com.jobsearch.responses.GetJobResponse;
import com.jobsearch.responses.FindEmployeesResponse.FindEmployeeUser;
import com.jobsearch.responses.ViewEmployerHomepageResponse.EmployerHomepageJob;
import com.jobsearch.responses.user.CreateUserResponse;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.StringUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class UserServiceImpl extends BaseService {

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

	public CreateUserResponse createUser(CreateUserRequest request) {
		CreateUserResponse response = new CreateUserResponse();
		
		// validate
		boolean valid = true;
		if (StringUtility.isBlank(request.getFirstName())){
			valid = false;
		}else if (StringUtility.isBlank(request.getLastName())){
			valid = false;
		}else if (StringUtility.isBlank(request.getEmailAddress())){
			valid = false;
		}else if (StringUtility.isBlank(request.getConfirmEmailAddress())){
			valid = false;
		}else if (StringUtility.isBlank(request.getPassword())){
			valid = false;
		}else if (StringUtility.isBlank(request.getConfirmPassword())){
			valid = false;
		}else if (!request.getPassword().matches(request.getConfirmPassword())){
			valid = false;
		}else if (!request.getEmailAddress().matches(request.getConfirmEmailAddress())){
			valid = false;
		}else if (request.getProfileId() == null || (request.getProfileId() != Profile.PROFILE_ID_EMPLOYEE 
				&& request.getProfileId() != Profile.PROFILE_ID_EMPLOYER)){
			valid = false;
		}		
		if (getUserByEmail(request.getEmailAddress()) != null){
			valid = false;
			response.setEmailInUse(true);
		}
		if (!mailer.isValidEmailAddress(request.getEmailAddress())){
			valid = false;
			response.setInvalidEmail(true);
		}

		if (valid){
			request.setPassword(encryptPassword(request.getPassword()));
			response = repository.createUser(request); 
			if (response.getUserId() != null){
				// **********************************************************
				// TODO: We should send a verification token, not the user's id
				// **********************************************************
				String content = "<h3>Welcome to Labor Vault " + request.getFirstName() + "!<h3>";
				content += "<div>please click the link to verify your email " + hostUrl + 
						"/JobSearch/email/validate?userId="	+ response.getUserId() + "</div>";
								
				mailer.sendMail(request.getEmailAddress(), "email verification", content);
			}
		}
		
		return response;
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
			
			if(applicationService.includeApplication(application, currentProposal, sessionUser)){
			
				ApplicationProgressStatus applicationProgressStatus = new ApplicationProgressStatus();
				initializeApplicationProgressStatus(applicationProgressStatus, application.getJobId());
				applicationProgressStatus.setApplication(application);

				applicationProgressStatus.setCurrentProposal(currentProposal);
				applicationProgressStatus.setIsCurrentProposalExpired(
						DateUtility.isDateExpired(currentProposal.getExpirationDate(), now));								
				applicationProgressStatus.setIsProposedToSessionUser(
						proposalService.isProposedToUser(currentProposal, sessionUser));	
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
				applicationProgressStatus.setCurrentProposalLabel(proposalService.getProposalLabel(currentProposal, sessionUser));
				
				
				applicationProgressStatus.setPreviousProposal(previousProposal);		
				applicationProgressStatus.setPreviousProposalLabel(proposalService.getProposalLabel(previousProposal, sessionUser));
				
				applicationProgressStatus.setMessages(applicationService.getMessages(employee,
						application,
						previousProposal,
						currentProposal));
				
				applicationProgressStatus.setJob(job);
				
				response.getApplicationProgressStatuses().add(applicationProgressStatus);			
				applicationService.inspectNewness(application);	
				proposalService.inspectNewness(currentProposal, sessionUser);
			}
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
		List<Job> jobs = jobService.getJobs_openByEmployer(employer.getUserId());
		
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
						.filter(p -> p.getExpirationDate().isAfter(now))
						.count());

			employerHomepageJob.setCountWageProposals_received(
					currentProposals.stream()
						.filter(p -> p.getProposedToUserId() == sessionUser.getUserId())
						.filter(p -> p.getExpirationDate() == null || p.getExpirationDate().isAfter(now))
						.count());
			
			employerHomepageJob.setCountWageProposals_received_new(
					currentProposals.stream()
						.filter(p -> p.getProposedToUserId() == sessionUser.getUserId())
//						.filter(p -> p.getExpirationDate() != null)
						.filter(p -> p.getExpirationDate() == null ||  p.getExpirationDate().isAfter(now))
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
		// time, as opposed to a particular event (i.e. a page load), this is the best place to put it for now
		// because I'm assuming this page loads most often.
		List<Job> jobs_needRating = jobService.getJobs_withUnratedCompletedShifts_byEmployer(sessionUser.getUserId());		
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
	public LoginResponse login(LoginRequest request, HttpSession session) {

		// ***********************************************************************
		// ***********************************************************************
		// this is not working. the login needs to be initiated by ajax.
		// however i cannot get the auth to work without the spring form...
		// ***********************************************************************
		// ***********************************************************************
		

		// ************************************************
		// ************************************************
		// I wasn't sure how the authenitcaion was working so I turned it off.
		// If enchances security, then lets use it, otherwise I dont' see the point.
		// ************************************************
		// ************************************************
//		Authentication auth = new UsernamePasswordAuthenticationToken(
//				request.getEmailAddress(), request.getPassword());		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JobSearchUser user = this.getUserByEmail(auth.getName());
		
//		JobSearchUser user = getUserByEmail(request.getEmailAddress());
		
		LoginResponse response = new LoginResponse();
		if(user != null){
			String enc = encryptPassword(request.getPassword());;
			if(user.getPassword() == encryptPassword(request.getPassword())){
				response.setSuccess(true);
				SessionContext.setUser(session, user);
			}else{
				response.setSuccess(false);
			}			
		}else{
			response.setSuccess(false);
		}

		return response;		
	}
	
	@Deprecated
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
			return "/homepage_employee/Homepage_Employee";
		else
			return "/homepage_employer/Homepage_Employer";

	}

	public void setFindEmployeesResponse(Model model, FindEmployeesRequest request, Integer jobId){

		if (GoogleClient.isValidAddress(request.getAddress()) && 
				verificationService.isPositiveNumber(request.getRadius())) {
			
			Coordinate coordinate = GoogleClient.getCoordinate(request.getAddress());
			request.setLat(coordinate.getLatitude());
			request.setLng(coordinate.getLongitude());
			
			FindEmployeesResponse response = new FindEmployeesResponse();
			response.setAddressSearched(request.getAddress());
			response.setRadiusSearched(request.getRadius());
			response.setJobId(jobId);
					
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
			calendarApplication.setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
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

	public String getAvailabliltyStatusMessage_forUserAndJob(int userId, int jobId) {
		
		Job job = jobService.getJob(jobId);
		List<String> workDays = workDayService.getWorkDayDateStrings(jobId);		
		Integer availableDays = jobService.getCount_availableDays_ByUserAndWorkDays(userId, workDays);
		
		if(didUserApplyForJob(jobId, userId)) return "already-applied";
		else if(job.getIsPartialAvailabilityAllowed()){
			if(availableDays > 0) return "available";
			else return "unavailable";
		}else{
			if(availableDays == workDays.size()) return "available";
			else return "unavailable";
		}
				
	}

	public boolean didSessionUserPostJob(HttpSession session, int jobId) {
		Job job = jobService.getJob(jobId);		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		return sessionUser.getUserId() == job.getUserId() ? true : false;
	}


	public boolean didUserApplyForJob(int jobId, int userId) {		
		Application application = applicationService.getApplication(jobId, userId);		
		return application != null ? true : false; 
	}
	
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

	public boolean isEmployer(JobSearchUser user) {
		if(user.getProfileId() == Profile.PROFILE_ID_EMPLOYER){
			return true;
		}else{
			return false;
		}	
	}

	public boolean isEmployer(Integer userId) {
		JobSearchUser user = getUser(userId);
		return isEmployer(user);
	}

	public UserApplicationStatusResponse getUserApplicationStatusResponse(int userId, int jobId, HttpSession session) {		
		
		if(didSessionUserPostJob(session, jobId)){			
			UserApplicationStatusResponse response = new UserApplicationStatusResponse();				
			Application application = applicationService.getApplication(jobId, userId);
			response.setHasApplied(application != null ? true : false);		
			if(application != null){
				JobSearchUser user = getUser(userId);
				response.setMessage(user.getFirstName() + " has already applied for this job");
			}
			return response;
		}else{
			return null;
		}
	}

	public boolean didUserPostJob(int userId, int jobId) {
		Job job = jobService.getJob(jobId);
		if(job != null){
			return job.getUserId() == userId ? true : false;
		}else{
			return false;	
		}		
	}
	
	@Deprecated
	public boolean didUserPostJob(JobSearchUser user, int jobId) {
		return didUserPostJob(user.getUserId(), jobId);
	}
}
