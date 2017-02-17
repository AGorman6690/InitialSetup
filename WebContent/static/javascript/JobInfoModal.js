var initMapCount = 0;


$(document).ready(function(){
	
	$(".display-job-info").click(function(){
		
		broswerIsWaiting(true);
		$.ajax({
			type : "POST",
			url :"/JobSearch/preview/job-info/" + $(this).attr("data-job-id"),
			headers : getAjaxHeaders(),
			dataType : "html",
			async: false,
			success : _success,
			error : _error,
			cache: true
		})
		
		function _success(html_jobInfo) {
			
			broswerIsWaiting(false);
			$("#jobInfoModal .mod-body").html(html_jobInfo);
			$("#jobInfoModal").show();
			
			setWorkDays();
			initCalendar_JobInfo();
						
			initMap();
			initMapCount = 1;
					
			
		}
		function _error(html_jobInfo) {
			
			broswerIsWaiting(false);
		};
	})
})