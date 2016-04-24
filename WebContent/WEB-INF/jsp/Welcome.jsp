<%@ include file="./includes/Header.jsp"%>

	<head>
		<link rel="stylesheet" type="text/css" href="./static/css/signIn.css" />

	</head>

	<div class="container">
	
				
		<button id="debug1">Employer (UserId = 1)</button>
		<button id="debug2">Employee (UserId = 2)</button>
	
	
		<div class="jumbotron">
			<a href="./jobs/find" style="display: inline; margin-right: 5px"
			type="button" class="btn btn-info">Looking For Work</a>
			<h1 style="display: inline; margin: auto">Labor Vault</h1> 
			<a href="./employees/find" style="display: inline; margin-left: 5px"
			type="button" class="btn btn-info">Looking To Hire</a> 
		</div>

		<!-- Original code found here -->
		<!-- https://gist.github.com/bMinaise/7329874#file-bs3-login-form-html -->
		<div class="container">
		    <div class="row">
		        <div class="col-sm-6 col-md-4 col-md-offset-4">
		<!--             <h1 class="text-center login-title">Sign in to continue to Bootsnipp</h1> -->
		            <div class="account-wall">
		                <img class="profile-img" src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120"
		                    alt="">
		                <form:form class="form-signin" commandName="user" action="./login.do" method="POST">
			                <form:input type="text" class="form-control" placeholder="Email" 
			                	path="username" id="userName"/>
			                <form:password class="form-control" placeholder="Password"
			                	path="password" id="password" />
			                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign In</button>
			             
		<!-- 	                <label class="checkbox pull-left"> -->
		<!-- 	                    <input type="checkbox" value="remember-me"> -->
		<!-- 	                    Remember me -->
		<!-- 	                </label> -->
		<!-- 	                <a href="#" class="pull-right need-help">Need help? </a><span class="clearfix"></span> -->
		                </form:form>
		            </div>
		            <a id="createAccount" onclick="createAccount()" class="text-center new-account">Create an account </a>
		        </div>
		    </div>
		
		    <div class="row" style="margin-top: 10px">
		        <div class="col-sm-6 col-md-4 col-md-offset-4">

					<div id="createAccountContainer" style="display: none">
							
						<p>Create New Account</p>
						<form:form action="./registerUser" method="POST" commandName="user">
							<table>
								<tr>
									<td class="form-control-label">First Name:</td>
									<td><form:input id='co_firstName' path="firstName" class="form-control"/></td>
								</tr>
								<tr>
									<td>Last Name:</td>
									<td><form:input id='co_lastName' path="lastName" class="form-control"/></td>
								</tr>
								<tr>
									<td>Email:</td>
									<td><form:input id='co_emailAddress' path="emailAddress" class="form-control"/></td>
								</tr>
								<tr>
									<td>Password:</td>
									<td><form:password id='co_password' path="password" class="form-control"/></td>
								</tr>
								<tr>
									<td>Confirm Password:</td>
									<td><form:password id='co_matchingPassword'
											path="matchingPassword" class="form-control"/></td>
								</tr>
								<tr>
									<td>Profile Type:</td>
									<td><form:select path="profileId">
											<form:options items="${profiles}" itemValue="id" itemLabel="name"></form:options>
										</form:select></td>
								</tr>
								<tr>
									<td><input id='co_registerUser' type="submit"
										value="Create Account" class="btn"/></td>
								</tr>
								<tr>
									<td><input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}"/></td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
			</div>
		</div>

<a href="./dummyData">Set Dummy Data</a>

	</div>
		
<script>

	$(document).ready(function(){
		$("#debug1").click(function(){
			$("#password").val('1');
			$("#userName").val('1');
			$("button[type=submit]")[0].click();
			
		})
		
		$("#debug2").click(function(){
			$("#password").val('2');
			$("#userName").val('2');
			$("button[type=submit]")[0].click();
			
		})	
		

	})
	
		function createAccount(){
			$("#createAccountContainer").toggle();
			
		}
	


</script>

<%@ include file="./includes/Footer.jsp"%>