package com.jobsearch.dtos

import com.jobsearch.job.service.Job

public class CompletedJobDto {
	Job job
	Double rating;
	List<String> comments;
}
