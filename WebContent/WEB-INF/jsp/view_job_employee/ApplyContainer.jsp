<%@ include file="../includes/TagLibs.jsp" %>

<div class="section-body">
	<h4>Apply</h4>
		<a id="notLoggedIn-ApplicationWarning"
		class="${!isLoggedIn ? 'show-warning' : ''}"
		href="/JobSearch/login-signup?login=true">
		You must be logged in to apply for a job.
			</a>	
	<div class="body-element-container ${!isLoggedIn ? 'not-logged-in' : '' }">

		<div id="submitApplicationContainer">
			<a id="submitApplication" class="accent">Submit</a>
		</div>														
	
		<div class="info-container">
			<div class="info-label">Desired Pay Per Hour</div>
			<div class="info-value">
				<input class="form-control" placeholder="" id="amount">								
			</div>
		</div>
		
	<c:if test="${jobDto.job.isPartialAvailabilityAllowed == true }">
		<div class="info-container">
					<div class="info-label">Work Days You Are Available</div>
			<div class="info-value">
				<div class="calendar-container">
					<div id="apply_selectWorkDays" class="calendar"
						data-min-date="${jobDto.date_firstWorkDay }"
						data-number-of-months="${jobDto.months_workDaysSpan }"></div>
				</div>							
			</div>
		</div>
	</c:if>
			
	<c:if test="${jobDto.questions.size() > 0 }">			
		<div id="questions" class="body-element-container info-container">
			<div class="info-label">Questions</div>
			<div id="answersContainer" class="info-value">									
				<c:forEach items="${jobDto.questions }" var="question">
					<div class="question-container" data-question-id="${question.questionId }"
					 	data-question-format-id="${question.formatId }">
						${question.text }
						<div class="answer-container">
							
							<c:choose>
								<c:when test="${question.formatId == 1 }">
									<div>
										<textarea data-question-id="${question.questionId }" rows="3"></textarea>
									</div>
								</c:when>
								
								<c:when test="${question.formatId == 0 || question.formatId == 2 || question.formatId == 3}">
									<div class="answer-options-container">
									<c:forEach items="${question.answerOptions }" var="answerOption">
										<div class="answer-option">
											<label>
												<input type="${question.formatId == 3 ? 'checkbox' : 'radio' }"
													name="answer-options-${question.questionId }"
													data-id="${answerOption.answerOptionId }"
													data-question-id="${question.questionId}">
													${answerOption.text }
											</label>
										</div>
									</c:forEach>
									</div>
								</c:when>
							</c:choose>
							
						</div>
					</div>
				</c:forEach>												
			</div>
		</div>	
	</c:if>	
	</div>							
</div>