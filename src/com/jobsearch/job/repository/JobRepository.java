package com.jobsearch.job.repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
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

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<Job>() {
	
				@Override
				public Job mapRow(ResultSet rs, int rownumber) throws SQLException {
					Job e = new Job();
					e.setId(rs.getInt("JobId"));
					e.setJobName(rs.getString("JobName"));
					e.setUserId(rs.getInt("UserId"));
					e.setIsActive(rs.getInt("IsActive"));
					e.setDescription(rs.getString("Description"));
					e.setStreetAddress(rs.getString("StreetAddress"));
					e.setCity(rs.getString("City"));
					e.setState(rs.getString("State"));
					e.setZipCode(rs.getString("ZipCode"));
					e.setLat(rs.getFloat("Lat"));
					e.setLng(rs.getFloat("Lng"));
					e.setStartDate(rs.getDate("StartDate"));
					e.setEndDate(rs.getDate("EndDate"));
					return e;
				}
			});
		}catch(Exception e){
			return null;
		}
		
		
	}

	public void addJob(List<CreateJobDTO> jobDtos) {
		List<Job> jobsCreatedByUser = new ArrayList<>();

		try {

			ResultSet result = null;
			for (CreateJobDTO job : jobDtos) {
					CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call create_Job(?, ?, ?, ?, ?)}");

					cStmt.setString(1, job.getJobName());
					cStmt.setInt(2, job.getUserId());
					cStmt.setString(3, job.getDescription());
//					cStmt.setString(4, job.getLocation());
					cStmt.setInt(5, job.getOpenings());

					result = cStmt.executeQuery();

					Job createdJob = new Job();
					while (result.next()) {
						createdJob.setId(result.getInt("JobId"));
						createdJob.setJobName(result.getString("JobName"));
						createdJob.setIsActive(result.getInt("isActive"));
						createdJob.setUserId(result.getInt("UserId"));
						createdJob.setDescription(result.getString("Description"));
//						createdJob.setLocation(result.getString("Location"));
						createdJob.setOpenings(result.getInt("Openings"));
					}
					for(Integer categoryId: job.getCategoryIds()){
						 cStmt = jdbcTemplate.getDataSource().getConnection()
								.prepareCall("{call insertJobCategories(?, ?)}");

						cStmt.setInt(1, createdJob.getId());
						cStmt.setInt(2, categoryId);

						cStmt.executeQuery();
					}
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void addJob(CreateJobDTO jobDto) {
//		List<Job> jobsCreatedByUser = new ArrayList<>();

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall(
					"{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, jobDto.getJobName());
			 cStmt.setInt(2, jobDto.getUserId());
			 cStmt.setString(3, jobDto.getDescription());
			 //cStmt.setInt(6, jobDto.getOpenings());
			 cStmt.setString(4, jobDto.getStreetAddress());
			 cStmt.setString(5, jobDto.getCity());
			 cStmt.setString(6, jobDto.getState());
			 cStmt.setString(7, jobDto.getZipCode());
			 cStmt.setFloat(8,  jobDto.getLat());
			 cStmt.setFloat(9,  jobDto.getLng());
			 cStmt.setDate(10, (Date) jobDto.getStartDate());
			 cStmt.setDate(11, (Date) jobDto.getEndDate());
			 

			 
			 ResultSet result = cStmt.executeQuery();
			 
			 Job createdJob = new Job();
			 while(result.next()){
				 createdJob.setId(result.getInt("JobId"));
//				 job.setJobName(result.getString("JobName"));
//				 job.setIsActive(result.getInt("isActive"));
//				 job.setUserId(result.getInt("UserId"));
//				 job.setDescription(result.getString("Description"));
////				 job.setOpenings(result.getInt("Openings"));
//				 job.setStreetAddress(result.getString("StreetAddress"));
//				 job.setCity(result.getString("City"));
//				 job.setState(result.getString("State"));
//				 job.setZipCode(result.getString("ZipCode"));
//				 job.setLat(result.getFloat("Lat"));
//				 job.setLng(result.getFloat("Lng"));
				 
//				 jobsCreatedByUser.add(job);
			 }
			 
				for(Integer categoryId: jobDto.getCategoryIds()){
					 cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call insertJobCategories(?, ?)}");
		
					cStmt.setInt(1, createdJob.getId());
					cStmt.setInt(2, categoryId);
		
					cStmt.executeQuery();
				}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Job> getJobsByUser(int userId) {
		String sql = "SELECT * FROM job WHERE UserId = ?";
		return this.JobRowMapper(sql, new Object[] { userId });

	}

	public List<Job> getActiveJobsByUser(int userId) {

		// String sql = "SELECT job.JobId, job.JobName, category.CategoryId,
		// category.Name FROM job "
		// + " INNER JOIN job_category ON job_category.JobId = job.JobId"
		// + " INNER JOIN category ON category.CategoryId =
		// job_category.CategoryId"
		// + " AND job.UserId = ? AND IsActive = 1";

		// Get active jobs
		String sql = "SELECT * FROM job WHERE IsActive = 1 AND UserId = ?";
		List<Job> activeJobs = this.JobRowMapper(sql, new Object[] { userId });

		// For each active job, set its category and list of applications
		for (Job job : activeJobs) {

			// Get job's category
			job.setCategory(categoryService.getCategoryByJobId(job.getId()));

			// Get the job's applicants
			job.setApplications(applicationService.getApplicationsByJob(job.getId()));

			// Get the job's employees
			job.setEmployees(userService.getEmployeesByJob(job.getId()));
		}

		return activeJobs;

	}

	public List<Job> getCompletedJobsByUser(int userId) {
		String sql = "SELECT * FROM job WHERE IsActive = 0 AND UserId = ?";
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

		return this.JobRowMapper(sql, new Object[] { categoryId });
	}

	public int getJobCountByCategory(int categoryId) {

		String sql = "SELECT COUNT(*)" + " FROM job " + " INNER JOIN job_category"
				+ " ON job.JobId = job_category.JobId" + " AND job_category.CategoryId = ? AND job.IsActive = 1";

		int result = jdbcTemplate.queryForObject(sql, new Object[] { categoryId }, int.class);

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
		int count = jdbcTemplate.queryForObject(sql, new Object[] { jobId, userId }, int.class);

		if (count > 0)
			return true;
		else
			return false;
	}

	public Job getJob(int jobId) {
		String sql = "SELECT * FROM job WHERE JobId=?";

		List<Job> jobs = this.JobRowMapper(sql, new Object[]{ jobId });
		
		if(jobs.size() > 0) return jobs.get(0);
		else return null;
	}


	public List<Job> getFilteredJobs(int radius, double lat, double lng, int[] categoryIds) {
		// TODO Auto-generated method stub
		
		//Distance formula found here: https://developers.google.com/maps/articles/phpsqlsearch_v3?csw=1#finding-locations-with-mysql
		String sql = "SELECT *, ( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
					+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) )"
					+ " AS distance FROM job";
		
		int distanceFilterArgs = 4;
		String sql2; 
		Object[] args;
		//If there is not category filter
		if(categoryIds == null){
			args = new Object[distanceFilterArgs];
			args[0] = lat;
			args[1] = lng;
			args[2] = lat;
			args[3] = radius;
			sql2 = " WHERE job.IsActive = 1";
			
		//Else append AND conditions for category Ids
		}else{
			args = new Object[distanceFilterArgs + categoryIds.length];
			args[0] = lat;
			args[1] = lng;
			args[2] = lat;

			sql2 = " INNER JOIN job_category ON job.JobId = job_category.JobId AND job.IsActive = 1 AND (";
			for(int i = 0; i < categoryIds.length; i++){
				if(i < categoryIds.length - 1){
					sql2 += " job_category.CategoryId = ? OR";
				}else{
					sql2 += " job_category.CategoryId = ?)";
				}
			
				args[3 + i] = categoryIds[i];				
			}			
					
			args[args.length - 1] = radius;
			
		}
		sql += sql2;	
		sql += " HAVING distance < ? ORDER BY distance LIMIT 0 , 20";
		return this.JobRowMapper(sql, args );

	}

}
