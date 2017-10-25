<%@ include file="./includes/TagLibs.jsp"%>
<div>
	<p>Last 10 Equations:</p>
	<c:forEach items="${expressions }" var="expression">
		<div>${expression }</div>
	</c:forEach>
</div>