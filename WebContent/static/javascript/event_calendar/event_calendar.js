
$(document).ready(function(){
	
	
	
	
	initCalendar_eventCalendar();
	
	$(".calendar").on("mouseover", "td.application", function(){
		
		var visiblePopup = $(".calendar").find(".popuptext:visible").eq(0);
		
		if(visiblePopup != this){
			$(visiblePopup).hide();
			$(this).find(".popuptext").show();
		}

	})
	
	$(".calendar").on("mousedown", ".ui-datepicker-group-last *", function(e){
		if(e.which == 1){
			e.stopPropagation();
			$(".calendar").find(".ui-datepicker-next").eq(0).click();
		}
	})
	
	$(".calendar").on("mousedown", ".ui-datepicker-group-first *", function(e){
		if(e.which == 1){
			e.stopPropagation();
			$(".calendar").find(".ui-datepicker-prev").eq(0).click();
		}
	})
	
	var workDays_hoveredApplication = [];
	$("body").on("mouseover", ".popuptext p", function(e){
		
		var applicationId = $(this).attr("data-application-id");
		var $application = $("#application-details").find(".application[data-id='" + applicationId + "']").eq(0);
		
		removeApplicationHoverStyles();
		
		workDays_hoveredApplication  = getDateFromContainer($application);
		$calendar = $(".calendar");
		
		$(workDays_hoveredApplication).each(function(){
			var td = getTdByDate($calendar, this);
			$(td).addClass("application-hover");
		})
	})
	
	$("body").on("mouseout", ".popuptext", function(e){
		hidePopup();
		removeApplicationHoverStyles();
	});
	
	$(".calendar").on("mouseout", ".ui-datepicker-group-middle td", function(e){
//		if($(e.target).hasClass("ui-datepicker-group-middle")){
		if($(e.target).hasClass("application")){
			hidePopup();
			removeApplicationHoverStyles();
		}
		
	})

	
})

function removeApplicationHoverStyles(){
	$(".calendar td.application-hover").each(function(){
		$(this).removeClass("application-hover");
	})
}

function hidePopup(){
	$(".calendar").find(".popuptext:visible").eq(0).hide();
	
}

function initCalendar_eventCalendar(){
	
	var dates_employment = getDateFromContainer($("#employment-details"));
	var dates_applications = getDateFromContainer($("#application-details"));
	var dates_unavailable = getDateFromContainer($("#unavailability-details"));
	var $calendar_events = $(".calendar").eq(0);
	
	var date_lastMonth = new Date();
	date_lastMonth.setMonth((new Date()).getMonth() - 1);
	$calendar_events.datepicker({
		minDate: date_lastMonth,
		numberOfMonths: 3,
		beforeShowDay: function(date){
			
			if(doesDateArrayContainDate(date, dates_employment)) return [true, "active111 employment"];
			else if(doesDateArrayContainDate(date, dates_applications)) return [true, "active111 application"];
			else if(doesDateArrayContainDate(date, dates_unavailable)) return [true, "active111 unavailable"];
			else return [true, ""];
		},
		afterShow: function(){
		
			var html = "";
			var $calendar_middle = $calendar_events.find(".ui-datepicker-group-middle");
			$(dates_employment).each(function(){
				
				var td = getTdByDate($calendar_middle, this);
				
				html = "<div class='start-and-end-times'>";
				html += "<p>7:30a</p><p>5:30p</p>";
				html += "</div>";
				
				$(td).append(html);
			})			
			
			
			$("#application-details").find(".application").each(function(){
				html = this.cloneNode(false).outerHTML;				
				$(this).find("[data-date]").each(function(){
					var td = getTdByDate($calendar_middle, dateify($(this).attr("data-date")));	
					$(td).append(html);
				})
			})
			
			$calendar_middle.find("td.application").each(function(){
				var countApplications = $(this).find("div.application").size();;				
				html = "<div class='application-count'><span>" + countApplications + "</span></div>";
				
				html += "<div class='popup'><div class='popuptext'>";
				$(this).find(".application").each(function(){
					
					html += "<p data-application-id='" + $(this).attr("data-id") + "'"
					  + " data-job-id='" + $(this).attr("data-job-id") + "'"
					  + ">" + $(this).attr("data-job-name") + "</p>";
				})			
				html += "</div></div>";				
				
				
				
				$(this).append(html);
				
				
				
			})

		}
	})
	
}