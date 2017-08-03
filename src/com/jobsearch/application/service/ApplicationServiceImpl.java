package com.jobsearch.application.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.proposal.service.ProposalServiceImpl;
import com.jobsearch.responses.ConflictingApplicationsResponse;
import com.jobsearch.responses.ConflictingApplicationsResponse.ConflictingApplication;
import com.jobsearch.responses.MessageResponse;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
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
	@Autowired
	ProposalServiceImpl proposalService;
	@Autowired
	WorkDayServiceImpl workDayService;	

	public List<ApplicationDTO> getApplicationDtos_ByJob_OpenApplications(int jobId, HttpSession session) {

		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();

		// Query the application table
		List<Application> applications = repository.getApplications_ByJob_OpenApplications(jobId);

		for (Application application : applications) {

			ApplicationDTO applicationDto =  new ApplicationDTO();

			applicationDto.setApplication(application);
			
			// Applicant
			applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));
			int applicantId = applicationDto.getApplicantDto().getUser().getUserId();
			applicationDto.getApplicantDto().setRatingValue_overall(userService.getRating(applicantId));

			// Proposal
			applicationDto.setEmploymentProposalDto(this.getCurrentEmploymentProposal(application.getApplicationId()));
			applicationDto.getEmploymentProposalDto().setIsProposedToSessionUser(
					getIsProposedToSessionUser(session, applicationDto.getEmploymentProposalDto()));
			applicationDto.setPreviousProposal(getPreviousProposal(
					applicationDto.getEmploymentProposalDto().getEmploymentProposalId(), application.getApplicationId()));

			// Answers
			applicationDto.setQuestions(this.getQuestionsWithAnswersByJobAndUser(jobId, applicantId));

			applicationDto.setAnswerOptionIds_Selected(
						this.getAnswerOptionIds_Selected_ByApplicantAndJob(applicantId, jobId));

			// Job
			applicationDto.getJobDto().setJob(jobService.getJob(jobId));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(jobId));

			// Misc.
			applicationDto.setTime_untilEmployerApprovalExpires(
					this.getTime_untilEmployerApprovalExpires(
							application.getExpirationDate()));
			applicationDto.getJobDto().setDate_firstWorkDay(DateUtility.getMinimumDate(applicationDto.getJobDto().getWorkDays()).toString());
			applicationDto.getJobDto().setMonths_workDaysSpan(DateUtility.getMonthSpan(applicationDto.getJobDto().getWorkDays()));

			applicationDtos.add(applicationDto);
		}

		return applicationDtos;
	}


	public String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate) {
		
		LocalDateTime now = LocalDateTime.now();
		return getTime_untilEmployerApprovalExpires(expirationDate, now);

	}
	
	public String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate, LocalDateTime now) {


		if(expirationDate != null){
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
		}else return null;
	}

	public List<Integer> getAnswerOptionIds_Selected_ByApplicantAndJob(int userId, int jobId) {
		return repository.getAnswerOptionIds_Selected_ByApplicantAndJob(userId, jobId);
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
		// Verify at least one application work day is within the job work days.
		// Verify the user id not yet apply.
		// ****************************************
		// ****************************************

		if(verificationService.isPositiveNumber(applicationDto.getEmploymentProposalDto().getAmount())){

			// Set the employment proposal
			applicationDto.getEmploymentProposalDto().setProposedByUserId(sessionUser.getUserId());
			applicationDto.getEmploymentProposalDto().setProposedToUserId(appliedToJob.getUserId());
			applicationDto.getEmploymentProposalDto().setStatus(WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED);

//			if(!appliedToJob.getIsPartialAvailabilityAllowed()){
//				applicationDto.getEmploymentProposalDto().setDateStrings_proposedDates(
//										jobService.getWorkDayDateStrings(applicationDto.getJobId()));
//			}

			// Set the application dto
			applicationDto.getApplication().setStatus(Application.STATUS_SUBMITTED);
			applicationDto.setApplicantId(sessionUser.getUserId());

			insertApplication(applicationDto);

		}
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








	public void resolveApplicationConflicts_withinApplicationsForAJob(Job job,
			Proposal proposalDto_justAccepted) {
		
		if(job.getFlag_isNotAcceptingApplications() == 1){
			closeAllOpenApplications(job.getId());
		}
		else{
	
			List<WorkDay> workDays_toFindConflictsWith = jobService.getWorkDays_byProposalId(
											proposalDto_justAccepted.getProposalId());
			List<WorkDay> workDays_withAllPositionsFilled = new ArrayList<WorkDay>();
	
			for ( WorkDay workDay_toFindConflictWith : workDays_toFindConflictsWith ){
				int positions_filled = getCount_positionsFilledByDay(workDay_toFindConflictWith.getDateId(),
													job.getId());
	
				// This condition should only be an equal sign.
				// However, until we ensure the positions per day will never be exceeded (see note in respondToEmploymentProposal()),
				// a greater-than-or-equal-to will be used.
				if(positions_filled >= job.getPositionsPerDay())
					workDays_withAllPositionsFilled.add(workDay_toFindConflictWith);
			}
	
			if(workDays_withAllPositionsFilled.size() > 0){
				List<Application> applications_withConflicts = getOpenApplications_byJob_withAtLeastOneWorkDay(
						job.getId(), workDays_withAllPositionsFilled);
	
				for ( Application application : applications_withConflicts ){
	
					// Modify the proposal for the conflicting application
					Proposal currentProposal = proposalService.getCurrentProposal(
							application.getApplicationId());
					
					Proposal modifiedProposal = currentProposal;
	
					modifiedProposal.setProposedDates(jobService.removeConflictingWorkDays(
							currentProposal.getProposedDates(), workDays_withAllPositionsFilled));
	
					proposalService.updateProposalFlag(currentProposal,
							EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS,
							1);
	
//					updateWageProposalStatus(currentProposal.getEmploymentProposalId(),
//												EmploymentProposalDTO.STATUS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS);
	
					proposalService.insertProposal(modifiedProposal);

				}
			}
		}
	}


	public void closeAllOpenApplications(Integer jobId) {
		List<Application> applications_open = repository.getApplications_ByJob_OpenApplications(jobId);
		for(Application application_open : applications_open){
			closeApplication(application_open.getApplicationId());
			updateApplicationFlag(application_open,
					Application.FLAG_CLOSED_DUE_TO_ALL_POSITIONS_FILLED, 1);
		}
		
	}

	public void resolveApplicationConflicts_withinApplicationsForUser(HttpSession session, Integer reference_applicationId,
											List<WorkDay> workDays_toFindConflictsWith) {

		// **************************************************************
		// **************************************************************
		// Design note: two of the three below "for" loops are very, very similar.
		// The only difference is that the ProposedTo and ProposedBy are switched.
		// Consider combining these somehow.
		// **************************************************************
		// **************************************************************


		List<ApplicationDTO> applicationDtos_conflicting = this.getApplicationDtos_Conflicting(
												SessionContext.getUser(session).getUserId(),
												reference_applicationId, workDays_toFindConflictsWith);

		if(verificationService.isListPopulated(applicationDtos_conflicting)){

			// Object just to hold the categorized conflicting applications
			ApplicationDTO applicationDto_placeHolder = new ApplicationDTO();

			categorizeConflictingApplicationDtos(session, applicationDto_placeHolder, applicationDtos_conflicting);

			// *************************************************
			// Remove applications
			// *************************************************
			for ( ApplicationDTO applicationDto_toBeRemoved :
					applicationDto_placeHolder.getApplicationDtos_conflicting_willBeRemoved()){

				updateApplicationStatus(applicationDto_toBeRemoved.getApplication().getApplicationId(),
								Application.STATUS_CANCELLED_DUE_TO_TIME_CONFLICT);

				closeApplication(applicationDto_toBeRemoved.getApplication().getApplicationId());
			}

			// *************************************************
			// For applications currently in the **applicant's** queue:
			// 1) Create a new proposal with the conflicting days removed
			// 2) Send this proposal to the employer
			// *************************************************
			for ( ApplicationDTO applicationDto_toModifyAndSendToEmployer :
						applicationDto_placeHolder.getApplicationDtos_conflicting_willBeSentBackToEmployer()){

				Proposal currentProposal = proposalService.getCurrentProposal(applicationDto_toModifyAndSendToEmployer.getApplication().getApplicationId());
				Proposal newProposal = new Proposal(currentProposal);
				newProposal.setAmount(currentProposal.getAmount());
				newProposal.setProposedDates(jobService.removeConflictingWorkDays(
						currentProposal.getProposedDates(), workDays_toFindConflictsWith));

				proposalService.updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT, 1);

				proposalService.insertProposal(newProposal);

//				updateWageProposalStatus(currentProposal.getEmploymentProposalId(), EmploymentProposalDTO.STATUS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHER_EMPLOYMENT);

			}

			// *************************************************
			// For applications currently in the **employer's** queue:
			// 1) Create a new proposal with the conflicting days removed
			// 2) Send this proposal to the employer
			// *************************************************
			for ( ApplicationDTO applicationDto_toModifyAndNotifyEmployer :
					applicationDto_placeHolder.getApplicationDtos_conflicting_willBeModifiedButRemainAtEmployer()){

				Proposal currentProposal = proposalService.getCurrentProposal(
						applicationDto_toModifyAndNotifyEmployer.getApplication().getApplicationId());
				Proposal newProposal = new Proposal(currentProposal);
				
				newProposal.setAmount(currentProposal.getAmount());				
				newProposal.setProposedDates(jobService.removeConflictingWorkDays(
						currentProposal.getProposedDates(), workDays_toFindConflictsWith));

				proposalService.updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT, 1);

				proposalService.insertProposal(newProposal);
//				updateWageProposalStatus(currentProposal.getEmploymentProposalId(), EmploymentProposalDTO.STATUS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHER_EMPLOYMENT);

			}
		}
	}

	public void closeApplication(int applicationId) {
		repository.closeApplication(applicationId);
	}

	public void updateWageProposal_isCurrentProposal(Integer employmentProposalId, int isCurrentProposal) {
		repository.updateWageProposal_isCurrentProposal(employmentProposalId, isCurrentProposal);
	}



	public EmploymentProposalDTO getPreviousProposal(Integer referenceEmploymentProposalId, int applicationId) {
		return repository.getPreviousProposal(referenceEmploymentProposalId, applicationId);
	}


	public void categorizeConflictingApplicationDtos(HttpSession session, ConflictingApplicationsResponse response) {

		// ******************************************************
		// ******************************************************
		// Conflicting applications are dealt with in 1 of 3 ways:
		// 1) the application is removed/withdrawn.
		// 2) the current proposal is in the applicant's inbox. Thus  it is
		// modified and sent back to employer for his review.
		// 3) the current proposal is in the employer's inbox.
		// Thus it is modified and the employer is notified of the modification.
		// ******************************************************
		// ******************************************************

		for ( ConflictingApplication conflictingApplication : response.getConflictingApplications() ){

			// If a conflict exists with a job that DOES NOT allow partial availability,
			// then this application MUST be removed.
			if(conflictingApplication.getJob().getIsPartialAvailabilityAllowed() == false){
				response.getConflictingApplicationsToBeRemoved().add(conflictingApplication);
			}else{

				// Else if the conflict exists with a job that DOES allow partial availability:

				// and if the proposal is in the applicant's (i.e. the session user) queue,
				// then the proposal must be modified to remove the conflicting days from the proposal
				// and be sent back to the employer for his review.
				if(verificationService.isCurrentProposalProposedToSessionUser(
						conflictingApplication.getApplication().getApplicationId(), session)){

					response.getConflictingApplicationsToBeSentBackToEmployer().add(conflictingApplication);
				}else{

					// else the proposal is in the employer's queue.
					// The proposal will be updated to remove the conflicting days,
					// however, the employer will simply be notified of the modification.
					response.getConflictingApplicationsToBeModifiedButRemainAtEmployer().add(conflictingApplication);
				}
			}
		}
	}


	public Boolean getIsProposedToSessionUser(HttpSession session, EmploymentProposalDTO employmentProposalDto) {
		if(SessionContext.getUser(session).getUserId() == employmentProposalDto.getProposedToUserId())
			return true;
		else return false;
	}


	public List<ApplicationDTO> getApplicationDtos_Conflicting(int userId, int reference_applicationId,
													List<WorkDay> workDays) {

		List<ApplicationDTO> applicationDtos_conflicting = new ArrayList<ApplicationDTO>();

		if(workDays != null && workDays.size() > 0){

			List<Application> applications_conflicting = getApplications_WithAtLeastOneWorkDay(
															userId, reference_applicationId, workDays);

			for(Application application : applications_conflicting){
				ApplicationDTO applicationDto = new ApplicationDTO();

				applicationDto.setApplication(application);
				applicationDto.getJobDto().setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));

				applicationDtos_conflicting.add(applicationDto);
			}

		}

		return applicationDtos_conflicting;
	}


	public List<Application> getOpenApplications_byJob_withAtLeastOneWorkDay(
										int jobId, List<WorkDay> workDays){
		return repository.getOpenApplications__byJob_withAtLeastOneWorkDay(jobId, workDays);
	}

	public List<Application> getApplications_WithAtLeastOneWorkDay(int userId, int reference_applicationId,
			List<WorkDay> workDays) {

		return repository.getApplications_WithAtLeastOneWorkDay(
				userId, reference_applicationId, workDays);
	}




	public LocalDateTime getExpirationDate(LocalDateTime start, EmploymentProposalDTO employmentProposalDto){

		LocalDateTime expirationDate = start;

		if(employmentProposalDto.getDays_offerExpires() != null && 
				employmentProposalDto.getDays_offerExpires() > 0)
			expirationDate = expirationDate.plusDays(employmentProposalDto.getDays_offerExpires() );
		if(employmentProposalDto.getHours_offerExpires()  != null && 
				employmentProposalDto.getHours_offerExpires() > 0)
			expirationDate = expirationDate.plusHours(employmentProposalDto.getHours_offerExpires() );
		if(employmentProposalDto.getMinutes_offerExpires()  != null && 
				employmentProposalDto.getMinutes_offerExpires() > 0)
			expirationDate = expirationDate.plusMinutes(employmentProposalDto.getMinutes_offerExpires() );

		return expirationDate;

	}

	public void updateWageProposalStatus(int wageProposalId, int status) {
		repository.updateWageProposalStatus(wageProposalId, status);
	}

	public JobSearchUser getOtherUserInvolvedInWageProposal(WageProposal failedWageProposal,
			int dontReturnThisUserId) {

		int otherUserId;

		// If the proposed-by-user is not the prohibited user,
		if (failedWageProposal.getProposedByUserId() != dontReturnThisUserId) {
			otherUserId = failedWageProposal.getProposedByUserId();
		} else {
			otherUserId = failedWageProposal.getProposedToUserId();
		}
		return userService.getUser(otherUserId);
	}



	public void addAnswer(Answer answer) {
		repository.addAnswer(answer);
	}

	public AnswerOption getAnswerOption(int answerOptionId) {
		return repository.getAnswerOption(answerOptionId);
	}

	public void updateIsNew(Job job, int value) {
		repository.updateIsNew(job.getId(), value);

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
		if(verificationService.didSessionUserPostJob(session,
				applicationDto.getJobId()) &&
				!verificationService.didUserApplyForJob(applicationDto.getJobId(),
						applicationDto.getApplicantId())){

			// Set the application
			applicationDto.getApplication().setStatus(Application.STATUS_PROPOSED_BY_EMPLOYER);

			// Set the wage proposal
			LocalDateTime employerAcceptedDate = LocalDateTime.now();
			LocalDateTime expirationDate = getExpirationDate(employerAcceptedDate,
													applicationDto.getEmploymentProposalDto());
			applicationDto.getEmploymentProposalDto().setEmployerAcceptedDate(employerAcceptedDate);
			applicationDto.getEmploymentProposalDto().setExpirationDate(expirationDate);
			applicationDto.getEmploymentProposalDto().setProposedToUserId(
					applicationDto.getApplicantId());
			applicationDto.getEmploymentProposalDto().setProposedByUserId(
					SessionContext.getUser(session).getUserId());
			applicationDto.getEmploymentProposalDto().setStatus(WageProposal.STATUS_PENDING_APPLICANT_APPROVAL);

			
			this.insertApplication(applicationDto);	
			Application newApplication = getApplication(applicationDto.getJobId(),
					applicationDto.getApplicantId());
			Proposal proposal = proposalService.getCurrentProposal(newApplication.getApplicationId());

			// ******************************************************************
			// Is this flag needed on both objects?????????			
			updateApplicationFlag(newApplication, Application.FLAG_EMPLOYER_INITIATED_CONTACT, 1);
			proposalService.updateProposalFlag(proposal, EmploymentProposalDTO.FLAG_EMPLOYER_INITIATED_CONTACT, 1);
			// ******************************************************************
		}

	}

	public void updateApplicationFlag(Application newApplication, String flag, int value) {
		// Why do i have this??????
		// Just pass teh application id...
		repository.updateApplicationFlag(newApplication.getApplicationId(), flag, value );
	}

	public void updateApplicationFlag(Integer applicationId, String flag, int value) {
		repository.updateApplicationFlag(applicationId, flag, value );
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

	public Integer getCount_applicantsByDay(int dateId, int jobId) {
		return repository.getCount_applicantsByDay(dateId, jobId);
	}

	public Integer getCount_positionsFilledByDay(int dateId, int jobId) {
		return repository.getCount_positionsFilledByDay(dateId, jobId);
	}

	public List<Application> getApplications_byJobAndDate(int jobId, String dateString) {
		return repository.getApplications_byJobAndDate(jobId, jobService.getDateId(dateString));
	}

	public Boolean getIsWorkDayProposed(int workDayId, int applicationId) {
		Integer count_proposedDay = this.getCount_proposedDay_byApplicationAndWorkDay(applicationId, workDayId);
		if(count_proposedDay == 1) return true;
		else return false;
	}

	public Integer getCount_proposedDay_byApplicationAndWorkDay(int applicationId, int workDayId) {
		return repository.getCount_ProposedDay_byApplicationAndWorkDay(applicationId, workDayId);
	}


	public List<String> getMessages(JobSearchUser sessionUser, Application application,
			Proposal previousProposal,
			Proposal currentProposal) {
		
		List<String> messages = new ArrayList<>();
		Job job = jobService.getJob_ByApplicationId(application.getApplicationId());
		
		boolean isEmployee = true;
		if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER) isEmployee = false;
		
		if(previousProposal != null){
			// Employer filed all positions
			if(previousProposal.getFlag_isCanceledDueToEmployerFillingAllPositions() == 1){
				if(job.getIsPartialAvailabilityAllowed()){
					messages.add((isEmployee ? "The employer" : "You" )+ " filled all positions on select work days. The proposal has"
							+ " been updated to remove the work days that have been filled.");
				}else{
					messages.add((isEmployee ? "The employer" : "You" ) + " filled all positions.");
					messages.add((isEmployee ? "Your" : "The applicant's" ) + " proposal will remain in"
							+ (isEmployee ? " the employer's" : " your" ) + " inbox" );
				}
			}
			
			// The applicant accepted other employment
			if(previousProposal.getFlag_isCanceledDueToApplicantAcceptingOtherEmployment() == 1){
				messages.add((isEmployee ? "You have" : "The applicant has" )+ " accepted other employment.");
				
				if(job.getIsPartialAvailabilityAllowed()){
					messages.add("Because this job allows partial availability,"
							+ (isEmployee ? " your" : " the applicant's" )+ " proposed work days have"
									+ " been updated to resolve the time conflicts.");
				}else{
					messages.add("Because this job does not allow partial availability,"
							+ (isEmployee ? " your" : " the applicant's" )+ " applicant has been closed.");			}		
			}
			
			// The application was re-opened
			if(previousProposal.getFlag_applicationWasReopened() == 1){
				if(previousProposal.getFlag_aProposedWorkDayWasRemoved() == 1){
					if(isEmployee){
						messages.add("The employer deleted work days from the job posting that affect"
								+ " your employment.");
						messages.add("The employer is required to submit you a new proposal");
					}else{
						messages.add("You deleted work days from the job posting that affect the"
								+ " employee's schedule.");
						messages.add("You are required to submit a new proposal to the employee.");
					}
				}else if(previousProposal.getFlag_aProposedWorkDayTimeWasEdited() == 1){
					if(isEmployee){
						messages.add("The employer edited the start and end times that affect"
								+ " your employment.");
						messages.add("The employer is required to submit you a new proposal");
					}else{
						messages.add("You edited the start and end times that affect the employee's"
								+ " schedule.");
						messages.add("You are required to submit a new proposal to the employee.");
					}				
				}			
			}
		}
		
		
		
		// Employer initiated contact
		if(currentProposal.getFlag_employerInitiatedContact() == 1){
			messages.add((isEmployee ? "The employer" : "You") + " made you an initial offer");
		}
		
		if(currentProposal.getFlag_aProposedWorkDayWasRemoved() == 1){
			messages.add((isEmployee ? "The employer" : "You") + " deleted work days from the job post"
					+ " that affected" + (isEmployee ? " your" : " the applicant's") + " proposal");
		}else if(currentProposal.getFlag_aProposedWorkDayTimeWasEdited() == 1){
			messages.add((isEmployee ? "The employer" : "You") + " updatyed the start and end "
					+ " times that affect the current proposal");			
		}
		
		return messages;
	}

	public void setConflictingApplicationsResponse(Model model, HttpSession session,
					int applicationId_withReferenceTo, List<String> dateStrings_toFindConflictsWith) {
		
		
		ConflictingApplicationsResponse response = new ConflictingApplicationsResponse();
		Application referenceApplication = getApplication(applicationId_withReferenceTo);
		Job job = jobService.getJob_ByApplicationId(applicationId_withReferenceTo);
		JobSearchUser sessionUser = SessionContext.getUser(session);
		if(verificationService.didUserApplyForJob(job.getId(), sessionUser.getUserId())){
		

			List<WorkDay> workDays_toFindConflictsWith = jobService.getWorkDays_byJobAndDateStrings(
					job.getId(), dateStrings_toFindConflictsWith);
		

			if (verificationService.isListPopulated(workDays_toFindConflictsWith)){

				List<Application> conflictingApplications = getApplications_WithAtLeastOneWorkDay(
																sessionUser.getUserId(),
																applicationId_withReferenceTo,
																workDays_toFindConflictsWith);

				for(Application application : conflictingApplications){
					
					ConflictingApplication conflictingApplication = new ConflictingApplication();
					conflictingApplication.setApplication(application);
					conflictingApplication.setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
					response.getConflictingApplications().add(conflictingApplication);
				}

			}
						
			categorizeConflictingApplicationDtos(session, response);
			response.setReferenceApplication(referenceApplication);
			
			model.addAttribute("areConflictsCausedByCounteringWorkDays", true);
			model.addAttribute("response", response);
		}	
		
	}

	public boolean setModel_employerMakeFirstOffer(Model model, HttpSession session, int jobId) {

		if(verificationService.didSessionUserPostJob(session, jobId)){

			ApplicationDTO applicationDto = new ApplicationDTO();
			applicationDto.getJobDto().setJob(jobService.getJob(jobId));
			applicationDto.getJobDto().setWorkDayDtos(jobService.getWorkDayDtos(jobId));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(jobId));

			applicationDto.getJobDto().setDate_firstWorkDay(DateUtility.getMinimumDate(applicationDto.getJobDto().getWorkDays()).toString());
			applicationDto.getJobDto().setMonths_workDaysSpan(DateUtility.getMonthSpan(applicationDto.getJobDto().getWorkDays()));

			model.addAttribute("applicationDto", applicationDto);
			model.addAttribute("isEmployerMakingFirstOffer", true);
			model.addAttribute("json_workDayDtos", JSON.stringify(applicationDto.getJobDto().getWorkDayDtos()));

			return true;
		}else return false;
	}

	public List<WorkDayDto> getWorkDayDtos_proposedWorkDays(int applicationId, HttpSession session) {

		JobSearchUser sessionUser =  SessionContext.getUser(session);
		if( ( sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE &&
				verificationService.didUserApplyForJob(jobService.getJob_ByApplicationId(applicationId).getId(),
														sessionUser.getUserId() ) )

			||

			( sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER &&
			verificationService.didSessionUserPostJob(session, jobService.getJob_ByApplicationId(applicationId)) ) ){

			return workDayService.getWorkDayDtos_byProposal(proposalService.getCurrentProposal(applicationId));
		}else return null;
	}

	public List<Application> getApplications_byJobAndAtLeastOneWorkDay(int jobId,
			List<WorkDay> workDays) {
		return repository.getApplications_byJobAndAtLeastOneWorkDay(jobId, workDays);
	}

	public List<Application> getAcceptedApplications_byJobAndAtLeastOneWorkDay(int jobId,
			List<WorkDay> workDays) {


		// **************************************************
		// **************************************************
		// Verify this is still needed after the logic for job edits is finalized.
		// Review this later
		// **************************************************
		// **************************************************


		return repository.getAcceptedApplications_byJobAndAtLeastOneWorkDay(jobId, workDays);
	}



	public void openApplication(int applicationId) {
		repository.openApplication(applicationId);
	}

	public void deleteEmployment(int userId, int jobId) {
		repository.deleteEmployment(userId, jobId);
		repository.deleteRatings(userId, jobId);
	}

	public String getTotalPayment(EmploymentProposalDTO acceptedProposal) {

		// ***********************************************************
		// This assumes each work day starts and ends on the same calendar day
		// ***********************************************************

		double totalMinutes = 0;
		List<WorkDay> workDays = jobService.getWorkDays_byProposalId(acceptedProposal.getEmploymentProposalId());
		for( WorkDay workDay : workDays){
			totalMinutes += ChronoUnit.MINUTES.between(
					LocalTime.parse(workDay.getStringStartTime()),
					LocalTime.parse(workDay.getStringEndTime()));
		}


		return String.format("%.2f", (totalMinutes / 60) * Double.parseDouble(acceptedProposal.getAmount()));
	}

	public List<Application> getApplications_byUser_openOrAccepted(int userId) {
		return repository.getApplications_byUser_openOrAccepted(userId);
	}

	public void updateFlag_applicantAcknowledgesAllPositionsAreFilled(HttpSession session, int applicationId) {

		Application application = getApplication(applicationId);

		if(application.getUserId() == SessionContext.getUser(session).getUserId()){
			updateApplicationFlag(application,
					Application.FLAG_APPLICANT_ACKNOWLEDGED_ALL_POSITIONS_ARE_FILLED, 1);
		}
	}

	public List<WorkDayDto> getWorkDayDtos_proposedWorkDays(int jobId, int userId, HttpSession session) {

		Application application = getApplication(jobId, userId);
		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();
		if(application != null){
			workDayDtos = getWorkDayDtos_proposedWorkDays(application.getApplicationId(), session);
		}
		return workDayDtos;
	}

	public void deleteProposedWorkDays(List<WorkDay> workDays, int applicationId) {
		repository.deleteProposedWorkDays(workDays, applicationId);
	}

	public List<Application> applications_closedDueToAllPositionsFilled_unacknowledged(int userId) {
		return repository.applications_closedDueToAllPositionsFilled_unacknowledged(userId);
	}





	public void inspectNewness(Application application) {
		if(application.getIsNew() == 1)	updateApplicationFlag(application, "IsNew", 0);
	}

	public List<Application> getApplications_byJob(Integer jobId) {
		return repository.getApplications_byJob(jobId);
	}

	public List<MessageResponse> getMessagesResponses_applicationsClosedDueToAllPositionsFilled(int userId) {
		
		// Applications that were closed due to the employer filling all positions
		List<Application> applications_closedDueToAllPositionsFilled_unacknowledged =
				applications_closedDueToAllPositionsFilled_unacknowledged(userId);
		List<MessageResponse> messageResponses = new ArrayList<>();
		
		if(verificationService.isListPopulated(applications_closedDueToAllPositionsFilled_unacknowledged)){
			for(Application application : applications_closedDueToAllPositionsFilled_unacknowledged){
				MessageResponse messageResponse = new MessageResponse();
				messageResponse.setApplication(application);
				messageResponse.setJob(jobService.getJob(application.getJobId()));
				messageResponses.add(messageResponse);
			}
		}	
		
		return messageResponses;
	}



}
