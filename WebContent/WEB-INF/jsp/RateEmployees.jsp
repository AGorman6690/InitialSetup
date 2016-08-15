<%@ include file="./includes/Header.jsp"%>

<head>
<%-- 	<script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<!-- 	<link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/css/ratings.css" /> -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rateEmployees.css" />
</head>

<body>

	<input type="hidden" id="jobId" value="${job.id}"/>
	<input type="hidden" id="employeeId" />
	<input type="hidden" id="onTimeRating" />
	<input type="hidden" id="workEthicRating" />
	<input type="hidden" id="hireAgainRating" />

	<div class="container">
		<div class="row">
			<div class="col col-sm-4">
				<div id="employees" class="column">	
					<div class="header">Employees</div>
					<div>
						<ul>
						<c:forEach items="${job.employees }" var="employee">
							<li>${employee.firstName }</li>
						</c:forEach>
						</ul>
					</div>
				</div>			
			</div>
			<div class="col col-sm-4">
				<div id="ratings" class="column">	
					<div class="header">Ratings</div>
					<div>
						
					</div>
				</div>			
			</div>
			<div class="col col-sm-4">
				<div id="endorsements" class="column">	
					<div class="header">Endorsements</div>
				</div>			
			</div>			
		</div>
		
	</div> <!-- end container -->
</body>

<script>
	
	var activeButtonClass = "button-active";
	$(".rate-values").click(function() {
				activateButton($(this))
			})
			
	$("#employeesToRate a").click(function(){
			activateEmployee($(this));
		}
	)
	
	
	function toggleEndorsement(event){
// 		alert($(event).attr('id'))
		
		if($(event).hasClass("btn-secondary")){
			$(event).removeClass("btn-secondary")
			$(event).addClass("btn-success")
		}else{
			$(event).addClass("btn-secondary")
			$(event).removeClass("btn-success")
		}
		
	}
	
	
	function submitRatings(){
	
	//**********************************************
	//This is very hardcoded.
	//Eventually pretty it up. 
	//**********************************************
		
		var jobId = $("#jobId").val();
		var employeeId = getActivatedEmployee();
		var rateCriteria = [];
	
		var rateCriterion_0 = {};
		rateCriterion_0.jobId = jobId;
		rateCriterion_0.employeeId = employeeId;
		rateCriterion_0.rateCriterionId = 1;
		rateCriterion_0.value = getActivatedButtonValue("workEthic");
		rateCriteria.push(rateCriterion_0);
		
		var rateCriterion_1 = {};
		rateCriterion_1.jobId = jobId;
		rateCriterion_1.employeeId = employeeId;
		rateCriterion_1.rateCriterionId = 2;
		rateCriterion_1.value = getActivatedButtonValue("onTime");
		rateCriteria.push(rateCriterion_1);
		
		var rateCriterion_2 = {};
		rateCriterion_2.jobId = jobId;
		rateCriterion_2.employeeId = employeeId;
		rateCriterion_2.rateCriterionId = 3;
		rateCriterion_2.value = getActivatedButtonValue("hireAgain");
		rateCriteria.push(rateCriterion_2);
		
		var categories = $("#categories").find("button");
		var endorsements = [];
		for(var i = 0; i < categories.length; i++){
			if($(categories[i]).hasClass("btn-success")){
				var endorsement = {};
				endorsement.jobId = jobId;
				endorsement.userId = employeeId;
				endorsement.categoryId = categories[i].id;
				endorsements.push(endorsement);
			}			
		}	

		var ratingDTO = {};		
		ratingDTO.jobId = jobId;
		ratingDTO.employeeId = employeeId;
		ratingDTO.rateCriteria = rateCriteria;
		ratingDTO.endorsements = endorsements;
		ratingDTO.comment = $("#comments").val();

		rateEmployee(ratingDTO);
				
	}
		
	function getActivatedButtonValue(containerId){
		var buttons = $("#" + containerId + " :button");
		for(var i=0; i<buttons.length; i++){
			if( $(buttons[i]).hasClass(activeButtonClass) == 1){
				return $(buttons[i]).val();
			}
		}
	}
	
	function getActivatedEmployee(){
		var employees = $("#employeesToRate").find("a");
		for(var i=0; i<employees.length; i++){
			if($(employees[i]).hasClass('active')){
				return $(employees[i]).attr('id');
			}
			
		}
	}
	
	function activateButton(clickedButton) {
		var otherButtons = $(clickedButton).siblings("button")
		for (var i = 0; i < otherButtons.length; i++) {
			if ($(otherButtons[i]).hasClass(activeButtonClass) == 1) {
				$(otherButtons[i]).removeClass(activeButtonClass);
				$(otherButtons[i]).addClass('btn-default');
			}
		}
		$(clickedButton).removeClass('btn-default');
		$(clickedButton).addClass(activeButtonClass);
	}
	
	function activateEmployee(clickedEmployee) {
		
		var otherEmployees = $(clickedEmployee).siblings("a")
		for (var i = 0; i < otherEmployees.length; i++) {
			//NOTE: "active" is a bootstrap class
			if ($('#' + otherEmployees[i].id).hasClass('active') == 1) {
				$('#' + otherEmployees[i].id).removeClass('active');
			}
		}
		$(clickedEmployee).addClass('active');
	}
	
	function activateItem(itemId, containerId, className) {
		var items = $("#" + containerId).find("a")
		for (var i = 0; i < items.length; i++) {
			if ($('#' + items[i].id).hasClass(className) == 1) {
				$('#' + items[i].id).removeClass(className);
			}
		}
		$('#' + itemId).addClass(className);
	}
</script>

<%@ include file="./includes/Footer.jsp"%>