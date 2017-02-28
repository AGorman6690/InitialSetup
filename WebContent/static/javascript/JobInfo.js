	
var workDays = [];
var apply_selectedWorkDays = [];

$(document).ready(function() {
	
	
	$("body").on("click", "#jobAddress", function(){
// **********************
// 			http://stackoverflow.com/questions/6582834/use-a-url-to-link-to-a-google-map-with-a-marker-on-it
// **********************
		var lat = $("#map").attr("data-lat");
		var lng = $("#map").attr("data-lng");
		var win = window.open("https://www.google.com/maps/place/" + lat + "+" + lng + "/@" + lat + "," + lng + ",15z", "_blank");
		win.focus();
	})
	
	
	$("#content_jobInfo").click(function(){
		
		if($("#map").attr("data-is-init") != 1){
			initMap();
			$("#map").attr("data-is-init", "1");	
		}
		
	})

	
	setWorkDays();
	initCalendar_JobInfo();
	initCalendar_Apply_SelectWorkDays();
})


function isCalendarInDOM_applicantSelectWorkDays(){
	
	// If the job is accepting partial availability, the calendar will be in the DOM
	var $calendar = $("#applyContainer").find("#apply_selectWorkDays.calendar").eq(0);
	if($calendar.length > 0) return true;
	else return false;
}

function initCalendar_Apply_SelectWorkDays(){

	if(isCalendarInDOM_applicantSelectWorkDays()){
		
		var $calendar = $("#apply_selectWorkDays");
		var numberOfMonths = getNumberOfMonths($calendar);	
		if(numberOfMonths > 2) numberOfMonths = 2;
		
		var minDate = new Date($calendar.attr("data-min-date").replace(/-/g, "/"));
		if(isNaN(minDate.getTime()) == true) minDate = new Date();
		
		$calendar.datepicker({
			minDate: minDate,
			numberOfMonths: numberOfMonths,
			onSelect: function(dateText, inst){
				
				// The applicant can only select a valid work day
				if(doesDateArrayContainDate(new Date(dateText), workDays)){
					apply_selectedWorkDays = onSelect_multiDaySelect_withRange(
							dateText, apply_selectedWorkDays);	
				}
				
			},
	        beforeShowDay: function (date) {

				if(isDateAlreadySelected(date, apply_selectedWorkDays)) return [true, "active111 apply-selected-work-day"];
				else if(isDateAlreadySelected(date, workDays)) return [true, "active111"];
				else return [true, ""];
	        	
	        }
	    });		
		
	}
	
}


function setWorkDays(){
	
	var dates = [];
	
	$("#workDays").find("div").each(function(){
		var dateString = $(this).attr("data-date").replace(/-/g, "/");
		workDays.push(new Date(dateString));
	})
	
	
}

function initCalendar_JobInfo(){
	
	var $calendar = $("#workDaysCalendar");
	
	if($calendar.length > 0 ){
		
		var numberOfMonths = getNumberOfMonths($calendar);	
		if(numberOfMonths > 2) numberOfMonths = 2;
		
		var minDate = new Date($calendar.attr("data-min-date").replace(/-/g, "/"));
		if(isNaN(minDate.getTime()) == true) minDate = new Date();
		
		$calendar.datepicker({
			minDate: minDate,
			numberOfMonths: numberOfMonths, 
	        beforeShowDay: function (date) {

				if(isDateAlreadySelected(date, workDays)) return [true, "active111"];
				else return [true, ""];
	        	
	        }
	    });		
	}

}

