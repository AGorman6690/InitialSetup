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
<!-- ../static/css/  C:/Users/Owner/git/InitialSetup/WebContent/static/css/ratings.css-->
	
	
	

  

	
	<h1>Here is your profile ${user.firstName} ${user.userId}</h1>
	
	<input type="hidden" id="userId" value="${user.userId}">
<!-- **********************************************	 -->
<!-- 	****** JOBS ****** -->
<!-- **********************************************	 -->
	
	<h1>Active jobs</h1>
	<div class="container">
		<div id="activeJobs"></div>
	</div>
	
	<script >		
		
		//Get and populate user's active and completed jobs
 		getActiveJobsByUser_AppCat($("#userId").val(), function(jobs){
//  			alert(JSON.stringify(jobs))
 			appendJobs_EmployerActive("activeJobs", jobs, function(elementId, jobs){})		
 		});
		
	</script>
				
		
<%@ include file="./includes/Footer.jsp" %>