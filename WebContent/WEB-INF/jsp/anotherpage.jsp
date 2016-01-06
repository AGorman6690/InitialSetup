<%@ include file="./includes/Header.jsp" %>
   
    <script src="<c:url value="/static/javascript/User.js" />"></script>
    
<%-- 	 <c:set var="user" value="${user}"/> --%>
	
	<div> Hello ${user.firstName} ${user.lastName}</div>
	
	<input type="hidden" id="userId" value="3"/>
	<input id="userGetRequest" type="button" value="Ajax Get Request"/>
	
	
	<!-- <input type="hidden" id="userIdSet" value="4"/>
	<input id="userSetRequest" type="button" value="Ajax Set Request"/>
	 -->
	<div id="userFromAjaxRequest"> </div>
	
<%@ include file="./includes/Footer.jsp" %>