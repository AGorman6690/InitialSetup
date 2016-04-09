/**
 *
 */

$(document).ready(function(){
	$('#co_registerUser').click(function(){
		if($('#co_password').val !== $('co_matchingPassword').val){
			return false;
		}
	});

})

//function getApplicants(jobId, callback){
//	//alert("getApplicants");
//	$.ajax({
//		type: "GET",
//		url: 'http://localhost:8080/JobSearch/getApplicants?jobId=' + jobId,
//			dataType: "json",
//	        success: _success,
//	        error: _error
//	    });
//
//		function _success(response){
//			//alert("success getApplicants");
//			callback(response);
//		}
//
//		function _error(response, errorThrown){
//			alert("error getApplicants");
//		}
//}

function getOfferedApplicantsByJob(jobId, callback){
	//alert("getOfferedApplicantsByJob");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getOfferedApplicantsByJob?jobId=' + jobId,
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){
			//alert("success getOfferedApplicantsByJob");
			callback(response);
		}

		function _error(response, errorThrown){
			alert("error getOfferedApplicantsByJob");
		}
}


function getEmployeesByJob(jobId, callback){
	//alert("getEmployees");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getEmployeesByJob?jobId=' + jobId,
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){
		//	alert("success getEmployees");
			callback(response);
		}

		function _error(response, errorThrown){
			alert("error getEmployees");

		} 
}



function updateApplicationStatus(applicationId, status){
	
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");
	
	$.ajax({
	type: "POST",
	url: 'http://localhost:8080/JobSearch/application/status/update?id=' + applicationId + "&status=" + status,
	headers : headers,		
	success: _success,
    error: _error
    });
	
	function _success(response){
	}
	
	function _error(response, errorThrown){
		alert("error hireApplicant");

	}
}

function getProfiles(callback){
	//	alert("1");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getProfiles',
        dataType: 'json',
		success: _success,
        error: _error
	    });

		function _success(response){
//			alert("success getProfiles");
			callback(response);
		}

		function _error(response){
			alert(JSON.stringify(response))
			alert("error getProfiles");
		}
}

//function rateEmployee(rateCriterionId, value, jobId, employeeId){
function rateEmployee(reviewDTO){

	
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");

	
//	var rating = {};
//	rating.rateCriterionId = rateCriterionId;
//	rating.value = value;
//	rating.jobId = jobId;
//	rating.employeeId = employeeId;
	
	$.ajax({
		type: "POST",
		url: 'http://localhost:8080/JobSearch/user/rate',
			contentType : "application/json",
			headers : headers,			
//			dataType: "application/json",
			data: JSON.stringify(reviewDTO), // + 
//					"&endorsements=" + JSON.stringify(endorsements),
	        success: _success,
	        error: _error
	    });

		function _success(){
//			alert("success rateEmpoyee");
			//callback(response);
		}

		function _error(response, errorThrown){
			alert("error rateEmpoyee");
		} 
}
