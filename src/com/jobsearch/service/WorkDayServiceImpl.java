package com.jobsearch.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.repository.WorkDayRepository;

@Service
public class WorkDayServiceImpl {
	
	@Autowired
	WorkDayRepository repository;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	JobServiceImpl jobService;
	
	
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
	
	public List<WorkDayDto> getWorkDayDtos(int jobId) {

		List<WorkDayDto> workDayDtos = new ArrayList<WorkDayDto>();
		List<WorkDay> workDays = getWorkDays(jobId);

		for(WorkDay workDay : workDays){

			WorkDayDto workDayDto = new WorkDayDto();
			workDayDto.setWorkDay(workDay);
			workDayDto.setCount_applicants(applicationService.getCount_applicantsByDay(workDay.getDateId(), jobId));
			workDayDto.setCount_positionsFilled(applicationService.getCount_positionsFilledByDay(workDay.getDateId(), jobId));
			workDayDto.setCount_totalPositions(jobService.getJob(jobId).getPositionsPerDay());
			workDayDtos.add(workDayDto);
		}

		return workDayDtos;
	}
	
	public List<WorkDay> getWorkDays(int jobId) {
		return repository.getWorkDays(jobId);
	}
	
	public List<WorkDay> getWorkDays_byProposalId(Integer employmentProposalId) {
		return repository.getWorkDays_byProposalId(employmentProposalId);
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

			// Positions filled
			int positionsFilled = applicationService.getCount_positionsFilledByDay(
					workDay.getDateId(), job.getId());
			if(positionsFilled < job.getPositionsPerDay()) workDayDto.setHasOpenPositions(true);
			else workDayDto.setHasOpenPositions(false);

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

}
