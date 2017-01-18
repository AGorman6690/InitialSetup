<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


	<%@ include file="./TagLibs.jsp"%>

<html>
	<head>
	

<!-- 		Global Scripts - External -->
		<script
			src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
			type="text/javascript"></script>
		<script
			src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
		<script
			src="/JobSearch/static/External/underscore-min.js"></script>
	


	

		
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
		
		
		<!-- 	Additional code for Bootstrap date picker -->
		<script type="text/javascript"
			src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
		<script type="text/javascript"
			src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
		<link rel="stylesheet" type="text/css"
			href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />
		
		<!-- <script type="text/javascript" src="http://eternicode.github.io/bootstrap-datepicker/bootstrap-datepicker/js/bootstrap-datepicker.js"></script> -->
		<!-- <link rel="stylesheet" type="text/css" href="http://eternicode.github.io/bootstrap-datepicker/bootstrap-datepicker/css/datepicker3.css" /> -->
		
		
		<!-- Bootstrap Drop down -->
		<link rel="stylesheet"
			href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">
		<script
			src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
		
		<!-- 	   	********************************************************* -->
		
					
<!-- 		Google Font -->
		<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Nunito">
	
			
		
<!-- 		Global Links -->
		<link href="/JobSearch/static/css/global.css" rel="stylesheet" />		
		<link href="/JobSearch/static/css/layout.css" rel="stylesheet" />			
		<link href="/JobSearch/static/css/navBar.css" rel="stylesheet" />
		<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/Templates/Modal.css" />	
		<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/LoginSignup.css" />	
		
<!-- 		Global Scripts -->
		<script src="<c:url value="/static/javascript/Layout.js" />"></script>	
		<script src="<c:url value="/static/javascript/NavBar.js" />"></script>	
		<script src="<c:url value="/static/javascript/Utilities.js" />"></script>	
		<script src="<c:url value="/static/javascript/SideBar.js" />"></script>	
		<script src="<c:url value="/static/javascript/Utilities/Modal.js" />"></script>
						
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
		
		<meta name="_csrf" content="${_csrf.token}" />
		<meta name="_csrf_header" content="${_csrf.headerName}" />
	</head>

	<body>
		
		<c:set var="LaborVaultHost" scope="session" value="${url}"/>
		
		
			<nav id="navBar" class="">
				<div class="nav-container nav-border">
					<a id="home" class="logo" href="/JobSearch/${!empty sessionScope.user ? 'user/profile' : '' }">Labor Vault</a>
					<div class="nav-items">					
						<c:choose>
							<c:when test="${!empty sessionScope.user }">
									<a id="nav_logOut" href="/JobSearch/logout">Log out</a>
								<c:choose>
									<c:when test="${sessionScope.user.profileId == 1}">
										<a id="nav_settings" href="/JobSearch/settings">Settings</a>
										<a id="nav_findJobs" href="/JobSearch/jobs/find">Find Jobs</a>								
									</c:when>
									<c:when test="${sessionScope.user.profileId == 2}">
										<a id="nav_findEmployees" href="/JobSearch/employees/find">Find Employees</a>
										<a id="nav_postJob" href="/JobSearch/viewPostJob">Post Job</a>
									</c:when>
								</c:choose>				
								<a id="nav_profile" class="logo selected-green" href="/JobSearch/user/profile">Profile</a>	
							</c:when>
							<c:otherwise>
								<a id="nav_login" data-toggle-mod-id="loginContainer">Login</a>
								<a id="nav_signUp" data-toggle-mod-id="signUpContainer">Sign Up</a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</nav>
			
			<c:choose>
				<c:when test="${empty sessionScope.user }">				
					<%@ include file="../nav_bar/Login.jsp" %>
					<%@ include file="../nav_bar/SignUp.jsp" %>						
				</c:when>	
			</c:choose>




<script type="text/javascript">


	
	var environmentVariables = {
			LaborVaultHost: "${url}"
	};

</script>