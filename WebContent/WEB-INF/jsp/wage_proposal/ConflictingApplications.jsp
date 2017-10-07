<%@ include file="../includes/TagLibs.jsp"%>						

<c:if test="${response.conflictingApplications.size() > 0 }">
	<c:set var="isOneConflict"
	 	value="${response.conflictingApplications.size() == 1 ? '1' : '0' }"></c:set>
								
	<c:set var="text_application"
	 	value="${response.conflictingApplications.size() == 1 ? 'application' : 'applications' }"></c:set>
	<div class="other-application-conflicts">

		<div id="conflicting-apps-${response.referenceApplication.applicationId }"
				>
		<h4 class="red-bold" data-toggle-id="conflicting-apps-${response.referenceApplication.applicationId }">
			${response.conflictingApplications.size() } conflicting ${text_application }
			
		</h4>				
			<c:if test="${!response.jobHasOnlyOneWorkDay }">
				<p>These proposed work days overlap with ${isOneConflict == '1' ? 'another application'
					: 'other applications' } of yours.</p>
			</c:if>
			<c:choose>
				<c:when test="${areConflictsCausedByCounteringWorkDays }">
					<p class="if-you-accept">If you <span class="bold">accept</span>
						 this offer, your following ${text_application } will be:</p>		
				</c:when>
				<c:otherwise>
					<p class="if-you-accept">If <span class="bold">you accept</span>
						 this proposal, your following ${text_application }:</p>
				</c:otherwise>
			</c:choose>								
<%-- 			<c:if test="${applicationDto.applicationDtos_conflicting_willBeRemoved.size() > 0  || --%>
<%-- 							applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer.size() > 0  || --%>
<%-- 							applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer.size() > 0 }">		 --%>
				<div class="conflicting-applications ">
					<c:if test="${response.conflictingApplicationsToBeRemoved_jobDoesNotAllowPartial.size() > 0 }">
						<div class="disposition">
							<p><span class="bold">Removed</span> because the following ${response.conflictingApplicationsToBeRemoved_jobDoesNotAllowPartial.size() > 1 ? 'jobs require' : 'job requires'} you to apply for all work days</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplicationsToBeRemoved_jobDoesNotAllowPartial }"
										var="conflictingApplication">
										<li>${conflictingApplication.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					<c:if test="${response.conflictingApplicationsToBeRemoved_applicantHasNoAvailability.size() > 0 }">
						<div class="disposition">
							<p><span class="bold">Removed</span> because you have a schedule conflict with all work days for the following ${response.conflictingApplicationsToBeRemoved_applicantHasNoAvailability.size() > 1 ? 'jobs' : 'job'}</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplicationsToBeRemoved_applicantHasNoAvailability }"
										var="conflictingApplication">
										<li>${conflictingApplication.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>					
					<c:if test="${response.conflictingApplicationsToBeModifiedButRemainAtEmployer.size() > 0 }">
						<div class="disposition">
							<p><span class="bold">Modified</span> because
								 ${response.conflictingApplicationsToBeModifiedButRemainAtEmployer.size() == 1 ? 'the job list below allows' :
								  'the jobs listed below allow' } partial availability</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplicationsToBeModifiedButRemainAtEmployer }"
											var="conflictingApplication">
											<li>${conflictingApplication.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					<c:if test="${response.conflictingApplicationsToBeSentBackToEmployer.size() > 0 }">
						<div class="disposition">
							<p><span class="bold">Modified</span> and <span class="bold">sent back to the employer</span> because the following
								 ${response.conflictingApplicationsToBeSentBackToEmployer.size() == 1 ? 'job allows' :
								  'jobs allow' } partial availability</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplicationsToBeSentBackToEmployer }"
										var="conflictingApplication">
										<li>${conflictingApplication.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>												
				</div>
<%-- 			</c:if> --%>
		</div>
	</div>
</c:if>