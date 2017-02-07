
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

function getNumberOfMonths($e){
	
	var numberOfMonths = $e.attr("data-number-of-months");
	if(numberOfMonths == undefined){
		return 1;
	}
	else{
		return parseInt(numberOfMonths);
	}
		
}

function attemptToAddDate(date, days){
	
	if(!isDateAlreadySelected(date, days)){        	
		days.push(date);
    }
	
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

function getSelectedDates($calendar, format){
	
	var selectedTds = $calendar.find(".active111");
	var selectedDates = [];
	var date;

	selectedTds.each(function(){
		date = getDateFromTdElement(this);
		if(format != undefined) date = $.datepicker.formatDate(format, date);
		selectedDates.push(date);
	})
	
	return selectedDates;
}