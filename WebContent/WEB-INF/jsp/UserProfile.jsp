
<%@ include file="./includes/Header.jsp" %>

	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
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
	var pageContext = "profile";

	getCategoriesByUser($("#userId").val(), function(usersCategories){

		getCategoriesBySuperCat('0', function(response, elementId){

			//Append seed categories
			appendCategories(elementId, response);
		})
	})


	</script>


<%@ include file="./includes/Footer.jsp" %>
