<div class="proposal-item amount">		
	<p><%@ include file="../wage_proposal/History_WageProposals.jsp" %></p>
</div>	
<c:if test="${jobDto.job.isPartialAvailabilityAllowed }"> 	
	<div class="proposal-item work-days">
		<c:choose>
			<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
				<p class="pointer">${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days
					<span class="glyphicon glyphicon-menu-down"></span>
				</p>
			</c:when>
			<c:otherwise>
				<p>${jobDto.workDays.size() } days<span class="glyphicon glyphicon-menu-down"></span></p>
			</c:otherwise>
		</c:choose>		
		<div class="mod simple-header">
			<div class="mod-content">
				<div class="mod-header">
					<span class="glyphicon glyphicon-remove"></span>
				</div>
				<div class="mod-body">
					<div class="v2 calendar-container proposal-calendar hide-unused-rows hide-prev-next read-only">
						<div class="calendar"
							data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
							data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
						</div>										
					</div>
				</div>
			</div>	
		</div>		
	</div>
</c:if>
<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser &&
				applicationDto.application.isAccepted == 0}">	
	<div class="proposal-item respond">
		<button class="sqr-btn gray-2 show-mod">Respond</button>	
		<div class="present-proposal"></div>
	</div>
</c:if>	
<c:if test="${user.profileId == 1 &&
				applicationDto.application.isAccepted == 1}">	
	<div class="proposal-item respond">
		<a href="/JobSearch/employee/leave-job/${jobDto.job.id}/confirm" class="sqr-btn gray-2">Leave</a>	
	</div>
</c:if>	