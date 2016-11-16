$(document).ready(function() {
		
		var filteredStartDate = null;
		var filteredEndDate = null;
		var exitClickEvent = false;
		
		
		$("#getJobs").click(function(){
			getJobs(1);
		})
		
		$("#filterContainer").on("click", ".trigger-dropdown", function(e){
			
			//The below click event that is triggered will infinitely fire this event.
			//(i.e. the aTriggerDropdown element has the trigger-dropdown class).
			//The below code should only execute on the **user's** actual click,
			//not the click that is generated programmatically. 
			if(exitClickEvent == false){
				
				var aTriggerDropdown;
				var clickedTriggerDropdown = this;
				var visibleDropdowns = $("#additionalFiltersContainer")
										.find(".dropdown:visible");
				
				if(visibleDropdowns.length == 1){
					$.each(visibleDropdowns, function(){
						aTriggerDropdown = $(this).closest(".filter-container").find(".trigger-dropdown")[0];
						if(aTriggerDropdown != clickedTriggerDropdown){
							
							//Used to inform the click event, that will be triggered by the next line, to exit
							exitClickEvent = true;
							
							//Close this other dropdown
							$(aTriggerDropdown).trigger("click");

						}
					})
				}				
			}
			else{
				exitClickEvent = false;
			}
			

		})
		
	
		$("#filterContainer").on("click", ".remove-filter", function(){
			removeFilter(getFilterContainer($(this)));
		})
	
		$("#filterContainer").on("click", ".glyphicon-ok", function(){
			
			var filterContainer = getFilterContainer($(this));
			
			if(areApproveFilterInputsValid(filterContainer)){
				approveFilter(filterContainer);				
			}
						
		})
		
		
		$("#mainBottom").on("click", "div[data-scroll-to]", function(){
			
//			var currentScrollPosition = $('#filteredJobs').scrollTop();
//			var topFilteredJobsContainer = $('#filteredJobs').offset().top; 
//			var topClickedJob = $("#" + $(this).attr("data-scroll-to")).offset().top;
//			var newScrollTop;
//			
//			//If the current job to scroll to is already scrolled past
//			if(topClickedJob < topFilteredJobsContainer){
//				
//				//Scroll up
//				newScrollTop = currentScrollPosition - topFilteredJobsContainer + topClickedJob;
//			}
//			else{
//				//Scroll down
//				newScrollTop =  currentScrollPosition + topClickedJob - topFilteredJobsContainer;
//			}
//			$('#filteredJobs').animate({ scrollTop: newScrollTop}, 500);
		})
		
		
		$("#filteredJobs").on("click", ".show-more-less", function(){
			//Toggle the filter job's description to show more or less
			
			var description = $(this).siblings(".job-description")[0];			
			
			toggleClasses($(description), "less-description", "more-description");

			//Toggle icon
			var icon = $(this).find(".plus-minus")[0];
			toggleClasses($(icon), "glyphicon-plus", "glyphicon-minus");
			
			//Set text
			var text = $(this).find(".show-more-less-text")[0];
			if(isShowingMore){
				$(text).html(" Show less");
			}else{
				$(text).html(" Show more");
			}
		})

		
// 		**********************************************************************************
// 		**********************************************************************************
		
		$("#filteredJobs").scroll(function(){

			//Load more jobs when they scroll to the bottom
			if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight){
				appendFilteredJobs();
			}
		})

		
		$("body").click(function(e){

			var len = $(e.target).closest(".filter-container");
			if(doCloseDropdown(e.target)){
				$(".dropdown").each(function(){
					slideUp($(this), 300);
				})
			}

		})
		
	
		
		$(".select-time").click(function(){
	
			//Not working. trying to set a default scroll position
// 			//***************************************************************************
// 			var $s = $(this);		
// 			var defaultScrollValue = $s.data("default-scroll-value");			
// 			var optionTop = $s.find('[value="' + defaultScrollValue + '"]').offset().top;
// 			var selectTop = $s.offset().top;
// 			$s.scrollTop($s.scrollTop() + (optionTop - selectTop));
			//***************************************************************************
		})

		//******************************************************************************
		//******************************************************************************
		//Currently not being used.
		//This is used so the user can view time by 15 or 30 minute intervals, not just 60.
		//Incorporate this later
		$(".show-time-options-container").click(function(){
			$($(this).siblings(".time-options")[0]).toggle();	
		})	
		
		$(".change-time-options").click(function(){
			setTimeOptions($("#" + $(this).data("for-select")), $(this).data("increment"));
		})		

		setTimeOptions($("#startTimeOptions"), 60);
		setTimeOptions($("#endTimeOptions"), 60);
		//******************************************************************************
		//******************************************************************************		
		
	
		$('.date').datepicker({
			autoclose: true,
			toggleActive: true});

	
	})



function areInputsValid(){
	
	var areValid = 1;

	areValid *= isRadiusValid();
	areValid = areValid * isLocationValid();

	return areValid;
	
}


function isLocationValid(){
	
	var location = getLocation();
	var $e = $("#locationContainer");
	$errorMessage = $("#locationErrorMessage");
	
	if(location == ""){
		setInvalidCss($e);
		$errorMessage.show();
		return 0;
	}
	else{
		setValidCss($e);
		$errorMessage.hide();
		return 1;
	}
	
}

function isRadiusValid(){
	var $e;
	var $errorMessage;
	
	$e = $("#radius");
	$errorMessage = $("#radiusErrorMessage");
	if(!isValidatePositiveNumber($e.val())){
		areValid = 0;
		setInvalidCss($e);
		$errorMessage.show();
		return 0;
	}
	else{
		setValidCss($e);
		$errorMessage.hide();
		return 1;
	}
}


function getLocation(){
	var city = $.trim($("#city").val());
	var state = $.trim($("#state").val());
	var zipCode = $.trim($("#zipCode").val());
	
 	var location = "";
 	if(city != ""){
 		city += " ";
 	}
 	
 	if(state != ""){
 		state += " ";
 	}
	
	return city +  state +  zipCode;
}



function appendTime(filterDropdownId, paramName1, paramName2_isBefore){

	var endTime = $($("#" + filterDropdownId).find("option:checked")[0]).val();
	var isBefore = $($("#" + filterDropdownId).find("input[type=radio]:checked")[0]).attr("data-filter-value");
	var param = "";
	if(isValidInput(endTime) && isValidInput(isBefore)){
		param += "&" + paramName1 + "=" + formatTime(endTime);
		
		
		
		param += "&" + paramName2_isBefore + "=" + parseInt(isBefore);
	}
	return param;
}

function appendDate(filterDropdownId, paramName1, paramName2_isBefore){
	
	var selectedDate = getSelectedDate("#" + filterDropdownId);
	var param = "";
	var isBefore = getIsBeforeOrIsShorter($("#" + filterDropdownId));
	
	if(selectedDate != null && isValidInput(isBefore)){		
		param += "&" + paramName1 + "=" + $.datepicker.formatDate("yy-mm-dd", selectedDate);
		param += "&" + paramName2_isBefore + "=" + isBefore;
	}
	
	return param;	
}


function appendDuration(){
	var $filterContainer = $("#durationFilterContainer");
	var param = "";
	var duration = getFilterValue($filterContainer);
	var isShorter = getIsBeforeOrIsShorter($filterContainer)
	
	if(isValidatePositiveNumber(duration) && isValidInput(isShorter)){		
		param += "&duration=" + duration;
		param += "&isLessThanDuration=" + isShorter;		
	}
	return param;
}

function isValidRadioSelection(radioResult){
	
}


function getIsBeforeOrIsShorter($radioContainer){
	return $($radioContainer.find("input[type=radio]:checked")[0]).attr("data-filter-value");
}

function appendWorkDays(){
	var param = "";
	if(selectedDays.length > 0){
		$.each(selectedDays, function(){
			var date = new Date(this)
			param += "&d=" + $.datepicker.formatDate("yy-mm-dd", date);
		})
	}
	else{
// 		param += "&d=-1";
	}
	return param;
}

function getSelectedDate(calendarContainer){
	var td = $(calendarContainer).find(".active111")[0];
	var month;
	var year;
	var day;
	if(td == undefined){
		return null;
	}
	else{
		year = $(td).attr("data-year");
		month = parseInt($(td).attr("data-month"));
		day = $(td).children().html();
		return  new Date(year, month, day);
	}
	
}

function isValidRadioSelection(filterContainer){
	
	var selectedRadio;
	var radioContainer = $(filterContainer).find(".radio-container")[0];
	var isValid = 1;
	
	if(filterHasRadioSelection(filterContainer)){
		selectedRadio = $(filterContainer).find("input[type=radio]:checked")[0];
		if(selectedRadio == undefined){
			isValid = 0;
		}					
	}	
	
	if(isValid){
		setValidCss($(radioContainer));
		return true;
	}
	else{
		setInvalidCss($(radioContainer));
		return false;
	}
	
}

function filterHasRadioSelection(filterContainer){
	
	var radios = $(filterContainer).find("input[type=radio]");			
	if(radios.length > 0){
		return true;
	}
	else{
		return false;
	}
}



function isValidFilterValue(filterContainer){
	
	var filterValue = getFilterValue(filterContainer);
	var filterValueContainer = $(filterContainer).find(".filter-value-container")[0];
	
	//Filter value input
	if(filterValue == null){
		setInvalidCss($(filterValueContainer));
		return false;
	}
	else{
		setValidCss($(filterValueContainer));
		return true;
	}
				
}

function areApproveFilterInputsValid(filterContainer){
	
	var areValid = true;

	//Radio button
	if(!isValidRadioSelection(filterContainer)){
		areValid = false;
	}
	
	//Filter value
	if(!isValidFilterValue(filterContainer)){
		areValid = false;
	}
	
	return areValid;
	
}

function removeFilter(filterContainer){

	var $filterText = $($(filterContainer).find(".filter-text")[0]);
	$filterText.html($filterText.attr("data-reset-text"));
	
	$(filterContainer).removeClass("approve-filter");		
	
	$($(filterContainer).find(".remove-filter")[0]).hide();
//		slideUp($($(filterContainer).find(".dropdown")[0]));	
//		hideDropdown(filterContainer);
	
	
	clearFilterValueText(filterContainer);
}

function getFilterContainer($e){
	return $e.closest(".filter-container");
}

function hideDropdown(filterContainer){
	$($(filterContainer).find(".trigger-dropdown")[0]).trigger("click");
}


function approveFilter(filterContainer){
	
	var beginningTextToShow;
	var newText;
	var filterContainerText;
	var endingTextToShow;
	var filterValueText;			
	var selectedRadioText;
	
	if(filterHasRadioSelection(filterContainer)){
		selectedRadioText= $($(filterContainer).find("input[type=radio]:checked")[0]).attr("data-display-text");
	}
	else{
		selectedRadioText = "";
	}
	
	if($(filterContainer).attr("id") == "workDays"){
		filterValueText = "";
	}
	else{
		filterValueText = getFilterValue(filterContainer);	
	}
					
	beginningTextToShow = $(filterContainer).attr("data-display-text");
	endingTextToShow = getEndingTextToShow(filterContainer);
	newText = beginningTextToShow + " " + selectedRadioText + " " + filterValueText + " " + endingTextToShow;
	filterContainerText = $(filterContainer).find(".filter-text")[0];

	filterContainer.addClass("approve-filter");
	$(filterContainerText).html(newText);
	
	$($(filterContainer).find(".remove-filter")[0]).show();
	hideDropdown(filterContainer);
	
}

function getEndingTextToShow(filterContainer){
	var value = $(filterContainer).attr("data-display-text-suffix");
	
	if(value == undefined){
		return "";
	}
	else{
		return value;
	}
}

function getFilterValue(filterContainer){
		
	var selectOption;
	var inputValue;
	var calendarContainer;
	var selectedDate;

	//Attempt to find the select
	selectOption = $($(filterContainer).find("select option:checked")[0]).val();
	if(selectOption != undefined){
		return selectOption;
	}
	
	//Attempt to find input
	inputValue = $($(filterContainer).find(".input-container input")[0]).val();
	if(inputValue != undefined && inputValue != ""){
		return inputValue;
	}
	
	
	//Attempt to find **single** date calendar
	calendarContainer = $(filterContainer).find(".calendar-single-date")[0];
	if(calendarContainer != undefined){
		
		selectedDate = getSelectedDate(calendarContainer);
		if(selectedDate != null){
			return $.datepicker.formatDate("D m/d", selectedDate);
		}
	}
	
	//Attempt to find **multi** date calendar
	calendarContainer = $(filterContainer).find(".calendar-multi-date")[0];
	if(calendarContainer != undefined){
		
		selectedDate = getSelectedDate(calendarContainer);
		if(selectedDate != null){
			return $.datepicker.formatDate("D m/d", selectedDate);	
		}
		
	}
	
	return null;

}


function clearFilterValueText(filterContainer){
	
	var radio;
	var input;
	var select;
	var calendarContainer;
	var selectedCalendarDays;
	
	radio = $(filterContainer).find("input[type=radio]:checked")[0];
	$(radio).prop("checked", false);
	
	select = $(filterContainer).find("select")[0];
	$(select).val("");
	
	input = $(filterContainer).find(".input-container input")[0];
	$(input).val("");

	selectedCalendarDays = $(filterContainer).find("td.active111");
	$.each(selectedCalendarDays, function(){
		$(this).removeClass("active111");
	})

}

function doCloseDropdown(clickedElement){
	if($(clickedElement).parents(".filter-container").length > 0){
		return false;
	}
	// For whatever reason if the user clicks the "Next" button on the datepicker,
	// only two parent elements are returned. Thus the above equality equates to true even
	// though the date picker is a child of the .filter-container div.
	// This ensures the dropdown will not close when the user clicks the datepicker.
	else if ($(clickedElement).parent().hasClass("ui-datepicker-next")){
		return false;
	}
	else if ($(clickedElement).parent().hasClass("ui-datepicker-prev")){
		return false;
	}			
	else{
		return true;
	}
}


function getJobs(doSetMap){
	
	if(areInputsValid()){
		
		var params = ""; 
		params += "?isAppendingJobs=0";
		params += "&radius=" + $("#radius").val();
		params += "&fromAddress=" + getLocation();
		params += appendTime("start-time-dropdown", "startTime", "beforeStartTime");
		params += appendTime("end-time-dropdown", "endTime", "beforeEndTime");
		params += appendDate("start-date-dropdown", "startDate", "beforeStartDate");
		params += appendDate("end-date-dropdown", "endDate", "beforeEndDate");
		params += appendDuration();
		params += appendWorkDays();
		
//			params += "&categoryId=-1";
		
		$("html").addClass("waiting");
		$.ajax({
			type : "GET",
				url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
				success : _success,
				error : _error,
				cache: true
			});

		function _success(response) {
			//This function receives a velocity template
			
			$("#filteredJobs").html(response);		
			
			
			
			
			
			//Show the jobs and map container if this is the first job request
			if(!$("#mainBottom").is("visible")){
				$("#mainBottom").show();
			}
			
			//The map should not be set when sorting jobs because the same jobs will be returned,
			//they will only be displayed in a different order.
			//Because the same jobs will be returned, the map markers will remain the same.
			//Reloading the map is a bit awkward when sorting. 
			if(doSetMap == 1){
				setMap();	
			}
			
//			sessionStorage.clear();
//			sessionStorage.setItem("doStoreFilteredJobs", doStoreFilteredJobs(response));			
//			if(sessionStorage.doStoreFilteredJobs == 1){
//				sessionStorage.setItem("filteredJobs", response);
//				sessionStorage.setItem("map", $("#map").html());
//				sessionStorage.setItem("filters", $("#filterContainer").html());
//			}
			
			$("html").removeClass("waiting");
		}	

		function _error(response) {
			alert('DEBUG: error set filter jobs')
			$("html").removeClass("waiting");
		}
	}
}	

function doStoreFilteredJobs(response){
	if(response.indexOf("Sorry, no jobs match your search.") == -1){
		return 1;
	}
	else{
		return 0;
	}
}

function setMap(){
	
	var requestedLat = $("#requestOrigin").data("lat");
	var requestedLng = $("#requestOrigin").data("lng");
	var requestedRadius = $("#requestOrigin").data("max-dist");
	
	var requestedLatLng = {
			lat : requestedLat,
			lng : requestedLng,
		};

	var map = new google.maps.Map(document.getElementById('map'), {
		zoom : getZoom(requestedRadius),
		center : requestedLatLng,
		scrollwheel: false,
		streetViewControl: false,
//			disableDefaultUI: true,
	    mapTypeControlOptions: {
	      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
	    }
	});
	
	//Show job markers
	$("#filteredJobs").find(".job").each(function(){
		var jobId = $(this).attr("id");
		
		var jobLatLng = {
				lat : $(this).data("lat"),
				lng : $(this).data("lng")
			};
		
		var icon = {
			url: "/JobSearch/static/images/green-square.png",
			scaledSize: new google.maps.Size(10, 10),
		}
		
		var icon_mouseover = {
    			url: "/JobSearch/static/images/map-marker-red.png",
    			scaledSize: new google.maps.Size(30, 30),
    		}
		
		var marker = new google.maps.Marker({
			position : jobLatLng,
			map : map,
			icon: icon,
			jobId: jobId,
		});
		

	  var infowindow = new google.maps.InfoWindow({
	    content: "<div>" +
	    			"<div data-scroll-to='" + $(this).attr("id") + "'>Scroll To Job</div>" +
//	    			"<a href='#" + $(this).attr("id") + "'>Scroll To Job</div>" +
	    		"</div>",
	  });


      marker.addListener('click', function() {
         scrollToJob(this.jobId);
         addBorderToJob(this.jobId, true);
    });
      
	 
//	marker.addListener('mouseover', function() {
//		infowindow.open(map,marker);
//	  });
	
	var jobName = this;//.find("a.job-name")[0];
	var centerMapOnJob = $(this).find(".glyphicon-move")[0];
	
    google.maps.event.addDomListener(jobName, 'mouseout', function () {    	  
    	marker.setIcon(icon);
    });
	
    google.maps.event.addDomListener(jobName, 'mouseover', function () {  
    	marker.setIcon(icon_mouseover);

    });
    
    google.maps.event.addDomListener(centerMapOnJob, "click", function () {
//        infowindow.setContent(this.html);
//        infowindow.open(map, this);
        map.setCenter(marker.getPosition()); 
//        map.setZoom(5);
    });
	
//	marker.addListener('mouseout', function() {
//		infowindow.close();
//	  });
		
	})	
	
}

function addBorderToJob(jobId, request){	
	highlightArrayItem($("#" + jobId), $("#filteredJobs").find(".job"), "selected-job");
}

function scrollToJob(jobId){
	var currentScrollPosition = $('#filteredJobs').scrollTop();
	var topFilteredJobsContainer = $('#filteredJobs').offset().top; 
	var topClickedJob = $("#" + jobId).offset().top;
	var newScrollTop;
	
	//If the current job to scroll to is already scrolled past
	if(topClickedJob < topFilteredJobsContainer){
		
		//Scroll up
		newScrollTop = currentScrollPosition - topFilteredJobsContainer + topClickedJob;
	}
	else{
		//Scroll down
		newScrollTop =  currentScrollPosition + topClickedJob - topFilteredJobsContainer;
	}
	$('#filteredJobs').animate({ scrollTop: newScrollTop}, 1000);
}



function getZoom(radius){
	
	if (radius < 0)
		//This will occur if no jobs match the user's fiter(s)
		return 11;
	else if (radius < 5)
		return 12
	else if (radius < 25)
		return 11
	else if (radius < 50)
		return 10
	else if (radius < 100)
		return 8
	else if (radius < 500)
		return 6
	else
		return 5;
}

function initMap() {
	//Eventually initialize it to a user defualt
	var myLatLng = {
		lat : 44.954445,
		lng : -93.091301,
	};
	var map = new google.maps.Map(document.getElementById('map'), {
		zoom : 8,
		center : myLatLng,
		scrollwheel: false,
		streetViewControl: false,
//			disableDefaultUI: true,
	    mapTypeControlOptions: {
	      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
	    }

	});
}