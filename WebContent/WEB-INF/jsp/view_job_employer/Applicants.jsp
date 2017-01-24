<%@ include file="../includes/TagLibs.jsp" %>
				
				
<c:choose>			

	<c:when test="${empty jobDto.applications}">
		<div>There are currently no applicants for this job</div>
	</c:when>

	<c:otherwise>
		<table id="applicantsTable" class="main-table-style">
			<thead>
				<tr>
					<th id="" class="" colspan="1"></th>
					<th id="" class="span" colspan="2">Wage Proposal</th>
					<th id="" class="" colspan="1"></th>
					<th id="" class="" colspan="1"></th>
					<th id="" class="" colspan="1"></th>
					<th id="" class="" colspan="1"></th>
					<th id="" class="" colspan="1"></th>
				</tr>
			
				<tr>
					<th id="applicantName">Name</th>
					<th id="wageNegotiation_status">Status</th>
					<th id="wageNegotiation_current_offer">Current Offer</th>
					<th id="rating">Rating</th>
					<th id="endorsements">Endorsements</th>
				<c:if test="${jobDto.questions.size() > 0 }">
					<th id="questions" class="left">
					<span data-toggle-id="selectQuestionsContainer" >
						<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>Answers
					</span>					
						<div id="selectQuestionsContainer" >
							
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
							<c:forEach items="${jobDto.questions }" var="question">
								<div class="checkbox">
									<label><input type="checkbox" name="questions-select" value="${question.questionId }">${question.text }</label> 
								</div>
							</c:forEach>
							</div>
						</div>					
					</th>
				</c:if>							
					<th id="status" class="left">
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
			<c:forEach items="${jobDto.applications }" var="application">
				<tr class="" data-application-status="${application.status }"
					data-application-id="${application.applicationId }">
					<td><a class="accent" href="/JobSearch/job/${jobDto.job.id }/user/${application.applicant.userId}/jobs/completed"> ${application.applicant.firstName }</a></td>
					
					<td>
						<c:choose>
							<c:when test="${application.currentWageProposal.status == 1 }">
							<!-- ****** If the current wage proposal has been accepted-->
								<div><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/> has been accepted</div>
							</c:when>						
							<c:when test="${application.currentWageProposal.proposedToUserId != application.applicant.userId }">
								<c:set var="param_is_employer" value="1" />
								<c:set var="param_wage_proposal" value="${application.currentWageProposal }" />
								<%@ include file="../templates/WageNegotiation.jsp" %>												
							</c:when>
							<c:otherwise>					
								<div class="offer-context">
									Waiting for applicant															
								</div>									
							</c:otherwise>
						</c:choose>
					</td>									
					<td>$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${application.currentWageProposal.amount}"/></td>
					<td> ${application.applicant.rating}</td>
					<td>										
						<c:forEach items="${application.applicant.endorsements }" var="endorsement">
						
							<div class="endorsement">													
								${endorsement.categoryName } <span class="badge">  ${endorsement.count }</span>
							</div>
						</c:forEach>
	
					</td>	
				<c:if test="${jobDto.questions.size() > 0 }">
					<td class="left">
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
					<td class="">
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