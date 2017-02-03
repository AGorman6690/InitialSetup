<%@ include file="../includes/TagLibs.jsp"%>

<div class="section">
	<div class="section-body">
		<c:choose>
			<c:when test="${jobDtos.size() == 0 }">
				<div>You have no jobs waiting to start</div>	
			</c:when>
			
			<c:otherwise>
				<table id="table_jobsWaitingToStart" class="main-table-style">
						<col>
						<col>
						<colgroup span="3"></colgroup>
						<colgroup span="2"></colgroup>
						<col>
						<tr class="header-1">
		
							<th rowspan="1" class="left-edge"></th>
							<th rowspan="1" class="left-edge"></th>
							<th id="headerWageNegotiaions" class="span left-edge right-edge" colspan="3" scope="colgroup">Wage Negotiations</th>
							<th id="headerApplications" class="span right-edge" colspan="2" scope="colgroup">Applications</th>
							<th rowspan="1" class="right-edge"></th>
							<tr class="header-2">
								<th id="name" class="left-edge">Job Name</th>
								<th id="startsIn" class="left-edge">Starts In</th>
								<th id="" class="number left-edge">New</th>
								<th id="" class="number">Sent</th>
								<th id="" class="number right-edge">Received</th>
								<th id="newApplicantions" class="number left-edge">New</th>
								<th id="" class="number right-edge">Total</th>
								<th id="hires" class="number right-edge">Employees</th>									
<!-- 								<th id="markJobCompleteHeader"></th> -->
								<th></th>
							</tr>
						</tr>
				
					<tbody>
		
						
						<c:forEach items="${yetToStartJobs_Dtos }" var="jobDto">
						
							<tr id="${jobDto.job.id }">
								
								<td class="job-name"><a class="accent" href="../job/${jobDto.job.id}?c=waiting&p=2" >${jobDto.job.jobName }</a></td>
								<td>${jobDto.daysUntilStart } days</td>
								
								<td class="data">-</td>
								<td class="data">-</td>
	
								<c:set var="tdValue" value="${jobDto.failedWageNegotiationDtos.size() }" /> 
								<td class="data ${tdValue > 0 ? 'pop' : '' }">${tdValue > 0 ? tdValue : '-' }</td>	
		
								<c:set var="tdValue" value="${jobDto.newApplicationCount }" /> 
								<td class="data ${tdValue > 0 ? 'pop' : '' }">${tdValue > 0 ? tdValue : '-' }</td>	
									
								<c:set var="tdValue" value="${jobDto.applications.size() }" /> 
								<td class="data ${tdValue > 0 ? 'pop' : '' }">${tdValue > 0 ? tdValue : '-' }</td>	
									
								<c:set var="tdValue" value="${jobDto.employees.size() }" /> 
								<td class="data ${tdValue > 0 ? 'pop' : '' }">${tdValue > 0 ? tdValue : '-' }</td>	

<!-- 								<td><a class="square-button-green" href="/JobSearch/job/$jobDto.job.id/update/status/1">Start Job</a></td> -->

							</tr>

						</c:forEach>
					</tbody>
				</table>		
			</c:otherwise>		
		</c:choose>
	</div>
</div>