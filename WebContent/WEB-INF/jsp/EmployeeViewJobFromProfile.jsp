<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/Jobs.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/Questions.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/ChangeForm.js"/>"></script> --%>
	
	<script src="<c:url value="/static/javascript/SideBar.js" />"></script>
	

	<script src="<c:url value="/static/javascript/Map.js" />"></script>

	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/categories.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/sideBar.css " />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/questions.css " />	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/map.css " />	
</head>

<body>
	<div class="container">
		<div class="row">
			<div id="sideBarContainer" class="col-sm-2">
				<div id="jobInfo" class="side-bar selected-blue" data-section-id="jobInfoContainer">Job Information</div>				
				<div id="questionInfo" class="side-bar" data-section-id="section_questionsContainer">Questions</div>
			</div>			
			<div class="col-sm-10" id="sectionContainers">
				<div id="jobInfoContainer" class="first section-container">
					<div class="section-body">
						<h4>Job Information</h4>
						<div class="body-element-container">
							<%@include file="./templates/JobInformation.jsp"%>
						</div>
					</div>
				</div>						
				<div id="section_questionsContainer" class="section-container">
					<div class="section-body">
						<h4>Questions</h4>
						<div class="body-element-container">
							<%@include file="./templates/Questions_ShowAnswers.jsp"%>
						</div>
					</div>		
				</div>
			</div> 
		</div>
	</div>
</body>



<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
</script>


<%@ include file="./includes/Footer.jsp"%>