package com.jobsearch.request

import com.jobsearch.model.Job
import com.jobsearch.model.Question
import com.jobsearch.model.Skill
import com.jobsearch.model.WorkDay

public class AddJobRequest {
	Job job
	List<WorkDay> workDays
	List<Question> questions
	List<Skill> skills	
}
