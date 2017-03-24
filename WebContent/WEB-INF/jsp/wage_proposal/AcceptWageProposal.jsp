<%@ include file="../includes/TagLibs.jsp"%>

<c:choose>
	<c:when test="${user.profileId == 1 }">
		<c:if test="${!empty applicationDto.applicationDtos_conflicting }">
			<div class="conflicting-apps-container sub-section">			
				By accepting this proposal, your following
				${applicationDto.applicationDtos_conflicting.size() > 1 ? 'applications' : 'application' }
				will be removed.
				<c:forEach items="${applicationDto.applicationDtos_conflicting }"
							 var="applicationDto_conflicting">
					<div><a class="accent display-job-info" data-job-id="${applicationDto_conflicting.jobDto.job.id }"
							 >${applicationDto_conflicting.jobDto.job.jobName }</a></div>
<!-- 							 href="/JobSearch/job/?c=profile-incomplete&p=1" -->
				</c:forEach>
			</div>
		</c:if>		
		<div class="accept-actions proposal-actions">
			<span class="accent approve-by-applicant" data-application-id="${applicationDto.application.applicationId }">Confirm</span>
			<span class="accent cancel">Cancel</span>
		</div>
	</c:when>
	<c:otherwise>
		<div class="lbl">Your offer expires in:</div>
		<div class="time-container">
			<span>Days</span><input class="days-pre-hire" />
		</div>
		<div class="time-container">
			<span>Hours</span><input class="hours-pre-hire" />
		</div>
		<div class="time-container">
			<span>Minutes</span><input class="minutes-pre-hire" />
		</div>
		<div class="send-cancel proposal-actions">
			<span class="accent accept-employer">Send</span>
			<span class="accent cancel">Cancel</span>
		</div>								
	</c:otherwise>
</c:choose>	
