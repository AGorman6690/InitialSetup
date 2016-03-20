<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
</head>

<input type="hidden" id="userId" value="${user.userId}" />

<body>
	<input type="hidden" id="userId" value="${user.userId}">
	<div class="container">
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Jobs you have applied to</div>
			<div id="appliedTo" class="color-panel panel-body"></div>
		</div>
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Jobs you were hired for</div>
			<div id="hiredFor" class="color-panel panel-body"></div>
		</div>
	</div>
</body>

<script>
	//Get all active jobs that the user has applied for
	getApplicationsByUser($("#userId").val(), function(applications) {
		appendJobs_EmployeeAppliedTo("appliedTo", applications, function() {
		})
	});

	//Get the user's employement. Show both active and inactive jobs
	getEmploymentByUser("${user.userId}", function(employment) {
		//alert("callback getEmploymentByUser");			
		appendJobs_EmployeeHiredFor("hiredFor", employment, function() {
		})
	});
</script>



<%@ include file="./includes/Footer.jsp"%>