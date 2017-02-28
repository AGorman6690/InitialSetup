function onSelect_multiDaySelect_withRange(dateText, days){
	    
        var date = new Date(dateText);
        
        if(days.length == 1){
        	if(date.getTime() == days[0].getTime()) days = removeDay(date, days);
        	else attemptToAddDateRange(date, days);
        }
        else {
        	days = addOrRemoveDate(date, days);	            	
        }
        
        return days;
        				
}


function beforeShowDay_ifSelected(date, days){
	if(isDateAlreadySelected(date, days)) return [true, "active111"];
	else return [true, ""];	   
}

function beforeShowDay_findEmployees_ifUserHasAvailability(
			date, selectedDays, dates_employeeAvailability){
	// This is to for the find employees page.
	// If the user (i.e. the returned prospective employee) as availability
	// on the requested date, then the calendar date will be green.
	// If the user is not available, then the calendar date will be red.
	
	
	// Check if date is in the particular array of dates
	if(isDateAlreadySelected(date, dates_employeeAvailability)) return [true, 'is-available'];
	else if(isDateAlreadySelected(date, selectedDays)) return [true, 'is-not-available'];
	else return [true, ""];
	
}

function onSelect_multiDaySelect_noRange(dateText, days){
 
	var date = new Date(dateText);
    
//    if(days.length == 1){
//    	if(date.getTime() == days[0].getTime()) days = removeDay(date, days);
//    	else attemptToAddDateRange(date, days);
//    }
//    else { 
    	days = addOrRemoveDate(date, days);	            	
//    }
    
    return days;	
}


function beforeShowDay_Availability(month, date){

	var doAddClass_active111 = false;
	var doAddClass_saved = false;
	
	// Has the user already saved this date
	if(isDateAlreadySavedAsAvailable(date)){	
		
		// If the user has NOT toggled off the already-saved date
		if(isDateSetToBeRemovedFromDatabase(date) == 0){
			doAddClass_active111 = true;
			doAddClass_saved = true;
		}		
	}
	else{
		
		if(isDateAlreadySelected(date, selectedDays)){
			doAddClass_active111 = true;
		}
		
	}		
	
	// If this date is the calendar being set (i.e. the correct month) and the
	// date is not selected, the unmark the corresponding day checkbox
	if(doAddClass_active111 == false && date.getMonth() == month){
		var dayOfWeek = date.getDay();
		$("#daysContainer").find("input[data-day-of-week=" + dayOfWeek + "]").eq(0).prop("checked", false);
		$("#daysContainer").find("input.select-all").eq(0).prop("checked", false);
	}
	
	
	// If showing an "already-saved" date, then this checkbox should not be selected
	if(doAddClass_saved){
		$("#removeCurrentAvailability").prop("checked", false);
	}	
	
	// Return the correct classes to apply to the date
	if(doAddClass_active111){
		if(doAddClass_saved) return [true, "active111 saved"];
		else return [true, "active111"];
	}
	else return [true, ""];
	
}


function onSelect_Availability(dateText) {	

	// Initially set all days to checked.
	// The BeforeShowDay function will unmark the necessary checkboxes.
	changeSelectAllCheckbox($("#daysContainer"), true, false);
		
	var date = new Date(dateText);
	
	// Check if the date is already saved as available by the user.
	/// By "saved" I mean "in the database" .
	if(isDateAlreadySavedAsAvailable(date)){	
		
		if(isDateSetToBeRemovedFromDatabase(date)){
			
			// Set it back to "do not remove from database"
			setDateToBeRemovedFromDatabase(date, 0);
			
			// Add date
			attemptToAddDate(date, selectedDays);
		}
		else{
			// Set it back to "remove from database"
			setDateToBeRemovedFromDatabase(date, 1);
			
			// Remove date
			selectedDays = removeDay(date, selectedDays);	
		}	
		
	}
	else{
		selectedDays = addOrRemoveDate(date, selectedDays);
	}
		
}