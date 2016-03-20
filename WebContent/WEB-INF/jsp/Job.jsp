<%@ include file="./includes/Header.jsp"%>
<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<link rel="stylesheet" type="text/css" href="./static/css/global.css" />
<link rel="stylesheet" type="text/css"
	href="./static/css/employerActiveJob.css" />

</head>

<input type="hidden" id="userId" value="${user.userId}" />

<div class="container">
	<div class="job-options btn-group" role="group" aria-label="...">
		<a href="./edit" class="btn btn-default">Edit Job</a>
		<button onclick="markJobComplete(${job.getId()})" id="markJobComplete"
			class="btn btn-default">Mark Job Complete</button>
	</div>

	<div>
		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Name</div>
			<div id="jobName" class="panel-body">${job.getJobName() }</div>
		</div>

		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Location</div>
			<div id="jobLocation" class="panel-body">${job.getLocation() }</div>
		</div>

		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Description</div>
			<div id="jobDescription" class="panel-body">${job.getDescription() }</div>
		</div>

	</div>

	<br>
	<div id="container">

		<div style="width: 750px" class="panel panel-warning">
			<div class="panel-heading">Applicants</div>
			<div id="applicants" class="panel-body">
				<c:choose>
					<c:when test="${job.getApplicants().size() > 0 }">
						<table class="table table-condensed">
							<thead>
								<tr>

									<th>Applicant Name</th>
									<c:forEach items="${job.getApplicants().get(0).getRatings() }"
										var="rating">
										<th>${rating.getName() }</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${job.getApplicants() }" var="applicant">

									<tr id="applicant_${applicant.getUserId() }">

										<td><a href="#">${applicant.getFirstName() }
												${applicant.getLastName() } </a></td>
										<c:forEach items="${applicant.getRatings()}" var="rating">
											<td>${rating.getValue() }</td>
										</c:forEach>
										<td><button class="hire btn btn-info btn-sm margin-hori"
												onclick="hireApplicant(${applicant.getUserId()},${job.getId() })">
												Hire</button></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<div id="noApplicants">
							<div>No applicants</div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<div style="width: 750px" class="panel panel-warning">
			<div class="panel-heading">Employees</div>
			<div id="employees" class="panel-body">
				<c:choose>
					<c:when test="${job.getEmployees().size() > 0 }">
						<table class="table table-condensed">
							<thead>
								<tr>
									<th>Employee Name</th>
									<c:forEach items="${job.getEmployees().get(0).getRatings() }"
										var="rating">
										<th>${rating.getName() }</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${job.getEmployees() }" var="employee">
									<tr id="employee_${employee.getUserId() }">

										<td><a href="#">${employee.getFirstName() }
												${employee.getLastName() } </a></td>
										<c:forEach items="${employee.getRatings()}" var="rating">
											<td>${rating.getValue() }</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<div id="noEmployees">
							<div>No Employees</div>
						</div>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
		var activeJob = ${job};

		appendUsers_ApplicantsAndEmployeesForActiveJob(activeJob, "applicants", "employees");

	</script>

<%@ include file="./includes/Footer.jsp"%>