$(document).ready(function() {

})

function unacceptedApplicantsExist(applicants){

	for(var i = 0; i < applicants.length; i++){		
		if(applicants[i].application.isAccepted == 0) return true;
	}
	
	return false;
}

function getFilteredJobs(radius, fromAddress, categories, callback){
	
	// Build a parameter string for the category ids
	var catString = "";
	if (categories.length > 0){		
//		catString = 'category=' + categories[0];
		for (var i = 0; i < categories.length; i++) {
			catString += '&category=' + categories[i];
		}
	}else catString = "&category=-1";
	
	alert(catString)
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/jobs/filter?radius=' + radius + '&fromAddress=' + fromAddress + "," + catString,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getApplicationsByUser");
		callback(response);
	}

	function _error(response) {
		alert("error getApplicationsByUser");
	}
}

function addJob(jobName, userId, categoryId, callback) {
	// var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
	// var csrfToken =
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");
	var newJob = {};
	newJob.jobName = jobName;
	newJob.userId = userId;
	newJob.categoryId = categoryId;

	$.ajax({
		type : "POST",
		url : "http://localhost:8080/JobSearch/job/post",
		headers : headers,
		contentType : "application/json",
		dataType : "application/json", // Response
		data : JSON.stringify(newJob),
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success add job");
		callback(response);
	}

	function _error(response) {
		alert("error addJob");
	}
}

function applyForJob(jobId, userId, callback) {
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
		// alert("markApplicationUnderConsideration;
		callback(response);
	}

	function _error(response) {
		alert("error applyForJob");
	}
}


function markJobComplete(jobId, callback) {
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");
	$.ajax({
		type : "POST",
		url : 'http://localhost:8080/JobSearch/job/' + jobId + '/markComplete',
//		dataType : "json",
		headers : headers,
		success : _success,
		error : _error
	});

	function _success() {
		// alert("success markJobComplete")
		callback();
	}

	function _error() {
		alert("error markJobComplete");
	}
}

function getApplicationsByUser(userId, callback) {
	// function getJobs(e){
	// alert("getApplicationsByUser");

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getApplicationsByUser?userId='
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getApplicationsByUser");
		callback(response);
	}

	function _error(response) {
		alert("error getApplicationsByUser");
	}
}

function getEmploymentByUser(userId, callback) {
	// function getJobs(e){
	// alert("getEmploymentByUser");
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/user/' + userId + '/employment',
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getEmploymentByUser");
		callback(response);
	}

	function _error(response) {
		alert("error getEmploymentByUser");
	}
}

function getJobsByUser(userId, callback) {
	// function getJobs(e){
	// alert("getJobsByUser");
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getJobsByUser?userId=' + userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getJobsByUser");

		callback(response);
	}

	function _error(response) {
		alert("error");
	}
}

function getActiveJobsByUser(userId, callback) {
	// function getJobs(e){
	// alert("getActiveJobsByUser");
	$
			.ajax({
				type : "GET",
				url : 'http://localhost:8080/JobSearch/jobs/active/user?userId='
						+ userId,
				dataType : 'json',
				success : _success,
				error : _error
			});

	function _success(response) {
		// alert("success getActiveJobsByUser");
		callback(response);
	}

	function _error(response) {
		alert("error getActiveJobsByUser");
	}
}

function getCompletedJobsByUser(userId, callback) {
	// function getJobs(e){
	// alert("getActiveJobsByUser");
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/jobs/completed/user?userId='
				+ userId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getCompletedJobByUser");
		callback(response);
	}

	function _error(response) {
		alert("error getCompletedJobByUser");
	}
}

function getJobCountByCategory(categoryId, callback) {
	// alert("getJobCountByCategory")
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
		// alert("success getJobCountByCategory");
		callback(response);
	}

	function _error(response) {
		alert("error getJobCountByCategory");
	}
}

function getJobsByCategory(categoryId, callback) {
	// alert("getJobCountByCategory")
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getJobsByCategory?categoryId='
				+ categoryId,
		dataType : 'json',
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getJobsByCategory");
		callback(response);
	}

	function _error(response) {
		alert("error getJobsByCategory");
	}
}

function getSubJobCountByCategory(categoryId, callback) {
	// alert("getSubJobCountByCategory")
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
		// alert("success getSubJobCountByCategory");
		callback(response);
	}

	function _error(response) {
		alert("error getSubJobCountByCategory");
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
		// alert("success getJobOffersByUser");
		callback(response);
	}

	function _error(response) {
		alert("error getJobOffersByUser");
	}
}

