<%@ include file="../includes/TagLibs.jsp"%>

<div class="application-proposal-container
	 ${applicationDto.employmentProposalDto.isProposedToSessionUser ? 'action-required' : '' }
	 ${applicationDto.application.isAccepted == 1 ? 'accepted' : '' }">
	<div class="expiration-time-cont"> 
		<c:set var="doSkipRemaingHtml" value="false"></c:set>		
		<c:choose>
			<c:when test="${applicationDto.application.isAccepted == 1 }">
				<p>You are hired</p>
			</c:when>
			<c:otherwise>
				<c:if test="${applicationDto.previousProposal.flag_isCanceledDueToApplicantAcceptingOtherEmployment == 1 }">
					<p>${user.profileId == 1 ? 'You' : 'Applicant'} accepted other employment.</p>
					<p>The proposed work days have been updated to resolve overlapping work days.</p>
				</c:if>	
				
				<c:if test="${applicationDto.previousProposal.flag_isCanceledDueToEmployerFillingAllPositions == 1 }">
					<c:choose>
						<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">		
							<p>${user.profileId == 2 ? 'You' : 'Employer'} filled all positions on select work days. The proposal has been updated to remove the work days that have been filled.</p>
						</c:when>
						<c:otherwise>
							<c:set var="doSkipRemaingHtml" value="true"></c:set>
							<c:if test="${applicationDto.application.flag_applicantAcknowledgedAllPositionsAreFilled == 0}">
								<p>${user.profileId == 2 ? 'You' : 'Employer'} filled all positions.</p>
								<p>${user.profileId == 2 ? "The applicant's" : 'Your' } proposal will remain in ${user.profileId == 2 ? "your" : "the employer's"} proposal inbox.</p>
								<a class="sqr-btn gray-2" href="/JobSearch/application/${applicationDto.application.applicationId}/all-positions-filled/acknowledge">OK</a>
							</c:if>							
						</c:otherwise>	
					</c:choose>		
				</c:if>				
			</c:otherwise>
		</c:choose>		
		<c:if test="${!doSkipRemaingHtml && applicationDto.application.isAccepted == 0 }">			
			<c:if test="${applicationDto.employmentProposalDto.flag_employerInitiatedContact == 1 }">
				<p>${user.profileId == 2 ? 'You' : 'Employer'} initiated contact</p>
			</c:if>			
			<c:choose>	
				<c:when test="${ applicationDto.previousProposal.flag_applicationWasReopened == 1 }">				
					<c:choose>
						<c:when test="${ applicationDto.previousProposal.flag_aProposedWorkDayWasRemoved == 1 }">							
							<c:choose>
								<c:when test="${user.profileId == 1 }">
									<p>The employer deleted work days from the job posting that affect your employment.</p>
									<p>The employer is required to submit you a new proposal.</p>						
								</c:when>	
								<c:otherwise>
									<p>You deleted work days from the previously agreed upon proposal.</p>
									<p>You are required to submit a new proposal to the applicant.</p>	
								</c:otherwise>
							</c:choose>	
						</c:when>				
						<c:when test="${ applicationDto.previousProposal.flag_aProposedWorkDayTimeWasEdited == 1 }">							
							<c:choose>
								<c:when test="${user.profileId == 1 }">
									<p>The employer edited the start and end times that affect your employment.</p>
									<p>The employer is required to submit you a new proposal.</p>						
								</c:when>	
								<c:otherwise>
									<p>You edited the start and end times that affect your employment.</p>
									<p>The applicant is reviewing your new proposal.</p>	
								</c:otherwise>
							</c:choose>	
						</c:when>				
					</c:choose>				
	<!-- 				********************************************** -->
	<!-- 				**********************************************		 -->
	<!-- 				Add flags to the job table when dates are added or removed. -->
	<!-- 				Notify applicant that dates have changed that do not affect their current proposal -->
	<!-- 				********************************************** -->
	<!-- 				**********************************************				 -->
				</c:when>	
				<c:otherwise>
	<%-- 				<c:choose> --%>
						<c:if test="${ applicationDto.employmentProposalDto.flag_aProposedWorkDayWasRemoved == 1 ||
										applicationDto.previousProposal.flag_aProposedWorkDayWasRemoved == 1 }">							
							<c:choose>
								<c:when test="${user.profileId == 1 }">
									<p>The employer deleted work days from the job posting that affected your proposal.</p>	
								</c:when>	
								<c:otherwise>
									<p>You deleted work days from the job posting that affected the applicant's proposal.</p>
								</c:otherwise>
							</c:choose>	
						</c:if>				
						<c:if test="${ applicationDto.employmentProposalDto.flag_aProposedWorkDayTimeWasEdited == 1 ||
										applicationDto.previousProposal.flag_aProposedWorkDayTimeWasEdited == 1 }">							
							<c:choose>
								<c:when test="${user.profileId == 1 }">
									<p>The employer updated times for work days on the current proposal.</p>	
								</c:when>	
								<c:otherwise>
									<p>You  updated times for work days on the current proposal.</p>	
								</c:otherwise>
							</c:choose>	
						</c:if>				
	<%-- 				</c:choose>				 --%>
				</c:otherwise>
			</c:choose>					
			<c:choose>
				<c:when test="${applicationDto.employmentProposalDto.isProposedToSessionUser }">
					<p>Waiting for you</p>
					<c:if test="${user.profileId == 1 }">						
						<p>
							<span>Employer's offer expires in:</span>								
							<span class="expiration-time">${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</span>
						</p>
					</c:if>									
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${user.profileId == 2 }">		
							<p>Waiting for applicant</p>			
							<p>
								<span>Your offer expires in:</span>								
								<span class="expiration-time">${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</span>
							</p>
						</c:when>
						<c:otherwise>
							<p>Waiting for employer</p>
						</c:otherwise>
					</c:choose>				
				</c:otherwise>
			</c:choose>		
		</c:if>		
	</div>

	<c:if test="${!doSkipRemaingHtml }">
		<c:if test="${!empty applicationDto.previousProposal && applicationDto.application.isAccepted == 0 }">
			<div class="previous-proposal">				
				<c:set var="p_proposal" value="${applicationDto.previousProposal }"></c:set>
				<p class="proposal-lbl">${p_proposal.proposedByUserId == user.userId ? 'Your last proposal' : 
						user.profileId == 1 ? "Employer's last proposal" : "Applicant's last proposal"}
				<%@ include file="./Proposal_Details.jsp" %>
			</div>
		</c:if>
		<div class="current-proposal">				
			<c:set var="p_proposal" value="${applicationDto.employmentProposalDto }"></c:set>
			<c:if test="${applicationDto.application.isAccepted == 0 }">
				<p class="proposal-lbl">${p_proposal.proposedByUserId == user.userId ? 'Your current proposal' : 
						user.profileId == 1 ? "Employer's current proposal" : "Applicant's current proposal"}</p>
			</c:if>
			<%@ include file="./Proposal_Details.jsp" %>
		</div>		
		<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser &&
						applicationDto.application.isAccepted == 0}">	
			<div class="proposal-item respond">
				<button class="sqr-btn gray-3 show-mod accept-current-proposal">Accept</button>
				<button class="sqr-btn gray-3 show-mod counter-current-proposal">Counter</button>	
				<div class="render-present-proposal-mod"></div>
			</div>
		</c:if>	
		<c:if test="${user.profileId == 1 &&
						applicationDto.application.isAccepted == 1}">	
			<div class="proposal-item respond">
				<a href="/JobSearch/employee/leave-job/${jobDto.job.id}/confirm" class="sqr-btn gray-2">Leave</a>	
			</div>
		</c:if>		
	</c:if>
</div>

