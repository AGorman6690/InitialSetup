<%@ include file="../includes/Header.jsp"%>


	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js"/>"></script>
	<script src="<c:url value="/static/javascript/SideBar.js"/>"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
	<script src="<c:url value="/static/javascript/Map.js" />"></script>

	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/categories.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob2.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/calendar.css " />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/datepicker.css " />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/map.css" />
		
	
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>	
	
<div class="container">
	<div class="row">
		<div id="sideBarContainer" class="col-sm-2">
			<%@ include file="./SideBar_EmployeeViewJob.jsp" %>
		</div>
		
		<div class="col-sm-10" id="sectionContainers">
			<div id="jobInfoContainer" class="section-container">
				<div class="section-body">
					<h4>Job Information</h4>
					<div class="body-element-container">
				
<%-- 						<%@include file="../templates/JobInformation.jsp"%> --%>
						
					</div>
				</div>
			</div>

		<c:choose>
		
			<c:when test="${context == 'find'}">
			1
				<div id="applyContainer" class="section-container ${!isLoggedIn ? 'not-logged-in' : '' }">
					2
					<%@ include file="./ApplyContainer.jsp" %>
				</div>
			</c:when>
			<c:otherwise>
				<div id="section_questionsContainer" class="section-container">
					<div class="section-body">
						<h4>Questions</h4>
						<div class="body-element-container">
							<%@include file="../templates/Questions_ShowAnswers.jsp"%>
						</div>
					</div>		
				</div>		
			</c:otherwise>	
		</c:choose>		
		</div> <!-- close sections container -->
	</div>

<input type="hidden" id="jobId" value="${jobDto.job.id }">
</div>

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap">
</script>