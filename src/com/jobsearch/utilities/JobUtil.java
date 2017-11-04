package com.jobsearch.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jobsearch.model.Job;
import com.jobsearch.repository.JobRepository;

@Component
public class JobUtil {
	
	@Autowired
	private JobRepository repo;

	public boolean doesUserHaveEmploymentConflicts(int jobId_reference, int dateId, int userId ){
		Job conflictingJob = getEmploymentConflicts_byUserAndWorkDay(jobId_reference, userId, dateId);
		if(conflictingJob != null){
			return true;
		}else{
			return false;
		}
	}
	
	public Job getEmploymentConflicts_byUserAndWorkDay(int jobId_reference, int userId, int workDayId) {
		return repo.getConflictingEmployment_byUserAndWorkDay(jobId_reference, userId, workDayId);
	}
}
