<%@ include file="../includes/TagLibs.jsp"%>

<c:choose>
	<c:when test="${!empty users_employees && users_employees.size() > 0 }">
		<div id="affected-employees">
			<p>If you remove ${isRemovingOneDate ?  'this date' : 'these dates'},
				then you will have to reach
				${users_employees.size() == 1 ? 'a new negotiation' : 'new negotiations' }  with the following ${users_employees.size() == 1 ? 'employee' : 'employees' }</p>
			<ul>
				<c:forEach items="${users_employees }" var="user">
					<li>${user.firstName } ${user.lastName }</li>	
				</c:forEach>
			</ul>
		 </div>
	</c:when>
	<c:otherwise>	
<%-- 		<p>There are no employees who will be affected by removing ${isRemovingOneDate ?  'this date' : 'these dates'}</p> --%>
	</c:otherwise>
</c:choose>