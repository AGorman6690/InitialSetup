<%@ include file="../includes/TagLibs.jsp" %>

<div class="section-body">
	<h4>Apply</h4>
		<a id="notLoggedIn-ApplicationWarning"
		class="${!isLoggedIn ? 'show-warning' : ''}"
		href="/JobSearch/login-signup?login=true">
		You must be logged in to apply for a job.
			</a>	
	<div class="body-element-container ${!isLoggedIn ? 'not-logged-in' : '' }">

		<div id="submitApplicationContainer">
			<a id="submitApplication" class="accent">Submit</a>
		</div>														
	
		<div class="info-container">
			<div class="info-label">Desired Pay Per Hour</div>
			<div class="info-value">
				<input class="form-control" placeholder="" id="amount">								
			</div>
		</div>
					
		<div id="questions" class="body-element-container info-container">
			<div class="info-label">Questions</div>
			<div id="answersContainer" class="info-value">									
				<c:forEach items="${jobDto.questions }" var="param_question">
					<%@include file="../templates/Questions_AnswerInput.jsp"%>
				</c:forEach>																	
			</div>
		</div>		
	</div>								
</div>