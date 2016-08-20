<%@ include file="./includes/Header.jsp"%>

<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<link rel="stylesheet" type="text/css" href="../static/css/profile.css" />

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
</head>


<body>


	<div class="container">
		<c:choose>
			<c:when test="${user.activeJobs.size() >0 }">
				<div class="active-jobs-container">
					<div class="header"><h3>Active Jobs</h3></div>
					<div class="jobs-table-container">
						<table id="jobTable">
							<thead>
								<tr>
									<th>Job Name</th>
									<th>New Applications</th>
									<th>Total Applications</th>
									<th>Hires</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
		<!-- 						For each active job -->
								<c:forEach items="${user.getActiveJobs() }" var="activeJob">
									<tr class="job-row" id="${activeJob.getId()}">
										
										<c:choose>
											<c:when test="${activeJob.applications.size() > 0 }">
												<td class="job-name"><a href="../job/${activeJob.getId()}" >${activeJob.getJobName()}</a></td>
												
																						
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
												<td class="job-name no-applicants"><a href="../job/${activeJob.getId()}" >${activeJob.getJobName()}</a></td>
												<td class="job-data no-applicants">${activeJob.newApplicationCount }</td>
												<td class="job-data no-applicants">${activeJob.getApplications().size()}</td>
												<td class="job-data no-applicants">${activeJob.getEmployees().size()}</td>
											</c:otherwise>
										</c:choose>
										<td>							
											<a href="/JobSearch/job/${activeJob.id }/rate-employees?markComplete=1"><button class="square-button">Mark Complete</button></a>
										</td>
									</tr>
									
<!-- 									Expandable row that shows applicant info -->
									<tr class="applicants-row">
										<td colspan="4">
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
				<!-- 													Set endorsements -->
																	<c:forEach items="${application.applicant.endorsements }" var="endorsement">
																	
																		<div class="endorsement">													
																			${endorsement.categoryName } <span class="badge count">  ${endorsement.count }</span>
																		</div>
																	</c:forEach>
				
																</td>
																
<!-- 																Set the wage status -->
																<td id="${application.currentWageProposal.id }">																	
																	<div class="wage-status">
																	
																	
																		<c:choose>
																			<c:when test="${user.userId == application.currentWageProposal.proposedByUserId}">
<!-- 																			****** If the current wage proposal was proposed TO the employer -->
																																							
																				<div class="desired-pay">
																					Applicant asking for 
																					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
																				
																				
																				</div>
																				<div class="offered-pay hide-element">
																					Offered applicant  
																					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
																				</div>		

																				<div class="counter-container">															
																					<button class="counter">Counter</button>
																					<div class="counter-offer-container">
																						<input class="counter-offer-amount"></input>
																						<button class="send-counter-offer">Send</button>
																						<button class="cancel-counter-offer">Cancel</button>
																					</div>
																				</div>
																				<div class="sent-counter-notification hide-element">Counter offer sent</div>																				
																												
																			</c:when>
																			<c:otherwise>
<!-- 																			****** Otherise the current wage proposal was proposed BY the employer -->
																																							
																				<div class="desired-pay hide-element">
																					Applicant asking for 
																					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
																				</div>
																				<div class="offered-pay">
																					Offered applicant  
																					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
																				</div>		

																				<div class="counter-container hide-element">															
																					<button class="counter">Counter</button>
																					<div class="counter-offer-container">
																						<input class="counter-offer-amount"></input>
																						<button class="send-counter-offer">Send</button>
																						<button class="cancel-counter-offer">Cancel</button>
																					</div>
																				</div>
																				<div class="sent-counter-notification hide-element">Counter offer sent</div>																				
																																											
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
									</tr>
		
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</c:when>
		</c:choose>	
		
		<c:choose>
			<c:when test="${user.completedJobs.size() >0 }">			
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
								<c:forEach items="${user.completedJobs }" var="completedJobDTO">
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
</body>

<script>



$(document).ready(function(){

	
	$("#activeJobsTable tr td").click(function(){
		window.location = '../job/' + $(this).parent().attr('id');
	})	
	
	$(".has-applicants").click(function(e){
		
		//Only toggle if the element WITH "has-applicants" class (i.e. e.currentTaget)
		//gets clicked.
		//Do not toggle if any pathe job name hyperlink is clicked
// 		if(e.target == e.currentTarget){
			var parentRow = $(this).parents('tr')[0];
			$(parentRow).next(".applicants-row").toggle();	
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
	
	$(".counter").click(function(){
		var container = $(this).siblings(".counter-offer-container")[0];
		$(container).show();
	})
	
	$(".cancel-counter-offer").click(function(){
		$(this).parent().hide();
		$($(this).siblings("input")[0]).val("");
	})
	
	$(".send-counter-offer").click(function(){
		
		//Read the DOM
		var wageStatusContainer = $(this).parents(".wage-status")[0];
		var desiredPayContainer = $(wageStatusContainer).find(".desired-pay")[0];
		var offeredPayContainer = $(wageStatusContainer).find(".offered-pay")[0];
		var counterContainer = $(wageStatusContainer).find(".counter-container")[0];
		var wagePropoalId = $($(this).parents("td")[0]).attr("id");
		var counterAmount = $($(this).siblings("input")[0]).val();

		//Create the dto
		var wageProposalCounterDTO = {};
		wageProposalCounterDTO.wageProposalIdToCounter = wagePropoalId;
		wageProposalCounterDTO.counterAmount = counterAmount;

		//Make the ajax coll
		sendCounterOffer(wageProposalCounterDTO, function(){
			
			//After the counter has been made, hide the counter controls.			
			$(counterContainer).hide();
			
			//Hide the desired pay container
			$(desiredPayContainer).hide();
			
			//Show the offered pay container
			$(offeredPayContainer).html("Offered applicant " + twoDecimalPlaces(counterAmount));
			$(offeredPayContainer).show();

		})
		

		
	})
	


})


</script>

<%@ include file="./includes/Footer.jsp"%>

