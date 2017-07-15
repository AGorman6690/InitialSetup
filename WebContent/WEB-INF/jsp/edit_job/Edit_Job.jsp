<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/DatePicker.jsp"%> --%>
<%@ include file="../includes/resources/SelectPageSection.jsp" %>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/post_job/post_job_work_days_and_times.css" />
<link href="/JobSearch/static/css/replace_an_employee.css" rel="stylesheet" />	
<script src="/JobSearch/static/javascript/ReplaceAnEmployee.js" type="text/javascript"></script>
<script	src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>
<!-- <script type="text/javascript" src="/JobSearch/static/javascript/post_job/PostJob_WorkDaysAndTimes.js"></script> -->

<input id="jobId" type="hidden" value="${jobId }">
<div id="edit-options" class="pad-top center button-group" data-class-name1="selected-shadow">
<!-- 	<h3 class="h3 green">What would you like to edit?</h3> -->
	<div class="pad-top">
		<button id="edit-dates" class="sqr-btn teal select-page-section"
			data-page-section-id="dates-container">Edit Job Work Days</button>
	<!-- 	<button id="edit-times" class="sqr-btn teal-2 select-page-section" data-page-section-id="times-container">Times</button> -->
	
		<button id="edit-employee-schedule" class="sqr-btn teal select-page-section"
			 data-page-section-id="employee-schedule-container">Remove An Employee</button>
	</div>
</div>
<div id="main-save-cancel-edits" class="center pad-top-2 hide-on-load">
	<span id="save-edits" class="sqr-btn green">Save</span>
	<span id="cancel-edits">Cancel</span>
</div>
<div class="center">
	<div id="dates-container" class="page-section pad-top">
		<h3 class="h3 green">Edit Job Work Days</h3>
		<div id="affected-employees-html"></div>
		<div class="row">			
			<div class="v2 sz-med item calendar-container teal-title">
				<div id="workDaysCalendar_postJob" class="calendar" data-is-showing-job="0">
				</div>			
			</div>
		</div>		
		<div id="work-days-to-add" class="pad-top-2"></div>	
	</div>			
	<div id="times-container" class="page-section">	
		<div id="times-cont">				
			<div class="radio-container pad-top">
				<label><input id="select-all-dates" type="radio" name="set-times">
					Select all dates</label>
				<label><input id="deselect-all-dates" type="radio" name="set-times">
					Deselect all dates</label>
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
	<div id="employee-schedule-container" class="width-500 page-section pad-top">
		<c:choose>
			<c:when test="${users_employees.size() > 0 }">
				<div id="select-an-employee">
					<h3 class="h3 green">Please select an employee to remove</h3>
					<div id="employees">
						<c:forEach items="${users_employees }" var="user">
							<p data-user-id="${user.userId }">${user.firstName } ${user.lastName }</p>
						</c:forEach>
					</div>
				</div>
				<div id="verify-removal" class=" hide-on-load">
					<h4 class="h4"><span class="employee-name"></span> will be removed from all unfinished work days that he was assigned to work.</h4>
					<h4 class="h4">Are you sure you want to remove <span class="employee-name"></span>?</h4>

					<div class="pad-top">
						<span id="confirm-employee-removal" class="sqr-btn green">Yes</span>
					</div>
<!-- 					<span id="cancel-employee-removal" class="">Cancel</span> -->
				</div>
<!-- 				<div class="v2 sz-med item hide-on-load calendar-container teal-title"> -->
<!-- 					<h3 class="h3 green">Edit <span id="employee-name"></span>'s Schedule</h3> -->
<!-- 					<div id="employee-work-days" class="calendar pad-top" data-is-showing-job="0"> -->
<!-- 					</div>			 -->
<!-- 				</div> -->
			</c:when>
			<c:otherwise>
				<p>There are currently no employees for this job</p>
			</c:otherwise>
		</c:choose>		
	</div>
</div>
<div id="json_work_day_dtos">${json_work_day_dtos }</div>

<%@ include file="../includes/Footer.jsp"%>