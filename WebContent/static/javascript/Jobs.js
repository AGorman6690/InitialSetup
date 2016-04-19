
function getFilteredJobs(params, callback){
		
	$.ajax({
		type : "GET", 
		url: 'http://localhost:8080/JobSearch/jobs/filter' + params,
			dataType : "json",
			success : _success,
			error : _error
		});

		function _success(response) {
			callback(response)
		}

		function _error(response) {
			alert('error filter jobs')
		}

}


//function submitJobs(jobName, userId, categoryId, callback) {
function submitJobs() {	
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");

	$.ajax({
		type : "POST",
		url : "http://localhost:8080/JobSearch/jobs/post",
		headers : headers,
		contentType : "application/json",
		dataType : "application/json", // Response
		data : JSON.stringify(jobs)
	}).done(function() {
		$('#home')[0].click();
	}).error(function() {
		$('#home')[0].click();
	});
}


function addJobToCart() {
	
	$("#submitJobsContainer").show();

	var job = {};
	
	job.jobName = document.getElementsByName('jobName')[0].value;
	
	job.streetAddress = document.getElementsByName('streetAddress')[0].value;
	job.city = document.getElementsByName('city')[0].value;
	job.state = document.getElementsByName('state')[0].value;
	job.zipCode = document.getElementsByName('zipCode')[0].value;
	job.description = document.getElementsByName('description')[0].value;
	job.userId = document.getElementsByName('userId')[0].value;
	job.stringStartDate = $("#dateRange").data('daterangepicker').startDate;
	job.stringEndDate =  $("#dateRange").data('daterangepicker').endDate;
	job.stringStartTime = formatTime($("#startTime").val());
	job.stringEndTime = formatTime($("#endTime").val());
	job.categoryIds = getCategoryIds("selectedCategories");
	
	job.questions = [];
	var questionElements = $("#addedQuestions").find(".question");
	for(var i = 0; i < questionElements.length; i++){
		
		var questionElement = questionElements[i];
		var question = {};
		question.question = $(questionElement).find('textarea').val();
		question.formatId = $(questionElement).find('input').val();

		
		if(question.formatId == 0 || question.formatId == 2 || question.formatId == 3){
			question.answerOptions = [];
			var answerOptions = $(questionElement).find('.answer-option');

			for(var j = 0; j < answerOptions.length; j++){
				var answerOption = {};
				answerOption.answerOption = $(answerOptions[j]).val();
				if(answerOption != "") {
					question.answerOptions.push(answerOption);
				}
			}			 
		}
		
		job.questions.push(question);
	}
	
	
	jobs.push(job);

	$("#pendingJobSubmissions").append(
			"<div id='job_" + jobCount + "'><a>" + job.jobName + "</a></div>")
	jobCount++;
}

function formatTime(time){
	//When converting from string to java.sqlTime on the server,
	//java.sql.Time.valueOf(someString) needs the parameter string to be in "hh:mm:ss" format.

	if( time == "" ){
		return "00:00:00";
	}else{
		
		var len = time.length;
		
		 //am or pm
		var dayHalf = time.substring(len - 2);
		
		var colon = time.indexOf(":");
		var hour = time.substring(0, colon);
		var minutes = time.substring(colon + 1, len - 2);
		
		if(dayHalf == "pm"){
			hour = parseInt(hour) + 12;
		}
		
		if(hour.length == 1){
			hour = "0" + hour;
		}
		
		return hour + ":" + minutes + ":00";	
	}

		
}


function getJob(jobId) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/job/' + jobId,
		dataType : "json",
		success : _success,
		error : _error
	});

	function _success(response) {
	}

	function _error(response) {
		alert('error')
	}
}

function markJobComplete(jobId) {
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");
	$.ajax({
		type : "PUT",
		url : 'http://localhost:8080/JobSearch/job/' + jobId + '/markComplete',
		headers : headers
	}).done(function() {
		$('#home')[0].click();
	}).error(function() {
		$('#home')[0].click();

	});
}

