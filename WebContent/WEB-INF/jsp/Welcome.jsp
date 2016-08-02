<%@ include file="./includes/Header.jsp"%>

<head>
<link rel="stylesheet" type="text/css" href="./static/css/welcome.css" />

</head>

<div class="container">



	
	<!-- Original code found here -->
	<!-- https://gist.github.com/bMinaise/7329874#file-bs3-login-form-html -->	
	<div class="input-container sign-up bottom-border-thin">
		<form:form action="/JobSearch/registerUser" method="POST" commandName="user">
			<table>
				<tr>
					<td class="">First Name:</td>
					<td><form:input id='co_firstName' path="firstName"
							class="form-control" /></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><form:input id='co_lastName' path="lastName"
							class="form-control" /></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><form:input id='co_emailAddress' path="emailAddress"
							class="form-control" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><form:password id='co_password' path="password"
							class="form-control" /></td>
				</tr>
				<tr>
					<td>Confirm Password:</td>
					<td><form:password id='co_matchingPassword'
							path="matchingPassword" class="form-control" /></td>
				</tr>
				<tr>
					<td>Profile Type:</td>
					<td><form:select path="profileId" class="form-control">
							<form:option value="-1" label="Select a profile type" />
							<form:options items="${profiles}" itemValue="id"
								itemLabel="name" ></form:options>
						</form:select></td>
				</tr>
				<tr>
					<td></td>
					<td id="createAccount"><input id='co_registerUser' type="submit"
						value="Create Account" class="square-button" /></td>
				</tr>
				<tr>
					<td><input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /></td>
				</tr>
			</table>
		</form:form>	
	</div>

	<div class="main">
		<div class="main-item">
			<a href="./jobs/find">Looking For Work</a>
		</div>
		<div class="main-item">
			<h1>Labor Vault</h1>
		</div>
		<div class="main-item">
			<a href="./employees/find">Looking To Hire</a>
		</div>
	</div>
	

<!-- 	<a href="./dummyData">Set Dummy Data</a> -->
	
	
	
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>	
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>

	
	<button id="debug1">Sign in as employer (UserId = 1)</button>
	<button id="debug2">Sign in a employee (UserId = 2)</button>

</div>

<script>
	$(document).ready(function() {
		$("#debug1").click(function() {
			$("#password").val('jg');
			$("#userName").val('gorma080@d.umn.edu');
			$("button[type=submit]")[0].click();

		})

		$("#debug2").click(function() {
			$("#password").val('2');
			$("#userName").val('2');
			$("button[type=submit]")[0].click();

		})
		
		$("#login").click(function(){
			$("div.login").show();
			$("div.sign-up").hide();
		})
		
		$("#signUp").click(function(){
			$("div.login").hide();
			$("div.sign-up").show();
		})

	})

	function createAccount() {
		$("#createAccountContainer").toggle();

	}
</script>

<%@ include file="./includes/Footer.jsp"%>