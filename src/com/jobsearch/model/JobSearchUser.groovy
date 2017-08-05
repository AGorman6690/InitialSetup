package com.jobsearch.model

import com.jobsearch.model.Application
import com.jobsearch.model.Job
import com.jobsearch.category.service.Category
import java.util.List


class JobSearchUser {
	
	int userId
	int profileId
	String firstName
	String lastName
	String username
	String emailAddress
	String matchingEmailAddress
	String password
	String matchingPassword
	Profile profile
//	List<Category> categories
//	List<Job> jobs
//	List<Job> activeJobs
//	List<Job> jobsAppliedTo
//	List<Job> jobsHiredFor
//	List<RateCriterion> ratings
//	List<Endorsement> endorsements
//	Application application
	double rating
//	List<String> availableDates
	float homeLat
	float homeLng
	String homeCity
	String homeState
	String homeZipCode
	Integer maxWorkRadius //Units = miles
	double distanceFromJob
	int createNewPassword
//	double wage
	Double minimumDesiredPay
	String stringMinimumDesiredPay
	String about
	
}	
