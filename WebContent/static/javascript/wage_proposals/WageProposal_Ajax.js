
function offerNewProposal(respondToProposalRequest){
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/proposal/",			
		headers : getAjaxHeaders(),
		data: JSON.stringify(respondToProposalRequest),
		contentType : "application/json",	
		datType: "text"
	}).done(function(response) {	
		broswerIsWaiting(false);
		location.reload(true);		
	})	
}
function acceptProposal(respondToProposalRequest){
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/proposal/accept",			
		headers : getAjaxHeaders(),
		data: JSON.stringify(respondToProposalRequest),
		contentType : "application/json",	
		datType: "text"
	}).done(function(response) {	
		broswerIsWaiting(false);
		location.reload(true);		
	})	
}
function sendInitialProposal(makeInitialOfferByEmployerRequest){	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/proposal/initial-proposal",
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
function getCurrentProposal(proposalId, $e, callback){
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