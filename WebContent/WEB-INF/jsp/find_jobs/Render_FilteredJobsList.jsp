<%@ include file="../includes/TagLibs.jsp"%>


<c:choose>
	<c:when test="${jobDtos.size() > 0 }">
		<c:forEach items="${jobDtos }" var="jobDto">
		 	<div id="${jobDto.job.id }" class="job" data-job-id="${jobDto.job.id }" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }">
				<span class="glyphicon glyphicon-move"></span>
				<p class="job-name accent show-job-info-mod" data-context="find" data-p="1" data-job-id="${jobDto.job.id }">${jobDto.job.jobName }</p>
		<%-- 		<p class="job-name"><a href="/JobSearch/job/${jobDto.job.id}?c=find&p=1" class="accent">${jobDto.job.jobName }</a></p> --%>
				<p class="employer-rating">
					<span>${jobDto.employerDto.user.firstName } ${jobDto.employerDto.user.lastName }</span>
					<input name="input-1" class="rating-loading"
							value="${jobDto.employerDto.ratingValue_overall }	"></p>
				
				<p class="job-description less-description">${jobDto.job.description }</p>
				<div class="start-date-time">
					<p class="show-cal-mod">
						${jobDto.job.stringStartDate} - ${jobDto.job.stringEndDate} (${jobDto.workDays.size() } days)
		<!-- 				<span class="glyphicon glyphicon-calendar"></span></p> -->
					<div class="mod simple-header">
						<div class="mod-content">
							<div class="mod-header">
							</div>
							<div class="mod-body">
								<div class="v2 calendar-container teal-title proposal-calendar hide-unused-rows hide-prev-next read-only">
									<div class="calendar"
										data-min-date="${jobDto.date_firstWorkDay }"
										data-number-of-months=${jobDto.months_workDaysSpan }>
									</div>										
								</div>
							</div>
						</div>	
					</div>					
				</div>			
				<p>${jobDto.job.city_formatted }, ${jobDto.job.state } (${jobDto.distanceFromFilterLocation } miles)</p>		
			</div> 
		</c:forEach>
	</c:when>
	<c:otherwise>
		RETURN_NO_MORE_JOBS
	</c:otherwise>
</c:choose>

