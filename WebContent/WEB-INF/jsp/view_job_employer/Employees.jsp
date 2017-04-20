<%@ include file="../includes/TagLibs.jsp" %>				
			
<c:choose>						
	<c:when test="${empty jobDto.employeeDtos}">
		<div class="no-data">
			<p>There are currently no employees for this job</p>
			<a class="sqr-btn teal" href="/JobSearch/job/${jobDto.job.id }/find-employees">Find Employees</a>	
		</div>
	</c:when>
	
	<c:otherwise>
	
		<table id="employeesTable" class="main-table-style shadow">
			<thead>
				<tr>
					<th id="Name">Name</th>
					<th>Hourly Wage</th>
					<th>Work Days</th>
					<th>Total Wage</th>
				</tr>
			</thead>
			<tbody>						
			<c:forEach items="${jobDto.employeeDtos }" var="userDto">
				<tr>
					<td>${userDto.user.firstName} ${userDto.user.lastName }</td>
					<td>$ ${userDto.acceptedProposal.amount }</td>
					<td>${userDto.acceptedProposal.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days</td>
					<td>$ ${userDto.totalPayment }</td>
				</tr>	
			</c:forEach>						
			</tbody>
		</table>
	</c:otherwise>
</c:choose>