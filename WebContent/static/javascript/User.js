/**
 *
 */

$(document).ready(function(){
	$('#co_registerUser').click(function(){
		if($('#co_password').val !== $('co_matchingPassword').val){
			return false;
		}
	});

	$("#saveChanges")
	.click(
			function() {

				var editProfileDTO = {};
				editProfileDTO.homeCity = $(
						"#homeCity").val();
				editProfileDTO.homeState = $(
						"#homeState").val();
				editProfileDTO.homeZipCode = $(
						"#homeZipCode").val();
				editProfileDTO.maxWorkRadius = parseInt($(
						"#maxDistance").val());
				editProfileDTO.minPay = $("#minPay")
						.val();

				var categoryIds = [];

				_.each($(".categoryIds"), function(id){
					if(id.checked){
						categoryIds.push(parseInt(parseInt(id.value)));
					}
				});

				editProfileDTO.categoryIds = categoryIds;

				//Verify
				if (areValidSettings(editProfileDTO) == 1) {

					// 			editProfileDTO.categoryIds = [];
					// 			var categories = $('#selectedCategories').find("button");
					// 			for (var i = 0; i < categories.length; i++) {
					// 				var id = categories[i].id;
					// 				editProfileDTO.categoryIds
					// 						.push(id
					// 								.substring(
					// 										0,
					// 										id
					// 												.indexOf("-")));
					// 			}

					var headers = {};
					headers[$(
							"meta[name='_csrf_header']")
							.attr("content")] = $(
							"meta[name='_csrf']")
							.attr("content");

					$
							.ajax(
									{
										type : "POST",
										url : environmentVariables.LaborVaultHost
												+ "/JobSearch/user/settings/edit",
										headers : headers,
										contentType : "application/json",
										dataType : "application/json", // Response
										data : JSON
												.stringify(editProfileDTO)
									}).done(
									function() {

									}).error(
									function() {

									});

				}
			})

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
