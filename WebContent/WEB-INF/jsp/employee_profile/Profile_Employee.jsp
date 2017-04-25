<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/DatePicker.jsp" %> --%>
<%@ include file="../includes/resources/WageProposal.jsp" %>
<%-- <%@ include file="../includes/resources/JobInfoModal.jsp" %> --%>

<%@ include file="../includes/resources/SelectPageSection.jsp" %>
<%@ include file="../includes/resources/TableFilter.jsp" %>
<%-- <%@ include file="../includes/resources/Modal.jsp" %> --%>



<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/calendar_application_summary.css" />




<!-- <link rel="stylesheet" type="text/css" href="/JobSearch/static/css/job_info/calendar_work_day.css" /> -->
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/inputValidation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee_NEW.css" />
		
<script src="<c:url value="/static/javascript/profile_employee/Profile_Employee.js" />"></script>		
		
<!-- <div class="select-page-section-container"> -->
<!-- 	<span data-page-section-id="applications_list_view" class="selected select-page-section">List</span> -->
<!-- 	<span data-page-section-id="applications_calendar_view"  class="select-page-section ">Calendar</span> -->
<!-- 	<span id="show_list_and_calendar" data-page-section-id="applications_list_calendar_view" -->
<!-- 		class="select-page-section override-click-event">List/Calendar</span> -->
<!-- </div> -->
	
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
	<div id="all-other-application-dates">
		<c:forEach items="${applicationDtos }" var="applicationDto">						
				<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay">
					<div class="work-day" data-date="${workDay.stringDate }"
						 data-application-id="${applicationDto.application.applicationId }"
						 data-job-name="${applicationDto.jobDto.job.jobName }"
					 	 data-job-id="${applicationDto.jobDto.job.id}"
						 data-job-status="${applicationDto.jobDto.job.status}"></div>
				</c:forEach>			
		</c:forEach>					
	</div>
</div>
<div id="employmentDetails">	
	<c:forEach items="${jobDtos_employment_currentAndFuture }" var="jobDto">
		<div class="employment"
				 data-job-name="${jobDto.job.jobName }"
				 data-job-id="${jobDto.job.id}"
				 data-job-status="${jobDto.job.status}">
						
			<c:forEach items="${jobDto.workDays }" var="workDay">
				<div class="work-day" data-date="${workDay.stringDate }"></div>
			</c:forEach>		
		</div>
		
	</c:forEach>
	<c:forEach items="${applicationDtos }" var="applicationDto">						
			<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay">
				<div class="work-day" data-date="${workDay.stringDate }"
					 data-job-name="${applicationDto.jobDto.job.jobName }"
				 	 data-job-id="${applicationDto.jobDto.job.id}"
					 data-job-status="${applicationDto.jobDto.job.status}"></div>
			</c:forEach>			
	</c:forEach>					
</div>
<div class="container center">
	<div id="applications_list_view" class="">
		<c:choose>
			<c:when test="${applicationDtos.size() > 0 }">	
				<%@ include file="./Applications_Employee.jsp" %>									
			</c:when>
			<c:otherwise>
				<p id="noApplications">You have no open applications at this time.</p>
	
				<a id="" href="/JobSearch/jobs/find" class="sqr-btn teal">Find Jobs</a>
			</c:otherwise>		
		</c:choose>
	</div>
</div>

<%@ include file="../includes/Footer.jsp"%>
<%-- <%@ include file="../includes/resources/JobInformation.jsp" %> --%>