<%@ include file="../includes/TagLibs.jsp"%>

<div class="section">
	<div class="section-body">
		<c:choose>
			<c:when test="${jobDtos_jobsCompleted.size() >0 }">			
				<table id="table_jobsComplete" class="main-table-style">
					<thead>
						<tr>
							<th>Job Name</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${jobDtos_jobsCompleted }" var="jobDto">
							<tr class="static-row" id="${jobDto.job.id}">
								<td class="job-name"><a href="/JobSearch/job/${jobDto.job.id}?c=complete&p=2" class="accent">${jobDto.job.jobName}</a></td>
							</tr>						

						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
				You have no completed jobs at this time
			</c:otherwise>
		</c:choose>
	</div>
</div>