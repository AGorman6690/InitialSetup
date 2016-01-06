<%@ include file="./includes/Header.jsp" %>

	<h1>Here is your profile ${user.firstName}</h1>
	
	<h1>${user.profile.name2}</h1>
	
	<h1>Current Profile Categories</h1>
		
	<form:form modelAttribute="user">
		<form:select path="categories" multiple="true" id="profileCats" style= "width: 200px"  >
			<form:options items="${user.categories}" itemValue="id" itemLabel="name"/>
		</form:select>
	</form:form>
	
	<button type="button" id="profileDeleteCat">Delete</button>
	
	
	<br>
 	<h1>All Categories</h1>
		
	 	<form:form modelAttribute="app">	 	
	 	<form:select id="allCats" multiple="true" path="categories" style="width: 200px;">
	 		<form:options items="${app.categories}" itemValue="id" itemLabel="name"/>
	 	</form:select>		 		 	
	</form:form>
	
	<button type="button" id="profileAddCat">Add</button>
	
	<script>
	
		document.getElementById("profileDeleteCat").addEventListener("click", deleteCat);
		document.getElementById("profileAddCat").addEventListener("click", addCat);
		
		function deleteCat(){
 			var e = document.getElementById("profileCats");
 			var index = e.selectedIndex;
 			var text = e.options[index].text;
 			//alert(e.selectedIndex);
 			if(index > -1){
 				//alert(e.selectedIndex);
 				
 				var selectNext;
 				if(index == e.options.length - 1){
 					selectNext = index - 1;
 				}else{
 					selectNext = index;
 				}
 				e.remove(index);	 				
 				e.selectedIndex = selectNext; 	
 			}
 			
 			var dbItem = {};
 			dbItem.userId = "${user.userId}";
 			dbItem.categoryName = text;
 			
 			$.ajax({	 	
 				type: "POST",
 		        url: 'http://localhost:8080/JobSearch/deleteCat',
 		        data: JSON.stringify(dbItem),
 		        contentType: "application/json", // Request
 		        dataType: "json", // Response
 		    });		
		}
	
		function addCat(){
 			var e = document.getElementById("allCats");
 			
 			//Set the db item to add
 			var dbItem = {};
 			dbItem.userId = "${user.userId}";
 			dbItem.categoryName = e.options[e.selectedIndex].text;
 			
 			//Add the usercategory item to the db
 			$.ajax({	 	
 				type: "POST",
 		        url: 'http://localhost:8080/JobSearch/addCat',
 		        data: JSON.stringify(dbItem),
 		        contentType: "application/json", // Request
 		        dataType: "json", // Response
 		    });
	
		}
	</script>
	
	
	
<%@ include file="./includes/Footer.jsp" %>