

$(document).ready(function(){
	
	$applications_list_view = $("#applications_list_view");
	$applications_calendar_view = $("#applications_calendar_view");
	
	$(".select-page-section-container #show_list_and_calendar").click(function(){
		
		$applications_list_view.show();
		$applications_calendar_view.show();
		
		$applications_list_view.addClass("with-calendar");
		$applications_calendar_view.addClass("with-list");
		
		highlightArrayItem($(this), $(".select-page-section-container .select-page-section"), "selected");
		
		hideVisiblePopup();
		
	})
	
	$(".select-page-section-container :not(#show_list_and_calendar)").click(function(){


		
		$applications_list_view.removeClass("with-calendar");
		$applications_calendar_view.removeClass("with-list");
		
	})

	
	initCalendar_employeeProfile_applicantSummary()
	
})

function initCalendar_employeeProfile_applicantSummary() {
	
	var workDays = getDateFromContainer($("#applicationDetails"));
	var $calendar = $("#job-calendar-application-summary .calendar");
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		beforeShowDay: function(date){
			if(doesDateArrayContainDate(date, workDays)) return [true, "active111"];
			else return [true, ""];
		},
		afterShow: function(){
			var html = "";
			var counts = [3, 8, 10];
			var max = Math.max.apply(null, counts);
			$(workDays).each(function(i, date){
				
				var td = getTdByDate($calendar, date);
				if($(td).find(".application-count").size() == 0){
					html = "";
					var number = parseInt(Math.random() * 10);
//					html = "<div class='employment-fraction'>1 / 4</div>";
					html += "<div class='application-count'>";
					html += "<span>" + number + "</span>";
					html += "</div>"
					
					$(td).append(html);
					var $addedDiv = $(td).find(".application-count");
					
					$addedDiv.css("height", 40 * number / 10 + "%");				
				}

			})				
		}
	})
	
	$calendar.datepicker("setDate", firstDate);
}