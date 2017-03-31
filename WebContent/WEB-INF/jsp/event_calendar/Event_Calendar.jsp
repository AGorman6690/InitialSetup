<%@ include file="../includes/Header.jsp"%>	
<%@ include file="../includes/resources/DatePicker.jsp"%>	
<%@ include file="../includes/resources/Modal.jsp"%>	

<script src="/JobSearch/static/javascript/event_calendar/event_calendar.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/event_calendar/event_calendar.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/event_calendar/employment_line.css" />	
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />

<div id="application-details">	
	<c:forEach items="${applicationDtos }" var="applicationDto">
		<div class="application" data-id="${applicationDto.application.applicationId }"
				 data-job-name="${applicationDto.jobDto.job.jobName }"
				 data-job-id="${applicationDto.jobDto.job.id}"
				 data-job-status="${applicationDto.jobDto.job.status}">
						
			<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay">
				<div data-date="${workDay.stringDate }"></div>
			</c:forEach>		
		</div>
	</c:forEach>				
</div>


<div id="employment-details">
	<c:forEach items="${jobDtos_employment }" var="jobDto">
		<div class="employment"
				 data-job-id="${jobDto.job.id}"
				 data-job-name="${jobDto.job.jobName}">
						
			<c:forEach items="${jobDto.workDays }" var="workDay">
				<div data-date="${workDay.stringDate }"></div>
			</c:forEach>		
		</div>
	</c:forEach>		
</div>

<div id="unavailability-details">
	<c:forEach items="${stringDates_unavailability }" var="stringDate">						
		<div data-date="${stringDate }"></div>
	</c:forEach>		
</div>

<div id="event-calendar-mod" class="mod container calendar-container employment-line">
	<div class="mod-content">
		<div class="mod-header">
		<span class="glyphicon glyphicon-remove"></span>
		<h2>Calendar</h2>
		</div>
		<div class="mod-body">
			<div class="calendar">
			</div>
		</div>
	</div>
</div>