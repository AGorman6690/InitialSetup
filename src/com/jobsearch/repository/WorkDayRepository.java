package com.jobsearch.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.bases.BaseRepository;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.WorkDay;

@Repository
public class WorkDayRepository extends BaseRepository {
	
	@Autowired
	JobServiceImpl jobService;
	
	public List<WorkDay> WorkDayMapper(String sql, Object[] args) {

		try {

			return jdbcTemplate.query(sql, args, new RowMapper<WorkDay>() {

				@Override
				public WorkDay mapRow(ResultSet rs, int rownumber) throws SQLException {

					WorkDay e = new WorkDay();

					e.setWorkDayId(rs.getInt("WorkDayId"));
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
