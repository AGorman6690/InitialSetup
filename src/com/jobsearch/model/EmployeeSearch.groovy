package com.jobsearch.model

import java.awt.TexturePaintContext.Int

import com.jobsearch.job.web.JobDTO;;

public class EmployeeSearch {
	JobDTO jobDto;
	Integer jobId_excludeApplicantsOfThisJob;
	Integer jobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays;
}
