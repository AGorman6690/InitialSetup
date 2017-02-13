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
//		setCalendarDays_employment();
	
	initCalendar_Employee_Profile($("#calendar_applications"));	
	
	initCalendar_Both();

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
	
	
	$("#calendarContainer_both").on("mouseover", "td div.job", function(){
	
		$("#calendarDetails_applications").addClass("disabled");
		$("#calendarDetails_applications").show();
		
		$("#applications_on_day_clicked").hide();
		$("#applications_on_day_hover").show();
		
		
		
		var html = "";
		var jobName;
		var applicationId;
		var jobId;
		var applicationIds = [];
		
		
		$("#date_detail").html(getDateText($(this).parent()));
		
		html = "<div class='content'>";
		
		$(this).find("div").each(function(){
			
			jobName = $(this).attr("data-job-name");
			applicationId = $(this).attr("data-id");
			
						
			html  += "<div><a class='accent' href=# data-application-id='" + applicationId + "'>" 
							+ jobName + "</a></div>";
			
			applicationIds.push(applicationId);
		})

		html += "</div>";
		
		$("#applications_on_day_hover").empty();
		$("#applications_on_day_hover").html(html);
		
		showApplications_onHover(applicationIds);
		
		

		
	})
	
	$("#calendarContainer_both").on("mouseout", "td div.job", function(){
		

		if($("#applications_on_day_clicked").html() == ""){
//				$("#calendarDetails_applications").hide();
			$("#date_detail").html("...");
		}
		else{
			$("#date_detail").html(clickedDate);
			$("#calendarDetails_applications").removeClass("disabled");
		}

		
		$("#applications_on_day_clicked").show();
		$("#applications_on_day_hover").hide();
		
		$("#calendarDetails_applications").removeClass("still-hovering");
		
		showAllApplications();
					
	})
	
	var clickedDate;
	
	$("#calendarContainer_both").on("mousedown", "td div.job", function(event){
		
		// Only left click
		if(event.which == 1){
			
			var html_hover;
			var html_click;
			
			html_hover = $("#applications_on_day_hover .content").clone();
			
			$("#applications_on_day_clicked").html(html_hover);
			$("#calendarDetails_applications").removeClass("disabled");
			$("#calendarDetails_applications").addClass("still-hovering");
//				toggleClasses($("#application_on_day"), "disabled", "enabled");	
			
			clickedDate = getDateText($(this).parent());

		}
		
	})		
	
	$("table#openApplications_oneLine tr").on("mouseover", "*", function(){
		var applicationId = $(this).closest("tr").attr("data-application-id");
		highlightApplicationWorkDays(applicationId, true);		
	})
	
	$("table#openApplications_oneLine tr").on("mouseout", "*", function(){
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

function showAllApplications(){
	$("#openApplications_oneLine").find("tr.application").each(function(){
		
		$(this).show();
		
	})	
}

function showApplications_onHover(applicationIds){
	
	var iApplicationId;
	
	$("#openApplications_oneLine").find("tr.application").each(function(){
		
		iApplicationId = $(this).attr("data-application-id");
		
		if(doesArrayContainValue(iApplicationId, applicationIds)) $(this).show();
		else $(this).hide();
		
	})
	
}

function getDateText(td){
	var date = getDateFromTdElement(td);
	return $.datepicker.formatDate("D m/dd", date);
	
}

function highlightApplicationWorkDays(applicationId, doHightlight){
		
	var workDays = getWorkDaysByApplicationId(applicationId);
	var td;
	var month_firstWorkDay;
	
	
	$("#calendar_both").datepicker("setDate",
			$.datepicker.formatDate("mm/dd/yy", workDays[0])); 
	
	$(workDays).each(function(i, workDay){
		
		td = getTdByDayMonthYear($("#calendarContainer_both"), this.getDate(), this.getMonth(),
									this.getFullYear());
		
		if(doHightlight) $(td).find("div.job").addClass("hover");
		else $(td).find("div.job").removeClass("hover");
	})	
	
}

function getWorkDaysByApplicationId(applicationId){
	var application = $("#applicationDetails").find(".application[data-id=" + applicationId + "]");
	var workDays = [];
	
	$(application).find(".work-day").each(function(){
		var date = new Date($(this).attr("data-date").replace(/-/g, "/"));
		workDays.push(date);
	})
	
	return workDays;
}

function addClassToDivWithAttr(className, attrName, attrValue){
	
	$("#calendarContainer_applications").find("td div[" + attrName + "='" + attrValue + "']").each(function(){
		
		$(this).addClass(className);
	})
	
}

function removeClassToDivWithAttr(className, attrName, attrValue){
	
	$("#calendarContainer_applications").find("td div[" + attrName + "='" + attrValue + "']").each(function(){
		
		$(this).removeClass(className);
	})
	
}


function setCalendarDays_employment(){
	
	var jobs = $("#employmentDetails").find(".job");
	var workDays = [];
	var application = {};	
	var calendarDays_toAddTo = [];
	
	$(jobs).each(function(){
		
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
//		application.lineClassName = getLineClassForApplication(calendarDays_toAddTo);
		application.jobName = $(this).attr("data-job-name");
		application.jobId = $(this).attr("data-job-id");
		application.id = $(this).attr("data-id");
		
		application.isEmployment = true;
				
		// Add the application object to each necessary calendar day.
		addApplicationToCalendarDays(application, calendarDays_toAddTo);
		
	})	
}


function setCalendarDays(){
	
	var applications = $("#applicationDetails").find(".application");
	var workDays = [];
	var lineClass;
	var application = {};
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
		application.jobStatus = $(this).attr("data-job-status");
		application.id = $(this).attr("data-id");
		
		application.isEmployment = false;
				
		// Add the application object to each necessary calendar day.
		addApplicationToCalendarDays(application, calendarDays_toAddTo);
		
	})

}


function initCalendar_Both(){
//	var numberOfMonths = getNumberOfMonths($e);
//	var minDate = new Date(?);

	$("#calendar_both").datepicker({
//		minDate: minDate,
		numberOfMonths: 1, 
		hideIfNoPrevNext: true,
		afterShow: showApplications_both,
	});
}


function initCalendar_Employee_Profile($e){
		
	var minDate = new Date($e.attr("data-min-date"));

	$e.datepicker({
		minDate: minDate,
		numberOfMonths: 2, 
		hideIfNoPrevNext: true,
		afterShow: showApplications,
	});
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
			
			
			if(this.applications[0].jobStatus == 1){
				divHtml = "<div class='job employment'>";
			}
			else{
				
				if(appCount == 1) colorClass = "lgt";
				else if(appCount == 2) colorClass = "med";
				else colorClass = "drk";
				
				divHtml = "<div class='job application " + colorClass + "'><span class='app-count'>" + appCount + "</span>";	
			}
			
			
			$(this.applications).each(function(){
				divHtml += "<div data-id='" + this.id + "'"
							  + " data-job-name='" + this.jobName + "'"
							  + " data-job-id='" + this.jobId + "'"
							  + " class='hide both'></div>";	
							  
				
			})
			
			divHtml += "</div>";
			
			$(td).append(divHtml);	
		}
		
	})
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