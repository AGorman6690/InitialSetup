var workDayDtos = [];
var filterJobDto = {};

$(document).ready(function(){	
	setStates();	
	
	$("#get-jobs").click(function() {	
//		filterJobDto = getFilterJobDto();
		var paramString = getParamString();				
	})
	
	$(".approve-filter").click(function() {
		$(this).closest(".filter").find(".remove-filter").show();
		var $e = $(this).closest(".dropdown-style"); 
		$e.attr("data-is-approved", 1);
		$e.removeClass("always-show");
		$e.hide();
	})
	
	$(".remove-filter").click(function() {
		var $e = $(this).closest(".dropdown-style"); 
		$e.attr("data-is-approved", 0);
		$(this).hide();
	})
	
	$(".time").each(function(){
		setTimeOptions($(this), 60);
	})
	
	initCalendar_selectSingleDate($("#end-date-filter .calendar"));
	initCalendar_selectSingleDate($("#start-date-filter .calendar"));	
	initCalendar_selectWorkDays($("#work-days-filter .calendar"), undefined, 1);
})
function getParamString() {
	
	var dates = [];
	var paramString = "";
	var isBefore;
	
	paramString = "?fromAddress = " + getAddress();
	paramString += "&radius" + $("#miles").val();
	
	if($("#start-date-filter").attr("data-is-approved") == 1){
		isBefore = $("#start-date-filter .radio-container").find("input:checked").attr("data-is-before");
		dates = getSelectedDates($("#start-date-filter .calendar"), "yy-mm-dd", "selected");
		paramString += "&startDate=" + dates[0];
		paramString += "&beforeStartDate=" + isBefore;
		
	}
	
	if($("#end-date-filter").attr("data-is-approved") == 1){
		isBefore = $("#end-date-filter .radio-container").find("input:checked").attr("data-is-before");
		dates = getSelectedDates($("#end-date-filter .calendar"), "yy-mm-dd", "selected");
		paramString += "&endDate=" + dates[0];
		paramString += "&beforeEndDate=" + isBefore;
		
	}
	
	if($("#start-time-filter").attr("data-is-approved") == 1){
		isBefore = $("#start-time-filter .radio-container").find("input:checked").attr("data-is-before");
		time = $("#start-time-filter select").find("option:selected").attr("data-filter-value");
		paramString += "&startTime=" + time;
		paramString += "&beforeStartTime=" + isBefore;
		
	}	
	
	if($("#end-time-filter").attr("data-is-approved") == 1){
		isBefore = $("#end-time-filter .radio-container").find("input:checked").attr("data-is-before");
		time = $("#end-time-filter select").find("option:selected").attr("data-filter-value");
		paramString += "&endTime=" + time;
		paramString += "&beforeEndTime=" + isBefore;
		
	}
	return paramString;	
	
}

function getAddress(){	

	var arr = [];
	var city = "";
	var state = "";
	var zipCode = "";
	
	city = $("#city").val();
	state = $("#state").val()
	zipCode = $("#zip").val()
	
	arr.push(city);
	arr.push(state);
	arr.push(zipCode);
	
	return buildStringFromArray(arr);
}