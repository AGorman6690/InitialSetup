<%@ include file="./includes/Header.jsp"%>


	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js"/>"></script>
	<script src="<c:url value="/static/javascript/SideBar.js"/>"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
	<script src="<c:url value="/static/javascript/Map.js" />"></script>

	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/categories.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob2.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/calendar.css " />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/datepicker.css " />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/map.css" />
		
	
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
	
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
</head>


<body>
<!-- ***************************************************************** -->
<!-- NOTE: Allow user to add job to favorites -->
<!-- ***************************************************************** -->

		
		<div class="container">
			<div class="row">
				<div id="sideBarContainer" class="col-sm-2">
					<div id="jobInfo" class="first side-bar selected-blue" data-section-id="jobInfoContainer">Job Information</div>
<!-- 					<div id="questions" class="side-bar" data-section-id="questionsContainer">Questions</div> -->
					<div id="apply" class="side-bar" data-section-id="applyContainer">Apply</div>					
				</div>
				
				<div class="col-sm-10" id="sectionContainers">
					<div id="jobInfoContainer" class="section-container">
						<div class="section-body">
							<h4>Job Information</h4>
							<div class="body-element-container">
								<%@include file="./templates/JobInformation.jsp"%>
							</div>
						</div>
					</div>

<!-- 					<div id="questionsContainer" class="section-container"> -->
<!-- 						<div class="section-body"> -->
<!-- 							<h4>Questions</h4> -->
							
<%-- 							<c:set var="param_questions" value="${jobDto.questions }"/>		 --%>
<!-- 							<div id="questionsContainer" class="body-element-container">					 -->
<%-- 								<c:forEach items="${param_questions }" var="question"> --%>
<!-- 									<div class="question body-element info-container"> -->
<%-- 										<div class="question-text">${question.text }</div>			 --%>
<!-- 										<div class="answer-options-container"> -->
<%-- 											<c:forEach items="${question.answerOptions }" var="answerOption"> --%>
<%-- 											<div class="answer-option"><span class="glyphicon glyphicon-menu-right"></span>${answerOption.text }</div> --%>
<%-- 											</c:forEach> --%>
<!-- 										</div> -->
<!-- 									</div> -->
<%-- 								</c:forEach>	 --%>
<!-- 							</div> -->
										
<!-- 						</div> -->
<!-- 					</div> -->
					
					<c:choose>				
						<c:when test="${isLoggedIn == false }">					
							<c:set var="notLoggedInClass" value="not-logged-in"> </c:set>		
						</c:when>
						<c:otherwise>
							<c:set var="notLoggedInClass" value=""> </c:set>
						</c:otherwise>				
					</c:choose>
			
					<div id="applyContainer" class="section-container ${notLoggedInClass }">
						<div class="section-body">
							<h4>Apply</h4>
							<div class="body-element-container">
							
								<c:if test="${isLoggedIn == false }">
									<div id="notLoggedIn-ApplicationWarning">
										You must be logged in to apply for a job.
									</div>
								</c:if>			
								<div id="submitApplicationContainer">
									<a id="submitApplication" class="accent">Submit</a>
								</div>														
							
								<div class="info-container">
									<div class="info-label">Desired Pay Per Hour</div>
									<div class="info-value">
										<input class="form-control" placeholder="" id="amount">								
									</div>
								</div>
											
								<div id="questions" class="body-element-container info-container">
									<div class="info-label">Answers</div>
									<div id="answersContainer" class="info-value">									
										<c:forEach items="${jobDto.questions }" var="param_question">
											<%@include file="./templates/Questions_AnswerInput.jsp"%>
										</c:forEach>																	
									</div>
								</div>		
							</div>								
						</div>
					</div>
					
				</div> <!-- close sections container -->
			</div>
	
		<input type="hidden" id="jobId" value="${jobDto.job.id }">
		</div>

<script>

	var pageContext = "postJob";
	var jobs = [];	
	var jobCount = 0;
	var questions = [];
	var questionCount = 0;
	var questionContainerIdPrefix = 'question-';
	
		

	$(document).ready(function() {
		
		
		$("#jobAddress").click(function(){
			var lat = $("#map").attr("data-lat");
			var lng = $("#map").attr("data-lng");
			var win = window.open("https://www.google.com/maps/preview/@" + lat + "," + lng + ",15z", "_blank");
			win.focus();
		})
		
		
		setAllDatesAsUnselectable($("#calendar"), true);

		

		var $calendar = $("#workDaysCalendar")
		
		

		var firstDate;
		$.each($("#workDays").find("[data-date]"), function(i){

			var workDay_date = $(this).attr("data-date");
			var date = $.datepicker.parseDate("yy-mm-dd", workDay_date)
			
			if(i == 0) firstDate = date;
			
			selectedDays.push(date.getTime());
		})
		
		initReadOnlyCalendar($calendar, firstDate);		
		$calendar.datepicker("setDate", firstDate);
		
		$(".single").click(function(){
			var container = $(this).closest(".answer");
			var answers = $(container).find(".single")
			highlightArrayItemByAttribute(this, answers, "selected");
		})
		
				
		$(".multi").click(function(){
			toggleClass($(this), "selected");
		})

		$("#submitApplication").click(function(){
			apply();
		})

				
	})
	
	function showInvalidAmountMessage(message){
		var $e = $("#invalidAmount");
		$e.html(message);
		$e.show();
		return 1;
	}
	
	function isInputValid(){
		
		var isValid = 1;
		var errorMessage;
		var desiredPayInput = $("#amount")
		
		//Desired pay
		if(isDesiredPayValid() == 0) {
			isValid = 0;
			setInvalidCss($(desiredPayInput));
		}
		else{
			setValidCss($(desiredPayInput));
		}
		
		//Answers
		errorMessage = $("#invalidAnswers");
		if(areAnswersValid() == 0){
			isValid = 0;
			$(errorMessage).show();
		}
		else{
			$(errorMessage).hide();
		}
		
		
		return isValid;
		
	}
	
	function isDesiredPayValid(){
		
		var invalidCount = 0;
		var amount = $("#amount").val();
		
		//Verify pay proposal		
		if(amount == ""){
			invalidCount = showInvalidAmountMessage("Desired pay is required.");
		}else if($.isNumeric(amount) == 0){
			invalidCount = showInvalidAmountMessage("Desired pay must be numeric.");
		}else if(amount <= 0){
			invalidCount = showInvalidAmountMessage("Desired pay must be greater than 0.");
		}
		
		//Hide error message if input is valid
		if(invalidCount == 0){
			$("#invalidAmount").hide();
			return 1;
		}
		else{
			return 0;
		}
	}
	

	
	function isValidInput(value){
		if(value == "" || value === null || value === undefined){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	function areAnswersValid(){
		
		var questionFormatId;
		var answerContainers = $("#answersContainer").find(".answer-container")
		var invalidCount = 0;
		var answer;
		
		var radioInput;
		var textareaInput;
		var answerOptionInput;
		
		
		$.each(answerContainers, function(){
			
			//All possible answer inputs
			radioInput = $(this).find("input[type=radio]:checked").parents('label').text();
			textareaInput = $(this).find("textarea").val();
			answerOptionInput = $(this).find(".answer-option.selected")[0];
			
			//If at least on possible input is valid
			if(isValidInput(radioInput) || isValidInput(textareaInput) || isValidInput(answerOptionInput)){
				setValidCss($(this));
			}
			else{				
				setInvalidCss($(this));
				invalidCount += 1;
			}
			
		})
		
		if(invalidCount > 0){
			return 0;
		}
		else{
			return 1;
		}
		
	}
	
	
	function getApplicationRequestDTO(){
		
		var jobId = $("#jobId").val();
		var dto = {};
		
		dto.jobId = jobId;
		
		dto.wageProposal = {};
		dto.wageProposal = getWageProposal();

		dto.answers = [];
		dto.answers = getAnswers();
	
		return dto;
	
	};
	
	function getWageProposal(){
		
		var wageProposal = {}
		wageProposal.amount = $("#amount").val();
		wageProposal.status = -1;
		
		return wageProposal;
	}
	
	function getAnswers(){
		
		var selectedAnswers = [];
		var selectedAnswer;		
		var answers = [];
		var answer = {};
		var questions = $("#questions").find(".question");
		var questionId;
		var questionFormatId
		
		$.each(questions, function(){	

			questionId = $(this).attr("data-question-id");
			questionFormatId = $(this).attr("data-question-format-id");
			
			answer = {};
			answer.questionId = questionId;
			
			if(questionFormatId == 0){
				answer.text = $(this).find("input[type=radio]:checked").parents('label').text();
				answers.push(answer);
			}
			else if(questionFormatId == 1){
				answer.text = $(this).find("textarea").val();
				answers.push(answer);
			}
			else if(questionFormatId == 2){
				selectedAnswer = $(this).find(".answer-option.selected")[0];
				answer.answerOptionId = $(selectedAnswer).attr("data-answer-option-id")
				answers.push(answer);
			}
			else if(questionFormatId == 3){
				selectedAnswers = $(this).find(".answer-option.selected"); 
				$.each(selectedAnswers, function(){
					answer = {};
					answer.questionId = questionId;
					answer.answerOptionId = $(this).attr("data-answer-option-id");
					
					answers.push(answer);
				})
			}	
		})
		
		return answers;		
	}
	
	function apply(){
		
		var applicationRequestDTO = {};
		var headers = {};
		
		if(isInputValid()){
			
			//Get dto			
			applicationRequestDTO = getApplicationRequestDTO();
			
			//Submit the apppliation			
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + '/JobSearch/job/apply',
				headers : headers,
				contentType : "application/json",
				data : JSON.stringify(applicationRequestDTO),
			}).done(function() {
				$('#home')[0].click();
			}).error(function() {
				$('#home')[0].click();
			});
		}
	}
	


// 	function initMap(){
// 		var lat = $("#jobLat").val();
// 		var lng = $("#jobLng").val();
// 		var map =  initializeMap("map", lat, lng);
// 		showMapMarker(map, lat, lng);

// 	}

		
// 	function setPopovers(){

// 		$('.popover1').popover({
// 			trigger: "hover",
// 			delay: {
// 				show: "250",		
// 			}
// 		});
		
// 		$('.popoverInstant').popover({
// 			trigger: "hover",
// 			delay: {
// 				show: "0",		
// 			}
// 		});	
		
// 	}
		

</script>

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap">
</script>


<%@include file="./includes/Footer.jsp"%></body>