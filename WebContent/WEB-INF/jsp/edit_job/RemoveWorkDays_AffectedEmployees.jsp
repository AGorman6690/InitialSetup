<%@ include file="../includes/TagLibs.jsp"%>

<div id="affected-employees">
	<c:choose>
		<c:when test="${applicationDtos.size() > 0 }">
			<p>The following employees are currently employed on this day.</p>
			<p>If you remove these days, then each employee will have to right to review the changes
			and, if desired, issue a counter offer.</p>
			<ul id="employees">
				<c:forEach items="${applicationDtos }" var="applicationDto">
					<li>${applicationDto.applicantDto.user.firstName } ${applicationDto.applicantDto.user.lastName }</li>
				</c:forEach>
			</ul>
			<div>
				<span id="approve-work-day-removal">Approve</span>
				<span id="cancel-work-day-removal">Cancel</span>
			</div>
			<div>
				<input id="days" type="text">
				<input id="hours" type="text">
				<input id="minutes" type="text">
			</div>		
		</c:when>
		<c:otherwise>
		
		</c:otherwise>
	</c:choose>

</div>