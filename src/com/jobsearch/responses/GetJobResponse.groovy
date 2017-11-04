package com.jobsearch.responses

import com.jobsearch.model.Application

import javax.servlet.http.HttpSession

import com.jobsearch.dtos.ProfileInfoDto;
import com.jobsearch.model.Job
import com.jobsearch.model.Question
import com.jobsearch.model.Skill
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto

public class GetJobResponse extends BaseResponse {
	
	public GetJobResponse(){}
	
	public GetJobResponse(HttpSession s) {
		super(s);
	}
	Job job
	Application application
	List<WorkDayDto> workDayDtos
	int countWorkDays
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
//	Boolean cannotApplyBecauseLacksAvailability
	String warningMessage
	Boolean isApplyable
}
