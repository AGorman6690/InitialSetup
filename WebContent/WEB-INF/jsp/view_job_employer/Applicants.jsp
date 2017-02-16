<%@ include file="../includes/TagLibs.jsp" %>
				
				
<c:choose>			

	<c:when test="${empty jobDto.applications}">
		<div class="no-data">There are currently no applicants for this job</div>
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
					<th id="wageNegotiation_status" class="header-dropdown" data-filter-attr="data-is-sent-proposal">
						<span data-toggle-id="filterWageProposalStatus" >
							<span class="glyphicon glyphicon-menu-down"></span>Status
						</span>
						<div id="filterWageProposalStatus" class="dropdown-container filter-container checkbox-container">
							<span class="approve-filter glyphicon glyphicon-ok"></span>
							
							<label class="select-all-container">
								<input id="filterOption_wageProposal_selectAll" class="select-all"
									type="checkbox" name="wage-prpoposal-status"
									>Select All
							</label>
							<div class="options">
								<label>
									<input id="" type="checkbox"
										name="wage-prpoposal-status"
										data-filter-attr-value="1">Sent
								</label>
								<label>
									<input id="" type="checkbox"
										name="wage-prpoposal-status"
										data-filter-attr-value="0">Received
								</label>
							</div>
						
						</div>
					</th>
					<th id="wageNegotiation_current_offer" class="header-dropdown" data-sort-attr="data-wage-proposal-amount">
					
						<span data-toggle-id="sortWageProposalOffer" >
							<span class="glyphicon glyphicon-menu-down"></span>Current Offer
						</span>
						<div id="sortWageProposalOffer" class="dropdown-container sort-container">
<!-- 							<span class="approve-sort glyphicon glyphicon-ok"></span> -->
							
							<label>
								<input id="" type="radio"
									name="wage-prpoposal-current-offer"
									data-sort-ascending="0">High to Low
							</label>
							<label>
								<input id="" type="radio"
									name="wage-prpoposal-current-offer"
									data-sort-ascending="1">Low to High
							</label>					
						</div>					
					
					
					</th>
					<th id="rating" class="header-dropdown" data-sort-attr="data-applicant-rating">
					
						<span data-toggle-id="sortApplicantRating" >
							<span class="glyphicon glyphicon-menu-down"></span>Rating
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
					<th id="endorsements">Endorsements</th>
				<c:if test="${jobDto.questions.size() > 0 }">
					<th id="questions" class="header-dropdown"
						 data-filter-attr="data-answer-option-ids-seleted"
						 data-must-match-all-filter-values="1">
						<span data-toggle-id="filterAnswersContainer" >
							<span class="sub-header-toggle glyphicon glyphicon-menu-down"></span>Answers
						</span>					
						<div id="filterAnswersContainer" class="dropdown-container dropdown-style filter-container" >
							
							<span id="selectQuestionsOK" class="approve-filter glyphicon glyphicon-ok"></span>
							
							<table id="table_headerAnswers" class="main-table-style">
								<thead>
									<tr class="no-filter">
										<th id="filterAnswers"></th>
										<th id="header_question">Question</th>
										<th id="header_answers">Answer</th>												
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${jobDto.questions }" var="question">
										<tr class="no-filter">
											<td>															
												<label>
													<input type="checkbox" checked
														name="show-question-${question.questionId }"
														class="filter-answers">
														<span>Filter Answers</span>
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
					<th id="status" class="left header-dropdown" data-filter-attr="data-application-status">
					
						<span data-toggle-id="filterApplicationStatus" >
							<span class="glyphicon glyphicon-menu-down"></span>Status
						</span>
						<div id="filterApplicationStatus" class="dropdown-container filter-container checkbox-container">
							<span class="approve-filter glyphicon glyphicon-ok"></span>
							
							<label class="select-all-container">
								<input id="filterOption_wageProposal_selectAll" class="select-all"
									type="checkbox" name="wage-prpoposal-status"
									>Select All
							</label>
							<div class="options">
								<label>
									<input id="" type="checkbox"
										name="application-status"
										data-filter-attr-value="0">No action taken
								</label>
								<label>
									<input id="" type="checkbox"
										name="application-status"
										data-filter-attr-value="1">Not considering
								</label>
								<label>
									<input id="" type="checkbox"
										name="application-status"
										data-filter-attr-value="2">Considering
								</label>								
							</div>
						
						</div>					

					</th>
				</tr>
			</thead>
			<tbody class="vertical-lines">
			<c:forEach items="${jobDto.applications }" var="application">
				<tr class="" data-application-status="${application.status }"
					data-applicant-rating="${application.applicant.rating}"
					data-application-id="${application.applicationId }"
					data-is-old="${application.hasBeenViewed }"
					data-wage-proposal-amount="${application.currentWageProposal.amount }"
					data-wage-proposal-status="${application.currentWageProposal.status }"
					data-is-sent-proposal="${application.currentWageProposal.proposedToUserId ==
												 application.applicant.userId ? '1' : '0'}"
					data-answer-option-ids-seleted="${application.answerOptionIds_Selected }">
					
					<td>
						<div class="vert-border">
							<a class="accent" href="/JobSearch/job/${jobDto.job.id }/
										user/${application.applicant.userId}/jobs/completed">
										 ${application.applicant.firstName }</a>
						</div>
					</td>
					
					<td>
						<div class="">
							<c:choose>
								<c:when test="${application.currentWageProposal.status == 1 }">
								<!-- ****** If the current wage proposal has been accepted-->
									<div>
										<fmt:formatNumber type="number" minFractionDigits="2"
										 maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
										 has been accepted
									</div>
								</c:when>	
								<c:when test="${application.currentWageProposal.status == 3 }">
								<!-- ****** If the current wage proposal has been accepted-->
									<div>
										<c:choose>
											<c:when test="${application.time_untilEmployerApprovalExpires == '-1' }">
												<div>The applicant's time has expired</div>
											</c:when>
											<c:otherwise>
												<div>Waiting for applicant's approval.</div>
												<div class="expiration-time">Expires in ${application.time_untilEmployerApprovalExpires }</div>	
											</c:otherwise>										
										</c:choose>
										
									</div>
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
						</div>
					</td>									
					<td>
						<div class="vert-border">
							<span class="dollar-sign">$</span>
							<fmt:formatNumber type="number" minFractionDigits="2" 
								maxFractionDigits="2" value="${application.currentWageProposal.amount}"/>
							
							<c:if test="${application.wageProposals.size() > 1 }">
								<span data-toggle-id="wp-history-${application.applicationId }" 
									class="show-wage-proposal-history glyphicon glyphicon-menu-down"></span>
								<div id="wp-history-${application.applicationId }" class="dropdown-style">
									<table class="wage-proposal-history-table">
										<thead>
											<tr class="no-filter">
												<th>Proposed By</th>
												<th>Amount</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${application.wageProposals }" var="wageProposal">
												<tr class="no-filter ${wageProposal.proposedByUserId == userId ? 'you' : 'not-you' }">
													<td>${wageProposal.proposedByUserId == userId ? 'You' : 'Applicant' }</td>
													<td>
														<span class="dollar-sign">$</span>
														<fmt:formatNumber type="number" minFractionDigits="2" 
															maxFractionDigits="2" value="${wageProposal.amount}"/>
													</td>
												</tr>
											</c:forEach>	
										</tbody>
									</table>
								</div>
							</c:if>
						</div>
					</td>
					<td>
						<div class="vert-border">
						 	${application.applicant.rating}
						 </div>
					</td>
					<td>		
						<div class="vert-border">						
						<c:forEach items="${application.applicant.endorsements }" var="endorsement">
						
							<div class="endorsement">													
								${endorsement.categoryName } <span class="badge">  ${endorsement.count }</span>
							</div>
						</c:forEach>
						</div>
					</td>	
				<c:if test="${jobDto.questions.size() > 0 }">
					<td class="left">
						<div class="vert-border">
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
						</div>
					</td>
				</c:if>			
	
	<!-- 								Application Status						 -->
					<td class="">
						<div class="application-status-container">
							<c:choose>
								<c:when test="${application.status == 1 }">
								<button id="" value="1" class="active">Not Considering</button>
								</c:when>
								<c:otherwise>
								<button id="" value="1" class="">Not Considering</button>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${application.status == 2 }">
								<button id="" value="2" class="active">Considering</button>
								</c:when>
								<c:otherwise>
								<button id="" value="2" class="">Considering</button>
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