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

//
//function updateApplicationStatus(applicationId, status){
//
//	var headers = {};
//	headers[$("meta[name='_csrf_header']").attr("content")] = $(
//			"meta[name='_csrf']").attr("content");
//
//	$.ajax({
//	type: "POST",
//	url: environmentVariables.LaborVaultHost + '/JobSearch/application/status/update?id=' + applicationId + "&status=" + status,
//	headers : headers,
//	success: _success,
//    error: _error
//    });
//
//	function _success(response){
//	}
//
//	function _error(response, errorThrown){
//		alert("error hireApplicant");
//
//	}
//}

//function rateEmployee(reviewDTO){
//
//
//	var headers = {};
//	headers[$("meta[name='_csrf_header']").attr("content")] = $(
//			"meta[name='_csrf']").attr("content");
//
//
////	var rating = {};
////	rating.rateCriterionId = rateCriterionId;
////	rating.value = value;
////	rating.jobId = jobId;
////	rating.employeeId = employeeId;
//
//	$.ajax({
//		type: "POST",
//		url: environmentVariables.LaborVaultHost + '/JobSearch/user/rate',
//			contentType : "application/json",
//			headers : headers,
////			dataType: "application/json",
//			data: JSON.stringify(reviewDTO), // +
////					"&endorsements=" + JSON.stringify(endorsements),
//	        success: _success,
//	        error: _error
//	    });
//
//		function _success(){
////			alert("success rateEmpoyee");
//			//callback(response);
//		}
//
//		function _error(response, errorThrown){
//			alert("error rateEmpoyee");
//		}
//}
