<%@ include file="./includes/Header.jsp" %>

	<head>	
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>		
	</head>
	
	<div>Welcome! ${user.firstName} ${user.lastName}</div>
	<div>Set up your account</div>
 	
 	<c:choose>
 		<c:when test="${user.profileId ==1}">
 			Select the categories where you want to post jobs
 		</c:when>
 		<c:when test="${user.profileId ==2}">
 			Select the categories where you want to find jobs
 		</c:when>
 	</c:choose>
 	
 	<br>
		<h1>Available Categories</h1>
		
	 	<form:form modelAttribute="app">	 	
	 	<form:select id="allCategories" multiple="true" path="categories" style="width: 200px;">
	 		<form:options items="${app.categories}" itemValue="id" itemLabel="name"/>
	 	</form:select>		 		 	
	</form:form>

	<br>
	<h1>Selected Categories</h1>
	
	<select multiple id="selectedCats" style="width: 200px;"></select>
	<br>
	<button type="button" id="delete">Delete</button>
	<br>
	<button type="button" id="complete">Finish</button>

		<script>
 		document.getElementById("allCategories").addEventListener("click", addCategory); 
 		document.getElementById("delete").addEventListener("click", deleteCategory); 
 		document.getElementById("complete").addEventListener("click", complete);	
	</script>

<%@ include file="./includes/Footer.jsp" %>