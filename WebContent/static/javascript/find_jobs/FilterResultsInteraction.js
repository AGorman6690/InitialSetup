$(document).ready(function(){
	$("#filteredJobsContainer").on("click", ".job .show-desc", function(){
		
		var jobDescription = $(this).siblings(".job-description")[0];				
		toggleClasses($(jobDescription), "less-description", "more-description");
		
		var siblingShow = $(this).siblings(".show-desc")[0];
		
		$(this).hide();
		$(siblingShow).show();
		
		
//		if($(jobDescription).hasClass("more-description")) $(text_showMoreLess).html("Show Less");
//		else $(text_showMoreLess).html("Show More");
	})
})




function addBorderToJob(jobId){	
	highlightArrayItem($("#" + jobId), $("#filteredJobs").find(".job"), "selected-job");
}

function scrollToJob(jobId){

	var currentScrollPosition = $('#filteredJobs').scrollTop();
	var topFilteredJobsContainer = $('#filteredJobs').offset().top; 
	var topClickedJob = $("#" + jobId).offset().top;
	var newScrollTop;
	
	//If the current job to scroll to is already scrolled past
	if(topClickedJob < topFilteredJobsContainer){
		
		//Scroll up
		newScrollTop = currentScrollPosition - topFilteredJobsContainer + topClickedJob - 5;
	}
	else{
		//Scroll down
		newScrollTop =  currentScrollPosition + topClickedJob - topFilteredJobsContainer - 5;
	}
	$('#filteredJobs').animate({ scrollTop: newScrollTop}, 1000);
}

