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
						 class="response-container dropdown-style"
						 data-application-id="${applicationDto.application.applicationId}">
						
						<div class="proposal-container">
							<div class="header">
								<button class="sqr-btn">${user.profileId == 1 ? 'Withdraw your application' : 'Decline the application' }</button>
							</div>

							
									<div class="proposal wage-proposal-container">
										
										<p>Wage Proposal</p>
										<p >$ ${applicationDto.currentWageProposal.amount }</p>
										<div class="button-group">
											<button class="sqr-btn accept">Accept</button>
											<button class="sqr-btn counter">Counter</button>
										</div>
										
									</div>
									<div class="counter-container counter-wage">
										<div>
											<p>Current proposal<span>$ ${applicationDto.currentWageProposal.amount }</span>
											
											</p>
											<c:if test="${applicationDto.wageProposals.size() > 1 }">
												<p>Your last proposal<span>$ ${applicationDto.wageProposals[fn:length(applicationDto.wageProposals) - 2].amount }</span></p>
											</c:if>				
											<p>Counter proposal<input type="text" /></p>
											</div>								
										</div>
									<div class="proposal work-day-proposal-container">
										<p>Work Days Proposal</p>
										<p>${applicationDto.dateStrings_availableWorkDays.size() } of ${applicationDto.jobDto.workDays.size() } days
											<span class="glyphicon glyphicon-menu-down"></span>
										</p>
										<div class="button-group">
											<button class="sqr-btn accept">Accept</button>
											<button class="sqr-btn counter">Counter</button>
										</div>
									</div>
									<div class="counter-container counter-work-days">
										<div class="calendar-container-proposed-work-days">
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
											<div class="days-unavailable">
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
									</div>
									<c:if test="${user.profileId == 2 }">
										<div class="proposal set-expiration">
											<p>This Proposal Expires In</p>
											<div>
												<div>
													<p>Days</p>
													<input class="days" type="text" value="0"/>
												</div>
												<div>
													<p>Hours</p>
													<input class="hours" type="text" value="0"/>
												</div>
												<div>
													<p>Minutes</p>
													<input class="minutes" type="text" value="0"/>
												</div>		
											</div>								
										</div>
									</c:if>
							<div class="proceed-to-confirmation-container">
								<span class="confirm sqr-btn">Confirm</span>
								<span class="cancel">Cancel</span>
							</div>										
						</div>
						
						
						<div class="confirmation-container">
							<div class="proposal confirm confirm-wage-container">
								<p>Wage Proposal</p>
								<div class="cell accept-proposal">
									<p>You are <span class="bold">accepting</span> $ ${applicationDto.currentWageProposal.amount }</p>
								</div>
								<div class="cell counter-proposal">
<%-- 									<p>You are countering $ ${applicationDto.currentWageProposal.amount }</p> --%>
									<p>You are <span class="bold">proposing</span> <span class="new-proposed-amount"></span></p>
								</div>							
							</div>
							<div class="proposal confirm confirm-work-days-container">
								<p>Work Days</p>
								<div class="cell accept-proposal">
									<p>You are <span class="bold">accepting</span> the following work days</p>
								</div>
								<div class="cell counter-proposal">
									<p>You are <span class="bold">proposing</span> the following work days</p>
										
									<div class="calendar-container-confirm-proposed-work-days">
	
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
										<div class="days-unavailable">
											<c:forEach items="${applicationDto.dateStrings_unavailableWorkDays }" var="dateString">
												<div data-date="${dateString }"></div>
											</c:forEach>
										</div>
									</div>	
								</div>	
								<div class="calendar-container read-only">
									<div class="calendar"
										data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
										data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
									</div>
								</div>
																					
							</div>
							<c:if test="${user.profileId == 2 }">
								<div class="proposal confirm confirm-expiration-container">
									<p>Expiration</p>
									<div class="cell accept-proposal">
										<p>Your proposal will expire in <span class="bold confirm-expiration"></span></p>
									</div>							
								</div>		
							</c:if>					
							
							<div class="send-container">
								<span class="send sqr-btn">Send</span>
								<span class="edit">Edit</span>
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
	
		<c:when test="${applicationDto.currentWageProposal.status == 4 }">
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