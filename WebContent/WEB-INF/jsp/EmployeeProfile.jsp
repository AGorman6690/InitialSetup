	<%@ include file="./includes/Header.jsp"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/wageNegotiation.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/calendar.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
		
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
	<script src="<c:url value="/static/javascript/Calendar.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js" />"></script> --%>

</head>


<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
	
<body>

	<div class="container">
	
	
	
	
	
	
	
	
	
<!-- 		<div class="section"> -->
		
<!-- 			<div id="availableDays"> -->
<%-- 			<c:forEach items="${availableDays }" var="availableDay"> --%>
<%-- 				${availableDay }* --%>
<%-- 			</c:forEach> --%>
<!-- 			</div> -->
<!-- 			<div class="header"> -->
<!-- 				<span data-toggle-id="availabilityContainer" class="glyphicon glyphicon-menu-down"></span> -->
<!-- 				<span class="header-text">Availability</span> -->
<!-- 			</div>		 -->
<!-- 			<div class="section-body" id="availabilityContainer">	 -->
<!-- 				<div id="calendar" class="calendar-multi-date-no-range" data-number-of-months="2"></div> -->
<!-- 				<div id="saveButtonContainer"> -->
<!-- 					<button id="saveAvailability" class="square-button">Save</button> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
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
		
		
	
	
		<div id="applicationsContainer" class="section">
			<div class="header-container">
				<div class="header">
					<span class="header-text">Applications</span>
				</div>
				<div id="applicationSubHeader" class="sub-header">
					<div class="sub-header-item">
						<div class="item-label">
							Open
						</div>
						<div data-value="0" class="item-value selcted-application-type">
							${openApplicationCount }
						</div>
					</div>
					<div class="sub-header-item">
						<div class="item-label">
							Failed
						</div>
						<div data-value="1" id="failedApplicationCount" class="item-value accent">
							${failedApplicationCount }
						</div>
					</div>
					<div class="sub-header-item">
						<div class="item-label">
							All
						</div>
						<div data-value="2" id="allApplications" class="item-value accent">					
							${applicationDtos.size() }
						</div>
					</div>										
				</div>
			</div>
			
			<div class="section-body">
			<c:choose>
				<c:when test="${applicationDtos.size() > 0 }">						
					<table id="openApplications" class="main-table-style">
					
						<thead>
							<tr class="header-1">		
								<th id="jobName" class="left-edge" colspan="1"></th>
								<th id="wageProposal" class="span left-edge right-edge" colspan="3">Wage Proposal</th>
							</tr>
							
							<tr class="header-2">
								<th id="" class="left-edge">Job Name</th>
								<th id="action-th" class="left-edge">Action</th>
								<th id="amount-th" >Amount</th>
								<th id="status-th" class="right-edge">Status</th>								
							</tr>
						</thead>					

						<tbody>
						
							<c:forEach items="${applicationDtos }" var="dto">
								<tr class="static-row application" data-application-status="${dto.application.status }">
									<td><a class="accent" href="/JobSearch/job/${dto.job.id }">${dto.job.jobName }</a></td>
									<td>
										<c:choose>
											<c:when test="${dto.currentWageProposal.status == 3 }">
												<c:choose>
													<c:when test="${dto.currentWageProposal.proposedToUserId == user.userId }">
														You rejected
													</c:when>
													<c:otherwise>
														Employer rejected
													</c:otherwise>
												</c:choose>												
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${dto.currentWageProposal.proposedToUserId == user.userId }">
														<c:set var="toggleId" value="${dto.application.applicationId }-wp-response"></c:set>
														<span class="accent" data-toggle-id="${toggleId }">Waiting for you</span> 
														<div id="${toggleId }" class="wage-proposal-response-container">
															<div id="${dto.currentWageProposal.id}" class="counter-offer-container">
																<button class="accept-counter">Accept</button>
																<button class="re-counter">Counter</button>		
																<button class="decline-counter">Decline</button>							
																<div class="re-counter-amount-container">
																	<input class="re-counter-amount"></input>
																	<button class="send-counter-offer">Send</button>
																	<button class="cancel-counter-offer">Cancel</button>
																</div>										
															</div>
															<div class="sent-response-notification"></div>																
														</div>
													</c:when>
													<c:otherwise>
														Waiting for employer
													</c:otherwise>
												</c:choose>												
											
											</c:otherwise>
										</c:choose>
									</td>	
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount}"/></td>										
									<td>
										<c:choose>
											<c:when test="${dto.application.status == 0 }">Open</c:when>
											<c:when test="${dto.application.status == 1 }">Failed</c:when>
											<c:when test="${dto.application.status == 2 }">Open</c:when>
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
		
		
		
		<div id="employmentContainer" class="section">
			<div class="header-container">
				<div class="header">
					<span class="header-text">Employment</span>
				</div>
				<div id="employmentSubHeader" class="sub-header">
					<div class="sub-header-item">
						<div class="item-label">
							Past
						</div>
						<div data-job-status="2" class="item-value">
							${pastEmploymentCount }
						</div>
					</div>
					<div class="sub-header-item">
						<div class="item-label">
							Current
						</div>
						<div data-job-status="1" id="failedApplicationCount" class="item-value accent selcted-application-type">
							${currentEmploymentCount }
						</div>
					</div>
					<div class="sub-header-item">
						<div class="item-label">
							Future
						</div>
						<div data-job-status="0" id="allApplications" class="item-value accent">					
							${futureEmploymentCount }
						</div>
					</div>										
				</div>
			</div>
			
			<div id="employment" class="section-body">
			<c:choose>
				<c:when test="${jobs_employment.size() > 0 }">						
					<table id="" class="main-table-style">
					
						<thead>
							
							<tr class="header-2">
								<th id="jobName-e" class="left-edge">Job Name</th>
								<th id="action-e" class="">Start Date</th>
								<th id="status-e" class="right-edge">End Date</th>								
							</tr>
						</thead>					

						<tbody>
						
							<c:forEach items="${jobs_employment }" var="job">
								<tr class="static-row job" data-job-status="${job.status }">
									<td><a class="accent" href="/JobSearch/job/${job.id }">${job.jobName }</a></td>
									<td>${job.stringStartDate }</td>
									<td>${job.stringEndDate }</td>													
								</tr>
							</c:forEach>
													
						</tbody>
					</table>
									
				</c:when>
				<c:otherwise>
					
					Your employment record is non-existent.
				</c:otherwise>
				
			</c:choose>
			</div>	

		</div> <!-- close open applications -->		
		
		
		
		
	</div>
</body>

<script>
var availableDays = [];

	function toggleApplicationVisibility(value){
		
		var applicationStatus;
		var applications = $("#openApplications").find(".application");
		
		$.each(applications, function(){
			
			applicationStatus = $(this).attr("data-application-status");
			
			//Open was clicked
			if(value == "0"){				
				if(applicationStatus < 3) $(this).show();
				else $(this).hide()				
			}
			
			//Failed was clicked
			else if(value == "1"){				
				if(applicationStatus == 3) $(this).show();
				else $(this).hide()				
			}
			
			else $(this).show();
		})

	}
	
	function toggleEmploymentVisibility(clickedJobStatus){
		
		var jobStatus;
		var jobs = $("#employment").find(".job");
		var message;
		
		if(jobs.length == 0){
			if(clickedJobStatus == "0") message = "You do not have future employment";
			else if(clickedJobStatus == "1") message = "You do not have current employment";
			else if(clickedJobStatus == "2") message = "You do not have past employment";
			
		}
		else{			
			$.each(jobs, function(){				
				jobStatus = $(this).attr("data-job-status");				
				if(jobStatus == clickedJobStatus) $(this).show();
				else $(this).hide();								
			})
		}
	}

	$(document).ready(function(){
		
		$("#applicationSubHeader .item-value").click(function(){
			var value = $(this).attr("data-value");
			
			toggleApplicationVisibility(value);
			highlightArrayItem(this, $("#applicationSubHeader").find(".item-value"), "selcted-application-type");
		})
		
		$("#employmentSubHeader .item-value").click(function(){
			var clickedJobStatus = $(this).attr("data-job-status");
			
			toggleEmploymentVisibility(clickedJobStatus);
			highlightArrayItem(this, $("#employmentSubHeader").find(".item-value"), "selcted-application-type");
		})

// 		var dateToday = new Date();
// 		var dateYesterday = new Date();
// 		dateYesterday.setDate(dateToday.getDate() -1);
// 		//Set the user's current availability
// 		var availableDaysHTML = $("#availableDays").html();
// 		var availableDays_string = availableDaysHTML.split("*");		
// 		var formattedDateString;
// 		$.each(availableDays_string, function(){
			
// 			//For whatever reason, "2016-10-31" does not work although
// 			//this is javascript's preferred formatt...
// 			//However, "2016/10/31" does work...
// 			formattedDateString = this.replace("-", "/");			
// 			var date = new Date(formattedDateString);			
// 			if(!isNaN(date) & date > dateYesterday){
// 				availableDays.push(date.getTime());	
// 			}			
// 		})

		
      
// 	var numberOfMonths = getNumberOfMonths($(".calendar-multi-date-no-range"));
	$(".calendar-multi-date-no-range").datepicker({
		 numberOfMonths: 2,
		 minDate: new Date(),
		 onSelect:function(dateText){
			var date = new Date(dateText);
			
			if(isDayAlreadyAdded(date.getTime(), availableDays)){
				availableDays = removeDate(date.getTime(), availableDays); 
			}
			else{
				availableDays.push(date.getTime());  
			}
			
			
		  },
	      beforeShowDay:function(date){
	    	  
			if(isDayAlreadyAdded(date.getTime(), availableDays)){
	    		return [true, "active111"]; 
	    	}
	    	else{
	    		return [true, ""];
	    	}
		
	    	  
	
	      },
	
	      
	    });


		$("#saveAvailability").click(function(){
			//Read the DOM

			
			var availabilityDto = {};
			availabilityDto.stringDays = [];
			
			$.each(availableDays, function(){
				date = new Date;
				date.setTime(this);		
				date = $.datepicker.formatDate("yy-mm-dd", date);
				
				availabilityDto.stringDays.push(date);
			})
			
			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + "/JobSearch/user/availability/update",
				headers : getAjaxHeaders(),
				contentType : "application/json",
				data : JSON.stringify(availabilityDto)
			}).done(function() { 				
//				window.location = "/JobSearch/user/profile";
			}).error(function() {
//				alert("error submit jobs")
//				$('#home')[0].click();
			});			
		})


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

