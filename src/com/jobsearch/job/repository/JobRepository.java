package com.jobsearch.job.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Application;
import com.jobsearch.model.CategoryJob;
import com.jobsearch.user.service.UserServiceImpl;

@Repository
public class JobRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	CategoryServiceImpl categoryService;
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	JobServiceImpl jobService;
	

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
	
	public List<CategoryJob> CategoryJobRowMapper(String sql, Object[] args) {

		return jdbcTemplate.query(sql, args, new RowMapper<CategoryJob>() {

			@Override
			public CategoryJob mapRow(ResultSet rs, int rownumber) throws SQLException {
				CategoryJob e = new CategoryJob();
				e.setJobId(rs.getInt(1));
				e.setJobName(rs.getString(2));
				e.setCategoryId(rs.getInt(3));
				e.setCategoryName(rs.getString(4));
				return e;
			}
		});

	}

	public void addJob(String jobName, int userId) {
		String sql;
		sql = "INSERT INTO job (JobName, UserId, IsActive)" + " VALUES (?, ?, 1)";

		jdbcTemplate.update(sql, new Object[] { jobName, userId });

	}

	public List<Job> getJobsByUser(int userId) {
		String sql = "SELECT * FROM job WHERE UserId = ?";
		return  this.JobRowMapper(sql, new Object[] { userId });

	}
	

	public List<Job> getActiveJobsByUser_AppCat(int userId) {
		
	
//		String sql = "SELECT job.JobId, job.JobName, category.CategoryId, category.Name FROM job "
//				+ " INNER JOIN job_category ON job_category.JobId = job.JobId"
//				+ " INNER JOIN category ON category.CategoryId = job_category.CategoryId"
//				+ " AND job.UserId = ? AND IsActive = 1";
		
		//Get active jobs
		String sql =  "SELECT * FROM job WHERE IsActive = 1 AND UserId = ?";		
		List<Job> activeJobs = this.JobRowMapper(sql, new Object[] { userId });
		
		//For each active job, set its category and list of applications		
		for (Job job : activeJobs){
			
			//Get job's category		
			job.setCategory(categoryService.getCategoryByJobId(job.getId()));

			//Get the job's applicants 
			job.setApplications(userService.getApplicationsByJob(job.getId()));	
			
			//Get the job's employees
			job.setEmployees(userService.getEmployeesByJob(job.getId()));
		}
		
		return activeJobs;

	}

	public void markJobComplete(int jobId) {
		String sql = "UPDATE job" + " SET IsActive = 0 WHERE JobId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId });

	}



	public void applyForJob(int jobId, int userId) {
		String sql = "INSERT INTO application (UserId, JobId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public List<Job> getJobsByCategory(int categoryId) {

		String sql = "SELECT *" + " FROM job " + " INNER JOIN job_category" + " ON job.JobId = job_category.JobId"
				+ " AND job_category.CategoryId = ?";

		return  this.JobRowMapper(sql, new Object[] { categoryId });
	}
	
	public int getJobCountByCategory(int categoryId) {	
		
		String sql = "SELECT COUNT(*)" + " FROM job " + " INNER JOIN job_category" + " ON job.JobId = job_category.JobId"
				+ " AND job_category.CategoryId = ? AND job.IsActive = 1";
		
		int result = jdbcTemplate.queryForObject(sql, new Object[]{categoryId}, int.class);
		
		return result;
	}


	public List<Job> getApplicationsByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN application" + " ON job.JobId = application.JobId"
				+ "	AND application.UserId = ?";

		return this.JobRowMapper(sql, new Object[] { userId });
	}
	
	public List<Job> getJobOffersByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN application" + " ON job.JobId = application.JobId"
				+ "	AND application.UserId = ? AND applicaion.IsOffered = 1";

		return this.JobRowMapper(sql, new Object[] { userId });
	}
	
	
	public List<Job> getEmploymentByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment" + "	ON job.JobId = employment.JobId"
				+ "	AND employment.UserId = ?";

		return this.JobRowMapper(sql, new Object[] { userId });
	}


	public Job getJobByJobNameAndUser(String jobName, int userId) {
		String sql = "SELECT * FROM job WHERE jobName = ? AND UserId = ?";
		return JobRowMapper(sql, new Object[]{ jobName, userId }).get(0);
	}

	public int getSubJobCount(int categoryId, int count) {
		

		List<Category> categories;
		
		//For the categoryId pass as the paramenter, get the categories 1 level deep
		categories = categoryService.getCategoriesBySuperCategory(categoryId);
		
		//For each category 1 level deep
		for(Category category : categories){
			
			//Get its job count
			count += jobService.getJobCountByCategory(category.getId());
			
			//Recursively get the job count for the category 1 level deep
			count = jobService.getSubJobCount(category.getId(), count);
		}
		
		return count;
	}



}
