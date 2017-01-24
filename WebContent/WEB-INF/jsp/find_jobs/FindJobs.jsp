<%@ include file="../includes/Header.jsp"%>


	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js" />"></script>


	<script src="<c:url value="/static/javascript/find_jobs/Filters.js" />"></script>
	<script src="<c:url value="/static/javascript/find_jobs/AjaxCall.js" />"></script>
	<script src="<c:url value="/static/javascript/find_jobs/Map.js" />"></script>
	<script src="<c:url value="/static/javascript/find_jobs/FilterResultsInteraction.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities/Modal.js" />"></script>
	
	<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />

	<link rel="stylesheet" type="text/css"	href="../static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/calendar.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/Templates/Modal.css" />
	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/find_jobs/findJobs.css" />	
	<link rel="stylesheet" type="text/css"	href="../static/css/find_jobs/sortBy.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/find_jobs/filters.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/find_jobs/filteredJobs.css" />

	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>


	<div class="container">
		<div id="filtersContainer">
			<div><%@ include file="./Filters.jsp" %></div>
		</div>
		<div id="mainBottom">
			<div class="row" >
				<div id="filteredJobsContainer" class="col-sm-3">
					<div id="sortByContainer"><%@ include file="./SortBy.jsp" %></div>
			 		<div id="getMoreJobsContainer-top" class="get-more-jobs">Get More Jobs</div> 	
					<div id="filteredJobs">
						<c:if test="${!empty jobDtos }">
							<%@ include file="./Render_GetJobs_InitialRequest.jsp" %>
						</c:if>
					</div>				
				</div>
				<div id="mapContainer" class="col-sm-9" data-init-map-on-load="${!empty jobDtos ? 1 : 0 }">				
					<div id="map" class="right-border"></div>				
				</div>		
			</div>
		</div>
	</div>

<!-- // ******************************************** -->
<!-- The "async" an "defer" attributes cannot be included -->
<!-- Since the map can be set with jobs on page load, the map needs be be set -->
<!-- BEFORE the $(document).ready() function is called -->
<!-- These two attributes allow the $(document).ready() to run BEFROE the map is initialized.
<!-- This sequence obviously cannot be allowed to happen  -->
<!-- // ******************************************** -->
<!-- 	<script async defer  -->
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
	</script>
	

<%@ include file="../includes/Footer.jsp"%>