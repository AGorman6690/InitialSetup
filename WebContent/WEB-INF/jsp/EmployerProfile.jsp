<%@ include file="./includes/Header.jsp"%>

<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>

<link rel="stylesheet" type="text/css" href="../static/css/profile.css" />
</head>


<body>


	<div class="container">
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
							</tr>
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
	
})


</script>

<%@ include file="./includes/Footer.jsp"%>

