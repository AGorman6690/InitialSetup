<%@ include file="../includes/TagLibs.jsp"%>


<div id="requestOrigin" data-lat="${filterRequest.lat }" data-lng="${filterRequest.lng }" data-max-dist="${maxDistance }"></div>
<c:choose>
	<c:when test="${jobDtos.size() == 0 }">
 	<div id="noJobs">Sorry, no jobs match your search.</div>	 	
	</c:when>
	<c:otherwise>
		<%@ include file="../find_jobs/Render_FilteredJobsList.jsp"%>
		<div id="getMoreJobsContainer" class="get-more-jobs">Get More Jobs</div>	
	</c:otherwise>
</c:choose>
 
