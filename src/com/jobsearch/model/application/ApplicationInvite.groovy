package com.jobsearch.model.application

import java.awt.TexturePaintContext.Int;

public class ApplicationInvite{
	
	int applicationInviteId
	int jobId
	int userId
	
	int status
	public static int STATUS_NOT_YET_VIEWED = 0
	public static int STATUS_NO_ACTION_TAKEN = 1
	public static int STATUS_APPLIED_TO_JOB = 2
	public static int STATUS_DECLINED = 3
}