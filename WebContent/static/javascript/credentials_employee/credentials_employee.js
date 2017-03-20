var dates_editedAvailability = []

$(document).ready(function(){
	$("#saveHomeLocation").click(function(){
		var user_edited = {};
		user_edited.homeCity = $("#city").val();;
		user_edited.homeState = $("#state option:selected").val();;
		user_edited.homeZipCode = $("#zipCode").val();
		
		executeAjaxCall_updateUserSettings(user_edited);
	})
	
	$("#saveMaxDistance").click(function(){
		var user_edited = {};
		user_edited.maxWorkRadius = $("#miles").val();;
		
		executeAjaxCall_updateUserSettings(user_edited);
	})
	
	$("#saveMinimumPay").click(function(){
		var user_edited = {};
		user_edited.minimumDesiredPay = $("#dollarsPerHour").val();;
		
		executeAjaxCall_updateUserSettings(user_edited);
	})	
	
	$(".cancel-changes").click(function(){
		$(this).closest(".edit-container").find("input, select").each(function(){
			$(this).val("");
		})
		
		$(this).closest(".edit-container").siblings("[data-toggle-id]").eq(0).click();
	})
	
	initCalendar_availability();
	
	$(".days-of-week-container input").change(function(){
		disableFirstAndLastCalendars();
		selectCalendarDays_byDaysOfWeek();
	})
	
	$(document).on("click", ".ui-datepicker-prev span, .ui-datepicker-next span", function(){
		selectCalendarDays_byDaysOfWeek();
	})
	
	
})


function disableFirstAndLastCalendars(){

	$(".availability-container").addClass("editing"); 
//	$(".availability-container .ui-datepicker-group-first, .availability-container .ui-datepicker-group-last").each(function(){
//		$(this).addClass("disabled2222");
//	})
}

function selectCalendarDays_byDaysOfWeek(){
	
	
	
	
	var daysOfWeek_selected = getSelectedCheckboxesAttributeValue("days-of-week", "data-day-of-week");
	var $calendar = $(".availability-container .ui-datepicker-group-middle");
	var date;
	$calendar.find("td:not(.ui-datepicker-unselectable)").each(function(){
		date = getDateFromTdElement(this);
		
		if(doesArrayContainValue(date.getDay().toString(), daysOfWeek_selected)){
			dates_editedAvailability = attemptToAddDate(date, dates_editedAvailability);
		}
		else{
			dates_editedAvailability = removeDateFromArray(date, dates_editedAvailability);
		}
		
	})
	
	$(".availability-container .calendar").datepicker("refresh");
	
}

function initCalendar_availability(){

	var dates_applications = getDateFromContainer($("#applicationDetails"));
	var dates_employment = getDateFromContainer($("#employmentDetails"));
	var dates_available = getDateFromContainer($("#availabilityDetails"));
	
	$(".availability-container .calendar").datepicker({
		numberOfMonths: 3,
		beforeShowDay: function(date){
			
			if(doesDateArrayContainDate(date, dates_employment)) return [true, "employment"];
			else if(doesDateArrayContainDate(date, dates_applications)) return [true, "application"];
			else if(doesDateArrayContainDate(date, dates_editedAvailability)) return [true, "application edited-availability"]
			else if(doesDateArrayContainDate(date, dates_available)) return [true, "available"];
			else return [true, ""];
			
		}
	})

}

function executeAjaxCall_updateUserSettings(user_edited){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: '/JobSearch/user/settings/edit',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(user_edited),
//			dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success() {
		broswerIsWaiting(false);	
		location.reload();
	}	

	function _error() {
		broswerIsWaiting(false);	
	}
}