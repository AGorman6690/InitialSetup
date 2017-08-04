
<%@ include file="../includes/TagLibs.jsp"%>	

<div id="application-details">	
	<c:forEach items="${response.calendarApplications }" var="calendarApplication">
		<div class="application" data-id="${calendarApplication.application.applicationId }"
				 data-job-name="${calendarApplication.jobDto.job.jobName }"
				 data-job-id="${calendarApplication.jobDto.job.id}"
				 data-job-status="${calendarApplication.jobDto.job.status}"
				 data-is-accepted="${calendarApplication.application.isAccepted }">
						
			<c:forEach items="${calendarApplication.dates }" var="date">
				<div data-date="${date}"></div>
			</c:forEach>		
		</div>
	</c:forEach>				
</div>
<div id="event-calendar-mod" class="mod simple-header container calendar-container employment-line hide-prev-next">
	<div class="mod-content">
		<div class="mod-header">
		</div>
		<div class="mod-body">
			<div class="calendar">
			</div>
		</div>
	</div>
</div>

