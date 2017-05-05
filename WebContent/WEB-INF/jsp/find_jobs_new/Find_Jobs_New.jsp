<%@ include file="../includes/Header.jsp"%>
<script src="/JobSearch/static/javascript/find_jobs/Find_jobs.js" type="text/javascript"></script>
<script src="/JobSearch/static/javascript/Utilities/FormUtilities.js" type="text/javascript"></script>
<link href="/JobSearch/static/css/find_jobs/find_jobs_new.css" rel="stylesheet" />

<div>
	<div id="filters">
		<div id="location-filter">
			<input id="miles" type="text" placeholder="Miles"/>
			<span>From</span>
			<input id="city" type="text" placeholder="City"/>
			<select id="state"></select>
			<input id="zip-code" type="text" placeholder="Zip"/>
		</div>
		<div id="other-filters">
			<div class="filter">				
			<span class="remove-filter glyphicon glyphicon-remove"></span>
				<p data-toggle-id="start-date-filter">
					Start Date<span class="glyphicon glyphicon-menu-down"></span>
				</p>
				<div id="start-date-filter" class="dropdown-style">
					<h3 class="approve-filter"><span class="glyphicon glyphicon-ok"></span></h3>
					<div class="radio-container">
						<label><input type="radio" name="start-date" data-is-before="1"/>Before</label>
						<label><input type="radio" name="start-date" data-is-before="0"/>After</label>
					</div>
					<div class="calendar-container v2">
						<div class="calendar"></div>
					</div>						
				</div>
			</div>
			<div class="filter">
				<p data-toggle-id="end-date-filter">
					<span class="remove-filter glyphicon glyphicon-remove"></span>End Date<span class="glyphicon glyphicon-menu-down"></span>
				</p>
				<div id="end-date-filter" class="dropdown-style">
					<h3 class="approve-filter"><span class="glyphicon glyphicon-ok"></span></h3>
					<div class="radio-container">
						<label><input type="radio" name="end-date" data-is-before="1"/>Before</label>
						<label><input type="radio" name="end-date" data-is-before="0"/>After</label>
					</div>		
					<div class="calendar-container v2">
						<div class="calendar"></div>
					</div>	
				</div>
			</div>
			<div class="filter">
				<p data-toggle-id="start-time-filter">
					<span class="remove-filter glyphicon glyphicon-remove"></span>Start Time<span class="glyphicon glyphicon-menu-down"></span>
				</p>
				<div id="start-time-filter" class="dropdown-style">
					<h3 class="approve-filter"><span class="glyphicon glyphicon-ok"></span></h3>
					<div class="radio-container">
						<label><input type="radio" name="start-time" data-is-before="1"/>Before</label>
						<label><input type="radio" name="start-time" data-is-before="0"/>After</label>
					</div>
					<select class="time"></select>
				</div>
			</div>
			<div class="filter">
				<p data-toggle-id="end-time-filter">
					<span class="remove-filter glyphicon glyphicon-remove"></span>End Time<span class="glyphicon glyphicon-menu-down"></span>
				</p>
				<div id="end-time-filter" class="dropdown-style">
					<h3 class="approve-filter"><span class="glyphicon glyphicon-ok"></span></h3>
					<div class="radio-container">
						<label><input type="radio" name="end-time" data-is-before="1"/>Before</label>
						<label><input type="radio" name="end-time" data-is-before="0"/>After</label>
					</div>
					<select class="time"></select>							
				</div>
			</div>
			<div class="filter">
				<p data-toggle-id="duration-filter">
					<span class="remove-filter glyphicon glyphicon-remove"></span>Duration<span class="glyphicon glyphicon-menu-down"></span>
				</p>
				<div id="duration-filter" class="dropdown-style">
					<h3 class="approve-filter"><span class="glyphicon glyphicon-ok"></span></h3>
					<div class="radio-container">
						<label><input type="radio" name="duration" data-is-shorter-than="1"/>Shorter than</label>
						<label><input type="radio" name="duration" data-is-shorter-than="0"/>Longer than</label>
					</div>
					<input type="text" />
				</div>
			</div>
			<div class="filter">
				<p data-toggle-id="work-days-filter">
					<span class="remove-filter glyphicon glyphicon-remove"></span>Work Days<span class="glyphicon glyphicon-menu-down"></span>
				</p>
				<div id="work-days-filter" class="dropdown-style">
					<div class="calendar-container v2">
						<div class="calendar"></div>
					</div>						
				</div>
			</div>															
		</div>
		<button id="get-jobs" class="sqr-btn green">Get Jobs</button>
	</div>
	<div>
		<div>
		
		</div>
		<div>
		
		</div>	
	</div>
</div>


<%@ include file="../includes/Footer.jsp"%>