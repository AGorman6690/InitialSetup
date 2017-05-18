<%@ include file="../includes/TagLibs.jsp" %>

<div id="makeOfferModal" class="mod">
	<div class="mod-content">
		<div class="mod-header">
			<span class="glyphicon glyphicon-remove"></span>
			<h3>Initiate Contact With <span class="make-offer-to-name"></span></h3>			
		</div>

		<div class="mod-body">
			<div class="apply-action">
					<p>Propose a  wage</p>
					<input id="amount" type="text">		
			</div>
			<c:if test="${applicationDto.jobDto.job.isPartialAvailabilityAllowed == true }">	
				<div class="apply-action">
						<p>Propose the days you can work</p>
						<button id="select-all-work-days" class="sqr-btn gray-2">Select All</button>		
						<div id="apply-work-days-calendar-container" class="v2 proposal-calendar pad-top calendar-container hide-prev-next">
							<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
						</div>					
				</div>	
			</c:if>	
		</div>		
	</div>
</div>
<div id="json_workDayDtos" class="hide">${json_workDayDtos }</div>