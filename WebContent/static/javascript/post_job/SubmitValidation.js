$(document).ready(function(){

	
	$("body").on("change", "#generalContainer .invalid", function(){
		validate_General();
	})
	
//	$("body").on("change", "#datesContainer .invalid", function(){
//		validate_DatesAndTimes();
//	})
//	
//	$("body").on("mousedown", "#datesContainer .invalid *", function(){
//		validate_DatesAndTimes();
//	})

})




function isPostJobDtoValid(postJobDto){
	
	var invalidCount = 0;
	var input;

	invalidCount += validate_General();
	invalidCount += validate_DatesAndTimes();
	invalidCount += validate_Location();
	
	if(invalidCount > 0) return false;
	else return true;
	
}

function validate_General(){
	
	var invalidCount = 0;

	
	$input = $("#name");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	$input = $("#description");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	if(invalidCount > 0){
		setInvalidCss($("#general"));
		return 1;
	}
	else{
		setValidCss($("#general"));
		return 0;
	}
		
}

function validate_DatesAndTimes(){
	
	var selectedDates = [];
	var invalidCount = 0;

	$input = $("#endTime-singleDate");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	$input = $("#startTime-singleDate");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	

	$input = $("#workDaysCalendar");
	selectedDates = getSelectedDates($input);
	if(selectedDates.length == 0 ){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	if(invalidCount > 0){
		setInvalidCss($("#date"));
		return 1;
	}
	else{
		setValidCss($("#date"));
		return 0;
	}
		
}


function validate_Location(){
	
	var invalidCount = 0;

	
	$input = $("#street");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	$input = $("#city");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	$input = $("#state");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	$input = $("#zipCode");	
	if($input.val() == ""){
		setInvalidCss($input);
		invalidCount += 1;
	}
	
	if(invalidCount > 0){
		setInvalidCss($("#location"));
		return 1;
	}
	else{
		setValidCss($("#location"));
		return 0;
	}
		
}