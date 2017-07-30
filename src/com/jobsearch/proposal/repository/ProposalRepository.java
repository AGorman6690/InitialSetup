package com.jobsearch.proposal.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.bases.BaseRepository;
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.Proposal;
import com.jobsearch.utilities.DateUtility;

@Repository
public class ProposalRepository extends BaseRepository {
	
	@Autowired
	ApplicationServiceImpl applicationService;

	
	public List<Proposal> ProposalMapper(String sql, Object[] args) {
	
		return jdbcTemplate.query(sql, args, new RowMapper<Proposal>() {
			@Override
			public Proposal mapRow(ResultSet rs, int rownumber) throws SQLException {

				Proposal e = new Proposal();
				
				e.setAmount(String.format("%.2f", rs.getFloat("Amount")));
				e.setApplicationId(rs.getInt("ApplicationId"));
				e.setEmploymentProposalId(rs.getInt("WageProposalId"));
				e.setProposedByUserId(rs.getInt("ProposedByUserId"));
				e.setProposedToUserId(rs.getInt("ProposedToUserId"));
				e.setIsNew(rs.getInt("IsNew"));
				
				e.setExpirationDate(DateUtility.getLocalDateTime(rs.getString("ExpirationDate")));
				e.setEmployerAcceptedDate(DateUtility.getLocalDateTime(rs.getString("EmployerAcceptedDate")));
				
				e.setFlag_isCanceledDueToApplicantAcceptingOtherEmployment(
						rs.getInt(EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_APPLICANT_ACCEPTING_OTHEREMPLOYMENT));			
				e.setFlag_isCanceledDueToEmployerFillingAllPositions(
						rs.getInt(EmploymentProposalDTO.FLAG_IS_CANCELED_DUE_TO_EMPLOYER_FILLING_ALL_POSITIONS));				
				e.setFlag_applicationWasReopened(rs.getInt(EmploymentProposalDTO.FLAG_APPLICATION_WAS_REOPENED));
				e.setFlag_aProposedWorkDayWasRemoved(rs.getInt(EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_WAS_REMOVED));
				e.setFlag_aProposedWorkDayTimeWasEdited(rs.getInt(EmploymentProposalDTO.FLAG_A_PROPOSED_WORK_DAY_TIME_WAS_EDITED));
				e.setFlag_employerInitiatedContact(rs.getInt(EmploymentProposalDTO.FLAG_EMPLOYER_INITIATED_CONTACT));
				e.setFlag_employerAcceptedTheOffer(rs.getInt("Flag_EmployerAcceptedTheOffer"));

				e.setProposedDates(getProposedDateStrings(e.getEmploymentProposalId()));
				
				return e;
			}
		});
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
	
	public List<String> getProposedDateStrings(int employmentProposalId) {
		
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
		
		return jdbcTemplate.queryForList(sql, new Object[]{ employmentProposalId }, String.class);
	}
	
}
