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
//PURPOSE: This will populate an array of user objects into a ***select element***
	
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

function popUl_user(arr, e){
//PURPOSE: This will populate an array of user objects into an ***unordered list element***
	
	//	alert("popUl_user");
		e.empty();
		var i;
		for(i=0; i<arr.length; i++){
			e.append('<li value=' + arr[i].userId + '>' + arr[i].firstName + '</li>');
		}
		
		//Make this more robust. It's hardcoded with respect to 
		//************************************************
		//Add click event for each list item.
		//When a list item is clicked, the following will be displayed
		e.on('click', 'li', function(){
			$('#selectedEmployee').html("How did " + this.innerText + " perform?");
			
			//Store the employee's id in the input element's value attribute
			$('#selectedEmployee').val($(this).val());		
		})
		//************************************************
	}

