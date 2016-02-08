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
		
		getOfferedApplicantsByJob(jobId, function(response){
			populateUsers(response, document.getElementById("offeredApplicants"));
		})
		
		
//		//Get the employees for the selected job
//		getEmployeesByJob(jobId, function(response){
//			populateUsers(response, document.getElementById("employees"));
//		});
//		
//		//Get categories for the selected job
//		getCategoriesByJob(jobId, function(response){
//			populateCategories(response, document.getElementById("selectedJobCats"));
//		});
	});
	

	
	$("#markJobComplete").click(function(){
		var jobId = $("#activeJobs").val();//document.getElementById("selectedJob").name;
		
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




function addJob(jobName, userId, categoryId, callback){
	
	$.ajax({	 	
		type: "GET",
        url: "http://localhost:8080/JobSearch/addJob?jobName=" + jobName + "&userId=" + userId + '&categoryId=' + categoryId,
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


function markJobComplete(jobId, callback){

	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/markJobComplete?jobId=' + jobId,
        dataType: "json",
		success: _success,
        error: _error
    });

	function _success(response){			
		//alert("markApplicationUnderConsideration;
		callback(response);
	}

	function _error(response){
		alert("error markJobComplete");
	}
}


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
		//alert("success getEmploymentByUser");
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

function getActiveJobsByUser_AppCat(userId, callback){		
	//	function getJobs(e){	
		//alert("getActiveJobsByUser_AppCat");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getActiveJobsByUser_AppCat?userId=' + userId,
        dataType: 'json',
		success: _success,
        error: _error
    });
	
	function _success(response){
	//	alert("success getActiveJobsByUser_AppCat");		
		callback(response);
	}
	
	function _error(response){
		alert("error getActiveJobsByUser_AppCat");
	}
}

function getJobCountByCategory(categoryId, callback){
//	alert("getJobCountByCategory")
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getJobCountByCategory?categoryId=' + categoryId,
        dataType: 'json',
		success: _success,
        error: _error
    });
	
	function _success(response){
		//alert("success getJobCountByCategory");	
		callback(response);
	}
	
	function _error(response){
		alert("error getJobCountByCategory");
	}	
}

function getSubJobCountByCategory(categoryId, callback){
//	alert("getSubJobCountByCategory")
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getSubJobCountByCategory?categoryId=' + categoryId,
        dataType: 'json',
		success: _success,
        error: _error
    });
	
	function _success(response){
		//alert("success getSubJobCountByCategory");	
		callback(response);
	}
	
	function _error(response){
		alert("error getSubJobCountByCategory");
	}	
}


//*******************************************************************
//Should getApplicationsByEmployers accomplish this?????
//*******************************************************************
function getJobOffersByUser(userId, callback){			
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getJobOffersByUser?userId=' + userId,
        dataType: 'json',
		success: _success,
        error: _error
    });
	
	function _success(response){
		//alert("success getJobOffersByUser");			
		callback(response);
	}
	
	function _error(response){
		alert("error getJobOffersByUser");
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



