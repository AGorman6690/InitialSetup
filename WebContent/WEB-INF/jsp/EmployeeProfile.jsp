<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
</head>

<input type="hidden" id="userId" value="${user.userId}" />

<body>
	<input type="hidden" id="userId" value="${user.userId}">
	<div class="container">
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Open Applications</div>			
			<div id="appliedTo" class="color-panel panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>Job Name</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${user.getJobsAppliedTo() }" var="job">
						<c:if test="${job.getIsActive() == 1 }">
							<td>${job.getJobName() }</td>
						</c:if>						
					</c:forEach>				
					</tbody>
				</table>			
			
			</div>
		</div>

		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Current Employment</div>
			<div id="hiredFor" class="color-panel panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>Job Name</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${user.getJobsHiredFor() }" var="job">
						<c:if test="${job.getIsActive() == 1 }">
							<td>${job.getJobName() }</td>
						</c:if>	
					</c:forEach>				
					</tbody>
				</table>			
			</div>
		</div>
		
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Completed Jobs</div>
			<div id="hiredFor" class="color-panel panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>Job Name</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${user.getCompletedJobs() }" var="completedJobDTO">
						<td>${completedJobDTO.getJob().getJobName() }</td>
					</c:forEach>				
					</tbody>
				</table>			
			</div>
		</div>		
		
		<input type="hidden" id="arrayDates" value='${user.getAvailableDates() }'>
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Available Days to Work</div>
			<div class="color-panel panel-body">
				<div id='availableDays' class="input-group date" style="width: 250px">
		  		</div>			
			<button style="display: block; margin-top: 10px" type="button" 
				class="btn btn-success" onclick="updateAvailability()">Update</button>			
			</div>
		</div>		
		
	</div>
</body>

<script>
	$(document).ready(function(){
		
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
			url : "http://localhost:8080/JobSearch/user/availability/update",
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

