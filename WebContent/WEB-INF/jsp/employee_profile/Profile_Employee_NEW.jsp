<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/SelectPageSection.jsp" %>
<%@ include file="../includes/resources/TableFilter.jsp" %>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/inputValidation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee_NEW_2.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/employment_summary_cal.css" />
		
	
	
<div class="container">
	
	<c:if test="${!empty jobs_terminated }">
		<div class="pad-top-2">
			<c:forEach items="${jobs_terminated }" var="job">
				<div class="alert-message width-500">
					<h4 class="h4">The employer has removed you from the following job:</h4>	
					<p class="job-name accent show-job-info-mod" data-job-id="${job.id }">job.jobName</p>			
					<a class="sqr-btn gray-2" href="/JobSearch/employer-removed-you-from-job/${job.id}/acknowledge">OK</a>				
				</div>	
			</c:forEach>
		</div>
	</c:if>
	<c:if test="${!empty applicationDtos_closedDueToAllPositionsFilled_unacknowledged }">
		<div class="pad-top-2">
			<c:forEach items="${applicationDtos_closedDueToAllPositionsFilled_unacknowledged }"
						 var="applicationDto">
				<div class="alert-message width-500">
					<h4 class="h4">Your following applications have been closed due to the employer filling all positions. Your application will remain in the employer's inbox:</h4>				
					<p>${applicationDto.jobDto.job.jobName } <a class="sqr-btn gray-2" href="/JobSearch/application-closed-due-to-all-positions-filleed/${applicationDto.application.applicationId}/acknowledge">OK</a></p>				
				</div>	
			</c:forEach>
		</div>
	</c:if>	
	
	<div id="activity" class="">	
		
		<div id="employment-summary-calendar">
			<h1>Upcoming Employment</h1>
			<%@ include file="./EmploymentSummaryCalendar.jsp" %>
		</div>
		<c:choose>
			<c:when test="${applicationDtos.size() > 0 }">	
						<h1>Your Activity</h1>
					<div id="job-list-sort">
						<div class="filter-item">
							<div class="filter-item-header">
								<input checked id="all-applications" type="checkbox" name="filter-item">
								<label for="all-applications">Applications
									<span class="total">${userDto.countApplications_open }</span></label>			
							</div>
							<div class=filter-sub-items>
								<div>
									<input checked id="waiting-on-you" type="checkbox" name="proposal-status">
									<label for="waiting-on-you">Waiting for you
										<span class="total">${userDto.countProposals_waitingOnYou}</span>
										<c:if test="${userDto.countProposals_waitingOnYou_new > 0}">
										<span class="total new">${userDto.countProposals_waitingOnYou_new} new</span>
										</c:if>
									</label>
								</div>
								<div>
									<input checked id="waiting-on-other" type="checkbox" name="proposal-status">
									<label for="waiting-on-other">Waiting for the employer
										<span class="total">${userDto.countProposals_waitingOnOther}</span></label>
								</div>
							</div>
						</div>
						<div class="filter-item">
							<div class="filter-item-header">
								<input checked id="all-employment" type="checkbox" name="filter-item">
								<label for="all-employment">Employment
									<span class="total">${userDto.countJobs_employment }</span></label>			
							</div>
						</div>			
					</div>	
				
				<div id="job-list">		
					<c:forEach items="${applicationDtos }" var="applicationDto">
						<c:if test="${applicationDto.application
										.flag_applicantAcknowledgedAllPositionsAreFilled == 0 }">
							<div class="application "
								data-application-status="${applicationDto.application.status }"
								data-application-id="${applicationDto.application.applicationId }"
								data-employment-proposal-amount="${applicationDto.employmentProposalDto.amount }"
								data-job-start-date="${applicationDto.jobDto.milliseconds_startDate }"
								data-job-end-date="${applicationDto.jobDto.milliseconds_endDate }"
								data-job-duration-days="${applicationDto.jobDto.workDays.size() }"
								data-job-distance="${applicationDto.jobDto.distance }"
								data-is-accepted="${applicationDto.application.isAccepted }"
								data-is-waiting-on-you="${applicationDto.employmentProposalDto.isProposedToSessionUser }"
								>
								<div class="job-header ">
									<div class="status-wrapper ${applicationDto.employmentProposalDto.isProposedToSessionUser
										 ? '' : '' }">
										<div class="messages">
											<c:forEach items="${applicationDto.messages }" var="message">
												<p>* ${message }</p>
											</c:forEach>
										</div>										 
										<p class="status">${applicationDto.application.isAccepted == 1
											 ? "Employment" : "Application" }</p>
										<p class="waiting-status">${applicationDto.currentProposalStatus }</p>
										<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser &&
													applicationDto.application.isAccepted == 0 }">
											<p class="exp-time">The employer's offer expires in
												 ${applicationDto.time_untilEmployerApprovalExpires }</p>
										</c:if>

									</div>								
									<p class="job-name accent show-job-info-mod"
										 data-job-id="${applicationDto.jobDto.job.id }">${applicationDto.jobDto.job.jobName }</p>
<%-- 									<p>${applicationDto.jobDto.job.stringStartDate } --%>
<%-- 										 - ${applicationDto.jobDto.job.stringEndDate } --%>
<%-- 										 (${applicationDto.jobDto.workDays.size() } ${applicationDto.jobDto.workDays.size() <= 1 ? 'day' : 'days' })</p> --%>
<%-- 									<p >${applicationDto.jobDto.job.city_formatted }, ${applicationDto.jobDto.job.state }</p> --%>
								</div>			
								<div class="proposal ${sessionScope.user.profileId == 1 &&
									applicationDto.employmentProposalDto.flag_employerAcceptedTheOffer == 1 ? 'confirm' : '' }"
									 data-application-id="${applicationDto.application.applicationId }">
									
									<c:set var="jobDto" value="${applicationDto.jobDto }" />
									<c:choose>
										<c:when test="${applicationDto.application.isAccepted == 1 }">
											<%@ include file="./EmploymentJobSummary.jsp" %>
										</c:when>
										<c:otherwise>
											<c:set var="jobDto" value="${applicationDto.jobDto }" />
											<%@ include file="../wage_proposal/CurrentProposal.jsp" %>
											<div class="render-present-proposal-mod"></div>										
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:if>
					</c:forEach>	
				</div>			
			</c:when>
			<c:otherwise>
				<div class="center">
					<p id="noApplications">You have no activity at this time.</p>	
					<a id="" href="/JobSearch/jobs/find" class="sqr-btn teal">Find Jobs</a>
				</div>
			</c:otherwise>		
		</c:choose>
	
	</div>
</div>

<%@ include file="../includes/resources/WageProposal.jsp" %>
<%@ include file="../includes/Footer.jsp"%>
<script src="<c:url value="/static/javascript/profile_employee/Profile_Employee.js" />"></script>
				