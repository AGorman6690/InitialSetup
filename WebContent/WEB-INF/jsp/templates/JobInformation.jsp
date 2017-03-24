
<%@ include file="../includes/TagLibs.jsp"%>	

<div>

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
		<div id="map-container">
			<div id="jobAddress">
				<div class="accent">${jobDto.job.streetAddress }</div>
				<div class="accent">${jobDto.job.city}, ${jobDto.job.state }</div>
				<div class="accent">${jobDto.job.zipCode }</div>		
			</div>	
			<div id="map" class="right-border" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }"></div>
		</div>		

	</div>
	<div id="middle-container">
		
		<div id="work-days-calendar-container" class="calendar-container read-only">
			<div class="calendar" data-min-date=${jobDto.date_firstWorkDay } data-number-of-months="${jobDto.months_workDaysSpan }"></div>
			<div class="work-days">
				<c:forEach items="${jobDto.workDays }" var="workDay">
					<div data-date="${workDay.stringDate }"></div>
				</c:forEach>
			</div>
		</div>		
	</div>
	
	
		<c:if test="${jobDto.questions.size() > 0 }">			
			<div id="questions-container">
				<p>Questions</p>
				<div class="questions">
					<c:forEach items="${jobDto.questions }" var="question">					
						<p>${question.text }</p>
						<div class="answer-container">
							<c:choose>
								<c:when test="${question.formatId == 2 || question.formatId == 3}">
									<div class="answer-options-container">
									<c:forEach items="${question.answerOptions }" var="answerOption">
										<div class="answer-option">
											${answerOption.text }
										</div>
									</c:forEach>
									</div>
								</c:when>
							</c:choose>
						</div>
					</c:forEach>		
				</div>			
			</div>		
		</c:if>		
</div>


<div class="row">
					
	<div class="container">


	
	<c:if test="${jobDto.skillsRequired.size() > 0 }">
		<div class="info-container row">
			<div class="info-label col-sm-4">
				Required Skills
			</div>
			<div class="info-value col-sm-8">
				<ul>
					<c:forEach items="${jobDto.skillsRequired }" var="skill">
						<li>${skill.text }</li>	
					</c:forEach>
				</ul>
			</div>
		</div>			
	</c:if>
			
	<c:if test="${jobDto.skillsDesired.size() > 0 }">
		<div class="info-container row">
			<div class="info-label col-sm-4">
				Desired Skills
			</div>
			<div class="info-value col-sm-8">
				<ul>
					<c:forEach items="${jobDto.skillsDesired }" var="skill">
						<li>${skill.text }</li>	
					</c:forEach>
				</ul>
			</div>
		</div>			
	</c:if>

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
	</div>
	<div class="col-sm-4">
	
		
 
	</div>
</div>

		