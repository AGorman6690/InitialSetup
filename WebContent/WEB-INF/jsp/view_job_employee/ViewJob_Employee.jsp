<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/DatePicker.jsp"%>
<%@ include file="../includes/resources/JobInformation.jsp"%>
<%@ include file="../includes/resources/SelectPageSection.jsp"%>

	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>

	<script src="<c:url value="/static/javascript/SideBar.js"/>"></script>



	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/categories.css" />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob2.css " />		
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employee/viewJob_Employee.css" />


		
	<c:if test="${context == 'find' }">
		<script src="<c:url value="/static/javascript/view_job_employee/SubmitApplication.js"/>"></script>
	</c:if>
	
<div class="select-page-section-container">
	<span data-page-section-id="jobInfoContainer" class="selected select-page-section">Job Information</span>
	<span data-page-section-id="applyContainer"  class="select-page-section ">Apply</span>
</div>
	
<div class="container">
	<div class="row">

		
		<div class="col-sm-12" id="sectionContainers">
			<div id="applicationStatus">	
				<c:choose>					
					<c:when test="${context == 'find' && !empty jobDto.application}">		
							${jobDto.application.status == 0 || jobDto.application.status == 2 ? "Application has been submitted" :
								jobDto.application.status == 1 ? "Application has been declined" :
								jobDto.application.status == 4 ? "Application is waiting for your approval" :
								jobDto.application.status == 5 ? "Application has been withdrawn" :
								"Application has been accepted" }						
						
					</c:when>
					<c:when test="${context == 'find' }">
							${jobDto.availabilityStatus == 0 ? "Per your availability calendar, you are NOT available" :
								jobDto.availabilityStatus == 1 ? "You are NOT available due to other employment. You cannot apply for this job." :	
								jobDto.availabilityStatus == 2 ? "Per your availability calendar, you are PARTIALLY available" :						
								jobDto.availabilityStatus == 3 ? "You are PARTIALLY available due to other employment" :
								jobDto.availabilityStatus == 4 ? "You are available" : "" }							
					</c:when>					
				</c:choose>
			</div>
			<div id="jobInfoContainer" class="section-container page-section">
				<div class="section-body">
	
						<%@include file="../templates/JobInformation.jsp"%>						
				</div>
			</div>

		<c:choose>		
			<c:when test="${context == 'find' && 
								(empty jobDto.application ||
								jobDto.availabilityStatus != 1) }">
								
				<div id="applyContainer" class="section-container page-section">
					<%@ include file="./ApplyContainer.jsp" %>
				</div>
			</c:when>
			<c:otherwise>
<!-- 				<div id="section_questionsContainer" class="section-container"> -->
<!-- 					<div class="section-body"> -->
<!-- 						<h4>Questions</h4> -->
<!-- 						<div class="body-element-container"> -->
<%-- 							<%@include file="../templates/Questions_ShowAnswers.jsp"%> --%>
<!-- 						</div> -->
<!-- 					</div>		 -->
<!-- 				</div>		 -->
			</c:otherwise>	
		</c:choose>		
		</div> <!-- close sections container -->
	</div>

<input type="hidden" id="jobId" value="${jobDto.job.id }">
</div>

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap">
</script>