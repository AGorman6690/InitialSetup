<%@ include file="../includes/TagLibs.jsp"%>


<c:forEach items="${jobDtos }" var="jobDto">
 	<div id="${jobDto.job.id }" class="job" data-job-id="${jobDto.job.id }" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }">
		<div class="job-header">
			<div>
				<span class="glyphicon glyphicon-move"></span>
			</div>
			<a href="/JobSearch/job/${jobDto.job.id}?c=find&p=1" class="job-name accent">${jobDto.job.jobName }</a>

			<div class="job-category-container">
				<c:forEach items="${jobDto.categories }" var="category" varStatus="status">
				<span class="job-category bold">${category.name }</span>
				${!status.last ? '<span class="spacer bold">.</span>' : ''}							
				</c:forEach>
			</div>
		</div>
		<div class="job-body">	
<!-- 			<div class="job-location"> -->
<%-- 				${jobDto.job.city }, ${jobDto.job.state } (${jobDto.distanceFromFilterLocation } miles) --%>
<!-- 			</div>					 -->
			<div class="job-description less-description">
				${jobDto.job.description }
			</div>
<!-- 			<div class="show-more show-desc"> -->
<!-- 				<span class="glyphicon glyphicon-plus"></span><span>Show More</span> -->
<!-- 			</div> -->
<!-- 			<div class="show-less show-desc"> -->
<!-- 				<span class="glyphicon glyphicon-minus"></span><span>Show Less</span> -->
<!-- 			</div>				 -->
			<div class="job-dates-times body-details">
				<div class="start-date-time">
					<p class="show-cal-mod linky-hover">
						${jobDto.job.stringStartDate} - ${jobDto.job.stringEndDate} (${jobDto.durationDays } days)
						<span class="glyphicon glyphicon-calendar"></span>	
									 
					</p>
					<div class="times">
						<p>Start time: ${jobDto.job.stringStartTime }</p>
						<p>End time: ${jobDto.job.stringEndTime }</p>
					</div>
					<div class="mod simple-header">
						<div class="mod-content">
							<div class="mod-header">
								<span class="glyphicon glyphicon-remove"></span>
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
			</div>	
			<div class="job-location body-details-2">
				${jobDto.job.city }, ${jobDto.job.state } (${jobDto.distanceFromFilterLocation } miles)
			</div>
<!-- 			<div class="job-distance"> -->
<%-- 			    <span class="bold">Distance</span> - ${jobDto.distanceFromFilterLocation } miles --%>
<!-- 			</div>				 -->
		</div>		
		<div class="job-footer">			
		</div> 
	</div>
</c:forEach>


