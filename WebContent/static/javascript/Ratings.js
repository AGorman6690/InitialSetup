$(document).ready(function(){	
	
})


function rateEmployee(rateCriterionId, value, jobId, employeeId){
//	alert("rateEmployee");
	
	var rating = {};
	rating.rateCriterionId = rateCriterionId;
	rating.value = value;
	rating.jobId = jobId;
	rating.employeeId = employeeId;
	
	$.ajax({
		type: "POST",
		url: 'http://localhost:8080/JobSearch/user/rate',
			contentType : "application/json",
			dataType: "application/json",
			data: JSON.stringify(rating),
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