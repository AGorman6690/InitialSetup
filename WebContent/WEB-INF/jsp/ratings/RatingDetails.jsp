<%@ include file="../includes/TagLibs.jsp"%>	

<div id="user-rating-details" class="">
	<c:forEach items="${response.profileInfoDto.profileRatingDto.rateCriteria }" var="rateCriterion">
		<div class="criteria-cont">
			<span class="criteria-name">${rateCriterion.shortName }</span>
			<span class="rating-value">
				<input name="input-1" class="rating-loading"
						value="${rateCriterion.stringValue }">${rateCriterion.stringValue }	
			</span>
		</div>
	</c:forEach>
</div>	