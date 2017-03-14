var daysOfWeekToSelect = [];
var monthsToSelect = [];
var selectedDays = [];

$(document).ready(function(){

	setSelectedDays_OnLoad();
	
	$("#calendarContainers").find(".calendar").each(function(){

		var minDate = new Date($(this).attr("data-min-date"));
		var month = minDate.getMonth();
		
		var today = new Date();
		
		// This if statement is here for debugging.
		// Need to determine how we want to handle months that have passed.
		if(minDate.getMonth() >= today.getMonth()){
			$(this).datepicker({
				minDate: minDate,
				numberOfMonths: 1, 
				changeMonth: false,
				hideIfNoPrevNext: true,
				onSelect: function(dateText, inst) {	    
					onSelect_Availability(dateText);
				},		        
		        beforeShowDay: function (date) {       
		        	return beforeShowDay_Availability(month, date, daysOfWeekToSelect);	
		     	}
		    });				
		}
		

	})
	
	$("#monthsContainer .options input[type=checkbox]").change(function(){
		// *** FYI: *** the "select all months" option triggers this event from the Checkboxes.js
		
			
			var calendarId = $(this).attr("data-cal-id");
			var $calendar = $("#" + calendarId);
			var month = $(this).attr("data-month");
			
			// Show or hide the calendar
			// Add or remove month number from array
			if($(this).is(":checked")){
				$calendar.css("display", "inline-block");
				monthsToSelect = attemptToAddValueToArray(month, monthsToSelect);
			}
			else{
				$calendar.css("display", "none");
				monthsToSelect = removeValueFromArray(month, monthsToSelect);
			}
			
			// If there is at least one month selected,
			// allow the user to set his/her availability
			if($("#monthsContainer").find(".options input[type=checkbox]:checked").length > 0){
				$("#setAvailability").removeClass("disabled");
			}
			else{
				$("#setAvailability").addClass("disabled");
			}
			
			
			// Set html
			if($("#monthsContainer").find(".options input[type=checkbox]:checked").length > 1){
				$("#daysContainer h5").html("Use the calendars to select the days you are available");
			}
			else{
				$("#daysContainer h5").html("Use the calendar to select the days you are available");
			}
//		}
		
	})
	
	$("#daysContainer .options input[type=checkbox]").change(function(){
		
		var dayOfWeek = $(this).attr("data-day-of-week");
		var isDaySelected = $(this).is(":checked")
		if(isDaySelected){
			daysOfWeekToSelect = attemptToAddValueToArray(dayOfWeek, daysOfWeekToSelect);
		}
		else{
			daysOfWeekToSelect = removeValueFromArray(dayOfWeek, daysOfWeekToSelect);
		}
		
		// *****************************************
		// On "Select All Days", these two events can be called up to 7 times
		// when in reality they only need to be called only once.
		// Revisit this once this logic matures.
		// *****************************************		
		
		updateSelectedDates_ByWeekDay(isDaySelected, dayOfWeek);
		refreshCalendars_Visible();
				
	})
	
	$("body").on("click" , "#setAvailability", function(){
		
		if($(this).hasClass("disabled") == 0){
	
			$("#setAvailability").addClass("disabled");
			$("#saveAvailability").removeClass("disabled");
			$("#cancel").removeClass("disabled");
					
			disableCheckboxes($("#monthsContainer"));
			
			changeSelectAllCheckbox($("#daysContainer input.select-all"), false, false);
			
			slideDown($("#availabilityCont"), 1000);
		}

	})

	
	
	$("#saveAvailability").click(function(){	
		if($(this).hasClass("disabled") == 0){
			saveAvailability();
		}
	})
	
	$("#cancel").click(function(){		
		setState_AfterAvailabilityAlterations();
	})
	
	$("#removeCurrentAvailability").change(function(){
	
		var flag;
		if($(this).is(":checked")) flag = 1;
		else flag = 0;
		
		$("#availableDays").find("div").each(function(){
			var date = new Date($(this).attr("data-date").replace(/-/g, "/"))
			setDateToBeRemovedFromDatabase(date, flag);
		})
		
		refreshCalendars_Visible();
		
		
	})
	

	
//	$("#j").click();
//	$("#f").click();
//	$("#m").click();
//	$("#monthsContainer").find(".select-all").eq(0).prop("checked", true).change();
	
	
	
})



function setState_AfterAvailabilityAlterations(){

	setSelectedDays_OnLoad();
	refreshCalendars_All();
	allowMonthSelection();
	$("#removeCurrentAvailability").prop("checked", false);
}
	

function allowMonthSelection(){
	
	$("#setAvailability").removeClass("disabled");
	$("#saveAvailability").addClass("disabled");
	$("#cancel").addClass("disabled");
	
	$("#monthsContainer").removeClass("not-checkable");
	enableCheckboxes($("#monthsContainer"));

	$("#availabilityCont").hide();	
}

function setSelectedDays_OnLoad(){
	
	selectedDays = [];
	var day;
	
	$("#availableDays").find("div").each(function(){
		day = $(this).attr("data-date").replace(/-/g, "/");
		selectedDays.push(new Date(day));
	})
	
}

function updateSelectedDates_ByWeekDay(isDaySelected, dayOfWeek){
	
	$(monthsToSelect).each(function(){		
		selectDatesBy_Month_Year_And_WeekDay(this, 2017, isDaySelected, dayOfWeek);		
	})		
}

//function updateSelectedDates(){
//	
////	$("html").addClass("waiting");	
//
//	var month;
//	
//	$(monthsToSelect).each(function(){		
//		selectDatesBy_Month_Year_And_WeekDays(this, 2017, daysOfWeekToSelect);		
//	})	
//	
////	broswerIsWaiting(false);
//}
//
//function selectDatesBy_Month_Year_And_WeekDays(month, year, weekdays){
//	
//	var iWeekday
//	var day = 1;
//	var iDate = new Date(year, month, day);
//	
//	// For each day in month
//	while(iDate.getMonth() == month){
//		iDate = new Date(year, month, day);
//		iWeekDay = iDate.getDay().toString();
//		
//		// Is the date's weekday selected 
//		if(doesArrayContainValue(iWeekDay, weekdays)){
//		
//			// If the date is already added to the database
//			// and it is currently flagged to be removed from the database,
//			// then set the flag back to 0 (i.e. do not remove from database)
//			if(isDateAlreadySavedAsAvailable(iDate)){
//				if(isDateSetToBeRemovedFromDatabase(iDate)){
//					setDateToBeRemovedFromDatabase(iDate, 0);
//				}
//			}
//			else{
//				attemptToAddDate(iDate, selectedDays);	
//			}						
//		}
//		
//		day += 1;
//	}
//}

function selectDatesBy_Month_Year_And_WeekDay(month, year, isDaySelected, weekday){
	
	var iWeekday
	var day = 1;
	var iDate = new Date(year, month, day);
	
	while(iDate.getMonth() == month){
		
		iDate = new Date(year, month, day);
		iWeekDay = iDate.getDay().toString();
		
		if(iWeekDay == weekday){
		
			if(isDaySelected){				
		
				// If the date is already added to the database
				// and it is currently flagged to be removed from the database,
				// then set the flag back to 0 (i.e. do not remove from database)
				if(isDateAlreadySavedAsAvailable(iDate)){
					if(isDateSetToBeRemovedFromDatabase(iDate)){
						setDateToBeRemovedFromDatabase(iDate, 0);
					}
				}
				else{
					attemptToAddDate(iDate, selectedDays);	
				}
			}
			else selectedDays = removeDay(iDate, selectedDays);
			
		}

		day += 1;
	}
}

function refreshCalendars_All(){
	
	$("#calendarContainers").find(".calendar-container .calendar").each(function(){
		$(this).datepicker("refresh");
	})		
	
}

function refreshCalendars_Visible(){
	
	
	$("#calendarContainers").find(".calendar-container:visible .calendar").each(function(){
		$(this).datepicker("refresh");
	})		

}

function isDateSetToBeRemovedFromDatabase(dateObject){
	
	var dateString = $.datepicker.formatDate("yy-mm-dd", dateObject);
	var div = $("#availableDays").find("div[data-date=" + dateString + "]")[0];
	
	if($(div).attr("data-do-remove") == 1) return true;
	else return false;
//	if($("#availableDays").find("div[data-date=" + dateString + "]").length > 0) return true;
//	else return false;
	
}

function isDateAlreadySavedAsAvailable(dateObject){
	
	var dateString = $.datepicker.formatDate("yy-mm-dd", dateObject);

	if($("#availableDays").find("div[data-date=" + dateString + "]").length > 0) return true;
	else return false;
	
}

function setDateToBeRemovedFromDatabase(dateObject, flag){
	
	var dateString = $.datepicker.formatDate("yy-mm-dd", dateObject);
	
	$("#availableDays").find("div[data-date=" + dateString + "]")
						.eq(0)
						.attr("data-do-remove", flag);
}
