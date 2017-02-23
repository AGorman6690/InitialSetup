package com.jobsearch.application.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.application.repository.ApplicationRepository;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WageProposalDTO;
import com.jobsearch.model.WorkDay;
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


	public List<ApplicationDTO> getApplicationDtos_ByJob(int jobId) {
		
		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();

		// Query the application table
		List<Application> applications = repository.getApplications_ByJob(jobId);

		for (Application application : applications) {
			
			ApplicationDTO applicationDto =  new ApplicationDTO();
			
			applicationDto.setApplication(application);
			
			applicationDto.getApplicantDto().setUser(userService.getUser(application.getUserId()));	
			
			int applicantId = applicationDto.getApplicantDto().getUser().getUserId(); 
					
			applicationDto.setWageProposals(this.getWageProposals(application.getApplicationId()));
			
			applicationDto.setCurrentWageProposal(this.getCurrentWageProposal(application));
			
			applicationDto.setTime_untilEmployerApprovalExpires(
							this.getTime_untilEmployerApprovalExpires(
									application.getExpirationDate()));
			
			applicationDto.getApplicantDto().setRatingValue_overall(userService.getRating(applicantId));
			
			applicationDto.setQuestions(this.getQuestionsWithAnswersByJobAndUser(jobId, applicantId));
			
			applicationDto.setAnswerOptionIds_Selected(
						this.getAnswerOptionIds_Selected_ByApplicantAndJob(applicantId, jobId));

			applicationDtos.add(applicationDto);
		}

		return applicationDtos;
	}
	
	private String getTime_untilEmployerApprovalExpires(LocalDateTime expirationDate) {
		
		// This will subtract the current time from the expiration date.
		
		
		if(expirationDate != null){
			LocalDateTime now = LocalDateTime.now();
			
			// If expired
			if(ChronoUnit.MINUTES.between(now, expirationDate) < 0){
				return "-1";
			}
			else{
				long days = ChronoUnit.DAYS.between(now, expirationDate);
				long hours =  ChronoUnit.HOURS.between(now, expirationDate) - days * 24 ;
				long minutes =  ChronoUnit.MINUTES.between(now, expirationDate) - ( days * 24 * 60 ) - ( hours * 60 );
				String result = "";
				
				if(days == 1) result += days + " day, ";
				else if(days > 1) result += days + " days, ";
				
				result += " " + hours + ":";
				
				if(minutes < 10) result += "0";
				
				result += minutes + " hrs";

				// For example, the result will be in the form "2 days 15:45 hrs"
				return result;				
			}
	
		}
		return null;
	}


	private List<Integer> getAnswerOptionIds_Selected_ByApplicantAndJob(int userId, int jobId) {
		return repository.getAnswerOptionIds_Selected_ByApplicantAndJob(userId, jobId);
	}


	public float getCurrentWageProposedTo(int applicationId, int proposedToUserId) {
		repository.getCurrentWageProposedTo(applicationId, proposedToUserId);
		return 0;
	}

	public WageProposal getCurrentWageProposal(Application application) {
		return repository.getCurrentWageProposal(application.getApplicationId());
	}

//	public float getCurrentWageProposedBy(int applicationId, int proposedByUserId) {
//		return repository.getCurrentWageProposedBy(applicationId, proposedByUserId);
//	}

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

	public void applyForJob(ApplicationDTO applicationDto, HttpSession session) {

		// Set the application's user (i.e. the applicant)
		JobSearchUser user = (JobSearchUser) session.getAttribute("user");
		applicationDto.setApplicantId(user.getUserId());

		// Set the wage proposed BY the applicant
		applicationDto.getWageProposal().setProposedByUserId(user.getUserId());

		// Set the wage proposed TO the employer
		// Get the employer's id from the job object.
		Job appliedToJob = jobService.getJob(applicationDto.getJobId());
		applicationDto.getWageProposal().setProposedToUserId(appliedToJob.getUserId());

		// Add the application to the database
		// repository.addApplication(applicationDto.getJobId(),
		// applicationDto.getUserId());
		insertApplication(applicationDto);

		// //Add the wage proposal to the database
		// this.addWageProposal(applicationDto.getWageProposal());

		// Whether the applicant answered all the questions is handled on the
		// client side.
		// At this point, all questions have a valid answer.

	}

	public void addWageProposal(WageProposal wageProposal) {
		repository.addWageProposal(wageProposal);

	}

	public void insertApplication(ApplicationDTO applicationDto) {
		repository.insertApplication(applicationDto);

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


	public WageProposal getWageProposal(int wageProposalId) {
		return repository.getWageProposal(wageProposalId);
	}

	public void insertCounterOffer(WageProposalDTO dto, HttpSession session) {
	

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

		List<ApplicationDTO> applicationDtos = getApplicationDtos_ByApplications(applications, userId);
		
		return applicationDtos;
	}


	private List<ApplicationDTO> getApplicationDtos_ByApplications(List<Application> applications, int userId) {
		
		List<ApplicationDTO> applicationDtos = new ArrayList<ApplicationDTO>();
		
		for(Application application : applications){
			
			ApplicationDTO applicationDto = new ApplicationDTO();
			
			applicationDto.setApplication(application);
			
			applicationDto.setCurrentWageProposal(this.getCurrentWageProposal(application));
			applicationDto.setWageProposals(this.getWageProposals(application.getApplicationId()));
			
			applicationDto.getJobDto().setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
			applicationDto.getJobDto().setWorkDays(jobService.getWorkDays(applicationDto.getJobDto().getJob().getId()));
			
			applicationDto.setTime_untilEmployerApprovalExpires(
					this.getTime_untilEmployerApprovalExpires(application.getExpirationDate()));
			
			applicationDto.setApplicationDtos_conflicting(
					this.getApplicationDtos_Conflicting(userId,
											application.getApplicationId(),
											applicationDto.getJobDto().getWorkDays()));
			
			applicationDtos.add(applicationDto);
		}
		
		return applicationDtos;
	}

	private List<ApplicationDTO> getApplicationDtos_Conflicting(int userId, int reference_applicationId, List<WorkDay> workDays) {
		
		List<ApplicationDTO> applicationDtos_conflicting = new ArrayList<ApplicationDTO>();
		
		if(workDays != null && workDays.size() > 0){
			
			List<Application> applications_conflicting = repository.getApplications_WithAtLeastOneWorkDay(userId, reference_applicationId, workDays);
			
			for(Application application : applications_conflicting){
				ApplicationDTO applicationDto = new ApplicationDTO();
				
				applicationDto.setApplication(application);
				applicationDto.getJobDto().setJob(jobService.getJob_ByApplicationId(application.getApplicationId()));
			
				applicationDtos_conflicting.add(applicationDto);
			}
						
		}
		
		return applicationDtos_conflicting;
	}


	public List<Application> getApplications_ByUserAndStatuses_OpenJobs(int userId, List<Integer> statuses) {

		if (statuses.size() > 0) {
			return repository.getApplications_ByUserAndStatuses_OpenJobs(userId, statuses);
		} else {
			return null;
		}

	}


	public List<Application> getApplicationsByUser(int userId) {
		return repository.getApplicationsByUser(userId);
	}

	public void acceptWageProposal_Employee(int wageProposalId, HttpSession session) {
		
		// Get the wage proposal
		WageProposal wageProposal = this.getWageProposal(wageProposalId);	
		
		
		// Verify:
		// 1) the proposal is proposed TO the session user (i.e. the applicant)
		// OR
		// 2) a)the proposal is pending the session user's approval
			// AND
			// b) the  the employer's acceptance is not expired
		if( isWageProposalCurrentlyProposedToUser(wageProposalId, session) || 
				(isWageProposalCurrentlyPendingApplicantsApproval(wageProposalId, session) &&
				!isEmployerAcceptanceExpired(wageProposal.getApplicationId()) )){

			JobSearchUser user = SessionContext.getUser(session);
			
			// Hire the applicant
			userService.hireApplicant(wageProposal);	
			
			// If necessary, cancel the applicant's conflicting applications
			List<ApplicationDTO> applicationDtos_conflicting = this.getApplicationDtos_Conflicting(
																		user.getUserId(),
																		wageProposal.getApplicationId(),
																		jobService.getWorkDays(wageProposal));
			
			for(ApplicationDTO applicationDto : applicationDtos_conflicting){
			
				this.updateApplicationStatus(applicationDto.getApplication().getApplicationId(),
												Application.STATUS_CANCELLED_DUE_TO_TIME_CONFLICT);
			}
		}

	}
	
	private boolean isEmployerAcceptanceExpired(int applicationId) {
		
		Application application = this.getApplication(applicationId);
		
		
		if(ChronoUnit.MINUTES.between(LocalDateTime.now(), application.getExpirationDate()) < 0) return true;
		else return false;
	}


	public void acceptWageProposal_Employer(int wageProposalId, HttpSession session,
										Integer days, Integer hours, Integer minutes) {
				
		
		
		if( isWageProposalCurrentlyProposedToUser(wageProposalId, session) &&
				(days != null || hours != null || minutes != null)){
			
			LocalDateTime employerAcceptedDate = LocalDateTime.now();
			LocalDateTime expirationDate = employerAcceptedDate;
			
			if(days != null) expirationDate = expirationDate.plusDays(days);
			if(hours != null) expirationDate = expirationDate.plusHours(hours);
			if(minutes != null) expirationDate = expirationDate.plusMinutes(minutes);
						
			repository.updateWageProposalStatus(wageProposalId, WageProposal.STATUS_PENDING_APPLICANT_APPROVAL);
			repository.updateApplication_PendingApplicantApproval(wageProposalId, employerAcceptedDate,
																expirationDate);
				
		
		}		
	}


	private boolean isWageProposalCurrentlyPendingApplicantsApproval(int wageProposalId, HttpSession session) {
		
		JobSearchUser user = SessionContext.getUser(session);
		WageProposal wp = this.getWageProposal(wageProposalId);
		
		// This is a somewhat counter intuitive.
		// Because a new wage proposal is NOT created when the employer 
		// accepts the applicant's proposal (and and expiration date is set),
		// but rather the status of the wage proposal sent BY the applicant is changed,
		// the wage proposal's "ProposedByUserId" will still be the
		// applicant's (i.e. the session user) user id.
		if(wp.getStatus() == WageProposal.STATUS_PENDING_APPLICANT_APPROVAL &&
				wp.getProposedByUserId() == user.getUserId()){
			
			return true;

		}
		else return false;
	}


	private boolean isWageProposalCurrentlyProposedToUser(int wageProposalId, HttpSession session) {
		
		JobSearchUser user = SessionContext.getUser(session);
		WageProposal wp = this.getWageProposal(wageProposalId);
		
		// Verify the wage proposal has not yet been acted upon
		if(!this.hasActionBeenTakenOnWageProposal(wp)){
			
			if(wp.getProposedToUserId() == user.getUserId()) return true;
			else return false;
		}
		else return false;
	}
	
	private boolean hasActionBeenTakenOnWageProposal(WageProposal wp) {
		
		if( wp.getStatus() == WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED ||
				wp.getStatus() == WageProposal.STATUS_VIEWED_BUT_NO_ACTION_TAKEN ){
			
			return false;
		}
		else return true;
	}


	public void updateWageProposalStatus(int wageProposalId, int status) {
		repository.updateWageProposalStatus(wageProposalId, status);
	}

	public void declineWageProposalStatus(int wageProposalId, HttpSession session) {

		if(isWageProposalCurrentlyProposedToUser(wageProposalId, session)){
			
			this.updateWageProposalStatus(wageProposalId, WageProposal.STATUS_DECLINED);

			// Is the below comment still relevant?
			// Will the employer wish to have this transparency?
			// Currently this transparency is not provided.
			// If it is demanded, then another application status will need
			// to be created in order to differentiate between the two cases. 
			// Review this later.
			// ****************************************
			// Old thought:
			// This status will allow the employer to see
			// whom he declined outright and those with whom he could not reach a
			// negotiation.
			// ****************************************
			WageProposal wp = this.getWageProposal(wageProposalId);
			Application application = this.getApplication(wp.getApplicationId());
			this.updateApplicationStatus(application.getApplicationId(), Application.STATUS_DECLINED);
			
		}
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
