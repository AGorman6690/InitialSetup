<%@ include file="../includes/TagLibs.jsp"%>

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
			<button class="withdraw-application sqr-btn">${user.profileId == 1 ? 'Withdraw your application' : 'Decline the application' }</button>							
			<div class="proposal wage-container">	
				<c:if test="${user.profileId == 1 }">
					<div class="proposal applicant-expiration-clock">
						<h1>This offer expires in</h1>
						<p>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
					</div>
				</c:if>							
				<h1>Wage</h1>								
				<div class="proposal-container">
					<h2>${user.profileId == 1 ? "Employer" : "Applicant" } proposed</h2>
					<p class="current-wage-proposal">$ ${applicationDto.employmentProposalDto.amount }</p>											
				</div>
				<div class="button-group pad-top">
					<button class="sqr-btn gray-2 accept">Accept</button>
					<button class="sqr-btn gray-2 counter">Counter</button>
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
				<div class="confirmation-container pad-top">
					<p class="accept">You are <span class="bold">accepting</span> $ ${applicationDto.employmentProposalDto.amount }</p>
					<p class="counter">You are <span class="bold">proposing</span> <span class="new-proposed-amount"></span></p>
				</div>										
			</div>
			<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed }"> 	
				<div class="proposal work-day-container">		
					<h1 data-toggle-id="work-day-proposal-input">Work Days<span class="glyphicon glyphicon-menu-down"></span></h1>
						
					<div id="work-day-proposal-input" class="proposal-input">
						<c:if test="${user.profileId == 1 }">
							<c:if test="${applicationDto.applicationDtos_conflicting_willBeRemoved.size() > 0  ||
											applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer.size() > 0  ||
											applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer.size() > 0 }">
									
								<div class="conflicting-applications">
									<p class="if-you-accept">If you <span class="bold">accept</span> this proposal, your following applications:</p>
									<c:if test="${applicationDto.applicationDtos_conflicting_willBeRemoved.size() > 0 }">
										<div class="disposition">
											<h6>will be <span class="bold">removed</span><span class="why">why?</span></h6>
											<div class="applications">
												<ul>
													<c:forEach items="${applicationDto.applicationDtos_conflicting_willBeRemoved }"
														var="applicationDto">
														<li>${applicationDto.jobDto.job.jobName }</li>
													</c:forEach>
												</ul>
											</div>
										</div>
									</c:if>
									<c:if test="${applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer.size() > 0 }">
										<div class="disposition">
											<h6>will be <span class="bold">modified</span><span class="why">why?</span></h6>
											<div class="applications">
												<ul>
													<c:forEach items="${applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer }"
				 										var="applicationDto">
				 										<li>${applicationDto.jobDto.job.jobName }</li>
													</c:forEach>
												</ul>
											</div>
										</div>
									</c:if>
									<c:if test="${applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer.size() > 0 }">
										<div class="disposition">
											<h6>will be <span class="bold">modified</span> to resolve the time conflicts and <span class="bold">sent back</span> to the employer<span class="why">why?</span></h6>
											<div class="applications">
												<ul>
													<c:forEach items="${applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer }"
				 										var="applicationDto">
														<li>${applicationDto.jobDto.job.jobName }</li>
				 									</c:forEach>
			 									</ul>
											</div>
										</div>
									</c:if>												
								</div>
							</c:if>
						</c:if>
						<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed }">
							<div class="confirmation-container">
								<p class="accept">You are <span class="bold">accepting</span> the following work days</p>
								<p class="counter">You are <span class="bold">proposing</span> the following work days</p>
							</div>																										
							
							<div class="proposal-container">	
								<h2 class="proposed-work-day-count">${user.profileId == 1 ? 'Employer' : 'Applicant' } proposed ${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } work days</h2>												
								<div class="v2 proposal-calendar hide-unused-rows calendar-container wage-proposal-calendar hide-prev-next read-only">
									<div class="calendar proposed-calendar"
										data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
										data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
									</div>										
								</div>
							</div>
							<div class="counter-container">									
								<div class="v2 proposal-calendar hide-unused-rows calendar-container wage-proposal-calendar hide-prev-next">									
									<div class="calendar counter-calendar"
										data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
										data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
									</div>
								</div>		
							</div>	
							<div class="button-group pad-top">
								<button class="sqr-btn gray-2 accept">Accept</button>
								<button class="sqr-btn gray-2 counter">Counter</button>
							</div>								
						</c:if>
					</div>
				</div>
			</c:if>
			<c:if test="${user.profileId == 2 }">
				<div class="proposal expiration-container">
					<h1 data-toggle-id="expiration-input-cont">This Proposal Expires In<span class="glyphicon glyphicon-menu-down"></span></h1>
					<div id="expiration-input-cont" class="proposal-input">
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

<div id="json_workDayDtos" class="hide">
	${json_workDayDtos }
</div>