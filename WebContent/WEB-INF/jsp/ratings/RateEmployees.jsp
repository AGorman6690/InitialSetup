<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>	

<!-- <link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employees.css" /> -->
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rate_employer.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<script src="/JobSearch/static/javascript/ratings/RateEmployer.js" type="text/javascript"></script>

<input id="jobId" type="hidden" value="${job.id }">
<div class="my-container pad-top-2">
	
	<div id="submit-cont">
		<button id="submit" class="sqr-btn teal">Submit Ratings</button>
	</div>
	<div id="job" class="item pad-top">
		<h1>Job</h1>
		<p><a href="/JobSearch/job/${job.id }?p=2&c=complete">${job.jobName }</a></p>
	</div>
	<div id="main-cont" class="item pad-top">
		<div id="employee-list">
			<h2>Employees</h2>	
			<div id="employees">
				<c:forEach items="${employees }" var="employee">
					<p data-user-id="${employee.userId }"
						data-name="${employee.firstName } ${employee.lastName }">							
						${employee.firstName } ${employee.lastName }
					</p>		
				</c:forEach>		
			</div>	
		</div>
		<div id="ratings-cont">
			<c:forEach items="${employees }" var="employee" varStatus="status">
				<div class="user-cont" data-user-id="${employee.userId}">
					<h2>Rate <span id="selected-employee">${employee.firstName} ${employee.lastName}</span></h2>	
					<div class="star-rating-cont">
						<input class="star-rating" name="" class="rating-loading"
							value="0" data-size="xs">
					</div>		
					<div class="rating-criteria-cont pad-top-2">
						<div class="on-time rate-criterion" data-rate-criterion-id="2">
							<p>Did ${employee.firstName}'s timeliness meet your expectations?</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes">Yes</button>
								<button class="sqr-btn no">No</button>
							</div>
							<div class="if-no radio-container">
								<label><input type="radio" value="3" name="on-time-${employee.userId }">
									${employee.firstName} occasionally arrived late
								</label>
								<label><input type="radio" value="1" name="on-time-${employee.userId }">
									${employee.firstName} never arrived on time
								</label>							
							</div>
						</div>
						<div class="work-ethic rate-criterion" data-rate-criterion-id="1">
							<p>Did ${employee.firstName}'s work ethic meet your expectations</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes">Yes</button>
								<button class="sqr-btn no">No</button>
							</div>
							<div class="if-no radio-container">
								<label><input type="radio" value="3" name="work-${employee.userId }">
									${employee.firstName}'s work ethic was OK, but I expected more
								</label>
								<label><input type="radio" value="1" name="on-time-${employee.userId }">
									${employee.firstName}'s work ethic was poor
								</label>							
							</div>
						</div>	
						<div class="experience rate-criterion" data-rate-criterion-id="7">
							<p>Did ${employee.firstName}'s experience level meet your expectations?</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes">Yes</button>
								<button class="sqr-btn no">No</button>
							</div>
							<div class="if-no radio-container">
								<label><input type="radio" value="3" name="on-time-${employee.userId }">
									${employee.firstName}'s experience level was OK, but I was expected more
								</label>
								<label><input type="radio" value="1" name="on-time-${employee.userId }">
									${employee.firstName}'s experience level was far less than I expected
								</label>							
							</div>
						</div>				
						<div class="hire-again rate-criterion" data-rate-criterion-id="3">
							<p>Would you hire ${employee.firstName} again?</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes">Yes</button>
								<button class="sqr-btn no" data-rating-value="1">No</button>
							</div>
						</div>														
					</div>			
					<div class="pad-top">
						<p>Comment</p>
						<textarea class="comment" rows="4"></textarea>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</div>