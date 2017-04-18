package com.jobsearch.job.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.FindJobFilterDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WorkDay;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.VerificationServiceImpl;


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

	@Autowired
	VerificationServiceImpl verificationService;
	
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
					e.setStatus(rs.getInt("Status"));
					e.setIsPartialAvailabilityAllowed(rs.getBoolean("IsPartialAvailabilityAllowed"));
					e.setPositionsPerDay(rs.getInt("PositionsPerDay"));
					
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
					
					e.setWorkDayId(rs.getInt("WorkDayId"));
					e.setStringStartTime(rs.getString("StartTime"));
					e.setStringEndTime(rs.getString("EndTime"));
					e.setDateId(rs.getInt("DateId"));					
					
					e.setStringDate(jobService.getDate(e.getDateId()).replace("-", "/"));			
					e.setDate(LocalDate.parse(e.getStringDate().replace("/", "-")));
					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}
	
	public List<Skill> SkillRowMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<Skill>() {

				@Override
				public Skill mapRow(ResultSet rs, int rownumber) throws SQLException {
					
					Skill e = new Skill();
										
					e.setSkillId(rs.getInt("SkillId"));
					e.setText(rs.getString("Text"));
					e.setType(rs.getInt("Type"));
					e.setJobId(rs.getInt("JobId"));
					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}
	
	public List<FindJobFilterDTO> FindJobFilterDtoMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<FindJobFilterDTO>() {

				@Override
				public FindJobFilterDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
					
					FindJobFilterDTO e = new FindJobFilterDTO();
					
					e.setId(rs.getInt("Id"));
										
					e.setStartDate(rs.getDate("StartDate"));					
					e.setBeforeStartDate(rs.getBoolean("IsBeforeStartDate"));
					
					e.setStartTime(rs.getTime("StartTime"));
					e.setBeforeStartTime(rs.getBoolean("IsBeforeStartTime"));
					
					e.setEndDate(rs.getDate("EndDate"));
					e.setBeforeEndDate(rs.getBoolean("IsBeforeEndDate"));
					
					e.setEndTime(rs.getTime("EndTime"));
					e.setBeforeEndTime(rs.getBoolean("IsBeforeEndTime"));
					
					e.setDuration(rs.getDouble("Duration"));
					if(e.getDuration() <= 0) e.setDuration(null);
					e.setIsShorterThanDuration(rs.getBoolean("IsShorterThanDuration"));

					e.setSavedName(rs.getString("Name"));
					e.setCity(rs.getString("City"));
					e.setState(rs.getString("State"));
					e.setZipCode(rs.getString("ZipCode"));
					e.setRadius(rs.getInt("Radius"));
					e.setEmailFrequencyId(rs.getInt("EmailFrequencyId"));
					e.setUserId(rs.getInt("UserId"));
					

					// Set Local Dates and Local Times
					try {
						e.setStartDate_local(e.getStartDate().toLocalDate());
					} catch (Exception e2) {
						// TODO: handle exception
					}
					
					try {
						e.setEndDate_local(e.getEndDate().toLocalDate());
					} catch (Exception e2) {
						// TODO: handle exception
					}
					
					try {
						e.setStartTime_local(e.getStartTime().toLocalTime());
					} catch (Exception e2) {
						// TODO: handle exception
					}
					
					try {
						e.setEndTime_local(e.getEndTime().toLocalTime());
					} catch (Exception e2) {
						// TODO: handle exception
					}
					
					
					return e;
				}
			});

		}catch(Exception e){
			return null;
		}

	}
	
	

	public void addJob(JobDTO jobDto, JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall(
					"{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, jobDto.getJob().getJobName());
			 cStmt.setInt(2, user.getUserId());
			 cStmt.setString(3, jobDto.getJob().getDescription());
			 cStmt.setString(4, jobDto.getJob().getStreetAddress());
			 cStmt.setString(5, jobDto.getJob().getCity());
			 cStmt.setString(6, jobDto.getJob().getState());
			 cStmt.setString(7, jobDto.getJob().getZipCode());
			 cStmt.setFloat(8,  jobDto.getJob().getLat());
			 cStmt.setFloat(9,  jobDto.getJob().getLng());
			 cStmt.setInt(10,  Job.STATUS_FUTURE);
			 cStmt.setBoolean(11, jobDto.getJob().getIsPartialAvailabilityAllowed());
			 cStmt.setInt(12, jobDto.getJob().getPositionsPerDay());

			 ResultSet result = cStmt.executeQuery();

			 // Set the newly created job
			 Job createdJob = new Job();
			 result.next();
			 createdJob.setId(result.getInt("JobId"));

			 // Add the job's categories to the database
			 for(Integer categoryId: jobDto.getCategoryIds()){
				cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call insertJobCategories(?, ?)}");

				cStmt.setInt(1, createdJob.getId());
				cStmt.setInt(2, categoryId);

				cStmt.executeQuery();
			}

			// Add the questions
			for(Question question : jobDto.getQuestions()){
				question.setJobId(createdJob.getId());
				applicationService.addQuestion(question);
			}

			// Add the work days
			jobService.addWorkDays(createdJob.getId(), jobDto.getWorkDays());
			
			// Add the skills
			jobService.addSkills(createdJob.getId(), jobDto.getSkills());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Job> getFilteredJobs(FindJobFilterDTO filter) {
		
		// ****************************************************************
		// ****************************************************************
		// Summary:
		// 1) The distance query is the only required query.
		// 2) All other filters are sub queries within sub queries.
		// 3) The sub queries are returning the job ids that match the particular filter.
		// ****************************************************************
		// ****************************************************************
		
		
		List<Object> argsList = new ArrayList<Object>();

		// **************************************************************
		//Main query: distance filter
		// **************************************************************
		//Distance formula found here: https://developers.google.com/maps/articles/phpsqlsearch_v3?csw=1#finding-locations-with-mysql
		String sql = "SELECT *, "
				+ "( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
				+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) ) AS distance"
				+ " FROM job j WHERE j.Status < 2";

		
		argsList.add(filter.getLat());
		argsList.add(filter.getLng());
		argsList.add(filter.getLat());
		
		
		String startNextSubQuery = " AND j.JobId IN (";
		int count_subQueries = 0;
		
		// **************************************************
		// Work day sub query
		// **************************************************	
		String sql_subQuery = null;
		if (verificationService.isListPopulated(filter.getWorkingDays())) {
			
			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs to be opened
			// for this sub query.
			if(startNextSubQuery != "") sql_subQuery = startNextSubQuery;
			else sql_subQuery = "";			

			sql_subQuery += " SELECT DISTINCT wd0.jobId";
			sql_subQuery += " FROM work_day wd0";


			// *************************************
			// If **ALL** work days are selected.
			// A job is returned if all their work days was selected by the user.
			// *************************************
			if(filter.getDoMatchAllDays()){
								
				int i = 1;
				for (String workDay : filter.getWorkingDays()) {
					
					if(i == filter.getWorkingDays().size()){
						
						// The WHERE clause must FOLLOW the JOINs
						sql_subQuery += " WHERE wd0.DateId = ?";
					}
					else{
						sql_subQuery += " INNER JOIN work_day wd" + i
					 			+ " ON wd" + i +".JobId = wd" + (i-1) + ".JobId"
					 			+ " AND wd" + i + ".DateId = ?";			
			
										
					}
					argsList.add(jobService.getDateId(workDay));	
					i += 1;
				}					
										
			}
			else{
				
				// *************************************
				// If **At least one** work day was selected.
				// A job is returned if at least one of their word days was selected by the user.
				// *************************************	
				boolean isFirst = true;
				sql_subQuery += " WHERE (";
				for (String workDay : filter.getWorkingDays()) {
					
					if(!isFirst) sql_subQuery += " OR ";
					sql_subQuery += " wd0.DateId = ?";
					
					argsList.add(jobService.getDateId(workDay));	
					isFirst = false;
				}		
				
				// Close the where clause
				sql_subQuery += ")";		
				
			}
				
			sql += sql_subQuery;
			
			count_subQueries += 1;
			
			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd0.JobId IN (";
		
		}	

		// ************************************************
		// Start date sub query
		// ************************************************
		if(filter.getStartDate() != null){
			
			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs to be opened
			// for this sub query.
			if(startNextSubQuery != "") sql_subQuery = startNextSubQuery;
			else sql_subQuery = "";
			
			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " INNER JOIN date d ON d.Id = wd.DateId";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING MIN(d.Date)";
			
			if(filter.getBeforeStartDate()) sql_subQuery += " <= ?";
			else sql_subQuery += " >= ?";
			
			argsList.add(filter.getStartDate());
			
			sql += sql_subQuery;
			
			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";		
		}	
		
		
		// ************************************************
		// End date sub query
		// ************************************************
		if(filter.getEndDate() != null){
			
			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs to be opened
			// for this sub query.
			if(startNextSubQuery != "") sql_subQuery = startNextSubQuery;
			else sql_subQuery = "";
			
			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " INNER JOIN date d ON d.Id = wd.DateId";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING MAX(d.Date)";
			
			if(filter.getBeforeEndDate()) sql_subQuery += " <= ?";
			else sql_subQuery += " >= ?";
			
			argsList.add(filter.getEndDate());
			
			sql += sql_subQuery;
			
			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";		
		}		
		
		
		// ************************************************
		// Start time sub query
		// ************************************************
		if(filter.getStartTime() != null){
			
			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs to be opened
			// for this sub query.
			if(startNextSubQuery != "") sql_subQuery = startNextSubQuery;
			else sql_subQuery = "";
			
			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";
			
			if(filter.getBeforeStartTime()) sql_subQuery += " HAVING MAX(wd.StartTime) <= ?";
			else sql_subQuery += " HAVING MIN(wd.StartTime) >= ?";
			
			argsList.add(filter.getStartTime());
			
			sql += sql_subQuery;
			
			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";		
		}		
		
		// ************************************************
		// End time sub query
		// ************************************************
		if(filter.getEndTime() != null){
			
			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs to be opened
			// for this sub query.
			if(startNextSubQuery != "") sql_subQuery = startNextSubQuery;
			else sql_subQuery = "";
			
			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";
			
			if(filter.getBeforeEndTime()) sql_subQuery += " HAVING MAX(wd.EndTime) <= ?";
			else sql_subQuery += " HAVING MIN(wd.EndTime) >= ?";
			
			argsList.add(filter.getEndTime());
			
			sql += sql_subQuery;
			
			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";		
		}	
				
		// ************************************************
		// Duration sub query
		// ************************************************
		if(filter.getEndTime() != null){
			
			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs to be opened
			// for this sub query.
			if(startNextSubQuery != "") sql_subQuery = startNextSubQuery;
			else sql_subQuery = "";
			
			sql_subQuery += " SELECT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING COUNT(wd.DateId)";
			
			if(filter.getIsShorterThanDuration()) sql_subQuery += " <= ?";
			else sql_subQuery += " >= ?";
			
			argsList.add(filter.getDuration());
			
			sql += sql_subQuery;
			
			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";		
		}			
		
		// Close the sub queries before finishing the distance query
		int i = 0;
		while(i < count_subQueries){
			sql += " )";
			i += 1;
		}
		
	
		// Only closed jobs
//		sql += " WHERE j.Status < 2";

		//Complete the distance filter.			
		sql += " HAVING distance < ?";
		argsList.add(filter.getRadius());	
		
		//Skip already-loaded jobs
		if(filter.getJobIdsToExclude() != null){
			
			for(Integer id : filter.getJobIdsToExclude()){
				sql += " AND j.jobId <> ?";
				argsList.add(id);
			}
		}
		
		//Order by
		sql += " ORDER BY ";
//		if(filter.getSortBy() != null){
//			sql += filter.getSortBy() + " ";
//			if(filter.getIsAscending()){
//				sql += "ASC";
//			}else{
//				sql += "DESC";
//			}
//		}else{
			//If user did not sort, then sort by ascending distance as a default
			sql += " distance ASC";
//		}
		
		
		
		//Number of jobs to return
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
	
	public List<Job> getJobs_ByEmplyeeAndStatuses(int userId_employee, List<Integer> jobStatuses) {
		
		
		String sql = "SELECT * FROM job j"
					+ " INNER JOIN employment e ON j.JobId = e.JobId"
					+ " WHERE e.UserId = ?"
					+ " AND e.WasTerminated = 0"
					+ " AND (";
		
		List<Object> args = new ArrayList<Object>();
		args.add(userId_employee);
		
		boolean isFirst = true;
		for(Integer jobStatus : jobStatuses){
			
			if(isFirst) sql += "j.Status = ?";
			else sql += " OR j.Status = ?";
		
			isFirst = false;
			args.add(jobStatus);					
		}
		
		sql += ")";
		
		return JobRowMapper(sql, args.toArray());
	}

	public List<Job> getJobs_byEmployerAndStatuses(int userId_employer, List<Integer> jobStatuses) {
		
		
		String sql = "SELECT * FROM job j"
					+ " WHERE j.UserId = ? AND (";
		
		List<Object> args = new ArrayList<Object>();
		args.add(userId_employer);
		
		boolean isFirst = true;
		for(Integer jobStatus : jobStatuses){ 
			
			if(isFirst) sql += "j.Status = ?";
			else sql += " OR j.Status = ?";
		
			isFirst = false;
			args.add(jobStatus);					
		}
		
		sql += ")";
		
		return JobRowMapper(sql, args.toArray());
	}


	public Job getJob(int jobId) {

	   String sql = "SELECT * FROM job j WHERE j.JobId = ?";

		List<Job> jobs = this.JobRowMapper(sql, new Object[] { jobId });

		if(verificationService.isListPopulated(jobs)) return jobs.get(0);
		else return null;
	}


	public Job getJobByApplicationId(int applicationId) {
		String sql = "SELECT * FROM job j INNER JOIN application a"
						+ " ON j.JobId = a.JobId WHERE a.ApplicationId = ?";

		return this.JobRowMapper(sql, new Object[]{ applicationId }).get(0);
	}

	public void addWorkDay(int jobId, WorkDay workDay) {
		
		
		// *******************************************************
		// *******************************************************
		// If the date is in yyyy-mm-dd format, does it need to be
		// converted to a Sql Date object????
		// Review this.
		// *******************************************************
		// *******************************************************
		
		String sql = "INSERT INTO work_day (JobId, StartTime, EndTime, DateId)"
						+ "  VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, new Object[]{ jobId,
												workDay.getStringStartTime(),
												workDay.getStringEndTime(),
												workDay.getDateId()});

	}

	public Date getEndDate(int jobId) {
		String sql = "SELECT MAX(d.Date) FROM date d"
					+ " INNER JOIN work_day wd ON wd.DateId = d.Id"
					+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Date.class);
	}

	public Date getStartDate(int jobId) {
		String sql = "SELECT MIN(d.Date) FROM date d"
				+ " INNER JOIN work_day wd ON wd.DateId = d.Id"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, Date.class);
	}
	
	public LocalDate getStartLocalDate(int jobId) {
		String sql = "SELECT MIN(d.Date) FROM date d"
				+ " INNER JOIN work_day wd ON wd.DateId = d.Id"
				+ " WHERE JobId = ?";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId }, LocalDate.class);
	}
	
	public LocalDate getEndLocalDate(int jobId) {
		String sql = "SELECT MAX(d.Date) FROM date d"
				+ " INNER JOIN work_day wd ON wd.DateId = d.Id"
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
		String sql = "SELECT * FROM work_day WHERE JobId = ? ORDER BY DateId ASC";
		return this.WorkDayMapper(sql, new Object[]{ jobId });
	}
	
	
	public int getRatingCountByJobAndRatingValue(int jobId, double value){

		String sql = "SELECT COUNT(*) FROM rating WHERE JobId = ? AND Value = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { jobId, value }, Integer.class);

	}

	public void insertSavedFindJob(FindJobFilterDTO filter, JobSearchUser user) {
		
		String columns = "";
		String values = "";
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<Object> args = new ArrayList<Object>();
		
		
		columnNames.add("Name");
		args.add(filter.getSavedName());
		columnNames.add("Radius");
		args.add(filter.getRadius());
		columnNames.add("UserId");
		args.add(user.getUserId());
		columnNames.add("EmailFrequencyId");
		args.add(filter.getEmailFrequencyId());
		
		
		if(filter.getCity() != null){
			columnNames.add("City");
			args.add(filter.getCity());
		}
		
		if(filter.getState() != null){
			columnNames.add("State");
			args.add(filter.getState());
		}
		
		if(filter.getZipCode() != null){
			columnNames.add("ZipCode");
			args.add(filter.getZipCode());
		}

		
		if(filter.getStartDate() != null){
			columnNames.add("StartDate");
			columnNames.add("IsBeforeStartDate");
			args.add(filter.getStartDate());
			args.add(filter.getBeforeStartDate());
		}
		
		if(filter.getEndDate() != null){
			columnNames.add("EndDate");
			columnNames.add("IsBeforeEndDate");
			args.add(filter.getEndDate());
			args.add(filter.getBeforeEndDate());
		}
		
		if(filter.getStartTime() != null){
			columnNames.add("StartTime");
			columnNames.add("IsBeforeStartTime");
			args.add(filter.getStartTime());
			args.add(filter.getBeforeStartTime());
		}
		
		if(filter.getEndTime() != null){
			columnNames.add("EndTime");
			columnNames.add("IsBeforeEndTime");
			args.add(filter.getEndTime());
			args.add(filter.getBeforeEndTime());
		}		
		
		if(filter.getDuration() != null){
			columnNames.add("Duration");
			columnNames.add("IsShorterThanDuration");
			args.add(filter.getDuration());
			args.add(filter.getIsShorterThanDuration());
		}
		
		boolean isFirst = true;
		for(String columnName : columnNames){
			if(isFirst){
				columns = " (" + columnName;
				values = " (?";
				isFirst = false;
			}
			else{
				columns += ", " + columnName;
				values += ", ?";
			}					
		}		
		columns += ")";	
		values += ")";
		
		String sql = "INSERT INTO saved_find_job_filter" + columns + " VALUES" + values;  
		
		jdbcTemplate.update(sql, args.toArray());
	}

	public List<FindJobFilterDTO> getSavedFindJobFilters(int userId) {
		String sql = "SELECT * FROM saved_find_job_filter WHERE UserId = ?";
		return this.FindJobFilterDtoMapper(sql, new Object[]{ userId });
	}

	public FindJobFilterDTO getSavedFindJobFilter(int savedFindJobFilterId) {
		String sql = "SELECT * FROM saved_find_job_filter WHERE Id = ?";
		try {
			return this.FindJobFilterDtoMapper(sql, new Object[]{ savedFindJobFilterId }).get(0);	
		} catch (Exception e) {
			return null;
		}
		
	}

	public List<Job> getJobsByIds(List<Integer> jobIds) {
		
		
		String sql = "SELECT * FROM job j WHERE";
		List<Object> args = new ArrayList<Object>();
		
		boolean isFirst = true;
		for(Integer jobId : jobIds){
			
			if(!isFirst) sql += " OR";
			sql += " j.JobId = ?";
			args.add(jobId);
			
			isFirst = false;
		}

		return this.JobRowMapper(sql, args.toArray());
	}

	public Integer getDateId(String date) {
		// ***************************************************
		// date must be in the form yyyy-mm-dd
		// ***************************************************
		String sql = "SELECT Id FROM date where Date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{ date }, Integer.class);
	}

	public List<Job> getJobs_ByEmployer(int userId) {
		
		String sql = "SELECT * FROM job WHERE UserId = ?";
		return JobRowMapper(sql, new Object[]{ userId });
	}

	public List<Job> getJobs_needRating_byUser(int userId) {

		// The sub query is required because **employers**, if a job had more than 1 employee,
		// will return the same job more than once.
		
		String sql = "SELECT * FROM job j WHERE j.JobId IN ("
						+ " SELECT DISTINCT(j1.JobId) FROM job j1"
						+ " INNER JOIN rating r ON r.JobId = j1.JobId "
						+ " WHERE r.RatedByUserId = ?"
						+ " AND r.Value = ?"
						+ " AND j1.Status = ? )";
		
		return JobRowMapper(sql, new Object[]{ userId, RateCriterion.VALUE_NOT_YET_RATED, Job.STATUS_PAST });
	}

	public void addSkill(Integer jobId, Skill skill) {
		
		String sql = "INSERT INTO skill (Text, Type, JobId) VALUES (?, ?, ?)";		
		jdbcTemplate.update(sql, new Object[]{ skill.getText(), skill.getType(), jobId });
		
	}

	public List<Skill> getSkills_ByType(int jobId, int type) {
		String sql = "SELECT * FROM skill WHERE JobId = ? AND Type = ?";
		return SkillRowMapper(sql, new Object[]{ jobId, type });
	}

	
	public int getCount_JobsCompleted_ByCategory(int userId, int categoryId) {

		String sql = "SELECT COUNT(*) FROM job j"
					+ " INNER JOIN job_category jc ON jc.JobId = j.JobId"
					+ " INNER JOIN application a ON a.JobId = j.JobId"
					+ " WHERE j.Status = ?"
					+ " AND a.UserId = ?"
					+ " AND a.Status = ?"
					+ " AND jc.CategoryId = ?";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ Job.STATUS_PAST,
																userId,
																Application.STATUS_ACCEPTED,
																categoryId }, Integer.class);

	}

	public int getCount_JobsCompleted_ByUser(int userId) {
		
		String sql = "SELECT COUNT(*) FROM job j"
				+ " INNER JOIN application a ON a.JobId = j.JobId"
				+ " WHERE j.Status = ?"
				+ " AND a.UserId = ?"
				+ " AND a.Status = ?";
	
		return jdbcTemplate.queryForObject(sql, new Object[]{ Job.STATUS_PAST,
															userId,
															Application.STATUS_ACCEPTED },
																Integer.class);
	}

	public Integer getCount_unavailableDays_ByUserAndWorkDays(int userId, List<WorkDay> workDays) {
		
		// Main query
		String sql = "SELECT COUNT(e.Id) FROM employment e"
					+ " JOIN job j ON e.JobId = e.JobId"
					+ " JOIN application a ON j.JobId = a.JobId"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
					+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
					+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE e.UserId = ?"
					+ " AND e.WasTerminated = 0"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND j.Status";

		
		List<Object> args = new ArrayList<Object>();
		args.add(userId);
		
		boolean isFirst = true;		
		sql += " AND (";
		for(WorkDay workDay : workDays){
			
			if(!isFirst) sql += " OR ";			
			sql += " wd.DateId = ?";
			isFirst = false;
			
			workDay.setDateId(jobService.getDateId(workDay.getStringDate()));
			args.add(workDay.getDateId());
			
		}
		
		// Close the AND
		sql += ")";
	
		
		return jdbcTemplate.queryForObject(sql, args.toArray(), Integer.class);
	}

	public String getDateId(int dateId) {
		String sql = "SELECT Date From Date WHERE Id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{ dateId }, String.class);
	}

	public Integer getCount_employmentDays_byUserAndWorkDays(int userId, List<WorkDay> workDays) {
		
		// Find a user's employment in which the work days for theur employed-for jobs
		// span the passed-in work days.
		// The "passed-in work days" are typically the work days in which a user is
		// attempting to APPLY for.
		// ________________________________
		// For a time conflict to exist, the following conditions MUST be met:
		// For a particular calendar day:
		// 1) The START TIME for the applying-for job must be LESS THAN the
		//	 	END TIME of the employed-for job
		//		---- AND ----
		// 2) The END TIME for the applying-for job must GREATER THAN the
		// 		START TIME of the employed-for job
		// Simply said: A time conflict exists if the applying-for job starts before
		//				an employed-for job ends AND the applying-for job ends after
		//				an employed-for job starts
		// ____________________________________
		// I.e.
		// ---- If ----
		// the passed work days are (the days you are applying for):
		// 1-A) 4-1 5:00 to 4-1 13:00
		// 2-A) 4-2 5:00 to 4-2 13:00
		// ---- AND ----
		// the user's employment work days are (the days to compare against):
		// 1-E) 4-1 11:00 to 4-1 17:00
		// 2-E) 4-2 15:00 to 4-2 17:00
		// ---- THEN ----
		// the returned count would be 1;
		// 1-A starts before 1-E ends; AND 1-A ends after 1-E starts; thus a conflict;  
		// Although 2-A starts before 2-E ends, 2-A ends before 2-E starts; thus NOT a conflict;
		
		// Main query
		String sql = "SELECT COUNT(*) FROM work_day wd"
						+ " JOIN employment e ON e.JobId = wd.JobId"
						+ " JOIN job j ON e.JobId = j.JobId"
						+ " WHERE e.UserId = ?"
						+ " AND j.Status != ?"
						+ " AND (";
		
		List<Object> args = new ArrayList<Object>();
		args.add(userId);
		args.add(Job.STATUS_PAST);
		
		boolean isFirst = true;
		for(WorkDay workDay : workDays){
			
			if(!isFirst) sql += " OR ";
			
			sql += "(";
			sql += " wd.DateId = ?";
			sql += " AND ? < wd.EndTime";
			sql += " AND ? > wd.StartTime";
			sql += ")";
			isFirst = false;
			
			workDay.setDateId(jobService.getDateId(workDay.getStringDate()));
		
			args.add(workDay.getDateId());
			args.add(workDay.getStringStartTime());
			args.add(workDay.getStringEndTime());
			
		}
		
		// Close the AND
		sql += ")";
				
		return jdbcTemplate.queryForObject(sql, args.toArray(), Integer.class);
	}

	public Integer getWorkDayId(int jobId, int dateId) {
		
		String sql = "SELECT WorkDayId FROM work_day WHERE JobId = ? and DateId = ?"; 
		return jdbcTemplate.queryForObject(sql, new Object[]{ jobId,  dateId }, Integer.class);
	}

	public List<String> getDateStrings_UnavailableWorkDays(int userId, List<WorkDay> workDays) {
		
		
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT d.Date from date d WHERE d.Id NOT IN (";
		
		// **********************************************
		// Structure:
		// If the user is either:
		// 1) unavailable
		// 2) or employed
		// on the requested days,
		// then return the date id
		// **********************************************
		
		// Unavailability sub query.
		sql += " SELECT a.DateId FROM availability a WHERE a.UserId = ?";
		args.add(userId);		

		boolean isFirst = true;
		for(WorkDay workDay : workDays){
			if(isFirst) sql += " AND (";
			else sql += " OR";
			
			sql += " a.DateId = ?";
			workDay.setDateId(jobService.getDateId(workDay.getStringDate()));
			args.add(workDay.getDateId());
			
			isFirst = false;
		}
		
		// Close the AND clause
		sql += " ) ";
		
		
		// Employment sub query.
		sql += " OR a.DateId IN ("
				+ " SELECT DISTINCT(d1.Id) FROM date d1"
				+ " INNER JOIN work_day wd ON wd.DateId = d1.Id"
				+ " INNER JOIN employment e ON e.JobId = wd.JobId"
				+ " WHERE e.UserId = ?";
		
		args.add(userId);
		

		isFirst = true;
		for(WorkDay workDay : workDays){
			if(isFirst) sql += " AND (";
			else sql += " OR";
			
			sql += " wd.DateId = ?";
			args.add(workDay.getDateId());
			
			isFirst = false;
		}
				
		// Close the AND and two IN clauses
		sql += " ) ) )";			
				
		
		// Restrict the main query to the work days in question
		isFirst = true;
		for(WorkDay workDay : workDays){
			if(isFirst) sql += " AND (";
			else sql += " OR";
			
			sql += " d.Id = ?";
			workDay.setDateId(jobService.getDateId(workDay.getStringDate()));
			args.add(workDay.getDateId());
			
			isFirst = false;
		}	
		// Close the AND clausec
		sql += " ) ";		
		
		
		return jdbcTemplate.queryForList(sql, args.toArray(), String.class);
	}

	public Job getConflictingEmployment_byUserAndWorkDay(int userId, int DateId) {
		String sql = "SELECT * FROM job j"
					+ " JOIN employment e ON j.JobId = e.JobId"
					+ " JOIN application a ON e.JobId = a.JobId AND e.UserId = a.UserId"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
					+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
					+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " WHERE e.UserId = ?"
					+ " AND e.WasTerminated = 0"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND wd.DateId= ?"
					+ " AND j.Status != ?";
		
		List<Job> jobs = JobRowMapper(sql, new Object[]{ userId, DateId, Job.STATUS_PAST });
		if(verificationService.isListPopulated(jobs))
			return jobs.get(0);
		else return null;
	}
	

	public List<WorkDay> getWorkDays_byProposalId(Integer employmentProposalId) {
		String sql = "SELECT * FROM work_day wd"
					+ " JOIN employment_proposal_work_day ep ON wd.WorkDayId = ep.WorkDayId"
					+ " WHERE ep.EmploymentProposalId = ?";
		
		return WorkDayMapper(sql, new Object[]{ employmentProposalId });
	}

	public List<String> getWorkDayDateStrings(int jobId) {
		String sql = "SELECT d.Date FROM date d"
					+ " JOIN work_day wd ON d.Id = wd.DateID"
					+ " WHERE wd.JobId = ?";
		
		return jdbcTemplate.queryForList(sql, new Object[]{ jobId }, String.class);
	}

	public void replaceEmployee(int jobId, int userId) {
		
		String sql = "UPDATE employment SET WasTerminated = 1 WHERE JobId = ? AND UserId = ?";
		jdbcTemplate.update(sql, new Object[]{ jobId, userId });
		
	}

	public void deleteProposedWorkDays(List<WorkDay> workDays) {
		String sql = "DELETE FROM employment_proposal_work_day"
					+ " WHERE (";
		
		ArrayList<Object> args = new ArrayList<Object>();
		boolean isFirst = true;
		for(WorkDay workDay : workDays){
			
			if(!isFirst) sql += " OR";
			sql += " WorkDayId = ?";
			args.add(workDay.getWorkDayId());		
			
			isFirst = false;
		}		
		sql += " )";
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void deleteWorkDays(List<WorkDay> workDays) {
		String sql = "DELETE FROM work_day"
				+ " WHERE (";
	
	ArrayList<Object> args = new ArrayList<Object>();
	boolean isFirst = true;
	for(WorkDay workDay : workDays){
		
		if(!isFirst) sql += " OR";
		sql += " WorkDayId = ?";
		args.add(workDay.getWorkDayId());		
		
		isFirst = false;
	}
	sql += " )";
	
	jdbcTemplate.update(sql, args.toArray());
		
	}

	public void updateWorkDay(int workDayId, String stringStartTime, String stringEndTime) {
		String sql = "UPDATE work_day SET StartTime = ?, EndTime = ? WHERE WorkDayId = ?";
		
		jdbcTemplate.update(sql, new Object[]{ stringStartTime, stringEndTime, workDayId });
		
	}

	public List<WorkDay> getWorkDays_byJobAndDateStrings(int jobId, List<String> dateStrings) {
		String sql = "SELECT * FROM work_day wd"
					+ " JOIN date d ON wd.DateId = d.Id"
					+ " WHERE wd.JobId = ?"
					+ " AND (";
					
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(jobId);
		
		boolean isFirst = true;
		for(String dateString : dateStrings){
			if(!isFirst) sql += " OR";
			sql += " d.Date = ?";
			args.add(dateString);
			isFirst = false;
		}
		sql += " )";
		return WorkDayMapper(sql, args.toArray());
	}





}

