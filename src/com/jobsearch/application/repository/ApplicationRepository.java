package com.jobsearch.application.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.VerificationServiceImpl;

@Repository
public class ApplicationRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	ApplicationServiceImpl applicationService;

	@Autowired
	VerificationServiceImpl verificationService;

	public List<Application> ApplicationRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<Application>() {
			@Override
			public Application mapRow(ResultSet rs, int rownumber) throws SQLException {

				Application application = new Application();
				application.setApplicationId(rs.getInt("ApplicationId"));
				application.setUserId(rs.getInt("UserId"));
				application.setJobId(rs.getInt("JobId"));
				application.setHasBeenViewed(rs.getInt("HasBeenViewed"));
				application.setStatus(rs.getInt("Status"));
				
				
				Timestamp ts_employerAcceptedDate = rs.getTimestamp("EmployerAcceptedDate");
				if(ts_employerAcceptedDate != null)
					application.setEmployerAcceptedDate(ts_employerAcceptedDate.toLocalDateTime());
				
				
				Timestamp ts_expirationDate = rs.getTimestamp("ExpirationDate");
				if(ts_expirationDate != null)
					application.setExpirationDate(ts_expirationDate.toLocalDateTime());

				return application;
			}
		});
	}

	public List<WageProposal> WageProposalRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<WageProposal>() {
			@Override
			public WageProposal mapRow(ResultSet rs, int rownumber) throws SQLException {

				WageProposal wageProposal = new WageProposal();
				wageProposal.setAmount(String.format("%.2f", rs.getFloat("Amount")));
				wageProposal.setApplicationId(rs.getInt("ApplicationId"));
				wageProposal.setId(rs.getInt("WageProposalId"));
				wageProposal.setStatus(rs.getInt("Status"));
				wageProposal.setProposedByUserId(rs.getInt("ProposedByUserId"));
				wageProposal.setProposedToUserId(rs.getInt("ProposedToUserId"));

				return wageProposal;
			}
		});
	}

	public List<Application> getApplicationsByUser(int userId) {
		String sql = "SELECT * FROM application WHERE UserId = ?";

		return this.ApplicationRowMapper(sql, new Object[] { userId });
	}

	public List<Application> getApplications_ByUserAndStatuses_OpenJobs(int userId, List<Integer> statuses) {

		List<Object> argsList = new ArrayList<Object>();

		String sql = "SELECT * FROM application a"
				+ " INNER JOIN job j ON j.JobId = a.JobId"
				+ " WHERE a.UserId = ? AND j.Status < 2";

		argsList.add(userId);


		// Build or string for statuses
		boolean isFirst = true;
		for (int status : statuses) {
			if (isFirst) {
				sql += " AND (a.Status = ?";
				isFirst = false;
			} else {
				sql += " OR a.Status = ?";
			}

			argsList.add(status);
		}

		sql += ")";

		return this.ApplicationRowMapper(sql, argsList.toArray());
	}

	public List<Application> getApplicationDtos_ByJob_OpenApplications(int jobId) {

		String sql = "SELECT * FROM application a"
						+ " INNER JOIN user u ON u.UserId = a.UserId"
						+ " WHERE a.JobId = ?"
						+ " AND (a.Status != ?"
						+ " AND a.Status != ?"
						+ " AND a.Status != ? )";

		return ApplicationRowMapper(sql, new Object[] { jobId, Application.STATUS_ACCEPTED,
																Application.STATUS_DECLINED,
																Application.STATUS_CANCELLED_DUE_TO_TIME_CONFLICT});

	}

	public Application getApplication(int jobId, int userId) {
		
		String sql = "SELECT * FROM application a"
					+ " WHERE a.JobId = ?"
					+ " AND a.UserId = ?";

		List<Application> applications = this.ApplicationRowMapper(sql, new Object[] { jobId, userId });
//		return  jdbcTemplate.queryForObject(sql, new Object[] { jobId, userId }, Application.class);

		if (verificationService.isListPopulated(applications)) return applications.get(0);
		else return null;

	}

	public void updateApplicationStatus(int applicationId, int status) {
		String sql = "UPDATE application SET Status = ? WHERE ApplicationId = ?";
		jdbcTemplate.update(sql, new Object[] { status, applicationId });

	}
	
	public void updateApplication_PendingApplicantApproval(int wageProposalId,
																LocalDateTime employerAcceptedDate,
																LocalDateTime expirationDate) {
	
		String sql = "UPDATE application a"
					+ " INNER JOIN wage_proposal wp on wp.ApplicationId = a.ApplicationId"
					+ " SET a.Status = ?, a.EmployerAcceptedDate = ?, a.ExpirationDate = ?"
					+ " WHERE wp.WageProposalId = ?";
		
		jdbcTemplate.update(sql, new Object[]{ Application.STATUS_WAITING_FOR_APPLICANT_APPROVAL,
												employerAcceptedDate.toString(),
												expirationDate.toString(),
												wageProposalId });
		
	}

	public Application getApplication(int applicationId) {
		String sql = "SELECT * FROM application WHERE ApplicationId = ?";
		return this.ApplicationRowMapper(sql, new Object[] { applicationId }).get(0);

	}

	public void addAnswer(Answer answer) {

		String sql = "INSERT INTO answer (QuestionId, UserId, Text, AnswerOptionId) VALUES(?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { answer.getQuestionId(), answer.getUserId(), answer.getText(),
				answer.getAnswerOptionId() });

	}

	public List<Question> getQuestions(int id) {
		String sql = "SELECT * FROM question WHERE JobId = ? ORDER BY QuestionId ASC";
		return this.QuestionRowMapper(sql, new Object[] { id });
	}

	public List<AnswerOption> getAnswerOptions(int questionId) {

		String sql = "SELECT * FROM answer_option WHERE QuestionId = ?";
		return this.AnswerOptionRowMapper(sql, new Object[] { questionId });

	}

	public Answer getAnswer(int questionId, int userId) {

		String sql = "SELECT * FROM answer WHERE QuestionId = ? AND UserId = ?";
		return this.AnswerRowMapper(sql, new Object[] { questionId, userId }).get(0);

	}

	public List<Answer> getAnswers(int questionId, int userId) {

		String sql = "SELECT * FROM answer WHERE QuestionId = ? AND UserId = ?";
		return this.AnswerRowMapper(sql, new Object[] { questionId, userId });
	}

	public List<AnswerOption> AnswerOptionRowMapper(String sql, Object[] args) {

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<AnswerOption>() {

				@Override
				public AnswerOption mapRow(ResultSet rs, int rownumber) throws SQLException {
					AnswerOption e = new AnswerOption();
					e.setAnswerOptionId(rs.getInt("AnswerOptionId"));
					e.setText(rs.getString("AnswerOption"));

					return e;
				}
			});

		} catch (Exception e) {
			return null;
		}

	}

	public List<Question> QuestionRowMapper(String sql, Object[] args) {

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<Question>() {

				@Override
				public Question mapRow(ResultSet rs, int rownumber) throws SQLException {
					Question e = new Question();
					e.setQuestionId(rs.getInt("QuestionId"));
					e.setFormatId(rs.getInt("FormatId"));
					e.setText(rs.getString("Question"));
					e.setJobId(rs.getInt("JobId"));
					
					e.setAnswerOptions(applicationService.getAnswerOptions(e.getQuestionId()));

					return e;
				}
			});

		} catch (Exception e) {
			return null;
		}

	}

	public List<Answer> AnswerRowMapper(String sql, Object[] args) {

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<Answer>() {

				@Override
				public Answer mapRow(ResultSet rs, int rownumber) throws SQLException {
					Answer e = new Answer();
					e.setAnswerOptionId(rs.getInt("AnswerOptionId"));
					e.setQuestionId(rs.getInt("QuestionId"));
					e.setText(rs.getString("Text"));
					e.setUserId(rs.getInt("UserId"));

					
					if (e.getAnswerOptionId() > 0) {
						
						//Set the answer text equal to the selected answer option's text
						e.setText(applicationService.getAnswerOption(e.getAnswerOptionId()).getText());
					}

					return e;
				}
			});

		} catch (Exception e) {
			return null;
		}

	}

	public void addQuestion(Question question) {

		CallableStatement cStmt;
		try {
			cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall("{call insert_question(?, ?, ?)}");

			cStmt.setString(1, question.getText());
			cStmt.setInt(2, question.getFormatId());
			cStmt.setInt(3, question.getJobId());

			ResultSet result = cStmt.executeQuery();
			result.next();
			int createdQuestionId = result.getInt(("QuestionId"));

			if (question.getAnswerOptions() != null) {
				String sql = "INSERT INTO answer_option (QuestionId, AnswerOption) VALUES (?, ?)";
				for (AnswerOption answerOption : question.getAnswerOptions()) {
					jdbcTemplate.update(sql, new Object[] { createdQuestionId, answerOption.getText() });
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public void addWageProposal(WageProposal wageProposal) {
		String sql = "INSERT INTO wage_proposal (ApplicationId, ProposedByUserId, ProposedToUserId, Amount, Status)"
				+ " VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, new Object[] { wageProposal.getApplicationId(),
													wageProposal.getProposedByUserId(),
													wageProposal.getProposedToUserId(),
													wageProposal.getAmount(),
													WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED });

	}

	public void addApplication(int jobId, int userId) {
		String sql = "INSERT INTO application (UserId, JobId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public void insertApplication(ApplicationDTO applicationDto) {

		try {

			// Insert the application
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insert_application(?, ?, ?, ?, ?)}");

			cStmt.setInt(1, applicationDto.getApplicantId());
			cStmt.setInt(2, applicationDto.getJobId());
			cStmt.setInt(3, applicationDto.getApplication().getStatus());
			
			if(applicationDto.getApplication().getEmployerAcceptedDate() != null){
				cStmt.setTimestamp(4, Timestamp.valueOf(applicationDto.getApplication().getEmployerAcceptedDate()));	
			}
			else{
				cStmt.setTimestamp(4, null);
			}
			
			if(applicationDto.getApplication().getExpirationDate() != null){
				cStmt.setTimestamp(5, Timestamp.valueOf(applicationDto.getApplication().getExpirationDate()));	
			}
			else{
				cStmt.setTimestamp(5, null);
			}
			
			
			

			ResultSet result = cStmt.executeQuery();

			// Get the new application id from the result
			result.next();
			int newApplicationId = result.getInt("ApplicationId");

			// Update the application DTO's wage proposal's application id
			applicationDto.getWageProposal().setApplicationId(newApplicationId);

			// Add the wage proposal
			applicationService.addWageProposal(applicationDto.getWageProposal());

			// Add answers
			if(verificationService.isListPopulated(applicationDto.getAnswers())){
				for (Answer answer : applicationDto.getAnswers()) {
					answer.setUserId(applicationDto.getApplicantId());
					applicationService.addAnswer(answer);

				}	
			}
			
			// Add the work days the applicant applied for						
			applicationService.insertApplicationWorkDays(newApplicationId,
															applicationDto.getJobId(),
															applicationDto.getAvailableDays());


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<WageProposal> getWageProposals(int applicationId) {
		String sql = "SELECT * FROM wage_proposal WHERE ApplicationId = ? ORDER BY WageProposalId ASC";
		return WageProposalRowMapper(sql, new Object[] { applicationId });
	}

	public WageProposal getWageProposal(int wageProposalId) {
		String sql = "SELECT * FROM wage_proposal WHERE WageProposalId = ?";
		return WageProposalRowMapper(sql, new Object[] { wageProposalId }).get(0);
	}

	public WageProposal getCurrentWageProposal(int applicationId) {

		String sql = "SELECT * FROM wage_proposal" + " WHERE WageProposalId = "
				+ "(SELECT MAX(WageProposalId) FROM wage_proposal WHERE ApplicationId = ?)";

		List<WageProposal> result = this.WageProposalRowMapper(sql, new Object[] { applicationId });
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public float getCurrentWageProposedBy(int applicationId, int proposedById) {

		String sql = "SELECT amount FROM wage_proposal" + " WHERE WageProposalId = "
				+ "(SELECT MAX(WageProposalId) FROM wage_proposal WHERE ApplicationId = ? AND ProposedByUserId = ?)";

		return jdbcTemplate.queryForObject(sql, new Object[] { applicationId, proposedById }, float.class);
	}

	public void updateWageProposalStatus(int wageProposalId, int status) {
		String sql = "UPDATE wage_proposal SET Status = ? WHERE WageProposalId = ?";
		jdbcTemplate.update(sql, new Object[] { status, wageProposalId });

	}

	public float getCurrentWageProposedTo(int applicationId, int proposedToUserId) {

		String sql = "SELECT amount FROM wage_proposal" + " WHERE WageProposalId = "
				+ "(SELECT MAX(WageProposalId) FROM wage_proposal WHERE ApplicationId = ? AND ProposedToUserId = ?)";

		return jdbcTemplate.queryForObject(sql, new Object[] { applicationId, proposedToUserId }, float.class);

	}

	public List<WageProposal> getFailedWageProposalsByJob(int jobId) {

		// Inner query: find all the application ids for the job id.
		// Outer query: with these application ids, find the wage proposals that
		// have been declined (i.e. status = 2)
		String sql = "SELECT * FROM wage_proposal w WHERE w.ApplicationId IN("
				+ " SELECT ApplicationId FROM application a"
				+ " INNER JOIN job j ON a.JobId = j.JobId WHERE j.JobId = ?" + ") AND w.Status = 2";

		return WageProposalRowMapper(sql, new Object[] { jobId });
	}

	public double getWage(int applicationId) {
		String sql = "SELECT Amount FROM Wage_Proposal WHERE ApplicationId = ? AND Status = 1";
		
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { applicationId }, Double.class);	
		} catch (Exception e) {
			return -1;
		}
		
	}

	public List<WageProposal> getFailedWageProposalsByUser(int userId) {

		// Inner query: find all application ids for the user where the job is
		// still accepting applications
		String sql = "SELECT * FROM wage_proposal w WHERE w.ApplicationId IN("
				+ " SELECT ApplicationId FROM application a"
				+ " INNER JOIN Job j ON a.JobId = j.JobId WHERE a.UserId = ? AND j.IsAcceptingApplications = 1"
				+ ") AND w.Status = 2";

		return WageProposalRowMapper(sql, new Object[] { userId });
	}

	public List<Answer> getAnswersByJobAndUser(int jobId, int userId) {
		String sql = "SELECT * " + " FROM answer a" + " WHERE a.UserId = ? AND a.QuestionId"
				+ " IN (SELECT q.QuestionId FROM question q WHERE q.JobId = ?)";

		return this.AnswerRowMapper(sql, new Object[] { userId, jobId });

	}

	public AnswerOption getAnswerOption(int answerOptionId) {
		String sql = "SELECT * FROM answer_option WHERE AnswerOptionId = ?";
		return AnswerOptionRowMapper(sql, new Object[] { answerOptionId }).get(0);
	}

	public void updateHasBeenViewed(Integer jobId, int value) {
		String sql = "UPDATE application SET HasBeenViewed = ? where jobId = ?";
		jdbcTemplate.update(sql, new Object[] { value, jobId });
	}

	public int getCountWageProposal_Sent(Integer jobId, int userId) {

		String sql = "SELECT COUNT(*) FROM wage_proposal w"
					+ " INNER JOIN application a ON a.applicationId = w.applicationId"
					+ " INNER JOIN job j ON j.jobId = a.JobId"
					+ " WHERE j.JobId = ? AND w.ProposedByUserId = ?"
					+ " AND ( w.Status = ? OR w.Status = ? )";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId,  userId,
											WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED,
											WageProposal.STATUS_VIEWED_BUT_NO_ACTION_TAKEN },
											Integer.class);
	}

	public int getCountWageProposal_Received(Integer jobId, int userId) {
	
		// This includes the "New" wage proposals
		
		String sql = "SELECT COUNT(*) FROM wage_proposal w"
				+ " INNER JOIN application a ON a.applicationId = w.applicationId"
				+ " INNER JOIN job j ON j.jobId = a.JobId"
				+ " WHERE j.JobId = ? AND w.ProposedToUserId = ?"
				+ " AND ( w.Status = ? OR w.status = ? )";
	
		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId,  userId,
											WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED,
											WageProposal.STATUS_VIEWED_BUT_NO_ACTION_TAKEN },
											Integer.class);
	}
	

	public int getCountWageProposal_Received_New(Integer jobId, int userId) {
	
		String sql = "SELECT COUNT(*) FROM wage_proposal w"
				+ " INNER JOIN application a ON a.applicationId = w.applicationId"
				+ " INNER JOIN job j ON j.jobId = a.JobId"
				+ " WHERE j.JobId = ? AND w.ProposedToUserId = ?"
				+ " AND w.Status = ?";
	
		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId,  userId,
											WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED },
											Integer.class);

	}

	public void updateWageProposalsStatus_ToViewedButNoActionTaken(Integer jobId) {
		
		String sql = "UPDATE wage_proposal w"
						 + " INNER JOIN application a ON w.ApplicationId = a.ApplicationId"
						 + " INNER JOIN job j on j.JobId = a.JobId"
						 + " SET w.Status = ?"
						 + " WHERE j.jobid = ?"
						 + " AND w.status = ?";
		 
		 jdbcTemplate.update(sql, new Object[]{ WageProposal.STATUS_VIEWED_BUT_NO_ACTION_TAKEN,
				 								jobId, WageProposal.STATUS_SUBMITTED_BUT_NOT_VIEWED });


		
	}

	public List<Question> getQuestions_ByEmployer(int userId) {
		
		String sql = "SELECT * FROM question q"
						+ " INNER JOIN job j ON j.JobId = q.JobId"
						+ " WHERE j.UserId = ?";
		
		return QuestionRowMapper(sql, new Object[]{ userId });

		
	}

	public Question getQuestion(int questionId) {
		
		String sql = "SELECT * FROM question WHERE QuestionId = ?";
		return QuestionRowMapper(sql, new Object[] { questionId }).get(0);
	}

	public Integer getCount_Employment_ByUserAndJob(int userId, int jobId) {

		String sql = "SELECT COUNT(*) FROM employment WHERE UserId = ? AND JobId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{ userId ,  jobId }, Integer.class);
	}

	public List<Integer> getAnswerOptionIds_Selected_ByApplicantAndJob(int userId, int jobId) {
		String sql = "SELECT AnswerOptionId FROM answer a"
					+ " INNER JOIN question q ON q.QuestionId = a.QuestionId"
					+ " WHERE q.JobId = ? AND a.UserId = ?";
		
		return jdbcTemplate.queryForList(sql, new Object[]{ jobId, userId }, Integer.class);
	}

	public List<Application> getApplications_WithAtLeastOneWorkDay(int userId, int reference_applicationId,
			List<WorkDay> workDays) {
		
		String sql = "SELECT * FROM application a WHERE a.ApplicationId IN("
					+ " SELECT DISTINCT a.ApplicationId FROM application a"
					+ " INNER JOIN job j on j.JobId = a.JobId"
					+ " INNER JOIN work_day wd on wd.JobId = j.JobId"
					+ " WHERE a.UserId = ? AND a.ApplicationId != ?"
					+ " AND ( a.Status = ? OR a.Status = ? OR a.Status = ? )"
					+ " AND (";
			
		List<Object> args = new ArrayList<Object>();
		args.add(userId);
		args.add(reference_applicationId);
		args.add(Application.STATUS_SUBMITTED);
		args.add(Application.STATUS_CONSIDERED);
		args.add(Application.STATUS_WAITING_FOR_APPLICANT_APPROVAL);
		
		boolean isFirst = true;
		for(WorkDay wd : workDays){
			
			if(!isFirst) sql += " OR ";			
			sql += " ( wd.DateId = ? AND wd.StartTime <= ? AND wd.EndTime >= ? )";
			
			args.add(wd.getDateId());
			args.add(wd.getStringEndTime());
			args.add(wd.getStringStartTime());
			
			isFirst = false;					
		}
		
		sql += "))";
		
		return ApplicationRowMapper(sql, args.toArray());
	}

	public void insertApplicationWorkDay(int applicationId, Integer workDayId) {
		String sql = "INSERT INTO application_work_day (ApplicationId, WorkDayId) VALUES (?, ?)";
		jdbcTemplate.update(sql, new Object[]{ applicationId, workDayId } );
		
	}

	public List<String> getAvailableDays_byApplication(int applicationId) {


		String sql = "Select d.Date FROM date d"
					+ " INNER JOIN work_day wd on wd.DateId = d.Id"
					+ " INNER JOIN application_work_day a_wd ON a_wd.WorkDayId = wd.WorkDayId"
					+ " WHERE a_wd.ApplicationId = ?";
		
		return jdbcTemplate.queryForList(sql, new Object[]{ applicationId }, String.class);
	}



}
