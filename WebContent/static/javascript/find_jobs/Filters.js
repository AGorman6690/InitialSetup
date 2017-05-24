//var $filterContainer;
var slideSpeed_filterDropdowns = 500;
var initialUrlParameterString = "?";

var selectedDays_workDaysFilter = []
var selectedDay_endDate = [];
var selectedDay_startDate = [];

$(document).ready(function(){
	attachEventHandles_Filters();
	// triggerGetJobs();
	
	if($("#mapContainer").attr("data-init-map-on-load") == "1"){
		
		$("#mainBottom").show();
		setMap();

	}
	
	$("body").on("click", ".show-cal-mod", function() {
		var jobId = $(this).closest(".job").attr("data-job-id");
		var $calendar = $(this).closest(".job").find(".mod-body .calendar").eq(0);
		executeAjaxCall_getJobWorkDays(jobId, $calendar);
	})
		
	$(window).scroll(function(){
		
		$e = $("#headerRow");
//		alert($e.position().top + ", " + $e.outerHeight(true));
		
		console.log("current pos:" + $(document).scrollTop() + "top:" + $e.position().top + ", bottom: " + $e.position().top + ", " + $e.outerHeight(true))
	})
})

function executeAjaxCall_getJobWorkDays(jobId, $calendar) {
	$.ajax({
		type: "GET",
		url: "/JobSearch/job/" + jobId + "/work-days",
		headers: getAjaxHeaders(),
		dataType: "json",			
	}).done(function(workDayDtos){
		initCalendar_new($calendar, workDayDtos);
		$calendar.closest(".mod").show();
	})
}

function attachEventHandles_Filters(){
	
	$(".approve-filter").click(function(){
		approveFilter(this);
		
	})
	
	$(".trigger-dropdown").click(function(e){		
		e.stopPropagation();
		$filterContainer = getParent_FilterContainer($(this));
		
		var dropdownId = $(this).attr("data-trigger-dropdown-id");
		
		if(isDropdownVisible(this))	showDropdown($filterContainer, false);
		else showDropdown($filterContainer, true);
		
		closeOtherDropdowns(dropdownId);
	
	})
	
	$(".remove-filter").click(function(){
		removeFilter(this);
	})
	
	$("input, select").on("change keyup", function(){
		$filterContainer = getParent_FilterContainer($(this));
		if(isErrorMessageDisplayed($filterContainer)){
			areFilterInputsValid($filterContainer);
		}
	})
		
	
	$("#getJobs").click(function(){
		getJobs(0);
		

	})
		
	
	$("html").click(function(e){
		
		// Close all dropdowns if user clicked outside of a dropdown		
		if($(e.target).closest(".dropdown").length == 0 && !$(e.target).hasClass("dropdown")){
			
			// For some reason, if a datepicker's "prev" or "next" buttons are clicked,
			// the ".closest(".dropdown").length" will return 0 even though this element
			// has a .dropdown element as an ancestor...
			if($(e.target).parent().hasClass("ui-datepicker-prev") == 0 &&
				$(e.target).parent().hasClass("ui-datepicker-next") == 0 &&
				
				$(e.target).hasClass("ui-datepicker-prev") == 0 &&
				$(e.target).hasClass("ui-datepicker-next") == 0){
				
				closeOtherDropdowns("");
			}
			
		}
	})


	
	$("#approveSaveFilter").click(function(){
		approveSaveFilter();
	})
	
	$("#clearAllFilters").click(function(){
		clearAllFilters();
	})
	
	$(".saved-find-job-filter span").click(function(){
		loadFindJobFilter($(this).attr("data-id"));
	})
	
	$("#clearWorkDays").click(function(){
		selectedDays_workDaysFilter = [];
		$("#workDaysCalendar").datepicker("refresh");
	})
	
	
	
	
	$("#showSaveFilter").click(function(){
		
		if($(this).hasClass("not-logged-in") == 1){
			showErrorMessage_NotLoggedIn("You must be logged in to save " +
											"a find-job filter");
		}
		else{
			showSaveFilterModal();	
		}
				
	})


	
	$("#loadSavedFilterContainer.not-logged-in").click(function(){
		
		showErrorMessage_NotLoggedIn("You must be logged in to load a " +
									"previously saved find-job filter");

	})
	
	$("#mustBeLoggedIn .error-message").click(function(){
		
	})
	
//	
//	$("#okFilterWorkingDays").click(function(){	
//		approveWorkDays();
//	})
	
	
//	$("#loadSaveFilter").hover(function(){
//		$(this).click();
//	})
	
	initializeTimeAndDateControls();
	setInitialValues_TimeAndDates(approveFiltersOnLoad);
}

function showErrorMessage_NotLoggedIn(errMessage){
	
	$error = $("#mustBeLoggedIn .error-message");
	$error.html(errMessage);
	$error.show();
	
	$("#mustBeLogeedIn").show();

	$("#load_save").css("visibility", "hidden");
}


function loadFindJobFilter(savedFindJobFilterId){
	executeAjaxCall_loadFindJobFilter(savedFindJobFilterId)
}

function showSaveFilterModal(){
	if(areAddressAndRadiusValid()){
		$("#saveModal").show();	
		$("#saveFilterName").focus();
	}	
}

function clearAllFilters(){
	var activeFilters = $("#filtersContainer").find(".remove-filter");
	
	$.each(activeFilters, function(){
		$(this).click();
	})
}

function approveSaveFilter(){
	
	var findJobFilterDto = getJsonObject_findJobFilterDTO();
	if(areInputsValid_ApproveSaveFilter(findJobFilterDto)){		
		executeAjaxCall_saveFindJobFilter(findJobFilterDto)
	}
}

function areInputsValid_ApproveSaveFilter(findJobFilterDto){

	var invalidCount = 0;
	
	if(findJobFilterDto.savedName == ""){
		setInvalidCss($("#saveFilterName"));
		invalidCount += 1;
	}
	else if(getEmailFrequencyId() == undefined){
		invalidCount += 1;
	}

	if(invalidCount == 0) return true;
	else return false;
}

function triggerGetJobs(){
	// On page load, attempt to trigger "Get Jobs".
	// If the page loaded with a filterDto not equal to null, then
	// this attribute will equal 1.
	$("#getJobs[data-click-on-load=1]").click();
}

function approveFiltersOnLoad(){
	$("span.approve-filter[data-click-on-load=1]").each(function(){
		$(this).click();
	})	
}

function setInitialValues_TimeAndDates(callback){
	
	
	// Set the initial values for the time dropdowns
	$("select.time").each(function(){
		var initTime = $(this).attr("data-init-time") + ":00";
		var option = $(this).find("option[data-filter-value='" + initTime + "']")[0];
		$(option).prop("selected", true);
	})
	
	// Set calendars' initial date
	$(".calendar-single-date").each(function(){
		
		var string_initDate = $(this).attr("data-init-date").replace(/-/g, "/");
		var initDate;
		var td;
		if(initDate != -1){
			
			initDate = new Date(string_initDate);
			
			// This will display the calendar on the correct date.
			// The following getTd will fail if the date is in a future month.
			$(this).datepicker("setDate", $.datepicker.formatDate("mm/dd/yy", initDate));
		
			
			td = getTdByDayMonthYear($(this), initDate.getDate(),
										initDate.getMonth(), initDate.getFullYear());
			
			$(td).addClass("active111");

		}
	})
	
	// Once the controls are set, approve the specified filters
	if(typeof callback === "function") callback();

}

function closeOtherDropdowns(dropdownIdToExclude){
	
	$.each($("#filtersContainer").find(".dropdown"), function(){
		if($(this).attr("id") != dropdownIdToExclude) slideUp($(this), slideSpeed_filterDropdowns);
	})
	
}

function getJobs(isAppendingJobs){
	
	if(areAddressAndRadiusValid()){
		
		var urlParams = getUrlParameters(initialUrlParameterString, isAppendingJobs);
		executeAjaxCall_getFilteredJobs(urlParams, 1, isAppendingJobs);
		
//		// Verify the user set at least one filter parameter
//		if(urlParams != initialUrlParameterString) {
//			
//		}
	}

}

function isErrorMessageDisplayed($container){
	return $container.find(".error-message").eq(0).is(":visible");
}

function removeFilter(clickedIcon){
	resetText(clickedIcon);
	clearAllInputs(getParent_FilterContainer($(clickedIcon)));
	showRemoveFilterIcon(clickedIcon, false);
	addApprovedFilterClass($filterContainer, false);
	resetGlobalVars(clickedIcon);
}

function resetGlobalVars(clickedIcon){
	$filterContainer = getParent_FilterContainer($(clickedIcon));
	var id = $filterContainer.attr("id"); 
	
	if(id == "workDays") selectedDays_workDaysFilter = [];
	else if(id == "startDate") selectedDay_startDate = [];
	else if(id == "endDate") selectedDay_endDate = [];
}



function resetText(clickedIcon){
	$filterContainer = getParent_FilterContainer($(clickedIcon));
	var textElement = $filterContainer.find("span.filter-text").eq(0); 
	var textToShow = $(textElement).attr("data-reset-text");
	$(textElement).html(textToShow);	
}

function isDropdownVisible(clickedTriggerDropdown){
	
	$filterContainer = getParent_FilterContainer($(clickedTriggerDropdown));
	var dropdown = $filterContainer.find(".dropdown")[0];
	
	return $(dropdown).is(":visible");
}

function showDropdown($filterContainer, request){
//	$filterContainer = getParent_FilterContainer($(clickedTriggerDropdown));
	var dropdown = $filterContainer.find(".dropdown")[0];
	
	if(request) slideDown($(dropdown), slideSpeed_filterDropdowns);
	else slideUp($(dropdown), slideSpeed_filterDropdowns);
}

function getParent_FilterContainer($e){
	return $($e.parents().closest(".filter-container"));
}

function approveFilter(clickedGlyphicon){
	
	$filterContainer = getParent_FilterContainer($(clickedGlyphicon));
		
	if(areFilterInputsValid($filterContainer)){
			
		var dropdown = $filterContainer.find(".dropdown")[0];
		var textToShow = getTextToShow($(dropdown));
		
		showApprovedFilterText(textToShow, clickedGlyphicon);		
		showRemoveFilterIcon(clickedGlyphicon, true);		
		showDropdown($filterContainer, false);
		addApprovedFilterClass($filterContainer, true);
		
	}	
}

function addApprovedFilterClass($filterContainer, request){
	var dropdownHeader = $filterContainer.find(".dropdown-header").eq(0);
	if(request) $(dropdownHeader).addClass("approved-filter");
	else $(dropdownHeader).removeClass("approved-filter");
}


function areFilterInputsValid($filterContainer){
	
	var $errorMessage = $filterContainer.find(".error-message").eq(0);
	
	if(areInputsValid_Container($filterContainer)){
		$errorMessage.hide();
		return true;
	}
	else{		
		$errorMessage.html("Please set all filter inputs");
		$errorMessage.show();
		return false;
	}
}

function showRemoveFilterIcon(clickedGlyphicon, request){
	$filterContainer = getParent_FilterContainer($(clickedGlyphicon));
	var removeFilterIcon = $filterContainer.find(".remove-filter")[0];
	
	if(request){
		$(removeFilterIcon).show();
	}
	else{
		$(removeFilterIcon).hide();
	}
}

function showApprovedFilterText(textToShow, clickedGlyphicon){
	$filterContainer = getParent_FilterContainer($(clickedGlyphicon));
	var filterText = $filterContainer.find(".filter-text");
	$(filterText).html(textToShow);
}

function getTextToShow($dropdown){
//	var inputText = "";
	var root = "";
//	var suffix = "";
	var radioSelection = "";
	
	var filterValue = "";
//	var prefix = "";
	var units = "";
	var arr = [];
	
	root = $dropdown.attr("data-text-root");
	radioSelection = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-text-radio-selection");
	filterValue = getFilterValue($dropdown);
	units = $dropdown.attr("data-units");
	
	
	if(filterValue == 1 && units == "days selected") units = "day selected";
	
	arr.push(root);
	arr.push(radioSelection);
	arr.push(filterValue);
	arr.push(units);
		
	return buildStringFromArray(arr);

}

function getFilterValue($dropdown){
	var filterValueElement = $dropdown.find(".filter-value")[0];
	var filterValue = "";
	var slectedDate;
	
	if($(filterValueElement).hasClass("calendar-single-date")){
		slectedDate = getSelectedDate($(filterValueElement));
		filterValue = $.datepicker.formatDate("D m/d", slectedDate);
	}
	if($(filterValueElement).hasClass("calendar-multi-date")){
		filterValue = $(filterValueElement).find("td.active111").length;
	}
	else if($(filterValueElement).is("select")){
		filterValue = $(filterValueElement).find("option:selected").eq(0).html();
	}
	else if($(filterValueElement).is("input[type=text]")){
		filterValue = $(filterValueElement).val();
	}
	
	return filterValue;
}

function initializeTimeAndDateControls(){
	
	// Populate time select boxes
	setTimeOptions($("#startTimeOptions"), 60);
	setTimeOptions($("#endTimeOptions"), 60);
	
	// Date pickers
	initWorkDaysCalendar();
	initStartAndEndDateCalendars();
}

function initStartAndEndDateCalendars(){
	
	$("#endDateCalendar").datepicker({
		minDate: new Date(),
		numberOfMonths: 1, 
		onSelect: function(dateText, inst) {	    
            var date = new Date(dateText);            
            selectedDay_endDate = addOrRemoveDate_SingeDateCalendar(date, selectedDay_endDate);	 
		},		        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {
        	if(isDateAlreadySelected(date, selectedDay_endDate)) return [true, "active111"];
        	else return [true, ""];	        	
        }
    });	
	
	$("#startDateCalendar").datepicker({
		minDate: new Date(),
		numberOfMonths: 1, 
		onSelect: function(dateText, inst) {	    
            var date = new Date(dateText);            
            selectedDay_startDate = addOrRemoveDate_SingeDateCalendar(date, selectedDay_startDate);	 
            
		},		        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {
        	if(isDateAlreadySelected(date, selectedDay_startDate)) return [true, "active111"];
        	else return [true, ""];	        	
        }
    });	
	
	
	
	
}

function initWorkDaysCalendar(){
	
	$("#workDaysCalendar").datepicker({
			minDate: new Date(),
			numberOfMonths: 2, 
			onSelect: function(dateText, inst) {	        
				selectedDays_workDaysFilter = onSelect_multiDaySelect_withRange(dateText, selectedDays_workDaysFilter);

			},		        
	        // This is run for every day visible in the datepicker.
	        beforeShowDay: function (date) {
	        	return beforeShowDay_ifSelected(date, selectedDays_workDaysFilter);
	        }
	    });	
}

