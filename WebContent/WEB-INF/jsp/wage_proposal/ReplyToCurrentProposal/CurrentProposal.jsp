<%@ include file="../../includes/TagLibs.jsp"%>
<div class="proposal-wrapper ${sessionScope.user.profileId == 1 ? 'context-employee' : 'context-employer' }"
	data-is-partial-availability-allowed="${jobDto.job.isPartialAvailabilityAllowed}"
	data-user-id-make-offer-to="${user_makeOfferTo.userId }"
	data-job-id-make-offer-for="${jobDto.job.id }"
	data-context="${sessionScope.user.profileId == 1 ? 'employee' : 'employer' }">
	<h1 class="counter-context">
		${context == 'employer-make-initial-offer' 
			? 'Make An Offer To ' += user_makeOfferTo.firstName += " " += user_makeOfferTo.lastName
			: 'Propose A Counter Offer'}</h1>
	<h1 class="review-context">Review</h1>	

	<div class="button-wrapper left">
		<div class="counter-context cancel-proposal">
			<span class="glyphicon glyphicon-menu-left"></span><span class="text">Cancel</span>
		</div>
		<div class="review-context edit-proposal">
			<span class="glyphicon glyphicon-menu-left"></span><span class="text">Edit</span>
		</div>		
	</div>

		<div class="proposal-content-wrapper">
			<div class="send-status-warning review-context">
				<%@ include file="./SendWarningMessage.jsp" %>
			</div>
			<c:choose>
				<c:when test="${user.profileId == 1 }">
					<div class="proposal-item counter-context">
						<label>This Proposal Expires In</label>
						<div class="proposal-item-content">
							<p class="red-bold">${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
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
				data-proposed-amount="${applicationDto.employmentProposalDto.amount }">
				<label>Wage Proposal</label>		
				<div class="proposal-item-content">
					<div class="counter-context">				
						<c:if test="${context != 'employer-make-initial-offer' }">	
							<div class="proposed-offer hide-on-load">
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
						<p>You are <span class="red-bold accepting-or-proposing"></span> a $<span class="wage-amount"></span> / hr wage 
					</div>		
				</div>					
			</div>
			<c:if test="${jobDto.job.isPartialAvailabilityAllowed}">
				<div class="proposal-item work-day-proposal-wrapper" data-proposed-work-days="${datestrings_workDays }">
					<label class="">Work Day Proposal</label>		
					<div class="proposal-item-content">
						<div class="conflicting-applications-countering"></div>						
						<p class="red-bold counter-context">
							Select ${context != 'employer-make-initial-offer' ? 'or deselect ' : '' } your proposed work days</p>
						<p class="review-context">You are <span class="red-bold accepting-or-proposing"></span> the following work days</p>							
						
						<div class="counter-context review-context v2 teal-title proposal-calendar
							 calendar-container wage-proposal-calendar hide-prev-next hide-unused-rows">	
							<button class="counter-context sqr-btn gray-3 select-all-work-days-override">
								Select All Work Days</button>								
							<div class="calendar counter-calendar ${user.profileId == 1 ? 'find-conflicting-applications-on-select' : ''}"
								data-min-date="${jobDto.date_firstWorkDay }"
								data-number-of-months=${jobDto.months_workDaysSpan }>
							</div>
						</div>
					</div>									
				</div>				
			</c:if>
		</div>
		<div class="button-wrapper right">
			<div class="counter-context review-proposal">
				<span class="text">Review</span>
				<span class="glyphicon glyphicon-menu-right"></span>
			</div>
			<div class="review-context send-proposal accepting-offer-context">
				<span class="text">Accept Employment</span>
				<span class="glyphicon glyphicon-menu-right"></span>
			</div>	
			<div class="review-context send-proposal proposing-new-offer-context">
				<span class="text">Send New Proposal</span>
				<span class="glyphicon glyphicon-menu-right"></span>
			</div>	
		</div>
</div>