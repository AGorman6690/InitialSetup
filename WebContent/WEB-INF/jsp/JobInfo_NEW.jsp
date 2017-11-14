<%@ include file="./includes/TagLibs.jsp"%>	

<div class="job-info wrapper ${response.isEmployee ? 'employee-context' : 'employer-context'}
			${ response.context =='find' ? 'find-context': ''}">
	<c:if test="${response.isEmployee }">
		<c:choose>
			<c:when test="${response.context == 'find' && !empty response.warningMessage}">						
				<div class="warning-message">	
					<span>${response.warningMessage }</span>	
				</div>						
			</c:when>
			<c:when test="${sessionScope.jobs_needRating.size() > 0 }">
				<div class="warning-message">	
					<span>Please rate your previous employer before applying to another job</span>
				</div>
			</c:when>	
		</c:choose>
	</c:if>
	<c:if test="${!response.isLoggedIn && response.context == 'find' }">
		<div class="warning-message">	
			<h3><span class="show-login-sign-up-mod linky-hover" data-context="login">Please login to apply for a job</span></h3>
		</div>
	</c:if>
	<div class=" ${response.isApplyable ? 'show-apply-button' : '' }">
		<input id="jobId" type="hidden" value="${response.job.id }">
			<div>
				<div class="">
					<c:if test="${response.isEmployee }">
						<div id="submit-application-error">
							<div class="invalid-input-message">
								<p>Invalid input</p>
							</div>
						</div>
					</c:if>			
					<c:if test="${response.isApplyable }">
						<c:set var="thisIsAHack" value="1" />
						<div id="apply-for-job-cont" class="fix-content-container">
							<button id="apply-for-job" class="sqr-btn green">Apply for job</button>					
						</div>
					</c:if>
					<c:if test="${context == 'preview-job-post'}">
						<c:set var="thisIsAHack" value="1" />
						<div id="submit-job-info-container" class="fix-content-container center">
							<button id="edit-job-post" class="sqr-btn gray">Edit</button>
							<button id="submit-job-post" class="sqr-btn blue">Submit job post</button>					
						</div>
					</c:if>
				</div>

			</div>
		</div>
		<div class="header-wrap ${!empty thisIsAHack ? 'top-margin' : '' }">
			<div class="header-item">
				<label>Job Name</label>
				<div>${response.job.jobName }</div>
			</div>
			<c:if test="${response.isEmployee }">
				<div class="header-item">
					<label>Employer Name</label>
					<div>
						<div class="ratings-mod-container">
							<span class="linky-hover employer show-ratings-mod user-rating"
								 data-user-id="${response.profileInfoDto.user.userId }">${response.profileInfoDto.user.firstName }
								 ${response.profileInfoDto.user.lastName }</span>
							<div class="mod simple-header">
								<div class="mod-content">
									<div class="mod-header"></div>
									<div class="mod-body">	
									</div>
								</div>
							</div>						 
						</div>
					</div>
				</div>
				<div class="header-item">
					<label>Employer Rating</label>
					<div>
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
				</div>	
			</c:if>				
		</div>		
		<div id="job-description" class="sub">
			<label>Description</label>
			<div class="job-info-content">
				<p>${response.job.description }</p>
			</div>
		</div>		
		<c:if test="${response.skillsRequired.size() > 0 || response.skillsDesired.size() > 0 }">
			<div id="skills" class="sub">
				<label>Skills</label>
				<div class="job-info-content">
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
			</div>	
		</c:if>	
		<c:if test="${response.questions.size() > 0 }">		
			<div id="questions" class="sub validate-input">
				<label>Questions</label>
				<div class="job-info-content">
					<c:if test="${ response.context =='find'}">
						<p class="instructions employee-context">Please answer questions</p>
					</c:if>
					<c:forEach items="${response.questions }" var="question">
						<div class="question-container ${question.formatId == 3 ? 'checkbox-container' : 'radio-container' }"
							data-question-id="${question.questionId }"
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
													<c:if test="${response.isEmployee && response.context == 'find'}">
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
			</div>	
		</c:if>			
		<c:if test="${response.context=='find'}">
			<div id="desired-wage" class="sub validate-input">
				<label>Wage Proposal</label>
				<div class="job-info-content">
					<p class="instructions employee-context">Please enter a wage proposal ($ / hr)</p>
					<input type="text" data-greater-than="0"/>
				</div>
			</div>		
		</c:if>
		<div  class="sub">		
			<label id="work-days-label">Work Days</label>	
<!-- 			<p class="instructions employee-context">Please select the days you wish to work</p> -->
			<div class="job-info-content">
				<c:if test="${response.countWorkDays > 1 }">
					<p id="work-day-message">
						<c:choose>
							<c:when test="${response.job.isPartialAvailabilityAllowed }">
								<c:if test="${ response.context =='find'}">
									<p class="instructions employee-context">Please select the work days you want to work</p>
								</c:if>
								<p>This job allows partial availability.</p>
								<p>${response.isEmployee? 'You' : 'Applicant'} can apply for one or more days.</p>
							</c:when>
							<c:otherwise>
								<p>This job does not allow partial availability.</p>
								<p>${response.isEmployee ? 'You' : 'Applicant'} must apply for all days.</p>
							</c:otherwise>
						</c:choose>
					</p> 
				</c:if>
				<div id="work-days-calendar-container" class="v2 validate-input calendar-container no-pad stack hide-unused-rows
					 hide-prev-next ${!response.isEmployee ? 'preview-job-post hide-select-work-day read-only' : '' }
					 ${!response.job.isPartialAvailabilityAllowed ? 'read-only no-partial' : 'proposal-calendar requires-validation' }
					 ${response.isEmployee && response.context=='profile' ? 'read-only hide-select-work-day' : ''}">
					<c:if test="${response.job.isPartialAvailabilityAllowed
						&& response.isEmployee 
						&& response.context == 'find'
						&& response.countWorkDays > 1 }">
						<button id="select-all-work-days">Select all work days</button>		
					</c:if>
					<div class="calendar" data-min-date=${response.date_firstWorkDay }
						 data-number-of-months="${response.monthSpan_allWorkDays }"
						 data-selected-class-name="is-proposed"></div>
				</div>	
			</div>	
		</div>
		<div id="map-section" class="sub">
			<label id="location-label">Location</label>	
			<div id="map-container" class="job-info-content">
				<div id="job-address" class="linky-hover">
					<p class="">${response.job.streetAddress_formatted }</p>
					<p class="">${response.job.city_formatted}, ${response.job.state }</p>
					<p class="">${response.job.zipCode_formatted }</p>		
				</div>			
				<div id="job-info-map" class="map right-border corner" data-do-init="1"
					data-lat="${response.job.lat }" data-lng="${response.job.lng }"></div>
			</div>
		</div>	
	</div>
	<div id="json_work_day_dtos">${response.json_workDayDtos }</div>

<div id="application-success-container">
	<p id="submit-application-success">Your application has been received!</p>
	<p id="submit-application-fail">Oh no. Something went wrong. Your application was not received.</p>
	<div>
		<a id="continue-searching" class="sqr-btn">Continue searching for jobs</a>
		<a class="sqr-btn" href="/JobSearch/user">Back to profile</a>
	</div>
</div>

	
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
<%-- 	<a class="square-button-green" href="/JobSearch/job/${response.job.id }/update/status/1">Start Job (for debugging)</a>	 --%>
	<a href="/JobSearch/job/${response.job.id }/update/status/2"><button class="square-button">Mark Complete (for debugging)</button></a>								

