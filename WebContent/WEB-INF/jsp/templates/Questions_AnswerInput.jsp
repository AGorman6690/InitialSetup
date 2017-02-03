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
			
			<c:when test="${formatId == 2 || formatId == 3 }">
	
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${formatId == 2 }"> --%>
<%-- 						<c:set var="classToAdd" value="single"/> --%>
<%-- 						<c:set var="help" value="Select One"/> --%>
<%-- 					</c:when> --%>
<%-- 					<c:otherwise> --%>
<%-- 						<c:set var="classToAdd" value="multi"/> --%>
<%-- 						<c:set var="help" value="Select One or More"/> --%>
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose> --%>
					<div class="help"> ${formatId == 2 ? 'Select One' : 'Select One or More' }</div>
					<c:forEach items="${param_question.answerOptions }" var="answerOption">
						<div class="${formatId == 2 ? 'single' : 'multi' }  answer-option" 
							data-answer-option-id="${answerOption.answerOptionId }">${answerOption.text }</div>
					</c:forEach>
			</c:when>
		</c:choose>
	</div>

</div>
	