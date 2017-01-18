<%@ include file="../includes/TagLibs.jsp" %>

<div id="loginContainer" class="mod">						
    <div class="mod-content">
		<div class="mod-header">
			<span class="glyphicon glyphicon-remove"></span>
			<h3>Login</h3>			
		</div>
      <div class="mod-body">
		<form:form class="form-signin" commandName="user"
			action="/JobSearch/login.do?redirectUrl=${redirectUrl}" method="POST"> 						
			<form:input type="text" class="form-control" placeholder="Email"
				path="username" id="userName" />
			<form:password class="form-control" placeholder="Password"
				path="password" id="password" />
			<div class="forgot-password">
				<a href="./user/password/reset">Forgot Password?</a>
			</div>
			<input class="square-button" type="submit" value="Login"/>
		</form:form>									
      </div>
    </div>		
</div>