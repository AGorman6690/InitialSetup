package com.jobsearch.model

import java.util.List

import com.jobsearch.application.service.Application
import com.jobsearch.category.service.Category
import com.jobsearch.job.service.CompletedJobResponseDTO
import com.jobsearch.job.service.Job

class JobSearchUser {
	int userId
	int profileId
	String firstName
	String lastName
	String username
	String emailAddress
	String password
	String matchingPassword
	Profile profile
	List<Category> categories
	List<Job> jobs
	List<Job> activeJobs
	List<Job> jobsAppliedTo
	List<Job> jobsHiredFor
	List<CompletedJobResponseDTO> completedJobs
	List<RateCriterion> ratings
	List<Endorsement> endorsements
	Application application
	double rating
	List<String> availableDates
	float homeLat
	float homeLng
	String homeCity
	String homeState
	String homeZipCode
	int maxWorkRadius //Units = miles
	double distanceFromJob
	int createNewPassword
}
