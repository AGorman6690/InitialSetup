$(document).ready(function(){	
	
})


function rateEmployee(rateCriterionId, value, jobId, employeeId){
//	alert("rateEmployee");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/rateEmployee?rateCriterionId=' + rateCriterionId + '&value=' + value +
				'&jobId=' + jobId + '&employeeId=' + employeeId,
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){
//			alert("success rateEmpoyee");
			//callback(response);
		}

		function _error(response, errorThrown){
			alert("error rateEmpoyee");
		} 
}