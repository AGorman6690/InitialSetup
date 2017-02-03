var className_approvedFilter = ".approved-filter";

$(document).ready(function(){
	$(".sort-direction").click(function(){
		executeAjaxCall_sortFilteredJobs(this);
	})
})



function getFilterValue2(filterContainer){
	
	var filterValueElement = $(filterContainer.find(".filter-value").eq(0));
	var filterValue = "";
	var slectedDate;
	
	if($(filterValueElement).hasClass("calendar-single-date")){
		filterValue = getSelectedDate(filterValueElement);
	}
	else if($(filterValueElement).is("select")){
		filterValue = $(filterValueElement).find("option:selected").eq(0).attr("data-filter-value");
	}
	else if($(filterValueElement).is("input[type=text]")){
		filterValue = $(filterValueElement).val();
	}
	
	return filterValue;
}

function getSelectedRadioValue(filterContainer){
	return parseInt($(filterContainer).find("input[type=radio]:checked").eq(0).attr("data-is-before"));
}

function getJsonObject_findJobFilterDTO(){
	
	var dateformatString = "yy-mm-dd" //i.e. 2017-01-13
	
	var dto = {};
	
	dto.savedName = $("#saveFilterName").val();
	dto.city = $("#city").val();
	dto.state = $("#state").val();
	dto.zipCode = $("#zipCode").val();
	dto.emailFrequencyId = getEmailFrequencyId();
	dto.radius = $("#radius").val();
	
	if(isFilterApplied($("#startTime"))){
		dto.startTime = getFilterValue2($("#startTime"));
		dto.beforeStartTime = getSelectedRadioValue($("#startTime"));
	}
	
	if(isFilterApplied($("#startDate"))){
		dto.startDate = $.datepicker.formatDate(dateformatString, getFilterValue2($("#startDate")));
		dto.beforeStartDate = getSelectedRadioValue($("#startDate"));	
	}
	
	if(isFilterApplied($("#endTime"))){
		dto.endTime = getFilterValue2($("#endTime"));
		dto.beforeEndTime = getSelectedRadioValue($("#endTime"));
	}
	
	if(isFilterApplied($("#endDate"))){
		dto.endDate = $.datepicker.formatDate(dateformatString, getFilterValue2($("#endDate")));
		dto.beforeEndDate = getSelectedRadioValue($("#endDate"));	
	}
	
	if(isFilterApplied($("#duration"))){
		dto.duration = getFilterValue2($("#duration"));
		dto.isShorterThanDuration = getSelectedRadioValue($("#duration"));	
	}
	
//	if(isFilterApplied($("#workDays"))){
//		
//		var selectedDates = getSelectedDates($("#workDaysCalendar"), "yy-mm-dd");
//		$(selectedDates).each(function(){
//			
//		})
//		dto.duration = getFilterValue2($("#duration"));
//		dto.isShorterThanDuration = getSelectedRadioValue($("#duration"));	
//	}
	
	return dto;
	
}

function getEmailFrequencyId(){
	return $("#emailFrequencyContainer").find("input[type=radio]:checked").eq(0).attr("value");
}

function getFullAddress(){	

	var arr = [];
	var city = "";
	var state = "";
	var zipCode = "";
	
	city = $("#city").val();
	state = $("#state").val()
	zipCode = $("#zipCode").val()
	
	arr.push(city);
	arr.push(state);
	arr.push(zipCode);
	
	return buildStringFromArray(arr);
}

function getUrlParameters(initialUrlParameterString, isAppendingJobs){
	
	// *****************************************************
	// *****************************************************
	// Tidy this up.
	// This might be able to be combined with getJsonObject_findJobFilterDTO().
	// See the note in the job controller.
	// *****************************************************	
	// *****************************************************
	
	var urlParameter = initialUrlParameterString;
	var filterValue = "";
	var parameterName = "";
	var $filterContainer;
	var $dropdown;
	var dateformatString = "yy-mm-dd" //i.e. 2017-01-13
	var arr = []
	var address = "";
	var radius = "";
	var city = "";
	var state = "";
	var zipCode = "";


	address = getFullAddress();
	radius = $("#radius").val();
	city = $("#city").val();
	state = $("#state").val()
	zipCode = $("#zipCode").val()
	
	// Full Address			
	parameterName = "fromAddress";
	filterValue = address;	
	urlParameter += "&" + parameterName + "=" + filterValue;
	
	// City
	if(city != ""){
		parameterName = "city";
		filterValue = city;	
		urlParameter += "&" + parameterName + "=" + filterValue;	
	}
	
	// State
	if(state != ""){
		parameterName = "state";
		filterValue = state;	
		urlParameter += "&" + parameterName + "=" + filterValue;
	}
	
	// Zip Code
	if(zipCode != ""){
		parameterName = "zipCode";
		filterValue = zipCode;	
		urlParameter += "&" + parameterName + "=" + filterValue;
	}
	
	// Radius
	parameterName = "radius";
	filterValue = radius;	
	urlParameter += "&" + parameterName + "=" + filterValue;	
	
	// Appending jobs flag		
	parameterName = "isAppendingJobs";
	filterValue = isAppendingJobs;		
	urlParameter += "&" + parameterName + "=" + filterValue;			
	
	// Start time
	$filterContainer = $("#startTime");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "startTime";
		filterValue = $dropdown.find("option:selected").eq(0).attr("data-filter-value");
		urlParameter += "&" + parameterName + "=" + filterValue;		
		
		parameterName = "beforeStartTime";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
		urlParameter += "&" + parameterName + "=" + filterValue;
	}
	
	// End time
	$filterContainer = $("#endTime");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "endTime";
		filterValue = $dropdown.find("option:selected").eq(0).attr("data-filter-value");
		urlParameter += "&" + parameterName + "=" + filterValue.toString();		
		
		parameterName = "beforeEndTime";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
		urlParameter += "&" + parameterName + "=" + filterValue;
	}
		
	// Duration
	$filterContainer = $("#duration");
	if(isFilterApplied($filterContainer)){

		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "duration";
		filterValue = getFilterValue($dropdown);
		urlParameter += "&" + parameterName + "=" + filterValue;		
		
		parameterName = "isShorterThanDuration";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");	
		urlParameter += "&" + parameterName + "=" + filterValue;
	}
	
	// Start date
	$filterContainer = $("#startDate");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "startDate";
		filterValue = getSelectedDate($($dropdown.find(".calendar-single-date")[0]));
		filterValue = $.datepicker.formatDate(dateformatString, filterValue);
		urlParameter += "&" + parameterName + "=" + filterValue;		
		
		parameterName = "beforeStartDate";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
		urlParameter += "&" + parameterName + "=" + filterValue;
	}	
	
	// End date
	$filterContainer = $("#endDate");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "endDate";
		filterValue = getSelectedDate($($dropdown.find(".calendar-single-date")[0]));
		filterValue = $.datepicker.formatDate(dateformatString, filterValue);
		urlParameter += "&" + parameterName + "=" + filterValue;		
		
		parameterName = "beforeEndDate";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
		urlParameter += "&" + parameterName + "=" + filterValue;
	}			
	
	if(isFilterApplied($("#workDays"))){
		
		var selectedDates = getSelectedDates($("#workDaysCalendar"), "yy-mm-dd");
		$(selectedDates).each(function(){			
			urlParameter += "&d=" + this;			
		})

	}
	
	return urlParameter;
	
}

function areAddressAndRadiusValid(){

	var validAddress = 1;
	var validRadius = 1;
	var errorMessage;
	
	var address = "";
	var radius = "";
	var city = "";
	var state = "";
	var zipCode = "";

	address = getFullAddress();	
	city = $("#city").val();
	state = $("#state").val()
	zipCode = $("#zipCode").val()
	
	radius = $("#radius").val();
	
	// Address
	if(address == "" || address == undefined) validAddress = 0;	
	
	if(validAddress == 0) ;
	else $("#locationErrorMessage").hide();
	
	if(validAddress == 0){
		$("#locationErrorMessage").show()
		setInvalidCss($("#city"));
		setInvalidCss($("#state"));
		setInvalidCss($("#zipCode"));
	}
	else{
		$("#locationErrorMessage").hide()
		setValidCss($("#city"));
		setValidCss($("#state"));
		setValidCss($("#zipCode"));		
	}
	
	// Radius
	if(radius == "" || radius == undefined) validRadius = 0;
	else if(!isValidNumberGreaterThan0(radius)) validRadius = 0;	

	if(validRadius == 0){
		$("#radiusErrorMessage").show();
		setInvalidCss($("#radius"))
	}
	else {
		$("#radiusErrorMessage").hide();
		setValidCss($("#radius"));
	}
	
	if(validAddress == 0 || validRadius == 0) return false;
	else return true;
	
}

function isAtleastOneFilterApplied(){
	if($("#additionalFiltersContainer").find(className_approvedFilter).length == 0) return false;
	else return true;
}

function isFilterApplied($filterContainer){
	if($filterContainer.find(className_approvedFilter).length > 0) return true;
	else return false;
}

function executeAjaxCall_loadFindJobFilter(savedFindJobFilterId){
	
	$("html").addClass("waiting");
	$.ajax({
		type : "GET",
		url: '/JobSearch/jobs/find/load-filter?savedFindJobFilterId=' + savedFindJobFilterId,
		headers : getAjaxHeaders(),	
		success : _success,
		error : _error,
		cache: true
	});

	function _success(response) {
		$("html").removeClass("waiting");	
		$("#filtersContainer").html(response);

		attachEventHandles_Filters();
		triggerGetJobs();
	}	

	function _error() {
		$("html").removeClass("waiting");
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}	
	
}

function executeAjaxCall_saveFindJobFilter(findJobFilterDto){
	
	$("html").addClass("waiting");
	$.ajax({
		type : "POST",
		url: '/JobSearch/jobs/save-find-job-filter',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(findJobFilterDto),
//		dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success() {
		$("html").removeClass("waiting");	
		$("#saveFilterName").val("");
		closeModal($(".mod"));
	}	

	function _error() {
		$("html").removeClass("waiting");
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}


function executeAjaxCall_getFilteredJobs(urlParameters, doSetMap, isAppendingJobs){
	
	$("html").addClass("waiting");
	if(!$("#mainBottom").is("visible"))	$("#mainBottom").show();
	
	$.ajax({
		type : "GET",
		url: '/JobSearch/jobs/filter' + urlParameters,
		headers : getAjaxHeaders(),
		success : _success,
		error : _error,
		cache: true
	});

	function _success(response) {
		
		$("html").removeClass("waiting");		
		
		if(isAppendingJobs)	$("#getMoreJobsContainer").before(response);
		else $("#filteredJobs").html(response);		

		
		//Show the jobs and map container if this is the first job request
//		if(!$("#mainBottom").is("visible")){
//			$("#mainBottom").show();
//		}
		
//		if(doSetMap == 1){
			setMap();	
//		}
		
		//The map should not be set when sorting jobs because the same jobs will be returned,
		//they will only be displayed in a different order.
		//Because the same jobs will be returned, the map markers will remain the same.
		//Reloading the map is a bit awkward when sorting. 

		
//		sessionStorage.clear();
//		sessionStorage.setItem("doStoreFilteredJobs", doStoreFilteredJobs(response));			
//		if(sessionStorage.doStoreFilteredJobs == 1){
//			sessionStorage.setItem("filteredJobs", response);
//			sessionStorage.setItem("map", $("#map").html());
//			sessionStorage.setItem("filters", $("#filterContainer").html());
//		}
		
		
	}	

	function _error(response) {
		$("html").removeClass("waiting");
		alert('DEBUG: error set filter jobs')
		
	}
}

function executeAjaxCall_sortFilteredJobs(clickedSortDirection){
	
	// Close dropdown
	$("#sortByHeader").find("div[data-toggle-id]").eq(0).click();
	
	$("html").addClass("waiting");
	
	var isAscending = $(clickedSortDirection).attr("data-is-ascending");
	var sortBy = $(clickedSortDirection).closest(".sort-by-container").attr("data-sort-by");
	$.ajax({
		type : "GET",
			url: '/JobSearch/jobs/filtered/sort?sortBy=' + sortBy + "&isAscending=" + isAscending,
			success : _success,
			error : _error,
			cache: true
		});

	function _success(response) {
		$("html").removeClass("waiting");		
		$("#filteredJobs").html(response);	
		
		
		//***********************************************
		//***********************************************
		// Since the same jobs are being returned, just in a different order, the map
		// should not have to be reloaded.
		// Currently when sorted, it is.
		// Look into a way that can avoid it such as:
		// http://stackoverflow.com/questions/10253265/get-google-maps-map-instance-from-a-htmlelement
		//***********************************************
		//***********************************************
		setMap();

	}	

	function _error(response) {
		$("html").removeClass("waiting");
		alert('DEBUG: error set filter jobs')
		
	}

}

