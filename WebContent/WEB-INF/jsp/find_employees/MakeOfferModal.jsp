<%@ include file="../includes/TagLibs.jsp" %>

<div id="makeOfferModal" class="mod">
	<div class="mod-content">
		<div class="mod-header">
			<span class="glyphicon glyphicon-remove"></span>
			<h3>Initiate Contact With <span class="make-offer-to-name"></span></h3>			
		</div>

		<div class="mod-body">
			<c:choose>
				<c:when test="${empty jobDtos_current }">
					You cannot make an offer. In order to make an offer, you must first post a job.
				</c:when>
				<c:otherwise>
					<div class="item button-group">
						<button id="inviteToApply">Invite To Apply</button>
						<button id="makeAnOffer">Make An Offer</button>
					</div>
					<p id="makeAnOffer_applicationStatus"></p>
					<div id="selectJob_initiateContact" class="item">
						<p>Select a job</p>
						<select>
							<option disabled selected></option>
							<c:forEach items="${jobDtos_current }" var="jobDto">
								<option data-job-id="${jobDto.job.id }">${jobDto.job.jobName }</option>
							</c:forEach>
						</select>	
					</div>
					<div id="detailsContainer_makeAnOffer">					
						<div class="item">
							<p>Offer Amount</p>
							<input id="amount" />
						</div>
						<div class="item">
							<p>Your offer expires in</p>
							<div class="time-container">
								<span class="label-horiz">Days</span><input class="days-pre-hire" />
							</div>
							<div class="time-container">
								<span class="label-horiz">Hours</span><input class="hours-pre-hire" />
							</div>
							<div class="time-container">
								<span class="label-horiz">Minutes</span><input class="minutes-pre-hire" />
							</div>
						</div>										
						<div class="item">														
							<div class="calendar-container">
								<p>Select work days you want <span class="make-offer-to-name"></span> to work</p>
								<div id="makerOffer_workDaysCalendar" class="calendar" data-number-of-months="${jobDto.months_workDaysSpan }"
										data-first-date="${jobDto.date_firstWorkDay }">										
								</div>							
								<button class="clear" data-clear-class="apply-selected-work-day">Clear</button>
							</div>
						</div>
					</div>	
					<div id="actionsContainer_initiateContact">
						<span id="sendInvite">Send Invite</span>
						<span id="sendOffer">Send Offer</span>
						<span id="cancelContact">Cancel</span>			
					</div>		
				</c:otherwise>
			</c:choose>	
		</div>
		
	</div>
</div>