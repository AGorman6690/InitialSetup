<%@ include file="../includes/TagLibs.jsp"%>		
		
<div class="job-detail" data-job-id="${jobDto.job.id }">
	<div class="sort-and-filter-wrapper">
		<div class="item">
			<input type="checkbox" id="employees-${jobDto.job.id }" checked>
			<label for="employees-${jobDto.job.id }">Employees</label>
		</div>	
		<div class="item">
			<input type="checkbox" id="applicants-${jobDto.job.id }" checked>
			<label for="applicants-${jobDto.job.id }">Applicants</label>
			<div class="sub-items">
				<div>
					<input type="checkbox" id="waiting-on-you-${jobDto.job.id }" checked>
					<label for="waiting-on-you-${jobDto.job.id }">Proposals waiting on you</label>
				</div>
				<div>
					<input type="checkbox" id="waiting-on-applicant-${jobDto.job.id }" checked>
					<label for="waiting-on-applicant-${jobDto.job.id }">Proposals waiting on applicant</label>
				</div>
				<div>
					<input type="checkbox" id="expired-proposals-${jobDto.job.id }" checked>
					<label for="expired-proposals-${jobDto.job.id }">Expired proposals</label>
				</div>					
			</div>
		</div>				
		<div class="item">
			<input type="checkbox" id="favorites-${jobDto.job.id }">
			<label for="favorites-${jobDto.job.id }">Favorites</label>
		</div>	
		<div class="item sort">
			<p><span>Rating</span><span class="glyphicon glyphicon-arrow-down"></span>
				<span class="glyphicon glyphicon-arrow-up"></span></p>
		</div>	
		<div class="item sort">
			<p><span>Proposed wage</span><span class="glyphicon glyphicon-arrow-down"></span>
				<span class="glyphicon glyphicon-arrow-up"></span></p>
		</div>								
	</div>
	<div class="applicants">
		<c:forEach items="${jobDto.userDtos_applicants }" var="userDto">
			<div class="applicant"
				data-user-id="${userDto.user.userId }"
				data-is-accepted="${userDto.applicationDto.application.isAccepted }"
				data-is-waiting-on-you="${userDto.applicationDto.employmentProposalDto.isProposedToSessionUser }"
				data-is-expired="${userDto.applicationDto.employmentProposalDto.isExpired ? 1 : 0 }"
				data-is-favorite="${userDto.applicationDto.application.status == 2 ? 1 : 0 }"
				data-rating="${userDto.ratingValue_overall }"
				data-proposed-amount="${userDto.applicationDto.employmentProposalDto.amount }"
				>
				<div class="status">
					<h2 class="${userDto.applicationDto.employmentProposalDto.isProposedToSessionUser &&
									userDto.applicationDto.application.isAccepted == 0
											 ? 'warning-message' : '' }">${userDto.applicationDto.currentProposalStatus }
											 
					 </h2>
					 <h3>					 
						 ${!userDto.applicationDto.employmentProposalDto.isProposedToSessionUser &&
								userDto.applicationDto.application.isAccepted == 0 ?
									"Your proposal expires in " += userDto.applicationDto.time_untilEmployerApprovalExpires : ""}
					 </h3>
				</div>
				<div>
					<span class=" favorite-flag glyphicon glyphicon-flag
						${applicationDto.application.status == 2 ? 'glyphicon glyphicon-star' :
							'glyphicon glyphicon-star-empty not-selected' }">
					</span>
					<div>
<%-- 						<span class="applicant-name">${userDto.user.firstName }</span> --%>
						<p class="applicant-name show-applicant-ratings-mod linky-hover">
							 ${userDto.user.firstName }</p>						
						<div class="show-applicant-ratings-mod">
							<input name="input-1" class="rating-loading"
											value="4.2"><span class="rating-value-overall">4.2</span>
							<c:choose>
								<c:when test="${empty userDto.ratingValue_overall }">
									No Rating
								</c:when>
								<c:otherwise>
									<input name="input-1" class="rating-loading"
											value="${userDto.ratingValue_overall }	">									
									${userDto.ratingValue_overall}
								</c:otherwise>
							</c:choose>							 	
						 </div>
						<div class="ratings-mod-container">
							<div class="mod simple-header">
								<div class="mod-content">
									<div class="mod-header">
									</div>
									<div class="mod-body">	
									</div>
								</div>
							</div>						
						</div>
					</div>
								
				</div>
				<c:if test="${userDto.applicationDto.questions.size() > 0 }">
					<div class="questions">
						<c:choose>
							<c:when test="${userDto.applicationDto.application.flag_employerInitiatedContact == 1 }">
								<p>You initiated contact. Applicant did not answer questions.</p>
							</c:when>
							<c:otherwise>
								
								<c:forEach items="${userDto.applicationDto.questions }"
									 var="question" varStatus="status_questions">
									<div data-question-id="${question.questionId }"
										class="question-container ${status_questions.first ? 'displayed' : 'not-first' }">
										<p class="question">${question.text }</p>										
										<p class="answer">
											<c:forEach items="${question.answers }" var="answer" varStatus="status">
												${answer.text}${!status.last ? ',' : '' }										
											</c:forEach>									
										</p>
									</div>
									<c:if test="${!status_questions.first && status_questions.last && applicationDto.questions.size() > 0 }">
										<span data-all-are-shown="0" class="linky-hover show-all-questions">See all</span>
									</c:if>
								</c:forEach>
							
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>					
				<div>
					<div class="proposal" data-application-id="${userDto.applicationDto.application.applicationId }">
<%-- 						<c:set var="jobDto" value="${applicationDto.jobDto }" /> --%>
						<c:set var="applicationDto" value="${userDto.applicationDto }" />
						<%@ include file="../wage_proposal/CurrentProposal.jsp" %>
						<div class="render-present-proposal-mod"></div>
	<%-- 									<%@ include file="../wage_proposal/Proposal_Main.jsp" %> --%>
					</div>
				</div>
				
			</div>
		</c:forEach>
	</div>
</div>