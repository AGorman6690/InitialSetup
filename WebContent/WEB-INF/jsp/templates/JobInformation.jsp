
<%@ include file="../includes/TagLibs.jsp"%>	



<div class="page">
		<div id="map-section">
		<div id="map-container" class="corner">
			<div id="jobAddress">
				<p class="accent">${jobDto.job.streetAddress_formatted }</p>
				<p class="accent">${jobDto.job.city_formatted}, ${jobDto.job.state }</p>
				<p class="accent">${jobDto.job.zipCode_formatted }</p>		
			</div>			
			<div id="map" class="right-border corner" data-do-init="1"
				data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }"></div>
		</div>
	</div>
	
	<div class="title">
		<p class="job-name">${jobDto.job.jobName }</p>
<!-- 		<p class="categories">		 -->
<%-- 			<c:forEach items="${jobDto.categories }" var="category"> --%>
<%-- 				<span class="category">${category.name}</span> --%>
<%-- 			</c:forEach>	 --%>
<!-- 		</p> -->
		<c:if test="${sessionScope.user.profileId == 1 }">
			<div id="employer-rating" class="center">
				<p class="detail-header-lbl green">Employer Rating</p>
				<c:choose>
					<c:when test="${userHasEnoughRatingData }">			
						<input name="input-1" class="rating-loading"					
							value="${jobDto.ratingValue_overall }">${jobDto.ratingValue_overall }									
					</c:when>
					<c:otherwise>NA</c:otherwise>					
				</c:choose>
			</div>	
		</c:if>
		<div class="width-500">
<!-- 			<p class="detail-header-lbl">Description</p> -->
			<p id="job-description">
				${jobDto.job.description }
			</p>
		</div>
		<c:if test="${jobDto.skillsRequired.size() > 0 || jobDto.skillsDesired.size() > 0 }">
			<div id="skills-container">
<!-- 				<p data-toggle-id="skills-details" class="detail-header-lbl">Skills<span class="glyphicon-menu-down glyphicon"></span></p> -->
				<div id="skills-details"  class="details-container">
					<c:if test="${jobDto.skillsRequired.size() > 0 }">
						<div>
							<p class="detail-header-lbl">Required Skills</p>
							<ul>
								<c:forEach items="${jobDto.skillsRequired }" var="skill">
									<li>${skill.text }</li>	
								</c:forEach>
							</ul>
						</div>		
					</c:if>
						
					<c:if test="${jobDto.skillsDesired.size() > 0 }">
						<div>
							<p class="detail-header-lbl">Desired Skills</p>
							<ul>
								<c:forEach items="${jobDto.skillsDesired }" var="skill">
									<li>${skill.text }</li>	
								</c:forEach>
							</ul>
						</div>			
					</c:if>
				</div>
			</div>	
		</c:if>		
		<c:if test="${jobDto.questions.size() > 0 }">			
			<div id="questions-container-new">
				<p data-toggle-speed="2" data-toggle-id="question-details" class="detail-header-lbl">Questions<span class="glyphicon-menu-down glyphicon"></span></p>
				<div id="question-details" class="details-container">
					<c:forEach items="${jobDto.questions }" var="question">					
						<p>${question.text }</p>
						
						<c:choose>
							<c:when test="${question.formatId == 2 || question.formatId == 3}">
								<div class="answer-container">
									<ul>
									<c:forEach items="${question.answerOptions }" var="answerOption">
										<li>${answerOption.text }</li>
									</c:forEach>
									</ul>
								</div>
							</c:when>
						</c:choose>
						
					</c:forEach>		
				</div>			
			</div>		
		</c:if>		

	</div>
	<div id="middle-container" class="center">		
	
		<c:if test="${jobDto.workDayDtos.size() > 1 }">
			<p class="detail-header-lbl green">
				<c:choose>
					<c:when test="${jobDto.job.isPartialAvailabilityAllowed }">
						Applicants can apply for one or more days
					</c:when>
					<c:otherwise>
						Applicants must apply for all days
					</c:otherwise>
				</c:choose>
			</p> 
		</c:if>
		<div id="work-days-calendar-container" class="v2 hide-select-work-day calendar-container read-only">
			<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
		</div>		
	</div>
</div>
<div id="json_work_day_dtos">${json_work_day_dtos }</div>

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<a class="square-button-green" href="/JobSearch/job/${jobDto.job.id }/update/status/1">Start Job (for debugging)</a>	
<a href="/JobSearch/job/${jobDto.job.id }/update/status/2"><button class="square-button">Mark Complete (for debugging)</button></a>								


		