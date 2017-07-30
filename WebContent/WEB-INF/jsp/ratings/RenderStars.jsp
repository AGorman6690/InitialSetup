<%@ include file="../includes/TagLibs.jsp"%>


<!-- ********************************************************** -->
<!-- ********************************************************** -->
<!-- Can this be in the template_render stars????? -->
<!-- ********************************************************** -->
<!-- ********************************************************** -->
<c:choose>
	<c:when test="${empty userDto.ratingValue_overall }">
		No Rating
	</c:when>
	<c:otherwise>
		<input name="input-1" class="rating-loading"
				value="${userDto.ratingValue_overall }	">									
		${userDto.ratingValue_overall}
	</c:otherwise>
</c:choose>	