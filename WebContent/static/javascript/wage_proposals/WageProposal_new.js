var g_employmentProposalDto = {};
var g_workDayDtos_originalProposal = [];
var g_workDayDtos_counter = [];

$(document).ready(function() {
	$("body").on("keydown", ".custom-times-inputs input", function() {
		var $cont = $(this).closest(".expiration-input-container");
		$cont.find("input.one-day-from-now").prop("checked", false);
		$cont.find("input.one-day-before-job-starts").prop("checked", false);
	})
	
	$("body").on("click", "button.counter-proposal", function() {
		showCounterOffer(true, $(this));
		
	})
	
	$("body").on("click", "button.accept-proposal", function() {
		showCounterOffer(false, $(this));
	})	
	
	$("body").on("click", ".review-proposal", function() {
		
		g_employmentProposalDto = getEmploymentProposalDto($(this));	
//		executeAjaxCall_respondToProposal(g_employmentProposalDto, "sdf");
		
		
		if(isInputValid2(g_employmentProposalDto, $(this))){			
			showConfirmProposal(true, $(this), g_employmentProposalDto);
		}		
	})
	
	$("body").on("change", ".expiration-input-container input[type=radio]", function() {
		$(this).closest(".expiration-input-container").removeClass("invalid");
	})
	
	$("body").on("click", ".edit-response-to-proposal", function() {
		showConfirmProposal(false, $(this));
	})
	
	$("body").on("click", ".send-proposal", function() {
		// ************************************************************
		// validation was done on .review-proposal event
		// ************************************************************
		var context = getContext($(this));
		executeAjaxCall_respondToProposal(g_employmentProposalDto, context);
	})
	
	$("body").on("click", ".send-initial-offer", function() {
			// ************************************************************
			// validation was done on .review-proposal event
			// ************************************************************			
		var context = getContext($(this));
		executeAjaxCall_sendOffer($(this));
	})
	
	$("body").on("click", ".cancel", function() {
		$(this).closest(".mod").find(".mod-header .glyphicon-remove").eq(0).click();			
	})
	
	$("body").on("click", ".select-all-work-days-override", function() {
		var $calendar = $(this).closest(".proposal-container").find(".counter-calendar").eq(0);
		selectAllWorkDays($calendar, g_workDayDtos_counter);
	})
	
	$("body").on("click", ".show-mod", function(){
	
		var applicationId = $(this).closest("tr").attr("data-application-id");
		var $renderHtml = $(this).siblings(".render-present-proposal-mod").eq(0);
		var $proposalMod = $renderHtml.find(".mod").eq(0);
		
	
		if($proposalMod.length > 0) $proposalMod.show();
		else executeAjaxCall_getCurrentProposal(applicationId, $renderHtml);
		
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
function getContext($e){
	
	if(!getIsAcceptingWage($e) || !getIsAcceptingWorkDays($e))
		return "counter-by-applicant";
	else if(isSessionUserAnEmployer($e))
		return "acknowledge-by-employer";
	else return "approve-by-applicant";
	
}
function showCounterOffer(request, $e) {
	var $proposal = $e.closest(".proposal");
	var $counterOffer = $proposal.find(".counter-offer").eq(0);
	var speed = 650;
	if(request){
		$counterOffer.slideDown(speed);
		$proposal.attr("data-is-accepting", 0);
		$e.closest(".proposal").find(".proposed-offer").slideUp(speed);
	}
	else{
		$counterOffer.slideUp(speed);
		$proposal.attr("data-is-accepting", 1);
		$e.closest(".proposal").find(".proposed-offer").slideDown(speed);
	}
}
function showConfirmProposal(request, $e, employmentProposalDto){
	var $modBody = $e.closest(".mod-body");
	var $respondToProposal = $modBody.find(".respond-to-proposal").eq(0);
	var $confirmProposal = $modBody.find(".confirm-response-to-proposal").eq(0);

	if(request){
		
		// Show proposed wage and work day messages
		$confirmProposal.find(".wage-amount").html(g_employmentProposalDto.amount);
		if(g_employmentProposalDto.dateStrings_proposedDates != undefined){
			if(g_employmentProposalDto.dateStrings_proposedDates.length == 1){
				$confirmProposal.find(".work-day-count").html(g_employmentProposalDto.dateStrings_proposedDates.length + " work day");
			}else{
				$confirmProposal.find(".work-day-count").html(g_employmentProposalDto.dateStrings_proposedDates.length + " work days");
			}
		}
		
		// Show expiration time
		if(isSessionUserAnEmployer($e)){
			$confirmProposal.find(".expires-in").html(getExpirationTimeToConfirm(employmentProposalDto));
		}
		
		// Initialize calendar
		var $calendar = $modBody.find(".calendar.confirm-calendar").eq(0);
		$calendar.datepicker("destroy");
		if(getIsAcceptingWorkDays($e)){
			initCalendar_new($calendar, g_workDayDtos_originalProposal);
		}else{
			initCalendar_new($calendar, g_workDayDtos_counter);
		}
		
		// Hide/show main containers
		$confirmProposal.show();
		$respondToProposal.hide();		
		
		// Set which message to show
		var $message_acceptingProposal = $confirmProposal.find(".confirm-proposal-accept");
		var $message_counteringProposal = $confirmProposal.find(".confirm-proposal-counter");
		var $message_sendInitialOffer = $confirmProposal.find(".confirm-send-initial-offer");
		var $proposalContainer = $e.closest(".proposal-container");
		if(getIsEmployerMakingInitalOffer($proposalContainer)){
			$message_acceptingProposal.hide();
			$message_counteringProposal.hide();
			$message_sendInitialOffer.show();			
		}
		else if(getIsAcceptingWorkDays($e) &&
				getIsAcceptingWage($e)){
			$message_acceptingProposal.show();
			$message_counteringProposal.hide();
			$message_sendInitialOffer.hide();
		}else{
			$message_acceptingProposal.hide();
			$message_counteringProposal.show();
			$message_sendInitialOffer.hide();
		}

		
	}else{
		$confirmProposal.hide();
		$respondToProposal.show();
	}
}
function getIsAcceptingWorkDays($e) {
	var $workDayProposal = $e.closest(".proposal-container").find(".work-day-proposal").eq(0); 
	var dataAttr = $workDayProposal.attr("data-is-accepting");
	
	// Will be empty when job does not accept partial availability
	if($workDayProposal.length == 0) return true;
	else{
		if(dataAttr == undefined) return undefined;
		else if(dataAttr == 1) return true;
		else return false;	
	}
	
}
function getIsAcceptingWage($e) {
	var dataAttr = $e.closest(".proposal-container").find(".wage-proposal").attr("data-is-accepting");
	
	if(dataAttr == undefined) return undefined;
	else if(dataAttr == 1) return true;
	else return false;
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
function executeAjaxCall_getCurrentProposal(applicationId, $e){
	broswerIsWaiting(true);	
	$.ajax({
		type: "GET",
		url: "/JobSearch/application/" + applicationId + "/current-proposal",
		header: getAjaxHeaders(),
		dataType: "html",
		success: function(html) {
			$e.html(html);
			$e.find(".mod").eq(0).show();	
			
			g_workDayDtos_originalProposal = JSON.parse($("#json_workDayDtos").html());
			$.extend(true, g_workDayDtos_counter, g_workDayDtos_originalProposal);

			initCalendar_new($(".calendar.proposed-calendar"), g_workDayDtos_originalProposal);
			initCalendar_new($(".calendar.counter-calendar"), g_workDayDtos_counter);
			
			broswerIsWaiting(false);

		},
		error: function() {
			broswerIsWaiting(false);
		}
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
function executeAjaxCall_sendOffer($e){
	
	var $proposalContainer = $e.closest(".proposal-container");
	
	var applicationDto = {};
	applicationDto.application = {};
	applicationDto.employmentProposalDto = {};
	
	applicationDto.jobId = $("#jobId_getOnPageLoad").val();
	applicationDto.applicantId = $e.closest("tr").attr("data-user-id");	
	applicationDto.employmentProposalDto = g_employmentProposalDto;
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/employer/initiate-contact",
		headers : getAjaxHeaders(),
		contentType : "application/json",	
		data: JSON.stringify(applicationDto),
		dataType: "text"
		
	}).done(function(response){
		$("#findEmployees").click();
		$proposalContainer.hide();
		broswerIsWaiting(false);
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
function getEmploymentProposalDto($e){

	var employmentProposalDto = {};
	var $proposalContainer = $e.closest(".proposal-container");
	var $wageProposal = $proposalContainer.find(".wage-proposal.proposal").eq(0);
	var $workDayProposal = $proposalContainer.find(".work-day-proposal.proposal").eq(0);
	var isEmployerMakingInitalOffer = getIsEmployerMakingInitalOffer($proposalContainer);
	employmentProposalDto.applicationId = $proposalContainer.attr("data-application-id");
	
	// Wage
	var isAcceptingWage = getIsAcceptingWage($e);
	if(isEmployerMakingInitalOffer){
		employmentProposalDto.amount = $wageProposal.find("input").val();	
	}
	else if(isAcceptingWage == undefined) {		
	}
	else if(isAcceptingWage){
		employmentProposalDto.amount = $wageProposal.attr("data-proposed-amount");		
	}else{
		employmentProposalDto.amount = $wageProposal.find("input").val();		
	}
	
	// Work days
	employmentProposalDto.dateStrings_proposedDates = [];
	var $calendar;
	var isAcceptingWorkDays = getIsAcceptingWorkDays($e);
	if(isEmployerMakingInitalOffer){
		$calendar = $proposalContainer.find(".calendar.make-initial-offer-calendar");
		employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
									$calendar, "yy-mm-dd", "is-proposed");	
		
	// Job does not allow partial availability
	}else if($workDayProposal.length == 0){
		employmentProposalDto.dateStrings_proposedDates = getDatesFromWorkDayDtos(g_workDayDtos_originalProposal);
	}
	else if(isAcceptingWorkDays == undefined) {
	}
	else if(isAcceptingWorkDays){ 
		$calendar = $proposalContainer.find(".calendar.proposed-calendar");
		employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
									$calendar, "yy-mm-dd", "is-proposed");
	}else{
		$calendar = $proposalContainer.find(".calendar.counter-calendar")
		employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
									$calendar, "yy-mm-dd", "is-proposed");
	}

	// Expiration time
	if(isSessionUserAnEmployer($e) && employmentProposalDto.dateStrings_proposedDates.length > 0){
		
		var date_expiration = new Date();
		var date_now = new Date();
		
		// Job does not allow partial availability
		if($workDayProposal.length == 0){
			workDayDto_firstProposedWorkDay = g_workDayDtos_originalProposal[0];
		
		// Get the first date proposed/accepted
		}else if(employmentProposalDto.dateStrings_proposedDates.length > 0){			
			var date_firstProposedWorkDay = getMinDateFromDateStringsArray(
					employmentProposalDto.dateStrings_proposedDates);
			
			var workDayDto_firstProposedWorkDay = getWorkDayDtoByDate(date_firstProposedWorkDay,
													g_workDayDtos_originalProposal);		
		}
	
		var dateTime_firstProposedWorkDay = new Date(workDayDto_firstProposedWorkDay.workDay.stringDate +
				" " + workDayDto_firstProposedWorkDay.workDay.stringStartTime);
		
		var days = 0;
		var hours = 0;
		var mins = 0;			
		
		// 1 day from now
		if($proposalContainer.find("input.one-day-from-now:checked").length){
			date_expiration.setDate(date_now.getDate() + 1);
			
		// 1 day before the first proposed work day starts
		}else if($proposalContainer.find("input.one-day-before-first-proposed-work-day:checked").length){
			
			date_expiration = new Date(dateTime_firstProposedWorkDay);
			date_expiration.setDate(date_expiration.getDate() - 1);
			
		
		// Custom time ...
		}else{
			
			days = parseInt($proposalContainer.find(".set-expiration input.days").val());
			hours = parseInt($proposalContainer.find(".set-expiration input.hours").val());
			mins = parseInt($proposalContainer.find(".set-expiration input.minutes").val());			
			
			// ... from now 
			if($proposalContainer.find("input.custom-time-from-now:checked").length){
				date_expiration = new Date();
				
				if(!isNaN(days)) date_expiration.setDate(date_expiration.getDate() + days);
				if(!isNaN(hours)) date_expiration.setHours(date_expiration.getHours() + hours);
				if(!isNaN(mins)) date_expiration.setMinutes(date_expiration.getMinutes() + mins);
			
			// ... from the start time of the first proposed work day
			}else if($proposalContainer.find("input.custom-time-before-first-proposed-work-day:checked").length){
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
		
		employmentProposalDto.days_offerExpires = days_offerExpires;
		employmentProposalDto.hours_offerExpires = hours_offerExpires;
		employmentProposalDto.minutes_offerExpires = minutes_offerExpires;	
		
	}
	
	return employmentProposalDto;
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