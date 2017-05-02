<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/DatePicker.jsp"%> --%>
<%-- <%@ include file="../includes/resources/Modal.jsp"%> --%>
<%@ include file="../includes/resources/InputValidation.jsp"%>
<%@ include file="../includes/resources/WageProposal.jsp" %>


<script src="/JobSearch/static/javascript/InputValidation.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/Utilities/FormUtilities.js" type="text/javascript"></script>
<link href="/JobSearch/static/css/Templates/forms.css" rel="stylesheet" />
<link href="/JobSearch/static/css/table.css" rel="stylesheet" />

<script src="<c:url value="/static/javascript/find_employees/FindEmployees.js" />"></script>
<link href="/JobSearch/static/css/find_employees/findEmployees.css" rel="stylesheet" />
<link href="/JobSearch/static/css/find_employees/make_offer_modal.css" rel="stylesheet" />
<!-- <link href="/JobSearch/static/css/find_employees/cal_find_employees.css" rel="stylesheet" /> -->


<c:if test="${!empty jobDtos_current}">
	<div id="what-kind-of-job-container">
		<p>Find employees for a job...</p>
		<div id="posted-jobs-container" class="dropdown-container" data-toggle-id="posted-jobs">
			<button class="sqr-btn teal">I have already posted</button>
			<div id="posted-jobs" class="dropdown-style">
				<c:forEach items="${jobDtos_current }" var="jobDto">
					<div data-posted-job-id="${jobDto.job.id }">${jobDto.job.jobName }</div>
				</c:forEach>
			</div>
		</div>			
		<button id="job-i-might-post" class="sqr-btn teal">I am thinking about posting</button>
	</div>
</c:if>

<input id="jobId_getOnPageLoad" type="hidden" value="${job.id }">
		
	<div id="filtersContainer" class="${!empty jobDtos_current ? 'hide-on-load' : ''}">
<%-- 		<c:if test="${!empty job }"> --%>
			<div id="job-info">
				<h2>Job</h2>
				<p><a href="/JobSearch/job/${job.id }?c=waiting&p=2&d=all-apps">${job.jobName }</a></p>
			</div>
<%-- 		</c:if> --%>
		<div id="locationFilterContainer" class="filter">
			<h3>Location</h3>
			<div id="location" class="filter-value">
				<input id="street" type="text" placeholder="Street">
				<input id="city" type="text" placeholder="City">
				<select id="state"></select>
				<input id="zipCode" type="text" placeholder="Zip Code" value="55119">
			</div>
		</div>
		<div id="availabilityFilterContainer" class="filter">
			<h3>Work Days</h3>
<!-- 			<div class="filter-value"> -->
<!-- 				<label><input id="partialAvailabilityAllowed" type="checkbox">Partial Availability Allowed</label> -->
<!-- 			</div> -->
			<div class="v2 select-work-days calendar-container filter-value work-day-filter">
				<button class="clear-calendar sqr-btn gray-2">Clear</button>
				<div id="availabilityCalendar" class="calendar">
				</div>
			</div>
		</div>
		
<!-- 		<div id="categoriesFilterContainer" class="filter"> -->
<!-- 			<h3>Categories</h3> -->
<!-- 			<div  class="filter-value"></div> -->
<!-- 		</div>	 -->
		
		<div><button id="findEmployees" class="sqr-btn green">Get Results</button></div>
	</div>
	<div id="resultsContainer">
		<h3>Results</h3>
		<div id="results">
		
		</div>
	</div>
	
<%-- <div id="json_work_day_dtos">${json_job_work_days }</div> --%>

<%@ include file="./MakeOfferModal.jsp"%>

<%@ include file="../includes/Footer.jsp"%>