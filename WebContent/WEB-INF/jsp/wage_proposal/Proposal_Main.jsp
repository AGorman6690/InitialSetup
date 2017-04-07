<%@ include file="../includes/TagLibs.jsp"%>

<div class="application-proposal-container ${applicationDto.employmentProposalDto.isProposedToSessionUser ? 'action-required' : '' }">
	<div class="expiration-time-cont"> 
		<c:if test="${applicationDto.previousProposal.isCanceledDueToApplicantAcceptingOtherEmployment == 1 }">
			<p>${user.profileId == 1 ? 'You' : 'Applicant'} accepted other employment: time conflicts resolved</p>
		</c:if>	
		<c:if test="${applicationDto.previousProposal.isCanceledDueToEmployerFillingAllPositions == 1 }">
			<p>${user.profileId == 2 ? 'You' : 'Employer'} filled all positions on select days: time conflicts resolved</p>
		</c:if>			
		<c:choose>
			<c:when test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">	

				<c:if test="${user.profileId == 1 }">						
					<p>
						<span>Employer's offer expires in:</span>								
						<span class="expiration-time">${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</span>
					</p>
				</c:if>									
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${user.profileId == 2 }">
					
						<p>
							<span>Your offer expires in:</span>								
							<span class="expiration-time">${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</span>
						</p>
					</c:when>
					<c:otherwise>
						<p>Waiting for employer</p>
					</c:otherwise>
				</c:choose>				
			</c:otherwise>
		</c:choose>		
	</div>
	<div class="proposal-item amount">		
		<p><%@ include file="../wage_proposal/History_WageProposals.jsp" %></p>
	</div>	
	<div class="proposal-item work-days">
		<c:choose>
			<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
				<p>${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days
					<span class="glyphicon glyphicon-menu-down"></span>
				</p>
			</c:when>
			<c:otherwise>
				<p>${jobDto.workDays.size() } days<span class="glyphicon glyphicon-menu-down"></span></p>
			</c:otherwise>
		</c:choose>		
		<div class="mod">
			<div class="mod-content">
				<div class="mod-body">
					<div class="job-info-calendar calendar-container wage-proposal-calendar hide-prev-next read-only">
						<div class="calendar"
							data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
							data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
						</div>										
					</div>
				</div>
			</div>	
		</div>		

	</div>
	<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">	
		<div class="proposal-item respond">
			<button class="sqr-btn gray-2 show-mod">Respond</button>	
			<div class="present-proposal"></div>
		</div>
	</c:if>	
</div>
