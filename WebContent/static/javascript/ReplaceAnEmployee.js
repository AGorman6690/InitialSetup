var workDayDtos = [];
var workDayDtos_original = [];

$(document).ready(function() {
	
	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	$.extend(true, workDayDtos_original, workDayDtos);
	
	var dates = getDatesFromWorkDayDtos(workDayDtos, dates);
	var numberOfMonths = getMonthsSpan(dates);
	var $calendar = $("#workDaysCalendar_postJob");
	var $calendar_times = $("#select-times-cal");
	
	initCalendar_selectWorkDays($calendar, $calendar_times, numberOfMonths);
	initCalendar_setStartAndEndTimes($calendar_times);
	
	$("body").on("click", "#approve-work-day-removal", function() {
		
		var editJobDto = {};
		editJobDto.proposalDto = {};
		editJobDto.jobId = $("#jobId").val();
		editJobDto.newWorkDays = getWorkDays_forAjaxRequest(workDayDtos);		
		editJobDto.proposalDto.days_offerExpires = $("input#days").val();
		editJobDto.proposalDto.hours_offerExpires = $("input#hours").val();
		editJobDto.proposalDto.minutes_offerExpires = $("input#minutes").val();

		executeAjaxCall_saveEdits_dates(editJobDto);
	})
	
	$("#select-an-employee-cont p").click(function(){
		$("#post-select").show();
		
		$("#select-an-employee-cont [data-toggle-id").click();
		
		var $e = $("#employee-to-replace-name");
		$e.html($(this).html());
		$e.attr("data-user-id", $(this).attr("data-user-id"));
	})	
	
	$("#send-request button").click(function() {
		var jobId = $("#jobId").val();
		var userId_employeeToReplace = $("#employee-to-replace-name").attr("data-user-id");
		executeAjaxCall_replaceAnEmployee(jobId, userId_employeeToReplace);
	})
	
	$("#edit-times").click(function(){
//		var $calendar_times = $("#select-times-cal");
//		initCalendar_setStartAndEndTimes($calendar_times, workDayDtos);
//		
	})
	
	$("#save-edits").click(function() {
		var jobId = $("#jobId").val();
		var $calendar = $("#workDaysCalendar_postJob");
//		if($("#edit-dates").hasClass("selected")){
			
			// ****************************************************
			// ****************************************************
			// If removing dates, then give a warning message to the employer
			// explaining that all agreements previously reached, that are affected
			// by the removal, will need to be re-accepted by the employee.
			// ****************************************************
			// ****************************************************
			if(areOriginalWorkDaysBeingRemoved()){
				var datesOriginal = getSelectedDates($calendar, "yy-mm-dd", "original");
				var datesRemoved = [];
				$(datesOriginal).each(function(i, dateString) {
					var td = getTdByDate($calendar, dateify(dateString)); 
					if($(td).hasClass("not-selected")) datesRemoved.push(dateString);
				})

				executeAjaxCall_beforeSaveEdits_verifyAffectedEmployees(jobId, datesRemoved);
			}else{
				var editJobDto = {};			
				editJobDto.jobId = $("#jobId").val();
				editJobDto.newWorkDays = getWorkDays_forAjaxRequest(workDayDtos);					
				executeAjaxCall_saveEdits_dates(editJobDto);	
			}
			
//		}
		
	})
	
//	setTimeOptions($("#single-start-time"), 30);
//	setTimeOptions($("#single-end-time"), 30);
	setTimeOptions($("#multiple-start-times"), 30);
	setTimeOptions($("#multiple-end-times"), 30);
//	setTimeOptions($("#startTime-singleDate"), 30);
//	setTimeOptions($("#endTime-singleDate"), 30);
//	setTimeOptions($("#endTime-singleDate"), 30);	
	

})

function areOriginalWorkDaysBeingRemoved() {
	if($("#workDaysCalendar_postJob td.original.not-selected").length > 0) return true;
	else return false;
}
function getWorkDayDtos() {
	return workDayDtos;
}

function getWorkDays_forAjaxRequest(workDayDtos){
	
	// **************************************
	// This workaround exists because I cannot successfully pass
	// workDay objects when its date (Local Date type) is present.
	// **************************************
	
	var workDays = [];
	
	$(workDayDtos).each(function(){
		
		var workDay = {};
		workDay.stringDate = $.datepicker.formatDate("yy-mm-dd", this.date);
		workDay.stringStartTime = this.workDay.stringStartTime;
		workDay.stringEndTime = this.workDay.stringEndTime;
		
		workDays.push(workDay);	
	})

	return workDays;
}
function executeAjaxCall_beforeSaveEdits_verifyAffectedEmployees(jobId, datesRemoved){
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: "/JobSearch/job/" + jobId + "/edit/work-days/pre-process",
		contentType: "application/json",
		data: JSON.stringify(datesRemoved),
		dataType: "html",
		success: function(html) {
			
			$("#remove-work-days-affected-employees").html(html);
			
			var affectedEmployees = $("#remove-work-days-affected-employees #employees li");
		
			if(affectedEmployees.length >0){
				$("#main-save-cancel-edits").hide();

			// Else there are no affected employees.
			// Make changes.
			}else{
				
				var editJobDto = {};			
				editJobDto.jobId = $("#jobId").val();
				editJobDto.newWorkDays = getWorkDays_forAjaxRequest(workDayDtos);					
				
				executeAjaxCall_saveEdits_dates(editJobDto);				
				
			}
		},
		error: function() {			
		}
	})
}
function executeAjaxCall_saveEdits_dates(editJobDto) {
	

	
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: "/JobSearch/job/edit/work-days",
		contentType: "application/json",
		data: JSON.stringify(editJobDto),
		dataType: "text",
		success: function(response) {
			location.reload();
		},
		error: function(response) {
			
		}
	})
	
	
}
