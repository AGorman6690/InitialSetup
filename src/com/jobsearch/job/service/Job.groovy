package com.jobsearch.job.service

import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

import com.jobsearch.category.service.Category
import com.jobsearch.model.JobSearchUser
import com.jobsearch.model.WorkDay


public class Job {
	Integer id
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
	Float lat
	Float lng
	Boolean isPartialAvailabilityAllowed
	int positionsPerDay
	
	int flag_isNotAcceptingApplications
	
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
//	List<Question> questions
	Integer Duration
	List<Integer> selectedQuestionIds
	
	//0 = not yet started;
	//1 = started;
	//2 = finished;
	int status
	public static Integer STATUS_FUTURE = 0;
	public static Integer STATUS_PRESENT = 1;
	public static Integer STATUS_PAST = 2;
	
	public static String FLAG_IS_NOT_ACCEPTING_APPLICATIONS = "Flag_IsNotAcceptingApplications"
	
	// Do away with work days from the job lass
	List<WorkDay> workDays;

}
