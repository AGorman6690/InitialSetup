<%@ include file="../includes/TagLibs.jsp"%>

<h4>Employment</h4>
<div id="" class="section">
	<div id="employmentSubHeader" class="sub-header">
		<div class="sub-header-item">
			<div class="item-label">
				Past
			</div>
			<div data-job-status="2" class="item-value selcted-application-type accent">
				${pastEmploymentCount }
			</div>
		</div>
		<div class="sub-header-item">
			<div class="item-label">
				Current
			</div>
			<div data-job-status="1" id="failedApplicationCount" class="item-value accent accent">
				${currentEmploymentCount }
			</div>
		</div>
		<div class="sub-header-item">
			<div class="item-label">
				Future
			</div>
			<div data-job-status="0" id="allApplications" class="item-value accent">					
				${futureEmploymentCount }
			</div>
		</div>										
	</div>

	<div id="employment" class="section-body">
	<c:choose>
		<c:when test="${jobs_employment.size() > 0 }">						
			<table id="" class="main-table-style">
			
				<thead>					
					<tr class="header-2">
						<th id="jobName-e" class="left-edge">Job Name</th>
						<th id="action-e" class="">Start Date</th>
						<th id="status-e" class="right-edge">End Date</th>								
					</tr>
				</thead>					

				<tbody>
				
					<c:forEach items="${jobs_employment }" var="job">
						<tr class="static-row job" data-job-status="${job.status }">
							<td><a class="accent" href="/JobSearch/job/${job.id }?c=profile-complete&p=1">${job.jobName }</a></td>
							<td>${job.stringStartDate }</td>
							<td>${job.stringEndDate }</td>													
						</tr>
					</c:forEach>
											
				</tbody>
			</table>
							
		</c:when>
		<c:otherwise>
			
			Your employment record is non-existent.
		</c:otherwise>
		
	</c:choose>
	</div>	

</div> 
