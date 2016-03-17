$(document).ready(function(){	
	
})


function rateEmployee(rateCriterionId, value, jobId, employeeId){
//	alert("rateEmployee");
	
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");

	
	var rating = {};
	rating.rateCriterionId = rateCriterionId;
	rating.value = value;
	rating.jobId = jobId;
	rating.employeeId = employeeId;
	
	$.ajax({
		type: "POST",
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/user/rate',
			contentType : "application/json",
			headers : headers,			
//			dataType: "application/json",
			data: JSON.stringify(rating),
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