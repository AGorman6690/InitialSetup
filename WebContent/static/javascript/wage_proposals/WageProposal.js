


$(document).ready(function(){
	
//	initCalendar_proposedWorkDays();

	
	$("body").on("click", ".show-mod", function(){
	
		var applicationId = $(this).closest("tr").attr("data-application-id");
		var $renderHtml = $(this).siblings(".present-proposal").eq(0);
		var $proposalMod = $renderHtml.find(".mod").eq(0);
		
	
		if($proposalMod.length > 0) $proposalMod.show();
		else executeAjaxCall_getProposal(applicationId, $renderHtml);
		
	})
	
	
	$("body").on("click", ".wage-container button.accept", function() {
		hideCounterContainer($(this), true);
	})
	
	$("body").on("click", ".wage-container button.counter", function() {
		hideCounterContainer($(this), false);
	})

	$("body").on("click", ".work-day-container button.accept", function() {
		hideProposalContainer($(this), false);
		hideCounterContainer($(this), true);
		
	})
	
	$("body").on("click", ".work-day-container button.counter", function() {
		hideCounterContainer($(this), false);
		hideProposalContainer($(this), true);
	})

	
	$("body").on("click", ".proceed-to-confirmation-container .confirm", function (){
		setExpirationTimeToConfirm($(this));
		showConfirmationContainer($(this));			
		
	})
	
	$("body").on("click", ".send-proposal-container .send", function() { sendEmploymentProposal($(this)) })
	
	$("body").on("click", ".cancel", function() { $(this).closest(".mod").find(".mod-header .glyphicon-remove").eq(0).click() })
	
	$("body").on("click", ".send-proposal-container .edit", function() { hideConfirmationContainer($(this)) })
	
	$("body").on("click", ".approve-by-applicant", function() { sendApplicantApproval($(this)) });
	

	$("body").on(".cancel", function(){		
		$(this).closest(".proposal-actions-container").hide();		
	})
})


function initCalendar_proposedWorkDays(workDayDtos){
	
	$(".work-day-container .calendar").each(function(){
//		var $responseContainer = $(this).closest(".response-container"); 				
//		var workDayDtos = getWorkDayDtosFromContainer($responseContainer.find(".work-day-dtos"));

		initCalendar_showWorkDays($(this), workDayDtos);	
	})
}

function executeAjaxCall_getProposal(applicationId, $e){

	broswerIsWaiting(true);
	
	$.ajax({
		type: "GET",
		url: "/JobSearch/application/" + applicationId + "/current-proposal",
		header: getAjaxHeaders(),
		dataType: "html",
		success: function(html) {
			$e.html(html);
			$e.find(".mod").eq(0).show();	
			var workDayDtos = JSON.parse($("#json_workDayDtos").html());
			initCalendar_proposedWorkDays(workDayDtos);
			broswerIsWaiting(false);
		},
		error: function() {
			broswerIsWaiting(false);
		}
	})
}


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
		return "counter-by-applicant";
	else if(isSessionUserAnEmployer($responseContainer))
		return "acknowledge-by-employer";
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

}




function isDateProposed(date, workDayDtos) {
	var workDayDto = getWorkDayDtoByDate(date, workDayDtos);
	if(workDtoDto != undefined){
		if(workDayDto.isProposed == "1") return true;
		else return false;
	}else return false;
	
}

function isDateAJobWorkDay(date, workDayDtos){
	
	$(workDayDtos).each(function(i, workDayDto) {
		if(workDayDto.date.getTime() == dateToFind.getTime()) return true;
	})
	
	return false;
}



