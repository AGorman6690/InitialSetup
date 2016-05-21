<%@ include file="./includes/Header.jsp" %>
	<head>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
	</head>

	<body>

		<div class="container">
			<h1>Find Employees</h1>
			<button id="findEmployees" class="btn btn-danger" type="button"
				 style="margin-bottom: 10px">Find Employees</button>
			<div style="width: 750px" class="panel panel-warning">
				<div class="panel-heading">Available Days to Work</div>
				<div class="panel-body">
					<div id='availableDays' data-date-format="MM/DD/YYYY" class="input-group date" style="width: 250px">
			  		</div>
				</div>
			</div>


			<div style="width: 750px" class="panel panel-warning">
				<div class="panel-heading">Employee Location</div>
				<div class="panel-body">

					<div class="form-group row">
						<label for="radius"
							class="post-job-label col-sm-2 form-control-label">Number of miles:</label>
						<div class="col-sm-10">
							<input name="city" type="text"
								class="post-job-input form-control" id="radius" value="50000"></input>
						</div>
					</div>

					<h4>
						<span class="label label-primary">From: (at least one field is required)</span>
					</h4>
					<div class="form-group row">
						<label for="homeCity"
							class="post-job-label col-sm-2 form-control-label">City</label>
						<div class="col-sm-10">
							<input name="city" type="text"
								class="post-job-input form-control" id="homeCity"></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="homeState"
							class="post-job-label col-sm-2 form-control-label">State</label>
						<div class="col-sm-10">
							<input name="state" type="text"
								class="post-job-input form-control" id="homeState""></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="homeZipCode"
							class="post-job-label col-sm-2 form-control-label">Zip
							Code</label>
						<div class="col-sm-10">
							<input name="zipCode" type="text"
								class="post-job-input form-control" id="homeZipCode"
								value="55119"></input>
						</div>
					</div>
				</div>
			</div><!-- end home location panel -->

			<div class="panel panel-info">
				<div class="panel-heading">Categories</div>
				<div class="panel-body">
					<div style="display: inline" id="selectedCategories">
					</div>
					<div id="addCategories" style="display: none"></div>
					<div id="removeCategories" style="display: none"></div>
					<br>
					<div id="0T"></div>
				</div>
			</div><!-- end categories panel -->


			<div style="width: 750px" class="panel panel-success">
				<div class="panel-heading">Employees</div>
				<div class="color-panel panel-body" id="employees">
				</div>
			</div>	<!-- end employees panel -->

		</div><!-- end container -->



	</body>


	<script>

		var pageContext = "profile";

		getCategoriesBySuperCat('0', function(response, categoryId) {
			appendCategories(categoryId, "T", response, function() {
			});
		})

		$(document).ready(function(){



			$('#availableDays').datepicker({
				toggleActive: true,
				format: "yyyy-MM-dd",
				clearBtn: true,
				todayHighlight: true,
				startDate: new Date(),
				multidate: true

			});

			$('#findEmployees').click(function(){

				var radius = $("#radius").val();
				var city = $("#homeCity").val();
				var state = $("#homeState").val();
				var zipCode = $("#homeZipCode").val();

				if(radius > 0 && (city != "" || state != "" || zipCode != "")){

					//Location
					var parameters = "?radius=" + radius;
					parameters += "&city=" + $("#homeCity").val();
					parameters += "&state=" + $("#homeState").val();
					parameters += "&zipCode=" + $("#homeZipCode").val();

					//Dates
					var dates = [];
// 					var str = $("#arrayDates").val();
// 					str = str.substring(1, str.length -1);
// 					dates = str.split(",");
					var dates= $("#availableDays").datepicker('getDates');
					if(dates.length > 0){
						for(var i = 0; i < dates.length; i++){
							parameters += "&date=" + dates[i];
						}
					} else{parameters += "&date=-1"}

					//Categories
					var categories = getCategoryIds("selectedCategories");
					if(categories.length > 0){
						for(var i = 0; i < categories.length; i++){
							parameters += "&categoryId=" + categories[i];
						}
					} else{parameters += "&categoryId=-1"}

// 	 				alert(JSON.stringify(parameters))
					$.ajax({
						type : "GET",
						url : environmentVariables.LaborVaultHost + "/JobSearch/employees/filter" + parameters,
						dataType : "json", // Response
						success: _success,
				        error: _error
					    });

					function _success(employees){

						$("#employees").empty();

						var j = -1;
						var r = [];
						r[++j] = '<table id="filterEmployeesTable" class="table table-hover table-striped'
									+ ' table-bordered" cellspacing="0" width="100%">';
						r[++j] = 	'<thead>';
						r[++j] = 		'<tr>';
						r[++j] =			'<th>Employee Name</th>';
						r[++j] =			'<th>Categories</th>';
						r[++j] =			'<th>Endorsements</th>';
						r[++j] =			'<th>Rating</th>';
						r[++j] =			'<th>Distance From Job</th>';
						r[++j] =		'</tr>';
						r[++j] =	'</thead>';
						r[++j] = 	'<tbody>';

						for(var i = 0; i < employees.length; i++){
							var e = employees[i];
							r[++j] = '<tr id="user_' + e.userId + '" class="clickToWorkHistory">';
							r[++j] =	'<td>' + e.firstName + " " + e.lastName + '</td>';

							var categoryNames = "";
							for (var k = 0; k < e.categories.length; k++){
								categoryNames += e.categories[k].name;
								if ( k < e.categories.length - 1){
									categoryNames +=  ", ";
								}
							}
							r[++j] = 	'<td>' + categoryNames + '</td>';
							r[++j] = 	'<td>(not built)</td>';
							r[++j] = 	'<td>' + e.rating + '</td>';
							r[++j] = 	'<td>' + e.distanceFromJob + '</td>';
							r[++j] = '</tr>';
						}

						$("#employees").append(r.join(''));
						$('#filterEmployeesTable').DataTable();

						$(".clickToWorkHistory").click(function(){
// 							alert(3)
			 				elementId = $(this).attr('id');
							var idBegin = elementId.indexOf("_") + 1;
							var userId =  elementId.substring(idBegin);

							window.location = "../jobs/completed/employee/?userId=" + userId + "&c=1";
						})

					}

					function _error(response){

					}
				}
			})	//end find employees click function
		}) //end document ready


	</script>



<%@ include file="./includes/Footer.jsp" %>