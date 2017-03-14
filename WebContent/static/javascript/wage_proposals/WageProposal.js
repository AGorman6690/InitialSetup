$(document).ready(function(){
	
	$(".cancel").click(function(){		
		$(this).closest(".proposal-actions-container").hide();		
	})
	
	$(".accept-employer").click(function(){
		
		var $acceptActionsContainer = $(this).parents(".accept-actions-container").eq(0);

		//Get the wage proposal id and proposal amount
		var wageProposalId = $(this).parents("[data-wage-proposal-id]")
									.eq(0)
									.attr("data-wage-proposal-id");
		
		var days = $acceptActionsContainer.find(".time-container input.days-pre-hire").val();
		var hours = $acceptActionsContainer.find(".time-container input.hours-pre-hire").val();
		var minutes = $acceptActionsContainer.find(".time-container input.minutes-pre-hire").val();
		
		if(days == undefined) days = 0;
		if(hours == undefined) hours = 0;
		if(minutes == undefined) minutes = 0;
		
		broswerIsWaiting(true);
		$.ajax({
			type : "POST",
			url :"/JobSearch/wage-proposal/accept/employer?wageProposalId=" + wageProposalId + "&days=" + days
										+ "&hours=" + hours + "&minutes=" + minutes,			
			headers : getAjaxHeaders(),
			contentType : "application/json",	
			dataType : "json",
		}).done(function(wageProposal) {			

			broswerIsWaiting(false);
			location.reload(true);
			
		}).error(function() {
			broswerIsWaiting(false);
		});	
	})
	
	$(".confirm-counter").click(function(){
		
		var $e = $(this);
		var $calendar = $e.closest(".proposal-actions-container").find(".counter-calendar .calendar").eq(0);	

		
		var employmentProposalDto = {};
		employmentProposalDto.applicationId = $e.closest("tr").attr("data-application-id");
		employmentProposalDto.amount = $e.closest(".proposal-actions-container")
													.find("input.counter-amount").eq(0).val();	
		employmentProposalDto.employmentProposalId = $e.parents("[data-wage-proposal-id]")
															.eq(0)
															.attr("data-wage-proposal-id");

		employmentProposalDto.dateStrings_proposedDates = getSelectedDates($calendar, "yy-mm-dd", "proposed");;
		
		


		// Update the table's row attribute
//		$(this).closest("tr").attr("data-is-sent-proposal", "1");
		
		broswerIsWaiting(true);
		$.ajax({
			type : "POST",
			url :"/JobSearch/employment-proposal/counter",
			headers : getAjaxHeaders(),
			contentType : "application/json",
			data : JSON.stringify(employmentProposalDto)			
		}).done(function() {	
			
			broswerIsWaiting(false);
			location.reload(true);

		}).error(function() {
			
			broswerIsWaiting(false);
		});	
	})
	
	initCalendars_counterCalendars();
	
})

//function getDateStrings_proposedDates($e){
//	
//	var dateStrings_proposedDates = [];
//	var workDayProposalDto = {};
//
//	var $calendar = $e.closest(".proposal-actions-container").find(".counter-calendar .calendar").eq(0);	
//	var proposedDates = getSelectedDates($calendar, "yy-mm-dd", "proposed");
//	
//	$(proposedDates).each(function(){
//		dateStrings_proposedDates.push(this);
//	})
//	
//	return workDayProposalDtos;
//}
//
//function getWageProposalDto($e){
//	
//	var wageProposalDto = {};
//	
//	wageProposalDto.wageProposalIdToCounter = $e.parents("[data-wage-proposal-id]")
//														.eq(0)
//														.attr("data-wage-proposal-id");
//
//	wageProposalDto.counterAmount = $e.closest(".proposal-actions-container")
//											.find("input.counter-amount").eq(0).val();;	
//
//	return wageProposalDto;
//}

function initCalendars_counterCalendars(){
	
	$(".counter-calendar .calendar").each(function(){
		
		var $container = $(this).closest(".counter-calendar-container");
		
		
		var dates_application = getDateFromContainer($container.find(".work-days-application"));
		var dates_job = getDateFromContainer($container.find(".work-days-job"));
		var dates_unavailable = getDateFromContainer($container.find(".work-days-unavailable"));
		
		initCalendar_counterApplicationDays($(this), dates_application, dates_job, dates_unavailable)
	})
}