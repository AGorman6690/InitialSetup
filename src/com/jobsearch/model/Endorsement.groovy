package com.jobsearch.model

public class Endorsement {
	int jobId
	int userId
	int categoryId
	int count
	String categoryName

	public Endorsement(){}

	public Endorsement(int userId, int categoryId, int jobId){
		this.userId = userId
		this.categoryId = categoryId
		this.jobId = jobId
	}
}

