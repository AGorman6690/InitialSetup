$(document).ready(function(){
	$("#activeJobs").change(function(){
		
		//Get job id
		var jobId = $("#activeJobs").val();
		var jobName = $("#activeJobs option:selected").text();
		
		//Store the job Id in the input element's name attribute.
		//This is not really needed.
		//I was using this element to identity the selected job, rather
		//than the selected option in the active job drop down.
		$("#selectedJob").val(jobName);
		$("#selectedJob").attr("name", jobId);

		//Get the applicants for the selected job
		getApplicants(jobId, function(response){
			populateUsers(response, document.getElementById("applicants"));
		});
		
		//Get the employees for the selected job
		getEmployees(jobId, function(response){
			populateUsers(response, document.getElementById("employees"));
		});
		
		//Get categories for the selected job
		getCategoriesByJob(jobId, function(response){
			populateCategories(response, document.getElementById("selectedJobCats"));
		});
	});
	
	$("#completedJobs").change(function(){		
		//Get job id
		var jobId = $("#completedJobs").val();
		//alert(jobId);
		//Get the employees for the selected job
		getEmployees(jobId, function(response){
			alert(JSON.stringify(response));
			populateUsers(response, document.getElementById("employeesCompletedJob"));
		});
	})
	
	$("#markJobComplete").click(function(){
		var jobId = $("#activeJobs").val();//document.getElementById("selectedJob").name;
		alert(jobId);
		//Mark the job complete
		markJobComplete(jobId, function(response){
			
			//Populate the user's active jobs
			populateJobs(response, document.getElementById("activeJobs"), 1);
			
			//Populate the user's completed jobs
			populateJobs(response, document.getElementById("completedJobs"), 0);

		//	$("#rateEmployee").html(response);
		});
	});
	
})



function addJob(jobName, userId, callback){
	
	$.ajax({	 	
		type: "GET",
        url: "http://localhost:8080/JobSearch/addJob?jobName=" + jobName + "&userId=" + userId,
        dataType: "json", // Response
        success: _success,
        error: _error
    });

	function _success(response){
		//alert("success add job");
		callback(response);
		
	}

	function _error(response){
		alert("error addJob");
	}

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

//function getSelectedUser() {
//	// alert("get jobs");
//	var e = document.getElementById("employers");
//	var userId = e.options[e.selectedIndex].value;
//	$.ajax({
//		type : "GET",
//		url : 'http://localhost:8080/JobSearch/getSelectedUser?userId='
//				+ userId,
//		contentType : "application/json", // Request
//		dataType : "json", // Response
//		success : _success,
//		error : _error
//	});
//
//	// Populate the selected employer's jobs
//	function _success(response) {
//		populateJobs(response.jobs, document
//				.getElementById("jobs"));
//	}
//
//	function _error(response) {
//		alert("error");
//	}
//}

function markJobComplete(jobId, callback){

	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/markJobComplete?jobId=' + jobId,
        dataType: "json",
		success: _success,
        error: _error
	    });
	
		function _success(response){
		//alert("success markJobComplete");
			callback(response);
		}

		function _error(response){
			alert("error markJobComplete");
		}
}

//function getSelectedCategory() {
//	// alert("get jobs");
//	var e = document.getElementById("categories");
//	var categoryId = e.options[e.selectedIndex].value;
//
//	$.ajax({
//		type : "GET",
//		url : 'http://localhost:8080/JobSearch/getSelectedCategory?categoryId='
//				+ categoryId,
//		contentType : "application/json", // Request
//		dataType : "json", // Response
//		success : _success,
//		error : _error
//	});
//
//	function _success(response) {
//
//		// Clear jobs by selected users
//		$("#jobs").empty();
//
//		populateUsers(response.selectedCategory.users, document
//				.getElementById("employers"));
//		populateJobs(response.selectedCategory.jobs, document
//				.getElementById("jobsBySelectedCat"));
//	}
//
//	function _error(response) {
//		alert("error");
//	}
//}

function getApplicationsByUser(userId, callback){		
	//	function getJobs(e){	
		//alert("getApplicationsByUser");
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/getApplicationsByUser?userId=' + userId,
	        dataType: 'json',
			success: _success,
	        error: _error
		    });
			
			function _success(response){
			//	alert("success getApplicationsByUser");
				callback(response);
			}
			
			function _error(response){
				alert("error getApplicationsByUser");
			}
	}

function getEmploymentByUser(userId, callback){		
	//	function getJobs(e){	
		//alert("getEmploymentByUser");
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/getEmploymentByUser?userId=' + userId,
	        dataType: 'json',
			success: _success,
	        error: _error
		    });
			
			function _success(response){
				alert("success getEmploymentByUser");
				callback(response);
			}
			
			function _error(response){
				alert("error getEmploymentByUser");
			}
	}

function getJobsByUser(userId, callback){		
	//	function getJobs(e){	
		//alert("getJobsByUser");
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/getJobsByUser?userId=' + userId,
	        dataType: 'json',
			success: _success,
	        error: _error
		    });
			
			function _success(response){
			//	alert("success getJobsByUser");
				callback(response);
			}
			
			function _error(response){
				alert("error");
			}
	}

function populateJobs(arr, e, active){
	//active values:
	//pass a negative number to populate all jobs, whether active or complete.
	//pass 0 to populate only completed jobs.
	//pass 1 to populate only active jobs.
	
	//alert(arr.length);
		
		e.options.length=0;
		var i;
		for(i = 0; i < arr.length ; i++){
		//	alert(arr[i].id);
			//If negative, then populate all jobs
			if(active < 0){
			//	alert("populate jobs length" + arr.length);
				var opt = document.createElement("option");					
				opt.value = arr[i].id;
				opt.innerHTML = arr[i].jobName;
				e.appendChild(opt);				
				
			//else only populate active or inactive jobs as specified by active parameter
			}else if(arr[i].isActive == active){
				
				var opt = document.createElement("option");					
				opt.value = arr[i].id;
				opt.innerHTML = arr[i].jobName;
				e.appendChild(opt);
			}
		}		
	}	



