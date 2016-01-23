<%@ include file="./includes/Header.jsp" %>

	<script src="<c:url value="/static/javascript/Profile.js" />"></script>

	<a href="./findEmployees">Find Employees</a>	
	
	<h1>Here is your profile ${user.firstName}</h1>
	
<!-- **********************************************	 -->
<!-- 	****** CATEGORIES ****** -->
<!-- **********************************************	 -->
	
	<h1 class="section">Categories</h1>
	
	<h1 >These are the categories you can select from</h1>		
	 <form:form modelAttribute="app">	 	
	 	<form:select id="allCats" multiple="true" path="categories" style="width: 200px;">
	 		<form:options items="${app.categories}" itemValue="id" itemLabel="name"/>
	 	</form:select>		 		 	
	</form:form>
	
	<button type="button" id="profileAddCat">Add</button>
	<br>
		
	<h1>These are you currently selected categories</h1>		
	<form:form modelAttribute="user">
		<form:select id="profileCats" path="categories" multiple="true" style= "width: 200px"  >
			<form:options items="${user.categories}" itemValue="id" itemLabel="name"/>
		</form:select>
	</form:form>
	
	<button type="button" id="profileDeleteCat">Delete</button>

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
	<form:form modelAttribute="user">
		<form:select  path="jobs" multiple="true" id="activeJobs" style= "width: 200px"  >
			<form:options items="${user.activeJobs}" itemValue="id" itemLabel="jobName"/>
		</form:select>
	</form:form>
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
	<form:form modelAttribute="app">
		<form:select path="categories" multiple="true" id="selectedJobCats" style="width: 200px">
			<form:options items="${app.selectedJob.categories}" itemValue="id" itemLabel="name"/>
		</form:select>
	</form:form>	
	
	<h1>Applicants for the selected job</h1>	
	<form:form modelAttribute="app">
		<form:select path="selectedJob.applicants" multiple="true" id="applicants" style="width: 200px">
			<form:options items="${app.selectedJob.applicants}" itemValue="userId" itemLabel="firstName"/>
		</form:select>
	</form:form>
	<button id ="hireApplicant" type="button">Hire applicant</button>
	<br>
	
	<h1>Employees for the selected job</h1>	
	<form:form modelAttribute="app">
		<form:select path="selectedJob.employees" multiple="true" id="employees" style="width: 200px">
			<form:options items="${app.selectedJob.employees}" itemValue="userId" itemLabel="firstName"/>
		</form:select>
	</form:form>		

	<h1>Mark selected job as complete</h1>
	<p>Select an active job and press "Mark job complete"</p>
	<button type="button" id="markJobComplete">Mark job complete</button>	
	
	<div id="rateEmployee"></div>
	
	<script >				
		document.getElementById("profileDeleteCat").addEventListener("click", deleteCategory);
  		document.getElementById("profileAddCat").addEventListener("click", addCategory);
  		document.getElementById("profileCats").addEventListener("click", showCategory);
		document.getElementById("addJob").addEventListener("click", addJob);
 		document.getElementById("addCatToJob").addEventListener("click", addCatToJob);
		document.getElementById("activeJobs").addEventListener("change", getSelectedJob);
		document.getElementById("markJobComplete").addEventListener("click", markJobComplete);
		document.getElementById("hireApplicant").addEventListener("click", hireApplicant);		
	</script>
		
<%@ include file="./includes/Footer.jsp" %>