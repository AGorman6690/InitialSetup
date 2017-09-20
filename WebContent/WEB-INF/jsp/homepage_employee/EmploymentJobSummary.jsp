
<div class="employment-job-summary">
	<div>
		<label>Job Site</label>
		<div class="address summary-value">
			<p>${applicationProgressStatus.job.streetAddress_formatted }</p> 
			<p>${applicationProgressStatus.job.city_formatted }, ${applicationProgressStatus.job.state }</p>
			<p>${applicationProgressStatus.job.zipCode }</p>
		 </div>		
	</div>
	<div>
		<label>Wage</label>
		<div  class="summary-value">
			<p>$ ${applicationProgressStatus.currentProposal.amount } / hr</p>
		</div>
	</div>
	<div>
		<label>Schedule</label>
		<div  class="summary-value">
			<p>${applicationProgressStatus.currentProposal.proposedDates.size() }
		 		${applicationProgressStatus.currentProposal.proposedDates.size() == 1 ? 'day' : 'days' }</p>
		</div>
	</div>
</div>