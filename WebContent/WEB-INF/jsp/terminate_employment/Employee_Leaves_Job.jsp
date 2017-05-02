<%@ include file="../includes/Header.jsp"%>

<div class="center pad-top-2">
	<c:choose>
		<c:when test="${!hasJobStarted }">
			<p>You are about to leave a job that has not yet started.<p>
			<p>Leaving a job that has not yet started will hurt your Reliability rating.</p>
			<p>Are you sure you want to leave this job?</p>
			<div class="pad-top">
				<a href="/JobSearch/job/${jobId}/leave" class="sqr-btn green">Yes</a>
				<a href="/JobSearch/user/profile" class="cancel">Cancel</a>
			</div>
			
		</c:when>
		<c:otherwise>
			<p>Why are you leaving the job?</p>
			<div class="radio-container">
				<label><input type="radio" name="why-leave">Because the job has no remaining work load</label>
				<label><input type="radio" name="why-leave">Because I want to apply for another job that interferes with this one</label>
				<label><input type="radio" name="why-leave">Because the employer is a jerk</label>
				<label>Other</label>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<%@ include file="../includes/Footer.jsp"%>