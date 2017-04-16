
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
			
//			$(dates_employment).each(function(){
//				
//				var td = getTdByDate($calendar_middle, this);
//				
//				html = "<div class='start-and-end-times'>";
//				html += "<p>7:30a</p><p>5:30p</p>";
//				html += "</div>";
//				
//				$(td).append(html);
//			})		
			
//			renderEmpploymentLines($calendar_middle);		
			
			setEmploymentLines($calendar_middle);
			
//			$("#application-details").find(".application").each(function(){
//				html = this.cloneNode(false).outerHTML;				
//				$(this).find("[data-date]").each(function(){
//					var td = getTdByDate($calendar_middle, dateify($(this).attr("data-date")));	
//					$(td).append(html);
//				})
//			})
			
//			$calendar_middle.find("td.application").each(function(){
//				var countApplications = $(this).find("div.application").size();;				
//				html = "<div class='application-count'><span>" + countApplications + "</span></div>";
//				
//				html += "<div class='popup'><div class='popuptext'>";
//				$(this).find(".application").each(function(){
//					
//					html += "<p data-application-id='" + $(this).attr("data-id") + "'"
//					  + " data-job-id='" + $(this).attr("data-job-id") + "'"
//					  + ">" + $(this).attr("data-job-name") + "</p>";
//				})			
//				html += "</div></div>";				
//	
//				$(this).append(html);
//			})
			
			

		}
	})	
}


function setEmploymentLines($calendar){
	
	var dates;
	var jobs_employment = [];
	var job = {};
	
	// for each employed job, build a job object
	$("#employment-details .employment").each(function(){
		dates = getDateFromContainer($(this));
		job = {};
		job.isApplication = false;
		job.jobName = $(this).attr("data-job-name");
		job.topMargin_className = getTopMarginClassName(dates[0], $calendar);		
		job.line_elements = getLineElements(dates);
		jobs_employment.push(job);
	})	
	
	$("#application-details .application").each(function(){
		dates = getDateFromContainer($(this));
		job = {};
		job.isApplication = true;
		job.jobName = $(this).attr("data-job-name");
		job.topMargin_className = getTopMarginClassName(dates[0], $calendar);		
		job.line_elements = getLineElements(dates);
		jobs_employment.push(job);
	})	
	
	$(jobs_employment).each(function(i, job){
		
		$(job.line_elements).each(function(j, line_element) {
			
			var $td = $(getTdByDate($calendar, line_element.leftEndPoint.date));
			if($td.length == 0) $td = $(getTdByDate($calendar, line_element.rightEndPoint.date));

			var $tr = $td.closest("tr");
			
			var jobType_className = "employment-job";
			if(job.isApplication) jobType_className = "application-job";
			
			var leftMargin_className = "start-day-" + line_element.leftEndPoint.day;
			var html = "<div class='job-line " + jobType_className + " "  + job.topMargin_className + " " + leftMargin_className + "'>";
			
			
			
			var additionMargin_endPoint = 30;
			var tdWidth = 114.444;
			
			var employmentLineWidth = tdWidth * (line_element.rightEndPoint.day - line_element.leftEndPoint.day + 1)

			
			var leftMargin = tdWidth * (6 - line_element.leftEndPoint.day + 1);
			
			if(line_element.leftEndPoint.isFirst){
				employmentLineWidth -= additionMargin_endPoint;
				leftMargin -= additionMargin_endPoint;
			}
			if(line_element.rightEndPoint.isLast) employmentLineWidth -= additionMargin_endPoint;
			
			if(j == 0) html += job.jobName;
			else html += "... " + job.jobName;
			
			html += "</div>";
			
			$tr.append(html);
			
			var $appendedDiv = $tr.find(".job-line").last(); 
			$appendedDiv.css("width", employmentLineWidth);
			$appendedDiv.css("margin-left", -1 * leftMargin);
			
			
		})
		
		
		
	})
}

function getLineElements(jobDates){

	var line_elements = []
	var line_element = {};
	var previousDate;

	$(jobDates).each(function(i, date){

		if(i == 0){			
			line_element = getNewLineElement(date);


		}else if(isDateASunday(date)){
//		else if(i == 0){			
			line_element = getNewLineElement(date);
			

		}else if(!isDateConsecutive(date, previousDate)){
			// this date is not consecutive.
			// set the current line element's right end point equal to the previous day.
			line_element.rightEndPoint.date = previousDate;
			line_element.rightEndPoint.day = previousDate.getDay();
			line_element.rightEndPoint.isFirst = false;
			line_element.rightEndPoint.isLast = true;
			
			line_elements.push(line_element);
						
			// start a new line element whose left end point is this day
			line_element = getNewLineElement(date);
			
			
			
		}else if(isDateASaturday(date)){
			
			line_element.rightEndPoint.date = date;
			line_element.rightEndPoint.day = date.getDay();
			
			if(i == jobDates.length - 1) line_element.rightEndPoint.isLast = true;
			else line_element.rightEndPoint.isLast = false;
			
			
			line_elements.push(line_element);
			
		}
		
		if(i == jobDates.length - 1){
			
			// Set the current line element's last day
			line_element.rightEndPoint.date = date;
			line_element.rightEndPoint.day = date.getDay();
			line_element.rightEndPoint.isLast = true;	
			line_elements.push(line_element);
		}
		
		previousDate = date;
		
		
	})
	return line_elements;

}

function isDateASunday(date){
	if(date.getDay() == 0) return true;
	else return false;
}
function isDateASaturday(date){
	if(date.getDay() == 6) return true;
	else return false;
}
function isDateConsecutive(date, compareToDate){
	
	
	
	if(isDateASunday(date)){
		if(compareToDate.getDay() == 6) return true;
		else return false;
	}else{
		if(date.getDay() == compareToDate.getDay() + 1) return true;
		else return false;
	}
	
}

function getNewLineElement(firstDate){
	var line_element = {};
	line_element.leftEndPoint = {};
	line_element.rightEndPoint = {};
	
	// initialize left end point
	line_element.leftEndPoint.date = firstDate;
	line_element.leftEndPoint.day = firstDate.getDay();
	line_element.leftEndPoint.isFirst = true;
	
	return line_element;
	
}

function getTopMarginClassName(jobFirstDate, $calendar){
	
	// This is the top margin applied to the employment line 
	
	
	var topMarginClassName = "";
	td = getTdByDate($calendar, jobFirstDate);
	$tr = $(td).closest("tr");
	
	if(!$tr.hasClass("top-margin-0")) topMarginClassName =  "top-margin-0";
	else if(!$tr.hasClass("top-margin-1")) topMarginClassName = "top-margin-1";
	else if(!$tr.hasClass("top-margin-2")) topMarginClassName = "top-margin-2";
	else return "";
	
	$tr.addClass(topMarginClassName);
	return topMarginClassName;
}


function renderEmpploymentLines($calendar){
	var dates;
	var countDates;
	var td;
	var html;
	var jobName;
	$("#employment-details .employment").each(function(){
		dates = $(this).find("[data-date]");
		countDates = dates.length;
		jobName = $(this).attr("data-job-name");
		$(dates).each(function(i, date_html){
			
			html = "<div class='";
			
			if(i == 0) html += "first ";
			else if(i == countDates - 1) html += "last ";
			
			html += "employment-line";			
			html += "'>";
			
//			if(i == 0) html += "<p>" + jobName + "</p>";
			if(i == 0) html += jobName;
			
			html += "</div>";
//			if(i == 0) html += "asdfa sdfa sdfa sdf asdfa sdfasdf asdf asdfa asdf asdf asfsadf asfasdf asf asdfasfasd fasf asfdas f a sdfa"
				
			td = getTdByDate($calendar, dateify($(date_html).attr("data-date")));
			$(td).append(html);
		})
	})
}

