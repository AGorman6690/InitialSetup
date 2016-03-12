$(document).ready(function() {

})

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
		url : "http://localhost:8080/JobSearch/createJob",
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
	alert('job complete ' + jobId)
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/job/' + jobId + '/markComplete',
		dataType : "json",
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success markJobComplete")
		callback(response);
	}

	function _error(response) {
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

function getActiveJobsByUser_AppCat(userId, callback) {
	// function getJobs(e){
	// alert("getActiveJobsByUser_AppCat");
	$
			.ajax({
				type : "GET",
				url : 'http://localhost:8080/JobSearch/getActiveJobsByUser_AppCat?userId='
						+ userId,
				dataType : 'json',
				success : _success,
				error : _error
			});

	function _success(response) {
		// alert("success getActiveJobsByUser_AppCat");
		callback(response);
	}

	function _error(response) {
		alert("error getActiveJobsByUser_AppCat");
	}
}

function getCompletedJobsByUser(userId, callback) {
	// function getJobs(e){
	// alert("getActiveJobsByUser_AppCat");
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getCompletedJobsByUser?userId='
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

function populateJobs(arr, e, active) {
	// active values:
	// pass a negative number to populate all jobs, whether active or complete.
	// pass 0 to populate only completed jobs.
	// pass 1 to populate only active jobs.

	// alert(arr.length);

	e.options.length = 0;
	var i;
	for (i = 0; i < arr.length; i++) {
		// alert(arr[i].id);
		// If negative, then populate all jobs
		if (active < 0) {
			// alert("populate jobs length" + arr.length);
			var opt = document.createElement("option");
			opt.value = arr[i].id;
			opt.innerHTML = arr[i].jobName;
			e.appendChild(opt);

			// else only populate active or inactive jobs as specified by active
			// parameter
		} else if (arr[i].isActive == active) {

			var opt = document.createElement("option");
			opt.value = arr[i].id;
			opt.innerHTML = arr[i].jobName;
			e.appendChild(opt);
		}
	}
}
