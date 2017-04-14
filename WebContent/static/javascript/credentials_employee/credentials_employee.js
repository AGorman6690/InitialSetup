var dates_editedAvailability = []

$(document).ready(function(){
	
	$("#make-edits").click(function(){
		$(this).hide();
		$("#save-edits").show();
		$(".edit-container").each(function(){ $(this).show() });
		$("#personalInfo .value").each(function(){ $(this).hide() });
	})
	
	$("#save-edits").click(function(){
		
		executeAjaxCall_updateUserSettings();
	})	
	
	
//	$("#saveHomeLocation").click(function(){
//		var user_edited = {};
//		user_edited.homeCity = $("#city").val();;
//		user_edited.homeState = $("#state option:selected").val();;
//		user_edited.homeZipCode = $("#zipCode").val();
//		
//		executeAjaxCall_updateUserSettings(user_edited);
//	})
//	
//	$("#saveMaxDistance").click(function(){
//		var user_edited = {};
//		user_edited.maxWorkRadius = $("#miles").val();;
//		
//		executeAjaxCall_updateUserSettings(user_edited);
//	})
//	
//	$("#saveMinimumPay").click(function(){
//		var user_edited = {};
//		user_edited.minimumDesiredPay = $("#dollarsPerHour").val();;
//		
//		executeAjaxCall_updateUserSettings(user_edited);
//	})	
	
	$(".cancel-changes").click(function(){
		$(this).closest(".edit-container").find("input, select").each(function(){
			$(this).val("");
		})
		
		$(this).closest(".edit-container").siblings("[data-toggle-id]").eq(0).click();
	})
	
	
	$(".days-of-week-container input").change(function(){
		disableFirstAndLastCalendars();
		selectCalendarDays_byDaysOfWeek();
	})
	
	$(document).on("click", ".ui-datepicker-prev span, .ui-datepicker-next span", function(){
		selectCalendarDays_byDaysOfWeek();
	})
	
	
	$("#availabilityCalendar").datepicker({
		minDate: new Date(),
		numberOfMonths: 1, 
		onSelect: function(dateText, inst) {	    
			
		},		        
        beforeShowDay: function (date) {        	
        	return [true, ""];
     	}
	})

	
	setStates();
	
	
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



function executeAjaxCall_updateUserSettings(user_edited){
	
	var user_edited = {};
	
	user_edited.homeCity = $("#city").val();;
	user_edited.homeState = $("#state option:selected").val();;
	user_edited.homeZipCode = $("#zipCode").val();
	user_edited.maxWorkRadius = $("#miles").val();;
	user_edited.minimumDesiredPay = $("#dollarsPerHour").val();;	
	
	
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