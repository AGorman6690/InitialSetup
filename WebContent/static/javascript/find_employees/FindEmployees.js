var selectedDays = [];

$(document).ready(function(){
	
	$(".clear-calendar").click(function(){
		selectedDays = clearCalendar($(this).closest(".calendar-container"));
	})
	
	$("#loadCurrentJobContainer select").change(function(){
		
		var selectedJobId = $(this).find("option:selected").attr("data-job-id");
		
		$.ajax({
			type: "GET",
			url: "/JobSearch/get/job-dto/" + selectedJobId,
			dataType: "json",
			success: _success,
			error: _error,
		})
		
		function _success(jobDto){			
			showJobInfo(jobDto);
		}

		function _error(){
			alert("error load job")
		}
	})
	
	$("#findEmployees").click(function(){
		
		var jobDto = {};
		jobDto.job = {};
		
		jobDto.job.streetAddress = $("#street").val();
		jobDto.job.city = $("#city").val();
		jobDto.job.state = $("#state").val();
		jobDto.job.zipCode = $("#zipCode").val();
		
		jobDto.workDays = getWorkDays_FromSelectedDates($("#availabilityCalendar"), "yy-mm-dd");
		
		if($("#partialAvailabilityAllowed").is(":checked")) jobDto.job.isPartialAvailabilityAllowed = 1;
		else jobDto.job.isPartialAvailabilityAllowed = 0;

		
		broswerIsWaiting(true);
		$.ajax({
			type: "POST",
			url: "/JobSearch/find/employees/results",
			contentType: "application/json",
			headers : getAjaxHeaders(),
			data: JSON.stringify(jobDto),
			dataType: "html",
			success: _success,
			error: _error,
		})
		
		function _success(html_findEmployee_results){
			broswerIsWaiting(false);
			$("#resultsContainer").show();
			$("#results").empty();
			$("#results").append(html_findEmployee_results);
		}
		
		function _error(){
			broswerIsWaiting(false);
		}
		
	})
	
	setStates();
	initAvailabilityCalendar();
	
})

function showJobInfo(jobDto){
	
	// Set the location
	$("#street").val(jobDto.job.street);
	$("#city").val(jobDto.job.city);
	$("#state").val(jobDto.job.state);
	$("#zipCode").val(jobDto.job.zipCode);
	
	
	// Set the availability calendar
	selectedDays = getDaysFromWorkDays(jobDto.workDays, selectedDays);
	$("#availabilityCalendar").datepicker("refresh");
	
	
}

function initAvailabilityCalendar(){
	
	$("#availabilityCalendar").datepicker({
		numberOfMonths: 2,
		onSelect: function(dateText){
			selectedDays = onSelect_multiDaySelect_withRange(dateText, selectedDays)
		},
		 beforeShowDay: function (date) {
			 return beforeShowDay_ifSelected(date, selectedDays);
		 }
	})
	
}
