	<%@ include file="../includes/Header.jsp"%>
	<%@ include file="../includes/resources/DatePicker.jsp" %>
	<%@ include file="../includes/resources/TableFilter.jsp" %>
	<%@ include file="../includes/resources/WageProposal.jsp" %>
	<%@ include file="../includes/resources/StarRatings.jsp" %>
	<%@ include file="../includes/resources/SelectPageSection.jsp" %>
	<%@ include file="../includes/resources/JobInformation.jsp" %>
	<%@ include file="../includes/resources/Modal.jsp" %>
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/calendar_application_summary.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/view_job_employer.css" />
	
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>
	<script src="/JobSearch/static/javascript/view_job_employer/View_Job_Employer.js" type="text/javascript" ></script>
	
	<c:if test="${context != 'complete' }">
		<script src="<c:url value="/static/javascript/view_job_employer/Applicants.js" />"></script>
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/applicants.css" />
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/applicants_tileView.css" />	
	</c:if>
	
	<c:if test="${context == 'complete' }">	
		<script src="<c:url value="/static/javascript/view_job_employer/Employee_Ratings.js" />"></script>
	</c:if>
	
	<div class="select-page-section-container">	
		<%@ include file="./ContentBar_EmployerViewJob.jsp" %>	
	</div>	
	<div id="job-calendar-application-summary" class="v2 hide-unused-rows hide-prev-next calendar-container">
		<div class="left-fill"></div>
		<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
		<div class="right-fill"></div>
	</div>		
	<input id="jobId" type="hidden" value="${jobDto.job.id }">
	<input id="data_pageInit" type="hidden" value="${data_pageInit }">
	<div class="pad-top-2">
		<c:if test="${data_pageInit != 'all-apps' && !empty data_pageInit }">
			<button id="showAllApplicants" class="sqr-btn teal">Show All Applicants</button>			
		</c:if>
		<c:if test="${context == 'waiting' }">
			<div id="applicantsContainer" class="page-section pad-top-2">
				<div id="applicants" class="">
					<%@ include file="./Applicants.jsp" %>
				</div>
			</div>	
		</c:if>	
		<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
			<div id="employeesContainer" class="page-section pad-top-2">				
				<div id="employees" class="">
					<%@ include file="./Employees.jsp" %>					
				</div>			
			</div>	
		</c:if>
		<div id="jobInfoContainer" class="page-section">
			<%@include file="../templates/JobInformation.jsp"%>
		</div>	
		
		<div id="modal_applicants" class="mod">
			<div class="mod-content"></div>
		</div>	
	</div>	

<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
</script>



<%@ include file="../includes/Footer.jsp"%>