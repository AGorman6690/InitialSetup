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
							
<!-- 		Google Font -->
		<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Nunito">
		<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Montserrat">
		<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Nunito Sans">
	
			
		
<!-- 		Global Links -->
		<link href="/JobSearch/static/css/global.css" rel="stylesheet" />	
		<link href="/JobSearch/static/css/global_new.css" rel="stylesheet" />	
		<link href="/JobSearch/static/css/global_2.css" rel="stylesheet" />	
		<link href="/JobSearch/static/css/navBar.css" rel="stylesheet" />
		<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/Templates/forms.css" />	
		<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/Templates/headerOptions.css" />	
		

		
						
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Labor Vault</title>
		
		<meta name="_csrf" content="${_csrf.token}" />
		<meta name="_csrf_header" content="${_csrf.headerName}" />
		
		
<%-- 		<%@ include file="../includes/resources/DatePicker.jsp" %> --%>
<%-- 		<%@ include file="../includes/resources/Modal.jsp"%>	 --%>
<%-- 		<%@ include file="../includes/resources/EventCalendar.jsp"%>	 --%>
		
	</head>

	<body>
		<div id="spinner-wheel"> 
			<img src="/JobSearch/static/images/spinner1.gif">
		</div>
		<div id="user-event-calendar"></div>
		<div id="login-signup-mod-wrapper"></div>
		
		<c:set var="LaborVaultHost" scope="session" value="${url}"/>
	
		<nav id="navBar" class="">
			<div id="logoContainer">
				<a id="nav_logo" href="/JobSearch/${!empty sessionScope.user ? 'user' : '' }">Labor Vault</a>
			</div>
			<div id="navItemsContainer">					
				<c:choose>
					<c:when test="${!empty sessionScope.user }">
						<c:choose>
							<c:when test="${sessionScope.user.profileId == 1}">
								<a id="nav_profile" class="" href="/JobSearch/user">Profile</a>
								<a id="nav_calendar" class="" href="#">Calendar</a>								
								<a id="nav_findJobs" href="/JobSearch/jobs/find">Find Jobs</a>	
							</c:when>
							<c:when test="${sessionScope.user.profileId == 2}">
								<a id="nav_jobs" href="/JobSearch/user">Profile</a>					
								<a id="nav_postJob" href="/JobSearch/post-job">Post Job</a>							
								<a id="nav_findEmployees" href="/JobSearch/employees/find">Find Employees</a>
							</c:when>
						</c:choose>				
						<a id="nav_logOut" href="/JobSearch/logout.do">Log out</a>		
					</c:when>
					<c:otherwise>
						<span class="show-login-sign-up-mod" data-context="sign-up" href="#">Sign Up</span>					
						<span class="show-login-sign-up-mod" data-context="login" href="#">Login</span>
					</c:otherwise>
				</c:choose>
			</div>
		</nav>



		
		<div id="login-sign-up-wrapper"></div>


<%-- 	<%@ inlude file="../event_calendar/Event_Calendar.jsp" %> --%>

<script type="text/javascript">	
	var environmentVariables = {
			LaborVaultHost: "${url}"
	};
</script>






























