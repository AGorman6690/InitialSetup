<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<%-- 		<script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/Display.js" />"></script> --%>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="./static/css/categories.css" />
<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" />
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
							<label for="jobLocation" class="col-sm-2 form-control-label">Location</label>
							<div class="col-sm-10">
								<input name="location" type="text"
									class="post-job-input form-control" id="jobLocation"
									placeholder="Location"></input>
							</div>
						</div>
					</div>

					<div class="container">
						<div class="row">
							<label class="col-sm-2 form-control-label" for="jobDescription">Job
								Description</label>
							<div class="post-job-description col-sm-10">
								<textarea name="description" class="form-control"
									id="jobDescription" rows="3" placeholder="Job Description"></textarea>
							</div>
						</div>
					</div>

					<br>
					<div class="container">
						<div class="row">
							<label class="col-sm-2 form-control-label">Job Category</label> <input
								id="selectedCategory" type="hidden"></input>
							<div class="category-list-container form-group col-sm-10">
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
	var pageContext = "postJob";
	getCategoriesBySuperCat('0', function(response, elementId) {

		appendCategories(elementId, response);
	})

	var jobs = [];
	var jobCount = 1;
</script>

<%@ include file="./includes/Footer.jsp"%>