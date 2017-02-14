<%@ include file="../includes/TagLibs.jsp"%>


<div id="${param_wage_proposal.id}" class="counter-offer-container dropdown-container">	
								
	<c:set var="toggleId" value="${ param_wage_proposal.id}-toggle-id" />
	<span class="accent" data-toggle-id="${toggleId }">Waiting for you</span> 
	<div id="${toggleId }" class="counter-offer-response dropdown-style">
		<div class="action">
			<a class="accent ${param_is_employer == 1 ? 'show-post-hire-action' : 'accept-counter ' }">
				${param_is_employer == 1 ? 'Hire' : 'Accept' }</a>
		</div>																	
		<div class="post-hire-action">
			
			<div class="lbl">Your offer expires in:</div>
			<div class="time-container">
				<span>Days</span><select class="days"></select>
			</div>
			<div class="time-container">
				<span>Hours</span><select class="days"></select>
			</div>
			<div class="time-container">
				<span>Minutes</span><select class="days"></select>
			</div>
			<div class="send-cancel">
				<span class="accent send">Send</span>
				<span class="accent cancel">Cancel</span>
			</div>
		</div>
		<div class="action decline-counter-container"><a class="accent decline-counter">Decline</a></div>
		
		<c:set var="toggleIdCounter" value="${param_wage_proposal.id }-toggle-id-counter" />
		<div class="action re-counter-container"><span class=" re-counter" >Counter</span></div>							
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