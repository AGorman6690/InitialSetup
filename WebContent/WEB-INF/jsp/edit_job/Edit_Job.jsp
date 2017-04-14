<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/DatePicker.jsp"%>
<%@ include file="../includes/resources/SelectPageSection.jsp" %>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/post_job/post_job_work_days_and_times.css" />
<link href="/JobSearch/static/css/replace_an_employee.css" rel="stylesheet" />	
<script src="/JobSearch/static/javascript/ReplaceAnEmployee.js" type="text/javascript"></script>
<!-- <script src="/JobSearch/static/javascript/post_job/PostJob.js" type="text/javascript"></script> -->
<script	src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>
<script type="text/javascript" src="/JobSearch/static/javascript/post_job/PostJob_WorkDaysAndTimes.js"></script>

<input id="jobId" type="hidden" value="${jobId }">
<div id="edit-options" class="pad-top center button-group" data-class-name1="selected-shadow">
	<h2>Edit...</h2>
	<button id="edit-dates" class="sqr-btn teal-2 select-page-section" data-page-section-id="dates-container">Dates</button>
	<button id="edit-times" class="sqr-btn teal-2 select-page-section" data-page-section-id="times-container">Times</button>
	<button id="edit-employee-schedule" class="sqr-btn teal-2 select-page-section" data-page-section-id="employee-schedule-container">Employee Schedule</button>
	<div id="main-save-cancel-edits" class="pad-top">
		<span id="save-edits">Save</span>
		<span id="cancel-edits">Cancel</span>
	</div>
</div>
<div id="remove-work-days-affected-employees"></div>
<!-- <div class="pad-top center button-group"> -->
<!-- 	<button id="edit-work-days" class="sqr-btn gray-2">Work Days</button> -->
<!-- 	<button id="edit-times" class="sqr-btn gray-2">Times</button> -->
<!-- </div> -->

<!-- <div class="v2 calendar-container teal-title pad-top"> -->
<!-- 	<div id="job-work-days" class="calendar"></div> -->
<!-- </div> -->
<div class="center">
	<div id="dates-container" class="page-section">
		<div class="row">			
			<div class="v2 sz-med item calendar-container teal-title">
				<div id="workDaysCalendar_postJob" class="calendar" data-is-showing-job="0">
				</div>			
			</div>
		</div>			
	</div>			
	<div id="times-container" class="page-section">	
		<div id="times-cont">				
			<div class="radio-container pad-top">
				<label><input id="select-all-dates" type="radio" name="set-times">Select all dates</label>
				<label><input id="deselect-all-dates" type="radio" name="set-times">Deselect all dates</label>
			</div>
			<div id="multiple-time-cont">
				<div>
					<p>Start Time</p>
					<select id="multiple-start-times" class="time start-time"></select>
				</div>
				<div>
					<p>End Time</p>
					<select id="multiple-end-times" class="time end-time"></select>
				</div>
				<div>
					<button id="apply-multiple-times" class="sqr-btn gray-2">Apply</button>
				</div>					
			</div>				
			<div class="item calendar-container teal-navigation v2">
				<div id="select-times-cal" class="calendar">
				</div>											
			</div>		
		</div>
	</div>	
	<div id="employee-schedule-container">
	</div>
</div>
<div id="json_work_day_dtos">${json_work_day_dtos }</div>