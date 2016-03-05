<%@ include file="./includes/Header.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
	</head>
	
	<input type="hidden" id="userId" value="${user.userId}"/>
	<input id='selectedCategory' type='hidden'> 

	<h1 class="section">Post A Job</h1>
		
	<h1>Job name</h1>
	<input type="text" id="jobToAdd">		

	<h1>Select a category to place the job in</h1>		
	
	<div id='0T'>
	</div>
	
	<br>
	<button type="button" id="addJob" onClick="addJob">Add Job</button>
	
<!-- 	This div tag will hold other div tags that represent the categories -->
<!-- 	selected by the user. -->
<!-- 	As the user checks/unchecks the category checkbox, divs will be added/removed. -->
<!-- 	Once the user adds the job, this div will be inspected to determine which categories -->
<!-- 	have been selected -->	
	<div id="selectedCategory" style="display: none">
	</div>
	
	

<script>
			
	//Get the seed categories.
	//Seed categories are sub categories to a category with id=0
	getCategoriesBySuperCat('0', function(response, elementId){
		
	//	alert('callback1 ' + elementId)
		appendFirstLevelCategories_PostJob(elementId, response, function(){
			
			//alert(JSON.stringify(response))
			
			$("li").each(function(){
				//alert('for each li. id = ' + this.id)
				getCategoriesBySuperCat(this.id, function(response, elementId){
					//alert('callback2 ' + elementId)
					appendFirstLevelCategories_PostJob(elementId, response, function(){

					})
				})
			})	
		})
	})	
	
</script>
	
<%@ include file="./includes/Footer.jsp" %>