


$(document).ready(function(){
	
	$("body").on("click", ".show-mod", function(){
		$(this).parent().find(".mod").eq(0).show();
	})
	
	
	$(".wage-container button.accept").click(function() {
		hideCounterContainer($(this), true);
	})
	
	$(".wage-container button.counter").click(function() {
		hideCounterContainer($(this), false);
	})

	$(".work-day-container button.accept").click(function() {
		hideProposalContainer($(this), false);
		hideCounterContainer($(this), true);
		
	})
	
	$(".work-day-container button.counter").click(function() {
		hideCounterContainer($(this), false);
		hideProposalContainer($(this), true);
	})

	
	$(".proceed-to-confirmation-container .confirm").click(function (){
		setExpirationTimeToConfirm($(this));
		showConfirmationContainer($(this));			
		
	})
	
	$(".send-proposal-container .send").click(function() { sendEmploymentProposal($(this)) })
	
	$(".cancel").click(function() { $(this).closest(".mod").find(".mod-header .glyphicon-remove").eq(0).click() })
	
	$(".send-proposal-container .edit").click(function() { hideConfirmationContainer($(this)) })
	
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
	
//	initCalendars_counterCalendars();
	initCalendar_proposedWorkDays();
//	initCalendar_counterWorkDays()
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
		employmentProposalDto.amount = $responseContainer.find(".wage-container input.counter-wage-amount").val();
	}
	
	if(areWorkDaysBeingCountered($responseContainer)){
		employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
												$responseContainer.find(".work-day-container .counter-container .calendar"),
													"yy-mm-dd", "proposed-work-day");
	}	
	
	if(isSessionUserAnEmployer($responseContainer)){
		employmentProposalDto.days_offerExpires = $responseContainer.find(".set-expiration input.days").val();
		employmentProposalDto.hours_offerExpires = $responseContainer.find(".set-expiration input.hours").val();
		employmentProposalDto.minutes_offerExpires = $responseContainer.find(".set-expiration input.minutes").val();		
	}
	
	return employmentProposalDto;
}

function isAmountBeingCountered($responseContainer){
	if($responseContainer.find(".wage-container button.counter.selected").size() > 0)
		return true;
	else return false;
}

function areWorkDaysBeingCountered($responseContainer){
	if($responseContainer.find(".work-day-container button.counter.selected").size() > 0)
		return true;
	else return false;
}

function isSessionUserAnEmployer($responseContainer){
	if($responseContainer.attr("data-session-user-is-employer") == "1") return true;
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
		datType: "text"
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
	$e.closest("div.proposal").find(".counter-container").eq(0).toggle();
}


function hideCounterContainer($e, request){
	
	$cont = $e.closest("div.proposal"); 
	if(request){
		$cont.find(".counter-container").eq(0).slideUp();
	}else{
		$cont.find(".counter-container").eq(0).slideDown();
	}

//	if(request){
//		$cont.find(".counter-container").eq(0).animate({width:'hide'}, 500);
//	}else{
//		$cont.find(".counter-container").eq(0).animate({width:'show'}, 500);
//	}
	
}

function hideProposalContainer($e, request){
	
	$cont = $e.closest("div.proposal"); 
	if(request){
		$cont.find(".proposal-container").eq(0).animate({width:'hide'}, 500);
	}else{
		$cont.find(".proposal-container").eq(0).animate({width:'show'}, 500);
	}
	
}

function showConfirmationContainer($e){

	var $responseContainer = $e.closest(".response-container");
	var $wageContainer = $responseContainer.find(".wage-container").eq(0);
	var $workDayContainer = $responseContainer.find(".work-day-container").eq(0);
	var counterWageAmount = 0;
	
	var doAcceptWage = -1;
	if($wageContainer.find("button.accept.selected").size() > 0) doAcceptWage = 1;
	else if($wageContainer.find("button.counter.selected").size() > 0){
		doAcceptWage = 0;
		counterWageAmount = $wageContainer.find(".counter-wage-amount").val()
		$wageContainer.find(".new-proposed-amount").eq(0).html("$ " + counterWageAmount);
	}
	
	var doAcceptWorkDays = -1;
	if($workDayContainer.find("button.accept.selected").size() > 0) doAcceptWorkDays = 1;
	else if($workDayContainer.find("button.counter.selected").size() > 0) doAcceptWorkDays = 0;
	
	if(doAcceptWage != -1){
//		$wageContainer.find(".proposal-container").hide();
//		$wageContainer.find(".counter-container").hide();
//		$wageContainer.find(".button-group").hide();
//		$wageContainer.find(".confirmation-container").show();
		if(doAcceptWage == 1){
			$wageContainer.find(".confirmation-container .accept").eq(0).show();
			$wageContainer.find(".confirmation-container .counter").eq(0).hide();
		}else{
			$wageContainer.find(".confirmation-container .accept").eq(0).hide();
			$wageContainer.find(".confirmation-container .counter").eq(0).show();
		}	
	}
	
	if(doAcceptWorkDays != -1){
//		$workDayContainer.find(".confirmation-container").show();
//		$workDayContainer.find(".button-group").hide();
		if(doAcceptWorkDays == 1){
			$workDayContainer.find(".confirmation-container .accept").eq(0).show();
			$workDayContainer.find(".confirmation-container .counter").eq(0).hide();
		}else{
			$workDayContainer.find(".confirmation-container .accept").eq(0).hide();
			$workDayContainer.find(".confirmation-container .counter").eq(0).show();
		}	
	}	
	
	$responseContainer.addClass("confirm");
//	$responseContainer.find(".proceed-to-confirmation-container").hide();
//	$responseContainer.find(".send-proposal-container").show();
	
}


function hideConfirmationContainer($e){

	var $responseContainer = $e.closest(".response-container");
	var $wageContainer = $responseContainer.find(".wage-container").eq(0);
	var $workDayContainer = $responseContainer.find(".work-day-container").eq(0);
	
	$responseContainer.removeClass("confirm");


//	$responseContainer.find(".proceed-to-confirmation-container").show();
//	$responseContainer.find(".send-proposal-container").hide();
	
//	$workDayContainer.find(".button-group button.selected").eq(0).click();
//	$workDayContainer.find(".confirmation-container").hide();

//	$wageContainer.find(".button-group button.selected").eq(0).click();
//	$wageContainer.find(".confirmation-container").hide();
//	$wageContainer.find(".proposal-container").show();
//	$workDayContainer.find(".button-group").show();
}


function initCalendar_proposedWorkDays(){
	
	$(".work-day-container .calendar").each(function(){
		
		var $responseContainer = $(this).closest(".response-container"); 
		var applicationId = $responseContainer.attr("data-application-id");
		var $allDaysContainer = $responseContainer.find(".dates-container").eq(0);		
		
		var dates_proposed = getDateFromContainer($allDaysContainer.find(".proposal-work-days"));
		var dates_workDays = getDateFromContainer($allDaysContainer.find(".job-work-days"));
		var dates_unavailable = getDateFromContainer($allDaysContainer.find(".days-unavailable"));	
		var dates_other_applications = [];
		$("#all-other-application-dates")
									.find(".work-day:not([data-application-id='" + applicationId + "'])").each(function() {
										dates_other_applications.push(dateify($(this).attr("data-date")));
									})
		
		initCalendar_showWorkDays($(this), dates_workDays, dates_unavailable, dates_proposed,
				dates_other_applications);	
	})

	
}

//function initCalendar_counterWorkDays(){
//	
//	$(".work-day-container .calendar").each(function(){
//		var $allDaysContainer = $(this).closest(".response-container").find(".dates-container").eq(0);
//
//		var dates_proposed = getDateFromContainer($allDaysContainer.find(".proposal-work-days"));
//		var dates_workDays = getDateFromContainer($allDaysContainer.find(".job-work-days"));
//		var dates_unavailable = getDateFromContainer($allDaysContainer.find(".days-unavailable"));	
//		
//		initCalendar_showWorkDays_counterProposal($(this), dates_workDays, dates_unavailable, dates_proposed);	
//	})
//
//	
//}

//function initCalendars_counterCalendars(){
//	
//	$(".counter-calendar .calendar").each(function(){
//		
//		var $allDaysContainer = $(this).closest(".calendar-container-proposed-work-days");
//		
//		
//		var dates_application = getDateFromContainer($allDaysContainer.find(".work-days-application"));
//		var dates_job = getDateFromContainer($allDaysContainer.find(".work-days-job"));
//		var dates_unavailable = getDateFromContainer($allDaysContainer.find(".days-unavailable"));
//		
//		initCalendar_counterApplicationDays($(this), dates_application, dates_job, dates_unavailable)
//	})
//}