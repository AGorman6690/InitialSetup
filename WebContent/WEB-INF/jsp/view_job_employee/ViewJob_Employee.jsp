<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/DatePicker.jsp"%> --%>

<%@ include file="../includes/resources/SelectPageSection.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employee/viewJob_Employee.css" />



<c:if test="${context == 'find' }">
	<script src="<c:url value="/static/javascript/view_job_employee/SubmitApplication.js"/>"></script>
</c:if>
	
<!-- <div class="select-page-section-container"> -->
<!-- 	<span data-page-section-id="job-info-container" class="selected select-page-section">Job Post</span> -->
<!-- 	<span data-page-section-id="employer-info-container" class="select-page-section">Employer</span> -->
<%-- 	<c:if test="${context == 'find' && empty jobDto.application}"> --%>
<!-- 		<span data-page-section-id="apply-container"  class=" select-page-section ">Apply</span> -->
<%-- 	</c:if> --%>
<!-- </div> -->


<c:if test="${isLoggedIn}">		
	<c:choose>					
		<c:when test="${context == 'find' && !empty jobDto.application}">	
			<div id="applicationStatus">	
				${jobDto.application.status == 0 ||
					 jobDto.application.status == 2 ||
					 jobDto.application.status == 4 ? "Application has been submitted" :
					jobDto.application.status == 1 ? "Application has been declined" :
					jobDto.application.status == 5 ? "You have withdrawn your application" :
					jobDto.application.status == 6 ? "The employer filled all positions. Your application remains in the employer's inbox." :
					"Application has been accepted" }		
			</div>			
		</c:when>
		<c:when test="${context == 'find' }">
<!-- 			<div id="applicationStatus"> -->
<%-- 				${ --%>
<%-- 					jobDto.availabilityStatus == 1 ? "You are not available due to other employment. You cannot apply for this job." :												 --%>
<%-- 					jobDto.availabilityStatus == 3 ? "You are partially available due to other employment" : --%>
<%-- 					jobDto.availabilityStatus == 4 ? "You are available" : "" } --%>
<!-- 			</div>							 -->
		</c:when>					
	</c:choose>
</c:if>

<div id="job-info-container" class="">	
	<%@include file="../JobInfo_NEW.jsp"%>						
</div>
<div id="employer-info-container" class="page-section center pad-top-2">	
	<%@ include file="../ratings/RatingsByUser.jsp"%>
</div>
<c:if test="${context == 'find' && empty jobDto.application &&
			jobDto.availabilityStatus != 1 }">
			
<%-- 	<div id="apply-container" class="page-section ${!isLoggedIn ? 'not-logged-in' : '' }"> --%>
<%-- 		<%@ include file="./ApplyContainer.jsp" %> --%>
<!-- 	</div> -->
</c:if>



<%@ include file="../includes/Footer.jsp"%>
<%@ include file="../includes/resources/JobInformation.jsp"%>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap">
</script>