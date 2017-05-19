	<%@ include file="../includes/Header.jsp"%>
<%-- 	<%@ include file="../includes/resources/DatePicker.jsp" %> --%>
	<%@ include file="../includes/resources/TableFilter.jsp" %>
	<%@ include file="../includes/resources/WageProposal.jsp" %>
	<%@ include file="../includes/resources/StarRatings.jsp" %>
	<%@ include file="../includes/resources/SelectPageSection.jsp" %>

	<%@ include file="../includes/resources/Modal.jsp" %>
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/calendar_application_summary.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/view_job_employer.css" />
	
	
	<c:if test="${context != 'complete' }">
		<script src="<c:url value="/static/javascript/view_job_employer/Applicants.js" />"></script>
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/applicants.css" />
			
	</c:if>
	
	<c:if test="${context == 'complete' }">	
		<script src="<c:url value="/static/javascript/view_job_employer/Employee_Ratings.js" />"></script>
	</c:if>
	
	<div class="select-page-section-container">	
		<%@ include file="./ContentBar_EmployerViewJob.jsp" %>	
	</div>	

	<div id="job-calendar-application-summary" class="pad-top v2 hide-unused-rows hide-prev-next calendar-container">
		<c:if test="${!empty jobDto.employees_whoLeft }">
			<div id="employees-who-left" class="center width-500 alert-message">
				<h4 class="h4">The following ${jobDto.employees_whoLeft.size() == 1 ? 'employee has' : 'employees have' } left</h4>
				<c:forEach items="${jobDto.employees_whoLeft }" var="user">
					<p>${user.firstName } ${user.lastName }
						 <a href="/JobSearch/employee/${user.userId}/left/job/${jobDto.job.id}/acknowledge" class="sqr-btn gray-2">OK</a></p>
				</c:forEach>
			</div>
		</c:if>
		<c:if test="${jobDto.job.flag_isNotAcceptingApplications == 1 }">
			<div id="all-positions-are-filled">
				<p>All Positions Are Filled</p>
			</div>
		</c:if>		
		<div class="left-fill"></div>
		<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
		<div class="right-fill"></div>
		<div class="checkbox-container center">
			<label><input id="toggle-calendar-numbers" type="checkbox" name="toggle-calendar-numbers" >Toggle calendar numbers</label>
		</div>
	</div>		
	<input id="jobId" type="hidden" value="${jobDto.job.id }">
	<input id="data_pageInit" type="hidden" value="${data_pageInit }">
	<div class="pad-top-2">
		<c:if test="${data_pageInit != 'all-apps' && !empty data_pageInit }">
			<button id="showAllApplicants" class="sqr-btn teal">Show All Applicants</button>			
		</c:if>
		<c:if test="${context == 'waiting' }">
			<div id="applicantsContainer" class="page-section pad-top-2">
				<c:if test="${jobDto.job.flag_isNotAcceptingApplications == 0 }">
					<div id="applicants" class="">				
						<%@ include file="./Applicants.jsp" %>
					</div>
				</c:if>
			</div>	
		</c:if>	
		<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
			<div id="employeesContainer" class="page-section">				
				<div id="employees" class="">
					<%@ include file="./Employees.jsp" %>					
				</div>			
			</div>	
		</c:if>
		<div id="jobInfoContainer" class="page-section">
			<%@include file="../JobInfo_NEW.jsp"%>
		</div>	
		
		<div id="modal_applicants" class="mod">
			<div class="mod-content"></div>
		</div>	
	</div>	





<%@ include file="../includes/Footer.jsp"%>
<%@ include file="../includes/resources/JobInformation.jsp" %>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>
	<script src="/JobSearch/static/javascript/view_job_employer/View_Job_Employer.js" type="text/javascript" ></script>

<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
</script>