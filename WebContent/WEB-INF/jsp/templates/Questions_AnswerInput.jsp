<%@ include file="../includes/TagLibs.jsp"%>	

<c:set var="formatId" value="${param_question.formatId }" />
<c:set var="questionId" value="${param_question.questionId }" />

<div class="question" data-question-id="${questionId }" data-question-format-id="${formatId }">
	<div class="question-text">${param_question.text }</div>
	<div class="answer-container">
		<c:choose>
			<c:when test="${formatId == 0 }">
				<div class="radio">
					<label><input value="1" type="radio" name="yesNo-${questionId }">Yes</label>
				</div>
				<div class="radio">
					<label><input value="0" type="radio" name="yesNo-${questionId }">No</label>
				</div>
			</c:when>
			
			<c:when test="${formatId == 1 }">
				<textarea class="form-control" rows="3"></textarea>
			</c:when>
			
			<c:when test="${formatId == 2 || formatId == 3}">
				<div class="help">Select One</div>
				<c:forEach items="${param_question.answerOptions }" var="answerOption">
				
					<label>
						<input type="${formatId == 2 ? 'radio' : 'checkbox' }"
							name="answer-options-${param_question.questionId }">${answerOption.text }
					</label>				
				</c:forEach>
			</c:when>
		</c:choose>
	</div>

</div>
	