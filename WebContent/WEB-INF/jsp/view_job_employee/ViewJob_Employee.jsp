<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/SelectPageSection.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employee/viewJob_Employee.css" />

<c:if test="${context == 'find' }">
	<script src="<c:url value="/static/javascript/view_job_employee/SubmitApplication.js"/>"></script>
</c:if>

<div class="">

	<div id="job-info-container" class="">	
		<%@include file="../JobInfo_NEW.jsp"%>						
	</div>
</div>

<%@ include file="../includes/Footer.jsp"%>
<%-- <%@ include file="../includes/resources/JobInformation.jsp"%> --%>
<!-- <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap"> -->
<!-- </script> -->