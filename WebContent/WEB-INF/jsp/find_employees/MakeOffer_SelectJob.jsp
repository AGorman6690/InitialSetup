<%@ include file="../includes/TagLibs.jsp" %>
<div class="mod simple-header">
	<div class="mod-content">
		<div class="mod-header"></div>
		<div class="mod-body select-a-job" data-user-id="${user_makeOfferTo.userId }"
			data-first-name=>
			<c:choose>
				<c:when test="${empty openJobs }">
					<h4>You do not have any open jobs</h4>
					<a id="nav_postJob" class="sqr-btn" href="/JobSearch/post-job">Post Job</a>
				</c:when>
				<c:otherwise>
					<h4>Select the job you want to make an offer for</h4>
					<p id="unavailable-message">${user_makeOfferTo.firstName } <span></span></p>
					<c:forEach items="${openJobs }" var="job">
						<p data-job-id="${job.id }">${job.jobName }</p>
					</c:forEach>
				</c:otherwise>
			</c:choose>			
		</div> 

	</div>
</div>