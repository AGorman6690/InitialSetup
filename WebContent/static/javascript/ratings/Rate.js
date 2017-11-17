$(document).ready(function() {
	// ************************************************************
	$(".rate-criterion button.yes").click(function() {
		var $rateCriterion = $(this).closest(".rate-criterion")
		var $userCont = $(this).closest(".user-cont");
		
		$rateCriterion.attr("data-value", "5");		
		inspectIsRateCriterionComplete($rateCriterion);
		inspectIsEmployeeCompletelyRated($userCont, false);
		showIfNoContainer(false, $(this));
	})
	
	$(".rate-criterion button.no").click(function() {
		var $rateCriterion = $(this).closest(".rate-criterion");
		var $userCont = $(this).closest(".user-cont");
		
		showIfNoContainer(true, $(this));
		
//		if($rateCriterion.find(".if-no").length){
			
			$rateCriterion.attr("data-value", $(this).val());
			$rateCriterion.find(".if-no input:checked").prop("checked", false);
			
//			inspectIsRateCriterionComplete($rateCriterion);
			inspectIsEmployeeCompletelyRated($userCont, false);
			
			
//		}else if($(this).val()!= undefined){
		
//			$rateCriterion.attr("data-value", $(this).val());		
//			
//			inspectIsRateCriterionComplete($rateCriterion);
//			inspectIsEmployeeCompletelyRated($userCont, false);
//		}				
	})
	
	$(".rate-criterion .if-no input[type=radio]").change(function() {
		var $rateCriterion = $(this).closest(".rate-criterion")
		var $userCont = $(this).closest(".user-cont");
		
		$rateCriterion.attr("data-value", $(this).val());		
		inspectIsRateCriterionComplete($rateCriterion);
		inspectIsEmployeeCompletelyRated($userCont, false);
	})
	// ************************************************************
	

	
	$("button.no").click(function() {
//		showIfNoContainer(true, $(this));
//		$userCont = $(this).closest(".user-cont"); 
//		$(this).siblings("button.yes").eq(0).attr("data-is-selected", 0);
//		if($userCont.attr("data-star-rating-value") == "5"){
//			$userCont.find(".star-rating").rating("update", 4);
//		}
	})
  
	$(".if-no input").change(function() {		
		if($(this).is(":checked")){
			$rateCriterion = $(this).closest(".rate-criterion");
			$rateCriterion.find("button.no").attr("data-rating-value", $(this).val());
		}			
	})
    $(".star-rating").rating({
        step: 1,
        hoverChangeStars: false,
    });
	
	
	$(".star-rating").rating().on('rating.change', function(event, value, caption) {	
		$(this).closest(".star-rating-cont").slideUp(0);
		var $userCont = $(this).closest(".user-cont");		
		if(value == "5") $userCont.find("button.yes").click();
		else {
			if($userCont.find("button.yes[data-is-selected=1]").length == 4)
				$userCont.find("button.yes").removeClass("selected");
		}
		$userCont.find(".rating-criteria-cont").eq(0).slideDown(700);
		$userCont.attr("data-star-rating-value", value);
	});	
		

})
function showIfNoContainer(request,$e) {
	var $cont = $e.closest(".rate-criterion").find(".if-no").eq(0);
	if(request) $cont.slideDown(400);
	else $cont.slideUp(400);
}
function isInputValid() {
	var isValid = true;
	var $userCont;		
	$(".user-cont").each(function(i, userCont) {		
		$(userCont).find(".rate-criterion").each(function(j, rateCriterion) { 			
			if(!inspectIsRateCriterionComplete($(rateCriterion))){
				isValid = false;
			}
		})					
		inspectIsEmployeeCompletelyRated($(userCont), true);				
	})	
	return isValid;
}
function getSubmitRatingsRequest($userCont) {
		
	var submitRatingRequest = {};			
	submitRatingRequest.jobId = $("#job-id").val();;
	submitRatingRequest.userId_ratee = $userCont.attr("data-user-id");	;
	submitRatingRequest.comment = $userCont.find("textarea.comment").eq(0).val();
	submitRatingRequest.rateCriteria = [];
	
	// each rate criterion
	$userCont.find(".rate-criterion").each(function() {
		var rateCriterion = {};
		rateCriterion.rateCriterionId = $(this).attr("data-rate-criterion-id");
		rateCriterion.value = $(this).attr("data-value");
		submitRatingRequest.rateCriteria.push(rateCriterion);
	})				
	return submitRatingRequest;
}
function inspectIsEmployeeCompletelyRated($userCont, doAddInvalidClass) {
	
	var userId = $userCont.attr("data-user-id");
	var $employee = $("#employees p[data-user-id=" + userId + "]").eq(0)
							.closest(".employee");
	
	var incompleteRatings = getIncompleteRatings($userCont);
	if(incompleteRatings.length > 0){
		if(doAddInvalidClass){
			$employee.addClass("invalid");			
		}
		$employee.removeClass("complete");
	}else{
		 $employee.removeClass("invalid");
		 $employee.addClass("complete");
	}	
}

function inspectIsRateCriterionComplete($rateCriterion){
	attrValue = $rateCriterion.attr("data-value");
	if( attrValue == undefined || attrValue < 0){
		$rateCriterion.addClass("invalid");	
		return false;
	}else{
		$rateCriterion.removeClass("invalid");
		return true;
	}
}
function getIncompleteRatings($userCont){
	var result = [];
	$userCont.find(".rate-criterion").each(function() {
		attrValue = $(this).attr("data-value");
		if( attrValue == undefined || attrValue < 0 || attrValue === "") result.push($(this));
	})
	return result;
}