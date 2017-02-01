	<%@ include file="../includes/Header.jsp"%>
	<%@ include file="../includes/ScriptsAndLinks_DatePicker.jsp" %>
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/wageNegotiation.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/datepicker.css" />
<!-- 	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/calendar.css" /> -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/calendar.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee.css" />
	<script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script>

<%-- 	<script src="<c:url value="/JobSearch/WebContent/static/javascript/profile_employee/Calendar_Applications.js" />"></script> --%>
												 
		
<%-- 	<script src="<c:url value="/static/javascript/Utilities.js" />"></script> --%>
	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
<%-- 	<script	src="<c:url value="/static/javascript/SideBar.js" />"></script>	 --%>

</head>


<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
	
<body>

	<div class="container">
		<div class="row">
			<div id="contentLinksContainer" class="col-sm-12">
				<span class="content-link selected" data-section-id="applicationContainer">Applications</span>
				<span>/</span>
				<span class="content-link" data-section-id="employmentContainer">Employment</span>
				<span>/</span>
				<span class="content-link" data-section-id="bothContainer">Both</span>
<!-- 				<div id="general" class="first side-bar selected-blue" data-section-id="applicationContainer">Applications</div> -->
<!-- 				<div id="date" class="side-bar" data-section-id="employmentContainer">Employment</div> -->
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
					
<!-- 					<div class="application" data-id="1" data-job-name="3"> -->
					
<!-- 						<div class="work-day" data-date="2017/01/30"></div> -->
<!-- 						<div class="work-day" data-date="2017/01/31"></div> -->

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
			</div>
		</div>
	</div>
</body>

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

