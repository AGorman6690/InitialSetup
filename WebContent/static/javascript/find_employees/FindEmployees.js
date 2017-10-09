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
		var jobId = $("#job-id-on-load").val();
		executeAjaxCall_setUpEmployerToMakeInitialProposal(userId, jobId);
	})
	
	$("body").on("change", "#select-job", function() {
		executeAjaxCall_getUserApplicationStatus();
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
})
function executeAjaxCall_getUserApplicationStatus() {
	
	var userId = $("[data-user-id-make-offer-to]").attr("data-user-id-make-offer-to");
	var jobId = $("#select-job").find("option:selected").attr("data-job-id");
	
	$.ajax({
		type: "GET",
		url: "/JobSearch/user/" + userId + "/job/" + jobId + "/application-status",
		dataType: "json",
		headers: getAjaxHeaders(),
	}).done(function(userApplicationStatusResponse) {
		var $e = $("#user-application-status");
		if(userApplicationStatusResponse.hasApplied !== null && userApplicationStatusResponse.hasApplied === true ){
			$e.html(userApplicationStatusResponse.message);
			$(".proposal-content-wrapper").addClass("disable");
		}else{
			$e.html("");
			$(".proposal-content-wrapper").removeClass("disable");
		}
	})
}

function executeAjaxCall_employerMakeInitialProposal(userId, jobId) {
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

	})
}

function executeAjaxCall_setUpEmployerToMakeInitialProposal(userId, jobId) {
	
	var url = "/JobSearch/proposal/employer-make-initial-proposal/user/" + userId;
	if(jobId !== undefined && jobId !== ""){
		url += "?jobId=" + jobId;
	}
	
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: url,
		dataType: "html",
		headers: getAjaxHeaders(),
	}).done(function(html) {
		
		broswerIsWaiting(false);
		var $e = $("#make-offer-modal");
		$e.empty();
		$e.append(html);
		$e.find(".mod").show(function(){
			if(jobId !== undefined && jobId !== ""){
				var html_workDayDtos = $e.find("#json_workDayDtos").eq(0).html();				
				g_workDayDtos_originalProposal = JSON.parse(html_workDayDtos);
				$.extend(true, g_workDayDtos_counter, g_workDayDtos_originalProposal);	
				$("#select-job").find("option[data-job-id='" + jobId + "']").prop("selected", true);
				executeAjaxCall_getUserApplicationStatus();

			}			
			initCalendar_new($e.find(".calendar"), g_workDayDtos_originalProposal);
		});			
		broswerIsWaiting(false);
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

	if(validateInputElements($("#distance-filter-wrapper"))){
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
			$("#bottom-content").slideDown();
			renderStars($e);
	
		})
	}
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





function initCalendar_employeeAvailability($calendar){
	
	$calendar.datepicker({
		numberOfMonths: parseInt($("#calendarSpecs").attr("data-number-of-months")),
		 beforeShowDay: function (date) {
			 return beforeShowDay_findEmployees_ifUserHasAvailability(
					 						date, selectedDays, dates_employeeAvailability);
		 }
	})
	
}
