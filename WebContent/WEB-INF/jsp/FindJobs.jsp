<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employee.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
		<script src="<c:url value="/static/javascript/Application.js" />"></script>
<!-- 		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" /> -->
<!-- 		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" /> -->
		<link rel="stylesheet" type="text/css" href="./static/css/global.css" />

	</head>

	<body>
		<input type="hidden" id="userId" value="${user.userId}"/>
		<div class="container">
			<div style="width: 750px" class="panel panel-success">
			  <div class="panel-heading">
			    Select a category to view jobs
			  </div>
			  <div id='0T' class="color-panel panel-body"></div>
			</div>
			
			<div style="width: 750px" class="panel panel-success">
			  <div class="panel-heading">
			    Available Jobs
			  </div>
			  <div id='jobList' class="color-panel panel-body"></div>
			</div>
		</div>
	
	</body>
	
	<script>
	
	
	//Get the seed categories.
	//Seed categories are sub categories to a category with id=0
	getCategoriesBySuperCat('0', function(response, elementId){
		

		appendFirstLevelCategories_FindJobs(elementId, response, function(){
			
			var arr = $('#' + elementId + 'T').find('li');
			for(var i = 0; i < arr.length; i++){

				getCategoriesBySuperCat(arr[i].id, function(response, elementId){

					appendFirstLevelCategories_FindJobs(elementId, response, function(){
			
					})
				})
			}
		})
	})	
		
	</script>

<%@ include file="./includes/Footer.jsp" %>