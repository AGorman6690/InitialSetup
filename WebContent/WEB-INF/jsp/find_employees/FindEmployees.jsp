<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/InputValidation.jsp"%>
<%@ include file="../includes/resources/WageProposal.jsp" %>
<%@ include file="../includes/resources/StarRatings.jsp" %>


<script src="<c:url value="/static/javascript/find_employees/FindEmployees.js" />"></script>
<link href="/JobSearch/static/css/find_employees/find_employees.css" rel="stylesheet" />
<link href="/JobSearch/static/css/find_employees/results.css" rel="stylesheet" />


<%-- <c:if test="${!empty jobDtos_current}"> --%>
<!-- 	<div id="what-kind-of-job-container"> -->
<!-- 		<p>Find employees for a job...</p> -->
<!-- 		<div id="posted-jobs-container" class="dropdown-container" data-toggle-id="posted-jobs"> -->
<!-- 			<button class="sqr-btn teal">I have already posted</button> -->
<!-- 			<div id="posted-jobs" class="dropdown-style"> -->
<%-- 				<c:forEach items="${jobDtos_current }" var="jobDto"> --%>
<%-- 					<p data-posted-job-id="${jobDto.job.id }">${jobDto.job.jobName }</p> --%>
<%-- 				</c:forEach> --%>
<!-- 			</div> -->
<!-- 		</div>			 -->
<!-- 		<button id="job-i-might-post" class="sqr-btn teal">I am thinking about posting</button> -->
<!-- 	</div> -->
<%-- </c:if> --%>
	
<div id="distance-filter-wrapper">
	<div id="distance-filter">
		<input id="miles" type="text" placeholder="number of" value="25"/>
		<span>miles from</span>
		<input id="address" type="text" placeholder="city, state, zip" value="55119"/>
		<button id="find-employees" class="sqr-btn">Find Employees</button>
	</div>
</div>
<div id="filters">
	<div class="filter">
		
		<p data-toggle-id="rating-filter-value" class="name">Rating<span class="glyphicon glyphicon-menu-down"></span></p>		
		<div id="rating-filter-value" class="filter-value-container">
			<div>
				<input id="5-stars" type="radio" name="rating-filter-value" value="5"/>
				<label for="5-stars">5 stars</label>
			</div>
			<div>
				<input id="4-stars" type="radio" name="rating-filter-value" value="4"/>
				<label for="4-stars">4+ stars</label>
			</div>
			<div>
				<input id="3-stars" type="radio" name="rating-filter-value" value="3"/>
				<label for="3-stars">3+ stars</label>
			</div>
			<div>
				<input id="2-stars" type="radio" name="rating-filter-value" value="2"/>
				<label for="2-stars">2+ stars</label>
			</div>
			<div>
				<input id="1-stars" type="radio" name="rating-filter-value" value="1"/>
				<label for="1-stars">1+ stars</label>
			</div>			
		</div>
	</div>
	<div class="filter">
		
		<p data-toggle-id="jobs-completed-filter-value" class="name">Jobs Completed<span class="glyphicon glyphicon-menu-down"></span></p>		
		<div id="jobs-completed-filter-value" class="filter-value-container">
			<div>
				<input id="15-jobs" type="radio" name="jobs-completed-filter-value" value="15"/>
				<label for="15-jobs">15+ jobs</label>
			</div>
			<div>
				<input id="10-jobs" type="radio" name="jobs-completed-filter-value" value="10"/>
				<label for="10-jobs">10+ jobs</label>
			</div>
			<div>
				<input id="5-jobs" type="radio" name="jobs-completed-filter-value" value="5"/>
				<label for="5-jobs">5+ jobs</label>
			</div>			
		</div>
	</div>
	<div class="filter">		
		<p data-toggle-id="work-days-filter-value" class="name">Work Days<span class="glyphicon glyphicon-menu-down"></span></p>		
		<div id="work-days-filter-value" class="filter-value-container">
			<p class="apply-filter">Apply</p>		
			<p id="clear-work-day-filter" class="apply-filter">Clear</p>		
			<div class="calendar-container">
				<div class="calendar v2"></div>
			</div>						
		</div>
	</div>	
</div>
<div id="results">
	
</div>

<%@ include file="../includes/Footer.jsp"%>