<%@ include file="../includes/TagLibs.jsp" %>
				
				
<c:choose>			

	<c:when test="${empty jobDto.applicationDtos}">
		<div class="no-data">
			<p>There are currently no applicants for this job</p>
			<a class="sqr-btn teal" href="/JobSearch/job/${jobDto.job.id }/find-employees">Find Employees</a>
		</div>
	</c:when>

	<c:otherwise>
		<div id="applications-employer-view-job">
			<c:forEach items="${jobDto.applicationDtos }" var="applicationDto">
				<div class="application"></div>
			</c:forEach>								
		</div>

		<table id="applicantsTable" class="main-table-style table-view shadow
			${jobDto.job.isPartialAvailabilityAllowed ? 'has-work-days' : '' }
			${jobDto.questions.size() > 0 ? 'has-answers' : ''}">
			<thead>
				<tr>
				
					<th id="applicantName" class="left header-dropdown table-view tile-view"
					 data-filter-attr="data-application-status" data-must-match-all-filter-values="0">
					
						<span data-toggle-id="filterApplicationStatus" >
							<span class="favorite-flag glyphicon glyphicon glyphicon-star-empty"></span>						
						</span>
						<div id="filterApplicationStatus" class="dropdown-container filter-container checkbox-container">
							<div class="options">
								<label>
									<input id="" type="radio"
										name="application-status"
										data-filter-attr-value="0">All
								</label>
								<label>
									<input id="" type="radio"
										name="application-status"
										data-filter-attr-value="2">Favorites
								</label>								
							</div>
						
						</div>					

					</th>	
					
					<th id="rating" class="header-dropdown table-view tile-view"
						 data-sort-attr="data-applicant-rating">
						<span class="glyphicon glyphicon-sort" data-sort-ascending="0"
								data-sort-attr="data-applicant-rating"></span>				
					
					</th>
					<c:if test="${jobDto.questions.size() > 0 }">
						<th id="questions" class="header-dropdown table-view tile-view"
							 data-filter-attr="data-answer-option-ids-seleted"
							 data-must-match-all-filter-values="1">
							<span data-toggle-id="filterAnswersContainer" >
								<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>
							</span>					
							<div id="filterAnswersContainer" class="dropdown-container dropdown-style filter-container" >
								
								<span id="selectQuestionsOK" class="approve-filter glyphicon glyphicon-ok"></span>
								
								<table id="table_headerAnswers" class="main-table-style">
									<thead>
										<tr class="no-filter">
											<th id="filterAnswers">Display Question</th>
											<th id="header_question">Question</th>
											<th id="header_answers">Filter Answer</th>												
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${jobDto.questions }" var="question" varStatus="status">
											<tr class="no-filter">
												<td>	
													<label class="display-question">
														<input type="checkbox" ${status.first ? 'checked' : '' }
															data-question-id="${question.questionId }"
															class="show-question-and-answers">
															<span></span>
													</label>																									
												</td>
												<td class="question">${question.text }</td>
												<td class="answers answers-container">
													<div class="checkbox-container">
														<c:forEach items="${question.answerOptions }"
																var="answerOption">													
															<div>
																<label>
																	<input type="checkbox" checked
																		name="header-question-${question.questionId }"
																		data-filter-attr-value="${answerOption.answerOptionId }">
																		<span>${answerOption.text }</span>
																</label>														
															</div>																														
														</c:forEach>
													</div>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>					
						</th>						
	
					</c:if>									

					<th class="proposal teal first">
<!-- 						<p>Proposal</p> -->
						<div>
							<div class="sort-proposal-cont">
								<span>Amount</span>						
								<span class="glyphicon glyphicon-sort" data-sort-ascending="0"
								data-sort-attr="data-wage-proposal-amount"></span>
							</div>
							<c:if test="${jobDto.job.isPartialAvailabilityAllowed }">
								<div class="sort-proposal-cont">
									<span>Work Days</span>						
									<span class="glyphicon glyphicon-sort" data-sort-ascending="0"
									data-sort-attr="data-proposed-work-day-count"></span>
								</div>							
							</c:if>
							<div class="sort-proposal-cont">
								<span>Status</span>						
								<span class="glyphicon glyphicon-sort" data-sort-ascending="0"
								data-sort-attr="data-is-sent-proposal"></span>
							</div>													
						</div>	
					</th>
				</tr>
			</thead>
			<tbody class="vertical-lines">
			<c:forEach items="${jobDto.applicationDtos }" var="applicationDto">
				<tr class=""
					data-user-id="${applicationDto.application.userId }"
					data-application-status="[0,${applicationDto.application.status }]"
					data-applicant-rating="${applicationDto.applicantDto.ratingValue_overall}"
					data-application-id="${applicationDto.application.applicationId }"
					data-is-new="${applicationDto.application.isNew }"
					data-wage-proposal-amount="${applicationDto.employmentProposalDto.amount }"
					data-wage-proposal-status="${applicationDto.employmentProposalDto.status }"
					data-is-sent-proposal="${applicationDto.employmentProposalDto.proposedToUserId ==
												 applicationDto.applicantDto.user.userId ? '1' : '0'}"
					data-answer-option-ids-seleted="${applicationDto.answerOptionIds_Selected }"
					data-proposed-work-day-count="${applicationDto.dateStrings_availableWorkDays.size() }"
					>
					
					
					<td class="table-view">
						<div class="vert-border name-container">
							<span class=" favorite-flag glyphicon ${applicationDto.application.status == 2 ? 'glyphicon glyphicon-star' : 'glyphicon glyphicon-star-empty' }"></span>
							<p class="applicant-name show-applicant-ratings-mod">
							 ${applicationDto.applicantDto.user.firstName }</p>
						</div>
						<div class="ratings-mod-container">
							<div class="mod simple-header">
								<div class="mod-content">
									<div class="mod-header">
										<span class="glyphicon glyphicon-remove"></span>
									</div>
									<div class="mod-body">	
									</div>
								</div>
							</div>						
						</div>
					</td>
					<td class="table-view">
						<div class="show-applicant-ratings-mod vert-border">
							<c:choose>
								<c:when test="${empty applicationDto.applicantDto.ratingValue_overall }">
									NA
								</c:when>
								<c:otherwise>
									${applicationDto.applicantDto.ratingValue_overall}
								</c:otherwise>
							</c:choose>
						 	
						 </div>
					</td>
	
					<c:if test="${jobDto.questions.size() > 0 }">
						<td class="left table-view">
							<c:choose>
								<c:when test="${applicationDto.application.flag_employerInitiatedContact == 1 }">
									<p>You initiated contact. Applicant did not answer questions.</p>
								</c:when>
								<c:otherwise>
									<div class="vert-border">
									<c:forEach items="${applicationDto.questions }" var="question" varStatus="status_questions">
										<div data-question-id="${question.questionId }"
											class="question-container ${status_questions.first ? 'displayed' : 'not-first' }">
											<p class="question">${question.text }</p>										
											<p class="answer">
												<c:forEach items="${question.answers }" var="answer" varStatus="status">
													${answer.text}${!status.last ? ',' : '' }										
												</c:forEach>									
											</p>
										</div>
										<c:if test="${!status_questions.first && status_questions.last }">
											<span class="glyphicon glyphicon-menu-down show-all-questions"></span>
										</c:if>
									</c:forEach>
									</div>								
								</c:otherwise>
							</c:choose>
						</td>
					</c:if>	
					<td>
						<%@ include file="../wage_proposal/Proposal_Main.jsp" %>
					</td>						

				</tr>				

			</c:forEach>						
			</tbody>					
		</table>			
	</c:otherwise>			
</c:choose>	