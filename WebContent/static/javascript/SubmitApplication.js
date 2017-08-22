$(document).ready(function() {

		$("body").on("click", "#select-all-work-days", function() {
			$(workDayDtos).each(function() {
				this.isProposed = "1";
			})
			$("#work-days-calendar-container .calendar").datepicker("refresh");
		})
		

		$("body").on("click", "#apply-for-job", function(){
			apply();
		})
				
	})

	function isInputValid(){
		
		var isValid = true;
		var $e;
		
		// Desired pay
		$e = $("#desired-wage input");
		if(isDesiredPayValid($e.val())) setValidCss($e);
		else{			
			isValid = false;
			setInvalidCss($e);			
		}
		
		// Work days		
		if($("#work-days-calendar-container").hasClass("proposal-calendar")){
			
			var $calendar = $("#work-days-calendar-container .calendar");
			var count_selectedWorkDays = $calendar.find(".is-proposed").size();
			var count_allWorkDays = $calendar.find(".job-work-day").size();			
			
			$e = $calendar.find(".ui-datepicker-inline").eq(0);
			if(count_selectedWorkDays == 0){
				isValid = false;
				setInvalidCss($e);
				$("#invalid-work-days").show();
			}else{
				setValidCss($e);
				$("#invalid-work-days").hide();
			}
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
		
		var $e_missing = $("#invalid-desired-wage-missing");
		var $e_not_positive_number = $("#invalid-desired-wage-not-positive-number");
		
		$e_missing.hide();
		$e_not_positive_number.hide();
		
		//Verify pay proposal		
		if(desiredPay == ""){
			$e_missing.show();
			isValid = false;
		}else if($.isNumeric(desiredPay) == 0 || desiredPay <= 0){
			$e_not_positive_number.show();
			isValid = false;
		}
		
		return isValid;
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
		var isValid = true;
		var invalidCount = 0;
		
		$(".question-container .answer-options-container").each(function(){				
			if($(this).find("input:checked").length > 0){
				setValidCss($(this).closest(".question-container"));
			}
			else{				
				setInvalidCss($(this).closest(".question-container"));
				isValid = false;
			}
		})
		$(".question-container textarea").each(function(){			
			
			if($(this).val() != ""){
				setValidCss($(this).closest(".question-container"));
			}
			else{				
				setInvalidCss($(this).closest(".question-container"));
				isValid = false;
			}
		})
		
		if(!isValid) $("#invalid-answers").show();
		else $("#invalid-answers").hide();
		
		return isValid;
	
	}	
	function getApplyForJobRequest(){		
		var request = {};
		request.proposedDates = [];
		request.jobId = $(".job-info #jobId").val();
		request.proposedWage = $("#desired-wage input").val();		
		request.proposedDates = getSelectedDates(
				$("#work-days-calendar-container .calendar"), "yy-mm-dd", "is-proposed");
		request.answers = getAnswers();
		return request;	
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
				url : '/JobSearch/apply',
				headers : getAjaxHeaders(),
				contentType : "application/json",
				data : JSON.stringify(getApplyForJobRequest()),
			}).done(function() {
				$("#job-info-mod").hide();
			});
		}
	}