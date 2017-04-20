var selectedDays = [];
var dates_employeeAvailability = [];
var dates_selectedAvailability_makeOffer;

var workDayDtos= [];
var workDayDtos_original = [];
$(document).ready(function(){
	
	initPage();
	
	
	
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
		$("#what-kind-of-job-container").hide();
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
		var $e_renderHtml = $(this).siblings(".make-offer-container").eq(0);
		var jobId = $("#jobId_getOnPageLoad").val();
		
		if($e_renderHtml.find(".calendar.hasDatepicker").length == 0)
			executeAjaxCall_getMakeOfferModal(jobId, $e_renderHtml);
		else 
			$e_renderHtml.find(".mod").show();

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
//		selectedDays = resetCalendar_hoverRange(selectedDays, $("#availabilityCalendar"));

		resetCal_findEmplyees();
		
		
	})
	
	$("#posted-jobs [data-posted-job-id]").click(function(){
		
		var selectedJobId = $(this).attr("data-posted-job-id");
		
		executeAjaxCall_loadJobDto(selectedJobId);
		
		$("#what-kind-of-job-container").hide();
	})
	
	$("#findEmployees").click(function(){
//		
		executeAjaxCall_findEmployees();
//		$("#resultsContainer").show();
//		$('html, body').animate({
//	        scrollTop: $("#results").offset().top -100
//	    }, 2000);
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
	var workDayDtos = [];
//	initCalendar_findEmployees_workDaysFilter($("#availabilityCalendar"), workDayDtos);
	initCalendar_selectWorkDays($(".calendar"), undefined, 2);
	$("button.clear").click(function(){
		clearCalendar($("#makerOffer_workDaysCalendar"), "apply-selected-work-day");
		dates_selectedAvailability_makeOffer = [];
	})
	
})


function executeAjaxCall_getMakeOfferModal(jobId, $e){
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: "/JobSearch/job/" + jobId + "/make-an-offer/initialize",
		headers: getAjaxHeaders(),
		dataType: "html",
		success: function(html) {
			$e.empty();
			$e.html(html);
			$e.find(".mod").show();
			workDayDtos = JSON.parse($("#json_workDayDtos").html());
			$(workDayDtos).each(function() {
				this.isProposed = "0";
			})
			var $calendar = $(".calendar.counter-calendar");
			initCalendar_new($calendar, workDayDtos);	
//			$calendar.find("tr").show();
			$(".counter-container").show();
			broswerIsWaiting(false);
		},
		error: function(html) {
			broswerIsWaiting(false);
		},
	})
}

function initPage(){
	var jobId_getOnPageLoad = $("#jobId_getOnPageLoad").val();
	
	if(jobId_getOnPageLoad != "") executeAjaxCall_loadJobDto(jobId_getOnPageLoad);
}

function resetCal_findEmplyees(){
	var $calendar = $("#availabilityCalendar");
	workDayDtos = [];	
	
	$calendar.datepicker("refresh");
	$calendar.removeClass("show-hover-range");	
}

function onSelect_ShowHoverRange($calendar, selectedDateText, selectedDays){

	var isThisTheFirstDateSelected = false;
	var isThisTheSecondDateSelected = false;
	
	if(selectedDays.length == 0) isThisTheFirstDateSelected = true;
	if(selectedDays.length == 1) isThisTheSecondDateSelected = true;
	
	selectedDays = onSelect_multiDaySelect_withRange(selectedDateText, selectedDays);
				
	if(isThisTheFirstDateSelected){
		$calendar.addClass("show-hover-range");
		$calendar.attr("data-first-date", selectedDateText);
	}
	else $calendar.removeClass("show-hover-range");
	
	if(selectedDays.length == 0){
		selectedDays = [];
		$calendar.datepicker("refresh");
		$calendar.removeClass("show-hover-range");		
	}
	
	return selectedDays;
}

function initCalendar_findEmployees_workDaysFilter($calendar, workDayDtos){

	// ***************************************************************
	// ***************************************************************
	// Consider passing the selectedDays and not having it be global
	// ***************************************************************
	// ***************************************************************
	
	$calendar.datepicker({
		numberOfMonths: 2,
		onSelect: function(dateText, inst) {
			
			selectedDays = onSelect_ShowHoverRange($calendar, dateText, selectedDays);

		},		        
        beforeShowDay: function (date) {       
        	
        	var className = ""
        	if(isDateInWorkDayDtos(date, workDayDtos)) className += " job-work-day";
        	if(doesDateArrayContainDate(date, selectedDays)) className += " active111";        	
        	
        	return [true, className];	    
     	},
		afterShow: function(){
			var html = "";
			$(workDayDtos).each(function(){
				
				var td = getTdByDate($calendar, dateify(this.workDay.stringDate));
				
				html = "<div class='added-content'>";
				html += "<p>7:30a</p><p>5:30p</p>";
//				html += "<div class='select-work-day'>";
//				html += "<span class='glyphicon glyphicon-ok'></span>";
//				html += "</div>";	
//				html += "<div class='other-application'></div>";
				html += "</div>"
				
				$(td).append(html);
				
				var $tr = $(td).closest("tr");
				if($tr.hasClass("show-row") == 0) $tr.addClass("show-row"); 
			})	
			
		}
    });	

	
}

function executeAjaxCall_loadJobDto(jobId) {
	$.ajax({
		type: "GET",
		url: "/JobSearch/get/job-dto/" + jobId,
		dataType: "json",
		success: _success,
		error: _error,
	})
	
	function _success(jobDto){			
		showJobInfo(jobDto);
		$("#findEmployees").click();
	}

	function _error(){
		alert("error load job")
	}
}


function showJobInfo(jobDto){
	
	$("#filtersContainer").show();
	$("#job-info").show();
	$("#job-info p a").html(jobDto.job.jobName);
	
	// Set the location
	$("#street").val(jobDto.job.streetAddress);
	$("#city").val(jobDto.job.city);
	$("#state").val(jobDto.job.state);
	$("#zipCode").val(jobDto.job.zipCode);
		

	$("#availabilityCalendar").datepicker("destroy");
//	initCalendar_findEmployees_workDaysFilter($("#availabilityCalendar"), jobDto.workDayDtos);
//	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	workDayDtos = jobDto.workDayDtos;
	initCalendar_selectWorkDays($(".calendar"), undefined, 2);

}

function executeAjaxCall_findEmployees(){
	var employeeSearch = {};
	var jobDto = {};
	jobDto.job = {};
	
	jobDto.job.id = $("#jobId_getOnPageLoad").val();
	jobDto.job.streetAddress = $("#street").val();
	jobDto.job.city = $("#city").val();
	jobDto.job.state = $("#state").val();
	jobDto.job.zipCode = $("#zipCode").val();
	
	jobDto.workDays = getWorkDays_FromSelectedDates($("#availabilityCalendar"), "yy-mm-dd");
	
//	if($("#partialAvailabilityAllowed").is(":checked")) jobDto.job.isPartialAvailabilityAllowed = 1;
//	else jobDto.job.isPartialAvailabilityAllowed = 0;
	jobDto.job.isPartialAvailabilityAllowed = 1;
	
	employeeSearch.jobDto = jobDto;
	
	broswerIsWaiting(true);
	$.ajax({
		type: "POST",
		url: "/JobSearch/find/employees/results",
		contentType: "application/json",
		headers : getAjaxHeaders(),
		data: JSON.stringify(employeeSearch),
		dataType: "html",
		success: _success,
		error: _error,
	})
	
	function _success(html_findEmployee_results){
		broswerIsWaiting(false);
		$("#resultsContainer").show();
		$("#results").empty();
		$("#results").append(html_findEmployee_results);
	   
		$('html, body').animate({
	        scrollTop: $("#results").offset().top - 100
	    }, 1000);

	}
	
	function _error(){
		broswerIsWaiting(false);
	}
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




function initCalendar_employeeAvailability($calendar){
	
	$calendar.datepicker({
		numberOfMonths: parseInt($("#calendarSpecs").attr("data-number-of-months")),
		 beforeShowDay: function (date) {
			 return beforeShowDay_findEmployees_ifUserHasAvailability(
					 						date, selectedDays, dates_employeeAvailability);
		 }
	})
	
}
