/**
 * 
 */

$(document).ready(function(){
	$('#co_registerUser').click(function(){
		if($('#co_password').val !== $('co_matchingPassword').val){
			return false;
		}
	});
	
	$("#hireApplicant").click(function(){
		
		var eUser = document.getElementById("applicants");
		var userId = eUser.options[eUser.selectedIndex].value;			
		var jobId = $("#activeJobs").val();
		
		hireApplicant(userId, jobId, function(response){			
			populateUsers(response, document.getElementById("employees"));
		});
	});
})

function getApplicants(jobId, callback){
	//alert("getApplicants");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getApplicants?jobId=' + jobId,
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

function hireApplicant(userId, jobId, callback){
	
	//	alert("1");
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/hireApplicant?userId=' + userId + '&jobId=' + jobId,
	        dataType: 'json',
			success: _success,
	        error: _error
		    });
			
			function _success(response){
				//alert("success hire applicant");
				callback(response);
			}
			
			function _error(response, errorThrown){
				alert("error");
			}
	}

function populateUsers(arr, e){ 		
	//alert("sweet populateUsers");		
	var i;
	e.options.length=0;		
	for(i=0; i< arr.length; i++){
		//alert("here");
		var opt = document.createElement('option');
		opt.value = arr[i].userId;
		opt.innerHTML = arr[i].firstName;
		e.appendChild(opt);					
	}		
}