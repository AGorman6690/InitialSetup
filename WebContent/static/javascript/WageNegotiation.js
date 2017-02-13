	
$(document).ready(function(){
	
	
	$(".decline-counter").click(function(){
		
		//*******************************************************************
		//*******************************************************************
		//Eventually ask the applicant if they with to proceed and if they choose to,
		//then their application will be withdrawn
		//*******************************************************************
		//*******************************************************************
				
		//Read the DOM.
		var counterOfferContainer = getCounterOfferContainer($(this));
		var responseNotificationContainer = getResponseNotificationContainer($(counterOfferContainer));

		//Get the wage proposal id and proposal amount
		var wageProposalId = $(counterOfferContainer).attr("id");
//		var proposalAmount = $($(counterOfferContainer).find("#amount")[0]).html();
		
		$.ajax({
			type : "POST",
			url :"/JobSearch/desired-pay/decline?wageProposalId=" + wageProposalId,
			headers : getAjaxHeaders(),
			contentType : "application/json",
			dataType : "json",
		}).done(function(wageProposal) {	
			
			updateDOM($(counterOfferContainer), $(responseNotificationContainer),
						"Declined " + twoDecimalPlaces(wageProposal.amount) + " offer. Negotiations have ended.");
			
		}).error(function() {
			$('#home')[0].click();

		});		
	})

		
	
	$(".accept-counter").click(function(){
		
		//Read the DOM.
		var counterOfferContainer = $(this).parents(".counter-offer-container")[0];
		var responseNotification = $(counterOfferContainer).siblings(".sent-response-notification")[0];		

		//Get the wage proposal id and proposal amount
		var wageProposalId = $(counterOfferContainer).attr("id");
//		var proposalAmount = $($(counterOfferContainer).find("#amount")[0]).html();
		
//		updateDOM($(counterOfferContainer), $(responseNotification),"Accepted " + twoDecimalPlaces(proposalAmount) + " offer");
		
		$.ajax({
			type : "POST",
			url :"/JobSearch/desired-pay/accept?wageProposalId=" + wageProposalId,
			headers : getAjaxHeaders(),
			contentType : "application/json",	
			dataType : "json",
		}).done(function(wageProposal) {			
			updateDOM($(counterOfferContainer), $(responseNotification),
						"Accepted " + twoDecimalPlaces(wageProposal.amount) + " offer");
			
		}).error(function() {
			$('#home')[0].click();
		});		
	})
	
//	$(".re-counter").click(function(){
//		var $e = $($(this).siblings(".re-counter-amount-container")[0]); 
//
//		slideDown($e, 500);
//		//		$e.slideToggle(200);
////		toggleClasses($e, "hide-element", "show-block");
//	})
	
	$(".cancel-counter-offer").click(function(){
		var $e = $($(this).parents(".re-counter-amount-container")[0]);
		slideUp($e, 500);
//		toggleClasses($e, "hide-element", "show-block");
		$($(this).siblings("input")[0]).val("");
	})

	$(".send-counter-offer").click(function(){
		
		//Read the DOM.
		var counterOfferContainer = $(this).parents(".counter-offer-container")[0];
		var responseNotification = $(counterOfferContainer).siblings(".sent-response-notification")[0];
		
		var counterAmount = $($(counterOfferContainer).find("input")[0]).val();	
		
		//Create dto
		var wageProposalCounterDTO = {};
		wageProposalCounterDTO.wageProposalIdToCounter = $(counterOfferContainer).attr("id");
		wageProposalCounterDTO.counterAmount = counterAmount;

		// Update the table's row attribute
		$(this).closest("tr").attr("data-is-sent-proposal", "1");
		
		//Make ajax call
		sendCounterOffer(wageProposalCounterDTO, function(){
			updateDOM($(counterOfferContainer), $(responseNotification), twoDecimalPlaces(counterAmount) + " counter offer has been sent");
			
		})
	})		

})


	function getResponseNotificationContainer($e){
	 	return $e.siblings(".sent-response-notification")[0];		
	}

	function getCounterOfferContainer($e){
		return $e.parents(".counter-offer-container")[0];
	}
	
	
	function updateDOM($hide, $show, response){
		//After the response has been made, hide the re-counter controls.			
//		$hide.slideUp(500);
		$hide.hide();
		
		//Inform the user that the response has been sent.
		$show.html(response);
//		$show.slideDown(500);
		$show.show();
	}

	function sendCounterOffer(wageProposalCounterDTO, callback){
		
		broswerIsWaiting(true)
		$.ajax({
			type : "POST",
			url :"/JobSearch/desired-pay/counter",
			headers : getAjaxHeaders(),
			contentType : "application/json",
			data : JSON.stringify(wageProposalCounterDTO)			
		}).done(function() {	
			
			broswerIsWaiting(false);
			callback();		
			
		}).error(function() {
			$('#home')[0].click();

		});
	}	

