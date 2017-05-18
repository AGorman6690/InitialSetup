<%@ include file="../../includes/TagLibs.jsp"%>

<c:forEach items="${jobDto.workDayDtos }" var="workDayDto">
	<div class="work-day-dto" data-date="${workDayDto.workDay.stringDate }"
		 data-count-applicants="${workDayDto.count_applicants }"
		 data-count-positions-filled="${workDayDto.count_positionsFilled }"
		 data-count-total-positions="${workDayDto.count_totalPositions }">
	 </div>
</c:forEach>