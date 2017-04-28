var workDayDtos = [];
var workDayDtos_original = [];
var g_jobId;
var g_clickedUser;
$(document).ready(function() {
	
	g_jobId = $("#jobId").val();
	

	

//	initCalendar_selectWorkDays($calendar, $calendar_times, numberOfMonths);
//	initCalendar_setStartAndEndTimes($calendar_times);
	
	$("#edit-options button").click(function() {
		$("#edit-options").hide();
		$("#main-save-cancel-edits").show();
	})
	
	$("#cancel-edits").click(function() {
		$("#edit-options").show();
		$("#main-save-cancel-edits").hide();
		$(".page-section").hide();
		$("#workDaysCalendar_postJob").datepicker("destroy");
		$("#employee-work-days").datepicker("destroy");
		$("#select-an-employee").show();
		$("#employee-schedule-container .calendar-container").hide();
		$("#affected-employees-html").empty();
		$("#verify-removal").hide();
	})
	
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
	
	$("#edit-dates").click(function() {
		
		workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
		$.extend(true, workDayDtos_original, workDayDtos);
		
		var dates = getDatesFromWorkDayDtos(workDayDtos, dates);
		var numberOfMonths = getMonthsSpan(dates);
		var firstDate = getMinDateFromDateArray(dates);
		var $calendar = $("#workDaysCalendar_postJob");
		var $calendar_times = $("#select-times-cal");		
		initCalendar_editWorkDays($calendar, firstDate, numberOfMonths)		
	})

//	$("#select-an-employee-cont p").click(function(){
//		$("#post-select").show();
//		
//		$("#select-an-employee-cont [data-toggle-id").click();
//		
//		var $e = $("employee-to-replace-name");
//		$e.html($(this).html());
//		$e.attr("data-user-id", $(this).attr("data-user-id"));
//		
//
//	})	
	$("#cancel-employee-removal").click(function() {
		$("#edit-options").show();
		$("#employee-schedule-container").hide();
		$("#verify-removal").hide();
		$("#select-an-employee").show();
		
	})
	
	$("#send-request button").click(function() {
		
		var userId_employeeToReplace = $("#employee-to-replace-name").attr("data-user-id");
		executeAjaxCall_replaceAnEmployee(g_jobId, userId_employeeToReplace);
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
			

			if(areOriginalWorkDaysBeingRemoved()){
				var datesOriginal = getSelectedDates($calendar, "yy-mm-dd", "original");
				var datesRemoved = [];
				$(datesOriginal).each(function(i, dateString) {
					var td = getTdByDate($calendar, dateify(dateString)); 
					if($(td).hasClass("not-selected")) datesRemoved.push(dateString);
				})

				executeAjaxCall_editJob_deleteWorkDays(jobId, datesRemoved);
			}else{
				var editJobDto = {};			
				editJobDto.jobId = $("#jobId").val();
				editJobDto.newWorkDays = getWorkDays_forAjaxRequest(workDayDtos);					
				executeAjaxCall_saveEdits_dates(editJobDto);	
			}
			
//		}
		
	})
	
	$("#confirm-employee-removal").click(function () {
		executeAjaxCall_removeEmployee(g_jobId, g_clickedUser);
	})
	
	$("#employees p").click(function() {
		
		$(".employee-name").html($(this).html());
		$("#select-an-employee").hide();
		
		g_clickedUser = $(this).attr("data-user-id");
		
		$("#verify-removal").show();
//		executeAjaxCall_getEmployeesWorkDays(g_jobId, userId);
	})
	
//	setTimeOptions($("#single-start-time"), 30);
//	setTimeOptions($("#single-end-time"), 30);
	setTimeOptions($("#multiple-start-times"), 30);
	setTimeOptions($("#multiple-end-times"), 30);
//	setTimeOptions($("#startTime-singleDate"), 30);
//	setTimeOptions($("#endTime-singleDate"), 30);
//	setTimeOptions($("#endTime-singleDate"), 30);	
	

})

function executeAjaxCall_removeEmployee(jobId, userId) {
	$.ajax({
		type: "POST",
		url: "/JobSearch/job/" + jobId + "/user/" + userId + "/remove-remaining-work-days",
		headers: getAjaxHeaders(),
		dataType: "text"
	}).done(function(response) {
		location.reload(true);
	})
}
function executeAjaxCall_getEmployeesWorkDays(jobId, userId){
	
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: "/JobSearch/job/" + jobId + "/employee/" + userId + "/work-days",
		headers: getAjaxHeaders(),
		dataType: "json",
	}).done(function(workDayDtos_response) {
	
		broswerIsWaiting(false);
		workDayDtos = workDayDtos_response;
		dateifyWorkDayDtos(workDayDtos);
		
		var $calendar = $("#employee-work-days")
		$calendar.datepicker("destroy");
		$calendar.closest(".calendar-container").show();
		
		$.extend(true, workDayDtos_original, workDayDtos);
		var dates = getDatesFromWorkDayDtos(workDayDtos, dates);
		var numberOfMonths = getMonthsSpan(dates);
		var firstDate = getMinDateFromDateArray(dates);
		initCalendar_editEmployeeSchedule($calendar, firstDate, numberOfMonths)
	})
}
function initCalendar_editWorkDays($calendar, firstDate, numberOfMonths){
	
	
	
	$calendar.datepicker({
		minDate: new Date(),
		numberOfMonths: numberOfMonths, 
		onSelect: function(dateText, inst) {	   
			var date = new Date(dateText);
			if(doesWorkDayDtoArrayContainDate(date, workDayDtos)){
				workDayDtos = removeWorkDayDto(date, workDayDtos);						
			}else{
				workDayDtos.push(getNewWorkDayDto(date));
			}
			
			if(doesWorkDayDtoArrayContainDate(date, workDayDtos_original)){
				var originalDates = getSelectedDates($calendar, "yy-mm-dd", "original");
				var originalDates_notSelected = [];
				$(originalDates).each(function() {
					var originalDate = dateify(this);
					
					// This first condition is here because the original date that **WAS CLICKED**
					// will not yet have the class "not-selected" because the beforeShowDay() event
					// happens **AFTER** the onSelect() event.
					// Thus if the clicked date **DOES** have the "not-selected" class at this point,
					// then the user is re-selecting this original date.
					if(originalDate.getTime() == date.getTime()){
						var td = getTdByDate($calendar, originalDate);						
						if(!$(td).hasClass("not-selected")) originalDates_notSelected.push(this);	

					}
					else{
						var td = getTdByDate($calendar, originalDate);						
						if($(td).hasClass("not-selected")) originalDates_notSelected.push(this);	
					}
					
				})				
				if(originalDates_notSelected.length > 0)
					executeAjaxCall_removeDate_getAffectedEmployees(g_jobId, originalDates_notSelected);
				else
					$("#affected-employees-html").empty();
			}
		},		        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {       
        	var className = "";
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos_original)) className += "original";
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) className += " active111";
        	else className += " not-selected";
        	
        	return [true, className];
        
     	},
		defaultDate: firstDate
    });	
}
function initCalendar_editEmployeeSchedule($calendar, firstDate, numberOfMonths){

	$calendar.datepicker({
		minDate: new Date(),
		numberOfMonths: numberOfMonths, 
		onSelect: function(dateText, inst) {			
			var date = new Date(dateText);
			if(doesWorkDayDtoArrayContainDate(date, workDayDtos_original)){
				if(doesWorkDayDtoArrayContainDate(date, workDayDtos)){
					workDayDtos = removeWorkDayDto(date, workDayDtos);						
				}else{
					workDayDtos.push(getNewWorkDayDto(date));
				}	
			}
		},		        
        beforeShowDay: function (date) {       
        	var className = "";
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos_original)) className += "original";
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) className += " active111";
        	else className += " not-selected";
        	
        	return [true, className];        
     	},
		defaultDate: firstDate
    });	
}
function executeAjaxCall_editJob_deleteWorkDays(jobId, dateStrings_toDelete){
	$.ajax({
		type: "POST",
		url: "/JobSearch/job/" + jobId + "/delete-work-days",
		headers: getAjaxHeaders(),
		contentType: "application/json",
		data: JSON.stringify(dateStrings_toDelete),
		dataType: "text",
	}).done(function(response) {
		location.reload(true);
	})	
}
function executeAjaxCall_removeDate_getAffectedEmployees(jobId, originalDates_notSelected){
	$.ajax({
		type: "POST",
		url: "/JobSearch/job/" + jobId + "/employees/by-work-days",
		headers: getAjaxHeaders(),
		contentType: "application/json",
		data: JSON.stringify(originalDates_notSelected),
		dataType: "html"
	}).done(function(html) {
		var $e = $("#affected-employees-html");
		$e.show();
		$e.html(html);
	})
}
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
