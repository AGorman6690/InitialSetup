package com.jobsearch.job.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.job.service.PostJobDTO;
import com.jobsearch.job.service.PostQuestionDTO;
import com.jobsearch.job.service.WorkDay;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;

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

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<Job>() {

				@Override
				public Job mapRow(ResultSet rs, int rownumber) throws SQLException {
					Job e = new Job();


					int jobId = rs.getInt("JobId");

					e.setId(jobId);
					e.setJobName(rs.getString("JobName"));
					e.setUserId(rs.getInt("UserId"));
					e.setDescription(rs.getString("Description"));
					e.setStreetAddress(rs.getString("StreetAddress"));
					e.setCity(rs.getString("City"));
					e.setState(rs.getString("State"));
					e.setZipCode(rs.getString("ZipCode"));
					e.setLat(rs.getFloat("Lat"));
					e.setLng(rs.getFloat("Lng"));
					e.setDurationTypeId(rs.getInt("DurationTypeId"));

					e.setStartDate(jobService.getStartDate(jobId));
					e.setEndDate(jobService.getEndDate(jobId));
					e.setStartTime(jobService.getStartTime(jobId));
					e.setEndTime(jobService.getEndTime(jobId));

					//************************************************************************					
					//Once the method of storing work days is decided,
					//then remove these try blocks.
					//************************************************************************
					try {
						e.setStartDate_local(jobService.getStartLocalDate(jobId));	
					} catch (Exception e2) {
						e.setStartDate_local(null);
					}
					
					try {
						e.setEndDate_local(jobService.getEndLocalDate(jobId));	
					} catch (Exception e2) {
						e.setEndDate_local(null);
					}
					try {
						e.setStartTime_local(jobService.getStartLocalTime(jobId));	
					} catch (Exception e2) {
						e.setStartTime_local(null);
					}
					
					try {
						e.setEndTime_local(jobService.getEndLocalTime(jobId));	
					} catch (Exception e2) {
						e.setEndTime_local(null);
					}
					
					
					
					
					e.setStatus(rs.getInt("Status"));
					
					
					
					//The default **string** time format is, for example,: "3:30 PM"
					if (e.getStartTime() != null){
						e.setStringStartTime(DateUtility.formatSqlTime(e.getStartTime(), "h:mm a"));	
					}
					
					if(e.getEndTime() != null){
						e.setStringEndTime(DateUtility.formatSqlTime(e.getEndTime(), "h:mm a"));	
					}
					
					//The default **string" date format is, for example,: "Sun Dec 25, 2017"
					if (e.getStartTime() != null){
						e.setStringStartDate(DateUtility.formatSqlDate(e.getStartDate(), "E MMM d, y"));	
					}
					
					if(e.getEndDate() != null){
						e.setStringEndDate(DateUtility.formatSqlDate(e.getEndDate(), "E MMM d, y"));	
					}					
					

					//Set duration
//					DateTime dtStart = new DateTime(e.getStartDate());
//					DateTime dtEnd = new DateTime(e.getEndDate());
//					e.setDuration(Days.daysBetween(dtStart, dtEnd).getDays());
					
					try {
//						e.setStartDate(rs.getDate("work_day_StartDate"));
//						e.setEndDate(rs.getDate("work_day_EndDate"));
					} catch (Exception e2) {
						// TODO: handle exception
						Job r = new Job();
					}

					return e;
				}
			});

		} catch (Exception e) {
			return null;
		}

	}

	public List<WorkDay> WorkDayMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<WorkDay>() {

				@Override
				public WorkDay mapRow(ResultSet rs, int rownumber) throws SQLException {
					
					WorkDay e = new WorkDay();
										
					e.setDate(rs.getDate("Date"));
					e.setStringStartTime(rs.getString("StartTime"));
					e.setStringEndTime(rs.getString("EndTime"));
										
					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}
	
	public void addJob(List<PostJobDTO> jobDtos) {
		try {

			ResultSet result = null;
			for (PostJobDTO job : jobDtos) {
					CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call create_Job(?, ?, ?, ?, ?)}");

					cStmt.setString(1, job.getJobName());
					cStmt.setInt(2, job.getUserId());
					cStmt.setString(3, job.getDescription());
					cStmt.setInt(5, job.getOpenings());

					result = cStmt.executeQuery();

					Job createdJob = new Job();
					while (result.next()) {
						createdJob.setId(result.getInt("JobId"));
						createdJob.setJobName(result.getString("JobName"));
						createdJob.setUserId(result.getInt("UserId"));
						createdJob.setDescription(result.getString("Description"));
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

	public void addJob(Job job, JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall(
					"{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, job.getJobName());
			 cStmt.setInt(2, user.getUserId());
			 cStmt.setString(3, job.getDescription());
			 cStmt.setString(4, job.getStreetAddress());
			 cStmt.setString(5, job.getCity());
			 cStmt.setString(6, job.getState());
			 cStmt.setString(7, job.getZipCode());
			 cStmt.setFloat(8,  job.getLat());
			 cStmt.setFloat(9,  job.getLng());
			 cStmt.setInt(10, job.getDurationTypeId());

			 ResultSet result = cStmt.executeQuery();

			 //Set the newly created job
			 Job createdJob = new Job();
			 result.next();
			 createdJob.setId(result.getInt("JobId"));

			 //Add the job's categories to the database
			 for(Integer categoryId: job.getCategoryIds()){
				cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call insertJobCategories(?, ?)}");

					cStmt.setInt(1, createdJob.getId());
					cStmt.setInt(2, categoryId);

					cStmt.executeQuery();
			}

			for(PostQuestionDTO question : job.getQuestions()){
				question.setJobId(createdJob.getId());
				applicationService.addQuestion(question);
			}

			//Set the work days
			jobService.addWorkDays(createdJob.getId(), job.getWorkDays());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Job> getFilteredJobs(FilterJobRequestDTO filter, List<Integer> alreadyLoadedFilteredJobIds) {


//SQL example for filtering jobs.
//Copy and paste into MySQL for better viewing.
//***********************************************************
//***********************************************************
		
		
//	select j.*, 
//		( 3959 * acos( cos( radians( 45.104839) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(-93.144936) ) 
//		+ sin( radians( 45.104839) ) * sin( radians( lat ) ) ) ) AS distance,
//		(EndDate - StartDate + 1) as duration
//	from job j
//
//	-- work days
//	join (
//		select distinct w0.jobid
//		from work_day w0
//		join work_day w1 on w0.jobid = w1.jobid and w1.date = '2016-10-26'
//		join work_day w2 on w1.jobid = w2.jobid and w2.date = '2016-10-25'
//	) wd0
//	on wd0.jobid = j.jobid
//
//	-- categories
//	join (
//		select distinct jc0.jobid
//		from job_category jc0
//		join job_category jc1 on jc0.jobid = jc1.jobid and jc1.categoryid = '3'
//		join job_category jc2 on jc1.jobid = jc2.jobid and jc2.categoryid = '3'
//	) jc
//	on jc.jobid = wd0.jobid
//
//	-- start time
//	join(
//		select distinct jobid
//		from work_day
//		group by jobid
//		having min(starttime) < '07:30:00'
//	) wd1
//	on wd1.jobid = jc.jobid
//
//	-- end time
//	join(
//		select distinct jobid
//		from work_day
//		group by jobid
//		having min(endtime) > '17:30:00'
//	) wd2
//	on wd2.jobid = wd1.jobid
//
//	having distance < 16 and j.jobid <> 71 and j.jobid <> 73;		
		
//***********************************************************
//***********************************************************		

		List<Object> argsList = new ArrayList<Object>();

		//Arguments for distance filter

		
		String nextTableToJoin;
		String subTable;
		
		//Distance
		//Distance formula found here: https://developers.google.com/maps/articles/phpsqlsearch_v3?csw=1#finding-locations-with-mysql
		String sql = "SELECT *, "
					+ "( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
					+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) ) AS distance"
					+ " FROM job j";
		
		argsList.add(filter.getLat());
		argsList.add(filter.getLng());
		argsList.add(filter.getLat());
		
		nextTableToJoin = "j";
		
		//Duration Type
		if(filter.getDurationTypeIds() != null){
			boolean isFirst = true;
			sql += " WHERE (";
			for(Integer durationTypeId : filter.getDurationTypeIds()){
				
				if (isFirst){
					sql += " j.DurationTypeId = ?";
					isFirst = false;
				}
				else sql += " OR j.DurationTypeId = ?";
				
				argsList.add(durationTypeId);
			}
			
			sql += ")";
			
		}
		
		
		
		//Work days
		if(filter.getWorkingDays() != null){
			sql += " JOIN (";
			sql += " SELECT DISTINCT wd0.jobId";
			sql += " FROM work_day wd0";
			
			for (int i = 1; i < filter.getWorkingDays().size() + 1; i++) {
				
				String table1 = "wd" + (i - 1);
				String table2 = "wd" + i;
				
				sql += " JOIN work_day " + table2;
				sql += " ON " + table1 + ".jobId = " + table2 + ".jobId AND " + table2 + ".date = ?";
				
				argsList.add(filter.getWorkingDays().get(i - 1));
						
			}
			subTable = "wd_work_days";
		
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = subTable;
		}
					
		//Categories
		if(filter.getCategoryIds() != null){
			sql += " JOIN (";
			sql += " SELECT DISTINCT jc0.jobId";
			sql += " FROM job_category jc0";
			

			for (int i = 1; i < filter.getCategories().size() + 1; i++) {
				
				String table1 = "jc" + (i - 1);
				String table2 = "jc" + i;
				
				sql += " JOIN job_categories " + table2;
				sql += " ON " + table1 + ".jobId = " + table2 + ".jobId AND " + table2 + ".categoryId = ?";
				
				argsList.add(filter.getCategories().get(i).getId());
						
			}
			subTable = "job_category";
			
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = subTable;
		}					

		//Start time
		if(filter.getStartTime() != null){
			sql += " JOIN (";
			sql += " SELECT DISTINCT jobId";
			sql += " FROM work_day";
			sql += " GROUP BY jobId";
			
			if(filter.getBeforeStartTime()){
				sql += " HAVING MAX(startTime) <= ?";
			}
			else{
				sql += " HAVING MIN(startTime) >= ?";
			}
			
			argsList.add(filter.getStartTime());
			
			subTable = "wd_start_time";
			
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = "wd_start_time";
		}	
		
		//End time
		if(filter.getEndTime() != null){
			sql += " JOIN (";
			sql += " SELECT DISTINCT jobId";
			sql += " FROM work_day";
			sql += " GROUP BY jobId";
			
			if(filter.getBeforeEndTime()){
				sql += " HAVING MAX(endTime) <= ?";	
			}
			else{
				sql += " HAVING MIN(endTime) >= ?";
			}
			
			argsList.add(filter.getEndTime());
			
			subTable = "wd_end_time";
			
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = subTable;
		}	
		
		//Start date
		if(filter.getStartDate() != null){
			sql += " JOIN (";
			sql += " SELECT DISTINCT jobId";
			sql += " FROM work_day";
			sql += " GROUP BY jobId";
			sql += " HAVING MIN(Date)";
			
			if(filter.getBeforeStartDate()){
				sql += " <= ?";
			}
			else{
				sql += " >= ?";
			}
			
			argsList.add(filter.getStartDate());
			
			subTable = "wd_start_date";
			
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = subTable;
		}		

		//End date
		if(filter.getEndDate() != null){
			sql += " JOIN (";
			sql += " SELECT DISTINCT jobId";
			sql += " FROM work_day";
			sql += " GROUP BY jobId";
			sql += " HAVING MAX(Date)";
			
			if(filter.getBeforeEndDate()){
				sql += " <= ?";
			}
			else{
				sql += " >= ?";
			}
			
			argsList.add(filter.getEndDate());
			
			subTable = "wd_end_date";
			
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = subTable;
		}

			
		//Duration
		if(filter.getDuration() != null){
			sql += " JOIN (";
			sql += " SELECT jobId";
			sql += " FROM work_day";
			sql += " GROUP BY jobId";
			sql += " HAVING COUNT(jobId)";
			
			if(filter.getIsLessThanDuration()){
				sql += " <= ?";
			}
			else{
				sql += " >= ?";
			}
			
			argsList.add(filter.getDuration());
			
			subTable = "wd_duration";
			
			sql += ") " + subTable;
			sql += " ON " + subTable + ".jobId = " + nextTableToJoin + ".jobId";
			
			nextTableToJoin = subTable;
		}
		

		//Complete the distance filter.			
		sql += " HAVING distance < ?";
		argsList.add(filter.getRadius());	
		
		//Skip already-loaded jobs
		if(alreadyLoadedFilteredJobIds != null){
			
			for(Integer id : alreadyLoadedFilteredJobIds){
				sql += " AND job.jobId <> ?";
				argsList.add(id);
			}
		}
		
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
	
	
	
	public List<Job> getJobsByStatusAndByEmployer(int userId, int jobStatus) {
		String sql = "SELECT * FROM job WHERE Status = ? AND UserId = ?";
		return this.JobRowMapper(sql, new Object[] { jobStatus , userId});
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
	
	public List<Job> getJobsByEmployee(int employeeUserId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment ON job.JobId = employment.JobId"
				+ "	AND employment.UserId = ?";

		return this.JobRowMapper(sql, new Object[] { employeeUserId });
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

//		select j.* , MIN(w.Date) as start, MAX(w.Date) as end
//		from job j inner join work_day w on j.jobid = w.jobid where j.jobid = '65'

		String sql = "SELECT j.*, MIN(w.Date) as work_day_StartDate, MAX(w.Date) as work_day_EndDate"
						+ " FROM job j"
						+ " INNER JOIN work_day w ON j.JobId = w.JobId"
						+ " WHERE j.JobId = ?";

		List<Job> jobs = this.JobRowMapper(sql, new Object[] { jobId });

		if(jobs != null){
			return jobs.get(0);
		}
		return null;
	}


	public Job getJobByApplicationId(int applicationId) {
		String sql = "SELECT * FROM job j INNER JOIN application a"
						+ " ON j.JobId = a.JobId WHERE a.ApplicationId = ?";

		return this.JobRowMapper(sql, new Object[]{ applicationId }).get(0);
	}

	public void addWorkDay(int jobId, WorkDay workDay) {
		String sql = "INSERT INTO work_day (JobId, StartTime, EndTime, Date)"
						+ "  VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, new Object[]{ jobId, workDay.getStringStartTime(), workDay.getStringEndTime(), workDay.getDate() });

	}

	public Date getEndDate(int jobId) {
		String sql = "SELECT MAX(Date)"
				+ " FROM work_day"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Date.class);
	}

	public Date getStartDate(int jobId) {
		String sql = "SELECT MIN(Date)"
				+ " FROM work_day"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Date.class);
	}
	
//	public LocalDate getStartLocalDate(int jobId) {
//		String sql = "SELECT MIN(Date)"
//				+ " FROM work_day"
//				+ " WHERE JobId = ?";
//
//		
//		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, LocalDate.class);
//	}
//	
	public LocalDate getEndLocalDate(int jobId) {
		String sql = "SELECT MAX(Date)"
				+ " FROM work_day"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, LocalDate.class);
	}	

	public Time getStartTime(int jobId) {
		String sql = "SELECT MIN(StartTime)"
				+ " FROM work_day"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Time.class);
	}

	public Time getEndTime(int jobId) {
		String sql = "SELECT MAX(EndTime)"
				+ " FROM work_day"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Time.class);
	}

	public void updateJobStatus(int status, int jobId) {

		String sql = "UPDATE job set Status = ? WHERE JobId = ?";
		jdbcTemplate.update(sql, new Object[]{ status, jobId });

	}

	public List<Integer> getActiveJobIdsByDistance(float lat, float lng, int radius) {

		String sql = "SELECT JobId"
				+ " FROM job"
				+ " WHERE Status = 0"
				+ " AND ( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
				+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) ) <= ?";

		return jdbcTemplate.queryForList(sql, new Object[]{ lat,  lng, lat, radius }, Integer.class);
	}


	public Integer getDuration(int jobId) {
		
		String sql = "SELECT COUNT(*) FROM work_day WHERE JobId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Integer.class);
	}

	public List<WorkDay> getWorkDays(int jobId) {
		String sql = "SELECT * FROM work_day WHERE JobId = ?";
		return this.WorkDayMapper(sql, new Object[]{ jobId });
	}
	
	
	public int getRatingCountByJobAndRatingValue(int jobId, double value){

		String sql = "SELECT COUNT(*) FROM rating WHERE JobId = ? AND Value = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { jobId, value }, Integer.class);

	}




}

