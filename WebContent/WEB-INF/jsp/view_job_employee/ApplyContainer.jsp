<%@ include file="../includes/TagLibs.jsp" %>


<a id="not-logged-in-warning"	class="${!isLoggedIn ? 'show-warning' : ''}"
	href="/JobSearch/login-signup?login=true">You must be logged in to apply for a job
</a>	


<div class="apply-action">
		<p>Propose a desired hourly wage</p>
		<input id="amount" class="form-control">		
</div>
<c:if test="${jobDto.job.isPartialAvailabilityAllowed == true }">	
	<div class="apply-action">
			<p>Propose the days you can work</p>
			<button class="sqr-btn gray-2">Select All</button>		
			<div id="apply-work-days-calendar-container" class="pad-top calendar-container job-info-calendar hide-prev-next">
				<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
				<div class="work-days">
					<c:forEach items="${jobDto.workDays }" var="workDay">
						<div data-date="${workDay.stringDate }"></div>
					</c:forEach>
				</div>
			</div>					
	</div>	
</c:if>		
<c:if test="${jobDto.questions.size() > 0 }">		
	<div class="apply-action">
		<p>Answer the employer's ${jobDto.questions.size() > 1 ? 'questions' : 'question' }</p>
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
<div id="submit-application-container">
	<button id="submitApplication" class="sqr-btn teal" data-job-id="${jobDto.job.id }">Submit Application</button>
			
</div>	

	