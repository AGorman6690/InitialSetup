package com.jobsearch.application.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
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
				Application e = new Application();
				e.setApplicationId(rs.getInt(1));
				e.setUserId(rs.getInt(2));
				e.setJobId(rs.getInt(3));
				e.setIsOffered(rs.getInt(4));
				e.setBeenViewed(rs.getInt(5));
				e.setIsAccepted(rs.getInt(6));
				return e;
			}
		});
	}
	
	
	public void markApplicationViewed(int jobId, int userId) {
		String sql = "UPDATE application" + " SET BeenViewed = 1"
				+ " WHERE JobId = ? AND UserId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId, userId });
		
	}


	public void markApplicationAccepted(int jobId, int userId) {
		String sql = "UPDATE application" + " SET IsAccepted = 1"
				+ " WHERE JobId = ? AND UserId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId, userId });
		
	}
	

	//FIX THIS!!!!!!!!!
	//*****************************************************************************************
	public List<Application> getApplicationsByEmployer(int userId) {		
		String sql = "SELECT user.FirstName, job.JobName, application.IsOffered, application.BeenViewed,"
				+ " application.IsAccepted, category.Name"
				+ " FROM application"
				+ " INNER JOIN job ON application.JobId = job.JobId"
				+ " AND job.UserId = ? AND job.IsActive = 1"
				+ " INNER JOIN user ON application.UserId = user.UserId"
				+ " INNER JOIN job_category ON job.JobId = job_category.JobId"
				+ " INNER JOIN category ON job_category.CategoryId = category.CategoryId";
				
		return this.ApplicationRowMapper(sql, new Object[]{ userId });
	}
	//*****************************************************************************************
	
	public List<Application> getApplicationsByJob(int jobId) {
		
		//Get all applications for job
		String sql = "SELECT * FROM application WHERE JobId = ? AND IsAccepted = 0";
		List<Application> applications = ApplicationRowMapper(sql, new Object[]{ jobId });
		
		//For each application, set the applicant
		for(Application application : applications){
			application.setApplicant(userService.getUser(application.getUserId()));
			
		}
		
		return applications;
	}


	public Application getApplication(int jobId, int userId) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM application WHERE JobId = ? and UserId = ?";
		List<Application> applications = this.ApplicationRowMapper(sql, new Object[]{ jobId, userId });
		
		if(applications.size() > 0) return applications.get(0);
		else return null;

	}
	
}
