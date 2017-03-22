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

	$(".show-all-questions").click(function(){
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
	
	initCalendars_applicantAvailability();
	
})

function initCalendars_applicantAvailability(){
	
	
	
	$(".availability-calendar-container .calendar").each(function(){
		var $container = $(this).closest(".calendar-container");
		
		var dateStrings_jobWorkDays = getDateFromContainer($container.find(".dates-job-work-days"));
		var dateStrings_applicantProposal = getDateFromContainer($container.find(".dates-applicant-proposal"));
	
		
		$(this).datepicker({
			minDate: getMinDate($(this)),
			numberOfMonths: getNumberOfMonths($(this)),
			beforeShowDay: function (date) {
				 return beforeShowDay_findEmployees_ifUserHasAvailability(
						 						date, dateStrings_jobWorkDays, dateStrings_applicantProposal);
			 }
			
		})
		
	})
	
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
	

	