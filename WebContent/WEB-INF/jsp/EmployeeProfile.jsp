<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employee.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
	</head>
	
	<input type="hidden" id="userId" value="${user.userId}"/>

	<h1>Jobs you applied to</h1>
	<div id="container">
		<div id="appliedTo"></div>
	</div>
	
	<h1>Jobs you were hired for</h1>
	<div id="container">
		<div id="hiredFor"></div>
	</div>
	<script>
	
	
		//Get all active jobs that the user has applied for
		getApplicationsByUser($("#userId").val(), function(applications){		
			appendJobs("appliedTo", applications, function(){})
		});
		
		
		//Get the user's employement. Show both active and inactive jobs
		getEmploymentByUser("${user.userId}", function(employment){
			//alert("callback getEmploymentByUser");			
			appendJobs("hiredFor", employment, function(){})
		});
		
	</script>
	
	
	
<%@ include file="./includes/Footer.jsp" %>