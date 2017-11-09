<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/TableFilter.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>
<%@ include file="../includes/resources/InputValidation.jsp" %>

<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/homepage_employee/homepage_employee.css" />
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/homepage_employee/employment_summary_cal.css" />
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/homepage/personal_info.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/application_progress.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/homepage/homepage.css" />

<script src="/JobSearch/static/javascript/homepage/Personal_Info.js" type="text/javascript"></script>



<div class="a-container">
	<div id="personal-info-container">
		<%@ include file="../homepage/Personal_Info.jsp" %>			
	</div>	
	<div id="other-info-content">
		<div id="required-ratings">
			<%@ include file="../ratings/RatingRequired.jsp" %>
		</div>
		<c:if test="${!empty messageResponses_jobsTerminatedFrom }">
			<div class="message-container">
				<c:forEach items="${messageResponses_jobsTerminatedFrom }" var="messageResponse">
					<div class="message">
						<h4>The employer has removed you from the following
							job</h4>
						<p class="job-name accent show-job-info-mod" data-context="profile"
							data-job-id="${messageResponse.job.id }">messageResponse.job.jobName</p>
						<a class="acknowlege-message"
							href="/JobSearch/employer-removed-you-from-job/${messageResponse.job.id}/acknowledge">OK</a>
					</div>
				</c:forEach>
			</div>
		</c:if>
		<c:if
			test="${!empty messageResponses_applicationsClosedDueToAllPositionsFilled }">
			<div class="message-container">
				<c:forEach
					items="${messageResponses_applicationsClosedDueToAllPositionsFilled }"
					var="messageResponse">
					<div class="message">
						<h4>Your following applications have been closed
							due to the employer filling all positions. Your application will
							remain in the employer's inbox</h4>
						<p>${messageResponse.job.jobName }
							<a class="acknowlege-message"
								href="/JobSearch/application-closed-due-to-all-positions-filleed/
									${messageResponse.application.applicationId}/acknowledge">OK</a>
						</p>
					</div>
				</c:forEach>
			</div>
		</c:if>	
		<div id="employment-summary-calendar">
			<h1>Upcoming Employment</h1>
			<%@ include file="./EmploymentSummaryCalendar.jsp"%>
		</div>			
		<div id="activity" class="">
			<h1>Your Activity</h1>
			<div id="activity-content">
				<c:choose>
					<c:when test="${response.applicationProgressStatuses.size() > 0 }">				
						<div id="proposal-filters">
							<div class="filter-item">
								<input checked id="waiting-on-you" type="checkbox" name="proposal-status"
									data-waiting-on-you="1">
								<label for="waiting-on-you">
									<span>Proposals waiting	for you</span>				
									<span class="total">${response.countProposals_waitingOnYou}</span>						

								</label>
								<c:if test="${response.countProposals_waitingOnYou_new > 0}">
									<span class="total new red-bold">${response.countProposals_waitingOnYou_new}
										new</span>
								</c:if>
							</div>
							<div class="filter-item">
								<input checked id="waiting-on-other" type="checkbox" name="proposal-status"
									data-waiting-on-you="0">
								<label for="waiting-on-other">
									<span>Proposals waiting for the employer</span> 
									<span class="total">${response.countProposals_waitingOnOther}</span>
								</label>
							</div>
							<div class="filter-item">
								<input checked id="accepted-proposals" type="checkbox" name="proposal-status"
									data-accepted="1">
								<label for="accepted-proposals">
									<span>Accepted proposals</span>
									<span class="total">${response.countJobs_employment }</span>
								</label>
							</div>
						</div>
						<div id="applications">
							<c:forEach items="${response.applicationProgressStatuses }"
								var="applicationProgressStatus">
								<c:if test="${applicationProgressStatus.application
												.flag_applicantAcknowledgedAllPositionsAreFilled == 0 }">							
									<div class="application-container">
										<div class="application"
											data-application-status="${applicationProgressStatus.application.status }"
											data-proposal-id="${applicationProgressStatus.currentProposal.proposalId }"
											data-employment-proposal-amount="${applicationProgressStatus.currentProposal.amount }"
											data-job-start-date=""
											data-job-end-date=""
											data-job-duration-days=""
											data-job-distance=""
											data-is-accepted="${applicationProgressStatus.application.isAccepted }"
											data-is-waiting-on-you="${applicationProgressStatus.isProposedToSessionUser }">
											
											<div class="application-content">
												<p class="job-name show-job-info-mod"
													data-context="profile" data-p="1" data-job-id="${applicationProgressStatus.job.id }">
													${applicationProgressStatus.job.jobName }</p>
												<c:if test="${applicationProgressStatus.messages.size() > 0 }">									
													<div class="messages">
														<h6>Messages</h6>
														<c:forEach items="${applicationProgressStatus.messages }" var="message">
															<p>${message }</p>
														</c:forEach>
													</div>
												</c:if>														
												<div class="proposal ${applicationProgressStatus.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'confirm' : '' }"
													data-proposal-id="${applicationProgressStatus.currentProposal.proposalId }">
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
</div>
<!-- <script -->
<!-- 	src="https://maps.googleapis.com/maps/api/ -->
<!-- 		js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI"> -->
<!-- </script> -->

<%@ include file="../includes/resources/WageProposal.jsp"%>
<%@ include file="../includes/Footer.jsp"%>
<script
	src="<c:url value="/static/javascript/profile_employee/Profile_Employee.js" />"></script>
