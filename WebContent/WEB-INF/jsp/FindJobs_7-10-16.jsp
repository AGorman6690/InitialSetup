<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/Utilities.js" />"></script>

<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />
<link rel="stylesheet" type="text/css"	href="../static/css/findJobs.css" />
<link rel="stylesheet" type="text/css"	href="../static/css/findJobs_Jobs.css" />

<!-- Time picker -->
<!-- <link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/External/jquery.timepicker.css" /> -->
<%-- <script src="<c:url value="http://localhost:8080/JobSearch/static/External/jquery.timepicker.min.js" />"></script> --%>

<!-- <!-- Checkbox picker --> 
<%-- <script src="<c:url value="/static/External/bootstrap-checkbox.min.js" />"></script> --%>

</head>

<body>

	<input type="hidden" id="userId" value="${user.userId}" />
	
	<div class="container" style="height: 1000px; width: 90%; margin: auto">

	

		<div class="row">
			<div id="filtersContainer" class="col-sm-12">
			
				<div id="radiusErrorMessage" class="error-message"></div>
				<div id="locationErrorMessage" class="error-message"></div>
				<span id="distanceFilter" class="form-group">										
					<input name="radius" type="text"
						class="form-control" id="radius" placeholder="Number Of" value="50"></input>					
					<label id="milesFrom" for="radius">Miles From</label>
					<input name="radius" type="text"
						class="form-control" id="city" placeholder="City"></input>
					<input name="radius" type="text"
						class="form-control" id="state" placeholder="State"></input>
					<input name="radius" type="text"
						class="form-control" id="zipCode" placeholder="Zip Code" value="55119"></input>
																							
				</span>
				

			
				<div id="otherFiltersContainer" class="">
					
					<span id="toggleOtherFilters" class="">
						<span class="header">Filters</span>
						<span id="toggleOtherFiltersIcon" class="glyphicon glyphicon-menu-down"></span>
					</span>
										
					<span id="selectedFilters">					
					</span>
					
					<div id="otherFilters" class="">						
						
						<div id="startDateContainer" class="other-filter-container">
							<div class="other-filter-label">Start Date</div>

							<div class="radio-button-container">
								<div class="">
									<input type="radio" id="beforeStartDate" name="startDate" 
										onchange="verifyFilterInput(this)" value="">
									<label id="beforeStartDateLabel" 
									class="normal-text" for="beforeStartDate">Before</label>
								</div>								
								<div class="">
									<input type="radio" id="afterStartDate" name="startDate" value=""
										onchange="verifyFilterInput(this)" >
									<label id="afterStartDateLabel" class="normal-text" for="afterStartDate">After</label>
								</div>
							</div>
							<div class="form-group other-filter-content">
						  		<input id='filterStartDateCalendar' oninput="verifyFilterInput(this)"
						  			onchange="verifyFilterInput(this)"  type="text"
						  			class="filter-input form-control date size" >
					  		</div>						
					  		
						</div>	<!-- end start date filter container -->
						
						<div id="endDateContainer" class="other-filter-container">
							<div class="other-filter-label">End Date</div>

							<div class="radio-button-container">
								<div class="">
									<input type="radio" id="beforeEndDate" name="endDate" value=""
										onchange="verifyFilterInput(this)" >
									<label id="beforeEndDateLabel" class="normal-text" for="beforeEndDate">Before</label>
								</div>
								
								<div class="">
									<input type="radio" id="afterEndDate" name="endDate" value=""
										onchange="verifyFilterInput(this)" >
									<label id="afterEndDateLabel" class="normal-text" for="afterEndDate">After</label>
								</div>
							</div>
							<div class="form-group other-filter-content">
						  		<input id='filterEndDateCalendar' type="text"
						  			 oninput="verifyFilterInput(this)" onchange="verifyFilterInput(this)"
						  			 class="filter-input form-control date size" >
					  		</div>						

						</div>							

						<div id="durationContainer" class="other-filter-container">
							
							<div class="other-filter-label">Duration</div>
								
							<div class="radio-button-container">
								<div class="">
									<input type="radio" id="lessThanDuration" name="duration"
										onchange="verifyFilterInput(this)"  value="" >
									<label class="normal-text" for="lessThanDuration">Less Than</label>
								</div>
								
								<div class="">
									<input type="radio" id="moreThanDuration" name="duration"
										onchange="verifyFilterInput(this)" value="">
									<label class="normal-text" for="moreThanDuration">More Than</label>
								</div>	
							</div>							
														
							<div class="other-filter-content">
<!-- 								<div class="inline clear-filter"> -->
<!-- 									<span class="glyphicon glyphicon-remove"></span><span class="form-control-label normal-text" > Clear filter</span> -->
<!-- 								</div>								 -->
<!-- 								<span class="normal-text" for="duration">Number of days</span> -->
								<input oninput="verifyFilterInput(this)" name="duration" type="text"
									 class="filter-input form-control size" id="duration" placeholder="Days"></input>						  		
						  	</div>	
						</div>
						
						




						<div id="startTimeContainer" class="other-filter-container">
							
							<div class="other-filter-label">Start Time
							</div>

							<div class="radio-button-container">
								<div class="">
									<input type="radio" id="beforeStartTime" name="startTime"
										onchange="verifyFilterInput(this)"  value="">
									<label class="normal-text" for="beforeStartTime">Before</label>
								</div>
								
								<div class="">
									<input type="radio" id="afterStartTime" name="startTime"
										onchange="verifyFilterInput(this)" value="">
									<label class="normal-text" for="afterStartTime">After</label>
								</div>
							</div>
											
							<div class="other-filter-content">								
								<select oninput="verifyFilterInput(this)" data-default-scroll-value="7:00am"
									id="startTimeOptions" name="startTime" class="filter-input form-control size">
								 </select>	
					  		</div>	
							  		
						</div>			
						
						
						

						<div id="endTimeContainer" class="other-filter-container">
							
							<div class="other-filter-label">End Time
							</div>

							<div class="radio-button-container">
								<div class="">
									<input type="radio" id="beforeEndTime" name="endTime"
										onchange="verifyFilterInput(this)" value="">
									<label class="normal-text" for="beforeEndTime">Before</label>
								</div>
								
								<div class="">
									<input type="radio" id="afterEndTime" name="endTime"
										onchange="verifyFilterInput(this)" value="">
									<label class="normal-text" for="afterEndTime">After</label>
								</div>
							</div>
												
							<div class="other-filter-content">								
								<select oninput="verifyFilterInput(this)" data-default-scroll-value="5:00pm"
									id="endTimeOptions" name="endTime" class="filter-input form-control size">
								 </select>	
					  		</div>	
						</div>
						
													
					</div> <!--  end other filters -->
				</div>		<!-- end other filters container -->	
				
<!-- 				<div class="" id="getJobsContainer" style="display: inline; margin-bottom:20px"> -->
<!-- 					<button id="getJobs" class="btn">Get Jobs</button> -->
<!-- 				</div>	 -->
				
			</div><!-- end col -->
		</div>	<!-- end row -->
		<div class="row">
			<div class="col-sm-12">
			
			
			
			</div>
		</div>
		
		
		<div class="row" style="margin-bottom:20px">
			<div class="col-sm-12">
				<div class="" id="getJobsContainer" style="display: inline; margin-bottom:20px">
					<button id="getJobs" class="btn">Get Jobs</button>
				</div>	
			</div>
		</div>		
		
		<div class="row" id="mainBottom">
			<div id="jobsContainer" class="col-sm-4" >
				<h3 class="header">Jobs</h3>				
				<div id="filteredJobs" style="border-right-style:outset; height: 1000px"></div>						
			</div>
						
			<div id="mapContainer" class="col-sm-8">				
				<h3 class="header">Map</h3>				
				<div id="map">
				</div>				
			</div>		
		</div>
	</div>
</body>


<script>
	var filters = [];
	var filteredRadius = -1;
	var filteredLat = -1;
	var filteredLng = -1;
	
	$(document).ready(function() {
		
		$("#jobsContainer").on("click", ".show-more-less", function(){
			var description = $(this).siblings(".job-description")[0];			
			var isShowingMore;
			var exceedsMaxHeight;
			
			//Determine if the user is showing more or less
			if($(description).hasClass("less-description")){
				isShowingMore = 1;
			}else{
				isShowingMore = 0;
			}
			
			//Determine if job description exceeds an arbitrary max height
			if ($(description)[0].scrollHeight > 180){	
				exceedsMaxHeight = 1
			}else{
				exceedsMaxHeight = 0;
			}		
			
			if(isShowingMore){				
				if(exceedsMaxHeight){	
					$(description).addClass("exceeds-max-description")
				}else{
					$(description).removeClass("exceeds-max-description")				
				}				
			}else{
				//Always remove this class if showing less
				$(description).removeClass("exceeds-max-description")
			}
			
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
		
		$("#getJobs").click(function(){
			
			if(validateLocation() == 1 && validateRadius() == 1){
				updateFilter("fromAddress",
								$("#city").val() + " " + $("#state").val() + " " + $("#zipCode").val(),
								"selectedFromAddress");
				updateFilter("radius", $("#radius").val(), "selectedRadius");
				filterJobs();
			}
		})
		
		$("#selectedFilters").on("click", ".remove-filter", function(){
			
			//Remove html and selected filter from the filter json object
			var parentButton = $(this).parent();	
// 			parentButton.remove();
			
			deleteSelectedFilterButton($(parentButton).attr("filter-container-name"));
			clearFilterContainerValues($(parentButton).attr("filter-container-name"));
			resetFilterContainerControls($(parentButton).attr("filter-container-id"));
		})
		

		
		$("body").click(function(e){
			
			//Hide the other filters if user clicks outside of it
			//Do not hide if user clicked in an "other filter container" or 
			//if they just deleted a selected filter
			var doNotHide = [];			
			doNotHide = $(e.target).parents("#otherFiltersContainer");
// 			$.merge(doNotHide, $(e.target).parents("#selectedFilters"));

			if(doNotHide.length == 0){
				if($("#otherFilters").is(":visible")){
					toggleOtherFilters();
				}	
			}
		})
		
		$("#toggleOtherFilters").click(function(){
			toggleOtherFilters();
		})
		
		$(".show-time-options-container").click(function(){
			$($(this).siblings(".time-options")[0]).toggle();	
		})
		
		$("#filterStartTime").click(function(){
			
			var $s = $(this);
			
			var defaultScrollValue = $s.data("default-scroll-value");
		
			var optionTop = $s.find('[value="5:00pm"]').offset().top;
			var selectTop = $s.offset().top;
	        $s.scrollTop($s.scrollTop() + (optionTop - selectTop));
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

// 		$(".clear-filter").click(function(){
// 			var filterContent = $(this).parents(".filter-content")[0];
			
// 			$(filterContent).find("input, select").each(function(){
// 				$(this).val("");
// 			})
			
// 			//Reset filter header text
// 			var f = $(this).parents(".filter-content-container")[0];
// 			var g = $(f).find(".filter-criteria-header")[0];
// 			var header = $($(this).parents(".filter-criteria-container")[0]).find(".filter-criteria-header")[0];
// 			$(header).text($(this).data("reset-header"));
			
// 			$(this).hide();
// 		})
		
		
		$(".change-time-options").click(function(){
			setTimeOptions($("#" + $(this).data("for-select")), $(this).data("increment"));
		})
		
// 		$(".expand-filter-content-container").click(function(){			
// 			var clickedFilterContent = $(this).siblings(".filter-content-container")[0];
			
// 			//If necessary, hide the OTHER filter content that is currently expanded
// 			var expandedFilterContent = $("#filtersContainer").find(".filter-content-container.expanded")[0]; 
// 			if(expandedFilterContent != null){
// 				if(expandedFilterContent.id != clickedFilterContent.id){
// 					toggleFilterContent($($(expandedFilterContent).siblings
// 											(".expand-filter-content-container")[0]));
// 				}							
// 			}			
// 			toggleFilterContent($(this));
// 		})
		
		
		

		setTimeOptions($("#startTimeOptions"), 60);
		setTimeOptions($("#endTimeOptions"), 60);
		initializeFilters();

		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		
				
		$('#workingDays').datepicker({
			toggleActive: true,
			clearBtn: true,
			todayHighlight: true,
			startDate: new Date(),
			multidate: true			
		});	
	
		$('.date').datepicker({
			autoclose: true,
			toggleActive: true});


// 		$('#filterStartTime').timepicker({
// 			'useSelect' : true,
// 			'scrollDefault': '7:00am'
			
// 		});
		
		$('#filterEndTime').timepicker({
			'scrollDefault': '5:00pm'
			
		});
		
	
		
	
	})
	
	function initializeFilters(){
			
			//Location
			addFilter("radius", "", "selectedRadius");
			addFilter("fromAddress", "", "selectedFromAddress");
			
			//Time
			addFilter("startTime", "00:00:00", "selectedStartTime");
			addFilter("beforeStartTime", "0", "selectedStartTime");
			addFilter("endTime", "00:00:00", "selectedEndTime");
			addFilter("beforeEndTime", "0", "selectedEndTime");
			
			//Date
			addFilter("startDate", "-1", "selectedStartDate");
			addFilter("beforeStartDate", "0", "selectedStartDate");
			addFilter("endDate", "-1", "selectedEndDate");
			addFilter("beforeEndDate", "0", "selectedEndDate");
			
			//Duration
			addFilter("duration", "-1", "selectedDuration");
			addFilter("lessThanDuration", "0", "selectedDuration");
			
			//Job count
			addFilter("returnJobCount", "25", "selectedReturnJobCount");			
		}
	
	function validateRadius(){
		
		//Radius		
		var errorMessage;
		var isValidRadius = 0;
		var radius = $("#radius").val();
		var errorMessageText;
		
		//Validate
		if(radius == ""){
			radiusErrorMessage = "Number of miles is required."
			validRadius = 0;		
		}else if(!$.isNumeric(radius)){
			errorMessageText = "Number of miles must be numeric."
			validRadius = 0;
		}else if(radius <= 0){
			errorMessageText = "Number of miles must be greater than 0."
			validRadius = 0;
		}else{
			validRadius = 1;
		}
		
		//Set error message
		errorMessage = $("#radiusErrorMessage"); 			
		if(validRadius == 0){
			$(errorMessage).html(errorMessageText);
			$(errorMessage).show();
		}else{
			$(errorMessage).hide();
		}
		
		return validRadius;
	}
	
	function validateLocation(){
	
		var errorMessage;
		var city;
		var state;
		var zipCode;
		var validRadius;
		var validCity;
		var validState;
		var validZipCode;
		var validLocation;
		
		//City		
		city = $("#city").val();
		if( !city || city.length === 0){
			validCity = 0;
		}else{
			validCity = 1;
		}
		
		//State
		state = $("#state").val();
		if( !state || state.length === 0){
			validState = 0;
		}else{
			validState = 1;
		}

		//Zip code
		zipCode = $("#zipCode").val();
		if( !zipCode || zipCode.length === 0){
			validZipCode = 0;
		}else{
			validZipCode = 1;
	
		}			
		
		//Set error message
		errorMessage = $("#locationErrorMessage");
		if(validCity == 0 && validState == 0 && validZipCode == 0){			
			$(errorMessage).html("City, state or zip code required.")
			$(errorMessage).show();
			validLocation = 0;

		}else{
			$(errorMessage).hide();
			validLocation = 1;
		}
		
		return validLocation;
	}

	function verifyFilterInput(event){
		
		//Get filter container id
		var filterContainerId = $($(event).parents(".other-filter-container")[0]).attr('id');
		
		var filterContainerName = "";
		var filterValue = "";
		var isValidInput = 0;
		var buttonText = "";
		var isBefore;
		var isLessThanDuration;
		
		//Start date
		if(filterContainerId == "startDateContainer"){
			
			//Verify inputs
			filterValue = $("#filterStartDateCalendar").val();
			filterContainerName="selectedStartDate";
						
			if(($("#beforeStartDate").is(":checked") || $("#afterStartDate").is(":checked")) && 
				 filterValue != ""){
				
				isValidInput = 1;	
				
				//Set button text
				if($("#beforeStartDate").is(":checked")){
					buttonText = "Start before " + filterValue;
					isBefore = 1;
				}else{
					buttonText = "Start after " + filterValue;
					isBefore = 0;
				}
				
				updateFilter("startDate", filterValue, filterContainerName);
				updateFilter("beforeStartDate", isBefore, filterContainerName);
				
			}else{
				isValidInput = 0;
			}
			
		//End date
		}else if(filterContainerId == "endDateContainer"){
				
				//Verify inputs
				filterValue = $("#filterEndDateCalendar").val();
				filterContainerName="selectedEndDate";
				if(($("#beforeEndDate").is(":checked") || $("#afterEndDate").is(":checked")) && 
					 filterValue != ""){
					
					isValidInput = 1;	
					
					//Set button text
					if($("#beforeEndDate").is(":checked")){
						buttonText = "End before " + filterValue;
						isBefore = 1;
					}else{
						buttonText = "End after " + filterValue;
						isBefore = 0;
					}
					
					updateFilter("endDate", filterValue, filterContainerName);
					updateFilter("beforeEndDate", isBefore, filterContainerName);
					
				}else{
					isValidInput = 0;
				}
				
		//Duration
		}else if(filterContainerId == "durationContainer"){
			
			//Verify inputs
			filterValue = $("#duration").val();
			filterContainerName = "selectedDuration";
			
			var lessThanRadio = $("#lessThanDuration");
			var moreThanRadio = $("#moreThanDuration");
			
			if(($(lessThanRadio).is(":checked") || $(moreThanRadio).is(":checked")) && 
					$.isNumeric(filterValue)){
				
				isValidInput = 1;	
				
				//Set button text
				if($(lessThanRadio).is(":checked")){
					buttonText = "Duration less than " + filterValue + " days";
					isLessThanDuration = 1;
				}else{
					buttonText = "Duration more than " + filterValue + " days";
					isLessThanDuration = 0;
				}
				
				updateFilter("duration", filterValue, filterContainerName);
				updateFilter("lessThanDuration", isLessThanDuration, filterContainerName);			
			}else{
				isValidInput = 0;
			}
		//Start time
		}else if(filterContainerId == "startTimeContainer"){
			
			//Verify inputs
			filterValue = $("#startTimeOptions").val();
			filterContainerName="selectedStartTime";
			var beforeRadio = $("#beforeStartTime");
			var afterRadio = $("#afterStartTime");
			
			if(($(beforeRadio).is(":checked") || $(afterRadio).is(":checked")) && 
					filterValue != ""){
				
				isValidInput = 1;	
				
				//Set button text
				if($(beforeRadio).is(":checked")){
					buttonText = "Start before " + filterValue;
					isBefore = 1;
				}else{
					buttonText = "Start after " + filterValue;
					isBefore = 0;
				}
				
				updateFilter("startTime", formatTime(filterValue), filterContainerName);
				updateFilter("beforeStartTime", isBefore, filterContainerName);			
			}else{
				isValidInput = 0;
			}
			
		//End time
		}else if(filterContainerId == "endTimeContainer"){
			
			//Verify inputs
			filterValue = $("#endTimeOptions").val();
			filterContainerName="selectedEndTime";
			var beforeRadio = $("#beforeEndTime");
			var afterRadio = $("#afterEndTime");
			
			if(($(beforeRadio).is(":checked") || $(afterRadio).is(":checked")) && 
					filterValue != ""){
				
				isValidInput = 1;	
				
				//Set button text
				if($(beforeRadio).is(":checked")){
					buttonText = "End before " + filterValue;
					isBefore = 1;
				}else{
					buttonText = "End after " + filterValue;
					isBefore = 0;
				}
				
				updateFilter("endTime", formatTime(filterValue), filterContainerName);
				updateFilter("beforeEndTime", isBefore, filterContainerName);			
			}else{
				isValidInput = 0;
			}			
		}
				
		//If valid input, create/update button
		if(isValidInput){
			
			createSelectedFilterButton(buttonText, filterContainerName, filterContainerId);			
		
		//If inputs are invalid, then, if necessary, clear the filter's values
		//and remove button
		}else{
			deleteSelectedFilterButton(filterContainerName);
			clearFilterContainerValues(filterContainerName);			
		}
		
	}		
	
	function resetFilterContainerControls(filterContainerId){
		
		var container = $("#" + filterContainerId);
		var radioButtons  = $(container).find("input[type=radio]");
		var filterValue = $(container).find(".filter-input")[0];
		var i;
		
		//Uncheck radio buttons
		for(i=0; i<radioButtons.length; i++){
			$(radioButtons[i]).removeAttr("checked");
		}
		
		//Clear filter input
		$(filterValue).val('');
	}

		
	function deleteSelectedFilterButton(filterContainerName){		
		var button = $("#selectedFilters").find("button[filter-container-name='" + filterContainerName + "']")[0];
		$(button).remove();
	}
	
	function clearFilterContainerValues(containerName){
		var i;
		var filter;
		
		//Find the filter
		for(i = 0; i < filters.length; i++){
			filter = filters[i];
			
			if(filter.containerName == containerName && filter.name == name){
				filter.value = "";
			}			
		}		
	}	
	
	function updateFilter(name, value, containerName){
		var i;
		var filter;
		
		//Find the filter
		for(i = 0; i < filters.length; i++){
			filter = filters[i];
			
			if(filter.containerName == containerName && filter.name == name){
				filter.value = value;
			}			
		}		
	}	
	
	function addFilter(name, value, containerName){
		//The container name is simply used to group multiple filters together.

		var newFilter = {};			
		newFilter.containerName = containerName;
		newFilter.name = name;
		newFilter.value = value;
		filters.push(newFilter);
	
	}
	
	function createSelectedFilterButton(buttonText, filterContainerName, filterContainerId){
		
		//Attempt to find filter in selected filters
		var $sf = $("#selectedFilters");
		var existingButton = $sf.find("button[filter-container-name='" + filterContainerName + "']")[0]; 
		
		
		var buttonHtml = buttonText + "<span class='remove-filter glyphicon glyphicon-remove'></span>";
		
		//Append if filter has not yet been selected
		if(existingButton == null){
			$sf.append("<button filter-container-name='" + filterContainerName + "'" +
						" filter-container-id='" + filterContainerId + "'" +
						"class='btn'>" + buttonHtml + "</button>");
		}else{
			$(existingButton).html(buttonHtml);
		}
		
	}
	
	function toggleOtherFilters(){
		$("#otherFilters").toggle();
		toggleClasses($("#toggleOtherFiltersIcon"), "glyphicon-menu-down", "glyphicon-menu-up");
	}
	
	function toggleFilterContent($expandFilterContentContainer){
	
		var contentToToggle = $expandFilterContentContainer.siblings(".filter-content-container")[0]; 
		var iconToToggle = $expandFilterContentContainer.find('.toggle-filter-content-icon')[0];

		if($(contentToToggle).is(":visible")){
			hideFilterContent($(contentToToggle), $(iconToToggle));
		}else{
			showFilterContent($(contentToToggle), $(iconToToggle));
		}
	}
			
	function hideFilterContent($eContent, $eIcon){				
		$eIcon.removeClass("glyphicon-menu-up");
		$eIcon.addClass("glyphicon-menu-down");		
		$eContent.removeClass("expanded");
		$eContent.hide();
	}
	
	function showFilterContent($eContent, $eIcon){	
		$eIcon.addClass("glyphicon-menu-up");
		$eIcon.removeClass("glyphicon-menu-down");	
		$eContent.addClass("expanded");
		$eContent.show();
	}
	
	function setTimeOptions($eSelect, increment){
		
		if(increment > 0){
				
			$eSelect.empty();
			$eSelect.append('<option value="" selected style="display: none"></option>');
			
			var hourCount;
			var hour;
			var minute;
			var modifiedMinute;
			var amPm;
			
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
					$eSelect.append("<option value='" + hour + ":" + modifiedMinute + amPm + "'>"
										+ hour + ":" + modifiedMinute + amPm + "</option>");
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

	var pageContext = "findJob";
// 	getCategoriesBySuperCat('0', function(response, categoryId) {
		
// 		appendCategories(categoryId, "F", response);
// 	});

	function setMap(){
		
		//Set map origin
		var myLatLng = {
				lat : $("#requestOrigin").data("lat"),
				lng : $("#requestOrigin").data("lng")
			};

		//Set map zoom
		var zoom;
		var requestedRadius = $("#requestOrigin").data("radius");
		if (requestedRadius < 5)
			zoom = 12
		else if (requestedRadius < 25)
			zoom = 11
		else if (requestedRadius < 50)
			zoom = 10
		else if (requestedRadius < 100)
			zoom = 8
		else if (requestedRadius < 500)
			zoom = 6
		else
			zoom = 5;
		
		//Set map
		var map = new google.maps.Map(document.getElementById('map'), {
			zoom : zoom,
			center : myLatLng
		});
		
		//Show job markers
		$("#filteredJobs").find(".job").each(function(){
			var jobLatLng = {
					lat : $(this).data("lat"),
					lng : $(this).data("lng")
				};
			var marker = new google.maps.Marker({
				position : jobLatLng,
				map : map,
			});
		})	
		
	}
	
	
	function filterJobs() {

		var i;
		var filter;
		var radius = $("#radius").val();			
		var address = $.trim($("#city").val() + " "
								+ $("#state").val() + " " + $("#zipCode").val());
		
		//******************************************************************
		//Pretty this up.
		//A global variable seems hackish.
		filteredRadius = radius;
		//******************************************************************
		
		
		
		var params = "";
		params += "?radius=" + radius;
		params += "&fromAddress=" + address;
		
		for(i=0; i<filters.length; i++){
			filter = filters[i];
			params += "&" + filter.name;
			params += "=" + filter.value; 
		}
			

			//Category ids
// 			var categoryIds = getCategoryIds("selectedCategories");
// 			if (categoryIds.length > 0){		
// 				for (i = 0; i < categoryIds.length; i++) {
// 					params += '&categoryId=' + categoryIds[i];
// 				}
// 			}else params += "&categoryId=-1";
		params += "&categoryId=-1";
			
// 			// Working days
// 			var days = [];
// 			var days = $("#workingDays").datepicker('getDates');;
// 			if (days.length > 0){		
// 				for (i = 0; i < days.length; i++) {
// 					params += '&day=' + days[i];
// 				}
// 			}else params += "&day=-1";
		params += "&day=-1";
			
			
			
		getFilteredJobs(params, function(response) {
			
			$("#filteredJobs").html(response);
			setMap();	
		
		})
	
	}
	
	function getFilteredJobs(params, callback){

		$.ajax({
			type : "GET",
				url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
// 				dataType : "json",
				success : _success,
				error : _error
			});

			function _success(response) {
				callback(response)
			}

			function _error(response) {
				alert('error filter jobs')
			}

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
			streetViewControl: false,
// 			disableDefaultUI: true,
		    mapTypeControlOptions: {
		      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
		    }

		});
	}
</script>

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
	
</script>

<%@ include file="./includes/Footer.jsp"%>