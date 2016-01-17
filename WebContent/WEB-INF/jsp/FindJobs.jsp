<%@ include file="./includes/Header.jsp" %>

<!-- 	<a href="./ProfileYee">Back to profile</a> -->

<head>
	<script src="<c:url value="/static/javascript/Profile.js" />"></script>
	<script>

		function showJob(){			
				var eTo = document.getElementById("jobToApplyFor");
				eTo.value = this.options[this.selectedIndex].text;
				eTo.name = this.options[this.selectedIndex].value;
			}
			
			function applyForJob(){
				
				//The job id is stored in the input's name attribute (see showJob())
				var jobId = document.getElementById("jobToApplyFor").name;
	
				$.ajax({
					type: "GET",
					url: 'http://localhost:8080/JobSearch/applyForJob?jobId=' + jobId,
					dataType: "json",
				})
			}
			
			function getSelectedUser(){
				//alert("get jobs");
				var e = document.getElementById("employers");
				var userId = e.options[e.selectedIndex].value;
	 			$.ajax({	 	
	 				type: "GET",
	 		        url: 'http://localhost:8080/JobSearch/getSelectedUser?userId=' + userId,
	 		        contentType: "application/json", // Request
	 		        dataType: "json", // Response
			        success: _success,
	 		        error: _error
	 		    });
				
	 			//Populate the selected employer's jobs
	 			function _success(response){
					populateJobs(response.selectedUser.jobs, document.getElementById("jobs"));
	 			}
	 			
	 			function _error(response){
	 				alert("error");
	 			}
			}
			
			function getSelectedCategory(){
				//alert("get jobs");
				var e = document.getElementById("categories");
				var categoryId = e.options[e.selectedIndex].value;

	 			$.ajax({	 	
	 				type: "GET",
	 		        url: 'http://localhost:8080/JobSearch/getSelectedCategory?categoryId=' + categoryId,
	 		        contentType: "application/json", // Request
	 		        dataType: "json", // Response
	 		        success: _success,
	 		        error: _error
	 		    });
	
	 			function _success(response){
	 				
	 				//Clear jobs by selected users
	 				$("#jobs").empty();

	 				populateUsers(response.selectedCategory.users, document.getElementById("employers")); 	
					populateJobs(response.selectedCategory.jobs, document.getElementById("jobsBySelectedCat"));						
	 			}
	 			
	 			function _error(response){
	 				alert("error");
	 			}				
			}
		</script>
	</head>
	
	<h1>Find Jobs ${user.firstName} ${user.userId}</h1>
	<h1>Select a category to find jobs</h1>
	<form:form modelAttribute="user">
		<form:select path = "categories" id="categories" multiple="true" style="width: 200px">		
			<form:options items="${user.categories}" itemValue = "id" itemLabel = "name"/>
		</form:select>
	</form:form>
	<br>
	
	<h1>Here are all the active jobs for the selected CATEGORY</h1>
	<form:form modelAttribute="app">
		<form:select path = "selectedCategory.jobs" id="jobsBySelectedCat" multiple="true" style="width: 200px">		
			<form:options items="${app.selectedCategory.jobs}" itemValue = "id" itemLabel = "jobName"/>
		</form:select>
	</form:form>	
	
	<h1>Here are all the employers for the selected category</h1>
	<form:form modelAttribute="app">
		<form:select path = "users" id="employers" multiple="true" style="width: 200px">		
			<form:options items="${app.selectedCategory.users}" itemValue = "userId" itemLabel = "firstName"/>
		</form:select>
	</form:form>	
	
	<h1>Here are all the active jobs for the selected EMPLOYER</h1>
	<form:form modelAttribute="app">
		<form:select path = "selectedUser.jobs" id="jobs" multiple="true" style="width: 200px">		
			<form:options items="${app.selectedUser.jobs}" itemValue = "id" itemLabel = "jobName"/>
		</form:select>
	</form:form>

	<h1>Here you can apply for a job</h1>
	<p>Select a job</p>
	<input type="text" id="jobToApplyFor">
	<button type="button" id="apply">Apply</button>
	
	<script>
		document.getElementById("categories").addEventListener("click", getSelectedCategory);
		document.getElementById("employers").addEventListener("click", getSelectedUser);
		document.getElementById("apply").addEventListener("click", applyForJob);
		document.getElementById("jobs").addEventListener("click", showJob);
		document.getElementById("jobsBySelectedCat").addEventListener("click", showJob);			
	</script>

<%@ include file="./includes/Footer.jsp" %>