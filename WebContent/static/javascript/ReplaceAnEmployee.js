$(document).ready(function() {
	
	$("#select-an-employee-cont p").click(function(){
		$("#post-select").show();
		
		$("#select-an-employee-cont [data-toggle-id").click();
		
		var $e = $("#employee-to-replace-name");
		$e.html($(this).html());
		$e.attr("data-user-id", $(this).attr("data-user-id"));
	})	
	
	$("#send-request button").click(function() {
		var jobId = $("#jobId").val();
		var userId_employeeToReplace = $("#employee-to-replace-name").attr("data-user-id");
		executeAjaxCall_replaceAnEmployee(jobId, userId_employeeToReplace);
	})
})

function executeAjaxCall_replaceAnEmployee(jobId, userId_employeeToReplace) {
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: "/JobSearch/job/" + jobId + "/replace-employee/" + userId_employeeToReplace,
		dataType: "text",
		success: function() {
			redirectToProfile();
		}
	})
}