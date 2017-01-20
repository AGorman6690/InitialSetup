<%@ include file="../includes/TagLibs.jsp"%>

	<h4>Applications</h4>
	<div id="" class="section">

<!-- 		<div id="applicationSubHeader" class="sub-header"> -->
<!-- 			<div class="sub-header-item"> -->
<!-- 				<div class="item-label"> -->
<!-- 					Open -->
<!-- 				</div> -->
<!-- 				<div data-value="open" class="item-value selcted-application-type accent"> -->
<%-- 					${openApplicationCount } --%>
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="sub-header-item"> -->
<!-- 				<div class="item-label"> -->
<!-- 					Failed -->
<!-- 				</div> -->
<!-- 				<div data-value="failed" id="failedApplicationCount" class="item-value accent"> -->
<%-- 					${failedApplicationCount } --%>
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="sub-header-item"> -->
<!-- 				<div class="item-label"> -->
<!-- 					All -->
<!-- 				</div> -->
<!-- 				<div data-value="all" id="allApplications" class="item-value accent">					 -->
<%-- 					${applicationDtos.size() } --%>
<!-- 				</div> -->
<!-- 			</div>										 -->
<!-- 		</div> -->

	<c:choose>
		<c:when test="${applicationDtos.size() > 0 }">						
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
<!-- 						<th id="status-th" class="right-edge">Status</th>								 -->
					</tr>
				</thead>					

				<tbody>
				
					<c:forEach items="${applicationDtos }" var="dto">
						<c:if test="${dto.application.status < 3 }">
							<tr class="static-row application" data-application-status="${dto.application.status }">
								<td><a class="accent" href="/JobSearch/job/${dto.job.id }?c=profile-incomplete">${dto.job.jobName }</a></td>
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
<%-- 													<c:set var="toggleId" value="${dto.application.applicationId }-wp-response"></c:set> --%>
													
<%-- 													<span class="accent" data-toggle-id="${toggleId }">Waiting for you</span>  --%>
														<c:set var="param_is_employer" value="0" />
														<c:set var="param_wage_proposal" value="${dto.currentWageProposal }" />
														<%@ include file="../templates/WageNegotiation.jsp" %>
<%-- 													<div id="${toggleId }" class="wage-proposal-response-container"> --%>
<%-- 														<c:set var="toggleId" value="${dto.application.applicationId }-counter"></c:set> --%>
<%-- 														<div id="${dto.currentWageProposal.id}" class="counter-offer-container"> --%>
<!-- 															<button class="accept-counter">Accept</button> -->
<%-- 															<button class="re-counter" data-toggle-id="${toggleId }">Counter</button>		 --%>
<!-- 															<button class="decline-counter">Decline</button>							 -->
<%-- 															<div id="${toggleId }" class="re-counter-amount-container"> --%>
<!-- 																<input class="re-counter-amount"></input> -->
<!-- 																<button class="send-counter-offer">Send</button> -->
<!-- 																<button class="cancel-counter-offer">Cancel</button> -->
<!-- 															</div>										 -->
<!-- 														</div> -->
<!-- 														<div class="sent-response-notification"></div>																 -->
<!-- 													</div> -->
												</c:when>
												<c:otherwise>
													Waiting for employer
												</c:otherwise>
											</c:choose>												
										
										</c:otherwise>
									</c:choose>
								</td>	
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/></td>										
<!-- 								<td> -->
<%-- 									<c:choose> --%>
<%-- 										<c:when test="${dto.application.status == 0 }">Open</c:when> --%>
<%-- 										<c:when test="${dto.application.status == 1 }">Failed</c:when> --%>
<%-- 										<c:when test="${dto.application.status == 2 }">Open</c:when> --%>
<%-- 									</c:choose>							 --%>
<!-- 								</td>														 -->
							</tr>
						</c:if>
					</c:forEach>
											
				</tbody>
			</table>
							
		</c:when>
		<c:otherwise>
			You have no open applications at this time.
		</c:otherwise>
		
	</c:choose>
</div>		

