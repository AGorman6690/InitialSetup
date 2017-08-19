package com.jobsearch.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.model.Application;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.model.Job;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.repository.ProposalRepository;
import com.jobsearch.request.RespondToProposalRequest;
import com.jobsearch.responses.CurrentProposalResponse;
import com.jobsearch.session.SessionContext;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.NumberUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class ProposalServiceImpl{
	
	@Autowired
	ProposalRepository repository;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	VerificationServiceImpl verificationService;
	@Autowired
	GoogleClient googleClient;
	@Autowired
	RatingServiceImpl ratingService;
	
	public List<Proposal> getCurrentProposals_byJob(Integer jobId) {
		return repository.getCurrentProposals_byJob(jobId);
	}
	
	public String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate) {
		
		LocalDateTime now = LocalDateTime.now();
		return getTime_untilEmployerApprovalExpires(expirationDate, now);

	}
	
	public void deleteProposedWorkDays(List<WorkDay> workDays, int applicationId) {
		repository.deleteProposedWorkDays(workDays, applicationId);
	}
	
	public void deleteProposedWorkDays(List<WorkDay> workDays) {
		repository.deleteProposedWorkDays(workDays);
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
	
	public Proposal getCurrentProposal(Integer applicationId) {
		return repository.getCurrentProposal(applicationId);
	}

	public Proposal getPreviousProposal(Integer referenceEmploymentProposalId, int applicationId) {
		return repository.getPreviousProposal(referenceEmploymentProposalId, applicationId);
	}

	public List<String> getProposedDates(Integer proposalId) {
		return repository.getProposedDates(proposalId);
	}
	
	public List<WorkDayDto> getWorkDayDtos_proposedWorkDays(int applicationId, HttpSession session) {

		JobSearchUser sessionUser =  SessionContext.getUser(session);
		if( ( sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE &&
				verificationService.didUserApplyForJob(jobService.getJob_ByApplicationId(applicationId).getId(),
														sessionUser.getUserId() ) )

			||

			( sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER &&
			verificationService.didSessionUserPostJob(session, jobService.getJob_ByApplicationId(applicationId)) ) ){

			return workDayService.getWorkDayDtos_byProposal(getCurrentProposal(applicationId));
		}else return null;
	}
	
	public List<WorkDayDto> getWorkDayDtos_proposedWorkDays(int jobId, int userId, HttpSession session) {

		Application application = applicationService.getApplication(jobId, userId);
		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();
		if(application != null){
			workDayDtos = getWorkDayDtos_proposedWorkDays(application.getApplicationId(), session);
		}
		return workDayDtos;
	}
	

	public void inspectNewness(Proposal proposal) {
		if(proposal.getIsNew() == 1){
			repository.updateFlag(proposal.getProposalId(), "IsNew", 0);
		}			
	}
	
	public String getCurrentProposalStatus(int application_isOpen, int application_isAccepted,			
			Integer proposedByUserId_currentProposal, int userId_setStatusFor, int profileId_forUser) {
		
		
		if(application_isAccepted == 1){			
			if(profileId_forUser == Profile.PROFILE_ID_EMPLOYEE)
				return "";
			else return "";			
		}else if(application_isOpen == 0){
			return "Application is closed";
		}else{
			if(proposedByUserId_currentProposal == userId_setStatusFor){
				if(profileId_forUser == Profile.PROFILE_ID_EMPLOYEE)
					return "Waiting for the employer";
				else return "Waiting for the applicant";						
			}else{
				return "Waiting for you";
			}
		}

	}

	public Boolean isProposedToUser(Proposal proposal, int userId) {
		if(proposal.getProposedToUserId() == userId) return true;
		else return false ;
	}


	public void respondToProposal(RespondToProposalRequest request, HttpSession session) {

		Proposal proposalBeingRespondedTo = getCurrentProposal(
							request.getProposal().getApplicationId());

		JobSearchUser sessionUser = SessionContext.getUser(session);
		
		
		boolean isAcceptingOffer = getIsAcceptingProposal(proposalBeingRespondedTo, request.getProposal());

		if(proposalBeingRespondedTo.getProposedToUserId() == SessionContext.getUser(session).getUserId() &&
				proposalBeingRespondedTo.getIsDeclined() == 0){

			if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYER){		
				respondToProposal_byEmployer(proposalBeingRespondedTo, request, isAcceptingOffer);
			}else{
				if(isAcceptingOffer){							
					approveProposal_byEmployee(proposalBeingRespondedTo, session);	
				}else{									
					counterProposal_byEmployee(proposalBeingRespondedTo, request.getProposal(),
							 sessionUser);
				}				
			}
		}
	}
	
	private boolean getIsAcceptingProposal(Proposal proposalBeingRespondedTo,
			Proposal newProposal) {
	
		boolean isAcceptingOffer = true;
		if(!proposalBeingRespondedTo.getAmount().matches(newProposal.getAmount()))
			isAcceptingOffer = false;
		else if(proposalBeingRespondedTo.getProposedDates().size() !=
				newProposal.getProposedDates().size())			
			isAcceptingOffer = false;
		else{
			for(String dateString : newProposal.getProposedDates()){
				if(proposalBeingRespondedTo.getProposedDates()
						.stream()
						.filter(ds -> ds.matches(dateString))
						.count() == 0){
					isAcceptingOffer = false;
					break;
				}
			}
		}
		return isAcceptingOffer;
	}
	

	private void respondToProposal_byEmployer(Proposal proposalBeingRespondedTo,			
		RespondToProposalRequest request, Boolean isAcceptingOffer) {

		Proposal newProposal = getNewProposalFromEmployer(request, proposalBeingRespondedTo);			
		insertProposal(newProposal);			
		
		if(isAcceptingOffer){				
			updateProposalFlag(newProposal,
					Proposal.FLAG_EMPLOYER_ACCEPTED_THE_OFFER, 1);		
		}

	}
	
	public Proposal getNewProposalFromEmployer(RespondToProposalRequest request){
		 return getNewProposalFromEmployer(request, null);
	}
		
	public Proposal getNewProposalFromEmployer(RespondToProposalRequest request, Proposal proposalBeingRespondedTo) {
		
		LocalDateTime acceptedDate = LocalDateTime.now();
		LocalDateTime expirationDate = DateUtility.getFutureDate(
				acceptedDate, request.getDays_offerExpires(),
				request.getHours_offerExpires(), request.getMinutes_offerExpires());
		
		// **********************************************
		// Refactor: Is this constructor all that useful??????
		// Find a better way to initialize these properties
		Proposal newProposal = new Proposal();	
		if(proposalBeingRespondedTo != null){
			newProposal = new Proposal(proposalBeingRespondedTo);	
		}
		// **********************************************	
		
		newProposal.setEmployerAcceptedDate(acceptedDate);
		newProposal.setExpirationDate(expirationDate);
		newProposal.setProposedDates(request.getProposal().getProposedDates());
		newProposal.setAmount(request.getProposal().getAmount());		
		newProposal.setApplicationId(request.getProposal().getApplicationId());
		return newProposal;
	}

	public void updateProposalFlag(Proposal proposal, String proposalFlag, int value) {
		repository.updateProposalFlag(proposal.getProposalId(),
				proposalFlag, value);
	}
	
	
	public void declineProposal(Proposal proposal) {
		
		applicationService.updateApplicationStatus(proposal.getApplicationId(),
				Application.STATUS_DECLINED);

		Application application = applicationService.getApplication(proposal.getApplicationId());
		applicationService.updateApplicationStatus(application.getApplicationId(),
			Application.STATUS_DECLINED);
	
	}

	private void counterProposal_byEmployee(Proposal proposalBeingRespondedTo,
			Proposal counterProposal, JobSearchUser sessionUser) {

		
		LocalDateTime now = LocalDateTime.now();		
		if(DateUtility.isValidFutrueDate(now, proposalBeingRespondedTo.getExpirationDate())){
			
			Proposal newProposal = new Proposal(proposalBeingRespondedTo);
			
			newProposal.setProposedDates(counterProposal.getProposedDates());
			newProposal.setAmount(counterProposal.getAmount());

			insertProposal(newProposal);
		}
	}


	private void approveProposal_byEmployee(Proposal proposalBeingRespondedTo,
			HttpSession session) {
	
		// ******************************************************
		// Checking whether amount-hired is less than positions-to-be-filled-per-day is not adequate.
		// Since queries are asynchronous, this condition can be met by two different requests,
		// while there is only one position left to fill.
		// This needs to be addressed.
		// _________________________________________________________

		// Update: to verify each proposed work day has a position available,
		// the "counts" are being compared. Essentially, we need to handle the situation
		// where multiple, simultaneous requests for the same position are submitted.
		// Do we process each request, include a time stamp, then do a post-insert query
		// to ensure the work day's position's-per-day is not exceeded??? If it is exceeded,
		// then delete the necessary records starting with the newest time stamps.
		// ******************************************************
		// ******************************************************
		
		Job job = jobService.getJob_ByApplicationId(proposalBeingRespondedTo.getApplicationId());
		
		LocalDateTime now = LocalDateTime.now();		
		if(DateUtility.isValidFutrueDate(now, proposalBeingRespondedTo.getExpirationDate()) &&
			workDayService.isAPositionAvaiableEachProposedWorkDay(
						proposalBeingRespondedTo.getProposalId(), job.getId())) {


			applicationService.updateApplicationStatus(proposalBeingRespondedTo.getApplicationId(),
										Application.STATUS_ACCEPTED);

			applicationService.closeApplication(proposalBeingRespondedTo.getApplicationId());

			userService.insertEmployment(proposalBeingRespondedTo.getProposedToUserId(), job.getId());
			
			updateProposalFlag(proposalBeingRespondedTo, "IsNew", 0);

			// Update the job after the employment record has been inserted.
			// The "IsStillAcceptingApplications" property needs to be updated.
			job = jobService.getJob(job.getId());

			applicationService.resolveApplicationConflicts_withinApplicationsForAJob(job,
					proposalBeingRespondedTo);

			applicationService.resolveApplicationConflicts_withinApplicationsForUser(session,
					proposalBeingRespondedTo.getApplicationId(),
					workDayService.getWorkDays_byProposalId(
							proposalBeingRespondedTo.getProposalId()));

		}	
		
	}
	
	public void insertProsalWorkDays(Proposal proposal) {

		if(verificationService.isListPopulated(proposal.getProposedDates())){
			int jobId = jobService.getJob_ByApplicationId(proposal.getApplicationId()).getId();

			for(String dateString : proposal.getProposedDates()){
				int dateId = jobService.getDateId(dateString);
				int workDayId = workDayService.getWorkDayId(jobId, dateId);
				repository.insertProsalWorkDay(proposal.getProposalId(), workDayId);
			}
		}
	}
	
	public Boolean getIsProposedToSessionUser(HttpSession session, Proposal proposal) {
		return SessionContext.getUser(session).getUserId() == proposal.getProposedToUserId();
	}
	
	public void insertProposal(Proposal proposal) {

		// *********************************************************
		// *********************************************************
		// Verify the work day proposal is valid. i.e. verify at least one of the
		// proposed date strings is a work day for the particular job.
		// *********************************************************
		// *********************************************************
		
		if(proposal.getProposedDates().size() > 0 &&
				NumberUtility.isPositiveNumber(proposal.getAmount())){
		
			Proposal currentProposal = getCurrentProposal(proposal.getApplicationId());
			if(currentProposal != null){
				updateProposalFlag(currentProposal, "IsCurrentProposal", 0);
			}
				
			// Set date strings, if necessary
			Job job = jobService.getJob_ByApplicationId(proposal.getApplicationId());		
			if(!job.getIsPartialAvailabilityAllowed()){
				proposal.setProposedDates(workDayService.getWorkDayDateStrings(job.getId()));
			}
			
			repository.insertProposal(proposal);
		}
	}
	
	public Proposal getProposal(int proposalId) {
		return repository.getProposal(proposalId);
	}

	public void setCurrentProposalResponse(Model model, HttpSession session, int proposalId) {
		// **************************************
		// **************************************
		// Need verification:
		// 1) is current proposal?
		// 2) is proposed to session user?
		// 3) is not expired (if employee)
		// **************************************
		// **************************************		
//		if(verificationService.isCurrentProposalProposedToSessionUser(applicationId, session)){

			JobSearchUser sessionUser = SessionContext.getUser(session);			
			Proposal proposal = getProposal(proposalId);			
			Job job = jobService.getJob_ByApplicationId(proposal.getApplicationId());			
			List<WorkDay> workDays = workDayService.getWorkDays(job.getId()) ;
			
			CurrentProposalResponse response = new CurrentProposalResponse();
			response.setJob(job);
			response.setCurrentProposal(proposal);
//			response.setJobWorkDays(workDays);
			response.setDate_firstWorkDay(DateUtility.getMinimumDate(workDays).toString());
			response.setMonthSpan_allWorkDays(DateUtility.getMonthSpan(workDays));
			response.setTime_untilEmployerApprovalExpires(
					getTime_untilEmployerApprovalExpires(proposal.getExpirationDate(), LocalDateTime.now()));
			response.setTimeUntilStart(DateUtility.getTimeInBetween(
					LocalDateTime.now(),
					workDays.get(0).getStringDate(),
					workDays.get(0).getStringStartTime()));
			
			model.addAttribute("response", response);
			model.addAttribute("json_workDayDtos", 
					JSON.stringify(workDayService.getWorkDayDtos_byProposal(proposal)));
			model.addAttribute("user", sessionUser);
			model.addAttribute("isEmployerMakingFirstOffer", false);
		}
//	}	

	
}
