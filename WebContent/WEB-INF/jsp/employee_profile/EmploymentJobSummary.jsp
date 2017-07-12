
<div class="employment-job-summary">
	<div>
		<label>Job Site</label>
		<span>${applicationDto.jobDto.job.streetAddress_formatted } <span class="dot">.</span>
		 ${applicationDto.jobDto.job.city_formatted},
		  ${applicationDto.jobDto.job.state } <span class="dot">.</span>${applicationDto.jobDto.job.zipCode }</span>
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