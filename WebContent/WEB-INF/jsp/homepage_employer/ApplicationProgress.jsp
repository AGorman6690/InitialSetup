<%@ include file="../includes/TagLibs.jsp"%>		
		
<div class="job-detail" data-job-id="${jobDto.job.id }">
	<div class="applicants">
		<c:forEach items="${response.applicationProgressStatuses }" var="applicationProgressStatus">
			<div class="applicant"
				data-user-id="${applicationProgressStatus.applicantId }"
				data-is-accepted="${applicationProgressStatus.application.isAccepted }"
				data-is-waiting-on-you="${applicationProgressStatus.isProposedToSessionUser }"
				data-is-expired="${applicationProgressStatus.isCurrentProposalExpired ? 1 : 0 }"
				data-is-favorite="${applicationProgressStatus.application.status == 2 ? 1 : 0 }"
				data-rating="${applicationProgressStatus.applicantRating }"
				data-proposed-amount="${applicationProgressStatus.currentProposal.amount }"
				>

				<div class="status surpress">
					<span class=" favorite-flag glyphicon glyphicon-flag
						${applicationProgressStatus.application.status == 2 ? 'glyphicon glyphicon-star' :
							'glyphicon glyphicon-star-empty not-selected' }">
					</span>
					<p class="applicant-or-employee">${applicationProgressStatus.application.isAccepted == 1
						? 'Employee' : 'Applicant' }</p>
					<p class="waiting-status ${applicationProgressStatus.isProposedToSessionUser &&
									applicationProgressStatus.application.isAccepted == 0
											 ? '' : '' }">${applicationProgressStatus.currentProposalStatus }
											 
					 </p>
					 <h3>					 
						 ${!applicationProgressStatus.isProposedToSessionUser &&
								applicationProgressStatus.application.isAccepted == 0 ?
									"Your proposal expires in " += applicationProgressStatus.time_untilEmployerApprovalExpires : ""}
					 </h3>
					 

				</div>
				<div>

					<div class="center">
<!-- 						<p class="applicant-name show-applicant-ratings-mod linky-hover"> -->
<%-- 							 ${userDto.user.firstName }</p>						 --%>
						<%@ include file="./../ratings/Template_RenderRatingsMod.jsp" %>
						<div class="show-applicant-ratings-mod-0">
							<c:choose>
								<c:when test="${empty applicationProgressStatus.applicantRating }">
									No Rating
								</c:when>
								<c:otherwise>
									<input name="input-1" class="rating-loading"
											value="${applicationProgressStatus.applicantRating }	">									
									${applicationProgressStatus.applicantRating}
								</c:otherwise>
							</c:choose>							 	
						 </div>
<!-- 						<div class="ratings-mod-container"> -->
<!-- 							<div class="mod simple-header"> -->
<!-- 								<div class="mod-content"> -->
<!-- 									<div class="mod-header"> -->
<!-- 									</div> -->
<!-- 									<div class="mod-body">	 -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</div>						 -->
<!-- 						</div> -->
					</div>
								
				</div>
				<c:if test="${applicationProgressStatus.questions.size() > 0 }">
					<div class="questions surpress">
						<c:choose>
							<c:when test="${applicationProgressStatus.application.flag_employerInitiatedContact == 1 }">
								<p>You initiated contact. Applicant did not answer questions.</p>
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
				<div>
					<div class="proposal" data-application-id="${applicationProgressStatus.application.applicationId }">
						<c:set var="applicationDto" value="${userDto.applicationDto }" />
						<c:set var="jobDto" value="${userDto.applicationDto.jobDto }" />
						<%@ include file="../wage_proposal/CurrentProposal.jsp" %>
					</div>
				</div>
				
			</div>
		</c:forEach>
	</div>
</div>