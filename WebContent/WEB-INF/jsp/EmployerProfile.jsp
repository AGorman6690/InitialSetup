<%@ include file="./includes/Header.jsp"%>

<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
<link rel="stylesheet" type="text/css" href="../static/css/profile.css" />
<link rel="stylesheet" type="text/css" href="../static/css/wageNegotiation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />


<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
</head>


<body>

	
	
	<div class="container">
<%-- 		<div>${vtFailedWageNegotiations }</div> --%>
		<div>${vtYetToStartJobs }</div>
		<div>${vtActiveJobs }</div>

		<c:choose>
			<c:when test="${completedJobs.size() >0 }">			
				<div class="section completed-jobs-container">
					<div class="header"><h3>Completed Jobs</h3></div>
					<div class="section-body">
						<table id="" class="main-table-style">
							<thead>
								<tr>
									<th>Job Name</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
		<!-- 						For each active job -->
								<c:forEach items="${completedJobs }" var="completedJobDTO">
									<tr class="static-row" id="${completedJobDTO.job.id}">
										<td class="job-name"><a href="/JobSearch/completed/job/${completedJobDTO.job.id }" class="accent">${completedJobDTO.job.jobName}</a></td>
		
<!-- 										<td > -->
<%-- 										<a href="/JobSearch/job/${completedJobDTO.job.id }/employees/rate"><button class="square-button">Rate Employees</button></a> --%>
<!-- 										</td> -->
									</tr>						
		
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</c:when>
		</c:choose>
	</div>

</body>

<script>



$(document).ready(function(){

	$("#activeJobsTable tr td").click(function(){
		window.location = '../job/' + $(this).parent().attr('id');
	})	
	
	
	$(".mark-complete-ouououoiuiou").click(function(){
	
		var headers = {};
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");
		
		var jobId = $($(this).parents("tr")[0]).attr("id");
		
		$.ajax({
			type : "PUT",
			url : "/JobSearch/job/" + jobId + "/markComplete",
			headers : headers
		}).done(function() {
			$('#home')[0].click();
		}).error(function() {
			$('#home')[0].click();

		});
		
	})
	


})


</script>

<%@ include file="./includes/Footer.jsp"%>

