<%@ include file="../includes/TagLibs.jsp" %>			

<c:choose>
	<c:when test="${jobDto.questions.size() > 0 }">		

			<c:forEach items="${jobDto.questions }" var="question">
				<div class="question-container">
					${question.text }
					<div class="answer-container">
						<div class="question-format">
						<c:choose>
							<c:when test="${question.formatId == 0}">
								(Yes or No)
							</c:when>
							<c:when test="${question.formatId == 1}">
								(Short Answer)
							</c:when>
							<c:when test="${question.formatId == 2}">
								(Select One Answer)
							</c:when>
							<c:when test="${question.formatId == 3}">
								(Select One or Multiple Answers)
							</c:when>
						</c:choose>
						</div>
						<c:choose>
							<c:when test="${question.formatId == 2 || question.formatId == 3}">
								<div class="answer-options-container">
								<c:forEach items="${question.answerOptions }" var="answerOption">
									<div>${answerOption.text }</div>
								</c:forEach>
								</div>
							</c:when>
						</c:choose>
					</div>
				</div>
			</c:forEach>
						
	</c:when>
	<c:otherwise>
		<div>This job does not have any questions.</div>
	</c:otherwise>
</c:choose>