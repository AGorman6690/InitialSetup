<%@ include file="./includes/Header.jsp"%>

<div class="row" style="margin-top: 10px">
	<div class="col-sm-6 col-md-4 col-md-offset-4">

		<div id="createAccountContainer" style="display: none">

			<p>Create New Account</p>
			<form:form action="/JobSearch/registerUser" method="POST"
				commandName="user">
				<table>
					<tr>
						<td>New Password:</td>
						<td><form:password id='co_password' path="password"
								class="form-control" /></td>
					</tr>
					<tr>
						<td>Confirm Password:</td>
						<td><form:password id='co_matchingPassword'
								path="matchingPassword" class="form-control" /></td>
					</tr>
					<tr>
						<td><input id='co_registerUser' type="submit"
							value="Create New Password" class="btn" /></td>
					</tr>
					<tr>
						<td><input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /></td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
</div>

<%@ include file="./includes/Footer.jsp"%>