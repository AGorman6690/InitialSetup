$(document).ready(function() {

})

function hireApplicant(userId, jobId) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/job/' + jobId + '/hire/user/'
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		var employeeHtml = $("#application_" + userId);
		$("#application_" + userId).remove();
		$("#employees").append(employeeHtml);
	}

	function _error(response, errorThrown) {

	}
}

function unacceptedApplicantsExist(applicants) {

	for (var i = 0; i < applicants.length; i++) {
		if (applicants[i].application.isAccepted == 0)
			return true;
	}

	return false;
}


//function getFilteredJobs(radius, fromAddress, categories, callback){
function getFilteredJobs(filter, callback){

	// Build a parameter string for the category ids
	var categoryIds = "";
	if (filter.categories.length > 0){		
		for (var i = 0; i < filter.categories.length; i++) {
			categoryIds += '&categoryId=' + filter.categories[i];
		}
	}else categoryIds = "&categoryId=-1";
	
	$.ajax({
		type : "GET", 
		url : 'http://localhost:8080/JobSearch/jobs/filter?radius=' + filter.radius + '&fromAddress='
				+ filter.fromAddress + categoryIds + "&startTime=" + filter.stringStartTime
				+ "&endTime=" + filter.stringEndTime + "&beforeEndTime=" + filter.beforeEndTime
				+ "&beforeStartTime=" + filter.beforeStartTime + "&startDate=" + filter.stringStartDate
				+ "&endDate=" + filter.stringEndDate + "&beforeStartDate=" + filter.beforeStartDate
				+ "&beforeEndDate=" + filter.beforeEndDate,
			dataType : "json",
			success : _success,
			error : _error
		});

		function _success(response) {
			callback(response)
		}

		function _error(response) {
		}

}


function submitJobs(jobName, userId, categoryId, callback) {
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


function applyForJob(jobId, userId) {
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");
	$.ajax({
		type : "POST",
		url : 'http://localhost:8080/JobSearch/job/apply?jobId=' + jobId
				+ '&userId=' + userId,
		headers : headers,
		dataType : "json",
		success : _success,
		error : _error
	});

	function _success(response) {
	}

	function _error(response) {
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

function getApplicationsByUser(userId, callback) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getApplicationsByUser?userId='
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getEmploymentByUser(userId, callback) {
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/user/' + userId + '/employment',
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getJobsByUser(userId, callback) {
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getJobsByUser?userId=' + userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getActiveJobsByUser(userId, callback) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/jobs/active/user?userId='
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});


	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getCompletedJobsByUser(userId, callback) {
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/jobs/completed/employer?userId='
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getJobCountByCategory(categoryId, callback) {
	$
			.ajax({
				type : "GET",
				url : 'http://localhost:8080/JobSearch/getJobCountByCategory?categoryId='
						+ categoryId,
				dataType : 'json',
				success : _success,
				error : _error
			});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getJobsByCategory(categoryId, callback) {
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getJobsByCategory?categoryId='
				+ categoryId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

function getSubJobCountByCategory(categoryId, callback) {
	$
			.ajax({
				type : "GET",
				url : 'http://localhost:8080/JobSearch/getSubJobCountByCategory?categoryId='
						+ categoryId,
				dataType : 'json',
				success : _success,
				error : _error
			});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}

// *******************************************************************
// Should getApplicationsByEmployers accomplish this?????
// *******************************************************************
function getJobOffersByUser(userId, callback) {
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getJobOffersByUser?userId='
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		callback(response);
	}

	function _error(response) {
	}
}
