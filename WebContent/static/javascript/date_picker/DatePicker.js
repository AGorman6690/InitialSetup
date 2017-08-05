$(document).ready(function(){

	$(".calendar-container button.clear").click(function(){
		
		var clearClassName = $(this).attr("data-clear-class");
		if(clearClassName == undefined) clearClassName = "active111";
		clearCalendar($(this).closest(".calendar-container"), clearClassName);
	})
	
	$("body").on("mouseover", ".calendar.show-hover-range td", function(){
		
		var $caledar = $(this).closest(".calendar");
		var hoverDate = getDateFromTdElement(this);
		var firstDate = dateify($caledar.attr("data-first-date"));

		showHoverDateRange($caledar, hoverDate, firstDate);
		
	})
	
})
function changePrevNextText($calendar, prevText, nextText){
		$calendar.find(".ui-icon.ui-icon-circle-triangle-e").eq(0);
 		$calendar.find(".ui-icon.ui-icon-circle-triangle-e").eq(0).html(nextText);
 		$calendar.find(".ui-icon.ui-icon-circle-triangle-w").eq(0).html(prevText);
}
function dateifyWorkDayDtos(workDayDtos) {
	$(workDayDtos).each(function(i, workDayDto) {
		workDayDto.date = dateify(workDayDto.workDay.stringDate);
	})
	return workDayDtos;
}

function isDateInWorkDayDtos(date, workDayDtos) {
	
	var workDayDto = getWorkDayDtoByDate(date, workDayDtos);
	
	if(workDayDto != undefined) return true;
	else return false;
}

function getWorkDayDtoByDate_new(dateToFind, workDayDtos) {
	
	var result = false;
	$(workDayDtos).each(function(i, workDayDto) {
		if(workDayDto.date != undefined && workDayDto.date.getTime() == dateToFind.getTime()){
			result = workDayDto;
			return false;
		}
	})
	
	if(!result) return undefined;
	else return result;
}

function getWorkDayDtoByDate(dateToFind, workDayDtos) {
	// *****************************************************
	// *****************************************************
	// change function name to: getWorkDayDtoByDateString
	// *****************************************************
	// *****************************************************
	
	var result = false;
	$(workDayDtos).each(function(i, workDayDto) {
		if(workDayDto.date != null){
			if(workDayDto.date.getTime() == dateToFind.getTime()){		
				result = workDayDto;
				return false;
			}
		}
		else if(dateify(workDayDto.workDay.stringDate).getTime() == dateToFind.getTime()){		
			result = workDayDto;
			return false;
		}		
	})
	
	if(!result) return undefined;
	else return result;
}

function getDaysFromWorkDays(workDays, daysArray){
	// Receive an array of work day objects and return
	// an array of dates
	
	daysArray = [];
	
	$(workDays).each(function(){
		daysArray.push(new Date(this.stringDate));
	})
	
	return daysArray;
	
}

function getDatesFromWorkDayDtos(workDayDtos, daysArray){
	// Receive an array of work day objects and return
	// an array of dates
	
	daysArray = [];
	
	$(workDayDtos).each(function(i, workDayDto){
		if(workDayDto.date != null && !isNaN(workDayDto.date.getTime())) daysArray.push(workDayDto.date);
		else daysArray.push(dateify(workDayDto.workDay.stringDate));
	})
	
	return daysArray;
	
}
function getDateStringsFromWorkDayDtos(workDayDtos){
	// Receive an array of work day objects and return
	// an array of dates
	
	dateStrings = [];
	
	$(workDayDtos).each(function(i, workDayDto){
		dateStrings.push(workDayDto.workDay.stringDate);
	})
	
	return dateStrings;
	
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

function resetCalendar_hoverRange(days, $calendar){
	
	
	

	
}

function getTdByDate($calendar, date){
	
	return getTdByDayMonthYear($calendar, date.getDate(), date.getMonth(), date.getFullYear());
	
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


function getSelectedDate($calendar, format, classToSearchFor){
	
	if(classToSearchFor == undefined) classToSearchFor = "active111";
	var selectedTd = $calendar.find("td." + classToSearchFor);
	
	var date = getDateFromTdElement(selectedTd);
	if(!isNaN(date.getTime())){
		if(format != undefined) return $.datepicker.formatDate(format, date);
		else return date	
	}else return undefined;
	
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
	if(numberOfMonths > 10) return 5; // This a safety net in case logic gets screwy on the server
	else if(numberOfMonths == undefined){
		return 1;
	}
	else{
		numberOfMonths =  parseInt(numberOfMonths);
		
		if(isNaN(numberOfMonths)) return 1;
		else return numberOfMonths;
	}
		
}

function getMonthsSpan(dateArray) {
	
	if(dateArray.length > 0){
		var minDate = new Date(Math.min.apply(null, dateArray));
		var maxDate = new Date(Math.max.apply(null, dateArray));
		return maxDate.getMonth() - minDate.getMonth() + 1;
	}	
}

function getMinDateFromDateArray(dateArray){
	if(dateArray.length > 0){
		return new Date(Math.min.apply(null, dateArray));
	}	
}

function getMinDateFromDateStringsArray(dateStringArray) {
	
	var dates = [];
	$(dateStringArray).each(function() {
		dates.push(dateify(this));
	})
	
	return getMinDateFromDateArray(dates);
	
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
		}else{
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

function getNewWorkDayDto(initDate){
	var newWorkDayDto = {};
	newWorkDayDto.workDay = {};
	newWorkDayDto.workDay.stringStartTime = "";
	newWorkDayDto.workDay.stringEndTime = "";
	newWorkDayDto.date = initDate;
	return newWorkDayDto;
	
}

function attemptToAddDateRange_workDayDtos(date, workDayDtos){
	
	var iDate = new Date();
	var firstDate = new Date();
	var i = 1;
	var endDate = new Date();
	
	// If the user is toggling off the first clicked date
	if(doesWorkDayDtoArrayContainDate(date, workDayDtos))
		removeWorkDayDto(date, workDayDtos);	
	// Else set the date range
	else {		
		
		var newWorkDayDto = getNewWorkDayDto(date);
		workDayDtos.push(newWorkDayDto);
		
		if(workDayDtos[0].date.getTime() < date.getTime()){
			firstDate = workDayDtos[0].date;
			endDate = date;
		}else{
			firstDate = date;
			endDate = workDayDtos[0].date; 	
		}
		
		iDate = incrementDate(firstDate, i);
		while(iDate < endDate){		
			newWorkDayDto = getNewWorkDayDto(iDate);
			workDayDtos.push(newWorkDayDto);
			
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


function removeWorkDayDto(dateToRemove, workDayDtos) {

	var arr = [];
	arr = $.grep(workDayDtos, function(workDayDto){
		return workDayDto.date.getTime() != dateToRemove.getTime();
	})		
	
	return arr;	
}

function addOrRemoveWorkDayDtoByDate(date, workDayDtos){
	
	if(!isDateAlreadySelected_workDayDtos(date, workDayDtos)){
		
		var workDayDto = getNewWorkDayDto(date);
		workDayDtos.push(workDayDto);
	} else workDayDtos = removeWorkDayDto(date, workDayDtos);
	
	return workDayDtos;
}
function isDateAlreadySelected_workDayDtos(dateToCheck, workDayDtos){
	
	var arr = [];	
	arr = $.grep(workDayDtos, function(workDayDto){
		return workDayDto.date.getTime() == dateToCheck.getTime();
	})
	
	if(arr.length > 0) return true;
	else return false;
	
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

function showHoverDateRange($calendar, hoverDate, firstDate){

	var iDate;
	var td;
	var startDate;
	var endDate;
	
	if(!isNaN(hoverDate.getTime()) && firstDate.getTime() != hoverDate.getTime()){
		if(hoverDate.getTime() < firstDate.getTime()){
			
			startDate = hoverDate;
			endDate = firstDate;
			endDate.setDate(endDate.getDate() - 1);
		}else{
			startDate = firstDate;
			startDate.setDate(startDate.getDate() + 1);
			endDate = hoverDate;
		}
			
		$calendar.find("td.hover-range").each(function(){ $(this).removeClass("hover-range") });
		iDate = startDate;
		while(startDate.getTime() <= endDate.getTime()){
			
			td = getTdByDate($calendar, iDate);
			$(td).addClass("hover-range");
		
			iDate.setDate(iDate.getDate() + 1);
			
		}			
	}
}