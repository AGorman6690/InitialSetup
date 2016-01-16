<%@ include file="./includes/Header.jsp" %>

	<h1>Sign In With Email</h1>
	<form:form commandName="user" action="profile" method="post">
		<table>
			<tr>
				<td>Email:</td>
			</tr>
			<tr>
				<td><form:input path="emailAddress" /></td>
			</tr>		
			<tr>
				<td><input type="submit" value="Sign in" /></td>
			</tr>
		</table>		
	</form:form>

<%@ include file="./includes/Footer.jsp" %>