<%@ include file="../includes/TagLibs.jsp"%>
<div>
	<div>
		<div class="a-proposal">
			<c:if test="${!empty applicationProgressStatus.previousProposal &&
 			 	applicationProgressStatus.application.isAccepted == 0 }">
				<label class="">${applicationProgressStatus.isProposedToSessionUser ?
	 				'Your last proposal' :
	 				sessionScope.user.profileId == 1 ? "Employer's last proposal" :
	 				"Applicant's last proposal"}</label>
	 			<div class="proposed-value">
					<span class="dollar-sign">$</span>
					<fmt:formatNumber type="number" minFractionDigits="2" 
 						maxFractionDigits="2"
						value="${applicationProgressStatus.previousProposal.amount}" /> / hr
				</div>
				<div class="proposed-value">
				
		 			<c:if test="${param_job.isPartialAvailabilityAllowed}"> 							 
		 				${applicationProgressStatus.previousProposal.proposedDates.size() } 
		 				 of ${applicationProgressStatus.countJobWorkDays } ${applicationProgressStatus.countJobWorkDays == 1 ? 'day' : 'days' }		 
		 			</c:if> 
	 			</div>
			</c:if>
		</div>
			<div class="a-proposal">
			<label class="">${applicationProgressStatus.application.isAccepted == "1" ? "Wage" :
				!applicationProgressStatus.isProposedToSessionUser ?
				'Your current proposal' : 
				sessionScope.user.profileId == 1 ? "Employer's current proposal" :
				"Applicant's current proposal"}</label>			
			<div class="proposed-value">
				<span class="dollar-sign">$</span>
				<fmt:formatNumber type="number" minFractionDigits="2" 
					maxFractionDigits="2"
					value="${applicationProgressStatus.currentProposal.amount}"/> / hr
			</div>
			<div class="proposed-value">
				<c:if test="${param_job.isPartialAvailabilityAllowed }"> 	
					${applicationProgressStatus.currentProposal.proposedDates.size() }
					 of ${applicationProgressStatus.countJobWorkDays } ${applicationProgressStatus.countJobWorkDays == 1 ? 'day' : 'days' }			
				</c:if>
			</div>
		</div>
	</div>
	
	<c:if test="${applicationProgressStatus.isProposedToSessionUser &&
					applicationProgressStatus.application.isAccepted == 0}">	
					
		<div class="respond-to-proposal">
			<c:choose>
				<c:when test="${sessionScope.user.profileId == 2 || (sessionScope.user.profileId == 1 &&
						applicationProgressStatus.currentProposal.flag_employerAcceptedTheOffer == 0) }">
					<button class="sqr-btn blue accept-current-proposal">Accept</button>
					<button class="sqr-btn blue counter-current-proposal">Counter</button>	
				</c:when>
				<c:otherwise>
					<button class="sqr-btn blue accept-current-proposal">Confirm</button>
				</c:otherwise>
			</c:choose>
		
			<button class="sqr-btn blue decline-current-proposal">Decline</button>	
			<div class="render-present-proposal-mod"></div>
		</div>
	</c:if>	
</div>	
