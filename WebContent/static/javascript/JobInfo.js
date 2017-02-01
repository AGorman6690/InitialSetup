	
var workDays = [];

$(document).ready(function() {
	
	
	$("#jobAddress").click(function(){
// **********************
// 			http://stackoverflow.com/questions/6582834/use-a-url-to-link-to-a-google-map-with-a-marker-on-it
// **********************
		var lat = $("#map").attr("data-lat");
		var lng = $("#map").attr("data-lng");
		var win = window.open("https://www.google.com/maps/place/" + lat + "+" + lng + "/@" + lat + "," + lng + ",15z", "_blank");
		win.focus();
	})
	
	setWorkDays();
	initCalendar_JobInfo();
})

function setWorkDays(){
	
	var dates = [];
	
	$("#workDays").find("div").each(function(){
		var dateString = $(this).attr("data-date").replace(/-/g, "/");
		workDays.push(new Date(dateString));
	})
	
	
}

function initCalendar_JobInfo(){
	
	var $calendar = $("#workDaysCalendar");
		
	var numberOfMonths = getNumberOfMonths($calendar);
	var milliseconds;
	var date;
	
	var minDate = $calendar.attr("data-min-date").replace(/-/g, "/");

	$calendar.datepicker({
		minDate: new Date(minDate),
		numberOfMonths: numberOfMonths, 
        beforeShowDay: function (date) {

			if(isDateAlreadySelected(date, workDays)) return [true, "active111"];
			else return [true, ""];
        	
        }
    });
}

