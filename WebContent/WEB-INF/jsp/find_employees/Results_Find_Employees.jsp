<%@ include file="../includes/TagLibs.jsp" %>


<c:choose>
	<c:when test="${empty userDtos }" >
		No prospective applicants meet your search criteria
	</c:when>
	<c:otherwise>
		<div id="calendarSpecs" data-first-date="${jobDto.date_firstWorkDay }"
			data-number-of-months="${jobDto.months_workDaysSpan }">
		</div>
		<table class="main-table-style">
			<thead>
				<tr>
					<th></th>
					<th>Name</th>
					<th>Overall Rating</th>
					<th>Jobs Completed</th>
					<th>Home Location</th>
					<th>Days Available</th>
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
						<td>${userDto.ratingValue_overall }</td>
						<td>${userDto.count_jobsCompleted }</td>
		<%-- 				<td>${userDto.user.homeCity != '' && userDto.user.homeState != '' ? --%>
		<%-- 						 userDto.user.homeCity, userDto.user.homeState : ''}</td> --%>
						<td>${userDto.user.homeCity}, ${userDto.user.homeState }</td> 
						<td class="days-available">
							<span class="toggle-availability-calendar">
								${userDto.count_availableDays_perFindEmployeesSearch }<span class="glyphicon glyphicon-menu-up"></span>
							</span>
							<div class="calendar-container read-only">
								<div class="calendar"></div>
							</div>
							<div class="dates">
								<c:forEach items="${userDto.availableDays }" var="date">
									<div data-date="${date }"></div>
								</c:forEach>
							</div>						
						</td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>