package com.jobsearch.job.repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.FilterJobRequestDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobInfoPostRequestDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
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
//					e.setIsActive(rs.getInt("IsActive"));
					e.setDescription(rs.getString("Description"));
					e.setStreetAddress(rs.getString("StreetAddress"));
					e.setCity(rs.getString("City"));
					e.setState(rs.getString("State"));
					e.setZipCode(rs.getString("ZipCode"));
					e.setLat(rs.getFloat("Lat"));
					e.setLng(rs.getFloat("Lng"));
					e.setStartDate(rs.getDate("StartDate"));
					e.setEndDate(rs.getDate("EndDate"));
					e.setStartTime(rs.getTime("StartTime"));
					e.setEndTime(rs.getTime("EndTime"));	
					e.setStatus(rs.getInt("Status"));
					
					//Set duration
					DateTime dtStart = new DateTime(e.getStartDate());
					DateTime dtEnd = new DateTime(e.getEndDate());
					e.setDuration(Days.daysBetween(dtStart, dtEnd).getDays());
					
					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}

	public void addJob(List<JobInfoPostRequestDTO> jobDtos) {
		try {

			ResultSet result = null;
			for (JobInfoPostRequestDTO job : jobDtos) {
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
//						createdJob.setIsActive(result.getInt("isActive"));
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


	public void addJob(JobInfoPostRequestDTO jobDto, JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall(
					"{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, jobDto.getJobName());
			 cStmt.setInt(2, user.getUserId());
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
			 cStmt.setTime(12,  jobDto.getStartTime());
			 cStmt.setTime(13,  jobDto.getEndTime());

			 ResultSet result = cStmt.executeQuery();

			 Job createdJob = new Job();
			 result.next();
			 createdJob.setId(result.getInt("JobId"));

			 for(Integer categoryId: jobDto.getCategoryIds()){
				cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call insertJobCategories(?, ?)}");

					cStmt.setInt(1, createdJob.getId());
					cStmt.setInt(2, categoryId);

					cStmt.executeQuery();
			}

			for(Question question : jobDto.getQuestions()){

				question.setJobId(createdJob.getId());
				applicationService.addQuestion(question);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public List<Job> getJobsByUser(int userId) {
//		String sql = "SELECT * FROM job WHERE UserId = ?";
//		return this.JobRowMapper(sql, new Object[] { userId });
//
//	}

	public List<Job> getJobsByStatusAndByEmployer(int userId, int jobStatus) {
		String sql = "SELECT * FROM job WHERE Status = ? AND UserId = ?";
		return this.JobRowMapper(sql, new Object[] { jobStatus , userId});
	}
	
//	public List<Job> getActiveJobsByEmployer(int userId) {
//
//		// Get active jobs
//		String sql = "SELECT * FROM job WHERE Status 1 AND UserId = ?";
//		return this.JobRowMapper(sql, new Object[] { userId });
//
//
//	}

//	public List<Job> getCompletedJobsByEmployer(int userId) {
//		String sql = "SELECT * FROM job WHERE IsActive = 0 AND UserId = ?";
//		return this.JobRowMapper(sql, new Object[] { userId });
//	}

//	public List<Job> getCompletedJobsByEmployee(int userId) {
//		String sql = "SELECT * FROM job INNER JOIN employment ON job.JobId = employment.JobId"
//						+ " AND job.Status = 2 AND employment.UserId = ?";
//		return this.JobRowMapper(sql, new Object[] { userId });
//	}

	public void markJobComplete(int jobId) {
		String sql = "UPDATE job" + " SET IsActive = 0 WHERE JobId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId });

	}


	public int getJobCountByCategory(int categoryId) {

		String sql = "SELECT COUNT(*)" + " FROM job " + " INNER JOIN job_category"
				+ " ON job.JobId = job_category.JobId" + " AND job_category.CategoryId = ? AND job.IsActive = 1";

		int result = jdbcTemplate.queryForObject(sql, new Object[] { categoryId }, int.class);

		return result;
	}

//	public List<Job> getJobsAppliedTo(int userId) {
//		String sql = "SELECT *" + " FROM job" + " INNER JOIN application" + " ON job.JobId = application.JobId"
//				+ "	AND application.UserId = ?";
//
//		return this.JobRowMapper(sql, new Object[] { userId });
//	}
	
	public List<Job> getJobsByStatusByEmployee(int userId, int jobStatus) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment ON job.JobId = employment.JobId"
				+ "	AND employment.UserId = ? and job.Status = ?";

		return this.JobRowMapper(sql, new Object[] { userId, jobStatus });
	}


//	public List<Job> getYetToStartJobsByEmployee(int userId) {
//		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment ON job.JobId = employment.JobId"
//				+ "	AND employment.UserId = ? and job.Status = 0";
//
//		return this.JobRowMapper(sql, new Object[] { userId });
//	}
//
//	public List<Job> getActiveJobsByEmployee(int userId) {
//		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment ON job.JobId = employment.JobId"
//				+ "	AND employment.UserId = ? and job.Status = 1";
//
//		return this.JobRowMapper(sql, new Object[] { userId });
//	}

//	public boolean hasAppliedForJob(int jobId, int userId) {
//		String sql = "SELECT COUNT(*) FROM application WHERE jobId = ? AND userID = ?";
//		int count = jdbcTemplate.queryForObject(sql, new Object[] { jobId, userId }, int.class);
//
//		if (count > 0)
//			return true;
//		else
//			return false;
//	}

	public Job getJob(int jobId) {
		String sql = "SELECT * FROM job WHERE JobId=?";

		List<Job> jobs = this.JobRowMapper(sql, new Object[]{ jobId });

		if(jobs.size() > 0) return jobs.get(0);
		else return null;
	}


	public List<Job> getFilteredJobs(FilterJobRequestDTO filter, List<Integer> alreadyLoadedFilteredJobIds) {
		// TODO Auto-generated method stub

		//Distance formula found here: https://developers.google.com/maps/articles/phpsqlsearch_v3?csw=1#finding-locations-with-mysql
		//This calculates the distance between each job and a given lat/lng.
		String sql = "SELECT *, "
					+ "( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
					+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) ) AS distance,"
					+ " (EndDate - StartDate + 1) AS duration"
					+ " FROM job WHERE job.JobId IN (SELECT job.JobId FROM job WHERE job.IsAcceptingApplications = 1 ";


		List<Object> argsList = new ArrayList<Object>();

		//Arguments for distance filter
		argsList.add(filter.getLat());
		argsList.add(filter.getLng());
		argsList.add(filter.getLat());

		//If there are no categories to filter on
		if(filter.getCategoryIds() == null){

//			sql += " WHERE job.IsActive = 1";

		//Else build the where condition for the categories
		}else{

			sql += " INNER JOIN job_category ON job.JobId = job_category.JobId WHERE job.IsActive = 1 AND (";
			for(int i = 0; i < filter.getCategoryIds().length; i++){
				if(i < filter.getCategoryIds().length - 1){
					sql += " job_category.CategoryId = ? OR";
				}else{
					sql += " job_category.CategoryId = ?)";
				}

				argsList.add(filter.getCategoryIds()[i]);
			}

		}

		//Start time
		if(filter.getStartTime() != null){
			sql += " AND job.StartTime";
			if(filter.getBeforeStartTime()){
				sql += " <= ?";
			}else{
				sql += " >= ?";
			}

			argsList.add(filter.getStartTime());
		}

		//End time
		if(filter.getEndTime() != null){
			sql += " AND job.EndTime";
			if(filter.getBeforeEndTime()){
				sql += " <= ?";
			}else{
				sql += " >= ?";
			}

			argsList.add(filter.getEndTime());
		}

		//Start date
		if(filter.getStartDate() != null){
			sql += " AND job.StartDate";
			if(filter.getBeforeStartDate()){
				sql += " <= ?";
			}else{
				sql += " >= ?";
			}

			argsList.add(filter.getStartDate());
		}

		//End date
		if(filter.getEndDate() != null){
			sql += " AND job.EndDate";
			if(filter.getBeforeEndDate()){
				sql += " <= ?";
			}else{
				sql += " >= ?";
			}

			argsList.add(filter.getEndDate());
		}

		//Duration
		if(filter.getDuration() > 0){
			sql += " AND job.EndDate - job.StartDate";
			if(filter.getLessThanDuration()){
				sql += " <= ?";
			}else{
				sql += " >= ?";
			}

			argsList.add(filter.getDuration());
		}
		
		//Skip already-loaded jobs
		if(alreadyLoadedFilteredJobIds != null){
			
			for(Integer id : alreadyLoadedFilteredJobIds){
				sql += " AND job.jobId <> ?";
				argsList.add(id);
			}
		}


		//Close the sub query and complete the distance filter.
		argsList.add(filter.getRadius());		
		sql += ") HAVING distance < ?";
		
		//Order by
		sql += " ORDER BY ";
		if(filter.getSortBy() != null){
			sql += filter.getSortBy() + " ";
			if(filter.getIsAscending()){
				sql += "ASC";
			}else{
				sql += "DESC";
			}
		}else{
			//If user did not sort, the sort by ascending distance as a default
			sql += " distance ASC";
		}
		
		
		
		//Number of jobs to return
		//argsList.add(filter.getReturnJobCount());
		sql += " LIMIT 0 , 25";

		return this.JobRowMapper(sql, argsList.toArray());
	}

	
	//*******************************************************************
	//*******************************************************************
	//This method can be deleted if the job objects that are filtered are stored in session,
	//not the job ids
	//*******************************************************************
	//*******************************************************************
	public List<Job> sortJobs(List<Integer> jobIds, String sortBy, boolean isAscending) {
		
		List<Object> argsList = new ArrayList<Object>();		
		String sql = "SELECT * FROM job WHERE ";		
		
		
//		if(sortBy.matches("Distance")){
//			
//		}
		
		
		//Append job ids
		boolean first = true;
		for(Integer id : jobIds){
			if(first) {
				sql += "JobId = ?";
				first = false;
			}else{
				sql += " OR JobId = ?";
			}
			
			argsList.add(id);
		}
		
		//Order by
		sql += " ORDER BY ?";
		argsList.add(sortBy);
		
		if(isAscending){
			sql += " ASC";
		}else{
			sql += " DESC";
		}
				
		return JobRowMapper(sql, argsList.toArray());
	}



}
