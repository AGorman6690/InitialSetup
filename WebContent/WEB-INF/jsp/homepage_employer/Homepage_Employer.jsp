<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>
<%@ include file="../includes/resources/WageProposal.jsp" %>
<%@ include file="../includes/resources/InputValidation.jsp" %>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/homepage/personal_info.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/home_page_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/home_page_employer/application_progress.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/homepage/homepage.css" />
<script src="/JobSearch/static/javascript/HomePage_Employer.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/homepage/Personal_Info.js" type="text/javascript"></script>

<div class="a-container">

	<div id="personal-info-container">
		<%@ include file="../homepage/Personal_Info.jsp" %>			
	</div>	
	<div id="jobs-list">
		<div id="required-ratings">
			<%@ include file="../ratings/RatingRequired.jsp" %>
		</div>	
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
							<a class="hide-on-load" href="/JobSearch/job/${employerHomepageJob.job.id}/edit" >
								<span class="glyphicon glyphicon-pencil"></span></a>	
							<a class="" href="/JobSearch/job/${employerHomepageJob.job.id}/find-employees" >
								<span class="glyphicon glyphicon-search"></span></a>
						</div>		
						<p class="job-name accent show-job-info-mod" data-context="waiting" data-job-id="${employerHomepageJob.job.id }">
							${employerHomepageJob.job.jobName }</p>
						<div class="sort-proposals-wrapper">
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
						<div class="proposal-details" data-job-id="${employerHomepageJob.job.id }">							
							<div class="proposal-detail">
								<input type="checkbox" id="waiting-on-you-${employerHomepageJob.job.id }" class="waiting-on-you">
								<label for="waiting-on-you-${employerHomepageJob.job.id }" class="${employerHomepageJob.countWageProposals_received > 0 ? 'action' : ''}">
									<span>Proposals waiting on you</span>
									<span class="total proposals-waiting-on-you
										 ${employerHomepageJob.countWageProposals_received > 0 ? 'action' : ''}">
										${employerHomepageJob.countWageProposals_received }</span>									
								</label>
								<c:if test="${employerHomepageJob.countWageProposals_received_new > 0 }">
									<span class="new red pointer">${employerHomepageJob.countWageProposals_received_new } new</span>							
								</c:if>									
							</div>
								
<%-- 							<c:if test="${employerHomepageJob.countWageProposals_received_new > 0 }"> --%>
<!-- 								<div class="proposal-detail new-wrapper">								 -->
<%-- 									<input type="checkbox" id="new-${employerHomepageJob.job.id }" class=""> --%>
<%-- 									<label for="new-${employerHomepageJob.job.id }"> --%>
<!-- 										<span>New</span> -->
<%-- 										<span class="total total-applicants">${employerHomepageJob.countWageProposals_received_new } new</span> --%>
<!-- 									</label> -->
<!-- 								</div>								 -->
<%-- 							</c:if>			 --%>
							<div class="proposal-detail">								
								<input type="checkbox" id="waiting-on-applicant-${employerHomepageJob.job.id }" class="waiting-on-other">
								<label for="waiting-on-applicant-${employerHomepageJob.job.id }">
									<span>Proposals waiting on applicant</span>
									<span class="total total-applicants">${employerHomepageJob.countWageProposals_sent }</span>
								</label>
							</div>											
							<div class="proposal-detail">
								<input type="checkbox" id="expired-${employerHomepageJob.job.id }" class="expired">
								<label for="expired-${employerHomepageJob.job.id }">
									<span>Expired proposals</span>
									<span class="total total-applicants">${employerHomepageJob.countProposals_expired }</span>
								</label>
							</div>
							<div class="proposal-detail">
								<input type="checkbox" id="employees-${employerHomepageJob.job.id }" class="accepted">
								<label for="employees-${employerHomepageJob.job.id }">
									<span>Accepted proposals</span>
									<span class="total total-employees">
										${employerHomepageJob.countEmployees_hired }</span>
								</label>
							</div>
							<div class="proposal-list"></div>
						</div>
						
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>				
	</div>
</div>

<!-- <script -->
<!-- 	src="https://maps.googleapis.com/maps/api/ -->
<!-- 		js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI"> -->
<!-- </script> -->

<%@ include file="../includes/Footer.jsp"%>