<%@ include file="../includes/TagLibs.jsp"%>

<div id="user-ratings">			
	<div class="header">
		<h3>${response.profileInfoDto.user.firstName }
		 ${response.profileInfoDto.user.lastName }</h3>
	</div>
	<div class="personal-info-section">
		<label>Rating</label>
		<div class="personal-info">
			<c:choose>
				<c:when test="${!response.profileInfoDto.doesUserHaveEnoughDataToCalculateRating}">
					<p>${response.profileInfoDto.user.firstName } ${response.profileInfoDto.user.lastName }
						 has not completed enough jobs in order to calculate a rating at this time</p>
				</c:when>			
				<c:otherwise>		
						<p id="overall-rating" data-toggle-id="user-rating-details">
							<input name="input-1" class="rating-loading"
									value="${response.profileInfoDto.profileRatingDto.overallRating }	">
							${response.profileInfoDto.profileRatingDto.overallRating }			
						</p>		
						<%@ include file="./RatingDetails.jsp" %>				
				</c:otherwise>
			</c:choose>	
		</div>				
	</div>		
	<c:if test="${not empty response.profileInfoDto.user.about }">				
		<div class="personal-info-section">
			<label>About</label>
			<div class="personal-info">
				<p>${response.profileInfoDto.user.about }</p>
			</div>
		</div>				
	</c:if>
	<div class="personal-info-section">
		<label>Work History</label>
		<div class="personal-info">
			<c:choose>
				<c:when test="${empty response.profileInfoDto.completedJobsDtos }">					
					<p class="no-data">No completed jobs at this time</p>
				</c:when>
				<c:otherwise>
					<div id="completed-jobs" class="">
						<c:forEach items="${response.profileInfoDto.completedJobsDtos }" var="completedJobDto">
							<div class="completed-job">
								<span class="">${completedJobDto.job.jobName }</span>
								<span class="rating-value">
								<input name="input-1" class="rating-loading"
										value="${completedJobDto.rating }">${completedJobDto.rating }
									</span>
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
				</c:otherwise>
			</c:choose>
		</div>
	</div>		
</div>
