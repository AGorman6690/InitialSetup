<%@ include file="../includes/TagLibs.jsp"%>

<c:choose>
	<c:when test="${jobDtos.size() == 0 }">
		<div>You have no jobs waiting to start</div>	
	</c:when>
	<c:otherwise>
		<table id="table_jobsWaitingToStart" class="main-table-style">
			<thead>

				<tr class="">
					<th id="job-name" >Job</th>
					<th id="" >Start Date</th>
					<th id="" class="">End Date</th>
					<th id="" class="">Location</th>								
					<th id="" class="">Offers Made</th>
					<th id="" class=" ">Offers Received</th>
					<th id="" class=" ">Positions Filled</th>
					<th id="" class="">Positions Available</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${jobDtos }" var="jobDto">
					<tr id="${jobDto.job.id }">
						<td class="job-details perm1 perm perm-first">
							<a class="accent" href="../job/${jobDto.job.id}?c=waiting&p=2&d=all-apps" >
								${jobDto.job.jobName }
							</a>
						</td>						
						<td class="job-details"><span>${jobDto.job.stringStartDate }</span></td>
						<td class="job-details"><span>${jobDto.job.stringEndDate }</span></td>
						<td class="job-details"><span>${jobDto.job.city}, ${jobDto.job.state }</span></td>
						<td>	
							<c:choose>
								<c:when test="${jobDto.countWageProposals_sent > 0 }">
									<a class="accent" href="../job/${jobDto.job.id}/?c=waiting&d=sent-proposals">
										${jobDto.countWageProposals_sent }</a>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>																								
						</td>
						<td>							
							<div class="offers-received-container">
								<c:choose>
									<c:when test="${jobDto.countWageProposals_received > 0 }">
										<a class="accent" href="../job/${jobDto.job.id}/?c=waiting&d=received-proposals">
											${jobDto.countWageProposals_received }</a>
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>
								</c:choose>									
								<c:if test="${jobDto.countWageProposals_received_new > 0 }">
									<a class="new-offers-received" href="../job/${jobDto.job.id}/?c=waiting&d=received-proposals-new">
										${jobDto.countWageProposals_received_new }</a>
								</c:if>
							</div>																
						</td>							
						<td>
							<c:choose>
								<c:when test="${jobDto.countEmployees_hired > 0 }">
									<a class="accent" href="#">
										${jobDto.countEmployees_hired }</a>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>														
						</td>																					
						<td>
							8								
						</td>	
					</tr>
				</c:forEach>
			</tbody>
		</table>		
	</c:otherwise>		
</c:choose>