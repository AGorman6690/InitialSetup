$(document).ready(function(){
	
	$("#table_headerAnswers td input.show-question-and-answers").change(function(){
		
		var doShow = $(this).prop("checked");
		var questionId = $(this).attr("data-question-id");
		var $es = $("tbody td .question-container[data-question-id='" + questionId + "'");
		if(doShow){
			$es.each(function(){
				$(this).show();
			})
		}else{
			$es.each(function(){
				$(this).hide();
			})
		}
		
	})
	
//	$("#table_headerAnswers td input.filter-answers").change(function(){
//		
//		var checkedValue;
//		var disabledValue;
//		var $tr = $(this).closest("tr");
//		
//		if($(this).is(":checked")){
////			checkedValue = true;
//			disabledValue = false;
//			$tr.find("td.question").eq(0).removeClass("show-disabled");
//			$tr.find("td.answers").eq(0).removeClass("show-disabled");
//		}
//		else{
//			disabledValue = true;
//			checkedValue = true;
//			$tr.find("td.question").eq(0).addClass("show-disabled");
//			$tr.find("td.answers").eq(0).addClass("show-disabled");
//		}
//		
//		
//		// Check the answer checkboxes
//		$tr.find("td.answers input").each(function(){
//			$(this).prop("checked", checkedValue);
//			$(this).prop("disabled", disabledValue);
//		})
//		
//		
//		
//	})
	
	
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
	
function showTileView(request){
	
	
	
	if(request){
		$("#applicantsTable").find("> tbody > tr td, > thead th").each(function(){
			if($(this).hasClass("tile-view")) $(this).show();
			else $(this).hide();
		})
		
		
	}else{
		$("#applicantsTable").find("> tbody > tr td, > thead th").each(function(){
			if($(this).hasClass("table-view")) $(this).show();
			else $(this).hide();
		})
	}
	
	toggleClasses($("#applicantsTable"), "tile-view", "table-view");
	
	
}				
	

	