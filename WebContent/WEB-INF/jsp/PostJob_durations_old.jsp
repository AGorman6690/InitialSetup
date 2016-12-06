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
		
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
		
</head>

<body>

	<div class="container">	
<!-- 	<a href="/JobSearch/postJob-with-cart">Post Job - Method 2 - With Cart</a> -->
<!-- 	<div><a href="/JobSearch/postJob-without-cart">Post Job - Method 3 - WithOUT Cart</a></div> -->
	
		<div class="row">
			<div class="col-sm-2">
				<div id="postingCriteriaContainer">
					<div id="general" class="posting-criteria">General</div>
					<div id="date" class="posting-criteria">Dates</div>
					<div id="location" class="posting-criteria">Location</div>
					<div id="compensation" class="posting-criteria">Compensation</div>
					<div id="categories" class="posting-criteria">Categories</div>
					<div id="questions" class="posting-criteria">Questions</div>
					<div id="employeeSkills" class="posting-criteria">Employee Skills</div>					
				</div>
			</div>
			
			<div class="col-sm-10">
				<div id="generalContainer">
					<div class="section-body">
						<h4>General</h4>
						<div class="body-element-container form-group ">
							<div class="input-container">
								<div id="invalidJobName" class="invalid-message">Job names must be unique</div>
								<label for="name"
									class="form-control-label">Name</label>
								<input name="name" type="text" class="form-control"
									id="name" value=""></input>
							</div>
							<div class="input-container">
								<label for="description"
									class="form-control-label">Description</label>
								<textarea name="description" class="form-control"
									id="description" rows="3"></textarea>
							</div>
							
							<div class="input-container">
								<label for="description"
									class="form-control-label">Employment Type</label>
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
				</div>
				
				<div id="dateContainer">
						<div class="section-body">
							<h4>Dates</h4>
							<div class="body-element-container form-group ">
								<div id="durationsContainer">
									<div id="durationQuestion">How long will the job last?</div>
									<div id="durations">
										<div id="hours" class="duration" data-id="1">Hours</div>
										<div id="days" class="duration" data-id="2">Days</div>
										<div id="weeks" class="duration" data-id="3">Weeks</div>
										<div id="months" class="duration" data-id="4">Months</div>
										<div id="years" class="duration" data-id="5">Years</div>
										<div id="hopefullyForever" class="duration" data-id="6">Hopefully Forever</div>
									</div>
								</div>
							</div>					
						</div>		
				
				</div>
			</div>
		</div>
	
	

	

		<div id="infoContainer">	
			<div id="submitJobContainer" class="button-container">
	 			<button id="submitJobs" data-confirmed="0" type="button" class="clickable square-button">Submit Job</button>
			</div>						
			<div id="postingContainer">
				<div id="jobTypeInfo" class="section info-container">
					<div class="header">
						<span data-toggle-id="jobTypeInfoBody" data-toggle-speed="0">
							<span class="glyphicon glyphicon-menu-down"></span>
							<span class="header-text">Job Type</span>
						</span>				
					</div>
					<div id="jobTypeInfoBody">
						<div class="section-body">
							<h4>Duration</h4>
							<div class="body-element-container form-group ">
								<div id="durationsContainer">
									<div id="durationQuestion">How long will the job last?</div>
									<div id="durations">
										<div id="hours" class="duration" data-id="1">Hours</div>
										<div id="days" class="duration" data-id="2">Days</div>
										<div id="weeks" class="duration" data-id="3">Weeks</div>
										<div id="months" class="duration" data-id="4">Months</div>
										<div id="years" class="duration" data-id="5">Years</div>
										<div id="hopefullyForever" class="duration" data-id="6">Hopefully Forever</div>
									</div>
								</div>
							</div>					
						</div>		
						<div class="section-body">
							<h4>Employment Type</h4>
							<div class="body-element-container ">
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
				</div>															
		
				<div id="jobInfo" class="section info-container">
					<div class="header">
						<span data-toggle-id="jobInfoBody" data-toggle-speed="-1">
							<span class="glyphicon glyphicon-menu-down"></span>
							<span class="header-text">Job Info</span>
						</span>
					</div>	
					<div id="jobInfoBody">
						<div class="section-body">
							<h4>General</h4>
							<div class="body-element-container form-group ">
								<div class="input-container">
									<div id="invalidJobName" class="invalid-message">Job names must be unique</div>
									<label for="name"
										class="form-control-label">Name</label>
									<input name="name" type="text" class="form-control"
										id="name" value=""></input>
								</div>
								<div class="input-container">
									<label for="description"
										class="form-control-label">Description</label>
									<textarea name="description" class="form-control"
										id="description" rows="3"></textarea>
								</div>
							</div>					
						</div>				
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
					
						<div class="section-body">
							<h4 id="datesHeader">Dates</h4>
								<div class="body-element-container form-group ">
									<div class="input-container">
										<div id="calendarContainer">
											<div id="calendar-single-day" data-is-showing-job="0">
											</div>
											<div id="calendar-multi-day" data-is-showing-job="0">
											</div>											
											<button class="square-button" id="clearCalendar">Clear</button>
										</div>
									</div>	
									<div id="numberOfDuration" class="input-container" >
										<div id="invalidDurationLength" class="invalid-message">Duration length must be a positive number</div>
										<div id="durationUnitLengthContainer" class="input-container">
											<label id="numberOf-label" for="durationUnitLength"
												class="form-control-label">Number of</label>
											<input id="durationUnitLength" name="startTime" type="text" class="form-control time-input"
												value=""></input>
										</div>
									</div>																
								</div>					
						</div>
						<div id="timeSectionBody" class="section-body">
							<h4 id="timesHeader">Times</h4>
								<div class="body-element-container form-group ">
									
									<div id="timeContainer-SingleDate" class="input-container" >
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
																
<!-- 									<div id="timeContainer-MultiDate" class="input-container"> -->
<!--  										<label class="form-control-label">Times</label> --> 
<!-- 										<div id="timeInputsContainer"> -->
<!-- 											<div id="selectAllContainer" class=""> -->
<!-- 												<span id="expandSelectedDates" class="glyphicon glyphicon-menu-up"></span> -->
<!-- 												<span id="setAllLabel">Set All Dates</span> -->
<!-- 												<div class="form-group time-container"> -->
<!-- 												  <label for="allStartTimes">Start Time</label> -->
<!-- 												  <input type="text" class="form-control" id="allStartTimes"> -->
<!-- 												</div> -->
<!-- 												<div class="form-group time-container"> -->
<!-- 												  <label for="allEndTimes">End Time</label> -->
<!-- 												  <input type="text" class="form-control" id="allEndTimes"> -->
<!-- 												</div>		 -->
<!-- 												<span id="applyTimesToAllDates" class="glyphicon glyphicon-ok"></span>								 -->
<!-- 			   								</div>							 -->
											
				
<!-- 											<div id="times">												 -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
								</div>	
												
						</div>			
						<div class="section-body">
							<h4>Pay</h4>
								<div class="body-element-container">
									<div>
										<div class="sub-header">Method</div>								
										<div class="body-element-container input-container">
											<div class="radio">
											  <label><input type="radio" name="pay-method">By Hour</label>
											</div>
											<div class="radio">
											  <label><input type="radio" name="pay-method">By Job</label>
											</div>
											<div id="pay-method-salary" class="radio">
											  <label><input type="radio" name="pay-method">Salary</label>
											</div>
										</div>
									</div>
									<div>
										<div class="sub-header">Range</div>
										<div class="body-element-container input-container">
											<div class="radio">
											  <label><input value="0" type="radio" name="pay-range">No</label>
											</div>
											<div class="radio">
											  <label><input value = "1" type="radio" name="pay-range">Yes</label>
											</div>
											<div id="payRangeContainer" class="body-element-container">										
												<div id="minPayContainer" class="form-group">
													<label for="startTime"
														class="form-control-label">Min</label>
													<input id="startTime-singleDate" name="startTime" type="text" class="form-control time-input"
														value=""></input>												
												</div>
												<div id="maxPayContainer" class="form-group">
													<label for="startTime"
														class="form-control-label">Max</label>
													<input id="startTime-singleDate" name="startTime" type="text" class="form-control time-input"
														value=""></input>												
												</div>	
											</div>											 									
										</div>		
										
									</div>
								</div>												
						</div>			
															
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
				</div>
				
				<div id="employeeInfo" class="section info-container">
					<div class="header">
						<span data-toggle-id="employeeInfoBody" data-toggle-speed="0">
							<span class="glyphicon glyphicon-menu-down"></span>
							<span id="employeeOrContractor" class="header-text">Employee Info</span>
						</span>
					</div>	
					<div id="employeeInfoBody">
						<div class="section-body">
							<h4>Required Skills</h4>
							<div class="list-container body-element-container form-group ">
								<div id="requiredSkillsContainer" class="list-items-container">
									<div class="list-item">
										<span class="delete-list-item glyphicon glyphicon-remove"></span>
										<input class="form-control answer-option">
									</div>
								</div>
								<span class="add-list-item glyphicon glyphicon-plus"></span>
							</div>					
						</div>	
						<div class="section-body">
							<h4>Desired Skills</h4>
							<div class="list-container body-element-container form-group ">
								<div id="desiredSkillsContainer" class="list-items-container">
									<div class="list-item">
										<span class="delete-list-item glyphicon glyphicon-remove"></span>
										<input class="form-control answer-option">
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
				</div>	
								
				<div id="questionInfo" class="section info-container">
					<div class="header">
						<span data-toggle-id="questionInfoBody" data-toggle-speed="1">
							<span class="glyphicon glyphicon-menu-up"></span>
							<span class="header-text">Question Info</span>
						</span>
						<span class="button-container">
							<button id="newQuestion" class="clickable new square-button">New</button>
							<button id="addQuestion" class="clickable square-button">Add</button>
							<span id="invalidAddQuestion" class="invalid-message">Please fill in all required fields</span>
						</span>					
					</div>	
					<div id="questionInfoBody">
						<div class="section-body">
							<h4>Question Format</h4>
							<div class="body-element-container form-group ">
								<select id="questionFormat" class="question-formats form-control" title="">
								  <option selected value="-1" style="display: none"></option>	
								  <option class="answer-format-item" value="0">Yes or No</option>
								  <option class="answer-format-item" value="1">Short Answer</option>
								  <option class="answer-format-item" value="2">Single Answer</option>
								  <option class="answer-format-item" value="3">Multiple Answer</option>
								</select>
							</div>					
						</div>	
						<div class="section-body">
							<h4>Question</h4>
							<div class="body-element-container form-group ">
								<textarea id="question" class="form-control" rows="2"></textarea>
							</div>					
						</div>	
						<div id="answerListContainer" class="section-body">
							<h4>Answers</h4>
							<div class="body-element-container form-group ">
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
						<div id="cartContainer" class="section-body actions-not-clickable">
<!-- 							<div class="header"> -->
								<h4>Questions</h4>
								<div class="action-container">
									<span id="deleteQuestion" class="delete action">Delete</span>
								</div>
								<div class="action-container">
									<span id="editQuestion" class="action requires-acknowledgement">Edit</span>
									<span id="okEditQuestion" class="glyphicon glyphicon-ok"></span>
								</div>
								<div class="action-container">
									<span id="copyQuestion" class="action">Copy</span>
								</div>		
<!-- 							</div>		 -->
							<div id="questionCart" class="body-element-container" data-edit="0">
								<div id="addedQuestions">
								</div>
							</div>
						</div>						
					</div>												
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
	
	var employmentType = -1;
	var duration = -1;

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
// 				slideUp($payMethod_salary, speed)
			}
			else if(employmentType == "0"){
				
				$employeeOrContractorHeader.html("Employee Info");
				
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
		
			duration= $(this).attr("data-id");
			setJobInfoControls();
			
			
			var id = $(this).attr("id");
			
			var $eSingle = $("#calendar-single-day");
			var $eMulti = $("#calendar-multi-day");
			var $eDatesLabel = $("#datesLabel");
			var $eDatesHeader = $("#datesHeader");
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
				$eDatesHeader.html("Date");
// 				$eTimeContainerSingle.show();
// 				$eTimeContainerMulti.hide();
// 				slideDown($eTimeSectionBody, 500);
			}
			else if(id == "days"){
				$eDatesLabel.html("");
				$eDatesHeader.html("Dates");
// 				$eTimeContainerSingle.show();
// 				$eTimeContainerMulti.show();
// 				slideDown($eTimeSectionBody, 500);
			}
			else{
				$eDatesLabel.html("");
				$eDatesHeader.html("Start Date");
// 				slideUp($eTimeSectionBody, 500);
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		$("#confirmJobDelete").click(function(){
			var d = this;
			deleteJob();
			
			//If necessary, disable the "delete job" alert
			if($("#disableJobDeleteAlert").is(":checked")){
				
				//Set the element's attributes such that the alert/modal will not display
				var $e = $("#deleteJob");
				$e.attr("data-disable-alert", 1);
				$e.attr("data-target", "");
				
				//Attach the delete job function directly to the "delete" action
				$e.click(deleteJob);
			}
			
			
		})
		
		$("#deleteQuestion").click(function(){
			deleteQuestion();
		})
		
		$("#copyJob").click(function(){
			copyJob();
		})
		
		$("#copyQuestion").click(function(){
			copyQuestion();
		})		
		
		$("#submitJobs").click(function(){
			submitJobs(1);
		})
		
		
		
		$(".button-container .new").click(function(){
			

					clearPostQuestionInputs();
					expandInfoBody("questionInfoBody", true);
					setButtonAsClickable(true, $("#addQuestion"));
					deselectQuestion();
					setActionsAsClickable(false, "cartContainer");


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

		
		$(".action-container .glyphicon-ok").click(function(){
			
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
					
					updateAddedQuestionButtonText(editedQuestion.id, editedQuestion.text);
					
// 					//Set the job controls
// 					setButtonsAsClickable(true, "jobInfo");
// 					disableInputFields(false, "jobInfoBody");

					//Set the question controls
					setActionsAsClickable(true, "questionCart");
					setButtonAsClickable(true, $("#newQuestion"));
				}
			
			
		})
		

		
		$(".sub-cart").on("click", "button", function(){
			var buttons;
			
			//If the clicked button's current state is clickable
			if(isButtonClickable($(this))){
				//Determine whether the clicked button is a job or question
// 				var subCartId = getSubCartId(this)
				
// 				toggleActionAppearances(subCartId);
				
				
				//If the user is selecting questions for a job
				if(isSelectingQuestions()){
					
					//If a question button was clicked
					if(jobIsClicked(this) == false){
						toggleClass($(this), "tied-to-job");
					}
				}
				//If a job was clicked
				else if(jobIsClicked(this)) {
					
					if(buttonIsCurrentlySelected(this)){
						deselectJob();	
					}else{
						
						selectJob(this);
						
					}
					
				}
				//Else a question was clicked
				else{					
					
					if(buttonIsCurrentlySelected(this)){
						deselectQuestion();	
					}else{
						selectQuestion(this);
					}	
				}

			
			}
		})


		function getSubCartId(childElement){
			var subCart = $(childElement).parents(".sub-cart")[0];
			return $(subCart).attr("id");
	
		}
	
		
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
						slideDown($("#cartContainer"), 700);	
					}
					else{
						slideUp($("#cartContainer"));
					}
					
// 					$("#jobInfoBody").hide(500);
// 					$("#cartContainer").show(500);
				}
			}
		})
		
		
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
			var html = "<button data-question-id='" + postQuestionDto.id + "' class='btn clickable'>";
			
			var buttonText = getAddedQuestionButtonText(postQuestionDto.text);
			html += buttonText;			
			html += "</button>";
						
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