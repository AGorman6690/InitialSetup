<%@ include file="../includes/TagLibs.jsp"%>



<div class="dropdown-container">



	<c:choose>
		<c:when test="${applicationDto.employmentProposalDto.status <= 0 ||
						applicationDto.employmentProposalDto.status == 3 }">
			
			<c:choose>
				<c:when test="${applicationDto.employmentProposalDto.proposedToUserId == user.userId }">

<%-- 					<div class="accent show-mod" data-toqqqqggle-id="response-container-${applicationDto.application.applicationId }"> --%>
<%-- 					<c:if test="${applicationDto.application.status == -1 }"> --%>
<!-- 						<div>Employer initiated contact</div> -->
<%-- 					</c:if>					 --%>
						
<!-- 						Waiting for you -->
<!-- 					</div>	 -->
				
					<div id="response-container-${applicationDto.application.applicationId }"
						 class="response-container mod"
						 data-application-id="${applicationDto.application.applicationId}"
						 data-session-user-is-employer=${user.profileId == 2 ? '1' : '0' }>
						 
						<div class="mod-content">
							<div class="mod-header">
								<span class="glyphicon glyphicon-remove"></span>
								<h2>Employment Proposal</h2>
							</div>
							
							<div class="mod-body">
								<div class="dates-container">
									<div class="job-work-days">
										<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay">
											<div data-date="${workDay.stringDate }"></div>
										</c:forEach>
									</div>
									<div class="proposal-work-days">
										<c:forEach items="${applicationDto.employmentProposalDto.dateStrings_proposedDates }" var="dateString">
											<div data-date="${dateString }"></div>
										</c:forEach>
									</div>
									<div class="days-unavailable">
										<c:forEach items="${applicationDto.dateStrings_unavailableWorkDays }" var="dateString">
											<div data-date="${dateString }"></div>
										</c:forEach>
									</div>
								</div>	
													
								<button class="withdraw-application sqr-btn">${user.profileId == 1 ? 'Withdraw your application' : 'Decline the application' }</button>							
								<div class="proposal wage-container">	
									<c:if test="${user.profileId == 1 }">
										<div class="proposal applicant-expiration-clock">
											<h1>This offer expires in</h1>
											<p>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
										</div>
									</c:if>							
									<h1>Wage</h1>
									<div class="button-group">
										<button class="sqr-btn gray-2 accept">Accept</button>
										<button class="sqr-btn gray-2 counter">Counter</button>
									</div>								
									<div class="proposal-container">
										<h2>${user.profileId == 1 ? "Employer's" : "Applicant's" } proposal</h2>
										<p class="current-wage-proposal">$ ${applicationDto.employmentProposalDto.amount }</p>											
									</div>
									<div class="counter-container">									
										<c:if test="${applicationDto.wageProposals.size() > 1 }">
											<div>
												<h2>Your last proposal</h2>
												<p>$ ${applicationDto.wageProposals[fn:length(applicationDto.wageProposals) - 2].amount }</p>
											</div>
										</c:if>													
										<div>
											<h2>Your counter amount</h2>
											<input class="counter-wage-amount" type="text" />
										</div>							
									</div>
									<div class="confirmation-container">
										<p class="accept">You are <span class="bold">accepting</span> $ ${applicationDto.employmentProposalDto.amount }</p>
										<p class="counter">You are <span class="bold">proposing</span> <span class="new-proposed-amount"></span></p>
									</div>										
								</div>
	
								<div class="proposal work-day-container pad-top">						
									<h1>Work Days</h1>
									<div class="button-group">
										<button class="sqr-btn gray-2 accept">Accept</button>
										<button class="sqr-btn gray-2 counter">Counter</button>
									</div>	
									<div class="confirmation-container">
										<p class="accept">You are <span class="bold">accepting</span> the following work days</p>
										<p class="counter">You are <span class="bold">proposing</span> the following work days</p>
									</div>																	
<%-- 									<p class="number-of-work-days">${applicationDto.dateStrings_availableWorkDays.size() } of ${applicationDto.jobDto.workDays.size() } days</p>	 --%>
									<div class="proposal-container">						
										<div class="calendar-container hide-prev-next read-only">
											<div class="calendar"
												data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
												data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
											</div>										
										</div>
									</div>
									<div class="counter-container">
										<div class="job-info-calendar calendar-container hide-prev-next">									
											<div class="calendar"
												data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
												data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
											</div>
										</div>		
									</div>	
								</div>
								<c:if test="${user.profileId == 2 }">
									<div class="expiration-container">
										<h1>This Proposal Expires In</h1>
										<div class="proposal set-expiration">
											<div class="time-container">
												<p>Days</p>
												<input class="days" type="text" value="0"/>
											</div>
											<div class="time-container">
												<p>Hours</p>
												<input class="hours" type="text" value="0"/>
											</div>
											<div class="time-container">
												<p>Minutes</p>
												<input class="minutes" type="text" value="0"/>
											</div>									
										</div>
										<div class="proposal confirmation-container">
											<p>Your proposal will expire in <span class="bold confirm-expiration"></span></p>						
										</div>		
									</div>
								</c:if>	
								<div class="action-button-container">
									<div class="proceed-to-confirmation-container">
										<span class="confirm sqr-btn teal">Review</span>
										<span class="cancel">Cancel</span>
									</div>
									<div class="send-proposal-container">
										<span class="send sqr-btn teal">Send</span>
										<span class="edit">Edit</span>
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
<%-- 					Waiting for ${user.profileId == 1 ? 'employer.' : 'applicant.' } --%>
				</c:otherwise>
			</c:choose>												
		</c:when>
	
		<c:when test="${applicationDto.employmentProposalDto.status == 4 }">
			Accepted
		</c:when>
		
		<c:when test="${applicationDto.employmentProposalDto.status == 2 }">
			Declined
		</c:when>
		
		<c:when test="${applicationDto.employmentProposalDto.status == 3 }">
		
		
			<div id="response-container-${applicationDto.application.applicationId }"
				 class="response-container dropdown-style"
				 data-application-id="${applicationDto.application.applicationId}">
				
				<div class="proposal-container">
					<div class="header">
						<button class="sqr-btn">${user.profileId == 1 ? 'Withdraw your application' : 'Decline the application' }</button>
					</div>					
					<div class="proposal wage-proposal-container">						
						<p>Wage</p>
						<p >$ ${applicationDto.employmentProposalDto.amount }</p>
					</div>						
				</div>
				
				<div class="proposal work-day-proposal-container">
					<p>Work Days</p>
					<div class="calendar-container">
						<p>${applicationDto.dateStrings_availableWorkDays.size() } of ${applicationDto.jobDto.workDays.size() } days
							<span class="glyphicon glyphicon-menu-down"></span>
						</p>
						<div class="job-work-days">
							<c:forEach items="${applicationDto.employmentProposalDto.dateStrings_proposedDates }" var="dateString">
								<div data-date="${dateString }"></div>
							</c:forEach>
						</div>
						<div class="calendar"></div>
					</div>

				</div>				
			</div>	
			
				<div>
					<c:choose>
						<c:when test="${applicationDto.time_untilEmployerApprovalExpires == '-1' }">
							<div>${user.profileId == 1 ? "Your" : "The applicant's" } time has expired</div>
						</c:when>
						<c:otherwise>
							<div>
								<c:choose>
									<c:when test="${user.profileId == 1 }">
										<div class="accent">
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
								<div 
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