<%@ include file="./includes/Header.jsp" %>
<html>
<head>

	<script src="<c:url value="/static/javascript/Display.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	
	<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />

</head>

<div id="0-0" class='show'></div>


<script>

	//alert(2);
	
	//Use the seed categories (categories with no super category) to set the initial first level.
	//The div with id='0-0' will be level 0 and contain all seed categories including thier respective sub categories.
	//The seed categores will have level 1.
	//The seed categories' initial sub items will have level 2, and so on.
	//The level value is used to set the div's style.
	setFirstLevel('0-0', function(elementId){

		//Set the seeds' second level
		setSecondLevel('0-0', function(elementId){
			
			//Show the seed categories (i.e. the direct children of the initial div 0-0))
			showChildren(elementId);
		});
	});

</script>

</html>