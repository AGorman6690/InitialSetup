<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/User.js" />"></script>
	<link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/css/ratings.css" />
</head>

<body>

	<input type="hidden" id="jobId" value="${job.id}"/>
	<input type="hidden" id="employeeId" />
	<input type="hidden" id="onTimeRating" />
	<input type="hidden" id="workEthicRating" />
	<input type="hidden" id="hireAgainRating" />

	<div class="container">
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Rate Employees
			</div>
			
			<div class="color-panel panel-body"
				style="position: relative; min-height: 135px">
				
				<div class="row">
				
					<div id="employeeContainer" class="col-sm-3">	
						<div id="employees" class="rate-employees list-group">
							<div class="list-group" id="employeesToRate">
								<c:forEach items="${job.employees }" var="employee">
									<a href="#" id="${employee.getUserId() }"
										class="list-group-item margin-hori"	>
										${employee.getFirstName()} ${ employee.getLastName()}</a>
								</c:forEach>
							</div>
						</div>	
						
						<div class="rate-submit">
							<button id="submitRating" type="button" class="btn btn-primary" 
								onclick="submitRatings()">Submit
								Rating</button>
						</div>					
					</div> <!-- end employee column -->
						
					<div id="ratingContainer" class="col-sm-6" style="">
						
						<div class="" style="display:relative">
							
							<div class="rate-group">
								<span class="rate-label label label-success">On Time</span>
								<div id="onTime" class="btn-group" role="group" aria-label="...">
									<button id="onTime-value0" type="button" value="0"
										class="rate-values btn btn-default">Never</button>
									<button id="onTime-value1" type="button" value="2.5"
										class="rate-values btn btn-default">Occasionally</button>
									<button id="onTime-value2" type="button" value="5"
										class="rate-values btn btn-default">Always</button>
								</div>
							</div>
			
							<div class="rate-group">
								<span class="rate-label label label-success">Work Ethic</span>
								<div id="workEthic" class="btn-group" role="group"
									aria-label="...">
									<button id="workEthic-value0" type="button" value="0"
										class="rate-values btn btn-default">Poor</button>
									<button id="workEthic-value1" type="button" value="2.5"
										class="rate-values btn btn-default">Average</button>
									<button id="workEthic-value2" type="button" value="5"
										class="rate-values btn btn-default">Excellent</button>
								</div>
							</div>
			
							<div class="rate-group">
								<span class="rate-label label label-success">Hire Again</span>
								<div id="hireAgain" class="btn-group" role="group"
									aria-label="...">
									<button id="hireAgain-value0" value="0" type="button"
										class="rate-values btn btn-default">No</button>
									<button id="hireAgain-value1" value="2.5" type="button"
										class="rate-values btn btn-default">Maybe</button>
									<button id="hireAgain-value2" value="5" type="button"
										class="rate-values btn btn-default">Yes</button>
								</div>
							</div>		
																		
						</div> <!-- end rate criteria -->	
											
						<div style="margin-top: 25px; display:block" class="">
							<span class="rate-label label label-success">Comments</span>
							<div style="margin-top:15px">
								<textarea name="comments" class="form-control"
									id="comments" rows="3" placeholder="Comments"></textarea>
							</div>
						</div>
						
					</div> <!-- end rating column -->
					
					<div class="col-sm-3">
					
						<div id="categories" class="pull-right">
							<h3 style="margin: 0px 0px 0px 0px; display:block">
								<span style="margin: 0px 0px 5px 0px; display:block" class="label label-primary">
								Endorsements</span></h3>
							<c:forEach items="${job.categories}" var="category">
									<button style="margin-bottom:5px; display:block" type="button" 
										id="${category.getId()}"	class="btn btn-secondary"
										onClick="toggleEndorsement(this)">
										${category.getName()}</button>
							
							</c:forEach>				
						</div> <!-- end categories -->
						
					</div> <!-- end category column -->
					
				</div> <!-- end row -->
			</div> <!-- end panel body -->
		</div> <!-- end entire panel -->
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