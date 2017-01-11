<%@ include file="./includes/Header.jsp"%>


<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/questions.css" />

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>


</head>

<body>


	<div class="container">
	
		<div class="">${vtJobInfo }</div>


<c:choose>
	<c:when test="${questions.size() > 0 }">
		<div class="section">
			<div class="header2">
				<span data-toggle-id="questionsContainer">
					<span class="glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Questions and Answers</span>
				</span>
			</div>
			<div id="questionsContainer" class="section-body">
				<c:forEach items="${questions }" var="question">
					<div class="question-container">
						<span class="question-text">${question.text }</span>
						<div class="answer-container">
<!-- 							<div class="question-format"> -->
<%-- 							<c:choose> --%>
<%-- 								<c:when test="${question.formatId == 0}"> --%>
<!-- 									(Yes or No) -->
<%-- 								</c:when> --%>
<%-- 								<c:when test="${question.formatId == 1}"> --%>
<!-- 									(Short Answer) -->
<%-- 								</c:when> --%>
<%-- 								<c:when test="${question.formatId == 2}"> --%>
<!-- 									(Select One Answer) -->
<%-- 								</c:when> --%>
<%-- 								<c:when test="${question.formatId == 3}"> --%>
<!-- 									(Select One or Multiple Answers) -->
<%-- 								</c:when> --%>
<%-- 							</c:choose> --%>
<!-- 							</div> -->
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
			</div>			
		</div>			
	</c:when>
</c:choose>
	
	</div>
</body>


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

<%@ include file="./includes/Footer.jsp"%>