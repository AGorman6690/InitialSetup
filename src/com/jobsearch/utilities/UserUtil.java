package com.jobsearch.utilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jobsearch.model.Job;
import com.jobsearch.service.JobServiceImpl;

@Component
public class UserUtil {
	
	@Autowired
	JobServiceImpl jobService;

	public  boolean employeeHasOutstandingRatings(int userId) {
		List<Job> jobsRquireRating = jobService.getJobs_needRating_byEmployee(userId);
		if (jobsRquireRating == null || jobsRquireRating.size() == 0){
			return false;
		}else{
			return true;
		}		
	}

}
