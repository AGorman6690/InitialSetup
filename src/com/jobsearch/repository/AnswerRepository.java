package com.jobsearch.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.bases.BaseRepository;
import com.jobsearch.model.Answer;
import com.jobsearch.service.ApplicationServiceImpl;

@Repository
public class AnswerRepository extends BaseRepository{
	
	@Autowired
	ApplicationServiceImpl applicationService;
	
	private List<Answer> AnswerRowMapper(String sql, Object[] args) {

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
	
	public List<Answer> getAnswers(int questionId, int userId) {
		String sql = "SELECT * FROM answer WHERE QuestionId = ? AND UserId = ?";
		return this.AnswerRowMapper(sql, new Object[] { questionId, userId });
	}

	
	public Answer getAnswer(int questionId, int userId) {
		String sql = "SELECT * FROM answer WHERE QuestionId = ? AND UserId = ?";
		return this.AnswerRowMapper(sql, new Object[] { questionId, userId }).get(0);
	}
	
	public void addAnswer(Answer answer) {
		String sql = "INSERT INTO answer (QuestionId, UserId, Text, AnswerOptionId) VALUES(?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { answer.getQuestionId(), answer.getUserId(), answer.getText(),
				answer.getAnswerOptionId() });
	}
}
