package com.jobsearch.application.service

import com.jobsearch.model.Answer
import java.util.List


public class ApplicationRequestDTO {
	int jobId
	int userId
	List<Answer> answers
}
