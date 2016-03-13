
<%@ include file="./includes/Header.jsp" %>

	<c:choose>
		<c:when test="${user.profileId == 1}">
			<%@ include file="./includes/Header_Employer.jsp" %>
		</c:when>
		<c:otherwise>
	         <%@ include file="./includes/Header_Employee.jsp" %>
	    </c:otherwise>
	</c:choose>

	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<script src="<c:url value="/static/javascript/Application.js" />"></script>
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />		
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	</head>
	

	
	<input type="hidden" id="userId" value="${user.userId}"/>
	<div id="addCategories" style="display: none"></div>
	<div id="removeCategories" style="display: none"></div>
	
	<h1>Edit Profile</h1>
	<button type="button" id="saveEditProfileCats">Save</button>
	
	<br>
	<div id="0T"></div>
	
	
	<script>
	
	//Get the seed categories.
	//Seed categories are sub categories to a category with id=0

	
	getCategoriesByUser($("#userId").val(), function(usersCategories){

		getCategoriesBySuperCat('0', function(response, elementId){
			
			//Append seed categories
			appendFirstLevelCategories_ProfileCats(elementId, response, function(elementId, response){
				
				
				//appendSubCategories()
				//alert(elementId)
				//For each seed (the "T" is simply the div element in which the sub categories
				//will be appended to),
				var arr = $('#' + elementId + 'T').find('li');
				for(var i = 0; i < arr.length; i++){
					
					//get sub categories.
					getCategoriesBySuperCat(arr[i].id, function(response, elementId){

						//Append sub categories
						appendFirstLevelCategories_ProfileCats(elementId, response, function(){		
							setCategoriesCheckbox(usersCategories)
						})
					})				
				}
			})			
		})			
	})
		
	
	</script>


<%@ include file="./includes/Footer.jsp" %>
