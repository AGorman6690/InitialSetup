<%@ include file="../includes/TagLibs.jsp" %>


<c:choose>
	<c:when test="${empty userDtos }" >
		<p>No prospective applicants meet your search criteria</p>
	</c:when>
	<c:otherwise>
		<table>
			<thead>
				<tr>
					<th></th>
					<th>Name</th>
					<th>Overall Rating</th>
					<th>Jobs Completed</th>
					<th>Home Location</th>
					<c:if test="${doShowAvailability }">
						<th>Days Available</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${userDtos }" var="userDto">
					<tr data-user-id="${userDto.user.userId }"
						data-user-name="${userDto.user.firstName }">
						<td>
							<span class="accent show-make-offer-modal">
								Make An Offer
							</span>
							<div class="make-offer-container"></div>
						</td>
						<td>${userDto.user.firstName }</td>
						<td>
							<%@ include file="./../ratings/RenderStars.jsp" %>
						</td>
						<td>${userDto.count_jobsCompleted }</td>
		<%-- 				<td>${userDto.user.homeCity != '' && userDto.user.homeState != '' ? --%>
		<%-- 						 userDto.user.homeCity, userDto.user.homeState : ''}</td> --%>
						<td>${userDto.user.homeCity}, ${userDto.user.homeState }</td> 
						<c:if test="${doShowAvailability }">
							<td class="days-available">
								<span class="toggle-availability-calendar">
									${userDto.count_availableDays_perFindEmployeesSearch } of 
									${employeeSearch.workDays.size(); }
								</span>										
							</td>		
						</c:if>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>