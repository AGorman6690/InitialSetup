$(document).ready(function(){
	
	$("#submit-employer-rating").click(function() {
		if (isInputValid()){
			var submitRatingRequest = getSubmitRatingsRequest($(".user-cont").eq(0));
			submitEmployerRating(submitRatingRequest);
		}
	})	

})

function showIfNoContainer(request,$e) {
	var $cont = $e.closest(".rate-criterion").find(".if-no").eq(0);
	if(request) $cont.slideDown(400);
	else $cont.slideUp(400);
}


function submitEmployerRating(request){


	$.ajax({
		type: "PUT",
		url: "/JobSearch/ratings/rate/employer",
		contentType : "application/json",
		headers : getAjaxHeaders(),
		data: JSON.stringify(request),
		dataType: "text",
    }).done(function(response){
    	redirectToProfile();
    })
}

