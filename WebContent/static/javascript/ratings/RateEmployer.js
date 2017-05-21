$(document).ready(function(){
	
	// ************************************************************
	$(".rate-criterion button.yes").click(function() {
		var $rateCriterion = $(this).closest(".rate-criterion")
		var $userCont = $(this).closest(".user-cont");
		
		$rateCriterion.attr("data-value", "5");		
		inspectIsRateCriterionComplete($rateCriterion);
		inspectIsEmployeeCompletelyRated($userCont, false);
		
	})
	
	$(".rate-criterion button.no").click(function() {
		var $rateCriterion = $(this).closest(".rate-criterion");
		var $userCont = $(this).closest(".user-cont");
		
		if(!$rateCriterion.find(".if-no").length){
			$rateCriterion.attr("data-value", $(this).val());		
			
			inspectIsRateCriterionComplete($rateCriterion);
			inspectIsEmployeeCompletelyRated($userCont, false);
//		}else if($(this).val()!= undefined){
		
//			$rateCriterion.attr("data-value", $(this).val());		
//			
//			inspectIsRateCriterionComplete($rateCriterion);
//			inspectIsEmployeeCompletelyRated($userCont, false);
		}				
	})
	
	$(".rate-criterion .if-no input[type=radio]").change(function() {
		var $rateCriterion = $(this).closest(".rate-criterion")
		var $userCont = $(this).closest(".user-cont");
		
		$rateCriterion.attr("data-value", $(this).val());		
		inspectIsRateCriterionComplete($rateCriterion);
		inspectIsEmployeeCompletelyRated($userCont, false);
	})
	// ************************************************************
	
	$("#next-employee").click(function() {
		if($("#employees p.selected.last").length) $("#employees p").eq(0).click();
		else $("#employees p.selected").parent().next().children().eq(0).click();
	})
	
	$("button.no").click(function() {
		showIfNoContainer(true, $(this));
		$userCont = $(this).closest(".user-cont"); 
		$(this).siblings("button.yes").eq(0).attr("data-is-selected", 0);
		if($userCont.attr("data-star-rating-value") == "5"){
			$userCont.find(".star-rating").rating("update", 4);
		}
	})
  
	$(".if-no input").change(function() {		
		if($(this).is(":checked")){
			$rateCriterion = $(this).closest(".rate-criterion");
			$rateCriterion.find("button.no").attr("data-rating-value", $(this).val());
		}			
	})
	$("button.yes").click(function() {
		showIfNoContainer(false, $(this));
		$userCont = $(this).closest(".user-cont");
		
		$(this).attr("data-is-selected", 1);
		
		if($userCont.find("button.yes[data-is-selected=1]").length == 4)
			$userCont.find(".star-rating").rating("update", 5);		
		
	})
	
    $("#employees p").click(function(){    	
    	var userId = $(this).attr("data-user-id");
    	$(".user-cont").hide();
    	$("#ratings-cont").find(".user-cont[data-user-id='" + userId + "']").eq(0).show();
    	highlightArrayItem($(this), $("#employees p"), "selected");
    })
    
    $(".star-rating").rating({
        step: 1,
        hoverChangeStars: false,
    });
		
	$('.star-rating').on('rating.change', function(event, value, caption) {
		
		$(this).closest(".star-rating-cont").hide();
		
		
		var $userCont = $(this).closest(".user-cont");		
		if(value == "5") $userCont.find("button.yes").click();
		else {
			if($userCont.find("button.yes[data-is-selected=1]").length == 4)
				$userCont.find("button.yes").removeClass("selected");
		}
		
		$userCont.find(".rating-criteria-cont").eq(0).show();
		$userCont.attr("data-star-rating-value", value);
	});	
	
	
	$("#submit").click(function() {
		if(isInputValid()){
			$(".error-message").hide();
			submitRatings();
		}else{
			$(".error-message").show();
		}
	})
    
	
	initPage();
	
})
function inspectIsEmployeeCompletelyRated($userCont, doAddInvalidClass) {
	
	var userId = $userCont.attr("data-user-id");
	var $employee = $("#employees p[data-user-id=" + userId + "]").eq(0)
							.closest(".employee");
	
	var incompleteRatings = getIncompleteRatings($userCont);
	if(incompleteRatings.length > 0){
		if(doAddInvalidClass){
			$employee.addClass("invalid");
			$employee.removeClass("complete");
		}

	}else{
		 $employee.removeClass("invalid");
		 $employee.addClass("complete");
	}	
}
function isInputValid() {
	var isValid = true;
	var $userCont;	
	var userIsCompleteyRated;
	
	var isRatingEmployees = true;
	if($("#main-cont").hasClass("rate-employer")) isRatingEmployees = false;
	
	$(".user-cont").each(function(i, userCont) {
		
		userIsCompleteyRated = true;
		$(userCont).find(".rate-criterion").each(function(j, rateCriterion) { 			
			if(!inspectIsRateCriterionComplete($(rateCriterion))){
				isValid = false;
				userIsCompletelyRated = false;
			}
		})
				
		if(isRatingEmployees){			
			inspectIsEmployeeCompletelyRated($(userCont), true);		
		}
		
	})
	
	return isValid;
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
		if( attrValue == undefined || attrValue < 0) result.push($(this));
	})
	return result;
}
function inspectRatingCompleteness_forEmployee($userCont) {
	var isComplete = true;
	
	var userId = $userCont.attr("data-user-id");
	var attrValue;

	$userCont.find(".rate-criterion").each(function() {
		attrValue = $(this).attr("data-value");
		if( attrValue == undefined || attrValue < 0){
			isComplete = false;
			return false;
		}else{
			$(this).removeClass("invalid");
		}
	})
	
	var employeeListItem = $("#employees p[data-user-id=" + userId + "]").eq(0).closest(".employee");
	if(isComplete){
		employeeListItem.addClass("complete");
		employeeListItem.removeClass("invalid");
	}
	else employeeListItem.removeClass("complete");
	
	
}
function initPage() {
	$("#employees p").eq(0).click();
}

function showIfNoContainer(request,$e) {
	var $cont = $e.closest(".rate-criterion").find(".if-no").eq(0);
	if(request) $cont.show();
	else $cont.hide();
}
function getSubmitRatingDtos() {
	var submitRatingDtos = [];
	var jobId = $("#jobId").val();
	
	$(".user-cont").each(function() {
		var userId_employee = $(this).attr("data-user-id");
		
		var submitRatingDto = {};	
		
		submitRatingDto.jobId = jobId;
		submitRatingDto.userId_ratee = userId_employee;
		
		submitRatingDto.rateCriteria = [];
		$(this).find(".rate-criterion").each(function() {
			var rateCriterion = {};
			rateCriterion.rateCriterionId = $(this).attr("data-rate-criterion-id");
			rateCriterion.value = $(this).attr("data-value");
			submitRatingDto.rateCriteria.push(rateCriterion);
		})
		submitRatingDto.commentString = $(this).find("textarea.comment").eq(0).val();
		
		submitRatingDtos.push(submitRatingDto);
	})
	
	return submitRatingDtos;
}

function submitRatings(){
	var submitRatingDtos = getSubmitRatingDtos();

	$.ajax({
		type: "POST",
		url: '/JobSearch/user/rate/employees',
		contentType : "application/json",
		headers : getAjaxHeaders(),
		data: JSON.stringify(submitRatingDtos),
		dataType: "text",
    }).done(function(response){
    	window.location = "/JobSearch/user/profile";
    })
}