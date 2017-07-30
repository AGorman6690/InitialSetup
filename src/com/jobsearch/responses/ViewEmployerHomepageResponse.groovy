package com.jobsearch.responses

import com.jobsearch.job.service.Job

public class ViewEmployerHomepageResponse{
	
	public ViewEmployerHomepageResponse(){
		employerHomepageJobs = new ArrayList<>();
	}
	
	List<EmployerHomepageJob> employerHomepageJobs;
	
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