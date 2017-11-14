$(document).ready(function() {
	
	$(".proposal-detail input").change(function() {
		var $proposalDetails = $(this).closest(".proposal-details");
		var jobId = $proposalDetails.attr("data-job-id");
		var applicationProgressRequest = getApplicationProgressRequest($proposalDetails);
		executeAjaxCall_getFullDetailsForJob(jobId, $proposalDetails, applicationProgressRequest);	
		showSortWrapper(true);		
	})	
	
//	$(".proposal-detail .new").click(function() {
//		var $proposalDetails = $(this).closest(".proposal-details");
//		var jobId = $proposalDetails.attr("data-job-id");
//		var applicationProgressRequest = getApplicationProgressRequest_newProposals();
//		executeAjaxCall_getFullDetailsForJob(jobId, $proposalDetails, applicationProgressRequest);	
//		showSortWrapper(true);		
//	})	
	
	$("#surpress-certain-details").click(function() {
		if($(this).is(":checked")) $("#job-details").addClass("surpress");
		else $("#job-details").removeClass("surpress");
	})
	
	$("body").on("click", ".show-job-info-mod-employer", function() {
		var jobId = $(this).attr("data-job-id");
		executeAjaxCall_showJobInfoMod(jobId, "waiting", "2", function() {			
			selectAllWorkDays($(".calendar"), workDayDtos)
		});
	})
})
function getApplicationProgressRequest($proposalDetails){
	var applicationProgressRequest = {};
	applicationProgressRequest.showProposalsWaitingOnYou = $proposalDetails.find(".waiting-on-you.total").prop("checked");
	applicationProgressRequest.showProposalsWaitingOnOther = $proposalDetails.find(".waiting-on-other").prop("checked");
	applicationProgressRequest.showExpiredProposals = $proposalDetails.find(".expired").prop("checked");
	applicationProgressRequest.showAcceptedProposals = $proposalDetails.find(".accepted").prop("checked");
	applicationProgressRequest.showProposalsWaitingOnYou_new = $proposalDetails.find(".waiting-on-you.new").prop("checked");
	return applicationProgressRequest;
}
function getApplicationProgressRequest_newProposals(){
	var applicationProgressRequest = {};
	applicationProgressRequest.showProposalsWaitingOnYou = false
	applicationProgressRequest.showProposalsWaitingOnOther = false
	applicationProgressRequest.showExpiredProposals = false
	applicationProgressRequest.showAcceptedProposals = false
	applicationProgressRequest.showProposalsWaitingOnYou_new = true;
	return applicationProgressRequest;
}
function executeAjaxCall_getFullDetailsForJob(jobId, $proposalDetails, applicationProgressRequest) {
	broswerIsWaiting(true);
	$.ajax({
		type: "POST",
		url: "/JobSearch/job/" + jobId + "/application-progress",
		headers: getAjaxHeaders(),
		dataType: "html",
		data: JSON.stringify(applicationProgressRequest), 
		contentType: "application/json"
	}).done(function(html) {
		broswerIsWaiting(false);
		$proposalList = $proposalDetails.find(".proposal-list");
		$proposalList.show();
		$proposalList.html(html);
		renderStars($proposalList);
	});
	
}
function showSortWrapper(request){
	
	if(request) $("#sort-wrapper").show(); 
	else $("#sort-wrapper").hide();
}