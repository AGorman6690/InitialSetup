<%@ include file="./includes/Header.jsp" %>

<body>

	<form:form action="registerUser" method="post" commandName="user">
		<table>
			<tr>
				<td>First Name:</td>
				<td><form:input path="firstName"/>
			</tr>
			<tr>
				<td>Last Name:</td>
				<td><form:input path="lastName"/>
			</tr>
			<tr>
				<td>Email:</td>
				<td><form:input path="emailAddress"/>
			</tr>	
			<tr>
				<td>Profile Type:</td>
				<td>
				<form:select path="profileId">
					<form:options items="${app.profiles}" itemValue="id" itemLabel="name"></form:options>
				</form:select>
			</tr>		
			<tr>
				<td><input type="submit" value="Create Account" /></td>
			</tr>		

		</table>
	</form:form>
</body>


<%@ include file="./includes/Footer.jsp" %>
