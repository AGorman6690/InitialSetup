<%@ include file="../includes/TagLibs.jsp"%>

<div class="proposal-item amount">		
	<p>
		<span class="dollar-sign">$</span>
		<fmt:formatNumber type="number" minFractionDigits="2" 
			maxFractionDigits="2" value="${p_proposal.amount}"/> / hr		
	</p>
</div>	
<c:if test="${jobDto.job.isPartialAvailabilityAllowed }"> 	
	<div class="proposal-item work-days">
		<c:choose>
			<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
				<p class="pointer">${p_proposal.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days
					<span class="glyphicon glyphicon-calendar"></span>
				</p>
			</c:when>
			<c:otherwise>
				<p>${jobDto.workDays.size() } days<span class="glyphicon glyphicon-calendar"></span></p>
			</c:otherwise>
		</c:choose>		
		<div class="mod simple-header">
			<div class="mod-content">
				<div class="mod-header"></div>
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
