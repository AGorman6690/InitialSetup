	<%@ include file="./includes/Header.jsp"%>
	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />	
	
	<div class="group-container">
		<div id="loginHeader" class="a-header">
			<h3>Login</h3>
		</div>
		<div id="loginContainer" class="group ${requestedLogin == 0 ? 'do-hide' : '' }">
			<div class="">
				<form:form class="" modelAttribute="user" action="/JobSearch/login.do" method="POST"> 
					<div class="item">
						<label>Email Address</label>
						<form:input type="text" class="" path="username" id="userName"/>
					</div>
					<div class="item">
						<label>Password</label>
						<form:password class="" path="password" id="password"/>
											
						<div class="item">
							<label></label>							
							<span id="errorMessage" class="invalid-message-show">${errorMessage }</span>					
						</div>							
						<div class="forgot-password">
							<label></label>							
							<a href="./user/password/reset">Forgot Password?</a>
						</div>						
					</div>
					
			
					<div class="item">		
						<div class="checkbox">
							<label></label>
						    <label class="checkboxLabel"><input type="checkbox" value="">Keep Me Signed In</label>
						</div>							
						<label></label>
						<input id="login" class="square-button" type="submit" value="Login"/>
					</div>					
				</form:form>			
			</div>
			<br></br>
			<br></br>
			<br></br>
			<button style="display:block" id="debug1">Sign in as employer (UserId = 1)</button>
			<button style="display:block" id="debug2">Sign in as employee 1</button>
			<button style="display:block" id="debug3">Sign in as employee 2</button>
	    </div>
	    
	</div>
	
    <div class="group-container">
		<div id="setupHeader" class="a-header">
			<h3>Sign Up</h3>
		</div>	    
		<div id="signUpContainer" class="group ${requestedLogin == 1 ? 'do-hide' : '' }">						
			<form:form action="/JobSearch/user/sign-up" method="POST" modelAttribute="user">
				<div class="item">
					<label>First Name</label>
					<form:input path="firstName"/>
				</div>
				<div class="item">
					<label>Last Name</label>
					<form:input path="lastName"/>
				</div>										
					
				<div class="item">
					<label>Email Address</label>
					<form:input path="emailAddress"/>
				</div>
				<div class="item">
					<label>Password</label>
					<form:password path="password"/>
				</div>
				<div class="item">
					<label>Confirm Password</label>
					<form:password path="matchingPassword" class="" />
				</div>
				<div class="item">
					<label>Profile Type</label>
					<form:select path="profileId" class="">
							<form:option value="-1" label="Select a profile type" />
							<form:options items="${sessionScope.profiles}" itemValue="id"
								itemLabel="name" ></form:options>
						</form:select>
				</div>
				<div class="item">
					<label></label>
					<input id="createAccount" type="submit" value="Create Account" class="square-button" />
				</div>
				<div class="item">
					<label><input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /></label>
				</div>
			</form:form>	
		</div>	
	</div>


	<script>
	$(document).ready(function() {
		
		


		$(".a-header").click(function(){
			
			var $slideUp;
			var $slideDown;
			
			if($(this).attr("id") == "loginHeader"){
				$slideUp = $("#signUpContainer");
				$slideDown = $("#loginContainer");
			}
			else{
				$slideDown = $("#signUpContainer");
				$slideUp = $("#loginContainer");
			}
			
			slideDown($slideDown, 700);
			slideUp($slideUp, 700);
		})
		
		
		$("#debug1").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#password").val('jg');
			$("#userName").val('gorma080@d.umn.edu');
			$("#login").click();
			
			

		})

		$("#debug2").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#password").val('2');
			$("#userName").val('2');
			$("#login").click();

		})
		
		$("#debug3").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#password").val('jg');
			$("#userName").val('justin.gorman@wilsontool.com');
			$("#login")[0].click();

		})		
		
// 		$("#login").click(function(){
// 			$("div.login").show();
// 			$("div.sign-up").hide();
// 		})
		
		$("#signUp").click(function(){
			$("div.login").hide();
			$("div.sign-up").show();
		})

	})

	function createAccount() {
		$("#createAccountContainer").toggle();

	}
</script>	