package com.jobsearch.application.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationDTO;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.WageProposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.model.application.ApplicationInvite;
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
				application.setIsNew(rs.getInt("IsNew"));
				application.setIsOpen(rs.getInt("IsOpen"));
				application.setIsAccepted(rs.getInt("IsAccepted"));
				application.setStatus(rs.getInt("Status"));
				
				application.setFlag_employerInitiatedContact(
						rs.getInt(Application.FLAG_EMPLOYER_INITIATED_CONTACT));
				application.setFlag_closedDueToAllPositionsFilled(
						rs.getInt(Application.FLAG_CLOSED_DUE_TO_ALL_POSITIONS_FILLED));
				application.setFlag_applicantAcknowledgedAllPositionsAreFilled(
						rs.getInt(Application.FLAG_APPLICANT_ACKNOWLEDGED_ALL_POSITIONS_ARE_FILLED));
								
				return application;
			}
		});
	}
	
	
	public List<ApplicationInvite> ApplicationInviteRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<ApplicationInvite>() {
			@Override
			public ApplicationInvite mapRow(ResultSet rs, int rownumber) throws SQLException {

				ApplicationInvite applicationInvite = new ApplicationInvite();
				applicationInvite.setApplicationInviteId(rs.getInt("ApplicationInviteId"));
				applicationInvite.setJobId(rs.getInt("JobId"));
				applicationInvite.setUserId(rs.getInt("UserId"));

				return applicationInvite;
			}
		});
	}	

	public List<EmploymentProposalDTO> EmploymentProposalDTOMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<EmploymentProposalDTO>() {
			@Override
			public EmploymentProposalDTO mapRow(ResultSet rs, int rownumber) throws SQLException {

				EmploymentProposalDTO e = new EmploymentProposalDTO();
				
				e.setAmount(String.format("%.2f", rs.getFloat("Amount")));
				e.setApplicationId(rs.getInt("ApplicationId"));
				e.setEmploymentProposalId(rs.getInt("WageProposalId"));
				e.setStatus(rs.getInt("Status"));
				e.setProposedByUserId(rs.getInt("ProposedByUserId"));
				e.setProposedToUserId(rs.getInt("ProposedToUserId"));
				e.setIsNew(rs.getInt("IsNew"));
				
				
				String expirationDate = rs.getString("ExpirationDate");
				if(expirationDate != null){
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
					e.setExpirationDate(LocalDateTime.parse(
							expirationDate, formatter));	
				}
				
				
				e.setFlag_isCanceledDueToApplicantAcceptingOtherEmployment(
						rs.getInt(EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT));			
				e.setFlag_isCanceledDueToEmployerFillingAllPositions(
						rs.getInt(EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS));				
				e.setFlag_applicationWasReopened(rs.getInt(EmploymentProposalDTO.FLAG_APPLICATION_WAS_REOPENED));
				e.setFlag_aProposedWorkDayWasRemoved(rs.getInt(EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED));
				e.setFlag_aProposedWorkDayTimeWasEdited(rs.getInt(EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED));
				e.setFlag_employerInitiatedContact(rs.getInt(EmploymentProposalDTO.FLAG_EMPLOYER_INITIATED_CONTACT));
				e.setFlag_employerAcceptedTheOffer(rs.getInt("Flag_EmployerAcceptedTheOffer"));
				
				
				Timestamp ts_employerAcceptedDate = rs.getTimestamp("EmployerAcceptedDate");
				if(ts_employerAcceptedDate != null)
					e.setEmployerAcceptedDate(ts_employerAcceptedDate.toLocalDateTime());
								
				Timestamp ts_expirationDate = rs.getTimestamp("ExpirationDate");
				if(ts_expirationDate != null)
					e.setExpirationDate(ts_expirationDate.toLocalDateTime());				
				
				e.setDateStrings_proposedDates(
										applicationService.getProposedDateStrings(
												e.getEmploymentProposalId()));
				
				e.setTime_untilEmployerApprovalExpires(
						applicationService.getTime_untilEmployerApprovalExpires(e.getExpirationDate()));

				
				return e;
			}
		});
	}
	
	public List<Application> getApplications_ByJob_OpenApplications(int jobId) {

		String sql = "SELECT * FROM application a"
						+ " INNER JOIN user u ON u.UserId = a.UserId"
						+ " WHERE a.JobId = ?"
						+ " AND a.IsOpen = 1";

		return ApplicationRowMapper(sql, new Object[] { jobId });

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

	public void insertEmploymentProposal(EmploymentProposalDTO employmentProposalDto) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insert_employment_proposal(?, ?, ?, ?, ?, ? , ?)}");

			cStmt.setInt(1, employmentProposalDto.getApplicationId());
			cStmt.setInt(2, employmentProposalDto.getProposedByUserId());
			cStmt.setInt(3, employmentProposalDto.getProposedToUserId());
			cStmt.setFloat(4, Float.valueOf(employmentProposalDto.getAmount()));
			cStmt.setInt(5, employmentProposalDto.getStatus());
			
			if(employmentProposalDto.getEmployerAcceptedDate() != null &&
					employmentProposalDto.getExpirationDate() != null){
				
				cStmt.setTimestamp(6, Timestamp.valueOf(employmentProposalDto.getEmployerAcceptedDate()));
				cStmt.setTimestamp(7, Timestamp.valueOf(employmentProposalDto.getExpirationDate()));
			}else{
				cStmt.setTimestamp(6, null);
				cStmt.setTimestamp(7, null);
				
			}
			
			ResultSet result = cStmt.executeQuery();		
			result.next();
			employmentProposalDto.setEmploymentProposalId(result.getInt("WageProposalId"));

			applicationService.insertEmploymentProsalWorkDays(employmentProposalDto);		
			
		} catch (Exception e) {
			// TODO: handle exception
			int i = 0;
		}		
	}

	public void insertApplication(ApplicationDTO applicationDto) {

		try {

			// Insert the application
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insert_application(?, ?, ?)}");

			cStmt.setInt(1, applicationDto.getApplicantId());
			cStmt.setInt(2, applicationDto.getJobId());
			cStmt.setInt(3, applicationDto.getApplication().getStatus());
	
			
			ResultSet result = cStmt.executeQuery();

			// Get the new application id from the result
			result.next();
			Integer newApplicationId = result.getInt("ApplicationId");

			if(newApplicationId != null){
			
				// If this was NOT an invite to apply, insert the employment proposal
//				if(applicationDto.getApplication().getStatus() != Application.STATUS_PROPOSED_BY_EMPLOYER){
					applicationDto.getEmploymentProposalDto().setApplicationId(newApplicationId);
					applicationService.insertEmploymentProposal(applicationDto.getEmploymentProposalDto());	
//				}			
	
				// Add answers  
				if(verificationService.isListPopulated(applicationDto.getAnswers())){
					for (Answer answer : applicationDto.getAnswers()) {
						answer.setUserId(applicationDto.getApplicantId());
						applicationService.addAnswer(answer);
	
					}	
				}
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public EmploymentProposalDTO getCurrentEmploymentProposal(Integer applicationId) {
	
		String sql = "SELECT * FROM wage_proposal WHERE IsCurrentProposal = 1 AND ApplicationId = ?";

		List<EmploymentProposalDTO> result = this.EmploymentProposalDTOMapper(sql, new Object[] { applicationId });
		
		if (verificationService.isListPopulated(result)) return result.get(0);
		else return null;
	}

	public void updateWageProposalStatus(int wageProposalId, int status) {
		String sql = "UPDATE wage_proposal SET Status = ? WHERE WageProposalId = ?";
		jdbcTemplate.update(sql, new Object[] { status, wageProposalId });

	}

	public AnswerOption getAnswerOption(int answerOptionId) {
		String sql = "SELECT * FROM answer_option WHERE AnswerOptionId = ?";
		return AnswerOptionRowMapper(sql, new Object[] { answerOptionId }).get(0);
	}

	public void updateIsNew(Integer jobId, int value) {
		String sql = "UPDATE application SET IsNew = ? WHERE JobId = ?";
		jdbcTemplate.update(sql, new Object[] { value, jobId });
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

	public List<Question> getDistinctQuestions_byEmployer(int userId) {
		
		String sql = "SELECT * FROM question q"
						+ " INNER JOIN job j ON j.JobId = q.JobId"
						+ " WHERE j.UserId = ?"
						+ " GROUP BY q.question";
		
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

	public List<Application> getApplications_byJobAndAtLeastOneWorkDay(int jobId, List<WorkDay> workDays) {
			
		String sql = "SELECT * FROM application a WHERE a.ApplicationId IN("
					+ " SELECT DISTINCT a.ApplicationId FROM application a"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				    + " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				    + " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE a.JobId = ?"
					+ " AND a.IsOpen = 1"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND (";
			
		List<Object> args = new ArrayList<Object>();
		args.add(jobId);
		
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
	
	public List<Application> getAcceptedApplications_byJobAndAtLeastOneWorkDay(int jobId, List<WorkDay> workDays) {
		
		String sql = "SELECT * FROM application a WHERE a.ApplicationId IN("
					+ " SELECT DISTINCT a.ApplicationId FROM application a"
					+ " JOIN employment e ON a.UserId = e.UserId AND a.JobId = e.JobId"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				    + " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				    + " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE e.JobId = ?"
					+ " AND e.WasTerminated = 0"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND (";
			
		List<Object> args = new ArrayList<Object>();
		args.add(jobId);
		
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

	public List<Application> getApplications_WithAtLeastOneWorkDay(int userId, int reference_applicationId,
			List<WorkDay> workDays) {
		
		String sql = "SELECT * FROM application a WHERE a.ApplicationId IN("
					+ " SELECT DISTINCT a.ApplicationId FROM application a"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				    + " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				    + " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE a.UserId = ?"
				    + " AND a.ApplicationId != ?"
					+ " AND a.IsOpen = 1"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND (";
			
		List<Object> args = new ArrayList<Object>();
		args.add(userId);
		args.add(reference_applicationId);

		
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

	public List<Application> getOpenApplications__byJob_withAtLeastOneWorkDay(int jobId, List<WorkDay> workDays) {
		
		String sql = "SELECT * FROM application a WHERE a.ApplicationId IN("
					+ " SELECT DISTINCT a.ApplicationId FROM application a"
					+ " JOIN job j ON a.JobId = j.JobId"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				    + " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				    + " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE j.JobId = ?"
					+ " AND a.IsOpen = 1"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND (";
			
		List<Object> args = new ArrayList<Object>();
		args.add(jobId);
		
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
	
	public List<String> getProposedDateStrings(int employmentProposalId) {
		
		// **************************************************************************
		//Now that the user needs to specify the work days when applying to
		//jobs with partial availability, can this method be removed????
		// Review this.
		// **************************************************************************
		String sql = "Select d.Date FROM date d"
					+ " INNER JOIN work_day wd on wd.DateId = d.Id"
					+ " INNER JOIN employment_proposal_work_day ep_wd ON ep_wd.WorkDayId = wd.WorkDayId"
					+ " WHERE ep_wd.EmploymentProposalId = ?"
					+ " ORDER BY d.Id ASC";
		
		return jdbcTemplate.queryForList(sql, new Object[]{ employmentProposalId }, String.class);
	}

	public void insertEmploymentProsalWorkDay(int employmentProposalId, int workDayId) {
	
		String sql = "INSERT INTO employment_proposal_work_day (EmploymentProposalId, WorkDayId)"
						+ " VALUES (?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{ employmentProposalId, workDayId } );
		
	}

	public List<ApplicationInvite> getApplicationInvites(int userId) {
		
		String sql = "SELECT * FROM application_invite WHERE UserId = ?";

		return ApplicationInviteRowMapper(sql, new Object[]{ userId } );
	}


	public void insertApplicationInvite(ApplicationInvite applicationInvite) {
		
		String sql = "INSERT INTO application_invite (JobId, UserId, Status) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[]{ applicationInvite.getJobId(),
											   applicationInvite.getUserId(),
											   applicationInvite.getStatus() });
		
	}


	public EmploymentProposalDTO getEmploymentProposalDto(int employmentProposalId) {

		String sql = "SELECT * FROM wage_proposal wp WHERE wp.WageProposalId = ?"; 

		return this.EmploymentProposalDTOMapper(sql, new Object[]{ employmentProposalId }).get(0);
	}

	public Integer getCount_applicantsByDay(int dateId, int jobId) {

		String sql = "SELECT COUNT(a.ApplicationId)";
		sql += " FROM application a";
		sql += " INNER JOIN wage_proposal wp ON wp.ApplicationId = a.ApplicationId";
		sql += " INNER JOIN employment_proposal_work_day e ON e.EmploymentProposalId = wp.WageProposalId";
		sql += " INNER JOIN work_day wd ON wd.WorkDayId = e.WorkDayId";
		sql += " WHERE wd.DateId = ?";
		sql += " AND a.IsOpen = 1";
		sql += " AND a.JobId = ?";
//		sql += " AND wp.WageProposalId IN ( "
//				+ "SELECT MAX(wp1.WageProposalId) FROM wage_proposal wp1 WHERE wp1.ApplicationId = ? )";
		sql += " AND wp.IsCurrentProposal = 1";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ dateId, jobId }, Integer.class);
	}

	public Integer getCount_positionsFilledByDay(int dateId, int jobId) {
		
		String sql = "SELECT COUNT(e.Id)";
		sql += " FROM employment e";
		sql += " INNER JOIN application a ON a.JobId = e.JobId AND a.UserId = e.UserId";
		sql += " INNER JOIN wage_proposal wp ON wp.ApplicationId = a.ApplicationId";
		sql += " INNER JOIN employment_proposal_work_day e_p ON e_p.EmploymentProposalId = wp.WageProposalId";
		sql += " INNER JOIN work_day wd ON wd.WorkDayId = e_p.WorkDayId";
		sql += " WHERE wd.DateId = ?";
		sql += " AND e.JobId = ?";
		sql += " AND e.WasTerminated = 0";
		sql += " AND wp.IsCurrentProposal = 1";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ dateId, jobId }, Integer.class);
	}

	public void updateWageProposal_isCurrentProposal(Integer employmentProposalId, int isCurrentProposal) {
		String sql = "UPDATE wage_proposal SET IsCurrentProposal = ? WHERE WageProposalId = ?";
		jdbcTemplate.update(sql, new Object[]{ isCurrentProposal, employmentProposalId });		
	}

	public List<Application> getApplications_byJobAndDate(int jobId, int dateId) {
		String sql = "SELECT * FROM application a";
				sql += " INNER JOIN wage_proposal wp ON wp.ApplicationId = a.ApplicationId";
				sql += " INNER JOIN employment_proposal_work_day e_p ON e_p.EmploymentProposalId = wp.WageProposalId";
				sql += " INNER JOIN work_day wd ON wd.WorkDayId = e_p.WorkDayId";
				sql += " WHERE a.IsOpen = 1";
				sql += " AND a.JobId = ?";
				sql += " AND wp.IsCurrentProposal = 1";
				sql += " AND wd.DateId = ?";
				
		return ApplicationRowMapper(sql, new Object[]{ jobId, dateId });
	}

	public Integer getCount_ProposedDay_byApplicationAndWorkDay(int applicationId, int workDayId) {
		
		String sql = "SELECT COUNT(Id) FROM employment_proposal_work_day ep";
		sql += " INNER JOIN wage_proposal wp ON ep.EmploymentProposalId = wp.WageProposalId";
		sql += " WHERE wp.ApplicationId = ?";
		sql += " AND wp.IsCurrentProposal = 1";
		sql += " AND ep.WorkDayId = ?";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ applicationId, workDayId }, Integer.class);
	}
	
	public List<Application> getApplications_byUser_openOrAccepted(int userId) {
		
		String sql = "SELECT * FROM application a WHERE a.ApplicationId IN ("
				
					// Jobs that allow partial availability
					+ "	SELECT a.ApplicationId FROM application a"
					+ " LEFT JOIN employment e ON a.JobId = e.JobId AND a.UserId = e.UserId" // do not include any employment has that has been terminated (LEFT JOIN to include NULL employment records)
					+ " JOIN job j ON a.JobId = j.JobId"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
					+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
					+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE wp.IsCurrentProposal = 1"
//					+ " AND j.IsPartialAvailabilityAllowed = 1"
					+ " AND wd.IsComplete = 0"
					+ " AND a.UserId = ?"
					+ " AND ( e.WasTerminated = 0 OR e.WasTerminated is NULL )"
					+ " AND ( a.IsOpen = 1 OR a.IsAccepted = 1)"
					
//					+ " UNION"
					
//					// Jobs that DO NOT allow partial availability
//					+ "	SELECT a.ApplicationId FROM application a"
//					+ " JOIN job j ON a.JobId = j.JobId"
//					+ " LEFT JOIN employment e ON j.JobId = e.JobId" // do not include any employment has that has been terminated (LEFT JOIN to include NULL employment records)
//					+ " JOIN work_day wd ON j.JobId = wd.JobId"
//					+ " AND j.IsPartialAvailabilityAllowed = 0"
//					+ " AND wd.IsComplete = 0"
//					+ " AND a.UserId = ?"
//					+ " AND ( e.WasTerminated = 0 OR e.WasTerminated is NULL )"
//					+ " AND ( a.IsOpen = 1 OR a.IsAccepted = 1)"					
					+ " )";
		
		return ApplicationRowMapper(sql, new Object[]{ userId });
	}

	public List<Application> applications_closedDueToAllPositionsFilled_unacknowledged(int userId) {
	
		String sql = "SELECT * FROM application a"
				+ " JOIN job j ON a.JobId = j.JobId"
				+ " WHERE a.UserId = ?"
				+ " AND j.Status != ?"
				+ " AND ( a.Flag_ClosedDueToAllPositionsFilled = 1 AND"
				+ " a.Flag_ApplicantAcknowledgedAllPositionsAreFilled = 0 )";
	
	return ApplicationRowMapper(sql, new Object[]{ userId, Job.STATUS_PAST });
	}
	
	public void closeApplication(int applicationId) {
		String sql = "UPDATE application SET IsOpen = 0 WHERE ApplicationId = ?";
		jdbcTemplate.update(sql, new Object[]{ applicationId });
		
	}

	public void openApplication(int applicationId) {
		String sql = "UPDATE application SET IsOpen = 1 WHERE ApplicationId = ?";
		jdbcTemplate.update(sql, new Object[]{ applicationId });
	
	}

	public EmploymentProposalDTO getPreviousProposal(Integer referenceEmploymentProposalId, int applicationId) {
		String sql = "SELECT * FROM wage_proposal"
					+ " WHERE WageProposalId IN ("
					+ " SELECT MAX(WageProposalId) FROM wage_proposal"
					+ " WHERE WageProposalId != ?"
					+ " AND ApplicationId = ?"
					+ ")";
		
		List<EmploymentProposalDTO> result = EmploymentProposalDTOMapper(sql, new Object[]{ referenceEmploymentProposalId, applicationId });
		if(verificationService.isListPopulated(result)) return result.get(0);
		else return null;
	}

	public void updateProposalFlag(Integer employmentProposalId, String proposalFlag, int value) {
		String sql = "UPDATE wage_proposal wp"
						+ " SET " + proposalFlag + " = ?"
						+ " WHERE WageProposalId = ?";
		
		jdbcTemplate.update(sql, new Object[]{ value, employmentProposalId });
		
	}

	public void deleteEmployment(int userId, int jobId) {
		String sql = "DELETE FROM employment WHERE UserId = ? AND JobId = ?";
		jdbcTemplate.update(sql, new Object[]{ userId, jobId });		
	}


	public void updateApplicationFlag(int applicationId, String flag, int value) {
		String sql = "UPDATE application SET " + flag + " = ? WHERE ApplicationId = ?";
		jdbcTemplate.update(sql, new Object[]{ value, applicationId });		
	}


	public void deleteProposedWorkDays(List<WorkDay> workDays, int applicationId) {
		
//		http://stackoverflow.com/questions/5816840/delete-i-cant-specify-target-table
		
		String sql = "DELETE FROM employment_proposal_work_day WHERE Id IN ("
					+ " SELECT * FROM ("
					+ " SELECT DISTINCT ep.Id FROM employment_proposal_work_day ep"
					+ " JOIN wage_proposal wp ON ep.EmploymentProposalId = wp.WageProposalId"
					+ " WHERE wp.IsCurrentProposal = 1"
					+ " AND wp.ApplicationId = ?"
					+ " AND (";
		
		List<Object> args = new ArrayList<Object>();
		args.add(applicationId);
		
		boolean isFirst = true;
		for(WorkDay workDay : workDays){
			if(!isFirst) sql += " OR";
			sql += " ep.WorkDayId = ?";
			args.add(workDay.getWorkDayId());
			isFirst = false;
		}
		sql += ") ) as ep1 )";
		
		jdbcTemplate.update(sql, args.toArray());
		
	}


	public void deleteRatings(int userId, int jobId) {
		String sql = "DELETE FROM rating WHERE (UserId = ? AND JobId = ?)"
				+ " OR (RatedByUserId = ? AND JobId = ?)";
		jdbcTemplate.update(sql, new Object[]{ userId, jobId, userId, jobId });		
	}


	public List<Application> getApplications_byJob(Integer jobId) {
		String sql = "SELECT * FROM application a"
				+ " JOIN job j ON a.JobId = j.JobId"
				+ " WHERE j.JobId = ?";
		
		return ApplicationRowMapper(sql, new Object[]{ jobId });
	}




}
