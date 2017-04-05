<%@ include file="../includes/TagLibs.jsp"%>

<div class="application-proposal-container ${applicationDto.employmentProposalDto.isProposedToSessionUser ? 'alert' : '' }">
	<div class="proposal-item amount">		
		<p><%@ include file="../wage_proposal/History_WageProposals.jsp" %></p>
	</div>
	
	<div class="proposal-item work-days">
		<c:choose>
			<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
				<p>${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days</p>
			</c:when>
			<c:otherwise>
				<p>${jobDto.workDays.size() } days</p>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="proposal-item status">
		<c:choose>
			<c:when test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">	
				<c:if test="${user.profileId == 1 }">		
					<p>Offer expires in:</p>								
					<p>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
				</c:if>
<!-- 				<button class="sqr-btn gray-2 show-mod">Respond</button>	 -->
<!-- 				<div class="present-proposal"></div> -->
<%-- 				<%@ include file="../wage_proposal/WageProposal.jsp" %>										 --%>
			</c:when>
			<c:otherwise>
<%-- 				<p>Waiting for ${user.profileId == 1 ? 'employer' : 'applicant'}</p> --%>
				<c:if test="${user.profileId == 2 }">
					<div>										
						<p>Your offer expires in ${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
					</div>
				</c:if>				
			</c:otherwise>
		</c:choose>	
	</div>
	<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">	
		<div class="proposal-item respond">
			<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">	
				<button class="sqr-btn gray-2 show-mod">Respond</button>	
				<div class="present-proposal"></div>
			</c:if>
		</div>
	</c:if>	
</div>
