<%@ include file="../includes/TagLibs.jsp"%>						
						
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
