<%@ include file="./includes/Header.jsp"%>

<head>
<link rel="stylesheet" type="text/css" href="./static/css/signIn.css" />

</head>

<div class="container">

	<!-- Original code found here -->
	<!-- https://gist.github.com/bMinaise/7329874#file-bs3-login-form-html -->

	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-md-4 col-md-offset-4">
				<!--             <h1 class="text-center login-title">Sign in to continue to Bootsnipp</h1> -->
				<div class="account-wall">
					<img class="profile-img"
						src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120"
						alt="">
					<form:form class="form-signin" commandName="user"
						action="/JobSearch/user/password/reset?redirectUrl=newpassword" method="POST">
						<form:input type="text" class="form-control" placeholder="Email"
							path="username" id="username" />
						<button class="btn btn-lg btn-primary btn-block" type="submit">Reset Password</button>
						<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>

<%@ include file="./includes/Footer.jsp"%>