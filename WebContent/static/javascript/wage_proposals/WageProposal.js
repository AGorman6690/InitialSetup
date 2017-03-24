


$(document).ready(function(){
	
//	$("button.counter").click(function (){ toggleCounterContainer($(this)) })
//	
//	$("button.accept").click(function (){ hideCounterContainer($(this)) })
	
	$(".wage-proposal-container button.accept").click(function() {
		hideCounterContainer($(this));
		setConfirmationContainer_showApprovedWage($(this), true);
	})
	
	
	$(".wage-proposal-container button.counter").click(function() {
		toggleCounterContainer($(this));		 
		setConfirmationContainer_showApprovedWage($(this), false);
	})
	
	$(".work-day-proposal-container button.counter").click(function() {
		toggleCounterContainer($(this));		 
		setConfirmationContainer_showApprovedWorkDays($(this), false);
	})
	
	$(".work-day-proposal-container button.accept").click(function() {
		hideCounterContainer($(this));
		setConfirmationContainer_showApprovedWorkDays($(this), true);
	})
	
	$(".counter-wage input").focusout(function(){
		setConfirmationContainer_showNewProposedWageAmount($(this));
	})
	
	$(".proceed-to-confirmation-container .confirm").click(function (){
		initCalendar_confirmProposedWorkDays($(this));
		setExpirationTimeToConfirm($(this));
		toggleConfirmationContainer($(this));			
		
	})
	
	$(".confirmation-container .send").click(function() { sendEmploymentProposal($(this)) })
	
	$(".confirmation-container .edit").click(function() { toggleConfirmationContainer($(this)) })
	
	$(".approve-by-applicant").click( function() { sendApplicantApproval($(this)) });
	
	
	
	
	
	
	
	
	
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

function getEmploymentProposalDto($responseContainer){

	var employmentProposalDto = {};
	
	employmentProposalDto.applicationId = $responseContainer.attr("data-application-id");
	
	if(isAmountBeingCountered($responseContainer)){
		employmentProposalDto.amount = $(".counter-container.counter-wage input").val();
	}
	
	if(areWorkDaysBeingCountered($responseContainer)){
		employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
								$(".counter-work-days .calendar"), "yy-mm-dd", "proposed");
	}	
	
	if(isSessionUserAnEmployer($responseContainer)){
		employmentProposalDto.days_offerExpires = $responseContainer.find(".set-expiration input.days").val();
		employmentProposalDto.hours_offerExpires = $responseContainer.find(".set-expiration input.hours").val();
		employmentProposalDto.minutes_offerExpires = $responseContainer.find(".set-expiration input.minutes").val();		
	}
	
	return employmentProposalDto;
}

function isAmountBeingCountered($responseContainer){
	if($responseContainer.find(".wage-proposal-container button.counter.selected").size() > 0)
		return true;
	else return false;
}

function areWorkDaysBeingCountered($responseContainer){
	if($responseContainer.find(".work-day-proposal-container button.counter.selected").size() > 0)
		return true;
	else return false;
}

function isSessionUserAnEmployer($responseContainer){
	if($responseContainer.find(".confirm-expiration-container").size() > 0) return true;
	else return false;
}

function getContext($responseContainer){
	
	if(isAmountBeingCountered($responseContainer) || areWorkDaysBeingCountered($responseContainer))
		return "counter";
	else if(isSessionUserAnEmployer($responseContainer))
		return "accept-by-employer";
	else return "approve-by-applicant";
	
}

function sendApplicantApproval($e){
	
	var context = "approve-by-applicant";
	var employmentProposalDto = {};	
	employmentProposalDto.applicationId = $e.attr("data-application-id");
	
	executeAjaxCall_respondToProposal(employmentProposalDto, context);
}

function sendEmploymentProposal($e){
	
	// ****************************************************************
	// ****************************************************************
	// Need to add verification before sending ajax request
	// ****************************************************************
	// ****************************************************************
	
	var $responseContainer = $e.closest(".response-container");
	var employmentProposalDto = getEmploymentProposalDto($responseContainer);
	var context = getContext($responseContainer);

	executeAjaxCall_respondToProposal(employmentProposalDto, context);
}

function executeAjaxCall_respondToProposal(employmentProposalDto, context){
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/employment-proposal/respond?c=" + context,			
		headers : getAjaxHeaders(),
		data: JSON.stringify(employmentProposalDto),
		contentType : "application/json",	
		dataType : "text",
	}).done(function(response) {			

		broswerIsWaiting(false);
		location.reload(true);
		
	}).error(function() {
		broswerIsWaiting(false);
	});		
}

function setExpirationTimeToConfirm($e){
	var $responseContainer = $e.closest(".response-container");

	var days = $responseContainer.find("input.days").val();
	var hours = $responseContainer.find("input.hours").val();
	var minutes = $responseContainer.find("input.minutes").val();
	
	var html = "";
	
	if(days > 1) html += days + " days ";
	else if(days == 1) html += days + " day ";
	
	if(hours > 1)html += hours + ":";

	
	if(minutes == 0) html += "00";
	else if(minutes < 10) html += "0" + minutes;
	else html += minutes;
	
	if(hours > 1)html += " hrs";
	else if(hours == 1 && minutes > 0) html += " hrs";
	else html += " hr";
	
	
	$responseContainer.find(".confirm-expiration").eq(0).html(html);
}

function setConfirmationContainer_showNewProposedWageAmount($e){
	var $responseContainer = $e.closest(".response-container");
	
	var newProposedWageAmount = $responseContainer.find(".counter-wage input").eq(0).val();
	
	var $span_newAmount = $responseContainer.find(".confirmation-container .confirm-wage-container" +
												" .counter-proposal span.new-proposed-amount").eq(0);
	
	$span_newAmount.html(newProposedWageAmount);
}

function setConfirmationContainer_showApprovedWage($e, doShow){
	
	var $confirmWageContainer = $e.closest(".response-container")
									.find(".confirmation-container .confirm-wage-container");
	
	if(doShow){
		$confirmWageContainer.find(".accept-proposal").eq(0).show();
		$confirmWageContainer.find(".counter-proposal").eq(0).hide();	
	}else{
		$confirmWageContainer.find(".accept-proposal").eq(0).hide();
		$confirmWageContainer.find(".counter-proposal").eq(0).show();
	}
	
	
}

function setConfirmationContainer_showApprovedWorkDays($e, doShow){
	
	var $confirmWorkDaysContainer = $e.closest(".response-container")
									.find(".confirmation-container .confirm-work-days-container");
	
	if(doShow){
		$confirmWorkDaysContainer.find(".accept-proposal").eq(0).show();
		$confirmWorkDaysContainer.find(".counter-proposal").eq(0).hide();	
	}else{
		$confirmWorkDaysContainer.find(".accept-proposal").eq(0).hide();
		$confirmWorkDaysContainer.find(".counter-proposal").eq(0).show();
	}
	
	
}

function toggleCounterContainer($e){
	$e.closest("div.proposal").next("div.counter-container").eq(0).toggle();
}


function hideCounterContainer($e){
	$e.closest("div.proposal").next("div.counter-container").eq(0).hide();
}

function toggleConfirmationContainer($e){
	
	var speed = 700;
	var $responseContainer = $e.closest(".response-container");
	var $confirmationContainer = $responseContainer.find(".confirmation-container").eq(0);
	var $proposalContainer = $responseContainer.find(".proposal-container").eq(0);
	
	if($confirmationContainer.is(":visible")){
		slideUp($confirmationContainer, speed);
		slideDown($proposalContainer, speed);
	}
	else{
		slideDown($confirmationContainer, speed);
		slideUp($proposalContainer, speed);
	}
	
}









function initCalendar_confirmProposedWorkDays($e){
	
	var $responseContainer = $e.closest(".response-container");
	var $calendar_countered = $responseContainer.find(".counter-work-days .calendar").eq(0);
	var dates_proposed = [];

		
	$calendar_confirmProposal = $responseContainer.find(".confirm-work-days-container .calendar");		
	$calendar_confirmProposal.datepicker("destroy");
		
	var $allDaysContainer = $responseContainer.find(".calendar-container-proposed-work-days");

	// If work days were countered
	if($calendar_countered.is(":visible")){
		dates_proposed = getSelectedDates($calendar_countered, undefined, "proposed");
	}else{
		dates_proposed = getDateFromContainer($allDaysContainer.find(".work-days-application"));
	}
	
	var dates_job = getDateFromContainer($allDaysContainer.find(".work-days-job"));
	var dates_unavailable = getDateFromContainer($allDaysContainer.find(".days-unavailable"));
	
	initCalendar_counterApplicationDays($calendar_confirmProposal,
										dates_proposed, dates_job, dates_unavailable);
		

}






function initCalendars_counterCalendars(){
	
	$(".counter-calendar .calendar").each(function(){
		
		var $allDaysContainer = $(this).closest(".calendar-container-proposed-work-days");
		
		
		var dates_application = getDateFromContainer($allDaysContainer.find(".work-days-application"));
		var dates_job = getDateFromContainer($allDaysContainer.find(".work-days-job"));
		var dates_unavailable = getDateFromContainer($allDaysContainer.find(".days-unavailable"));
		
		initCalendar_counterApplicationDays($(this), dates_application, dates_job, dates_unavailable)
	})
}