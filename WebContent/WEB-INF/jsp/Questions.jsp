<%@ include file="../includes/TagLibs.jsp"%>	

<div id="questionsContainer">
	<c:forEach items="${param_questions }" var="question">
		<div class="question body-element">
			<div class="question-text">${question.text }</div>			
			<div class="answer-options-container">
				<c:forEach items="${question.answerOptions }" var="answerOption">
				<div class="answer-option">${answerOption.text }</div>
				</c:forEach>
			</div>
		</div>
	</c:forEach>	
</div>