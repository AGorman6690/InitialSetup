function getAjaxParameters(){
	
	var ajaxParameter = "?";
	var filterValue = "";
	var parameterName = "";
	var $filterContainer;
	var $dropdown;
	
	// Start time
	$filterContainer = $("#startTime");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "startTime";
		filterValue = getFilterValue($dropdown);
		ajaxParameter += parameterName + "=" + filterValue;		
		
		parameterName = "beforeStartTime";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
		ajaxParameter += "&" + parameterName + "=" + filterValue;
	}
	
	// End time
	$filterContainer = $("#endTime");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "endTime";
		filterValue = getFilterValue($dropdown);
		ajaxParameter += "&" + parameterName + "=" + filterValue;		
		
		parameterName = "beforeEndTime";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-before");		
		ajaxParameter += "&" + parameterName + "=" + filterValue;
	}
	
	
	// Duration
	$filterContainer = $("#duration");
	if(isFilterApplied($filterContainer)){
		
		$dropdown = $($filterContainer.find(".dropdown").eq(0));
		
		parameterName = "duration";
		filterValue = getFilterValue($dropdown);
		ajaxParameter += "&" + parameterName + "=" + filterValue;		
		
		parameterName = "isLessThanDuration";
		filterValue = $dropdown.find("input[type=radio]:checked").eq(0).attr("data-is-shorter-than");		
		ajaxParameter += "&" + parameterName + "=" + filterValue;
	}
	
	
}

function isFilterApplied($filterContainer){
	if($filterContainer.find(".approved-filter").length > 0) return true;
	else return false;
}