<%@ include file="../includes/TagLibs.jsp"%>	

<div id="questionsContainer" class="section">
<c:choose>
	<c:when test="${jobDto.questions.size() > 0 }">
			<c:forEach items="${jobDto.questions }" var="question">
				<div class="question-container">
					<span class="question-text">${question.text }</span>
					<div class="answer-container">
						<c:choose>
							<c:when test="${question.formatId == 2 || question.formatId == 3}">
								<div class="answer-options-container">
								<c:forEach items="${question.answers }" var="answer">
									<div class="selected-answer-option-id">${answer.answerOptionId }</div>							
								</c:forEach>
								
								<c:forEach items="${question.answerOptions }" var="answerOption">
									<div class="answer-option" data-answer-option-id="${answerOption.answerOptionId }">${answerOption.text }</div>
								</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<c:forEach items="${question.answers }" var="answer">
									<div class="">${answer.text }</div>							
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:forEach>
		
			
	</c:when>
	<c:otherwise>
		<div>This job does not have questions.</div>
	</c:otherwise>
</c:choose>
</div>


<script type="text/javascript">

$(document).ready(function(){
	
	var answerOptionContainers = $("#questionsContainer").find(".answer-options-container");
	var selectedAnswerOptionIds = [];
	var answerOption;
	$.each(answerOptionContainers, function(){
		selectedAnswerOptionIds = $(this).find(".selected-answer-option-id");
		
		$.each(selectedAnswerOptionIds, function(){
			answerOption = $("#questionsContainer").find("[data-answer-option-id='" + $(this).html() + "']")[0];
			$(answerOption).addClass("selected");
		})
	})
	
})
</script>