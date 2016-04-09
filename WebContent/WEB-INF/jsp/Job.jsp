<%@ include file="./includes/Header.jsp"%>
<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>

<!-- Bootstrap Drop down -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript --> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>

</head>

<input type="hidden" id="jobId" value="${job.id}" />
<input type="hidden" id="userId" value="${user.userId}" />

<div class="container">

			<div class="job-options btn-group" role="group" aria-label="...">
				<c:choose>
					<c:when test="${user.getProfileId() == 1}">
						<a href="./edit" class="btn btn-default">Edit Job</a>
						<button onclick="markJobComplete(${job.getId()})" id="markJobComplete"
							class="btn btn-default">Mark Job Complete</button>
					</c:when>
					<c:when test="${user.getProfileId() == 2}">
<%-- 						<a href="../job/${job.getId()}/user/${user.getUserId()}/apply" class="btn btn-default">Apply</a> --%>
							<a onclick="apply()" class="btn btn-default">Apply</a>
					</c:when>	
				</c:choose>				
			</div>

	<div>
		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Name</div>
			<div id="jobName" class="panel-body">${job.getJobName() }</div>
		</div>

			<div style="width: 500px" class="panel panel-info">
			  <div class="panel-heading">
			    Job Location
			  </div>
			  <div id="jobLocation" class="panel-body"></div>
			  
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">Street Address</span>
				  <div id="jobStreetAddress" class="panel-body">${job.getStreetAddress() }</div>
				</div>
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">City</span>
				  <div id="jobCity" class="panel-body">${job.getCity() }</div>
				</div>		
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">State</span>
				  <div id="jobState" class="panel-body">${job.getState() }</div>
				</div>	
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">Zip Code</span>
				  <div id="jobZipCode" class="panel-body">${job.getZipCode() }</div>
				</div>											
			</div>

		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Description</div>
			<div id="jobDescription" class="panel-body">${job.getDescription() }</div>
		</div>

	</div>

	<c:choose>
		<c:when test="${user.getProfileId() == 1}">
			<br>
			<div id="container">
		
				<div style="width: 750px" class="panel panel-warning">
					<div class="panel-heading">Applicants</div>
					<div id="applicants" class="panel-body">
						<c:choose>
							<c:when test="${job.getApplicants().size() > 0 }">
								<table id="applicantsTable" class="table table-hover table-striped 
										table-bordered" cellspacing="0" width="100%">
									<thead>
										<tr>
		
											<th>Applicant Name</th>
											<th>Rating</th>
											<c:forEach items="${job.getCategories() }"
 		 										var="category"> 
 		 										<th>${category.getName()} Endorsements</th>  
 		 									</c:forEach> 
 		 									<c:if test="${fn:length(job.getCategories()) > 1 }"> 		 									
 		 										<th>Total Endorsements</th>
 		 									</c:if>
 		 									<th> </th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${job.getApplicants() }" var="applicant">
											<c:choose>
												<c:when test="${applicant.getApplication().status < 3 }">
													<tr id="applicant_${applicant.getUserId() }">
														<td class="clickToWorkHistory">${applicant.getFirstName() }
																${applicant.getLastName() }</td>
																
														<td class="clickToWorkHistory">${applicant.getRating() }</td>														
														<c:set var="total" value = "${0 }" />
														<c:forEach items="${applicant.getEndorsements()}" var="endorsement">
															<td class="clickToWorkHistory">${endorsement.getCount() }</td>
															<c:set var="total" value="${total + endorsement.getCount() }" />
														</c:forEach>
			 		 									<c:if test="${fn:length(job.getCategories()) > 1 }"> 		 									
			 		 										<td class="clickToWorkHistory">${total}</td>
			 		 									</c:if>
														<td>
<!-- 															<div class="btn-group" role="group" aria-label="...">	 -->
																<button class="btn btn-info btn-sm"
																onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 1 )">
																Decline</button>	
																<button class="btn btn-info btn-sm"
																onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 2 )">
																Consider</button>																																												
																<button class="btn btn-info btn-sm"
																onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 3 )">
																Hire</button>	
<!-- 															</div> -->
														</td>
																
													</tr>
												</c:when>
											</c:choose>
										</c:forEach>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								<div id="noApplicants">
									<div>No applicants</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div><!-- end applicants panel body -->
				</div> <!-- end applicants panel -->
		
				<div style="width: 750px" class="panel panel-warning">
					<div class="panel-heading">Employees</div>
					<div id="employees" class="panel-body">
						<c:choose>
							<c:when test="${job.getEmployees().size() > 0 }">
								<table id="employeesTable" class="table table-hover table-striped 
										table-bordered" cellspacing="0" width="100%">
									<thead>
										<tr>		
											<th>Employee Name</th>
											<th>Rating</th>
											<c:forEach items="${job.getCategories() }"
 		 										var="category"> 
 		 										<th>${category.getName()} Endorsements</th>  
 		 									</c:forEach> 
 		 									<th>Total Endorsements</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${job.getEmployees() }" var="employee">
											<tr id="employee_${employee.getUserId() }">
		
												<td class="clickToWorkHistory">${employee.getFirstName() }
														${employee.getLastName() }</td>
														
												<td class="clickToWorkHistory">${employee.getRating() }</td>														
												<c:set var="total" value = "${0 }" />
												<c:forEach items="${employee.getEndorsements()}" var="endorsement">
													<td class="clickToWorkHistory">${endorsement.getCount() }</td>
													<c:set var="total" value="${total + endorsement.getCount() }" />
												</c:forEach>
												<td class="clickToWorkHistory">${total }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								<div id="noEmployees">
									<div>No Employees</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div> <!-- end employes panel body -->
				</div> <!-- end employees panel -->
			</div> <!-- end container -->
		</c:when>
		
		<c:when test="${user.getProfileId() == 2}">
			<c:choose>
				<c:when test="${job.getQuestions().size() > 0}">
				
					<div class="panel panel-danger">
						<div class="panel-heading">Questions</div>
						<div class="panel-body">
							<c:forEach items="${job.getQuestions() }" var="question">
								<div class="panel panel-warning">
									<div class="panel-heading">Question</div>
									<div class="panel-body">
										<textarea class="form-control" rows="3">${question.question}</textarea>
									
										<div class="dropdown" style="margin-top: 10px">
											<input type="hidden"></input> 									
<!-- 											<a class="btn btn-primary dropdown-toggle" type="button" -->
<!-- 												data-toggle="dropdown">Dropdown Example <span -->
<!-- 												class="caret"></span></a> -->
<!-- 											<ul class="dropdown-menu">	 -->
												<c:choose>										
													<c:when test="${question.getFormatId() == 0 }">
<!-- 														<li class="answer-format-item" value="0"><a>Yes</a></li> -->
<!-- 														<li class="answer-format-item" value="1"><a>No</a></li> -->

														<select class="selectpicker" data-style="btn-primary" title="Select an answer">															
															<option>Yes</option>
															<option>No</option>															
														</select>													
													</c:when>
													<c:when test="${question.getFormatId() == 2 }">
														<select class="selectpicker" data-style="btn-primary" title="Select an answer">
															<c:forEach items="${question.getAnswerOptions() }" var="option">
																<option>${option }</option>
															</c:forEach>
														</select>
													</c:when>
													<c:when test="${question.getFormatId() == 3 }">
														<select class="selectpicker" multiple data-style="btn-primary" title="Select an answer">
															<c:forEach items="${question.getAnswerOptions() }" var="option">
																<option>${option }</option>
															</c:forEach>
														</select>
													</c:when>													
												</c:choose>
<!-- 											</ul> -->
										</div>										
									</div>
								
								</div>
							</c:forEach>						
						</div>
					
					</div>
				</c:when>
				<c:otherwise>
					<div class="panel panel-danger">
						<div class="panel-heading">Questions</div>
						<div class="panel-body">	
							No Questions					
						</div>
					
					</div>
				</c:otherwise>
				
				</c:choose>
		
		</c:when>
	</c:choose>
</div>


<script type="text/javascript">

	$(document).ready(function(){
		$('#applicantsTable').DataTable();
		$('#employeesTable').DataTable();
		
		$(".clickToWorkHistory").click(function(){			
			elementId = $(this).parent().attr('id');
			var idBegin = elementId.indexOf("_") + 1;
			var userId =  elementId.substring(idBegin);
			window.location = "../jobs/completed/employee/?userId=" + userId + '&jobId=' + $("#jobId").val();
		})
		
// 		$("#hireApplicant").click(function(){
// 			window.location = "../job/" + $("#jobId").val();
// 		})


    
		
	});
	
	function apply(){
		
		
		
		
	}
	
	
	

</script>

<%@ include file="./includes/Footer.jsp"%>