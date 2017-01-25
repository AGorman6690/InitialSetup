
var selectedDays = [];
var selectedDay;
var initDate;


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
		
		
	})
	

	
	initializeCalendar();
	
	initializeSingeDateCalendars();
	
	
})

function initializeSingeDateCalendars(){
	
	$(".calendar-single-date").datepicker({
		minDate: new Date(),
		numberOfMonths: 1,
		// defaultDate: new Date($(this).attr("data-init-date").replace("-", "/")),
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
        	initDate = initDate.replace("-", "/");

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

function initMultiDayCalendar($e){
	var numberOfMonths = getNumberOfMonths($e);
	
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
		
		return modifiedSelectedDays = $.grep(selectedDays, function(day){
    		return day != dateToRemove;
    	})		

	}

	function addDay(dateToAdd){
		selectedDays.push(dateToAdd);
	}
	
	function isDateAlreadySelected(dateToCheck){
		
		var arr = [];
		
		arr = $.grep(selectedDays, function(date){
			return date == dateToCheck;
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
	
	function attemptToAddDate(dateText, callback){
		
		dateText = getDate(dateText);
		
		if(isDateAlreadySelected(dateText)){        	
        	selectedDays = removeDay(dateText);        	
		}
		else{
			addDay(dateText);
        }
		
		callback();
		
	}

function isDateInRange(dateText){
	if(dateText >= selectedDays[0] && dateText <= selectedDays[1]){
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
	

