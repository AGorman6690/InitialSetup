package com.jobsearch.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.bases.BaseRepository;
import com.jobsearch.model.RateCriterion;



@Repository
public class RatingRepository extends BaseRepository{
	

	public List<RateCriterion> RateCriterionRowMapper(String sql, Object[] args) {		
		List<RateCriterion> list;
		list = jdbcTemplate.query(sql, args, new RowMapper<RateCriterion>() {
			public RateCriterion mapRow(ResultSet rs, int rownumber) throws SQLException {

				RateCriterion e = new RateCriterion();
				e.setRateCriterionId(rs.getInt("RateCriterionId"));
				e.setName(rs.getString("Name"));
				e.setIsUsedToRateEmployee(rs.getBoolean("IsUsedToRateEmployee"));
				e.setShortName(rs.getString("ShortName"));
				return e;
			}
		});
		return (List<RateCriterion>) list;
	}

	public List<String> getCommentsGivenToUser_byJob(int userId, Integer jobId) {
		String sql = "SELECT Comment FROM comment WHERE UserId = ? and JobId = ?";
		return jdbcTemplate.queryForList(sql, new Object[] { userId, jobId }, String.class);
	}


	public void deleteRatings(int userId, int jobId) {
		String sql = "DELETE FROM rating WHERE (UserId = ? AND JobId = ?)"
				+ " OR (RatedByUserId = ? AND JobId = ?)";
		jdbcTemplate.update(sql, new Object[]{ userId, jobId, userId, jobId });		
	}
	
	public Double getRating_givenByUser(Integer jobId, int userId) {
		String sql = "SELECT Value FROM rating WHERE RatedByUserId = ? AND JobId = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[]{ userId,  jobId }, Double.class);	
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public Integer getCount_nullRatings_givenByUserForJob(Integer jobId, int userId) {
		String sql = "SELECT COUNT(*) FROM rating WHERE RatedByUserId = ? AND JobId = ? AND Value IS NULL";
		return jdbcTemplate.queryForObject(sql, new Object[]{ userId,  jobId }, Integer.class);	
	}

	public Double getRating_byJobAndUser(Integer jobId, int userId) {
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? AND JobId = ? AND Value is not null";
		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId, jobId }, Double.class);
		if (rating == null) return null;
		else return rating;
	}
	
	public List<RateCriterion> getRatingCriteia_toRateEmployee() {
		String sql = "SELECT * FROM ratecriterion WHERE IsUsedToRateEmployee = 1 ORDER BY DisplayOrder ASC";
		return this.RateCriterionRowMapper(sql, new Object[] {});
	}
	
	public List<RateCriterion> getRateCriteria_toRateEmployer() {
		String sql = "SELECT * FROM ratecriterion WHERE IsUsedToRateEmployee = 0 ORDER BY DisplayOrder ASC";
		return this.RateCriterionRowMapper(sql, new Object[]{});
	}
	

	public void deleteComment(int jobId, int employeeId) {
		String sql = "DELETE FROM comment WHERE JobId = ? AND UserId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId, employeeId });

	}

	public Double getRatingValue_byCriteriaAndUser(Integer rateCriterionId, int userId) {

		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? AND RateCriterionId = ? AND Value is not null";

		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId, rateCriterionId }, Double.class);

		if (rating == null) return null;
		else return rating;
	}
	
	public void updateRating(RateCriterion rc) {
		String sql = "UPDATE rating SET Value = ?"
					+ " WHERE RateCriterionId = ?"
					+ " AND UserId = ?"
					+ " AND RatedByUserId = ?"
					+ " AND JobId = ?";

		jdbcTemplate.update(sql,
				new Object[] { rc.getValue(),
						rc.getRateCriterionId(),
						rc.getUserId_ratee(),
						rc.getUserId_rater(),
						rc.getJobId() });

	}
	public Double getOverallRating(int userId) {
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? and Value is not null";
		return jdbcTemplate.queryForObject(sql, new Object[] { userId }, Double.class);
	}
	public void addComment(int userId, int jobId, String comment, int userId_commenter) {
		String sql = "INSERT INTO comment (JobId, UserId, Comment, UserId_Commenter) VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, new Object[] { jobId, userId, comment, userId_commenter });

	}



	public String getComment(int jobId, int userId) {
		String sql = "SELECT Comment FROM comment WHERE JobId = ? AND UserId = ?";

		String comment = "";
		try {
			comment = jdbcTemplate.queryForObject(sql, new Object[] { jobId, userId }, String.class);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return comment;

	}
	
	public void insertRating(Integer rateCriterionId, int userIdToRate, Integer jobId, Integer ratedByUserId) {		
		String sql = "INSERT INTO rating (RateCriterionId, UserId, JobId, RatedByUserId)"
					+ " VALUES (?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{ rateCriterionId, userIdToRate, jobId, ratedByUserId });
	}

	public Double getRating(int userId) {
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? and Value is not null";

		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId }, Double.class);

//		if (rating == null) {
//			return -1.0;
//		} else {
			return rating;
//		}
	}
	
}
