
//	var jobs = [];	
//	var jobCount = 0;
//	var questions = [];
//	var questionCount = 0;
//	var questionContainerIdPrefix = 'question-';
	
		
	$(document).ready(function() {

		
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
	