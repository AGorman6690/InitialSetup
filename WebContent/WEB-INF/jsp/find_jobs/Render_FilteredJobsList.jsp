<%@ include file="../includes/TagLibs.jsp"%>


<c:choose>
	<c:when test="${response.jobDtos.size() > 0 }">
		<c:forEach items="${response.jobDtos }" var="jobDto">
		 	<div id="${jobDto.job.id }" class="job show-job-info-mod" data-job-id="${jobDto.job.id }"
		 		 data-context="find" data-p="1" data-job-id="${jobDto.job.id }" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }">
				<span class="glyphicon glyphicon-move"></span>
				<p class="job-name blue">${jobDto.job.jobName }</p>
		<%-- 		<p class="job-name"><a href="/JobSearch/job/${jobDto.job.id}?c=find&p=1" class="accent">${jobDto.job.jobName }</a></p> --%>
				<p class="employer-rating">
					<span>${jobDto.employerName }</span>
					<input name="input-1" class="rating-loading"
							value="${jobDto.employerOverallRating }"></p>
				
				<p class="job-description less-description">${jobDto.job.description }</p>
				<div class="start-date-time">
					<p class="show-cal-mod">
						${jobDto.job.stringStartDate} - ${jobDto.job.stringEndDate} (${jobDto.workDays.size() } days)
				</div>			
				<p>${jobDto.job.city_formatted }, ${jobDto.job.state } (${jobDto.distance } miles)</p>		
			</div> 
		</c:forEach>
	</c:when>
	<c:otherwise>
		RETURN_NO_MORE_JOBS
	</c:otherwise>
</c:choose>

