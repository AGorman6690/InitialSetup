<%@ include file="../includes/TagLibs.jsp"%>

		
			<c:choose>
				<c:when test="${!empty userDto_ratings.jobDtos_jobsCompleted }">	
					<div id="user-header">
						<h3>${userDto_ratings.user.firstName } ${userDto_ratings.user.lastName }</h3>
						<p>
							<input name="input-1" class="rating-loading"
									value="${userDto_ratings.ratingValue_overall }	">
							${userDto_ratings.ratingValue_overall }						
						</p>		
						<div id="user-rating-details">
							<c:forEach items="${userDto_ratings.ratingDto.rateCriteria }" var="rateCriterion">
								<div>
									<span class="criteria-name">${rateCriterion.shortName }</span>
									<span class="rating-value">
										<input name="input-1" class="rating-loading"
												value="${rateCriterion.stringValue }">${rateCriterion.stringValue }	
									</span>
								</div>
							</c:forEach>
						</div>
					</div>		
					<div id="user-complted-jobs">
						<h2>Completed Jobs</h2>
						<c:forEach items="${userDto_ratings.jobDtos_jobsCompleted }" var="jobDto">
							<div class="completed-job">
								<h3>${jobDto.job.jobName }</h3>
								<input name="input-1" class="rating-loading"
										value="${jobDto.ratingValue_overall }">${jobDto.ratingValue_overall }
								<c:if test="${jobDto.comments.size() > 0 }">
									<div class="comments-cont">
										<h5 data-toggle-id="comments-${jobDto.job.id }">
											${jobDto.comments.size() == 1 ? 'Comment' : 'Comments' }<span class="glyphicon glyphicon-menu-up"></span>
										</h5>
										<div id="comments-${jobDto.job.id }" class="comments">
											<c:forEach items="${jobDto.comments }" var="comment">
												<span>${comment }</span>
											</c:forEach>
										</div>
									</div>
								</c:if>								
							</div>			
						</c:forEach>
					</div>		
				</c:when>
				<c:otherwise>
					<p>${userDto_ratings.user.firstName } ${userDto_ratings.user.lastName } has not completed enough jobs in order to calculate a rating at this time</p>
				</c:otherwise>
			</c:choose>
