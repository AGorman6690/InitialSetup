var workDayDtos = [];
var filterJobDto = {};

$(document).ready(function(){	
	
	$(document).keypress(function(e) {
	    if(e.which == 13) {
	        getJobs();
	    }
	});
	
	$("body").on("click", "#get-more-jobs", function(){
		var findJobsRequest = getFindJobsRequest();	
		findJobsRequest.isAppendingJobs = true;		
		findJobsRequest.alreadyLoadedJobIds = getAlreadyLoadedJobIds();
		executeAjaxCall_getFilteredJobs(findJobsRequest, true);
	})
	
	$("#location-filter input").focus(function() {
		this.select();
	})
//	$("#address").focus();
	
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
		getJobs();
	})
	
	$("#applied-filters").on("click", "button", function() {
		var $e = $(this);
		clearFilterValue($e);
		$e.remove();
		getJobs();
	})
	$(".apply-filter-container select").change(function() {
		var $e = $(this)
		var value = $e.find("option:selected").attr("data-filter-value")
		setAppliedFilterValue($e, value);
		getJobsAfterApplyingFilter($e, value)
	})
	$(".apply-filter-container input[type=text]").keyup(function() {
		var $e = $(this)
		var value = $e.val();
		setAppliedFilterValue($e, value);
		getJobsAfterApplyingFilter($e, value)
	})
	$(".apply-filter-container input[type=radio]").change(function() {
		var $e = $(this)
		var value = getAppliedFilterValue($e);
		getJobsAfterApplyingFilter($e, value);
	})	
	$(".remove-filter").click(function() {
		var $e = $(this).closest(".dropdown-style"); 
		$e.attr("data-is-approved", 0);
		$(this).hide();		
	})
	$(".filter .clear").click(function() {
		clearFilterValue($(this))
	})
	
	$(".time").each(function(){
		setTimeOptions($(this), 60);
	})
		
	initCalendar_selectSingleDate($("#end-date-filter .calendar"));
	initCalendar_selectSingleDate($("#start-date-filter .calendar"));	
	initCalendar_selectWorkDays($("#work-days-filter .calendar"), undefined, 1);
		
	if($("#wrapper").hasClass("find-jobs-on-load") == 1){
		getJobs();
	}
	
})
function getAppliedFilterValue($e){
	return $e.closest(".filter").attr("data-value");
}
function setAppliedFilterValue($e, value){
	$e.closest(".filter").attr("data-value", value);
}
function getJobsAfterApplyingFilter($e, value){	
	if(value != "" && value != undefined){
		getJobs($e)
		$e.closest(".filter").addClass("applied")
	}
}
function getAppliedFilter($e){
	return $e.closest(".filter");
}
function getJobs($triggeredByFilter){
	var findJobsRequest = getFindJobsRequest();	
	findJobsRequest.isAppendingJobs = false;
	executeAjaxCall_getFilteredJobs(findJobsRequest, true);
}
function clearFilterValue($e){
	var $filter = $e.closest(".filter")
	$filter.removeClass("applied")
	$filter.find(".calendar td.selected").each(function() {
		$(this).removeClass("selected");
	})
	$filter.find("select option").eq(0).prop("selected", true)
	$filter.find("input").each(function() {
		$(this).val("");
	})
	getJobs();
}
function getAlreadyLoadedJobIds(){
	var jobIds = [];
	$("#get-jobs-results .job").each(function() {
		jobIds.push($(this).attr("data-job-id"));
	})
	return jobIds;
}
function applyFilter($e) {
	var html_appliedFilter = "";
	
	var filterTextSuffix
	var filterText = null;
	var filterValue = null;
	var filterWrapperId = $e.closest(".dropdown-style").attr("id");

	
	var $filter = $e.closest(".filter");
	$filter.find(".remove-filter").show();
		
	// Filter value
	if($filter.find(".calendar").length){
		var $calendar = $filter.find(".calendar");
		filterValue = getSelectedDate($calendar, "D MM d", "selected");
	}else if($filter.find("select").length){
		var $option = $filter.find("select option:selected").eq(0);
		filterValue = $option.html();
	}else if($filter.find("input[type=text]").length){ 
		var $input = $filter.find("input[type=text]").eq(0);
		filterValue = $input.val();
	}
	else{
		filterValue = null;
	}
	
	// Filter text	
	if($filter.find(".radio-container").length){
		var $radio = $filter.find("input[type=radio]:checked").eq(0);
		filterText =  $radio.attr("data-filter-text");

	}else{
		return null;
	}
	filterTextSuffix = $filter.attr("data-filter-text-suffix");	
	
	// delete existing button if filter is already applied
	$("#applied-filters").find("button[data-filter-wrapper-id='" + filterWrapperId + "']").eq(0).remove();
	
	// Show applied filter
	if(filterValue != null && filterText != null){
		html_appliedFilter = "";
		html_appliedFilter = "<button data-filter-wrapper-id=" + filterWrapperId +
				"><p>"	+ filterText + " " + filterValue;
		
		if(filterTextSuffix != undefined){
			html_appliedFilter += " " + filterTextSuffix;		
		}		
		html_appliedFilter += "</p></button>";
		$("#applied-filters").append(html_appliedFilter);
	}
	
	$filter.find(".filter-name").click();

//	var $appliedFilter = $filter.find(".applied-filter").eq(0);
//	$appliedFilter.html(filterText + " " + filterValue);
//	$appliedFilter.show();

}
function executeAjaxCall_getFilteredJobs(findJobsRequest, doSetMap){
	
	if(validateInputElements($("#location-filter"))){
		broswerIsWaiting(true);
		var isAppendingJobs = findJobsRequest.isAppendingJobs;
		
		$.ajax({
			type : "POST",
			url: '/JobSearch/jobs/find/request',
			headers : getAjaxHeaders(),
			contentType : "application/json",
			data : JSON.stringify(findJobsRequest),		
			dataType: "html"
		}).done(function(html){		
			broswerIsWaiting(false);
			var $e = $("#get-jobs-results");	
			$("#get-jobs-results-cont").show();
			$("#other-filters").show();
	//		$("#wrapper").removeClass("find-jobs-on-load");
			if(isAppendingJobs){
				if(html.indexOf("RETURN_NO_MORE_JOBS") != -1){
					$e.addClass("no-more-jobs");
					doSetMap = false;				
				}
				else{
					$("#find-jobs-response").append(html);
				}
			}else{
				$e.removeClass("no-more-jobs");
				$e.html(html);	
			}				
			if(doSetMap) setMap_renderFindJobsResults();		
			renderStars($e);
		})
	}
}
function getFindJobsRequest() {
	
	var dates = [];
	var findJobsRequest = {};
	findJobsRequest.dates = [];
	var isBefore;
	
	findJobsRequest.address =  $("#address").val();
	findJobsRequest.radius = $("#miles").val();
	
	findJobsRequest.stringStartDate = getSelectedDate($("#start-date-cal"), "yy-mm-dd", "selected");
	findJobsRequest.isBeforeStartDate = getRadioValue($("#start-date-filter"));
	
	findJobsRequest.stringEndDate = getSelectedDate($("#end-date-cal"), "yy-mm-dd", "selected");
	findJobsRequest.isBeforeEndDate = getRadioValue($("#end-date-filter"));

	findJobsRequest.stringStartTime = getSelectedTime($("#start-time-select"));
	findJobsRequest.isBeforeStartTime = getRadioValue($("#start-time-filter"));
	
	findJobsRequest.stringEndTime = getSelectedTime($("#end-time-select"));
	findJobsRequest.isBeforeEndTime = getRadioValue($("#end-time-filter"));
	
	findJobsRequest.duration = $("#duration-filter input[type=text]").val();
	findJobsRequest.isShorterThanDuration = getRadioValue($("#duration-filter"));
	
	return findJobsRequest;		
}
function getSelectedTime($select){
	return $select.find("option:selected").attr("data-filter-value");
}
function getRadioValue($container){
	return parseInt($container.find(".radio-container input[type=radio]:checked").attr("data-parameter-value"));
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