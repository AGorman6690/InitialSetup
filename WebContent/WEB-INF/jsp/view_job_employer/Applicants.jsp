<%@ include file="../includes/TagLibs.jsp" %>
				
				
<c:choose>			

	<c:when test="${empty jobDto.applicationDtos}">
		<div class="no-data">There are currently no applicants for this job</div>
	</c:when>

	<c:otherwise>
		<div id="applications-employer-view-job">
			<c:forEach items="${jobDto.applicationDtos }" var="applicationDto">
				<div class="application"></div>
			</c:forEach>								
		</div>


		<div id="changeLayout_applicants">
<!-- 			<span id="tileView_applicants" class="glyphicon glyphicon-th-large"></span> -->
<!-- 			<span id="tableView_applicants" class="glyphicon glyphicon-th-list"></span> -->
			<span id="tableView_applicants">Table View</span>
			<span id="tileView_applicants">Tile View</span>
		</div>
	
		<table id="applicantsTable" class="main-table-style table-view shadow
			${jobDto.job.isPartialAvailabilityAllowed ? 'has-work-days' : '' }
			${jobDto.questions.size() > 0 ? 'has-answers' : ''}">
			<thead>
<!-- 				<tr> -->
<!-- 					<th id="" class="table-view" colspan="1"></th> -->
<%-- 					<th id="" class="table-view span tile-view" colspan="${jobDto.job.isPartialAvailabilityAllowed ? 3 : 2 }">Proposal</th> --%>
<!-- 					<th id="" class="table-view tile-view" colspan="1"></th> -->
<%-- 				<c:if test="${jobDto.questions.size() > 0 }"> --%>
<!-- 					<th id="" class="table-view" colspan="1"></th> -->
<%-- 				</c:if> --%>
<!-- 					<th id="" class="table-view tile-view" colspan="1"></th> -->
<!-- 				</tr> -->
			
				<tr>
				
					<th id="applicantName" class="left header-dropdown table-view tile-view"
					 data-filter-attr="data-application-status" data-must-match-all-filter-values="0">
					
						<span data-toggle-id="filterApplicationStatus" >
							Name<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="filterApplicationStatus" class="dropdown-container filter-container checkbox-container">
<!-- 							<span class="approve-filter glyphicon glyphicon-ok"></span> -->
							
<!-- 							<label class="select-all-container"> -->
<!-- 								<input id="filterOption_wageProposal_selectAll" class="select-all" -->
<!-- 									type="checkbox" name="wage-prpoposal-status" -->
<!-- 									>Select All -->
<!-- 							</label> -->
							<div class="options">
<!-- 								<label> -->
<!-- 									<input id="" type="checkbox" -->
<!-- 										name="application-status" -->
<!-- 										data-filter-attr-value="0">No action taken -->
<!-- 								</label> -->
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
					
						<span data-toggle-id="sortApplicantRating" >
							Rating<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="sortApplicantRating" class="dropdown-container sort-container">
<!-- 							<span class="approve-sort glyphicon glyphicon-ok"></span> -->
							
							<label>
								<input id="" type="radio"
									name="applicant-rating"
									data-sort-ascending="0">High to Low
							</label>
							<label>
								<input id="" type="radio"
									name="applicant-rating"
									data-sort-ascending="1">Low to High
							</label>					
						</div>					
					
					</th>
					<c:if test="${jobDto.questions.size() > 0 }">
						<th id="questions" class="header-dropdown table-view tile-view"
							 data-filter-attr="data-answer-option-ids-seleted"
							 data-must-match-all-filter-values="1">
							<span data-toggle-id="filterAnswersContainer" >
								Answers<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>
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
	<!-- 												<label> -->
	<!-- 													<input type="checkbox" checked -->
	<%-- 														name="show-question-${question.questionId }" --%>
	<!-- 														class="filter-answers"> -->
	<!-- 														<span>Filter Answers</span> -->
	<!-- 												</label> -->
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

					<th class="proposal teal first">Proposal</th>
<!-- 					<th id="wageNegotiation_status" class="header-dropdown table-view tile-view" data-filter-attr="data-is-sent-proposal"> -->
<!-- 						<span data-toggle-id="filterWageProposalStatus" > -->
<!-- 							Status<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 						</span> -->
<!-- 						<div id="filterWageProposalStatus" class="dropdown-container filter-container checkbox-container"> -->
<!-- 							<span class="approve-filter glyphicon glyphicon-ok"></span> -->
							
<!-- 							<label class="select-all-container"> -->
<!-- 								<input id="filterOption_wageProposal_selectAll" class="select-all" -->
<!-- 									type="checkbox" name="wage-prpoposal-status" -->
<!-- 									>Select All -->
<!-- 							</label> -->
<!-- 							<div class="options"> -->
<!-- 								<label> -->
<!-- 									<input id="" type="checkbox" -->
<!-- 										name="wage-prpoposal-status" -->
<!-- 										data-filter-attr-value="1">Waiting for applicant -->
<!-- 								</label> -->
<!-- 								<label> -->
<!-- 									<input id="" type="checkbox" -->
<!-- 										name="wage-prpoposal-status" -->
<!-- 										data-filter-attr-value="0">Waiting for you -->
<!-- 								</label> -->
<!-- 							</div> -->
						
<!-- 						</div> -->
<!-- 					</th> -->
<!-- 					<th id="wageNegotiation_current_offer" class="header-dropdown table-view tile-view" data-sort-attr="data-wage-proposal-amount"> -->
					
<!-- 						<span data-toggle-id="sortWageProposalOffer" > -->
<!-- 							Wage<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 						</span> -->
<!-- 						<div id="sortWageProposalOffer" class="dropdown-container sort-container"> -->
<!-- <!-- 							<span class="approve-sort glyphicon glyphicon-ok"></span> --> 
							
<!-- 							<label> -->
<!-- 								<input id="" type="radio" -->
<!-- 									name="wage-prpoposal-current-offer" -->
<!-- 									data-sort-ascending="0">High to Low -->
<!-- 							</label> -->
<!-- 							<label> -->
<!-- 								<input id="" type="radio" -->
<!-- 									name="wage-prpoposal-current-offer" -->
<!-- 									data-sort-ascending="1">Low to High -->
<!-- 							</label>					 -->
<!-- 						</div>					 -->
					
					
<!-- 					</th> -->
<%-- 				<c:if test="${jobDto.job.isPartialAvailabilityAllowed }"> --%>

<!-- 					<th class="header-dropdown table-view tile-view" -->
<!-- 						 data-sort-attr="data-proposed-work-day-count"> -->
					
<!-- 						<span data-toggle-id="sortProposedWorkDayCount" > -->
<!-- 							Work Days<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 						</span> -->
<!-- 						<div id="sortProposedWorkDayCount" class="dropdown-container sort-container">		 -->
<!-- 							<label> -->
<!-- 								<input type="radio"	name="proposed-work-day-count" -->
<!-- 									data-sort-ascending="0">High to Low -->
<!-- 							</label> -->
<!-- 							<label> -->
<!-- 								<input type="radio"	name="proposed-work-day-count" -->
<!-- 									data-sort-ascending="1">Low to High -->
<!-- 							</label>					 -->
<!-- 						</div>					 -->
					
<!-- 					</th>					 -->
<%-- 				</c:if> --%>
											

				</tr>
			</thead>
			<tbody class="vertical-lines">
			<c:forEach items="${jobDto.applicationDtos }" var="applicationDto">
				<tr class=""
					data-application-status="[0,${applicationDto.application.status }]"
					data-applicant-rating="${applicationDto.applicantDto.ratingValue_overall}"
					data-application-id="${applicationDto.application.applicationId }"
					data-is-old="${applicationDto.application.hasBeenViewed }"
					data-wage-proposal-amount="${applicationDto.employmentProposalDto.amount }"
					data-wage-proposal-status="${applicationDto.employmentProposalDto.status }"
					data-is-sent-proposal="${applicationDto.employmentProposalDto.proposedToUserId ==
												 applicationDto.applicantDto.user.userId ? '1' : '0'}"
					data-answer-option-ids-seleted="${applicationDto.answerOptionIds_Selected }"
					data-proposed-work-day-count="${applicationDto.dateStrings_availableWorkDays.size() }"
					>
					
					<td class="table-view">
						<div class="vert-border name-container">
							<span class="favorite-flag glyphicon ${applicationDto.application.status == 2 ? 'glyphicon glyphicon-star' : 'glyphicon glyphicon-star-empty' }"></span>
							<a class="accent" href="/JobSearch/user/${applicationDto.applicantDto.user.userId}/profile">
										 ${applicationDto.applicantDto.user.firstName }</a>
						</div>
					</td>
					<td class="table-view">
						<div class="vert-border">
						 	${applicationDto.applicantDto.ratingValue_overall}
						 </div>
					</td>
	
					<c:if test="${jobDto.questions.size() > 0 }">
						<td class="left table-view">
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
						</td>
					</c:if>							

	
					<td class="tile-view" colspan="99">
						<div class="image-container">
							<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">
						</div>
						<div class="info-container">
							<div>
								<p class="name">
									<a class="accent" href="/JobSearch/user/${applicationDto.applicantDto.user.userId}/profile">
											 ${applicationDto.applicantDto.user.firstName }</a>
								</p>
								<p>
									<input name="input-1" class="rating-loading"
											value="${applicationDto.applicantDto.ratingValue_overall }	">
									${applicationDto.applicantDto.ratingValue_overall }						
								</p>
							</div>
							<div class="proposal-container">
								<h3>Proposal</h3>
								<p>$ ${applicationDto.employmentProposalDto.amount }</p>
								<p>${fn:length(applicationDto.dateStrings_availableWorkDays) } of ${fn:length(applicationDto.jobDto.workDays) } days</p>
							</div>
							<div class="answer-container">
								<c:forEach items="${applicationDto.questions }" var="question">
									<p>${question.text }</p>
									<p>
										<c:forEach items="${question.answers }" var="answer" varStatus="status">
											${answer.text}${!status.last ? ',' : ''}			
										</c:forEach>
									</p>									
								</c:forEach>
								
							</div>
						</div>
					</td>
					<td>
						<%@ include file="../wage_proposal/Proposal_Main.jsp" %>
					</td>
					
				</tr>				

			</c:forEach>						
			</tbody>					
		</table>			
	</c:otherwise>			
</c:choose>	