$(document).ready(function(){

	$(".calendar-container button.clear").click(function(){
		
		var clearClassName = $(this).attr("data-clear-class");
		if(clearClassName == undefined) clearClassName = "active111";
		clearCalendar($(this).closest(".calendar-container"), clearClassName);
	})
	
})



function getDaysFromWorkDays(workDays, daysArray){
	// Receive an array of work day objects and return
	// an array of dates
	
	daysArray = [];
	
	$(workDays).each(function(){
		daysArray.push(new Date(this.stringDate));
	})
	
	return daysArray;
	
}

function clearCalendar($calendar, clearClassName){
	$calendar.find("td." + clearClassName).each(function(){
		$(this).removeClass(clearClassName);
	})
	return [];
}

function getTdByDayMonthYear($calendar, day, month, year){
	
//	var a = $calendar.find("td[data-year=" + year + "][data-month=" + month + "] a:contains(" + day + ")");
	var anchors = $calendar.find("td[data-year=" + year + "][data-month=" + month + "] a");
	var td;
	$(anchors).each(function(){
		if(this.innerHTML == day){
			td = $(this).parent(); 
		}
	});
	
	return td;
	
}

function selectCalendarTdElement_ByDate($calendar, stringDate){
	
	var date = new Date(stringDate);
	
	var $td = $(getTdByDayMonthYear($calendar, date.getDate(), date.getMonth(), date.getFullYear()));
	
	$td.click();
	
	
	
}

function getDateFromTdElement(td){
	var year = $(td).attr("data-year");
	var month = parseInt($(td).attr("data-month"));
	var day = $(td).children().html();
	return  new Date(year, month, day);
}


function getSelectedDate($calendar){
	
	var selectedTd = $calendar.find("td.active111");
	
	return getDateFromTdElement(selectedTd);
	
}

function setDataAttributes_calendarContainer_byJobDto($calendar, jobDto){
	
	// Some calendars are initiated with the calendars data attributes.
	// When calendars are initiated after an ajax request, these data attributes
	// need to be set after the page is rendered.
	
	$calendar.attr("data-number-of-months", jobDto.months_workDaysSpan);
	$calendar.attr("data-min-date", jobDto.date_firstWorkDay); 
	
	return $calendar;
}

function getNumberOfMonths($e){
	
	var numberOfMonths = $e.attr("data-number-of-months");
	if(numberOfMonths == undefined){
		return 1;
	}
	else{
		numberOfMonths =  parseInt(numberOfMonths);
		
		if(isNaN(numberOfMonths)) return 1;
		else return numberOfMonths;
	}
		
}


function getMinDate($calendar){
	
	var dateString = $calendar.attr("data-min-date");
	
	if(dateString != undefined){
		var minDate = new Date(dateString.replace(/-/g, "/"));
		
		if(isNaN(minDate.getTime()) == true) return new Date();
		else return minDate;		
	}else{
		return new Date();
	}

	
}

function attemptToAddDate(date, days){
	
	if(!isDateAlreadySelected(date, days)){        	
		days.push(date);
    }
	
	return days;
	
}

function dateify(dateString){
	
	// This is used to initialize a date object.
	// For some reason the date string needs to be formatted as yyyy/mm/dd.
	// If it isn't, then the date in initialized to the date string minus one day.
	
	var date = new Date(dateString.replace(/-/g, "/"));
	
	if(isNaN(date.getTime()) == true) return undefined;
	else return date;
	
}


function addOrRemoveDate(date, days){
	if(!isDateAlreadySelected(date, days)) days.push(date);
	else days = removeDay(date, days);
	
	return days;
}

function addOrRemoveDate_SingeDateCalendar(date, days){
	
	if(days.length == 1){
		if(isDateAlreadySelected(date, days)){
			days = removeDay(date, days);
		}
		else days[0] = date;			
	}
	else days[0] = date;
	
	return days;
}

function attemptToAddDateRange(date, days){
	

	var iDate = new Date();
	var firstDate = new Date();
	var i = 1;
	var endDate = new Date();
	
	// If the user is toggling off the first clicked date
	if(isDateAlreadySelected(date, days)) removeDay(date, days);
	
	// Else set the date range
	else {
		
		days.push(date);
		
		if(days[0].getTime() < date.getTime()){
			firstDate = days[0];
			endDate = date;
		}
		else{
			firstDate = date;
			endDate = days[0]; 	
		}
		

		iDate = incrementDate(firstDate, i);
		while(iDate < endDate){
			
			days.push(iDate);
			
			i += 1;
			iDate = incrementDate(firstDate, i);
			
			
		}		
	}
}

function incrementDate(fromDate, i){

	var newDate = new Date(fromDate);
	newDate.setDate(newDate.getDate() + i)
	newDate.setHours(0, 0, 0, 0);
	return newDate;
}


function removeDay(dateToRemove, days){
	
	var arr = [];
	arr = $.grep(days, function(day){
		return day.getTime() != dateToRemove.getTime();
	})		
	
	return arr;

}

function isDateAlreadySelected(dateToCheck, days){
	
	var arr = [];	
	arr = $.grep(days, function(date, days){
		return date.getTime() == dateToCheck.getTime();
	})
	
	if(arr.length > 0) return true;
	else return false;
	
}

function getWorkDays_FromSelectedDates($calendar, format){
	
	var selectedDates = getSelectedDates($calendar, format)
	var workDays = [];
	
	$(selectedDates).each(function(){
		var workDay = {};
		workDay.stringDate = this;
		
		workDays.push(workDay);
	})
	
	return workDays;
}

function getSelectedDates($calendar, format, className_selectedDate){
		
	if(className_selectedDate == undefined) className_selectedDate = "active111";
	
	
	var selectedTds = $calendar.find("." + className_selectedDate);
	var selectedDates = [];
	var date;

	selectedTds.each(function(){
		date = getDateFromTdElement(this);
		if(format != undefined) date = $.datepicker.formatDate(format, date);
		selectedDates.push(date);
	})
	
	return selectedDates;
}