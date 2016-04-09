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
				e.setApplicationId(rs.getInt("ApplicationId"));
				e.setUserId(rs.getInt("UserId"));
				e.setJobId(rs.getInt("JobId"));
				e.setBeenViewed(rs.getInt("BeenViewed"));
				e.setStatus(rs.getInt("Status"));
				return e;
			}
		});
	}
	
	public List<Application> getApplicationsByEmployer(int userId) {		
		String sql = "SELECT * FROM application WHERE UserId = ?";
				
		return this.ApplicationRowMapper(sql, new Object[]{ userId });
	}
	
	public List<Application> getApplicationsByJob(int jobId) {
		
		//Get all applications for job.
		//Less than 3 is anything but accepted
		String sql = "SELECT * FROM application WHERE JobId = ? AND Status < 3";
		return ApplicationRowMapper(sql, new Object[]{ jobId });

	}


	public Application getApplication(int jobId, int userId) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM application WHERE JobId = ? and UserId = ?";
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
	
}
