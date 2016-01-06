<%@ include file="./includes/Header.jsp" %>
   
    <script src="<c:url value="/static/javascript/User.js" />"></script>
		
	<form>
		First Name:<br>
		<input id="firstName" type="text" name = "FirstName"> <br> 
		
		<br>
		Last Name:<br>
		<input id="lastName" type="text" name = "LastName"> <br>
		
		<br>
		Email:<br>
		<input id="emailAddress" type="text" name = "Email">		
	
	</form>
	
	<br>
	<input id="userSetRequest" type="button" value="Create New Account"/>
	
	<input id="setCategories" type="button" value="Set Categories"/>
	
	
	
<%@ include file="./includes/Footer.jsp" %>