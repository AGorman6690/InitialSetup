<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>	

<!-- <link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employees.css" /> -->
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<script src="<c:url value="/static/javascript/ratings/RateEmployer.js" />"></script>

<div class="my-container">
	
	<div id="submit-cont">
		<button id="submit" class="sqr-btn teal">Submit Ratings</button>
	</div>
	<div id="job" class="item pad-top">
		<h1>Job</h1>
		<p><a href="/JobSearch/job/${job.id }?p=2&c=complete">${job.jobName }</a></p>
	</div>
	<div class="item pad-top">
		
		<c:if test="${employees.size() > 1 }">
			<div id="employee-list">
				<h2>Employees</h2>	
				<div id="employees">
					<c:forEach items="${employees }" var="employee" varStatus="status">
						<p class="${status.first ? 'select-on-load' : '' }" data-user-id="${employee.userId }"
							data-name="${employee.firstName } ${employee.lastName }">
							
							${employee.firstName } ${employee.lastName }
						</p>		
					</c:forEach>		
				</div>	
			</div>
		</c:if>
		<div id="ratings-cont">
			<h2>Rate <span id="selected-employee">
				<c:if test="${employees.size() == 1 }">
					${employees[0].firstName} ${employees[0].lastName}
				</c:if> 
				</span>
			</h2>	
			<c:forEach items="${rateCriteria }" var="rateCriterion">
				<div class="my-rating-container">
					<p>${rateCriterion.name }</p>
					<input name="work-ethic" class="work-ethic rating-loading"
							value="0" data-size="xs">
				</div>	
			</c:forEach>
			<div class="my-rating-container comment-container">
				<p>Comment</p>
				<textarea id="comment" rows="4"></textarea>
			</div>
		</div>
	</div>
</div>