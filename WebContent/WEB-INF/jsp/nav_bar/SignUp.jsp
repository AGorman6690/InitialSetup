<%@ include file="../includes/TagLibs.jsp" %>

<div id="signUpContainer" class="mod">						
    <div class="mod-content">
		<div class="mod-header">
			<span class="glyphicon glyphicon-remove"></span>
			<h3>Sign Up</h3>			
		</div>
		<div class="modal-body">
			<form:form action="/JobSearch/user/sign-up" method="POST" commandName="user">
				<div class="input-container">
					<label>First Name</label>
					<form:input id='co_firstName' path="firstName"
								class="form-control" />
				</div>
				<div class="input-container">
					<label>Last Name</label>
					<form:input id='co_firstName' path="lastName"
								class="form-control" />
				</div>										
					
				<div class="input-container">
					<label>Email:</label>
					<form:input id='co_emailAddress' path="emailAddress"
							class="form-control" />
				</div>
				<div class="input-container">
					<label>Password:</label>
					<form:password id='co_password' path="password"
							class="form-control" />
				</div>
				<div class="input-container">
					<label>Confirm Password:</label>
					<form:password id='co_matchingPassword'
							path="matchingPassword" class="form-control" />
				</div>
				<div class="input-container">
					<label>Profile Type:</label>
					<form:select path="profileId" class="form-control">
							<form:option value="-1" label="Select a profile type" />
							<form:options items="${sessionScope.profiles}" itemValue="id"
								itemLabel="name" ></form:options>
						</form:select>
				</div>
				<div class="input-container">
					<input id='co_registerUser' type="submit"
						value="Create Account" class="square-button" />
				</div>
				<div class="input-container">
					<label><input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /></label>
				</div>
			</form:form>	
		</div>	
      </div>
    </div>