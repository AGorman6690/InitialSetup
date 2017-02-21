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




function arePostJobInputsValid(){
	
	var invalidCount = 0;
	var input;

	invalidCount += validate_General();
	invalidCount += validate_Dates();
	invalidCount += validate_Times();
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

function validate_Dates(){
	
	var selectedDates = [];
	var invalidCount = 0;

	$input = $("#workDaysCalendar_postJob");
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

function validate_Times(){
	
	var invalidCount = 0;
	var $startTime;
	var $endTime;
	
	
	// If no days have been selected, also show "Times" as invalid
	if(selectedDays.length == 0) invalidCount += 1;
	
	$("#timesTable").find("tbody tr.work-day-row").each(function(){

		
		$startTime = $(this).find("select.start-time").eq(0);
		$endTime = $(this).find("select.end-time").eq(0);
		
		if($startTime.val() == ""){
			setInvalidCss($startTime);
			invalidCount += 1;
		}
		else{
			setValidCss($startTime);
		}
		
		
		
		if($endTime.val() == ""){
			setInvalidCss($endTime);
			invalidCount += 1;
		}
		else{
			setValidCss($endTime);
		}
		
				
	})
	
	if(invalidCount > 0){
		setInvalidCss($("#times"));
		return 1;
	}
	else{
		setValidCss($("#times"));
		return 0;
	}
	
	return invalidCount;
	
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