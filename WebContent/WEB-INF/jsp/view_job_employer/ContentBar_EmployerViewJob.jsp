<%@ include file="../includes/TagLibs.jsp" %>


<span id="job-name-header">${jobDto.job.jobName }</span>
<span  class="select-page-section ${context == 'complete' ? 'selected' : ''}" data-page-section-id="jobInfoContainer">Job Post</span>

<c:if test="${context == 'waiting' || context == 'in-process' }">
<span class="select-page-section selected" data-page-section-id="job-calendar-application-summary">Calendar</span>
	<span class="select-page-section " data-page-section-id="applicantsContainer">Applicants</span>
</c:if>

<c:if test="${context == 'waiting' || context == 'in-process'}">
	<span class="select-page-section ${context != 'waiting' ? 'selected' : '' }"
		 data-page-section-id="employeesContainer">
		Employees</span>
</c:if>


<%-- <c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">		 --%>
<!-- 	<p class="content-bar" data-section-id="questionsContainer">Questions</p> -->
<%-- </c:if>  --%>