<%@ include file="./includes/Header.jsp"%>

<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
<%-- <script	src="<c:url value="/static/External/jquery-ui.min.js" />"></script>    --%>
</head>

<!-- <script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script> -->

<script   src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"   integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
	
<body>

	<div class="container">
	
		<div class="section">
			<div id="calendar"></div>
		</div>
		<!-- ****** Failed Wage Negotiations -->
		<c:choose>
			<c:when test="${failedWageNegotiationDtos.size() > 0 }">
				<div id="failedWageNegotiationsContainer" class="section">
					<div class="header">
						<span class="header-text">Failed Wage Negotiations</span>
					</div>
					
					<div class="section-body">
						<table id="openApplications">
							<thead>
								<tr>
									<th>Job Name</th>
									<th>Result</th>
								</tr>
							</thead>
							<tbody>
							
								<c:forEach items="${failedWageNegotiationDtos }" var="dto">
								<tr class="static-row">
									<td><a class="accent" href="/JobSearch/job/${dto.job.id }">${dto.job.jobName }</a></td>
	
									<td>
										<fmt:formatNumber var="amount" type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.failedWageProposal.amount}"/>
										<c:choose>
											<c:when test="${user.userId == dto.failedWageProposal.proposedByUserId }">
												<div>Your ${amount } proposal was rejected</div>
											</c:when>
											<c:otherwise>
												<div>You rejected a ${amount } offer</div>
											</c:otherwise>
										</c:choose>										
									</td>					
								</tr>
							</c:forEach>
														
							</tbody>
						</table>						

					</div>
				</div> <!-- close failed wage negotiations -->				
			</c:when>		
		</c:choose>
		
		
	
	
		<div id="openApplicationsContainer" class="section">
			<div class="header">
				<span class="header-text">Open Applications</span>
			</div>
			
			<div class="section-body">
			<c:choose>
				<c:when test="${openApplicationResponseDtos.size() > 0 }">						
					<table id="openApplications">
						<thead>
							<tr>
								<th>Job Name</th>
								<th>Application Status</th>
		<!-- 						<th>Desired Wage</th> -->
								<th>Wage Status</th>
							</tr>
						</thead>
						<tbody>
						
							<c:forEach items="${openApplicationResponseDtos }" var="dto">
							<tr class="static-row">
								<td><a class="accent" href="/JobSearch/job/${dto.job.id }">${dto.job.jobName }</a></td>
								<td>
									<c:choose>
										<c:when test="${dto.application.status == 0  }">Waiting for response</c:when>
										<c:when test="${dto.application.status == 1  }">Declined</c:when>
										<c:when test="${dto.application.status == 2  }">Being considered</c:when>
										<c:when test="${dto.application.status == 3  }">Accepted</c:when>
									</c:choose>							
								</td>
								
		<!-- 						<td> -->
		<%-- 							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentDesiredWage}"/> --%>
		<!-- 						</td> -->
								
								<!-- set the wage status -->
								<td>
									<c:choose>
										<c:when test="${dto.currentWageProposal.status == 1 }">
										<!-- ****** If the current wage proposal has been accepted-->
											<div><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/> has been accepted</div>
										</c:when>						
										<c:when test="${dto.currentWageProposal.proposedByUserId != user.userId }">
			<!-- 						******* If employer has made the last wage proposal -->
											<div id="${dto.currentWageProposal.id}" class="counter-offer-container">
												
												<div class="offer-context">
													Employer offered   
													<span id="amount">
														<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/>
													</span>
												</div>									
												<div class="counter-offer-response">
													<button class="accept-counter">Accept</button>
													<button class="re-counter">Counter</button>		
													<button class="decline-counter">Decline</button>							
													<div class="re-counter-amount-container hide-element">
														<input class="re-counter-amount"></input>
														<button class="send-counter-offer">Send</button>
														<button class="cancel-counter-offer">Cancel</button>
													</div>										
												</div>	
											</div>
											<div class="sent-response-notification"></div>	
										</c:when>
										<c:otherwise>
			<!-- 						******* Otherwise the applicant has made the last wage proposal -->							
												<div class="offer-context">
													Requested   
													<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/>
												</div>									
										</c:otherwise>
									</c:choose>
								</td>					
							</tr>
						</c:forEach>
													
						</tbody>
					</table>
									
				</c:when>
				<c:otherwise>
					You have no open applications at this time.
				</c:otherwise>
				
			</c:choose>
			</div>	

		</div> <!-- close open applications -->
		
		<c:if test="${yetToStartJobs.size() > 0 }">	
			<div id="notYetStartedJobsContainer" class="section">
				<div class="header">
					<span class="header-text">Jobs Hired For - Waiting To Begin</span>
				</div>
				
				<div class="section-body">				
					<table id="yetToStartJobs">
						<thead>
							<tr>
								<th>Job Name</th>
								<th>Start Date</th>
								<th>End Date</th>
							</tr>
						</thead>
						<tbody>		
							<c:forEach items="${yetToStartJobs }" var="yetToStartJob">
								<tr class="static-row">
									<td><a class="accent">${yetToStartJob.jobName }</a></td>
									<td>${yetToStartJob.startDate }</td>
									<td>${yetToStartJob.endDate }</td>
								</tr>							
							</c:forEach>
						</tbody>
					</table>		
				</div>		
			</div><!-- close active jobs container -->						
		</c:if>

		<div id="activeJobsContainer" class="section">
			<div class="header">
				<span class="header-text">Jobs In Process</span>
			</div>
			
			<div class="section-body">
			<c:choose>
				<c:when test="${activeJobs.size() > 0 }">					
					<table id="activeJobs">
						<thead>
							<tr>
								<th>Job Name</th>
								<th>Start Date</th>
								<th>End Date</th>
							</tr>
						</thead>
						<tbody>
		
							<c:forEach items="${activeJobs }" var="activeJob">
								<tr class="static-row">
									<td><a class="accent">${activeJob.jobName }</a></td>
									<td>${activeJob.startDate }</td>
									<td>${activeJob.endDate }</td>
								</tr>							
							</c:forEach>
						</tbody>
					</table>							
				</c:when>
				<c:otherwise>
					<div>You have no active jobs at this time.</div>
				</c:otherwise>							
			</c:choose>	
			</div>	
		</div><!-- close active jobs container -->		
		
		<div id="completedJobsContainer" class="section">
			<div class="header">
				<span class="header-text">Completed Jobs</span>
			</div>
			
			<div class="section-body">
			<c:choose>
				<c:when test="${completedJobs.size() > 0 }">					
					<table id="completedJobs">
						<thead>
							<tr>
								<th>Job Name</th>
								<th>End Date</th>
							</tr>
						</thead>
						<tbody>		
							<c:forEach items="${completedJobs }" var="completedJob">
								<tr class="static-row">
									<td><a class="accent">${completedJob.jobName }</a></td>
									<td>${completedJob.endDate }</td>
								</tr>							
							</c:forEach>
						</tbody>
					</table>							
				</c:when>
				<c:otherwise>
					<div>You have no completed jobs at this time.</div>
				</c:otherwise>
			</c:choose>	
			</div>		
		</div><!-- close completed jobs container -->
	</div>
</body>

<script>
	$(document).ready(function(){
		
		var dates = [];
		dates.push(new Date(2016, 8 , 30))

		
      	$("#calendar").datepicker({
    	      numberOfMonths: 3,
//     	      showButtonPanel: true,
//     	      multidate: true,
    	      beforeShowDay:function(date){
    	    	  
    	    	var aDate =   new Date(2016, 8 ,12);
    	    	  if(date.getTime() == aDate.getTime()){
    	    		 return [true, "test111"];

    	    	  }else{
    	    		  return [true, ""];
    	    	  }
    	      }
    	      
    	    });
		



		
		
		$('#availableDays').datepicker({
			toggleActive: true,
			clearBtn: true,
			todayHighlight: true,
			startDate: new Date(),
			multidate: true
		});

		//Display employee's availability.
		//This seems hackish...
		var dates = [];
		var str = $("#arrayDates").val();
		str = str.substring(1, str.length -1);
		dates = str.split(",");
		var formatedDates = [];
		for(var i = 0; i < dates.length; i++){
			var date = dates[i];
			date =  date.trim();
			var day = date.substring(8, 10);
			var month = date.substring(5, 7);
			var year = date.substring(0, 4);
			formatedDates.push(month + "-" + day + "-" + year);
		}

		$("#availableDays").datepicker('setDates', formatedDates);

	})
	

	function updateAvailability(){


		var availabilityDTO = {};
		availabilityDTO.userId = $("#userId").val();
		availabilityDTO.stringDays = $("#availableDays").datepicker('getDates');

		var headers = {};
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");

		$.ajax({
			type : "POST",
			url : environmentVariables.LaborVaultHost + "/JobSearch/user/availability/update",
			headers : headers,
			contentType : "application/json",
			dataType : "application/json", // Response
			data : JSON.stringify(availabilityDTO)
		}).done(function() {
// 			$('#home')[0].click();
		}).error(function() {
// 			$('#home')[0].click();
		});

	}

</script>



<%@ include file="./includes/Footer.jsp"%>

