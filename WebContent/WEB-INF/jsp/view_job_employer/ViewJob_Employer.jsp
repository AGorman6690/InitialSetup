	<%@ include file="../includes/Header.jsp"%>
	<%@ include file="../includes/ScriptsAndLinks_DatePicker.jsp" %>
	
	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
	<link rel="stylesheet" type="text/css" href="../static/css/employerViewJob.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/wageNegotiation.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/jobInfo.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/questions.css" />
	
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Map.js" />"></script>
	<script src="<c:url value="/static/javascript/SideBar.js" />"></script>
	<script src="<c:url value="/static/javascript/JobInfo.js" />"></script>
	
		
	<c:if test="${context == 'complete' }">	
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/ratings/star-rating.css" />
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/ratings.css" />
		<script src="<c:url value="/static/External/ratings/star-rating.js" />"></script>
		<script src="<c:url value="/static/javascript/view_job_employer/Employee_Ratings.js" />"></script>
	</c:if>
	
	<div class="container">	
		<div class="row"  >

			<div id="contentBarContainer" class="header-container">
			<div id="jobNameHeader" id="tempRow1">
				<h3>${jobDto.job.jobName }</h3>
			</div>		
				<%@ include file="./ContentBar_EmployerViewJob.jsp" %>	
<!-- 				<p class="content-bar selected-lines" data-section-id="jobInfoContainer">Job Information</p> -->
<!-- 				<span class="glyphicon glyphicon-unchecked"></span> -->
<!-- 				<p class="content-bar" data-section-id="questionsContainer">Questions</p> -->
<!-- 				<span class="glyphicon glyphicon-unchecked"></span>				 -->
<!-- 				<p class="content-bar" data-section-id="applicantsContainer">Applicants</p> -->
<!-- 				<span class=" glyphicon glyphicon-unchecked"></span> -->
<!-- 				<p class="content-bar" data-section-id="employeesContainer">Employees</p>				 -->
			</div>			
			
			
		</div>
		<div class="row">

			<div class="col-sm-12" id="sectionContainers">
				<c:if test="${context == 'waiting' }">
				<div id="applicantsContainer" class="section-container">
					<div id="applicants" class="">
						<%@ include file="./Applicants.jsp" %>
					</div>
				</div>	
				</c:if>	
				
				
				<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
				<div id="employeesContainer" class="section-container">				
					<div id="employees" class="">
					<c:choose>
						<c:when test="${context == 'complete' }">
							<%@ include file="./Employee_Ratings.jsp" %>
						</c:when>
						<c:otherwise>	
							<%@ include file="./Employees.jsp" %>					
						</c:otherwise>
					</c:choose>
					</div>			
				</div>	
				</c:if>
				
				<div id="jobInfoContainer" class="section-container">
					<%@include file="../templates/JobInformation.jsp"%>
				</div>				
				
				<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
				<div id="questionsContainer" class="section-container">
					<%@include file="./Questions.jsp"%>	
				</div>
				</c:if>
				
			</div> <!-- end column -->

		</div>		
	</div>	

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap">
</script>

<script type="text/javascript">

$(document).ready(function(){

	$("#selectQuestionsOK").click(function(){
	
		var questionIdsToShow = getQuestionIdsToShow();
		showQuestions(questionIdsToShow);
		
		triggerToggle("selectQuestionsContainer");
		
// 		slideUp($("#selectQuestionsContainer"));
		
	})
	
	$("#selectStatusOK").click(function(){
	
		var statusValuesToShow = getStatusValuesToShow();
		showApplicantRowsByStatus(statusValuesToShow);
		
		triggerToggle("selectStatusContainer");

		
	})
	
	$("#selectAllQuestions").click(function(){
		selectAllCheckboxes($("#questionListContainer"), true);
	})
	
	$("#selectNoQuestions").click(function(){
		selectAllCheckboxes($("#questionListContainer"), false);
	})
	
	$("#statusListContainer input[type=checkbox]").click(function(){
		$("#selectAllStatuses").prop("checked", false);
	})
	
	$("#selectAllStatuses").click(function(){
		selectAllCheckboxes($("#statusListContainer"), $(this).prop("checked"));
	})
	
	$(".application-status-container button").click(function(){
		
		
		var applicationDto = getApplicationDto_UpdateStatus(this);

		updateApplicationStatus(applicationDto);

	})
	
	$("#questionListContainer input[type='checkbox']").click(function(){
		$("#selectAllQuestions").prop("checked", false);
		$("#selectNoQuestions").prop("checked", false);
	})
	
})

function isAddingApplicationStatus(clickedStatusButton){
	
	var currentApplicationStatus = $($(clickedStatusButton).closest("[data-application-status]")[0]).attr("data-application-status");
	var clickedApplicationStatus = $(clickedStatusButton).val();
	
	// If the row's application status is the same as the clicked button's status,
	// then the user is toggling the status to off (i.e. they are removing the application status) 
	if (currentApplicationStatus == clickedApplicationStatus){
		return false;
	}
	else{
		return true;
	}
}


function getStatusValuesToShow(){
	
	var values = [];
	$.each($("#statusListContainer").find("input:checked"), function(){
		values.push($(this).val());
	})
	
	return values;	
	
	
}


function getApplicationDto_UpdateStatus(clickedStatusButton){
	
	var applicationDto = {}
	
	applicationDto.application.applicationId = $($(clickedStatusButton).closest("[data-application-id]")[0]).attr("data-application-id");
	
	if(isAddingApplicationStatus(clickedStatusButton)){
		applicationDto.newStatus = $(clickedStatusButton).val();
	}
	else{
		applicationDto.newStatus = 0;
	}

	return applicationDto;
}

function showApplicantRowsByStatus(statusValuesToShow){
	
	var applicantRows = $("#applicantsTable").find("tr[data-application-status]");
	var applicationStatus;
	$.each(applicantRows, function(){
		applicationStatus = $(this).attr("data-application-status");
		if($.inArray(applicationStatus, statusValuesToShow) > -1){
			$(this).show();
		}
		else{
			$(this).hide();
		}
	})
}

function showQuestions(idsToShow){
	var questionContainers = $("#applicantsTable").find(".question-container");
	var questionId;
	$.each(questionContainers, function(){
		questionId = $(this).attr("data-question-id");
		if($.inArray(questionId, idsToShow) > -1){
			$(this).show();
		}
		else{
			$(this).hide();
		}
	})
}
 

function getQuestionIdsToShow(){

	var ids = [];
	$.each($("#questionListContainer").find("input:checked"), function(){
		ids.push($(this).val());
	})
	
	return ids;

}

function updateApplicationStatus(applicationDto){

	
	$.ajax({
		type : "POST",
		url : '/JobSearch/application/status/update',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data: JSON.stringify(applicationDto),
		success: _success,
		error: _error
	})
	
	function _success(){		
		
		upateRowsApplicationStatus(applicationDto.application.applicationId,
										applicationDto.newStatus);

		applicationStatusButtons = getApplicationStatusButtonsByApplicationId(applicationDto.application.applicationId)
		

		if(applicationDto.newStatus == 0){
			removeClassFromArrayItems(applicationStatusButtons, "active");
		}
		else{
			highlightArrayItemByAttributeValue("value", applicationDto.newStatus,
					applicationStatusButtons, "active");	
		}
		

	}
	
	function _error(){
// 		alert("status error")
	}
}

function upateRowsApplicationStatus(applicationId, newStatus){
	var applicantRow = $("#applicantsTable").find("tr[data-application-id='" + applicationId + "']")[0];
	$(applicantRow).attr("data-application-status", newStatus);
}

function getApplicationStatusButtonsByApplicationId(applicationId){
	
	
	var applicantRow = $("#applicantsTable").find("tr[data-application-id='" + applicationId + "']")[0];
	
	var applicationStatusContainer = $(applicantRow).find(".application-status-container")[0];
	
	return $(applicationStatusContainer).find("button");
	
	
}


</script>



<%@ include file="../includes/Footer.jsp"%>