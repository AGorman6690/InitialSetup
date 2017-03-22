<%@ include file="../includes/TagLibs.jsp"%>

<div class="section">
	<div class="section-body">
		<c:choose>
			<c:when test="${jobDtos.size() == 0 }">
				<div>You have no jobs waiting to start</div>	
			</c:when>
			
			<c:otherwise>
				<table id="table_jobsWaitingToStart" class="main-table-style">
					<thead>
						<tr class="">		
							<th class="placeholder perm1 perm"></th>
							<th class="placeholder perm1 perm"></th>
							<th class="wage-proposal" colspan="4" scope="colgroup">
								<button class="sqr-btn teal select-on-load" data-perspective="wage-proposal">Wage Proposals</button>
							</th>
							<th class="application employee" colspan="2" scope="colgroup">
								<button class="sqr-btn gray" data-perspective="wage-proposal">Wage Proposals</button>
							</th>
							<th class="application" colspan="3" scope="colgroup">
							<button class="sqr-btn teal" data-perspective="application">Applications</button>
							</th>
							<th class="wage-proposal employee" colspan="1" scope="colgroup">
								<button class="sqr-btn gray" data-perspective="application">Applications</button>
							</th>
							<th class="employee" colspan="2" scope="colgroup">
								<button class="sqr-btn teal" data-perspective="employee">Employees</button>
							</th>
							<th class="wage-proposal application" colspan="1" scope="colgroup">
								<button class="sqr-btn gray" data-perspective="employee">Employees</button>
							</th>
							
							<th rowspan="1" class=""></th>
						</tr>
						<tr class="">
							<th id="job-name" class="perm1 perm">Job Name</th>
							<th id="starts-in" class="other perm1 perm">Starts In</th>								
							<th id="" class="wage-proposal">New</th>
							<th id="waiting-for-applicant" class="wage-proposal perm">Sent</th>
							<th id="waiting-for-you" class="wage-proposal perm">Received</th>
							<th id="" class="wage-proposal">Accepted</th>
<!-- 							<th id="" class="wage-proposal">Offers you initiated</th> -->
<!-- 							<th id="" class="wage-proposal">Pending applicant approval</th> -->
							<th id="" class="application">New</th>
							<th id="" class="application perm">Open</th>
							<th id="" class="application">Declined</th>
<!-- 							<th id="" class="application">Invites</th> -->
							<th id="" class="employee perm">Hires</th>
							<th id="" class="employee">Max Hires</th>									
						</tr>
					</thead>
					<tbody>
		
						
						<c:forEach items="${jobDtos }" var="jobDto">
						
							<tr id="${jobDto.job.id }">
								
								<td class="perm1 perm">
									<a class="accent" href="../job/${jobDto.job.id}?c=waiting&p=2&d=all-apps" >
										${jobDto.job.jobName }
									</a>
								</td>
								
								<td class="perm1 perm">${jobDto.daysUntilStart } days</td>

								<td class="wage-proposal first" data-job-status="${jobDto.job.status }" data-job-data="received-proposals-new">
									<span class="${jobDto.countWageProposals_received_new == 0 ? '' : 'accent'}">
										${jobDto.countWageProposals_received_new == 0 ? '-' : jobDto.countWageProposals_received_new}
									</span>									
								</td>
								
								<td class="wage-proposal perm perm-first" data-job-status="${jobDto.job.status }" data-job-data="sent-proposals">
									<span class="${jobDto.countWageProposals_sent == 0 ? '' : 'accent'}">
										${jobDto.countWageProposals_sent == 0 ? '-' : jobDto.countWageProposals_sent}
									</span>									
								</td>								

								<td class="wage-proposal perm" data-job-status="${jobDto.job.status }" data-job-data="received-proposals">
									<span class="${jobDto.countWageProposals_received == 0 ? '' : 'accent'}">
										${jobDto.countWageProposals_received == 0 ? '-' : jobDto.countWageProposals_received}
									</span>									
								</td>	
								
								<td class="wage-proposal last" data-job-status="${jobDto.job.status }" data-job-data="accepted-proposals">
									-
<%-- 									<span class="${jobDto.countWageProposals_received == 0 ? '' : 'accent'}"> --%>
<%-- 										${jobDto.countWageProposals_received } --%>
<!-- 									</span>									 -->
								</td>	
								
<%-- 								<td class="wage-proposal" data-job-status="${jobDto.job.status }" data-job-data="initiated-proposals">- --%>
<%-- 									<span class="${jobDto.countWageProposals_received == 0 ? '' : 'accent'}"> --%>
<%-- 										${jobDto.countWageProposals_received } --%>
<!-- 									</span>									 -->
<!-- 								</td>				 -->
								
<%-- 								<td class="wage-proposal" data-job-status="${jobDto.job.status }" data-job-data="pending-applicant-approval-proposals">- --%>
<%-- 									<span class="${jobDto.countWageProposals_received == 0 ? '' : 'accent'}"> --%>
<%-- 										${jobDto.countWageProposals_received } --%>
<!-- 									</span>									 -->
<!-- 								</td>	 -->
								
								<td class="application first" data-job-status="${jobDto.job.status }" data-job-data="new-apps">
									<span class="${jobDto.countApplications_new == 0 ? '' : 'accent'}">
										${jobDto.countApplications_new == 0 ? '-' : jobDto.countApplications_new}
									</span>									
								</td>																					
	
								<td class="application perm perm-first" data-job-status="${jobDto.job.status }" data-job-data="all-apps">
									<span class="${jobDto.countApplications_received == 0 ? '' : 'accent'}">
										${jobDto.countApplications_received == 0 ? '-' : jobDto.countApplications_received}
									</span>									
								</td>	
								
								<td class="application" data-job-status="${jobDto.job.status }" data-job-data="declined-apps">-
<%-- 									<span class="${jobDto.countApplications_received == 0 ? '' : 'accent'}"> --%>
<%-- 										${jobDto.countApplications_received == 0 ? '-' : 'jobDto.countApplications_received'} --%>
<!-- 									</span>									 -->
								</td>
								
<%-- 								<td class="application" data-job-status="${jobDto.job.status }" data-job-data="invites-apps"> --%>
<%-- 									<span class="${jobDto.countApplications_received == 0 ? '' : 'accent'}"> --%>
<%-- 										${jobDto.countApplications_received == 0 ? '-' : jobDto.countApplications_received} --%>
<!-- 									</span>									 -->
<!-- 								</td> -->

								<td class="employee perm perm-first first" data-job-status="${jobDto.job.status }" data-job-data="hired-employees">
									<span class="${jobDto.countEmployees_hired == 0 ? '' : 'accent'}">
										${jobDto.countEmployees_hired == 0 ? '-' : jobDto.countEmployees_hired}
									</span>									
								</td>
									
								<td class="employee max-hires">5
<%-- 									<span class="${jobDto.countEmployees_hired == 0 ? '' : 'accent'}"> --%>
<%-- 										${jobDto.countEmployees_hired == 0 ? '-' : 'jobDto.countEmployees_hired'} --%>
<!-- 									</span>									 -->
								</td>
							</tr>

						</c:forEach>
					</tbody>
				</table>		
			</c:otherwise>		
		</c:choose>
	</div>
</div>