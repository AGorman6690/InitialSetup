<%@ include file="../includes/TagLibs.jsp"%>
							

<%-- <c:if test="${applicationDto.application.status > -1 }">					 --%>
	<span class="dollar-sign">$</span>
	<fmt:formatNumber type="number" minFractionDigits="2" 
		maxFractionDigits="2" value="${applicationDto.employmentProposalDto.amount}"/>
	
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
							<td>${wageProposal.proposedByUserId == user.userId ? 'You' :
									user.profileId == 1 ? 'Employer' : 'Applicant' }</td>
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
<%-- </c:if> --%>