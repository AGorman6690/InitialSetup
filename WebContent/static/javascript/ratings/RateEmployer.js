$(document).ready(function(){
	
	
	
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
		submitRatings();
	})
    
	
	initPage();
	
})

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
			rateCriterion.value = getRatingValue($(this));
			submitRatingDto.rateCriteria.push(rateCriterion);
		})
		submitRatingDto.commentString = $(this).find("textarea.comment").eq(0).val();
		
		submitRatingDtos.push(submitRatingDto);
	})
	
	return submitRatingDtos;
}
function getRatingValue($e) {
	if($e.find("button.yes").hasClass("selected")) return 5;
	else return $e.find("button.no").attr("data-rating-value");
}
function submitRatings(){
	
//	if(areInputsValid()){
		
		var submitRatingDtos = getSubmitRatingDtos();
	
		$.ajax({
			type: "POST",
			url: '/JobSearch/user/rate/employees',
			contentType : "application/json",
			headers : getAjaxHeaders(),
			data: JSON.stringify(submitRatingDtos),
	        success: _success,
	        error: _error
	    });

		function _success(){
			window.location = "/JobSearch/user/profile";
		}

		function _error(){
		}
			
//	}
}