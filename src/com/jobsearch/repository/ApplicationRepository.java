package com.jobsearch.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.model.Job;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.Application;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.service.AnswerServiceImpl;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.ProposalServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
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
	@Autowired
	ProposalServiceImpl proposalService;
	@Autowired
	AnswerServiceImpl answerService;

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





	public List<AnswerOption> getAnswerOptions(int questionId) {
		String sql = "SELECT * FROM answer_option WHERE QuestionId = ?";
		return this.AnswerOptionRowMapper(sql, new Object[] { questionId });
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









	public void insertApplication(Application application, Proposal proposal, List<Answer> answers) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insert_application(?, ?, ?)}");

			cStmt.setInt(1, application.getUserId());
			cStmt.setInt(2, application.getJobId());
			cStmt.setInt(3, application.getStatus());
	
			ResultSet result = cStmt.executeQuery();
			result.next();
			Integer newApplicationId = result.getInt("ApplicationId");

			if(newApplicationId != null){			
				proposal.setApplicationId(newApplicationId);
				
				proposalService.insertProposal(proposal);	
			
				// Add answers  
				if(verificationService.isListPopulated(answers)){
					for (Answer answer : answers) {
						answer.setUserId(application.getUserId());
						answerService.addAnswer(answer);	
					}	
				}				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public AnswerOption getAnswerOption(int answerOptionId) {
		String sql = "SELECT * FROM answer_option WHERE AnswerOptionId = ?";
		return AnswerOptionRowMapper(sql, new Object[] { answerOptionId }).get(0);
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
					+ " AND wd.Timestamp_EndDateTime > ?"
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
		
		return ApplicationRowMapper(sql, new Object[]{ DateUtility.getCurrentTimestamp(), userId });
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

	public void deleteEmployment(int userId, int jobId) {
		String sql = "DELETE FROM employment WHERE UserId = ? AND JobId = ?";
		jdbcTemplate.update(sql, new Object[]{ userId, jobId });		
	}


	public void updateApplicationFlag(int applicationId, String flag, int value) {
		String sql = "UPDATE application SET " + flag + " = ? WHERE ApplicationId = ?";
		jdbcTemplate.update(sql, new Object[]{ value, applicationId });		
	}







	public List<Application> getApplications_byJob(Integer jobId) {
		String sql = "SELECT * FROM application a"
				+ " JOIN job j ON a.JobId = j.JobId"
				+ " WHERE j.JobId = ?";
		
		return ApplicationRowMapper(sql, new Object[]{ jobId });
	}




}
