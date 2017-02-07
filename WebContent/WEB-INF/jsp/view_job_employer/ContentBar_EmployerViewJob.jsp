<%@ include file="../includes/TagLibs.jsp" %>


<p id="content_jobInfo" class="content-bar" data-section-id="jobInfoContainer">Job Information</p>

<c:if test="${context == 'waiting' }">
	<p id="content_applicants" class="content-bar selected-lines" data-section-id="applicantsContainer">Applicants</p>
</c:if>

<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
	<p id="content_employees" class="content-bar  ${context != 'waiting' ? 'selected-lines' : '' }" data-section-id="employeesContainer">
		${context == 'complete' ? 'Employee Ratings' : 'Employees' }</p>
</c:if>


<%-- <c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">		 --%>
<!-- 	<p class="content-bar" data-section-id="questionsContainer">Questions</p> -->
<%-- </c:if>  --%>