package com.jobsearch.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.controller.BaseRepository;
import com.jobsearch.model.WorkDay;
import com.jobsearch.service.JobServiceImpl;

@Repository
public class WorkDayRepository extends BaseRepository {
	
	@Autowired
	JobServiceImpl jobService;
	
	private List<WorkDay> WorkDayMapper(String sql, Object[] args) {

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<WorkDay>() {

				@Override
				public WorkDay mapRow(ResultSet rs, int rownumber) throws SQLException {

					WorkDay e = new WorkDay();

					e.setWorkDayId(rs.getInt("WorkDayId"));
					e.setJobId(rs.getInt("JobId"));
					e.setStringStartTime(rs.getString("StartTime"));
					e.setStringEndTime(rs.getString("EndTime"));
					e.setDateId(rs.getInt("DateId"));
					e.setIsComplete(rs.getInt("IsComplete"));
	
					// I can't remember why I am swapping the slash and the dash...
					// I think it's because in the browser, js likes dates with a dash...
					// Any who.
					e.setStringDate(jobService.getDate(e.getDateId()).replace("-", "/"));			
					e.setDate(LocalDate.parse(e.getStringDate().replace("/", "-")));
					return e;
				}
			});

		} catch (Exception e) {
			return null;
		}

	}
	
	public void addWorkDay(int jobId, WorkDay workDay) {

		// *******************************************************
		// *******************************************************
		// If the date is in yyyy-mm-dd format, does it need to be
		// converted to a Sql Date object????
		// Review this.
		// *******************************************************
		// *******************************************************

		String sql = "INSERT INTO work_day (JobId, StartTime, EndTime, DateId, Timestamp_EndDateTime)"
						+ "  VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, new Object[]{ jobId,
												workDay.getStringStartTime(),
												workDay.getStringEndTime(),
												workDay.getDateId(),
												workDay.getTimestamp_endDate()});


	}
	

	public void updateWorkDay_isComplete(int workDayId, int value) {
		String sql = "UPDATE work_day SET IsComplete = ? WHERE WorkDayId = ?";
		jdbcTemplate.update(sql, new Object[] { value, workDayId });
	}

	
	public Integer getWorkDayId(int jobId, int dateId) {

		String sql = "SELECT WorkDayId FROM work_day WHERE JobId = ? and DateId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { jobId, dateId }, Integer.class);
	}
	
	public void deleteWorkDays(List<WorkDay> workDays) {
		String sql = "DELETE FROM work_day" + " WHERE (";

		ArrayList<Object> args = new ArrayList<Object>();
		boolean isFirst = true;
		for (WorkDay workDay : workDays) {

			if (!isFirst)
				sql += " OR";
			sql += " WorkDayId = ?";
			args.add(workDay.getWorkDayId());

			isFirst = false;
		}
		sql += " )";

		jdbcTemplate.update(sql, args.toArray());

	}

	public void updateWorkDay(int workDayId, String stringStartTime, String stringEndTime) {
		String sql = "UPDATE work_day SET StartTime = ?, EndTime = ? WHERE WorkDayId = ?";

		jdbcTemplate.update(sql, new Object[] { stringStartTime, stringEndTime, workDayId });

	}
	

	public List<String> getWorkDayDateStrings(int jobId) {
		String sql = "SELECT d.Date FROM date d" + " JOIN work_day wd ON d.Id = wd.DateID" + " WHERE wd.JobId = ?";

		return jdbcTemplate.queryForList(sql, new Object[] { jobId }, String.class);
	}

	public List<WorkDay> getWorkDays_incomplete(Integer jobId) {
		String sql = "SELECT * FROM work_day wd WHERE wd.WorkDayId IN ("
				+ " SELECT DISTINCT wd.WorkDayId FROM work_day wd"
				+ " WHERE wd.JobId = ?"
				+ " AND wd.IsComplete = 0"
				+ ")";
				
		return WorkDayMapper(sql, new Object[]{ jobId });
	}


	public List<WorkDay> getWorkDays_incomplete_byUser(int jobId, int userId) {

		String sql = "SELECT * FROM work_day wd WHERE wd.WorkDayId IN ("
				+ " SELECT DISTINCT wd.WorkDayId FROM work_day wd" + " JOIN employment e ON wd.JobId = e.JobId"
				+ " JOIN application a ON e.JobId = a.JobId AND e.UserId = a.UserId"
				+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " WHERE e.UserId = ?" + " AND e.JobId = ?" + " AND wp.IsCurrentProposal = 1"
				+ " AND e.WasTerminated = 0" + " AND wd.IsComplete = 0" + ")";

		return WorkDayMapper(sql, new Object[] { userId, jobId });
	}

	public List<WorkDay> getWorkDays_byJobAndDateStrings(int jobId, List<String> dateStrings) {
		String sql = "SELECT * FROM work_day wd" + " JOIN date d ON wd.DateId = d.Id" + " WHERE wd.JobId = ?"
				+ " AND (";

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(jobId);

		boolean isFirst = true;
		for (String dateString : dateStrings) {
			if (!isFirst)
				sql += " OR";
			sql += " d.Date = ?";
			args.add(dateString);
			isFirst = false;
		}
		sql += " )";
		return WorkDayMapper(sql, args.toArray());
	}
	
	public WorkDay getWorkDay(Integer jobId, String dateString) {
		String sql = "SELECT * FROM work_day wd"
				+ " INNER JOIN job j ON wd.JobId = j.JobId"
				+ " INNER JOIN date d ON wd.DateId = d.Id"
				+ " WHERE j.JobId = ?"
				+ " AND d.Date = ?";
		
		return WorkDayMapper(sql, new Object[]{ jobId, dateString }).get(0);
	}

	
	public List<WorkDay> getWorkDays(int jobId) {
		String sql = "SELECT * FROM work_day WHERE JobId = ? ORDER BY DateId ASC";
		return this.WorkDayMapper(sql, new Object[] { jobId });
	}
	
	public List<WorkDay> getWorkDays_byProposalId(Integer employmentProposalId) {
		String sql = "SELECT * FROM work_day wd"
				+ " JOIN employment_proposal_work_day ep ON wd.WorkDayId = ep.WorkDayId"
				+ " WHERE ep.EmploymentProposalId = ?";

		return WorkDayMapper(sql, new Object[] { employmentProposalId });
	}

}
