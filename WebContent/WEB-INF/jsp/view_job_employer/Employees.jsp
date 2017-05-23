<%@ include file="../includes/TagLibs.jsp" %>				
			
<c:if test="${jobDto.job.flag_isNotAcceptingApplications == 0}">
	<div class="center pad-btm-2 pad-top">
		<a class="sqr-btn teal" href="/JobSearch/job/${jobDto.job.id }/find-employees">Find Employees</a>
	</div>
</c:if>
<c:choose>						
	<c:when test="${empty jobDto.employeeDtos}">
		<div class="no-data center">
			<p>There are currently no employees for this job</p>	
		</div>
	</c:when>	
	<c:otherwise>	
		<div id="terminate-employee-message" class="mod  simple-header">
			<div class="mod-content">
				<div class="mod-header">				
				</div>
				<div class="mod-body">
				
				</div>
			</div>
		</div>
		<table id="employeesTable" class="main-table-style shadow ">
			<thead>
				<tr>
					<th id="Name">Name</th>
					<th>Hourly Wage</th>
					<th>Work Days</th>
					<th>Total Wage</th>
					<th></th>
				</tr>
			</thead>
			<tbody>						
			<c:forEach items="${jobDto.employeeDtos }" var="userDto">
				<tr data-user-id="${userDto.user.userId }">
					<td>${userDto.user.firstName} ${userDto.user.lastName }</td>
					<td>$ ${userDto.acceptedProposal.amount }</td>
					<td>${userDto.acceptedProposal.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days</td>
					<td>$ ${userDto.totalPayment }</td>
					
					<td>
						<div class="popup">					
						<span class="glyphicon glyphicon-remove display-message-terminate-employee"></span>		
							<div class="popuptext no-arrow">
								<p>Terminate employee</p>
							</div>					
						</div>
					</td>
				</tr>	
			</c:forEach>						
			</tbody>
		</table>
	</c:otherwise>
</c:choose>