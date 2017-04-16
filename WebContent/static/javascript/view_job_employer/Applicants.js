$(document).ready(function(){
	
	$("#table_headerAnswers td input.show-question-and-answers").change(function(){
		
		var doShow = $(this).prop("checked");
		var questionId = $(this).attr("data-question-id");
		var $es = $("tbody td .question-container[data-question-id='" + questionId + "'");
		
		if(doShow){
			$es.each(function(){
				$(this).show();
				$(this).addClass("displayed");
			})
		}else{
			$es.each(function(){
				$(this).hide();
				$(this).removeClass("displayed");
			})
		}
		
	})

	$("body").on("click",".show-all-questions", function(){
		showAllQuestions($(this));
	})
	
	
	$(".rating-loading").rating({
		min: 0,
		max: 5,
		step: 0.1,
		stars: 5,
		displayOnly: true
	
	});
	
	$("#tileView_applicants").click(function(){
		
		if($("#applicantsTable").hasClass("table-view")) showTileView(true);
		
	})
	
	$("#tableView_applicants").click(function(){
		if($("#applicantsTable").hasClass("tile-view")) showTileView(false);
	})
	
	
	
	$(".favorite-flag").click(function(){ updateApplicationStatus($(this)) })
	
})


function updateApplicationStatus($e){


	var applicationDto = {};
	applicationDto.application = {};
	
	var $tr = $e.closest("tr");
	applicationDto.application.applicationId = $tr.attr("data-application-id");
	
	toggleClasses($e, "glyphicon-star", "glyphicon-star-empty");
	
	if($e.hasClass("glyphicon-star")) applicationDto.newStatus = 2;
	else applicationDto.newStatus = 0;
	
	// This is very hackish.
	// A string-array, with zero as an element, is set because this will ensure the
	// "All" filter on the "Name" column will still work after this row's application-status attribute
	// is updated below.
	$tr.attr("data-application-status", "[0," + applicationDto.newStatus + "]"); 
	
	$.ajax({
		type : "POST",
		url : '/JobSearch/application/status/update',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data: JSON.stringify(applicationDto),
		success: _success,
		error: _error
	})
	
	function _success(){		
		
		
	}
	
	function _error(){
// 		alert("status error")
	}
}


function showAllQuestions($e){
	
	var doShowAllQuestions = false;
	if($e.hasClass("glyphicon-menu-down")) doShowAllQuestions = true;

	$e.siblings(".question-container:not(.displayed)").each(function(){
		if(doShowAllQuestions) $(this).show();
		else $(this).hide();
	})
	
	toggleClasses($e, "glyphicon-menu-up", "glyphicon-menu-down");
}	
	
function showTileView(request){
	
	
	
	if(request){
		$("#applicantsTable").find("> tbody > tr > td, > thead > th").each(function(){
			if($(this).hasClass("tile-view")) $(this).show();
			else $(this).hide();
		})
		
		
	}else{
		$("#applicantsTable").find("> tbody > tr > td, > thead > th").each(function(){
			if($(this).hasClass("table-view")) $(this).show();
			else $(this).hide();
		})
	}
	
	toggleClasses($("#applicantsTable"), "tile-view", "table-view");
	
	
}				
	

	