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

function getApplicants(jobId, callback){
	//alert("getApplicants");
	$.ajax({
		type: "GET",
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/getApplicants?jobId=' + jobId,
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){
			//alert("success getApplicants");
			callback(response);
		}

		function _error(response, errorThrown){
			alert("error getApplicants");
		} 
}

function getOfferedApplicantsByJob(jobId, callback){
	//alert("getOfferedApplicantsByJob");
	$.ajax({
		type: "GET",
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/getOfferedApplicantsByJob?jobId=' + jobId,
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
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/getEmployeesByJob?jobId=' + jobId,
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


function hireApplicant(userId, jobId, callback){
	
//		alert(userId)
//		alert(jobId)
	$.ajax({
		type: "GET",
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/user/hire?userId=' + userId + '&jobId=' + jobId,
        dataType: 'json',
		success: _success,
        error: _error
	    });
		
		function _success(response){
//			alert("success hire applicant");
//			alert(JSON.stringify(response))
			callback(response);
		}
		
		function _error(response, errorThrown){
			alert("error hireApplicant");
		}
}

function getProfiles(callback){
	//	alert("1");
	$.ajax({
		type: "GET",
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/getProfiles',
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
