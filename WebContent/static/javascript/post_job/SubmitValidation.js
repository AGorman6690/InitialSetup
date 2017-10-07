$(document).ready(function(){

	$("body").on("keyup", ".invalid", function(){
		
		// ****************************************
		// ****************************************
		// Note: Ensure the click event in the InputValidation.js file happens first
		// ****************************************
		// ****************************************
		validateSection($(this));
	})

})

function validateSection($e) {
	var $pageSection = $e.closest(".page-section");
	var $showPageSection = $("#postSections [data-page-section-id=" + $pageSection.attr("id") + "]").eq(0);
	if($pageSection.find(".invalid").length == 0) setValidCss($showPageSection);
}


function arePostJobInputsValid(jobDto){
	
	var invalidCount = 0;
	var input;

	invalidCount += validate_General(jobDto);
	invalidCount += validate_Dates(jobDto);
	invalidCount += validate_Times(jobDto);
	invalidCount += validate_Location(jobDto);
	invalidCount += validate_Positions(jobDto);
	
	if(invalidCount > 0) return false;
	else return true;
	
}

function validate_General(jobDto){
	
	var isValid = true;

	
	if(jobDto.job.jobName == ""){
		setInvalidCss($("#name"));
		isValid = false;
	}
	
	if(jobDto.job.description == ""){
		setInvalidCss($("#description"));
		isValid = false;
	}
	
	if(!isValid){
		setInvalidCss($("#general"));
		setInvalidCss($("#show-general"));
		return 1;
	}else{
		setValidCss($("#general"));
		setValidCss($("#show-general"));
		return 0;
	}
		
}

function validate_Dates(jobDto){
	
	var isValid = true
	if(jobDto.workDays.length == 0 ){
		setInvalidCss($("#workDaysCalendar_postJob .ui-datepicker"));		
		isValid = false;
	}else{
		setValidCss($("#workDaysCalendar_postJob .ui-datepicker"));		
	}		
	
	var $e = $("#datesContainer .radio-container");
	if(!isRadioContainerSelected($e)){
		setInvalidCss($e);
		isValid = false;
	}else{
		setValidCss($e);
	}
	
	if(!isValid ){
		setInvalidCss($("#show-dates-section"));
		return 1;
	}else{
		setValidCss($("#show-dates-section"));
		return 0;
	}
	
}

function validate_Times(jobDto){
	
	isValid = true;
	if(jobDto.workDays.length == 0){
		isValid = false;
	}else{
		$(jobDto.workDays).each(function(i, workDay) {
			if(workDay.stringStartTime == "" || workDay.stringEndTime == "") isValid = false;
		})
	}
	
	if(!isValid){
		setInvalidCss($("#select-times"));
		return 1;
	}else{
		setValidCss($("#select-times"));
		return 0;
	}
		
}


function validate_Location(jobDto){
	
	var isValid = true;
	
	if(jobDto.job.streetAddress == ""){
		setInvalidCss($("#street"));
		isValid = false;
	}
	
	if(jobDto.job.city == ""){
		setInvalidCss($("#city"));
		isValid = false;
	}
	
	if(jobDto.job.state == "" || jobDto.job.state == null){
		setInvalidCss($("#state"));
		isValid = false;
	}
	
//	if(jobDto.job.zipCode == ""){
//		setInvalidCss($("#zipCode"));
//		isValid = false;
//	}
	
	if(!isValid){
		setInvalidCss($("#show-location"));
		return 1;
	}else{
		setValidCss($("#show-location"));
		return 0;
	}
		
}