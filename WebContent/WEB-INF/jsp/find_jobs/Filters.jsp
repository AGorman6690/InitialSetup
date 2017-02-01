<%@ include file="../includes/TagLibs.jsp"%>




	<div id="headerRow" class="row">
		<div class="col-sm-12">
			<div class="group">
				<span id="clearAllFilters" class="accent">Clear All</span>
			</div>	
			<div class="group"> 
				<button id="getJobs" class="square-button-green">Get Jobs</button>
			</div>	
			<div class="group filter-container">
				<div data-trigger-dropdown-id="savedFindJobFiltersContainer" class="trigger-dropdown">
					<span id="loadSaveFilter" class="accent">Load</span>
					<div id="savedFindJobFiltersContainer" class="dropdown">
						<c:forEach items="${userDto.savedFindJobFilters }" var="filter">
							<div class="saved-find-job-filter">
								<span class="accent" data-id="${filter.id }">${filter.savedName }</span>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>					
			<div class="group">
				<span id="showSaveFilter" class="accent">Save</span>
			</div>
		</div>
	</div>





<!-- <div id="filtersContainer"> -->
	<div class="row">
		<div id="distanceContainer" class="col-sm-12">
			<div id="distanceErrorMessage" class="error-message-container">
				<div id="radiusErrorMessage" class="error-message">The number of miles must be a positive number.</div>
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
						  	data-is-before="1" ${filterDto.beforeStartTime && !empty filterDto.startTime_local ? 'checked' : '' }>Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="startTime"
						  	data-text-radio-selection="After"
						  	data-is-before="0" ${!filterDto.beforeStartTime && !empty filterDto.startTime_local ? 'checked' : '' }>After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container">								
						<select id="startTimeOptions" data-default-scroll-value="7:00am"
							name="startTime" class="filter-value form-control size time" 
							data-init-time="${!empty filterDto.startTime_local ? filterDto.startTime_local : '' }">
						 </select>	
			  		</div>	
			  		<span class="approve-filter glyphicon glyphicon-ok"
			  			data-click-on-load="${!empty filterDto.startTime_local ? 1 : 0}" ></span>	
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
						  	data-is-before="1" ${filterDto.beforeEndTime && !empty filterDto.endTime_local ? 'checked' : '' }>Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="endTime"
						  	data-text-radio-selection="After"
						  	data-is-before="0" ${!filterDto.beforeEndTime && !empty filterDto.endTime_local ? 'checked' : '' }>After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container">								
						<select id="endTimeOptions" data-default-scroll-value="7:00am"
							name="endTime" class="filter-value form-control size time" 
							data-init-time="${!empty filterDto.endTime_local ? filterDto.endTime_local : '' }">
						 </select>	
			  		</div>	
			  		<span class="approve-filter glyphicon glyphicon-ok"
			  			data-click-on-load="${!empty filterDto.endTime_local ? 1 : 0}" ></span>		
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
						  	data-is-before="1" ${filterDto.isShorterThanDuration && !empty filterDto.duration ? 'checked' : '' }>Shorter than</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="duration"
						  	data-text-radio-selection="Longer than"
						  	data-is-before="0" ${!filterDto.isShorterThanDuration && !empty filterDto.duration ? 'checked' : '' }>Longer than</label>
						</div>										
					</div>
					<div class="filter-value-container">	
						<span>Number of days</span>							
						<input class="filter-value" type="text" data-text-order="1"
							value="${!empty filterDto.duration ? filterDto.duration : '' }">	
			  		</div>	
			  		<span class="approve-filter glyphicon glyphicon-ok"
			  			data-click-on-load="${!empty filterDto.duration ? 1 : 0}" ></span>		
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
						  	data-is-before="1" ${filterDto.beforeStartDate && !empty filterDto.startDate_local ? 'checked' : '' }>Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="startDate"
						  	data-text-radio-selection="After"
						  	data-is-before="0" ${!filterDto.beforeStartDate && !empty filterDto.startDate_local ? 'checked' : '' }>After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container calendar-container" >		
						<div id="startDateCalendar" class="calendar-single-date filter-value calendar" data-number-of-months="1"
							data-init-date="${!empty filterDto.startDate_local ? filterDto.startDate_local : '-1'}"></div>
					</div>
			  		<span class="approve-filter glyphicon glyphicon-ok"
			  			data-click-on-load="${!empty filterDto.startDate_local ? 1 : 0}" ></span>
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
						  	data-is-before="1" ${filterDto.beforeEndDate && !empty filterDto.endDate_local ? 'checked' : '' }>Before</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="endDate"
						  	data-text-radio-selection="After"
						  	data-is-before="0" ${!filterDto.beforeEndDate && !empty filterDto.endDate_local ? 'checked' : '' }>After</label>
						</div>										
					</div>
					<div class="select-container filter-value-container calendar-container">		
						<div id="endDateCalendar" class="calendar calendar-single-date filter-value" data-number-of-months="1"
							data-init-date="${!empty filterDto.endDate_local ? filterDto.endDate_local : '-1'}">
						</div>
					</div>
			  		<span class="approve-filter glyphicon glyphicon-ok"
			  			data-click-on-load="${!empty filterDto.endDate_local ? 1 : 0}" ></span>
					<div class="error-message-container error-message"></div>	
				</div>
					
			</div>								
			<div id="workDays" class="col-sm-4 filter-container" >								
				<div class="dropdown-header">	
					<span class="remove-filter glyphicon glyphicon-remove"></span>
					<div data-trigger-dropdown-id="work-days-dropdown" data-toggle-speed="2" class="trigger-dropdown">	
						<span class="filter-text" data-reset-text="Work Days">Work Days</span>
						<span class="glyphicon glyphicon-menu-down"></span>		
					</div>			
				</div>
				
				<div id="work-days-dropdown" class="dropdown" data-units="days selected">
					<div class="note">Jobs having at least one work day in the selected date range will be returned</div>
					<div class="filter-value calendar-container calendar-multi-date">
						<div id="workDaysCalendar" class="calendar">
						</div>
					</div>
					<button id="clearWorkDays" class="square-button clear-calendar">Clear</button>
					<span id="okFilterWorkingDays" class="approve-filter glyphicon glyphicon-ok"></span>
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


<div id="saveModal" class="mod">
	<div class="mod-content">
		<div class="mod-header">
			<span class="glyphicon glyphicon-remove"></span>
			<h3>Save Filter</h3>			
		</div>
		<div class="mod-body">
			<div class="mod-item">
				<label class="lbl">Name</label>
				<input id="saveFilterName" class="mod-input"/>
			</div>
			<div class="mod-item">
				<label class="lbl">Email Frequency</label>
				<div id="emailFrequencyContainer" class="radio-container" class="mod-input">
					<div class="radio">
					  <label><input type="radio" name="setupEmailAlert" value="1" checked>Daily</label>
					</div>
					<div class="radio">
						<label><input type="radio" name="setupEmailAlert" value="0">Never</label>
					</div>																			
				</div>
			</div>
			<div class="mod-item center">
				<button id="approveSaveFilter" class="square-button blue-button">Save</button>
			</div>					
		</div>
		
	</div>
</div>

	