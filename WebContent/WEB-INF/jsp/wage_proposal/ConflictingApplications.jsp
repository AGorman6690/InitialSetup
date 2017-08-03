<%@ include file="../includes/TagLibs.jsp"%>						

<c:if test="${response.conflictingApplications.size() > 0 }">
	<c:set var="isOneConflict"
	 	value="${response.conflictingApplications.size() == 1 ? '1' : '0' }"></c:set>
								
	<c:set var="text_application"
	 	value="${response.conflictingApplications.size() == 1 ? 'application' : 'applications' }"></c:set>
	<div class="other-application-conflicts width-500 mar-btm">
		<h4 class="red-bold" data-toggle-id="conflicting-apps-${response.referenceApplication.applicationId }">
			${response.conflictingApplications.size() } conflicting ${text_application }
			<span class="glyphicon glyphicon-menu-down"></span>	
		</h4>
		<div id="conflicting-apps-${response.referenceApplication.applicationId }"
				class="alert-message ">
			<p>These proposed work days overlap with ${isOneConflict == '1' ? 'another application'
				: 'other applications' }.</p>
			<c:choose>
				<c:when test="${areConflictsCausedByCounteringWorkDays }">
					<p class="if-you-accept">If you <span class="bold">accept</span>
						 these work days, your following ${text_application } will be:</p>		
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
					<c:if test="${response.conflictingApplicationsToBeRemoved.size() > 0 }">
						<div class="disposition">
							<p><span class="bold">Removed</span> because this job requires you to apply for all work days</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplicationsToBeRemoved }"
										var="conflictingApplication">
										<li>${conflictingApplication.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					<c:if test="${response.conflictingApplictionsToBeModifiedButRemainAtEmployer.size() > 0 }">
						<div class="disposition">
							<p><span class="bold">Modified</span> because this job allows partial availability</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplictionsToBeModifiedButRemainAtEmployer }"
											var="conflictingApplication">
											<li>${conflictingApplication.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					<c:if test="${response.conflictingApplicationsToillBeSentBackToEmployer.size() > 0 }">
						<div class="disposition">
							<p>will be <span class="bold">modified</span> and <span class="bold">sent back</span> to the employer</p>
							<div class="applications">
								<ul>
									<c:forEach items="${response.conflictingApplicationsToillBeSentBackToEmployer }"
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