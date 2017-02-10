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
import com.jobsearch.model.FailedWageNegotiationDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalCounterDTO;
import com.jobsearch.session.SessionContext;
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

		// Query the application table
		List<Application> applications = repository.getApplicationsByJob(jobId);

		// Get the job's categories
		List<Category> categories = categoryService.getCategoriesByJobId(jobId);

		// Set each application's wage proposals.
		// Set each applicant's endorsements.
		for (Application application : applications) {

			JobSearchUser applicant = application.getApplicant();

			// Set the application's wage proposals
			application.setWageProposals(this.getWageProposals(application.getApplicationId()));

			// If applicable, get the application's current wage proposal,
			// whether proposed by the employer or applicant
			application.setCurrentWageProposal(this.getCurrentWageProposal(application));

			// Set the current wage proposed BY the applicant.
			// Since the desired pay can be countered several times,
			// this holds the applicant's most recent pay request.
			application.setCurrentDesiredWage(
					this.getCurrentWageProposedBy(application.getApplicationId(), applicant.getUserId()));

			applicant.setRating(userService.getRating(applicant.getUserId()));

			// //Set the current wage proposed TO the applicant.
			// application.setEmployersCurrentOfferedWage(this.getCurrentWageProposedTo(application.getApplicationId(),
			// applicant.getUserId()));

			applicant.setEndorsements(new ArrayList<Endorsement>());
			// applicant.setAnswers(this.getAnswers(application.ge, userId));

			// Set the application's questions and answers
			// application.setQuestions(this.getQuestionsWithAnswers(jobId,
			// applicant.getUserId()));
			application.setQuestions(this.getQuestionsWithAnswersByJobAndUser(jobId, applicant.getUserId()));

			// application.setAnswers(this.getAnswersByJobAndUser(jobId,
			// applicant.getUserId()));
			// application.setAnswers(this.getAnswers(application.getQuestions(),
			// applicant.getUserId());

			// Get the applicant's endorsement for ONLY the particular job's
			// categories, not ALL categories.
			for (Category category : categories) {

				// Set the endorsement.
				// Currently only the employer profile jsp is using this.
				// As such, only the category name and endorsement count are
				// needed.
				Endorsement endorsement = new Endorsement();
				endorsement.setCategoryName(category.getName());
				endorsement
						.setCount(userService.getEndorsementCountByCategory(applicant.getUserId(), category.getId()));

				// Add the endorsement to the applicant
				applicant.getEndorsements().add(endorsement);

			}

		}

		return applications;
	}

	public float getCurrentWageProposedTo(int applicationId, int proposedToUserId) {
		repository.getCurrentWageProposedTo(applicationId, proposedToUserId);
		return 0;
	}

	public WageProposal getCurrentWageProposal(Application application) {
		return repository.getCurrentWageProposal(application.getApplicationId());
	}

	public float getCurrentWageProposedBy(int applicationId, int proposedByUserId) {
		return repository.getCurrentWageProposedBy(applicationId, proposedByUserId);
	}

	public List<WageProposal> getWageProposals(int applicationId) {
		return repository.getWageProposals(applicationId);
	}

	public Application getApplication(int jobId, int userId) {
		return repository.getApplication(jobId, userId);
	}

	public void updateApplicationStatus(int applicationId, int status) {
		// //If hired
		if (status == 3) {
			Application application = getApplication(applicationId);
			userService.hireApplicant(application.getUserId(), application.getJobId());
		}
		repository.updateApplicationStatus(applicationId, status);
	}

	public Application getApplication(int applicationId) {
		return repository.getApplication(applicationId);
	}

	public void applyForJob(Application application, HttpSession session) {

		// Set the application's user (i.e. the applicant)
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		application.setUserId(user.getUserId());

		// Set the wage proposed BY the applicant
		application.getWageProposal().setProposedByUserId(user.getUserId());

		// Set the wage proposed TO the employer
		// Get the employer's id from the job object.
		Job appliedToJob = jobService.getJob(application.getJobId());
		application.getWageProposal().setProposedToUserId(appliedToJob.getUserId());

		// Add the application to the database
		// repository.addApplication(applicationDto.getJobId(),
		// applicationDto.getUserId());
		insertApplication(application);

		// //Add the wage proposal to the database
		// this.addWageProposal(applicationDto.getWageProposal());

		// Whether the applicant answered all the questions is handled on the
		// client side.
		// At this point, all questions have a valid answer.

	}

	public void addWageProposal(WageProposal wageProposal) {
		repository.addWageProposal(wageProposal);

	}

	public void insertApplication(Application application) {
		repository.insertApplication(application);

	}

	public List<Answer> getAnswers(int questionId, int userId) {
		return repository.getAnswers(questionId, userId);
	}

	public List<Answer> getAnswersByJobAndUser(int jobId, int userId) {
		return repository.getAnswersByJobAndUser(jobId, userId);
	}

	public List<Question> getQuestions(int jobId) {
		// This will not set an answer

		List<Question> questions = repository.getQuestions(jobId);
		for (Question q : questions) {
			q.setAnswerOptions(this.getAnswerOptions(q.getQuestionId()));
		}
		return questions;
	}

	public List<Question> getQuestionsWithAnswersByJobAndUser(int jobId, int userId) {

		List<Question> questions = repository.getQuestions(jobId);
		
		for(Question question : questions){
			question.setAnswerOptions(this.getAnswerOptions(question.getQuestionId()));
			question.setAnswers(this.getAnswers(question.getQuestionId(), userId));
		}
		return questions;
	}



	//
	public List<AnswerOption> getAnswerOptions(int questionId) {

		return repository.getAnswerOptions(questionId);
	}

	public Answer getAnswer(int questionId, int userId) {
		return repository.getAnswer(questionId, userId);
	}

	public void addQuestion(Question question) {
		repository.addQuestion(question);
	}


	public WageProposal getWageProposal(int wageProposalId) {
		return repository.getWageProposal(wageProposalId);
	}

	public void insertCounterOffer(WageProposalCounterDTO dto, HttpSession session) {
	

		// As received from the browser, get the wage proposal to counter
		WageProposal wagePropasalToCounter = this.getWageProposal(dto.getWageProposalIdToCounter());
		
		// Get the application that pertains to the requested counter offer
		Application application = this.getApplication(wagePropasalToCounter.getApplicationId());
		
		// Get the current wage proposal in the database that pertains to this application
		WageProposal currentWageProposalInDatabase = this.getCurrentWageProposal(application);
		
		
		// Verify:
		// 1) The wage proposal id received from the browser was indeed proposed TO the session user
		//		Reason: the user can edit the wage proposal id in the browser.
		// 2) The current wage proposal pertaining to this application is currently
		//			proposed TO the session user.
		//		Reason: Because this is initiated by an Ajax call, the browser can lag and the user
		//					might click "send counter offer" more than once.
		if(wagePropasalToCounter.getProposedToUserId() == SessionContext.getUser(session).getUserId() &&
			currentWageProposalInDatabase.getProposedToUserId() == SessionContext.getUser(session).getUserId()){
				
		
			// Set the proposal-to-counter's status to "countered"
			this.updateWageProposalStatus(dto.getWageProposalIdToCounter(), 0);
	
			// Update the application's status to considering.
			// The employer's action of making a counter offer implies the employer
			// is considering the applicant.
			// Because the employer does not need to explicitly set the applicant's
			// status to "considering"
			// before making a counter offer, this ensures the application is set to
			// the correct status.		
			this.updateApplicationStatus(application.getApplicationId(), 2);
	
			// Create a new wage proposal for the counter offer
			WageProposal counter = new WageProposal();
			counter.setAmount(dto.getCounterAmount());
	
			// This counter offer is being proposed BY the user whom the
			// wage-proposal-to-counter was proposed TO,
			// and vice versa.
			counter.setProposedByUserId(wagePropasalToCounter.getProposedToUserId());
			counter.setProposedToUserId(wagePropasalToCounter.getProposedByUserId());
			counter.setApplicationId(wagePropasalToCounter.getApplicationId());
								
			repository.addWageProposal(counter);
		}	

	}

	public List<ApplicationDTO> getApplicationDtos_ByUserAndApplicationStatus_OpenJobs(int userId, List<Integer> applicationStatuses){
		
		List<Application> applications = this.getApplications_ByUserAndStatuses_OpenJobs(userId, applicationStatuses);		

		List<ApplicationDTO> applicationDtos = getApplicationDtos_ByApplications(applications);
		
		return applicationDtos;
	}


	private List<ApplicationDTO> getApplicationDtos_ByApplications(List<Application> applications) {
		
		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
		
		for(Application application : applications){
			
			ApplicationDTO applicationDto = new ApplicationDTO();
			
			applicationDto.setApplication(application);
			applicationDto.setCurrentWageProposal(this.getCurrentWageProposal(application));
			applicationDto.setWageProposals(this.getWageProposals(application.getApplicationId()));
			applicationDto.setJob(jobService.getJobByApplicationId(application.getApplicationId()));
			applicationDto.getJob().setWorkDays(jobService.getWorkDays(applicationDto.getJob().getId()));
			applicationDtos.add(applicationDto);
		}
		
		return applicationDtos;
	}

	public List<Application> getApplications_ByUserAndStatuses_OpenJobs(int userId, List<Integer> statuses) {

		if (statuses.size() > 0) {
			return repository.getApplications_ByUserAndStatuses_OpenJobs(userId, statuses);
		} else {
			return null;
		}

	}

	private boolean isApplicationOpen(int status) {

		// If submitted or considered
		if (status == 0 || status == 2) {
			return true;
		} else {
			return false;
		}

	}

	public List<Application> getApplicationsByUser(int userId) {
		return repository.getApplicationsByUser(userId);
	}

	public void acceptWageProposal(int wageProposalId) {
		// Update the wage proposal's status to accepted
		updateWageProposalStatus(wageProposalId, 1);
		// Hire the applicant
		userService.hireApplicant(wageProposalId);

	}

	public void updateWageProposalStatus(int wageProposalId, int status) {
		repository.updateWageProposalStatus(wageProposalId, status);
	}

	public void declineWageProposalStatus(int wageProposalId) {

		// Update wage proposal's status to declined
		this.updateWageProposalStatus(wageProposalId, 2);

		// This status will allow the employer to see
		// whom he declined outright and those with whom he could not reach a
		// negotiation.
		WageProposal wp = this.getWageProposal(wageProposalId);
		Application application = this.getApplication(wp.getApplicationId());
		this.updateApplicationStatus(application.getApplicationId(), 4);

	}



	public List<FailedWageNegotiationDTO> getFailedWageNegotiationDTOsByJob(Job job) {

		List<FailedWageNegotiationDTO> result = new ArrayList<FailedWageNegotiationDTO>();

		// Query the database for all the job's failed wage proposals
		List<WageProposal> failedWageProposals = repository.getFailedWageProposalsByJob(job.getId());

		// Create the failed wage negotiation DTOs
		for (WageProposal failedWageProposal : failedWageProposals) {

			// Create the dto
			FailedWageNegotiationDTO dto = new FailedWageNegotiationDTO();

			// Set the failed wage proposal
			dto.setFailedWageProposal(failedWageProposal);

			// Set the "other user" involved in the failed wage proposal
			dto.setOtherUser(this.getOtherUserInvolvedInWageProposal(failedWageProposal, job.getUserId()));

			// Add the dto to the results
			result.add(dto);

		}

		return result;
	}

	public List<FailedWageNegotiationDTO> getFailedWageNegotiationsDTOsByUser(int userId) {

		List<FailedWageNegotiationDTO> result = new ArrayList<FailedWageNegotiationDTO>();

		// Query the database failed wage proposals.
		// Only failed proposal pertaining to jobs still accepting applications
		// is returned
		List<WageProposal> failedWageProposals = repository.getFailedWageProposalsByUser(userId);

		// Set the dtos
		for (WageProposal failedWageProposal : failedWageProposals) {

			// Create the dto
			FailedWageNegotiationDTO dto = new FailedWageNegotiationDTO();

			// Set the dto
			dto.setFailedWageProposal(failedWageProposal);
			dto.setJob(jobService.getJobByApplicationId(failedWageProposal.getApplicationId()));

			// Add the dto to the result
			result.add(dto);

		}
		return result;

	}

	public JobSearchUser getOtherUserInvolvedInWageProposal(WageProposal failedWageProposal, int dontReturnThisUserId) {

		int otherUserId;

		// If the proposed-by-user is not the prohibited user,
		if (failedWageProposal.getProposedByUserId() != dontReturnThisUserId) {
			otherUserId = failedWageProposal.getProposedByUserId();
		} else {
			otherUserId = failedWageProposal.getProposedToUserId();
		}
		return userService.getUser(otherUserId);
	}

	public double getWage(int userId, int jobId) {

		Application application = repository.getApplication(jobId, userId);

		return repository.getWage(application.getApplicationId());
	}


	public void addAnswer(Answer answer) {
		repository.addAnswer(answer);
	}

	public AnswerOption getAnswerOption(int answerOptionId) {
		return repository.getAnswerOption(answerOptionId);
	}

	public int getFailedApplicationCount(List<ApplicationDTO> applicationDtos) {		
		
		if(applicationDtos != null){
			return (int) applicationDtos.stream().filter(a -> a.getApplication().getStatus() == 1).count();
		}else{
			return 0;	
		}
		
	}

	public int getOpenApplicationCount(List<ApplicationDTO> applicationDtos) {
		
		if(applicationDtos != null){
			return (int) applicationDtos.stream().filter(a -> a.getApplication().getStatus() == 0 || 
																a.getApplication().getStatus() == 2).count();
		}else{
			return 0;	
		}
	}

	public void updateHasBeenViewed(Job job, int value) {
		repository.updateHasBeenViewed(job.getId(), value);
		
	}

	public int getCountWageProposal_Sent(Integer jobId, int userId) {

		return repository.getCountWageProposal_Sent(jobId, userId);
	}

	public int getCountWageProposal_Received(Integer jobId, int userId) {
	
		return repository.getCountWageProposal_Received(jobId, userId);
	}
	
	public int getCountWageProposal_Received_New(Integer jobId, int userId) {
		return repository.getCountWageProposal_Received_New(jobId, userId);
	}

	public void updateWageProposalsStatus_ToViewedButNoActionTaken(Integer jobId) {

		repository.updateWageProposalsStatus_ToViewedButNoActionTaken(jobId);
		
	}

	public List<Question> getQuestions_ByEmployer(int userId) {
		
		return repository.getQuestions_ByEmployer(userId);
	}

	public Question getQuestion(int questionId) {
		
		return repository.getQuestion(questionId);
	}

	public boolean wasUserEmployedForJob(int userId, int jobId) {
			
		int countEmploymentRecord = repository.getCount_Employment_ByUserAndJob(userId, jobId);
		
		if(countEmploymentRecord  == 1) return true;
		else return false;
	
	}






}
