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

		//If hired
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

			if (answer.getAnswerText() != ""){
				repository.addTextAnswer(answer);
			}else if(answer.getAnswerBoolean() != -1){
				repository.addBooleanAnswer(answer);
			}else if(answer.getAnswerOptionId() != -1){
				repository.addOptionAnswer(answer, answer.getAnswerOptionId());
			}else if(answer.getAnswerOptionIds().size() > 0){
				for(int answerOptionId : answer.getAnswerOptionIds()){
					repository.addOptionAnswer(answer, answerOptionId);
				}
			}
		}
	}


	public List<Answer> getAnswers(List<Question> questions, int userId) {

		List<Answer> answers = new ArrayList<Answer>();

		for(Question question : questions){
			Answer answer;

			if(question.getFormatId() == 2 || question.getFormatId() == 3){
				answer = new Answer();
				answer.setAnswers(repository.getAnswers(question.getQuestionId(), userId));
//			}else if(question.getFormatId() == 2){
//				answer = repository.getAnswer(question.getQuestionId(), userId);
//
			}else{
				answer = repository.getAnswer(question.getQuestionId(), userId);
			}

			answer.setQuestionFormatId(question.getFormatId());
			answers.add(answer);
		}
		return answers;
	}


	public List<Question> getQuestions(int jobId) {

		List<Question> questions = repository.getQuestions(jobId);
		for(Question q : questions){
			q.setAnswerOptions(repository.getAnswerOptions(q.getQuestionId()));
		}
		return questions;
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
