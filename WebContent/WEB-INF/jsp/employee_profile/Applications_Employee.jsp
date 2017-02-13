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
			
				<c:forEach items="${applicationDtos }" var="dto">
					<c:if test="${dto.application.status <= 3 }">
						<tr class="static-row application" data-application-status="${dto.application.status }"
							data-application-id="${dto.application.applicationId }">
							<td>
								<a class="accent ${dto.application.status == 3 ? 'accepted' : ''}"
								   href="/JobSearch/job/${dto.job.id }?c=profile-incomplete&p=1">
									${dto.job.jobName }
								</a>
							</td>

							<td>
								<c:choose>
									<c:when test="${dto.application.status == 3 }">
										<span class="accepted">Accepted</span>
									</c:when>
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
							<td>
								<span class="dollar-sign">$</span>
								<fmt:formatNumber type="number" minFractionDigits="2" 
									maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/>
								
								<c:if test="${dto.wageProposals.size() > 1 }">
									<span data-toggle-id="wp-history-${dto.application.applicationId }"
										data-toggle-speed="-2" 
										class="show-wage-proposal-history glyphicon glyphicon-menu-down"></span>
									<div id="wp-history-${dto.application.applicationId }" class="dropdown-style">
										<table class="wage-proposal-history-table">
											<thead>
												<tr>
													<th>Proposed By</th>
													<th>Amount</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${dto.wageProposals }" var="wageProposal">
													<tr class="${wageProposal.proposedByUserId == userId ? 'you' : 'not-you' }">
														<td>${wageProposal.proposedByUserId == userId ? 'You' : 'Employer' }</td>
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
						
							<td class="hide-with-calendar">${dto.job.stringStartDate }</td>
							<td class="hide-with-calendar">${dto.job.stringEndDate }</td>	
							<td class="hide-with-calendar">${dto.job.workDays.size() } ${dto.job.workDays.size() <= 1 ? 'day' : 'days' }</td>	
							<td class="hide-with-calendar">${dto.job.city }, ${dto.job.state }</td>
												
						</tr>
					</c:if>
				</c:forEach>
										
			</tbody>
	

