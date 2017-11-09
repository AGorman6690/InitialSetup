 <%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>	


<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/ratings/rate_employees.css" />

<script src="/JobSearch/static/javascript/ratings/Rate.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/ratings/RateEmployees.js" type="text/javascript"></script>

<input id="job-id" type="hidden" value="${response.job.id }">
<div class="my-container pad-top-2">
	
	<div id="submit-ratings-container" class="center">
		<button id="submit-employee-ratings" class="sqr-btn blue">Submit Ratings</button>
		<div class="error-message-container">
			<p class="error-message">Invalid Input</p>
		</div>
	</div>
	<div class="header v1">
		<div>
			<label>Job Name</label>
			<span class="show-job-info-mod" data-context="complete" data-job-id="${response.job.id }">
				${response.job.jobName }</span>
		</div>		
	</div>
	<div id="main-cont" class="item">
		<div id="employee-list">
			<div class="${response.employees.size() == 1 ? 'hide-on-load' : '' }">
				<h2>Employees</h2>	
				<div id="employees" class="circle-dot-list">
					<c:forEach items="${response.employees }" var="employee" varStatus="status">
						<div class="employee">
							<p data-user-id="${employee.userId }"
								data-name="${employee.firstName } ${employee.lastName }"
								class="list-item ${status.last ? 'last' :'' }">							
								${employee.firstName } ${employee.lastName }						
							</p>		
						</div>
					</c:forEach>		
				</div>	
			</div>
		</div>
		<div id="ratings-cont">
			<c:forEach items="${response.employees }" var="employee" varStatus="status">
				<div class="user-cont" data-user-id="${employee.userId}">
					<h2>Rate <span id="selected-employee">${employee.firstName} ${employee.lastName}</span></h2>	
					<div class="star-rating-cont">
						<input class="star-rating" name="" class="rating-loading"
							value="0" data-size="xs">
					</div>		
					<div class="rating-criteria-cont">
						<div class="on-time rate-criterion required" data-rate-criterion-id="2">
							<p>Did ${employee.firstName}'s timeliness meet your expectations?</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes" value="5">Yes</button>
								<button class="sqr-btn no">No</button>
							</div>
							<div class="if-no radio-container">
								<label><input type="radio" value="3" name="on-time-${employee.userId }">
									${employee.firstName} did not consistently arrive on time
								</label>
								<label><input type="radio" value="1" name="on-time-${employee.userId }">
									${employee.firstName} did not arrive at least one day and did not notify me
								</label>	
							</div>
						</div>
						<div class="work-ethic rate-criterion required" data-rate-criterion-id="1">
							<p>Did ${employee.firstName}'s work ethic meet your expectations</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes" value="5">Yes</button>
								<button class="sqr-btn no">No</button>
							</div>
							<div class="if-no radio-container ">
								<label><input type="radio" value="3" name="work-${employee.userId }">
									${employee.firstName}'s work ethic was OK, but I expected more
								</label>
								<label><input type="radio" value="1" name="work-${employee.userId }">
									${employee.firstName}'s work ethic was poor
								</label>							
							</div>
						</div>	
						<div class="experience rate-criterion required" data-rate-criterion-id="7">
							<p>Did ${employee.firstName}'s experience level meet your expectations?</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes" value="5">Yes</button>
								<button class="sqr-btn no">No</button>
							</div>
							<div class="if-no radio-container">
								<label><input type="radio" value="3" name="experience-${employee.userId }">
									${employee.firstName}'s experience level was OK, but I expected more
								</label>
								<label><input type="radio" value="1" name="experience-${employee.userId }">
									${employee.firstName}'s experience level was far less than I expected
								</label>							
							</div>
						</div>				
						<div class="hire-again rate-criterion required" data-rate-criterion-id="3">
							<p>Would you hire ${employee.firstName} again?</p>
							<div class="rating-answer button-group no-toggle">
								<button class="sqr-btn yes" value="5">Yes</button>
								<button class="sqr-btn no" value="1">No</button>
							</div>
						</div>	
						<div class="comment-cont">
							<p>Comment</p>
							<textarea class="comment" rows="4"></textarea>
						</div>													
					</div>		
				</div>
			</c:forEach>
			<c:if test="${response.employees.size() > 1 }">
				<p id="next-employee" class="linky-hover">Next Employee<span class="glyphicon glyphicon-menu-right"></span><span class="glyphicon glyphicon-menu-right"></span></p>
			</c:if>
		</div>
	</div>
</div>

<%@ include file="../includes/Footer.jsp"%>