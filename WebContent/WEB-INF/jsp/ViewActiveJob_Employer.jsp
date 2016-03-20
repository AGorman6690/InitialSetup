<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>
	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
<%-- 		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<%-- 		<script src="<c:url value="/static/javascript/Application.js" />"></script> --%>
		<link rel="stylesheet" type="text/css" href="./static/css/global.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/employerActiveJob.css" />

	</head>

	<input type="hidden" id="userId" value="${user.userId}"/>
	

	<div class="container">
		<div class="job-options btn-group" role="group" aria-label="...">
		  <a href="./job/edit"><button type="button" class="btn btn-default">Edit Job</button></a>
		  <button id="markJobComplete" type="button" class="btn btn-default">Mark Job Complete</button>
		</div>

		<div>
			<div style="width: 500px" class="panel panel-info">
			  <div class="panel-heading">
			    Job Name
			  </div>
			  <div id="jobName" class="panel-body"></div>
			</div>
			
			<div style="width: 500px" class="panel panel-info">
			  <div class="panel-heading">
			    Job Location
			  </div>
			  <div id="jobLocation" class="panel-body"></div>
			  
			  	<div class="job-location-container input-group">
				  <span class="job-location-label input-group-addon" id="sizing-addon2">Street Address</span>
				  <input id="jobStreetAddress" type="text" value="" class="form-control" aria-describedby="sizing-addon2">
				</div>
			  	<div class="job-location-container input-group">
				  <span class="job-location-label input-group-addon" id="sizing-addon2">City</span>
				  <input id="jobCity" type="text" class="form-control" aria-describedby="sizing-addon2">
				</div>		
			  	<div class="job-location-container input-group">
				  <span class="job-location-label input-group-addon" id="sizing-addon2">State</span>
				  <input id="jobState"  type="text" class="form-control" aria-describedby="sizing-addon2">
				</div>	
			  	<div class="job-location-container input-group">
				  <span class="job-location-label input-group-addon" id="sizing-addon2">Zip Code</span>
				  <input id="jobZipCode" type="text" class="form-control"  aria-describedby="sizing-addon2">
				</div>											
			</div>
			
			<div style="width: 500px" class="panel panel-info">
			  <div class="panel-heading">
			    Job Description
			  </div>
			  <div id="jobDescription" class="panel-body"></div>
			</div>
			
		</div>
		
		<br>	
		<div id="container">
		
			<div style="width: 750px" class="panel panel-warning">
			  <div class="panel-heading">
			    Applicants
			  </div>
			  <div id="applicants" class="panel-body"></div>
			</div>
		
			<div style="width: 750px" class="panel panel-warning">
			  <div class="panel-heading">
			    Employees
			  </div>
			  <div id="employees" class="panel-body"></div>
			</div>
		</div>		
	</div>

	<script type="text/javascript">

	
		var activeJob = ${job};
		
		$("#jobName").html(activeJob.jobName);		
		$("#jobStreetAddress").val(activeJob.streetAddress);
		$("#jobCity").val(activeJob.city);
		$("#jobState").val(activeJob.state);
		$("#jobZipCode").val(activeJob.zipCode);
		$("#jobDescription").html(activeJob.description);
		
		
		var jobId = activeJob.id;

		
		appendUsers_ApplicantsAndEmployeesForActiveJob(activeJob, "applicants", "employees")
		
// 		if(unacceptedApplicantsExist(activeJob.applicants)){
// 			appendApplicants("applicants", activeJob.applicants, jobId, function(){});
// 		}else{
// 			$("#applicants").append("<div>No applicants</div>");
// 		}

		
// 		if(activeJob.employees.length > 0){
// 			appendEmployees("employees", activeJob.employees, function(){});
// 		}else{
// 			$("#employees").append("<div>No employees</div>");
// 		}
				
		document.getElementById("markJobComplete").addEventListener("click", function() {
		    markJobComplete(activeJob.id, function(){})
		});


	</script>

<%@ include file="./includes/Footer.jsp" %>