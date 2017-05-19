$(document).ready(function() {
	renderStars($("body"));
	

	
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
		url: "/JobSearch/user/" + userId_applicant + "/ratings",
		headers: getAjaxHeaders(),
		dataType: "html",
	}).done(function (html) {
		$e_renderHtml.html(html);
		renderStars($e_renderHtml);
		$e_renderHtml.closest(".mod").show();		
	})
	
}