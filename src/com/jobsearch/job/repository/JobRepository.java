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

	public Job getJob(int jobId) {
		String sql = "SELECT *" + " FROM jobs" + " WHERE Id = ?";

		List<Job> jobs;
		jobs = this.JobRowMapper(sql, new Object[] { jobId });

		return jobs.get(0);
	}

	public ArrayList<Job> getAppliedToJobs(JobSearchUser user, Boolean showOnlyActiveJobs) {
		String sql = "SELECT *" + " FROM jobs" + " INNER JOIN applications" + "	ON jobs.Id = applications.JobId"
				+ "	AND applications.UserId = ?";

		if (showOnlyActiveJobs)
			sql += " AND jobs.IsActive = 1";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { user.getUserId() });
	}

	public ArrayList<Job> getEmployment(JobSearchUser user, boolean showOnlyActiveJobs) {
		String sql = "SELECT *" + " FROM jobs" + " INNER JOIN employment" + "	ON jobs.Id = employment.JobId"
				+ "	AND employment.UserId = ?";

		if (showOnlyActiveJobs)
			sql += " AND jobs.IsActive = 1";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { user.getUserId() });
	}

	public void addJob(String jobName, int userId) {
		String sql;
		sql = "INSERT INTO jobs (JobName, UserId, IsActive)" + " VALUES (?, ?, 1)";

		jdbcTemplate.update(sql, new Object[] { jobName, userId });

	}

	public ArrayList<Job> getJobs(int userId) {
		String sql;
		sql = "SELECT * FROM jobs WHERE UserId = ?";
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { userId });

	}

	public void updateJobComplete(int jobId) {
		String sql = "UPDATE jobs" + " SET IsActive = ?" + " WHERE Id = ?";

		jdbcTemplate.update(sql, new Object[] { 0, jobId });

	}

	public ArrayList<Job> getJobs(JobSearchUser user, boolean isActive) {

		// Con
		// int param;
		// if (isActive) param = 1;
		// else param = 0;

		String sql = "SELECT *" + " FROM jobs" + " WHERE UserId = ?" + " AND IsActive = ?";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { user.getUserId(), isActive });

	}

	public ArrayList<Job> getJobs(int userId, boolean isActive) {

		String sql = "SELECT *" + " FROM jobs" + " WHERE UserId = ?" + " AND IsActive = ?";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { userId, isActive });

	}

	public void applyForJob(int jobId, int userId) {
		String sql = "INSERT INTO applications (UserId, JobId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public ArrayList<Job> getJobsBySelectedCat(int categoryId) {
		String sql = "SELECT jobs.Id, jobs.JobName, jobs.UserId, jobs.IsActive" + " FROM jobs "
				+ " INNER JOIN jobscategories" + " ON jobs.Id = jobscategories.JobId"
				+ " AND jobscategories.CategoryId = ?" + " AND jobs.IsActive = 1";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { categoryId });
	}

	public ArrayList<Job> getJobsByCategory(int categoryId, boolean showOnlyActiveJobs) {

		String sql = "SELECT *" + " FROM jobs " + " INNER JOIN jobscategories" + " ON jobs.Id = jobscategories.JobId"
				+ " AND jobscategories.CategoryId = ?" + " AND jobs.IsActive = ?";

		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[] { categoryId, showOnlyActiveJobs });
	}

	public void addJobCategory(int jobId, int categoryId) {
		String sql;
		sql = "INSERT INTO jobscategories (JobId, CategoryId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { jobId, categoryId });

	}
}
