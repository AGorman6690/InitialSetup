$(document).ready(function(){
	
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

		var wageProposalDto = {};
		wageProposalDto.wageProposalIdToCounter = $(this).parents("[data-wage-proposal-id]")
																.eq(0)
																.attr("data-wage-proposal-id");
		
		wageProposalDto.counterAmount = $(this).closest(".proposal-actions-container")
														.find("input.counter-amount").eq(0).val();;

		// Update the table's row attribute
		$(this).closest("tr").attr("data-is-sent-proposal", "1");
		
		broswerIsWaiting(true);
		$.ajax({
			type : "POST",
			url :"/JobSearch/wage-proposal/counter",
			headers : getAjaxHeaders(),
			contentType : "application/json",
			data : JSON.stringify(wageProposalDto)			
		}).done(function() {	
			
			broswerIsWaiting(false);
			location.reload(true);

		}).error(function() {
			
			broswerIsWaiting(false);
		});	
	})

	
})