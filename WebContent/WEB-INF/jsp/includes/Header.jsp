<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
	type="text/javascript"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="./static/css/global.css" rel="stylesheet" />
<!-- <link href="http://localhost:8080/JobSearch/WebContent/static/css/global.css" rel="stylesheet" /> -->
<script src="./static/External/underscore-min.js"></script>

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
<link href="https://cdn.datatables.net/1.10.11/css/dataTables.bootstrap.min.css" rel="stylesheet" />
<script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.11/js/dataTables.bootstrap.min.js"></script>
	
<!-- 	Additional code for Bootstrap date picker -->
<script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />
<script type="text/javascript" src="http://eternicode.github.io/bootstrap-datepicker/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet" type="text/css" href="http://eternicode.github.io/bootstrap-datepicker/bootstrap-datepicker/css/datepicker3.css" />


<!-- 	   	********************************************************* -->


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
</head>
<body style="height: 100%; width: 95%; margin: auto">

	<nav class="navbar navbar-default">
	<div class="container-fluid">
		<c:choose>
			<c:when test="${user.getFirstName() != null}">
				<div class="navbar-header">
					<a id="home" class="navbar-brand" href="http://localhost:8080/JobSearch/getProfile">Labor Vault</a>
				</div>
			</c:when>
		</c:choose>
		<ul class="nav navbar-nav">
			<!--       <li><a href="./viewApplicationsE">View Applications</a></li> -->
			<c:choose>
				<c:when test="${user.getProfileId() == 1}">
					<li><a href="http://localhost:8080/JobSearch/viewFindEmployees">Find Employees</a></li>
					<li><a href="http://localhost:8080/JobSearch/viewPostJob">Post Job</a></li>					
				</c:when>
				<c:when test="${user.getProfileId() == 2}">
					<li><a href="http://localhost:8080/JobSearch/viewFindJobs">Find Jobs</a></li>
				</c:when>
			</c:choose>

			<c:choose>
				<c:when test="${user.getFirstName() != null}">
					<li><a href="http://localhost:8080/JobSearch/viewProfile">Profile</a></li>
					<li><a href="http://localhost:8080/JobSearch/logout">Log out</a></li>
				</c:when>
			</c:choose>



			<!-- 	   	      <li><a href="./viewRatings">View Ratings</a></li> -->
			<!-- 	  	      <li><a href="./viewApplicationsR">View Applications</a></li> -->
		</ul>
	</div>
	</nav>