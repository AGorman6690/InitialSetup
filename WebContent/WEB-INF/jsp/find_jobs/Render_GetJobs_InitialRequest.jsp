<%@ include file="../includes/TagLibs.jsp"%>


<div id="requestOrigin" data-lat="${filterDto.lat }" data-lng="${filterDto.lng }" data-max-dist="${maxDistance }"></div>
<c:choose>
	<c:when test="${jobDtos.size() == 0 }">
 		<p id="noJobs">Sorry, no jobs match your search.</p>	 	
	</c:when>
	<c:otherwise>
		<div id="sort-jobs" class="filter-sort-jobs-header">Sort</div>
		<%@ include file="../find_jobs/Render_FilteredJobsList.jsp"%>
		<div id="get-more-jobs">Get More Jobs</div>	
		<div id="no-more-jobs">No more jobs match your search criteria</div>	
	</c:otherwise>
</c:choose>
 
