package com.jobsearch.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.UserServiceImpl;

@Service
public class ApplicationServiceImpl {

	@Autowired
	ApplicationRepository repository;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	UserServiceImpl userService;



	public List<Application> getApplicationsByJob(int jobId) {

		//Query the application table
		List<Application> applications = repository.getApplicationsByJob(jobId);
		
		//Get the job's categories
		List<Category> categories = categoryService.getCategoriesByJobId(jobId);
		
		//Set each applicant's endorsements
		for (Application application : applications) {
			

		
			JobSearchUser applicant = application.getApplicant();
			applicant.setEndorsements(new ArrayList<Endorsement>());
//			applicant.setAnswers(this.getAnswers(application.ge, userId));
			
			//Set the application's questions and answers
			application.setQuestions(this.getQuestionsWithAnswers(jobId, applicant.getUserId()));
			
			//Get the applicant's endorsement for ONLY the particular job's
			//categories, not ALL categories.
			for(Category category : categories){
								
				//Set the endorsement.
				//Currently only the employer profile jsp is using this.
				//As such, only the category name and endorsement count are needed.
				Endorsement endorsement = new Endorsement();
				endorsement.setCategoryName(category.getName());
				endorsement.setCount(userService.
								getEndorsementCountByCategory(applicant.getUserId(), category.getId()));
				
				//Add the endorsement to the applicant
				applicant.getEndorsements().add(endorsement);
				
				
			}

		}

		return applications;
	}


	public Application getApplication(int jobId, int userId) {
		return repository.getApplication(jobId, userId);
	}


	public void updateStatus(int applicationId, int status) {
		repository.updateStatus(applicationId, status);

//		//If hired
		if (status == 3){
			Application application = getApplication(applicationId);
			userService.hireApplicant(application.getUserId(), application.getJobId());
		}

	}


	public Application getApplication(int applicationId) {
		return repository.getApplication(applicationId);
	}

	public void applyForJob(ApplicationRequestDTO applicationDto) {
		repository.addApplication(applicationDto.getJobId(), applicationDto.getUserId());

		//Whether the applicant answered all the questions is handled on the client side.
		//At this point, all questions have a valid answer.
		
		for(Answer answer : applicationDto.getAnswers()){
			answer.setUserId(applicationDto.getUserId());
			repository.addAnswer(answer);
//			if (answer.getText() != ""){
//				repository.addTextAnswer(answer);
//			}else if(answer.getAnswerBoolean() != -1){
//				repository.addBooleanAnswer(answer);
//			}else if(answer.getAnswerOptionId() != -1){
//				repository.addOptionAnswer(answer, answer.getAnswerOptionId());
//			}else if(answer.getAnswerOptionIds().size() > 0){
//				for(int answerOptionId : answer.getAnswerOptionIds()){
//					repository.addOptionAnswer(answer, answerOptionId);
//				}
//			}
		}
	}


	public List<Answer> getAnswers(int questionId, int userId) {
		return repository.getAnswers(questionId, userId);
	}




	public List<Question> getQuestions(int jobId) {
		//This will not set an answer

		List<Question> questions = repository.getQuestions(jobId);
		for(Question q : questions){
			q.setAnswerOptions(repository.getAnswerOptions(q.getQuestionId()));
		}
		return questions;
	}
	
	public List<Question> getQuestionsWithAnswers(int jobId, int userId) {
		//This will set the user's answers 

		List<Question> questions = repository.getQuestions(jobId);
		for(Question q : questions){
			q.setAnswerOptions(this.getAnswerOptions(q.getQuestionId()));
			q.setAnswers(this.getAnswers(q.getQuestionId(), userId));
		}
		return questions;
	}


	public  List<AnswerOption> getAnswerOptions(int questionId) {
		repository.getAnswerOptions(questionId);
		return null;
	}


	public Answer getAnswer(int questionId, int userId) {

		Answer answer = repository.getAnswer(questionId, userId);
		//for (Answe)
//		
//		//Set the answer's answer.
//		//Because a question can be in one of several formats, the answer this will 
		return repository.getAnswer(questionId, userId);
	}


	public void addQuestion(Question question) {
		repository.addQuestion(question);

	}


	public void setHasBeenViewed(List<Job> jobs, int value) {
		
		
		for (Job job : jobs){
			
			//If there are applications, set all jobs' applications' HasBeenViewed property
			if(job.getApplications().size() > 0){
				repository.setHasBeenViewed(job.getId(), value);
			}	
		}
		
		
	}

}
