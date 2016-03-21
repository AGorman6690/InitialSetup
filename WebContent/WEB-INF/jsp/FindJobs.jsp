<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
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


			<div class="color-panel panel-body">
				
				<ul class="list-group">
					<li class="list-group-item"><a style="margin-bottom: 10px" class="btn btn-warning" data-toggle="collapse" data-target="#collapseDistance">
					Distance</a>
										
						<div class="collapse" id="collapseDistance">
						
							<div class="job-location-container input-group">
								<span class="job-location-label input-group-addon"
									id="sizing-addon2">Number of miles</span> <input id="radius"
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
										id="sizing-addon2">Zip Code</span> <input id="fromZipCode"
										type="text" class="form-control"
										aria-describedby="sizing-addon2">
								</div>
							</div>					
						
						
						</div>
					</li>
					
					<li class="list-group-item"><a style="margin-bottom: 10px" class="btn btn-warning" data-toggle="collapse" data-target="#0F">
					Categories</a>
						<div class="collapse" id="0F">
						</div>
						
					</li>
				</ul>
			
				<br>
				<button id="filterJobs" onClick="filterJobs()" type="button"
					class="btn btn-info">Filter Jobs</button>


			</div>
		</div>
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Select a category to view jobs</div>
			<div id='0T' class="color-panel panel-body"></div>
		</div>

		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Available Jobs</div>
			<div id='jobList' class="color-panel panel-body"></div>
		</div>
	</div>


</body>

<script>

	var pageContext = "findJob";
	
	getCategoriesBySuperCat('0', function(response, elementId) {
	
		appendCategories(elementId, response);
	});
// 	//Get the seed categories.
// 	//Seed categories are sub categories to a category with id=0
// 	getCategoriesBySuperCat('0', function(response, elementId) {

// 		appendFirstLevelCategories_FindJobs(elementId, response, function() {

// 			var arr = $('#' + elementId + 'T').find('li');
// 			for (var i = 0; i < arr.length; i++) {

// 				getCategoriesBySuperCat(arr[i].id,
// 						function(response, elementId) {

// 							appendFirstLevelCategories_FindJobs(elementId,
// 									response, function() {

// 									})
// 						})
// 			}
// 		})
// 	})
	
	
// 	getCategoriesBySuperCat('0', function(response, elementId) {

// 		appendFirstLevelCategories_FilterCategories(elementId, response, function() {

// 			var arr = $('#' + elementId + 'F').find('li');
// 			for (var i = 0; i < arr.length; i++) {

// 				getCategoriesBySuperCat(arr[i].id,
// 						function(response, elementId) {

// 					appendFirstLevelCategories_FilterCategories(elementId,
// 									response, function() {

// 									})
// 						})
// 			}
// 		})
// 	})

	function filterJobs() {
		var radius = $("#radius").val();
		var fromAddress = $.trim($("#fromStreetAddress").val() + " "
				+ $("#fromCity").val() + " " + $("#fromState").val() + " "
				+ $("#fromZipCode").val());
		
		var categories = getCheckedCheckboxesId("0F");

			if (radius != "" && fromAddress != "") {
				getFilteredJobs(radius, fromAddress, categories, function(filter) {
// 					alert(JSON.stringify(filter))
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