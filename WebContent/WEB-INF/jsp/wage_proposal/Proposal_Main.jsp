<%@ include file="../includes/TagLibs.jsp"%>

<div class="application-proposal-container">
	<div class="proposal-item amount">		
		<%@ include file="../wage_proposal/History_WageProposals.jsp" %>
	</div>
	
	<div class="proposal-item work-days">
		<c:choose>
			<c:when test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed }">
				<p>${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } of ${applicationDto.jobDto.workDays.size() } days</p>
			</c:when>
			<c:otherwise>
				<p>${applicationDto.jobDto.workDays.size() } days</p>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="proposal-item status">
		<c:choose>
			<c:when test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">	
				<c:if test="${user.profileId == 1 }">										
					<p>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
				</c:if>
				<button class="sqr-btn gray-2 show-mod">Respond</button>	
				<%@ include file="../wage_proposal/WageProposal.jsp" %>										
			</c:when>
			<c:otherwise>
				<p>Waiting for ${user.profileId == 1 ? 'employer' : 'applicant'}</p>
				<c:if test="${user.profileId == 2 }">
					<div>										
						<p>Your offer expires in ${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
					</div>
				</c:if>				
			</c:otherwise>
		</c:choose>	
	</div>
</div>
