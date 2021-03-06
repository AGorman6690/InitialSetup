<%@ include file="../includes/TagLibs.jsp" %>


<c:choose>
	<c:when test="${empty response.users }" >
		<p>No prospective applicants meet your search criteria</p>
	</c:when>
	<c:otherwise>
		<table>
			<thead>
				<tr>					
					<th>Name</th>
					<th>Overall Rating</th>
					<th>Jobs Completed</th>
					<th>Home Location</th>
					<c:if test="${not empty response.countDatesSearched }">
						<th id="days-available-result">Days Available</th>
					</c:if>
					<th></th>
				</tr>
			</thead>
			<tbody>
			
				<c:forEach items="${response.users }" var="user">
				
					<tr data-user-id="${user.user.userId }"
						data-user-name="${user.user.firstName }">
	
						<td>
							<c:set var="param_userId" value="${user.user.userId }" ></c:set>
							<c:set var="param_userName" value="${user.user.firstName}" ></c:set>
							<%@ include file="./../ratings/Template_RenderRatingsMod.jsp" %>
						</td>
						
						<td class="">
							<c:set var="param_userOverallRating" value="${user.overallRating }" ></c:set>
							<%@ include file="./../ratings/RenderStars.jsp" %>
						</td>
							
						<td class="">${user.countJobsCompleted }</td>
						
						<td>${user.user.homeCity}, ${user.user.homeState }</td> 
						
						<c:if test="${!empty response.countDatesSearched }">
							<td class="days-available">
								<span class="toggle-availability-calendar">
									${user.countDaysAvailable } of 
									${response.countDatesSearched }
								</span>	
																
							</td>		
						</c:if>	
						
						
						<td>
							<span class="make-an-offer pointer" data-user-id="${user.user.userId }">
								Make An Offer
							</span>
							<div class="make-offer-container"></div>
						</td>	
															
									
					</tr>
					
				</c:forEach>
				 
			</tbody>
		</table>
	</c:otherwise>
</c:choose>