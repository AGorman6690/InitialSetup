<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>
	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<script src="<c:url value="/static/javascript/Application.js" />"></script>
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />

	</head>

	<input type="hidden" id="userId" value="${user.userId}"/>
	<input type="hidden" id="jobId"/>

	<br>
	<h1>Job</h1>
	<div id="jobName">(it would be nice to pass the entire job JSON object to this page, not just the job id)</div>
	
	<br>	
	<div id="container">
	
		<br>
		<button id="markJobComplete" type="button" class="btn btn-default">Mark Job Complete</button>
	
		<br>
		<h1>Applicants</h1>
		<div id="applicants">
		</div>
	
		<br>
		<h1>Employees</h1>

		<div id="employees">
		</div>
		
	
	</div>
	
	<script>
		var jobId = parent.document.URL.substring(parent.document.URL.indexOf('?jobId=') + 7, parent.document.URL.length);
		$("#jobId").val(jobId)

		getApplicants(jobId, function(users){
			
// 			for(var i = 0; i<user.length; i++){
				
				
// 			})
			
			if(users.length > 0){
				appendApplicants("applicants", users, function(){})
			}else{
				$("#applicants").append("<div>No applicants</div>")
			}
				
		})
		
		getEmployeesByJob(jobId, function(users){
			
			if(users.length > 0){
				appendUsers("employees", users, function(){})
			}else{
				$("#employees").append("<div>No employees</div>")
			}
			
		})
		
		
		document.getElementById("markJobComplete").addEventListener("click", function() {
		    markJobComplete(jobId, function(){})
		});


	</script>

<%@ include file="./includes/Footer.jsp" %>