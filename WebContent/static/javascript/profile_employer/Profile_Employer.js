$(document).ready(function(){
	
//	$(".employer-profile.select-page-section-container .select-page-section").click(function(){
	$("#table_jobsWaitingToStart thead th button").click(function(){
		
		var perspective = $(this).attr("data-perspective");
		showColumnsForPerspective(perspective);
		addPerspectiveToTable(perspective)
	})
	
	initPage();
	
})

function initPage(){
	$("#table_jobsWaitingToStart thead th button.select-on-load").eq(0).click();
}

function showColumnsForPerspective(perspective){
	
	$table = $("#table_jobsWaitingToStart");
	
//	$table.slideUp(200, function(){
//		
//		
//		$table.find("th, td").each(function(){
//			
//			if($(this).hasClass(perspective)) $(this).show();
//			else if($(this).hasClass("perm") == 0) $(this).hide();
////			else $(this).hide();
//			
//			
//		})	
//		
//		
//		$table.slideDown(1000);
//		
//	});
	
	

	$table.find("th, td").each(function(){
		
		if($(this).hasClass(perspective)) $(this).show();
		else if($(this).hasClass("perm") == 0) $(this).hide();
//		else $(this).hide();
		
		
	})	
	
	
	
}

function addPerspectiveToTable(perspective){
	
	$table = $("#table_jobsWaitingToStart");
	
	$table.removeClass("job-details");
	$table.removeClass("wage-proposal");
	$table.removeClass("application");
	$table.removeClass("employee");
	
	$table.addClass(perspective)
	
}