<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>	

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<script src="<c:url value="/static/javascript/ratings/RateEmployer.js" />"></script>

<div class="my-container">

	<div class="item">
		<h2>Job</h2>
		<p><a href="/JobSearch/job/${job.id }?p=2&c=complete">${job.jobName }</a></p>
	</div>
	<div class="item">
		<h2>Ratings</h2>	
		<c:forEach items="${employees }" var="employee">
			<p><a href="/JobSearch/user/${employee.userId}/profile">${employee.firstName } ${employee.lastName }</a></p>	
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
	
		</c:forEach>
	</div>
</div>