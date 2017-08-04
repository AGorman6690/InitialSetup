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

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.jobsearch.application.service.Application;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.bases.BaseRepository;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.web.FindJobFilterDTO;
import com.jobsearch.job.web.JobDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.Skill;
import com.jobsearch.model.WorkDay;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.QuestionServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Repository
public class JobRepository extends BaseRepository {

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
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	QuestionServiceImpl questionService;

	private List<Job> JobRowMapper(String sql, Object[] args) {


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
					e.setStreetAddress_formatted(rs.getString("StreetAddress_Formatted"));
					e.setCity_formatted(rs.getString("City_Formatted"));
					e.setZipCode_formatted(rs.getString("ZipCode_Formatted"));

					e.setFlag_isNotAcceptingApplications(rs.getInt(Job.FLAG_IS_NOT_ACCEPTING_APPLICATIONS));

					e.setStartDate(jobService.getStartDate(jobId));
					e.setEndDate(jobService.getEndDate(jobId));
					e.setStartTime(jobService.getStartTime(jobId));
					e.setEndTime(jobService.getEndTime(jobId));

					// ************************************************************************
					// Once the method of storing work days is decided,
					// then remove these try blocks.
					// ************************************************************************
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

					// The default **string** time format is, for example,:
					// "3:30 PM"
					if (e.getStartTime() != null) {
						e.setStringStartTime(DateUtility.formatSqlTime(e.getStartTime(), "h:mm a"));
					}

					if (e.getEndTime() != null) {
						e.setStringEndTime(DateUtility.formatSqlTime(e.getEndTime(), "h:mm a"));
					}

					// The default **string" date format is, for example,: "Sun
					// Dec 25, 2017"
					if (e.getStartTime() != null) {
						if (e.getStartDate_local().getYear() == LocalDate.now().getYear())
							e.setStringStartDate(DateUtility.formatSqlDate(e.getStartDate(), "E MMM d"));
						else
							e.setStringStartDate(DateUtility.formatSqlDate(e.getStartDate(), "E MMM d, y"));
					}

					if (e.getEndDate() != null) {
						if (e.getEndDate_local().getYear() == LocalDate.now().getYear())
							e.setStringEndDate(DateUtility.formatSqlDate(e.getEndDate(), "E MMM d"));
						else
							e.setStringEndDate(DateUtility.formatSqlDate(e.getEndDate(), "E MMM d, y"));
					}

					return e;
				}
			});


	}



	public List<Skill> SkillRowMapper(String sql, Object[] args) {

		try {

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

		} catch (Exception e) {
			return null;
		}

	}

	public List<FindJobFilterDTO> FindJobFilterDtoMapper(String sql, Object[] args) {

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<FindJobFilterDTO>() {

				@Override
				public FindJobFilterDTO mapRow(ResultSet rs, int rownumber) throws SQLException {

					FindJobFilterDTO jobFilterDTO = buildJobFilterResult(rs);

					// Set Local Dates and Local Times
					try {
						jobFilterDTO.setStartDate_local(jobFilterDTO.getStartDate().toLocalDate());
					} catch (Exception e2) {
						// TODO: handle exception
					}

					try {
						jobFilterDTO.setEndDate_local(jobFilterDTO.getEndDate().toLocalDate());
					} catch (Exception e2) {
						// TODO: handle exception
					}

					try {
						jobFilterDTO.setStartTime_local(jobFilterDTO.getStartTime().toLocalTime());
					} catch (Exception e2) {
						// TODO: handle exception
					}

					try {
						jobFilterDTO.setEndTime_local(jobFilterDTO.getEndTime().toLocalTime());
					} catch (Exception e2) {
						// TODO: handle exception
					}

					return jobFilterDTO;
				}
			});

		} catch (Exception e) {
			return null;
		}

	}

	protected FindJobFilterDTO buildJobFilterResult(ResultSet rs) {
		FindJobFilterDTO jobFilterDTO = new FindJobFilterDTO();
		try {
			jobFilterDTO.setId(rs.getInt("Id"));

			jobFilterDTO.setStartDate(rs.getDate("StartDate"));
			jobFilterDTO.setBeforeStartDate(rs.getBoolean("IsBeforeStartDate"));

			jobFilterDTO.setStartTime(rs.getTime("StartTime"));
			jobFilterDTO.setBeforeStartTime(rs.getBoolean("IsBeforeStartTime"));

			jobFilterDTO.setEndDate(rs.getDate("EndDate"));
			jobFilterDTO.setBeforeEndDate(rs.getBoolean("IsBeforeEndDate"));

			jobFilterDTO.setEndTime(rs.getTime("EndTime"));
			jobFilterDTO.setBeforeEndTime(rs.getBoolean("IsBeforeEndTime"));

			jobFilterDTO.setDuration(rs.getDouble("Duration"));

			if (jobFilterDTO.getDuration() <= 0) {
				jobFilterDTO.setDuration(null);
			}

			jobFilterDTO.setIsShorterThanDuration(rs.getBoolean("IsShorterThanDuration"));

			jobFilterDTO.setSavedName(rs.getString("Name"));
			jobFilterDTO.setCity(rs.getString("City"));
			jobFilterDTO.setState(rs.getString("State"));
			jobFilterDTO.setZipCode(rs.getString("ZipCode"));
			jobFilterDTO.setRadius(rs.getInt("Radius"));
			jobFilterDTO.setEmailFrequencyId(rs.getInt("EmailFrequencyId"));
			jobFilterDTO.setUserId(rs.getInt("UserId"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobFilterDTO;
	}

	public void addJob(JobDTO jobDto, JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			cStmt.setString(1, jobDto.getJob().getJobName());
			cStmt.setInt(2, user.getUserId());
			cStmt.setString(3, jobDto.getJob().getDescription());
			cStmt.setString(4, jobDto.getJob().getStreetAddress());
			cStmt.setString(5, jobDto.getJob().getCity());
			cStmt.setString(6, jobDto.getJob().getState());
			cStmt.setString(7, jobDto.getJob().getZipCode());
			cStmt.setFloat(8, jobDto.getJob().getLat());
			cStmt.setFloat(9, jobDto.getJob().getLng());
			cStmt.setInt(10, Job.STATUS_FUTURE);
			cStmt.setBoolean(11, jobDto.getJob().getIsPartialAvailabilityAllowed());
			cStmt.setInt(12, jobDto.getJob().getPositionsPerDay());
			cStmt.setString(13, jobDto.getJob().getStreetAddress_formatted());
			cStmt.setString(14, jobDto.getJob().getCity_formatted());
			cStmt.setString(15, jobDto.getJob().getZipCode_formatted());

			ResultSet result = cStmt.executeQuery();

			// Set the newly created job
			Job createdJob = new Job();
			result.next();
			createdJob.setId(result.getInt("JobId"));

			// Add the questions
			for (Question question : jobDto.getQuestions()) {
				question.setJobId(createdJob.getId());
				questionService.addQuestion(question);
			}

			// Add the work days
			workDayService.addWorkDays(createdJob.getId(), jobDto.getWorkDays());

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
		// 3) The sub queries are returning the job ids that match the
		// particular filter.
		// ****************************************************************
		// ****************************************************************

		List<Object> argsList = new ArrayList<Object>();

		// **************************************************************
		// Main query: distance filter
		// **************************************************************
		// Distance formula found here:
		// https://developers.google.com/maps/articles/phpsqlsearch_v3?csw=1#finding-locations-with-mysql
		String sql = "SELECT *, "
				+ "( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
				+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) ) AS distance" 
				+ " FROM job j"
				+ " WHERE j.Status < 2"
				+ " AND j.Flag_IsNotAcceptingApplications = 0";

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
			// If there was a previous sub query, then another sub query needs
			// to be opened
			// for this sub query.
			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd0.jobId";
			sql_subQuery += " FROM work_day wd0";

			// *************************************
			// If **ALL** work days are selected.
			// A job is returned if all their work days was selected by the
			// user.
			// *************************************
			if (filter.getDoMatchAllDays()) {

				int i = 1;
				for (String workDay : filter.getWorkingDays()) {

					if (i == filter.getWorkingDays().size()) {

						// The WHERE clause must FOLLOW the JOINs
						sql_subQuery += " WHERE wd0.DateId = ?";
					} else {
						sql_subQuery += " INNER JOIN work_day wd" + i + " ON wd" + i + ".JobId = wd" + (i - 1)
								+ ".JobId" + " AND wd" + i + ".DateId = ?";

					}
					argsList.add(jobService.getDateId(workDay));
					i += 1;
				}

			} else {

				// *************************************
				// If **At least one** work day was selected.
				// A job is returned if at least one of their word days was
				// selected by the user.
				// *************************************
				boolean isFirst = true;
				sql_subQuery += " WHERE (";
				for (String workDay : filter.getWorkingDays()) {

					if (!isFirst)
						sql_subQuery += " OR ";
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
		if (filter.getStartDate() != null) {

			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs
			// to be opened
			// for this sub query.
			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " INNER JOIN date d ON d.Id = wd.DateId";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING MIN(d.Date)";

			if (filter.getBeforeStartDate())
				sql_subQuery += " <= ?";
			else
				sql_subQuery += " >= ?";

			argsList.add(filter.getStartDate());

			sql += sql_subQuery;

			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// ************************************************
		// End date sub query
		// ************************************************
		if (filter.getEndDate() != null) {

			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs
			// to be opened
			// for this sub query.
			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " INNER JOIN date d ON d.Id = wd.DateId";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING MAX(d.Date)";

			if (filter.getBeforeEndDate())
				sql_subQuery += " <= ?";
			else
				sql_subQuery += " >= ?";

			argsList.add(filter.getEndDate());

			sql += sql_subQuery;

			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// ************************************************
		// Start time sub query
		// ************************************************
		if (filter.getStartTime() != null) {

			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs
			// to be opened
			// for this sub query.
			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";

			if (filter.getBeforeStartTime())
				sql_subQuery += " HAVING MAX(wd.StartTime) <= ?";
			else
				sql_subQuery += " HAVING MIN(wd.StartTime) >= ?";

			argsList.add(filter.getStartTime());

			sql += sql_subQuery;

			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// ************************************************
		// End time sub query
		// ************************************************
		if (filter.getEndTime() != null) {

			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs
			// to be opened
			// for this sub query.
			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";

			if (filter.getBeforeEndTime())
				sql_subQuery += " HAVING MAX(wd.EndTime) <= ?";
			else
				sql_subQuery += " HAVING MIN(wd.EndTime) >= ?";

			argsList.add(filter.getEndTime());

			sql += sql_subQuery;

			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// ************************************************
		// Duration sub query
		// ************************************************
		if (filter.getEndTime() != null) {

			// Initialize the sub query's sql string.
			// If there was a previous sub query, then another sub query needs
			// to be opened
			// for this sub query.
			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING COUNT(wd.DateId)";

			if (filter.getIsShorterThanDuration())
				sql_subQuery += " <= ?";
			else
				sql_subQuery += " >= ?";

			argsList.add(filter.getDuration());

			sql += sql_subQuery;

			count_subQueries += 1;

			// If there is another sub query, this needs to start it
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// Close the sub queries before finishing the distance query
		int i = 0;
		while (i < count_subQueries) {
			sql += " )";
			i += 1;
		}

		// Only closed jobs
		// sql += " WHERE j.Status < 2";

		// Complete the distance filter.
		sql += " HAVING distance < ?";
		argsList.add(filter.getRadius());

		// Skip already-loaded jobs
		if (filter.getJobIdsToExclude() != null) {

			for (Integer id : filter.getJobIdsToExclude()) {
				sql += " AND j.jobId <> ?";
				argsList.add(id);
			}
		}

		// Order by
		sql += " ORDER BY ";
		// if(filter.getSortBy() != null){
		// sql += filter.getSortBy() + " ";
		// if(filter.getIsAscending()){
		// sql += "ASC";
		// }else{
		// sql += "DESC";
		// }
		// }else{
		// If user did not sort, then sort by ascending distance as a default
		sql += " distance ASC";
		// }

		// Number of jobs to return
		sql += " LIMIT 0 , 25";

		return this.JobRowMapper(sql, argsList.toArray());
	}

	public List<Job> getJobs_ByEmployeeAndStatuses(int userId_employee, List<Integer> jobStatuses) {

		String sql = "SELECT * FROM job j" + " INNER JOIN employment e ON j.JobId = e.JobId" + " WHERE e.UserId = ?"
				+ " AND e.WasTerminated = 0" + " AND (";

		List<Object> args = new ArrayList<Object>();
		args.add(userId_employee);

		boolean isFirst = true;
		for (Integer jobStatus : jobStatuses) {

			if (isFirst)
				sql += "j.Status = ?";
			else
				sql += " OR j.Status = ?";

			isFirst = false;
			args.add(jobStatus);
		}

		sql += ")";

		return JobRowMapper(sql, args.toArray());
	}

	public List<Job> getJobs_byEmployerAndStatuses(int userId_employer, List<Integer> jobStatuses) {

		String sql = "SELECT * FROM job j" + " WHERE j.UserId = ? AND (";

		List<Object> args = new ArrayList<Object>();
		args.add(userId_employer);

		boolean isFirst = true;
		for (Integer jobStatus : jobStatuses) {

			if (isFirst)
				sql += "j.Status = ?";
			else
				sql += " OR j.Status = ?";

			isFirst = false;
			args.add(jobStatus);
		}

		sql += ")";

		return JobRowMapper(sql, args.toArray());
	}

	public Job getJob(int jobId) {

		String sql = "SELECT * FROM job j WHERE j.JobId = ?";

		List<Job> jobs = this.JobRowMapper(sql, new Object[] { jobId });

		if (verificationService.isListPopulated(jobs))
			return jobs.get(0);
		else
			return null;
	}

	public Job getJob_byApplicationId(int applicationId) {
		String sql = "SELECT * FROM job j INNER JOIN application a" + " ON j.JobId = a.JobId WHERE a.ApplicationId = ?";

		return this.JobRowMapper(sql, new Object[] { applicationId }).get(0);
	}



	public Date getEndDate(int jobId) {
		String sql = "SELECT MAX(d.Date) FROM date d" + " INNER JOIN work_day wd ON wd.DateId = d.Id"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { jobId }, Date.class);
	}

	public Date getStartDate(int jobId) {
		String sql = "SELECT MIN(d.Date) FROM date d" + " INNER JOIN work_day wd ON wd.DateId = d.Id"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { jobId }, Date.class);
	}

	public LocalDate getStartLocalDate(int jobId) {
		String sql = "SELECT MIN(d.Date) FROM date d" + " INNER JOIN work_day wd ON wd.DateId = d.Id"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { jobId }, LocalDate.class);
	}

	public LocalDate getEndLocalDate(int jobId) {
		String sql = "SELECT MAX(d.Date) FROM date d" + " INNER JOIN work_day wd ON wd.DateId = d.Id"
				+ " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { jobId }, LocalDate.class);
	}

	public Time getStartTime(int jobId) {
		String sql = "SELECT MIN(StartTime)" + " FROM work_day" + " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { jobId }, Time.class);
	}

	public Time getEndTime(int jobId) {
		String sql = "SELECT MAX(EndTime)" + " FROM work_day" + " WHERE JobId = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { jobId }, Time.class);
	}

	public void updateJobStatus(int status, int jobId) {

		String sql = "UPDATE job set Status = ? WHERE JobId = ?";
		jdbcTemplate.update(sql, new Object[] { status, jobId });

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

		if (filter.getCity() != null) {
			columnNames.add("City");
			args.add(filter.getCity());
		}

		if (filter.getState() != null) {
			columnNames.add("State");
			args.add(filter.getState());
		}

		if (filter.getZipCode() != null) {
			columnNames.add("ZipCode");
			args.add(filter.getZipCode());
		}

		if (filter.getStartDate() != null) {
			columnNames.add("StartDate");
			columnNames.add("IsBeforeStartDate");
			args.add(filter.getStartDate());
			args.add(filter.getBeforeStartDate());
		}

		if (filter.getEndDate() != null) {
			columnNames.add("EndDate");
			columnNames.add("IsBeforeEndDate");
			args.add(filter.getEndDate());
			args.add(filter.getBeforeEndDate());
		}

		if (filter.getStartTime() != null) {
			columnNames.add("StartTime");
			columnNames.add("IsBeforeStartTime");
			args.add(filter.getStartTime());
			args.add(filter.getBeforeStartTime());
		}

		if (filter.getEndTime() != null) {
			columnNames.add("EndTime");
			columnNames.add("IsBeforeEndTime");
			args.add(filter.getEndTime());
			args.add(filter.getBeforeEndTime());
		}

		if (filter.getDuration() != null) {
			columnNames.add("Duration");
			columnNames.add("IsShorterThanDuration");
			args.add(filter.getDuration());
			args.add(filter.getIsShorterThanDuration());
		}

		boolean isFirst = true;
		for (String columnName : columnNames) {
			if (isFirst) {
				columns = " (" + columnName;
				values = " (?";
				isFirst = false;
			} else {
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
		return this.FindJobFilterDtoMapper(sql, new Object[] { userId });
	}

	public FindJobFilterDTO getSavedFindJobFilter(int savedFindJobFilterId) {
		String sql = "SELECT * FROM saved_find_job_filter WHERE Id = ?";
		try {
			return this.FindJobFilterDtoMapper(sql, new Object[] { savedFindJobFilterId }).get(0);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Job> getJobs_byIds(List<Integer> jobIds) {

		String sql = "SELECT * FROM job j WHERE";
		List<Object> args = new ArrayList<Object>();

		boolean isFirst = true;
		for (Integer jobId : jobIds) {

			if (!isFirst)
				sql += " OR";
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
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, Integer.class);
	}

	public List<Job> getJobs_ByEmployer(int userId) {

		String sql = "SELECT * FROM job WHERE UserId = ?";
		return JobRowMapper(sql, new Object[] { userId });
	}


	public void addSkill(Integer jobId, Skill skill) {

		String sql = "INSERT INTO skill (Text, Type, JobId) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { skill.getText(), skill.getType(), jobId });

	}

	public List<Skill> getSkills_ByType(int jobId, int type) {
		String sql = "SELECT * FROM skill WHERE JobId = ? AND Type = ?";
		return SkillRowMapper(sql, new Object[] { jobId, type });
	}

	public int getCount_JobsCompleted_ByUser(int userId) {

		String sql = "SELECT COUNT(*) FROM job j" + " INNER JOIN application a ON a.JobId = j.JobId"
				+ " WHERE j.Status = ?" + " AND a.UserId = ?" + " AND a.Status = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { Job.STATUS_PAST, userId, Application.STATUS_ACCEPTED },
				Integer.class);
	}

	public Integer getCount_unavailableDays_ByUserAndWorkDays(int userId, List<String> workDays) {

		// Main query
		String sql = "SELECT COUNT(wd.WorkDayId) FROM work_day wd"
				+ " WHERE wd.WorkDayId IN ("
				+ " SELECT DISTINCT wd.WorkDayId FROM work_day wd"
				+ "	JOIN employment e ON wd.JobId = e.JobId"
				+ " JOIN application a ON e.JobId = a.JobId AND e.UserId = a.UserId"
				+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " JOIN date d ON wd.DateId = d.Id"
				+ " WHERE e.UserId = ?"
				+ " AND e.WasTerminated = 0" 
				+ " AND wp.IsCurrentProposal = 1";

		List<Object> args = new ArrayList<Object>();
		args.add(userId);

		boolean isFirst = true;
		sql += " AND (";
		for (String workDay : workDays) {

			if (!isFirst)
				sql += " OR ";
			sql += " d.Id = ?";
			isFirst = false;

			args.add(jobService.getDateId(workDay));

		}

		// Close the AND and sub query
		sql += ") )";

		return jdbcTemplate.queryForObject(sql, args.toArray(), Integer.class);
	}

	public String getDateId(int dateId) {
		String sql = "SELECT Date From Date WHERE Id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { dateId }, String.class);
	}



	public Job getConflictingEmployment_byUserAndWorkDay(int jobId_reference, int userId, int DateId) {
		String sql = "SELECT * FROM job j" + " JOIN employment e ON j.JobId = e.JobId"
				+ " JOIN application a ON e.JobId = a.JobId AND e.UserId = a.UserId"
				+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId" + " WHERE e.UserId = ?"
				+ " AND e.WasTerminated = 0" + " AND wp.IsCurrentProposal = 1" + " AND wd.DateId= ?"
				+ " AND j.Status != ?"
				+ " AND j.JobId != ?";

		List<Job> jobs = JobRowMapper(sql, new Object[] { userId, DateId, Job.STATUS_PAST, jobId_reference});
		if (verificationService.isListPopulated(jobs))
			return jobs.get(0);
		else
			return null;
	}




//	public void replaceEmployee(int jobId, int userId) {
//		
//		String sql = "UPDATE employment SET WasTerminated = 1 WHERE JobId = ? AND UserId = ?";
//		jdbcTemplate.update(sql, new Object[]{ jobId, userId });
//		
//	}







	public void updateJobFlag(int jobId, String flag, int value) {
		String sql = "UPDATE job SET " + flag + " = ? WHERE JobId = ?";
		jdbcTemplate.update(sql, new Object[] { value, jobId });
	}



	public void updateEmploymentFlag(int jobId, int userId, String flag, int value) {
		String sql = "UPDATE employment SET " + flag + " = ? WHERE jobId = ? AND UserId = ?";
		jdbcTemplate.update(sql, new Object[]{ value, jobId, userId });		
	}

	public List<Job> getJobs_terminatedFrom_byUser(int userId) {
		String sql = "SELECT * FROM job j"
				+ " JOIN employment e ON j.JobId = e.JobId"
				+ " WHERE j.Status != ?"
				+ " AND e.UserId = ?"
				+ " AND e.Flag_EmployerTerminatedEmployee = 1"
				+ " AND e.Flag_EmployeeAcknowledgedEmployerRemoval = 0";
		return JobRowMapper(sql, new Object[]{ Job.STATUS_PAST, userId });
	}

	public List<Job> getJobs_completedByUser(int userId) {
		String sql = "SELECT * FROM job j"
				+ " JOIN application a ON j.JobId = a.JobId"
				+ " JOIN employment e ON a.UserId = e.UserId AND a.JobId = e.JobId"
//				+ " WHERE e.UserId = ?"
//				+ " AND e.WasTerminated = 0"
				+ " WHERE j.JobId NOT IN ("
				+ " SELECT DISTINCT(j.JobId) FROM job j"
				+ " JOIN work_day wd ON j.JobId = wd.JobId"
				+ " JOIN application a ON j.JobId = a.JobId"
				+ " JOIN employment e ON a.UserId = e.UserId AND a.JobId = e.JobId"
				+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " WHERE wd.IsComplete = 0"
				+ " AND wp.IsCurrentProposal = 1"
				+ " AND e.UserId = ?"
				+ " AND e.WasTerminated = 0"
				+ " )"
				+ " AND e.UserId = ?";
		return JobRowMapper(sql, new Object[]{ userId, userId });
	}

	public List<Job> getJobs_needRating_byEmployer(int userId) {
		
		String sql = "SELECT * FROM job j"
				+ " WHERE j.JobId IN ("
				+ " SELECT DISTINCT j.JobId FROM job j"
				+ "	JOIN rating r ON j.JobId = r.JobId AND r.RatedByUserId = j.UserId"
				+ " WHERE r.Value IS NULL"
				+ "	AND j.UserId = ?"
				+ " )"
				+ " AND j.JobId NOT IN ("
				
				// Jobs that allow partial availability
				+ " SELECT DISTINCT(j.JobId) FROM job j"
				+ " JOIN application a ON j.JobId = a.JobId"
				+ " JOIN employment e ON a.UserId = e.UserId AND a.JobId = e.JobId"
				+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
				+ " WHERE wd.IsComplete = 0"
				+ " AND wp.IsCurrentProposal = 1"
//				+ " AND j.IsPartialAvailabilityAllowed = 1"
				+ " AND j.UserId = ?"
				+ " AND e.WasTerminated = 0"
				
//				+ " UNION"
//				
//				// Jobs that DO NOT allow partial availability
//				+ " SELECT DISTINCT(j.JobId) FROM job j"
//				+ " JOIN application a ON j.JobId = a.JobId"
//				+ " JOIN employment e ON a.UserId = e.UserId AND a.JobId = e.JobId"
//				+ " JOIN work_day wd ON j.JobId = wd.JobId"				
//				+ " WHERE wd.IsComplete = 0"
//				+ " AND j.IsPartialAvailabilityAllowed = 0"
//				+ " AND j.UserId = ?"
//				+ " AND e.WasTerminated = 0"				
				+ " )";
		
		return JobRowMapper(sql, new Object[]{ userId, userId });
	}

	public List<Job> getJobs_employment_byUserAndDate(int userId, String dateString) {
		String sql = "SELECT * FROM job j"
				+ " INNER JOIN employment e ON j.JobId = e.JobId"
				+ " INNER JOIN application a ON e.JobId = a.jobId AND e.UserId = a.UserId"
				+ " INNER JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " INNER JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " INNER JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
				+ " INNER JOIN date d ON wd.DateId = d.Id"
				+ " WHERE e.UserId = ?"
				+ "	AND d.Date = ?"
				+ " AND wp.IsCurrentProposal = 1"
				+ " AND wd.IsComplete = 0";
		
		return JobRowMapper(sql, new Object[]{ userId, dateString });
	}


}
