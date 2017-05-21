<%@ include file="./includes/TagLibs.jsp"%>	

<c:if test="${sessionScope.user.profileId == 1 && context == 'find' && empty jobDto.application &&
				sessionScope.jobs_needRating.size() == 0}">
	<c:set var="doShowApplyButton" value="1"></c:set>
</c:if>
<div class="job-info center ${!empty doShowApplyButton ? 'show-apply-button' : '' }">
	<input id="jobId" type="hidden" value="${jobDto.job.id }">

	<c:if test="${sessionScope.user.profileId == 1 }">
		<div id="error-messages">
			<div id="invalid-desired-wage-missing" class="invalid-message">Please enter a desired wage</div>
			<div id="invalid-desired-wage-not-positive-number" class="invalid-message">Please enter a positive number for a desired wage</div>
			<div id="invalid-answers" class="invalid-message">Please answer the employer's questions</div>
			<div id="invalid-work-days" class="invalid-message">Please select one or more work days</div>
		</div>
	</c:if>
	<c:if test="${!empty doShowApplyButton }">
		<div id="apply-for-job-cont">
			<button id="apply-for-job" class="sqr-btn green">Apply for job</button>
		</div>
	</c:if>
	<h2>${jobDto.job.jobName }</h2>
	<c:if test="${sessionScope.user.profileId == 1 }">
		<div class="">
			<div class="ratings-mod-container">
			<p class="linky-hover employer show-ratings-mod" data-user-id="${userDto_ratings.user.userId }">${userDto_ratings.user.firstName }
				 ${userDto_ratings.user.lastName }</p>
				<div class="mod simple-header">
					<div class="mod-content">
						<div class="mod-header">
							<span class="glyphicon glyphicon-remove"></span>
						</div>
						<div class="mod-body">	
						</div>
					</div>
				</div>						 
			</div>
			<c:choose>
				<c:when test="${userHasEnoughRatingData }">			
					<span data-toggle-id="user-rating-details-container">
						<input name="input-1" class="rating-loading"
							value="${userDto_ratings.ratingValue_overall }">
								${userDto_ratings.ratingValue_overall }
						 <span class="glyphicon glyphicon-menu-down"></span>
					</span>	
					<div id="user-rating-details-container" class="hide-on-load">												
						<%@ include file="./ratings/RatingDetails.jsp" %>
					</div>																					
				</c:when>
				<c:otherwise>NA</c:otherwise>					
			</c:choose>
		</div>	
	</c:if>
	<div id="job-description" class="sub">
		<h3>Description</h3>
		<p>${jobDto.job.description }</p>
	</div>		
	<c:if test="${jobDto.skillsRequired.size() > 0 || jobDto.skillsDesired.size() > 0 }">
		<div id="skills" class="sub">
			<h3>Skills</h3>
			<c:if test="${jobDto.skillsRequired.size() > 0 }">
				<div>
					<h4>Required</h4>
					<ul>
						<c:forEach items="${jobDto.skillsRequired }" var="skill">
							<li>${skill.text }</li>	
						</c:forEach>
					</ul>
				</div>		
			</c:if>							
			<c:if test="${jobDto.skillsDesired.size() > 0 }">
				<div>
					<h4>Desired</h4>
					<ul>
						<c:forEach items="${jobDto.skillsDesired }" var="skill">
							<li>${skill.text }</li>	
						</c:forEach>
					</ul>
				</div>			
			</c:if>
		</div>	
	</c:if>	
	<c:if test="${jobDto.questions.size() > 0 }">		
		<div id="questions" class="sub">
			<h3>Questions</h3>
			<c:forEach items="${jobDto.questions }" var="question">
				<div class="question-container" data-question-id="${question.questionId }"
				 	data-question-format-id="${question.formatId }">
					<p>${question.text }</p>
					<div class="answer-container">						
						<c:choose>
							<c:when test="${question.formatId == 1 }">
								<div>
									<textarea data-question-id="${question.questionId }" rows="3"></textarea>
								</div>
							</c:when>							
							<c:when test="${question.formatId == 0 || question.formatId == 2 || question.formatId == 3}">
								<ul class="answer-options-container">
								<c:forEach items="${question.answerOptions }" var="answerOption">
									<li class="answer-option">
										<label>
											<input type="${question.formatId == 3 ? 'checkbox' : 'radio' }"
												name="answer-options-${question.questionId }"
												data-id="${answerOption.answerOptionId }"
												data-question-id="${question.questionId}">
												${answerOption.text }
										</label>
									</li>
								</c:forEach>
								</ul>
							</c:when>
						</c:choose>						
					</div>
				</div>
			</c:forEach>		
		</div>	
	</c:if>			
	<div id="desired-wage" class="sub">
		<h3>Desired Wage</h3>
		<input type="text" class="requires-positive-number" />
	</div>		
	<div  class="sub">		
		<h3 id="work-days-label">Work Days</h3>	
		<c:if test="${jobDto.workDayDtos.size() > 1 }">
			<p id="work-day-message">
				<c:choose>
					<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
						${sessionScope.user.profileId == 1 ? 'You' : 'Applicant'} can apply for one or more days
						
					</c:when>
					<c:otherwise>
						${sessionScope.user.profileId == 1 ? 'You' : 'Applicant'} must apply for all days
					</c:otherwise>
				</c:choose>
			</p> 
		</c:if>
		<div id="work-days-calendar-container" class="v2 hide-select-work-day calendar-container hide-prev-next
			 ${!jobDto.job.isPartialAvailabilityAllowed ? 'read-only' : 'proposal-calendar' }">
			<c:if test="${jobDto.job.isPartialAvailabilityAllowed }">
				<button id="select-all-work-days" class="sqr-btn gray-3">Select All</button>		
			</c:if>
			<div class="calendar" data-min-date=${jobDto.date_firstWorkDay }
				 data-number-of-months="${jobDto.months_workDaysSpan }"></div>
		</div>		
	</div>
	<div id="map-section" class="sub">
		<h3 id="location-label">Location</h3>	
		<div id="map-container">
			<div id="job-address" class="linky-hover">
				<p class="accent">${jobDto.job.streetAddress_formatted }</p>
				<p class="accent">${jobDto.job.city_formatted}, ${jobDto.job.state }</p>
				<p class="accent">${jobDto.job.zipCode_formatted }</p>		
			</div>			
			<div id="map" class="right-border corner" data-do-init="1"
				data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }"></div>
		</div>
	</div>	
</div>
<div id="json_work_day_dtos">${json_work_day_dtos }</div>

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<a class="square-button-green" href="/JobSearch/job/${jobDto.job.id }/update/status/1">Start Job (for debugging)</a>	
<a href="/JobSearch/job/${jobDto.job.id }/update/status/2"><button class="square-button">Mark Complete (for debugging)</button></a>								

