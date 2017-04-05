<!-- 	Currently the "WageNegotiation.js" has to be loaded AFTER the "FilterTable.js". -->
<!-- 	If it is not, then the "html" click event in the FilterTable.js will take precedence -->
<!-- 	over the "send counter offer" click event in the WageNegotiation.js -->
<!-- 	This seems hackish. -->
<!-- 	Address the logic later -->
<%-- 	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script> --%>

	<%@ include file="../includes/Header.jsp"%>
	<%@ include file="../includes/resources/DatePicker.jsp" %>
	<%@ include file="../includes/resources/TableFilter.jsp" %>
	<%@ include file="../includes/resources/WageProposal.jsp" %>
	<%@ include file="../includes/resources/StarRatings.jsp" %>
	<%@ include file="../includes/resources/SelectPageSection.jsp" %>
	<%@ include file="../includes/resources/JobInformation.jsp" %>
	<%@ include file="../includes/resources/Modal.jsp" %>
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>
	<script src="/JobSearch/static/javascript/view_job_employer/View_Job_Employer.js" type="text/javascript" ></script>

	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/calendar_application_summary.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/view_job_employer.css" />
	
	<c:if test="${context != 'complete' }">
		<script src="<c:url value="/static/javascript/view_job_employer/Applicants.js" />"></script>
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/applicants.css" />
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/applicants_tileView.css" />
			
	</c:if>
	
	<c:if test="${context == 'complete' }">	

<!-- 		<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/ratings/star-rating.css" /> -->
<!-- 		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/ratings.css" /> -->
<%-- 		<script src="<c:url value="/static/External/ratings/star-rating.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/view_job_employer/Employee_Ratings.js" />"></script>
	</c:if>
	
	<div class="select-page-section-container">	
		<%@ include file="./ContentBar_EmployerViewJob.jsp" %>	
	</div>	
	<div class="container">	

		<input id="jobId" type="hidden" value="${jobDto.job.id }">
		<input id="data_pageInit" type="hidden" value="${data_pageInit }">
		
		<div id="work-day-dtos">
			<c:forEach items="${jobDto.workDayDtos }" var="workDayDto">
				<div class="work-day-dto" data-date="${workDayDto.workDay.stringDate }"
					 data-count-applicants="${workDayDto.count_applicants }"
					 data-count-positions-filled="${workDayDto.count_positionsFilled }"
					 data-count-total-positions="${workDayDto.count_totalPositions }">
				 </div>
			</c:forEach>
		</div>
		
		<c:if test="${data_pageInit != 'all-apps' && !empty data_pageInit }">
			<button id="showAllApplicants" class="sqr-btn teal">Show All Applicants</button>			
		</c:if>
		<c:if test="${context == 'waiting' }">
			<div id="applicantsContainer" class="page-section">
				<div id="applicants" class="">
					<%@ include file="./Applicants.jsp" %>
				</div>
			</div>	
			<div id="job-calendar-application-summary" class="v2 hide-unused-rows calendar-container page-section">
				<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
			</div>
		</c:if>	
		
		
		<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
		<div id="employeesContainer" class="page-section">				
			<div id="employees" class="">
			<c:choose>
				<c:when test="${context == 'complete' }">
<%-- 							<%@ include file="./Employee_Ratings.jsp" %> --%>
				</c:when>
				<c:otherwise>	
					<%@ include file="./Employees.jsp" %>					
				</c:otherwise>
			</c:choose>
			</div>			
		</div>	
		</c:if>
		
		<div id="jobInfoContainer" class="page-section">
			<%@include file="../templates/JobInformation.jsp"%>
		</div>	
		
		<div id="modal_applicants" class="mod">
			<div class="mod-content">
				
			</div>
		</div>			
	</div>	


<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
</script>



<%@ include file="../includes/Footer.jsp"%>