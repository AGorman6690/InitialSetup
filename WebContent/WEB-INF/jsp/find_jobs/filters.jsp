<%@ include file="../includes/TagLibs.jsp"%>


	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js" />"></script>
	<script src="<c:url value="/static/javascript/FindJobs.js" />"></script>
	
	<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/findJobs.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/calendar.css" />

	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>

</head>

<body>

	<div class="container">
<!-- 	<button id="clearSession">Clear Session Storage</button> -->
		<div id="filtersContainer">
			<div class="row">
				<div id="distanceContainer" class="col-sm-12">
						<div id="distanceErrorMessage" class="error-message-container">
							<div id="radiusErrorMessage" class="error-message">The number of miles must be a positive number</div>
							<div id="locationErrorMessage" class="error-message">At a minimum, a city, state, or zip code is required.</div>
						</div>							
						<input name="radius" type="text"
							class="" id="radius" placeholder="Number Of" value="50"></input>	
						<span id="milesFromContainer">Miles From</span>					
						<input name="radius" type="text"
							class="" id="city" placeholder="City"></input>				
						<input name="radius" type="text"
							class="" id="state" placeholder="State"></input>					
						<input name="radius" type="text"
							class="" id="zipCode" placeholder="Zip Code" value="55119"></input>																
				</div>
			</div>
					
<!-- 			<div class="row">			 -->
<!-- 				<div class="col-sm-12"> -->
					<div id="additionalFiltersContainer" class="">
						<div class="row">
							<div class="col-sm-4 filter-container"  >
								
								<div  class="dropdown-header">	
									<span class="remove-filter glyphicon glyphicon-remove"></span>
									<div data-toggle-id="start-time-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
										<span class="filter-text" data-reset-text="Start Time" >Start Time</span>
										<span class="glyphicon glyphicon-menu-down"></span>		
									</div>			
								</div>
							
								<div id="start-time-dropdown" class="dropdown" data-text-root="Start">
									<div class="radio-container">
										<div class="radio">
										  <label><input type="radio" name="startTime"
										  	data-text-suffix="Before"
										  	data-is-before="1">Before</label>
										</div>
										<div class="radio">
										  <label><input type="radio" name="startTime"
										  	data-display-text="After"
										  	data-is-before="0">After</label>
										</div>										
									</div>
									<div class="select-container filter-value-container">								
										<select id="startTimeOptions" data-default-scroll-value="7:00am"
											name="startTime"
											class="filter-input form-control size">
										 </select>	
							  		</div>	
							  		<span class="glyphicon glyphicon-ok"></span>							  		
								</div>
								
							</div>
							<div class="col-sm-4">
								<div data-display-text="End" class="filter-container">
									
									<div  class="dropdown-container">	
										<div class="remove-filter">								
											<span class="glyphicon glyphicon-remove"></span>
										</div>
										<div data-toggle-id="end-time-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
											<span class="filter-text" data-reset-text="End Time" >End Time</span>
											<span class="glyphicon glyphicon-menu-down"></span>		
										</div>			
									</div>
								
									<div id="end-time-dropdown" class="dropdown">
										<div class="radio-container">
											<div class="radio">
											  <label><input type="radio" name="endTime"
											  	data-display-text="Before" data-filter-dto-prop="beforeEndTime"
											  	data-filter-value="1">Before</label>
											</div>
											<div class="radio">
											  <label><input type="radio" name="endTime"
											  	data-display-text="After" data-filter-dto-prop="beforeEndTime"
											  	data-filter-value="0">After</label>
											</div>										
										</div>
										<div class="filter-value-container">
											<div class="select-container">								
												<select id="endTimeOptions" data-default-scroll-value="7:00am"
													data-filter-dto-prop="endTime" name="endTime"
													class="filter-input form-control size">
												 </select>	
									  		</div>	
									  	</div>
								  		<span class="glyphicon glyphicon-ok"></span>							  		
									</div>
									
								</div>
							</div>
							<div class="col-sm-4">
								<div id="durationFilterContainer" data-display-text="Duration:"
									 class="filter-container">
	
									<div  class="dropdown-container">	
										<div class="remove-filter">								
											<span class="glyphicon glyphicon-remove"></span>
										</div>
										<div data-toggle-id="duration-dropdown" class="trigger-dropdown">	
											<span class="filter-text" data-reset-text="Duration" >Duration</span>
											<span class="glyphicon glyphicon-menu-down"></span>		
										</div>			
									</div>
									
									
									<div id="duration-dropdown" class="dropdown">
										<div class="checkbox-container filter-value-container">
											<div class="checkbox">
											  <label><input type="checkbox" name="duration"
											  	data-display-text="Hours"
											  	data-filter-value="1">Hours</label>
											</div>
											<div class="checkbox">
											  <label><input type="checkbox" name="duration"
											  	data-display-text="Days"
											  	data-filter-value="2">Days</label>
											</div>	
											<div class="checkbox">
											  <label><input type="checkbox" name="duration"
											  	data-display-text="Weeks"
											  	data-filter-value="3">Weeks</label>
											</div>
											<div class="checkbox">
											  <label><input type="checkbox" name="duration"
											  	data-display-text="Months"
											  	data-filter-value="4">Months</label>
											</div>
											<div class="checkbox">
											  <label><input type="checkbox" name="duration"
											  	data-display-text="Years"
											  	data-filter-value="5">Years</label>
											</div>
											<div class="checkbox">
											  <label><input type="checkbox" name="duration"
											  	data-display-text="Hopefully Forever"
											  	data-filter-value="6">Hopefully Forever</label>
											</div>																																																				
										</div>									
<!-- 										<div class="radio-container"> -->
<!-- 											<div class="radio"> -->
<!-- 											  <label><input type="radio" name="duration" -->
<!-- 											  	data-display-text="Shorter Than" data-filter-dto-prop="lessThanDuration" -->
<!-- 											  	data-filter-value="1">Shorter Than</label> -->
<!-- 											</div> -->
<!-- 											<div class="radio"> -->
<!-- 											  <label><input type="radio" name="duration" -->
<!-- 											  	data-display-text="Longer Than" data-filter-dto-prop="lessThanDuration" -->
<!-- 											  	data-filter-value="0">Longer Than</label> -->
<!-- 											</div>										 -->
<!-- 										</div> -->
<!-- 										<div class="filter-value-container"> -->
<!-- 											<div class="input-container "> -->
<!-- 										  		<input type="text" placeholder="Number of days" class="filter-input form-control" data-filter-dto-prop="duration" > -->
<!-- 									  		</div>		 -->
<!-- 										</div> -->
								  		<span class="approve-additional-filter glyphicon glyphicon-ok"></span>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-4">
								<div data-display-text="Start" class="filter-container">
									
									<div  class="dropdown-container">	
										<div class="remove-filter">								
											<span class="glyphicon glyphicon-remove"></span>
										</div>
										<div data-toggle-id="start-date-dropdown" class="trigger-dropdown">	
											<span class="filter-text" data-reset-text="Start Date" >Start Date</span>
											<span class="glyphicon glyphicon-menu-down"></span>		
										</div>			
									</div>
								
									<div id="start-date-dropdown" class="dropdown">
										<div class="radio-container">
											<div class="radio">
											  <label><input type="radio" name="startDate"
											  	data-display-text="Before" data-filter-dto-prop="beforeStartDate"
											  	data-filter-value="1">Before</label>
											</div>
											<div class="radio">
											  <label><input type="radio" name="startDate"
											  	data-display-text="After" data-filter-dto-prop="beforeStartDate"
											  	data-filter-value="0">After</label>
											</div>										
										</div>
										<div class="filter-value-container">
											<div class="calendar-single-date" data-number-of-months="1">
											</div>
										</div>
								  		<span id="okFilterStartDate" class="glyphicon glyphicon-ok"></span>
									</div>
									
								</div>
							</div>	
							<div class="col-sm-4">
								<div data-display-text="End" class="filter-container">
									
									<div  class="dropdown-container">	
										<div class="remove-filter">								
											<span class="glyphicon glyphicon-remove"></span>
										</div>
										<div data-toggle-id="end-date-dropdown" class="trigger-dropdown">	
											<span class="filter-text" data-reset-text="End Date">End Date</span>
											<span class="glyphicon glyphicon-menu-down"></span>		
										</div>			
									</div>
								
									<div id="end-date-dropdown" class="dropdown">
										<div class="radio-container">
											<div class="radio">
											  <label><input type="radio" name="endDate"
											  	data-display-text="Before" data-filter-dto-prop="beforeEndDate"
											  	data-filter-value="1">Before</label>
											</div>
											<div class="radio">
											  <label><input type="radio" name="endDate"
											  	data-display-text="After" data-filter-dto-prop="beforeEndDate"
											  	data-filter-value="0">After</label>
											</div>										
										</div>
										<div class="filter-value-container">
											<div class="calendar-single-date" data-number-of-months="1">
											</div>
										</div>
								  		<span id="okFilterEndDate" class="glyphicon glyphicon-ok"></span>			  		
									</div>
									
								</div>
							</div>										
							<div class="col-sm-4">
								<div id="workDays" data-display-text="Work Days" class="filter-container">
									
									<div  class="dropdown-container">	
										<div class="remove-filter">								
											<span class="glyphicon glyphicon-remove"></span>
										</div>
										<div data-toggle-id="work-days-dropdown" class="trigger-dropdown">	
											<span class="filter-text" data-reset-text="Work Days" >Work Days</span>
											<span class="glyphicon glyphicon-menu-down"></span>		
										</div>			
									</div>
								
									<div id="work-days-dropdown" class="dropdown">
										<div class="note">Jobs having at least one work day in the selected date range will be returned</div>
										<div class="filter-value-container">
											<div id="calendar" class="calendar-multi-date" data-number-of-months="2">
											</div>
										</div>
										<button class="square-button" id="clearCalendar">Clear</button>
										<span id="okFilterWorkingDays" class="glyphicon glyphicon-ok"></span>
									</div>															  		
								</div>								
							</div>
						</div>
						<div class="row">
							<div class="col-sm-4">
								<div data-display-text="sadf" class="filter-container">
									
									<div  class="dropdown-container">	
										<div class="remove-filter">								
											<span class="glyphicon glyphicon-remove"></span>
										</div>
										<div data-toggle-id="" class="trigger-dropdown">	
											<span class="filter-text" data-reset-text="Categories" >Categories (not built)</span>
											<span class="glyphicon glyphicon-menu-down"></span>		
										</div>			
									</div>
								</div>								
							</div>										
						</div>					
					</div>			
				</div> <!--  end filters container -->
<!-- 			</div> end filters row	 -->
<!-- 		</div> -->
		<div class="row">
			<div class="col-sm-12">
				<div id="getJobsContainer"> 
					<div class="input-container">
						<button id="getJobs" class="square-button-green">Get Jobs</button>
					</div>
				</div>
			</div>
		</div>
<!-- 	</div> -->
		
<!-- 		<script async defer -->
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
	</script>

<script>



	$(document).ready(function(){

// 		$("#clearSession").click(function(){
// 			sessionStorage.clear();
// 		})

		
// 		if(sessionStorage.doStoreFilteredJobs == "1"){
// 			$("#filteredJobs").html(sessionStorage.filteredJobs);
// 			setMap();				
// 			$("#mainBottom").show();
// 			$("#filterContainer").empty();
// 			$("#filterContainer").append(sessionStorage.filters);
// 		}
// 		else{
// 		}

	})


		
	

	
	function setTimeOptions($eSelect, increment){
		
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
					$eSelect.append("<option data-filter-value='" + formatTime(time) + "'>"
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

	
	function getFilterParameters(){
		
		var params = "";
		var filterValue;
		var sortByRadio;
		
		
		//Distance filter			
		var address = $.trim($("#city").val() + " "
								+ $("#state").val() + " " + $("#zipCode").val());	
		
		params += "?radius=" + $("#radius").val();;
		params += "&fromAddress=" + address;

		
		//Loop through each additional filter
		$(".additional-filter").each(function(){
			
			//Check select filters
			$(this).find(".select-container select").each(function(){
				//Get the value of the selected option
				filterValue = $($(this).find("option:selected")[0]).data("filter-value");
				if(filterValue != null){
					params += "&" + $(this).data("filter-dto-prop");
					params += "=" + filterValue;
				}
			})
			
			//Check radio
			$(this).find("input[type=radio]").each(function(){
				if($(this).is(":checked")){
					params += "&" + $(this).data("filter-dto-prop");
					params += "=" + $(this).data("filter-value");
				}
			})
			
			//Check text inputs
			$(this).find("input[type=text]").each(function(){
				filterValue = $(this).val();
				if(filterValue != ""){
					params += "&" + $(this).data("filter-dto-prop");
					params += "=" + filterValue;
				}
			})		
			
		})
		
// 		//Check if data should be sorted
// 		sortByRadio = $("input[name=sort]").filter(":checked")[0];
// 		if(sortByRadio != null){
// 			params += "&" + getSortByParam();
// 		}
		
		
		
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
		
		return params;
	}
	
	function getSortByParam($sortByRadio){
		var sortByParam;
		sortByParam = "sortBy=" + $sortByRadio.data("col");
		sortByParam += "&isAscending=" + $sortByRadio.data("is-ascending");
		
		return sortByParam;
	}
	
	
	function appendFilteredJobs() {
		
		var params = getFilterParameters();
		params += "&isAppendingJobs=1";
	
		$.ajax({
			type : "GET",
				url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
				success : _success,
				error : _error
			});

			function _success(response) {
				//This will return a velocity template
				
				var doAppend = 0;
				
				//If the returned html is the "No Jobs" message 
				if(response.indexOf('id="noJobs"') > -1){
					
					
					//AND some jobs have already been posted, then do not show
					//the "No Jobs" message again.
					if($("#filteredJobs").find(".job").length > 0){
						doAppend = 0;
					}else{
						doAppend = 1;
					}
					
				//Else new jobs were returned
				}else{
					doAppend = 1;
				}
	
				if(doAppend){
					$("#filteredJobs").append(response);
					setMap();	
				}
					
			}	

			function _error(response) {
// 				alert('DEBUG: error append filter jobs')
			}
	}
	
	function sortLoadedJobs(){
		
		var $sortByRadio = $($("input[name=sort]").filter(":checked")[0]);
		var params = "?" + getSortByParam($sortByRadio);
		params += "&" + getRequestedLatAndLngParams();
		
		$.ajax({
			type : "GET",
				url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/sort' + params,
				success : _success,
				error : _error,
				cache: true
			});

			function _success(response) {
				//This will return a velocity template
				
				$("#filteredJobs").html(response);		
									
			}	

			function _error(response) {
// 				alert('DEBUG: error set filter jobs')
			}
	}
	
	

	
	
</script>

 

<%@ include file="./includes/Footer.jsp"%>