<%@ include file="./includes/Header.jsp"%>
<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>
<link rel="stylesheet" type="text/css" href="../static/css/employerViewJob.css" />
<link rel="stylesheet" type="text/css" href="../static/css/table.css" />
<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
</head>

<body>


	<div class="container">
	
		<div class="">${vtJobInfo }</div>


<c:choose>
	<c:when test="${questions.size() > 0 }">
		<div class="section">
			<div class="header2">
				<span data-toggle-id="questionsContainer">
					<span class="glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Questions</span>
				</span>
			</div>
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
		</div>			
	</c:when>
</c:choose>
	
	<c:if test="${not empty vtFailedWageNegotiationsByJob}">
		<div class="section">
			<div class="header2">
				<span class="header-text">Failed Wage Negotiations</span>
			</div>
			<div class="section-body">
				<div>${vtFailedWageNegotiationsByJob }</div>
			</div>
		
		</div>		
	</c:if>
	
	
		<div class="section">
			<div class="header2">
				<span data-toggle-id="applicantsContainer">
					<span class="glyphicon glyphicon-menu-up"></span>
					<span class="header-text">Applicants</span>
				</span>
			</div>
			
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
								<span data-toggle-id="selectStatusContainer" >
									<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>Status
								</span>					
								<div id="selectStatusContainer">
									
									<span id="selectStatusOK" class="glyphicon glyphicon-ok"></span>
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
								<td> ${application.applicant.rating}</td>
								<td>										
									<c:forEach items="${application.applicant.endorsements }" var="endorsement">
									
										<div class="endorsement">													
											${endorsement.categoryName } <span class="badge">  ${endorsement.count }</span>
										</div>
									</c:forEach>

								</td>	
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
<%-- 													<c:choose> --%>
<%-- 														<c:when test="${application.status == 3 }"> --%>
<!-- 														<button id="" value="3" class="active">Hire</button> -->
<%-- 														</c:when> --%>
<%-- 														<c:otherwise> --%>
<!-- 														<button id="" value="3" class="">Hire</button> -->
<%-- 														</c:otherwise> --%>
<%-- 													</c:choose>																						 --%>
										
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
		
		<div class="section">
			<div class="header2">
				<span data-toggle-id="employeesContainer">
					<span class="glyphicon glyphicon-menu-up"></span>
					<span class="header-text">Employees</span>
				</span>
			</div>
			
			<div id="employeesContainer" class="section-body">
			<c:choose>
				
				
				<c:when test="${empty employees}">
					<div>There are currently no applicants for this job</div>
				</c:when>
				
				<c:otherwise>
				
					<table id="applicantsTable" class="main-table-style">
						<thead>
							<tr>
								<th id="applicantName">Name</th>
								<th id="rating">Rating</th>
							</tr>
						</thead>
						<tbody>						
						<c:forEach items="${employees }" var="employee">
							<tr>
								<td><a href="/JobSearch/user/${employee.userId}/jobs/completed"> ${employee.firstName }</a></td>
								<td>${employee.rating }</td>
							</tr>	
						</c:forEach>						
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>
			</div>			
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
		selectAllQuestions(true);
	})
	
	$("#selectNoQuestions").click(function(){
		selectAllQuestions(false);
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
	updateApplicationStatusDto.newStatus = $(clickedStatusButton).val();
	
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

function selectAllQuestions(request){	
	$.each($("#questionListContainer").find("input[type=checkbox]"), function(){
		$(this).prop("checked", request);
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
		
		highlightArrayItemByAttributeValue("value", updateApplicationStatusDto.newStatus,
				applicationStatusButtons, "active");

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

<%@ include file="./includes/Footer.jsp"%>