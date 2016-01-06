<%@ include file="./includes/Header.jsp" %>

	//<script src="<c:url value="/static/javascript/User.js" />"></script>


	<form>
		Category Name:<br>
		<input type ="text" name="CatName" > 
		System.out.println("JLK:");
		<select id ="cats" style="width: 50px;">
<!-- 			<option value="concrete">Concrete</option> -->
<!-- 			<option value="concrete">Landscape</option> -->
<!-- 			<option value="concrete">Construction</option> -->
			
		</select>
		
		
		
		<ul>
			<c:forEach items="${app.categories}" var="item">
			<li>${item.name}</li>
			</c:forEach>
		</ul>
		
		<form:form modelAttribute="app">
			<form:select id="docNo" path="categories">
	           <form:option value="NONE" label="--- Select ---" />
	           <form:options items="${app.categories}" itemValue="name" itemLabel="name"/>
	         </form:select>
         </form:form>
			
			
	</form>
	
	<body>
<%-- 	<script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<!-- 	<script type="text/javascript"> -->
		
<!-- 	</script> -->
	<script src="<c:url value="/static/javascript/database.js" />"></script>
    <!-- <script type="text/javascript" src="/static/javascript/database.js"></script> -->
    <script type="text/javascript">
    	//alert("0")
    	
    	
    		var select = document.getElementById("cats"); 
	//alert("1")
	
	
	
	//var list2 = 
	//System.out.println(list.length);
	
	for(var i = 0; i < "${list}".length; i++) {
		
		//alert("2")
	    var opt = ${list[i].firstName};
	    var el = document.createElement("option");
	    el.textContent = opt;
	    el.value = opt;
	    select.appendChild(el);
	}	
    	
    	
    	
    	
       //LoadCats(${list});
       //alert("3")
    </script>
    	
	</body>
	
	
	
	
	
<%@ include file="./includes/Footer.jsp" %>
