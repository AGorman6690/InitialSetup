var selectedDays = [];
var dates_employeeAvailability = [];

$(document).ready(function(){
	
	// ********************************************************
	// ********************************************************
	// Address how the employer-initiated wage proposal fits into the
	// application-initated wage proposal.
	// Make the html and js more robust.
	// This is kludged 
	// ********************************************************
	// ********************************************************
	$("#makeOfferModal .accept-employer").click(function(){
		
		var applicationDto = {};
		applicationDto.application = {};
		applicationDto.wageProposal = {};
		
		applicationDto.jobId = $("#makeOfferModal select option:selected").attr("data-job-id");
		applicationDto.applicantId = $("#makeOfferModal").attr("data-user-id");
		applicationDto.wageProposal.amount = $("#makeOfferModal input#amount").val();

		var $acceptActionsContainer = $(this).parents(".accept-actions-container").eq(0);
		applicationDto.days_offerExpires = $acceptActionsContainer.find(".time-container input.days-pre-hire").val();
		applicationDto.hours_offerExpires = $acceptActionsContainer.find(".time-container input.hours-pre-hire").val();
		applicationDto.minutes_offerExpires = $acceptActionsContainer.find(".time-container input.minutes-pre-hire").val();

		
		broswerIsWaiting(true);
		$.ajax({
			type : "POST",
			url :"/JobSearch/employer/offer/wage-proposal",
			headers : getAjaxHeaders(),
			contentType : "application/json",	
			data: JSON.stringify(applicationDto),
			dataType : "json"
			
		}).done(function(){
			broswerIsWaiting(false);
		})
		
	})
	
	$("#makeOfferModal select").change(function(){
		showApplicationStatus_ProspectiveEmployee();
	})
	

	
	
	$("#resultsContainer").on("click", "span.show-make-offer-modal", function(){
		
		var $tr = $(this).closest("tr");
		var prospectiveEmployeeId = $tr.attr("data-user-id");
		
		$("#makeOfferModal").attr("data-user-id", prospectiveEmployeeId);
		
		$("#makeOfferTo_name").html($tr.attr("data-user-name"));
		
		$("#makeOfferModal").show();		
	})
	
	$("#resultsContainer").on("click", "td span.toggle-availability-calendar", function(){
		
		var $td = $(this).closest("td").eq(0);
		var $calendarContainer = $td.find(".calendar-container").eq(0);
		var $calendar = $calendarContainer.find(".calendar").eq(0);
		
		if($calendarContainer.is(":visible")){
			$calendarContainer.hide();
		}
		else{
			// Only set the calendar once
			if($calendar.hasClass("hasDatepicker") == 0){
				dates_employeeAvailability = [];
				$td.find(".dates div[data-date]").each(function(){
					dates_employeeAvailability.push(new Date($(this).attr("data-date").replace(/-/g, "/")));
				})
				initCalendar_employeeAvailability($calendar);
				
				$calendar.datepicker("setDate", new Date($("#calendarSpecs").attr("data-first-date").replace(/-/g, "/")));
			}		
			
			$calendarContainer.show();
		}

		toggleClasses($(this).find(".glyphicon").eq(0), "glyphicon-menu-up", "glyphicon-menu-down");
		
		
	})
	
	$("#resultsContainer").on("mouseout", "td.days-available .calendar-container", function(){
		
//		$(this).hide();
		
	})
	
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

function showApplicationStatus_ProspectiveEmployee(){
	
	var jobId = $(this).find("option:selected").eq(0).attr("data-job-id");
	var prospectiveEmployeeId = $("#makeOfferModal").attr("data-user-id");
	
	$.ajax({
		type: "GET",
		url: "/JobSearch/application/" + jobId + "/user/" + prospectiveEmployeeId + "/status",
		
	}).done(function(applicationStatus){
		 
		var message = "";
		var userName = $("#makeOfferTo_name").html();
		
		switch (applicationStatus) {
		case "-1":
			message = "You have already proposed an application to " + userName + " for this job.";
			break;

		case "0":
		case "2":
		case "4":
			message = userName + " has an open appliation for this job.";			
			break;
			
		case "1":
			message = userName + "'s application has already been declined for this job.";				
			break;
			
		case "3":
			message = userName + " has already been hired for this job.";
			break;
			
		case "5":
			message = userName + " has already applied for this job."
						+ " His application was withdrawn due to a time conflict with his other employment.";
			break;
			
		default:
			break;
		}
		
		
		if(message != ""){
			$("#makeAnOffer_applicationStatus").html(message);
		}
		
	})
}

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

function initCalendar_employeeAvailability($calendar){
	
	$calendar.datepicker({
		numberOfMonths: parseInt($("#calendarSpecs").attr("data-number-of-months")),
		 beforeShowDay: function (date) {
			 return beforeShowDay_findEmployees_ifUserHasAvailability(
					 						date, selectedDays, dates_employeeAvailability);
		 }
	})
	
}
