package com.jobsearch.responses

import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.model.Job
import javax.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

public class ViewEmployerHomepageResponse extends BaseResponse{

	ProfileInfoDto profileInfoDto;
	List<EmployerHomepageJob> employerHomepageJobs = new ArrayList<>();
	
	public ViewEmployerHomepageResponse() {}
	public ViewEmployerHomepageResponse(HttpSession session) {
		super(session);
	}

	public static class EmployerHomepageJob{
		Job job
		long countWageProposals_sent
		long countWageProposals_received
		long countWageProposals_received_new
		long countProposals_expired		
		long countApplications_total
		long countApplications_new		
		long countEmployees_hired
	}
	
}