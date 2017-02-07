<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/DatePicker.jsp" %>
<%@ include file="../includes/resources/PageContentManager.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/wageNegotiation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/calendar.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee.css" />

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/oneLine.css" />

<script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script>
<script src="<c:url value="/static/javascript/profile_employee/ToggleCalendar.js" />"></script>							
<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>


<div class="container">
<!-- 	<div class="row"> -->
<!-- 		<div id="pageContentLinksContainer" class="col-sm-12"> -->
<!-- 			<span class="page-content-link selected" data-section-id="applicationContainer">Applications</span> -->
<!-- 			<span>/</span> -->
<!-- 			<span class="page-content-link" data-section-id="employmentContainer">Employment</span> -->
<!-- 			<span>/</span> -->
<!-- 			<span class="page-content-link" data-section-id="bothContainer">Both</span> -->
<!-- 		</div> -->
<!-- 	</div> -->

				
	<div class="row">
		<div class="col-sm-12" id="sectionContainers">			

		
			<div id="applicationDetails">
			<c:forEach items="${applicationDtos }" var="applicationDto">
			
<%-- 				<c:if test="${applicationDto.application.status < 3 && --%>
<%-- 								applicationDto.job.status < 2}"> --%>
								
					<div class="application" data-id="${applicationDto.application.applicationId }"
											 data-job-name="${applicationDto.job.jobName }"
											 data-job-id="${applicationDto.job.id}"
											 data-job-status="${applicationDto.job.status}">
											 
					<c:forEach items="${applicationDto.job.workDays }" var="workDay">
						<div class="work-day" data-date="${workDay.stringDate }"></div>
					</c:forEach>
					
					</div>
<%-- 				</c:if> --%>
					
			</c:forEach>				
			</div>	
			
			<div id="employmentDetails">
			<c:forEach items="${jobDtos_employment_currentAndFuture }" var="jobDto">
			
				<div class="job" data-job-id="${jobDto.job.id}"
								data-job-name="${jobDto.job.jobName }">
										 
				<c:forEach items="${jobDto.workDays }" var="workDay">
					<div class="work-day" data-date="${workDay.stringDate }"></div>
				</c:forEach>
				
				</div>
					
			</c:forEach>				
			</div>	
							
			
<!-- 			<div id="applicationContainer" class="section-container"> -->
<!-- 				<div class="section-body"> -->
				
<!-- 					<div id="calendarContainer_applications" -->
<%-- 						 class="calendar-container ${applicationDtos.size() == 0 ? 'hide' : '' }"> --%>
<!-- 						<div id="calendar_applications" class="calendar" data-min-date="10/01/2016"></div> -->
<!-- 						<div id="jobDetails" > -->
<!-- 							<p class=""><span class="job-name"></span> -->
<!-- 							</p> -->
<!-- 						</div> -->
<!-- 					</div>	 -->
<!-- 					<div id="" class="section">	 -->
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${applicationDtos.size() > 0 }">		 --%>
<!-- 							<table id="openApplications" class="main-table-style"> -->
<%-- 								<%@ include file="./Applications_Employee.jsp" %>				 --%>
<!-- 							</table> -->
											
<%-- 								</c:when> --%>
<%-- 							<c:otherwise> --%>
<!-- 								<div id="noApplications">You have no open applications at this time.</div> -->
<%-- 							</c:otherwise> --%>
						
<%-- 						</c:choose> --%>
<!-- 					</div>						 -->
<!-- 				</div> -->
<!-- 			</div> -->
			
			
<!-- 			<div id="employmentContainer" class="section-container"> -->
<!-- 				<div class="section-body"> -->
<%-- 					<%@ include file="./Employment_Employee.jsp" %> --%>
<!-- 				</div> -->
<!-- 			</div>	 -->
			
			
			<div id="bothContainer" class="section-container">
				<div class="section-body">
					<div id="" class="section">
						<div class="row">
							<div id="toggleCalendarColumn" class="col-sm-12">
								<div id="toggleCalendar" class="do-hide">
								 	<span class="glyphicon glyphicon-menu-left"></span>
								 	<span>Calendar</span>
								 	<span class="glyphicon glyphicon-menu-right"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div id="tableColumn" class="col-sm-12 no-calendar">
								<div id="oneLineContainer" class="section">	
									<c:choose>
										<c:when test="${applicationDtos.size() > 0 }">		
											<table id="openApplications_oneLine" class="main-table-style">
												<%@ include file="./Applications_Employee.jsp" %>				
											</table>
														
										</c:when>
										<c:otherwise>
											<div id="noApplications">You have no open applications at this time.</div>
										</c:otherwise>
									
									</c:choose>
								</div>	
							</div>	
<!-- 						</div> -->
<!-- 						<div class="row"> -->
							<div id="calendarColumn" class="col-sm-6">
								<div id="calendarContainer_both" class="calendar-container">
									<div id="calendar_both" class="calendar" data-min-date="10/01/2016"></div>
								</div>	
								<div id="calendarDetails_applications" class="disabled">
									<div class="header">Jobs applied for with work day on <span id="date_detail">...</span></div>
									<div id="applications_on_day_hover" class="disabled"></div>									
									<div id="applications_on_day_clicked" class="disabled"></div>													
								</div>
							</div>
						</div>
					</div>
<%-- 						<%@ include file="./Applications_Employee.jsp" %> --%>
				</div>
			</div>							
		</div>
	</div>
</div>

<script>

function showSectionContainer(sectionContainerId){
	var sectionContainer = $("body").find("#" + sectionContainerId)[0];
	
	$(sectionContainer).show();
	
}

function selectSideBar(sectionContainerId){
	
	hideSectionContainers(sectionContainerId);
	showSectionContainer(sectionContainerId);
	
	
// 	highlightArrayItemByAttributeValue("data-section-id", sectionContainerId, $("body").find(".side-bar"), "selected-blue");
}





	$(document).ready(function(){
		
		$("span[data-section-id='bothContainer']").click();
	
		
		
		$("#employmentSubHeader .item-value").click(function(){
			var clickedJobStatus = $(this).attr("data-job-status");
			
			toggleEmploymentVisibility(clickedJobStatus);
			highlightArrayItem(this, $("#employmentSubHeader").find(".item-value"), "selcted-application-type");
		})
		
		$(".content-link").click(function(){
	
	
			var sectionContainerId = $(this).attr("data-section-id");
			selectSideBar(sectionContainerId);
			
			highlightArrayItem(this, $("#contentLinksContainer").find(".content-link"), "selected");
		// 	var urlPath = window.location.pathname;
		// 	var obj = {};
		// 	obj.urlPath = urlPath;
		// 	obj.sectionContainerId = sectionContainerId;
			
			
		// 	var newStringArray = addElementToStringArray(obj, sessionStorage.sectionContainerIds);
		// 	sessionStorage.sectionContainerIds = newStringArray;
		
		})
		
	})
      

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

	
function test(){
	
	
	var td = getTdByDayMonthYear($(".calendar"), "1", "0", "2017");	
	var div = "<div class='job one'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "2", "0", "2017");	
	var div = "<div class='job one'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "3", "0", "2017");	
	var div = "<div class='job one'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "4", "0", "2017");	
	var div = "<div class='job one'></div>";	
	$(td).append(div);
	
	
	
	
	
	var td = getTdByDayMonthYear($(".calendar"), "2", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "3", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);	
	
	var td = getTdByDayMonthYear($(".calendar"), "4", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);	
	
	var td = getTdByDayMonthYear($(".calendar"), "5", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "6", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "7", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "8", "0", "2017");	
	var div = "<div class='job two'></div>";	
	$(td).append(div);	
	
	
	
	var td = getTdByDayMonthYear($(".calendar"), "3", "0", "2017");	
	var div = "<div class='job three'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "4", "0", "2017");	
	var div = "<div class='job three'></div>";	
	$(td).append(div);
	
	var td = getTdByDayMonthYear($(".calendar"), "5", "0", "2017");	
	var div = "<div class='job three'></div>";	
	$(td).append(div);	
	
	
	
}




</script>



<%@ include file="../includes/Footer.jsp"%>

