function showJob() {
	var eTo = document.getElementById("jobToApplyFor");
	eTo.value = this.options[this.selectedIndex].text;
	eTo.name = this.options[this.selectedIndex].value;
}

function applyForJob() {

	// The job id is stored in the input's name attribute (see showJob())
	var jobId = document.getElementById("jobToApplyFor").name;

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/applyForJob?jobId=' + jobId,
		dataType : "json",
	})
}

function getSelectedUser() {
	// alert("get jobs");
	var e = document.getElementById("employers");
	var userId = e.options[e.selectedIndex].value;
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getSelectedUser?userId='
				+ userId,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error
	});

	// Populate the selected employer's jobs
	function _success(response) {
		populateJobs(response.jobs, document
				.getElementById("jobs"));
	}

	function _error(response) {
		alert("error");
	}
}

function getSelectedCategory() {
	// alert("get jobs");
	var e = document.getElementById("categories");
	var categoryId = e.options[e.selectedIndex].value;

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getSelectedCategory?categoryId='
				+ categoryId,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error
	});

	function _success(response) {

		// Clear jobs by selected users
		$("#jobs").empty();

		populateUsers(response.selectedCategory.users, document
				.getElementById("employers"));
		populateJobs(response.selectedCategory.jobs, document
				.getElementById("jobsBySelectedCat"));
	}

	function _error(response) {
		alert("error");
	}
}