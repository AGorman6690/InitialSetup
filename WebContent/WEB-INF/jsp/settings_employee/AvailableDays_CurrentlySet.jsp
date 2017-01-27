<%@ include file="../includes/TagLibs.jsp" %>

<c:forEach items="${userDto.availableDays }" var="day">
	<div data-date="${day}" data-do-remove="0"></div>
</c:forEach>