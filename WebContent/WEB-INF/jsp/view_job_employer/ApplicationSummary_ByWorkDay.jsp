<%@ include file="../includes/TagLibs.jsp" %>

<div class="mod-header">
	<h2>${date }</h2>
	<span class="glyphicon glyphicon-remove"></span>
</div>
<div class="mod-body">	
	<c:set var="param_applicationDtos" value="${jobDto.applicationDtos }" />
	<h1>Applicants</h1>
	<div id="applicantsOnDate">
		<%@ include  file="./Applicants_ByJobAndDate.jsp" %>
	</div>
	<c:if test="${!empty applicationDtos_applicantsWhoAreAvailableButDidNotApplyForDate }">
		<div id="otherApplicants" class="pad-top">
			<h1>Other Available Applicants</h1>
			<c:set var="param_applicationDtos" value="${applicationDtos_applicantsWhoAreAvailableButDidNotApplyForDate }" />
			<%@ include  file="./Applicants_ByJobAndDate.jsp" %>
		
		</div>
	</c:if>
	<c:if test="${count_userswhoAreAvailableButHaveNotApplied > 0}">
		<div id="otherUsersWhoHaveNotApplied" class="pad-top">
			<p>
				<a href="/JobSearch/job/${jobDto.job.id}/find-employees">${count_userswhoAreAvailableButHaveNotApplied } more available users</a>
			</p>
		</div>
	</c:if>
</div>