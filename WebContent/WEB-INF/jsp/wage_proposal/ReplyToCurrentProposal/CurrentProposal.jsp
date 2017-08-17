<%@ include file="../../includes/TagLibs.jsp"%>
<div class="proposal-wrapper ${sessionScope.user.profileId == 1 ? 'context-employee' : 'context-employer' }
	${context == 'employer-make-initial-offer' ? 'counter-context' : ''}"
	data-is-partial-availability-allowed="${response.job.isPartialAvailabilityAllowed}"
	data-user-id-make-offer-to="${response.proposeToUserId }"
	data-job-id-make-offer-for="${response.job.id }"
	data-context="${sessionScope.user.profileId == 1 ? 'employee' : 'employer' }">


	<div class="button-wrapper left">
		<div class="counter-context cancel-proposal">
			<button class="text sqr-btn">Cancel</button>
		</div>
		<div class="review-context edit-proposal">
			<button class="text sqr-btn">Edit</button>
		</div>		
	</div>

		<div class="proposal-content-wrapper">
			<h1 class="counter-context">
				${context == 'employer-make-initial-offer' 
					? 'Make An Offer To ' += user_makeOfferTo.firstName += " " += user_makeOfferTo.lastName
					: 'Propose A Counter Offer'}</h1>
			<h1 class="review-context">Review</h1>			
			<div class="send-status-warning review-context">
				<%@ include file="./SendWarningMessage.jsp" %>
			</div>
			<c:choose>
				<c:when test="${user.profileId == 1 }">
					<div class="proposal-item counter-context">
						<label>This Proposal Expires In</label>
						<div class="proposal-item-content">
							<p class="red-bold">${response.time_untilEmployerApprovalExpires }</p>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="expiration-time proposal-item review-context context-employer">
						<%@ include file="./ExpirationTime.jsp" %>
					</div>						
				</c:otherwise>				
			</c:choose>		
			
			<div class="wage-proposal-wrapper proposal-item"
				data-proposed-amount="${response.currentProposal.amount }">
				<label>Wage</label>		
				<div class="proposal-item-content">
					<div class="counter-context">				
						<c:if test="${context != 'employer-make-initial-offer' }">	
							<div class="proposed-offer hide-on-load">
								<p class="proposed-amount">${user.profileId == 1 ? "Employer" : "Applicant" }
									proposed $ ${response.currentProposal.amount } / hr</p>
							</div>				
						</c:if>		
						<div class="${context == 'employer-make-initial-offer' ? 'initial-offer' : 'counter-offer' }">									
							<p class="red-bold">${context == 'employer-make-initial-offer'
								 ? 'Propose a wage' : 'Enter a counter offer' } ($ / hr)</p>
							<input class="select-all counter-wage-amount" type="text"
								value="${response.currentProposal.amount }"/>						
						</div>	
					</div>
					<div class="review-context">
						<p>You are <span class="red-bold accepting-or-proposing"></span> a $<span class="wage-amount"></span> / hr wage 
					</div>		
				</div>					
			</div>
			<c:if test="${response.job.isPartialAvailabilityAllowed }">
				<div class="proposal-item work-day-proposal-wrapper"
					 data-proposed-work-days="${response.currentProposal.proposedDates }">
					<label class="">Work Days</label>		
					<div class="proposal-item-content">
						<div class="conflicting-applications-countering"></div>						
						<p class="red-bold counter-context">
							Select ${context != 'employer-make-initial-offer' ? 'or deselect ' : '' } your proposed work days</p>
						<p class="review-context">You are <span class="red-bold accepting-or-proposing"></span> the following work days</p>							
						
						<div class="counter-context review-context v2 teal-title proposal-calendar
							 calendar-container wage-proposal-calendar hide-prev-next hide-unused-rows">	
							<button class="hide-on-load counter-context sqr-btn gray-3 select-all-work-days-override">
								Select All Work Days</button>								
							<div class="calendar counter-calendar ${user.profileId == 1 ? 'find-conflicting-applications-on-select' : ''}"
								data-min-date="${response.date_firstWorkDay }"
								data-number-of-months=${response.monthSpan_allWorkDays }>
							</div>
						</div>
					</div>									
				</div>				
			</c:if>
		</div>
		<div class="button-wrapper right">
			<div class="counter-context review-proposal">
				<button class="text sqr-btn">Review</button>
			</div>
			<div class="review-context send-proposal accepting-offer-context">
				<button class="text sqr-btn">Accept Employment</button>
			</div>	
			<div class="review-context send-proposal proposing-new-offer-context">
				<button class="text sqr-btn">Send New Proposal</button>
			</div>	
		</div>
</div>