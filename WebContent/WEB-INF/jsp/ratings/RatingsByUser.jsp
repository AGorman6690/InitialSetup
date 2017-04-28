<%@ include file="../includes/TagLibs.jsp"%>

		
			<c:choose>
				<c:when test="${!empty userDto_ratings.jobDtos_jobsCompleted }">	
					<div id="user-header">
						<c:if test="${!isViewingOnesSelf }">
							<h3>${userDto_ratings.user.firstName } ${userDto_ratings.user.lastName }</h3>
						</c:if>
						<p data-toggle-id="user-rating-details">
							<input name="input-1" class="rating-loading"
									value="${userDto_ratings.ratingValue_overall }	">
							${userDto_ratings.ratingValue_overall }			
<!-- 							<span class="glyphicon glyphicon-menu-up"></span>			 -->
						</p>		
						<div id="user-rating-details" class="">
							<c:forEach items="${userDto_ratings.ratingDto.rateCriteria }" var="rateCriterion">
								<div class="criteria-cont">
									<span class="criteria-name">${rateCriterion.shortName }</span>
									<span class="rating-value">
										<input name="input-1" class="rating-loading"
												value="${rateCriterion.stringValue }">${rateCriterion.stringValue }	
									</span>
								</div>
							</c:forEach>
						</div>
					</div>		
					<c:if test="${!isViewingOnesSelf }">
						<div>
							<h3 data-toggle-id="about-user" class="h3">About<span class="glyphicon glyphicon-menu-up"></span></h3>
							<div id="about-user" class="details">
								<p>${userDto_ratings.user.about }</p>
							</div>
						</div>
					</c:if>
					<div id="user-complted-jobs" class="pad-top">
						<h3 data-toggle-id="completed-jobs" class="h3">Completed Jobs<span class="glyphicon glyphicon-menu-up"></span></h3>
						<div id="completed-jobs" class="details">
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
					</div>		
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isViewingOnesSelf }">
							<p>You have not completed enough jobs in order to calculate a rating at this time</p>	
						</c:when>
						<c:otherwise>
							<p>${userDto_ratings.user.firstName } ${userDto_ratings.user.lastName } has not completed enough jobs in order to calculate a rating at this time</p>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
