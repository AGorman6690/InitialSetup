<%@ include file="../includes/TagLibs.jsp" %>


<c:choose>
	<c:when test="${sessionScope.jobs_needRating.size() > 0 }">
		<div class="center pad-top-2">
			<p>You have completed a job that requires your rating.</p>
			<p>Please rate your past employer before applying to another job.</p>
		</div>
	</c:when>
	<c:otherwise>
		<div class="pad-top-2">
			<a id="not-logged-in-warning"	class="${!isLoggedIn ? 'show-warning' : ''}"
				href="/JobSearch/login-signup?login=true">You must be logged in to apply for a job
			</a>	
			
			<input id="isWorkDaySelectedRequired" type="hidden" value="${jobDto.job.isPartialAvailabilityAllowed }">
			<div class="apply-action">
					<p>Propose a desired hourly wage</p>
					<input id="amount" class="form-control">		
			</div>
			<c:if test="${jobDto.job.isPartialAvailabilityAllowed == true }">	
				<div class="apply-action">
						<p>Propose the days you can work</p>
						<button id="select-all-work-days" class="sqr-btn gray-2">Select All</button>		
						<div id="apply-work-days-calendar-container" class="v2 proposal-calendar pad-top calendar-container hide-prev-next">
							<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
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
		</div>
	</c:otherwise>
</c:choose>
	