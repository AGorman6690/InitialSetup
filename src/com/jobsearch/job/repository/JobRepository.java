package com.jobsearch.job.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.CategoryJob;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.JobSearchUser;
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
	
	@Autowired
	ApplicationServiceImpl applicationService;
	

	public List<Job> JobRowMapper(String sql, Object[] args) {

		return jdbcTemplate.query(sql, args, new RowMapper<Job>() {

			@Override
			public Job mapRow(ResultSet rs, int rownumber) throws SQLException {
				Job e = new Job();
				e.setId(rs.getInt(1));
				e.setJobName(rs.getString(2));
				e.setUserId(rs.getInt(3));
				e.setIsActive(rs.getInt(4));
				e.setDescription(rs.getString(5));
				e.setLocation(rs.getString(6));
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

	public List<Job> addJob(CreateJobDTO jobDto) {
		List<Job> jobsCreatedByUser = new ArrayList<>();
		
		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall("{call create_Job(?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, jobDto.getJobName());
			 cStmt.setInt(2, jobDto.getUserId());
			 cStmt.setInt(3, jobDto.getCategoryId());
			 cStmt.setString(4, jobDto.getDescription());
			 cStmt.setString(5, jobDto.getLocation());
			 cStmt.setInt(6, jobDto.getOpenings());
			 
			 ResultSet result = cStmt.executeQuery();
			 
			 Job job = new Job();
			 while(result.next()){
				 job.setId(result.getInt("JobId"));
				 job.setJobName(result.getString("JobName"));
				 job.setIsActive(result.getInt("isActive"));
				 job.setUserId(result.getInt("UserId"));
				 job.setDescription(result.getString("Description"));
				 job.setLocation(result.getString("Location"));
				 job.setOpenings(result.getInt("Openings"));
				 jobsCreatedByUser.add(job);
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jobsCreatedByUser;

	}

	public List<Job> getJobsByUser(int userId) {
		String sql = "SELECT * FROM job WHERE UserId = ?";
		return  this.JobRowMapper(sql, new Object[] { userId });

	}
	

	public List<Job> getActiveJobsByUser(int userId) {
		
	
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
			job.setApplications(applicationService.getApplicationsByJob(job.getId()));	
			
			//Get the job's employees
			job.setEmployees(userService.getEmployeesByJob(job.getId()));
		}
		
		return activeJobs;

	}
	
	public List<Job> getCompletedJobsByUser(int userId) {
		String sql =  "SELECT * FROM job WHERE IsActive = 0 AND UserId = ?";		
		return this.JobRowMapper(sql, new Object[] { userId });
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

	public boolean hasAppliedForJob(int jobId, int userId) {
		String sql = "SELECT COUNT(*) FROM application WHERE jobId = ? AND userID = ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[]{ jobId,  userId }, int.class);
		
		if (count > 0) return true;
		else return false;
	}

	public Job getJob(int jobId) {
		String sql = "SELECT * FROM job WHERE JobId=?";
		List<Job> jobs = this.JobRowMapper(sql, new Object[]{ jobId });
		
		if(jobs.size() > 0) return jobs.get(0);
		else return null;
	}





}
