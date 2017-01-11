package com.jobsearch.job.service

import com.jobsearch.application.service.Application
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.Question
import com.jobsearch.category.service.Category
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date
import java.util.List


public class Job {
	int id
	int isActive
	int userId
	int openings
	String jobName
	String description
	Category category
	String streetAddress
	String city
	String state
	String zipCode
	float lat
	float lng
	
	//******************************************************
	//******************************************************
	//Note: It appears LocalDate should be used over Date since 
	//Date is deprecated.
	//I'm beginning to switch this over.
	//Eventually delete all Date and Time properties
	//******************************************************
	//******************************************************
	Date startDate
	Date endDate
	Time startTime
	Time endTime
	LocalDate startDate_local
	LocalDate endDate_local
	LocalTime startTime_local
	LocalTime endTime_local
	String stringStartTime
	String stringEndTime
	String stringStartDate
	String stringEndDate
	
	
	
	Double distanceFromFilterLocation
	List<Integer> categoryIds
	List<Category> categories // do away with
	List<JobSearchUser> employees
//	List<JobSearchUser> applicants
//	List<Application> applications
	List<PostQuestionDTO> questions
	Integer Duration
	List<Integer> selectedQuestionIds
	
	//0 = not yet started;
	//1 = started;
	//2 = finished;
	int status
	
	// Do away with work days from the job lass
	List<WorkDay> workDays;
	
	// 1 = Hours
	// 2 = Days
	// 3 = Weeks
	// 4 = Months
	// 5 = Years
	// 6 = Hopefully Forever
	Integer durationTypeId;
	
	Integer durationUnitLength;
}
