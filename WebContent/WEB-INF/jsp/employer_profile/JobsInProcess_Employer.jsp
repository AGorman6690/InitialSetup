<%@ include file="../includes/TagLibs.jsp"%>

<div class="section">
	<div class="section-body">
		<c:choose>
			<c:when test="${jobDtos_jobsInProcess.size() == 0 }">
				<div>You have no active jobs at this time</div>
			</c:when>
			<c:otherwise>
				<table id="table_jobsInProgress" class="main-table-style">
					<thead>
						<tr>
							<th id="name">Job Name</th>
							<th id="newApplicantions">Start Date</th>
							<th id="totalApplications">End Date</th>								
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${jobDtos_jobsInProcess }" var="jobDto">
							<tr class="" id="${jobDto.job.id }">
							
								<td class="job-name"><a class="accent" href="../job/${jobDto.job.id}?c=in-process&p=2" >${jobDto.job.jobName }</a></td>
								<td><span class="">${jobDto.job.stringStartDate }</span></td>
								<td><span class="">${jobDto.job.stringEndDate }</span></td>							

							</tr>
		
						</c:forEach>
					</tbody>
				</table>		
			</c:otherwise>
		</c:choose>
	</div>
</div>