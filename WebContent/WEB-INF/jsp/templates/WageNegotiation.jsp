<%@ include file="../includes/TagLibs.jsp"%>


<div id="${param_wage_proposal.id}" class="counter-offer-container">	
								
	<c:set var="toggleId" value="${ param_wage_proposal.id}-toggle-id" />
	<span class="accent" data-toggle-id="${toggleId }">Waiting for you</span> 
	<div id="${toggleId }" class="counter-offer-response">
		<a class="accent accept-counter">${param_is_employer == 1 ? 'Hire' : 'Accept' }</a>																	
		<a class="accent decline-counter">Decline</a>	
		
		<c:set var="toggleIdCounter" value="${param_wage_proposal.id }-toggle-id-counter" />
		<a class="accent re-counter" data-toggle-id="${toggleIdCounter }" data-toggle-speed="1">Counter</a>							
		<div id="${toggleIdCounter }" class="re-counter-amount-container" >
			<div>Amount</div>
			<input class="re-counter-amount"></input>
			<div class="send-cancel-container">
				<a class="accent send-counter-offer">Send</a>
				<a class="accent cancel-counter-offer">Cancel</a>
			</div>
		</div>												
	</div>	
</div>
<div class="sent-response-notification"></div>	