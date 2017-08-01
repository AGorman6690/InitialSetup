

$(document).ready(function(){
	initCalendar_employmentSummary($("#employment-summary-calendar .calendar"));
	
	$("#personal-info .value").click(function() {
		$(this).closest(".info-item").find(".edit-container").eq(0).toggle();
	})
	
	$("#save-home-location").click(function(){		
		updateHomeLocation();
	})	
	$("#save-max-distance").click(function(){		
		updateMaxWorkRadius();
	})	
	$("#save-about").click(function(){		
		updateAbout();
	})		
	
})
function updateHomeLocation(){	
	var city = $("#city").val();;
	var state = $("#state option:selected").val();
	var zip = $("#zipCode").val();
	var url = "/JobSearch/user/update-home-location?city=" + city + "&state=" + state + "&zip=" + zip;
	executeAjaxCall_updateUserSettings(url);
}
function updateMaxWorkRadius(){	
	maxWorkRadius = $("#miles").val();
	var url = "/JobSearch/user/update-max-work-radius?maxWorkRadius=" + maxWorkRadius;
	executeAjaxCall_updateUserSettings(url);
}
function updateAbout(){	
	about = $("#about").val();
	var url = "/JobSearch/user/update-about?about=" + about;
	executeAjaxCall_updateUserSettings(url);
}
function executeAjaxCall_updateUserSettings(url){	

	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: url,
		headers : getAjaxHeaders(),
		dataType : "text",	
	}).done(function _success(response) {
		broswerIsWaiting(false);	
		location.reload();
	})
}
function getCalendarDays(){
	var calendarDays = [];
	$("#calendar-days .calendar-day").each(function() {
		var calendarDay = {};
		calendarDay.date = $(this).attr("data-date");
		
		calendarDay.jobs= [];
		$(this).find(".job").each(function(i, e) {
			var job= {};
			job.workDay = {};
			job.jobName = $(e).attr("data-job-name");
			job.workDay.startTime = $(e).attr("data-start-time");
			job.workDay.endTime = $(e).attr("data-end-time");
			calendarDay.jobs.push(job);
		})
		
		calendarDays.push(calendarDay);
		
	})
	
	return calendarDays;
	
}

	function initCalendar_employmentSummary($calendar){

		var firstDate = getMinDate($calendar)

		$calendar.datepicker({
			minDate: new Date(),
			numberOfMonths: getNumberOfMonths($calendar),
			onSelect: function(dateText, inst){			

			},
			beforeShowDay: function(date){			
				return [true, ""];
			},
			afterShow: function(){
				
				var calendarDays = getCalendarDays();
				

				
				$(calendarDays).each(function(i, calendarDay){				
					var td = getTdByDate($calendar, dateify(calendarDay.date));
					
					var html = "<div class='jobs'>";

					$(calendarDay.jobs).each(function(j, job) {
						html += "<div class='job'>";
						html += "<p class='job-name'>" + job.jobName + "</p>";
						html += "<p class='start-time'>" + formatTimeTo12Hours(job.workDay.startTime) + "</p>";
						html += "<p class='end-time'>" + formatTimeTo12Hours(job.workDay.endTime) + "</p>";						
						html += "</div>";
					})					
					html += "</div>";
					
					$(td).append(html);
					if(calendarDay.jobs.length > 0) $(td).addClass("has-employment");
					
					var $tr = $(td).closest("tr");
					if($tr.hasClass("show-row") == 0) $tr.addClass("show-row"); 
				})				
			}
		})
		
		$calendar.datepicker("setDate", firstDate);	
	}
	
