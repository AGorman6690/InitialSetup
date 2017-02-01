<%@ include file="../includes/TagLibs.jsp"%>

	<h4>Applications</h4>
	
	<div id="" class="section">	
	<c:choose>
		<c:when test="${applicationDtos.size() > 0 }">		
		<div id="calendarContainer_applications">
			<div id="calendar_applications" class="calendar" data-min-date="10/01/2016"></div>
			<div id="jobDetails" >
				<p class="">Job Name: <span class="job-name"></span>
				</p>
			</div>
		</div>	
					
		<table id="openApplications" class="main-table-style">
		
			<thead>
				<tr class="header-1">		
					<th id="jobName" class="left-edge" colspan="1"></th>
					<th id="wageProposal" class="span left-edge right-edge" colspan="2">Wage Proposal</th>
				</tr>
				
				<tr class="header-2">
					<th id="" class="left-edge">Job Name</th>
					<th id="action-th" class="left-edge">Status</th>
					<th id="amount-th" class="right-edge">Current Offer</th>							 
				</tr>
			</thead>					

			<tbody>
			
				<c:forEach items="${applicationDtos }" var="dto">
					<c:if test="${dto.application.status < 3 }">
						<tr class="static-row application" data-application-status="${dto.application.status }">
							<td><a class="accent" href="/JobSearch/job/${dto.job.id }?c=profile-incomplete&p=1">${dto.job.jobName }</a></td>
							<td>
								<c:choose>
									<c:when test="${dto.currentWageProposal.status == 2 }">
										<c:choose>
											<c:when test="${dto.currentWageProposal.proposedToUserId == user.userId }">
												You rejected
											</c:when>
											<c:otherwise>
												Employer rejected
											</c:otherwise>
										</c:choose>												
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${dto.currentWageProposal.proposedToUserId == user.userId }">
												<c:set var="param_is_employer" value="0" />
												<c:set var="param_wage_proposal" value="${dto.currentWageProposal }" />
												<%@ include file="../templates/WageNegotiation.jsp" %>

											</c:when>
											<c:otherwise>
												Waiting for employer
											</c:otherwise>
										</c:choose>												
									
									</c:otherwise>
								</c:choose>
							</td>	
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/></td>										
						</tr>
					</c:if>
				</c:forEach>
										
			</tbody>
		</table>
							
		</c:when>
		<c:otherwise>
			<div id="noApplications">You have no open applications at this time.</div>
		</c:otherwise>
		
	</c:choose>
</div>		

