<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>
<%@ include file="../includes/resources/WageProposal.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/home_page_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/application_progress.css" />
<script src="/JobSearch/static/javascript/HomePage_Employer.js" type="text/javascript"></script>

<div class="container">
	<h1>Your Jobs</h1>
	<div id="jobs-list">
		<c:forEach items="${jobDtos }" var="jobDto">
			<div class="job-container" data-job-id="${jobDto.job.id }">
				<a class="job-name">${jobDto.job.jobName }</a>
				<div class="brief-details">
					<div class="applicants-container">
						<p>Applicants<span class="total total-applicants">${jobDto.countApplications_total }</span>
							<c:if test="${jobDto.countApplications_new > 0 }">							
								<span class="new new-applicants">${jobDto.countApplications_new } new</span></c:if></p>						
						<div class="application-details">
							<p>Proposals waiting on you
								<span class="total proposals-waiting-on-you">${jobDto.countWageProposals_received }</span>
								<c:if test="${jobDto.countWageProposals_received_new > 0 }">							
									<span class="new new-proposals-waiting-on-you">${jobDto.countWageProposals_received_new } new</span></c:if></p>								
							<p>Proposals waiting on applicant
								<span class="total total-applicants">${jobDto.countWageProposals_sent }</span>
							<p>Expired proposals<span class="total total-applicants">${jobDto.countProposals_expired }</span></p>
						</div>
					</div>
					<p>Employees<span class="total total-employees">
						${jobDto.countEmployees_hired } of ${jobDto.job.positionsPerDay }</span></p>
<%-- 					<p class="see-full-details linky-hover" data-job-id="${jobDto.job.id }"> --%>
<!-- 						<span class="text">Details</span> -->
<!-- 						<span class="glyphicon glyphicon-menu-right"></span></p> -->
					<p class="show-hide-details see-details linky-hover "
						data-job-id="${jobDto.job.id }">See Details</p>
					<p class="show-hide-details hide-details hide-on-load linky-hover"
						data-job-id="${jobDto.job.id }">Hide Details</p>
				</div>
				
				
			</div>
		</c:forEach>
	</div>
	<div id="job-details">
		
	</div>	
</div>
<%@ include file="../includes/Footer.jsp"%>