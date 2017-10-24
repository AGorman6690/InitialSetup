<%@ include file="../includes/TagLibs.jsp"%>

<div id="personal-info">	
	<div id="image" class="personal-info-item">		
		<label id="user-name">${response.profileInfoDto.user.firstName } ${response.profileInfoDto.user.lastName }</label>	
		<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">	
	</div>
	<c:if test="${response.profileInfoDto.user.profileId == 1 }">				
		<div class="personal-info-item">
			<label>Home Location</label>						
			<div class="value editable">					
				<c:choose>
					<c:when test="${empty response.profileInfoDto.user.homeCity &&
									 empty response.profileInfoDto.user.homeState &&
									 empty response.profileInfoDto.user.homeZipCode }">
						<span class="not-set">Add your home location so employers can find you</span>
					</c:when>
					<c:otherwise>	
						<span>					
							${response.profileInfoDto.user.homeCity},
							 ${response.profileInfoDto.user.homeState}
							  ${response.profileInfoDto.user.homeZipCode }
						 </span>						
					</c:otherwise>
				</c:choose>
			</div>				
			<div id="edit-home-location" class="edit-container">					
				<div>
					<label>City</label>
					<input id="city" class="select-all" type="text" value="${response.profileInfoDto.user.homeCity }">
				</div>
				<div>
					<label>State</label>
					<select id="state" data-do-show-placeholder="0" data-init-value="${response.profileInfoDto.user.homeState }"></select>
				</div>
				<div>
					<label>Zip Code</label>
					<input id="zipCode" class="select-all" type="text" value="${response.profileInfoDto.user.homeZipCode }">
				</div>
				<p class="edit-wrapper"><span id="save-home-location" class="sqr-btn save-profile-info">Save</span></p>
			</div>					
		</div>
		<div class="personal-info-item">
			<label>Maximum travel distance</label>
			<div class="value editable">	
<!-- 				<span class="glyphicon glyphicon-pencil"></span>		 -->
				<c:choose>
					<c:when test="${empty response.profileInfoDto.user.maxWorkRadius ||
								response.profileInfoDto.user.maxWorkRadius == 0}">
						<span class="not-set">Add the max distance you are willing to travel so employers can find you</span>
					</c:when>
					<c:otherwise>					
						<span>${response.profileInfoDto.user.maxWorkRadius } miles</span>
					</c:otherwise>
				</c:choose>
			</div>				
			<div id="edit-max-distance" class="edit-container">					
				<label>Miles</label>
				<input id="miles" class="select-all" type="text" value="${response.profileInfoDto.user.maxWorkRadius }">
				<p class="edit-wrapper"><span id="save-max-distance" class="sqr-btn save-profile-info">Save</span></p>
			</div>		
		</div>						
	</c:if>
	<div class="personal-info-item">
		<label>About</label>
		<div class="value editable">						
			<c:choose>
				<c:when test="${response.profileInfoDto.user.about == '' || empty response.profileInfoDto.user.about }">
					<span class="not-set">Write something so
					${sessionScope.user.profileId == 1 ? ' employers' : ' applicants' } know a little bit about you</span>
				</c:when>
				<c:otherwise>
					<span>${response.profileInfoDto.user.about }</span>
				</c:otherwise>
			</c:choose>
		</div>				
		<div id="edit-about" class="edit-container">			
			<textarea id="about" class="select-all">${response.profileInfoDto.user.about }</textarea>
			<p class="edit-wrapper"><span id="save-about" class="sqr-btn save-profile-info">Save</span></p>
		</div>
	</div>	
	<div id="user-ratings" class="personal-info-item">
		
		<c:if test="${!isViewingOnesSelf }">
			<h3>${response.profileInfoDto.user.firstName }
			 ${response.profileInfoDto.user.lastName }</h3>
		</c:if>
		<label>Rating</label>
		<c:choose>
			
			<c:when test="${!response.profileInfoDto.doesUserHaveEnoughDataToCalculateRating}">
				<c:choose>
					<c:when test="${sessionScope.user.profileId == 1 }">
						<p class="no-data">You have not completed enough jobs in order to calculate a rating at this time. Obtain a high rating and let that be your bargaining chip!</p>
					</c:when>
					<c:otherwise>
						<p class="no-data">You have not completed enough jobs in order to calculate a rating at this time.</p>
					</c:otherwise>					
				</c:choose>	
			</c:when>			
			<c:otherwise>		
					<p id="overall-rating" data-toggle-id="user-rating-details">
						<input name="input-1" class="rating-loading"
								value="${response.profileInfoDto.profileRatingDto.overallRating }	">
						${response.profileInfoDto.profileRatingDto.overallRating }			
					</p>		
					<%@ include file="../ratings/RatingDetails.jsp" %>				
			</c:otherwise>
		</c:choose>		
	</div>		
	<div id="user-completed-jobs" class="personal-info-item">
		<label>Work History</label>
		<c:choose>
			<c:when test="${empty response.profileInfoDto.completedJobsDtos }">					
				<p class="no-data">${isViewingOnesSelf ? 'You do not have a work history. Apply for some jobs and get one going!' :
					'No completed jobs' }</p>
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
