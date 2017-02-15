<%@ include file="../includes/TagLibs.jsp"%>


<div id="${param_wage_proposal.id}" class="counter-offer-container dropdown-container">	
								
	<c:set var="toggleId" value="${ param_wage_proposal.id}-toggle-id" />
	<span class="accent" data-toggle-id="${toggleId }">Waiting for you</span> 
	<div id="${toggleId }" class="counter-offer-response dropdown-style">
		<div class="pre-hire action">
			<a data-toggle-id="conflicting-apps-${param_wage_proposal.id }" class="accent ${param_is_employer == 1 ? 'show-post-hire-action' : 'accept-counter ' }">
				${param_is_employer == 1 ? 'Hire (almost)' : 'Accept' }</a>
			<div class="conflicting-apps-container sub-section">
				Conflicting Applications:
				By accepting this proposal, your following applications will be removed.
				<c:forEach items="${dto.conflictingApplications }" var="application">
					<div><a class="accent" href="/JobSearch/job/${application.job.id }
							?c=profile-incomplete&p=1">${application.job.jobName }</a></div>
				</c:forEach>
			</div>
		</div>																	
		<div class="post-hire-action">
			
			<div class="lbl">Your offer expires in:</div>
			<div class="time-container">
				<span>Days</span><input class="days-pre-hire" />
			</div>
			<div class="time-container">
				<span>Hours</span><input class="hours-pre-hire" />
			</div>
			<div class="time-container">
				<span>Minutes</span><input class="minutes-pre-hire" />
			</div>
			<div class="send-cancel">
				<span class="accent send-pre-hire">Send</span>
				<span class="accent cancel-pre-hire">Cancel</span>
			</div>
		</div>
		<div class="action decline-counter-container">
			<a class="accent decline-counter">Decline</a>
		</div>
		
		<div class="re-counter-group">
			<div class="action re-counter-container">
				<span class="accent re-counter" data-toggle-id="${param_wage_proposal.id }-toggle-id-counter">Counter</span>
			</div>							
			<div id="${param_wage_proposal.id }-toggle-id-countersadfasdf" class="re-counter-amount-container" >
				<div>Amount</div>
				<input class="re-counter-amount"></input>
				<div class="send-cancel-container">
					<a class="accent send-counter-offer">Send</a>
					<a class="accent cancel-counter-offer">Cancel</a>
				</div>
			</div>							
		</div>					
	</div>	
</div>
<div class="sent-response-notification"></div>	