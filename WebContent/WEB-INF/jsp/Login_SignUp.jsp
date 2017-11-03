<%@ include file="./includes/TagLibs.jsp" %> 
<div id="page-wrapper" data-context="${context}" >
	<div id="login-sign-up-mod" class="mod simple-header">
		<div class="mod-content">
			<div class="mod-header"></div>
			<div class="mod-body">				
				<div id="login-sign-up-context">
					<span id="sign-up" class="context-item">Sign Up</span>
					<span>/</span>
					<span id="login" class="context-item">Login</span>									
				</div>
				<div id="login-wrapper" class="group-container validate-input">		
					<form:form class="" modelAttribute="user" action="/JobSearch/login.do" method="POST"> 		
						<div class="item">
							<label>Email Address</label>
<!-- 							<input id="login-email-address" type="text"/> -->
							<form:input type="text" class="" path="username" id="login-email-address"/>
						</div>
						<div class="item">
							<label>Password</label>
<!-- 							<input id="login-password" type="password"/>						 -->
							<form:password class="" path="password" id="login-password"/>
						</div>		
						<div class="item">
							<label></label>	
							<div class="no-label">		
	<!-- 							<div>				 -->
	<!-- 								<a href="./user/password/reset">Forgot Password?</a> -->
	<!-- 							</div> -->
	<!-- 							<div id="keep-me-signed-in-wrapper"> -->
	<!-- 								<input id="keep-me-signed-in" type="checkbox" value=""> -->
	<!-- 								<label for="keep-me-signed-in">Keep Me Signed In</label>	 -->
	<!-- 							</div> -->
								<div>	
<!-- 									<button id="do-login" class="sqr-btn blue">Login</button> -->
									<input id="do-login" class="sqr-btn blue" type="submit" value="Login"/>
								</div>					
							</div>				
						</div>	
					</form:form>	
				
											<br></br>
						<br></br>
						<br></br>
						<button style="display:block" id="debug1">Sign in as employer (UserId = 1)</button>
						<button style="display:block" id="debug2">Sign in as employee 1</button>
						<button style="display:block" id="debug3">Sign in as employee 2</button>
											
				</div>
				<div id="sign-up-wrapper" class="group-container"> 	
					<div id="sign-up-input" class="validate-input">			
						<div class="item" >
							<label>First Name</label>
							<input id="signup-first-name" type="text"/>
						</div>
						<div class="item">
							<label>Last Name</label>
							<input id="signup-last-name" type="text"/>
						</div>													
						<div class="item">
							<label>Email Address</label>
							<input id="signup-email-address" type="email"
								data-must-be-matched-by="signup-confirm-email-address"/>	
						</div>
						<div class="item">
							<label>Confirm Email Address</label>
							<input id="signup-confirm-email-address" type="email"
								data-must-match="signup-email-address"/>
						</div>						
						<div class="item">
							<label>Password</label>
							<input id="signup-password" type="password"
								data-must-be-matched-by="signup-confirm-password"/>					
							<div class="invalid-input-message">
								<p>Password must be between 6 and 20 characters</p>
							</div>
						</div>
						<div class="item">
							<label>Confirm Password</label>
							<input id="signup-confirm-password" type="password"
								data-must-match="signup-password"/>
						</div>
						<div class="item">
							<label>Profile Type</label>
							<select id="signup-profiles" class="${invalidProfile ? 'invalid' : '' }">
								<option value="-1" selected></option>
								<c:forEach items="${profiles }" var="profile">
									<option value="${profile.id }">${profile.altName1 }</option>
								</c:forEach>				
							</select>
						</div>
						<div id="do-sign-up-container" class="no-label">
							<button id="do-sign-up" class="sqr-btn blue">Sign Up</button>
							<div id="sign-up-fail" class="invalid-input-message">
<!-- 								<p>Invalid input</p> -->
								<p id="sign-up-fail-email-in-use" class="sign-up-fail-msg">Email address is already in use</p>
								<p id="sign-up-fail-invalid-email" class="sign-up-fail-msg">Invalid email address</p>
							</div>
						</div>						
						<div class="item">
							<label><input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" /></label>
						</div>	
					</div>
					<div id="successful-sign-up" class="center">
						<h4>Sign up successful!</h4>
						<p>Please login to you email and verify your acount.</p>
					</div>
				</div>	
			</div>			
		</div>
	</div>
</div>
