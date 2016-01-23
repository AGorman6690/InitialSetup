<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

    
<html>
<head>
       <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
       <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
	   <link href="./static/css/global.css" rel="stylesheet"/>
	   <script src="./static/External/underscore-min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<div>Welcome to JobSearch</div>
<c:choose>

  <c:when test="${user.getEmailAddress() == null}">
      <a href="./createUser">Create New User Account</a>
		<br>
		<a href="./signIn">Sign In</a>
    </c:when>
    <c:otherwise>
       <a href="./getProfile">View Profile</a>
    </c:otherwise>
</c:choose>