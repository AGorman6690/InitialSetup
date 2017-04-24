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