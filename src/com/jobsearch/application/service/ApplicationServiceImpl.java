package com.jobsearch.application.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
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


	public List<ApplicationDTO> getApplicationDtos_ByJob_OpenApplications(int jobId, HttpSession session) {

		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();

		// Query the application table
		List<Application> applications = repository.getApplications_ByJob_OpenApplications(jobId);

		for (Application application : applications) {

			ApplicationDTO applicationDto =  new ApplicationDTO();

			applicationDto.setApplication(application);

			applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));

			int applicantId = applicationDto.getApplicantDto().getUser().getUserId();

			applicationDto.setEmploymentProposalDto(this.getCurrentEmploymentProposal(application.getApplicationId()));
			applicationDto.getEmploymentProposalDto().setIsProposedToSessionUser(
					getIsProposedToSessionUser(session, applicationDto.getEmploymentProposalDto()));
			applicationDto.setPreviousProposal(getPreviousProposal(
					applicationDto.getEmploymentProposalDto().getEmploymentProposalId(), application.getApplicationId()));


			applicationDto.setTime_untilEmployerApprovalExpires(
							this.getTime_untilEmployerApprovalExpires(
									application.getExpirationDate()));
			applicationDto.getApplicantDto().setRatingValue_overall(userService.getRating(applicantId));

			applicationDto.setQuestions(this.getQuestionsWithAnswersByJobAndUser(jobId, applicantId));

			applicationDto.setAnswerOptionIds_Selected(
						this.getAnswerOptionIds_Selected_ByApplicantAndJob(applicantId, jobId));

//			applicationDto.setDateStrings_availableWorkDays(
//								this.getProposedWorkDays(applicationDto.getEmploymentProposalDto().getEmploymentProposalId()));


			applicationDto.getJobDto().setJob(jobService.getJob(jobId));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(jobId));
//			applicationDto.getJobDto().setWorkDayDtos(jobService.getWorkDayDtos_byProposal(
//											applicationDto.getEmploymentProposalDto()));

			applicationDto.getJobDto().setDate_firstWorkDay(DateUtility.getMinimumDate(applicationDto.getJobDto().getWorkDays()).toString());
			applicationDto.getJobDto().setMonths_workDaysSpan(DateUtility.getMonthSpan(applicationDto.getJobDto().getWorkDays()));

			applicationDtos.add(applicationDto);
		}

		return applicationDtos;
	}

	public EmploymentProposalDTO getCurrentEmploymentProposal(Integer applicationId) {
		return repository.getCurrentEmploymentProposal(applicationId);
	}

	public List<String> getProposedDateStrings(int employmentProposalId) {
		return repository.getProposedDateStrings(employmentProposalId);
	}

	public String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate) {

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
		// Verify at least one application work day is within the job work days
		// ****************************************
		// ****************************************

		if(verificationService.isPositiveNumber(applicationDto.getEmploymentProposalDto().getAmount())){

			// Set the employment proposal
			applicationDto.getEmploymentProposalDto().setProposedByUserId(sessionUser.getUserId());
			applicationDto.getEmploymentProposalDto().setProposedToUserId(appliedToJob.getUserId());
			applicationDto.getEmploymentProposalDto().setStatus(WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED);

			if(!appliedToJob.getIsPartialAvailabilityAllowed()){
				applicationDto.getEmploymentProposalDto().setDateStrings_proposedDates(
										jobService.getWorkDayDateStrings(applicationDto.getJobId()));
			}

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

	public void respondToEmploymentProposal(EmploymentProposalDTO employmentProposalDto,
											HttpSession session,
											String context) {


		// Get the proposal being **RESPONDED TO**.
		// Per the application id, get the most recent employment proposal in the database.
		EmploymentProposalDTO proposalBeingRespondedTo = getCurrentEmploymentProposal(
							employmentProposalDto.getApplicationId());

		JobSearchUser sessionUser = SessionContext.getUser(session);
		Job job = jobService.getJob_ByApplicationId(proposalBeingRespondedTo.getApplicationId());


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

				case "counter-by-applicant":
				case "acknowledge-by-employer":



					// ****************************************************
					// ****************************************************
					// Is it desired to have transparency into whether the employer is "accepting"
					// or "countering" the applicant's proposal?
					// I'm thinking so. Then we can say to the applicant: "the employer accepted your offer"
					// or "the employer countered your offer". As an applicant, I think I
					// would appreciate the distinction.
					// ****************************************************
					// ****************************************************

					// **Employers** must provide an expiration time for all acknowledgments they make.
					// **Applicants** must counter before the employer's offer is expired.
					if( ( sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER &&
							isValidExpirationTime(employmentProposalDto) ) ||
						(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE &&
								!isEmployerAcceptanceExpired(proposalBeingRespondedTo)) ){

						EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
						newProposal.setProposedByUserId(proposalBeingRespondedTo.getProposedToUserId());
						newProposal.setProposedToUserId(proposalBeingRespondedTo.getProposedByUserId());
						newProposal.setStatus(WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED);
						newProposal.setApplicationId(proposalBeingRespondedTo.getApplicationId());

						newProposal.setDateStrings_proposedDates(employmentProposalDto.getDateStrings_proposedDates());
						newProposal.setAmount(employmentProposalDto.getAmount());
						
						
						if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER){
							newProposal.setEmployerAcceptedDate(LocalDateTime.now());
							newProposal.setExpirationDate(getExpirationDate(LocalDateTime.now(), employmentProposalDto));
						}

						// ***********************************************************************************
						// This needs to be verified
//						if(verificationService.isListPopulated(employmentProposalDto.getDateStrings_proposedDates())){
//							newProposal.setDateStrings_proposedDates(employmentProposalDto.getDateStrings_proposedDates());
//						}
//						else{
//							newProposal.setDateStrings_proposedDates(proposalBeingRespondedTo.getDateStrings_proposedDates());
//						}
//						if(verificationService.isPositiveNumber(employmentProposalDto.getAmount())){
//							newProposal.setAmount(employmentProposalDto.getAmount());
//						}else{
//							newProposal.setAmount(proposalBeingRespondedTo.getAmount());
//						}
						// ***********************************************************************************

						this.updateWageProposalStatus(proposalBeingRespondedTo.getEmploymentProposalId(),
								WageProposal.STATUS_COUNTERED);

						this.insertEmploymentProposal(newProposal);



					}
					break;

				case "approve-by-applicant":

					// ******************************************************
					// ******************************************************
					// Checking whether amount-hired is less than positions-to-be-filled-per-day is not adequate.
					// Since queries are asynchronous, this condition can be met by two different requests,
					// while there is only one position left to fill.
					// This needs to be addressed.
					// _________________________________________________________
					// Also, it needs to be verified that each work day has at least one position available.
					// I think creating a new table that links WorkDayId to UserId is best.
					// So if Job 123 has WorkDayIds 4 and 5, is filling two positions per day, and hires UserIds 6 and 7:
					// then the table will have four rows:
					// 4 and 6;
					// 5 and 6;
					// 4 and 7;
					// 5 and 7;
					// __________________________________________________________
					// Update: to verify each proposed work day does indeed have a position available,
					// the "counts" are being compared. Essentially, we need to handle the situation
					// where multiple, simultaneous requests for the same position are submitted.
					// Do we process each request, include a time stamp, then do a post-insert query
					// to ensure the work day's position's-per-day is not exceeded??? If it is exceeded,
					// then delete the necessary records starting with the newest time stamps.
					// ******************************************************
					// ******************************************************
					if(!isEmployerAcceptanceExpired(proposalBeingRespondedTo) &&
							( verificationService.isAPositionAvaiableEachProposedWorkDay(
									proposalBeingRespondedTo.getEmploymentProposalId(), job.getId())) ){

						this.updateWageProposalStatus(proposalBeingRespondedTo.getEmploymentProposalId(),
														EmploymentProposalDTO.STATUS_APPROVED_BY_APPLICANT);

						this.updateApplicationStatus(proposalBeingRespondedTo.getApplicationId(),
													Application.STATUS_ACCEPTED);

						this.closeApplication(proposalBeingRespondedTo.getApplicationId());

						userService.insertEmployment(proposalBeingRespondedTo.getProposedToUserId(), job.getId());

						// Update the job after the employment record has been inserted.
						// The "IsStillAcceptingApplications" property needs to be updated.
						job = jobService.getJob(job.getId());

						resolveApplicationConflicts_withinApplicationsForAJob(job,
								proposalBeingRespondedTo);

						resolveApplicationConflicts_withinApplicationsForUser(session,
								employmentProposalDto.getApplicationId(),
								jobService.getWorkDays_byProposalId(
										proposalBeingRespondedTo.getEmploymentProposalId()));

					}
					break;
			}
		}
	}



	public void resolveApplicationConflicts_withinApplicationsForAJob(Job job,
			EmploymentProposalDTO proposalDto_justAccepted) {
		
		if(job.getFlag_isNotAcceptingApplications() == 1) closeAllOpenApplications(job.getId());
		else{
	
			List<WorkDay> workDays_toFindConflictsWith = jobService.getWorkDays_byProposalId(
											proposalDto_justAccepted.getEmploymentProposalId());
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
					EmploymentProposalDTO currentProposal = getCurrentEmploymentProposal(
							application.getApplicationId());
					EmploymentProposalDTO modifiedProposal = currentProposal;
	
					modifiedProposal.setDateStrings_proposedDates(jobService.removeConflictingWorkDays(
							currentProposal.getDateStrings_proposedDates(), workDays_withAllPositionsFilled));
	
					updateProposalFlag(currentProposal,
							EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS,
							1);
	
					updateWageProposalStatus(currentProposal.getEmploymentProposalId(),
												EmploymentProposalDTO.STATUS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS);
	
					insertEmploymentProposal(modifiedProposal);
	
//					updateApplicationStatus(application.getApplicationId(),
//							Application.STATUS_CANCELLED_DUE_TO_EMPLOYER_FILLED_ALL_POSITIONS);
//					updateApplicationFlag(application,
//							Application.FLAG_CLOSED_DUE_TO_ALL_POSITIONS_FILLED, 1);
//					closeApplication(application.getApplicationId());
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

				EmploymentProposalDTO currentProposal = getCurrentEmploymentProposal(applicationDto_toModifyAndSendToEmployer.getApplication().getApplicationId());

				EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
				newProposal.setApplicationId(currentProposal.getApplicationId());
				newProposal.setProposedByUserId(currentProposal.getProposedToUserId());
				newProposal.setProposedToUserId(currentProposal.getProposedByUserId());
				newProposal.setAmount(currentProposal.getAmount());
				newProposal.setStatus(EmploymentProposalDTO.STATUS_SUBMITTED_BUT_NOT_VIEWED);

				newProposal.setDateStrings_proposedDates(jobService.removeConflictingWorkDays(
						currentProposal.getDateStrings_proposedDates(), workDays_toFindConflictsWith));

				updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT, 1);

				insertEmploymentProposal(newProposal);

//				updateWageProposalStatus(currentProposal.getEmploymentProposalId(), EmploymentProposalDTO.STATUS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHER_EMPLOYMENT);

			}

			// *************************************************
			// For applications currently in the **employer's** queue:
			// 1) Create a new proposal with the conflicting days removed
			// 2) Send this proposal to the employer
			// *************************************************
			for ( ApplicationDTO applicationDto_toModifyAndNotifyEmployer :
					applicationDto_placeHolder.getApplicationDtos_conflicting_willBeModifiedButRemainAtEmployer()){

				EmploymentProposalDTO currentProposal = getCurrentEmploymentProposal(applicationDto_toModifyAndNotifyEmployer.getApplication().getApplicationId());

				EmploymentProposalDTO newProposal = new EmploymentProposalDTO();
				newProposal.setApplicationId(currentProposal.getApplicationId());
				newProposal.setProposedByUserId(currentProposal.getProposedByUserId());
				newProposal.setProposedToUserId(currentProposal.getProposedToUserId());
				newProposal.setAmount(currentProposal.getAmount());
				newProposal.setStatus(EmploymentProposalDTO.STATUS_SUBMITTED_BUT_NOT_VIEWED);

				newProposal.setDateStrings_proposedDates(jobService.removeConflictingWorkDays(
						currentProposal.getDateStrings_proposedDates(), workDays_toFindConflictsWith));

				updateProposalFlag(currentProposal,
						EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT, 1);

				insertEmploymentProposal(newProposal);
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

	public EmploymentProposalDTO getPreviousProposal(Integer referenceEmploymentProposalId, int applicationId) {
		return repository.getPreviousProposal(referenceEmploymentProposalId, applicationId);
	}


	public void categorizeConflictingApplicationDtos(HttpSession session, ApplicationDTO applicationDto,
														List<ApplicationDTO> applicationDtos_conflicting) {

		// ******************************************************
		// ******************************************************
		// Conflicting applications are dealt with in 1 of 3 ways:
		// 1) the application is removed/withdrawn.
		// 2) the application proposal is modified and sent back to employer for his review.
		// 3) the application proposal is modified and the employer is notified of the modification.
		// ******************************************************
		// ******************************************************

		for ( ApplicationDTO applicationDto_conflicting : applicationDtos_conflicting ){

			// If a conflict exists with a job that DOES NOT allow partial availability,
			// then this application MUST be removed.
			if(applicationDto_conflicting.getJobDto().getJob().getIsPartialAvailabilityAllowed() == false){
				applicationDto.getApplicationDtos_conflicting_willBeRemoved().add(applicationDto_conflicting);
			}else{

				// Else if the conflict exists with a job that DOES allow partial availability:

				// and if the proposal is in the applicant's (i.e. the session user) queue,
				// then the proposal must be modified to remove the conflicting days from the proposal
				// and be sent back to the employer for his review.
				if(verificationService.isCurrentProposalProposedToSessionUser(
						applicationDto_conflicting.getApplication().getApplicationId(), session)){

					applicationDto.getApplicationDtos_conflicting_willBeSentBackToEmployer()
						.add(applicationDto_conflicting);
				}else{

					// else the proposal is in the employer's queue.
					// The proposal will be updated to remove the conflicting days,
					// however, the employer will simply be notified of the modification.
					applicationDto.getApplicationDtos_conflicting_willBeModifiedButRemainAtEmployer()
						.add(applicationDto_conflicting);
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


	public EmploymentProposalDTO getEmploymentProposalDto(int employmentProposalId) {

		return repository.getEmploymentProposalDto(employmentProposalId);

	}

	private boolean isEmployerAcceptanceExpired(EmploymentProposalDTO employmentProposalDto) {


		if(ChronoUnit.MINUTES.between(LocalDateTime.now(), employmentProposalDto.getExpirationDate()) < 0) return true;
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



	public void addAnswer(Answer answer) {
		repository.addAnswer(answer);
	}

	public AnswerOption getAnswerOption(int answerOptionId) {
		return repository.getAnswerOption(answerOptionId);
	}

	public void updateIsNew(Job job, int value) {
		repository.updateIsNew(job.getId(), value);

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

			// Set the wage proposal
			LocalDateTime employerAcceptedDate = LocalDateTime.now();
			LocalDateTime expirationDate = getExpirationDate(employerAcceptedDate,
													applicationDto.getEmploymentProposalDto());
			applicationDto.getEmploymentProposalDto().setEmployerAcceptedDate(employerAcceptedDate);
			applicationDto.getEmploymentProposalDto().setExpirationDate(expirationDate);
			applicationDto.getEmploymentProposalDto().setProposedToUserId(applicationDto.getApplicantId());
			applicationDto.getEmploymentProposalDto().setProposedByUserId(SessionContext.getUser(session).getUserId());
			applicationDto.getEmploymentProposalDto().setStatus(WageProposal.STATUS_PENDING_APPLICANT_APPROVAL);

			this.insertApplication(applicationDto);	
			Application newApplication = getApplication(applicationDto.getJobId(), applicationDto.getApplicantId());
			EmploymentProposalDTO proposal = getCurrentEmploymentProposal(newApplication.getApplicationId());

			updateApplicationFlag(newApplication, Application.FLAG_EMPLOYER_INITIATED_CONTACT, 1);
			updateProposalFlag(proposal, EmploymentProposalDTO.FLAG_EMPLOYER_INITIATED_CONTACT, 1);

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


		// *******************************************************************
		// *******************************************************************
		// If there is a soon-to-be-previous proposal, should this method also be responsible to set the
		// status of this proposal in addition to setting its IsCurrentProposal = 0??
		// It will help ensure statuses are always being updated...
		// *******************************************************************
		// *******************************************************************
		// Before this new proposal is added, set the current proposal's IsCurrentProposal to 0
		EmploymentProposalDTO currentProposal = getCurrentEmploymentProposal(employmentProposalDto.getApplicationId());
		if(currentProposal != null) updateWageProposal_isCurrentProposal(currentProposal.getEmploymentProposalId(), 0);

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

	public int getCountApplications_total(Integer jobId) {
		return repository.getCountApplications_total(jobId);
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

	public void setModel_ViewCurrentProposal(Model model, HttpSession session, int applicationId) {

		if(verificationService.isCurrentProposalProposedToSessionUser(applicationId, session)){

			JobSearchUser sessionUser = SessionContext.getUser(session);

			ApplicationDTO	applicationDto = new ApplicationDTO();
			applicationDto.setApplication(getApplication(applicationId));

			int jobId = applicationDto.getApplication().getJobId();
			applicationDto.setEmploymentProposalDto(getCurrentEmploymentProposal(applicationId));

			// Job dto
			applicationDto.getJobDto().setJob(jobService.getJob(jobId));
			applicationDto.getJobDto().setWorkDayDtos(jobService.getWorkDayDtos_byProposal(
											applicationDto.getEmploymentProposalDto()));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(jobId));


			applicationDto.getJobDto().setDate_firstWorkDay(DateUtility.getMinimumDate(applicationDto.getJobDto().getWorkDays()).toString());
			applicationDto.getJobDto().setMonths_workDaysSpan(DateUtility.getMonthSpan(applicationDto.getJobDto().getWorkDays()));

			// Conflicting applications
			List<ApplicationDTO> applicationDtos_conflicting = getApplicationDtos_Conflicting(
												sessionUser.getUserId(),
												applicationId,
												jobService.getWorkDays_byProposalId(applicationDto.getEmploymentProposalDto().getEmploymentProposalId()));

			categorizeConflictingApplicationDtos(session, applicationDto, applicationDtos_conflicting);

			// Model
			model.addAttribute("json_workDayDtos", JSON.stringify(applicationDto.getJobDto().getWorkDayDtos()));
			model.addAttribute("applicationDto", applicationDto);
			model.addAttribute("user", sessionUser);
			model.addAttribute("isEmployerMakingFirstOffer", false);
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

			return jobService.getWorkDayDtos_byProposal(getCurrentEmploymentProposal(applicationId));
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

	public void updateProposalFlag(EmploymentProposalDTO currentProposal,
			String proposalFlag, int value) {

		repository.updateProposalFlag(currentProposal.getEmploymentProposalId(),
				proposalFlag, value);
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
}
