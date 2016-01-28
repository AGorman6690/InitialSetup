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
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
	</head>

	<a href="./findJobs">Find Jobs</a>

	<h1>Here is your profile ${user.firstName}</h1>
	
<!-- **********************************************	 -->
<!-- 	****** CATEGORIES ****** -->
<!-- **********************************************	 -->
	
	<h1 class="section">Categories</h1>
	
	<h1>These are the categories you can select from</h1>
			 	
 	<select id="appCats" multiple style="width: 200px;">
 	</select>		 		 	
	
	<button type="button" id="profileAddCat">Add</button>
	<br>
	
	
	<h1>These are you currently selected categories</h1>		
		<select multiple id="profileCats" style= "width: 200px"  >
		</select>
	
	<button type="button" id="profileDeleteCat">Delete</button>

	<br>
	<br>
<!-- **********************************************	 -->
<!-- 	****** JOBS ****** -->
<!-- **********************************************	 -->
	<h1 class="section">Jobs</h1>
	
	<h1>Jobs you applied to</h1>
		<select multiple id="jobsAppliedFor" style= "width: 200px"  >
		</select>

	<h1>Jobs you were hired for</h1>
		<select multiple id="jobsHiredFor" style= "width: 200px"  >
		</select>

	<script>
	
		//Get all the application's categories
		getAppCategories(function(response){
			//alert(JSON.stringify(response));
			populateCategories(response, document.getElementById("appCats"));
		});
		
		//Get the user's categories
		getCategoriesByUser("${user.userId}", function(response){
			//alert("callback getCategoriesByUser");			
			populateCategories(response, document.getElementById("profileCats"));
		});
	
		//Get all active jobs that the user has applied for
		getApplicationsByUser("${user.userId}", function(response){
			//alert("callback getApplicationsByUser");			
			populateJobs(response, document.getElementById("jobsAppliedFor"), 1);
		});
		
		
		//Get the user's employement. Show both active and inactive jobs
		getEmploymentByUser("${user.userId}", function(response){
			//alert("callback getEmploymentByUser");			
			populateJobs(response, document.getElementById("jobsHiredFor"), -1);
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