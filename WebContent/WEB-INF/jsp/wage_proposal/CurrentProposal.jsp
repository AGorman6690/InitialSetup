<%@ include file="../includes/TagLibs.jsp"%>
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
			applicationProgressStatus.currentProposalLabel }</label>			
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
	<c:if test="${ applicationProgressStatus.application.isAccepted == 0 && 
					applicationProgressStatus.currentProposal.flag_hasExpired == 0 }">
					
		<c:if test="${ (applicationProgressStatus.isProposedToSessionUser && 
					sessionScope.user.profileId == 1) || 
					(!applicationProgressStatus.isProposedToSessionUser && 
					sessionScope.user.profileId == 2)}">
			<div class="a-proposal">
				<label class="exp-time black-bold">Expiration</label>
				<div class="proposed-value">
					<span class="">${applicationProgressStatus.time_untilEmployerApprovalExpires }</span>
				</div>
			
			</div>
		</c:if>
	</c:if>			
	<c:if test="${applicationProgressStatus.isProposedToSessionUser &&
					applicationProgressStatus.application.isAccepted == 0}">	
		<div class="respond-to-proposal">
			<c:choose>
				<c:when test="${applicationProgressStatus.isCurrentProposalExpired }">
					<c:if test="${sessionScope.user.profileId == 2 }">
						<button class="sqr-btn blue accept-current-proposal make-new-offer">Make new offer</button>
					</c:if>
				 </c:when>					
				<c:when test="${applicationProgressStatus.currentProposal.flag_employerAcceptedTheOffer == 1 }">
					<c:if test="${sessionScope.user.profileId == 1 }">
						<button class="sqr-btn blue accept-current-proposal">Confirm Employment</button>
					</c:if>					
				</c:when>
				<c:otherwise>
					<button class="sqr-btn blue accept-current-proposal">Accept or Counter</button>
				</c:otherwise>
			</c:choose>
		
			<a href="/JobSearch/proposal/decline/${applicationProgressStatus.currentProposal.proposalId }"
				 class="sqr-btn gray decline-current-proposal">Decline</a>	
			<div class="render-present-proposal-mod"></div>
		</div>
	</c:if>	
</div>	
