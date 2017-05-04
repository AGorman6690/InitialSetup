var g_employmentProposalDto = {};
var g_workDayDtos_originalProposal = [];
var g_workDayDtos_counter = [];

$(document).ready(function() {
	$("body").on("click", "button.counter-proposal", function() {
		showCounterOffer(true, $(this));
		
	})
	
	$("body").on("click", "button.accept-proposal", function() {
		showCounterOffer(false, $(this));
	})	
	
	$("body").on("click", ".review-proposal", function() {
		
		g_employmentProposalDto = getEmploymentProposalDto($(this));
//		if(isInputValid($(this))){		
		if(isInputValid2(g_employmentProposalDto, $(this))){
			
			showConfirmProposal(true, $(this));
		}		
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
		var $renderHtml = $(this).siblings(".present-proposal").eq(0);
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
function showConfirmProposal(request, $e){
	var $modBody = $e.closest(".mod-body");
	var $respondToProposal = $modBody.find(".respond-to-proposal").eq(0);
	var $confirmProposal = $modBody.find(".confirm-response-to-proposal").eq(0);

	if(request){
		
		// Show proposed wage and work day messages
		$confirmProposal.find(".wage-amount").html(g_employmentProposalDto.amount);
		if(g_employmentProposalDto.dateStrings_proposedDates.length == 1){
			$confirmProposal.find(".work-day-count").html(g_employmentProposalDto.dateStrings_proposedDates.length + " work day");
		}else{
			$confirmProposal.find(".work-day-count").html(g_employmentProposalDto.dateStrings_proposedDates.length + " work days");
		}
		
		// Show expiration time
		if(isSessionUserAnEmployer($e)){
			$confirmProposal.find(".expires-in").html(getExpirationTimeToConfirm($e));
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
	var dataAttr = $e.closest(".proposal-container").find(".work-day-proposal").attr("data-is-accepting");
	
	if(dataAttr == undefined) return undefined;
	else if(dataAttr == 1) return true;
	else return false;
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
	
	// Validate work days
	$buttonGroup = $workDayProposal.find(".button-group").eq(0);
	if(dto.dateStrings_proposedDates == undefined){
		setInvalidCss($buttonGroup);
		isValid = false;
	}else if(dto.dateStrings_proposedDates.length == 0){
		
		isValid = false;
	}else {
		setValidCss($buttonGroup)
	}
	
	// Validate expiration time
	if(isSessionUserAnEmployer($e)){
		if(dto.days_offerExpires == 0 && 
				dto.hours_offerExpires == 0 && 
				dto.minutes_offerExpires == 0 ){
			
			$proposalContainer.find(".set-expiration input").each(function() {
				setInvalidCss($(this));
			})
			isValid = false;
		}else{
			$proposalContainer.find(".set-expiration input").each(function() {
				setValidCss($(this));
			})
		}		
	}
	
	return isValid;	
}
function isInputValid($e) {
	var $proposalContainer = $e.closest(".proposal-container");
	var $wageProposal = $proposalContainer.find(".wage-proposal.proposal").eq(0);
	var $workDayProposal = $proposalContainer.find(".work-day-proposal.proposal").eq(0);
	var isValid = true;
	
	// Validate wage
	var dataAttr_isAccepting_wageProposal = $wageProposal.attr("data-is-accepting"); 
	if(dataAttr_isAccepting_wageProposal== undefined){
		setInvalidCss($wageProposal.find(".button-group").eq(0));
		isValid = false;
	}else if(dataAttr_isAccepting_wageProposal == 0){
		var $counterAmount = $wageProposal.find("input").eq(0); 
		if($counterAmount.val() == ""){
			setInvalidCss($counterAmount);
			isValid = false;
		}
	}else {
		setValidCss($wageProposal.find(".button-group").eq(0))
	}
	
	// Validate work days
	var dataAttr_isAccepting_workDayProposal = $workDayProposal.attr("data-is-accepting");
	if(dataAttr_isAccepting_workDayProposal == undefined){
		setInvalidCss($workDayProposal.find(".button-group").eq(0));
		isValid = false;
	}else if(dataAttr_isAccepting_workDayProposal == 0){
		
	}else {
		setValidCss($workDayProposal.find(".button-group").eq(0))
	}
	
	// Validate expiration time
	if(isSessionUserAnEmployer($e)){
		var days
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
	var $calendar;
	var isAcceptingWorkDays = getIsAcceptingWorkDays($e);
	if(isEmployerMakingInitalOffer){
		$calendar = $proposalContainer.find(".calendar.make-initial-offer-calendar");
		employmentProposalDto.dateStrings_proposedDates = getSelectedDates(
									$calendar, "yy-mm-dd", "is-proposed");		
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
	if(isSessionUserAnEmployer($e)){
		employmentProposalDto.days_offerExpires = $proposalContainer.find(".set-expiration input.days").val();
		employmentProposalDto.hours_offerExpires = $proposalContainer.find(".set-expiration input.hours").val();
		employmentProposalDto.minutes_offerExpires = $proposalContainer.find(".set-expiration input.minutes").val();		
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
function getExpirationTimeToConfirm($e){
	var $proposalContainer = $e.closest(".proposal-container");
	var days = $proposalContainer.find("input.days").val();
	var hours = $proposalContainer.find("input.hours").val();
	var minutes = $proposalContainer.find("input.minutes").val();	
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