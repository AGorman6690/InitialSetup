package com.jobsearch.model

import java.util.List

public class Skill {
	
	int skillId
	String text
	int type
	int jobId
	
	public static Integer TYPE_REQUIRED_JOB_POSTING = 0
	public static Integer TYPE_DESIRED_JOB_POSTING = 1
	
}