<%@ include file="./includes/Header.jsp" %>

<body>

	<form:form action="registerUser" method="post" commandName="user">
		<table>
			<tr>
				<td>First Name:</td>
				<td><form:input id='co_firstName' path="firstName"/></td>
			</tr>
			<tr>
				<td>Last Name:</td>
				<td><form:input id='co_lastName' path="lastName"/></td>
			</tr>
			<tr>
				<td>Email:</td>
				<td><form:input id='co_emailAddress' path="emailAddress"/></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><form:password  id='co_password' path="password"/></td>
			</tr>
			<tr>
				<td>Confirm Password:</td>
				<td><form:password  id='co_matchingPassword' path="matchingPassword"/></td>
			</tr>	
			<tr>
				<td>Profile Type:</td>
				<td>
				<select id="profiles">
					<form:options items="${profiles}" itemValue="Id" itemLabel="name" />
				</select>
			</tr>		
			<tr>
				<td><input id='co_registerUser' type="submit" value="Create Account" /></td>
			</tr>		
		</table>
	</form:form>
</body>

<script>

</script>



<%@ include file="./includes/Footer.jsp" %>
