<%@ include file="../includes/TagLibs.jsp" %>				
			
<c:choose>						
	<c:when test="${empty jobDto.employeeDtos}">
		<div class="no-data">There are currently no employees for this job</div>
	</c:when>
	
	<c:otherwise>
	
		<table id="employeesTable" class="main-table-style">
			<thead>
				<tr>
					<th id="employeeName">Name</th>
					<th id="wage">Wage</th>
					<th id="employeerating">Rating</th>
				</tr>
			</thead>
			<tbody>						
			<c:forEach items="${jobDto.employeeDtos }" var="userDto">
				<tr>
					<td><a class="accent" href="/JobSearch/job/${jobDto.job.id }/user/
							${userDto.user.userId}/jobs/completed"> ${userDto.user.firstName }</a></td>
					<td>${userDto.wage }</td>
					<td>${userDto.rating.value }</td>
				</tr>	
			</c:forEach>						
			</tbody>
		</table>
	</c:otherwise>
</c:choose>