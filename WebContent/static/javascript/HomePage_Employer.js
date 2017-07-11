var $e_renderJobDetails;
var $e_jobsList;

$(document).ready(function() {
	
	$e_renderJobDetails = $("#job-details");
	$e_jobsList = $("#jobs-list");
	
	$("body").on("click", ".show-applicant-ratings-mod", function() {	
		
		var $e_applicant = $(this).closest(".applicant");
//		if($e_applicant.find(".mod").eq(0).is(":visible")){			
			var userId_applicant = $e_applicant.attr("data-user-id");
			var $e_renderHtml = $e_applicant.find(".ratings-mod-container .mod-body").eq(0);
			executeAjaxCall_getRatingsByUser(userId_applicant, $e_renderHtml);	
//		}
		
	})
	
	$(".see-details").click(function() {
		var jobId = $(this).attr("data-job-id");
		if(!hasJobDetailsBeenLoaded(jobId)){
			executeAjaxCall_getFullDetailsForJob(jobId);	
		}
		showJobDetails(jobId);	
		hideJobSummaries(jobId);
		showSortWrapper(true);
		
		$(this).hide();
		$(this).siblings(".hide-details").show();
		
	})
	
	$(".hide-details").click(function() {
		hideAllJobDetails();	
		showJobSummaries();
		showSortWrapper(false);
		$(this).hide();
		$(this).siblings(".see-details").show();
		
	})
	
	$("#surpress-certain-details").click(function() {
		if($(this).is(":checked")) $("#job-details").addClass("surpress");
		else $("#job-details").removeClass("surpress");
	})
	
	$("body").on("click", ".show-job-info-mod-employer", function() {
		var jobId = $(this).attr("data-job-id");
		executeAjaxCall_showJobInfoMod(jobId, "waiting", "2", function() {
			
			selectAllWorkDays($(".calendar"), workDayDtos)
			
//			$(".calendar").find("td.job-work-day").each(function() {	
				
//				$(this).click();
//				$(this).addClass("is-proposed");
//				selectAllWorkDays($(".calendar"), workDayDtos)
//			})
		});
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

function showSortWrapper(request){
	
	if(request) $("#sort-wrapper").show(); 
	else $("#sort-wrapper").hide();
}