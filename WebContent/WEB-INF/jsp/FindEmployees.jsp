<%@ include file="./includes/Header.jsp" %>
<head>
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/findEmployees.css" />
</head>

<body>
	<div class="container">
		<div class="section">
			<div class="header2">
				<h3>Find Employees</h3>
			</div>
			
			<div class="section-body">
				<div class="sub-header">
					<h4>Location</h4>
					<div class="body-element-container bottom-border-thinner" id="locationContainer">
						<input id="miles" placeholder="Number of" class="form-control">
						<div id="milesFromText">Miles From</div>
						<input id="miles" placeholder="City" class="form-control">
						<input id="miles" placeholder="State" class="form-control">
						<input id="miles" placeholder="Zip Code" class="form-control">
					</div>
				</div>

			</div>
			
			<div class="section-body">
				<div class="sub-header">
					<h4>Categories</h4>
					<div class="body-element-container bottom-border-thinner" id="categoriesContainer">
					</div>
				</div>

			</div>			
		
		</div>
	</div><!-- end container -->



</body>


<script>

	$(document).ready(function(){


	}) 


</script>



<%@ include file="./includes/Footer.jsp" %>