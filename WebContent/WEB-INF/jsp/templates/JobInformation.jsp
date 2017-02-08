<%@ include file="../includes/TagLibs.jsp"%>	
<div class="row">					
	<div class="col-sm-8 container">
		<div class="info-container row">
			<div class="info-label col-sm-4">Name
			</div>
			<div class="info-value col-sm-8">
				${jobDto.job.jobName }
			</div>
		</div>	
		<div class="info-container row">
			<div class="info-label col-sm-4">${jobDto.categories.size() > 1 ? "Categories" : "Category"}</div>
			<div class="info-value col-sm-8">
				<c:forEach items="${jobDto.categories }" var="category">
				<span class="category">${category.name}</span>
				</c:forEach>					
			</div>
		</div>
		<div class="info-container row">
			<div class="info-label col-sm-4">
				Description
			</div>
			<div class="info-value col-sm-8">
				${jobDto.job.description } Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.
			</div>
		</div>		
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
					
		<c:choose>
			<c:when test="${jobDto.job.durationTypeId == 1 }">
				<c:set var="dateLabel" value="Date" />
				<c:set var="durationValue" value="${jobDto.durationHours } hours"/>
			</c:when>
			<c:when test="${jobDto.job.durationTypeId == 2 }">
				<c:set var="dateLabel" value="Dates" />
				<c:set var="durationValue" value="${jobDto.durationDays } days"/>
			</c:when>
			<c:otherwise>
				<c:set var="dateLabel" value="Start Date" />
			</c:otherwise>
		</c:choose>	
								
	
				
	<%-- 					<c:if test="${job.durationTypeId == 1 || job.durationTypeId == 2 }"> --%>
		<div class="info-container row">
			<div class="info-label col-sm-4">Start Time</div>
			<div class="info-value col-sm-8">${jobDto.job.stringStartTime }</div>
		</div>
		<div class="info-container row">
			<div class="info-label col-sm-4">End Time</div>
			<div class="info-value col-sm-8">${jobDto.job.stringEndTime }</div>
		</div>				
		
		<div class="info-container row">
			<div class="info-label col-sm-4">Duration</div>
			<div class="info-value col-sm-8">${durationValue }</div>
		</div>
	
																	
	<%-- 					</c:if> --%>

					
		<div class="info-container row">
			<div class="info-label col-sm-4">
				${dateLabel }
			</div>
			<div class="info-value col-sm-8">
				<div id="workDays">
					<c:forEach items="${jobDto.workDays }" var="workDay">
						<div data-date="${workDay.date }"></div>
					</c:forEach>
					
				</div>
				<c:choose>
					<c:when test="${jobDto.job.durationTypeId == 2 }">		
						<div id="workDaysCalendar" class="calendar-container read-only"
							 data-min-date="${jobDto.date_firstWorkDay }"
							 data-number-of-months="${jobDto.months_workDaysSpan }"></div>
					</c:when>
					<c:otherwise>${jobDto.job.stringStartDate }</c:otherwise>				
				</c:choose>
			</div>
		</div>	
		
	<c:if test="${jobDto.questions.size() > 0 }">	
		<div class="info-container row">	
			<div class="info-label col-sm-4">Questions</div>
			<div class="info-value col-sm-8">
					
				<c:forEach items="${jobDto.questions }" var="question">
					<div class="question-container">
						${question.text }
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
					</div>
				</c:forEach>		
					
			</div>
		</div>
	</c:if>
		
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
		<div id="jobAddress">
			<div class="accent">${jobDto.job.streetAddress }</div>
			<div class="accent">${jobDto.job.city}, ${jobDto.job.state }</div>
			<div class="accent">${jobDto.job.zipCode }</div>
		</div>			
		<div id="map" class="right-border" data-lat="${jobDto.job.lat }" data-lng="${jobDto.job.lng }">				 --%>
		</div>	 
	</div>
</div>

		