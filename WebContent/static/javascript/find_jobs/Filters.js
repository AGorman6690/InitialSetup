var $filterContainer;
var slideSpeed_filterDropdowns = 500;
var initialUrlParameterString = "?";

$(document).ready(function(){
	attachEventHandles_Filters();
	// triggerGetJobs();
	
	if($("#mapContainer").attr("data-init-map-on-load") == "1"){
		$("#mainBottom").show();
		setMap();

	}
})



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
		if($(e.target).closest(".dropdown").length == 0 && !$(e.target).hasClass("dropdown")){
			closeOtherDropdowns("");
		}
	})

	$("#showSaveFilter").click(function(){
		showSaveFilterModal();

		
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
	
//	$("#loadSaveFilter").hover(function(){
//		$(this).click();
//	})
	
	initializeTimeAndDateControls();
	setInitialValues_TimeAndDates(approveFiltersOnLoad);
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
		var initDate = $(this).attr("data-init-date");
		var date = new Date(initDate.replace("-", "/"));
		if(initDate != undefined){
			$(this).datepicker("setDate", date);
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
		slectedDate = getSelectedDate(filterValueElement);
		filterValue = $.datepicker.formatDate("D m/d", slectedDate);
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
	initializeSingeDateCalendars();
}

