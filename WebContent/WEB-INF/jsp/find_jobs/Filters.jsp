<%@ include file="../includes/TagLibs.jsp"%>

<link rel="stylesheet" type="text/css"	href="../static/css/filters_findJobs.css" />

<!-- <div id="filtersContainer"> -->
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
	<div id="additionalFiltersContainer" class="">
		<div class="row">
			<div id="startTime" class="col-sm-4 filter-container"  >								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="start-time-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="Start Time" >Start Time</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>
			
				<div id="start-time-dropdown" class="dropdown" data-text-root="Start">
					<div class="radio-container">
						<div class="radio">
						  <label><input type="radio" name="startTime"
						  	data-text-radio-selection="Before"
						  	data-is-before="1">Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="startTime"
						  	data-text-radio-selection="After"
						  	data-is-before="0">After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container">								
						<select id="startTimeOptions" data-default-scroll-value="7:00am"
							name="startTime"
							class="filter-value form-control size">
						 </select>	
			  		</div>	
			  		<span class="glyphicon glyphicon-ok approve-filter"></span>		
			  		<div class="error-message-container error-message"></div>					  		
				</div>
				
			</div>
			<div id="endTime" class="col-sm-4 filter-container">								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="end-time-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="End Time" >End Time</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>
			
				<div id="end-time-dropdown" class="dropdown" data-text-root="End">
					<div class="radio-container">
						<div class="radio">
						  <label><input type="radio" name="endTime"
						  	data-text-radio-selection="Before"
						  	data-is-before="1">Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="endTime"
						  	data-text-radio-selection="After"
						  	data-is-before="0">After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container">								
						<select id="endTimeOptions" data-default-scroll-value="7:00am"
							name="endTime"
							class="filter-value form-control size">
						 </select>	
			  		</div>	
			  		<span class="glyphicon glyphicon-ok approve-filter"></span>		
			  		<div class="error-message-container error-message"></div>					  		
				</div>						
			</div>
			<div id="duration" class="col-sm-4 filter-container">								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="duration-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="Duration">Duration</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>
			
				<div id="duration-dropdown" class="dropdown" data-text-root="" data-units="days">
					<div class="radio-container">
						<div class="radio">
						  <label><input type="radio" name="duration"
						  	data-text-radio-selection="Shorter than"
						  	data-is-shorter-than="1">Shorter than</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="duration"
						  	data-text-radio-selection="Longer than"
						  	data-is-shorter-than="0">Longer than</label>
						</div>										
					</div>
					<div class="filter-value-container">	
						<span>Number of days</span>							
						<input class="filter-value" type="text" data-text-order="1">	
			  		</div>	
			  		<span class="glyphicon glyphicon-ok approve-filter"></span>		
			  		<div class="error-message-container error-message"></div>					  		
				</div>						
			</div>
		</div>
		<div class="row">
			<div id="startDate" class="col-sm-4 filter-container" >								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="start-date-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="Start Date" >Start Date</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>
			
				<div id="start-date-dropdown" class="dropdown" data-text-root="Start">
					<div class="radio-container">
						<div class="radio">
						  <label><input type="radio" name="startDate"
						  	data-text-radio-selection="Before"
						  	data-is-before="1">Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="startDate"
						  	data-text-radio-selection="After"
						  	data-is-before="0">After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container">		
						<div class="calendar-single-date filter-value" data-number-of-months="1"></div>
					</div>
			  		<span class="approve-filter glyphicon glyphicon-ok"></span>
					<div class="error-message-container error-message"></div>	
				</div>
					
			</div>
			<div id="endDate" class="col-sm-4 filter-container" >								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="end-date-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="End Date" >End Date</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>
			
				<div id="end-date-dropdown" class="dropdown" data-text-root="End">
					<div class="radio-container">
						<div class="radio">
						  <label><input type="radio" name="endDate"
						  	data-text-radio-selection="Before"
						  	data-is-before="1">Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="endDate"
						  	data-text-radio-selection="After"
						  	data-is-before="0">After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container">		
						<div class="calendar-single-date filter-value" data-number-of-months="1"></div>
					</div>
			  		<span class="approve-filter glyphicon glyphicon-ok"></span>
					<div class="error-message-container error-message"></div>	
				</div>
					
			</div>								
			<div class="col-sm-4 filter-container" >								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="work-days-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="Work Days">Work Days</span>
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
		<div class="row">
			<div class="col-sm-4 filter-container" >								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="categories-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="Categories" >Categories (not built)</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>							
			</div>										
		</div>					
	</div>		
<!-- </div>  end filters container -->

<div class="row">
	<div class="col-sm-12">
		<div id="getJobsContainer"> 
			<div class="input-container">
				<button id="getJobs" class="square-button-green">Get Jobs</button>
			</div>
		</div>
	</div>
</div>
