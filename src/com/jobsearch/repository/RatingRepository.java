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
	


	public Double getRatingValue_byCriteriaAndUser(Integer rateCriterionId, int userId) {

		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? AND RateCriterionId = ? AND Value is not null";

		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId, rateCriterionId }, Double.class);

		if (rating == null) return null;
		else return rating;
	}
	
	public Double getOverallRating(int userId) {
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? and Value is not null";
		return jdbcTemplate.queryForObject(sql, new Object[] { userId }, Double.class);
	}
	
}
