var workDayDtos = [];
var filterJobDto = {};

$(document).ready(function(){	
	
	$("#get-jobs-results").on("click", "#get-more-jobs", function(){
		var isAppendingJobs = true;
		var paramString = getParamString(isAppendingJobs);	
		executeAjaxCall_getFilteredJobs(paramString, true, isAppendingJobs);
	})
	
	$("#location-filter input").focus(function() {
		this.select();
	})
	$("#address").focus();
	
	$(".filter").click(function(event) {
		var filter_clicked = this;		
		$(".filter").each(function(){
			var $e = $(this);
			if(this != filter_clicked && $e.hasClass("expand")) $e.removeClass("expand");
		})
		
		// Do not close the filter if "next" or "prev" is clicked on a calendar
		if(!$(event.target).hasClass("ui-icon")){
			
			// Do not close if part of the filter dropdown was clicked.
			if(!$(event.target).closest(".dropdown-style").length){
				if($(filter_clicked).hasClass("expand")) $(filter_clicked).removeClass("expand");
				else $(filter_clicked).addClass("expand");	
			}
			
		}
	})
	

	$("#get-jobs").click(function() {	
		var isAppendingJobs = false;
		var paramString = getParamString(isAppendingJobs);	
		executeAjaxCall_getFilteredJobs(paramString, true, isAppendingJobs);
	})
	
	$("#applied-filters").on("click", "button", function() {
		$(this).remove();
		$("#get-jobs").click();
	})
	$(".apply-filter").click(function() {
		applyFilter($(this));
		$("#get-jobs").click();
	})
	
	$(".remove-filter").click(function() {
		var $e = $(this).closest(".dropdown-style"); 
		$e.attr("data-is-approved", 0);
		$(this).hide();
		
	})
	
	$(".time").each(function(){
		setTimeOptions($(this), 60);
	})
	
	$(".filter [data-toggle-id]").click(function(event) {
		var e_clicked = this;
		
	})
	
	initCalendar_selectSingleDate($("#end-date-filter .calendar"));
	initCalendar_selectSingleDate($("#start-date-filter .calendar"));	
	initCalendar_selectWorkDays($("#work-days-filter .calendar"), undefined, 1);
})
function applyFilter($e) {
	var html_appliedFilter = "";
	
	var filterTextSuffix
	var filterText = null;
	var filterValue = null;
	
	var parameterName1 = "";
	var parameterValue1 = "";
	var parameterName2 = "";
	var parameterValue2 = "";
	
	
	var $filter = $e.closest(".filter");
	$filter.find(".remove-filter").show();

	// Filter value
	if($filter.find(".calendar").length){
		var $calendar = $filter.find(".calendar");
		filterValue = getSelectedDate($calendar, "D MM d", "selected");
		
		parameterName1 = $calendar.attr("data-parameter-name");
		parameterValue1 = getSelectedDate($calendar, "yy-mm-dd", "selected");
	}else if($filter.find("select").length){
		var $option = $filter.find("select option:selected").eq(0);
		filterValue = $option.html();
		
		parameterName1 = $option.closest("select").attr("data-parameter-name");
		parameterValue1 = $option.attr("data-filter-value");
	}else if($filter.find("input[type=text]").length){ 
		var $input = $filter.find("input[type=text]").eq(0);
		filterValue = $input.val();
		
		parameterName1 = $input.attr("data-parameter-name");
		parameterValue1 = filterValue;
	}
	else{
		filterValue = null;
	}
	
	// Filter text	
	if($filter.find(".radio-container").length){
		var $radio = $filter.find("input[type=radio]:checked").eq(0);
		filterText =  $radio.attr("data-filter-text");
		
		parameterName2 = $radio.attr("data-parameter-name");
		parameterValue2 = $radio.attr("data-parameter-value");
	}else{
		return null;
	}
	filterTextSuffix = $filter.attr("data-filter-text-suffix");	
	
	// Show applied filter
	if(filterValue != null && filterText != null){
		html_appliedFilter = "";
		html_appliedFilter = "<button" +
				" data-parameter-name-1='" + parameterName1 + "'" +
				" data-parameter-value-1='" + parameterValue1 + "'" +
				" data-parameter-name-2='" + parameterName2 + "'" +
				" data-parameter-value-2='" + parameterValue2 + "'" +
				"><p>"	+ filterText + " " + filterValue;
		
		if(filterTextSuffix != undefined) html_appliedFilter += " " + filterTextSuffix;		
		
		html_appliedFilter += "</p></button>";
		$("#applied-filters").append(html_appliedFilter);
	}
	
	$filter.find(".filter-name").click();

}
function executeAjaxCall_getFilteredJobs(urlParameters, doSetMap, isAppendingJobs){
	
	broswerIsWaiting(true);
//	if(!$("#mainBottom").is("visible"))	$("#mainBottom").show();
	
	$.ajax({
		type : "GET",
		url: '/JobSearch/jobs/filter' + urlParameters,
		headers : getAjaxHeaders(),
		dataType: "html"
	}).done(function(html){		
		
		var $e_getJobsResults = $("#get-jobs-results");
		broswerIsWaiting(false);			
		
		if(isAppendingJobs){
			if(html.indexOf("RETURN_NO_MORE_JOBS") != -1){
				$e_getJobsResults.addClass("no-more-jobs");
				doSetMap = false;
			}else{
				$("#get-more-jobs").before(html);
			}
		}else{
			$e_getJobsResults.removeClass("no-more-jobs");
			$e_getJobsResults.html(html);	
		}
				
		if(doSetMap) setMap_renderFindJobsResults();
		
		renderStars($e_getJobsResults);
	})
}
function getParamString(isAppendingJobs) {
	
	var dates = [];
	var paramString = "";
	var isBefore;
	
	paramString = "?fromAddress=" + $("#address").val();
	paramString += "&radius=" + $("#miles").val();
	paramString += "&isAppendingJobs=" + isAppendingJobs;
	
	$("#applied-filters button").each(function(i, e) {
		var paramName1 = $(e).attr("data-parameter-name-1");
		var paramValue1 = $(e).attr("data-parameter-value-1");
		var paramName2 = $(e).attr("data-parameter-name-2");
		var paramValue2 = $(e).attr("data-parameter-value-2");
		
		if(paramName1 != null && paramValue1 != null){
			paramString += "&" + paramName1 + "=" + paramValue1;
		}
		if(paramName2 != null && paramValue2 != null){
			paramString += "&" + paramName2 + "=" + paramValue2;
		}
	})
	

	return paramString;	
	
}

function getAddress(){	

	var arr = [];
	var city = "";
	var state = "";
	var zipCode = "";
	
	city = $("#city").val();
	state = $("#state").val()
	zipCode = $("#zip").val()
	
	arr.push(city);
	arr.push(state);
	arr.push(zipCode);
	
	return buildStringFromArray(arr);
}
function getMoreJobs(){
	getJobs(1);
}


function addBorderToJob(jobId){	
	highlightArrayItem($("#" + jobId), $("#get-jobs-results").find(".job"), "selected-job");
}

function scrollToJob(jobId){

	var currentScrollPosition = $('#get-jobs-results').scrollTop();
	var topFilteredJobsContainer = $('#get-jobs-results').offset().top; 
	var topClickedJob = $("#" + jobId).offset().top;
	var newScrollTop;
	
	//If the current job to scroll to is already scrolled past
	if(topClickedJob < topFilteredJobsContainer){
		
		//Scroll up
		newScrollTop = currentScrollPosition - topFilteredJobsContainer + topClickedJob - 5;
	}
	else{
		//Scroll down
		newScrollTop =  currentScrollPosition + topClickedJob - topFilteredJobsContainer - 5;
	}
	$('#get-jobs-results').animate({ scrollTop: newScrollTop}, 1000);
}