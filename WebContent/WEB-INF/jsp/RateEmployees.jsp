<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>
	
	<head>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Ratings.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
			
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/global.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
	</head>


	<body>
	
		<input type="hidden" id="jobId"/>
		<input type="hidden" id="employeeId"/>
		<input type="hidden" id="onTimeRating"/>
		<input type="hidden" id="workEthicRating"/>
		<input type="hidden" id="hireAgainRating"/>
	

		
		<div class="container">
			<div style="width: 750px" class="panel panel-success">
				  <div class="panel-heading">
				  	Rate Employees
				  </div>
				  
				<div class="color-panel panel-body" style="position: relative; min-height: 135px">	
					<div id="employees" class="rate-employees list-group"></div>
					
					<div class="rate-group criteria1">
						<span class="rate-label label label-success">On Time</span>
						<div id="onTime" class="btn-group " role="group" aria-label="...">				
						  <button id="onTime-value0" type="button" value="0" class="rate-values btn btn-default">Never</button>
						  <button id="onTime-value1" type="button" value="2.5" class="rate-values btn btn-default">Occasionally</button>
						  <button id="onTime-value2" type="button" value="5" class="rate-values btn btn-default">Always</button>
						</div>
					</div>
						
					<div class="rate-group criteria2">
						<span class="rate-label label label-success">Work Ethic</span>
						<div id="workEthic" class="btn-group " role="group" aria-label="...">				
						  <button id="workEthic-value0" type="button" value="0" class="rate-values btn btn-default">Poor</button>
						  <button id="workEthic-value1" type="button" value="2.5" class="rate-values btn btn-default">Average</button>
						  <button id="workEthic-value2" type="button" value="5" class="rate-values btn btn-default">Excellent</button>
						</div>
					</div>
					
					<div class="rate-group criteria3">
						<span class="rate-label label label-success">Hire Again</span>
						<div id="hireAgain" class="btn-group " role="group" aria-label="...">				
						  <button id="hireAgain-value0" value="0" type="button" class="rate-values btn btn-default">No</button>
						  <button id="hireAgain-value1" value="2.5" type="button" class="rate-values btn btn-default">Maybe</button>
						  <button  id="hireAgain-value2" value="5" type="button" class="rate-values btn btn-default">Yes</button>
						</div>
					</div>
						
					<div class="rate-submit">
						<button id="submitRating" type="button" class="btn btn-primary">Submit Rating</button>
					</div>
				</div>				
			</div>		
		</div>		
	</body>

	<script>
	
		var jobId = parent.document.URL.substring(parent.document.URL.indexOf('?jobId=') + 7, parent.document.URL.length);
		$("#jobId").val(jobId)
	
		
		getEmployeesByJob(jobId, function(employees){
			appendUsers_RateEmployees("employees", employees, function(){})
			
		})
		
		
	
	
	</script>
	
	
	

<%@ include file="./includes/Footer.jsp" %>