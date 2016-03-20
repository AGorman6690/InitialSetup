<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
<%-- 		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/Ratings.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/Display.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
		
		<link rel="stylesheet" type="text/css" href="./static/css/employerProfile.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/global.css" />	
			
		<style>
			.section{
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
			  <div class="panel-heading">
			    Active Jobs
			  </div>
			  <div id="activeJobs" class="color-panel panel-body"></div>
			</div>
			<div style="width: 750px" class="panel panel-success">
			  <div class="panel-heading">
			    Completed Jobs
			  </div>
			  <div id="completedJobs" class="color-panel panel-body"></div>
			</div>
		</div>
		
	 <div id="map" style="width: 320px; height: 480px;"></div>
	  <div>
	    <input id="address" type="textbox" value="Sydney, NSW">
	    <input type="button" value="Encode" onclick="codeAddress()">
	  </div>
	  
	</body>
	
	<script >		
		
		//Get and populate user's active and completed jobs
 		getActiveJobsByUser($("#userId").val(), function(jobs){
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