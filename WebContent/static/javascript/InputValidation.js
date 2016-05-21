function validateInputExistence($e, value){
	if(value == ""){
		setInvalidCss($e);
		return 1;
	}else{
		setValidCss($e);
		return 0;
	}

}

function validateSelectInput($e, value){
	
	if(value < 0 || value == ""){
		setInvalidSelectCss($e);
		return 1;
	}else{
		setValidSelectCss($e);
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

	var $eJobName = $(document.getElementsByName('jobName')[0]);
	var i;

	for(i = 0; i < jobs.length; i++){
		if(jobs[i].jobName == requestedJobName){
			$("#invalidJobName").show();
			setInvalidCss($eJobName);
			return 1;
		}
	}
	
	if(jobs.length > 0){
		setValidCss($eJobName);
		$("#invalidJobName").hide();
	}
	return 0;
}

function setInvalidCss($e){
	if($e.hasClass("invalid-input-existence") == 0){
		$e.addClass("invalid-input-existence");
	}	
}

function setInvalidSelectCss($e){
	if($e.hasClass("invalid-select-input") == 0){
		$e.addClass("invalid-select-input");
	}	
}
function setValidCss($e){
	if($e.hasClass("invalid-input-existence") == 1){
		$e.removeClass("invalid-input-existence");
	}	
}

function setValidSelectCss($e){
	if($e.hasClass("invalid-select-input") == 1){
		$e.removeClass("invalid-select-input");
	}	
}

function validatePostJobInputs(jobs){

	var $e;
	var result = 0;
	
	$e = $(document.getElementsByName('jobName')[0]);
	result += validateInputExistence($e, $e.val());
	
	$e = $(document.getElementsByName('jobDescription')[0]);
	result += validateInputExistence($e, $e.val());

	$e = $(document.getElementsByName('streetAddress')[0]);
	result += validateInputExistence($e, $e.val());
	
	$e = $(document.getElementsByName('city')[0]);
	result += validateInputExistence($e, $e.val());
	
	$e = $("#state");
	result += validateSelectInput($e.parent(), $e.find(":selected").val());
	
	$e = $("#dateRange");
//	result += validateInputExistence($e, $e.data('daterangepicker').startDate);
//	result += validateInputValueContains($e, $e.val(), "Invalid date");
	result += validateInputExistence($e, $e.val());
	
	$e = $($("#startTime"));
	result += validateInputExistence($e, $e.val());
	
	$e = $($("#endTime"));
	result += validateInputExistence($e, $e.val());
	
	result += validateJobName(document.getElementsByName('jobName')[0].value, jobs)
//	alert(result)
	if(result > 0){
		return false;
	}else{
		return true;
	}
	
}

function validateAddQuestionInputs(){
	
	var $e;
	var result = 0;
	var questionFormatValue = -1;
	var answerItems = [];
	var i;
	
	
	$e = $($("#new-question-container").find(".question-formats")[0]);
	questionFormatValue = $e.find(":selected").val();
	result += validateSelectInput($e.parent(), $e.find(":selected").val());
	
	$e = $($("#newQuestionText"));
	result += validateInputExistence($e, $e.val());
	
	
	//Potentially validate answer items.
	//I.e. there must be at least 2 answer options if select single or select multiple.
	//Consider this later if deemed necessary.
	//*********************************************************
//	if(questionFormatValue == 2 || questionFormatValue == 3){
//		answerItems = $("#new-question-container").find(".answer-option");
//		for(i = 0; i = 1; i++)
//	}
	

	if(result > 0){
		return false;
	}else{
		return true;
	}
}
