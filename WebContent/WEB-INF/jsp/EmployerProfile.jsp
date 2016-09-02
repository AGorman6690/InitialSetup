<%@ include file="./includes/Header.jsp"%>

<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
<link rel="stylesheet" type="text/css" href="../static/css/profile.css" />

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
</head>


<body>

	<div>${vtFailedWageNegotiations }</div>
	
	<div class="container">
		<c:choose>
			<c:when test="${activeJobs.size() >0 }">
				<div class="active-jobs-container">
					<div class="header"><h3>Active Jobs</h3></div>
					<div class="jobs-table-container">
						<table id="jobTable">
							<thead>
								<tr>
									<th id="expandJob"></th>
									<th id="name">Job Name</th>
									<th id="newApplicantions">New Applications</th>
									<th id="totalApplications">Total Applications</th>
									<th id="hires">Employees</th>									
									<th id="markJobCompleteHeader"></th>
								</tr>
							</thead>
							<tbody>
		<!-- 						For each active job -->
								<c:forEach items="${activeJobs }" var="activeJob">
									<tr class="job-row" id="${activeJob.getId()}">
									
										<!-- ****** if there are applicants, show the expand icon -->
										<c:choose>
											<c:when test="${activeJob.applications.size() > 0 }">
												<td><span class="expand-job glyphicon glyphicon-menu-down"></span></td>
											</c:when>										
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
										<td class="job-name"><a href="../job/${activeJob.getId()}" >${activeJob.getJobName()}</a></td>
										<c:choose>
											<c:when test="${activeJob.applications.size() > 0 }">
<%-- 												<td class="job-name"><a href="../job/${activeJob.getId()}" >${activeJob.getJobName()}</a></td> --%>
																																		
												<c:choose>
													<c:when test="${activeJob.newApplicationCount > 0}">
													<td class="job-data has-applicants has-new-applicants">${activeJob.newApplicationCount }</td>
													</c:when>
													<c:otherwise>
													<td class="job-data"><span class="has-applicants">${activeJob.newApplicationCount }</span></td>
													</c:otherwise>
												</c:choose>
																								
												<td class="job-data"><span class="has-applicants">${activeJob.getApplications().size()}</span></td>
												<td class="job-data"><span class="has-applicants">${activeJob.getEmployees().size()}</span></td>
											</c:when>
											<c:otherwise>
<%-- 												<td class="job-name no-applicants"><a href="../job/${activeJob.getId()}" >${activeJob.getJobName()}</a></td> --%>
												<td class="job-data no-applicants">${activeJob.newApplicationCount }</td>
												<td class="job-data no-applicants">${activeJob.getApplications().size()}</td>
												<td class="job-data no-applicants">${activeJob.getEmployees().size()}</td>
											</c:otherwise>
										</c:choose>
										
										<td>	
														
											<a href="/JobSearch/job/${activeJob.id }/rate-employees?markComplete=1"><button class="square-button">Mark Complete</button></a>
										</td>
									</tr>
									
<!-- 								****** Expandable row that shows applicant info -->		
									<c:choose>
										<c:when test="${activeJob.applications.size() > 0 }">												
											<tr class="applicants-row">
												<td colspan="5">
													<div class="applicants-container">
														<h4>Applicants</h4>
														<div class="applicants">
															<table id="applicantsTable">
																<thead>
																	<tr>
																		<th class="applicant-name">Name</th>
																		<th class="applicant-rating">Rating</th>
																		<th class="applicant-endorsements">Endorsements</th>	
																		<th class="applicant-wage-negotiation">Wage Negotiation</th>
		<!-- 																<th class="applicant-desired-pay">Offered Pay ($/hr)</th>									 -->
																	</tr>
																</thead>
																<tbody>
																	<c:forEach items="${activeJob.applications }" var="application">
																	<tr class="applicant-row">
																		<td><a>${application.applicant.firstName }</a></td>
																		<td>${application.applicant.rating }</td>
																		<td>
						<!-- 												****** Set endorsements -->
																			<c:forEach items="${application.applicant.endorsements }" var="endorsement">
																			
																				<div class="endorsement">													
																					${endorsement.categoryName } <span class="badge count">  ${endorsement.count }</span>
																				</div>
																			</c:forEach>
						
																		</td>
																		
		<!-- 															****** Set the wage status -->
																		<td id="${application.currentWageProposal.id }">																	
																			<div class="wage-status">																	
																			
																				<c:choose>
																					<c:when test="${application.currentWageProposal.status == 1 }">
																					<!-- ****** If the current wage proposal has been accepted-->
																						<div><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/> has been accepted</div>
																					</c:when>										
																					<c:when test="${application.currentWageProposal.status == 2 }">
																					<!-- ****** If the current wage proposal has been denied-->
																						<div><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/> has been denied. Negotiations have ended.</div>
																					</c:when>	
																					<c:otherwise>
																					<!-- ****** Otherwise current wage proposal is a counter offer-->
																						<c:choose>
																							<c:when test="${user.userId == application.currentWageProposal.proposedToUserId}">
				<!-- 																		****** If the counter offer was proposed TO the employer -->
																								<div id="${application.currentWageProposal.id}" class="counter-offer-container">
																									<div class="offer-context">
																										Applicant asking for  
																										<span id="amount">
																											<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
																										</span>
																									</div>																				
																									<div class="counter-offer-response">
																										<button class="accept-counter">Hire</button>
																										<button class="re-counter">Counter</button>		
																										<button class="decline-counter">Decline</button>							
																										<div class="re-counter-amount-container hide-element">
																											<input class="re-counter-amount"></input>
																											<button class="send-counter-offer">Send</button>
																											<button class="cancel-counter-offer">Cancel</button>
																										</div>										
																									</div>
																																																																																									
																								</div>
																								<div class="sent-response-notification"></div>	
																							</c:when>																				
																							<c:otherwise>
				<!-- 																		****** Otherwise the counter offer was proposed BY the employer -->
																								<div class="offer-context">
																									Offered applicant  
																									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
																								</div>																																											
																							</c:otherwise>
																					
																						</c:choose>
																					</c:otherwise>																	
																					
		
																				</c:choose>
																			</div>
																		</td>
																	</tr>
																	</c:forEach>
																</tbody>
															</table>
														</div>
													</div>	
												</td>
											</tr> <!-- close applicant dropdown row -->
										</c:when>
									</c:choose>
		
								</c:forEach> <!-- close for each active job -->
							</tbody>
						</table>
					</div>
				</div>
			</c:when>
		</c:choose>	
		
		<c:choose>
			<c:when test="${completedJobs.size() >0 }">			
				<div class="completed-jobs-container">
					<div class="header"><h3>Completed Jobs</h3></div>
					<div class="jobs-table-container">
						<table id="jobTable">
							<thead>
								<tr>
									<th>Job Name</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
		<!-- 						For each active job -->
								<c:forEach items="${completedJobs }" var="completedJobDTO">
									<tr class="job-row" id="${completedJobDTO.job.id}">
											<td class="job-name"><a href="#" >${completedJobDTO.job.jobName}</a></td>
		
										<td><a href="/JobSearch/job/${completedJobDTO.job.id }/rate-employees?markComplete=1"><button class="square-button">Rate Employees</button></a>
										</td>
									</tr>						
		
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</c:when>
		</c:choose>
	</div>
	
	<div>${test }</div>
</body>

<script>



$(document).ready(function(){

	
	$("#activeJobsTable tr td").click(function(){
		window.location = '../job/' + $(this).parent().attr('id');
	})	
	
	$(".expand-job").click(function(e){
		
		//Only toggle if the element WITH "has-applicants" class (i.e. e.currentTaget)
		//gets clicked.
		//Do not toggle if any pathe job name hyperlink is clicked
// 		if(e.target == e.currentTarget){
			var parentRow = $(this).parents('tr')[0];
			$(parentRow).next(".applicants-row").toggle();	
			toggleClasses($(this), "glyphicon-menu-down", "glyphicon-menu-up");
// 		}
		
	})
	
	$(".mark-complete-ouououoiuiou").click(function(){
	
		var headers = {};
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");
		
		var jobId = $($(this).parents("tr")[0]).attr("id");
		
		$.ajax({
			type : "PUT",
			url : "/JobSearch/job/" + jobId + "/markComplete",
			headers : headers
		}).done(function() {
			$('#home')[0].click();
		}).error(function() {
			$('#home')[0].click();

		});
		
	})

})


</script>

<%@ include file="./includes/Footer.jsp"%>

