package com.jobsearch.utilities;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.session.SessionContext;
import com.jobsearch.user.repository.UserRepository;

// Not sure 
@Service
public class VerificationServiceImpl {
	
	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	ApplicationServiceImpl applicationService;	
	

	public boolean isPositiveNumber(Integer number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	public boolean isPositiveNumber(Double number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	
	public boolean isPositiveNumber(String number) {
		
		if(number != null){
			Double number_fromString = Double.valueOf(number);
			if(number_fromString == null || number_fromString <= 0) return false;
			else return true;
		}else return false;
	}

	

	public boolean didSessionUserPostJob(HttpSession session, int jobId) {

		Job job = jobService.getJob(jobId);
		
		return this.didSessionUserPostJob(session, job);

	}

	
	public boolean didSessionUserPostJob(HttpSession session, Job job) {
		
		if(job != null  && 
				job.getUserId() == SessionContext.getUser(session).getUserId()) return true;
		
		else return false;
	}

	public <T> boolean isListPopulated(List<T> list) {
		
		if(list != null && list.size() > 0) return true;
		else return false;
	}

	public boolean isValidLocation(Job job) {		
		
		if((job.getStreetAddress() == null || job.getStreetAddress().matches("")) && 
				(job.getCity() == null || job.getCity().matches("")) && 
				(job.getState() == null || job.getState().matches("")) && 
				(job.getZipCode() == null || job.getZipCode().matches(""))){
			return false;
		}
		else return true;
	
	}

	public boolean didUserApplyForJob(int jobId, int userId) {

		
		Application application = applicationService.getApplication(jobId, userId);
		
		if(application != null) return true;
		else return false;
	}

	public boolean canSessionUserAcceptEmploymentProposal(HttpSession session,
							EmploymentProposalDTO employmentProposalDto) {

		
		if(isProposalCurrentlyProposedToSessionUser(employmentProposalDto.getEmploymentProposalId(), session) &&
				isEmploymentProposalOpen(employmentProposalDto) ){
			return true;
		}else return false;
		
	}
		
	

	public boolean isProposalCurrentlyProposedToSessionUser(int wageProposalId, HttpSession session) {
		
		JobSearchUser user = SessionContext.getUser(session);
		WageProposal wp = applicationService.getWageProposal(wageProposalId);
		
		// ********************************
		// I cannot figure out why I had the hasActionBeenTakenOnWageProposal() condition...
		// ********************************
		// Verify the wage proposal has not yet been acted upon
//		if(!this.hasActionBeenTakenOnWageProposal(wp)){
			
			if(wp.getProposedToUserId() == user.getUserId()) return true;
			else return false;
//		}
//		else return false;
	}
	
	public boolean isEmploymentProposalOpen(EmploymentProposalDTO employmentProposalDto) {
		
		if( employmentProposalDto.getStatus() == WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED ||
				employmentProposalDto.getStatus() == WageProposal.STATUS_VIEWED_BUT_NO_ACTION_TAKEN ||
						employmentProposalDto.getStatus() == WageProposal.STATUS_COUNTERED ||
								employmentProposalDto.getStatus() == WageProposal.STATUS_PENDING_APPLICANT_APPROVAL){
			
			return true;
		} else return false;
	}
	

	public boolean isWageProposalCurrentlyPendingApplicantsApproval(int wageProposalId, HttpSession session) {
		
		JobSearchUser user = SessionContext.getUser(session);
		WageProposal wp = applicationService.getWageProposal(wageProposalId);
		
		// This is a somewhat counter intuitive.
		// Because a new wage proposal is NOT created when the employer 
		// accepts the applicant's proposal (and and expiration date is set),
		// but rather the status of the wage proposal sent BY the applicant is changed,
		// the wage proposal's "ProposedByUserId" will still be the
		// applicant's (i.e. the session user) user id.
		if(wp.getStatus() == WageProposal.STATUS_PENDING_APPLICANT_APPROVAL &&
				wp.getProposedByUserId() == user.getUserId()){
			
			return true;

		}
		else return false;
	}



	
}
