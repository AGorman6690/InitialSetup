$(document).ready(function() {
	renderStars($("body"));
	
	$("body").on("click", ".show-applicant-ratings-mod .user-rating", function() {
		var userId = $(this).attr("data-user-id");
		var $e_render = $(this).closest(".show-applicant-ratings-mod").find(".mod-body");
		executeAjaxCall_getRatingsByUser(userId, $e_render);
	})
	
})

function renderStars($e_container){
	$e_container.find(".rating-loading").rating({
		min: 0,
		max: 5,
		step: 0.1,
		stars: 5,
		displayOnly: true	
	});
}


function executeAjaxCall_getRatingsByUser(userId_applicant, $e_renderHtml) {
	
	$.ajax({
		type: "GET",
		url: "/JobSearch/rating/user/" + userId_applicant,
		headers: getAjaxHeaders(),
		dataType: "html",
	}).done(function (html) {
		$e_renderHtml.html(html);
		renderStars($e_renderHtml);
		$e_renderHtml.closest(".mod").show();		
	})
	
}