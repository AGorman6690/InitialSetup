<%@ include file="../../includes/TagLibs.jsp"%>
			
<h1 class="counter-context">Propose A Counter Offer</h1>
<h1 class="review-context">Review</h1>		
<c:if test="${user.profileId == 1 }">
	<div class="context-employee counter-context">
		<h3 class="blue">This Proposal Expires In</h3>
		<p>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</p>
	</div>
</c:if>
<div class="button-wrapper left">
	<p class="counter-context cancel-proposal linky-hover">
		<span class="glyphicon glyphicon-menu-left"></span><span class="text">Cancel</span></p>
	<p class="review-context edit-proposal linky-hover">
		<span class="glyphicon glyphicon-menu-left"></span><span class="text">Edit</span></p>		
<!-- 	<button class="red-fill counter-context cancel-proposal sqr-btn"><span class="glyphicon glyphicon-menu-left"></span>Cancel</button> -->
<!-- 	<button class="red-fill review-context edit-proposal sqr-btn"><span class="glyphicon glyphicon-menu-left"></span>Edit</button> -->
</div>
<div class="proposal-content-wrapper">
	<div class="proposal wage-proposal" data-proposed-amount="${applicationDto.employmentProposalDto.amount }">
		<h3 class="blue">Wage Proposal</h3>		
		<div class="counter-context">
			
			<c:if test="${!isEmployerMakingFirstOffer }">	
				<div class="proposed-offer">
					<p class="proposed-amount">${user.profileId == 1 ? "Employer" : "Applicant" }
						proposed $ ${applicationDto.employmentProposalDto.amount } / hr</p>
				</div>				
			</c:if>		
			<div class="${isEmployerMakingFirstOffer ? 'initial-offer' : 'counter-offer' }">									
	<%-- 			<c:if test="${applicationDto.wageProposals.size() > 1 }"> --%>
	<!-- 				<h2>Your last proposal</h2> -->
	<%-- 				<p>$ ${applicationDto.wageProposals[fn:length(applicationDto.wageProposals) - 2].amount }</p> --%>
	<%-- 			</c:if>													 --%>
				<p class="red-bold">${isEmployerMakingFirstOffer ? 'Propose a wage' : 'Enter a counter offer' } ($ / hr)</p>
				<input class="counter-wage-amount" type="text" placeholder="($ / hr)"
					value="${applicationDto.employmentProposalDto.amount }"/>						
			</div>	
		</div>
		<div class="review-context">
			<p>You are <span class="accepting-or-proposing"></span> a $<span class="wage-amount"></span> / hr wage 
		</div>							
	</div>
<%-- 	<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed || --%>
<%-- 					isEmployerMakingFirstOffer}"> 		 --%>
	<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed}">
		<div class="proposal work-day-proposal" data-proposed-work-days="${datestrings_workDays }">
			<h3 class="blue">Work Day Proposal</h3>								
			<p class="red-bold counter-context">Select or deselect your proposed work days</p>
			<p class="review-context">You are <span class="accepting-or-proposing"></span> the following work days</span>							
			<div class="conflicting-applications-countering"></div>
			<div class="counter-context review-context v2 teal-title proposal-calendar calendar-container wage-proposal-calendar
				 hide-prev-next hide-unused-rows">	
				<button class="counter-context sqr-btn gray-3 select-all-work-days-override">
					Select All Work Days</button>								
				<div class="calendar counter-calendar  ${user.profileId == 1 ? 'find-conflicting-applications-on-select' : ''}"
					data-min-date="${applicationDto.jobDto.date_firstWorkDay }"
					data-number-of-months=${applicationDto.jobDto.months_workDaysSpan }>
				</div>
			</div>									
		</div>				
	</c:if>
</div>
<div class="button-wrapper right">
<!-- 	<button class="green-fill counter-context review-proposal sqr-btn">Review<span class="glyphicon glyphicon-menu-right"></span></button> -->
	<p class="counter-context review-proposal linky-hover">
		<span class="glyphicon glyphicon-menu-right"></span><span class="text">Review</span></p>
	<p class="review-context send-proposal linky-hover">
		<span class="glyphicon glyphicon-menu-right"></span><span class="text">Send</span></p>		
<!-- 	<button class="green-fill review-context send-proposal sqr-btn">Send<span class="glyphicon glyphicon-menu-right"></span></button> -->
</div>