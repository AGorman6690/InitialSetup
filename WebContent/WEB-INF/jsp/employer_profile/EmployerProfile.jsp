<%@ include file="../includes/Header.jsp"%>
<script src="<c:url value="/static/javascript/profile_employer/Profile_Employer.js" />"></script>
<%@ include file="../includes/resources/SelectPageSection.jsp"%>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employer/profile_employer_NEW.css" />

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>

<div class="left-bar"></div>
<div class="container">	
	<c:choose>
		<c:when test="${jobDtos.size() == 0 }">
			<div>You have no jobs waiting to start</div>	
		</c:when>
		<c:otherwise>
			<table id="table_jobsWaitingToStart" class="main-table-style shadow">
				<thead>
					<tr class="">
						<th id="job-name" ></th>
						<th id="start-date" >Start date</th>
						<th id="end-date" class="">End date</th>
						<th id="location" class="">Location</th>
						<th id="" class="job-status first applicant-perspective">Total applicants</th>		
						<th id="offers-waiting-on" colspan="2" class="job-status applicant-perspective">
							<div>
								<p>Offers waiting on...</p>
								<p><span>Applicant</span><span>You</span></p>
							</div>
						</th>						
	<!-- 					<th id="" class="job-status applicant-perspective">Offers waiting on applicant</th> -->
	<!-- 					<th id="" class="job-status applicant-perspective">Offers waiting on you</th> -->
						<th id="" class="job-status">Positions filled</th>
						<th id="" class="job-status">Positions available</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jobDtos }" var="jobDto">
						<tr id="${jobDto.job.id }" class="${jobDto.job.status == 1 ? 'in-process' : '' }">
							<td class="job-name job-details perm1 perm perm-first">
								<a class="accent" href="../job/${jobDto.job.id}?c=waiting&p=2&d=all-apps" >
									${jobDto.job.jobName }
								</a>
							</td>						
							<td class="job-details"><span>${jobDto.job.stringStartDate }</span></td>
							<td class="job-details"><span>${jobDto.job.stringEndDate }</span></td>
							<td class="job-details job-location"><span>${jobDto.job.city}, ${jobDto.job.state }</span></td>
							<td>	
								<div class="new-container">
									<c:choose>
										<c:when test="${jobDto.countApplications_total > 0 }">
											<a class="accent" href="../job/${jobDto.job.id}/?c=waiting&d=all-apps">
												${jobDto.countApplications_total }</a>
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>									
									<c:if test="${jobDto.countApplications_new > 0 }">
										<a class="new" href="../job/${jobDto.job.id}/?c=waiting&d=applicants-new">
											${jobDto.countApplications_new }</a>
									</c:if>		
								</div>																											
							</td>						
							<td>	
								<c:choose>
									<c:when test="${jobDto.countWageProposals_sent > 0 }">
										<a class="accent" href="../job/${jobDto.job.id}/?c=waiting&d=sent-proposals">
											${jobDto.countWageProposals_sent }</a>
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>
								</c:choose>																								
							</td>
							<td>							
								<div class="new-container">
									<c:choose>
										<c:when test="${jobDto.countWageProposals_received > 0 }">
											<a class="accent" href="../job/${jobDto.job.id}/?c=waiting&d=received-proposals">
												${jobDto.countWageProposals_received }</a>
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>									
									<c:if test="${jobDto.countWageProposals_received_new > 0 }">
										<a class="new" href="../job/${jobDto.job.id}/?c=waiting&d=received-proposals-new">
											${jobDto.countWageProposals_received_new }</a>
									</c:if>
								</div>																
							</td>							
							<td>
								<c:choose>
									<c:when test="${jobDto.countEmployees_hired > 0 }">
										<a class="accent" href="../job/${jobDto.job.id}/?c=waiting&d=hired">
											${jobDto.countEmployees_hired }</a>
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>
								</c:choose>														
							</td>																					
							<td>
								${jobDto.job.positionsPerDay }
											
							</td>	
							<td>
								<a class="" href="/JobSearch/job/${jobDto.job.id}/edit" >
									<span class="glyphicon glyphicon-pencil"></span></a>
							</td>
	<!-- 						<td> -->
	<%-- 							<c:if test="${jobDto.countEmployees_hired == jobDto.job.positionsPerDay }"> --%>
	<%-- 								<a href="/JobSearch/job/${jobDto.job.id}/replace-employee" class="accent sqr-btn replace-an-employee">Replace an employee</a> --%>
	<%-- 							</c:if> --%>
	<!-- 						</td> -->
						</tr>
					</c:forEach>
				</tbody>
			</table>		
		</c:otherwise>		
	</c:choose>

</div>
<%@ include file="../includes/Footer.jsp"%>