<%@ include file="../includes/TagLibs.jsp"%>

<div class="application-proposal-container
	 ${applicationDto.employmentProposalDto.isProposedToSessionUser ? 'action-required' : '' }
	 ${applicationDto.application.isAccepted == 1 ? 'accepted' : '' }">
	<div class="expiration-time-cont"> 
		<c:set var="doSkipRemaingHtml" value="false"></c:set>
		
		<c:choose>
			<c:when test="${applicationDto.application.isAccepted == 1 }">
				<p>Accepted</p>
			</c:when>
			<c:otherwise>
				<c:if test="${applicationDto.previousProposal.isCanceledDueToApplicantAcceptingOtherEmployment == 1 }">
					<p>${user.profileId == 1 ? 'You' : 'Applicant'} accepted other employment</p>
					<p>The proposed work days have been updated</p>
				</c:if>	
				
				<c:if test="${applicationDto.previousProposal.isCanceledDueToEmployerFillingAllPositions == 1 }">
					<c:choose>
						<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">		
							<p>${user.profileId == 2 ? 'You' : 'Employer'} filled all positions on select work days. The proposed work days have been updated.</p>
						</c:when>
						<c:otherwise>
							<c:set var="doSkipRemaingHtml" value="true"></c:set>
							<c:if test="${applicationDto.application.flag_applicantAcknowledgedAllPositionsAreFilled == 0}">
								<p>${user.profileId == 2 ? 'You' : 'Employer'} filled all positions</p>
								<p>${user.profileId == 2 ? "The applicant's" : 'Your' } proposal will remain in ${user.profileId == 2 ? "your" : "the employer's"} proposal inbox</p>
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
									<p>Please review.</p>							
								</c:when>	
								<c:otherwise>
									<p>You deleted work days from the agreed upon proposal.</p>
									<p>The applicant is reviewing your new proposal.</p>	
								</c:otherwise>
							</c:choose>
	
						</c:when>				
						<c:when test="${ applicationDto.previousProposal.flag_aProposedWorkDayTimeWasEdited == 1 }">
							
							<c:choose>
								<c:when test="${user.profileId == 1 }">
									<p>The employer edited the start and end times that affect your employment.</p>
									<p>The employer is required to submit you a new proposal.</p>
									<p>Please review.</p>							
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
		<div class="proposal-item amount">		
			<p><%@ include file="../wage_proposal/History_WageProposals.jsp" %></p>
		</div>	
		<c:if test="${jobDto.job.isPartialAvailabilityAllowed }"> 	
			<div class="proposal-item work-days">
				<c:choose>
					<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
						<p>${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days
							<span class="glyphicon glyphicon-menu-down"></span>
						</p>
					</c:when>
					<c:otherwise>
						<p>${jobDto.workDays.size() } days<span class="glyphicon glyphicon-menu-down"></span></p>
					</c:otherwise>
				</c:choose>		
				<div class="mod simple-header">
					<div class="mod-content">
						<div class="mod-header">
							<span class="glyphicon glyphicon-remove"></span>
						</div>
						<div class="mod-body">
							<div class="v2 calendar-container proposal-calendar hide-unused-rows hide-prev-next read-only">
								<div class="calendar"
									data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
									data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
								</div>										
							</div>
						</div>
					</div>	
				</div>		
		
			</div>
		</c:if>
		<c:if test="${applicationDto.employmentProposalDto.isProposedToSessionUser &&
						applicationDto.application.isAccepted == 0}">	
			<div class="proposal-item respond">
				<button class="sqr-btn gray-2 show-mod">Respond</button>	
				<div class="present-proposal"></div>
			</div>
		</c:if>	
	</c:if>
</div>

