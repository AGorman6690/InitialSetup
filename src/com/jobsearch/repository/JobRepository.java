package com.jobsearch.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.controller.BaseRepository;
import com.jobsearch.model.Application;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.model.Skill;
import com.jobsearch.request.AddJobRequest;
import com.jobsearch.request.FindJobsRequest;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.QuestionServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.NumberUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Repository
public class JobRepository extends BaseRepository {

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




	public void addJob(AddJobRequest request, JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			cStmt.setString(1, request.getJob().getJobName());
			cStmt.setInt(2, user.getUserId());
			cStmt.setString(3, request.getJob().getDescription());
			cStmt.setString(4, request.getJob().getStreetAddress());
			cStmt.setString(5, request.getJob().getCity());
			cStmt.setString(6, request.getJob().getState());
			cStmt.setString(7, request.getJob().getZipCode());
			cStmt.setFloat(8, request.getJob().getLat());
			cStmt.setFloat(9, request.getJob().getLng());
			cStmt.setInt(10, Job.STATUS_FUTURE);
			cStmt.setBoolean(11, request.getJob().getIsPartialAvailabilityAllowed());
			cStmt.setInt(12, 0); // eventually remove this. "Positions per day" has been removed
			cStmt.setString(13, request.getJob().getStreetAddress_formatted());
			cStmt.setString(14, request.getJob().getCity_formatted());
			cStmt.setString(15, request.getJob().getZipCode_formatted());

			ResultSet result = cStmt.executeQuery();

			// Set the newly created job
			Job createdJob = new Job();
			result.next();
			createdJob.setId(result.getInt("JobId"));

			// Add the questions
			for (Question question : request.getQuestions()) {
				question.setJobId(createdJob.getId());
				questionService.addQuestion(question);
			}

			// Add the work days
			workDayService.addWorkDays(createdJob.getId(), request.getWorkDays());

			// Add the skills
			jobService.addSkills(createdJob.getId(), request.getSkills());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Job> getJobs_byFindJobsRequest(FindJobsRequest request) {

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
				+ " WHERE j.Status <> ?"
				+ " AND j.Flag_IsNotAcceptingApplications = 0";

		argsList.add(request.getLat());
		argsList.add(request.getLng());
		argsList.add(request.getLat());
		argsList.add(Job.STATUS_PAST);

		String startNextSubQuery = " AND j.JobId IN (";
		int count_subQueries = 0;

		String sql_subQuery = null;
		if (verificationService.isListPopulated(request.getDates())) {
			// Need to build this
		}

		if (request.getStringStartDate() != null) {

			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " INNER JOIN date d ON d.Id = wd.DateId";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING MIN(d.Id)";

			if (request.getIsBeforeStartDate()){
				sql_subQuery += " <= ?";
			}
			else{
				sql_subQuery += " >= ?";
			}
			Integer dateId = jobService.getDateId(request.getStringStartDate());
			argsList.add(dateId);
			sql += sql_subQuery;
			count_subQueries += 1;
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// ************************************************
		// End date sub query
		// ************************************************
		if (request.getStringEndDate() != null) {

			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " INNER JOIN date d ON d.Id = wd.DateId";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING MAX(d.Id)";

			if (request.getIsBeforeEndDate())
				sql_subQuery += " <= ?";
			else
				sql_subQuery += " >= ?";

			Integer dateId = jobService.getDateId(request.getStringEndDate());
			argsList.add(dateId);
			sql += sql_subQuery;
			count_subQueries += 1;
			startNextSubQuery = " AND wd.JobId IN (";
		}

		if (request.getStringStartTime() != null) {

			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";

			if (request.getIsBeforeStartTime())
				sql_subQuery += " HAVING MAX(wd.StartTime) <= ?";
			else
				sql_subQuery += " HAVING MIN(wd.StartTime) >= ?";

			argsList.add(request.getStringStartTime());
			sql += sql_subQuery;
			count_subQueries += 1;
			startNextSubQuery = " AND wd.JobId IN (";
		}

		if (request.getStringEndTime() != null) {

			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT DISTINCT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";

			if (request.getIsBeforeEndTime())
				sql_subQuery += " HAVING MAX(wd.EndTime) <= ?";
			else
				sql_subQuery += " HAVING MIN(wd.EndTime) >= ?";

			argsList.add(request.getStringEndTime());
			sql += sql_subQuery;
			count_subQueries += 1;
			startNextSubQuery = " AND wd.JobId IN (";
		}

		if (request.getDuration() != null && NumberUtility.isPositiveNumber(request.getDuration())) {

			if (startNextSubQuery != "")
				sql_subQuery = startNextSubQuery;
			else
				sql_subQuery = "";

			sql_subQuery += " SELECT wd.JobId FROM work_day wd";
			sql_subQuery += " GROUP BY wd.JobId";
			sql_subQuery += " HAVING COUNT(wd.DateId)";

			if (request.getIsShorterThanDuration())
				sql_subQuery += " <= ?";
			else
				sql_subQuery += " >= ?";

			argsList.add(request.getDuration());
			sql += sql_subQuery;
			count_subQueries += 1;
			startNextSubQuery = " AND wd.JobId IN (";
		}

		// Close the sub queries before finishing the distance query
		int i = 0;
		while (i < count_subQueries) {
			sql += " )";
			i += 1;
		}


		sql += " HAVING distance < ?";
		argsList.add(request.getRadius());

		if(CollectionUtils.isNotEmpty(request.getAlreadyLoadedJobIds())){
			for (Integer id : request.getAlreadyLoadedJobIds()) {
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



	public List<Job> getJobs_openByEmployer(int userId) {
		
		// TODO: I don't like the Timestamp_EndDateTime in the work_day table. It de-normalizes the data.
		// I think a better solution is to create a time table with time strings in increments of 15 minutes.
		// Then this should query against a DateId and a TimeId.
		
		String sql = "SELECT * FROM job j WHERE j.JobId IN"
				+ " ("
				+ " SELECT DISTINCT j.JobId FROM job j"
				+ " JOIN work_day wd ON j.JobId = wd.JobId"
				+ " WHERE j.UserId = ?"
				+ " AND wd.Timestamp_EndDateTime > ?"
				+ ")";
				
		String now = DateUtility.getCurrentTimestamp();		
		return JobRowMapper(sql, new Object[]{ userId, now });
	}
}
