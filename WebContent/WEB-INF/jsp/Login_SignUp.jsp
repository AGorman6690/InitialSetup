<%@ include file="./includes/Header.jsp"%>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />	
<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/Templates/forms.css" />	
	
<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>			
<script src="<c:url value="/static/javascript/Signup.js" />"></script>		
	
	<div id="verifyEmail">
		A verification link has been sent to your email, please login and verify.
	</div>
	
<div id="loginGroup" class="group-container">
	<div id="loginHeader" class="a-header">
		<h3>Login</h3>
	</div>
	<div id="loginContainer" class="group ${requestedLogin == 0 ? 'do-hide' : '' }">
		<div class="">
			<form:form class="" modelAttribute="user" action="/JobSearch/login.do" method="POST"> 
				<div class="">
					<label></label>							
					<span id="errorMessage" class="invalid-message-show">${errorMessage }</span>					
				</div>						
				<div class="item">
					<label>Email Address</label>
					<form:input type="text" class="" path="username" id="userName"/>
				</div>
				<div class="item">
					<label>Password</label>
					<form:password class="" path="password" id="login-password"/>
										
						
					<div class="no-label">						
						<a href="./user/password/reset">Forgot Password?</a>
					</div>										
				</div>		
				<div class="no-label">	
					<input id="keep-me-signed-in" type="checkbox" value="">
					<label for="keep-me-signed-in">Keep Me Signed In</label>
				</div>
				<div class="no-label">	
					<input id="login" class="" type="submit" value="Login"/>
				</div>					
			</form:form>			
		</div>

    </div>
    
</div>
	
  
<div id="signUpGroup" class="group-container">
	<div id="setupHeader" class="a-header">
		<h3>Sign Up</h3>
	</div>

    
	<div id="signUpContainer" class="group ${requestedLogin == 1 ? 'do-hide' : '' }">						
			<div class="item" >
				<label>First Name</label>
				<input id="signup-firstName" type="text" class="${invalidFirstName ? 'invalid' : '' }"/>
			</div>
			<div class="item">
				<label>Last Name</label>
				<input id="signup-lastName" type="text" class="${invalidLastName ? 'invalid' : '' }"/>
			</div>										
				
			<div class="item">
				<label>Email Address</label>
				<input id="signup-email" type="email"
					 class="${invalidEmail || invalidEmail_alreadyInUse ? 'invalid' : '' }"/>
			</div>
			<div id="invalidEmail_format" class="error-message ${invalidEmail ? 'do-show' : '' }">
				Invalid email address
			</div>		
			<div id="invalidEmail_duplicate" class="error-message ${invalidEmail_alreadyInUse ? 'do-show' : '' }">
				Email address already in use
			</div>	
			<div class="item">
				<label>Confirm Email Address</label>
				<input id="signup-confirm-email" type="email"/>
			</div>		
			<div id="invalidConfirmEmail" class="error-message">
				Email addresses do not match
			</div>					
			<div class="item">
				<label>Password</label>
				<div class="input-container">
					<input id="signup-password" type="password" class="${invalidPassword ? 'invalid' : '' }"/>					
				</div>
			</div>
			<div id="invalidPassword" class="error-message ${invalidPassword ? 'do-show' : '' }">
				Password must be between 6 and 20 characters
			</div>
			<div class="item">
				<label>Confirm Password</label>
				<input id="signup-confirm-password" type="password" class="${invalidMatchingPassword ? 'invalid' : '' }"/>
			</div>
			<div id="invalidConfirmPassword" class="error-message ${invalidMatchingPassword ? 'do-show' : '' }">
				Passwords do not match
			</div>
			<div class="item">
				<label>Profile Type</label>
				<select id="signup-profiles" class="${invalidProfile ? 'invalid' : '' }">
					<option value="-1" selected disabled>Select a profile type</option>
					<c:forEach items="${profiles }" var="profile">
						<option value="${profile.id }">${profile.altName1 }</option>
					</c:forEach>				
				</select>
			</div>
			<div class="item">
				<label></label>
				<input id="create-account" type="submit" value="Create Account" class="" />
			</div>
			<div class="item">
				<label><input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /></label>
			</div>
	</div>	
	
	<br></br>
	<br></br>
	<br></br>
	<button style="display:block" id="debug1">Sign in as employer (UserId = 1)</button>
	<button style="display:block" id="debug2">Sign in as employee 1</button>
	<button style="display:block" id="debug3">Sign in as employee 2</button>
		
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
			$("#login-password").val('jg');
			$("#userName").val('gorma080@d.umn.edu');
			$("#login").click();
			
			

		})

		$("#debug2").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#login-password").val('2');
			$("#userName").val('2a');
			$("#login").click();

		})
		
		$("#debug3").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#login-password").val('jg');
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

<%@ include file="./includes/Footer.jsp"%>