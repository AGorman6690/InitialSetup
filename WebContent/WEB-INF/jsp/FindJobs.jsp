<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/Utilities.js" />"></script>

<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />
<!-- <link rel="stylesheet" type="text/css"	href="../static/css/findJobs.css" /> -->
<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/findJobs_Gitman_Bros.css" />
<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/findJobs_Jobs.css" />

<!-- Time picker -->
<!-- <link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/External/jquery.timepicker.css" /> -->
<%-- <script src="<c:url value="http://localhost:8080/JobSearch/static/External/jquery.timepicker.min.js" />"></script> --%>

<!-- <!-- Checkbox picker --> 
<%-- <script src="<c:url value="/static/External/bootstrap-checkbox.min.js" />"></script> --%>

</head>

<body>

	<input type="hidden" id="userId" value="${user.userId}" />
	
	<div class="container">
		<div class="row">
			<div id="distanceFilterContainer" class="col-sm-12">
			
				<div id="radiusErrorMessage" class="error-message"></div>
				<div id="locationErrorMessage" class="error-message"></div>
				<div id="distanceFilter" class="input-container-group form-group">	
					<div class="input-container">									
						<input name="radius" type="text"
							class="form-control" id="radius" placeholder="Number Of" value="50"></input>
					</div>			
					<div class="input-container">					
						<label id="milesFrom" for="radius">Miles From</label>
					</div>			
					<div class="input-container">			
						<input name="radius" type="text"
							class="form-control" id="city" placeholder="City"></input>
					</div>			
					<div class="input-container">			
						<input name="radius" type="text"
							class="form-control" id="state" placeholder="State"></input>
					</div>			
					<div class="input-container">			
						<input name="radius" type="text"
							class="form-control" id="zipCode" placeholder="Zip Code" value="55119"></input>
					</div>																			
				</div>
			</div>
		</div>
				
		<div class="row">
			<div class="col-sm-12">
				<div id="additionalFiltersContainer" class="input-container-group">
					<div class="row row-margin-override">
						<div class="col-sm-4 col-padding">
							<div data-display-text="Start"								
								class="input-container dropdown-input-container input-width">
								
								<div class="dropdown-input-label">									
									<span class="remove-additional-filter glyphicon glyphicon-remove"></span>
									<span class="display-text"
										data-reset-text="Start Time" >Start Time</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>					
								</div>
							
								<div class="additional-filter dropdown-input-selection-container input-width">
									<div class="radio-container">
										<div class="radio">
										  <label><input type="radio" name="startTime"
										  	data-display-text="Before" data-filter-name="beforeStartTime"
										  	data-filter-value="1">Before</label>
										</div>
										<div class="radio">
										  <label><input type="radio" name="startTime"
										  	data-display-text="After" data-filter-name="beforeStartTime"
										  	data-filter-value="0">After</label>
										</div>										
									</div>
									<div class="select-container">								
										<select id="startTimeOptions" data-default-scroll-value="7:00am"
											data-filter-name="startTime" name="startTime"
											class="filter-input form-control size">
										 </select>	
							  		</div>	
							  		<span class="approve-additional-filter glyphicon glyphicon-ok"></span>
								</div>
								
							</div>
						</div>	
						<div class="col-sm-4 col-padding">
							<div data-display-text="End"								
								class="input-container dropdown-input-container input-width">
								
								<div class="dropdown-input-label">									
									<span class="remove-additional-filter glyphicon glyphicon-remove"></span>
									<span class="display-text"
										data-reset-text="End Time">End Time</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>					
								</div>
							
								<div class="additional-filter dropdown-input-selection-container input-width">
									<div class="radio-container">
										<div class="radio">
										  <label><input type="radio" name="endTime"
										  	data-display-text="Before" data-filter-name="beforeEndTime"
										  	data-filter-value="1">Before</label>
										</div>
										<div class="radio">
										  <label><input type="radio" name="endTime"
										  	data-display-text="After" data-filter-name="beforeEndTime"
										  	data-filter-value="0">After</label>
										</div>										
									</div>
									<div class="select-container">								
										<select id="endTimeOptions" data-default-scroll-value="5:00pm"
											data-filter-name="endTime" name="endTime"
											class="filter-input form-control size">
										 </select>	
							  		</div>	
							  		<span class="approve-additional-filter glyphicon glyphicon-ok"></span>
								</div>
								
							</div>
						</div>
						<div class="col-sm-4 col-padding">
							<div class="input-container dropdown-input-container input-width">
								<div class="dropdown-input-label">
									<span class="">Duration</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>
								</div>
							</div>
						</div>
					</div>
					<div class="row row-margin-override">
						<div class="col-sm-4 col-padding">
							<div data-display-text="Start"								
								class="input-container dropdown-input-container input-width">

								<div class="dropdown-input-label">									
									<span class="remove-additional-filter glyphicon glyphicon-remove"></span>
									<span class="display-text"
										data-reset-text="Start Date">Start Date</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>					
								</div>
								
								
								<div class="additional-filter dropdown-input-selection-container input-width">
									<div class="radio-container">
										<div class="radio">
										  <label><input type="radio" name="startDate"
										  	data-display-text="Before" data-filter-name="beforeStartDate"
										  	data-filter-value="1">Before</label>
										</div>
										<div class="radio">
										  <label><input type="radio" name="startDate"
										  	data-display-text="After" data-filter-name="beforeStartDate"
										  	data-filter-value="0">After</label>
										</div>										
									</div>
<!-- 									<div class="select-container">								 -->
									<div class="input-container form-group">
								  		<input type="text" class="filter-input form-control date" data-filter-name="startDate" >
							  		</div>		
<!-- 							  		</div>	 -->
							  		<span class="approve-additional-filter glyphicon glyphicon-ok"></span>
								</div>
								

								
								
							</div>
						</div>
						
						<div class="col-sm-4 col-padding">
							<div class="input-container dropdown-input-container input-width">
								<div class="dropdown-input-label">
									<span class="">End Date</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>
								</div>
							</div>	
						</div>
						<div class="col-sm-4 col-padding">
							<div class="input-container dropdown-input-container input-width">
								<div class="dropdown-input-label">
									<span class="">Categories</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>
								</div>
							</div>	
						</div>										
					</div>
					<div class="row row-margin-override">
						<div class="col-sm-4 col-padding">
							<div class="input-container dropdown-input-container input-width">
								<div class="dropdown-input-label">
									<span class="">Working Days</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>
								</div>
							</div>
						</div>
									
					</div>					
				</div>			
			</div> <!--  end filters container -->
		</div> <!-- end filters row -->	
		
		<div class="row">
			<div class="col-sm-12">
				<div id="getJobsContainer" class="input-container-group">
					<div class="input-container">
						<button id="getJobs" class="btn">Get Jobs</button>
					</div>
				</div>
			</div>
		</div>
		
		</div>
		<div class="row" id="mainBottom">
			<div id="jobsContainer" class="col-sm-4 right-border" >
<!-- 				<h3 class="header">Jobs</h3>							 -->
				
				<div class="sort-jobs-by-container">
					<div class="dropdown-input-container sort-width ">
						<div class="dropdown-input-label">
							<span class="">Sort By</span>
							<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>			
						</div>
						
						<div id="sortOptions" class="dropdown-input-selection-container">
							<ul class="sort-width">
								<li>
									<div class="sort">
										<div class="sort-filter-name">
											Start Date
										</div>
										<div class="sort-direction radio-container">
											<div class="radio">
											  <label><input data-col="StartDate" data-is-ascending="1"
											  			type="radio" name="sort">Earliest First</label>
											</div>
											<div class="radio">
											  <label><input data-col="StartDate" data-is-ascending="0"
											  			 type="radio" name="sort">Lastest First</label>
											</div>										
										</div>
									</div>
								</li>
								<li>
									<div class="sort">
										<div class="sort-filter-name">
											End Date
										</div>
										<div class="sort-direction radio-container">
											<div class="radio">
											  <label><input data-col="EndDate" data-is-ascending="1"
											   type="radio" name="sort">Earliest First</label>
											</div>
											<div class="radio">
											  <label><input data-col="EndDate" data-is-ascending="0"
											   type="radio" name="sort">Latest First</label>
											</div>										
										</div>
									</div>
								</li>		
								<li>
									<div class="sort">
										<div class="sort-filter-name">
											Start Time
										</div>
										<div class="sort-direction radio-container">
											<div class="radio">
											  <label><input data-col="StartTime" data-is-ascending="1"
											   type="radio" name="sort">Earliest First</label>
											</div>
											<div class="radio">
											  <label><input data-col="StartTime" data-is-ascending="0"
											   type="radio" name="sort">Latest First</label>
											</div>										
										</div>
									</div>
								</li>		
								<li>
									<div class="sort">
										<div class="sort-filter-name">
											End Time
										</div>
										<div class="sort-direction radio-container">
											<div class="radio">
											  <label><input data-col="EndTime" data-is-ascending="1"
											   type="radio" name="sort">Earliest First</label>
											</div>
											<div class="radio">
											  <label><input data-col="EndTime" data-is-ascending="0"
											   type="radio" name="sort">Latest First</label>
											</div>										
										</div>
									</div>
								</li>	
								<li>
									<div class="sort">
										<div class="sort-filter-name">
											Duration
										</div>
										<div class="sort-direction radio-container">
											<div class="radio">
											  <label><input data-col="Duration" data-is-ascending="1"
											   type="radio" name="sort">Shortest First</label>
											</div>
											<div class="radio">
											  <label><input data-col="Duration" data-is-ascending="0"
											   type="radio" name="sort">Longest First</label>
											</div>										
										</div>
									</div>
								</li>
								<li>
									<div class="sort">
										<div class="sort-filter-name">
											Distance
										</div>
										<div class="sort-direction radio-container">
											<div class="radio">
											  <label><input data-col="Distance" data-is-ascending="1"
											   type="radio" name="sort">Closest First</label>
											</div>
											<div class="radio">
											  <label><input data-col="Distance" data-is-ascending="0"
											   type="radio" name="sort">Furthest First</label>
											</div>										
										</div>
									</div>
								</li>																			
							</ul>
				
						</div>
				
					</div>
				</div>

				<div id="filteredJobs" class="">
				</div>			
			</div>
						
			<div id="mapContainer" class="col-sm-8">				
<!-- 				<h3 class="header">Map</h3>				 -->
				<div id="map" class="right-border">
				
				</div>				
			</div>		
		</div>
		

	
	
<!-- 	<form> -->
<!-- 		<div class="additional-filter dropdown-input-selection-container input-width"> -->
<!-- 			<div class="radio-container"> -->
<!-- 				<div class="radio"> -->
<!-- 				  <label><input type="radio" name="startTime" -->
<!-- 				  	data-display-text="Before" data-filter-name="beforeStartTime" -->
<!-- 				  	data-filter-value="1">Before</label> -->
<!-- 				</div> -->
<!-- 				<div class="radio"> -->
<!-- 				  <label><input type="radio" name="startTime" -->
<!-- 				  	data-display-text="After" data-filter-name="beforeStartTime" -->
<!-- 				  	data-filter-value="0">After</label> -->
<!-- 				</div>										 -->
<!-- 			</div> -->
<!-- 			<div class="select-container">								 -->
<!-- 				<select id="startTimeOptions" data-default-scroll-value="7:00am" -->
<!-- 					data-filter-name="startTime" name="startTime" -->
<!-- 					class="filter-input form-control size"> -->
<!-- 				 </select>	 -->
<!-- 	  		</div>	 -->
<!-- 	  		<span class="approve-additional-filter glyphicon glyphicon-ok"></span> -->
<!-- 		</div> -->
<!-- 	</form> -->
</body>


<script>

$(document).ready(function() {
	
		$("#jobsContainer").on("click", ".sort-direction input[type='radio']", function(){
			setFilteredJobs(0);
		})
		
	
		$("body").on("click", ".dropdown-input-label", function(){
			
			//Get the filter dropdown div to toggle
			var $container = $($(this).parents(".dropdown-input-container")[0]);
			var $dropdown = $($container.find(".dropdown-input-selection-container")[0]); 
			
			//Save the current state of the clicked filter.
			//If it is currently hidden, it needs to be shown.
			//However, the code below attempts to hide any filter that is shown.
			var willHide;			
			if($dropdown.is(":visible")){
				willHide = true; 
			}else{
				willHide = false;
			}
			
			//If another filter's dropdown is shown, then hide it. 
			$($("#filtersContainer").find(".dropdown-input-selection-container:visible")[0]).hide();
			
			//Toggle the clicked filter's dropdown
			if(willHide){
				$dropdown.hide();
			}else{
				$dropdown.show();
			}

		})
		
		$(".approve-additional-filter").click(function(){
			
			//************************************************************************
			//************************************************************************
			//Note: When the input is invalid, outline the missing input in red
			//************************************************************************
			//************************************************************************
			
			
			var dropdownContainer = $(this).parents(".dropdown-input-container")[0];
			var displayText = $(dropdownContainer).data("display-text");
			var arr = [];
			var checkedRadio;
			var select;
			var input;
			var isValidInput = 0;
			var $inputLabel;
			
			//If filter has a radio group
			arr = $(dropdownContainer).find(".radio-container");
			if(arr.length > 0){
				checkedRadio = $(arr[0]).find("input[type=radio]:checked")[0];
				
				//If a radio has not been selected
				if(checkedRadio == null){
					isValidInput = -1;		
				}else{
					displayText += " " + $(checkedRadio).data("display-text");
				}					
			}
				
			//If filter has a select 
			arr = $(dropdownContainer).find(".select-container");
			if(arr.length > 0){
				select = $(arr[0]).find("select")[0]; 
				
				//If select is blank
				if($(select).val() == ""){
					isValidInput = -1;
				}else{
					displayText += " " + $(select).val();
				}				
			}
			
			//If filter has text input
			arr = $(dropdownContainer).find(".input-container");
			if(arr.length > 0){
				input = $(arr[0]).find("input[type=text]")[0]; 
				
				//If input is blank
				if($(input).val() == ""){
					isValidInput = -1;
				}else{
					displayText += " " + $(input).val();
				}				
			}			
			
			//If input is valid, then format the dropdown
			$inputLabel = $($(dropdownContainer).find(".dropdown-input-label")[0]);
			if(isValidInput > -1){ 
				$($(dropdownContainer).find(".display-text")[0]).html(displayText);
				$inputLabel.addClass("selected");
				$($(dropdownContainer).find(".dropdown-input-selection-container")[0]).hide();
				$($(dropdownContainer).find(".remove-additional-filter")[0]).show();
			}else{
				$inputLabel.removeClass("selected");
			}
			
		})
		
		$(".remove-additional-filter").click(function(event){
			
			event.stopImmediatePropagation();
			
			var container;
			var selectionDiv;
			var $displayText;
			
			$(this).parent().removeClass("selected");
			$(this).hide();
			
			//Reset the display text
			$displayText = $($(this).parent().find(".display-text")[0]);
			$displayText.html($displayText.data("reset-text"));
			
			//Reset the dropdown div that the used to set the filter
			container = $(this).parents(".dropdown-input-container")[0];
			selectionDiv = $(container).find(".dropdown-input-selection-container")[0];
			
			//Clear select
			$(selectionDiv).find(".select-container select").each(function(){
				$(this).val("");
			})
			
			//Clear radios
			$(selectionDiv).find("input[type=radio]").each(function(){
				$(this).removeAttr("checked");
			})
			
			//Clear text inputs
			$(selectionDiv).find("input[type=text]").each(function(){
				$(this).html("");
			})
			
		})
		
	
		
		
		$("#jobsContainer").on("click", ".show-more-less", function(){
			//Toggle the filter job's description to show more or less
			
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
			
			//Validate location input
			if(validateLocation() == 1 && validateRadius() == 1){
				setFilteredJobs(1);
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
			
			//Hide the additional filter selection containers if the user clicked outside of one
			if($(e.target).parents(".dropdown-input-container").length == 0){
				$(".dropdown-input-selection-container").each(function(){
					$(this).hide();
				})
			}

		})

		
		$(".show-time-options-container").click(function(){
			
			
			
			
			$($(this).siblings(".time-options")[0]).toggle();	
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
		

		setTimeOptions($("#startTimeOptions"), 60);
		setTimeOptions($("#endTimeOptions"), 60);


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
		
// 		$('#filterEndTime').timepicker({
// 			'scrollDefault': '5:00pm'
			
// 		});
	
	})
		
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
			center : myLatLng,
			scrollwheel: false,
			streetViewControl: false,
// 			disableDefaultUI: true,
		    mapTypeControlOptions: {
		      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
		    }
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
					params += "&" + $(this).data("filter-name");
					params += "=" + filterValue;
				}
			})
			
			//Check radio
			$(this).find("input[type=radio]").each(function(){
				if($(this).is(":checked")){
					params += "&" + $(this).data("filter-name");
					params += "=" + $(this).data("filter-value");
				}
			})
			
			//Check text inputs
			$(this).find("input[type=text]").each(function(){
				filterValue = $(this).val();
				if(filterValue != ""){
					params += "&" + $(this).data("filter-name");
					params += "=" + filterValue;
				}
			})		
			
		})
		
			//Check if data should be sorted
		sortByRadio = $("input[name=sort]").filter(":checked")[0];
		if(sortByRadio != null){
			params += "&sortBy=" + $(sortByRadio).data("col");
			params += "&isAscending=" + $(sortByRadio).data("is-ascending");
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
		
		return params;
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
				alert('DEBUG: error append filter jobs')
			}
	}
	
	function setFilteredJobs(doSetMap) {
		
		var params = getFilterParameters();
		params += "&isAppendingJobs=0";
		
		$.ajax({
			type : "GET",
				url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
				success : _success,
				error : _error,
				cache: true
			});

			function _success(response) {
				//This will return a velocity template
				
				$("#filteredJobs").html(response);		
				
				//Show the jobs and map container if this is the first job request
				if(!$("#mainBottom").is("visible")){
					$("#mainBottom").show();
				}
				
				//The map should not be set when sorting jobs because the same jobs will be returned,
				//they will only be displayed in a different order.
				//Because the same jobs will be returned, the map markers will remain the same.
				//Reloading the map is a bit akward when sorting. 
				if(doSetMap ==1){
					setMap();	
				}
					
			}	

			function _error(response) {
				alert('DEBUG: error set filter jobs')
			}
	}
	
// 	function initMap2() {
// 		//Eventually initialize it to a user defualt
// 		var myLatLng = {
// 			lat : 44.954445,
// 			lng : -93.091301,
// 		};
// 		var map = new google.maps.Map(document.getElementById('map'), {
// 			zoom : 8,
// 			center : myLatLng,
// 			scrollwheel: false,
// 			streetViewControl: false,
// // 			disableDefaultUI: true,
// 		    mapTypeControlOptions: {
// 		      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
// 		    }

// 		});
// 	}
</script>

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap_NO_CALLBACK_FOR_NOW">
	
</script>

<%@ include file="./includes/Footer.jsp"%>