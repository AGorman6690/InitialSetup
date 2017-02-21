<%@ include file="../includes/Header.jsp"%>
	
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/ratings/star-rating.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/ratings.css" />

<script src="<c:url value="/static/External/ratings/star-rating.js" />"></script>
<script src="<c:url value="/static/javascript/ratings/RateEmployer.js" />"></script>

<div class="container">
<div id="ratingsContainer" style="padding-top:40px;">
		<table id="ratings" class="main-table-style">
			<thead>
				<tr>
					<th id="employee">Employer</th>
					<th id="rating">Rating</th>
					<th id="comment">Comment</th>
				</tr>
			</thead>
			<tbody>
				<tr class="employee-container">
					<td class="employee" data-id="${employer.userId }">${employer.firstName }</td>
					<td class="rate-criteria-container">
<%-- 						<c:forEach items="${employeeDto.rating.rateCriteria }" var="rateCriterion"> --%>
							<div class="rate-criterion" data-id="${rateCriterion.rateCriterionId }"
									 data-value="${rateCriterion.value }">
								
								<c:set var="rateCriterionValue_modifed" value="${rateCriterion.value == -1 ? 0 : rateCriterion.value }" />
<%-- 								<c:choose> --%>
									
										<div class="rate-criterion" style="padding-bottom: 20px;">
<%-- 									<c:when test="${rateCriterion.rateCriterionId == 1 }">											 --%>
											<label for="work-ethic" class="control-label">Did the employer financially compensate you as agreed upon?</label>
											<input name="work-ethic" class="work-ethic rating-loading"
												value="${rateCriterionValue_modifed }" data-size="xs">
										</div>																				
<%-- 									</c:when> --%>
<%-- 									<c:when test="${rateCriterion.rateCriterionId == 2 }"> --%>
	<!-- 											<div class="rate-criterion" data-id="2" data-value="0"> -->
										<div class="rate-criterion" style="padding-bottom: 20px;">
											<label for="timeliness" class="control-label">How accurately did the job posting describe the work you performed?</label>
											<input name="timeliness" class="timeliness rating-loading"
												value="${rateCriterionValue_modifed }" data-size="xs">
										</div>
	<!-- 											</div>										 -->
<%-- 									</c:when> --%>
<%-- 									<c:when test="${rateCriterion.rateCriterionId == 3 }"> --%>
	<!-- 											<div class="rate-criterion" data-id="3" data-value="0"> -->
										<div class="rate-criterion" style="padding-bottom: 20px;">
											<label for="hire-again" class="control-label">Would you work for this employer again?</label>
											<input name="hire-again" class="hire-again rating-loading"
												value="${rateCriterionValue_modifed }" data-size="xs">
										</div>
	<!-- 											</div>										 -->
<%-- 									</c:when>	 --%>
	
<%-- 								</c:choose> --%>
							</div>
<%-- 						</c:forEach> --%>
					</td>
					
					<td><textarea class="comment" rows="3">${employeeDto.ratingDto.comment }</textarea></td>
					
				</tr>		
			</tbody>
		
		</table>		
	</div>

</div>