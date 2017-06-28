<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>
<script src="/JobSearch/static/javascript/view_job_employee/SubmitApplication.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/find_jobs/Map.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/find_jobs/Find_jobs.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/Utilities/FormUtilities.js" type="text/javascript"></script>
<link href="/JobSearch/static/css/find_jobs/find_jobs_new.css" rel="stylesheet" />

<div class="page-cont">
	<div id="header">
		<div id="location-filter">
			<input id="miles" type="text" placeholder="number" value = "25"/>
			<span>Miles From</span>
			<input id="address" type="text" placeholder="city, state, zip code" value="55119"/>
			<button id="get-jobs" class="sqr-btn green">Get Jobs</button>
		</div>
<!-- 		<div id="applied-filters"></div> -->
	</div>
	<div id="wrapper">
		<div id="other-filters">
			<div id="side-location-filter" class="filter">
				<button id="get-jobs" class="sqr-btn green">Get Jobs</button>
				<input id="miles" type="text" placeholder="number" value = "25"/>
				<span>Miles From</span>
				<input id="" class="address" type="text" placeholder="city, state, zip code" value="55119"/>
				
			</div>
<!-- 			<p class="filter-sort-jobs-header">Filters</p> -->
			<div class="filter">						
				<div class="filter-name-container">
					<div data-toggle-id="wstart-date-filter">
						<p class="filter-name">Start Date<span class="glyphicon glyphicon-menu-right"></span></p>
					</div>
					<div id="start-date-filter" class="dropdown-style">
						<p class="apply-filter">Apply</p>
						<div class="radio-container">
							<label><input data-parameter-name="beforeStartDate" data-parameter-value="1"
								 data-filter-text="Start before" type="radio"
								 name="start-date"/>Before</label>
							<label><input data-parameter-name="beforeStartDate" data-parameter-value="0"
								 data-filter-text="Start after" type="radio"
								 name="start-date" checked/>After</label>
						</div>
						<div class="calendar-container v2">
							<div data-parameter-name="startDate" class="calendar"></div>
						</div>	
											
					</div>
				</div>
			</div>
			<div class="filter">
				<div class="filter-name-container">
					<div data-toggle-id="wend-date-filter">
						<span class="glyphicon glyphicon-menu-right"></span><p class="filter-name">End Date</p>
					</div>
					<div id="end-date-filter" class="dropdown-style">
						<p class="apply-filter">Apply</p>		
						<div class="radio-container">
							<label><input data-parameter-name="beforeEndDate" data-parameter-value="1"
								 data-filter-text="End before" type="radio" name="end-date"
								 checked/>Before</label>
							<label><input data-parameter-name="beforeEndDate" data-parameter-value="0"
								 data-filter-text="End after" type="radio" name="end-date"
								 />After</label>
						</div>		
						<div class="calendar-container v2">
							<div data-parameter-name="endDate" class="calendar"></div>
						</div>	
					</div>
				</div>
			</div>
			<div class="filter">
				<div class="filter-name-container">
					<div data-toggle-id="wstart-time-filter">
						<p class="filter-name">Start Time<span class="glyphicon glyphicon-menu-right"></span></p>
					</div>
					<div id="start-time-filter" class="dropdown-style">
						<p class="apply-filter">Apply</p>		
						<div class="radio-container">
							<label><input data-parameter-name="beforeStartTime" data-parameter-value="1"
								 data-filter-text="Start before" type="radio" name="start-time"
								 />Before</label>
							<label><input data-parameter-name="beforeStartTime" data-parameter-value="0"
								 data-filter-text="Start before" type="radio" name="start-time"
								 checked/>After</label>
						</div>
						<select data-parameter-name="startTime" class="time"></select>
					</div>
				</div>
			</div>
			
			<div class="filter">
				<div class="filter-name-container">
					<div data-toggle-id="wend-time-filter">
						<p class="filter-name">End Time<span class="glyphicon glyphicon-menu-right"></span></p>
					</div>
					<div id="end-time-filter" class="dropdown-style">
						<p class="apply-filter">Apply</p>		
						<div class="radio-container">
							<label><input data-parameter-name="beforeEndTime" data-parameter-value="1"
								 data-filter-text="End before" type="radio" name="end-time"
								 checked/>Before</label>
							<label><input data-parameter-name="beforeEndTime" data-parameter-value="0"
								 data-filter-text="End before" type="radio" name="end-time"/>
								 After</label>
						</div>
						<select data-parameter-name="endTime" class="time"></select>							
					</div>					
				</div>
			</div>
			<div class="filter" data-filter-text-suffix="days">
				<div class="filter-name-container">
					<div  data-toggle-id="wduration-filter">
						<p class="filter-name">Duration<span class="glyphicon glyphicon-menu-right"></span></p>
					</div>
					<div id="duration-filter" class="dropdown-style">
						<p class="apply-filter">Apply</p>		
						<div class="radio-container">
							<label><input data-parameter-name="isShorterThanDuration" data-parameter-value="1"
								 data-filter-text="Shorter than" type="radio" name="duration"/>
								 Shorter than</label>
							<label><input data-parameter-name="isShorterThanDuration" data-parameter-value="0"
								 data-filter-text="Longer than" type="radio" name="duration" checked/>
								 Longer than</label>
						</div>
						<input data-parameter-name="duration" type="text" />
					</div>
				</div>
			</div>
			<div class="filter">
				<div class="filter-name-container">
					<div  data-toggle-id="wwork-days-filter">
						<p class="filter-name">Work Days<span class="glyphicon glyphicon-menu-right"></span></p>
					</div>
					<div id="work-days-filter" class="dropdown-style">
						<p class="apply-filter">Apply</p>		
						<div class="calendar-container v2">
							<div class="calendar"></div>
						</div>						
					</div>
				</div>
			</div>	
			<div id="applied-filters"></div>														
		</div>
		
		<div id="get-jobs-results">				
		
<!-- 		<div id="job-info-map" class="map right-border corner" data-do-init="1" -->
<%-- 				data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }"></div> --%>
				
				</div>	
		<div id="get-jobs-map-container">
			<div id="find-jobs-map"></div>
		</div>
	</div>
</div>

		<div id="job-info-mod" class="mod simple-header">
			<div class="mod-content">
				<div class="mod-header"></div>
				<div class="mod-body"></div>
			</div>
		</div>

<script
	src="https://maps.googleapis.com/maps/api/
		js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap_find_jobs">
</script>
	
<c:set var="doSkip_loadGoogleMapsApiForJobInfo" value="1"></c:set>
<%@ include file="../includes/Footer.jsp"%>