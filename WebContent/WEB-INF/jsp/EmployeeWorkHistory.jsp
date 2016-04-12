<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/User.js" />"></script>
</head>

<body>

	<div class="container">
		<div id="applicationStatus" class="btn-group" role="group" aria-label="...">	
			<c:choose>
				<c:when test="${user.getApplication().getStatus() == 1 }">
					<button class="update-application-status btn btn-info btn-sm" >
					Decline</button>	
				</c:when>
				<c:otherwise>
					<button class="update-application-status btn btn-default btn-sm" 
					onclick="updateApplicationStatus(${user.getApplication().getApplicationId()}, 1 )">
					Decline</button>
				</c:otherwise>																																														
			</c:choose>																
			
			<c:choose>
				<c:when test="${user.getApplication().getStatus() == 2 }">
					<button class="update-application-status btn btn-info btn-sm"> 																		
					Consider</button>	
				</c:when>
				<c:otherwise>
					<button class="update-application-status btn btn-default btn-sm" 
					onclick="updateApplicationStatus(${user.getApplication().getApplicationId()}, 2 )">
					Consider</button>
				</c:otherwise>																									
			</c:choose>
			
			<c:choose>
				<c:when test="${user.getApplication().getStatus() == 3 }">
					<button class="update-application-status btn btn-info btn-sm"> 																		
					Hire</button>	
				</c:when>
				<c:otherwise>
					<button class="update-application-status btn btn-default btn-sm" 
					onclick="updateApplicationStatus(${user.getApplication().getApplicationId()}, 3 )">
					Hire</button>
				</c:otherwise>																									
			</c:choose>																

		</div><!-- end button group -->
	
		<div class="panel panel-success">
			<div class="panel-heading">Endorsements</div>
			<div class="panel-body">
				<c:forEach items="${user.getEndorsements() }" var="e">
					<a href="#">${e.getCategoryName() }<span style="margin-left:5px" class="badge">
					${e.getCount() }</span></a><br>
				</c:forEach>
			</div>
		</div><!-- end endorsement panel -->
	
		<c:forEach items="${completedJobDtos }" var="completedJobDto">
			<div style="width: 750px" class="panel panel-warning">
				<div class="panel-heading">Completed Job</div>
				<div id="job_${completedJobDto.job.getId() }" class="panel-body">
					
<%-- 					<div id="jobName_${completedJobDto.job.getId()}"> --%>
<!-- 						<h4><span class="label label-primary">Job Name</span></h4>					 -->
<%-- 						<div>${completedJobDto.job.getJobName()}</div> --%>
<!-- 					</div>	 -->
	
					<div id="categories_${completedJobDto.job.getId()}">				
						<c:choose>
							<c:when test="${fn:length(completedJobDto.job.categories) > 1}">				
									<h4><span class="label label-primary">Categories</span></h4>	
							</c:when>
							<c:otherwise>				
									<h4><span class="label label-primary">Category</span></h4>	
							</c:otherwise>
						</c:choose>
						<c:forEach items="${completedJobDto.job.getCategories() }" var="category">
							<div style="display:inline">${category.getName()}</div>
						</c:forEach>							
					</div> <!-- end categories -->
					
					
					<div id="rating_${completedJobDto.job.getId()}">
						<h4><span class="label label-primary">Rating</span></h4>					
						<div>${completedJobDto.getRating()}</div>
					</div>	

					<div id="jobEndDate_${completedJobDto.job.getId()}">
						<h4><span class="label label-primary">Job Completion Date</span></h4>					
						<div>${completedJobDto.job.getEndDate()}</div>
					</div>
					
					<div>				
						<c:choose>
							<c:when test="${fn:length(completedJobDto.endorsements) > 0}">				
								<h4><span class="label label-primary">Endorsements</span></h4>	
								<c:forEach items="${completedJobDto.getEndorsements() }" var="e">
									<span class="label label-success">
									${e.getCategoryName()}</span>
								</c:forEach>					
							</c:when>
						</c:choose>						
					</div> <!-- end endorsements -->							
						
					<br>
					<c:if test="${not empty completedJobDto.getComment()}">
						<div>
							<h4><span class="label label-primary">Comment</span></h4>					
							<textarea class="form-control" rows="3">
							${completedJobDto.getComment()}</textarea>
						</div>
					</c:if>
				</div> <!-- end completed job panel body -->
			</div><!-- end completed job panel -->
		
		</c:forEach> 	
	
	</div> <!-- end container -->
</body>


<script>
$(document).ready(function(){
	$(".update-application-status").click(function(){
		
		var buttons = $("#applicationStatus").find('button');
		for(var i = 0; i < buttons.length; i++){
			var button = buttons[i];
			if($(button).hasClass("btn-info")){
				$(button).removeClass("btn-info");
				$(button).addClass("btn-default");
			}
		}
		
		$(this).addClass('btn-info');
	})

})



</script>



<%@ include file="./includes/Footer.jsp"%>