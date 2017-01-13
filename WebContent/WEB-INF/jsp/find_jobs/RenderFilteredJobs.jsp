<%@ include file="../includes/TagLibs.jsp"%>


<div id="requestOrigin" data-lat="${filterRequest.lat }" data-lng="${filterRequest.lng }" data-max-dist="${maxDistance }"></div>
 
 <c:choose>
 	<c:when test="${jobDtos.size() == 0 }">
	 	<div id="noJobs">Sorry, no jobs match your search.</div>	 	
 	</c:when>
 	<c:otherwise>
 		<c:forEach items="${jobDtos }" var="jobDto">
	 	<div id="${jobDto.job.id }" class="job" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }">
			<div class="job-header">
				<div>
					<span class="glyphicon glyphicon-move"></span>
				</div>
				<a href="/JobSearch/jobs/find/job/${jobDto.job.id}" class="job-name accent">${jobDto.job.jobName }</a>

				<div class="job-category-container">
					<c:forEach items="${jobDto.categories }" var="category" varStatus="status">
						<span class="job-category bold">${category.name }</span>
						${!status.last ? '<span class="spacer bold">.</span>' : ''}							
					</c:forEach>
				</div>
			</div>
			<div class="job-body">				
				<div class="job-description less-description">
					${jobDto.job.description }
					Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.
					Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.
				</div>
				<div class="show-more-less"><span class="plus-minus glyphicon glyphicon-plus"></span>
					<span class="show-more-less-text"> Show more</span>
				</div>
				<div class="job-dates-times">
					<div class="start-date-time">
						<span class="bold">Start</span> - ${jobDto.job.stringStartDate }, ${jobDto.job.stringStartTime }						 
					</div>
					<div class="end-date-time">
						<span class="bold">End</span> - ${jobDto.job.stringEndDate }, ${jobDto.job.stringEndTime }						 						 
					</div>
					<div class="duration">
						<span class="bold">Duration</span> - ${jobDto.durationDays } days
					</div>
				</div>	
				<div class="job-location">
					<span class="bold">Location</span> - ${jobDto.job.city }, ${jobDto.job.state }
				</div>
				<div class="job-distance">
				    <span class="bold">Distance</span> - ${jobDto.distanceFromFilterLocation } miles
				</div>				
			</div>		
			<div class="job-footer">			
			</div> 
		</div>
 		</c:forEach>
 	</c:otherwise>
 </c:choose>
 
