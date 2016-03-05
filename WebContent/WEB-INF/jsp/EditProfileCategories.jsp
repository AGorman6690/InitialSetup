<%@ include file="./includes/Header.jsp" %>

	<head>
	<script src="<c:url value="/static/javascript/Profile.js" />"></script>
	<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/User.js" />"></script>
	<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
	<script src="<c:url value="/static/javascript/Lists.js" />"></script>
	<script src="<c:url value="/static/javascript/Application.js" />"></script>
	<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
		
		<style>
			.section{
				color: red;
				font-size: 4em;
			}		
		</style>			
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	</head>
	
	<input type="hidden" id="userId" value="${user.userId}"/>
	<div id="addCategories" style="display: none"></div>
	<div id="removeCategories" style="display: none"></div>
	
	<h1>Current Profile Categories</h1>
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
// 							alert("A")
						})
						
// 						if(i = arr.length){
// 							alert("C")
// 						}
					})
					
// 					alert("B1")
				}
				
// 				alert("D")
				
			})
			
// 			alert("E")
		})	
		
	})
		
	
	</script>


<%@ include file="./includes/Footer.jsp" %>
