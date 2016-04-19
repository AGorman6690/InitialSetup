<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/User.js" />"></script>
</head>

<body>
	<input type="hidden" value="${worker.getUserId() }"></input>
	<div class="container">
	
		<c:choose>
			<c:when test="${context == 'viewingApplication'}">
				<div style="margin-bottom: 10px" class="btn-group applicant-status" role="group" aria-label="...">	
					<input value="${worker.getApplication().getStatus()}" class="applicant-status" type="hidden" />
					<c:choose>
						<c:when test="${worker.getApplication().getStatus() == 1 }">
							<div>
								<button value="0" class="update-application-status btn btn-info btn-sm"
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 0 )" >
								Declined</button>
								
								<button style="display: none" value="1" class="update-application-status btn btn-default btn-sm" 
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 1 )">
								Declined</button>
							</div>																			
						</c:when>
						<c:otherwise>
							<div>
								<button style="display: none" value="0" class="update-application-status btn btn-info btn-sm"
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 0 )" >
								Declined</button>
								
								<button value="1" class="update-application-status btn btn-default btn-sm" 
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 1 )">
								Declined</button>
							</div>	
						</c:otherwise>																																														
					</c:choose>																
							
					<c:choose>
						<c:when test="${worker.getApplication().getStatus() == 2 }">
							<div>
								<button value="0" class="update-application-status btn btn-info btn-sm"
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 0 )" >
								Considering</button>
								
								<button style="display: none" value="2" class="update-application-status btn btn-default btn-sm" 
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 2 )">
								Considering</button>
							</div>	
						</c:when>
						<c:otherwise>
							<div>
								<button style="display: none" value="0" class="update-application-status btn btn-info btn-sm"
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 0 )" >
								Considering</button>
								
								<button value="2" class="update-application-status btn btn-default btn-sm" 
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 2 )">
								Considering</button>
							</div>	
						</c:otherwise>																									
					</c:choose>		
					
					<c:choose>
						<c:when test="${worker.getApplication().getStatus() == 3 }">
							<div>
								<button value="0" class="update-application-status btn btn-info btn-sm"
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 0 )" >
								Hired</button>
								
								<button style="display: none" value="3" class="update-application-status btn btn-default btn-sm" 
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 3 )">
								Hired</button>
							</div>	
						</c:when>
						<c:otherwise>
							<div>
								<button style="display: none" value="0" class="update-application-status btn btn-info btn-sm"
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 0 )" >
								Hired</button>
								
								<button value="3" class="update-application-status btn btn-default btn-sm" 
								onclick="updateApplicationStatus(${worker.getApplication().getApplicationId()}, 3 )">
								Hired</button>
							</div>	
						</c:otherwise>																									
					</c:choose>																			
				</div> <!-- end button group -->
			</c:when> 
		</c:choose>	<!-- end context -->
	
		<div class="panel panel-success">
			<div class="panel-heading">Endorsements</div>
			<div class="panel-body">
				<c:choose>
					<c:when test="${fn:length(worker.endorsements) > 0 }">
						<c:forEach items="${worker.getEndorsements() }" var="e">
							<a href="#">${e.getCategoryName() }<span style="margin-left:5px" class="badge">
							${e.getCount() }</span></a><br>
						</c:forEach>
					</c:when>
					<c:otherwise>
						No Endorsements
					</c:otherwise>
				</c:choose>
			</div>
		</div><!-- end endorsement panel -->
	
		<c:choose>		
			<c:when test="${fn:length(completedJobDtos) == 0 }">
				<div style="width: 750px" class="panel panel-warning">
					<div class="panel-heading">Completed Job</div>
					<div id="job_${completedJobDto.job.getId() }" class="panel-body">
						No Completed Jobs
					</div>
				</div>
			</c:when>
			<c:otherwise>
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
			</c:otherwise>
		</c:choose>
	</div> <!-- end container -->
	
	
</body>


<script>
$(document).ready(function(){
	$(".update-application-status").click(function(){

		//Store the value of the application status
		$(this).parent().siblings('input.applicant-status').val($(this).val());
		
		//Show all default buttons
		var buttons = $(this).parents('div.applicant-status').find('button');
		for(var i = 0; i < buttons.length; i++){
			var button = buttons[i];
			
			if($(button).hasClass("btn-info")){
				$(button).hide();
			}else if($(button).hasClass("btn-default")){
				$(button).show();
			}
		}
		
		//Toggle the clicked button
		$(this).hide();
		var otherButton = $(this).siblings('button')[0];
		$(otherButton).show();

	})

})



</script>



<%@ include file="./includes/Footer.jsp"%>