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

function addJobToCart() {
	$("#submitJobsContainer").show();

	var job = {};

	job.jobName = document.getElementsByName('jobName')[0].value;
	job.location = document.getElementsByName('location')[0].value;
	job.description = document.getElementsByName('description')[0].value;
	job.categoryIds = [];
	job.userId = document.getElementsByName('userId')[0].value;

	var categoryIds = document.getElementsByName('categoryId');

	for (var i = 0; i < categoryIds.length; i++) {
		if (categoryIds[i].checked) {
			job.categoryIds.push(categoryIds[i].value);
		}
	}

	jobs.push(job);

	$("#pendingJobSubmissions").append(
			"<div id='job_" + jobCount + "'><a>" + job.jobName + "</a></div>")
	jobCount++;
}

function submitJobs(jobName, userId, categoryId, callback) {
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");

	$.ajax({
		type : "POST",
		url : "http://localhost:8080/JobSearch/createJob",
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
		callback(response);
	}

	function _error(response) {
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
		url : 'http://localhost:8080/JobSearch/getCompletedJobsByUser?userId='
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
