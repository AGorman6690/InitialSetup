<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="./static/css/employerProfile.css" />
<link rel="stylesheet" type="text/css" href="./static/css/global.css" />
<style>
.section {
	color: red;
	font-size: 4em;
}
</style>
<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
</head>


<body>
	<input type="hidden" id="userId" value="${user.userId}">
	<div class="container">
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Active Jobs</div>
			<div id="activeJobs" class="color-panel panel-body">
				<c:choose>
					<c:when test="${user.getActiveJobs().size() > 0 }">
						<table id="activeJobsTable" class="table table-hover">
							<thead>
								<tr>
									<th>Job Name</th>
									<th>New Applications</th>
									<th>Open Applications</th>
									<th>Employees</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${user.getActiveJobs() }" var="activeJob">
									<tr id="${activeJob.getId()}">
										<td>${activeJob.getJobName()}</td>
										<td>(not built)</td>
										<td>${activeJob.getApplications().size()}</td>
										<td>${activeJob.getEmployees().size()}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<div>No active jobs</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Completed Jobs</div>
			<div id="completedJobs" class="color-panel panel-body">
				<c:choose>
					<c:when test="${user.getCompletedJobs().size() > 0 }">
						<table class="table table-hover">
							<thead>
								<tr>
									<th>Job Name</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${user.getCompletedJobs() }" var="completedJobDTO">
									<tr id="${completedJobDTO.getJob().getId() }">
										<td><a href="./job/${completedJob.getId() }">
												${completedJobDTO.getJob().getJobName() }</a></td>
										<td><a
											href="../job/${completedJobDTO.getJob().getId() }/rateEmployees"
											class="btn btn-info btn-sm margin-hori"> Rate Employees</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<div>No completed jobs</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

	</div>
</body>

<script>

$(document).ready(function(){
	$("#activeJobsTable tr td").click(function(){
		window.location = '../job/' + $(this).parent().attr('id');
	})	
})


</script>

<%@ include file="./includes/Footer.jsp"%>

