package com.jobsearch.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.model.Application;
import com.jobsearch.model.Job;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.repository.WorkDayRepository;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class WorkDayServiceImpl {
	
	@Autowired
	WorkDayRepository repository;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	VerificationServiceImpl verificationService;
	@Autowired
	ProposalServiceImpl proposalService;
	
	public List<WorkDay> getWorkDays_byProposalId(Integer employmentProposalId) {
		return repository.getWorkDays_byProposalId(employmentProposalId);
	}
	
	public boolean isAPositionAvaiableEachProposedWorkDay(Integer employmentProposalId, int jobId) {
				
		List<WorkDayDto> workDayDtos_jobWorkDay = getWorkDayDtos(jobId);
		List<WorkDay> workDays_proposed = getWorkDays_byProposalId(employmentProposalId);
		
		for( WorkDay workDay_proposed : workDays_proposed){			
			for(WorkDayDto workDayDto_jobWorkDay : workDayDtos_jobWorkDay){
				if(workDay_proposed.getWorkDayId() ==
						workDayDto_jobWorkDay.getWorkDay().getWorkDayId()){
					if(workDayDto_jobWorkDay.getCount_positionsFilled() ==
							workDayDto_jobWorkDay.getCount_totalPositions())
						return false;
				}
			}			
		}
		
		return true;
	}
	
	public Integer getWorkDayId(int jobId, int dateId) {
		return repository.getWorkDayId(jobId, dateId);
	}
	
	public List<WorkDay> getWorkDays_incomplete(Integer jobId) {
		return repository.getWorkDays_incomplete(jobId);
	}
	
	public List<WorkDay> getWorkDays_byJobAndDateStrings(int jobId, List<String> dateStrings) {
		if(verificationService.isListPopulated(dateStrings)){
			return repository.getWorkDays_byJobAndDateStrings(jobId, dateStrings);
		}else return null;
	}
	
	public void deleteWorkDays(int jobId, List<WorkDay> workDays_toDelete) {
		
		
		if(verificationService.isListPopulated(workDays_toDelete)){

			Job job = jobService.getJob(jobId);

//			List<WorkDay> workDays_toDelete = getWorkDays_byJobAndDateStrings(jobId, dateStrings_toDelete);
			
			// Affected applications that are open (not accepted)
			List<Application> affectedApplications =
					applicationService.getApplications_byJobAndAtLeastOneWorkDay(
							jobId, workDays_toDelete);

			for(Application affectedApplication : affectedApplications){
				Proposal currentProposal = proposalService.getCurrentProposal(
						affectedApplication.getApplicationId());

				proposalService.updateProposalFlag(currentProposal,
						Proposal.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);

			}

			// Affected employment
			List<Application> affectedApplications_employees = applicationService.
					getAcceptedApplications_byJobAndAtLeastOneWorkDay(jobId, workDays_toDelete);

			for(Application application : affectedApplications_employees){

				Proposal acceptedProposal = proposalService.getCurrentProposal(application.getApplicationId());

				// Update flags
				proposalService.updateProposalFlag(acceptedProposal,
						Proposal.FLAG_APPLICATION_WAS_REOPENED, 1);

//				applicationService.updateProposalFlag(acceptedProposal,
//						EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);

				// Insert new proposal
				// The proposal will be in the employer's inbox
				Proposal newProposal = new Proposal();
				newProposal.setApplicationId(application.getApplicationId());
				newProposal.setAmount(acceptedProposal.getAmount());
				newProposal.setProposedByUserId(application.getUserId());
				newProposal.setProposedToUserId(job.getUserId());
				newProposal.setProposedDates(
						removeConflictingWorkDays(acceptedProposal.getProposedDates(),
								workDays_toDelete));
							
				proposalService.insertProposal(newProposal);
				
				// Set flags for the new proposal
				newProposal = proposalService.getCurrentProposal(application.getApplicationId());
				proposalService.updateProposalFlag(newProposal, Proposal.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED, 1);

				// Undo this employment record
				applicationService.openApplication(application.getApplicationId());
				applicationService.updateApplicationFlag(application, "IsAccepted", 0);
				applicationService.deleteEmployment(application.getUserId(), application.getJobId());
			}

			proposalService.deleteProposedWorkDays(workDays_toDelete);
			repository.deleteWorkDays(workDays_toDelete);
			
//			updateJobFlag(jobId, Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS, 0);
		}
	}
	
	public List<String> removeConflictingWorkDays(List<String> dateStrings_toInspect,
			List<WorkDay> workDays_toInspectAgainst) {

		List<String> dateStrings_conflictsRemoved = new ArrayList<String>();
		
		for(String dateString_toInspect : dateStrings_toInspect){
		Integer dateId_toInspect = jobService.getDateId(dateString_toInspect);
		
		Boolean isConflicting = false;
		for(WorkDay workDay_toInspectAgainst : workDays_toInspectAgainst){
			if(workDay_toInspectAgainst.getDateId() == dateId_toInspect) {
				isConflicting = true;
				break;
			}
		}
		
		if(!isConflicting) dateStrings_conflictsRemoved.add(dateString_toInspect);
		
		}
		
		return dateStrings_conflictsRemoved;
	}	
		

	public Boolean areAllTimesTheSame(List<WorkDay> workDays) {

		if(verificationService.isListPopulated(workDays)){
			if(workDays.size() > 1){

				LocalTime a_startTime =  LocalTime.parse(workDays.get(0).getStringStartTime());
				LocalTime a_endTime =  LocalTime.parse(workDays.get(0).getStringEndTime());
				for( WorkDay workDay : workDays){
					if(!LocalTime.parse(workDay.getStringStartTime()).equals(a_startTime)){
						return false;
					}
					if(!LocalTime.parse(workDay.getStringEndTime()).equals(a_endTime)){
						return false;
					}
				}

				return true;
			}else return true;
		}else return null;
	}
	
	public void setWorkDayAsComplete(int workDayId) {
		repository.setWorkDayAsComplete(workDayId);

	}
	
	public boolean areValidWorkDays(List<WorkDay> workDays) {

		if(workDays == null || workDays.size() == 0){
			return false;
		}else{

			// Validate the string date and times can be parsed to
			// LocalDate and LocalTime objects
			for(WorkDay workDay : workDays){
				try {
					LocalDate.parse(workDay.getStringDate());
					LocalTime startTime = LocalTime.parse(workDay.getStringStartTime());
					LocalTime endTime = LocalTime.parse(workDay.getStringEndTime());
					
					if(endTime.isBefore(startTime)) return false;
					
				} catch (Exception e) {
					return false;
				}

			}
		}
		return true;
	}
	
	public void addWorkDays(int jobId, List<WorkDay> workDays) {

		// **************************************************************
		// **************************************************************
		// In order to alert all applicants of this job,
		// a flag on the job should be set
		// **************************************************************
		// **************************************************************

		for(WorkDay workDay : workDays){
			
			LocalDateTime localDateTime_endDate = LocalDateTime.of(
					LocalDate.parse(workDay.getStringDate()),
					LocalTime.parse(workDay.getStringEndTime()));
			
			workDay.setDate(LocalDate.parse(workDay.getStringDate()));
			workDay.setDateId(jobService.getDateId(workDay.getDate().toString()));
			workDay.setTimestamp_endDate(Timestamp.valueOf(localDateTime_endDate));
			repository.addWorkDay(jobId, workDay);
		}
	}
	
	public void updateWorkDayTimes(int jobId, List<WorkDay> workDays) {

		//*************************************************************************
		//*************************************************************************
		// Need to check if the job's current employment is affected by the time change
		//*************************************************************************
		//*************************************************************************

		if(verificationService.isListPopulated(workDays)){

			List<Application> affectedApplications =
					applicationService.getApplications_byJobAndAtLeastOneWorkDay(jobId, workDays);

			for(Application affectedApplication : affectedApplications){
				Proposal currentProposal = proposalService.getCurrentProposal(
						affectedApplication.getApplicationId());

				proposalService.updateProposalFlag(currentProposal,
						Proposal.FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED, 1);

			}

			for(WorkDay workDay : workDays){
				repository.updateWorkDay(workDay.getWorkDayId(), workDay.getStringStartTime(),
						workDay.getStringEndTime());
			}
		}
	}

	public List<WorkDay> getWorkDays_incomplete_byUser(int jobId, int userId) {	
		return repository.getWorkDays_incomplete_byUser(jobId, userId);
	}
	
	public List<String> getWorkDayDateStrings(int jobId) {
		return repository.getWorkDayDateStrings(jobId);
	}
	
	public WorkDay getWorkDay(Integer jobId, String dateString) {
		return repository.getWorkDay(jobId, dateString);
	}
	
	public List<WorkDayDto> getWorkDayDtos(int jobId) {
		
		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();
		List<WorkDay> workDays = getWorkDays(jobId);
		for(WorkDay workDay : workDays){

			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);
			workDayDto.setCount_applicants(applicationService.getCount_applicantsByDay(workDay.getDateId(), jobId));
			workDayDto.setCount_positionsFilled(applicationService.getCount_positionsFilledByDay(workDay.getDateId(), jobId));
			workDayDtos.add(workDayDto);
		}
		return workDayDtos;
	}	
	public List<WorkDay> getWorkDays(int jobId) {
		return repository.getWorkDays(jobId);
	}


	public List<WorkDayDto> getWorkDayDtos_byProposal(Proposal proposal) {

		// **************************************************************
		// **************************************************************
		// Review how work day dtos are used.
		// **************************************************************
		// **************************************************************

		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();
		Application application = applicationService.getApplication(proposal.getApplicationId());
		Job job = jobService.getJob_ByApplicationId(proposal.getApplicationId());
		List<WorkDay> workDays = getWorkDays(jobService.getJob_ByApplicationId(proposal.getApplicationId()).getId());

		for(WorkDay workDay: workDays){
			
			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);

			// Is proposed
			workDayDto.setIsProposed(applicationService.getIsWorkDayProposed(
					workDay.getWorkDayId(), proposal.getApplicationId()));

			if(workDayDto.getIsProposed() && application.getIsAccepted() == 1)
				workDayDto.setIsAccepted(true);
			else
				workDayDto.setIsAccepted(false);


			// Conflicting employment
			setWorkDayDto_conflictingEmployment(job.getId(), workDayDto, application.getUserId());

			// Conflicting applications
			workDayDto.setApplicationDtos_conflictingApplications(
					applicationService.getApplicationDtos_Conflicting(
							application.getUserId(), proposal.getApplicationId(), Arrays.asList(workDay)));
			workDayDtos.add(workDayDto);
		}
		return workDayDtos;
	}
	
	public void setWorkDayDto_conflictingEmployment(int jobId_reference, WorkDayDto workDayDto, int userId ){

		workDayDto.setJob_conflictingEmployment(jobService.getConflictingEmployment_byUserAndWorkDay(
				jobId_reference, userId, workDayDto.getWorkDay().getDateId()));
		if(workDayDto.getJob_conflictingEmployment() != null)
			workDayDto.setHasConflictingEmployment(true);
		else workDayDto.setHasConflictingEmployment(false);
	}

	public Integer getWorkDayCount(Integer jobId) {
		List<WorkDay> workDays = getWorkDays(jobId);
		if(workDays == null){
			return null;
		}else{
			return workDays.size();
		}
	}



}
