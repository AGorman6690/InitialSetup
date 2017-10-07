package com.jobsearch.service;

import java.time.LocalDate;
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
	
	// Proposals now have a flag for whether it has expired.
	@Deprecated
	public Boolean isProposalExpired(Proposal proposal){
		if(proposal.getExpirationDate() != null){
			if(ChronoUnit.MINUTES.between(LocalDateTime.now(), proposal.getExpirationDate()) < 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}
	
	public Proposal getCurrentProposal(Integer applicationId) {
		return repository.getCurrentProposal(applicationId);
	}

	public Proposal getPreviousProposal(int applicationId) {
		return repository.getPreviousProposal(applicationId);
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
	

	public void inspectNewness(Proposal proposal, JobSearchUser user) {
		if(proposal.getProposedToUserId() == user.getUserId() && proposal.getIsNew() == 1){
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

	public Boolean isProposedToUser(Proposal proposal, JobSearchUser user) {
		if(proposal.getFlag_hasExpired() == 1){
			if(userService.isEmployer(user)){
				return true;
			}else{
				return false;
			}
		}else if(proposal.getProposedToUserId() == user.getUserId()) return true;
		else return false ;
	}


	public void respondToProposal(RespondToProposalRequest request, HttpSession session) {

		Proposal proposalBeingRespondedTo = getCurrentProposal(request.getProposal().getApplicationId());
		JobSearchUser sessionUser = SessionContext.getUser(session);
		Job job = jobService.getJob_ByApplicationId(request.getProposal().getApplicationId());
		
		Proposal newProposal = new Proposal(proposalBeingRespondedTo);
		newProposal.setAmount(request.getProposal().getAmount());
		newProposal.setProposedDates(request.getProposal().getProposedDates());
		setAcceptedAndExpirationDates(newProposal, request);
		
		// validate
		boolean valid = true;
		if(proposalBeingRespondedTo.getIsCurrentProposal() == 0){
			valid = false;
		}
		else if(proposalBeingRespondedTo.getIsDeclined() == 1){
			valid = false;
		}
		else if(proposalBeingRespondedTo.getFlag_hasExpired() == 1){
			if(!userService.isEmployer(sessionUser)){
				valid = false;
			}
		}else if(!isProposedToUser(proposalBeingRespondedTo, sessionUser)){
			valid = false;
		}else if(!isValidProposal(newProposal, job)){
			valid = false;
		}		

		if(valid){			
			boolean isAcceptingOffer = getIsAcceptingProposal(proposalBeingRespondedTo, request.getProposal(), job);
			if(userService.isEmployer(sessionUser)){		
				
				insertProposal(newProposal, proposalBeingRespondedTo, job);					
				if(proposalBeingRespondedTo.getFlag_hasExpired() == 0 && isAcceptingOffer){		
					Proposal employerProposal = getCurrentProposal(newProposal.getApplicationId());
					updateProposalFlag(employerProposal, Proposal.FLAG_EMPLOYER_ACCEPTED_THE_OFFER, 1);		
				}					
			}else{
				if(isAcceptingOffer){							
					approveProposal_byEmployee(proposalBeingRespondedTo, session);	
				}else{									
					counterProposal_byEmployee(proposalBeingRespondedTo, request.getProposal(),
							 sessionUser);
				}				
			}			
			applicationService.inspectNewness(applicationService.getApplication(proposalBeingRespondedTo.getApplicationId()));
			inspectNewness(proposalBeingRespondedTo, sessionUser);
		}
	}

	
	private boolean isValidProposal(Proposal proposal, Job job) {
		
		boolean valid = true;
		
		// Proposed amount
		if (!verificationService.isPositiveNumber(proposal.getAmount())){
			valid = false; 
		}
		
		// Proposed dates		
		if (job.getIsPartialAvailabilityAllowed()){
			boolean atLeastOneWorkDayProposed = false;
			List<WorkDay> workDays = workDayService.getWorkDays(job.getId());
			for (WorkDay workDay : workDays){
				for (String dateString : proposal.getProposedDates()){
					if(LocalDate.parse(dateString).equals(LocalDate.parse(workDay.getStringDate().replace("/", "-")))){
						atLeastOneWorkDayProposed = true;
						break;
					}
				}
				if (atLeastOneWorkDayProposed){
					break;
				}
			}
			if (!atLeastOneWorkDayProposed){
				valid = false;
			}			
		}
		
		// Expiration time
		if(userService.isEmployer(proposal.getProposedByUserId())){
			if(proposal.getEmployerAcceptedDate() == null || proposal.getExpirationDate() == null){
				valid = false;
			}else if(!DateUtility.isValidFutrueDate(proposal.getEmployerAcceptedDate(), proposal.getExpirationDate())){
				valid = false;
			}
		}
				
		return valid;
	}

	private boolean getIsAcceptingProposal(Proposal proposalBeingRespondedTo,
			Proposal newProposal, Job job) {
	
		boolean isAcceptingOffer = true;
		if(!proposalBeingRespondedTo.getAmount().matches(newProposal.getAmount()))
			isAcceptingOffer = false;
		if(job.getIsPartialAvailabilityAllowed()){
			if(proposalBeingRespondedTo.getProposedDates().size() !=
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
		}

		return isAcceptingOffer;
	}
	
	public Proposal getNewProposalFromEmployer(RespondToProposalRequest request){
		 return getNewProposalFromEmployer(request, null);
	}
		
	public Proposal getNewProposalFromEmployer(RespondToProposalRequest request, Proposal proposalBeingRespondedTo) {
		
		LocalDateTime acceptedDate = LocalDateTime.now();
		LocalDateTime expirationDate = DateUtility.getFutureDate(
				acceptedDate, request.getDays_offerExpires(),
				request.getHours_offerExpires(), request.getMinutes_offerExpires());
		
		Proposal newProposal = new Proposal();			
		if(request.getIsRespondingToAnExpiredProposal() != null && request.getIsRespondingToAnExpiredProposal()){
			newProposal.setProposedByUserId(proposalBeingRespondedTo.getProposedByUserId());
			newProposal.setProposedToUserId(proposalBeingRespondedTo.getProposedToUserId());
		}else{
			newProposal.setProposedByUserId(proposalBeingRespondedTo.getProposedToUserId());
			newProposal.setProposedToUserId(proposalBeingRespondedTo.getProposedByUserId());
		}
		
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
	
	public void declineProposal(int proposalId, HttpSession session) {
		
		JobSearchUser sessionUser = SessionContext.getUser(session);
		Proposal proposal = getProposal(proposalId);
		
		boolean valid = true;
		if(proposal.getIsCurrentProposal() == 0){
			valid = false;
		}
		if(!isProposedToUser(proposal, sessionUser)){
			valid = false;
		}
		
		if(valid){			
			applicationService.updateApplicationStatus(proposal.getApplicationId(),
					Application.STATUS_DECLINED);
			applicationService.closeApplication(proposal.getApplicationId());
			updateProposalFlag(proposal, "IsDeclined", 1);			
		}
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

			applicationService.closeApplication(proposalBeingRespondedTo.getApplicationId());

			userService.insertEmployment(proposalBeingRespondedTo.getProposedToUserId(), job.getId());
			
			updateProposalFlag(proposalBeingRespondedTo, "IsNew", 0);

			// Update the job after the employment record has been inserted.
			// The "IsStillAcceptingApplications" property needs to be updated.
			job = jobService.getJob(job.getId());

			applicationService.resolveApplicationConflicts_withinApplicationsForUser(session,
					proposalBeingRespondedTo.getApplicationId(),
					workDayService.getWorkDays_byProposalId(
							proposalBeingRespondedTo.getProposalId()));
			
			applicationService.updateApplicationStatus(proposalBeingRespondedTo.getApplicationId(),
					Application.STATUS_ACCEPTED);

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
	
	public void insertProposal(Proposal newProposal) {
		Proposal currentProposal = getCurrentProposal(newProposal.getApplicationId());
		Job job = jobService.getJob_ByApplicationId(newProposal.getApplicationId());
		insertProposal(newProposal, currentProposal, job);
	}
	
	public void insertProposal(Proposal newProposal, Proposal currentProposal, Job job) {	
		
		// *********************************************************
		// *********************************************************
		// The proposal verification is handled before this is called.
		// If a given command (i.e. apply for job) makes an insert into more than one table,
		// then all inserts need to be validated before any are made.
		// *********************************************************
		// *********************************************************
		
		if(currentProposal != null){
			updateProposalFlag(currentProposal, "IsCurrentProposal", 0);
		}		
		
		// ****************************************************
		// Design note: If a job does not allow partial, it might seem unnecessary to insert all
		// the job's work days into the proposal work day table (i.e. there  can never be a subset
		// of work days), however in order to determine whether
		// an employee's employment is still active, all the job's work day's "IsComplete" field
		// is inspected and joined with the proposal work day table. Thus, EVERY proposal needs
		// to have days in the proposal work days table.
		// An alternative is to inspect the job's status field, if the job does not allow partial,
		// to determine if an employee's job is complete (i.e. if a job does not allow partial and
		// the job's status is complete, then the employee's employment is complete).
		// If performance is an issue, we can address this later.
		// ****************************************************

		// Set date strings, if necessary			
		if(!job.getIsPartialAvailabilityAllowed()){
			newProposal.setProposedDates(workDayService.getWorkDayDateStrings(job.getId()));
		}
		
		repository.insertProposal(newProposal);
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
			response.setJobWorkDayCount(workDays.size());
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

	public String getProposalLabel(Proposal proposal, JobSearchUser sessionUser) {
		if(proposal != null){
			if(proposal.getFlag_hasExpired() == 1){
				if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE){
					return "Employer's expired offer";
				}else{
					return "Your expired offer";
				}
			}else if(!isProposedToUser(proposal, sessionUser)){
				return "Your current offer";
			}else if(sessionUser.getProfileId() == Profile.PROFILE_ID_EMPLOYEE){
				return "The employer's current offer";			
			}else{
				return "The applicant's current offer";
			}
		}else{
			return null;
		}
	}

	public void setAcceptedAndExpirationDates(Proposal proposal, RespondToProposalRequest request) {
		
		if(request.getDays_offerExpires() != null || request.getHours_offerExpires() != null
				|| request.getMinutes_offerExpires() != null){
			
			LocalDateTime acceptedDate = LocalDateTime.now();
			LocalDateTime expirationDate = DateUtility.getFutureDate(
					acceptedDate, request.getDays_offerExpires(),
					request.getHours_offerExpires(), request.getMinutes_offerExpires());
			
			proposal.setEmployerAcceptedDate(acceptedDate);
			proposal.setExpirationDate(expirationDate);
		}

	}

	public void insertProposal(Proposal newProposal, Proposal currentProposal) {
		Job job = jobService.getJob_ByApplicationId(newProposal.getApplicationId());
		insertProposal(newProposal, currentProposal, job);
	}

	
}
