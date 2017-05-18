<%@ include file="../includes/TagLibs.jsp" %>

<table id="applicantsTable" class="main-table-style table-view">
	<thead>
		<tr>
			<th>Name</th>	
		</tr>
	</thead>
	<tbody class="vertical-lines">
		<c:forEach items="${applicants_whoAreAvailableButDidNotApplyForDate }" var="user">
			<tr>			
				<td class="table-view">
					<div class="vert-border name-container">
						<a class="accent" href="#"> ${user.firstName }</a>
					</div>
				</td>
			</tr>		
		</c:forEach>						
	</tbody>					
</table>		
