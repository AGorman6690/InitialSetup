$(document).ready(function(){
	$("[data-toggle-id]").click(function(){
		var toggleId = $(this).attr("data-toggle-id");
		$("#" + toggleId).toggle(200);
		toggleClasses($(this), "glyphicon-menu-up", "glyphicon-menu-down");
	})
})