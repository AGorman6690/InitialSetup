<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/TableFilter.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>

<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/inputValidation.css" />
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/Templates/popup.css" />
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/profile_employee/profile_employee_NEW_2.css" />
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/profile_employee/employment_summary_cal.css" />
	
<script src="/JobSearch/static/javascript/Personal_Info.js" type="text/javascript"></script>




<div class="containeraaa">

	<div id="profile-content">
		<%@ include file="../credentials_employee/Credentials_Employee.jsp" %>			
		<%@ include file="../ratings/RatingsByUser.jsp" %>
	</div>

	<c:if test="${!empty messageResponses_jobsTerminatedFrom }">
		<div class="pad-top-2">
			<c:forEach items="${messageResponses_jobsTerminatedFrom }" var="messageResponse">
				<div class="alert-message width-500">
					<h4 class="h4">The employer has removed you from the following
						job:</h4>
					<p class="job-name accent show-job-info-mod" data-context="profile"
						data-job-id="${messageResponse.job.id }">messageResponse.job.jobName</p>
					<a class="sqr-btn gray-2"
						href="/JobSearch/employer-removed-you-from-job/${messageResponse.job.id}/acknowledge">OK</a>
				</div>
			</c:forEach>
		</div>
	</c:if>
	<c:if
		test="${!empty messageResponses_applicationsClosedDueToAllPositionsFilled }">
		<div class="pad-top-2">
			<c:forEach
				items="${messageResponses_applicationsClosedDueToAllPositionsFilled }"
				var="messageResponse">
				<div class="alert-message width-500">
					<h4 class="h4">Your following applications have been closed
						due to the employer filling all positions. Your application will
						remain in the employer's inbox:</h4>
					<p>${messageResponse.job.jobName }
						<a class="sqr-btn gray-2"
							href="/JobSearch/application-closed-due-to-all-positions-filleed/
								${messageResponse.application.applicationId}/acknowledge">OK</a>
					</p>
				</div>
			</c:forEach>
		</div>
	</c:if>

	
	<div id="other-stuff">
		<div id="employment-summary-calendar">
			<h1>Upcoming Employment</h1>
			<%@ include file="./EmploymentSummaryCalendar.jsp"%>
		</div>
		<h1>Your Activity</h1>
		<div id="activity" class="">
		<c:choose>
			<c:when test="${response.applicationProgressStatuses.size() > 0 }">				
				<div id="job-list-sort">
					<div class="filter-item">
						<div class=filter-item>
							<div>
								<input checked id="waiting-on-you" type="checkbox"
									name="proposal-status">
								<label for="waiting-on-you">Proposals waiting
									for you
									
			
									<span class="total">${response.countProposals_waitingOnYou}</span>						
								</label>
								<c:if test="${response.countProposals_waitingOnYou_new > 0}">
									<span class="total new">${response.countProposals_waitingOnYou_new}
										new</span>
								</c:if>
							</div>
							<div>
								<input checked id="waiting-on-other" type="checkbox"
									name="proposal-status"> <label for="waiting-on-other">Proposals waiting
									for the employer 
									<span class="total">${response.countProposals_waitingOnOther}</span>
								</label>
								
							</div>
						</div>
					</div>
					<div class="filter-item">
						<div class="filter-item-header">
							<input checked id="all-employment" type="checkbox"
								name="filter-item"> <label for="all-employment">Accepted proposals
								<span class="total">${response.countJobs_employment }</span>
							</label>
							
						</div>
					</div>
				</div>
				<div id="job-list">
					<c:forEach items="${response.applicationProgressStatuses }"
						var="applicationProgressStatus">
						<c:if test="${applicationProgressStatus.application
										.flag_applicantAcknowledgedAllPositionsAreFilled == 0 }">							
							<c:if test="${applicationProgressStatus.messages.size() > 0 }">
								
								<div class="messages">
									<h6>Messages</h6>
									<c:forEach items="${applicationProgressStatus.messages }" var="message">
										<p>${message }</p>
									</c:forEach>
								</div>
							</c:if>
							<div class="application "
								data-application-status="${applicationProgressStatus.application.status }"
								data-proposal-id="${applicationProgressStatus.currentProposal.proposalId }"
								data-employment-proposal-amount="${applicationProgressStatus.currentProposal.amount }"
								data-job-start-date=""
								data-job-end-date=""
								data-job-duration-days=""
								data-job-distance=""
								data-is-accepted="${applicationProgressStatus.application.isAccepted }"
								data-is-waiting-on-you="${applicationProgressStatus.isProposedToSessionUser }">
								

								<div class="application-content-wrapper">
									<div class="job-header">
										<div class="status-wrapper">
											<span class="status ${applicationProgressStatus.application.isAccepted == 1
												 ? 'status-employment' : 'status-application' }">
													 ${applicationProgressStatus.application.isAccepted == 1
													 ? "Employment" : "Application" }</span>										
	
											<p class="job-name accent show-job-info-mod"
												data-context="profile" data-p="1" data-job-id="${applicationProgressStatus.job.id }">
												${applicationProgressStatus.job.jobName }</p>
										</div>
									</div>
									<div
										class="proposal ${sessionScope.user.profileId == 1 &&
										applicationProgressStatus.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'confirm' : '' }"
										data-proposal-id="${applicationProgressStatus.currentProposal.proposalId }">
	
										<c:if
											test="${applicationProgressStatus.isProposedToSessionUser &&
															applicationProgressStatus.application.isAccepted == 0 }">
											<p class="exp-time black-bold">
												The employer's ${applicationProgressStatus.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'acceptence' : 'offer' } expires in <span class="red-bold">
													${applicationProgressStatus.time_untilEmployerApprovalExpires }</span>
											</p>
										</c:if>
	
										<c:if
											test="${!applicationProgressStatus.isProposedToSessionUser }">
											<p class="waiting-status black-bold">
												${applicationProgressStatus.currentProposalStatus }</p>
										</c:if>
	
										<c:set var="jobDto" value="${applicationDto.jobDto }" />
										<c:choose>
											<c:when test="${applicationProgressStatus.application.isAccepted == 1 }">
												<%@ include file="./EmploymentJobSummary.jsp"%>
											</c:when>
											<c:otherwise>
												<c:set var="param_job" value="${applicationProgressStatus.job }" />
												
												<%@ include file="../wage_proposal/CurrentProposal.jsp"%>
												<div class="render-present-proposal-mod"></div>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<div class="center">
					<p id="noApplications">You have no activity at this time.</p>
					<a id="" href="/JobSearch/jobs/find" class="sqr-btn teal">Find
						Jobs</a>
				</div>
			</c:otherwise>
		</c:choose>

	</div>
</div>
</div>
<script
	src="https://maps.googleapis.com/maps/api/
		js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI">
</script>

<%@ include file="../includes/resources/WageProposal.jsp"%>
<%@ include file="../includes/Footer.jsp"%>
<script
	src="<c:url value="/static/javascript/profile_employee/Profile_Employee.js" />"></script>
