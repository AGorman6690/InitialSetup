var $filterContainer;
var slideSpeed_filterDropdowns = 500;
var initialUrlParameterString = "?";

$(document).ready(function(){
	attachEventHandles_Filters();
	triggerGetJobs();
})

function attachEventHandles_Filters(){
	
	$("#filtersContainer").on("click", ".approve-filter", function(){
		approveFilter(this);
		
	})
	
	$("#filtersContainer").on("click", ".trigger-dropdown", function(e){		
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
		getJobs(1);
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
	
	initializeFilterControls(approveFiltersOnLoad);
}

function loadFindJobFilter(savedFindJobFilterId){
	executeAjaxCall_loadFindJobFilter(savedFindJobFilterId)
}

function showSaveFilterModal(){
	if(areAddressAndRadiusValid(getFullAddress(), $("#radius").val())){
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

function initializeFilterControls(callback1){
	
	// Populate time select boxes
	setTimeOptions($("#startTimeOptions"), 60);
	setTimeOptions($("#endTimeOptions"), 60);
	
	// Set calendars' initial date
	$(".calendar-single-date").each(function(){
		var initDate = $(this).attr("data-init-date");
		var date = new Date(initDate.replace("-", "/"));
		if(initDate != undefined){
			$(this).datepicker("setDate", date);
		}
	})
	
	// Once the controls are set, approve the specified filters
	if(typeof callback1 === "function") callback1();

}

function closeOtherDropdowns(dropdownIdToExclude){
	
	$.each($("#additionalFiltersContainer").find(".dropdown"), function(){
		if($(this).attr("id") != dropdownIdToExclude) slideUp($(this), slideSpeed_filterDropdowns);
	})
	
}

function getJobs(){
	
	var urlParams = getUrlParameters(initialUrlParameterString);
	
	if(urlParams != initialUrlParameterString) executeAjaxCall_getFilteredJobs(urlParams, 1);

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

function setTimeOptions($eSelect, increment){
	
	// For whatever reason the Local Time object will not apped
	// the seconds if the seconds are zero...
	var initTime = $eSelect.attr("data-init-time") + ":00";
	var selected = "";
	var formattedTime = "";
	
	if(increment > 0){
			
		$eSelect.empty();
		$eSelect.append('<option value="" selected style="display: none"></option>');
		
		var hourCount;
		var hour;
		var minute;
		var modifiedMinute;
		var amPm;
		var time;
		//Hour
		hour = 12;
		for(hourCount = 1; hourCount < 25; hourCount++){

			//Am or pm
			if(hourCount <= 12){
				amPm = "am";
			}else{
				amPm = "pm"
			}
			
			//Minute
			for(minute = 0; minute < 60; minute += increment){
// 					alert("minute: " + minute + " " + increment)
				if(minute < 10){
					modifiedMinute = "0" + minute;	
				}else{
					modifiedMinute = minute;
				}	
				
				time = hour + ":" + modifiedMinute + amPm;
				formattedTime = formatTime(time);
				
				if(formattedTime == initTime) selected = "selected";
				else selected = "";
				
				$eSelect.append("<option data-filter-value='" + formattedTime + "' " + selected + ">"
									+ time + "</option>");
			}
			
			//Incerment the hour
			if(hour == 12){
				hour = 1;
			}else{
				hour ++;
			}				 
		}
	}
}