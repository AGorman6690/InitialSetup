<%@ include file="./includes/Header.jsp" %>

	<head>
		<style>
			.section{
				color: red;
				font-size: 4em;
			}		
		</style>	
	</head>

	<a href="./findJobs">Find Jobs</a>

	<h1>Here is your profile ${user.firstName}</h1>
	
<!-- **********************************************	 -->
<!-- 	****** CATEGORIES ****** -->
<!-- **********************************************	 -->
	
	<h1 class="section">Categories</h1>
	
	<h1>These are the categories you can select from</h1>
		
	<form:form modelAttribute="app">	 	
	 	<form:select id="allCats" multiple="true" path="categories" style="width: 200px;">
	 		<form:options items="${app.categories}" itemValue="id" itemLabel="name"/>
	 	</form:select>		 		 	
	</form:form>
	
	<button type="button" id="profileAddCat">Add</button>
	<br>
	
	
	<h1>These are you currently selected categories</h1>		
	<form:form modelAttribute="user">
		<form:select path="categories" multiple="true" id="profileCats" style= "width: 200px"  >
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
	
	<h1>Jobs you applied to</h1>
	<form:form modelAttribute="user">
		<form:select path="appliedToJobs" multiple="true" id="profileCats" style= "width: 200px"  >
			<form:options items="${user.appliedToJobs}" itemValue="id" itemLabel="jobName"/>
		</form:select>
	</form:form>

	<h1>Jobs you were hired for</h1>
	<form:form modelAttribute="user">
		<form:select path="employment" multiple="true" id="profileCats" style= "width: 200px"  >
			<form:options items="${user.employment}" itemValue="id" itemLabel="jobName"/>
		</form:select>
	</form:form>

	<script>

		document.getElementById("profileDeleteCat").addEventListener("click", deleteCat);
  		document.getElementById("profileAddCat").addEventListener("click", addCat);
		
  		function deleteCat(){
 			var e = document.getElementById("profileCats");
			var catId = e.value;
 			
 			$.ajax({	 	
 				type: "GET",
 		        url: 'http://localhost:8080/JobSearch/deleteCategory?categoryId=' + catId,
 		        contentType: "application/json", // Request
 		        dataType: "json", // Response
 		        success: _success,
		        error: _error
		    });
		
			
			//Executes if the ajax call is successful
			function _success(response){
				//alert("success");
				showCategories(response);
				
			}
			
			//Executs if the ajax call errors out
			function _error(response, errorThrown){
				alert("error");
			}	
		}
	
		function addCat(){
			//alert("3");
 			var e = document.getElementById("allCats");
 			var catId = e.options[e.selectedIndex].value;
 			
 			//Add the usercategory item to the db
 			$.ajax({	 	
 				type: "GET",
 		        url: 'http://localhost:8080/JobSearch/addCategory?categoryId=' + catId,
 		        contentType: "application/json", // Request
 		        dataType: "json", // Response
 		        success: _success,
		        error: _error
		    });
		
			function _success(response){
				//alert("success");
				showCategories(response);
			}
			
			function _error(response, errorThrown){
				alert("error");
			}	
	
		}
		
		
		function showCategories(obj){
			//alert(obj);
			//alert("show cats");
			var eCats = document.getElementById("profileCats");
			$("#profileCats").empty();				
			for(i = 0; i < obj.categories.length ; i++){
				var opt = document.createElement("option");					
				opt.value = obj.categories[i].id;
				opt.innerHTML = obj.categories[i].name;
				eCats.appendChild(opt);
			}
		}
	</script>
	
	
	
<%@ include file="./includes/Footer.jsp" %>