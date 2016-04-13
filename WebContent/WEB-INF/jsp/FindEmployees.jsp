<%@ include file="./includes/Header.jsp" %>
	<head>
	</head>

	<body>
		
		<div class="container">
			<h1>Find Employees</h1>
			<button id="findEmployees" class="btn btn-danger" type="button"
				 style="margin-bottom: 10px">Find Employees</button>
			<div style="width: 750px" class="panel panel-warning">
				<div class="panel-heading">Available Days to Work</div>
				<div class="panel-body">
					<div id='availableDays' class="input-group date" style="width: 250px">
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
								class="post-job-input form-control" id="homeCity"></input>
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
								class="post-job-input form-control" id="homeCity"
								value="${user.getHomeCity() }"></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="homeState"
							class="post-job-label col-sm-2 form-control-label">State</label>
						<div class="col-sm-10">
							<input name="state" type="text"
								class="post-job-input form-control" id="homeState"
								value="${user.getHomeState() }"></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="homeZipCode"
							class="post-job-label col-sm-2 form-control-label">Zip
							Code</label>
						<div class="col-sm-10">
							<input name="zipCode" type="text"
								class="post-job-input form-control" id="homeZipCode" 
								value="${user.getHomeZipCode() }"></input>
						</div>
					</div>
				</div>
			</div><!-- end home location panel -->
						
			<div style="width: 750px" class="panel panel-success">
				<div class="panel-heading">Employees</div>
				<div class="color-panel panel-body">		
				</div>
			</div>	<!-- end employees panel -->							
		
		</div><!-- end container -->
	
	
	
	</body>
	
	
	<script>
		$(document).ready(function(){
			$('#availableDays').datepicker({
				toggleActive: true,
				clearBtn: true,
				todayHighlight: true,
				startDate: new Date(),
				multidate: true		
				
			});	
			
			$('#findEmployees').click(function(){
				
// 				findEmployeesDto = {};
				dates= $("#availableDays").datepicker('getDates');
// 				
				var datesParameter = "";
				if(dates.length > 0){
					
					for(var i = 0; i < dates.length; i++){
						
						datesParameter += "&date=" + dates[i];
					}
				}else{datesParameter = "&date=-1"}
				alert(JSON.stringify(datesParameter))
				$.ajax({
					type : "GET",
					url : "http://localhost:8080/JobSearch/employees/find?userId=0" + datesParameter,
// 					contentType : "application/json",
					dataType : "application/json", // Response
// 					data : JSON.stringify(findEmployeesDto)
				}).done(function() {

				}).error(function() {

				});
				
			})
			
		})
		

	</script>

	

<%@ include file="./includes/Footer.jsp" %>