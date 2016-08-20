package com.jobsearch.application.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalCounterDTO;
import com.jobsearch.user.service.UserServiceImpl;

@Service
public class ApplicationServiceImpl {

	@Autowired
	ApplicationRepository repository;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	JobServiceImpl jobService;



	public List<Application> getApplicationsByJob(int jobId) {

		//Query the application table
		List<Application> applications = repository.getApplicationsByJob(jobId);
		
		//Get the job's categories
		List<Category> categories = categoryService.getCategoriesByJobId(jobId);
		
		//Set each application's wage proposals.
		//Set each applicant's endorsements.
		for (Application application : applications) {	
			
			JobSearchUser applicant = application.getApplicant();
			
			//Set the application's wage proposals
			application.setWageProposals(this.getWageProposals(application.getApplicationId()));
			
			//Get the application's current wage proposal, whether proposed by the employer or applicant
			application.setCurrentWageProposal(this.getCurrentWageProposal(application));
			
			//Set the current wage proposed BY the applicant.
			//Since the desired pay can be countered several times,
			//This holds the applican't most recent pay request.
			application.setApplicantsCurrentDesiredWage(
					this.getCurrentWageProposedBy(application.getApplicationId(), applicant.getUserId()));
			
//			//Set the current wage proposed TO the applicant.
//			application.setEmployersCurrentOfferedWage(this.getCurrentWageProposedTo(application.getApplicationId(), applicant.getUserId()));
				
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


	public  float getCurrentWageProposedTo(int applicationId, int proposedToUserId) {
		repository.getCurrentWageProposedTo(applicationId, proposedToUserId);
		return 0;
	}


	public WageProposal getCurrentWageProposal(Application application) {
		
//		//Verify the application has at least 1 wage proposal
//		if(application.getWageProposals().size() > 0){
			return repository.getCurrentWageProposal(application.getApplicationId());
//		}else{
//			return null;
//		}
		
	}
	
	public float getCurrentWageProposedBy(int applicationId, int proposedByUserId) {
		
		return repository.getCurrentWageProposedBy(applicationId, proposedByUserId);
		
	}	


	public  List<WageProposal> getWageProposals(int applicationId) {
		
		return repository.getWageProposals(applicationId);
	}


	public Application getApplication(int jobId, int userId) {
		return repository.getApplication(jobId, userId);
	}


	public void updateStatus(int applicationId, int status) {
		
//		//If hired
		if (status == 3){
			Application application = getApplication(applicationId);
			userService.hireApplicant(application.getUserId(), application.getJobId());
		}
		
		repository.updateStatus(applicationId, status);



	}


	public Application getApplication(int applicationId) {
		return repository.getApplication(applicationId);
	}

	public void applyForJob(ApplicationRequestDTO applicationDto, HttpSession session) {
		
		//Set the application's user (i.e. the applicant)
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		applicationDto.setUserId(user.getUserId());
		
		//Set the wage proposed BY the applicant
		applicationDto.getWageProposal().setProposedByUserId(user.getUserId());
		
		//Set the wage proposed TO the employer
		//Get the employer's id from the job object.
		Job appliedToJob = jobService.getJob(applicationDto.getJobId());
		applicationDto.getWageProposal().setProposedToUserId(appliedToJob.getUserId());
		
		
		//Add the application to the database
//		repository.addApplication(applicationDto.getJobId(), applicationDto.getUserId());
		this.insertApplication(applicationDto);
		
//		//Add the wage proposal to the database
//		this.addWageProposal(applicationDto.getWageProposal());

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


	public void addWageProposal(WageProposal wageProposal) {
		repository.addWageProposal(wageProposal);
		
	}


	public void insertApplication(ApplicationRequestDTO applicationDto) {
		repository.insertApplication(applicationDto);
		
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


	public void insertCounterOffer(WageProposalCounterDTO dto) {
		
		//Get the wage proposal to counter
		WageProposal wagePropasalToCounter = repository.getWageProposal(dto.getWageProposalIdToCounter());
	
		//Set the proposal to counter to rejected
		repository.updateWageProposalStatus(dto.getWageProposalIdToCounter(), 0);
		
		//Create a new wage proposal for the counter
		WageProposal counter = new WageProposal();
		counter.setAmount(dto.getCounterAmount());
		
		//The counter is being proposed BY the user whom the wage-proposal-to-counter was proposed TO,
		//and vice versa.
		counter.setProposedByUserId(wagePropasalToCounter.getProposedToUserId());
		counter.setProposedToUserId(wagePropasalToCounter.getProposedByUserId());
		counter.setApplicationId(wagePropasalToCounter.getApplicationId());
		counter.setStatus(-1);
		
		repository.addWageProposal(counter);
		
	}


	public List<ApplicationResponseDTO> getApplicationResponseDtosByApplicant(int userId) {
		
		List<ApplicationResponseDTO> result = new ArrayList<ApplicationResponseDTO>();
		
		//Query the database
		List<Application> applications = this.getApplicationsByUser(userId);
		
		//Create application response dtos
		for(Application application : applications){
			ApplicationResponseDTO dto = new ApplicationResponseDTO();
			dto.setApplication(application);
			dto.setCurrentDesiredWage(this.getCurrentWageProposedBy(application.getApplicationId(), userId));
			dto.setCurrentWageProposal(this.getCurrentWageProposal(application));
//			dto.setJobStatus(jobService.getJobStatus(application.getJobId()));
			dto.setJob(jobService.getJob(application.getJobId()));
			
			//Add the dto
			result.add(dto);
		}
		
		return result;
	}


	public List<Application> getApplicationsByUser(int userId) {		
		return repository.getApplicationsByUser(userId);
	}
	
	

}
