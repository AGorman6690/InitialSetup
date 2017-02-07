package com.jobsearch.job.service

import com.jobsearch.model.AnswerOption
import java.util.List;

class PostQuestionDTO {
	
	int formatId
	int id
	String text
//	List<String> answerOptions
	List<AnswerOption> answerOptions
	int jobId
	
}
