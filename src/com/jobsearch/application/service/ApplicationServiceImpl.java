package com.jobsearch.application.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayProposal;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DistanceUtility;
import com.jobsearch.utilities.VerificationServiceImpl;


@Service
public class ApplicationServiceImpl {

	@Autowired
	ApplicationRepository repository;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;
	
	@Autowired
	VerificationServiceImpl verificationService;


	public List<ApplicationDTO> getApplicationDtos_ByJob_OpenApplications(int jobId) {
		
		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();

		// Query the application table
		List<Application> applications = repository.getApplicationDtos_ByJob_OpenApplications(jobId);

		for (Application application : applications) {
			
			ApplicationDTO applicationDto =  new ApplicationDTO();
			
			applicationDto.setApplication(application);
			
			applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));	
			
			int applicantId = applicationDto.getApplicantDto().getUser().getUserId(); 
					
			applicationDto.setWageProposals(this.getWageProposals(application.getApplicationId()));
			
//			applicationDto.setCurrentWageProposal(this.getCurrentWageProposal(application));
			applicationDto.setEmploymentProposalDto(this.getCurrentEmploymentProposal(application.getApplicationId()));
			
			
			applicationDto.setTime_untilEmployerApprovalExpires(
							this.getTime_untilEmployerApprovalExpires(
									application.getExpirationDate()));
			
			applicationDto.getApplicantDto().setRatingValue_overall(userService.getRating(applicantId));
			
			applicationDto.setQuestions(this.getQuestionsWithAnswersByJobAndUser(jobId, applicantId));
			
			applicationDto.setAnswerOptionIds_Selected(
						this.getAnswerOptionIds_Selected_ByApplicantAndJob(applicantId, jobId));
		
			applicationDto.setDateStrings_availableWorkDays(
								this.getProposedWorkDays(applicationDto.getEmploymentProposalDto().getEmploymentProposalId()));

			
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(jobId));
			
			// **********************************************************************
			// **********************************************************************
			// Determine whether the employees should specify the days they are **available** 
			// or **unavailable**.
			// I'm leaning towards: "assume the user is available unless otherwise noted".
			// (i.e. the user specifies their unavailability.
			// **********************************************************************
			// **********************************************************************
//			applicationDto.setDateStrings_unavailableWorkDays(jobService.getDateStrings_UnavailableWorkDays(applicantId,
//																applicationDto.getJobDto().getWorkDays()));
			
			jobService.setCalendarInitData(applicationDto.getJobDto(), applicationDto.getJobDto().getWorkDays());
			
			// *************************************************
			// With the advent of applicationDto.availableDays,
			// this can probably be eliminated in the near future.
			// Review this.
			// *************************************************			
			int count_employmentDays = jobService.getCount_employmentDays_byUserAndWorkDays(
														applicantId, applicationDto.getJobDto().getWorkDays());
			
			// **********************************
			// See lengthy note in applyForJob()
			// **********************************
			applicationDto.getApplicantDto().setCount_availableDays_perFindEmployeesSearch(
													applicationDto.getJobDto().getWorkDays().size()
														- count_employmentDays);
			

			applicationDtos.add(applicationDto);
		}

		return applicationDtos;
	}


	public WageProposal getCurrentWageProposal(Application application) {
		return repository.getCurrentWageProposal(application.getApplicationId());
	}
	
	public EmploymentProposalDTO getCurrentEmploymentProposal(Integer applicationId) {
		return repository.getCurrentEmploymentProposal(applicationId);
	}


	public List<String> getProposedWorkDays(int employmentProposalId) {
		
		return repository.getProposedWorkDays(employmentProposalId);
	}

	private String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate) {
		
		// This will subtract the current time from the expiration date.
		
		
		if(expirationDate != null){
			LocalDateTime now = LocalDateTime.now();
			
			// If expired
			if(ChronoUnit.MINUTES.between(now, expirationDate) < 0){
				return "-1";
			}
			else{
				long days = ChronoUnit.DAYS.between(now, expirationDate);
				long hours =  ChronoUnit.HOURS.between(now, expirationDate) - days * 24 ;
				long minutes =  ChronoUnit.MINUTES.between(now, expirationDate) - ( days * 24 * 60 ) - ( hours * 60 );
				String result = "";
				
				if(days == 1) result += days + " day, ";
				else if(days > 1) result += days + " days, ";
				
				result += " " + hours + ":";
				
				if(minutes < 10) result += "0";
				
				result += minutes + " hrs";

				// For example, the result will be in the form "2 days 15:45 hrs"
				return result;				
			}
	
		}
		return null;
	}


	private List<Integer> getAnswerOptionIds_Selected_ByApplicantAndJob(int userId, int jobId) {
		return repository.getAnswerOptionIds_Selected_ByApplicantAndJob(userId, jobId);
	}


	public float getCurrentWageProposedTo(int applicationId, int proposedToUserId) {
		repository.getCurrentWageProposedTo(applicationId, proposedToUserId);
		return 0;
	}

//	public float getCurrentWageProposedBy(int applicationId, int proposedByUserId) {
//		return repository.getCurrentWageProposedBy(applicationId, proposedByUserId);
//	}

	public List<WageProposal> getWageProposals(int applicationId) {
		return repository.getWageProposals(applicationId);
	}

	public Application getApplication(int jobId, int userId) {
		return repository.getApplication(jobId, userId);
	}

	public void updateApplicationStatus(int applicationId, int status) {

		
		repository.updateApplicationStatus(applicationId, status);
	}

	public Application getApplication(int applicationId) {
		return repository.getApplication(applicationId);
	}

	public void applyForJob(ApplicationDTO applicationDto, HttpSession session) {
		
		Job appliedToJob = jobService.getJob(applicationDto.getJobId());
		JobSearchUser sessionUser = SessionContext.getUser(session);

		// ****************************************
		// ****************************************
		// Verify at least one application work day is within the job work days
		// ****************************************
		// ****************************************
		
		if(verificationService.isPositiveNumber(applicationDto.getEmploymentProposalDto().getAmount())){
		

			// Set the employment proposal
			applicationDto.getEmploymentProposalDto().setProposedByUserId(sessionUser.getUserId());	
			applicationDto.getEmploymentProposalDto().setProposedToUserId(appliedToJob.getUserId());
			applicationDto.getEmploymentProposalDto().setStatus(WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED);
			
			// Set the application dto
			applicationDto.getApplication().setStatus(Application.STATUS_SUBMITTED);
			applicationDto.setApplicantId(sessionUser.getUserId());
			
			insertApplication(applicationDto);
			
		}

		
		
		// *********************************************
		// Review this.
		// Currently the applicant's availability is not updated at the time of application.
		// An applicant's availability count will simply be:
		// job-work-day count minus employment-work-day count amongst the job's work days.
		// So even if the applicant has not explicitly claimed they are available on a particular day,
		// as long as they are not employed on this day, then it will
		// be assumed the applicant is available on this day
		// *********************************************
		// Reason for updating the applicant's availability:
		// It is assumed that if the user applies for a job, then they are available on those date(s).
		// If the user has not explicitly set this job's date(s) as "available" 
		// on their availability calendar, then this will ensure their calendar is updated.
		// This is implemented because if a job allows partial availability, and 
		// an applicant has not explicitly stated their availability, then the count of day's
		// the applicant is available cannot be returned to the employer.
		
//		userService.updateAvailability(session, appliedToJob.getWorkDays());
		// *********************************************

	}
	

	public void insertApplication(ApplicationDTO applicationDto) {
		
		// ****************************************************
		// Need to validate answers
		// ****************************************************
		
		
		repository.insertApplication(applicationDto);

	}

	public List<Answer> getAnswers(int questionId, int userId) {
		return repository.getAnswers(questionId, userId);
	}

	public List<Answer> getAnswersByJobAndUser(int jobId, int userId) {
		return repository.getAnswersByJobAndUser(jobId, userId);
	}

	public List<Question> getQuestions(int jobId) {
		// This will not set an answer

		List<Question> questions = repository.getQuestions(jobId);
		for (Question q : questions) {
			q.setAnswerOptions(this.getAnswerOptions(q.getQuestionId()));
		}
		return questions;
	}

	public List<Question> getQuestionsWithAnswersByJobAndUser(int jobId, int userId) {

		List<Question> questions = repository.getQuestions(jobId);
		
		for(Question question : questions){
			question.setAnswerOptions(this.getAnswerOptions(question.getQuestionId()));
			question.setAnswers(this.getAnswers(question.getQuestionId(), userId));
		}
		return questions;
	}



	//
	public List<AnswerOption> getAnswerOptions(int questionId) {

		return repository.getAnswerOptions(questionId);
	}

	public Answer getAnswer(int questionId, int userId) {
		return repository.getAnswer(questionId, userId);
	}

	public void addQuestion(Question question) {
		
		if(question.getFormatId() == Question.FORMAT_ID_YES_NO){
			
			question.setAnswerOptions(new ArrayList<AnswerOption>());
			
			AnswerOption answerOption = new AnswerOption();
			answerOption.setQuestionId(question.getQuestionId());
			answerOption.setText("Yes");			
			question.getAnswerOptions().add(answerOption);
			
			answerOption = new AnswerOption();
			answerOption.setQuestionId(question.getQuestionId());
			answerOption.setText("No");			
			question.getAnswerOptions().add(answerOption);
		}
		
		repository.addQuestion(question);
	}


	public WageProposal getWageProposal(int wageProposalId) {
		return repository.getWageProposal(wageProposalId);
	}
	
	





	public void respondToEmploymentProposal(EmploymentProposalDTO employmentProposalDto,
											HttpSession session,
											String context) {
	   
		JobSearchUser sessionUser = SessionContext.getUser(session);
		
		// Get the proposal being **RESPONDED TO**.
		// Per the application id, get the most recent employment proposal in the database.
		EmploymentProposalDTO proposalBeingRespondedTo = getCurrentEmploymentProposal(
							employmentProposalDto.getApplicationId());

		// For all responses:
		// 1) Is the proposal being responded to currently presented TO the session user
		// 2) AND is this proposal not yet declined? 
		if(proposalBeingRespondedTo.getProposedToUserId() == SessionContext.getUser(session).getUserId() &&
				proposalBeingRespondedTo.getStatus() != EmploymentProposalDTO.STATUS_DECLINED){
			
			switch (context) {
				case "decline":
					this.updateApplicationStatus(proposalBeingRespondedTo.getApplicationId(),
							EmploymentProposalDTO.STATUS_DECLINED);
	
						Application application = this.getApplication(proposalBeingRespondedTo.getApplicationId());
						this.updateApplicationStatus(application.getApplicationId(),
							Application.STATUS_DECLINED);				
					break;
	
				case "counter":
					
					// **Employers** must provide an expiration time for all counter offers they make
					if( ( sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER &&
							isValidExpirationTime(employmentProposalDto) ) || 
							sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE ){
						
						// Offers that are pending the applicant's approval
						// (i.e. the employer has accepted the applicant's proposal)
						// cannot be countered.
						if(proposalBeingRespondedTo.getStatus() != EmploymentProposalDTO.STATUS_PENDING_APPLICANT_APPROVAL){
					    	
							this.updateWageProposalStatus(proposalBeingRespondedTo.getEmploymentProposalId(),
									WageProposal.STATUS_COUNTERED);
	
							EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
							
							newProposal.setProposedByUserId(proposalBeingRespondedTo.getProposedToUserId());
							newProposal.setProposedToUserId(proposalBeingRespondedTo.getProposedByUserId());
							newProposal.setStatus(WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED);
							newProposal.setApplicationId(proposalBeingRespondedTo.getApplicationId());
							
							// ***********************************************************************************
							// This needs to be verified
							if(verificationService.isListPopulated(employmentProposalDto.getDateStrings_proposedDates())){
								newProposal.setDateStrings_proposedDates(employmentProposalDto.getDateStrings_proposedDates());
							}
							else{
								newProposal.setDateStrings_proposedDates(proposalBeingRespondedTo.getDateStrings_proposedDates());
							}
							if(verificationService.isPositiveNumber(employmentProposalDto.getAmount())){
								newProposal.setAmount(employmentProposalDto.getAmount());
							}else{
								newProposal.setAmount(proposalBeingRespondedTo.getAmount());
							}
							// ***********************************************************************************
							
							this.insertEmploymentProposal(newProposal);			
						}
					}					
					break;
				
				case "accept-by-employer":					
					
					// ****************************************************
					// ****************************************************
					// Review whether is makes most sense to place the expiration and accepted timestamps
					// on the application or the proposal.
					// As things have evolved, I think it makes more sense to place them on the proposal.
					// Review this.
					// ****************************************************
					// ****************************************************					
					
					if(proposalBeingRespondedTo.getStatus() != EmploymentProposalDTO.STATUS_PENDING_APPLICANT_APPROVAL ){

						if(isValidExpirationTime(employmentProposalDto)){

							LocalDateTime employerAcceptedDate = LocalDateTime.now();
							LocalDateTime expirationDate = getExpirationDate(employerAcceptedDate,
																employmentProposalDto);
						
							EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
							
							newProposal.setAmount(proposalBeingRespondedTo.getAmount());
							newProposal.setProposedByUserId(proposalBeingRespondedTo.getProposedToUserId());
							newProposal.setProposedToUserId(proposalBeingRespondedTo.getProposedByUserId());
							newProposal.setApplicationId(proposalBeingRespondedTo.getApplicationId());
							newProposal.setStatus(EmploymentProposalDTO.STATUS_PENDING_APPLICANT_APPROVAL);				
							newProposal.setDateStrings_proposedDates(proposalBeingRespondedTo.getDateStrings_proposedDates());
							
							this.insertEmploymentProposal(newProposal);
							
							repository.updateApplication_PendingApplicantApproval(proposalBeingRespondedTo.getApplicationId(),
																				employerAcceptedDate, expirationDate);				
						}				
					}					
					break;
				
				case "approve-by-applicant":
					
					// ***************************************************
					// ***************************************************
					// Should the status of the applicant's **proposal** be set to "Accepted"???
					// Currently it is not being updated and remaining at -1.
					// Review this.
					// ***************************************************
					// ***************************************************
				
					if(proposalBeingRespondedTo.getStatus() == EmploymentProposalDTO.STATUS_PENDING_APPLICANT_APPROVAL &&
						!isEmployerAcceptanceExpired(employmentProposalDto.getApplicationId()) ){
		
						this.updateWageProposalStatus(proposalBeingRespondedTo.getEmploymentProposalId(),
														EmploymentProposalDTO.STATUS_APPROVED_BY_APPLICANT);
		
						this.updateApplicationStatus(proposalBeingRespondedTo.getApplicationId(), Application.STATUS_ACCEPTED);
						
						
						userService.insertEmployment(proposalBeingRespondedTo.getProposedToUserId(),
										jobService.getJob_ByApplicationId(proposalBeingRespondedTo.getApplicationId()).getId());
						
						// If necessary, cancel the applicant's conflicting applications
						List<ApplicationDTO> applicationDtos_conflicting = this.getApplicationDtos_Conflicting(
													SessionContext.getUser(session).getUserId(),
													employmentProposalDto.getApplicationId(),
													jobService.getWorkDays(this.getApplication(employmentProposalDto.getApplicationId()).getJobId()));
						
						for(ApplicationDTO applicationDto : applicationDtos_conflicting){
						
							this.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
															Application.STATUS_CANCELLED_DUE_TO_TIME_CONFLICT);
						}
					}
			}
		}
	}
	
	

	private boolean isValidExpirationTime(EmploymentProposalDTO employmentProposalDto) {
		
		if( ( employmentProposalDto.getDays_offerExpires() != null ||
				employmentProposalDto.getHours_offerExpires() != null ||
					employmentProposalDto.getMinutes_offerExpires() != null) && 
				( employmentProposalDto.getDays_offerExpires() > 0 ||
					employmentProposalDto.getHours_offerExpires() > 0 ||
						employmentProposalDto.getMinutes_offerExpires() > 0) )
			
			return true;
		else return false;
	}




	public List<ApplicationDTO> getApplicationDtos_ByUserAndApplicationStatus_OpenJobs(int userId, List<Integer> applicationStatuses){
		
		List<Application> applications = this.getApplications_ByUserAndStatuses_OpenJobs(userId, applicationStatuses);		

		List<ApplicationDTO> applicationDtos = getApplicationDtos_ByApplications(applications, userId);
		
		return applicationDtos;
	}


	private List<ApplicationDTO> getApplicationDtos_ByApplications(List<Application> applications, int userId) {
		
		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
		
		for(Application application : applications){
			
			ApplicationDTO applicationDto = new ApplicationDTO();
			
			applicationDto.setApplication(application);
			
			// Wage proposal
			applicationDto.setEmploymentProposalDto(getCurrentEmploymentProposal(application.getApplicationId()));
			applicationDto.setCurrentWageProposal(this.getCurrentWageProposal(application));
			applicationDto.setWageProposals(this.getWageProposals(application.getApplicationId()));
			
			// Job dto
			applicationDto.getJobDto().setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(applicationDto.getJobDto().getJob().getId()));
			applicationDto.getJobDto().setMilliseconds_startDate(
										applicationDto.getJobDto().getJob().getStartDate_local().toEpochDay());
			applicationDto.getJobDto().setMilliseconds_endDate(
					applicationDto.getJobDto().getJob().getEndDate_local().toEpochDay());
			
			applicationDto.getJobDto().setDistance(DistanceUtility.getDistance(
											userService.getUser(userId), applicationDto.getJobDto().getJob()));
			
			
			// Miscellaneous
			applicationDto.setTime_untilEmployerApprovalExpires(
								this.getTime_untilEmployerApprovalExpires(application.getExpirationDate()));
			
			if(applicationDto.getCurrentWageProposal() != null){
				applicationDto.setDateStrings_availableWorkDays(this.getProposedWorkDays(
										applicationDto.getCurrentWageProposal().getId()));
			}
			
			
			// **************************************************
			// **************************************************
			// See note in getApplicationDtos_ByJob_OpenApplications();
			// **************************************************
			// **************************************************
//			applicationDto.setDateStrings_unavailableWorkDays(
//							jobService.getDateStrings_UnavailableWorkDays(userId,
//											applicationDto.getJobDto().getWorkDays()));
			
			applicationDto.setApplicationDtos_conflicting(
								this.getApplicationDtos_Conflicting(userId,
											application.getApplicationId(),
											applicationDto.getJobDto().getWorkDays()));
			
			jobService.setCalendarInitData(applicationDto.getJobDto(), applicationDto.getJobDto().getWorkDays());
			applicationDtos.add(applicationDto);
		}
		
		return applicationDtos;
	}

	private List<ApplicationDTO> getApplicationDtos_Conflicting(int userId, int reference_applicationId,
													List<WorkDay> workDays) {
		
		List<ApplicationDTO> applicationDtos_conflicting = new ArrayList<ApplicationDTO>();
		
		if(workDays != null && workDays.size() > 0){
			
			List<Application> applications_conflicting = repository.getApplications_WithAtLeastOneWorkDay(userId, reference_applicationId, workDays);
			
			for(Application application : applications_conflicting){
				ApplicationDTO applicationDto = new ApplicationDTO();
				
				applicationDto.setApplication(application);
				applicationDto.getJobDto().setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
			
				applicationDtos_conflicting.add(applicationDto);
			}
						
		}
		
		return applicationDtos_conflicting;
	}


	public List<Application> getApplications_ByUserAndStatuses_OpenJobs(int userId, List<Integer> statuses) {

		if (statuses.size() > 0) {
			return repository.getApplications_ByUserAndStatuses_OpenJobs(userId, statuses);
		} else {
			return null;
		}

	}


	public List<Application> getApplicationsByUser(int userId) {
		return repository.getApplicationsByUser(userId);
	}

	

	public EmploymentProposalDTO getEmploymentProposalDto(int employmentProposalId) {

		return repository.getEmploymentProposalDto(employmentProposalId);
		
	}



	
	private boolean isEmployerAcceptanceExpired(int applicationId) {
		
		Application application = this.getApplication(applicationId);
		
		
		if(ChronoUnit.MINUTES.between(LocalDateTime.now(), application.getExpirationDate()) < 0) return true;
		else return false;
	}


	public LocalDateTime getExpirationDate(LocalDateTime start, EmploymentProposalDTO employmentProposalDto){
		
		
		LocalDateTime expirationDate = start;
		
		if(employmentProposalDto.getDays_offerExpires() != null)
			expirationDate = expirationDate.plusDays(employmentProposalDto.getDays_offerExpires() );
		if(employmentProposalDto.getHours_offerExpires()  != null)
			expirationDate = expirationDate.plusHours(employmentProposalDto.getHours_offerExpires() );
		if(employmentProposalDto.getMinutes_offerExpires()  != null)
			expirationDate = expirationDate.plusMinutes(employmentProposalDto.getMinutes_offerExpires() );
		
		return expirationDate;
		
	}



	public void updateWageProposalStatus(int wageProposalId, int status) {
		repository.updateWageProposalStatus(wageProposalId, status);
	}



	public JobSearchUser getOtherUserInvolvedInWageProposal(WageProposal failedWageProposal, int dontReturnThisUserId) {

		int otherUserId;

		// If the proposed-by-user is not the prohibited user,
		if (failedWageProposal.getProposedByUserId() != dontReturnThisUserId) {
			otherUserId = failedWageProposal.getProposedByUserId();
		} else {
			otherUserId = failedWageProposal.getProposedToUserId();
		}
		return userService.getUser(otherUserId);
	}

	public double getWage(int userId, int jobId) {

		Application application = repository.getApplication(jobId, userId);

		return repository.getWage(application.getApplicationId());
	}


	public void addAnswer(Answer answer) {
		repository.addAnswer(answer);
	}

	public AnswerOption getAnswerOption(int answerOptionId) {
		return repository.getAnswerOption(answerOptionId);
	}

	public int getFailedApplicationCount(List<ApplicationDTO> applicationDtos) {		
		
		if(applicationDtos != null){
			return (int) applicationDtos.stream().filter(a -> a.getApplication().getStatus() == 1).count();
		}else{
			return 0;	
		}
		
	}

	public int getOpenApplicationCount(List<ApplicationDTO> applicationDtos) {
		
		if(applicationDtos != null){
			return (int) applicationDtos.stream().filter(a -> a.getApplication().getStatus() == 0 || 
																a.getApplication().getStatus() == 2).count();
		}else{
			return 0;	
		}
	}

	public void updateHasBeenViewed(Job job, int value) {
		repository.updateHasBeenViewed(job.getId(), value);
		
	}

	public int getCountWageProposal_Sent(Integer jobId, int userId) {

		return repository.getCountWageProposal_Sent(jobId, userId);
	}

	public int getCountWageProposal_Received(Integer jobId, int userId) {
	
		return repository.getCountWageProposal_Received(jobId, userId);
	}
	
	public int getCountWageProposal_Received_New(Integer jobId, int userId) {
		return repository.getCountWageProposal_Received_New(jobId, userId);
	}

	public void updateWageProposalsStatus_ToViewedButNoActionTaken(Integer jobId) {

		repository.updateWageProposalsStatus_ToViewedButNoActionTaken(jobId);
		
	}

	public List<Question> getDistinctQuestions_byEmployer(int userId) {
		
		return repository.getDistinctQuestions_byEmployer(userId);
	}

	public Question getQuestion(int questionId) {
		
		return repository.getQuestion(questionId);
	}

	public boolean wasUserEmployedForJob(int userId, int jobId) {
			
		int countEmploymentRecord = repository.getCount_Employment_ByUserAndJob(userId, jobId);
		
		if(countEmploymentRecord  == 1) return true;
		else return false;
	
	}

	public void initiateContact_byEmployer(ApplicationDTO applicationDto, HttpSession session) {

		// Verify:
		// 1) That the session user did indeed post this job.
		// 2) That the prospective employee has not already applied to this job.
		if(verificationService.didSessionUserPostJob(session, applicationDto.getJobId()) && 
				!verificationService.didUserApplyForJob(applicationDto.getJobId(), applicationDto.getApplicantId())){
			
			// Set the application
			applicationDto.getApplication().setStatus(Application.STATUS_PROPOSED_BY_EMPLOYER);
			
			LocalDateTime employerAcceptedDate = LocalDateTime.now();
			LocalDateTime expirationDate = getExpirationDate(employerAcceptedDate,
													applicationDto.getEmploymentProposalDto());
			
			applicationDto.getApplication().setEmployerAcceptedDate(employerAcceptedDate);
			applicationDto.getApplication().setExpirationDate(expirationDate);
			
			
			// Set the wage proposal
			applicationDto.getEmploymentProposalDto().setProposedToUserId(applicationDto.getApplicantId());
			applicationDto.getEmploymentProposalDto().setProposedByUserId(SessionContext.getUser(session).getUserId());
			applicationDto.getEmploymentProposalDto().setStatus(WageProposal.STATUS_PENDING_APPLICANT_APPROVAL);

			this.insertApplication(applicationDto);	
						
		}

	}

	public Integer getApplicationStatus(int jobId, int userId, HttpSession session) {
		
		if(verificationService.didSessionUserPostJob(session, jobId)){
			Application application = this.getApplication(jobId, userId);
			if(application == null) return Application.STATUS_DOES_NOT_EXIST;
			return application.getStatus();
		}
		else return null;
		
	}

	public void insertEmploymentProsalWorkDays(EmploymentProposalDTO employmentProposalDTO) {
		
		if(verificationService.isListPopulated(employmentProposalDTO.getDateStrings_proposedDates())){
			int jobId = jobService.getJob_ByApplicationId(employmentProposalDTO.getApplicationId()).getId();
			
			for(String dateString : employmentProposalDTO.getDateStrings_proposedDates()){
				
				int dateId = jobService.getDateId(dateString);				
				int workDayId = jobService.getWorkDayId(jobId, dateId);
				repository.insertEmploymentProsalWorkDay(employmentProposalDTO.getEmploymentProposalId(),
															workDayId);	
			}			
			
		}
		
	}

	public void insertEmploymentProposal(EmploymentProposalDTO employmentProposalDto) {
	
		// *********************************************************
		// *********************************************************
		// Verify the work day proposal is valid. i.e. verify at least one of the
		// proposed date strings is a work day for the particular job.
		// *********************************************************
		// *********************************************************
		
		
		repository.insertEmploymentProposal(employmentProposalDto);
		
	}

	public List<ApplicationInvite> getApplicationInvites(int userId) {
		
		return repository.getApplicationInvites(userId);
	}

	public void insertApplicationInvite(ApplicationInvite applicationInvite, HttpSession session) {
		
		// **********************************************************
		// **********************************************************
		// Re-asses the ApplicationInvite Class.
		// If invites-to-apply are only going to be differentiated from actual-appications
		// by a status, then do away with this class
		// Revisit this.
		// **********************************************************
		// **********************************************************		
		
		
		// Verify:
		// 1) The session user actually posted this job.
		if(verificationService.didSessionUserPostJob(session, applicationInvite.getJobId())){

		
			ApplicationDTO applicationDto = new ApplicationDTO();
			applicationDto.getApplication().setStatus(Application.STATUS_PROPOSED_BY_EMPLOYER);
			applicationDto.setJobId(applicationInvite.getJobId());
			applicationDto.setApplicantId(applicationInvite.getUserId());
			
			this.insertApplication(applicationDto);
			
	//		applicationInvite.setStatus(ApplicationInvite.STATUS_NOT_YET_VIEWED);
	//		repository.insertApplicationInvite(applicationInvite);
		
		}
	}


	public int getCountApplications_new(Integer jobId) {
		
		return repository.getCountApplications_new(jobId);
	}


	public int getCountApplications_received(Integer jobId) {
		
		return repository.getCountApplications_received(jobId);
	}


	public int getCountApplications_declined(Integer jobId) {
		
		return repository.getCountApplications_declined(jobId);
	}


	public int getCountEmployees_hired(Integer jobId) {
		
		return repository.getCountEmployees_hired(jobId);
	}

	public List<Integer> getApplicationStatuses_openOrAccepted(){	
		return Arrays.asList(Application.STATUS_PROPOSED_BY_EMPLOYER,
				Application.STATUS_SUBMITTED,
				Application.STATUS_CONSIDERED,
				Application.STATUS_ACCEPTED,
				Application.STATUS_WAITING_FOR_APPLICANT_APPROVAL);
	}


}
