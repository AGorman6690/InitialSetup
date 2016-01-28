<%@ include file="./includes/Header.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Ratings.js" />"></script>
		<style>
			.section{
				color: red;
				font-size: 4em;
			}		
		</style>			
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	</head>
<!-- ../static/css/  C:/Users/Owner/git/InitialSetup/WebContent/static/css/ratings.css-->
	<a href="./findEmployees">Find Employees</a>	
	
	<h1>Here is your profile ${user.firstName} ${user.userId}</h1>
	
<!-- **********************************************	 -->
<!-- 	****** CATEGORIES ****** -->
<!-- **********************************************	 -->
	
	<h1 class="section">Categories</h1>
	
	<h1 >These are the categories you can select from</h1>			
 	<select id="appCats" multiple style="width: 200px;">
 	</select>		 		 	

	
	<button type="button" id="addCatToUser">Add</button>
	<br>
		
	<h1>These are you currently selected categories</h1>		
	<select id="profileCats" multiple style= "width: 200px"  >
	</select>
	
	<ul id="usersCats">
	</ul>

	
	<button type="button" id="deleteCatFromUser">Delete</button>

	<br>
	<br>
	
<!-- **********************************************	 -->
<!-- 	****** JOBS ****** -->
<!-- **********************************************	 -->
	
	<h1 class="section">Jobs</h1>
		
	<h1>Here you can create a job</h1>
	<form>
		<input type="text" id="jobToAdd">
		<button type="button" id="addJob">Add Job</button>
	</form>			
			
	<h1>These are your active jobs</h1>
	<p>Select a job and refresh to display associated categories and applicants</p>
	<select multiple id="activeJobs" style= "width: 200px">
	</select>

	<br>

<!-- **********************************************	 -->
<!-- 	****** SELECTED JOB ****** -->
<!-- **********************************************	 -->
	<h1 class="section">Selected Job</h1>
	
	<h1>Selected job</h1>
	<input type="text" id="selectedJob">
	
	
	<h1>Add a category to the selected job.</h1>
	<p>Select a job, select a current profile category then press "Add category to job"</p>
	<input type="text" id="catToAdd">
	<br>
	<button type="button" id="addCatToJob">Add category to job</button>
	
	<h1>Categories associated with the selected job</h1>	
	<select multiple id="selectedJobCats" style="width: 200px">
	</select>
	
	<h1>Applicants for the selected job</h1>	
	<select multiple id="applicants" style="width: 200px">
	</select>

	<button id ="hireApplicant" type="button">Hire applicant</button>
	<br>
	
	<h1>Employees for the selected job</h1>	
	<select  multiple id="employees" style="width: 200px">
	</select>	

	<h1>Mark selected job as complete</h1>
	<p>Select an active job and press "Mark job complete"</p>
	<button type="button" id="markJobComplete">Mark job complete</button>	
	
	
	<!-- **********************************************	 -->
<!-- 	****** COMPLETED JOB ****** -->
<!-- **********************************************	 -->
	<h1 class="section">Completed Jobs</h1>
	
	<h1>Completed jobs</h1>
	<select multiple id="completedJobs" style= "width: 200px" >
	</select>
	<br>	

<!-- //********************************************** -->
<!-- //It might be cool to dynamically display the "rate the employee" elements -->
<!-- //after the user marks a job complete -->
<!-- //*********************************************** -->
<!-- 	<div id="rateEmployee"></div> -->

	

	
<h1>Rate the employees</h1>
<div class="main">

<!-- 	<ul id="employeesToRate" class="all"> -->
<!-- 	</ul> -->
	<select multiple id="employeesToRate" class="all">
	</select>


	<p data-userId="" id="selectedEmployee"  class="selected">  
	</p>
 	<div class="rating1">
		<p class="rating-content">
		On Time:
		</p>
   
		<button value=1 id="ontime1" type="button" class="button1">
		Never
		</button>
		
		<button value=2 id="ontime2" type="button" class="button2">
		  Rarely
		</button>
		
		<button value=3 id="ontime3" type="button" class="button3">
		  Occasionally
		</button>
		
		<button value=4 id="ontime4" type="button" class="button4">
		  Mostly
		</button>
		
		<button value=5 id="ontime5" type="button" class="button5">
		  Always
		</button> 
	</div> 
	
	 <div class="rating2">
		<p class="rating-content">
		Work Ethic:
		</p>
   
		<button value=1 id="workEthic1" type="button" class="button1">
		Poor
		</button>
		
		<button value=2 id="workEthic2" type="button" class="button2">
		  Acceptable at times
		</button>
		
		<button value=3 id="workEthic3" type="button" class="button3">
		  Acceptable
		</button>
		
		<button value=4 id="workEthic4" type="button" class="button4">
		  Outstanding at times
		</button>
		
		<button value=5 id="workEthic5" type="button" class="button5">
		  Outstanding
		</button> 
	</div> 
	
	 <div class="rating3">
		<p class="rating-content">
		Hire Again?:
		</p>
   
		<button value=1 id="hireAgain1" type="button" class="button1">
		No
		</button>
		
		<button value=2 id="hireAgain2" type="button" class="button2">
		  Maybe
		</button>
		
		<button value=3 id="hireAgain3" type="button" class="button3">
		  Yes
		</button>
	</div> 
	
	
	 <div class="rating4">
		<button id="nextEmployee" type="button" class="button1">
		Next Employee
		</button>
	</div> 
</div>
	
	<script >	
	
		//Get all the application's categories
		getAppCategories(function(response){
			populateCategories(response, document.getElementById("appCats"));
		});
		
		//Get the rate criteria for the application
		getAppRateCriteria(function(response){
			populateRateCriterion(response, document.getElementById("rateCriteria"));
		});
		
		//Get user's categories
		getCategoriesByUser("${user.userId}", function(response){
			populateCategories(response, document.getElementById("profileCats"));
		});
		
		//Get and populate user's active and completed jobs
 		getJobsByUser("${user.userId}", function(response){
 			populateJobs(response, document.getElementById("activeJobs"), 1);
 			populateJobs(response, document.getElementById("completedJobs"), 0);
 		});
		

// 		document.getElementById("submitRating").addEventListener("click", rateEmployee);
// 		document.getElementById("ontime5").addEventListener("click", submitOnTimeRating);
				
		
		//I have these functions here because the user's Id needs to be in scope.
		//When these functions are located in an external file, then the user object, and thus the user's id, is out of scope.
		//If the function is located in an external file, maybe there is a way to bind the user Id, while it is still in scote, to the function?
		//For now, they are here because it works.
		//*********************************************************************************************************
		$("#addJob").click(function(){
			var jobName = $("#jobToAdd").val();
			//alert(jobName);
			addJob(jobName, "${user.userId}", function(response){
				populateJobs(response, document.getElementById("activeJobs"), 1);
				$("#jobToAdd").val("");
			});
		});
		

		$("#addCatToUser").click(function(){
			
			//Get the category id to add
			var e = document.getElementById("appCats");
			var categoryId = e.value;
			
			addCategoryToUser(categoryId, "${user.userId}", function(response){
				populateCategories(response, document.getElementById("profileCats"));
			});
		});
		
		$("#deleteCatFromUser").click(function(){
			
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