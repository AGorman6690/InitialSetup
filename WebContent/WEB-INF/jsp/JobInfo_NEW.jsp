<%@ include file="./includes/TagLibs.jsp"%>	

<div class="wrapper ${sessionScope.user.profileId == 1 ? 'employee-context' : 'employer-context'}">
	<c:if test="${sessionScope.user.profileId == 1 }">
		<c:choose>
			<c:when test="${response.context == 'find' && !empty response.application}">						
				<div class="warning-message">	
					<h3>
					${response.application.status == 0 ||
						 response.application.status == 2 ||
						 response.application.status == 4 ? "Your application has been submitted" :
						 response.application.status == 1 ? "Your application has been declined" :
						 response.application.status == 5 ? "You have withdrawn your application" :
						 response.application.status == 6 ? "The employer filled all positions for this job. Your application will remain in the employer's inbox." :
						 response.application.status == -1 ? "The employer initiated contact with you" :
						 "Application has been accepted" }	
					</h3>	
				</div>						
			</c:when>
			<c:when test="${sessionScope.jobs_needRating.size() > 0 }">
				<p id="jobs-needing-rating-warning">Please rate your previous employer before applying to another job</p>
			</c:when>	
		</c:choose>
	</c:if>
	<c:if test="${empty sessionScope.user && response.context == 'find' }">
		<div class="warning-message">	
			<h3><a href="/JobSearch/login-signup?login=true">Please login to apply for a job</a></h3>
		</div>
	</c:if>
	<c:if test="${sessionScope.user.profileId == 1 &&
						 response.context == 'find' &&
						 empty response.application &&						 
						empty sessionScope.jobs_needRating}">
		<c:set var="doShowApplyButton" value="1"></c:set>
	</c:if>

	<div class="job-info center ${!empty doShowApplyButton ? 'show-apply-button' : '' }">
		<input id="jobId" type="hidden" value="${response.job.id }">
	
		<c:if test="${sessionScope.user.profileId == 1 }">
			<div id="error-messages">
				<p id="invalid-answers" class="invalid-message">Please answer the employer's questions</p>
				<p id="invalid-desired-wage-missing" class="invalid-message">Please enter a wage proposal</p>
				<p id="invalid-desired-wage-not-positive-number" class="invalid-message">Please enter a positive number for a wage proposal</p>
				<p id="invalid-work-days" class="invalid-message">Please select one or more work days</p>
			</div>
		</c:if>
		
		<c:if test="${doShowApplyButton == '1'}">
			<div id="apply-for-job-cont">
				<button id="apply-for-job" class="sqr-btn green">Apply for job</button>
				
			</div>
		</c:if>
		<c:if test="${context == 'preview-job-post'}">
			<div id="">
				<button id="submit-job-post" class="sqr-btn green">Submit job post</button>
				
			</div>
		</c:if>
		<c:if test="${response.isPreviewingBeforeSubmittingJobPost }">
			<div class="to-be-fixed-cont-disabled">
				<button id="submitPosting_final" class="sqr-btn green to-be-fixed-disalbed">Submit Job Post</button>
			</div>
		</c:if>
		<h2>${response.job.jobName }</h2>
		<c:if test="${sessionScope.user.profileId == 1 }">
			<div class="">
				<div class="ratings-mod-container">
				<p class="linky-hover employer show-ratings-mod user-rating"
					 data-user-id="${response.profileInfoDto.user.userId }">${response.profileInfoDto.user.firstName }
					 ${response.profileInfoDto.user.lastName }</p>
					<div class="mod simple-header">
						<div class="mod-content">
							<div class="mod-header"></div>
							<div class="mod-body">	
							</div>
						</div>
					</div>						 
				</div>
				<c:choose>
					<c:when test="${response.profileInfoDto.doesUserHaveEnoughDataToCalculateRating }">			
						<span data-toggle-id="user-rating-details-container">
							<input name="input-1" class="rating-loading"
								value="${response.profileInfoDto.profileRatingDto.overallRating }">
									${response.profileInfoDto.profileRatingDto.overallRating }
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
			<p>${response.job.description }</p>
		</div>		
		<c:if test="${response.skillsRequired.size() > 0 || response.skillsDesired.size() > 0 }">
			<div id="skills" class="sub">
				<h3>Skills</h3>
				<c:if test="${response.skillsRequired.size() > 0 }">
					<div>
						<h4>Required</h4>
						<ul>
							<c:forEach items="${response.skillsRequired }" var="skill">
								<li>${skill.text }</li>	
							</c:forEach>
						</ul>
					</div>		
				</c:if>							
				<c:if test="${response.skillsDesired.size() > 0 }">
					<div>
						<h4>Desired</h4>
						<ul>
							<c:forEach items="${response.skillsDesired }" var="skill">
								<li>${skill.text }</li>	
							</c:forEach>
						</ul>
					</div>			
				</c:if>
			</div>	
		</c:if>	
		<c:if test="${response.questions.size() > 0 }">		
			<div id="questions" class="sub">
				<h3>Questions</h3>
				<c:if test="${ response.context =='find'}">
					<p class="instructions employee-context">Please answer questions</p>
				</c:if>
				<c:forEach items="${response.questions }" var="question">
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
												<c:if test="${sessionScope.user.profileId == 1 && response.context == 'find'}">
													<input type="${question.formatId == 3 ? 'checkbox' : 'radio' }"
														name="answer-options-${question.questionId }"
														data-id="${answerOption.answerOptionId }"
														data-question-id="${question.questionId}">
												</c:if>
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
		<c:if test="${response.context=='find'}">
			<div id="desired-wage" class="sub">
				<h3>Wage Proposal</h3>
				<p class="instructions employee-context">Please enter a wage proposal ($ / hr)</p>
				<input type="text" class="requires-positive-number" />
			</div>		
		</c:if>
		<div  class="sub">		
			<h3 id="work-days-label">Work Days</h3>	
<!-- 			<p class="instructions employee-context">Please select the days you wish to work</p> -->
			
			<c:if test="${response.countWorkDays > 1 }">
				<p id="work-day-message">
					<c:choose>
						<c:when test="${response.job.isPartialAvailabilityAllowed }">
							<c:if test="${ response.context =='find'}">
								<p class="instructions employee-context">Please select the work days you want to work</p>
							</c:if>
							<p>This job allows partial availability.</p>
							${sessionScope.user.profileId == 1 ? 'You' : 'Applicant'} can apply for one or more days.
						</c:when>
						<c:otherwise>
							<p>This job does not allow partial availability.</p>
							${sessionScope.user.profileId == 1 ? 'You' : 'Applicant'} must apply for all days.
						</c:otherwise>
					</c:choose>
				</p> 
			</c:if>
			<div id="work-days-calendar-container" class="v2 calendar-container
				 hide-prev-next ${sessionScope.user.profileId == 2 ? 'preview-job-post hide-select-work-day read-only' : '' }
				 ${!response.job.isPartialAvailabilityAllowed ? 'read-only no-partial' : 'proposal-calendar' }
				 ${sessionScope.user.profileId == 1 && response.context=='profile' ? 'read-only hide-select-work-day' : ''}">
				<c:if test="${response.job.isPartialAvailabilityAllowed
					&& sessionScope.user.profileId == 1 
					&& response.context=='find' }">
					<button id="select-all-work-days">Select all work days</button>		
				</c:if>
				<div class="calendar" data-min-date=${response.date_firstWorkDay }
					 data-number-of-months="${response.monthSpan_allWorkDays }"></div>
			</div>		
		</div>
		<div id="map-section" class="sub">
			<h3 id="location-label">Location</h3>	
			<div id="map-container">
				<div id="job-address" class="linky-hover">
					<p class="accent">${response.job.streetAddress_formatted }</p>
					<p class="accent">${response.job.city_formatted}, ${response.job.state }</p>
					<p class="accent">${response.job.zipCode_formatted }</p>		
				</div>			
				<div id="job-info-map" class="map right-border corner" data-do-init="1"
					data-lat="${response.job.lat }" data-lng="${response.job.lng }"></div>
			</div>
		</div>	
	</div>
	<div id="json_work_day_dtos">${response.json_workDayDtos }</div>
	
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
	<a class="square-button-green" href="/JobSearch/job/${response.job.id }/update/status/1">Start Job (for debugging)</a>	
	<a href="/JobSearch/job/${response.job.id }/update/status/2"><button class="square-button">Mark Complete (for debugging)</button></a>								
</div>

