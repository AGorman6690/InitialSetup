<%@ include file="./includes/Header.jsp"%>

<div style="width: 80%; height: 800px; margin: auto">
	<div style="float: left; width: 40%; height: 25%; border: 1px gray; border-radius: 2px; margin: auto">

		
		
		<p>Sign In</p>
		<form:form commandName="user" action="./login.do" method="POST">
			<table>
				<tr>
					<td>Email:</td>
				</tr>
				<tr>
					<td><form:input path="username" id="userName" /></td>
				</tr>
				<tr>
					<td>Password:</td>
				</tr>
				<tr>
					<td><form:password path="password" id="password"/></td>
				</tr>

				<tr>
					<td><input type="submit" value="Sign in" /></td>
				</tr>
			</table>
		</form:form>
		
		<button id="debug1">Employer (UserId = 1)</button>
		<button id="debug2">Employee (UserId = 2)</button>
	</div>

	<div
		style="float: right; width: 40%; height: 25%; border: 1px gray; border-radius: 2px; margin: auto">
		<p>Create New Account</p>
		<form:form action="./registerUser" method="POST" commandName="user">
			<table>
				<tr>
					<td>First Name:</td>
					<td><form:input id='co_firstName' path="firstName" /></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><form:input id='co_lastName' path="lastName" /></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><form:input id='co_emailAddress' path="emailAddress" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><form:password id='co_password' path="password" /></td>
				</tr>
				<tr>
					<td>Confirm Password:</td>
					<td><form:password id='co_matchingPassword'
							path="matchingPassword" /></td>
				</tr>
				<tr>
					<td>Profile Type:</td>
					<td><form:select path="profileId">
							<form:options items="${profiles}" itemValue="id" itemLabel="name"></form:options>
						</form:select></td>
				</tr>
				<tr>
					<td><input id='co_registerUser' type="submit"
						value="Create Account" /></td>
				</tr>
				<tr>
					<td><input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}"/></td>
				</tr>
			</table>
		</form:form>
	</div>
</div>

<script>

	$(document).ready(function(){
		$("#debug1").click(function(){
			$("#password").val('1');
			$("#userName").val('1');
			$("input[type=submit]")[0].click();
			
		})
		
		$("#debug2").click(function(){
			$("#password").val('2');
			$("#userName").val('2');
			$("input[type=submit]")[0].click();
			
		})		
	})

</script>

<%@ include file="./includes/Footer.jsp"%>