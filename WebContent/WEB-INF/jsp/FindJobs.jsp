<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employee.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<script src="<c:url value="/static/javascript/Application.js" />"></script>
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />

	</head>
<!-- 	<span>Search for Jobs</span> -->
<!-- 	<ul> -->
<%-- 		<c:forEach items="${categories}" var="category"> --%>
<%-- 			<li class="co_jobCategory">${ category.getName()} --%>
<!-- 				<ul> -->
<%-- 					<c:forEach items="${category.getCategories()}" var="subcategory"> --%>
<%-- 						<li class="co_jobCategory">${ subcategory.getName()}</li> --%>
<%-- 					</c:forEach> --%>
<!-- 				</ul> -->
<!-- 			</li> -->
<%-- 		</c:forEach> --%>
<!-- 	</ul> -->

	<input type="hidden" id="userId" value="${user.userId}"/>

	<h1>Select a category to view jobs</h1>
	
	<div id='0T'>
	</div>
	
	<br>
	<h1>Available Jobs</h1>
	<div id="jobs">
		<ul class="list-group" id="jobList">
		</ul>	
	</div>
	
	<script>
	
	
	//Get the seed categories.
	//Seed categories are sub categories to a category with id=0
	getCategoriesBySuperCat('0', function(response, elementId){
		

		appendFirstLevelCategories(elementId, response, function(){
			
			var arr = $('#' + elementId + 'T').find('li');
			for(var i = 0; i < arr.length; i++){

				getCategoriesBySuperCat(arr[i].id, function(response, elementId){

					appendFirstLevelCategories(elementId, response, function(){
			
					})
				})
			}
		})
	})	
		
	</script>

<%@ include file="./includes/Footer.jsp" %>