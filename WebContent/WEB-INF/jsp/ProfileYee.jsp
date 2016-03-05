<%@ include file="./includes/Header.jsp" %>

	<head>
		<style>
			.section{
				color: red;
				font-size: 4em;
			}		
		</style>	
		
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
	</head>
	
	<input type="hidden" id="userId" value="${user.userId}"/>

	<br>
	<a href="./findJobs">Find Jobs</a>
	<br>
	<a href="./viewApplicationsE">View Applications</a>
	<br>
	<a href="./editProfileCategories">Edit Profile Categories</a>

	<h1>Here is your profile ${user.firstName}</h1>
	
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
		
		
		//I have these functions here because the user's Id needs to be in scope.
		//When these functions are located in an external file, then the user object, and thus the user's id, is out of scope.
		//If the function is located in an external file, maybe there is a way to bind the user Id, while it is still in scote, to the function?
		//For now, they are here because it works.
		//*********************************************************************************************************
		$("#profileAddCat").click(function(){
			
			//Get the category id to add
			var e = document.getElementById("appCats");
			var categoryId = e.value;
			
			addCategoryToUser(categoryId, "${user.userId}", function(response){
				populateCategories(response, document.getElementById("profileCats"));
			});
		});
		
		$("#profileDeleteCat").click(function(){
			
			//Get the category id to delete
			var e = document.getElementById("profileCats");
			var categoryId = e.value;
			
			deleteCategoryFromUser(categoryId, "${user.userId}", function(response){
				populateCategories(response, document.getElementById("profileCats"));
			});
		});
		//*********************************************************************************************************
		
	</script>
	
	
	
<%@ include file="./includes/Footer.jsp" %>