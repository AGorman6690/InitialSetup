<%@ include file="../includes/TagLibs.jsp"%>		
		
	<div class="applicants">
		<c:forEach items="${response.applicationProgressStatuses }" var="applicationProgressStatus">
			<div class="applicant-container">
				<div class="applicant"
					data-user-id="${applicationProgressStatus.applicantId }"
					data-is-accepted="${applicationProgressStatus.application.isAccepted }"
					data-is-waiting-on-you="${applicationProgressStatus.isProposedToSessionUser }"
					data-is-expired="${applicationProgressStatus.isCurrentProposalExpired ? 1 : 0 }"
					data-is-favorite="${applicationProgressStatus.application.status == 2 ? 1 : 0 }"
					data-rating="${applicationProgressStatus.applicantRating }"
					data-proposed-amount="${applicationProgressStatus.currentProposal.amount }"
					>
					<c:if test="${applicationProgressStatus.messages.size() > 0 }">				
						<div class="messages">
							<h6>Messages</h6>
							<c:forEach items="${applicationProgressStatus.messages }" var="message">
								<p>${message }</p>
							</c:forEach>
						</div>
					</c:if>					
					<div class="applicant-header">
						<span class="hide-on-load favorite-flag glyphicon glyphicon-flag
							${applicationProgressStatus.application.status == 2 ? 'glyphicon glyphicon-star' :
								'glyphicon glyphicon-star-empty not-selected' }"></span>
						<span class="applicant-or-employee">${applicationProgressStatus.application.isAccepted == 1
							? 'Employee' : 'Applicant' }</span>
					</div>			
					<div class="applicant-name">
						<c:set var="param_userId" value="${applicationProgressStatus.applicantId }"></c:set>
						<c:set var="param_userName" value="${applicationProgressStatus.applicantName }"></c:set>
						<%@ include file="./../ratings/Template_RenderRatingsMod.jsp" %>
						<div class="show-applicant-ratings-mod-0">
							<c:choose>
								<c:when test="${empty applicationProgressStatus.applicantRating }">
	<!-- 								No Rating -->
								</c:when>
								<c:otherwise>
									<input name="input-1" class="rating-loading"
											value="${applicationProgressStatus.applicantRating }	">									
									${applicationProgressStatus.applicantRating}
								</c:otherwise>
							</c:choose>							 	
						 </div>
					</div>
					<c:if test="${applicationProgressStatus.questions.size() > 0 }">
						<div class="questions surpress hide-on-load">
							<c:choose>
								<c:when test="${applicationProgressStatus.application.flag_employerInitiatedContact == 1 }">
									<p class="question-container">You initiated contact. Applicant did not answer questions.</p>
								</c:when>
								<c:otherwise>
									
									<c:forEach items="${applicationProgressStatus.questions }"
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
									</c:forEach>
								
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>					
					<div class="proposal" data-proposal-id="${applicationProgressStatus.currentProposal.proposalId }">
						<c:set var="param_job" value="${response.job }" />
						<%@ include file="../wage_proposal/CurrentProposal.jsp" %>
					</div>	
					<div class="expiration-time">					 
						<p>${!applicationProgressStatus.isProposedToSessionUser &&
							applicationProgressStatus.application.isAccepted == 0 ?
								"Your proposal expires in " += applicationProgressStatus.time_untilEmployerApprovalExpires : ""}
						</p>
					</div>								
				</div>
			</div>
		</c:forEach>
	</div>