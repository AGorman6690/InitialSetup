	
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
		
		$.ajax({
			type : "POST",
			url :"/JobSearch//wage-proposal/employee/accept?wageProposalId=" + wageProposalId,
			headers : getAjaxHeaders(),
			contentType : "application/json",	
			dataType : "json",
		}).done(function(wageProposal) {			
			updateDOM($(counterOfferContainer), $(responseNotification),
						"Accepted " + twoDecimalPlaces(wageProposal.amount) + " offer");
			
			
			// This is hackish.
			// Consider using hyperlinks for the wage proposal actions and not ajax.
			// Ajax is not necessary.
			// Then the page can be reloaded and show the effects of accepting.
			$("#nav_profile").click();
			
		}).error(function() {
			
		});		
	})
	
	$(".send-pre-hire").click(function(){
		
		//Read the DOM.
		var counterOfferContainer = $(this).parents(".counter-offer-container")[0];
		var responseNotification = $(counterOfferContainer).siblings(".sent-response-notification")[0];		

		//Get the wage proposal id and proposal amount
		var wageProposalId = $(counterOfferContainer).attr("id");
		
		var days = $(counterOfferContainer).find(".time-container input.days-pre-hire").val();
		var hours = $(counterOfferContainer).find(".time-container input.hours-pre-hire").val();
		var minutes = $(counterOfferContainer).find(".time-container input.minutes-pre-hire").val();
		
		if(days == undefined) days = 0;
		if(hours == undefined) hours = 0;
		if(minutes == undefined) minutes = 0;
		
		$.ajax({
			type : "POST",
			url :"/JobSearch/employer/accept?wageProposalId=" + wageProposalId + "&days=" + days
										+ "&hours=" + hours + "&minutes=" + minutes,			
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
	
	$(".show-post-hire-action").click(function(){
		var $response = $(this).closest(".counter-offer-response");
		var $postHireAction = $response.find(".post-hire-action"); 

		$postHireAction.toggle();
		$response.find(".re-counter-group").eq(0).toggle();
		$response.find(".decline-counter-container").eq(0).toggle();

	})
	
	$(".cancel-pre-hire").click(function(){
		
		var $response = $(this).closest(".counter-offer-response");
		$response.find(".post-hire-action").eq(0).hide();
		$response.find(".re-counter-group").eq(0).show();
		$response.find(".decline-counter-container").eq(0).show();		
		$response.find(".time-container input").each(function(){
			$(this).val("");
		})
		
		
	})
	
	
	$(".re-counter-container").click(function(){
		var $response = $(this).closest(".counter-offer-response");

		$response.find(".pre-hire").eq(0).toggle();
		$response.find(".decline-counter-container").eq(0).toggle();	
		$response.find(".re-counter-amount-container").eq(0).toggle();
	})
	
//	$(".re-counter").click(function(){
//		var $e = $($(this).siblings(".re-counter-amount-container")[0]); 
//
//		slideDown($e, 500);
//		//		$e.slideToggle(200);
////		toggleClasses($e, "hide-element", "show-block");
//	})
	
	$(".cancel-counter-offer").click(function(){
//		var $e = $($(this).parents(".re-counter-amount-container")[0]);
//		slideUp($e, 500);
//		toggleClasses($e, "hide-element", "show-block");
		$($(this).siblings("input")[0]).val("");
		
		var $response = $(this).closest(".counter-offer-response");
		$response.find(".pre-hire").eq(0).toggle();
		$response.find(".decline-counter-container").eq(0).toggle();
		$response.find(".re-counter-amount-container").eq(0).toggle();
	})

	$(".send-counter-offer").click(function(){
		
		//Read the DOM.
		var counterOfferContainer = $(this).parents(".counter-offer-container")[0];
		var responseNotification = $(counterOfferContainer).siblings(".sent-response-notification")[0];
		
		var counterAmount = $(counterOfferContainer).find(".re-counter-amount-container input").eq(0).val();	
		
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

