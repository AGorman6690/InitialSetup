<!-- <div>This was returned from test2</div> -->

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
 
<html>
<head>
<title>SELECT Operation</title>
</head>
<body>
 
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
     url="jdbc:mysql://localhost/jobsearch"
     user="root"  password="password"/>
 
<sql:query dataSource="${snapshot}" var="result">
SELECT jobName from job;
</sql:query>
 
<table border="1" width="100%">
<tr>
   <th>Emp ID</th>

</tr>
<c:forEach var="row" items="${result.rows}">
<tr>
   <td><c:out value="${row.jobName}"/></td>

</tr>
</c:forEach>
</table>
 
</body>
</html>
