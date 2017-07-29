package com.jobsearch.proposal.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.Profile;
import com.jobsearch.model.Proposal;
import com.jobsearch.proposal.repository.ProposalRepository;

@Service
public class ProposalServiceImpl {
	
	@Autowired
	ProposalRepository repository;

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
			repository.updateFlag(proposal.getEmploymentProposalId(), "IsNew", 0);
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

	
	
}
