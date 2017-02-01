<%@ include file="../includes/TagLibs.jsp" %>


<div id="jobInfo" class="first side-bar selected-blue" data-section-id="jobInfoContainer">Job Information</div>
<c:choose>
	<c:when test="${context == 'find' }">
		<div id="apply" class="side-bar" data-section-id="applyContainer">Apply</div>	
	</c:when>
	<c:otherwise>
		<div id="questions" class="side-bar" data-section-id="section_questionsContainer">Questions</div>						
	</c:otherwise>					
</c:choose>	