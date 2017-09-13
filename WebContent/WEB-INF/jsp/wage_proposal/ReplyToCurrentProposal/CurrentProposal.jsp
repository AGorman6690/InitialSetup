<%@ include file="../../includes/TagLibs.jsp"%>
<div class="proposal-wrapper ${sessionScope.user.profileId == 1 ? 'context-employee' : 'context-employer' }
	${response.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'employer-accepted-offer' : ''}
	${context == 'employer-make-initial-offer' ? 'employer-make-initial-offer' : ''}"
	data-is-partial-availability-allowed="${response.job.isPartialAvailabilityAllowed}"
	data-user-id-make-offer-to="${response.proposeToUserId }"
	data-job-id-make-offer-for="${response.job.id }"
	data-context="${sessionScope.user.profileId == 1 ? 'employee' : 'employer' }">


		<div class="proposal-content-wrapper">
			<h1 class="hide-on-load">
				${context == 'employer-make-initial-offer' 
					? 'Make An Offer To ' += user_makeOfferTo.firstName += " " += user_makeOfferTo.lastName
					: 'Proposal'}</h1>
						
			<c:choose>
				<c:when test="${user.profileId == 1 }">
					<div class="proposal-item">
						<label>This Proposal Expires In</label>
						<div class="proposal-item-content">
							<p class="red-bold-old">${response.time_untilEmployerApprovalExpires }</p>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="expiration-time proposal-item">
						<%@ include file="./ExpirationTime.jsp" %>
					</div>						
				</c:otherwise>				
			</c:choose>		
			
			<div class="wage-proposal-wrapper proposal-item"
				data-proposed-amount="${response.currentProposal.amount }">
				<label>Wage</label>		
				<div class="proposal-item-content">				
					<div class="${context == 'employer-make-initial-offer' ? 'initial-offer' : 'counter-offer' }">									
						<p class="red-bold-old">${context == 'employer-make-initial-offer'
							 ? 'Propose a wage you want to pay the applicant'
							 : response.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'Final wage'
							 : 'Edit to propose a new wage' } ($ / hr)</p>
						<input class="select-all counter-wage-amount" type="text"
							value="${response.currentProposal.amount }"
							${response.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'disabled' : '' }/>						
					</div>	
				</div>	
				<div class="proposal-status-wrapper">
					<p>Status</p>
					<button class="current-status status-accepting">Accepting</button>
					<button class="status-proposing">Proposing</button>
				</div>				
			</div>
			<c:if test="${response.job.isPartialAvailabilityAllowed }">
				<div class="proposal-item work-day-proposal-wrapper"
					 data-proposed-work-days="${response.currentProposal.proposedDates }">
					<label class="">Work Days</label>		
					<div class="proposal-item-content">
						<div class="conflicting-applications-countering"></div>						
						<p class="red-bold-old">
							${context == 'employer-make-initial-offer' ? 'Propose the days you want the applicant to work' 
							: response.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'Final work days' 
							: 'Edit to propose a new work day schedule' }</p>					
						<div class="v2 teal-title proposal-calendar negotiating-context
							 calendar-container wage-proposal-calendar hide-prev-next hide-unused-rows
							 ${response.currentProposal.flag_employerAcceptedTheOffer == 1 ? 'read-only' : '' }">	
							<button class="counter-context sqr-btn gray-3 select-all-work-days-override">
								Select All Work Days</button>								
							<div class="calendar counter-calendar ${user.profileId == 1 ? 'find-conflicting-applications-on-select' : ''}"
								data-min-date="${response.date_firstWorkDay }"
								data-number-of-months=${response.monthSpan_allWorkDays }>
							</div>
						</div>
					</div>		
					<div class="proposal-status-wrapper">
						<p>Status</p>
						<button class="current-status status-accepting">Accepting</button>
						<button class="status-proposing">Proposing</button>
					</div>													
				</div>	
							
			</c:if>
			<div class="warning-wrapper-1">
				<div class="warning-wrapper">
					<div class="send-status-warning">
						<%@ include file="./SendWarningMessage.jsp" %>					
					</div>
					<div class="send-proposal-wrapper">
						<button class="text proposing-new-offer-context">
							${context == 'employer-make-initial-offer' ? 'Send Initial Offer'
							: 'Send New Proposal' }</button>		
						<button class="text accepting-offer-context context-employee">
							Accept Employment</button>	
						<button class="text accepting-offer-context context-employer">
							Accept Offer</button>								
					</div>
				</div>
			</div>				
		</div>

</div>