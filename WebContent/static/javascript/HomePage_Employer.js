var $e_renderJobDetails;
var $e_jobsList;

$(document).ready(function() {
	
	$e_renderJobDetails = $("#job-details");
	$e_jobsList = $("#jobs-list");
	
	$(".see-details").click(function() {
		var jobId = $(this).attr("data-job-id");
		if(!hasJobDetailsBeenLoaded(jobId)){
			executeAjaxCall_getFullDetailsForJob(jobId);	
		}
		showJobDetails(jobId);	
		hideJobSummaries(jobId);
		
		$(this).hide();
		$(this).siblings(".hide-details").show();
		
	})
	
		$(".hide-details").click(function() {
			hideAllJobDetails();	
			showJobSummaries();
			
			$(this).hide();
			$(this).siblings(".see-details").show();
		
	})
})
function hideJobSummaries(jobId_notToHide) {
	$e_jobsList.find(".job-container").each(function() {
		var $e = $(this);
		if($e.attr("data-job-id") == jobId_notToHide) $(this).show();
		else $e.slideUp();
	})
}
function showJobSummaries() {
	$e_jobsList.find(".job-container").each(function() {
		var $e = $(this);
		$e.slideDown();
	})
}
function executeAjaxCall_getFullDetailsForJob(jobId) {
	$.ajax({
		type: "GET",
		url: "/JobSearch/job/" + jobId + "/application-progress",
		dataType: "html",
	}).done(function(html) {
		$e_renderJobDetails.append(html);
		var $e_newJob = $e_renderJobDetails.find("[data-job-id=" + jobId + "]").eq(0);
		renderStars($e_newJob);
	});
	
}
function hasJobDetailsBeenLoaded(jobId){
	if($e_renderJobDetails.find("[data-job-id=" + jobId + "]").length) return true;
	else return false;
}
function showJobDetails(jobId){
	
	$e_renderJobDetails.find(".job-detail:visible").each(function(){
		$(this).hide();		
	})

	$e_renderJobDetails.find(".job-detail[data-job-id=" + jobId + "]").eq(0).show();	
		
}
function hideAllJobDetails(){
	
	$e_renderJobDetails.find(".job-detail:visible").each(function(){
		$(this).hide();		
	})

	
		
}