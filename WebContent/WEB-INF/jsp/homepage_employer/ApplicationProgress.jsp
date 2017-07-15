<%@ include file="../includes/TagLibs.jsp"%>		
		
<div class="job-detail" data-job-id="${jobDto.job.id }">
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

				<div class="status surpress">
					<span class=" favorite-flag glyphicon glyphicon-flag
						${applicationDto.application.status == 2 ? 'glyphicon glyphicon-star' :
							'glyphicon glyphicon-star-empty not-selected' }">
					</span>
					<p class="applicant-or-employee">${userDto.applicationDto.application.isAccepted == 1
						? 'Employee' : 'Applicant' }</p>
					<p class="waiting-status ${userDto.applicationDto.employmentProposalDto.isProposedToSessionUser &&
									userDto.applicationDto.application.isAccepted == 0
											 ? '' : '' }">${userDto.applicationDto.currentProposalStatus }
											 
					 </p>
					 <h3>					 
						 ${!userDto.applicationDto.employmentProposalDto.isProposedToSessionUser &&
								userDto.applicationDto.application.isAccepted == 0 ?
									"Your proposal expires in " += userDto.applicationDto.time_untilEmployerApprovalExpires : ""}
					 </h3>
					 

				</div>
				<div>

					<div class="center">
<!-- 						<p class="applicant-name show-applicant-ratings-mod linky-hover"> -->
<%-- 							 ${userDto.user.firstName }</p>						 --%>
						<%@ include file="./../ratings/Template_RenderRatingsMod.jsp" %>
						<div class="show-applicant-ratings-mod-0">
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
				<c:if test="${userDto.applicationDto.questions.size() > 0 }">
					<div class="questions surpress">
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
								</c:forEach>
							
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>					
				<div>
					<div class="proposal" data-application-id="${userDto.applicationDto.application.applicationId }">
						<c:set var="applicationDto" value="${userDto.applicationDto }" />
						<c:set var="jobDto" value="${userDto.applicationDto.jobDto }" />
						<%@ include file="../wage_proposal/CurrentProposal.jsp" %>
					</div>
				</div>
				
			</div>
		</c:forEach>
	</div>
</div>