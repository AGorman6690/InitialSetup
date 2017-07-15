<%@ include file="../../includes/TagLibs.jsp"%>
<div class="wrapper ${context } ${sessionScope.user.profileId == 1 ? 'context-employee' : 'context-employer' }"
	data-is-partial-availability-allowed="${jobDto.job.isPartialAvailabilityAllowed}"
	data-user-id-make-offer-to="${user_makeOfferTo.userId }"
	data-job-id-make-offer-for="${jobDto.job.id }">
	<h1 class="counter-context context">
	${context111}
		${context == 'employer-make-initial-offer' 
			? 'Make An Offer To ' += user_makeOfferTo.firstName += " " += user_makeOfferTo.lastName
			: 'Propose A Counter Offer'}</h1>
	<h1 class="review-context context">Review</h1>		
	<c:if test="${user.profileId == 1 }">
		<div class="context-employee counter-context">
			<h3 class="blue">This Proposal Expires In</h3>
			<p class="red-bold">${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
		</div>
	</c:if>
	<c:if test="${context == 'employer-make-initial-offer' }">
		
	</c:if>
		<div class="button-wrapper left">
			<p class="counter-context cancel-proposal linky-hover">
				<span class="glyphicon glyphicon-menu-left"></span><span class="text">Cancel</span></p>
			<p class="review-context edit-proposal linky-hover">
				<span class="glyphicon glyphicon-menu-left"></span><span class="text">Edit</span></p>		
		</div>
		<div class="proposal-content-wrapper">
		
			<div class="send-status-warning review-context">
				<%@ include file="./SendWarningMessage.jsp" %>
			</div>
			<div class="proposal expiration-time review-context context-employer">
				<%@ include file="./ExpirationTime.jsp" %>
			</div>		
			<div class="proposal wage-proposal" data-proposed-amount="${applicationDto.employmentProposalDto.amount }">
				<h3 class="blue">Wage Proposal</h3>		
				<div class="counter-context">				
					<c:if test="${context != 'employer-make-initial-offer' }">	
						<div class="proposed-offer">
							<p class="proposed-amount">${user.profileId == 1 ? "Employer" : "Applicant" }
								proposed $ ${applicationDto.employmentProposalDto.amount } / hr</p>
						</div>				
					</c:if>		
					<div class="${context == 'employer-make-initial-offer' ? 'initial-offer' : 'counter-offer' }">									
						<p class="red-bold">${context == 'employer-make-initial-offer'
							 ? 'Propose a wage' : 'Enter a counter offer' } ($ / hr)</p>
						<input class="counter-wage-amount" type="text"
							value="${applicationDto.employmentProposalDto.amount }"/>						
					</div>	
				</div>
				<div class="review-context">
					<p>You are <span class="accepting-or-proposing"></span> a $<span class="wage-amount"></span> / hr wage 
				</div>							
			</div>
			<c:if test="${jobDto.job.isPartialAvailabilityAllowed}">
				<div class="proposal work-day-proposal" data-proposed-work-days="${datestrings_workDays }">
					<h3 class="blue">Work Day Proposal</h3>								
					<p class="red-bold counter-context">
						Select ${context != 'employer-make-initial-offer' ? 'or deselect ' : '' } your proposed work days</p>
					<p class="review-context">You are <span class="accepting-or-proposing"></span> the following work days</span>							
					<div class="conflicting-applications-countering"></div>
					<div class="counter-context review-context v2 teal-title proposal-calendar calendar-container wage-proposal-calendar
						 hide-prev-next hide-unused-rows">	
						<button class="counter-context sqr-btn gray-3 select-all-work-days-override">
							Select All Work Days</button>								
						<div class="calendar counter-calendar ${user.profileId == 1 ? 'find-conflicting-applications-on-select' : ''}"
							data-min-date="${jobDto.date_firstWorkDay }"
							data-number-of-months=${jobDto.months_workDaysSpan }>
						</div>
					</div>									
				</div>				
			</c:if>
		</div>
		<div class="button-wrapper right">
			<p class="counter-context review-proposal linky-hover">
				<span class="glyphicon glyphicon-menu-right"></span><span class="text">Review</span></p>
			<p class="review-context send-proposal linky-hover">
				<span class="glyphicon glyphicon-menu-right"></span><span class="text">Send</span></p>	
		</div>
</div>