$(document).ready(function(){

	$("#submitJobContainer").click(function(){
		
		submitJobPost();
	})
	
	setStates();
	setTimeOptions($("#startTime-singleDate"), 30);
	setTimeOptions($("#endTime-singleDate"), 30);
	
})



function submitJobPost(){
	var postJobDto = {}
	postJobDto = getPostJobDto();
	
	executeAjaxCall_postJob(postJobDto);
}

function getPostJobDto(){
	var postJobDto = {};
	
	postJobDto.postQuestionDtos = [];
	postJobDto.categoryIds = [];
	postJobDto.workDays = [];
	
	postJobDto.jobName = $("#name").val();
	postJobDto.description = $("#description").val();
	postJobDto.streetAddress = $("#street").val();
	postJobDto.city = $("#city").val();
	postJobDto.state = $("#state").val();
	postJobDto.zipCode = $("#zipCode").val();
	postJobDto.questions = postQuestionDtos;
	postJobDto.workDays = getWorkDays();
	postJobDto.categoryIds.push(1);
	postJobDto.durationTypeId = 2;
	postJobDto.durationUnitLength = postJobDto.workDays.length;
	
	return postJobDto;
	
}

function getWorkDays(){
	var dates = getSelectedDates($("#calendar-multi-day"), "yy-mm-dd");
	var workDays = [];
	var stringStartTime = $("#startTime-singleDate").find("option:selected").eq(0).attr("data-filter-value");
	var stringEndTime = $("#endTime-singleDate").find("option:selected").eq(0).attr("data-filter-value");
	
	$(dates).each(function(){
		var workDay = {};
		workDay.stringDate = this.toString();
		workDay.stringStartTime = stringStartTime;
		workDay.stringEndTime = stringEndTime;
		
		workDays.push(workDay);
	})
	
	return workDays;
}

function executeAjaxCall_postJob(postJobDto){
	$("html").addClass("waiting");
	$.ajax({
		type : "POST",
		url: '/JobSearch/job/post',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(postJobDto),
//		dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success() {
		$("html").removeClass("waiting");			
//		window.location.replace("/JobSearch/user/profile");
	}	

	function _error() {
		$("html").removeClass("waiting");
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}

function setStates(){
	var $e = $("#state"); 
	$e.append('<option value="" selected style="display: none"></option>');
	$e.append('<option value="AL">AL</option>');
	$e.append('<option value="AK">AK</option>');
	$e.append('<option value="AZ">AZ</option>');
	$e.append('<option value="AR">AR</option>');
	$e.append('<option value="CA">CA</option>');
	$e.append('<option value="CO">CO</option>');
	$e.append('<option value="CT">CT</option>');
	$e.append('<option value="DE">DE</option>');
	$e.append('<option value="DC">DC</option>');
	$e.append('<option value="FL">FL</option>');
	$e.append('<option value="GA">GA</option>');
	$e.append('<option value="HI">HI</option>');
	$e.append('<option value="ID">ID</option>');
	$e.append('<option value="IL">IL</option>');
	$e.append('<option value="IN">IN</option>');
	$e.append('<option value="IA">IA</option>');
	$e.append('<option value="KS">KS</option>');
	$e.append('<option value="KY">KY</option>');
	$e.append('<option value="LA">LA</option>');
	$e.append('<option value="ME">ME</option>');
	$e.append('<option value="MD">MD</option>');
	$e.append('<option value="MA">MA</option>');
	$e.append('<option value="MI">MI</option>');
	$e.append('<option selected value="MN">MN</option>');
	$e.append('<option value="MS">MS</option>');
	$e.append('<option value="MO">MO</option>');
	$e.append('<option value="MT">MT</option>');
	$e.append('<option value="NE">NE</option>');
	$e.append('<option value="NV">NV</option>');
	$e.append('<option value="NH">NH</option>');
	$e.append('<option value="NJ">NJ</option>');
	$e.append('<option value="NM">NM</option>');
	$e.append('<option value="NY">NY</option>');
	$e.append('<option value="NC">NC</option>');
	$e.append('<option value="ND">ND</option>');
	$e.append('<option value="OH">OH</option>');
	$e.append('<option value="OK">OK</option>');
	$e.append('<option value="OR">OR</option>');
	$e.append('<option value="PA">PA</option>');
	$e.append('<option value="RI">RI</option>');
	$e.append('<option value="SC">SC</option>');
	$e.append('<option value="SD">SD</option>');
	$e.append('<option value="TN">TN</option>');
	$e.append('<option value="TX">TX</option>');
	$e.append('<option value="UT">UT</option>');
	$e.append('<option value="VT">VT</option>');
	$e.append('<option value="VA">VA</option>');
	$e.append('<option value="WA">WA</option>');
	$e.append('<option value="WV">WV</option>');
	$e.append('<option value="WI">WI</option>');
	$e.append('<option value="WY">WY</option>');
	
	
}