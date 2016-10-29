<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employerViewEmployee.css" />
</head>

<body>

	<div class="container">
		<div class="section">
			<div class="header2">
				<span data-toggle-id="workHistoryContainer">
					<span class="glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Work History</span>
				</span>
			</div>	
			<div id="workHistoryContainer" class="section-body">
<c:choose>
	<c:when test="${completedJobDtos.size() > 0 }">		
		<c:forEach items="${completedJobDtos }" var="dto">
			<div class="job-container bottom-border-thinner">
				<div class="job-categories info">
					<div class="mock-row"><span class="accent mock-label">Job Name</span>
					<a class="accent" href="/JobSearch/job/${dto.job.id}">${dto.job.jobName }</a></div>
					<div class="mock-row"><span class="accent mock-label">Completion Date</span> ${dto.job.endDate }</div>
					<div class="mock-row"><span class="accent mock-label">Categories</span> 
					<c:forEach items="${dto.job.categories }" var="category">
						<span>${category.name},</span>
					</c:forEach>
					</div>
				</div>
				<div class="job-endorsements info">
					<div class="mock-row"><span class="accent mock-label">Endorsements</span> 
					<c:choose>
						<c:when test="${dto.endorsements.size() > 0 }">
							
							<c:forEach items="${dto.endorsements }" var="endorsement">
								<span class="endorsement">${endorsement.categoryName},</span>
							</c:forEach>
							
						</c:when>
						<c:otherwise>
							None
						</c:otherwise>
					</c:choose>
					</div>					
				</div>
				<div class="mock-row"><span class="accent mock-label">Rating</span> ${dto.rating }</div>
				
				<c:if test="${dto.comment != ''}">
					<div class="mock-row"><span class="accent mock-label">Comment</span> ${dto.comment }</div>
				</c:if>
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		No jobs have been completed.
	</c:otherwise>
</c:choose>
		</div> <!-- end container -->
	</div>
</div>
	
	
</body>


<script>
$(document).ready(function(){


})



</script>



<%@ include file="./includes/Footer.jsp"%>