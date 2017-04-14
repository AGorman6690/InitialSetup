
<%@ include file="../includes/TagLibs.jsp"%>	



<div class="page">
		<div id="map-section">
		<div id="map-container" class="corner">
			<div id="jobAddress">
				<p class="accent">${jobDto.job.streetAddress }</p>
				<p class="accent">${jobDto.job.city}, ${jobDto.job.state }</p>
				<p class="accent">${jobDto.job.zipCode }</p>		
			</div>	
			<div id="map" class="right-border corner" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }"></div>
		</div>
	</div>
	
	<div class="title">
		<p class="job-name">${jobDto.job.jobName }</p>
		<p class="categories">		
			<c:forEach items="${jobDto.categories }" var="category">
				<span class="category">${category.name}</span>
			</c:forEach>	
		</p>
		<p id="job-description">
			${jobDto.job.description } Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.
		</p>
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
	<div id="middle-container">		
		<div id="work-days-calendar-container" class="calendar-container read-only job-info-calendar ">
			<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
			<div class="work-days">
				<c:forEach items="${jobDto.workDays }" var="workDay">
					<div data-date="${workDay.stringDate }"></div>
				</c:forEach>
			</div>
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
		<div class="info-container row">
			<div class="info-label col-sm-4">
				Employment Type
			</div>
			<div class="info-value col-sm-8">
				<div class="checkbox">
					<label><input type="checkbox" checked disabled>Employee</label>					
				</div>
				<div class="checkbox">
					<label><input type="checkbox" disabled>Contractor</label>					
				</div>
			</div>
		</div>					
		<div class="info-container row">
			<div class="info-label col-sm-4">
				Compensation
			</div>
			<div class="info-value col-sm-8">
				<div class="checkbox">
					<label><input type="checkbox" disabled checked>Accepting all offers</label>					
				</div>
				<div class="checkbox">
					<label><input type="checkbox" disabled >Specified pay range</label>
	<!-- 								<div>Min: $15 / hr</div> -->
	<!-- 								<div>Max: $25 / hr</div>					 -->
				</div>
			</div>
		</div>	

		
	
		
		<a class="square-button-green" href="/JobSearch/job/${jobDto.job.id }/update/status/1">Start Job (for debugging)</a>	
		<a href="/JobSearch/job/${jobDto.job.id }/update/status/2"><button class="square-button">Mark Complete (for debugging)</button></a>								


		