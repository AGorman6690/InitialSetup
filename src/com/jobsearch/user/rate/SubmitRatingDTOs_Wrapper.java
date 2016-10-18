package com.jobsearch.user.rate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitRatingDTOs_Wrapper {
	
	
	
	private int jobId;
	private List<SubmitRatingDTO> submitRatingDtos;

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public List<SubmitRatingDTO> getSubmitRatingDtos() {
		return submitRatingDtos;
	}

	public void setSubmitRatingDtos(List<SubmitRatingDTO> submitRatingDtos) {
		this.submitRatingDtos = submitRatingDtos;
	}	

}
