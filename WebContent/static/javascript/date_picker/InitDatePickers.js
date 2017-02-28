$(document).ready(function(){
	
	$("html").on("click", ".availability-calendar-container", function(){
		
		var $calendarContainer = $(this).find(".calendar-container").eq(0);
		var $calendar = $calendarContainer.find(".calendar").eq(0);
		
		// Toggle calendar visibility
		if($calendarContainer.is(":visible")){
			$calendarContainer.hide();
		}
		else{
			
			// Only set the calendar once
			if($calendar.hasClass("hasDatepicker") == 0){
				
				// Get the available dates
				var dates_available = [];
				$(this).find(".dates-available div[data-date]").each(function(){
					dates_available.push(new Date($(this).attr("data-date").replace(/-/g, "/")));
				})
				
				var dates_inQuestion = [];
				$(this).find(".dates-in-question div[data-date]").each(function(){
					dates_inQuestion.push(new Date($(this).attr("data-date")));
				})
				
				initCalendar_employeeAvailability($calendar, dates_available, dates_inQuestion);
				
				$calendar.datepicker("setDate", new Date($calendar.attr("data-first-date").replace(/-/g, "/")));
			}		
			
			$calendarContainer.show();
		}

		toggleClasses($(this).find(".glyphicon").eq(0), "glyphicon-menu-up", "glyphicon-menu-down");
		
		
	})


})


function initCalendar_employeeAvailability($calendar, dates_employeeAvailability, dates_inQuestion){
	
	
	$calendar.datepicker({
		numberOfMonths: parseInt(getNumberOfMonths($calendar)),
		 beforeShowDay: function (date) {
			 return beforeShowDay_findEmployees_ifUserHasAvailability(
					 						date, dates_inQuestion, dates_employeeAvailability);
		 }
	})
	
}