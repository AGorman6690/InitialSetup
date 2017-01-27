
var selectedDays = [];
var selectedDay;
var initDate;
var selectedDates_DateObjs = []


//var firstworkDay = {};
//var secondworkDay = {};
//var isSecondDaySelected = 0;
//var isMoreThanTwoDaysSelected = 0;
var rangeIsSet = 0;
//var rangeIsBeingSet = 0;


$(document).ready(function(){
	
	$("#clearCalendar").click(function(e){
		e.preventDefault();
		clearCalendar();
		$($(this).closest(".calendar")).attr("data-selected-days-count", selectedDays.length);
		
	})
	

	
	initializeCalendar();
	
	initializeSingeDateCalendars();
	
	
})

function initCalendar($e, 
						function_onSelect,
						function_beforeShowDay,
						options){
	
	var numberOfMonths = getNumberOfMonths($e);
	var minDate = new Date($e.attr("data-min-date"));
	var month = minDate.getMonth();
//	var changeMonth = options.changeMonth;
	
	$e.datepicker({
		minDate: minDate,
		numberOfMonths: numberOfMonths, 
		changeMonth: options.changeMonth,
		hideIfNoPrevNext: options.hideIfNoPrevNext,
		onSelect: function(dateText, event){
			
			function_onSelect(dateText);
		},
	        
//         This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {
        	
        	return function_beforeShowDay(month, date, daysOfWeekToSelect);
//        	milliseconds = date.getTime();
//        	var date = new Date(date1.getTime());
//        	// Is the date already selected?
//			if(isDateAlreadySelected(date)) return [true, "active111"];
//			else return [true, ""];

        }
    });
}

function function_beforeShowDay_selectMultipleSingleDays(month, theDate){
	
	// *********************************************************
	// *********************************************************
	// This function should only be used for the Availability page
	// *********************************************************
	// *********************************************************
	
	
	// **************************
	// **************************
	// Verify that a new date object still needs to be created now that
	// this happens in a separate function
	// **************************
	// **************************

	var date = new Date(theDate.getTime());
	var doAddClass_active111 = false;
	var doAddClass_saved = false;
	

	if(isDateAlreadySavedAsAvailable(date)){
	
		if(isDateSetToBeRemovedFromDatabase(date) == 0){
			doAddClass_active111 = true;
			doAddClass_saved = true;
//			return [true, "active111 saved"];
		}
//		else return [true, ""];
		
	}
	else{
		
		if(isDateAlreadySelected(date)){
			doAddClass_active111 = true;
//			return [true, "active111"];
		}
//		else return [true, ""];
		
	}
		
	
	// If
	if(doAddClass_active111 == false && date.getMonth() == month){
		var dayOfWeek = date.getDay();
		$("#daysContainer").find("input[data-day-of-week=" + dayOfWeek + "]").eq(0).prop("checked", false);
		$("#daysContainer").find("input.select-all").eq(0).prop("checked", false);
	}
	
	if(doAddClass_saved){
		$("#removeCurrentAvailability").prop("checked", false);
	}
	
	if(doAddClass_active111){
		if(doAddClass_saved) return [true, "active111 saved"];
		else return [true, "active111"];
	}
	else return [true, ""];
	
	
//	if(isDateAlreadySavedAsAvailable(date)) return [true, "active111 saved"];
//	else if(isDateAlreadySelected(date)) return [true, "active111"];
//	else return [true, ""];
	
	
	
	
//	if(isDateAlreadySelected(date)){
//		
//		
//		
//		if(isDateAlreadySavedAsAvailable(date)) return [true, "active111 saved"];
//		else return [true, "active111"];
//	}
//	else return [true, ""];
	
}

function isDateInSelectedDaysOfWeek(date, selectedDaysOfWeek){
	
	var dayOfWeek = date.getDay().toString();
	
	if(doesArrayContainValue(dayOfWeek, selectedDaysOfWeek)) return true;
	else return false;
}

function onselect(dateText) {	

	changeSelectAllCheckbox($("#daysContainer"), true, false);
	
	
	var date = new Date(dateText);
	
	// Check if the date is already saved (i.e. in the database) as available
	if(isDateAlreadySavedAsAvailable(date)){
	
		
		if(isDateSetToBeRemovedFromDatabase(date)){
			
			// Set it back to "do not remove from database"
			setDateToBeRemovedFromDatabase(date, 0);
			
			// Add date
			attemptToAddDate2(date);
		}
		else{
			// Set it back to "remove from database"
			setDateToBeRemovedFromDatabase(date, 1);
			
			// Remove date
			selectedDays = removeDay(date, selectedDays);	
		}
	
		
	}
	else{
		if(isDateAlreadySelected(date)) selectedDays = removeDay(date, selectedDays);
	    else attemptToAddDate2(date);
	}
		
		
		
		
//	else if(isDateAlreadySelected(date)) selectedDays = removeDay(date, selectedDays);
//    else selectDate(date);
	
}


function initMultiDayCalendar($e){
	var numberOfMonths = getNumberOfMonths($e);
	var milliseconds;
	var date;
	$e.datepicker({
		minDate: new Date(),
		numberOfMonths: numberOfMonths, 
		onSelect: function(dateText, inst) {	

			//There's odd behavior when the user clicks, for example,
			//the 1st then the 3rd, highlighting the 2nd as part of the range.
			//If the user clicks the 1st, 2nd, or 3rd, the dates will not toggle off
			//because the
            if(selectedDays.length == 3){
            	rangeIsSet = 1;
            }
            
			selectDate(new Date(dateText));
			
			// Is this the best place to remove "invalid input" styling from the calendar??
			// I tried adding a listener to the ".invalid.calendar *" elements, but
			// that did not work.
			// This works, but seems hackish...
//			if(selectedDays.length > 0){
//				setValidCss($($e.closest(".calendar")));
//			}
			
			// ************************************************			
			// Revisit this regarding post-job validation
			// ************************************************
//			// This attribute is 
//			$($e.closest(".calendar")).attr("data-selected-days-count", selectedDays.length);
//
//			validate_DatesAndTimes();
		},
	        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date1) {

//        	milliseconds = date.getTime();
        	var date = new Date(date1.getTime());
        	// Is the date already selected?
			if(isDateAlreadySelected(date)) return [true, "active111"];
			
			// Else is the "range" attempting to be set?
        	else if(isSecondDaySelected() && !rangeIsSet){
        		
        		//Add the dates in between the first and second days
        		if(isDateInRange(date)){
        			
					if(!isDateAlreadySelected(date)){
						addDay(date);
//						addDate(date);
					}
        			return [true, "active111"];
        		}
        		else return [true, ""];

        	}else return [true, ""];
        	
        }
    });
}



function initializeSingeDateCalendars(){
	
	$(".calendar-single-date").datepicker({
		minDate: new Date(),
		numberOfMonths: 1,
		// defaultDate: new Date($(this).attr("data-init-date").replace(/-/g, "/")),
		onSelect: function(dateText, inst) {			
			
			var temp = (new Date(dateText)).getTime();
			
			if(temp == selectedDay){
				selectedDay = null;	
			}
			else{
				selectedDay = temp;
			}
			
		
			
		},
	        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {
        	
        	var dateTime = date.getTime();   	

        	// this data attribute holds a date that the calendar should initially select 
        	var initDate = $(this).attr("data-init-date");
        	initDate = initDate.replace(/-/g, "/");

			if(dateTime == selectedDay){
        		return [true, "active111"]; 
        	}
			else if(areDatesEqual_year_month_date(date, new Date(initDate))){
				return [true, "active111"]; 
			}
        	else{
        		return [true, ""];
        	}
        },
	
	})	
}


function areDatesEqual_year_month_date(date1, date2){
	// Dates are considered equal if the year, month and date are equal;

	var y1 = date1.getFullYear();
	var m1 = date1.getMonth();
	var d1 = date1.getDate();
	
	var y2 = date2.getFullYear();
	var m2 = date2.getMonth();
	var d2 = date2.getDate();
	
	if(y1 == y2 && m1 == m2 && d1 == d2) return true;
	else return false;
}

function initSingleDayCalendar($e){

	$e.datepicker({
		minDate: new Date(),
		numberOfMonths: 1,
		onSelect: function(dateText, inst) {			
			var temp = (new Date(dateText)).getTime();
			
			if(temp == selectedDay){
				selectedDay = null;	
			}
			else{
				selectedDay = temp;
			}
			 
		},
	        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {

       	date = date.getTime();   	

			if(date == selectedDay){
        		return [true, "active111"]; 
        	}
        	else{
        		return [true, ""];
        	}
        },
	
	})
}


function initReadOnlyCalendar($e, minDate){
	
	var numberOfMonths = getNumberOfMonths($e);

	$e.datepicker({
//		dateFormat: 'yy-mm-dd',
//		minDate: minDate, // new Date(2016, 11,30, 0,0,0, 0),
		numberOfMonths: numberOfMonths,
		selectOtherMonths:true,
        beforeShowDay: function (date) {

        	date = date.getTime();
			
        	if(isDateAlreadySelected(date)){
        		return [true, "active111"]; 
        	}
        	else{
        		return [true, ""];
        	}

        },

	});
	
}


	function initializeCalendar(){
		var numberOfMonths = getNumberOfMonths($("#calendar"));
		
		$("#calendar").datepicker({
			minDate: new Date(),
			numberOfMonths: numberOfMonths, 
			onSelect: function(dateText, inst) {	
				
				//There's odd behavior when the user clicks, for example,
				//the 1st then the 3rd, highlighting the 2nd as part of the range.
				//If the user clicks the 1st, 2nd, or 3rd, the dates will not toggle off
				//because the
	            if(selectedDays.length == 3){
	            	rangeIsSet = 1;
	            }
	            
				selectDate(dateText);
			},
		        
		        // This is run for every day visible in the datepicker.
		        beforeShowDay: function (date) {
	
		        	date = date.getTime();
						if(isDateAlreadySelected(date)){
			        		return [true, "active111"]; 
			        	}
	
	//	        	}
		        	else if(isSecondDaySelected() && !rangeIsSet){
		        		
		        		//Add the dates in between the first and second days
		        		if(isDateInRange(date)){
		        			
							if(!isDateAlreadySelected(date)){
	
		        				addDay(date);
		        			}
		        			
		        			return [true, "active111"];
		        		}
		        		else{
	
		        			return [true, ""];
		        		}	  
	
		        	}else{
		        		return [true, ""];
		        	}
	
		        }
	    });
}


	function removeDay(dateToRemove){
		
		var modifiedSelectedDays = [];
		
		modifiedSelectedDays = $.grep(selectedDays, function(day){
    		return day.getTime() != dateToRemove.getTime();
    	})		

    	return modifiedSelectedDays;
	}
	


	function addDay(dateToAdd){
		selectedDays.push(dateToAdd);
	}
	
	function addDate(date){
		selectedDates_DateObjs.push(date);
	}
	
	function isDateAlreadySelected(dateToCheck){
		
		var arr = [];
		
		arr = $.grep(selectedDays, function(date){
			return date.getTime() == dateToCheck.getTime();
		})
		
		if(arr.length > 0){
			return true;
		}
		else{
			return false;
		}		
		
	}
	
	function isDateAlreadySelected_DateObj(date){
		
		var arr = [];
		
		arr = $.grep(selectedDates_DateObjs, function(d){
			return d == date;
		})
		
		if(arr.length > 0){
			return true;
		}
		else{
			return false;
		}		
		
	}
	
	function activateDate($calendar, date){
	
		var m = date.getMonth();
		var d = date.getDate();
		var y = date.getFullYear();
		
		var tds = $calendar.find("td[data-month='" + m + "'][data-year='" + y + "']");
		var a;
		$.each(tds, function(){
			a = $(this).find("a");
			if($(a).html() == d){
				//$(this).addClass("active111");
				$(this).trigger("click");
			}
		})
	}
	
	function getDate(dateText){
	 	var date = new Date(dateText);
	 	return date.getTime();
	}
	
	
	function selectDate(dateText){
		
		attemptToAddDate(dateText, function(){
	
			if(selectedDays.length == 0){
	        	resetVars();
	        }
			else{
				setDateRangeSelection();
			}
		});
		
		
	}
	
	function setDateRangeSelection(){

    	if(selectedDays.length == 1){
//    		firstworkDay = selectedDays[0];
    		rangeIsSet = false;

    		
    	}
    	else if(selectedDays.length == 2){

//    		secondworkDay = selectedDays[1];
    		
//    		isSecondDaySelected = 1;
//    		rangeIsBeingSet = true;
    		
    		if(selectedDays[1] < selectedDays[0]){
    			clearCalendar();

    		}
    		
    	}
        else{
        	rangeIsSet = true;
        }
        
	}
	
	function attemptToAddDate2(dateText){
		
//		dateText = getDate(dateText);
		
		if(!isDateAlreadySelected(dateText)){        	
			addDay(dateText);
        }
		
	}
	
	function attemptToAddDate(dateText, callback){
		
//		dateText = getDate(dateText);
		
		if(isDateAlreadySelected(dateText)){        	
        	selectedDays = removeDay(dateText);        	
		}
		else{
			addDay(dateText);
        }
		
//		callback();
		if(typeof callback === "function") callback();
		
	}

function isDateInRange(dateText){
	if(dateText.getTime() >= selectedDays[0].getTime() && dateText.getTime() <= selectedDays[1].getTime()){
		return true;
	}
	else{
		return false;
	}
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





	function clearCalendar(){				
		resetVars();		
		removeActiveDaysFormatting();
		
	}
	
	

	function removeActiveDaysFormatting(){
		var activeDays = $("#calendarContainer").find(".active111");			
		$(activeDays).each(function(){
			$(this).removeClass("active111");
		})
	}
	
	
	function getInsertBeforeElement(newDate){
		
		var $e = null;
		
		//loop through all days added
		$("#times").find(".time").each(function(){
			
			var thisDate = parseInt($(this).attr("data-date"));
			
			//If the date is larger
			if(thisDate > newDate){
				
				//Insert before this time
				$e = $(this);
				
				//break from .each loop
				return false;
			}
		})
	
		if($e != null){
			return $e;
		}else{
			//The new date is greater than all dates already added
			return null;	
		}
		
		
	
	}
	
	function isSecondDaySelected(){
		if(selectedDays.length >= 2){
			return true;
		}
		else{
			return false;
		}
	}
	
	function resetVars(){
		selectedDays = [];
//		firstworkDay = {};
//		secondworkDay = {};
//		isSecondDaySelected = 0;
//		isMoreThanTwoDaysSelected = 0;	
		rangeIsSet = 0;
//		rangeIsBeingSet = 0;
	}
	
	function setAllDatesAsUnselectable($calendar, request){
		
		if(request == true){
			$.each($calendar.find("td"), function(){
				$(this).addClass("ui-datepicker-unselectable");
			})	
		}
		else{
			$.each($calendar.find("td"), function(){
				$(this).removeClass("ui-datepicker-unselectable");
			})
		}
		
		
	}
	
	function getSelectedDate(calendarContainer){
		
		// *************************************************
		// Update this to use getDateFromTdElement
		// *************************************************		
		
		var td = $(calendarContainer).find(".active111")[0];
		var month;
		var year;
		var day;
		if(td == undefined){
			return null;
		}
		else{
			year = $(td).attr("data-year");
			month = parseInt($(td).attr("data-month"));
			day = $(td).children().html();
			return  new Date(year, month, day);
		}
		
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
	
	function getDateFromTdElement(td){
		var year = $(td).attr("data-year");
		var month = parseInt($(td).attr("data-month"));
		var day = $(td).children().html();
		return  new Date(year, month, day);
	}
	
	
	function addSelectedDays(dateStrings){
		
		var date;
		
		$(dateStrings).each(function(){
			date = new Date(this);
			addDay(date.getTime());
		})
		
	}

