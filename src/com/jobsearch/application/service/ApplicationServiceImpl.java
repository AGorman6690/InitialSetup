package com.jobsearch.application.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.FailedWageNegotiationDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalCounterDTO;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.MathUtility;

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

	@Autowired
	@Qualifier("FailedWageNegotiationsVM")
	Template vmTemplate_failedWageNegotiations;	


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
			
			//If applicable, get the application's current wage proposal, whether proposed by the employer or applicant
			application.setCurrentWageProposal(this.getCurrentWageProposal(application));
			
			//If Set the current wage proposed BY the applicant.
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


	public void updateApplicationStatus(int applicationId, int status) {
		
//		//If hired
		if (status == 3){
			Application application = getApplication(applicationId);
			userService.hireApplicant(application.getUserId(), application.getJobId());
		}
		
		repository.updateApplicationStatus(applicationId, status);



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


	public void setJobsApplicationsHasBeenViewed(List<Job> jobs, int value) {
		
		
		for (Job job : jobs){
			
			//If there are applications, set all jobs' applications' HasBeenViewed property
			if(job.getApplications().size() > 0){
				repository.setHasBeenViewed(job.getId(), value);
			}	
		}
		
		
	}
	
	public WageProposal getWageProposal(int wageProposalId){
		return  repository.getWageProposal(wageProposalId);
	}

	public void insertCounterOffer(WageProposalCounterDTO dto) {
		
		//Get the wage proposal to counter
		WageProposal wagePropasalToCounter = this.getWageProposal(dto.getWageProposalIdToCounter());
	
		//Set the proposal-to-counter's status to countered
		this.updateWageProposalStatus(dto.getWageProposalIdToCounter(), 0);
		
		//Update the application's status to considering.
		//The employer's action of making a counter offer implies the employer is considering the applicant.		
		//Because the employer does not need to explicitly set the applicant's status to "considering" 
		//before making a counter offer, this ensures the application is set to the correct status.
		Application application = this.getApplication(wagePropasalToCounter.getApplicationId());
		this.updateApplicationStatus(application.getApplicationId(), 2);
		
		//Create a new wage proposal for the counter offer
		WageProposal counter = new WageProposal();
		counter.setAmount(dto.getCounterAmount());
		
		//This counter offer is being proposed BY the user whom the wage-proposal-to-counter was proposed TO,
		//and vice versa.
		counter.setProposedByUserId(wagePropasalToCounter.getProposedToUserId());
		counter.setProposedToUserId(wagePropasalToCounter.getProposedByUserId());
		counter.setApplicationId(wagePropasalToCounter.getApplicationId());
		counter.setStatus(-1);
		
		repository.addWageProposal(counter);
		
	}
	


	public List<ApplicationResponseDTO> getFailedWageNegotiations(int userId) {		

		//Query the database.
		//Return only applications with a status of "wage negotiations ended" (4)
		List<Application> failedWageNegotiations = this.getApplicationsByUserAndStatuses(userId, 
													new ArrayList<Integer>(Arrays.asList(4)));
		
		//Get application response DTOs
		if(failedWageNegotiations.size() >0){			
			return getApplicationResponseDTOsByApplicant(failedWageNegotiations, userId);	
		}else{
			return null;
		}
		
				
	}
	
	
	


	private List<ApplicationResponseDTO> getApplicationResponseDTOsByApplicant(
						List<Application> applications, int applicantId) {
		
		List<ApplicationResponseDTO> result = new ArrayList<ApplicationResponseDTO>();
		
		//Set application response dtos
		for(Application application : applications){

				ApplicationResponseDTO dto = new ApplicationResponseDTO();
				dto.setApplication(application);
				dto.setCurrentDesiredWage(this.getCurrentWageProposedBy(application.getApplicationId(), applicantId));
				dto.setCurrentWageProposal(this.getCurrentWageProposal(application));
				dto.setJob(jobService.getJob(application.getJobId()));
				
				//*****************************************************
				//*****************************************************
				//This shouldn't need to be here.
				//In reality, all open applications at the time a job is ended should be 
				//closed by default.
				//Thus, as long as an application's status is 0 or 2, then the job by definition is still open.
				//Leave this here for now. 
				//*****************************************************
				//*****************************************************
				//Add the dto if the job is still active
				if(dto.getJob().getStatus() < 2){
					result.add(dto);	
				}
//			}
			
		}
		
		return result;
		
	}



	public List<ApplicationResponseDTO> getOpenApplicationResponseDtosByApplicant(int userId) {
		
//		List<ApplicationResponseDTO> result = new ArrayList<ApplicationResponseDTO>();
		
		//Query the database.
		//Return only applications with a status of "submitted" (0) or "considered" (2)
		List<Application> openApplications = this.getApplicationsByUserAndStatuses(userId, 
													new ArrayList<Integer>(Arrays.asList(0, 2)));
		
		//Get application response DTOs
		if(openApplications.size() >0){			
			return getApplicationResponseDTOsByApplicant(openApplications, userId);	
		}else{
			return null;
		}		
		
//		//Create application response dtos
//		for(Application application : openApplications){
//			
//			
//			//Only consider the application if it is still open.
//			//I.e. the employer has not declinded it nor has the wage negotiation ended
////			if (this.isApplicationOpen(application.getStatus())){
//
//				ApplicationResponseDTO dto = new ApplicationResponseDTO();
//				dto.setApplication(application);
//				dto.setCurrentDesiredWage(this.getCurrentWageProposedBy(application.getApplicationId(), userId));
//				dto.setCurrentWageProposal(this.getCurrentWageProposal(application));
//	//			dto.setJobStatus(jobService.getJobStatus(application.getJobId()));
//				dto.setJob(jobService.getJob(application.getJobId()));
//				
//				//*****************************************************
//				//*****************************************************
//				//This shouldn't need to be here.
//				//In reality, all open applications at the time a job is ended should be 
//				//closed by default.
//				//Thus, as long as an application's status is 0 or 2, then the job by definition is still open.
//				//Leave this here for now. 
//				//*****************************************************
//				//*****************************************************
//				//Add the dto if the job is still active
//				if(dto.getJob().getStatus() < 2){
//					result.add(dto);	
//				}
////			}
//			
//		}
//		
//		return result;
	}


	public List<Application> getApplicationsByUserAndStatuses(int userId, ArrayList<Integer> statuses) {
		
		if(statuses.size() > 0){
			return repository.getApplicationsByUserAndStatuses(userId, statuses);	
		}else{
			return null;
		}
		
	}


	private boolean isApplicationOpen(int status) {

		//If submitted or considered
		if (status == 0 || status == 2){
			return true;
		}else{
			return false;
		}
			
		
	}


	public List<Application> getApplicationsByUser(int userId) {		
		return repository.getApplicationsByUser(userId);
	}


	public void acceptWageProposal(int wageProposalId) {
		
		//Update the wage proposal's status to accepted
		this.updateWageProposalStatus(wageProposalId, 1);
		
		//Hire the applicant
		userService.hireApplicant(wageProposalId);
		
	}


	public void updateWageProposalStatus(int wageProposalId, int status) {
		repository.updateWageProposalStatus(wageProposalId, status);
		
	}


	public void declineWageProposalStatus(int wageProposalId) {
		
		
		//Update wage proposal's status to declined
		this.updateWageProposalStatus(wageProposalId, 2);
		
		//This status will allow the employer to see
		//whom he declined outright and those with whom he could not reach a negotiation.
		WageProposal wp = this.getWageProposal(wageProposalId);
		Application application = this.getApplication(wp.getApplicationId());
		this.updateApplicationStatus(application.getApplicationId(), 4);
		
	}


	public String getFailedWageNegotiationsVelocityTemplate(List<JobDTO> activeJobsWithFailedWageNegotiations) {
		
		StringWriter writer = new StringWriter();
		
		//Set the context
		final VelocityContext context = new VelocityContext();		
		context.put("activeJobsWithFailedWageNegotiations", activeJobsWithFailedWageNegotiations);
		context.put("mathUtility", MathUtility.class);	
		context.put("numberTool", new NumberTool());
		
		
		//Run the template
		vmTemplate_failedWageNegotiations.merge(context, writer);
		
		//Return the html
		return writer.toString();
	}


	public List<FailedWageNegotiationDTO> getFailedWageNegotiationDTOsByJob(Job job) {
		
		List<FailedWageNegotiationDTO> result = new ArrayList<FailedWageNegotiationDTO>();
		
		//Query the database for all the job's failed wage proposals
		List<WageProposal> failedWageProposals = repository.getFailedWageProposalsByJob(job.getId());
		
		//Create the failed wage negotiation DTOs		
		for(WageProposal failedWageProposal : failedWageProposals){
			
			//Create the dto
			FailedWageNegotiationDTO dto = new FailedWageNegotiationDTO();
			
			//Set the failed wage proposal
			dto.setFailedWageProposal(failedWageProposal);
			
			//Set the "other user" involved in the failed wage proposal
			dto.setOtherUser(this.getOtherUserInvolvedInWageProposal(
								failedWageProposal, job.getUserId()));
			
			//Add the dto to the results
			result.add(dto);
			
		}
		
		return result;
	}


	public JobSearchUser getOtherUserInvolvedInWageProposal(WageProposal failedWageProposal, int dontReturnThisUserId) {
		
		int otherUserId;
		
		//If the proposed-by-user is not the prohibited user,
		if(failedWageProposal.getProposedByUserId() != dontReturnThisUserId){
			otherUserId =  failedWageProposal.getProposedByUserId();
		}else{
			otherUserId = failedWageProposal.getProposedToUserId();
		}
		return userService.getUser(otherUserId);
	}

	
	
	

}
