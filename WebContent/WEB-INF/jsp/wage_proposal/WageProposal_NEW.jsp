<%@ include file="../includes/TagLibs.jsp"%>

<div class="dropdown-container" data-wage-proposal-id="${applicationDto.currentWageProposal.id }">
	<c:choose>
		<c:when test="${applicationDto.currentWageProposal.status <= 0 }">
			
			<c:choose>
				<c:when test="${applicationDto.currentWageProposal.proposedToUserId == user.userId }">
					
				
					
					<div class="accent" data-toggle-id="response-container-${applicationDto.currentWageProposal.id }">
					<c:if test="${applicationDto.application.status == -1 }">
						<div>Employer initiated contact</div>
					</c:if>					
						
						Waiting for you
					</div>	
				
					<div id="response-container-${applicationDto.currentWageProposal.id }"
						 class="response-container dropdown-style">
						
						<table class="main-table-style">
							<thead>
								<tr>
									<th></th>
									<th></th>
									<th></th>
								</tr>								
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td>
										<p>Decline the application</p>
									</td>
								</tr>							
								<tr>
									<td>Wage Proposal</td>
									<td>$ ${applicationDto.currentWageProposal.amount }</td>
									<td>
										<p>Accept</p>
										<p>Counter</p>
									</td>
								</tr>
								<tr class="counter">
									<td colspan="3">
										
									</td>
								</tr>
								<tr>
									<td>Work Days Proposal</td>
									<td>
										<div class="calendar-container-proposed">
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
											<div class="calendar-container counter-calendar read-only ">
												<div class="calendar"
													data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
													data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
												</div>
											</div>
										</div>									
									</td>
									<td>
										<p>Accept</p>
										<p>Counter</p>
									</td>
								</tr>
							</tbody>
						</table>
					
						
						
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
							<div class="counter-actions proposal-actions">
								<span class="accent confirm-counter">Send</span>
								<span class="accent cancel">Cancel</span>
							</div>	
							
							<div class="counter-calendar-container">
								<p>Work Days</p>
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
								<div class="calendar-container counter-calendar read-only ">
									<div class="calendar"
										data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
										data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
									</div>
								</div>
							</div>				
						</div>
						
					</div>
					
				</c:when>
				<c:otherwise>
					<c:if test="${user.profileId == 2 && applicationDto.application.status == -1 }">
						<div>You initiated contact.</div>
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
		
		<c:when test="${applicationDto.currentWageProposal.status == 3 }">
			
				<div>
					<c:choose>
						<c:when test="${applicationDto.time_untilEmployerApprovalExpires == '-1' }">
							<div>${user.profileId == 1 ? "Your" : "The applicant's" } time has expired</div>
						</c:when>
						<c:otherwise>
							<div>
								<c:choose>
									<c:when test="${user.profileId == 1 }">
										<div data-toggle-id="accept-details-${applicationDto.currentWageProposal.id }"
											class="accent">
											<div>Employer accepted your offer.</div>
											<div>Waiting for your approval.</div>
										</div>
									</c:when>
									<c:otherwise>
										<div>Waiting for the applicant's approval</div>
									</c:otherwise>
								</c:choose>
									
								<div class="expiration-time">Expires in ${applicationDto.time_untilEmployerApprovalExpires }</div>	
							</div>
					
							<c:if test="${user.profileId == 1 }">
								<div id="accept-details-${applicationDto.currentWageProposal.id }"
									class="proposal-actions-container dropdown-style">						
									<%@ include file="./AcceptWageProposal.jsp" %>				
								</div>
							</c:if>					
						</c:otherwise>										
					</c:choose>
					
				</div>
							
		</c:when>
	
	</c:choose>
</div>