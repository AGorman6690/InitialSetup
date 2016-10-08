	


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
		var proposalAmount = $($(counterOfferContainer).find("#amount")[0]).html();
		
		$.ajax({
			type : "POST",
			url :"/JobSearch/desired-pay/decline?wageProposalId=" + wageProposalId,
			headers : getAjaxHeaders(),
			contentType : "application/json",		
		}).done(function() {	
			
			updateDOM($(counterOfferContainer), $(responseNotificationContainer),
						"Declined " + twoDecimalPlaces(proposalAmount) + " offer. Negotiations have ended.");
			
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
		var proposalAmount = $($(counterOfferContainer).find("#amount")[0]).html();
		
		$.ajax({
			type : "POST",
			url :"/JobSearch/desired-pay/accept?wageProposalId=" + wageProposalId,
			headers : getAjaxHeaders(),
			contentType : "application/json",		
		}).done(function() {			
			updateDOM($(counterOfferContainer), $(responseNotification),"Accepted " + twoDecimalPlaces(proposalAmount) + " offer");
			
		}).error(function() {
			$('#home')[0].click();

		});		
	})
	
	$(".re-counter").click(function(){
		var $e = $($(this).siblings(".re-counter-amount-container")[0]); 
		toggleClasses($e, "hide-element", "show-block");
	})
	
	$(".cancel-counter-offer").click(function(){
		$(this).parent().hide();
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
		$hide.hide();
		
		//Inform the user that the response has been sent.
		$show.html(response);
		$show.show();
	}

	function sendCounterOffer(wageProposalCounterDTO, callback){
		
		$.ajax({
			type : "POST",
			url :"/JobSearch/desired-pay/counter",
			headers : getAjaxHeaders(),
			contentType : "application/json",
			data : JSON.stringify(wageProposalCounterDTO)			
		}).done(function() {		
			callback();		
			
		}).error(function() {
			$('#home')[0].click();

		});
	}	

