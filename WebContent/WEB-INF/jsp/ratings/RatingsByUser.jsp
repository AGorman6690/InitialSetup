<%@ include file="../includes/TagLibs.jsp"%>

		<div id="user-stats">
			<c:choose>
				<c:when test="${!response.profileInfoDto.doesUserHaveEnoughDataToCalculateRating}">
					<c:choose>
						<c:when test="${isViewingOnesSelf }">
							<p>You have not completed enough jobs in order to calculate a rating at this time</p>	
						</c:when>
						<c:otherwise>
							<p>${response.profileInfoDto.user.firstName } ${response.profileInfoDto.user.lastName }
								 has not completed enough jobs in order to calculate a rating at this time</p>
						</c:otherwise>
					</c:choose>
				</c:when>			
				<c:otherwise>	
					<div id="user-ratings" class="personal-info-section">
						<c:if test="${!isViewingOnesSelf }">
							<h3>${response.profileInfoDto.user.firstName }
							 ${response.profileInfoDto.user.lastName }</h3>
						</c:if>
						<label>Rating</label>
						<p id="overall-rating" data-toggle-id="user-rating-details">
							<input name="input-1" class="rating-loading"
									value="${response.profileInfoDto.profileRatingDto.overallRating }	">
							${response.profileInfoDto.profileRatingDto.overallRating }			
						</p>		
						<%@ include file="./RatingDetails.jsp" %>
					</div>		
					<c:if test="${!isViewingOnesSelf && !empty response.profileInfoDto.user.about }">
						<div>
							<label data-toggle-id="about-user" class="">About</label>
							<div id="about-user" class="paragraph">
								<p>${response.profileInfoDto.user.about }</p>
							</div>
						</div>
					</c:if>
					<div id="user-completed-jobs" class="personal-info-section">
						<label>Work History</label>
						<div id="completed-jobs" class="">
							<c:forEach items="${response.profileInfoDto.completedJobsDtos }" var="completedJobDto">
								<div class="completed-job">
									<span class="">${completedJobDto.job.jobName }</span>
									<input name="input-1" class="rating-loading"
											value="${completedJobDto.rating }">${completedJobDto.rating }
									<c:if test="${completedJobDto.comments.size() > 0 }">
										<div class="comments-cont">
											<p class="toggle-comments" data-toggle-id="comments-${completedJobDto.job.id }">
												${completedJobDto.comments.size() == 1 ? 'Comment' : 'Comments' }
												<span class="glyphicon glyphicon-menu-up"></span>
											</p>
											<div id="comments-${completedJobDto.job.id }" class="comments">
												<c:forEach items="${completedJobDto.comments }" var="comment">
													<span>${comment }</span>
												</c:forEach>
											</div>
										</div>
									</c:if>								
								</div>			
							</c:forEach>
						</div>
					</div>		
				</c:otherwise>
			</c:choose>
		</div>
