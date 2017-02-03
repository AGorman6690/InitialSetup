<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/DatePicker.jsp" %>
<%@ include file="../includes/resources/PageContentManager.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/wageNegotiation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/calendar.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee.css" />

<script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script>							
<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>


<div class="container">
	<div class="row">
		<div id="pageContentLinksContainer" class="col-sm-12">
			<span class="page-content-link selected" data-section-id="applicationContainer">Applications</span>
			<span>/</span>
			<span class="page-content-link" data-section-id="employmentContainer">Employment</span>
			<span>/</span>
			<span class="page-content-link" data-section-id="bothContainer">Both</span>
		</div>
	</div>

				
	<div class="row">
		<div class="col-sm-12" id="sectionContainers">			

		
			<div id="applicationDetails">
			<c:forEach items="${applicationDtos }" var="applicationDto">
			
				<div class="application" data-id="${applicationDto.job.id }"
										 data-job-name="${applicationDto.job.jobName }"
										 data-job-id="${applicationDto.job.id}">
										 
				<c:forEach items="${applicationDto.job.workDays }" var="workDay">
					<div class="work-day" data-date="${workDay.stringDate }"></div>
				</c:forEach>
				
				</div>
					
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
							
			
			<div id="applicationContainer" class="section-container">
				<div class="section-body">
					<%@ include file="./Applications_Employee.jsp" %>
				</div>
			</div>
			<div id="employmentContainer" class="section-container">
				<div class="section-body">
					<%@ include file="./Employment_Employee.jsp" %>
				</div>
			</div>	
			<div id="bothContainer" class="section-container">
				<div class="section-body">
					<div id="" class="section">	
						<div id="calendarContainer_both" class="calendar-container">
							<div id="calendar_both" class="calendar" data-min-date="10/01/2016"></div>
						</div>	
						<div id="calendarDetails_applications" class="disabled">
							<div class="header">Jobs applied for with work day on <span id="date_detail">...</span></div>
							<div id="applications_on_day_hover" class="disabled"></div>									
							<div id="applications_on_day_clicked" class="disabled"></div>													
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

