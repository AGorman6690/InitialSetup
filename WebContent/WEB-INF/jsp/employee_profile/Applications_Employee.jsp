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
								<%@ include file="../wage_proposal/WageProposal.jsp" %>
							<td>
								<%@ include file="../wage_proposal/History_WageProposals.jsp" %>
							</td>										
						
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringStartDate }</td>
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringEndDate }</td>	
							<td class="hide-with-calendar">${applicationDto.jobDto.workDays.size() } ${applicationDto.jobDto.workDays.size() <= 1 ? 'day' : 'days' }</td>	
							<td class="hide-with-calendar">${applicationDto.jobDto.job.city }, ${applicationDto.jobDto.job.state }</td>
												
						</tr>
<%-- 					</c:if> --%>
				</c:forEach>
										
			</tbody>
	

