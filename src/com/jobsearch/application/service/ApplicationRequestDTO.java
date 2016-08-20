package com.jobsearch.application.service;

import java.util.List;

import com.jobsearch.model.Answer;
import com.jobsearch.model.WageProposal;

public class ApplicationRequestDTO {
	private int jobId;
	private int userId;
	private List<Answer> answers;
	private WageProposal wageProposal;
	
		
	public WageProposal getWageProposal() {
		return wageProposal;
	}
	public void setWageProposal(WageProposal wageProposal) {
		this.wageProposal = wageProposal;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	
}
