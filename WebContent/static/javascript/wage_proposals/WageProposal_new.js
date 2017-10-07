var g_workDayDtos_originalProposal = [];
var g_workDayDtos_counter = [];
var proposalView = {};

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
		
		proposalView = initProposalView();
		if($e.hasClass("make-new-offer")){
			proposalView.isRespondingToAnExpiredProposal = true;
		}

		executeAjaxCall_getCurrentProposal(proposalId, $e_renderHtml, function() {
			var $response = $cont.find(".current-proposal-response").eq(0);
			var applicationId = $response.attr("data-application-id");
			var doShowCounterContest = true
			if($e.hasClass("accept-current-proposal") == 1) doShowCounterContest = false; 
//			showCounterContext(doShowCounterContest, $e);
			$cont.find(".mod").show(function() {
				
				if(proposalView.isRespondingToAnExpiredProposal === true){
					var $proposal = getProposalContainer();					
					setProposalAcceptanceContextToAccepting(false, $proposal);
					hideProposalStatusWrappers(true, $proposal);
				}		
				
				// If there is only one work day, then a calendar will not be shown.
				// Thus the conflict search cannot be triggered by the calendar init.
				if(g_workDayDtos_originalProposal.length == 1){
					var dateString = [];
					var $e_renderHtml = getConflictingApplicationsContainer($cont);
					dateString.push(g_workDayDtos_originalProposal[0].workDay.stringDate);
					executeAjaxCall_getConflitingApplications($e_renderHtml, applicationId, dateString);
				}
			});	

		})		
	})	
	$("body").on("click", ".send-proposal-wrapper button", function() {
		// ************************************************************
		// Need validation
		// ************************************************************
		
		var employerMakeInitialOffer = $(this).closest(".proposal-container")
							.attr("data-employer-is-making-first-offer");
		
		
		if(employerMakeInitialOffer == "1"){			
			var makeInitialOfferByEmployerRequest = getMakeInitialOfferByEmployerRequest($(this));
			executeAjaxCall_makeInitialOffer(makeInitialOfferByEmployerRequest);	
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
	$("body").on("keyup", ".wage-proposal-wrapper input", function() {
		var $proposal = getProposalContainer();
		if(isCounteringWage($proposal)){
			$proposal.find(".wage-proposal-wrapper .status-accepting").removeClass("current-status")
			$proposal.find(".wage-proposal-wrapper .status-proposing").addClass("current-status")
		}else{
			$proposal.find(".wage-proposal-wrapper .status-accepting").addClass("current-status")
			$proposal.find(".wage-proposal-wrapper .status-proposing").removeClass("current-status")
		}
		setProposalAcceptanceContext();
	})
	
})
function initProposalView(){
	proposalView = {};
	proposalView.isRespondingToAnExpiredProposal = false;
	return proposalView;
}
function hideProposalStatusWrappers(request, $proposal){
	$proposal.find(".proposal-status-wrapper").each(function(){
		hideE(request, $(this));
	})
}
function getProposalContainer() {
	return $("body").find(".mod.proposal-container:visible .proposal-wrapper");
}
function setProposalAcceptanceContext(){	
	var $proposal = getProposalContainer();
	var totalProposalItems = $proposal.find(".proposal-status-wrapper").length
	if($proposal.find(".proposal-status-wrapper .status-accepting.current-status").length == totalProposalItems){
		setProposalAcceptanceContextToAccepting(true, $proposal);
	}else{
		setProposalAcceptanceContextToAccepting(false, $proposal);
	}
}
function setProposalAcceptanceContextToAccepting(request, $proposal){
	if(request){
		$proposal.addClass("accepting-offer-context");
		$proposal.removeClass("proposing-new-offer-context");
	}else{
		$proposal.removeClass("accepting-offer-context");
		$proposal.addClass("proposing-new-offer-context");
	}
}
function setWorkDayAcceptanceContext(){
	var $proposal = getProposalContainer();
	if(isCounteringWorkDays($proposal)){
		$proposal.find(".work-day-proposal-wrapper .status-accepting").removeClass("current-status")
		$proposal.find(".work-day-proposal-wrapper .status-proposing").addClass("current-status")
	}else{
		$proposal.find(".work-day-proposal-wrapper .status-accepting").addClass("current-status")
		$proposal.find(".work-day-proposal-wrapper .status-proposing").removeClass("current-status")
	}
	setProposalAcceptanceContext();
}
function getMakeInitialOfferByEmployerRequest($e) {
	var $wrapper = $e.closest(".proposal-wrapper");
	var request = {};
	request.respondToProposalRequest = getRespondToProposalRequest($e);
	request.proposeToUserId = $wrapper.attr("data-user-id-make-offer-to");
	request.jobId = $wrapper.find("#select-job option:selected").attr("data-job-id");
//	request.isRespondingToAnExpiredProposal = false;
	return request;	
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
	if(originalProposedWage - counteredWage == 0.0) return false;
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
function executeAjaxCall_makeInitialOffer(makeInitialOfferByEmployerRequest){	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/proposal/employer-make-initial-proposal",
		headers : getAjaxHeaders(),
		contentType : "application/json",	
		data: JSON.stringify(makeInitialOfferByEmployerRequest),
		dataType: "text"		
	}).done(function(response){
//		location.reload(true);
		broswerIsWaiting(false);
		$("#make-offer-modal .mod").hide();
	})	
}
function executeAjaxCall_getConflitingApplications($e_renderHtml, applicationId_withReferenceTo,
		dateStrings_toFindConflictsWith){
	
	var conflicingApplicationsRequest = {};
	conflicingApplicationsRequest.referenceApplicationId = applicationId_withReferenceTo;
	conflicingApplicationsRequest.datesToFindConflictWith = dateStrings_toFindConflictsWith;
	
	broswerIsWaiting(true);
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: "/JobSearch/application/conflicting-applications",
		contentType: "application/json",
		data: JSON.stringify(conflicingApplicationsRequest),
		dataType: "html",
	}).done(function(html){
		broswerIsWaiting(false);
		
		// This method of showing/hiding was a quick hack.
		// Pretty this up later.
		var show = html.indexOf("other-application-conflicts") === -1 ? false : true;
		
		if(show){

			if(!$e_renderHtml.is(":visible")){
				$e_renderHtml.hide();
				$e_renderHtml.html(html);
				slideDown($e_renderHtml, 800);			
			}else{
				$e_renderHtml.html(html);
			}

		}else{
			slideUp($e_renderHtml, 800, function(){
				$e_renderHtml.html(html);
			})			
		}
		
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
			setProposalAcceptanceContext();
			broswerIsWaiting(false);			
			callback();		
	})
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
	
	var $proposalContentWrapper = $e.closest(".proposal-content-wrapper");
	var $proposalContainer = $e.closest(".proposal-container");
	var $wageProposal = $proposalContainer.find(".wage-proposal-wrapper").eq(0);
	var $workDayProposal = $proposalContainer.find(".work-day-proposal-wrapper").eq(0);
	var isEmployerMakingInitalOffer = getIsEmployerMakingInitalOffer($proposalContainer);
	respondToProposalRequest.proposal.proposalId = $proposalContainer.attr("data-proposal-id");
	respondToProposalRequest.proposal.applicationId = $proposalContainer.attr("data-application-id");
	
	respondToProposalRequest.isRespondingToAnExpiredProposal = proposalView.isRespondingToAnExpiredProposal;
	// Wage
	respondToProposalRequest.proposal.amount = $wageProposal.find("input").val();
	
	// Work days
	respondToProposalRequest.proposal.proposedDates = [];
	$calendar = $proposalContainer.find(".calendar");
	respondToProposalRequest.proposal.proposedDates = getSelectedDates(
								$calendar, "yy-mm-dd", "is-proposed");
	
	// Expiration time
	if(isSessionUserAnEmployer($e)){
		
		var date_expiration = new Date();
		var date_now = new Date();
		
		var workDayDto_firstProposedWorkDay;
		
		// Job does not allow partial availability. (this is a hack. add some job meta data)
		if($workDayProposal.length == 0){			
			workDayDto_firstProposedWorkDay = g_workDayDtos_originalProposal[0];
		
		// Get the first date proposed/accepted
		}else if(respondToProposalRequest.proposal.proposedDates.length > 0){			
			var firstDate = getMinDateFromDateStringsArray(respondToProposalRequest.proposal.proposedDates);
			workDayDto_firstProposedWorkDay = getWorkDayDtoByDate(firstDate, g_workDayDtos_originalProposal);
		}
				
		var dateString = workDayDto_firstProposedWorkDay.workDay.stringDate; 
		var timeString = workDayDto_firstProposedWorkDay.workDay.stringStartTime
		var dateTime_firstProposedWorkDay = new Date(dateString + " " + timeString);
		
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
function getExpirationTimeToConfirm(){

	var days;
	var hours;
	var minutes;
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