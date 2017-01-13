<%@ include file="../includes/TagLibs.jsp"%>

<div id="sortByHeader">
	<div data-toggle-id="sortByOptions" data-toggle-speed="1">
		<p>Sort</p><span class="glyphicon glyphicon-menu-down"></span>
	</div>
</div>
<div id="sortByOptions">
	<div class="sort-by-container" data-sort-by="Distance">
		<p class="name">Distance</p>
		<div class="sort-direction-container">
			<p class="sort-direction" data-is-ascending="1">Closest first</p>
			<p class="sort-direction" data-is-ascending="0">Farthest first</p>										
		</div>
	</div>
	<div class="sort-by-container" data-sort-by="StartTime">
		<p class="name">Start Time</p>
		<div class="sort-direction-container">
			<p class="sort-direction" data-is-ascending="1">Earliest first</p>
			<p class="sort-direction" data-is-ascending="0">Latest first</p>										
		</div>
	</div>
	<div class="sort-by-container" data-sort-by="EndTime">
		<p class="name">End Time</p>
		<div class="sort-direction-container">
			<p class="sort-direction" data-is-ascending="1">Earliest first</p>
			<p class="sort-direction" data-is-ascending="0">Latest first</p>										
		</div>
	</div>
			<div class="sort-by-container" data-sort-by="StartDate">
		<p class="name">Start Date</p>
		<div class="sort-direction-container">
			<p class="sort-direction" data-is-ascending="1">Earliest first</p>
			<p class="sort-direction" data-is-ascending="0">Latest first</p>										
		</div>
	</div>
	<div class="sort-by-container" data-sort-by="EndDate">
		<p class="name">End Date</p>
		<div class="sort-direction-container">
			<p class="sort-direction" data-is-ascending="1">Earliest first</p>
			<p class="sort-direction" data-is-ascending="0">Latest first</p>										
		</div>
	</div>	
	<div class="sort-by-container" data-sort-by="Duration">
		<p class="name">Duration</p>
		<div class="sort-direction-container">
			<p class="sort-direction" data-is-ascending="1">Shortest first</p>
			<p class="sort-direction" data-is-ascending="0">Longest first</p>										
		</div>
	</div>		
</div>
