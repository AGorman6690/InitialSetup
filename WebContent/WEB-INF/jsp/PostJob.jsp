<%@ include file="./includes/Header.jsp" %>

	<head>

		
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
	
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
	</head>


	<h1 class="section">Post A Job</h1>
		
	<h1>Job name</h1>
	<input type="text" id="jobToAdd">		

	<h1>Select a category to place the job in</h1>		
	<div id="0-0--" class='show'></div>		
	
	<input id='selectedCategory' type='hidden'> 
	<br>
	<button type="button" id="addJob">Add Job</button>


	
<script>
		
	
	//Use the seed categories (categories with no super category) to set the initial first level.
	//The div with id='0-0' will be level 0 and contain all seed categories including thier respective sub categories.
	//The seed categores will have level 1.
	//The seed categories' initial sub items will have level 2, and so on.
	//The level value is used to set the div's style.
	
	setFirstLevel('0-0--', function(elementId){

		//Set the seeds' second level
		setSecondLevel(elementId, function(elementId){
			
			//Show the seed categories (i.e. the direct children of the initial div 0-0))
			showChildren(elementId);
		});
	});

	
	
	//I have these functions here because the user's Id needs to be in scope.
	//When these functions are located in an external file, then the user object, and thus the user's id, is out of scope.
	//If the function is located in an external file, maybe there is a way to bind the user Id, while it is still in scote, to the function?
	//For now, they are here because it works.
	//*********************************************************************************************************
	$("#addJob").click(function(){
		var jobName = $("#jobToAdd").val();
		var categoryId = $('#selectedCategory').val();
		//alert(jobName);
		addJob(jobName,  "${user.userId}", categoryId, function(response){
			populateJobs(response, document.getElementById("activeJobs"), 1);
			$("#jobToAdd").val("");
		});
	});
	
	
</script>
	
<%@ include file="./includes/Footer.jsp" %>