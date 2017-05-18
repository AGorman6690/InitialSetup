<%@ include file="../includes/TagLibs.jsp"%>						

<c:if test="${!empty applicationDto.count_conflictingApplications && 
					applicationDto.count_conflictingApplications > 0}">			
	<c:set var="text_application"
	 	value="${applicationDto.count_conflictingApplications == 1 ? 'application' : 'applications' }"></c:set>
	<div class="other-application-conflicts width-500 mar-btm">
		<h4 class="alert-text" data-toggle-id="conflicting-apps-${applicationDto.application.applicationId }">
			${applicationDto.count_conflictingApplications} conflicting ${text_application }
			<span class="glyphicon glyphicon-menu-down"></span>	
		</h4>
		<div id="conflicting-apps-${applicationDto.application.applicationId }"
				class="alert-message hide-on-load">
			<p>These proposed work days overlap with the following ${text_application }.</p>
			<c:choose>
				<c:when test="${areConflictsCausedByCounteringWorkDays }">
					<p class="if-you-accept">If the <span class="bold">employer accepts</span>
						 this proposal and you then accept employment, your following ${text_application } will be:</p>		
				</c:when>
				<c:otherwise>
					<p class="if-you-accept">If <span class="bold">you accept</span>
						 this proposal, your following ${text_application }:</p>
				</c:otherwise>
			</c:choose>								
			<c:if test="${applicationDto.applicationDtos_conflicting_willBeRemoved.size() > 0  ||
							applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer.size() > 0  ||
							applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer.size() > 0 }">		
				<div class="conflicting-applications ">
					<c:if test="${applicationDto.applicationDtos_conflicting_willBeRemoved.size() > 0 }">
						<div class="disposition">
							<p>Will be <span class="bold">removed</span> this job requires you to apply for all work days</p>
							<div class="applications">
								<ul>
									<c:forEach items="${applicationDto.applicationDtos_conflicting_willBeRemoved }"
										var="applicationDto">
										<li>${applicationDto.jobDto.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					<c:if test="${applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer.size() > 0 }">
						<div class="disposition">
							<p>will be <span class="bold">modified</span></p>
							<div class="applications">
								<ul>
									<c:forEach items="${applicationDto.applicationDtos_conflicting_willBeModifiedButRemainAtEmployer }"
											var="applicationDto">
											<li>${applicationDto.jobDto.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					<c:if test="${applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer.size() > 0 }">
						<div class="disposition">
							<p>will be <span class="bold">modified</span> and <span class="bold">sent back</span> to the employer</p>
							<div class="applications">
								<ul>
									<c:forEach items="${applicationDto.applicationDtos_conflicting_willBeSentBackToEmployer }"
										var="applicationDto">
										<li>${applicationDto.jobDto.job.jobName }</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>												
				</div>
			</c:if>
		</div>
	</div>
</c:if>