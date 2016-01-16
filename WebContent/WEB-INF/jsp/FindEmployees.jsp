<%@ include file="./includes/Header.jsp" %>
	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>		
		<script>
			function getEmployees(){
					
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
						//alert("success");				
						populateUsers(response.selectedCategory.users, document.getElementById("employees"));				
		 			}
		 			
		 			function _error(response){
		 				alert("error getEmployees");
		 			}			
				}	
		</script>	
	</head>

	<h1>Select a category to find employees</h1>
	<form:form modelAttribute="user">
		<form:select path = "categories" id="categories" multiple="true" style="width: 200px">		
			<form:options items="${user.categories}" itemValue = "id" itemLabel = "name"/>
		</form:select>
	</form:form>
	<br>
	
	<h1>Here are all the employees in this category</h1>
	<form:form modelAttribute="app">
		<form:select path = "usersBySelectedCat" id="employees" multiple="true" style="width: 200px">		
			<form:options items="${app.usersBySelectedCat}" itemValue = "userId" itemLabel = "firstName"/>
		</form:select>
	</form:form>	
	
	
	<h1>(Include functionality to send request to employee)</h1>
	
	<script>
		document.getElementById("categories").addEventListener("click", getEmployees);
	</script>

<%@ include file="./includes/Footer.jsp" %>