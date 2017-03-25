var $calendar_applicationWorkDays;


$(document).ready(function() {
	
	$calendar_applicationWorkDays = $("#apply-work-days-calendar-container .calendar");
	
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

	initCalendar_jobInfo_workDays();
	initCalendar_apply_workDays();

})


function initCalendar_jobInfo_workDays(){

	var workDays = getDateFromContainer($("#work-days-calendar-container .work-days"));
	var $calendar = $("#work-days-calendar-container .calendar");
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		beforeShowDay: function(date){
			if(doesDateArrayContainDate(date, workDays)) return [true, "active111"];
			else return [true, ""];
		},
		afterShow: function(){
			var html = "";
			$(workDays).each(function(){
				
				var td = getTdByDate($calendar, this);
				
				html = "<div class='start-and-end-times'>";
				html += "<p>7:30a</p><p>5:30p</p>";
				html += "</div>"
				
				$(td).append(html);
			})				
		}
	})
	
	$calendar.datepicker("setDate", firstDate);

}

function initCalendar_apply_workDays(){
	
	if(doesApplicantNeedToSelectWorkDays()){
		
		var firstDate = getMinDate($calendar_applicationWorkDays)
		var workDays = getDateFromContainer($calendar_applicationWorkDays.siblings(".work-days").eq(0));
		var selectedWorkDays = [];
		
		$calendar_applicationWorkDays.datepicker({
			minDate: firstDate,
			numberOfMonths: getNumberOfMonths($calendar_applicationWorkDays),
			onSelect: function(dateString, inst){
				var date = dateify(dateString);
				
				if(doesDateArrayContainDate(date, workDays)){
					if(doesDateArrayContainDate(date, selectedWorkDays))
						selectedWorkDays = removeDateFromArray(date, selectedWorkDays);
					else selectedWorkDays = attemptToAddDate(date, selectedWorkDays);
				}
				
			},
			beforeShowDay: function(date){
				if(doesDateArrayContainDate(date, selectedWorkDays)) return [true, "active111 selected-work-day"];
				else if(doesDateArrayContainDate(date, workDays)){
					
					return [true, "active111"];
				}
				else return [true, ""];
			},
			afterShow: function(){
				var html = "";
				$(workDays).each(function(){
					
					var td = getTdByDate($calendar_applicationWorkDays, this);
					
					html = "<div class='start-and-end-times'>";
					html += "<p>7:30a</p><p>5:30p</p>";
					html += "</div>"
					
					$(td).append(html);
				})			
			}

		})
		
		$calendar_applicationWorkDays.datepicker("setDate", firstDate);
		
	}


	

}


function doesApplicantNeedToSelectWorkDays(){
	
	var $calendar = $("#apply-work-days-calendar-container .calendar");
	if($calendar != undefined) return true;
	else return false;
}

function setWorkDays(){
	
	var dates = [];
	
	$("#workDays").find("div").each(function(){
		var dateString = $(this).attr("data-date").replace(/-/g, "/");
		workDays.push(new Date(dateString));
	})
	
	
}


