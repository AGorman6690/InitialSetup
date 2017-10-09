
$(document).ready(function(){
	$("body").on("change", "select.invalid", function(){
		validateSelect($(this));
	})
	$("body").on("keyup", "input[type=text].invalid, textarea.invalid", function(){
		validateTextInput($(this));
	})
	$("body").on("change", ".radio-container.invalid input[type=radio]", function(){
		validateRadioContainer($(this).closest(".radio-container"));
	})
})
function validateInputElements($cont, $errorMessageCont){
	var invalidCount = 0;
	$cont.find(".validate-input").each(function() {
		var $e = $(this);
		invalidCount += validateTextInputs($e);
		invalidCount += validateSelects($e)
		invalidCount += validateCalendars($e);
		invalidCount += validateRadioContainers($e);
	})
	if(invalidCount > 0){
		if($errorMessageCont !== undefined){
			$errorMessageCont.addClass("visible");
			$errorMessageCont.find(".invalid-input-message").slideUp(300, function(){		
				$errorMessageCont.find(".invalid-input-message").slideDown(600);
			})
		}
		return false;
	}else{
		if($errorMessageCont !== undefined){
			$errorMessageCont.find(".invalid-input-message").slideUp(300);
			$errorMessageCont.removeClass("visible");
		}
		return true;
	}	
}
function getFindParam(str){
	return str + ":not(.skip-validation):visible";
}
function validateTextInputs($cont){
	var invalidCount = 0;
	var findParam = getFindParam("input[type=text]");
	findParam += "," + getFindParam("textarea");
	var elements = $cont.find(findParam);
	$(elements).each(function(i, e){
		invalidCount += validateTextInput($(e));
	})	
	return invalidCount;
}
function validateTextInput($input){
	var valid = true;
	if($input.attr("data-greater-than-equal-to")){
		
	}
	if($input.val() === ""){
		valid = false;
	}
	return inspectValidity($input, valid);	
}
function validateSelects($cont){
	var invalidCount = 0;
	var elements = $cont.find(getFindParam("select"));
	$(elements).each(function(i, e){
		invalidCount += validateSelect($(e));
	})	
	return invalidCount;	
}
function validateSelect($select){
	var valid = true;
	var selectedOption = $select.find("option:selected")[0];
	if(selectedOption === undefined || selectedOption.text === "" || selectedOption.hasAttribute("disabled") ){
		valid = false;
	}
	return inspectValidity($select, valid);	
}
function inspectValidity($e, valid){
	if(valid === false){
		$e.addClass("invalid");
		return 1;
	}else{
		$e.removeClass("invalid")
		return 0;
	}	
}
function validateCalendars($cont){
	var invalidCount = 0;
	var elements = $cont.find(getFindParam(".calendar"));
	$(elements).each(function(i, e){
		invalidCount += validateCalendar($(e));	
	})	
	return invalidCount;	
}
function validateCalendar($calendar){
	var valid = true;
	var className = $calendar.attr("data-selected-class-name");
	var selectedDates = getSelectedDates($calendar, "yy-mm-dd", className);
	
	if(selectedDates.length == 0){
		valid = false;
	}
	return inspectValidity($calendar, valid);	
}
function validateRadioContainers($cont){
	var invalidCount = 0;
	var elements = $cont.find(getFindParam(".radio-container"));
	$(elements).each(function(i, e){
		invalidCount += validateRadioContainer($(e));	
	})	
	return invalidCount;	
}
function validateRadioContainer($radioContainer){
	var valid = true;
	if(!$radioContainer.find("input[type=radio]:checked").length){
		valid = false;
	}
	return inspectValidity($radioContainer, valid);	
}