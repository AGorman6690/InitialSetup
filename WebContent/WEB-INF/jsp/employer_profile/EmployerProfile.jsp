<%@ include file="../includes/Header.jsp"%>


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


</head>


<body>

	
	
	<div class="container">
	
		<div id="jobTypeSelectionBoard">
			<div id="jobsWaitingToStart" class="job-type selected-blue">Waiting To Start</div>
			<div id="jobsInProcess" class="job-type">In Process</div>
			<div id="jobsCompleted" class="job-type">Complete</div>			
		</div>
		

		<div id="waiting" class="job-type-section">
			<%@ include file="./JobsWaitingToStart_Employer.jsp" %>
		</div>
		<div id="active" class="job-type-section">
			<%@ include file="./JobsInProcess_Employer.jsp" %>
		</div>
		
		

		<c:choose>
			<c:when test="${jobDtos_jobsCompleted.size() >0 }">			
				<div id="complete" class="section completed-jobs-container job-type-section">
					<div class="header"><h3>Jobs Complete</h3></div>
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
								<c:forEach items="${jobDtos_jobsCompleted }" var="jobDto">
									<tr class="static-row" id="${jobDto.job.id}">
										<td class="job-name"><a href="/JobSearch/job/${jobDto.job.id}?c=complete&p=2" class="accent">${jobDto.job.jobName}</a></td>
		
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
	
	$(".job-type").click(function(){
		
		var id = $(this).attr("id");
		var sectionIdToShow;
		highlightArrayItem(this, $("#jobTypeSelectionBoard").find(".job-type"), "selected-blue");
		
		$.each($("body").find(".job-type-section"), function(){
			$(this).hide();
		})
		
		
		if(id == "jobsWaitingToStart"){
			sectionIdToShow = "waiting";
		}
		else if(id == "jobsInProcess"){
			sectionIdToShow = "active";
		}
		else if(id == "jobsCompleted"){
			sectionIdToShow = "complete";
		}
		
		$("#" + sectionIdToShow).show();	
				
	})
	



})



</script>

<%@ include file="../includes/Footer.jsp"%>
