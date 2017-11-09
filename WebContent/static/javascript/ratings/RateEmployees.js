$(document).ready(function() {
	$("#employees p").click(function(){    	
    	var userId = $(this).attr("data-user-id");
    	$(".user-cont").hide();
    	$("#ratings-cont").find(".user-cont[data-user-id='" + userId + "']").eq(0).show();
    	highlightArrayItem($(this), $("#employees p"), "selected");
    })	
    
	$("#next-employee").click(function() {
		if($("#employees p.selected.last").length) $("#employees p").eq(0).click();
		else $("#employees p.selected").parent().next().children().eq(0).click();
	})
	
	$("#submit-employee-ratings").click(function() {
		if(isInputValid()){
			$(".error-message").hide();
			submitEmployeeRatings();
		}else{
			$(".error-message").show();
		}
	})
	
	selectFirstEmployee();
}) 
function selectFirstEmployee(){
	$("#employees").find(".employee p").eq(0).click();
}
function submitEmployeeRatings(){
	var submitEmployeeRatingsRequest = getSubmitEmployeeRatingsRequest();
	$.ajax({
		type: "PUT",
		url: '/JobSearch/ratings/rate/employees',
		contentType : "application/json",
		headers : getAjaxHeaders(),
		data: JSON.stringify(submitEmployeeRatingsRequest),
		dataType: "text",
    }).done(function(response){
    	window.location = "/JobSearch/user";
    })
}
function getSubmitEmployeeRatingsRequest() {
	var submitRatingRequests = [];
	var jobId = $("#job-id").val();
	
	// each user
	$(".user-cont").each(function() {
		var userId_employee = $(this).attr("data-user-id");		
		var submitRatingRequest = {};	
		
		submitRatingRequest.jobId = jobId;
		submitRatingRequest.userId_ratee = userId_employee;
		submitRatingRequest.comment = $(this).find("textarea.comment").eq(0).val();
		submitRatingRequest.rateCriteria = [];
		
		// each rate criterion
		$(this).find(".rate-criterion").each(function() {
			var rateCriterion = {};
			rateCriterion.rateCriterionId = $(this).attr("data-rate-criterion-id");
			rateCriterion.value = $(this).attr("data-value");
			submitRatingRequest.rateCriteria.push(rateCriterion);
		})				
		submitRatingRequests.push(submitRatingRequest);
	})
	
	return submitRatingRequests;
}
