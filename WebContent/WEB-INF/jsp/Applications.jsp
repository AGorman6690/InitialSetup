<%@ include file="./includes/Header.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Ratings.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
		<style>
			.section{
				color: red;
				font-size: 4em;
			}		
		</style>			
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	</head>
	
<!-- 	Store the user's id in an element as opposed to accessing it through a model object.
	 Model objects are not within an external js file's scope-->
	<input id='userId' type='hidden' value="{user.userId}">

	<h1>Active jobs</h1>
	<select multiple id="activeJobs" style= "width: 200px">
	</select>
	
	<h1>Applicants for the selected job</h1>	
	<select multiple id="applicants" style="width: 200px">
	</select>
	
	<br>
	<button id ="hireApplicant" type="button">Extend offer to applicant</button>
	
	<br>
	<h1>Outst</h1>
	<script>
		//Get and populate user's active jobs
		getJobsByUser("${user.userId}", function(response){
			populateJobs(response, document.getElementById("activeJobs"), 1);
		});
	</script>


<%@ include file="./includes/Footer.jsp" %>
