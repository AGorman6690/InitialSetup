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
		
		<script type="text/javascript">
		
			function getSelectedCompletedJob(){
			//	alert("getSelectedCompletedJob");
				getEmployeesByJob(this.value, "employeesCompletedJob");
				getEmployeesByJob2(this.value, function(response){
					//alert("callback getEMployeesByJob2");
				//	alert(JSON.stringify(response));
					//alert(response.users.length);
					$('#employeesToRate').empty();
					var i;
					for(i=0; i<response.users.length; i++){
						$('#employeesToRate').append('<li>' + response.users[i].firstName + '</li>')
					}
					
					$('#employeesToRate').on('click', 'li', function(){
					//	alert(this.text);
						$('#selectedEmployee').html(this.innerText);
					})
				})
			
			}
			
		</script>
		
<!-- 		<link rel="stylesheet" type="text/css" href="/css/main.css"> -->

		<style>
		
.main{
      position: relative;
      top: 50px;
      left: 30px;
      border-style: solid;
      width: 500px;
      height: 300px;
}

.all{
  list-style: none;
  position: absolute;
  top: -18px;
  float: left;
  height: 290px;
  width: 100px;
  border: 1px solid black;
  padding-left: 10px;
  padding-right: 0px;
  padding-top: 10px
}

.list{
    padding-top: 20px;
}

.selected{
  position: absolute;
  top: -18px;
  left: 110px;
  border: 1px solid black;
  width: 390px;
  height: 25px;
}

.rating1{
  position: absolute;
  top: 40px;
  left: 110px;
  border: 1px solid black;
  width: 390px;
  height: 25px;
}

.rating-content{
  position: absolute;
  top: -10px;
  left: 0px;
  width: 190px;
  height: 25px
}

.button1{
  position: absolute;
  left: 70px;
  width: 50px;
}

.button2{
  position: absolute;
  left: 130px;
   width: 50px;
}

.button3{
  position: absolute;
  left: 190px;
   width: 50px;
}

.button4{
  position: absolute;
  left: 250px;
   width: 50px;
}


.button5{
  position: absolute;
  left: 310px;
   width: 50px;
}

.ratings{
  list-style: none;
  position: absolute;
  top: 8px;
  width: 180px;
  height: 265px;
  left: 110px;
  border: 1px solid black;
  padding-left: 10px;
  padding-right: 0px;
  padding-top: 10px
}
</style>


	</head>

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

	
	<h1>Selected job name</h1>
	<input type="text" id="selectedCompletedJob">
	
	<h1>Employees</h1>
	<select multiple id="employeesCompletedJob" style="width: 200px">
	</select>	
	
	<h1>Rate the employees</h1>
	<select  multiple id="rateCriteria" style="width: 200px">	
	</select>	
	
	<h1>Rate value</h1>
	<input type="text" id="rateValue">	
	<br>
	<button type="button" id="submitRating">Submit Rating</button>
	
	
<div class="main">
  

<ul id="employeesToRate" class="all">
  </ul>


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
		

// 		document.getElementById("completedJobs").addEventListener("click", getSelectedCompletedJob);
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