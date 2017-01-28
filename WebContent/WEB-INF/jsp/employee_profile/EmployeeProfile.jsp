	<%@ include file="../includes/Header.jsp"%>
	<%@ include file="../includes/ScriptsAndLinks_DatePicker.jsp" %>
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/wageNegotiation.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/calendar.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
	
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/calendar.css" />
		
<%-- 	<script src="<c:url value="/static/javascript/Utilities.js" />"></script> --%>
	<script src="<c:url value="/static/javascript/WageNegotiation.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/Calendar.js" />"></script> --%>
<%-- 	<script	src="<c:url value="/static/javascript/SideBar.js" />"></script>	 --%>

</head>


<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
	
<body>

	<div class="container">
		<div class="row">
			<div id="sideBarContainer" class="col-sm-2">
				<div id="general" class="first side-bar selected-blue" data-section-id="applicationContainer">Applications</div>
				<div id="date" class="side-bar" data-section-id="employmentContainer">Employment</div>
			</div>
			

					
			
			<div class="col-sm-10" id="sectionContainers">
			
				<div id="calendarContainer">
					<div class="calendar"></div>
				</div>
			
				<div id="jobDetails_applications">
				<c:forEach items="${applicationDtos }" var="applicationDto">
				
					<div class="job" data-id="${applicationDto.job.id }" data-name="${applicationDto.job.jobName }">
					<c:forEach items="${applicationDto.job.workDays }" var="workDay">
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
			</div>
		</div>
	</div>
</body>

<script>
var availableDays = [];

function setJob


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


	function toggleApplicationVisibility(clickedStatus){
		
		var applicationStatus;
		var applications = $("#openApplications").find(".application");
		
		$.each(applications, function(){
			
			//Status values:
			//0: submitted
			//1: declined
			//2: considered
			//3: accepted
			
			applicationStatus = $(this).attr("data-application-status");
			
			
			if(clickedStatus == "all"){
				
				$(this).show();
			}
			else if(clickedStatus == "open"){				
				if(applicationStatus == 0 || applicationStatus == 2) $(this).show();
				else $(this).hide()				
			}			
			else if(clickedStatus == "failed"){				
				if(applicationStatus == 1) $(this).show();
				else $(this).hide()				
			}
			
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
		

		var options = {};
		options.hideIfNoPrevNext = false;
		
		initReadOnlyCalendar($(".calendar"));		
		
		test();
		
		
		
		
		
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



<%@ include file="../includes/Footer.jsp"%>

