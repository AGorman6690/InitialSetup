package com.jobsearch.responses

import com.jobsearch.application.service.Application
import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.job.service.Job
import com.jobsearch.model.Question
import com.jobsearch.model.Skill
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto

public class GetJobResponse {
	Job job
	Application application
	List<WorkDayDto> workDayDtos
	String json_workDayDtos
	List<Skill> skillsRequired
	List<Skill> skillsDesired
	List<Question> questions
	String applicationStatus
	String context
	Boolean isPreviewingBeforeSubmittingJobPost
	ProfileInfoDto profileInfoDto
	String date_firstWorkDay
	int monthSpan_allWorkDays
	
}
