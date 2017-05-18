
$(document).ready(function(){
	
	$("body").on("keyup", ".invalid", function(event){		
		validateUserInput($(this));
	})
	
	$("body").on("change", ".invalid", function(event){		
		validateUserInput($(this));
	})
	
//	$("body").on("mousedown", ".invalid.calendar *", function(){
//		
//			var $calendar = $($(this).closest(".calendar"));
//			var e = $(this).closest(".calendar").attr("data-selected-days-count");
//			if($calendar.attr("data-selected-days-count") > 0){
//				setValidCss($calendar);
//			}
//
//	})
	
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
	
	
	$("body").on("mousedown", ".invalid.calendar.hasDatepicker td ", function(){
		$(this).closest(".invalid.calendar.hasDatepicker").removeClass("invalid");
	})

})

function validateUserInput($e) {
	if($e.is("input") || $e.is("textarea")){
		if($e.hasClass("positive-number")){
			
			if(isValidatePositiveNumber($e.val())){
				setValidCss($e);
			}				
		}else{
			if($e.val() != "") setValidCss($e);
		}
	}else if($e.is("select")){
		if($e.val() != "") setValidCss($e);
	}
	
	if($e.hasClass("radio-container")){
		if(isRadioContainerSelected($e)) setValidCss($e);
		else setInvalidCss($e);
	}
}

function isValidatePositiveNumber(value){
	
	var isValid = true;
	if(value == null || value == undefined || value == ""){
		isValid = false;
	}
	else if($.isNumeric(value) == 0){
		isValid = false;
	}
	else if(value < 0){
		isValid = false;
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

function validateDates_durations(){
//	var selectedDates = $("#times").find(".time");
	var $e = $("#calendarContainer");
	if(selectedDays.length == 0){
		setInvalidCss($e);
		return 0;
		
	}else{
		setValidCss($e);
		return 1;
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

function validateTimes_singleStartAndEnd(){
	var $eStartTime = $("#startTime-singleDate");
	var $eEndTime = $("#endTime-singleDate");

	var isValid = true;
	if(!isValidInput($eStartTime.val())){
		setInvalidCss($eStartTime);
		isValid = false;
	}
	else{
		setValidCss($eStartTime)
	}

	if(!isValidInput($eEndTime.val())){
		setInvalidCss($eEndTime);
		isValid = false;
	}
	else{
		setValidCss($eEndTime)
	}
	
	return isValid;
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
	var validAnswerOptionCount = 0;
	
	//Verify question format
	$e = $("#questionFormat");
	$selectedOption = $($e.find("option:selected")[0]);
	result += validateInput($e, $selectedOption.html());
	
	//If multiple choice question, verify at least two answers have been given
	if($selectedOption.val() == 2 || $selectedOption.val() ==3){
		answerItems = $("#answerList").find(".answer-container input");
		for(i = 0; i < 2; i++){
			result += validateInput($(answerItems[i]), $(answerItems[i]).val());
		}
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
	else if(value == null){
		return 0;
	}
	else{
		return 1;
	}
}
