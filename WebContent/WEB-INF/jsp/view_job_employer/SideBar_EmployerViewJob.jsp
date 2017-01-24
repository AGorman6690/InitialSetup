<%@ include file="../includes/TagLibs.jsp" %>


<c:if test="${context == 'waiting' }">
	<div id="applicants" class="first side-bar selected-blue" data-section-id="applicantsContainer">Applicants</div>
</c:if>

<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
	<div id="" class="side-bar  ${context != 'waiting' ? 'selected-blue' : '' }" data-section-id="employeesContainer">
		${context == 'complete' ? 'Employee Ratings' : 'Employees' }</div>
</c:if>

<div id="jobInfo" class="side-bar ${context == 'work-history' ? 'selected-blue' : '' }" data-section-id="jobInfoContainer">Job Information</div>		

<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">		
	<div id="questionInfo" class="side-bar" data-section-id="questionsContainer">Questions</div>
</c:if>