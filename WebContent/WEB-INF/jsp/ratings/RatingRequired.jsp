<%@ include file="../includes/TagLibs.jsp" %>
<c:if test="${sessionScope.jobs_needRating.size() > 0 }">
	<div id="rating-required" class="warning-message-2">
		<h1 class="lbl">Your Rating Is Required</h1>
		<div class="job-container">
			<c:forEach items="${jobs_needRating }" var="job">
				<p><a class="job accent"
					   href="/JobSearch/job/${job.id }/rate-${sessionScope.user.profileId == 1 ? 'employer' : 'employees' }">
						${job.jobName }</a></p>
			</c:forEach>
		</div>		
	</div>
</c:if>