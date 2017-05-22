$(document).ready(function() {
	$("body").on("click", "#confirm-employee-removal", function () {
		var jobId = $("#jobId").val();
		var userId_employee = $(this).attr("data-user-id");
		executeAjaxCall_removeEmployee(jobId, userId_employee);
	})
})

function executeAjaxCall_displayMessage_terminateEmployee(jobId, userId_employee) {
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: "/JobSearch/job/" + jobId + "/employee/" + userId_employee + "/display-termination-message",
		dataType: "html"
	}).done(function(html) {
		$("#terminate-employee-message .mod-body").html(html);
		$("#terminate-employee-message.mod").show();
	})
}
function executeAjaxCall_removeEmployee(jobId, userId) {
	$.ajax({
		type: "POST",
		url: "/JobSearch/job/" + jobId + "/user/" + userId + "/remove-remaining-work-days",
		headers: getAjaxHeaders(),
		dataType: "text"
	}).done(function(response) {
		location.reload(true);
	})
}