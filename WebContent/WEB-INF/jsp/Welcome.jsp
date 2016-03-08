<%@ include file="./includes/Header.jsp" %>

<c:choose>

  <c:when test="${user.getEmailAddress() == null}">
      <a href="./createUser">Create New User Account</a>
		<br>
<!-- 		<a href="./signIn">Sign In</a> -->
    </c:when>
    <c:otherwise>
       <a href="./getProfile">View Profile</a>
    </c:otherwise>
</c:choose>

<%@ include file="./includes/Footer.jsp" %>