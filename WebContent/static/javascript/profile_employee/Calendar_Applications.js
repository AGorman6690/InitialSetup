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
		setCalendarDays();
		
		initCalendar_Employee_Profile($(".calendar"));		

// 		test();	

		$("#calendarContainer_applications").on("mouseover", "td div[data-job-name]", function(){
			
			var jobName = $(this).attr("data-job-name");
			
			$("#jobDetails p").show();
			$("#jobDetails .job-name").html(jobName);
			
			addClassToDivWithAttr("hover", "data-job-name", jobName); 
			
		})
		
		$("#calendarContainer_applications").on("mousedown", "td div[data-job-name]", function(event){
			
			// Only left click
			if(event.which == 1){
				window.location = "/JobSearch/job/" + $(this).attr("data-job-id") + "?c=profile-incomplete&p=1";	
			}
			
		})
		
		$("#calendarContainer_applications").on("mouseout", "td div[data-job-name]", function(){
			$("#jobDetails p").hide();
			
			var jobName = $(this).attr("data-job-name");
			removeClassToDivWithAttr("hover", "data-job-name", jobName); 
		})

})

function addClassToDivWithAttr(className, attrName, attrValue){
	
	$("#calendarContainer_applications").find("td div[" + attrName + "=" + attrValue + "]").each(function(){
		
		$(this).addClass(className);
	})
	
}

function removeClassToDivWithAttr(className, attrName, attrValue){
	
	$("#calendarContainer_applications").find("td div[" + attrName + "=" + attrValue + "]").each(function(){
		
		$(this).removeClass(className);
	})
	
}


function setCalendarDays(){
	
	var applications = $("#applicationDetails").find(".application");
	var workDays = [];
	var lineClass;
	var application = {}	
	var calendarDays_toAddTo = [];
	
	$(applications).each(function(){
		
		// Read the DOM for the application's work days
		workDays = [];		
		$(this).find(".work-day").each(function(){
			workDays.push($(this).attr("data-date"))
		});
		
		// From the application's work days,
		// get the calendar day objects to add this application to
		calendarDays_toAddTo = []
		calendarDays_toAddTo = getCalendarDays(workDays, calendarDays);
		
		// Create the application object
		application = {}
		application.lineClassName = getLineClassForApplication(calendarDays_toAddTo);
		application.jobName = $(this).attr("data-job-name");
		application.jobId = $(this).attr("data-job-id");
		application.id = $(this).attr("data-id");
				
		// Add the application object to each necessary calendar day.
		addApplicationToCalendarDays(application, calendarDays_toAddTo);
		
	})

}





function initCalendar_Employee_Profile($e){
		
	var numberOfMonths = getNumberOfMonths($e);
	var minDate = new Date($e.attr("data-min-date"));
	
	var month = minDate.getMonth();
	

	$e.datepicker({
		minDate: minDate,
		numberOfMonths: 2, 
		hideIfNoPrevNext: true,
		afterShow: showApplications,
	});
}



function showApplications(){
		
	
	$(calendarDays).each(function(){
		
		var divHtml;
		date_calendarDay = new Date(this.date);
		var td = getTdByDayMonthYear($(".calendar"), date_calendarDay.getDate().toString(),
													 date_calendarDay.getMonth().toString(),
				
													 date_calendarDay.getFullYear().toString());	

		$(this.applications).each(function(){
			divHtml = "<div data-id='" + this.id + "'"
						  + " data-job-name='" + this.jobName + "'"
						  + " data-job-id='" + this.jobId + "'"
						  + " class='job " + this.lineClassName + "'></div>";	
						  
			$(td).append(divHtml);	
		})
	})
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

function getApplicationsOnCalendar(applicationWorkDays, calendarDays){
	
//	var applications = [];
//	
//	// For each application work day
//	$(applicationWorkDays).each(function(){
//		
//		// Build an array of the calendar
//		$(calendarDays).each(function(){
//			
//			if()
//			
//		})	
//	})
	
	
	
	
}