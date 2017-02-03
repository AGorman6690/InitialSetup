<%@ include file="../includes/TagLibs.jsp" %>		

<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>

<c:choose>         
	<c:when test="${haveJobRatingsBeenSubmitted == true }">
		<div>Job ratings have already been submitted</div>
	</c:when>
	<c:otherwise>
		<div id="buttonsContainer" class="row">
			<div class="col col-sm-12">
				<button id="submitRatings" class="square-button">Submit Ratings</button>
			</div>
		</div>
		
		
		<div id="ratingsContainer">
			<table id="ratings" class="main-table-style">
				<thead>
					<tr>
						<th id="employee">Employee</th>
						<th id="rating">Rating</th>
						<th id="endorsements">Endorsements</th>
						<th id="comment">Comment</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${jobDto.employeeDtos }" var="employeeDto">
					<tr class="employee-container">
						<td class="employee" data-id="${employeeDto.user.userId }">${employeeDto.user.firstName }</td>
						<td class="rate-criteria-container">
							<c:forEach items="${employeeDto.rating.rateCriteria }" var="rateCriterion">
								<div class="rate-criterion" data-id="${rateCriterion.rateCriterionId }"
										 data-value="${rateCriterion.value }">
									
									<c:set var="rateCriterionValue_modifed" value="${rateCriterion.value == -1 ? 0 : rateCriterion.value }" />
									<c:choose>
										
										<c:when test="${rateCriterion.rateCriterionId == 1 }">											
												<label for="work-ethic" class="control-label">Work Ethic</label>
												<input name="work-ethic" class="work-ethic rating-loading"
													value="${rateCriterionValue_modifed }" data-size="xs">																				
										</c:when>
										<c:when test="${rateCriterion.rateCriterionId == 2 }">
<!-- 											<div class="rate-criterion" data-id="2" data-value="0"> -->
												<label for="timeliness" class="control-label">Timeliness</label>
												<input name="timeliness" class="timeliness rating-loading"
													value="${rateCriterionValue_modifed }" data-size="xs">
<!-- 											</div>										 -->
										</c:when>
										<c:when test="${rateCriterion.rateCriterionId == 3 }">
<!-- 											<div class="rate-criterion" data-id="3" data-value="0"> -->
												<label for="hire-again" class="control-label">Hire Again?</label>
												<input name="hire-again" class="hire-again rating-loading"
													value="${rateCriterionValue_modifed }" data-size="xs">
<!-- 											</div>										 -->
										</c:when>	
		
									</c:choose>
								</div>																	
	
							</c:forEach>
						</td>
						<td>
							<div class="endorsementsContainer">
							<c:forEach items="${jobDto.categories }" var="category">
								
								<c:set var="isCategoryEndorsed" value="0" />
								<c:forEach items="${employeeDto.rating.endorsements }" var="endorsement">
									<c:if test="${endorsement.categoryId == category.id}">
										<c:set var="isCategoryEndorsed" value="1" />
									</c:if>
								</c:forEach>
								
								<div data-id="${category.id }" data-value="${isCategoryEndorsed }" class="endorsement">
									<span class="glyphicon ${isCategoryEndorsed == 1 ? 'glyphicon-ok' : 'glyphicon-remove' }"></span>
									${category.name }
								</div>
							</c:forEach>
							</div>
						</td>						
						<td><textarea class="comment" rows="3">${employeeDto.rating.comment }</textarea></td>
						
					</tr>
				</c:forEach>				
				</tbody>
			
			</table>		
		</div>
	</c:otherwise>
</c:choose>
