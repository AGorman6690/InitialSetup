<%@ include file="../includes/TagLibs.jsp"%>
	

		
			<thead>
				<tr class="header-1">		
					<th id="jobName" class="left-edge" colspan="1"></th>
					<th id="wageProposal" class="span left-edge right-edge" colspan="2">Wage Proposal</th>
					<th id="startDate" class="left-edge hide-with-calendar" colspan="1"></th>
					<th id="endDate" class="left-edge hide-with-calendar" colspan="1"></th>	
					<th id="location" class="left-edge hide-with-calendar" colspan="1"></th>	
					<th id="location" class="left-edge hide-with-calendar" colspan="1"></th>					
				</tr>
				
				<tr class="header-2">
					<th id="" class="left-edge">Job Name</th>
					<th id="action-th" class="left-edge">Status</th>
					<th id="amount-th" class="right-edge">Offer Amount</th>
					<th id="" class="left-edge hide-with-calendar">Start Date</th>
					<th id="" class="left-edge hide-with-calendar">End Date</th>
					<th id="" class="left-edge hide-with-calendar">Duration</th>	
					<th id="" class="left-edge hide-with-calendar">Location</th>		
																	 
				</tr>
			</thead>					

			<tbody>
			
				<c:forEach items="${applicationDtos }" var="applicationDto">
<%-- 					<c:if test="${applicationDto.application.status <= 3 }"> --%>
						<tr class="static-row application" data-application-status="${applicationDto.application.status }"
							data-application-id="${applicationDto.application.applicationId }">
							<td>
								<a class="accent ${applicationDto.application.status == 3 ? 'accepted' : ''}"
								   href="/JobSearch/job/${applicationDto.jobDto.job.id }?c=profile-incomplete&p=1">
									${applicationDto.jobDto.job.jobName }
								</a>
							</td>

							<td>
								<c:choose>
<%-- 									<c:when test="${applicationDto.application.status == 4 }"> --%>
<%-- 										<c:choose> --%>
<%-- 										 	<c:when test="${applicationDto.time_untilEmployerApprovalExpires == '-1'}"> --%>
<!-- 										 		<div>Your time has expired</div> -->
<%-- 										 	</c:when> --%>
<%-- 										 	<c:otherwise> --%>
<!-- 												<div class="accepted">Employer accepted</div> -->
<!-- 												<div>Waiting for your approval</div> -->
<%-- 												<div>You have ${applicationDto.time_untilEmployerApprovalExpires } to respond</div> --%>
<%-- 												<div id="${applicationDto.currentWageProposal.id}" class="dropdown-container counter-offer-container"> --%>
<%-- 													<span data-toggle-id="approval-${applicationDto.application.applicationId }" --%>
<!-- 														 class="glyphicon glyphicon-menu-down"></span> -->
<%-- 													<div id="approval-${applicationDto.application.applicationId }" --%>
<!-- 														 class=" dropdown-style response-for-approval-container"> -->
<%-- 														<c:if test="${!empty applicationDto.applicationDtos_conflicting }"> --%>
<!-- 															<div class="conflicting-apps-container sub-section"> -->
<!-- 																Conflicting Applications: -->
<!-- 																By accepting this proposal, your following applications will be removed. -->
<%-- 																<c:forEach items="${applicationDto.applicationDtos_conflicting }" --%>
<%-- 																			 var="applicationDto_conflicting"> --%>
<%-- 																	<div><a class="accent" href="/JobSearch/job/${applicationDto_conflicting.jobDto.job.id } --%>
<%-- 																			?c=profile-incomplete&p=1">${applicationDto_conflicting.jobDto.job.jobName }</a></div> --%>
<%-- 																</c:forEach> --%>
<!-- 															</div> -->
<%-- 														</c:if>														  --%>
<!-- 														<span class="accent accept-counter">Accept</span> -->
<!-- 														<span class="accent decline-counter">Decline</span> -->
<!-- 													</div> -->
<!-- 												</div>										 	 -->
<%-- 										 	</c:otherwise> --%>
<%-- 										</c:choose> --%>
<%-- 									</c:when>								 --%>
									<c:when test="${applicationDto.application.status == 3 }">
										<span class="accepted">Accepted</span>
									</c:when>
									<c:when test="${applicationDto.currentWageProposal.status == 2 }">
										<c:choose>
											<c:when test="${applicationDto.currentWageProposal.proposedToUserId == user.userId }">
												You rejected
											</c:when>
											<c:otherwise>
												Employer rejected
											</c:otherwise>
										</c:choose>												
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${applicationDto.currentWageProposal.proposedToUserId == user.userId }">
												<c:set var="param_is_employer" value="0" />
												<c:set var="param_wage_proposal" value="${applicationDto.currentWageProposal }" />
												<%@ include file="../templates/WageNegotiation.jsp" %>

											</c:when>
											<c:otherwise>
												Waiting for employer
											</c:otherwise>
										</c:choose>												
									
									</c:otherwise>
								</c:choose>
							</td>	
							<td>
								<span class="dollar-sign">$</span>
								<fmt:formatNumber type="number" minFractionDigits="2" 
									maxFractionDigits="2" value="${applicationDto.currentWageProposal.amount}"/>
								
								<c:if test="${applicationDto.wageProposals.size() > 1 }">
									<span data-toggle-id="wp-history-${applicationDto.application.applicationId }"
										data-toggle-speed="-2" 
										class="show-wage-proposal-history glyphicon glyphicon-menu-down"></span>
									<div id="wp-history-${applicationDto.application.applicationId }" class="dropdown-style">
										<table class="wage-proposal-history-table">
											<thead>
												<tr>
													<th>Proposed By</th>
													<th>Amount</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${applicationDto.wageProposals }" var="wageProposal">
													<tr class="${wageProposal.proposedByUserId == user.userId ? 'you' : 'not-you' }">
														<td>${wageProposal.proposedByUserId == user.userId ? 'You' : 'Employer' }</td>
														<td>
															<span class="dollar-sign">$</span>
															<fmt:formatNumber type="number" minFractionDigits="2" 
																maxFractionDigits="2" value="${wageProposal.amount}"/>
														</td>
													</tr>
												</c:forEach>	
											</tbody>
										</table>
									</div>
								</c:if>
							</td>										
						
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringStartDate }</td>
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringEndDate }</td>	
							<td class="hide-with-calendar">${applicationDto.jobDto.workDays.size() } ${applicationDto.jobDto.workDays.size() <= 1 ? 'day' : 'days' }</td>	
							<td class="hide-with-calendar">${applicationDto.jobDto.job.city }, ${applicationDto.jobDto.job.state }</td>
												
						</tr>
<%-- 					</c:if> --%>
				</c:forEach>
										
			</tbody>
	

