<%@ include file="./includes/Header.jsp"%>

<head>

</head>

<body>

	<div class="container">
		<c:choose>
			<c:when test="${completedJobDtos.size() > 0 }">		
				<c:forEach items="${completedJobDtos }" var="dto">
					<div class="job-container">
						<div class="job-categories info">
							<c:forEach items="${dto.job.categories }" var="category">
								<span>${category.name }</span>
							</c:forEach>
						</div>
						<div class="job-endorsements info">
							<c:choose>
								<c:when test="${dto.endorsements.size() > 0 }">
									Endorsements:
									<c:forEach items="${dto.endorsements }" var="endorsement">
										<div class="endorsement">${endorsement.categoryName }</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									Endorsements: None
								</c:otherwise>
							</c:choose>
						</div>
						<div class="job-rating info">
							Rating: ${dto.rating }						
						</div>
						<div class="job-comment info">
							Comment: ${dto.comment }
						</div>
					</div>
				
				
				
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div>No jobs have been completed.</div>
			</c:otherwise>
		</c:choose>
	</div> <!-- end container -->
	
	
</body>


<script>
$(document).ready(function(){


})



</script>



<%@ include file="./includes/Footer.jsp"%>