<%@ include file="./includes/Header.jsp" %>

		<div>Welcome! ${user.firstName} ${user.lastName}</div>
		<div>Set up your accout</div>
	 	
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
	 		document.getElementById("allCategories").addEventListener("click", myFunction); 
	 		document.getElementById("delete").addEventListener("click", deleteCat); 
	 		document.getElementById("complete").addEventListener("click", complete);
	 		
	 		function complete(){
	 			var e = document.getElementById("selectedCats");
	 			var cats = new Array();
	 			
	 			for(i = 0; i < e.options.length; i++){

	 				cats[i] = e.options[i].text;
	 			}
	 			
	 			$.ajax({	 	
	 				type: "POST",
	 		        url: 'http://localhost:8080/JobSearch/catsDone?cats=' + cats,
	 		        contentType: "application/json", // Request
	 		        dataType: "json", // Response
	 		    });		
	 		}

	 		function myFunction(){ 	 	  			
	 			var from = document.getElementById("allCategories");
	 			var to = document.getElementById("selectedCats");
	 			
	 			var text = from.options[from.selectedIndex].text;
	 			var doAdd;
	 			doAdd="";
	 			
	 			for (i=0; i < to.options.length; i++){		 				
	 				if (to.options[i].text == text){
	 					doAdd = "no";
	 				}	
	 			}
	 			if (doAdd != "no"){
	 				var option = document.createElement("option");
	 				option.text = text;
	 				to.add(option);
	 			}	 	 			
	 		} 
	 		
	 		function deleteCat(){
	 			
	 			var e = document.getElementById("selectedCats");
	 			var index = e.selectedIndex;
	 			if(index > -1){	 				
	 				var selectNext;
	 				if(index == e.options.length - 1){
	 					selectNext = index - 1;
	 				}else{
	 					selectNext = index;
	 				}
	 				e.remove(index);	 				
	 				e.selectedIndex = selectNext; 	
	 			}
	 		}
	
		</script>

<%@ include file="./includes/Footer.jsp" %>