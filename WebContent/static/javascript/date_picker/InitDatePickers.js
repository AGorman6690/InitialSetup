$(document).ready(function(){
	
	
	// ****************************************************
	// Is this the proper place for this initialization???
	// Should it be placed with the page's js file???
	// ****************************************************
//	$("html").on("click", ".availability-calendar-container", function(){
//		
//		var $calendarContainer = $(this).find(".calendar-container").eq(0);
//		var $calendar = $calendarContainer.find(".calendar").eq(0);
//		
//		// Toggle calendar visibility
//		if($calendarContainer.is(":visible")){
//			$calendarContainer.hide();
//		}
//		else{
//			
//			// Only set the calendar once
//			if($calendar.hasClass("hasDatepicker") == 0){
//			
//				// Get the available dates
//				var dates_available = [];
//				$(this).find(".dates-available div[data-date]").each(function(){
//					dates_available.push(new Date($(this).attr("data-date").replace(/-/g, "/")));
//				})
//				
//				// Get the dates in question (i.e. the dates that can be available)
//				var dates_inQuestion = [];
//				$(this).find(".dates-in-question div[data-date]").each(function(){
//					dates_inQuestion.push(new Date($(this).attr("data-date")));
//				})		
//				
//				initCalendar_showAvailability($calendar, dates_available, dates_inQuestion);
//				
//				$calendar.datepicker("setDate", new Date($calendar.attr("data-first-date").replace(/-/g, "/")));
//			}		
//			
//			$calendarContainer.show();
//		}
//
//		toggleClasses($(this).find(".glyphicon").eq(0), "glyphicon-menu-up", "glyphicon-menu-down");
//		
//		
//	})


})


function getDateFromContainer($container){
	
	var dates = [];
	var date;
	$container.find("[data-date]").each(function(){
		
		date = dateify($(this).attr("data-date"));		
		if(date != undefined) dates.push(date);		
	})
	
	return dates;
	
}



function initCalendar_selectAvailability($calendar, currentAvailability, workDays){
		
	
	
	var numberOfMonths = getNumberOfMonths($calendar);	
	var minDate = getMinDate($calendar);
	
	$calendar.datepicker({
		minDate: minDate,
		numberOfMonths: numberOfMonths,
		onSelect: function(dateText, inst){
			
			// The user can only select a valid work day
			if(doesDateArrayContainDate(new Date(dateText), workDays)){
				currentAvailability = onSelect_multiDaySelect_withRange(
						dateText, currentAvailability);	
			}
			
		},
        beforeShowDay: function (date) {

			if(isDateAlreadySelected(date, currentAvailability)) return [true, "active111 apply-selected-work-day"];
			else if(isDateAlreadySelected(date, workDays)) return [true, "active111"];
			else return [true, ""];
        	
        }
    });		

}


function initCalendar_showAvailability($calendar, dates_application, dates_job){
	
	$calendar.datepicker({
		minDate: getMinDate($calendar),
		numberOfMonths: parseInt(getNumberOfMonths($calendar)),
		beforeShowDay: function (date) {
			 return beforeShowDay_findEmployees_ifUserHasAvailability(
					 						date, dates_job, dates_application);
		 }
	})
	
}

function initCalendar_counterApplicationDays($calendar, dates_application, dates_job, dates_unavailable){


	
	$calendar.datepicker({
		minDate: getMinDate($calendar),
		numberOfMonths: parseInt(getNumberOfMonths($calendar)),
		onSelect: function(dateText, inst){
			
			if($(inst.input).closest(".calendar-container").hasClass("read-only") == 0){
				var date = dateify(dateText);
				
				if(doesDateArrayContainDate(date, dates_unavailable)){}
				else if(doesDateArrayContainDate(date, dates_application)){				
					dates_application = removeDateFromArray(date, dates_application);				
				}	
				else if(doesDateArrayContainDate(date, dates_job)){				
					dates_application.push(date);				
				}
			}
			
		},
		beforeShowDay: function (date) {
			 return beforeShowDay_counterApplicationDays(
					 						date, dates_application, dates_job, dates_unavailable);
		 }
	})
	
}