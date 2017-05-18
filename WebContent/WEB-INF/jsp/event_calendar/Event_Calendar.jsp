
<%@ include file="../includes/TagLibs.jsp"%>	

<div id="application-details">	
	<c:forEach items="${applicationDtos }" var="applicationDto">
		<div class="application" data-id="${applicationDto.application.applicationId }"
				 data-job-name="${applicationDto.jobDto.job.jobName }"
				 data-job-id="${applicationDto.jobDto.job.id}"
				 data-job-status="${applicationDto.jobDto.job.status}"
				 data-is-accepted="${applicationDto.application.isAccepted }">
						
			<c:forEach items="${applicationDto.employmentProposalDto.dateStrings_proposedDates }" var="dateString">
					<div data-date="${dateString}"></div>
			</c:forEach>		
		</div>
	</c:forEach>				
</div>


<!-- <div id="employment-details"> -->
<%-- 	<c:forEach items="${applicationDtos_employed }" var="applicationDto"> --%>
<%-- 		<div class="employment" data-id="${applicationDto.application.applicationId }" --%>
<%-- 				 data-job-name="${applicationDto.jobDto.job.jobName }" --%>
<%-- 				 data-job-id="${applicationDto.jobDto.job.id}" --%>
<%-- 				 data-job-status="${applicationDto.jobDto.job.status}"> --%>
						
<%-- 			<c:forEach items="${applicationDto.employmentProposalDto.dateStrings_proposedDates }" var="dateString"> --%>
<%-- 					<div data-date="${dateString}"></div> --%>
<%-- 			</c:forEach>		 --%>
<!-- 		</div> -->
<%-- 	</c:forEach> --%>
<%-- 	<c:forEach items="${jobDtos_employment }" var="jobDto"> --%>
<!-- 		<div class="employment" -->
<%-- 				 data-job-id="${jobDto.job.id}" --%>
<%-- 				 data-job-name="${jobDto.job.jobName}"> --%>
						
<%-- 			<c:forEach items="${jobDto.workDayDtos }" var="workDay"> --%>
<%-- 				<c:if test="${workDay.isProposed }"> --%>
<%-- 					<div data-date="${workDay.workDay.stringDate }"></div> --%>
<%-- 				</c:if> --%>
<%-- 			</c:forEach>		 --%>
<!-- 		</div> -->
<%-- 	</c:forEach>		 --%>
<!-- </div> -->

<div id="unavailability-details">
	<c:forEach items="${stringDates_unavailability }" var="stringDate">						
		<div data-date="${stringDate }"></div>
	</c:forEach>		
</div>

<div id="event-calendar-mod" class="mod simple-header container calendar-container employment-line hide-prev-next">
	<div class="mod-content">
		<div class="mod-header">
		<span class="glyphicon glyphicon-remove"></span>
		<h2></h2>
		</div>
		<div class="mod-body">
			<div class="calendar">
			</div>
		</div>
	</div>
</div>

