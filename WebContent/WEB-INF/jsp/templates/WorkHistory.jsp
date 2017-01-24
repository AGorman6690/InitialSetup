<%@ include file="../includes/TagLibs.jsp"%>

<c:choose>
	<c:when test="${userDto.jobDtos_jobsCompleted.size() > 0 }">		
		<c:forEach items="${userDto.jobDtos_jobsCompleted }" var="dto">
			<div class="job-container bottom-border-thinner">
				<div class="job-categories info">
					<div class="mock-row"><span class="accent mock-label">Job Name</span>
					<a class="accent" href="/JobSearch/job/${dto.job.id}?c=work-history&p=2">${dto.job.jobName }</a></div>
					<div class="mock-row"><span class="accent mock-label">Completion Date</span> ${dto.job.endDate }</div>
					<div class="mock-row"><span class="accent mock-label">Categories</span> 
					<c:forEach items="${dto.categories }" var="category" varStatus="status">
								<c:set var="html" value="${category.name }"></c:set>
								<c:if test="${not status.last }">
									<c:set var="html" value="${html },"></c:set>
								</c:if>
								<span class="endorsement">${html }</span>
					</c:forEach>
					</div>
				</div>
				<div class="job-endorsements info">
					<div class="mock-row"><span class="accent mock-label">Endorsements</span> 
					<c:choose>
						<c:when test="${dto.ratingDto.endorsements.size() > 0 }">
							
							<c:forEach items="${dto.ratingDto.endorsements }" var="endorsement" varStatus="status">
								<c:set var="html" value="${endorsement.categoryName }"></c:set>
								<c:if test="${not status.last }">
									<c:set var="html" value="${html },"></c:set>
								</c:if>
								<span class="endorsement">${html }</span>
							</c:forEach>
							
						</c:when>
						<c:otherwise>
							None
						</c:otherwise>
					</c:choose>
					</div>					
				</div>
				<div class="mock-row"><span class="accent mock-label">Rating</span> ${dto.ratingDto.value }</div>
				
				<c:if test="${dto.ratingDto.comment != ''}">
					<div class="mock-row"><span class="accent mock-label">Comment</span> ${dto.ratingDto.comment }</div>
				</c:if>
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		No jobs have been completed.
	</c:otherwise>
</c:choose>							
