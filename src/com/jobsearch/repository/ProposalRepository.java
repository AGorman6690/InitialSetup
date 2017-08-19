package com.jobsearch.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.controller.BaseRepository;
import com.jobsearch.model.Proposal;
import com.jobsearch.model.WorkDay;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.ProposalServiceImpl;
import com.jobsearch.utilities.DateUtility;

@Repository
public class ProposalRepository extends BaseRepository {
	
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	ProposalServiceImpl proposalService;
	
	public List<Proposal> ProposalMapper(String sql, Object[] args) {
	
		return jdbcTemplate.query(sql, args, new RowMapper<Proposal>() {
			@Override
			public Proposal mapRow(ResultSet rs, int rownumber) throws SQLException {

				Proposal e = new Proposal();
				
				e.setAmount(String.format("%.2f", rs.getFloat("Amount")));
				e.setApplicationId(rs.getInt("ApplicationId"));
				e.setProposalId(rs.getInt("WageProposalId"));
				e.setProposedByUserId(rs.getInt("ProposedByUserId"));
				e.setProposedToUserId(rs.getInt("ProposedToUserId"));
				e.setIsNew(rs.getInt("IsNew"));
				e.setIsDeclined(rs.getInt("IsDeclined"));
				
				e.setExpirationDate(DateUtility.getLocalDateTime(rs.getString("ExpirationDate")));
				e.setEmployerAcceptedDate(DateUtility.getLocalDateTime(rs.getString("EmployerAcceptedDate")));
				
				e.setFlag_isCanceledDueToApplicantAcceptingOtherEmployment(
						rs.getInt(Proposal.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT));			
				e.setFlag_isCanceledDueToEmployerFillingAllPositions(
						rs.getInt(Proposal.FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS));				
				e.setFlag_applicationWasReopened(rs.getInt(Proposal.FLAG_APPLICATION_WAS_REOPENED));
				e.setFlag_aProposedWorkDayWasRemoved(rs.getInt(Proposal.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED));
				e.setFlag_aProposedWorkDayTimeWasEdited(rs.getInt(Proposal.FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED));
				e.setFlag_employerInitiatedContact(rs.getInt(Proposal.FLAG_EMPLOYER_INITIATED_CONTACT));
				e.setFlag_employerAcceptedTheOffer(rs.getInt("Flag_EmployerAcceptedTheOffer"));

				e.setProposedDates(getProposedDateStrings(e.getProposalId()));
				
				return e;
			}
		});
	}
	
	public void deleteProposedWorkDays(List<WorkDay> workDays) {
		String sql = "DELETE FROM employment_proposal_work_day" + " WHERE (";

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
	
	
	public void deleteProposedWorkDays(List<WorkDay> workDays, int applicationId) {
		
//		http://stackoverflow.com/questions/5816840/delete-i-cant-specify-target-table
		
		String sql = "DELETE FROM employment_proposal_work_day WHERE Id IN ("
					+ " SELECT * FROM ("
					+ " SELECT DISTINCT ep.Id FROM employment_proposal_work_day ep"
					+ " JOIN wage_proposal wp ON ep.EmploymentProposalId = wp.WageProposalId"
					+ " WHERE wp.IsCurrentProposal = 1"
					+ " AND wp.ApplicationId = ?"
					+ " AND (";
		
		List<Object> args = new ArrayList<Object>();
		args.add(applicationId);
		
		boolean isFirst = true;
		for(WorkDay workDay : workDays){
			if(!isFirst) sql += " OR";
			sql += " ep.WorkDayId = ?";
			args.add(workDay.getWorkDayId());
			isFirst = false;
		}
		sql += ") ) as ep1 )";
		
		jdbcTemplate.update(sql, args.toArray());
		
	}
	
	public Proposal getProposal(int proposalId) {
		String sql = "SELECT * FROM wage_proposal wp WHERE wp.WageProposalId = ?"; 
		return ProposalMapper(sql, new Object[]{ proposalId }).get(0);
	}
	
	public void insertProposal(Proposal proposal) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insert_employment_proposal(?, ?, ?, ?, ?, ? , ?)}");

			cStmt.setInt(1, proposal.getApplicationId());
			cStmt.setInt(2, proposal.getProposedByUserId());
			cStmt.setInt(3, proposal.getProposedToUserId());
			cStmt.setFloat(4, Float.valueOf(proposal.getAmount()));
			cStmt.setInt(5, -9999); // remove this from the stored procedure
			
			if(proposal.getEmployerAcceptedDate() != null &&
					proposal.getExpirationDate() != null){
				
				cStmt.setTimestamp(6, Timestamp.valueOf(proposal.getEmployerAcceptedDate()));
				cStmt.setTimestamp(7, Timestamp.valueOf(proposal.getExpirationDate()));
			}else{
				cStmt.setTimestamp(6, null);
				cStmt.setTimestamp(7, null);
				
			}
			
			ResultSet result = cStmt.executeQuery();		
			result.next();
			proposal.setProposalId(result.getInt("WageProposalId"));

			proposalService.insertProsalWorkDays(proposal);		
			
		} catch (Exception e) {
			// TODO: handle exception
			int i = 0;
			
		}		
	}
	
	public void insertProsalWorkDay(int employmentProposalId, int workDayId) {
		
		String sql = "INSERT INTO employment_proposal_work_day (EmploymentProposalId, WorkDayId)"
						+ " VALUES (?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{ employmentProposalId, workDayId } );
		
	}
	
	public void updateProposalFlag(Integer employmentProposalId, String proposalFlag, int value) {
		String sql = "UPDATE wage_proposal wp"
						+ " SET " + proposalFlag + " = ?"
						+ " WHERE WageProposalId = ?";
		
		jdbcTemplate.update(sql, new Object[]{ value, employmentProposalId });
		
	}


	public List<Proposal> getCurrentProposals_byJob(Integer jobId) {
		String sql = "SELECT * FROM wage_proposal wp"
				+ " JOIN application a ON wp.ApplicationId = a.ApplicationId"
				+ " JOIN job j ON a.JobId = j.JobId"
				+ " WHERE wp.IsCurrentProposal = 1"
				+ " AND j.jobId = ?"
				+ " AND a.IsOpen = 1";
		return ProposalMapper(sql, new Object[]{ jobId });
	}


	public Proposal getCurrentProposal(Integer applicationId) {		
		String sql = "SELECT * FROM wage_proposal WHERE IsCurrentProposal = 1 AND ApplicationId = ?";
		List<Proposal> result = this.ProposalMapper(sql, new Object[] { applicationId });		
		if (verificationService.isListPopulated(result)) return result.get(0);
		else return null;
	}
	
	public Proposal getPreviousProposal(Integer referenceEmploymentProposalId, int applicationId) {
		String sql = "SELECT * FROM wage_proposal"
					+ " WHERE WageProposalId IN ("
					+ " SELECT MAX(WageProposalId) FROM wage_proposal"
					+ " WHERE WageProposalId != ?"
					+ " AND ApplicationId = ?"
					+ ")";
		
		List<Proposal> result = ProposalMapper(sql, new Object[]{ referenceEmploymentProposalId, applicationId });
		if(verificationService.isListPopulated(result)) return result.get(0);
		else return null;
	}
	
	public List<String> getProposedDates(int proposalId) {
		
		// **************************************************************************
		//Now that the user needs to specify the work days when applying to
		//jobs with partial availability, can this method be removed????
		// Review this.
		// **************************************************************************
		String sql = "Select d.Date FROM date d"
					+ " INNER JOIN work_day wd on wd.DateId = d.Id"
					+ " INNER JOIN employment_proposal_work_day ep_wd ON ep_wd.WorkDayId = wd.WorkDayId"
					+ " WHERE ep_wd.EmploymentProposalId = ?"
					+ " ORDER BY d.Id ASC";
		
		return jdbcTemplate.queryForList(sql, new Object[]{ proposalId }, String.class);
	}
	

	public void updateFlag(Integer employmentProposalId, String proposalFlag, int value) {
		String sql = "UPDATE wage_proposal wp"
						+ " SET " + proposalFlag + " = ?"
						+ " WHERE WageProposalId = ?";
		
		jdbcTemplate.update(sql, new Object[]{ value, employmentProposalId });
		
	}
	
	public List<String> getProposedDateStrings(int proposalId) {
		
		// **************************************************************************
		//Now that the user needs to specify the work days when applying to
		//jobs with partial availability, can this method be removed????
		// Review this.
		// **************************************************************************
		String sql = "Select d.Date FROM date d"
					+ " INNER JOIN work_day wd on wd.DateId = d.Id"
					+ " INNER JOIN employment_proposal_work_day ep_wd ON ep_wd.WorkDayId = wd.WorkDayId"
					+ " WHERE ep_wd.EmploymentProposalId = ?"
					+ " ORDER BY d.Id ASC";
		
		return jdbcTemplate.queryForList(sql, new Object[]{proposalId }, String.class);
	}
	
}
