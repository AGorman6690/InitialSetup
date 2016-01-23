<%@ include file="./includes/Header.jsp" %>

	<h1>Sign In With Email</h1>
	<form:form commandName="user" action="./login.do" method="POST">
		<table>
			<tr>
				<td>Email:</td>
			</tr>
			<tr>
				<td><form:input path="username" /></td>
			</tr>		
			<tr>
				<td>Password:</td>
			</tr>
			<tr>
				<td><form:input path="password" /></td>
			</tr>	
			<tr>
				<td><input type="submit" value="Sign in" /></td>
			</tr>
		</table>		
	</form:form>

<%@ include file="./includes/Footer.jsp" %>