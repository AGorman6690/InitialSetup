package com.jobsearch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.google.GoogleClient;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Question;
import com.jobsearch.repository.QuestionRepository;
import com.jobsearch.utilities.VerificationServiceImpl;

@Service
public class QuestionServiceImpl {
	

	@Autowired
	QuestionRepository repository;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	VerificationServiceImpl verificationService;
	@Autowired
	GoogleClient googleClient;
	@Autowired
	ProposalServiceImpl proposalService;
	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	AnswerServiceImpl answerService;	


	public Question getQuestion_PreviousPostedQuestion(HttpSession session, int questionId) {

		Question postedQuestion = getQuestion(questionId);
		if(verificationService.didSessionUserPostJob(session, postedQuestion.getJobId())){
			return postedQuestion;
		}
		else return null;
	}
	
	public List<Question> getQuestions(int jobId) {
		// This will not set an answer

		List<Question> questions = repository.getQuestions(jobId);
		for (Question q : questions) {
			q.setAnswerOptions(applicationService.getAnswerOptions(q.getQuestionId()));
		}
		return questions;
	}

	public List<Question> getQuestionsWithAnswersByJobAndUser(int jobId, int userId) {

		List<Question> questions = repository.getQuestions(jobId);

		for(Question question : questions){
			question.setAnswerOptions(applicationService.getAnswerOptions(question.getQuestionId()));
			question.setAnswers(answerService.getAnswers(question.getQuestionId(), userId));
		}
		return questions;
	}
	
	public Question getQuestion(int questionId) {
		return repository.getQuestion(questionId);
	}
	

	public List<Question> getDistinctQuestions_byEmployer(int userId) {
		return repository.getDistinctQuestions_byEmployer(userId);
	}

	

	public void addQuestion(Question question) {

		if(question.getFormatId() == Question.FORMAT_ID_YES_NO){

			question.setAnswerOptions(new ArrayList<AnswerOption>());

			AnswerOption answerOption = new AnswerOption();
			answerOption.setQuestionId(question.getQuestionId());
			answerOption.setText("Yes");
			question.getAnswerOptions().add(answerOption);

			answerOption = new AnswerOption();
			answerOption.setQuestionId(question.getQuestionId());
			answerOption.setText("No");
			question.getAnswerOptions().add(answerOption);
		}

		repository.addQuestion(question);
	}

	public boolean areAllQuestionsAnswered(List<Answer> allAnswers, int jobId) {
		
		boolean valid = true;		
		List<Question> questions = getQuestions(jobId);
		if(questions != null && questions.size() > 0){			
			for (Question question : questions){
				List<Answer> answers = allAnswers.stream()
									.filter(a -> a.getQuestionId() == question.getQuestionId())
									.collect(Collectors.toList());				
				if (answers == null || answers.size() == 0){
					valid = false;
					break;							
				}				
			}			
		}		
		return valid;
	}
	
}
