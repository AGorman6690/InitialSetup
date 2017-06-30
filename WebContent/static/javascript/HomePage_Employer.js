$(document).ready(function() {
	$(".see-full-details").click(function() {
		var jobId = $(this).attr("data-job-id");
		executeAjaxCall_getFullDetailsForJob(jobId);
	})
})

function executeAjaxCall_getFullDetailsForJob(jobId) {
	$.ajax({
		type: "GET",
		
	})
	
}