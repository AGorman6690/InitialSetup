<%@ include file="../includes/TagLibs.jsp" %>

<div id="calendar-days">
	<c:forEach items="${calendarDays_employmentSummary }" var="calendarDay">
		<div class="calendar-day" data-date="${calendarDay.date }">
			<c:forEach items="${calendarDay.jobDtos }" var="jobDto">
				<div class="job" data-job-name="${jobDto.job.jobName }"
					data-start-time="${jobDto.workDayDto.workDay.stringStartTime }"
					data-end-time="${jobDto.workDayDto.workDay.stringEndTime }">
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>
<div class="calendar-container hide-prev-next">
	<div class="calendar v2 hide-unused-rows style-unselectable-days" data-number-of-months=${monthSpan_employmentSummaryCalendar }></div>
</div>