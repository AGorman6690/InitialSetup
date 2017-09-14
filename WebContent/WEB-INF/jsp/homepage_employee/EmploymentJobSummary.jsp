
<div class="employment-job-summary">
	<div>
		<label>Job Site</label>
		<div class="address">
			<p>${applicationProgressStatus.job.streetAddress_formatted }</p> 
			<p>${applicationProgressStatus.job.city_formatted }, ${applicationProgressStatus.job.state }</p>
			<p>${applicationProgressStatus.job.zipCode }</p>
		 </div>		
	</div>
	<div>
		<label>Wage</label>
		<span>$ ${applicationProgressStatus.currentProposal.amount } / hr</span>
	</div>
	<div>
		<label>Schedule</label>
		<span>${applicationProgressStatus.currentProposal.proposedDates.size() }
		 ${applicationProgressStatus.currentProposal.proposedDates.size() == 1 ? 'day' : 'days' }
		</span>
	</div>
</div>