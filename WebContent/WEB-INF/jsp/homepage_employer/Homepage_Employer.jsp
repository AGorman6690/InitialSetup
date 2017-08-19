<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>
<%@ include file="../includes/resources/WageProposal.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/home_page_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/application_progress.css" />
<script src="/JobSearch/static/javascript/HomePage_Employer.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/Personal_Info.js" type="text/javascript"></script>

<div class="a-container">
	<div id="profile-content">
		<%@ include file="../credentials_employee/Credentials_Employee.jsp" %>			
		<%@ include file="../ratings/RatingsByUser.jsp" %>
	</div>
	
	<div id="jobs-list">
	<h1>Your Jobs</h1>
		<c:choose>
			<c:when test="${response.employerHomepageJobs.size() == 0 }">
				<p>You do not have any jobs posted right now</p>
				<a class="sqr-btn blue" href="/JobSearch/post-job">Post a job</a>
			</c:when>
			<c:otherwise>
				<c:forEach items="${response.employerHomepageJobs }" var="employerHomepageJob">
					<div class="job-container" data-job-id="${employerHomepageJob.job.id }">
						<div class="action-options">
							<a class="" href="/JobSearch/job/${employerHomepageJob.job.id}/edit" >
								<span class="glyphicon glyphicon-pencil"></span></a>	
							<a class="" href="/JobSearch/job/${employerHomepageJob.job.id}/find-employees" >
								<span class="glyphicon glyphicon-search"></span></a>
						</div>		
						<p class="job-name accent show-job-info-mod" data-context="waiting" data-job-id="${employerHomepageJob.job.id }">
							${employerHomepageJob.job.jobName }</p>
						
						<div class="brief-details">
							<div class="applicants-container">
		<%-- 						<input type="checkbox" id="employees-${employerHomepageJob.job.id }"> --%>
		<!-- 						<p>All applicants<span class="total total-applicants"> -->
		<%-- 							${employerHomepageJob.countApplications_total }</span> --%>
		<%-- 							<c:if test="${employerHomepageJob.countApplications_new > 0 }">							 --%>
		<%-- 								<span class="new new-applicants">${employerHomepageJob.countApplications_new } new</span></c:if></p>						 --%>
		<!-- 						<div class="application-details"> -->
									<div>
										<input type="checkbox" id="employees-${employerHomepageJob.job.id }">
										<p class="${employerHomepageJob.countWageProposals_received > 0 ? 'action' : ''}">
											<span>Proposals waiting on you</span>
											<span class="total proposals-waiting-on-you
												 ${employerHomepageJob.countWageProposals_received > 0 ? 'action' : ''}">
												${employerHomepageJob.countWageProposals_received }</span>
											<c:if test="${employerHomepageJob.countWageProposals_received_new > 0 }">							
												<span class="new new-proposals-waiting-on-you">${employerHomepageJob.countWageProposals_received_new } new</span></c:if></p>
									</div>
									<div>								
										<input type="checkbox" id="employees-${employerHomepageJob.job.id }">
										<p><span>Proposals waiting on applicant</span>
											<span class="total total-applicants">${employerHomepageJob.countWageProposals_sent }</span>
									</div>
									<input type="checkbox" id="employees-${employerHomepageJob.job.id }">
									<p>
										<span>Expired proposals</span>
										<span class="total total-applicants">${employerHomepageJob.countProposals_expired }</span>
									</p>
								</div>
		<!-- 					</div> -->
							<input type="checkbox" id="employees-${employerHomepageJob.job.id }">
							
								<p>
									<span>Accepted proposals</span>
									<span class="total total-employees">
									${employerHomepageJob.countEmployees_hired } of ${employerHomepageJob.job.positionsPerDay }</span></p>
			<%-- 					<p class="see-full-details linky-hover" data-job-id="${employerHomepageJob.job.id }"> --%>
			<!-- 						<span class="text">Details</span> -->
			<!-- 						<span class="glyphicon glyphicon-menu-right"></span></p> -->
							<div>	
								<p class="show-hide-details see-details linky-hover "
									data-job-id="${employerHomepageJob.job.id }">View proposals</p>
								<p class="show-hide-details hide-details linky-hover"
									data-job-id="${employerHomepageJob.job.id }">Hide proposals</p>
							</div>
						</div>
						
						
					</div>
				</c:forEach>
				<div id="sort-wrapper">
					<div class="item sort hide-on-load">
						<input id="surpress-certain-details" type="checkbox" name="surpress-certain-details">
						<label for="surpress-certain-details">Less Info</label>
					</div>		
					<div class="item sort">
						<p><span>Rating</span><span class="glyphicon glyphicon-arrow-down"></span>
							<span class="glyphicon glyphicon-arrow-up"></span></p>
					</div>	
					<div class="item sort">
						<p><span>Proposed wage</span><span class="glyphicon glyphicon-arrow-down"></span>
							<span class="glyphicon glyphicon-arrow-up"></span></p>
					</div>								
				
				</div>
				<div id="job-details">
					
				</div>	
			</c:otherwise>
		</c:choose>				
	</div>

</div>
		<div id="job-info-mod" class="mod simple-header">
			<div class="mod-content">
				<div class="mod-header"></div>
				<div class="mod-body"></div>
			</div>
		</div>
<script
	src="https://maps.googleapis.com/maps/api/
		js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI">
</script>

<%@ include file="../includes/Footer.jsp"%>