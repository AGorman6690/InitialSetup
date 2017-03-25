
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
	}
	
	function isInputValid(){
		
		var isValid = true;
		var $e;
		
		// Desired pay
		$e = $("#amount");
		if(isDesiredPayValid($e.val())) setValidCss($e);
		else{			
			isValid = false;
			setInvalidCss($e);			
		}
		
		// Work days
		if(doesApplicantNeedToSelectWorkDays()){
			var count_selectedWorkDays = $calendar_applicationWorkDays.find(".selected-work-day").size();
			var count_allWorkDays = $calendar_applicationWorkDays.find(".active111").size();			
			
			$e = $calendar_applicationWorkDays.find(".ui-datepicker-inline").eq(0);
			if(count_selectedWorkDays == 0){
				isValid = false;
				setInvalidCss($e);
			}else setValidCss($e);
		}
		
		// Answers
		$e = $("#invalidAnswers");
		if(areAnswersValid()) $e.hide();
		else{
			isValid = false;
			$e.show();			
		}
		
		return isValid;		
	}
	
	function isDesiredPayValid(desiredPay){
		
		var isValid = true;
		
		//Verify pay proposal		
		if(desiredPay == ""){
			showInvalidAmountMessage("Desired pay is required.");
			isValid = false;
		}else if($.isNumeric(desiredPay) == 0){
			showInvalidAmountMessage("Desired pay must be numeric.");
			isValid = false;
		}else if(desiredPay <= 0){
			showInvalidAmountMessage("Desired pay must be greater than 0.");
			isValid = false;
		}
		
		//Hide error message if input is valid
		if(isValid){
			$("#invalidAmount").hide();
			return true;
		} else return false;
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

		var invalidCount = 0;
		
		$(".question-container .answer-options-container").each(function(){			
			
			if($(this).find("input:checked").length > 0){
				setValidCss($(this).closest(".question-container"));
			}
			else{				
				setInvalidCss($(this).closest(".question-container"));
				invalidCount += 1;
			}
		})
		
		
		$(".question-container textarea").each(function(){			
			
			if($(this).val() != ""){
				setValidCss($(this).closest(".question-container"));
			}
			else{				
				setInvalidCss($(this).closest(".question-container"));
				invalidCount += 1;
			}
		})
		
		if(invalidCount > 0){
			return false;
		}
		else{
			return true;
		}
		
	}
	
	
	function getApplicationDto(){
		
		var applicationDto = {};
		
		applicationDto.jobId = $("#submitApplication").attr("data-job-id");
		applicationDto.employmentProposalDto = getEmploymentProposalDto();
		applicationDto.answers = getAnswers();		
	
		return applicationDto;	
	}

	
	function getEmploymentProposalDto(){
		
		var employmentProposalDto = {};
		employmentProposalDto.dateStrings_proposedDates = [];
		
		employmentProposalDto.amount = $("input#amount").val();
			
		if(doesApplicantNeedToSelectWorkDays){
			
			employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
										$("#apply-work-days-calendar-container .calendar"), "yy-mm-dd", "selected-work-day");
		}

		return employmentProposalDto;
	}
	
	function getAnswers(){
			
		var answers = [];
		var answer = {};
		
		$(".question-container .answer-option input:checked").each(function(){			
			answer = {};
			answer.questionId = $(this).attr("data-question-id");
			answer.answerOptionId = $(this).attr("data-id");
			
			answers.push(answer);
		})
		
		$(".question-container textarea").each(function(){			
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
				data : JSON.stringify(getApplicationDto()),
			}).done(function() {
				redirectToProfile();
			}).error(function() {
				redirectToProfile();
			});
		}
	}
	