<%@ include file="../includes/TagLibs.jsp"%>

<div class="dropdown-container" data-wage-proposal-id="${applicationDto.currentWageProposal.id }">
	<c:choose>
		<c:when test="${applicationDto.currentWageProposal.status == -2 || 
						applicationDto.currentWageProposal.status == -1 ||
						applicationDto.currentWageProposal.status == 0 ||
						applicationDto.currentWageProposal.status == 3 }">
			
			<c:choose>
				<c:when test="${applicationDto.currentWageProposal.proposedToUserId == user.userId }">
					
					<div class="accent" data-toggle-id="response-container-${applicationDto.currentWageProposal.id }">
						<c:choose>
							<c:when test="${applicationDto.currentWageProposal.status == 3 }">
								<c:choose>
									<c:when test="${applicationDto.time_untilEmployerApprovalExpires == '-1' }">
										<p>${user.profileId == 1 ? "Your" : "The applicant's" } time has expired</p>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${user.profileId == 1 }">
												<div data-toggle-id="accept-details-${applicationDto.currentWageProposal.id }"
													class="accent">
													<p>${applicationDto.application.status == -1 ?
															 "Employer initiated contact." :
															 "Employer accepted your offer." }</p>
													<p>Waiting for your approval.</p>
												</div>
											</c:when>
											<c:otherwise>
												<p>Waiting for the applicant's approval</p>
											</c:otherwise>
										</c:choose>
												
										<p class="expiration-time">Expires in ${applicationDto.time_untilEmployerApprovalExpires }</p>					
									</c:otherwise>										
								</c:choose>	
							</c:when>	
							<c:otherwise>
								<p>Waiting for you</p>
							</c:otherwise>						
						</c:choose>
					</div>	
					
					
					
				
					<div id="response-container-${applicationDto.currentWageProposal.id }"
						 class="response-container dropdown-style">
						<div class="accent response"
						 	data-toggle-id="accept-details-${applicationDto.currentWageProposal.id }">
						 	Accept</div>
						 	
						<div id="accept-details-${applicationDto.currentWageProposal.id }"
							class="proposal-actions-container accept-actions-container">						
							<%@ include file="./AcceptWageProposal.jsp" %>				
						</div>						 	
						 				
				 		<div class="accent response"
						 	data-toggle-id="decline-details-${applicationDto.currentWageProposal.id }">
						 	Decline</div>
						 	
						<div id="decline-details-${applicationDto.currentWageProposal.id }"
							class="proposal-actions-container">
							By declining the wage proposal,
							${user.profileId == 1 ? 'your' : 'this' }
							 application will be removed.
							<div class="decline-actions proposal-actions">
								<span class="accent confirm-decline">
									<a href="/JobSearch/wage-proposal/decline?wageProposalId=${applicationDto.currentWageProposal.id }">
									Confirm</a></span>
								<span class="accent cancel">Cancel</span>
							</div>
						</div>						 	
						 				
						<div class="accent response"
						 	data-toggle-id="counter-details-${applicationDto.currentWageProposal.id }">
						 	Counter</div>	

						<div id="counter-details-${applicationDto.currentWageProposal.id }"
							class="proposal-actions-container">
							<p>Current proposal: $ ${applicationDto.currentWageProposal.amount }</p>
							<c:if test="${applicationDto.wageProposals.size() > 1 }">
								<p>Your last proposal: $ ${applicationDto.wageProposals[fn:length(applicationDto.wageProposals) - 2].amount }</p>
							</c:if>
							<div class="amount-lbl">Counter Amount</div>
							<input class="counter-amount"></input>			
							
							<div class="counter-calendar-container">
								<div class="work-days-job">
									<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay">
										<div data-date="${workDay.stringDate }"></div>
									</c:forEach>
								</div>
								<div class="work-days-application">
									<c:forEach items="${applicationDto.dateStrings_availableWorkDays }" var="dateString">
										<div data-date="${dateString }"></div>
									</c:forEach>
								</div>
								<div class="work-days-unavailable">
									<c:forEach items="${applicationDto.dateStrings_unavailableWorkDays }" var="dateString">
										<div data-date="${dateString }"></div>
									</c:forEach>
								</div>								
								<div class="calendar-container counter-calendar">
									<div class="calendar"
										data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
										data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
									</div>
								</div>
							</div>								
							
										
							<div class="counter-actions proposal-actions">
								<span class="accent confirm-counter">Send</span>
								<span class="accent cancel">Cancel</span>
							</div>					
						</div>
						
					</div>
					
				</c:when>
				<c:otherwise>
				
					<c:if test="${user.profileId == 2 && 
									applicationDto.currentWageProposal.status == 3 }">

							<c:if test="${applicationDto.application.status == -1 }">
								<p>You initiated contact.</p>
							</c:if>
							<c:choose>
								<c:when test="${applicationDto.time_untilEmployerApprovalExpires == '-1' }">
									<p>The applicant's time has expired</p>
								</c:when>
								<c:otherwise>									
									<p class="expiration-time">Expires in ${applicationDto.time_untilEmployerApprovalExpires }</p>					
								</c:otherwise>										
							</c:choose>			
					</c:if>	
				
					Waiting for ${user.profileId == 1 ? 'employer.' : 'applicant.' }
				</c:otherwise>
			</c:choose>												
		</c:when>
	
		<c:when test="${applicationDto.currentWageProposal.status == 1 }">
			Accepted
		</c:when>
		
		<c:when test="${applicationDto.currentWageProposal.status == 2 }">
			Declined
		</c:when>
		
	</c:choose>
</div>
