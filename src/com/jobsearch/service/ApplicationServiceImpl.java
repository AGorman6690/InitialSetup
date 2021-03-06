package com.jobsearch.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.dtos.ApplicationDTO;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Application;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.Question;
import com.jobsearch.model.WorkDay;
import com.jobsearch.repository.ApplicationRepository;
import com.jobsearch.request.ApplicationProgressRequest;
import com.jobsearch.request.ApplyForJobRequest;
import com.jobsearch.request.ConflictingApplicationsRequest;
import com.jobsearch.request.MakeInitialOfferByEmployerRequest;
import com.jobsearch.responses.ApplyForJobResponse;
import com.jobsearch.responses.ConflictingApplicationsResponse;
import com.jobsearch.responses.CurrentProposalResponse;
import com.jobsearch.responses.ConflictingApplicationsResponse.ConflictingApplication;
import com.jobsearch.responses.MessageResponse;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.VerificationServiceImpl;


@Service
public class ApplicationServiceImpl {

	@Autowired
	ApplicationRepository repository;
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
	@Autowired
	RatingServiceImpl ratingService;	
	@Autowired
	QuestionServiceImpl questionService;
	

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

	public ApplyForJobResponse applyForJob(ApplyForJobRequest request, HttpSession session) {
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		Job job = jobService.getJob(request.getJobId());
		
		Proposal proposal = new Proposal();
		proposal.setProposedByUserId(sessionUser.getUserId());
		proposal.setProposedToUserId(job.getUserId());
		proposal.setAmount(request.getProposedWage());
		proposal.setProposedDates(request.getProposedDates());
				
		// Validate
		boolean valid = true;		
		if(verificationService.didUserApplyForJob(request.getJobId(), sessionUser.getUserId())){
			valid = false;
		}else if(job.getFlag_isNotAcceptingApplications() == 1){
			valid = false;
		}else if(!proposalService.isValidProposal(proposal, job)){
			valid = false;
		}else if(jobService.doesUserHaveConflictingEmployment(request.getJobId(), request.getProposedDates(), sessionUser.getUserId())){
			valid = false;
		}else if(!questionService.areAllQuestionsAnswered(request.getAnswers(), request.getJobId())){
			valid = false;
		}
		
		ApplyForJobResponse response = new ApplyForJobResponse();
		if(valid){				
			Application application = new Application();
			application.setUserId(sessionUser.getUserId());
			application.setJobId(request.getJobId());
			application.setStatus(Application.STATUS_SUBMITTED);
			insertApplication(application, proposal, request.getAnswers());
			response.setSuccess(true);		
		}else{
			response.setSuccess(false);
		}
		
		return response;
	}

	public void insertApplication(Application application, Proposal proposal, List<Answer> answers) {
		repository.insertApplication(application, proposal, answers);
	}
	
	public List<AnswerOption> getAnswerOptions(int questionId) {
		return repository.getAnswerOptions(questionId);
	}

	


	public void closeAllOpenApplications(Integer jobId) {
		List<Application> applications_open = repository.getApplications_ByJob_OpenApplications(jobId);
		for(Application application_open : applications_open){
			closeApplication(application_open.getApplicationId());
			updateApplicationFlag(application_open,
					Application.FLAG_CLOSED_DUE_TO_ALL_POSITIONS_FILLED, 1);
		}
		
	}

	public void resolveApplicationConflicts_withinApplicationsForUser(HttpSession session,
											Integer reference_applicationId,
											List<WorkDay> workDays_toFindConflictsWith) {
		
		List<Application> conflictingApplications = getApplications_WithAtLeastOneWorkDay(
				SessionContext.getUser(session).getUserId(),
				reference_applicationId,
				workDays_toFindConflictsWith);

		for (Application conflictingApplication : conflictingApplications){
			
			Proposal currentProposal = proposalService.getCurrentProposal(conflictingApplication.getApplicationId());
			List<String> resolvedWorkDays = workDayService.removeConflictingWorkDays(
					currentProposal.getProposedDates(), workDays_toFindConflictsWith);
			
			if(doesConflictingApplicationNeedToBeRemoved(conflictingApplication) 
					|| resolvedWorkDays.size() == 0){				
				updateApplicationStatus(conflictingApplication.getApplicationId(),
						Application.STATUS_CANCELLED_DUE_TO_TIME_CONFLICT);
				closeApplication(conflictingApplication.getApplicationId());		
			}else{
								
				Proposal newProposal = new Proposal(currentProposal);				
				
				if(!doesConflictingApplicationNeedToBeModifiedAndSentBackToEmployer(
						conflictingApplication, session)){			
					newProposal.setProposedByUserId(currentProposal.getProposedByUserId());
					newProposal.setProposedToUserId(currentProposal.getProposedToUserId());				
				}

				newProposal.setAmount(currentProposal.getAmount());
				newProposal.setProposedDates(resolvedWorkDays);
				proposalService.insertProposal(newProposal, currentProposal);		
				
				Proposal newCurrentProposal = proposalService.getCurrentProposal(
						conflictingApplication.getApplicationId());
				
				proposalService.updateProposalFlag(currentProposal,
						Proposal.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT, 1);
				proposalService.updateProposalFlag(newCurrentProposal,
						Proposal.FLAG_IS_CREATED_DUE_TO_APPLICANT_ACCEPTING_OTHER_EMPLOYMENT, 1);
			}				
		}
	}

	private boolean doesConflictingApplicationNeedToBeModifiedAndSentBackToEmployer(
			Application conflictingApplication, HttpSession session) {
		if( verificationService.isCurrentProposalProposedToSessionUser(
				conflictingApplication.getApplicationId(), session)){
			return true;
		}else return false;
	}

	private boolean doesConflictingApplicationNeedToBeRemoved(Application conflictingApplication) {
		Job job = jobService.getJob(conflictingApplication.getJobId());
		if(!job.getIsPartialAvailabilityAllowed()){
			return true;
		}else return false;
	}

	public void closeApplication(int applicationId) {
		repository.closeApplication(applicationId);
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


	public AnswerOption getAnswerOption(int answerOptionId) {
		return repository.getAnswerOption(answerOptionId);
	}

	public boolean wasUserEmployedForJob(int userId, int jobId) {
		int countEmploymentRecord = repository.getCount_Employment_ByUserAndJob(userId, jobId);
		if(countEmploymentRecord  == 1) return true;
		else return false;
	}

	public void updateApplicationFlag(Application newApplication, String flag, int value) {
		// Why do i have this??????
		// Just pass teh application id...
		repository.updateApplicationFlag(newApplication.getApplicationId(), flag, value );
	}

	public void updateApplicationFlag(Integer applicationId, String flag, int value) {
		repository.updateApplicationFlag(applicationId, flag, value );
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
			Proposal previousProposal, Proposal currentProposal) {
		
		List<String> messages = new ArrayList<>();
		Job job = jobService.getJob_ByApplicationId(application.getApplicationId());
		
		boolean isEmployee = true;
		if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER){
			isEmployee = false;
		}

		// Employer filed all positions
		if(currentProposal.getFlag_isCreatedDueToEmployerFillingAllPositions() == 1){
			if(job.getIsPartialAvailabilityAllowed()){
				messages.add((isEmployee ? "The employer" : "You" )+ " filled all positions on select work days. The proposal has"
						+ " been updated to remove the work days that have been filled.");
			}else{
				messages.add((isEmployee ? "The employer" : "You" ) + " filled all positions.");
				messages.add((isEmployee ? "Your" : "The applicant's" ) + " proposal will remain in"
						+ (isEmployee ? " the employer's" : " your" ) + " inbox." );
			}
		}
		
		// The applicant accepted other employment
		if(currentProposal.getFlag_isCreatedDueToApplicantAcceptingOtherEmployment() == 1){
			messages.add((isEmployee ? "You have" : "The applicant has" )+ " accepted other employment.");
			
			if(job.getIsPartialAvailabilityAllowed()){
				messages.add("Because this job allows partial availability,"
						+ (isEmployee ? " your" : " the applicant's" )+ " proposed work days have"
								+ " been updated to resolve the time conflicts.");
			}else{
				messages.add("Because this job does not allow partial availability,"
						+ (isEmployee ? " your" : " the applicant's" )+ " applicant has been closed.");			}		
		}
			
		if(previousProposal != null){
			// The application was re-opened
			if(previousProposal.getFlag_applicationWasReopened() == 1){
				if(previousProposal.getFlag_aProposedWorkDayWasRemoved() == 1){
					if(isEmployee){
						messages.add("The employer deleted work days from the job posting that affect"
								+ " your employment.");
						messages.add("The employer is required to submit you a new proposal.");
					}else{
						messages.add("You deleted work days from the job posting that affect the"
								+ " employee's schedule.");
						messages.add("You are required to submit a new proposal to the employee.");
					}
				}else if(previousProposal.getFlag_aProposedWorkDayTimeWasEdited() == 1){
					if(isEmployee){
						messages.add("The employer edited the start and end times that affect"
								+ " your employment.");
						messages.add("The employer is required to submit you a new proposal.");
					}else{
						messages.add("You edited the start and end times that affect the employee's"
								+ " schedule.");
						messages.add("You are required to submit a new proposal to the employee.");
					}				
				}			
			}
		}
		
		
		if(currentProposal.getFlag_employerAcceptedTheOffer() == 1 && application.getIsAccepted() == 0){
			messages.add((isEmployee ? "The employer accepted your offer"
					: "You accepted the applicant's offer." ));
		}
		
		// Employer initiated contact
		if(currentProposal.getFlag_employerInitiatedContact() == 1){
			messages.add((isEmployee ? "The employer made you" : "You made") + " the initial offer.");
		}
		
		if(currentProposal.getFlag_aProposedWorkDayWasRemoved() == 1){
			messages.add((isEmployee ? "The employer" : "You") + " deleted work days from the job post"
					+ " that affected" + (isEmployee ? " your" : " the applicant's") + " proposal.");
		}else if(currentProposal.getFlag_aProposedWorkDayTimeWasEdited() == 1){
			messages.add((isEmployee ? "The employer" : "You") + " updatyed the start and end "
					+ " times that affect the current proposal.");			
		}
		
		// Expiration
		if(currentProposal.getFlag_hasExpired() == 1 && application.getIsAccepted() == 0){
			if(isEmployee){
				messages.add("The employer's offer has expired. The employer has the next action.");	
			}else{
				messages.add("Your offer has expired.");
			}
		}
		
		return messages;
	}

	public void setConflictingApplicationsResponse(Model model, HttpSession session,
					ConflictingApplicationsRequest request) {
		
		ConflictingApplicationsResponse response = new ConflictingApplicationsResponse();
		
		Application referenceApplication = getApplication(request.getReferenceApplicationId());
		Job job = jobService.getJob_ByApplicationId(request.getReferenceApplicationId());
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		if(verificationService.didUserApplyForJob(job.getId(), sessionUser.getUserId())){
			
			Integer countWorkDays = workDayService.getWorkDayCount(job.getId());
			response.setJobHasOnlyOneWorkDay(countWorkDays == 1 ? true : false);
		
			List<WorkDay> workDays_toFindConflictsWith = workDayService.getWorkDays_byJobAndDateStrings(
					job.getId(), request.getDatesToFindConflictWith());
		
			if (verificationService.isListPopulated(workDays_toFindConflictsWith)){
				
				List<Application> conflictingApplications = getApplications_WithAtLeastOneWorkDay(
																sessionUser.getUserId(),
																request.getReferenceApplicationId(),
																workDays_toFindConflictsWith);
				
				response.setConflictingApplicationsToBeModifiedButRemainAtEmployer(new ArrayList<>());
				response.setConflictingApplicationsToBeRemoved_jobDoesNotAllowPartial(new ArrayList<>());
				response.setConflictingApplicationsToBeSentBackToEmployer(new ArrayList<>());
				response.setConflictingApplicationsToBeRemoved_applicantHasNoAvailability(new ArrayList<>());

				for(Application application : conflictingApplications){					
					ConflictingApplication conflictingApplication = new ConflictingApplication();
					conflictingApplication.setApplication(application);
					conflictingApplication.setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
					response.getConflictingApplications().add(conflictingApplication);
					

					
					
					// Determine what action needs to be taken on the conflicting application
					if(doesConflictingApplicationNeedToBeRemoved(application)){
						response.getConflictingApplicationsToBeRemoved_jobDoesNotAllowPartial().add(conflictingApplication);
					}else{								
						List<String> resolvedWorkDays = workDayService.removeConflictingWorkDays(
								request.getDatesToFindConflictWith(), workDays_toFindConflictsWith);
						if(resolvedWorkDays.size() == 0){
							response.getConflictingApplicationsToBeRemoved_applicantHasNoAvailability().add(conflictingApplication);
						}else if(doesConflictingApplicationNeedToBeModifiedAndSentBackToEmployer(application, session)){
							response.getConflictingApplicationsToBeSentBackToEmployer().add(conflictingApplication);
						}else{
							response.getConflictingApplicationsToBeModifiedButRemainAtEmployer().add(conflictingApplication);
						}						
					}
				}		
				response.setReferenceApplication(referenceApplication);
			}
		}	
		model.addAttribute("areConflictsCausedByCounteringWorkDays", true);
		model.addAttribute("response", response);		
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
		ratingService.deleteRatings(userId, jobId);
	}

	public String getTotalPayment(Proposal proposal) {

		// ***********************************************************
		// This assumes each work day starts and ends on the same calendar day
		// ***********************************************************

		double totalMinutes = 0;
		List<WorkDay> workDays = workDayService.getWorkDays_byProposalId(proposal.getProposalId());
		for( WorkDay workDay : workDays){
			totalMinutes += ChronoUnit.MINUTES.between(
					LocalTime.parse(workDay.getStringStartTime()),
					LocalTime.parse(workDay.getStringEndTime()));
		}
		return String.format("%.2f", (totalMinutes / 60) * Double.parseDouble(proposal.getAmount()));
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
	
	public  boolean includeApplication(Application application, Proposal currentProposal, JobSearchUser sessionUser) {
		
		/* Refactor this*/
		
		if(currentProposal.getIsDeclined() == 1 ){
			if(currentProposal.getProposedByUserId() == sessionUser.getUserId()
					&& currentProposal.getFlag_acknowledgedIsDeclined() == 0){
				return true;			
			}else{
				return false;		
			}
		}else{
			return true;
		}
	}

	public boolean includeApplication(Application application, Proposal currentProposal, JobSearchUser sessionUser,
			ApplicationProgressRequest request) {
		
		/* Refactor this*/
		
		boolean includeApplication = false;
		
		if(currentProposal.getIsDeclined() == 1 ){
			if(currentProposal.getProposedByUserId() == sessionUser.getUserId()
					&& currentProposal.getFlag_acknowledgedIsDeclined() == 0){
				includeApplication = true;			
			}
		}
		if(request.getShowProposalsWaitingOnYou() && currentProposal.getProposedToUserId() == sessionUser.getUserId()){
			includeApplication = true;		
		}		
		if(request.getShowProposalsWaitingOnOther() 
				&& currentProposal.getProposedToUserId() != sessionUser.getUserId()
				&& currentProposal.getFlag_hasExpired() == 0
				&& application.getIsAccepted() == 0){
			includeApplication = true;		
		}		
		if(request.getShowExpiredProposals()
				&& currentProposal.getFlag_hasExpired() == 1
				&& application.getIsAccepted() == 0){
			includeApplication = true;			
		}		
		if (request.getShowAcceptedProposals() && application.getIsAccepted() == 1){
			includeApplication = true;		
		}		
		if(request.getShowProposalsWaitingOnYou_new() && currentProposal.getProposedToUserId() == sessionUser.getUserId()
				&& currentProposal.getIsNew() == 1){
			includeApplication = true;
		}
		return includeApplication;
	}
}
