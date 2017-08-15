<%@ include file="../includes/TagLibs.jsp"%>


<!-- ********************************************************** -->
<!-- ********************************************************** -->
<!-- Can this be in the template_render stars????? -->
<!-- ********************************************************** -->
<!-- ********************************************************** -->
<c:choose>
	<c:when test="${empty param_userOverallRating }">
		No Rating
	</c:when>
	<c:otherwise>
		<input name="input-1" class="rating-loading"
				value="${param_userOverallRating }	">									
		${param_userOverallRating}
	</c:otherwise>
</c:choose>	