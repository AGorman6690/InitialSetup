<%@ include file="../includes/TagLibs.jsp" %>

<table class="main-table-style">
	<thead>
		<tr>
			<th>Name</th>
			<th>Overall Rating</th>
			<th>Jobs Completed</th>
			<th>Home Location</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${userDtos }" var="userDto">
			<tr>
				<td>${userDto.user.firstName }</td>
				<td>${userDto.ratingValue_overall }</td>
				<td>${userDto.count_jobsCompleted }</td>
<%-- 				<td>${userDto.user.homeCity != '' && userDto.user.homeState != '' ? --%>
<%-- 						 userDto.user.homeCity, userDto.user.homeState : ''}</td> --%>
				<td>${userDto.user.homeCity}, ${userDto.user.homeState }</td> 
			</tr>
		</c:forEach>
	</tbody>
</table>