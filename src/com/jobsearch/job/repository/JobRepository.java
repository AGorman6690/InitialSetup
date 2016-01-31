package com.jobsearch.job.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.job.service.Job;
import com.jobsearch.user.service.JobSearchUser;

@Repository
public class JobRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Job> JobRowMapper(String sql, Object[] args) {

		return jdbcTemplate.query(sql, args, new RowMapper<Job>() {

			@Override
			public Job mapRow(ResultSet rs, int rownumber) throws SQLException {
				Job e = new Job();
				e.setId(rs.getInt(1));
				e.setJobName(rs.getString(2));
				e.setUserId(rs.getInt(3));
				e.setIsActive(rs.getInt(4));
				return e;
			}
		});

	}


	public ArrayList<Job> getAppliedToJobs(JobSearchUser user, Boolean showOnlyActiveJobs) {
		String sql = "SELECT *" + " FROM jobs" + " INNER JOIN application" + "	ON jobs.Id = application.JobId"
				+ "	AND application.UserId = ?";

		if (showOnlyActiveJobs)
			sql += " AND jobs.IsActive = 1";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { user.getUserId() });
	}


	public void addJob(String jobName, int userId) {
		String sql;
		sql = "INSERT INTO job (JobName, UserId, IsActive)" + " VALUES (?, ?, 1)";

		jdbcTemplate.update(sql, new Object[] { jobName, userId });

	}

	public List<Job> getJobsByUser(int userId) {
		String sql = "SELECT * FROM job where UserId = ?";
		return  this.JobRowMapper(sql, new Object[] { userId });

	}

	public void updateJobComplete(int jobId) {
		String sql = "UPDATE job" + " SET IsActive = ?" + " WHERE JobId = ?";

		jdbcTemplate.update(sql, new Object[] { 0, jobId });

	}

	public void applyForJob(int jobId, int userId) {
		String sql = "INSERT INTO application (UserId, JobId,)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public List<Job> getJobsByCategory(int categoryId) {

		String sql = "SELECT *" + " FROM job " + " INNER JOIN job_category" + " ON job.JobId = job_category.JobId"
				+ " AND job_category.CategoryId = ?";

		return  this.JobRowMapper(sql, new Object[] { categoryId });
	}

	public List<Job> getApplicationsByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN application" + "	ON job.JobId = application.JobId"
				+ "	AND application.UserId = ?";

//		if (showOnlyActiveJobs)
//			sql += " AND jobs.IsActive = 1";

		return this.JobRowMapper(sql, new Object[] { userId });
	}
	
	public List<Job> getEmploymentByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment" + "	ON job.JobId = employment.JobId"
				+ "	AND employment.UserId = ?";

//		if (showOnlyActiveJobs)
//			sql += " AND jobs.IsActive = 1";

		return this.JobRowMapper(sql, new Object[] { userId });
	}
}
