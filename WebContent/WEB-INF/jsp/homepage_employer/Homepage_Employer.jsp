<%@ include file="../includes/Header.jsp"%>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/homepage_employer.css" />
<script src="/JobSearch/static/javascript/HomePage_Employer.js" type="text/javascript"></script>

<div class="container">
	<h1>Your Jobs</h1>
	<div id="jobs-list">
		<c:forEach items="${jobDtos }" var="jobDto">
			<div class="job-container">
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
					<p class="see-full-details linky-hover" data-job-id="${jobDto.job.id }">
						<span class="text">Details</span>
						<span class="glyphicon glyphicon-menu-right"></span></p>
				</div>
			</div>
		</c:forEach>
	</div>
	<div id="job-details">
		<div class="job-detail" data-job-id="">
			<div class="sort-and-filter-wrapper">
				<div class="item">
					<input type="checkbox" id="employees-123"><label for="employees-123">Employees</label>
				</div>	
				<div class="item">
					<input type="checkbox" id="applicants-123"><label for="applicants-123">Applicants</label>
					<div class="sub-items">
						<div>
							<input type="checkbox" id="waiting-on-you-123"><label for="waiting-on-you-123">Proposals waiting on you</label>
						</div>
						<div>
							<input type="checkbox" id="waiting-on-applicant-123"><label for="waiting-on-applicant-123">Proposals waiting on applicant</label>
						</div>
						<div>
							<input type="checkbox" id="expired-proposals-123"><label for="expired-proposals-123">Expired proposals</label>
						</div>					
					</div>
				</div>				
				<div class="item">
					<input type="checkbox" id="favorites-123"><label for="favorites-123">Favorites</label>
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
			<div class="people">
			
			</div>
		</div>
	</div>	
</div>