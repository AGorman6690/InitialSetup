package com.jobsearch.application.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.Answer;
import com.jobsearch.model.AnswerOption;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.UserServiceImpl;

@Repository
public class ApplicationRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserServiceImpl userService;

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

				//Set the applicant
				JobSearchUser user = new JobSearchUser();
				user.setUserId(rs.getInt("UserId"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setEmailAddress(rs.getString("Email"));
				user.setHomeLat(rs.getFloat("HomeLat"));
				user.setHomeLng(rs.getFloat("HomeLng"));
				user.setHomeCity(rs.getString("HomeCity"));
				user.setHomeState(rs.getString("HomeState"));
				user.setHomeZipCode(rs.getString("HomeZipCode"));
				user.setMaxWorkRadius(rs.getInt("MaxWorkRadius"));

				application.setApplicant(user);

				return application;
			}
		});
	}

	public List<Application> getApplicationsByEmployer(int userId) {
		String sql = "SELECT * FROM application WHERE UserId = ?";

		return this.ApplicationRowMapper(sql, new Object[]{ userId });
	}

	public List<Application> getApplicationsByJob(int jobId) {

		//Get all non-accepted applications for job.
		//Status less than 3 is anything but accepted
		String sql = "SELECT a.*, u.* "
				+ "FROM application a "
				+ "inner join user u "
				+ "on u.userid = a.userid "
				+ "WHERE JobId = ? AND Status < 3";
		return ApplicationRowMapper(sql, new Object[]{ jobId });

	}


	public Application getApplication(int jobId, int userId) {
		// TODO Auto-generated method stub

		String sql = "SELECT a.*, u.* "
				+ "FROM application a "
				+ "inner join user u "
				+ "on u.userid = a.userid "
				+ "WHERE JobId = ? and a.UserId = ?";

		List<Application> applications = this.ApplicationRowMapper(sql, new Object[]{ jobId, userId });

		if(applications.size() > 0) return applications.get(0);
		else return null;

	}


	public void updateStatus(int applicationId, int status) {
		String sql = "UPDATE application SET Status = ? WHERE ApplicationId = ?";
		jdbcTemplate.update(sql, new Object[]{ status, applicationId });

	}


	public Application getApplication(int applicationId) {
		String sql = "SELECT * FROM application WHERE ApplicationId = ?";
		return this.ApplicationRowMapper(sql, new Object[]{ applicationId }).get(0);

	}

	public void addAnswer(Answer answer) {

		String sql = "INSERT INTO answer (QuestionId, UserId, Text) VALUES(?, ?, ?)";
		jdbcTemplate.update(sql, new Object[]{ answer.getQuestionId(), answer.getUserId(), answer.getText() });

	}

//	public void addBooleanAnswer(Answer answer) {
//
//		String sql = "INSERT INTO answer (QuestionId, UserId, AnswerBoolean) VALUES(?, ?, ?)";
//		jdbcTemplate.update(sql, new Object[]{ answer.getQuestionId(), answer.getUserId(), answer.getAnswerBoolean() });
//
//	}

//	public void addOptionAnswer(Answer answer, int optionId) {
//
//		String sql = "INSERT INTO answer (QuestionId, UserId, AnswerOptionId) VALUES(?, ?, ?)";
//		jdbcTemplate.update(sql, new Object[]{ answer.getQuestionId(), answer.getUserId(), optionId });
//
//	}

	public void addApplication(int jobId, int userId) {
		String sql = "INSERT INTO application (UserId, JobId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public List<Question> getQuestions(int id) {
		String sql = "SELECT * FROM question WHERE JobId = ? ORDER BY QuestionId ASC";
		return this.QuestionRowMapper(sql, new Object[]{ id });
	}


	public List<AnswerOption> getAnswerOptions(int questionId) {

		String sql = "SELECT * FROM answer_option WHERE QuestionId = ?";
		return this.AnswerOptionRowMapper(sql, new Object[]{ questionId });

	}

	public Answer getAnswer(int questionId, int userId) {

		String sql = "SELECT * FROM answer WHERE QuestionId = ? AND UserId = ?";
		return this.AnswerRowMapper(sql, new Object[]{ questionId, userId }).get(0);	

		
	}

	public List<Answer> getAnswers(int questionId, int userId) {

		String sql = "SELECT * FROM answer WHERE QuestionId = ? AND UserId = ?";
		return this.AnswerRowMapper(sql, new Object[]{ questionId, userId });
	}

	public List<AnswerOption> AnswerOptionRowMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<AnswerOption>() {

				@Override
				public AnswerOption mapRow(ResultSet rs, int rownumber) throws SQLException {
					AnswerOption e = new AnswerOption();
					e.setAnswerOptionId(rs.getInt("AnswerOptionId"));
					e.setQuestionId(rs.getInt("QuestionId"));
					e.setAnswerOption(rs.getString("AnswerOption"));


					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}


	public List<Question> QuestionRowMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<Question>() {

				@Override
				public Question mapRow(ResultSet rs, int rownumber) throws SQLException {
					Question e = new Question();
					e.setQuestionId(rs.getInt("QuestionId"));
					e.setJobId(rs.getInt("JobId"));
					e.setFormatId(rs.getInt("FormatId"));
					e.setText(rs.getString("Question"));

					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}

	public List<Answer> AnswerRowMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<Answer>() {

				@Override
				public Answer mapRow(ResultSet rs, int rownumber) throws SQLException {
					Answer e = new Answer();
//					e.setAnswerOptionId(rs.getInt("AnswerOptionId"));
					e.setQuestionId(rs.getInt("QuestionId"));
					e.setText(rs.getString("Text"));
//					e.setAnswerBoolean(rs.getInt("AnswerBoolean"));
					e.setUserId(rs.getInt("UserId"));

					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}

	public void addQuestion(Question question) {


		CallableStatement cStmt;
		try {
			cStmt = jdbcTemplate.getDataSource().getConnection().
					prepareCall("{call insert_question(?, ?, ?)}");

			cStmt.setString(1, question.getText());
			cStmt.setInt(2, question.getFormatId());
			cStmt.setInt(3, question.getJobId());

			ResultSet result = cStmt.executeQuery();
			result.next();
			int createdQuestionId = result.getInt(("QuestionId"));


			if(question.getAnswerOptions() != null){
				String sql = "INSERT INTO answer_option (QuestionId, AnswerOption) VALUES (?, ?)";
				for(AnswerOption answerOption : question.getAnswerOptions()){
					jdbcTemplate.update(sql, new Object[]{ createdQuestionId, answerOption.getAnswerOption() });
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setHasBeenViewed(int jobId, int value) {
		String sql = "UPDATE application SET HasBeenViewed = ? where jobId = ?";
		jdbcTemplate.update(sql, new Object[]{ value, jobId });
		
	}

}
