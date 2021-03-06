<%@ include file="../includes/TagLibs.jsp" %>

<div id="calendar-days">
	<c:forEach items="${calendarDays_employmentSummary }" var="calendarDay">
		<div class="calendar-day" data-date="${calendarDay.date }">
			<c:forEach items="${calendarDay.jobDtos }" var="jobDto">
				<div class="job show-job-info-mod" data-job-name="${jobDto.job.jobName }"
					data-job-id="${jobDto.job.id }"
					data-start-time="${jobDto.workDay.stringStartTime }"
					data-end-time="${jobDto.workDay.stringEndTime }">
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>
<div class="calendar-container hide-prev-next">
	<div class="calendar v2 hide-unused-rows style-unselectable-days" data-number-of-months=${monthSpan_employmentSummaryCalendar }></div>
</div>