$(document).ready(function(){

	$("#submitJobContainer").click(function(){
		
		submitJobPost();
	})
	
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
	postJobDto.description = $("#jobDescription").val();
	postJobDto.city = $("#city").val();
	postJobDto.state = $("#state").val();
	postJobDto.zipCode = $("#zipCode").val();
	postJobDto.postQuestionDtos = postQuestionDtos;
	postJobDto.workDays = getWorkDays();
	postJobDto.categoryIds.push(1);
	
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
		window.location.replace("/JobSearch/user/profile");
	}	

	function _error() {
		$("html").removeClass("waiting");
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}