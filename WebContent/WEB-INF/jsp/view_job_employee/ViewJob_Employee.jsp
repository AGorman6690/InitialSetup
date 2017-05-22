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
<c:choose>
	<c:when test="${isLoggedIn && context == 'find' && !empty jobDto.application}">						
		<div class="warning-message">	
			<h3>
			${jobDto.application.status == 0 ||
				 jobDto.application.status == 2 ||
				 jobDto.application.status == 4 ? "Application has been submitted" :
				jobDto.application.status == 1 ? "Application has been declined" :
				jobDto.application.status == 5 ? "You have withdrawn your application" :
				jobDto.application.status == 6 ? "The employer filled all positions. Your application remains in the employer's inbox." :
				"Application has been accepted" }	
			</h3>	
		</div>						
	</c:when>
	<c:when test="${sessionScope.jobs_needRating.size() > 0 }">
		<p id="jobs-needing-rating-warning">Please rate your previous employer before applying to another job</p>
	</c:when>	
</c:choose>
	<div id="job-info-container" class="">	
		<%@include file="../JobInfo_NEW.jsp"%>						
	</div>
</div>

<%@ include file="../includes/Footer.jsp"%>
<%-- <%@ include file="../includes/resources/JobInformation.jsp"%> --%>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap">
</script>