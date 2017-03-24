var selectedDays = [];
var dates_employeeAvailability = [];
var dates_selectedAvailability_makeOffer;


$(document).ready(function(){
	
	// ********************************************************
	// ********************************************************
	// Address how the employer-initiated wage proposal fits into the
	// application-initated wage proposal.
	// Make the html and js more robust.
	// This is kludged 
	// ********************************************************
	// ********************************************************
	$("#makeOfferModal #sendOffer").click(function(){
		
		executeAjaxCall_sendOffer();

	})
	
	$("#job-i-might-post").click(function(){
		$("#filtersContainer").show();
//		$(this).hide();
	})
	
	$("#makeOfferModal #sendInvite").click(function(){
		
		executeAjaxCall_sendInvite();

	})
	
	
	$("#selectJob_initiateContact select").change(function(){
		
//		if($("#makeAnOffer").hasClass("selected-green")){
			var jobId = $(this).find("option:selected").eq(0).attr("data-job-id");;
			showApplicationStatus_ProspectiveEmployee(jobId);	
//		}
		
	})
	

	
	
	$("#resultsContainer").on("click", "span.show-make-offer-modal", function(){
		
		resetModal();
		
		var $tr = $(this).closest("tr");
		var prospectiveEmployeeId = $tr.attr("data-user-id");
		var prospectiveEmployeeName = $tr.attr("data-user-name");
		
		$("#makeOfferModal").attr("data-user-id", prospectiveEmployeeId);
		
		// Show the clicked employee's name
		$("#makeOfferModal").find("span.make-offer-to-name").each(function(){
			$(this).html(prospectiveEmployeeName);
		})
		

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
	
	$("#posted-jobs [data-posted-job-id]").click(function(){
		
		var selectedJobId = $(this).attr("data-posted-job-id");
		
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
		
		executeAjaxCall_findEmployees();
		
	})
	
	$("#makeAnOffer").click(function(){
//		$("#detailsContainer_makeAnOffer").show();
		$("#selectJob_initiateContact").show();
		$("#actionsContainer_initiateContact #sendInvite").hide();
		$("#actionsContainer_initiateContact #sendOffer").show();
	})
	
	$("#inviteToApply").click(function(){
		$("#detailsContainer_makeAnOffer").hide();
		$("#selectJob_initiateContact").show();
		$("#actionsContainer_initiateContact #sendInvite").show();
		$("#actionsContainer_initiateContact #sendOffer").hide();
	})
	
	setStates();
	initAvailabilityCalendar();

	$("button.clear").click(function(){
		clearCalendar($("#makerOffer_workDaysCalendar"), "apply-selected-work-day");
		dates_selectedAvailability_makeOffer = [];
	})
	
})


function executeAjaxCall_findEmployees(){
	var jobDto = {};
	jobDto.job = {};
	
	jobDto.job.streetAddress = $("#street").val();
	jobDto.job.city = $("#city").val();
	jobDto.job.state = $("#state").val();
	jobDto.job.zipCode = $("#zipCode").val();
	
	jobDto.workDays = getWorkDays_FromSelectedDates($("#availabilityCalendar"), "yy-mm-dd");
	
//	if($("#partialAvailabilityAllowed").is(":checked")) jobDto.job.isPartialAvailabilityAllowed = 1;
//	else jobDto.job.isPartialAvailabilityAllowed = 0;
	jobDto.job.isPartialAvailabilityAllowed = 1;
	
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
}

function executeAjaxCall_sendOffer(){
	
	var applicationDto = {};
	applicationDto.application = {};
	applicationDto.employmentProposalDto = {};
	
	applicationDto.jobId = $("#makeOfferModal select option:selected").attr("data-job-id");
	applicationDto.applicantId = $("#makeOfferModal").attr("data-user-id");
	applicationDto.employmentProposalDto.amount = $("#makeOfferModal input#amount").val();
	applicationDto.employmentProposalDto.dateStrings_proposedDates = getSelectedDates($("#makerOffer_workDaysCalendar"), 
										"yy-mm-dd", "apply-selected-work-day");

	var $detialsContainer = $("#detailsContainer_makeAnOffer");
	applicationDto.employmentProposalDto.days_offerExpires = $detialsContainer.find(".time-container input.days-pre-hire").val();
	applicationDto.employmentProposalDto.hours_offerExpires = $detialsContainer.find(".time-container input.hours-pre-hire").val();
	applicationDto.employmentProposalDto.minutes_offerExpires = $detialsContainer.find(".time-container input.minutes-pre-hire").val();

	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/employer/initiate-contact",
		headers : getAjaxHeaders(),
		contentType : "application/json",	
		data: JSON.stringify(applicationDto),
		dataType : "json"
		
	}).done(function(){
		broswerIsWaiting(false);
	})	
}

function executeAjaxCall_sendInvite(){
	
	var applicationInvite = {};

	
	applicationInvite.jobId = $("#makeOfferModal select option:selected").attr("data-job-id");
	applicationInvite.userId = $("#makeOfferModal").attr("data-user-id");
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url :"/JobSearch/employer/initiate-contact/application-invite",
		headers : getAjaxHeaders(),
		contentType : "application/json",	
		data: JSON.stringify(applicationInvite),
		dataType : "json"
		
	}).done(function(){
		broswerIsWaiting(false);
	})	
}

function resetModal(){
	$("#inviteToApply").removeClass("selected-green");
	$("#makeAnOffer").removeClass("selected-green");
	$("#selectJob_initiateContact").hide();
	$("#detailsContainer_makeAnOffer").hide();
	$("#actionsContainer_initiateContact").hide();
	$("#selectJob_initiateContact select").val("");
	$("#makeAnOffer_applicationStatus").hide();
}

function showApplicationStatus_ProspectiveEmployee(jobId){
	
	var prospectiveEmployeeId = $("#makeOfferModal").attr("data-user-id");
	
	// Get the user's application status for the particular job
	$.ajax({
		type: "GET",
		url: "/JobSearch/application/" + jobId + "/user/" + prospectiveEmployeeId + "/status",
		dataType: "json",
	}).done(function(jobDto){
		
		var attemptingToInvite;
		 
		// Per the applicaiton status, show a message to the employer
		var message = "";
		var userName = $(".mod-header .make-offer-to-name").html();
		var canInitiateContact = false;
		
		switch (jobDto.applicationStatus) {
		case -1:
			message = "You have already proposed an application to " + userName + " for this job.";
			break;

		case 0:
		case 2:
		case 4:
			message = userName + " has an open appliation for this job.";				
			break;
			
		case 1:
			message = userName + "'s application has already been declined for this job.";				
			break;
			
		case 3:
			message = userName + " has already been hired for this job.";
			break;
			
		case 5:
			message = userName + " has already applied for this job."
						+ " His application was withdrawn due to a time conflict with his other employment.";
			break;
			
		default:
			canInitiateContact = true;
			break;
		}
		
		var $e = $("#makeAnOffer_applicationStatus");
		if(message != ""){
			$e.html(message);
			$e.show();
		}
		else{
			$e.hide();
		}
		
		
		if($("button#inviteToApply").hasClass("selected-green")) attemptingToInvite = true;
		else attemptingToInvite = false;
		
		
		if(canInitiateContact){
			
			// Show/hide controls
			$("#actionsContainer_initiateContact").show();			
			if(attemptingToInvite){				
				$("#detailsContainer_makeAnOffer").hide();
				$("#sendOffer").hide();
				$("#sendInvite").show();				
			}
			else{
				$("#detailsContainer_makeAnOffer").show();
				$("#sendOffer").show();
				$("#sendInvite").hide();
			}
			
			// If the job allows partial availability,
			// then the employer has to select the days he wishes the
			// prospective employee to work.
			$calendarContainer = $("#makeOfferModal .calendar-container");
			if(jobDto.job.isPartialAvailabilityAllowed){
				
				dates_selectedAvailability_makeOffer = [];
				var dates_possibleAvailability = getDaysFromWorkDays(jobDto.workDays, dates_possibleAvailability);
				var $calendar = $("#makerOffer_workDaysCalendar");
				
				// Destroy the datepicker if it is present
				if($calendar.hasClass("hasDatepicker")) $calendar.datepicker("destroy");

				// Set the date picker and show the job's work days
				$calendar = setDataAttributes_calendarContainer_byJobDto($calendar, jobDto);		
				initCalendar_selectAvailability($calendar, dates_selectedAvailability_makeOffer, dates_possibleAvailability);

				$calendarContainer.show();
			}
			else $calendarContainer.hide();			
		}
		else{
			$("#actionsContainer_initiateContact").hide();	
			$("#detailsContainer_makeAnOffer").hide();
		}
		

		
	})
}

function showJobInfo(jobDto){
	
	$("#filtersContainer").show();
	
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
