var className_approvedFilter = ".approved-filter";

$(document).ready(function(){
	$(".sort-direction").click(function(){
		sortJobs(this);
	})
})

function sortJobs(clickedSortDirection){
	
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

function getUrlParameters(){
	
	var urlParameter = "?";
	var filterValue = "";
	var parameterName = "";
	var $filterContainer;
	var $dropdown;
	var dateformatString = "yy-mm-dd" //i.e. 2017-01-13
	var arr = []
	var address = "";
	var radius = "";
	
	arr.push($("#city").val());
	arr.push($("#state").val());
	arr.push($("#zipCode").val());
	address = buildStringFromArray(arr);
	
	radius = $("#radius").val();

	if(areAddressAndRadiusValid(address, radius)){
//		if(isAtleastOneFilterApplied()){
			
			// Address			
			parameterName = "fromAddress";
			filterValue = address;	
			urlParameter += "&" + parameterName + "=" + filterValue;
			
			// Radius
			parameterName = "radius";
			filterValue = radius;	
			urlParameter += "&" + parameterName + "=" + filterValue;	
			
			// Appending jobs flag		
			parameterName = "isAppendingJobs";
			filterValue = 0;		
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
				urlParameter += "&" + parameterName + "=" + filterValue;		
				
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
				
				parameterName = "isLessThanDuration";
				filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-shorter-than");	
				urlParameter += "&" + parameterName + "=" + filterValue;
			}
			
			// Start date
			$filterContainer = $("#startDate");
			if(isFilterApplied($filterContainer)){
				
				$dropdown = $($filterContainer.find(".dropdown").eq(0));
				
				parameterName = "startDate";
				filterValue = getSelectedDate($dropdown.find(".calendar-single-date")[0]);
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
				filterValue = getSelectedDate($dropdown.find(".calendar-single-date")[0]);
				filterValue = $.datepicker.formatDate(dateformatString, filterValue);
				urlParameter += "&" + parameterName + "=" + filterValue;		
				
				parameterName = "beforeEndDate";
				filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
				urlParameter += "&" + parameterName + "=" + filterValue;
			}			
//		}
	}
	
	return urlParameter;
	
}

function areAddressAndRadiusValid(address, radius){

	var validAddress = 1;
	var validRadius = 1;
	var errorMessage;
	
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

function executeAjaxCall_getFilteredJobs(urlParameters, doSetMap){
	
	$("html").addClass("waiting");
	$.ajax({
		type : "GET",
			url: '/JobSearch/jobs/filter' + urlParameters,
			success : _success,
			error : _error,
			cache: true
		});

	function _success(response) {
		$("html").removeClass("waiting");
		
		$("#filteredJobs").html(response);		

		//Show the jobs and map container if this is the first job request
		if(!$("#mainBottom").is("visible")){
			$("#mainBottom").show();
		}
		
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

