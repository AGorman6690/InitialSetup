<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/Jobs.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/Questions.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/ChangeForm.js"/>"></script> --%>

<!-- 	<link rel="stylesheet" type="text/css" href="./static/css/categories.css" /> -->
<!-- 	<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" /> -->
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />		
	
	<script src="<c:url value="/static/javascript/TimePickerUtilities.js"/>"></script>	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/calendar.css" />		
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob_durations.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob_ColumnLayout.css" />
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
	
	<script	src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js" />"></script>
	<script	src="<c:url value="/static/javascript/PostJob_durations.js" />"></script>
	<script	src="<c:url value="/static/javascript/Calendar.js" />"></script>
	<script	src="<c:url value="/static/javascript/SideBar.js" />"></script>
		
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
		
</head>

<body>
						

	<div class="container">	
<!-- 	<a href="/JobSearch/postJob-with-cart">Post Job - Method 2 - With Cart</a> -->
<!-- 	<div><a href="/JobSearch/postJob-without-cart">Post Job - Method 3 - WithOUT Cart</a></div> -->
	
		<div class="row">
			<div id="sideBarContainer" class="col-sm-2">
				<div id="general" class="first side-bar selected-blue" data-section-id="generalContainer">General</div>
				<div id="date" class="side-bar" data-section-id="datesContainer">Dates</div>
				<div id="location" class="side-bar" data-section-id="locationContainer">Location</div>
				<div id="compensation" class="side-bar" data-section-id="compensationContainer">Compensation</div>
				<div id="categories" class="side-bar" data-section-id="categoriesContainer">Categories</div>
				<div id="questions" class="side-bar" data-section-id="questionsContainer">Questions</div>
				<div id="employeeSkills" class="side-bar" data-section-id="employeeSkillsContainer">Employee Skills</div>					
			</div>
			
			<div class="col-sm-10" id="sectionContainers">
				<div id="generalContainer" class="section-container">
					<div class="section-body">
						<h4>General</h4>
						<div class="body-element-container form-group ">

							<div class="input-container">
								<div id="invalidJobName" class="invalid-message">Job names must be unique</div>
								<div class="row">
									<div class="col-sm-3">
										<label for="description"
											class="form-control-label">Employment Type</label>
									</div>
									<div class="col-sm-9">
										<div id="employmentTypeContainer">
											<div class="radio">
											  <label><input value="0" id="employmentType_employee" type="radio" name="employmentType">Employee</label>
											</div>
											<div class="radio">
											  <label><input value="1" id="employmentType_contractor" type="radio" name="employmentType">Contractor</label>
											</div>
										</div>						
									</div>			
								</div>
							</div>
							
							<div class="input-container">
								<div id="invalidJobName" class="invalid-message">Job names must be unique</div>
								<div class="row">
									<div class="col-sm-3">
										<label for="name"
											class="form-control-label">Job Name</label>
									</div>
									<div class="col-sm-9">
										<input name="name" type="text" class="form-control"
											id="name" value=""></input>								
									</div>			
								</div>
							</div>
							
							<div class="input-container">
								<div id="invalidJobName" class="invalid-message">Job names must be unique</div>
								<div class="row">
									<div class="col-sm-3">
										<label for="description"
											class="form-control-label">Job Description</label>
									</div>
									<div class="col-sm-9">
										<textarea name="description" class="form-control"
											id="description" rows="6"></textarea>							
									</div>			
								</div>
							</div>



						
							
						</div>					
					</div>	
				</div>
				
				<div id="datesContainer" class="section-container">
					<div class="section-body">
						<h4>Dates</h4>
						<div class="body-element-container form-group ">
						<div id="durationQuestion" class="section-sub-head">How long will the job last?</div>
							<div id="durationsContainer" class="body-element-container">
								
								<div id="durations">
									<div id="hours" class="duration" data-id="1">Hours</div>
									<div id="days" class="duration" data-id="2">Days</div>
<!-- 									<div id="weeks" class="duration" data-id="3">Weeks</div> -->
									<div id="months" class="duration" data-id="4">Months</div>
<!-- 									<div id="years" class="duration" data-id="5">Years</div> -->
									<div id="hopefullyForever" class="duration" data-id="6">Hopefully Forever</div>
								</div>
							</div>
						</div>	
						
						<div id="durationFollowUp">
							<div class="body-element-container form-group ">
								<div id="calendarSelectionNote" class="section-sub-head"></div>
								<div class="input-container body-element-container">								
									<div id="calendarContainer">
										<div id="calendar-single-day" data-is-showing-job="0">
										</div>
										<div id="calendar-multi-day" data-is-showing-job="0">
										</div>											
										<button class="square-button" id="clearCalendar">Clear</button>
									</div>
								</div>															
							</div>			
							
							<div class="body-element-container form-group ">
								<div class="section-sub-head">Specify start and end times</div>
								<div id="timeContainer-SingleDate" class="body-element-container input-container" >
									
									<div class="input-container">
										<label for="startTime"
											class="form-control-label">Start Time</label>
										<input id="startTime-singleDate" name="startTime" type="text" class="form-control time-input"
											value=""></input>
									</div>
									<div class="input-container">
										<label for="endTime"
											class="form-control-label">End Time</label>
										<input id="endTime-singleDate" name="endTime" type="text" class="form-control time-input"
											value=""></input>
									</div>
								</div>		
							</div>	
												
							<div class="body-element-container contractor-content">
								<div class="section-sub-head">Allow contractor to submit alternate dates?</div>
								<div class="body-element-container" >
									<div class="radio">
									  <label><input type="radio" name="allow-alternate-days" value="0">No</label>
									</div>
									<div class="radio">
									  <label><input type="radio" name="allow-alternate-days" value="1">Yes</label>
									</div>
								</div>		
							</div>		
							
							<div class="body-element-container contractor-content">
								<div class="section-sub-head">Allow contractor to submit alternate times?</div>
								<div class="body-element-container" >
									<div class="radio">
									  <label><input type="radio" name="allow-alternate-days" value="0">No</label>
									</div>
									<div class="radio">
									  <label><input type="radio" name="allow-alternate-days" value="1">Yes</label>
									</div>
								</div>		
							</div>												
						</div>																
					</div>	
				</div>
				
				<div id="locationContainer" class="section-container">
					<div class="section-body">
						<h4>Location</h4>
						<div class="body-element-container form-group ">
							<div class="input-container">
								<label for="streetAddress"
									class="form-control-label">Street Address</label>
								<input name="streetAddress" type="text" class="form-control"
									id="streetAddress" value=""></input>
							</div>
							<div class="input-container">
								<label for="city"
									class="form-control-label">City</label>
								<input name="city" type="text" class="form-control"
									id="city" value=""></input>
							</div>
							<div class="input-container">
								<label for="streetAddress"
									class="form-control-label">State</label>
								<select id="state" name="state" class="form-control"></select>	
							</div>
							<div class="input-container">
								<label for="zipCode"
									class="form-control-label">Zip Code</label>
								<input name="zipCode" type="text" class="form-control"
									id="zipCode" value=""></input>
							</div>
						</div>					
					</div>				
				</div>
				
				<div id="compensationContainer" class="section-container">
					<div class="section-body">
						<h4>Compensation</h4>
						<div class="body-element-container">
							<div>
															
								<div class="body-element-container input-container contractor-content">
								
									<div class="row">
										<div class="col-sm-2">
											<label for="description"
												class="form-control-label">Method</label>
										</div>
										<div class="col-sm-10">
											<div class="radio">
											  <label><input type="radio" name="pay-method">By the hour</label>
											</div>
											<div class="radio">
											  <label><input type="radio" name="pay-method">By the job</label>
											</div>
											<div id="pay-method-salary" class="radio">
											  <label><input type="radio" name="pay-method">Salary</label>
											</div>						
										</div>			
									</div>								
								</div>
							</div>
							<div>
							
								<div class="body-element-container">	
									<div class="row">
										<div class="col-sm-2">
											<label for="description"
												class="form-control-label">Range</label>
										</div>
										<div class="col-sm-10">
											<div class="radio">
											  <label><input value="0" type="radio" name="pay-range">No, accept all offers</label>
											</div>
											<div class="radio">
											  <label><input value = "1" type="radio" name="pay-range">Yes, only accept offers between:</label>
											</div>	
											<div id="payRangeContainer"> 
												<div id="minPayContainer" class="form-group">
													<label for="startTime"
														class="form-control-label">Min</label>
													<input id="minPay" name="" type="text" class="form-control"
														value=""></input>												
												</div>
												<div id="maxPayContainer" class="form-group">
													<label for="startTime"
														class="form-control-label">Max</label>
													<input id="maxPay" name="" type="text" class="form-control"
														value=""></input>												
												</div>	
											</div>																
										</div>			
									</div>																				 									
								</div>		
								
							</div>
						</div>												
					</div>					
				</div>
				
				<div id="categoriesContainer" class="section-container">
					<div class="section-body">
						<h4>Categories</h4>
						<div class="job-sub-info">
							<div id="invalidCategoryInput-None" class="invalid-message">At least one category must be selected</div>
							<div id="invalidCategoryInput-TooMany" class="invalid-message">A maximum of five categories can be selected</div>										
<!-- 									Eventaully render this with jstl -->
<!-- 									//********************************************************************************* -->
<!-- 									//********************************************************************************* -->
						
							<div id="selectedCategories">
							</div>
						
						
							<ul id="categoryTree" class="list-group ">
								<li class="category-list-item list-group-item"
									data-cat-id="1" data-super-cat-id="0" data-level="0"
									data-sub-categories-set="0">
									<span style="float:left" class="category-name level-zero">Concrete</span>	
									<span style="font-size: 1em; float:left"
										class="add-category glyphicon glyphicon-plus"></span>																					 
									<span style="font-size: 1em; display: inline-block" 
										class="show-sub-categories glyphicon glyphicon-menu-down"></span>
								
								</li>
							
								<li class="category-list-item list-group-item"
								data-cat-id="3" data-super-cat-id="0" data-level="0"
								data-sub-categories-set="0">
									<span class="category-name level-zero">Construction</span>	
									<span style="font-size: 1em" 
										class="add-category glyphicon glyphicon-plus"></span>												 
									<span style="font-size: 1em" 
										class="show-sub-categories glyphicon glyphicon-menu-down"></span>
								
								</li>
								<li class="category-list-item list-group-item"
								data-cat-id="2" data-super-cat-id="0" data-level="0"
								data-sub-categories-set="0">
									<span class="category-name level-zero">Landscape</span>	
									<span style="font-size: 1em" 
										class="add-category glyphicon glyphicon-plus"></span>												 
									<span style="font-size: 1em" 
										class="show-sub-categories glyphicon glyphicon-menu-down"></span>
								
								</li>
								
								<li class="category-list-item list-group-item"
								data-cat-id="9" data-super-cat-id="0" data-level="0"
								data-sub-categories-set="0">											 
									<span class="category-name level-zero">Snow Removal</span>
									<span style="font-size: 1em" 
										class="add-category glyphicon glyphicon-plus"></span>													
									<span style="font-size: 1em" 
										class="show-sub-categories glyphicon glyphicon-menu-down"></span>
								
								</li>										
							</ul>
						</div>
<!-- 									Eventaully render this with jstl -->
<!-- 									//********************************************************************************* -->
<!-- 									//********************************************************************************* -->

					</div>						
														
				</div>
				
				<div id="questionsContainer" class="section-container">
					<div class="section-body">
						<h4>Questions</h4>
<!-- 						<div id="cartContainer" class="body-element-container actions-not-clickable"> -->
<!-- 							<div class="action-container"> -->
<!-- 								<span id="deleteQuestion" class="delete action">Delete</span> -->
<!-- 							</div> -->
<!-- 							<div class="action-container"> -->
<!-- 								<span id="editQuestion" class="action requires-acknowledgement">Edit</span> -->
<!-- 								<span id="okEditQuestion" class="glyphicon glyphicon-ok"></span> -->
<!-- 							</div> -->
<!-- 							<div class="action-container"> -->
<!-- 								<span id="copyQuestion" class="action">Copy</span> -->
<!-- 							</div>		 -->

<!-- 						</div>							 -->
						<div class="body-element-container">
							<button id="addQuestion" class="clickable btn-sqr selected-blue">Add</button>
							<button id="deleteQuestion" class="selected-question-action not-clickable btn-sqr selected-blue">Delete</button>
							<button id="editQuestion" class="selected-question-action not-clickable btn-sqr selected-blue">Edit</button>
							<span id="saveEditQuestionChanges" class="selected-question-action glyphicon glyphicon-ok"></span>
							<span id="cancelEditQuestionChanges" class="selected-question-action glyphicon glyphicon-remove"></span>
							<span id="invalidAddQuestion" class="invalid-message">Please fill in all required fields</span>
						
							<div id="cartContainer" class="actions-not-clickable">
								<div id="questionCart" class="" data-edit="0">
									<div id="addedQuestions"></div>
								</div>
							</div>
						</div>			
						
						<div class="body-element-container form-group ">
							<div class="section-sub-head">Question Format</div>
							<select id="questionFormat" class="question-formats form-control" title="">
							  <option selected value="-1" style="display: none"></option>	
							  <option class="answer-format-item" value="0">Yes or No</option>
							  <option class="answer-format-item" value="1">Short Answer</option>
							  <option class="answer-format-item" value="2">Single Answer</option>
							  <option class="answer-format-item" value="3">Multiple Answer</option>
							</select>
						</div>					
						
						<div class="body-element-container form-group ">
							<div class="section-sub-head">Question</div>
							<textarea id="question" class="form-control" rows="2"></textarea>
						</div>					
						
						<div id="answerListContainer"  class="body-element-container form-group ">
							<div class="section-sub-head">Answers</div>
							<div id="answerList">
								<div class="answer-container">
									<span class="delete-answer glyphicon glyphicon-remove"></span>
									<input class="form-control answer-option">
								</div>
								<div class="answer-container">
									<span class="delete-answer glyphicon glyphicon-remove"></span>
									<input class="form-control answer-option">
								</div>
							</div>
							<span id="addAnswer" class="glyphicon glyphicon-plus"></span>
						</div>					
					</div>																							
				</div>
				
				
				
				<div id="employeeSkillsContainer" class="section-container">

				
						<div class="section-body">
							<h4>Required Skills</h4>
							<div class="list-container body-element-container form-group ">
								<div id="requiredSkillsContainer" class="list-items-container">
									<div class="list-item">
										<span class="delete-list-item glyphicon glyphicon-remove"></span>
										<span class="answer-option-container">
											<input class="form-control answer-option">
										</span>
									</div>
								</div>
								<span class="add-list-item glyphicon glyphicon-plus"></span>
							</div>					
						</div>	
						<div class="section-body">
							<h4>Desired Skills</h4>
							<div class="list-container body-element-container ">
								<div id="desiredSkillsContainer" class="list-items-container">
									<div class="list-item">
										<span class="delete-list-item glyphicon glyphicon-remove"></span>
										<span class="answer-option-container">
											<input class="form-control answer-option">
										</span>
									</div>
								</div>
								<span class="add-list-item glyphicon glyphicon-plus"></span>
							</div>					
						</div>
						<div id="contractorRequirements">
							<div class="section-body">
							<h4>LLC</h4>
								<div class="body-element-container">									
									<div class="sub-header">Must have an LLC?</div>								
									<div class="body-element-container input-container">
										<div class="radio">
										  <label><input type="radio" name="contractor-llc">Yes</label>
										</div>
										<div class="radio">
										  <label><input type="radio" name="contractor-llc">No</label>
										</div>
									</div>
								</div>
							</div>
							<div class="section-body">
							<h4>Licenses</h4>
								<div class="body-element-container">									
									<div class="sub-header">Must have the following licenses:</div>								
									<div class="list-container form-group ">
										<div id="desiredSkillsContainer" class="list-items-container">
											<div class="list-item">
												<span class="delete-list-item glyphicon glyphicon-remove"></span>
												<input class="form-control answer-option">
											</div>
										</div>
										<span class="add-list-item glyphicon glyphicon-plus"></span>
									</div>	
								</div>
							</div>														
						</div>						
					</div>			
					
					<div id="nextContainer" class="section-container section-body">
						<span id="next" class="accent">Next</span>
					</div>		
				</div>
			</div>
		</div>

<div style="display: none">
	<ul>
		<li id="categoryListItemTemplate" style="display: none" 
			class="category-list-item list-group-item" 
			data-cat-id="1" data-super-cat-id="0" data-level="-1"
			data-sub-categories-set="0">
			<span class="category-name indent">Category Name</span>	
			<span style="font-size: 1em" 
				class="add-category glyphicon glyphicon-plus"></span>												 
			<span style="font-size: 1em" 
				class="show-sub-categories glyphicon glyphicon-menu-down"></span>
		
		</li>	
	</ul>
</div>

<!-- Modals -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
	<div id="confirmJobDeleteModal" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Confirmation</h4>
	      </div>
	      <div class="modal-body">
	        <p>Continue to delete job?</p>
	        
	        <div class="checkbox" style="margin: 10px 0px 0px 15px">
	        	<label>
	        		<input id="disableJobDeleteAlert" data-confirmed="0" type="checkbox"> Disable alert
	        	</label>
	        </div>	        
	      </div>
	      <div class="modal-footer">
	        <button id="confirmJobDelete" type="button" class="btn btn-default" 
	        	data-dismiss="modal">Yes</button>
	        <button id="cancelJobDelete" type="button" class="btn btn-default"
	        	data-dismiss="modal">No</button>
	      </div>
	    </div>
	
	  </div>
	</div>	

	<div id="confirmJobSubmit" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Confirmation</h4>
	      </div>
	      <div class="modal-body">
	        <p>Complete job posting and submit jobs?</p>
	      </div>
	      <div class="modal-footer">
	        <button id="confirmJobSubmit" type="button" class="btn btn-default" 
	        	data-dismiss="modal" onclick="submitJobs(1)">Yes</button>
	        <button id="cancelJobSubmit" type="button" class="btn btn-default"
	        	data-dismiss="modal" onclick="submitJobs(0)">No</button>
	      </div>
	    </div>
	
	  </div>
	</div>	

<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->



</body>


<script>

	var pageContext = "postJob";
	var jobs = [];	
	var jobCount = 0;
	var postQuestionDtos = [];
	var questionCount = 0;
// 	var questionContainerIdPrefix = 'question-';
	var workDays = [];
	
	var $addQuestion = $("#addQuestion");
	var $editQuestion = $("#editQuestion")
	var $deleteQuestion = $("#deleteQuestion");
	
	var employmentType = -1;
	var duration = -1;
	
	function hideSectionContainers(sectionContainerId){
		$.each($("#sectionContainers").find(".section-container"), function(){
			if($(this).attr("id") != sectionContainerId){
				//slideUp($(this), 500);
				$(this).hide();
			}
			
		})
	}
	
	function showSectionContainer(sectionContainerId){
		var sectionContainer = $("#sectionContainers").find("#" + sectionContainerId)[0];
		
		//slideDown($(sectionContainer), 500);
		$(sectionContainer).show();
		
	}
	
	function showContractorContent(request){
		
		$.each($("#sectionContainers").find(".contractor-content"), function(){
			if(request)$(this).show();	
			else $(this).hide();
		})
		
		
	}
	

	$(document).ready(function() {
		
		

		

		
		
		
		
		
		
		initMultiDayCalendar($("#calendar-multi-day"));
		initSingleDayCalendar($("#calendar-single-day"));
		
		
		$(".add-list-item").click(function(){ 
			
			
			var listContainer = $(this).closest(".list-container");
			var listItemsContainer = $(listContainer).find(".list-items-container")[0];
			var listItem = $(listContainer).find(".list-item")[0];
			var clone = $(listItem).clone();			
			
			//Clear the input
			$(clone).find("input").val("");
			
			$(listItemsContainer).append(clone);
		
		})
		
		$("body").on("click", ".delete-list-item", function(){
			deleteListItem($(this).closest(".list-item"), 1);
		})
		

		
		$("#calendarContainer tbody").on("click", "td", function(){
// 			alert(89)
		})
		
		$("input[type='radio'][name='pay-range']").click(function(){
			var $container = $("#payRangeContainer");
			var speed = 500;
			if($(this).attr("value") == 1){
				slideDown($container, speed);
			}
			else{
				slideUp($container, speed);
			}
		})
		
		$("#employmentTypeContainer input[type='radio']").click(function(){
			
			employmentType = $(this).attr("value");
			
			setJobInfoControls();
			
// 			var id = $(this).attr("id");
// 			var speed = 500;
// 			var $payMethod_salary = $("#pay-method-salary");
// 			if(id == "employmentType_contractor"){
// 				slideUp($payMethod_salary, speed)
// 			}
// 			else if(id == "employmentType_employee"){
// 				slideDown($payMethod_salary, speed)
// 			}
		})
		
		$("#employmentType_employee").click();
		
		function setJobInfoControls(){
			// EmploymentType: 0 = employee; 1 = contractor
			
			
			var doShow_payMethod_salary = false;
			var doShow_contractorSection = false;
			var speed = 500;
			var $payMethod_salary = $("#pay-method-salary");
			var $contractorSection = $("#contractorRequirements");
			var $employeeOrContractorHeader = $("#employeeOrContractor");
			
			
			if(employmentType == "1"){
				doShow_contractorSection = true;
				$employeeOrContractorHeader.html("Contractor Info");
				showContractorContent(true);
// 				slideUp($payMethod_salary, speed)
			}
			else if(employmentType == "0"){
				
				$employeeOrContractorHeader.html("Employee Info");
				showContractorContent(false);
				if(duration == "5" || duration == "6"){
					doShow_payMethod_salary = true;
				}
				
			}	
			
			
			if(doShow_payMethod_salary){
				slideDown($payMethod_salary, speed);
			}
			else{
				slideUp($payMethod_salary, speed);
			}
			
			if(doShow_contractorSection){
				slideDown($contractorSection, speed);
			}
			else{
				slideUp($contractorSection, speed);
			}
			
		}

		
		$(".duration").click(function(){
		
			slideDown($("#durationFollowUp"), 500);
			
			duration= $(this).attr("data-id");
			setJobInfoControls();
			
			
			var id = $(this).attr("id");
			
			var $eCalendarSelectionNote = $("#calendarSelectionNote");
			var $eSingle = $("#calendar-single-day");
			var $eMulti = $("#calendar-multi-day");
			var $eDatesLabel = $("#datesLabel");
// 			var $eDatesHeader = $("#datesHeader");
			var $eTimeSectionBody = $("#timeSectionBody")
			var $eTimeContainerSingle = $("#timeContainer-SingleDate");
			var $eTimeContainerMulti = $("#timeContainer-MultiDate");
			var $eNumberOf_label = $("#numberOf-label");
			var $eNumberOfDuration = $("#numberOfDuration");
			var slideSpeed = 500;
			
			if(id == "days"){
// 				slideDown($eMulti, 500);
// 				slideUp($eSingle, 500);
				$eSingle.hide();
				$eMulti.show();
				
				
			}
			else{
// 				slideUp($eMulti, 500);
// 				slideDown($eSingle, 500);
				$eSingle.show();
				$eMulti.hide();
			}
			
			if(id == "hours"){
				$eDatesLabel.html("");
// 				$eDatesHeader.html("Date");
// 				$eTimeContainerSingle.show();
// 				$eTimeContainerMulti.hide();
// 				slideDown($eTimeSectionBody, 500);

				$eCalendarSelectionNote.html("Select a day");
			}
			else if(id == "days"){
				$eDatesLabel.html("");
// 				$eDatesHeader.html("Dates");
// 				$eTimeContainerSingle.show();
// 				$eTimeContainerMulti.show();
// 				slideDown($eTimeSectionBody, 500);

				$eCalendarSelectionNote.html("Select all days");
			}
			else if(id == "months"){
				$eCalendarSelectionNote.html("Select a start date and an end date");
			}			
			else if(id == "hopefullyForever"){
				$eCalendarSelectionNote.html("Select a start date");
			}
			
			if(id == "hours" || id == "days" || id == "hopefullyForever"){
				slideUp($eNumberOfDuration, slideSpeed);
			}
			else if(id == "weeks"){
				slideDown($eNumberOfDuration, slideSpeed);
				$eNumberOf_label.html("Number of weeks");
			}
			else if(id == "months"){
				slideDown($eNumberOfDuration, slideSpeed);
				$eNumberOf_label.html("Number of months");
			}
			else if(id == "years"){
				slideDown($eNumberOfDuration, slideSpeed);
				$eNumberOf_label.html("Number of years");
			}
			
			highlightArrayItem(this, $("#durationsContainer").find(".duration"), "selected-duration");
			
			
			
		})
		


		
		$("#deleteQuestion").click(function(){
			if(isButtonClickable($("#deleteQuestion"))){
				deleteQuestion();
				setQuestionActionsAsClickable(false);
				disableInputFields(false, "questionsContainer");
			}
			
		})

		$("#submitJobs").click(function(){
			submitJobs(1);
		})
		
		
		

		
		
		//Click event for an action that requires acknowledgement
		$("body").on("click", ".actions-clickable .action.requires-acknowledgement", function(){
			
			var clickedId = $(this).attr("id");
			
			//Show check mark
			$($(this).siblings(".glyphicon-ok")).show();
			
			//Display the actions as "un-clickable".
			//This forces the user to click the checkmark, and not another action, to signify
			//they are finished with the action they clicked.
			//Determine whether the button is a job or question
			var subCartId = getSubCartId(this)			
			toggleActionAppearances(subCartId);
			
			
			//If editing a quesiton, then collapse the job info body.
			//This saves the user from having to scroll to the bottom of the page.
			if(clickedId == "editQuestion"){
				expandInfoBody("questionInfoBody", true);
				disableInputFields(false, "questionInfoBody");

			}

			
		})

		
		$("#saveEditQuestionChanges").click(function(){
			
			var clickedId = $(this).attr("id");
			var editedQuestion = {};
			var selectedQuestion = {};
			var selectedJob = {};
			var editedJob = {};


				
				if(validateAddQuestionInputs()){
					
					//Hide check mark
					$(this).hide();
					
					//Format elements
					setActionsAsClickable(true, "questionCart");
					disableInputFields(true, "questionInfoBody");
					
					selectedQuestion = getSelectedQuestion();
					editedQuestion = getPostQuestionDto();
					
					//When editing a question, the id must remain the same
					editedQuestion.id = selectedQuestion.id;
					
					//Remove the old selected question
					postQuestionDtos = removeArrayElement(selectedQuestion.id, postQuestionDtos);
					
					//Add the new edited question
					postQuestionDtos.push(editedQuestion);
					
					updateAddedQuestionText(editedQuestion.id, editedQuestion.text);
					
// 					//Set the job controls
// 					setButtonsAsClickable(true, "jobInfo");
// 					disableInputFields(false, "jobInfoBody");

					//Set the question controls
					resetAddedQuestionContainer();
				}
			
			
		})
		
		function resetAddedQuestionContainer(){
			setActionAsClickable(false, $editQuestion);
			setActionAsClickable(false, $deleteQuestion);
			setActionAsClickable(true, $addQuestion);
			
			showSaveAndCancelEditQuestionChanges(false);
			// unselectQuestion();
			
			
		}
		

		
		$("#addedQuestions").on("click", "a", function(){
			
			var buttons;
			
			if(buttonIsCurrentlySelected(this)){
// 				deselectQuestion();	
				unselectQuestion();
				setQuestionActionsAsClickable(false);
				disableInputFields(false, "questionsContainer");
				setQuestionActionsAsClickable(false);
			}else{
				//selectQuestion(this);
				
				
				highlightArrayItem(this, $("#addedQuestions").find("a"), "selected");
				showQuestion();
				disableInputFields(true, "questionsContainer");
				setQuestionActionsAsClickable(true);
			}	
			

		})
		
		function unselectQuestion(){
			var q = $("#addedQuestions").find(".selected")[0];
			$(q).removeClass("selected");
			
			clearPostQuestionInputs();
		}
	
		function setQuestionActionsAsClickable(request){
			

			if(request){
				setActionAsClickable(true, $editQuestion);
				setActionAsClickable(true, $deleteQuestion);
			}
			else{
				setActionAsClickable(false, $editQuestion);
				setActionAsClickable(false, $deleteQuestion);
			}
			
		}

		function setActionAsClickable(request, $e){
			if(request){
				$e.addClass("clickable");
				$e.removeClass("not-clickable");
			}
			else{
				$e.removeClass("clickable");
				$e.addClass("not-clickable");
			}
		}
		
		function getSubCartId(childElement){
			var subCart = $(childElement).parents(".sub-cart")[0];
			return $(subCart).attr("id");
	
		}
		
		$("#editQuestion").click(function(){
			if(isButtonClickable($editQuestion)){
				setActionAsClickable(false, $addQuestion);
				setQuestionActionsAsClickable(false);
				showSaveAndCancelEditQuestionChanges(true);	
				disableInputFields(false, "questionsContainer");
			}
			
		})
		
		function showSaveAndCancelEditQuestionChanges(request){
			if(request){
				$("#saveEditQuestionChanges").show();
				$("#cancelEditQuestionChanges").show();
			}
			else{
				$("#saveEditQuestionChanges").hide();
				$("#cancelEditQuestionChanges").hide();
			}
		}
		
		$("#cancelEditQuestionChanges").click(function(){
			showQuestion();
			resetAddedQuestionContainer();
			unselectQuestion();
		})
	
		
		$("#addQuestion").click(function(e){
			
			//Validate inputs
			if(isButtonClickable($(this))){
				if(validateAddQuestionInputs()){
// 					e.stopPropagation();
					//Get the question dto
					var postQuestionDto = getPostQuestionDto();
					
					//Set its id
					questionCount += 1;				
					postQuestionDto.id = questionCount;
					
					//Add question to the array
					postQuestionDtos.push(postQuestionDto);
					
					//Add question to the DOM
					addQuestionToDOM(postQuestionDto);
					
					if(postQuestionDtos.length > 0){
						showAddedQuestions(true);
					}
					else{
						showAddedQuestions(false);
					}
					
					unselectQuestion();
					disableInputFields(false, "questionsContainer");
					setQuestionActionsAsClickable(false);
					
// 					$("#jobInfoBody").hide(500);
// 					$("#cartContainer").show(500);
				}
			}
		})
		
		function showAddedQuestions(request){
			if(request){
				slideDown($("#cartContainer"), 500);
				$("#editQuestion").show();
				$("#deleteQuestion").show();
			}
			else{
				slideUp($("#cartContainer"), 500);
				$("#editQuestion").hide();
				$("#deleteQuestion").hide();
			}
		}
		
		
		function updateAddedQuestionButtonText(questionId, questionText){
			
			var button = $("#addedQuestions").find("button[data-question-id='" + questionId + "']")[0];
			
			var buttonText = getAddedQuestionButtonText(questionText);
			
			$(button).html(buttonText);
			
		}
		
		function getAddedQuestionButtonText(questionText){
			
			//If the qustion is longer than 20 characters, then only show the first 20.
			if(questionText.length > 20){
				return questionText.substring(0, 19) + "..."
			}else{
				return questionText;
			}
		}
		
		function addQuestionToDOM(postQuestionDto){
			var html = "<a data-question-id='" + postQuestionDto.id + "' class='accent no-hover clickable'>";
			
			var buttonText = getAddedQuestionButtonText(postQuestionDto.text);
			html += buttonText;			
			html += "</a>";
						
			$("#addedQuestions").append(html);
			
			clearPostQuestionInputs();
				
		}

		
		$(".show-section").click(function(){
			var idToToggle = $(this).attr("data-show");
			
			if(idToToggle == "questionInfoBody"){				
				expandInfoBody("jobInfoBody", false);
				expandInfoBody("questionInfoBody", true);
			}
			else if(idToToggle == "jobInfoBody"){
				expandInfoBody("jobInfoBody", true);
			}

		})		
		
		
		$("#questionFormat").click(function(){
		
			var selectedOption = $(this).find("option:selected")[0];
			var value = $(selectedOption).val(); 
			if(value == 2 || value == 3){
				$("#answerListContainer").show(500);				
			}else{
				$("#answerListContainer").hide(500);
			}
			
// 			$("#questionInfo").animate({ scrollTop: $('#questionInfo').height()}, 1000);
// 			$('#questionInfo').scrollTop($('#questionInfo')[0].scrollHeight);

			
		})
		
		$("#questionFormat").change(function(){
// 			scrollToElement("questionInfo", 500);
		})
		
		$("#addAnswer").click(function(){ 
			
			
			var answerContainer = $("#answerList").find(".answer-container")[0];			
			var clone = $(answerContainer).clone(true);			
			
			//Clear the input
			$(clone).find("input").val("");
			
			$("#answerList").append(clone);
		
		})
		
		$(".delete-answer").click(function(){	
			deleteAnswer(this);
		})
			
		$("#addJobToCart").click(function(){
			addJobToCart();		
		})
		
		$("#expandSelectedDates").click(function(){
			$("#times").toggle(200);
			toggleClasses($(this), "glyphicon-menu-up", "glyphicon-menu-down");
		})
		
		$("#applyTimesToAllDates").click(function(){			
			applyTimesToAllDates();			
		})	
		
		

		

		$('#allStartTimes').timepicker({
			'scrollDefault' : '7:00am'
		});
		$('#allEndTimes').timepicker({
			'scrollDefault' : '5:00pm'
		});
		
		$('#startTime-singleDate').timepicker({
			'scrollDefault' : '7:00am'
		});
		
		$('#endTime-singleDate').timepicker({
			'scrollDefault' : '5:00pm'
		});
		
 		setPopovers();
 		
		setStates();
		
		//Load the seed category's sub categories
		var i;
		var seedCategoryIds = [];
		$("#categoryTree li").each(function(){
			seedCategoryIds.push($(this).attr("data-cat-id"));
		})		
		getSubCategories(seedCategoryIds);
		
		
		
		
		
	})
	
	
	
		
	function setPopovers(){

		$('.popover1').popover({
			trigger: "hover",
			delay: {
				show: "250",		
			}
		});
		
		$('.popoverInstant').popover({
			trigger: "hover",
			delay: {
				show: "0",		
			}
		});	
		
	}
	
	function clearPostQuestionInputs(){
		$("#question").val("");
		$("#questionFormat option[value='-1']").prop("selected", "selected");
		
		$("#answerList").find(".answer-container input").each(function(){
			$(this).val("");
		});
		
		clearInvalidCss();
	}
	
	function buttonIsCurrentlySelected(button){
		if($(button).hasClass("selected") == 1){
			return true;
		}
		else{
			return false;
		}
	}
	

	function getPostQuestionDto(){
		
		var postQuestionDto = {};
		var answerOptionsInputs = []
		var answerOptions = [];
		
		
		postQuestionDto.text = $("#question").val();
		postQuestionDto.formatId = $("#questionFormat").find("option:selected").val();
	
		//If necessary, set the answer options
		if(doesQuestionHaveAnAnswerList(postQuestionDto)){
			answerInputs = $("#answerList").find(".answer-container input");
			$.each(answerInputs, function(){
				answerOptions.push($(this).val());
			})
			
			postQuestionDto.answerOptions = answerOptions;
		
		}else{
			postQuestionDto.answerOptions = [];
		}
		
		return postQuestionDto;
	}
	

	

	function setStates(){
		var $e = $("#state"); 
		$e.append('<option value="" selected style="display: none"></option>');
		$e.append('<option value="AL">AL</option>');
		$e.append('<option value="AK">AK</option>');
		$e.append('<option value="AZ">AZ</option>');
		$e.append('<option value="AR">AR</option>');
		$e.append('<option value="CA">CA</option>');
		$e.append('<option value="CO">CO</option>');
		$e.append('<option value="CT">CT</option>');
		$e.append('<option value="DE">DE</option>');
		$e.append('<option value="DC">DC</option>');
		$e.append('<option value="FL">FL</option>');
		$e.append('<option value="GA">GA</option>');
		$e.append('<option value="HI">HI</option>');
		$e.append('<option value="ID">ID</option>');
		$e.append('<option value="IL">IL</option>');
		$e.append('<option value="IN">IN</option>');
		$e.append('<option value="IA">IA</option>');
		$e.append('<option value="KS">KS</option>');
		$e.append('<option value="KY">KY</option>');
		$e.append('<option value="LA">LA</option>');
		$e.append('<option value="ME">ME</option>');
		$e.append('<option value="MD">MD</option>');
		$e.append('<option value="MA">MA</option>');
		$e.append('<option value="MI">MI</option>');
		$e.append('<option value="MN">MN</option>');
		$e.append('<option value="MS">MS</option>');
		$e.append('<option value="MO">MO</option>');
		$e.append('<option value="MT">MT</option>');
		$e.append('<option value="NE">NE</option>');
		$e.append('<option value="NV">NV</option>');
		$e.append('<option value="NH">NH</option>');
		$e.append('<option value="NJ">NJ</option>');
		$e.append('<option value="NM">NM</option>');
		$e.append('<option value="NY">NY</option>');
		$e.append('<option value="NC">NC</option>');
		$e.append('<option value="ND">ND</option>');
		$e.append('<option value="OH">OH</option>');
		$e.append('<option value="OK">OK</option>');
		$e.append('<option value="OR">OR</option>');
		$e.append('<option value="PA">PA</option>');
		$e.append('<option value="RI">RI</option>');
		$e.append('<option value="SC">SC</option>');
		$e.append('<option value="SD">SD</option>');
		$e.append('<option value="TN">TN</option>');
		$e.append('<option value="TX">TX</option>');
		$e.append('<option value="UT">UT</option>');
		$e.append('<option value="VT">VT</option>');
		$e.append('<option value="VA">VA</option>');
		$e.append('<option value="WA">WA</option>');
		$e.append('<option value="WV">WV</option>');
		$e.append('<option value="WI">WI</option>');
		$e.append('<option value="WY">WY</option>');
		
		
	}
</script>


<%@ include file="./includes/Footer.jsp"%>