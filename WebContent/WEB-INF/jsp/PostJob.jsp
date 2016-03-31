<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="./static/css/categories.css" />

<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" />


<!-- Time picker -->
<link rel="stylesheet" type="text/css" href="./static/External/jquery.timepicker.css" />
<script src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
<%-- <script src="<c:url value="/static/External/GruntFile.js" />"></script> --%>


</head>



<body>
	<input type="hidden" id="userId" value="${user.userId}" />
	<div style="display: none" class="container" id="submitJobsContainer">
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Pending Job Submissions</div>

			<div class="color-panel panel-body">
				<div id="pendingJobSubmissions"></div>

				<div class="form-group row">
					<div class="col-sm-offset-2 col-sm-10">
						<button id="submitJob" type="submit" class="btn btn-secondary" onclick="submitJobs()">Submit
							Job(s)</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div style="width: 750px" class="panel panel-success">

			<div class="panel-heading">Job Information</div>

			<div class="color-panel panel-body">
				<div>
					<div class="container">
						<div class="form-group row">
							<label for="jobName" class="col-sm-2 form-control-label">Job
								Name</label>
							<div class="col-sm-10">
								<input name="jobName" type="text"
									class="post-job-input form-control" id="jobName"
									placeholder="Job Name"></input>
							</div>
						</div>
					</div>

					<div class="container">
						<div class="form-group row">
							<label for="jobStreetAddress" class="post-job-label col-sm-2 form-control-label">Street Address</label>
							<div class="col-sm-10">
								<input name="streetAddress" type="text" class="post-job-input form-control" id="jobStreetAddress" placeholder="Street Address"></input>
							</div>
						</div>
						<div class="form-group row">
							<label for="jobCity" class="post-job-label col-sm-2 form-control-label">City</label>
							<div class="col-sm-10">
								<input name="city" type="text" class="post-job-input form-control" id="jobCity" placeholder="City"></input>
							</div>
						</div>
						<div class="form-group row">
							<label for="jobState" class="post-job-label col-sm-2 form-control-label">State</label>
							<div class="col-sm-10">
								<input name="state" type="text" class="post-job-input form-control" id="jobState"></input>
							</div>
						</div>	
						<div class="form-group row">
							<label for="jobZipCode" class="post-job-label col-sm-2 form-control-label">Zip Code</label>
							<div class="col-sm-10">
								<input name="zipCode" type="text" class="post-job-input form-control" id="jobZipCode" placeholder="Zip Code" ></input>
							</div>
						</div>														
					</div>
					
<!-- 					***************************************************************** -->
<!-- 					NOTE: Found the date rage picker here: http://www.daterangepicker.com/ -->
<!-- 					***************************************************************** -->
					<div class="container">
					
						<div class="form-group row">
							<label for="startTime" class="col-sm-2 form-control-label">Start
								Time</label>
							<div class="col-sm-10">
								<input id="startTime" type="text" class="post-job-input form-control time ui-timepicker-input"
								 autocomplete="off">
							</div>
						</div>
						
						
						
						
						<div class="form-group row">
							<label for="endTime" class="col-sm-2 form-control-label">Estimated End
								Time</label>
							<div class="col-sm-10">
								<input id="endTime" type="text" class="post-job-input form-control time ui-timepicker-input"
								 autocomplete="off">
							</div>
						</div>
													
						<div class="form-group row">
							<label class="post-job-label col-sm-2 form-control-label" for="jobDescription">Job Start and End Dates</label>
							<div class="col-sm-10">
							<input style="width: 250px" class="form-control" type="text" id="dateRange" />								
								<script type="text/javascript">
									$(function() {
									    $('#dateRange').daterangepicker({
									        locale: {
									            format: 'MM/DD/YYYY'
									        }
									    });
									});
								</script>
							</div>	
						</div>
					</div>
					
					<div class="container">
						<div style="margin-bottom: 25px" class="row">
							<label class="col-sm-2 form-control-label" for="jobDescription">Job
								Description</label>
							<div class="post-job-description col-sm-10">
								<textarea name="description" class="form-control"
									id="jobDescription" rows="3" placeholder="Job Description"></textarea>
							</div>
						</div>
					</div>


				
<!-- 					<div class="container"> -->
<!-- 						<div class="form-group row"> -->
<!-- 							<label for="jobDuration" class="post-job-label col-sm-2 form-control-label">Duration (days) -->
<!-- 								</label> -->
<!-- 							<div class="col-sm-10"> -->
<!-- 								<input name="jobDuration" type="text" -->
<!-- 									class="post-job-input form-control" id="jobDuration" -->
<!-- 									placeholder="Job Duration (days)"></input> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
										
					
					
					<div class="container">
						<div class="row">
							<label class="post-job-label col-sm-2 form-control-label">Categories</label>
							<div style="min-height: 50px; display:inline" id="selectedCategories">
							</div>
						</div>
					</div>
					
					<div class="container">
						<div class="row">
							<input id="selectedCategory" type="hidden"></input>
							<div style="margin-left: 125px" class="category-list-container form-group col-sm-10">
								<div id='0T'></div>
							</div>

						</div>
					</div>

					<input name="userId" value="${user.userId}" type="hidden"></input>

					<div class="form-group row">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-secondary"
								onclick="addJobToCart()">Add Job</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>

<script>

$(document).ready(function(){
	$('#startTime').timepicker({'scrollDefault': '7:00am'});
	$('#endTime').timepicker({'scrollDefault': '5:00pm'});
})

	var pageContext = "postJob";
	getCategoriesBySuperCat('0', function(response, categoryId) {
		appendCategories(categoryId, "T", response, function(){});
	})
	var jobs = [];
	var jobCount = 1;
	

</script>

<%@ include file="./includes/Footer.jsp"%>