
$(document).ready(function(){
	
	$("body").on("change", ".invalid", function(){
		
		var value = "";
		if($(this).id == "times"){
			validateTimes();	
		}else if($(this).is("input") || $(this).is("textarea")){
			value = $(this).val()
		}else if($(this).is("select")){
			value = $(this).find(":selected").val();
		}
		
		validateInput($(this), value);

	})
	
	$("body").on("change", ".invalid.invalid-positive-number", function(){
		
		var value = "";
		if($(this).id == "times"){
			validateTimes();	
		}else if($(this).is("input") || $(this).is("textarea")){
			value = $(this).val()
		}else if($(this).is("select")){
			value = $(this).find(":selected").val();
		}
		
//		validatePositiveNumber($(this), value);

	})

})

function isValidatePositiveNumber(value){
	
	var isValid = 1;
	if($.isNumeric(value) == 0){
		isValid = 0;
	}
	else if(value < 0){
		isValid = 0;
	}
	
//	if(isValid){
//		setValidCss($e);
//		hideErrorMessage($e);	
//	}else{
//		setInvalidCss($e);
//	}
	
	return isValid;
}


function validateInput($e, value){
	if(value == ""){
		setInvalidCss($e);
		return 1;
	}else{
		setValidCss($e);
		hideErrorMessage($e);
		return 0;
	}

}

function hideErrorMessage($eInput){
	var errorMessage = $("body").find("[data-message-for='" + $eInput.attr("id") + "']")[0];
	
	hide($(errorMessage));
}




function validateInputExistence($e, value){
	if(value == ""){
		setInvalidCss($e);
		return 1;
	}else{
		setValidCss($e);
		return 0;
	}

}

function isValidNumberGreaterThan0(value){
	
	var result = 1;
	if($.isNumeric(value) == 0){
		result = 0;
	}else if(value <= 0){
		result = 0
	}
	
	return result;
}

function validateSelectInput($e, value){
	
	if(value < 0 || value == ""){
		setInvalidCss($e);
		return 1;
	}else{
		setValidCss($e);
		return 0;
	}

}

function validateInputValueContains($e, value, valueCannotContain){
	if(value.indexOf(valueCannotContain) > -1){
		setInvalidCss($e);
		return 1;
	}else{
		setValidCss($e);
		return 0;
	}
}

function validateJobName(requestedJobName, jobs){
	//Job names must be unique

	var $eInvalidMessage = $("#invalidJobName");
	var $eJobName = $(document.getElementsByName('name')[0]);
	var invalid = 0;
	
	//Search each job's name
	$.each(jobs, function(){
		if(this.jobName == requestedJobName){
			invalid = 1;
		}
	})
	
	if(invalid){
		//Show invalid message
		$eInvalidMessage.show();
		
		//Format input as invalid 
		setInvalidCss($eJobName);	
		
		return 1;
	}else{
		
		//Hide invalid message
		$eInvalidMessage.hide();
		
		//Format input as valid 
		setValidCss($eJobName);	
		
		return 0
	}

}

function setInvalidCss($e){

	if($e.hasClass("invalid") == 0){
		$e.addClass("invalid");
	}	
}

function validateDates(){
	var selectedDates = $("#times").find(".time");
	var $e = $("#calendar");
	if(selectedDates.length == 0){
		setInvalidCss($e);
		return 1;
		
	}else{
		setValidCss($e);
		return 0;
	}
}

function validateTimes(){
	var times = $("#times").find(".time-container input");
	var result = 0;
	if(times.length > 0){
		$.each(times, function(){
			result += validateInput($(this), $(this).val())
		})
		
	}
	
	return result;
}

//function setInvalidSelectCss($e){
//	if($e.hasClass("invalid-select-input") == 0){
//		$e.addClass("invalid-select-input");
//	}	
//}
function setValidCss($e){
	if($e.hasClass("invalid") == 1){
		$e.removeClass("invalid");
	}	
}

//function setValidSelectCss($e){
//	if($e.hasClass("invalid") == 1){
//		$e.removeClass("invalid");
//	}	
//}

function validatePostJobInputs(jobs){

	var $e;
	var result = 0;
	
	//Validate job name is provided
	$e = $(document.getElementsByName('name')[0]);
	result += validateInputExistence($e, $e.val());
	
	//Verify the job name is unique only if the job name is provided
	if(result == 0){
		result += validateJobName(document.getElementsByName('name')[0].value, jobs)	
	}
		
	//Validate job description is provided	
	$e = $(document.getElementsByName('description')[0]);
	result += validateInput($e, $e.val());

	//Validate street address is provided
	$e = $(document.getElementsByName('streetAddress')[0]);
	result += validateInput($e, $e.val());
	
	//Validate city is provided
	$e = $(document.getElementsByName('city')[0]);
	result += validateInput($e, $e.val());

	//Validate state is selected
	$e = $("#state");
	result += validateInput($e, $e.find(":selected").val());
	
	//Validate at least one date is selected
	result += validateDates();
	
	//Validate times are set
	result += validateTimes();
	
//	//Validate date range is selected
//	$e = $("#dateRange");
//	result += validateInputExistence($e, $e.val());
//	
//	$e = $($("#startTime"));
//	result += validateInputExistence($e, $e.val());
//	
//	$e = $($("#endTime"));
//	result += validateInputExistence($e, $e.val());
	
	//Validate categories
	result += validateMinimumSelectedCategories();
	
	$e = $("#invalidAddJob");
	if(result > 0){
		$e.show();
	}else{
		$e.hide();
	}
	
	
	
	return result;
//	if(result > 0){
//		return false;
//	}else{
//		return true;
//	}
	
}

function validateMinimumSelectedCategories(){
	
	if($("#selectedCategories").find("button").length  == 0){
		$("#invalidCategoryInput-None").show();
		setInvalidCss($("#categoryTree"));
		return 1;
	}else{
		$("#invalidCategoryInput-None").hide();
		setValidCss($("#categoryTree"));
		return 0;
	}
		
}

function validateMaximumSelectedCategories(){
	if($("#selectedCategories").find("button").length  >= 5){
		$("#invalidCategoryInput-TooMany").show();
		return 1;
	}else{
		$("#invalidCategoryInput-TooMany").hide();
		return 0;
	}
}

function validateAddQuestionInputs(){
	
	var $e;
	var $selectedOption;
	var result = 0;
	var answerItems = [];
	var i;
	
	
	//Verify question format
	$e = $("#questionFormat");
	$selectedOption = $($e.find("option:selected")[0]);
	result += validateInput($e, $selectedOption.html());
	
	//If multiple choice question, verify answers have been given
	if($selectedOption.val() == 2 || $selectedOption.val() ==3){
		answerItems = $("#answerList").find(".answer-container input");
		$(answerItems).each(function(){
			result += validateInput($(this), $(this).val());
		})
	}
		
	//Verify question
	$e = $("#question");
	result += validateInput($e, $e.val());

	
	$e = $("#invalidAddQuestion");
	if(result > 0){
		$e.show();
		return false;
	}else{
		$e.hide();
		return true;
	}
}

function isValidInput(value){
	if(value == undefined){
		return 0;
	}
	else if(value == ""){
		return 0;
	}
	else{
		return 1;
	}
}
