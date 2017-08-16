<%@ include file="../includes/TagLibs.jsp"%>


<div id="requestOrigin" data-lat="${response.latitudeSearched }"
	 data-lng="${response.longitudeSearched }" data-max-dist="${response.maxDistance }"></div>
<c:choose>
	<c:when test="${response.jobDtos.size() == 0 }">
 		<p id="noJobs">Sorry, no jobs match your search.</p>	 	
	</c:when>
	<c:otherwise>
		<div id="sort-jobs" class="filter-sort-jobs-header">Sort</div>
		<div id="find-jobs-response">
			<%@ include file="../find_jobs/Render_FilteredJobsList.jsp"%>
		</div>
		<div id="get-more-jobs">Get More Jobs</div>	
		<div id="no-more-jobs">No more jobs match your search criteria</div>	
	</c:otherwise>
</c:choose>
 
