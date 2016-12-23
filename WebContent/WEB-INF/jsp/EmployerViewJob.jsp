	<%@ include file="./includes/Header.jsp"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	
	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
	<link rel="stylesheet" type="text/css" href="../static/css/employerViewJob.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/wageNegotiation.css" />
	<link rel="stylesheet" type="text/css" href="../static/css/jobInfo.css" />
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Map.js" />"></script>
	<script src="<c:url value="/static/javascript/SideBar.js" />"></script>

</head>

<body>


	<div class="container">
	
	
		<div class="row">
			<div id="sideBarContainer" class="col-sm-2">
				<div id="applicants" class="first side-bar selected-blue"
					 data-section-id="applicantsContainer">Applicants</div>
				<div id="" class="side-bar" data-section-id="employeesContainer">Employees</div>
				<div id="jobInfo" class="side-bar" data-section-id="jobInfoContainer">Job Information</div>				
				<div id="questionInfo" class="side-bar" data-section-id="questionsContainer">Questions</div>
			</div>
			
			<div class="col-sm-10" id="sectionContainers">
				<div id="applicantsContainer" class="section-container">

				<h4>Applicants</h4>
					
				<div id="applicantsContainer" class="section-body">
				<c:choose>
					
					
					<c:when test="${empty applications}">
						<div>There are currently no applicants for this job</div>
					</c:when>
					
					<c:otherwise>
					
						<table id="applicantsTable" class="main-table-style">
							<thead>
								<tr>
									<th id="applicantName">Name</th>
									<th id="wageNegotiation">Wage Negotiation</th>
									<th id="rating">Rating</th>
									<th id="endorsements">Endorsements</th>
								<c:if test="${questions.size() > 0 }">
									<th id="questions">
									<span data-toggle-id="selectQuestionsContainer" >
										<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>Answers
									</span>					
										<div id="selectQuestionsContainer">
											
											<span id="selectQuestionsOK" class="glyphicon glyphicon-ok"></span>
											<div id="questionsAllOrNoneContainer">
												<div class="radio">
												  <label><input id="selectAllQuestions" type="radio" name="questions-all-or-none">All</label>
												</div>
												<div class="radio">
												  <label><input id="selectNoQuestions" type="radio" name="questions-all-or-none">None</label>
												</div>								
											</div>
											<div id="questionListContainer">
											<c:forEach items="${questions }" var="question">
												<div class="checkbox">
													<label><input type="checkbox" name="questions-select" value="${question.questionId }">${question.text }</label> 
												</div>
											</c:forEach>
											</div>
										</div>
									
															
									</th>
								</c:if>							
									<th id="status">
										<span data-toggle-id="selectStatusContainer" data-toggle-speed="2">
											<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>Status
										</span>					
										<div id="selectStatusContainer">
											
											<span id="selectStatusOK" class="glyphicon glyphicon-ok"></span>
											<div id="statusAllContainer">
												<div class="checkbox">
												  <label><input id="selectAllStatuses" type="checkbox" name="statuses-all">All</label>
												</div>								
											</div>									
											<div id="statusListContainer">
												<div class="checkbox">
												  <label><input id="selectStatusSubmitted" type="checkbox" 
												  		name="status-select" value="0">No Action Taken</label>
												</div>									
												<div class="checkbox">
												  <label><input id="selectStatusDeclined" type="checkbox" 
												  		name="status-select" value="1">Declined</label>
												</div>
												<div class="checkbox">
												  <label><input id="selectStatusConsidering" type="checkbox"
												  		name="status-select" value="2">Considering</label>
												</div>
											</div>
										</div>
									</th>
								</tr>
							</thead>
								<tbody>
								<c:forEach items="${applications }" var="application">
									<tr class="" data-application-status="${application.status }"
										data-application-id="${application.applicationId }">
										<td><a class="accent" href="/JobSearch/user/${application.applicant.userId}/jobs/completed"> ${application.applicant.firstName }</a></td>
										
										<td>
											<c:choose>
												<c:when test="${application.currentWageProposal.status == 1 }">
												<!-- ****** If the current wage proposal has been accepted-->
													<div><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/> has been accepted</div>
												</c:when>						
												<c:when test="${application.currentWageProposal.proposedToUserId != application.applicant.userId }">
					<!-- 						******* If applicant has made the last wage proposal -->
													<div id="${application.currentWageProposal.id}" class="counter-offer-container">
														
														<div class="offer-context">
															Applicant asking for 
															<span id="amount">
																<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
															</span>
														</div>									
														<c:set var="toggleId" value="${ application.currentWageProposal.id}-toggle-id" />
														<span class="glyphicon glyphicon-menu-hamburger" data-toggle-id="${ toggleId}" data-toggle-speed="1"></span>														
														<div id="${ toggleId}" class="counter-offer-response">
															<button class="accept-counter">Hire</button>
															<button class="re-counter">Counter</button>		
															<button class="decline-counter">Decline</button>							
															<div class="re-counter-amount-container">
																<input class="re-counter-amount"></input>
																<button class="send-counter-offer">Send</button>
																<button class="cancel-counter-offer">Cancel</button>
															</div>										
														</div>
													</div>
													<div class="sent-response-notification"></div>	
												</c:when>
												<c:otherwise>
					<!-- 						******* Otherwise the employer has made the last wage proposal -->							
														<div class="offer-context">
															You offered   
															<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
														</div>									
												</c:otherwise>
											</c:choose>
										</td>									
										
										<td> ${application.applicant.rating}</td>
										<td>										
											<c:forEach items="${application.applicant.endorsements }" var="endorsement">
											
												<div class="endorsement">													
													${endorsement.categoryName } <span class="badge">  ${endorsement.count }</span>
												</div>
											</c:forEach>
		
										</td>	
									<c:if test="${questions.size() > 0 }">
										<td>
										<c:forEach items="${application.questions }" var="question">
											<div data-question-id="${question.questionId }" class="question-container">
												<div class="question">${question.text }</div>										
												<div class="answer">
													<c:set var="answerCount" value="${question.answers.size() }"></c:set>
													<c:set var="i" value="${0 }"></c:set>
													<c:forEach items="${question.answers }" var="answer">
														${answer.text}<c:if test="${i < answerCount - 1 }">,</c:if>											
														<c:set var="i" value="${i +1 }"></c:set>
													</c:forEach>
													
												</div>
											</div>
										</c:forEach>
										</td>
									</c:if>			
		
		<!-- 								Application Status						 -->
										<td>
											<div class="application-status-container">
												<c:choose>
													<c:when test="${application.status == 1 }">
													<button id="" value="1" class="active">Decline</button>
													</c:when>
													<c:otherwise>
													<button id="" value="1" class="">Decline</button>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${application.status == 2 }">
													<button id="" value="2" class="active">Consider</button>
													</c:when>
													<c:otherwise>
													<button id="" value="2" class="">Consider</button>
													</c:otherwise>
												</c:choose>										
											</div>
										</td>
									</tr>
								</c:forEach>						
								</tbody>					
							</table>			
						</c:otherwise>			
					</c:choose>
					</div>
				</div> <!-- end applicants -->				

				
				<div id="employeesContainer" class="section-container">
		
					<h4>Employees</h4>
					<div id="employees" class="section-body">
					<c:choose>
						
						
						<c:when test="${empty employees}">
							<div>There are currently no employees for this job</div>
						</c:when>
						
						<c:otherwise>
						
							<table id="employeesTable" class="main-table-style">
								<thead>
									<tr>
										<th id="employeeName">Name</th>
										<th id="wage">Wage</th>
										<th id="employeerating">Rating</th>
									</tr>
								</thead>
								<tbody>						
								<c:forEach items="${employees }" var="employee">
									<tr>
										<td><a class="accent" href="/JobSearch/user/${employee.userId}/jobs/completed"> ${employee.firstName }</a></td>
										<td>${employee.wage }</td>
										<td>${employee.rating }</td>
									</tr>	
								</c:forEach>						
								</tbody>
							</table>
						</c:otherwise>
					</c:choose>
					</div>			
				</div>	
				
			
				<div id="jobInfoContainer" class="section-container">		
<%-- 					<div class="">${vtJobInfo }</div> --%>
					
				</div>
				
				<div id="questionsContainer" class="section-container">
					<c:choose>
						<c:when test="${questions.size() > 0 }">
							<h4>Questions</h4>
							<div id="questionsContainer" class="section-body">
								<c:forEach items="${questions }" var="question">
									<div class="question-container">
										${question.text }
										<div class="answer-container">
											<div class="question-format">
											<c:choose>
												<c:when test="${question.formatId == 0}">
													(Yes or No)
												</c:when>
												<c:when test="${question.formatId == 1}">
													(Short Answer)
												</c:when>
												<c:when test="${question.formatId == 2}">
													(Select One Answer)
												</c:when>
												<c:when test="${question.formatId == 3}">
													(Select One or Multiple Answers)
												</c:when>
											</c:choose>
											</div>
											<c:choose>
												<c:when test="${question.formatId == 2 || question.formatId == 3}">
													<div class="answer-options-container">
													<c:forEach items="${question.answerOptions }" var="answerOption">
														<div>${answerOption.text }</div>
													</c:forEach>
													</div>
												</c:when>
											</c:choose>
										</div>
									</div>
								</c:forEach>
							</div>						
						</c:when>
					</c:choose>
				</div>

				
			</div> <!-- end column -->


	
	<c:if test="${not empty vtFailedWageNegotiationsByJob}">
		<div class="section">
			<div class="header2">
				<span data-toggle-id="failedWageNegotiationsContainer" data-toggle-speed="1">
					<span class="glyphicon glyphicon-menu-up"></span>
					<span class="header-text">Failed Wage Negotiations</span>
				</span>
			</div>
			<div id="failedWageNegotiationsContainer" class="section-body">
				<div>${vtFailedWageNegotiationsByJob }</div>
			</div>
		
		</div>		
	</c:if>
	
	

		</div>
		
	</div>
	
</body>


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
		
		
		var updateApplicationStatusDto = getUpdateApplicationStatusDto(this);

		updateApplicationStatus(updateApplicationStatusDto);

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


function getUpdateApplicationStatusDto(clickedStatusButton){
	
	var updateApplicationStatusDto = {}
	
	updateApplicationStatusDto.applicationId = $($(clickedStatusButton).closest("[data-application-id]")[0]).attr("data-application-id");
	
	if(isAddingApplicationStatus(clickedStatusButton)){
		updateApplicationStatusDto.newStatus = $(clickedStatusButton).val();
	}
	else{
		updateApplicationStatusDto.newStatus = 0;
	}

	return updateApplicationStatusDto;
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

function updateApplicationStatus(updateApplicationStatusDto){

	
	$.ajax({
		type : "POST",
		url : '/JobSearch/application/status/update',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data: JSON.stringify(updateApplicationStatusDto),
		success: _success,
		error: _error
	})
	
	function _success(){		
		
		upateRowsApplicationStatus(updateApplicationStatusDto.applicationId,
									updateApplicationStatusDto.newStatus);

		applicationStatusButtons = getApplicationStatusButtonsByApplicationId(updateApplicationStatusDto.applicationId)
		

		if(updateApplicationStatusDto.newStatus == 0){
			removeClassFromArrayItems(applicationStatusButtons, "active");
		}
		else{
			highlightArrayItemByAttributeValue("value", updateApplicationStatusDto.newStatus,
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

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
</script>

<%@ include file="./includes/Footer.jsp"%>