package com.jobsearch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.model.Answer;
import com.jobsearch.repository.AnswerRepository;

@Service
public class AnswerServiceImpl {
	
	@Autowired
	AnswerRepository repository;
	
	public void addAnswer(Answer answer) {
		repository.addAnswer(answer);
	}
	
	public Answer getAnswer(int questionId, int userId) {
		return repository.getAnswer(questionId, userId);
	}

	public List<Answer> getAnswers(int questionId, int userId) {
		return repository.getAnswers(questionId, userId);
	}


}
