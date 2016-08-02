<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
	type="text/javascript"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script
	src="http://localhost:8080/JobSearch/static/External/underscore-min.js"></script>

<!-- 	   Bootstrap -->
<!-- 	   	********************************************************* -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
	crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	crossorigin="anonymous"></script>


<!-- 	Additional code for Bootstrap sortble table -->
<!-- Used this website: https://datatables.net/examples/styling/bootstrap.html -->
<link
	href="https://cdn.datatables.net/1.10.11/css/dataTables.bootstrap.min.css"
	rel="stylesheet" />
<script
	src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.10.11/js/dataTables.bootstrap.min.js"></script>

<!-- 	Additional code for Bootstrap date picker -->
<script type="text/javascript"
	src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript"
	src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />

<!-- <script type="text/javascript" src="http://eternicode.github.io/bootstrap-datepicker/bootstrap-datepicker/js/bootstrap-datepicker.js"></script> -->
<!-- <link rel="stylesheet" type="text/css" href="http://eternicode.github.io/bootstrap-datepicker/bootstrap-datepicker/css/datepicker3.css" /> -->
<link
	href="http://localhost:8080/JobSearch/static/css/bootstrap-datepicker3.standalone.css"
	rel="stylesheet" />
<script
	src="http://localhost:8080/JobSearch/static/External/bootstrap-datepicker.js"></script>

<!-- Bootstrap Drop down -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>


<!-- 	   	********************************************************* -->


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />


<link href="http://localhost:8080/JobSearch/static/css/global.css"
	rel="stylesheet" />
	
<link href="http://localhost:8080/JobSearch/static/css/navBar.css"
	rel="stylesheet" />

	

</head>


<body style="height: 100%; width: 95%; margin: auto">

<c:set var="LaborVaultHost" scope="session" value="${url}"/>


	<nav id="navBar" class="">
		<div class="nav-container nav-border">
			<div class=>
				<div class="logo">
				<c:choose>
					<c:when test="${user.profileId > 0 }">
						<a id="home" class="" href="/JobSearch/user/profile">Labor
							Vault</a>
					</c:when>
					<c:otherwise>
						<a id="home" class="" href="/JobSearch/">Labor
						
							Vault</a>	
					</c:otherwise>
					</c:choose>
				</div>
				<ul class="nav-items">
					
					<c:choose>
						<c:when test="${user.profileId > 0 }">
							<div class="link nav-item">
								<li><a href="/JobSearch/logout">Log out</a></li>
							</div>
							<c:choose>
								<c:when test="${user.profileId == 1}">
									<div class="link nav-item">
										<li><a href="/JobSearch/jobs/find">Find Jobs</a></li>
									</div>
								</c:when>
								<c:when test="${user.profileId == 2}">
									<div class="link nav-item">
										<li><a href="/JobSearch/employees/find">Find Employees</a></li>
									</div>
									<div class="link nav-item">
										<li><a href="/JobSearch/viewPostJob">Post Job</a></li>
									</div>
								</c:when>
							</c:choose>
				
							<c:choose>
								<c:when test="${user.getFirstName() != null}">
									<div class="link nav-item">
									<li><a href="/JobSearch/viewProfile">Profile</a></li>
									</div>
		
								</c:when>
							</c:choose>
						</c:when>
						<c:otherwise>
							<div id="login" class="click link nav-item">
								<li><a data-toggle="modal" data-target="#loginContainer">Login</a></li>
							</div>
							<div id="signUp" class="click link nav-item">
								<li>Sign up</li>
							</div>												
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</nav>
	
	<div id="loginContainer" class="modal fade input-container login bottom-border-thin" role="dialog">
		
		<div class="modal-dialog">
		
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		        <h4 class="modal-title">Login</h4>
		      </div>
		      <div class="modal-body">
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
<!-- 		      <div class="modal-footer"> -->
<!-- 		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
<!-- 		      </div> -->
		    </div>		
		
		
		
		

		</div>	
	</div>

<script type="text/javascript">
	var environmentVariables ={
			LaborVaultHost: "${url}"
	};

</script>