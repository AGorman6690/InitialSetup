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
		newScrollTop = currentScrollPosition - topFilteredJobsContainer + topClickedJob;
	}
	else{
		//Scroll down
		newScrollTop =  currentScrollPosition + topClickedJob - topFilteredJobsContainer;
	}
	$('#filteredJobs').animate({ scrollTop: newScrollTop}, 1000);
}
