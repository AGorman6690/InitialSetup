var calendarDays = [];

var lineClass_1 = "one";
var lineClass_2 = "two";
var lineClass_3 = "three";

//Refer to: https://bugs.jqueryui.com/ticket/6885
//I was directed to the above link from http://stackoverflow.com/questions/6334898/jquery-datepicker-after-update-event-or-equivalent/6337622#6337622
$(function() {
	$.datepicker._updateDatepicker_original = $.datepicker._updateDatepicker;
	$.datepicker._updateDatepicker = function(inst) {
		$.datepicker._updateDatepicker_original(inst);
		var afterShow = this._get(inst, 'afterShow');
		if (afterShow)
			afterShow.apply((inst.input ? inst.input[0] : null));  // trigger custom callback
	}
});

$(document).ready(function(){
	
	$(".container").on("mouseover", "#applications_calendar_view:not(.with-list) .calendar td div.popup", function(){
		
		var visiblePopup = $("#applications_calendar_view").find(".popuptext:visible").eq(0);
		
		if(visiblePopup != this){
			$(visiblePopup).hide();
			$(this).find(".popuptext").show();
		}
		
		
		
	})

	$("#applications_calendar_view .calendar").on("mousedown", "td .popuptext span", function(){

			window.location = "/JobSearch/job/" + $(this).attr("data-job-id") + "?c=profile-incomplete&p=1";
		
	})
	
	$("#applications_calendar_view").on("mouseout", ".calendar", function(e){
//		hideVisiblePopup();
	})
	


	setCalendarDays();

	
	initCalendar_Both();

	$(".container").on("mouseover", "#applications_list_view.with-calendar tr *", function(){
		var applicationId = $(this).closest("tr").attr("data-application-id");
		highlightApplicationWorkDays(applicationId, true);		
	})
	
	$(".container").on("mouseout", "#applications_list_view.with-calendar tr *", function(){
		var applicationId = $(this).closest("tr").attr("data-application-id");
		highlightApplicationWorkDays(applicationId, false);		
	})
	
	$("#applications_on_day_clicked").on("mouseover", "a", function(){
		
		var applicationId = $(this).attr("data-application-id");
		highlightApplicationWorkDays(applicationId, true);
		
	})
	
	$("#applications_on_day_clicked").on("mouseout", "a", function(){
		
		var applicationId = $(this).attr("data-application-id");
		highlightApplicationWorkDays(applicationId, false);
	})
})

function hideVisiblePopup(){
	
	var visiblePopup = $("#applications_calendar_view").find(".popuptext:visible").eq(0).hide();

}


function showApplications_both(){
	
	var colorClass;
	var divHtml;
	var td; 
	var appCount;
	
	$(calendarDays).each(function(){
		
		appCount = this.applications.length;
		
		if(appCount > 0){
			
			
			date_calendarDay = new Date(this.date);
			td = getTdByDayMonthYear($(".calendar"), date_calendarDay.getDate().toString(),
														 date_calendarDay.getMonth().toString(),				
														 date_calendarDay.getFullYear().toString());	
			
//			$(td).addClass("popup");
			
			if(this.applications[0].jobStatus == 1){
				divHtml = "<div class='job employment'>";
			}
			else{
				
				if(appCount == 1) colorClass = "lgt";
				else if(appCount == 2) colorClass = "med";
				else colorClass = "drk";
				
				divHtml = "<div class='job application popup " + colorClass + "'><span class='app-count'>" + "</span>";	
			}
			
			
			// *******************************************************************
			// *******************************************************************
			// Pretty this up.
			// If the popup is persued, then the below div can be removed entirely.
			// The job info can be stored in the span.
			// *******************************************************************
			// *******************************************************************
			
			$(this.applications).each(function(){
				divHtml += "<div data-id='" + this.id + "'"
							  + " data-job-name='" + this.jobName + "'"
							  + " data-job-id='" + this.jobId + "'"
							  + " class='hide line'></div>";	
							  
				
			})
			
			divHtml += "<span class='popuptext'>";
			$(this.applications).each(function(){
//				divHtml += 	"<aclass='accent'" +
//					   "href='/JobSearch/job/" + this.jobId + "?c=profile-incomplete&p=1'>" + this.jobName + "</a>";				
				
				divHtml += "<span data-id='" + this.id + "'"
				  + " data-job-id='" + this.jobId + "'"
				  + ">" + this.jobName + "</span>";
			})			
			divHtml += "</span>";
			
			
			divHtml += "</div>";
			
			$(td).append(divHtml);	
		}
		
	})
}


function highlightApplicationWorkDays(applicationId, doHightlight){
		
	var workDays = getWorkDaysByApplicationId(applicationId);
	var td;
	var month_firstWorkDay;
	var $calendar = $("#applications_calendar_view .calendar");
	
	$calendar.datepicker("setDate",
			$.datepicker.formatDate("mm/dd/yy", workDays[0])); 
	
	$(workDays).each(function(i, workDay){
		
		td = getTdByDayMonthYear($calendar, this.getDate(), this.getMonth(),
									this.getFullYear());
		
		if(doHightlight) $(td).find("div.job").addClass("hover");
		else $(td).find("div.job").removeClass("hover");
	})	
	
}

function getWorkDaysByApplicationId(applicationId){
	var application = $("#applicationDetails").find(".application[data-id=" + applicationId + "]");
	var workDays = getDateFromContainer($(application));

	return workDays;
}


function setCalendarDays(){
	
//	var applications = $("#applicationDetails").find(".application");
	var workDays = [];
	var lineClass;
	var application = {};
	var calendarDays_toAddTo = [];
	
	$("#applicationDetails .application").each(function(){
		
		// Read the DOM for the application's work days
		workDays = getDateFromContainer($(this))
		
		// From the application's work days,
		// get the calendar day objects to add this application to
		calendarDays_toAddTo = [];
		calendarDays_toAddTo = getCalendarDays(workDays, calendarDays);
		
		// Create the application object
		application = {}
		application.lineClassName = getLineClassForApplication(calendarDays_toAddTo);
		application.jobName = $(this).attr("data-job-name");
		application.jobId = $(this).attr("data-job-id");
		application.jobStatus = $(this).attr("data-job-status");
		application.id = $(this).attr("data-id");
		
		application.isEmployment = false;
				
		// Add the application object to each necessary calendar day.
		addApplicationToCalendarDays(application, calendarDays_toAddTo);
		
	})

}


function initCalendar_Both(){

	$("#applications_calendar_view .calendar").datepicker({
		minDate: new Date(),
		numberOfMonths: 1, 
		afterShow: showApplications_both,
	});
}

function getLineClassForApplication(calendarDays){
	
	
	var hasOption1 = false;
	var hasOption2 = false;
	var hasOption3 = false;
	
	$(calendarDays).each(function(){		
		$(this.applications).each(function(){	
			
			if(this.lineClassName == lineClass_1) hasOption1 = true;
			else if(this.lineClassName == lineClass_2) hasOption2 = true;
			else if(this.lineClassName == lineClass_3) hasOption3 = true;
			
		})		
		
	})
	
	if(!hasOption1) return lineClass_1;
	else if(!hasOption2) return lineClass_2;
	else if(!hasOption3) return lineClass_3;
	else return "";
	
	
	
		
}


function addApplicationToCalendarDays(application, calendarDays){
	
	$(calendarDays).each(function(){
		this.applications.push(application);
	})
	
	
}


function getCalendarDays(dates, calendarDays){
	
	var date;
	var calendarDate;
	var return_calendarDays = [];
	var found_calendarDay;
	var newCalendarDay;
	
	// For each application work day
	$(dates).each(function(i, dateText){
		
		date = new Date(dateText);
		found_calendarDay = false;
		
		// Build an array of the calendar days.
		$(calendarDays).each(function(){
			
			calendarDate = new Date(this.date);
			
			if(calendarDate.getTime() == date.getTime()){
				return_calendarDays.push(this);
				found_calendarDay = true;
			}
			
		})		
		
		// If the particular day does not yet exist in the calendar days array,
		// create the day and add it to the calendar day array.
		if(!found_calendarDay){
			 newCalendarDay = {};
			 newCalendarDay.applications = [];
			 newCalendarDay.date = dateText;
			 
			 calendarDays.push(newCalendarDay);			 
			 return_calendarDays.push(newCalendarDay);
		}
		
		
	})
	
	return return_calendarDays;
	
	
	
}
