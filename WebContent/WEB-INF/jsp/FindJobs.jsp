<%@ include file="./includes/Header.jsp" %>
	<script src="<c:url value="/static/javascript/Profile.js" />"></script>
	<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/User.js" />"></script>
	<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
	<script src="<c:url value="/static/javascript/Display.js" />"></script>
	
	<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
		
	<span>Search for Jobs</span>
	<ul>
		<c:forEach items="${categories}" var="category">
			<li class="co_jobCategory">${ category.getName()}
				<ul>
					<c:forEach items="${category.getCategories()}" var="subcategory">
						<li class="co_jobCategory">${ subcategory.getName()}</li>
					</c:forEach>
				</ul>
			</li>
		</c:forEach>
	</ul>
	
	
	<div id="0-0--"></div>	
	<div id="abc"></div>	
	
	<script>
	
	//Get the original seed categories.
	//The seed container div (id = "0-0--") will contain all seed categories and all levels of sub categories.
	getCategoriesBySuperCat("0-0--", function(response, elementId){

		//Create the children category divs for the seed container div.
		//(i.e. create divs for the seed categories) 
		createChildDivs_2(response, elementId, function(elementId){
			
			setChildJobCount(elementId, function(){});
			
			//Create the children category divs for the newly created category divs
			//(i.e. create divs for the seed categorys' sub categories)
			$('#' + elementId + ' > div').each(function(){

				//Get the sub categories
				getCategoriesBySuperCat(this.id, function(response, elementId){

					//Create children divs
					createChildDivs_2(response, elementId, function(elementId){

						//Add click event for the down arrow.
						addDownArrowClickEvent(elementId, function(){

						});
						
						setChildJobCount(elementId, function(){});

					})
				})
				
				showChildren(elementId);
			})
		})
	})
	
	</script>

<%@ include file="./includes/Footer.jsp" %>