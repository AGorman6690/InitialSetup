<%@ include file="../includes/TagLibs.jsp"%>

<table class="proposal-table">
	<thead>
		<tr>
			<th></th>
			<th>Wage</th>
			<c:if test="${jobDto.job.isPartialAvailabilityAllowed }">
				<th>Work Days</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:if test="${!empty applicationDto.previousProposal && applicationDto.application.isAccepted == 0 }">
			<tr>
				<td>
					<p class="">${applicationDto.previousProposal.proposedByUserId == user.userId ? 'Your last proposal' : 
					user.profileId == 1 ? "Employer's last proposal" : "Applicant's last proposal"}</p>
				</td>
				<td><span class="dollar-sign">$</span>
					<fmt:formatNumber type="number" minFractionDigits="2" 
					maxFractionDigits="2" value="${applicationDto.previousProposal.amount}"/> / hr
				</td>			
				<c:if test="${jobDto.job.isPartialAvailabilityAllowed }"> 	
					<td>
						${applicationDto.previousProposal.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days			
					</td>
				</c:if>
			</tr>	
		</c:if>
		<tr class="current-proposal ${applicationDto.employmentProposalDto.proposedToUserId == user.userId ? 'swarning-message' :'' }">
			<td>
				<p class="">${applicationDto.employmentProposalDto.proposedByUserId == user.userId ? 'Your current proposal' : 
					user.profileId == 1 ? "Employer's current proposal" : "Applicant's current proposal"}</p>
			
			</td>
			<td><span class="dollar-sign">$</span>
				<fmt:formatNumber type="number" minFractionDigits="2" 
				maxFractionDigits="2" value="${applicationDto.employmentProposalDto.amount}"/> / hr
			</td>			
			<c:if test="${jobDto.job.isPartialAvailabilityAllowed }"> 	
				<td>
					${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days			
				</td>
			</c:if>
		</tr>	
	</tbody>
</table>
<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser &&
				applicationDto.application.isAccepted == 0}">	
	<div class="proposal-item respond">
		<button class="sqr-btn gray-3 show-mod accept-current-proposal">Accept</button>
		<button class="sqr-btn gray-3 show-mod counter-current-proposal">Counter</button>	
		<button class="sqr-btn gray-3 show-mod decline-current-proposal">Decline</button>	
		<div class="render-present-proposal-mod"></div>
	</div>
</c:if>	

