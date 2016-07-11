<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/Utilities.js" />"></script>

<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />
<!-- <link rel="stylesheet" type="text/css"	href="../static/css/findJobs.css" /> -->
<link rel="stylesheet" type="text/css"	href="../static/css/findJobs_Gitman_Bros.css" />
<link rel="stylesheet" type="text/css"	href="../static/css/findJobs_Jobs.css" />

<!-- Time picker -->
<!-- <link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/External/jquery.timepicker.css" /> -->
<%-- <script src="<c:url value="http://localhost:8080/JobSearch/static/External/jquery.timepicker.min.js" />"></script> --%>

<!-- <!-- Checkbox picker --> 
<%-- <script src="<c:url value="/static/External/bootstrap-checkbox.min.js" />"></script> --%>

</head>

<body>

	<input type="hidden" id="userId" value="${user.userId}" />
	
	<div class="page-container">
		<div class="row">
			<div id="filtersContainer" class="col-sm-12">
			
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
				
				<div class="input-container-group">
					<div class="input-container">
						<button id="getJobs" class="btn">Get Jobs</button>
					</div>
				</div>
				
				<div class="input-container-group">
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
							
								<div class="dropdown-input-selection-container input-width">
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
							
								<div class="dropdown-input-selection-container input-width">
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
											data-filter-name="endTime" name="startTime"
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
							<div class="input-container dropdown-input-container input-width">
								<div class="dropdown-input-label">
									<span class="">Start Date</span>
									<span class="dropdown-input-icon glyphicon glyphicon-menu-down"></span>
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
		
		<div class="row" id="mainBottom">
			<div id="jobsContainer" class="col-sm-4" >
<!-- 				<h3 class="header">Jobs</h3>							 -->
				<div id="filteredJobs" class="right-border" style="height: 1000px">
					

				
				</div>			
			</div>
						
			<div id="mapContainer" class="col-sm-8">				
<!-- 				<h3 class="header">Map</h3>				 -->
				<div id="map" class="right-border">
				
				</div>				
			</div>		
		</div>
		
	</div>
</body>


<script>

$(document).ready(function() {
		
	
		$(".dropdown-input-label").click(function(){
			
			//This IF condition is hackish.
			//If need be, we can screw around with proper CSS later
// 			if($(this).hasClass(".remove-additional-filter") == 0){
				var $container = $($(this).parents(".dropdown-input-container")[0]);
				$($container.find(".dropdown-input-selection-container")[0]).toggle();	
// 			}
		})
		
		$(".approve-additional-filter").click(function(){
			var dropdownContainer = $(this).parents(".dropdown-input-container")[0];
			var displayText = $(dropdownContainer).data("display-text");
			var arr = [];
			var checkedRadio;
			var select;
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
			
			$displayText = $($(this).parent().find(".display-text")[0]);
			$displayText.html($displayText.data("reset-text"));
				
			container = $(this).parents(".dropdown-input-container")[0];
			selectionDiv = $(container).find(".dropdown-input-selection-container")[0];
			clearDropdownSelectionDiv($(selectionDiv))
			
		})
		
	
		
		function clearDropdownSelectionDiv($e){
			
			//Clear select
			$e.find(".select-container select").each(function(){
				$(this).val("");
			})
			
			//Clear radios
			$e.find("input[type=radio]").each(function(){
				$(this).removeAttr("checked");
			})
			
			//Clear text inputs
			$e.find("input[type=text]").each(function(){
				$(this).html("");
			})
			
		}
		
		
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
			
			//Validate location input
			if(validateLocation() == 1 && validateRadius() == 1){
				filterJobs();
			}
		})
		
		
// 		**********************************************************************************
// 		**********************************************************************************
		

		
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
		
		$('#filterEndTime').timepicker({
			'scrollDefault': '5:00pm'
			
		});
	
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
		
		
		//Loop through each additional filter
		$(".dropdown-input-selection-container").each(function(){
			
			var filterValue;
			
			
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
				if(filterValue != null){
					params += "&" + $(this).data("filter-name");
					params += "=" + filterValue;
				}
			})		
			
		})

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
			
	
		$.ajax({
			type : "GET",
				url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
// 				dataType : "json",
				success : _success,
				error : _error
			});

			function _success(response) {
				
				$("#filteredJobs").html(response);
				setMap();	
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