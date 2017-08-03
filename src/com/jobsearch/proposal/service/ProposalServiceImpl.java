package com.jobsearch.proposal.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.proposal.repository.ProposalRepository;
import com.jobsearch.request.RespondToProposalRequest;
import com.jobsearch.responses.CurrentProposalResponse;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.service.UserServiceImpl;
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
	CategoryServiceImpl categoryService;
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
	
	public Proposal getCurrentProposal(Integer applicationId) {
		return repository.getCurrentProposal(applicationId);
	}

	public Proposal getPreviousProposal(Integer referenceEmploymentProposalId, int applicationId) {
		return repository.getPreviousProposal(referenceEmploymentProposalId, applicationId);
	}

	public List<String> getProposedDates(Integer proposalId) {
		return repository.getProposedDates(proposalId);
	}
	
	
	public String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate, LocalDateTime now) {
		// *************************************************************
		// *************************************************************
		// Refactor: make this a date utility
		// *************************************************************
		// *************************************************************

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
			RespondToProposalRequest request,
			Boolean isAcceptingOffer) {
		
		LocalDateTime acceptedDate = LocalDateTime.now();
		LocalDateTime expirationDate = DateUtility.getFutureDate(
				acceptedDate, request.getDays_offerExpires(),
				request.getHours_offerExpires(), request.getMinutes_offerExpires());

		
//		if(	DateUtility.isValidFutrueDate(acceptedDate,
//						newProposal.getExpirationDate()) ){

			Proposal newProposal = new Proposal(proposalBeingRespondedTo);	
			newProposal.setEmployerAcceptedDate(acceptedDate);
			newProposal.setExpirationDate(expirationDate);
			newProposal.setProposedDates(request.getProposal().getProposedDates());
			newProposal.setAmount(request.getProposal().getAmount());
			
			insertProposal(newProposal);			
			
			if(isAcceptingOffer){				
				updateProposalFlag(newProposal,
						EmploymentProposalDTO.FLAG_EMPLOYER_ACCEPTED_THE_OFFER, 1);		
//				updateWageProposalStatus(proposalBeingRespondedTo.getEmploymentProposalId(),
//					WageProposal.STATUS_ACCEPTED);
			}
//		}
	}
	
	public void updateProposalFlag(Proposal proposal, String proposalFlag, int value) {
		repository.updateProposalFlag(proposal.getProposalId(),
				proposalFlag, value);
	}
	
	// **************************************************
	// **************************************************
	// Delete this 
	// **************************************************
	// **************************************************
	public void updateProposalFlag(EmploymentProposalDTO currentProposal,
			String proposalFlag, int value) {

		repository.updateProposalFlag(currentProposal.getEmploymentProposalId(),
				proposalFlag, value);
	}
	
	public void declineProposal(EmploymentProposalDTO proposalBeingRespondedTo) {
		
		applicationService.updateApplicationStatus(proposalBeingRespondedTo.getApplicationId(),
				EmploymentProposalDTO.STATUS_DECLINED);

		Application application = applicationService.getApplication(proposalBeingRespondedTo.getApplicationId());
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
		if(DateUtility.isValidFutrueDate(proposalBeingRespondedTo.getExpirationDate(), now) &&
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
					jobService.getWorkDays_byProposalId(
							proposalBeingRespondedTo.getProposalId()));

		}	
		
	}
	
	public void insertProsalWorkDays(Proposal proposal) {

		if(verificationService.isListPopulated(proposal.getProposedDates())){
			int jobId = jobService.getJob_ByApplicationId(proposal.getApplicationId()).getId();

			for(String dateString : proposal.getProposedDates()){
				int dateId = jobService.getDateId(dateString);
				int workDayId = jobService.getWorkDayId(jobId, dateId);
				repository.insertProsalWorkDay(proposal.getProposalId(), workDayId);
			}
		}
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
				proposal.setProposedDates(
										jobService.getWorkDayDateStrings(job.getId()));
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
			List<WorkDay> workDays = jobService.getWorkDays(job.getId()) ;
			
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
