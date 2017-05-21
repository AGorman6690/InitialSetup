<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>	

<!-- <link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employees.css" /> -->
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/inputValidation.css" />

<script src="/JobSearch/static/javascript/ratings/RateEmployer.js" type="text/javascript"></script>

<input id="jobId" type="hidden" value="${job.id }">
<div class="my-container pad-top-2">
	
	<div id="submit-cont">
		<button id="submit" class="sqr-btn teal">Submit Ratings</button>
		<div class="error-message-container">
			<p class="error-message">Please rate the employer</p>
		</div>
	</div>
	<div id="job" class="item">
<!-- 		<h1>Job</h1> -->
		<h1><a href="/JobSearch/job/${job.id }?p=2&c=complete">${job.jobName }</a></h1>
	</div>
	<div id="main-cont" class="item center rate-employer">	
		<div id="ratings-cont">
			<div class="user-cont" data-user-id="${employer.userId}">
				<div class="">
					<h2>Rate <span>${employer.firstName} ${employer.lastName}</span></h2>	
					<div class="star-rating-cont">
						<input class="star-rating" name="" class="rating-loading"
							value="0" data-size="xs">
					</div>		
				</div>
				<div class="rating-criteria-cont">
					<div class="on-time rate-criterion" data-rate-criterion-id="4">
						<p>Did ${employer.firstName} pay you as agreed upon?</p>
						<div class="rating-answer button-group no-toggle">
							<button class="sqr-btn yes">Yes</button>
							<button class="sqr-btn no">No</button>
						</div>
						<div class="if-no radio-container">
							<label><input type="radio" value="3" name="rate-1-${employer.userId }">
								I was paid the full amount, but the payment was late
							</label>
							<label><input type="radio" value="1" name="rate-1-${employer.userId }">
								He did not pay me
							</label>							
						</div>
					</div>
					<div class="work-ethic rate-criterion" data-rate-criterion-id="5">
						<p>Did the job posting accurately describe the work you performed?</p>
						<div class="rating-answer button-group no-toggle">
							<button class="sqr-btn yes">Yes</button>
							<button class="sqr-btn no">No</button>
						</div>
						<div class="if-no radio-container">
							<label><input type="radio" value="3" name="rate-2-${employer.userId }">
								The job posting somewhat described the work I performed, but could have been more accurate
							</label>
							<label><input type="radio" value="1" name="rate-2-${employer.userId }">
								None of the work I performed was described in the job posting
							</label>							
						</div>
					</div>	
					<div class="experience rate-criterion" data-rate-criterion-id="8">
						<p>Did ${employer.firstName} provide work for all the days you agreed upon, excluding weather related incidents?</p>
						<div class="rating-answer button-group no-toggle">
							<button class="sqr-btn yes">Yes</button>
							<button class="sqr-btn no">No</button>
						</div>
						<div class="if-no radio-container">
							<label><input type="radio" value="3" name="rate-3-${employer.userId }">
								There was work for some of the days we agreed upon, but not all days
							</label>
							<label><input type="radio" value="1" name="rate-3-${employer.userId }">
								There was at least one day with no work and ${employer.firstName } did not notify me beforehand
							</label>							
						</div>
					</div>				
					<div class="hire-again rate-criterion" data-rate-criterion-id="6">
						<p>Would you work for ${employer.firstName} again?</p>
						<div class="rating-answer button-group no-toggle">
							<button class="sqr-btn yes">Yes</button>
							<button class="sqr-btn no" data-rating-value="1">No</button>
						</div>
					</div>		
					<div class="comment-cont">
						<p>Comment</p>
						<textarea class="comment" rows="4"></textarea>
					</div>																	
				</div>			

			</div>
		</div>
	</div>
</div>

<%@ include file="../includes/Footer.jsp"%>