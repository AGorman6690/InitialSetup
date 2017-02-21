$(document).ready(function(){
	
	$("#loadCurrentJobContainer select").change(function(){
		alert($(this).find("option:selected").attr("data-job-id"));
	})
	
})