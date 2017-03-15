	<%@ include file="../includes/Header.jsp"%>
	<%@ include file="../includes/resources/DatePicker.jsp" %>
	<%@ include file="../includes/resources/TableFilter.jsp" %>
	<%@ include file="../includes/resources/WageProposal.jsp" %>
	<%@ include file="../includes/resources/StarRatings.jsp" %>
	<%@ include file="../includes/resources/SelectPageSection.jsp" %>
	
<!-- 	<link rel="stylesheet" type="text/css" href="../static/css/employerViewJob.css" /> -->
	<link rel="stylesheet" type="text/css" href="../static/css/table.css" />
<!-- 	<link rel="stylesheet" type="text/css" href="../static/css/jobInfo.css" /> -->
<!-- 	<link rel="stylesheet" type="text/css" href="../static/css/questions.css" /> -->

	
	<link rel="stylesheet" type="text/css" href="../static/css/profile_employer/profile_employer.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/view_job_employer/applicants.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/view_job_employer/applicants_tileView.css" />
	
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/Map.js" />"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/SideBar.js" />"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/JobInfo.js" />"></script> --%>
	<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>

	
		<script src="<c:url value="/static/javascript/view_job_employer/Applicants.js" />"></script>
	
<!-- 	Currently the "WageNegotiation.js" has to be loaded AFTER the "FilterTable.js". -->
<!-- 	If it is not, then the "html" click event in the FilterTable.js will take precedence -->
<!-- 	over the "send counter offer" click event in the WageNegotiation.js -->
<!-- 	This seems hackish. -->
<!-- 	Address the logic later -->
<%-- 	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script> --%>
	
	<c:if test="${context == 'complete' }">	
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/ratings/star-rating.css" />
		<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/ratings.css" />
		<script src="<c:url value="/static/External/ratings/star-rating.js" />"></script>
		<script src="<c:url value="/static/javascript/view_job_employer/Employee_Ratings.js" />"></script>
	</c:if>
	
	<div class="select-page-section-container">	
		<%@ include file="./ContentBar_EmployerViewJob.jsp" %>	
	</div>	
	<div class="container">	
		<input id="jobId" type="hidden" value="${jobDto.job.id }">
		<input id="data_pageInit" type="hidden" value="${data_pageInit }">

		<div class="row">
		
			<c:if test="${data_pageInit != 'all-apps' && !empty data_pageInit }">
				<button id="showAllApplicants" class="sqr-btn">Show All Applicants</button>			
			</c:if>

			<div class="col-sm-12" id="sectionContainers">
				<c:if test="${context == 'waiting' }">
				<div id="applicantsContainer" class="page-section">
					<div id="applicants" class="">
						<%@ include file="./Applicants.jsp" %>
					</div>
					
<!-- 					<div id="appicants_tileView"> -->
<%-- 						<%@ include file="./Applicants_TileView.jsp" %> --%>
<!-- 					</div> -->
					
				</div>	
				</c:if>	
				
				
				<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }">
				<div id="employeesContainer" class="page-section">				
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
				
				<div id="jobInfoContainer" class="page-section">
					<%@include file="../templates/JobInformation.jsp"%>
				</div>				
				
<%-- 				<c:if test="${context == 'waiting' || context == 'in-process' || context == 'complete' }"> --%>
<!-- 				<div id="questionsContainer" class="section-container"> -->
<%-- 					<%@include file="./Questions.jsp"%>	 --%>
<!-- 				</div> -->
<%-- 				</c:if> --%>
				
			</div> <!-- end column -->

		</div>		
	</div>	

<!-- <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp;callback=initMap"> -->
<!-- </script> -->

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp">
</script>


<script type="text/javascript">

$(document).ready(function(){
	
	initPage();

// 	$("#selectQuestionsOK").click(function(){
	
// 		var questionIdsToShow = getQuestionIdsToShow();
// 		showQuestions(questionIdsToShow);
		
// 		triggerToggle("selectQuestionsContainer");
		
// 		slideUp($("#selectQuestionsContainer"));
		
// 	})
	
// 	$("#selectStatusOK").click(function(){
	
// 		var statusValuesToShow = getStatusValuesToShow();
// 		showApplicantRowsByStatus(statusValuesToShow);
		
// 		triggerToggle("selectStatusContainer");

		
// 	})
	
// 	$("#selectAllQuestions").click(function(){
// 		selectAllCheckboxes($("#questionListContainer"), true);
// 	})
	
// 	$("#selectNoQuestions").click(function(){
// 		selectAllCheckboxes($("#questionListContainer"), false);
// 	})
	
// 	$("#statusListContainer input[type=checkbox]").click(function(){
// 		$("#selectAllStatuses").prop("checked", false);
// 	})
	
// 	$("#selectAllStatuses").click(function(){
// 		selectAllCheckboxes($("#statusListContainer"), $(this).prop("checked"));
// 	})
	
	$(".application-status-container button").click(function(){
		
		
		var applicationDto = getApplicationDto_UpdateStatus(this);

		updateApplicationStatus(applicationDto);

	})
	
	$("#questionListContainer input[type='checkbox']").click(function(){
		$("#selectAllQuestions").prop("checked", false);
		$("#selectNoQuestions").prop("checked", false);
	})
	
	$("#showAllApplicants").click(function(){{

		showAllApplications();
		$(this).hide();
	}})
	
})

function showAllApplications(){
	
	var filters = [];
	var filter = {};
	
	filter.attr = "data-is-sent-proposal";
	filter.values = [];
	filter.values.push("0");
	filter.values.push("1");
	
	filters.push(filter);
	
	filterTableRows(filters, $("#applicantsTable"));
}

function initPage(){
	
	var data_initPage = $("#data_pageInit").val();

	if(data_initPage != "all-apps"){
	
		var filters = [];
		var filter = {};
		filter.values = [];
	
		switch(data_initPage){
		case "new-apps":
			filter.attr = "data-is-old";
			filter.values.push("0");
			break;
		
	// 	case "all-apps":
	// 		showApplications(true, true, false, false );
	// 		break;
		
		case "sent-proposals":
			filter.attr = "data-is-sent-proposal";
			filter.values.push("1");
			break;
		
		case "received-proposals":
			filter.attr = "data-is-sent-proposal";
			filter.values.push("0");
			break;
			
		case "received-proposals-new":
			filter.attr = "data-wage-proposal-status";
			filter.values.push("-2");
			break;		
		}
		
		
		filters.push(filter);
		filterTableRows(filters, $("#applicantsTable"));
		
	}	
}




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


// function getStatusValuesToShow(){
	
// 	var values = [];
// 	$.each($("#statusListContainer").find("input:checked"), function(){
// 		values.push($(this).val());
// 	})
	
// 	return values;	
	
	
// }


function getApplicationDto_UpdateStatus(clickedStatusButton){
	
	var applicationDto = {}
	applicationDto.application = {};
	
	applicationDto.application.applicationId = $(clickedStatusButton).closest("[data-application-id]")
																	.eq(0)
																	.attr("data-application-id");
	
	if(isAddingApplicationStatus(clickedStatusButton)){
		applicationDto.newStatus = $(clickedStatusButton).val();
	}
	else{
		applicationDto.newStatus = 0;
	}

	return applicationDto;
}

// function showApplicantRowsByStatus(statusValuesToShow){
	
// 	var applicantRows = $("#applicantsTable").find("tr[data-application-status]");
// 	var applicationStatus;
// 	$.each(applicantRows, function(){
// 		applicationStatus = $(this).attr("data-application-status");
// 		if($.inArray(applicationStatus, statusValuesToShow) > -1){
// 			$(this).show();
// 		}
// 		else{
// 			$(this).hide();
// 		}
// 	})
// }

// function showQuestions(idsToShow){
// 	var questionContainers = $("#applicantsTable").find(".question-container");
// 	var questionId;
// 	$.each(questionContainers, function(){
// 		questionId = $(this).attr("data-question-id");
// 		if($.inArray(questionId, idsToShow) > -1){
// 			$(this).show();
// 		}
// 		else{
// 			$(this).hide();
// 		}
// 	})
// }
 

// function getQuestionIdsToShow(){

// 	var ids = [];
// 	$.each($("#questionListContainer").find("input:checked"), function(){
// 		ids.push($(this).val());
// 	})
	
// 	return ids;

// }

function updateApplicationStatus(applicationDto){

	// For user experience, change the button color before the AJAX call
	// in case the AJAX call lags
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