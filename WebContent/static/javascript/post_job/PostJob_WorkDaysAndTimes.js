

$(document).ready(function() {
	
	var $calendar_times = $("#select-times-cal");
	
	$("#times-cont #select-all-dates").change(function() {
		
		var allTds_toBeSet = getSelectedDates($calendar_times, "yy-mm-dd", "a-date-to-set");

		$(allTds_toBeSet).each(function(){
			var date = dateify(this)
			var td = getTdByDate($calendar_times, date);
			
			if($(td).hasClass("selected-date") == 0) $(td).addClass("selected-date");
			
			var workDayDto = getWorkDayDtoByDate_new(date, workDayDtos);
			workDayDto.isSelected = true;
		})
	
	})
	$("#times-cont #deselect-all-dates").change(function() {		
		deselectAllWorkDays();	
	})	

	$("#apply-multiple-times").click(function() {
		
		var value_startTime = $("#multiple-start-times").find("option:selected").attr("data-filter-value");
		var value_endTime = $("#multiple-end-times").find("option:selected").attr("data-filter-value");
		
		var selectedDateStrings = getSelectedDates($calendar_times, "yy-mm-dd", "selected-date");
		$(selectedDateStrings).each(function() {		
			
			var workDayDto = getWorkDayDtoByDate_new(dateify(this), workDayDtos);
			workDayDto.workDay.stringStartTime = value_startTime;
			workDayDto.workDay.stringEndTime = value_endTime;
		})
		
		deselectAllWorkDays();
		$calendar_times.datepicker("refresh");
	})
	
	$("#times-are-the-same").click(function(){
		
		if($(this).hasClass("gray")){
			$("#set-one-start-and-end-time").show();
			$("#times-cont").hide();
			highlightArrayItem(this, $("#initial-time-question").find("button"), "selected");
		}

	})
	
	$("#times-are-not-the-same").click(function(){
		
		if($(this).hasClass("gray")){
			$("#set-one-start-and-end-time").hide();
			$("#times-cont").show();
			highlightArrayItem(this, $("#initial-time-question").find("button"), "selected");
		}

	})		
	
})

function deselectAllWorkDays(){
	var allTds_toBeSet = getSelectedDates($calendar_times, "yy-mm-dd", "a-date-to-set");
	$(allTds_toBeSet).each(function(){
		var date = dateify(this)
		var td = getTdByDate($calendar_times, date);
		if($(td).hasClass("selected-date") == 1) $(td).removeClass("selected-date");
		
		var workDayDto = getWorkDayDtoByDate_new(date, workDayDtos);
		workDayDto.isSelected = false;
	})	
}