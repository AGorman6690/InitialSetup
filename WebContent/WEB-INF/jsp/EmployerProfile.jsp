<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Ratings.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<style>
			.section{
				color: red;
				font-size: 4em;
			}		
		</style>			
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	</head>

	<h1>Here is your profile ${user.firstName} ${user.userId}</h1>
	
	<input type="hidden" id="userId" value="${user.userId}">
	
	
	<div class="container">
		<h1>Active jobs</h1>
		<div id="activeJobs"></div>
		
		<br>
		<h1>Completed jobs</h1>
		<div id="completedJobs"></div>
	</div>
	
	<script >		
		
		//Get and populate user's active and completed jobs
 		getActiveJobsByUser_AppCat($("#userId").val(), function(jobs){
			if(jobs.length>0){
 				appendJobs_EmployerActive("activeJobs", jobs, function(elementId, jobs){})	
			}else{
				$("#activeJobs").append("<div>No active jobs</div>")
			}
 		});
		
		//Get and populate user's active and completed jobs
 		getCompletedJobsByUser($("#userId").val(), function(jobs){
			if(jobs.length>0){
				appendJobs_EmployerComplete("completedJobs", jobs, function(elementId, jobs){})	
			}else{
				$("#completedJobs").append("<div>No completed jobs</div>")
			}		
 		});
		
	</script>
				
		
<%@ include file="./includes/Footer.jsp" %>