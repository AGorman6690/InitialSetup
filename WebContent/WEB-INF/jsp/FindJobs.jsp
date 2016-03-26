<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<script src="<c:url value="/static/javascript/Application.js" />"></script>
<!-- 		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" /> -->
<!-- 		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" /> -->
<link rel="stylesheet" type="text/css" href="./static/css/global.css" />


</head>

<body>
	<input type="hidden" id="userId" value="${user.userId}" />

	<div class="container">
		<div style="height: 500px; width: 500px" id="map"></div>

		<div style="margin-top: 15px; width: 750px"
			class="panel panel-success">
			<div class="panel-heading">Filters</div>


			<div style="position:relative" class="color-panel panel-body">
				
				<ul class="list-group">
					<li class="list-group-item"><a style="margin-bottom: 10px" class="btn btn-warning" data-toggle="collapse" data-target="#collapseDistance">
					Distance</a>
										
						<div class="collapse" id="collapseDistance">
						
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon"
									id="sizing-addon2">Number of miles</span> <input value="987987" id="radius"
									type="text" class="form-control" aria-describedby="sizing-addon2">
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
										id="sizing-addon2">Zip Code</span> <input value="55119" id="fromZipCode"
										type="text" class="form-control"
										aria-describedby="sizing-addon2">
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
							 data-toggle="collapse" data-target="#collapseDate">Date (currently not working)</a>										
						<div class="collapse" id="collapseDate">
						

										
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon">Start Date</span>		
								<div class="input-group date">
								  <input id='filterStartDate' type="text" class="form-control"><span class="input-group-addon">
								  <i class="glyphicon glyphicon-th"></i></span>
								</div>					
							</div>		
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon">End Date</span>
									<div class="input-group date">
								  		<input id='filterEndDate' type="text" class="form-control"><span class="input-group-addon">
								  		<i class="glyphicon glyphicon-th"></i></span>
									</div>
							</div>	
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon">Duration (days)</span>
								<input id="filterDuration"
									type="text" class="form-control" aria-describedby="sizing-addon2">
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
		$('.input-group.date').datepicker({
		});
	} );

	var pageContext = "findJob";

	getCategoriesBySuperCat('0', function(response, categoryId) {
		
		appendCategories(categoryId, "F", response);
	});

	function filterJobs() {
		
		var filter = {};
		
		filter.radius = $("#radius").val();
		filter.fromAddress = $.trim($("#fromStreetAddress").val() + " "
				+ $("#fromCity").val() + " " + $("#fromState").val() + " "
				+ $("#fromZipCode").val());

		filter.categories = getCategoryIds("selectedCategories");
// 		filter.startDate = ""// $("#filterStartDate").val();
// 		filter.endDate = ""//$("#filterEndDate").val();
		
		if (filter.radius != "" && filter.fromAddress != "") {

			getFilteredJobs(filter, function(filter) {

				var myLatLng = {
					lat : filter.distanceFromLat,
					lng : filter.distanceFromLng
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
	

	function initMap() {

		//Eventually initialize it to a user defualt
		var myLatLng = {
			lat : 44.954445,
			lng : -93.091301
		};

		var map = new google.maps.Map(document.getElementById('map'), {
			zoom : 8,
			center : myLatLng
		});

	}
</script>

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
	
</script>

<%@ include file="./includes/Footer.jsp"%>