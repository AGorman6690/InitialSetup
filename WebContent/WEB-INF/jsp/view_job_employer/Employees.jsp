<%@ include file="../includes/TagLibs.jsp" %>				
			
<c:choose>						
	<c:when test="${empty jobDto.employeeDtos}">
		<div class="no-data">There are currently no employees for this job</div>
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
					<td><a class="accent" href="/JobSearch/job/${jobDto.job.id }/user/
							${userDto.user.userId}/jobs/completed"> ${userDto.user.firstName }</a></td>
					<td>$ ${userDto.acceptedProposal.amount }</td>
					<td>${userDto.acceptedProposal.dateStrings_proposedDates.size() } of ${jobDto.workDays.size() } days</td>
					<td>$ ${userDto.totalPayment }</td>
				</tr>	
			</c:forEach>						
			</tbody>
		</table>
	</c:otherwise>
</c:choose>