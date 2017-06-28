<%@ include file="../../includes/TagLibs.jsp"%>

<h3 class="blue">Review Your Actions</h3>
<div class="proposed-values">
	<c:if test="${user.profileId == 2 }">
		<p><label>Expires In</label><span class="expires-in"></span></p>
	</c:if>					
	<p><label>Wage</label>$ <span class="wage-amount"></span> / hr </p>
	<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed }">
		<p>			
			<label>Work Days </label><span class="work-day-count"></span>							
		</p>
	</c:if>

</div>
<div id="confirm-work-days-calendar-${applicationDto.application.applicationId }" 
    class="v2 teal-title proposal-calendar hide-unused-rows calendar-container
	 wage-proposal-calendar hide-prev-next read-only">
	<div class="calendar confirm-calendar"
		data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
		data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
	</div>										
</div>	
<div class="confirm-message pad-top">
	<c:choose>
		<c:when test="${user.profileId == 1 }">
			<h2 class="confirm-proposal-accept">You are about to accept employment</h2>
			<h2 class="confirm-proposal-counter">You are about to send the employer a new proposal</h2>
		</c:when>
		<c:otherwise>
			<h2 class="confirm-send-initial-offer">You are about to send the initial offer.</h2>
			<h2 class="confirm-proposal-accept">You are about to accept the applicant's proposal.</h2>
			<h2 class="confirm-proposal-accept">The applicant is required to confirm your acceptance.</h2>
			<h2 class="confirm-proposal-counter">You are about to send the applicant a new proposal.</h2>
			<h2 class="confirm-proposal-counter">If this proposal is accepted, the applicant will be employed.</h2>						
		</c:otherwise>
	</c:choose>
</div>				
<div class="">
	<span class="${isEmployerMakingFirstOffer ? 'send-initial-offer' : 'send-proposal' } sqr-btn green">Send</span>
	<span class="edit-response-to-proposal linky-hover">Edit</span>
</div>	


<c:if test="${user.profileId == 2 }">
	<div class="expiration-container">						
		<h3 class="blue">The Applicant Must Reply Within</h3>
		<h2>(Job starts in ${applicationDto.jobDto.timeUntilStart })</h2>						
		<div class="expiration-input-container">
			<div class="radio-container">
				<label><input class="one-day-from-now" type="radio" name="set-exp-time">
					1 day from now</label>
				<label><input class="one-day-before-first-proposed-work-day" type="radio" name="set-exp-time">
					1 day before the first proposed work day starts</label>
			</div>	
			<div class="set-expiration">	
				<div class="custom-times-inputs">						
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
<!-- 									<div class="time-container job-starts-in-message"> -->
<!-- 										<p>Job starts in</p> -->
<%-- 										<p>${applicationDto.jobDto.timeUntilStart }</p> --%>
<!-- 									</div> -->
				</div>		
				<div class="custom-time-radios">
					<label><input class="custom-time-from-now" type="radio" name="set-exp-time">
						from now</label>
					<label><input class="custom-time-before-first-proposed-work-day" type="radio" name="set-exp-time">
						before the first proposed work day starts</label>
				</div>						
			</div>	
		</div>
	</div>
</c:if>	