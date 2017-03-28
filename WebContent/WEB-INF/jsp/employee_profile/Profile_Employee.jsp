<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/DatePicker.jsp" %>
<%@ include file="../includes/resources/WageProposal.jsp" %>
<%-- <%@ include file="../includes/resources/JobInfoModal.jsp" %> --%>
<%-- <%@ include file="../includes/resources/JobInformation.jsp" %> --%>
<%@ include file="../includes/resources/SelectPageSection.jsp" %>
<%@ include file="../includes/resources/TableFilter.jsp" %>
<%@ include file="../includes/resources/Modal.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/job_info/calendar_work_day.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/inputValidation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee_NEW.css" />
		
<script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script>
<script src="<c:url value="/static/javascript/profile_employee/Profile_Employee.js" />"></script>		
		
<div class="select-page-section-container">
	<span data-page-section-id="applications_list_view" class="selected select-page-section">List</span>
	<span data-page-section-id="applications_calendar_view"  class="select-page-section ">Calendar</span>
	<span id="show_list_and_calendar" data-page-section-id="applications_list_calendar_view"
		class="select-page-section override-click-event">List/Calendar</span>
</div>
	
<div id="applicationDetails">	
	<c:forEach items="${applicationDtos }" var="applicationDto">
		<div class="application" data-id="${applicationDto.application.applicationId }"
				 data-job-name="${applicationDto.jobDto.job.jobName }"
				 data-job-id="${applicationDto.jobDto.job.id}"
				 data-job-status="${applicationDto.jobDto.job.status}">
						
			<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay">
				<div class="work-day" data-date="${workDay.stringDate }"></div>
			</c:forEach>		
		</div>
	</c:forEach>				
</div>

<div class="container">
	<div id="applications_list_view" class="page-section">
		<c:choose>
			<c:when test="${applicationDtos.size() > 0 }">		
				<table class="main-table-style">
					<%@ include file="./Applications_Employee.jsp" %>				
				</table>						
			</c:when>
			<c:otherwise>
				<p id="noApplications">You have no open applications at this time.</p>
			</c:otherwise>		
		</c:choose>
	</div>
	<div id="applications_calendar_view" class="page-section">
		<div class="calendar-container teal-title">
			<div class="calendar"></div>
		</div>
	</div>
	<div id="applications_list_calendar_view">
		<div class="calendar-container">
			<div class="calendar"></div>
		</div>	
	</div>
</div>