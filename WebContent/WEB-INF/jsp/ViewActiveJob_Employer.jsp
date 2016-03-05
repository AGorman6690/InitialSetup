<%@ include file="./includes/Header.jsp" %>
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

	<h1>Select a category to view jobs</h1>
	
	<div id='0T'>
	</div>
	
	<br>
	<h1>Job</h1>
	<div id="jobName">(it would be nice to pass the entire job JSON object to this page)</div>
	
	<br>
	<h1>Applicants</h1>
	<div id="container">
		<div id="applicants">
	</div>
	
	</div>
	
	<script>
		var jobId = parent.document.URL.substring(parent.document.URL.indexOf('?jobId=') + 7, parent.document.URL.length);

		
		
		getApplicants(jobId, function(users){
			
			appendUsers("applicants", users, function(){})
		})
		
		
		
	</script>

<%@ include file="./includes/Footer.jsp" %>