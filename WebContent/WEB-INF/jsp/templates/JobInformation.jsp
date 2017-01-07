<%@ include file="../includes/TagLibs.jsp"%>	
<div class="row">					
	<div class="col-sm-8">
		<div class="info-container">
			<div class="info-label">Name
			</div>
			<div class="info-value">
				${jobDto.job.jobName }
			</div>
		</div>	
		<div class="info-container">
			<div class="info-label">
				Categories
			</div>
			<div class="info-value">
				<c:forEach items="${categories }" var="category">
				<span class="category">${category.name}</span>
				</c:forEach>					
			</div>
		</div>
		<div class="info-container">
			<div class="info-label">
				Description
			</div>
			<div class="info-value">
				${jobDto.job.description } Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.
			</div>
		</div>		
		<div class="info-container">
			<div class="info-label">
				Employment Type
			</div>
			<div class="info-value">
				<div class="checkbox">
					<label><input type="checkbox" checked disabled>Employee</label>					
				</div>
				<div class="checkbox">
					<label><input type="checkbox" disabled>Contractor</label>					
				</div>
			</div>
		</div>					
		<div class="info-container">
			<div class="info-label">
				Compensation
			</div>
			<div class="info-value">
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
		<div class="info-container">
			<div class="info-label">
				Start Time
			</div>
			<div class="info-value">
				${jobDto.job.stringStartTime }
			</div>
		</div>
		<div class="info-container">
			<div class="info-label">
				End Time
			</div>
			<div class="info-value">
				${jobDto.job.stringEndTime }
			</div>
		</div>				
		
		<div class="info-container">
			<div class="info-label">
				Duration
			</div>
			<div class="info-value">
				${durationValue }
			</div>
		</div>
	
																	
	<%-- 					</c:if> --%>
					
		<div class="info-container">
			<div class="info-label">
				${dateLabel }
			</div>
			<div class="info-value">
				<div id="workDays">
					<c:forEach items="${jobDto.workDays }" var="workDay">
						<div data-date="${workDay.date }"></div>
					</c:forEach>
					
				</div>
				<c:choose>
					<c:when test="${jobDto.job.durationTypeId == 2 }">		
						<div id="workDaysCalendar" class="calendar-container read-only"></div>
					</c:when>
					<c:otherwise>${jobDto.job.stringStartDate }</c:otherwise>				
				</c:choose>
			</div>
		</div>										
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
		