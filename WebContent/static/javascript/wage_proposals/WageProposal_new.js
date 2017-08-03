var g_workDayDtos_originalProposal = [];
var g_workDayDtos_counter = [];

$(document).ready(function() {
	

	$("body").on("click", ".expiration-time input[name=exp-time-init]", function() {
		$e = $(this);
		var $otherContainer = $e.closest(".expiration-time").find(".other-container");
		if($e.hasClass("other")) $otherContainer.show();
		else $otherContainer.hide();
	})
	
	$("body").on("click", "button.accept-current-proposal, button.counter-current-proposal", function() {
		var $e = $(this);
		var $cont = $e.closest(".proposal[data-proposal-id]");
		var proposalId = $cont.attr("data-proposal-id");
		var $e_renderHtml = $cont.find(".render-present-proposal-mod").eq(0);
				
			executeAjaxCall_getCurrentProposal(proposalId, $e_renderHtml, function() {
				var doShowCounterContest = true
				if($e.hasClass("accept-current-proposal") == 1) doShowCounterContest = false; 
				showCounterContext(doShowCounterContest, $e);
				$cont.find(".mod").show();				
			})		
	})
	
	$("body").on("click", ".cancel-proposal", function() {
		$(this).closest(".mod").hide();
	})
	
	$("body").on("click", ".edit-proposal", function() {		
		var $cont = getProposalWrapper($(this));
		toggleClasses($cont, "counter-context", "review-context");
		$cont.find(".proposal-calendar").removeClass("read-only");		
	})
	
	
	$("body").on("keydown", ".custom-times-inputs input", function() {
		var $cont = $(this).closest(".expiration-input-container");
		$cont.find("input.one-day-from-now").prop("checked", false);
		$cont.find("input.one-day-before-job-starts").prop("checked", false);
	})
	
	
	$("body").on("click", ".review-proposal", function() {
		var $e = $(this);
		var $wrapper = getProposalWrapper($e);
		var employerIsMakingFirstOffer = false;		
		
		if($wrapper.hasClass("employer-make-initial-offer")){
			employerIsMakingFirstOffer = true;
		}
		
		var $cont = getProposalWrapper($e);
		var isAcceptingTheOffer = true;
		
		$cont.find(".proposal-calendar").addClass("read-only");				
		$cont.addClass("review-context");
		$cont.removeClass("counter-context");
		
		var $wage =  $cont.find(".wage-proposal-wrapper").eq(0);
		var $wage_acceptOrPropose = $wage.find("span.accepting-or-proposing").eq(0);
		var $workDays_acceptOrPropose = $cont.find(".work-day-proposal-wrapper span.accepting-or-proposing").eq(0);
		
		$wage.find(".review-context .wage-amount")
			.html($wage.find("input.counter-wage-amount").eq(0).val())
					
		if(isCounteringWage($cont) || employerIsMakingFirstOffer){
			$wage_acceptOrPropose.html("proposing");
			isAcceptingTheOffer = false;
		}else{
			$wage_acceptOrPropose.html("accepting");
		}
		
		if($workDays_acceptOrPropose != undefined){
			if(isPartialAvailabilityAllowed($wrapper)){
				if(isCounteringWorkDays($cont) || employerIsMakingFirstOffer ){
					$workDays_acceptOrPropose.html("proposing");
					isAcceptingTheOffer = false;
				}else{
					$workDays_acceptOrPropose.html("accepting");
				}			
			}			
		}
		
		if(isAcceptingTheOffer){
			$cont.addClass("accepting-offer-context");
			$cont.removeClass("proposing-new-offer-context");
		}else{
			$cont.removeClass("accepting-offer-context");
			$cont.addClass("proposing-new-offer-context");
		}
		
		$(".mod").animate({ scrollTop: 0 }, "200");
				
	})
	
	$("body").on("change", ".expiration-input-container input[type=radio]", function() {
		$(this).closest(".expiration-input-container").removeClass("invalid");
	})

	
	$("body").on("click", ".send-proposal", function() {
		// ************************************************************
		// Need validation
		// ************************************************************
		
		var employerMakeInitialOffer = $(this).closest(".proposal-container")
							.attr("data-employer-is-making-first-offer");
		
		
		if(employerMakeInitialOffer == "1"){			
			var applicationDto_makeInitialOffer = getApplicationDto_makeInitialOffer($(this));
			executeAjaxCall_makeInitialOffer(applicationDto_makeInitialOffer);	
		}else{
			var respondToProposalRequest = getRespondToProposalRequest($(this));
			executeAjaxCall_sendRespondToProposalRequest(respondToProposalRequest);	
		}
		
	})
	
	$("body").on("click", ".send-initial-offer", function() {
			// ************************************************************
			// validation was done on .review-proposal event
			// ************************************************************			
		executeAjaxCall_sendOffer($(this));
	})
	
	$("body").on("click", ".select-all-work-days-override", function() {
		var $calendar = $(this).closest(".proposal-container").find(".counter-calendar").eq(0);
		selectAllWorkDays($calendar, g_workDayDtos_counter);
		
//		$.extend(true, g_workDayDtos_counter, g_workDayDtos_originalProposal);
		
//		$calendar.find("td.job-work-day").eq(0).click();
	})
	
	$("body").on("click", ".application-proposal-container .proposal-item.work-days p", function() {
	
		var applicationId = $(this).closest("tr").attr("data-application-id");
		var $calendar = $(this).closest(".proposal-item").find(".calendar").eq(0);
		
		if($calendar.hasClass("hasDatepicker") == 0)
			executeAjaxCall_getProposedWorkDays(applicationId, $calendar);
		else
			$calendar.closest(".mod").show();		
	})
	
})

function getProposalWrapper($e) {
	var $proposalWrapper = $e.closest(".proposal-wrapper");
	if ($proposalWrapper.length) return $proposalWrapper;
	else return $e.closest(".respond-to-proposal").find(".proposal-wrapper").eq(0);
}
function getApplicationDto_makeInitialOffer($e) {
	var $wrapper = $e.closest(".wrapper");
	var applicationDto = {};
	applicationDto.application = {};
	applicationDto.employmentProposalDto = getRespondToProposalRequest($e);
	applicationDto.applicantId = $wrapper.attr("data-user-id-make-offer-to");
	applicationDto.jobId = $wrapper.attr("data-job-id-make-offer-for");
	return applicationDto;	
}
function isPartialAvailabilityAllowed($wrapper){
	if($wrapper.attr("data-is-partial-availability-allowed") == "true") return true;
	else return false;
}
function hasCurrentProposalAlreadyBeenImported($e_renderHtml) {
	if($e_renderHtml.find(".mod").length) return true;
	else return false;
}
function showCounterContext(request, $e) {
	var $cont = getProposalWrapper($e);
	if(request){
		$cont.addClass("counter-context");
		$cont.removeClass("review-context");
		$cont.find(".proposal-calendar").removeClass("read-only");
	}
	else {			
		$cont.find(".review-proposal").click();
	}
	
	
}
function isCounteringWorkDays($cont) {
	var $workDayProposal = $cont.find(".work-day-proposal-wrapper").eq(0);
	
	var originalProposedWorkDays = getArrayFromString($workDayProposal.attr("data-proposed-work-days"));
	var counteredWorkDays = getSelectedDates($workDayProposal.find(".calendar"),
			"yy-mm-dd", "is-proposed");
	
	if($(originalProposedWorkDays).not(counteredWorkDays).length === 0 
			&& $(counteredWorkDays).not(originalProposedWorkDays).length === 0) return false;
	else return true;
	
}
function isCounteringWage($cont) {
	var $wageProposal = $cont.find(".wage-proposal-wrapper").eq(0);
	var originalProposedWage = parseFloat($wageProposal.attr("data-proposed-amount"));
	var counteredWage = parseFloat($wageProposal.find("input.counter-wage-amount").eq(0).val());
	if(originalProposedWage - counteredWage == 0) return false;
	else return true;
	
}
function executeAjaxCall_sendRespondToProposalRequest(respondToProposalRequest){
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/proposal/respond",			
		headers : getAjaxHeaders(),
		data: JSON.stringify(respondToProposalRequest),
		contentType : "application/json",	
		datType: "text"
	}).done(function(response) {	
		broswerIsWaiting(false);
		location.reload(true);		
	})	
}
function executeAjaxCall_makeInitialOffer(applicationDto){	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/employer/make-initial-offer",
		headers : getAjaxHeaders(),
		contentType : "application/json",	
		data: JSON.stringify(applicationDto),
		dataType: "text"		
	}).done(function(response){
		location.reload(true);
	})	
}
function executeAjaxCall_getConflitingApplications($e_renderHtml, applicationId_withReferenceTo,
		dateStrings_toFindConflictsWith){
	broswerIsWaiting(true);
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: "/JobSearch/application/" + applicationId_withReferenceTo + "/conflicting-applications",
		contentType: "application/json",
		data: JSON.stringify(dateStrings_toFindConflictsWith),
		dataType: "html",
	}).done(function(html){
		broswerIsWaiting(false);
		$e_renderHtml.html(html);
	})
}
function executeAjaxCall_getCurrentProposal(proposalId, $e, callback){
	broswerIsWaiting(true);	
	$.ajax({
		type: "GET",
		url: "/JobSearch/proposal/" + proposalId,
		header: getAjaxHeaders(),
		dataType: "html",
	}).done(function(html) {
			$e.html(html);
			$e.find(".mod").eq(0).show();	
			
			// Perhaps make another ajax call for obtaining the proposed work days???
			// The down side is there would be a lag when rendering the calendar...
			g_workDayDtos_originalProposal = JSON.parse($e.find("#json_workDayDtos").eq(0).html());
			$.extend(true, g_workDayDtos_counter, g_workDayDtos_originalProposal);

			initCalendar_new($e.find(".calendar"), g_workDayDtos_originalProposal);
			
			broswerIsWaiting(false);			
			callback();		
	})
}
function executeAjaxCall_getProposedWorkDays(applicationId, $calendar) {
	
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		headers: getAjaxHeaders(),
		url: "/JobSearch/application/" + applicationId + "/proposed-work-days",
		dataType: "json",
		success: function(workDayDtos) {
			broswerIsWaiting(false);
//			initCalendar_showWorkDays($calendar, workDayDtos);
			initCalendar_new($calendar, workDayDtos);
			$calendar.closest(".mod").show();
			console.log("executeAjaxCall_getProposedWorkDays");
		},
		error: function() {
			broswerIsWaiting(false);
		}
	})
}

function isInputValid2(dto, $e){
	var $proposalContainer = $e.closest(".proposal-container");
	var $wageProposal = $proposalContainer.find(".wage-proposal.proposal").eq(0);
	var $workDayProposal = $proposalContainer.find(".work-day-proposal.proposal").eq(0);
	var isValid = true;
	
	// Validate wage
	var $counterAmount = $wageProposal.find("input").eq(0); 
	var $buttonGroup = $wageProposal.find(".button-group").eq(0)
	if(dto.amount == undefined){		
		setInvalidCss($buttonGroup);
		isValid = false;
	}else if(dto.amount == ""){
		setInvalidCss($counterAmount);
		isValid = false;
	}else{
		setValidCss($counterAmount);
		setValidCss($buttonGroup);
	}
	
	// Validate work days.
	// When a job does NOT allow partial availability, the work days cannot be negotiated.
	// Hence, the html for work day proposal container will not be rendered (1st "if" condition).
	if($workDayProposal.length > 0){
		$buttonGroup = $workDayProposal.find(".button-group").eq(0);
		if(dto.dateStrings_proposedDates == undefined){
			setInvalidCss($buttonGroup);
			isValid = false;
		}else if(dto.dateStrings_proposedDates.length == 0){
			setInvalidCss($buttonGroup);
			isValid = false;
		}else {
			setValidCss($buttonGroup)
		}
	}
	
	// Validate expiration time.
	// (Cannot inspect the dto property values. If the user did NOT acknowledge the work days,
	// then the dto's expiration time values will be undefined even if the user has
	// correctly set the expiration time)
	if(isSessionUserAnEmployer($e)){
		
		var $expirationContainer = $proposalContainer.find(".expiration-input-container").eq(0);
		
		// Default time options
		if($proposalContainer
				.find("input.one-day-from-now:checked, input.one-day-before-first-proposed-work-day:checked")
				.length){
			
			setValidCss($expirationContainer);
		
		// Custom time options	
		}else if($proposalContainer
					.find("input.custom-time-from-now:checked, input.custom-time-before-first-proposed-work-day:checked")
					.length){
			
//			if(isValidatePositiveNumber($proposalContainer.find("input.days")) ||
//				isValidatePositiveNumber($proposalContainer.find("input.hours")) || 
//				isValidatePositiveNumber($proposalContainer.find("input.minutes")) ){
						
					setValidCss($expirationContainer);
//			}else{
//				setInvalidCss($expirationContainer);
//			}
			
		}else{
			setInvalidCss($expirationContainer);
		}		
	}
	
	return isValid;	
}

function getRespondToProposalRequest($e){

	// ******************************************************
	// ******************************************************
	// Refactor this.
	// Create anohter js file for getting this dto
	// ******************************************************
	// ******************************************************

	var respondToProposalRequest = {};
	respondToProposalRequest.proposal = {};
	
	var $proposalContainer = $e.closest(".proposal-container");
	var $wageProposal = $proposalContainer.find(".wage-proposal-wrapper").eq(0);
	var $workDayProposal = $proposalContainer.find(".work-day-proposal-wrapper").eq(0);
	var isEmployerMakingInitalOffer = getIsEmployerMakingInitalOffer($proposalContainer);
	respondToProposalRequest.proposal.proposalId = $proposalContainer.attr("data-proposal-id");
	respondToProposalRequest.proposal.applicationId = $proposalContainer.attr("data-application-id");
	
	
	// Wage
	respondToProposalRequest.proposal.amount = $wageProposal.find(".review-context .wage-amount").eq(0).html();
	
	// Work days
	respondToProposalRequest.proposal.proposedDates = [];
	$calendar = $proposalContainer.find(".calendar");
	respondToProposalRequest.proposal.proposedDates = getSelectedDates(
								$calendar, "yy-mm-dd", "is-proposed");
	

	// Expiration time
	if(isSessionUserAnEmployer($e) && respondToProposalRequest.proposal.proposedDates.length > 0){
		
		var date_expiration = new Date();
		var date_now = new Date();
		
		// Job does not allow partial availability
		if($workDayProposal.length == 0){
			workDayDto_firstProposedWorkDay = g_workDayDtos_originalProposal[0];
		
		// Get the first date proposed/accepted
		}else if(respondToProposalRequest.proposal.proposedDates.length > 0){			
			var date_firstProposedWorkDay = getMinDateFromDateStringsArray(
					respondToProposalRequest.proposal.proposedDates);
			
			var workDayDto_firstProposedWorkDay = getWorkDayDtoByDate(date_firstProposedWorkDay,
													g_workDayDtos_originalProposal);		
		}
	
		var dateTime_firstProposedWorkDay = new Date(workDayDto_firstProposedWorkDay.workDay.stringDate +
				" " + workDayDto_firstProposedWorkDay.workDay.stringStartTime);
		
		var days = 0;
		var hours = 0;
		var mins = 0;			
		
		// 1 day from now
		if($proposalContainer.find("input.one-day-from-now[name='exp-time-init']:checked").length){
			date_expiration.setDate(date_now.getDate() + 1);
			
		// 1 day before the first proposed work day starts
		}else if($proposalContainer.find("input.one-day-before[name='exp-time-init']:checked").length){
			
			date_expiration = new Date(dateTime_firstProposedWorkDay);
			date_expiration.setDate(date_expiration.getDate() - 1);
			
		
		// Custom time ...
		}else{
			
			days = parseInt($proposalContainer.find(".expiration-time input.days").val());
			hours = parseInt($proposalContainer.find(".expiration-time input.hours").val());
			mins = parseInt($proposalContainer.find(".expiration-time input.minutes").val());			
			
			// ... from now 
			if($proposalContainer.find("input.one-day-from-now[name='exp-time-other']:checked").length){
				date_expiration = new Date();
				
				if(!isNaN(days)) date_expiration.setDate(date_expiration.getDate() + days);
				if(!isNaN(hours)) date_expiration.setHours(date_expiration.getHours() + hours);
				if(!isNaN(mins)) date_expiration.setMinutes(date_expiration.getMinutes() + mins);
			
			// ... from the start time of the first proposed work day
			}else if($proposalContainer.find("input.one-day-before[name='exp-time-other']:checked").length){
				date_expiration = new Date(dateTime_firstProposedWorkDay);
				
				if(!isNaN(days)) date_expiration.setDate(date_expiration.getDate() - days);
				if(!isNaN(hours)) date_expiration.setHours(date_expiration.getHours() - hours);
				if(!isNaN(mins)) date_expiration.setMinutes(date_expiration.getMinutes() - mins);				
			}						
		}
		
		// Calculate the days/hours/minutes the offer will expire from now
		var seconds = Math.floor((date_expiration - (date_now))/1000);
		var minutes_offerExpires = Math.floor(seconds/60);
		var hours_offerExpires = Math.floor(minutes_offerExpires/60);
		var days_offerExpires = Math.floor(hours_offerExpires/24);
		hours_offerExpires = hours_offerExpires-(days_offerExpires*24);
		minutes_offerExpires = minutes_offerExpires-(days_offerExpires*24*60)-(hours_offerExpires*60);
		
		respondToProposalRequest.days_offerExpires = days_offerExpires;
		respondToProposalRequest.hours_offerExpires = hours_offerExpires;
		respondToProposalRequest.minutes_offerExpires = minutes_offerExpires;	
		
	}
	
	return respondToProposalRequest;
}
function getIsEmployerMakingInitalOffer($proposalContainer) {
	var attr = $proposalContainer.attr("data-employer-is-making-first-offer");
	if(attr == undefined) return undefined;
	else if(attr == 1) return true;
	else return false;
}
function isSessionUserAnEmployer($e){
	if($e.closest(".proposal-container").attr("data-session-user-is-employer") == "1") return true;
	else return false;
}
function getExpirationTimeToConfirm(employmentProposalDto){

	var days = employmentProposalDto.days_offerExpires;
	var hours = employmentProposalDto.hours_offerExpires;
	var minutes = employmentProposalDto.minutes_offerExpires;
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
	
	return html;
}