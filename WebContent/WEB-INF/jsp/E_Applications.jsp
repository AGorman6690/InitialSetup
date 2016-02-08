<%@ include file="./includes/Header.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Ratings.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
		<script src="<c:url value="/static/javascript/Application.js" />"></script>
		
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
	<input id='userId' type='hidden' value="${user.userId}">

	<h1>Applied to jobs</h1>
	<select multiple id="activeJobs" style= "width: 200px">
	</select>
	
<!-- 	<h1>Applicants for the selected job</h1>	 -->
<!-- 	<select multiple id="applicants" style="width: 200px"> -->
<!-- 	</select> -->
	
	<br>
	
	<br>
	<h1>Outstanding offers</h1>
	<select multiple id="offeredJobs" style="width: 200px">
	</select>
	<br>
	<button id ="acceptJobOffer" type="button">Accept offer</button>
	
<!-- 	<br> -->
<!-- 	<br> -->
<!-- 	<table> -->
<!-- 	  <tr> -->
<!-- 	    <th>Job Name</th> -->
<!-- 	    <th>User Name</th> -->
<!-- 	    <th>Status</th> -->
<!-- 	  </tr> -->
<!-- 	</table> -->

	
	<script>

		//Get and populate user's active jobs
		getJobsByUser($('#userId').val(), function(response){
			populateJobs(response, document.getElementById("activeJobs"), 1);
		});
		
		//Get the jobs the user has been offered
		getJobOffersByUser($("#userId").val(), function(response){
			alert(JSON.stringify(response))
			populateJobs(response, document.getElementById("offeredJobs"), 1);			
		});
	</script>


<%@ include file="./includes/Footer.jsp" %>
