<%@ include file="../includes/TagLibs.jsp"%>	

<div id="user-rating-details" class="">
	<div id="overall-rating" data-toggle-id="user-rating-details">
		<div class="criteria-cont">
			<span class="criteria-name">Overall</span>
			<span class="rating-value">	
				<input name="input-1" class="rating-loading"
						value="${response.profileInfoDto.profileRatingDto.overallRating }	">
						${response.profileInfoDto.profileRatingDto.overallRating }	
			</span>
		</div>
					
	</div>
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