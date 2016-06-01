<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>

<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />

<!-- Time picker -->
<!-- <link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/External/jquery.timepicker.css" /> -->
<%-- <script src="<c:url value="http://localhost:8080/JobSearch/static/External/jquery.timepicker.min.js" />"></script> --%>

<!-- <!-- Checkbox picker --> 
<%-- <script src="<c:url value="/static/External/bootstrap-checkbox.min.js" />"></script> --%>



	<style>
	
		#filtersContainer{
/* 			border-bottom-style: outset */
		}
		
		.filter-criteria-container{
			margin-bottom:25px;
			display: inline;
		}
		
		.expand-filter-content-container{
			cursor: pointer;			
		}
		
		.filter-header{
			margin-right: 30px;
			display: inline-block;
			font-weight: bold;
		}
		
		.filter-criteria-header{
/* 			margin-right: 10px; */
			font-size: 14px;
		}
		
		.glyphicon-menu-up, .glyphicon-menu-down {
			margin-left: 5px;
			font-size: 10px;			
		}
		
		.filter-input{
			width: 95%;
			max-width: 200px;
			overflow: hidden;			
		}
		
		.filter-content-container{
 			display: none; 
			background-color: #F0F0F0 ;
			z-index: 1000;
			border: 2px solid #888888;
			border-radius: 5px;
			
		}
		
		.select-time{
			cursor: pointer;	
		}
		
		.filter-content{
			margin: 7.5px 7.5px 7.5px 7.5px;
		}
		
		.filter-criteria-content-container{
			margin-left: 20px;
			margin-top: 10px;
/*  			display: none; */
		}
		
		.normal-text{
			font-weight: normal;
		}
		
 		.bold-text{ 
 			font-weight: bold;
 			display: block;
  			margin-bottom: 5px;
 		} 
		
		h3.header{		
			border-bottom-style: outset;
			padding-bottom: 10px;
		}

		input[type=radio], input[type=checkbox] {
		    display:none;
		}
		input[type=radio] + label:before {
		    content: "";  
		    display: inline-block;  
		    width: 15px;  
		    height: 15px;  
		    vertical-align:middle;
		    margin-right: 8px;  
		    background-color: #D0D0D0 ;  
		    box-shadow: inset 0px 2px 2px rgba(0, 0, 0, .3);
		    border-radius: 8px;  
		}
		
		input[type=radio] + label{
			cursor: pointer;
		}

		input[type=radio]:checked + label:before {
			content: "\2022";
			color:#006666;
		    background-color: #aaa; 
			font-size:2.2em;
			text-align:center;
/* 			vertical-align: middle; */
 			line-height:15px; 
 			text-shadow:0px 0px 3px #eee;
		}

		#map{
			width: 100%;
			height: 100%;
		}
		
		.show-time-options{		
			font-size: 11px;
		}
		
		.show-time-options-container{
			cursor: pointer;
			margin-bottom: 10px;
		}
		
		.time-options{
			margin-top: 5px;
			margin-left: 25px;
			display: none;
		}
		
		.change-time-options{
			cursor: pointer;
		}
		
		.clear-filter{
 			display: none;
			position: absolute;
			right: 10px;
			margin-bottom: 10px;
			color: #b32d00;
			font-size: 16px;
			cursor: pointer;
		}
 
	</style>


</head>

<body>

	<input type="hidden" id="userId" value="${user.userId}" />
	
	<div class="container" style="height: 1000px; width: 90%; margin: auto">
	
	
		<div class="row">
			<div class="col-sm-12">
			
			<div id="filtersContainer" class="" >

					<form style="display: inline-block">
						
						<div id="filterDistance" class="filter-criteria-container">
							
							<fieldset class="filter-header">
								<legend class="expand-filter-content-container">
									<span class="filter-criteria-header">Distance</span>
									<span class="toggle-filter-content-icon glyphicon glyphicon-menu-down"></span>
								</legend>
								
								<div id="filterDistanceContent" style="position: absolute" class="filter-content-container">
									
									<div class="filter-content">
								
										<div data-reset-header="Distance" class="clear-filter">
											<span class="glyphicon glyphicon-remove"></span>
		<!-- 										<span class="form-control-label normal-text" > Clear filter</span> -->
										</div>
										<div class="form-group">									
											<label for="radius" class="form-control-label">Number of miles</label>
											<div id="invalidRadiusMessage" class="invalid-message">
												<span class="form-control-label normal-text" >
													Must be numeric</span>
											</div>											
											<input name="radius" type="text"
												class="filter-input form-control" id="radius"></input>
										</div>
																		
										<div class="form-group">
											<label for="city" class="form-control-label">City</label>
											<input name="city" type="text"
												class="filter-input form-control" id="city"></input>
										</div>
										
										<div class="form-group">
											<label for="state" class="form-control-label">State</label>
												
											<div class="filter-input">
												<select style="display: block" id="state" name="state"
												 class="filter-select form-control"></select>	
											</div>
										</div>
										
										<div class="form-group">
											<label for="zipCode" class="form-control-label">Zip code</label>
											<input name="zipCode" type="text"
												class="filter-input form-control" id="zipCode"></input>
										</div>
									</div>
								</div>								
		
							</fieldset>
						</div>
						
						<div id="filterStartDate" class="filter-criteria-container">
							<fieldset class="filter-header">
								<legend class="expand-filter-content-container">
									<span class="filter-criteria-header">Start Date</span>
									<span class="toggle-filter-content-icon glyphicon glyphicon-menu-down"></span>
								</legend>
	
								<div id="filterStartDateContent" style="position: absolute" class="filter-content-container">
									<div class="filter-content">
										<div data-reset-header="Start Date" class="clear-filter">
											<span class="glyphicon glyphicon-remove"></span>
		<!-- 										<span class="form-control-label normal-text" > Clear filter</span> -->
										</div>							
										<div style="margin-bottom: 10px">
											<input type="radio" id="beforeStartDate" name="startDate" value="" checked>
											<label id="beforeStartDateLabel" class="normal-text" for="beforeStartDate">Before</label><br>
											<input type="radio" id="afterStartDate" name="startDate" value="">
											<label id="afterStartDateLabel" class="normal-text" for="afterStartDate">After</label><br>
										</div>
										<div class="input-group filter-input date">
									  		<span class="input-group-addon">
									  		<i class="glyphicon glyphicon-calendar"></i></span>
									  		<input id='filterStartDateCalendar' type="text"
									  			class="form-control">
								  		</div>	
							  		</div>
							  	</div>						
							</fieldset>	
						</div>
						
						<fieldset class="filter-header">
							<legend class="expand-filter-content-container">
								<span class="filter-criteria-header">End Date</span>
								<span class="toggle-filter-content-icon glyphicon glyphicon-menu-down"></span>
							</legend>

							<div id="filterEndDateContent" style="position: absolute" class="filter-content-container">
								<div class="filter-content">
									<div class="clear-filter">
										<span class="glyphicon glyphicon-remove"></span><span class="form-control-label normal-text" > Clear filter</span>
									</div>								
									<div style="margin-bottom: 10px">
										<input type="radio" id="beforeEndDate" name="endDate" value="" checked>
										<label class="normal-text" for="beforeEndDate">Before</label><br>
										<input type="radio" id="afterEndDate" name="endDate" value="">
										<label class="normal-text" for="afterEndDate">After</label><br>
									</div>

									<div class="input-group filter-input date">
								  		<span class="input-group-addon">
								  		<i class="glyphicon glyphicon-calendar"></i></span>
								  		<input id='filterEndDateCalendar' type="text"
								  			class="form-control">
							  		</div>	
						  		</div>
						  	</div>						
						</fieldset>	
						
						<fieldset class="filter-header">
							<legend class="expand-filter-content-container"> 
								<span class="filter-criteria-header">Duration</span>
								<span class="toggle-filter-content-icon glyphicon glyphicon-menu-down"></span>
							</legend>

							<div id="filterDurationContent" style="position: absolute" class="filter-content-container">
								<div class="filter-content">
									<div class="clear-filter">
										<span class="glyphicon glyphicon-remove"></span><span class="form-control-label normal-text" > Clear filter</span>
									</div>								
									<label class="form-control-label" for="duration">Number of days</label>
									<input name="duration" type="text"
										class="filter-input form-control" id="duration"></input>						  		
						  		</div>
						  	</div>						
						</fieldset>		
						
						<fieldset class="filter-header">
							<legend class="expand-filter-content-container">
								<span class="filter-criteria-header">Start Time</span>
								<span class="toggle-filter-content-icon glyphicon glyphicon-menu-down"></span>
							</legend>

							<div id="filterStartTimeContent" style="position: absolute" class="filter-content-container">
								<div class="filter-content">
									<div class="clear-filter">
										<span class="glyphicon glyphicon-remove"></span><span class="form-control-label normal-text" > Clear filter</span>
									</div>								
									<div style="margin-bottom: 10px">
										<input type="radio" id="beforeStartTime" name="startTime" value="" checked>
										<label class="normal-text" for="beforeStartTime">Before</label><br>
										<input type="radio" id="afterStartTime" name="startTime" value="">
										<label class="normal-text" for="afterStartTime">After</label><br>
									</div>
									<div class="time-options-container">
										<div class="show-time-options-container">
											<span><span class="show-time-options glyphicon glyphicon-plus"></span> Show time options</span>
										</div>
										<div class="time-options">
											<input type="radio" id="filterStartTimeHourly" name="filterStartTimeOptions" checked>
											<label class="change-time-options normal-text" data-increment="60"
												for="filterStartTimeHourly" data-for-select="filterStartTime">Hourly</label><br>
											<input type="radio" id="filterStartTimeHalfHourly" name="filterStartTimeOptions">
											<label class="change-time-options normal-text" data-increment="30"
												 for="filterStartTimeHalfHourly" data-for-select="filterStartTime">Half hourly</label><br>										
											<input type="radio" id="filterStartTimeQuarterHourly" name="filterStartTimeOptions">
											<label class="change-time-options normal-text" data-increment="15"
												for="filterStartTimeQuarterHourly" data-for-select="filterStartTime">Quarter hourly</label><br>										
										
										</div>									
									</div>									
									<div class="input-group filter-input">
								  		<span class="input-group-addon">
								  			<i class="glyphicon glyphicon-time"></i>
								  		</span>
										<select data-default-scroll-value="7:00am" style="display: block" id="filterStartTime" name="startTime"
										 class="select-time filter-select form-control"></select>	
									</div>							  		
						  		</div>
						  	</div>						
						</fieldset>
						
						<fieldset class="filter-header">
							<legend class="expand-filter-content-container">
								<span class="filter-criteria-header">End Time</span>
								<span class="toggle-filter-content-icon glyphicon glyphicon-menu-down"></span>
							</legend>

							<div id="filterEndTimeContent" style="position: absolute" class="filter-content-container">
								<div class="filter-content">
									<div class="clear-filter">
										<span class="glyphicon glyphicon-remove"></span><span class="form-control-label normal-text" > Clear filter</span>
									</div>								
									<div style="margin-bottom: 10px">
										<input type="radio" id="beforeEndTime" name="endTime" value="" checked>
										<label class="normal-text" for="beforeEndTime">Before</label><br>
										<input type="radio" id="afterEndTime" name="endTime" value="">
										<label class="normal-text" for="afterEndTime">After</label><br>
									</div>
									
									<div class="time-options-container">
										<div class="show-time-options-container">
											<span><span class="show-time-options glyphicon glyphicon-plus"></span> Show time options</span>
										</div>
										<div class="time-options">
											<input type="radio" id="filterEndTimeHourly" name="filterEndTimeOptions" checked>
											<label class="change-time-options normal-text" data-increment="60"
												for="filterEndTimeHourly" data-for-select="filterEndTime">Hourly</label><br>
											<input type="radio" id="filterEndTimeHalfHourly" name="filterEndTimeOptions">
											<label class="change-time-options normal-text" data-increment="30"
												 for="filterEndTimeHalfHourly" data-for-select="filterEndTime">Half hourly</label><br>										
											<input type="radio" id="filterEndTimeQuarterHourly" name="filterEndTimeOptions">
											<label class="change-time-options normal-text" data-increment="15"
												for="filterEndTimeQuarterHourly" data-for-select="filterEndTime">Quarter hourly</label><br>										
										
										</div>									
									</div>										
									<div class="input-group filter-input">
								  		<span class="input-group-addon">
								  			<i class="glyphicon glyphicon-time"></i>
								  		</span>
										<select data-default-scroll-value="5:00pm" style="display: block" id="filterEndTime" name="endTime"
										 class="select-time filter-select form-control"></select>	
									</div>
						  		</div>
						  	</div>						
						</fieldset>																	
																	
					</form>
						
		
										
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<h3 class="header">Jobs</h3>
				
				<div id="jobsContainer" style="border-right-style:outset; height: 1000px">
				
				</div>
						
			</div>
						
			<div class="col-sm-6" style="height: 1000px;">
				
				<h3 class="header">Map</h3>				
				<div id="mapsContainer" style="border-right-style:outset; height: 1000px">
					<div id="map"></div>
				</div>
				
			</div>
		
		</div>
	</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	<div class="container">
		<h1>Find Job</h1>
		

		<div style="margin-top: 15px; width: 750px"
			class="panel panel-success">
			<div class="panel-heading">Filters</div>


			<div style="position:relative" class="color-panel panel-body">
				
				<ul class="list-group">
					<li class="list-group-item"><a style="margin-bottom: 10px" 
					class="btn btn-warning" data-toggle="collapse" data-target="#collapseReturnJobCount">
					Number of Jobs to Return</a>
					
						<div class="collapse" id="collapseReturnJobCount">						
							<div class="job-location-container input-group">
								<input id="returnJobCount"
									type="text" class="form-control" aria-describedby="sizing-addon2"
									 value="20">
							</div>	
						</div>
					</li>				
				
				
					<li class="list-group-item"><a style="margin-bottom: 10px" 
					class="btn btn-warning" data-toggle="collapse" data-target="#collapseDistance">
					Distance</a>
										
						<div class="collapse" id="collapseDistance">
						
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon"
									id="sizing-addon2">Number of miles</span> <input id="radius"
									type="text" class="form-control" aria-describedby="sizing-addon2" value="">
							</div>
	
							<div>
								<h4>
									<span class="label label-primary">From: (at least one field is required)</span>
								</h4>
								<div class="job-location-container input-group">
									<span class="job-location-label input-group-addon"
										id="sizing-addon2">Street Address</span> <input
										id="fromStreetAddress" type="text" value="" class="form-control"
										aria-describedby="sizing-addon2">
								</div>
								<div class="job-location-container input-group">
									<span class="job-location-label input-group-addon"
										id="sizing-addon2">City</span> <input id="fromCity" type="text"
										class="form-control" aria-describedby="sizing-addon2">
								</div>
								<div class="job-location-container input-group">
									<span class="job-location-label input-group-addon"
										id="sizing-addon2">State</span> <input id="fromState"
										type="text" class="form-control"
										aria-describedby="sizing-addon2">
								</div>
								<div class="job-location-container input-group">
									<span class="job-location-label input-group-addon"
										id="sizing-addon2">Zip Code</span> <input id="fromZipCode"
										type="text" class="form-control"
										aria-describedby="sizing-addon2" value="">
								</div>
							</div>					
						
						
						</div>
					</li>
					
					<li class="list-group-item"><a style="margin-bottom: 10px" class="btn btn-warning" 
						data-toggle="collapse" data-target="#0F">Categories</a>
					
						<div id="selectedCategories" style="display:inline-block"></div>					
						<div class="collapse" id="0F">
						</div>
						
					</li>
					
					<li class="list-group-item"><a style="margin-bottom: 10px" class="btn btn-warning"
							 data-toggle="collapse" data-target="#collapseDate">Date</a>										
						<div class="collapse" id="collapseDate">
			
							<div class="job-location-container input-group" style="margin-bottom: 10px">
								<span class="job-location-label input-group-addon">Start Date</span>		
										<input style="float:left" id="startDateBeforeAfter" type="checkbox" checked
										 data-off-class="btn-success" data-on-class="btn-success">								
										<div class="input-group date" style="width: 250px">
									  		<input id='OLD_filterStartDate' type="text" class="form-control"><span class="input-group-addon">
									  		<i class="glyphicon glyphicon-th"></i></span>
								  		</div>				
							</div>		
							<div class="job-location-container input-group" style="margin-bottom: 10px">
								<span class="job-location-label input-group-addon">End Date</span>
									<div >
										<input style="float:left" id="endDateBeforeAfter" type="checkbox" checked
										 data-off-class="btn-success" data-on-class="btn-success">								
										<div class="input-group date" style="width: 250px">
									  		<input id='filterEndDate' type="text" class="form-control"><span class="input-group-addon">
									  		<i class="glyphicon glyphicon-th"></i></span>
								  		</div>
									</div>
							</div>	
							<div class="job-location-container input-group" style="margin-bottom: 10px">
								<span class="job-location-label input-group-addon">Duration (days)</span>
								<input style="float:left" id="lessThanDuration" type="checkbox" checked
										 data-off-class="btn-success" data-on-class="btn-success">		
								<input id="filterDuration"
									type="text" class="form-control" aria-describedby="sizing-addon2">
							
							</div>	
								<span class="job-location-label input-group-addon">Working Days (isn't built)</span>
								<div id='workingDays' class="input-group date" style="margin-bottom: 10px; display: inline">
					  		</div>			
					</li>	
					
						<li class="list-group-item"><a style="margin-bottom: 10px" class="btn btn-warning"
							 data-toggle="collapse" data-target="#collapseTime">Time</a>																			
						<div class="collapse" id="collapseTime">
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon">Start Time</span>
									<input style="float:left" id="startTimeBeforeAfter" type="checkbox" checked
									 data-off-class="btn-success" data-on-class="btn-success">
									 
										<input style="width:100px; " id="asdfilterStartTime" type="text" class="form-control time ui-timepicker-input"
										 autocomplete="off">		
									
							</div>	
							
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon">End Time</span>
									<input style="float:left" id="endTimeBeforeAfter" type="checkbox" checked
									 data-off-class="btn-success" data-on-class="btn-success">
									<input style="width:100px; " id="asdffilterEndTime" type="text" 
									class="form-control time ui-timepicker-input" autocomplete="off">											
							</div>																			
						</div>
					</li>				
					
				</ul>
			
				<br>
				<button id="filterJobs" onClick="filterJobs()" type="button"
					class="btn btn-info">Filter Jobs</button>
								</div>
		</div>

		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Available Jobs</div>
			<div id='filteredJobs'></div>
			</div>
		</div>
		
		
</body>


<script>
	$(document).ready(function() {
		
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
		
		$("#filterDistance input, select").change(function(){
			//Hide or show the clear filter div.
			//Show the filter name or the filter selection.
			
			//Validate inputs
			var validRadius;
			var radius = $("#radius").val();
			if(jQuery.isNumeric(radius)){
				validRadius = 1;
			}else if(!radius || radius.length === 0){
				validRadius = 0;
			}else{
				//else radius is not blank and not number.
				//Thus, display error message.
				validRadius = -1;
			}
			
			var validCity;
			var city = $("#city").val();
			if( !city || city.length === 0){
				validCity = 0;
			}else{
				validCity = 1;
			}
			
			var validState;
			var state = $("#state").val();
			if( !state || state.length === 0){
				validState = 0;
			}else{
				validState = 1;
			}
			
			var validZipCode;
			var zipCode = $("#zipCode").val();
			if( !zipCode || zipCode.length === 0){
				validZipCode = 0;
			}else{
				validZipCode = 1;
			}			
			
			//Validate input
			var isValidInput;
			if(validRadius == 1 && (validCity || validState || validZipCode)){
				isValidInput = 1;
			}else{
				isValidInput = 0;
			}
			
			//Set clear filter element
			var clearFilter = $("#filterDistance").find(".clear-filter")[0];
			if(isValidInput == 1){
				$(clearFilter).show();				
			}else{
				$(clearFilter).hide();
			}

			//Set filter header text
			var header = $("#filterDistance").find(".filter-criteria-header")[0];
			if(isValidInput == 1){
				var prefix = radius + " miles: ";
				if(validCity){
					$(header).text(prefix + city);	
				}else if(validZipCode){
					$(header).text(prefix + zipCode);
				}else{
					$(header).text(prefix + state);
				}				
			}else{
				$(header).text("Distance");
			}
							
			//If necessary, show error message
			var invalidMessage = $("#filterDistance").find("#invalidRadiusMessage"); 			
			if(validRadius == -1){
				$(invalidMessage).show()
			}else{
				$(invalidMessage).hide();
			}									
		})
		
	$("#filterStartDate input, select").change(function(){
			//Hide or show the clear filter div.
			//Show the filter name or the filter selection.
			
			$filterContainer = $($("#filterStartDate"));
			
			var validDate;
			var date = $("#filterStartDateCalendar").val();
			if( !date || date.length === 0){
				validDate = 0;
			}else{
				validDate = 1;
			}					
		
			//Set clear filter element
			var clearFilter = $filterContainer.find(".clear-filter")[0];
			if(validDate == 1){
				$(clearFilter).show();
				
				var header = $filterContainer.find(".filter-criteria-header")[0];
				var prefix;
				if($("#beforeStartDate").is(":checked")){
					prefix = "Start Date Before ";
				}else{
					prefix = "Start Date After ";
				}
				$(header).text(prefix + date);	
			}else{				
				$(clearFilter).hide();
				$(header).text("Start Date");
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

		$("#filterDistance input, select").change(function(){
			//Hide or show the clear filter div.
			//Show the filter name or the filter selection.
			
			//Validate inputs
			var validRadius;
			var radius = $("#radius").val();
			if(jQuery.isNumeric(radius)){
				validRadius = 1;
			}else if(!radius || radius.length === 0){
				validRadius = 0;
			}else{
				//else radius is not blank and not number.
				//Thus, display error message.
				validRadius = -1;
			}
			
			var validCity;
			var city = $("#city").val();
			if( !city || city.length === 0){
				validCity = 0;
			}else{
				validCity = 1;
			}
			
			var validState;
			var state = $("#state").val();
			if( !state || state.length === 0){
				validState = 0;
			}else{
				validState = 1;
			}
			
			var validZipCode;
			var zipCode = $("#zipCode").val();
			if( !zipCode || zipCode.length === 0){
				validZipCode = 0;
			}else{
				validZipCode = 1;
			}			
			
			//Validate input
			var isValidInput;
			if(validRadius == 1 && (validCity || validState || validZipCode)){
				isValidInput = 1;
			}else{
				isValidInput = 0;
			}
			
			//Set clear filter element
			var clearFilter = $("#filterDistance").find(".clear-filter")[0];
			if(isValidInput == 1){
				$(clearFilter).show();				
			}else{
				$(clearFilter).hide();
			}

			//Set filter header text
			var header = $("#filterDistance").find(".filter-criteria-header")[0];
			if(isValidInput == 1){
				var prefix = radius + " miles: ";
				if(validCity){
					$(header).text(prefix + city);	
				}else if(validZipCode){
					$(header).text(prefix + zipCode);
				}else{
					$(header).text(prefix + state);
				}				
			}else{
				$(header).text("Distance");
			}
							
			//If necessary, show error message
			var invalidMessage = $("#filterDistance").find("#invalidRadiusMessage"); 			
			if(validRadius == -1){
				$(invalidMessage).show()
			}else{
				$(invalidMessage).hide();
			}									
		})
		
		$(".clear-filter").click(function(){
			var filterContent = $(this).parents(".filter-content")[0];
			
			$(filterContent).find("input, select").each(function(){
				$(this).val("");
			})
			
			//Reset filter header text
			var f = $(this).parents(".filter-content-container")[0];
			var g = $(f).find(".filter-criteria-header")[0];
			var header = $($(this).parents(".filter-criteria-container")[0]).find(".filter-criteria-header")[0];
			$(header).text($(this).data("reset-header"));
			
			$(this).hide();
		})
		
		
		$(".change-time-options").click(function(){
			setTimeOptions($("#" + $(this).data("for-select")), $(this).data("increment"));
		})
		
		$(".expand-filter-content-container").click(function(){			
			var clickedFilterContent = $(this).siblings(".filter-content-container")[0];
			
			//If necessary, hide the OTHER filter content that is currently expanded
			var expandedFilterContent = $("#filtersContainer").find(".filter-content-container.expanded")[0]; 
			if(expandedFilterContent != null){
				if(expandedFilterContent.id != clickedFilterContent.id){
					toggleFilterContent($($(expandedFilterContent).siblings
											(".expand-filter-content-container")[0]));
				}							
			}			
			toggleFilterContent($(this));
		})
		
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
		

		setTimeOptions($("#filterStartTime"), 60);
		setTimeOptions($("#filterEndTime"), 60);

		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		
		
		
		
		$("#filterDuration").keydown(function(){
			$("#filterStartDate").val('');
			$("#filterEndDate").val('');
		})
		
		$("#filterStartDate").change(function(){
			$("#filterDuration").val('');
		})
		
		$("#filterEndDate").change(function(){
			$("#filterDuration").val('');
		})
		
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
		
		//Combine these???
		//******************************************************************
		$('#startTimeBeforeAfter').checkboxpicker({
			  html: true,
			  offLabel: 'Before',
			  onLabel: 'After'
			});
	
		$('#endTimeBeforeAfter').checkboxpicker({
			  html: true,
			  offLabel: 'Before',
			  onLabel: 'After'
			});

		$('#startDateBeforeAfter').checkboxpicker({
			  html: true,
			  offLabel: 'Before',
			  onLabel: 'After'
			});	
		
		$('#endDateBeforeAfter').checkboxpicker({
			  html: true,
			  offLabel: 'Before',
			  onLabel: 'After'
			});	
		//******************************************************************
		
		$('#lessThanDuration').checkboxpicker({
			  html: true,
			  offLabel: 'Less Than',
			  onLabel: 'Greater Than'
			});			
		
		$('#startDateBeforeAfter').change(function () {
		    if (beforeStartDate == 0) {
		    	beforeStartDate = 1;
		    } else {
		    	beforeStartDate = 0;
		    }

		});
		
		$('#endTimeBeforeAfter').change(function () {
		    if (beforeEndTime == 0) {
		    	beforeEndTime = 1;
		    } else {
		    	beforeEndTime = 0;
		    }

		});
		
		$('#startTimeBeforeAfter').change(function () {
		    if (beforeStartTime == 0) {
		    	beforeStartTime = 1;
		    } else {
		    	beforeStartTime = 0;
		    }

		});
		
		$('#endDateBeforeAfter').change(function () {
		    if (beforeEndDate == 0) {
		    	beforeEndDate = 1;
		    } else {
		    	beforeEndDate = 0;
		    }

		});		
		
		$('#lessThanDuration').change(function () {
		    if (lessThanDuration == 0) {
		    	lessThanDuration = 1;
		    } else {
		    	lessThanDuration = 0;
		    }

		});	
		
		
		
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
	getCategoriesBySuperCat('0', function(response, categoryId) {
		
		appendCategories(categoryId, "F", response);
	});
	
	var beforeStartTime = 0;
	var beforeEndTime = 0;
	var beforeStartDate = 0;
	var beforeEndDate = 0;
	var lessThanDuration = 0;
	
	function filterJobs() {

		var radius = $("#radius").val();			
		var address = $.trim($("#fromStreetAddress").val() + " "
				+ $("#fromCity").val() + " " + $("#fromState").val() + " "
				+ $("#fromZipCode").val());
		
		if(radius != "" && address != ""){
			
			var params = "";
			params += "?radius=" + radius;
			params += "&fromAddress=" + address;			
			params += "&startTime=" + formatTime($("#filterStartTime").val());
			params += "&endTime=" + formatTime($("#filterEndTime").val());
			params += "&beforeEndTime=" + beforeEndTime;
			params += "&beforeStartTime=" + beforeStartTime;
			params += "&startDate=" + $("#filterStartDate").val();
			params += "&endDate=" + $("#filterEndDate").val();
			params += "&beforeStartDate=" + beforeStartDate;
			params += "&beforeEndDate=" + beforeEndDate;
			params += "&returnJobCount=" + $("#returnJobCount").val();
			
			var duration = $("#filterDuration").val();
			if(duration == ""){
				duration = 0;
			}
			params += "&duration=" + duration;
			params += "&lessThanDuration=" + lessThanDuration;
			
			var i;	
			
			//Category ids
			var categoryIds = getCategoryIds("selectedCategories");
			if (categoryIds.length > 0){		
				for (i = 0; i < categoryIds.length; i++) {
					params += '&categoryId=' + categoryIds[i];
				}
			}else params += "&categoryId=-1";
			
			// Working days
			var days = [];
			var days = $("#workingDays").datepicker('getDates');;
			if (days.length > 0){		
				for (i = 0; i < days.length; i++) {
					params += '&day=' + days[i];
				}
			}else params += "&day=-1";
			
			
			
			getFilteredJobs(params, function(filter) {
				
				var myLatLng = {
						lat : filter.lat,
						lng : filter.lng
					};

				var zoom;
				if (filter.radius < 5)
					zoom = 12
				else if (filter.radius < 25)
					zoom = 11
				else if (filter.radius < 50)
					zoom = 10
				else if (filter.radius < 100)
					zoom = 8
				else if (filter.radius < 500)
					zoom = 6
				else
					zoom = 5;
				
				var map = new google.maps.Map(document.getElementById('map'), {
					zoom : zoom,
					center : myLatLng
				});
				for (var i = 0; i < filter.jobs.length; i++) {
					myLatLng = {
						lat : filter.jobs[i].lat,
						lng : filter.jobs[i].lng
					};
					var marker = new google.maps.Marker({
						position : myLatLng,
						map : map,
					});
				}
				
				
				appendFilteredJobsTable(filter.jobs, $("#userId").val())
				
			})
		}
	
	}
	
	function getFilteredJobs(params, callback){

		$.ajax({
			type : "GET",
			url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
				dataType : "json",
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
	
	function initMap_HOLD() {
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