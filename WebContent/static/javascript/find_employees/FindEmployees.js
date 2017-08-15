var selectedDays = [];
var dates_employeeAvailability = [];
var dates_selectedAvailability_makeOffer;

var workDayDtos= [];
var workDayDtos_original = [];
$(document).ready(function(){
	
	
	initCalendar_selectWorkDays($(".calendar"), undefined, 1);
	
	$(".clear").click(function() {
		$(this).closest(".filter-value-container").find("input:checked").eq(0).prop("checked", false);
		executeAjaxCall_findEmployees();
	})
	
	$(".filter-value-container input," +
			" #apply-availability-filter").click(function() {
		executeAjaxCall_findEmployees();
	})
	$("body").on("click", ".make-an-offer", function() {
		var userId = $(this).attr("data-user-id");
		executeAjaxCall_makeAnOffer_initialize(userId);
	})
	
	$("body").on("click", ".select-a-job [data-job-id]", function() {
		var userId_makeOfferTo = $(this).closest(".select-a-job").attr("data-user-id");
		var jobId = $(this).attr("data-job-id");
		executeAjaxCall_verifyAvailability(userId_makeOfferTo, jobId);
	})
	
		
	$("#clear-work-day-filter").click(function(){	
		
		workDayDtos = [];
		workDayDtos_original = [];
		$(".calendar").datepicker("destroy");
		initCalendar_selectWorkDays($(".calendar"), undefined, 1);
		executeAjaxCall_findEmployees()
	})
	
	$("#find-employees").click(function(){
		executeAjaxCall_findEmployees();
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



})
function executeAjaxCall_verifyAvailability(userId, jobId) {
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: "/JobSearch/user/" + userId + "/verify-availability/job/" + jobId,
		dataType: "text",
		headers: getAjaxHeaders()
	}).done(function(text) {
		broswerIsWaiting(false);
		$e = $("#unavailable-message");
		if(text == "available"){
			$e.hide();
			executeAjaxCall_makeOffer(userId, jobId);
		}else{
			$e.show();
			$span = $e.find("span").eq(0);
			if(text == "unavailable"){
				$span.html("is unavailable.");
			}else if(text == "already-applied"){
				$span.html("already applied for this job.");
			}
		}
	})
}

function executeAjaxCall_makeOffer(userId, jobId) {
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: "/JobSearch/user/" + userId + "/make-offer/job/" + jobId,
		dataType: "html",
		headers: getAjaxHeaders(),
	}).done(function(html) {
		broswerIsWaiting(false);
		var $e = $("#make-offer-modal");
		$e.empty();
		$e.append(html);
		$e.find(".mod").show();
		
		g_workDayDtos_originalProposal = JSON.parse($e.find("#json_workDayDtos").eq(0).html());
		$.extend(true, g_workDayDtos_counter, g_workDayDtos_originalProposal);

		initCalendar_new($e.find(".calendar"), g_workDayDtos_originalProposal);
		
		broswerIsWaiting(false);
		
		$("#select-a-job .mod").hide();
		
	})
}

function executeAjaxCall_makeAnOffer_initialize(userId) {
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: "/JobSearch/user/" + userId + "/make-offer/initialize",
		dataType: "html",
		headers: getAjaxHeaders(),
	}).done(function(html) {
		broswerIsWaiting(false);
		var $e = $("#select-a-job");
		$e.html(html);
		$e.find(".mod").show();
		

		
	})
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

	employeeSearch.radius = $("#miles").val();
	employeeSearch.address = $("#address").val();
	employeeSearch.dates = getSelectedDates($(".calendar"), "yy-mm-dd", "active111");
	
	employeeSearch.minimumRating = $("#rating-filter-value").find("input:checked").eq(0).val();
	employeeSearch.minimumJobsCompleted = $("#jobs-completed-filter-value").find("input:checked").eq(0).val();
	
	
	broswerIsWaiting(true);
	$.ajax({
		type: "POST",
		url: "/JobSearch/find/employees/results",
		contentType: "application/json",
		headers : getAjaxHeaders(),
		data: JSON.stringify(employeeSearch),
		dataType: "html",
	}).done(function(html){
		broswerIsWaiting(false);
		$e = $("#results");
		$e.empty();
		$e.append(html);

		renderStars($e);

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
