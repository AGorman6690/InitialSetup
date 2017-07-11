//Refer to: https://bugs.jqueryui.com/ticket/6885
//I was directed to the above link from http://stackoverflow.com/questions/6334898/jquery-datepicker-after-update-event-or-equivalent/6337622#6337622
// ***********************************************************************
// Note: It appears that parameters cannot be passed to this "afterShow" method.
// Without understanding the details of the code below, I cannot answer why.
// ***********************************************************************
$(function() {
	$.datepicker._updateDatepicker_original = $.datepicker._updateDatepicker;
	$.datepicker._updateDatepicker = function(inst) {
		$.datepicker._updateDatepicker_original(inst);
		var afterShow = this._get(inst, 'afterShow');
		if (afterShow)
			afterShow.apply((inst.input ? inst.input[0] : null));  // trigger custom callback
	}
});


$(document).ready(function() {
	
	$("body").on("click", ".calendar-container .select-all-work-days", function() {
		var $calendar = $(this).closest(".calendar-container").find(".calendar");
		selectAllWorkDays($calendar, workDayDtos)		
	})
})

function selectAllWorkDays($calendar, workDayDtos){
	
	$(workDayDtos).each(function(i, workDayDto) {
		if(workDayDto.isProposed == "0" || workDayDto.isProposed == false || workDayDto.isProposed == null){
			var td = getTdByDate($calendar, dateify(workDayDto.workDay.stringDate));
			workDayDto.isProposed = "1";
			$(td).click();
		}
	})
}

function onSelect_multiDaySelect_withRange(dateText, days){
	
	// **************************************
	// **************************************
	// Phase this out
	// **************************************
	// **************************************
	
	    
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

function onSelect_multiDaySelect_withRange_workDayDtos(dateText, workDayDtos){
    
    var date = new Date(dateText);

    if(workDayDtos.length == 1){
    	if(date.getTime() == workDayDtos[0].date.getTime())
    		workDayDtos = removeWorkDayDto(date, workDayDtos);
    	else attemptToAddDateRange_workDayDtos(date, workDayDtos);
    }
    else {
    	workDayDtos = addOrRemoveWorkDayDtoByDate(date, workDayDtos);      	
    }
    
    return workDayDtos;
    				
}

function beforeShowDay_ifSelected(date, days){
	if(isDateAlreadySelected(date, days)) return [true, "active111"];
	else return [true, ""];	   
}

function beforeShowDay_findEmployees_ifUserHasAvailability(
			date, dates_jobWorkDays, dates_applicantProposal){
	// This is to for the find employees page.
	// If the user (i.e. the returned prospective employee) as availability
	// on the requested date, then the calendar date will be green.
	// If the user is not available, then the calendar date will be red.
	
	
	// Check if date is in the particular array of dates
	if(doesDateArrayContainDate(date, dates_applicantProposal)) return [true, 'proposed'];
	else if(doesDateArrayContainDate(date, dates_jobWorkDays)) return [true, 'a-job-work-day'];
	else return [true, ""];
	
}

function beforeShowDay_counterApplicationDays(date,
		dates_application, dates_job, dates_unavailable){
	// This is to for the find employees page.
	// If the user (i.e. the returned prospective employee) as availability
	// on the requested date, then the calendar date will be green.
	// If the user is not available, then the calendar date will be red.
	
	
	// Check if date is in the particular array of dates
	if(doesDateArrayContainDate(date, dates_unavailable)) return [true, 'unavailable'];
	else if(doesDateArrayContainDate(date, dates_application)) return [true, 'proposed'];
	else if(doesDateArrayContainDate(date, dates_job)) return [true, 'a-job-work-day'];
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