
//	var jobs = [];	
//	var jobCount = 0;
//	var questions = [];
//	var questionCount = 0;
//	var questionContainerIdPrefix = 'question-';
	
		
	$(document).ready(function() {

		
		$(".single").click(function(){
			var container = $(this).closest(".answer-container");
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
		var questions = $("#answersContainer").find(".answer-container")
		var invalidCount = 0;
		var answer;
		
		var radioInput;
		var textareaInput;
		var answerOptionInput;
		
		
		$("#answersContainer").find(".question-container .answer-options-container").each(function(){			
			
			if($(this).find("input:checked").length > 0){
				setValidCss($(this).closest(".question-container"));
			}
			else{				
				setInvalidCss($(this).closest(".question-container"));
				invalidCount += 1;
			}
		})
		
		
		$("#answersContainer").find("textarea").each(function(){			
			
			if($(this).val() != ""){
				setValidCss($(this).closest(".question-container"));
			}
			else{				
				setInvalidCss($(this).closest(".question-container"));
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
	
	
	function getApplicationDTO(){
		
		var applicationDto = {};
		
		applicationDto.jobId = $("#jobId").val();
		applicationDto.employmentProposalDto = getEmploymentProposalDto();
		applicationDto.answers = getAnswers();		
	
		return applicationDto;	
	}

	
	function getEmploymentProposalDto(){
		
		var employmentProposalDto = {};
		employmentProposalDto.dateStrings_proposedDates = [];
		
		employmentProposalDto.amount = $("#amount").val();
			
		if(isCalendarInDOM_applicantSelectWorkDays){
			
			employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
										$("#apply_selectWorkDays"), "yy-mm-dd", "apply-selected-work-day");
		}

		return employmentProposalDto;
	}
	
	function getAnswers(){
		
		var selectedAnswers = [];
		var selectedAnswer;		
		var answers = [];
		var answer = {};
		var questions = $("#questions").find(".question-container");
		var questionId;
		var questionFormatId
		
		$("#answersContainer").find(".answer-option input:checked").each(function(){
			
			answer = {};
			answer.questionId = $(this).attr("data-question-id");
			answer.answerOptionId = $(this).attr("data-id");
			
			answers.push(answer);
			
			
		})
		
		$("#answersContainer").find("textarea").each(function(){
			
			answer = {};
			answer.questionId = $(this).attr("data-question-id");
			answer.text = $(this).val();
			
			answers.push(answer);			
			
		})
		
		return answers;		
	}
	
	function apply(){

		if(isInputValid()){

			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + '/JobSearch/apply',
				headers : getAjaxHeaders(),
				contentType : "application/json",
				data : JSON.stringify(getApplicationDTO()),
			}).done(function() {
				redirectToProfile();
			}).error(function() {
				redirectToProfile();
			});
		}
	}
	