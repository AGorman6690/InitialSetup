
<div class="employment-job-summary">
	<div>
		<label>Job Site</label>
		<div class="address">
			<p>${applicationProgressStatus.job.streetAddress_formatted }</p> 
			<p>${applicationProgressStatus.job.city_formatted }, ${applicationProgressStatus.job.state }</p>
			<p>${applicationProgressStatus.job.zipCode }</p>
		 </div>		
<%-- 		<span>${applicationProgressStatus.job.streetAddress_formatted } <span class="dot">.</span> --%>
<%-- 		 ${applicationProgressStatus.job.city_formatted}, --%>
<%-- 		  ${applicationProgressStatus.job.state } <span class="dot">.</span>${applicationProgressStatus.job.zipCode }</span> --%>
	</div>
	<div>
		<label>Wage</label>
		<span>$ ${applicationDto.employmentProposalDto.amount } / hr</span>
	</div>
	<div>
		<label>Schedule</label>
		<span>${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() }
		 ${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() == 1 ? 'day' : 'days' }
		</span>
	</div>
</div>