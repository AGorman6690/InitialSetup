package com.jobsearch.job.service;

import java.util.List;

import com.jobsearch.model.FailedWageNegotiationDTO;

public class JobDTO {
	
	//**********************************************
	//**********************************************
	//The thought behind this DTO was to create a place to put all the
	//miscellaneous information that is sometimes associated with a job object.
	//The job class's property list is getting awfully long and confusing.
	//So any info that needs to be bundled with a job, for whatever purpose,
	//in order to display something to the user, I purpose putting the info here.
	//Then the job class can only contain the info related to a job (i.e. the columns in the job table).
	//**********************************************
	//**********************************************
	
	Job job;
	List<FailedWageNegotiationDTO> failedWageNegotiationsDtos;

	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public List<FailedWageNegotiationDTO> getFailedWageNegotiationDtos() {
		return failedWageNegotiationsDtos;
	}

	public void setFailedWageNegotiationDtos(List<FailedWageNegotiationDTO> failedWageNegotiationsDtos) {
		this.failedWageNegotiationsDtos = failedWageNegotiationsDtos;
	}
	
	

}
