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
				<div class="action-options">
					<a class="" href="/JobSearch/job/${jobDto.job.id}/edit" >
						<span class="glyphicon glyphicon-pencil"></span></a>	
						<span class="glyphicon glyphicon-search"></span>
				</div>		
				<p class="job-name accent show-job-info-mod-employer" data-job-id="${jobDto.job.id }">
					${jobDto.job.jobName }</p>
				
				<div class="brief-details">
					<div class="applicants-container">
						<input type="checkbox" id="employees-${jobDto.job.id }">
						<p>Applicants<span class="total total-applicants">
							${jobDto.countApplications_total }</span>
							<c:if test="${jobDto.countApplications_new > 0 }">							
								<span class="new new-applicants">${jobDto.countApplications_new } new</span></c:if></p>						
						<div class="application-details">
							<div>
								<input type="checkbox" id="employees-${jobDto.job.id }">
								<p class="${jobDto.countWageProposals_received > 0 ? 'action' : ''}">
									Proposals waiting on you
									<span class="total proposals-waiting-on-you
										 ${jobDto.countWageProposals_received > 0 ? 'action' : ''}">
										${jobDto.countWageProposals_received }</span>
									<c:if test="${jobDto.countWageProposals_received_new > 0 }">							
										<span class="new new-proposals-waiting-on-you">${jobDto.countWageProposals_received_new } new</span></c:if></p>
							</div>
							<div>								
								<input type="checkbox" id="employees-${jobDto.job.id }">
								<p>Proposals waiting on applicant
									<span class="total total-applicants">${jobDto.countWageProposals_sent }</span>
							</div>
							<input type="checkbox" id="employees-${jobDto.job.id }">
							<p>Expired proposals<span class="total total-applicants">${jobDto.countProposals_expired }</span></p>
						</div>
					</div>
					<input type="checkbox" id="employees-${jobDto.job.id }">
					
						<p>Employees<span class="total total-employees">
							${jobDto.countEmployees_hired } of ${jobDto.job.positionsPerDay }</span></p>
	<%-- 					<p class="see-full-details linky-hover" data-job-id="${jobDto.job.id }"> --%>
	<!-- 						<span class="text">Details</span> -->
	<!-- 						<span class="glyphicon glyphicon-menu-right"></span></p> -->
					<div>	
						<p class="show-hide-details see-details linky-hover "
							data-job-id="${jobDto.job.id }">Show All</p>
						<p class="show-hide-details hide-details linky-hover"
							data-job-id="${jobDto.job.id }">Hide All</p>
					</div>
				</div>
				
				
			</div>
		</c:forEach>
	</div>
	<div id="sort-wrapper">
		<div class="item sort">
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