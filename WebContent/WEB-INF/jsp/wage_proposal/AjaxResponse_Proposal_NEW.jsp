<%@ include file="../includes/TagLibs.jsp"%>

<div class="mod simple-header proposal-container" data-session-user-is-employer=${user.profileId == 2 ? '1' : '0' }
	data-application-id="${applicationDto.application.applicationId }">
	<div class="center mod-content" >
		<div class="mod-header">
			<span class="glyphicon glyphicon-remove"></span>
			<h2></h2>
		</div>	
		<div class="mod-body">		
			<c:if test="${!isEmployerMakingFirstOffer }">
				<button class="withdraw-application sqr-btn">${user.profileId == 1 ? 'Withdraw your application' : 'Decline the application' }</button>
			</c:if>				
			<div class="respond-to-proposal">
				<c:if test="${user.profileId == 1 }">
					<div class="context-employee">
						<h3 class="blue">This Proposal Expires In</h3>
						<h2>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</h2>
					</div>
				</c:if>
				<div class="proposal wage-proposal" data-proposed-amount="${applicationDto.employmentProposalDto.amount }">
					<h3 class="blue">Wage Proposal</h3>
					<div class="proposed-offer">
						<c:choose>
							<c:when test="${isEmployerMakingFirstOffer }">
								<h2>Propose an hourly wage</h2>
								<input class="" type="text" />
							</c:when>
							<c:otherwise>						
								<h2>${user.profileId == 1 ? "Employer" : "Applicant" } proposed</h2>
								<p class="">$ ${applicationDto.employmentProposalDto.amount }</p>
							</c:otherwise>		
						</c:choose>	
					</div>			
					<c:if test="${!isEmployerMakingFirstOffer }">	
						<div class="button-group no-toggle">
							<button class="sqr-btn gray-2 accept-proposal">Accept</button>
							<button class="sqr-btn gray-2 counter-proposal">Counter</button>
						</div>				
						<div class="counter-offer">									
							<c:if test="${applicationDto.wageProposals.size() > 1 }">
								<h2>Your last proposal</h2>
								<p>$ ${applicationDto.wageProposals[fn:length(applicationDto.wageProposals) - 2].amount }</p>
							</c:if>													
							<h2>Your wage counter</h2>
							<input class="counter-wage-amount" type="text" />						
						</div>						
					</c:if>				
				</div>
				<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed ||
								isEmployerMakingFirstOffer}"> 		
					<div class="proposal work-day-proposal">
						<h3 class="blue">Work Day Proposal</h3>
						<div class="other-application-conflicts"></div>
						<div class="proposed-offer">
							<h2>${user.profileId == 1 ? 'Employer' : 'Applicant' } proposed ${applicationDto.employmentProposalDto.dateStrings_proposedDates.size() } work days</h2>															
							<div class="v2 teal-title proposal-calendar hide-unused-rows calendar-container
								 wage-proposal-calendar hide-prev-next read-only">
								<div class="calendar proposed-calendar"
									data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
									data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
								</div>										
							</div>			
						</div>
						<div class="button-group pad-top no-toggle">
							<button class="sqr-btn gray-2 accept-proposal">Accept</button>
							<button class="sqr-btn gray-2 counter-proposal">Counter</button>
						</div>	
						<div class="counter-offer">		
							<h2>Your work day counter</h2>
							<div class="v2 teal-title proposal-calendar calendar-container wage-proposal-calendar
								 hide-prev-next hide-unused-rows">	
								<button class="sqr-btn gray-2 select-all-work-days-override">
									Select All Work Days</button>								
								<div class="calendar counter-calendar"
									data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
									data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
								</div>
							</div>			
						</div>		
					</div>
				</c:if>
				<c:if test="${user.profileId == 2 }">
					<div class="expiration-container">
						<h3 class="blue">The Applicant Must Answer Within</h3>
						<div class="set-expiration">
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
					</div>
				</c:if>	
				<div class="pad-top-2">
					<span class="review-proposal sqr-btn green">Review</span>
					<span class="cancel linky-hover">Cancel</span>
				</div>		
			</div>		
			<div class="confirm-response-to-proposal hide-on-load pad-top-2">
				<c:choose>
					<c:when test="${user.profileId == 1 }">
						<h2 class="confirm-proposal-accept">You are about to accept employment</h2>
						<h2 class="confirm-proposal-counter">You are about to send the employer a new proposal</h2>
					</c:when>
					<c:otherwise>
						<h2 class="confirm-proposal-accept">You are about to accept the applicant's proposal.</h2>
						<h2 class="confirm-proposal-accept">The applicant is required to confirm your acceptance.</h2>
						<h2 class="confirm-proposal-counter">You are about to send the applicant a new proposal.</h2>
						<h2 class="confirm-proposal-counter">If this proposal is accepted, the applicant will be employed.</h2>						
					</c:otherwise>
				</c:choose>
					<div class="proposed-values pad-top">
						<p><label>Wage</label>$ <span class="wage-amount"></span> / hr </p>
						<p>
							<span data-toggle-id="confirm-work-days-calendar-${applicationDto.application.applicationId }">
								<label>Work Days </label><span class="work-day-count"></span><span class="glyphicon glyphicon-menu-down"></span>
							</span>
						</p>
						<c:if test="${user.profileId == 2 }">
							<p><label>Expires In</label><span class="expires-in"></span></p>
						</c:if>
					</div>
					<div id="confirm-work-days-calendar-${applicationDto.application.applicationId }" 
					    class="v2 teal-title proposal-calendar hide-unused-rows calendar-container
						 wage-proposal-calendar hide-prev-next read-only hide-on-load">
						<div class="calendar confirm-calendar"
							data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
							data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
						</div>										
					</div>	
					<div class="pad-top-2">
						<span class="send-proposal sqr-btn green">OK</span>
						<span class="edit-response-to-proposal linky-hover">Edit</span>
					</div>						
				
			</div>		
		</div> <!-- close mod body -->
	</div>
</div>
<div id="json_workDayDtos" class="hide">${json_workDayDtos }</div>