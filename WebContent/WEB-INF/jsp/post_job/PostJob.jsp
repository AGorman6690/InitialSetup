<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/ScriptsAndLinks_DatePicker.jsp" %>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />				
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />

<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>	
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>		
			
			
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/post_job/questions.css" />			
<script	src="<c:url value="/static/javascript/post_job/Questions.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/PostJob.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/SubmitValidation.js" />"></script>
<script	src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>

<div class="container">
	<div class="row first">
		<div class="col-sm-2">
			<div id="submitJobContainer" class="">Submit Job</div>
			<div id="general" class="side-bar selected-blue" data-section-id="generalContainer">General
				<div class="red"></div>
			</div>
			<div id="date" class="side-bar" data-section-id="datesContainer">Dates and Times</div>
			<div id="location" class="side-bar" data-section-id="locationContainer">Location</div>
			<div id="compensation" class="side-bar" data-section-id="compensationContainer">Compensation</div>
			<div id="categories" class="side-bar" data-section-id="categoriesContainer">Categories</div>
			<div id="questions" class="side-bar" data-section-id="questionsContainer">Questions</div>
			<div id="employeeSkills" class="side-bar" data-section-id="employeeSkillsContainer">Employee Skills</div>					
		
		</div>
		<div class="col-sm-10">
			<div id="generalContainer" class="first section-container">
				<div class="header row">
					<p>General</p>
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Employment Type</p>
					</div>
					<div class="col-sm-9">
						<div class="radio-container">
							<div class="radio">
							  <label><input value="0" id="employmentType_employee" type="radio" name="employmentType">Employee</label>
							</div>
							<div class="radio">
							  <label><input value="1" id="employmentType_contractor" type="radio" name="employmentType">Contractor</label>
							</div>
						</div>
					</div> 
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Job Name</p>
					</div>
					<div class="col-sm-9">
						<input id="name" name="name" type="text" class=""	value=""></input>						
					</div> 					
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Description</p>
					</div>
					<div class="col-sm-9">
						<textarea id="description" name="description" class="" rows="6"></textarea>							
					</div> 					
				</div>
			</div>
			
			<div id="datesContainer" class="section-container">
				<div class="header row">
					<p>Dates and Times</p>
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Work Days</p>
					</div>
					<div class="col-sm-9">							
						<div id="calendarContainer" class="calendar">
							<div id="calendar-multi-day" data-number-of-months="2" data-is-showing-job="0">
							</div>											
							<button class="square-button" id="clearCalendar">Clear</button>
						</div>
					</div> 
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Start Time</p>
					</div>
					<div class="col-sm-9">							
						<select id="startTime-singleDate"></select>
					</div> 
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>End Time</p>
					</div>
					<div class="col-sm-9">							
						<select id="endTime-singleDate"></select>
					</div> 
				</div>				
			</div>		
			
			
			<div id="locationContainer" class="section-container">
				<div class="header row">
					<p>Location</p>
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Street Address</p>
					</div>
					<div class="col-sm-9">							
						<input id="street" type="text" class=""	value="2217 Bonnie Lane"></input>						
					</div> 
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>City</p>
					</div>
					<div class="col-sm-9">							
						<input id="city" type="text" class=""	value="St. Paul"></input>						
					</div> 
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>State</p>
					</div>
					<div class="col-sm-9">							
						<select id="state" class=""	></select>						
					</div> 
				</div>	
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Zip Code</p>
					</div>
					<div class="col-sm-9">							
						<input id="zipCode" type="text" value="55119"></input>						
					</div> 
				</div>						
			</div>	
			
			
			<div id="compensationContainer" class="section-container">
				<div class="header row">
					<p>Compensation</p>
				</div>		
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Method</p>
					</div>
					<div class="col-sm-9">							
						<div class="radio">
						  <label><input type="radio" name="pay-method">By the hour</label>
						</div>
						<div class="radio">
						  <label><input type="radio" name="pay-method">By the job</label>
						</div>					
					</div> 
				</div>	
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Range</p>
					</div>
					<div class="col-sm-9">							
						<div class="radio">
						  <label><input value="0" type="radio" name="pay-range">No, accept all offers</label>
						</div>
						<div class="radio">
						  <label><input value = "1" type="radio" name="pay-range">Yes, only accept offers between:</label>
						</div>	
						<div id="payRangeContainer"> 
							<div id="minPayContainer">
								<label for="startTime" class="form-control-label">Min</label>
								<input id="minPay" type="text"></input>												
							</div>
							<div id="maxPayContainer">
								<label for="startTime" class="form-control-label">Max</label>
								<input id="maxPay" type="text"></input>												
							</div>	
						</div>				
					</div> 
				</div>								
			</div>			
							
			<div id="categoriesContainer" class="section-container">
				<div class="header row">
					<p>Categories</p>
				</div>		
				<div class="row">

				</div>								
			</div>	
			
			<div id="questionsContainer" class="section-container">
				<div class="header row">
					<p>Questions</p>
				</div>		
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-9">
						<div id="questionActions">
							<button id="addQuestion" class="clickable btn-sqr">Add</button>
							<button id="deleteQuestion" class="btn-sqr">Delete</button>
							<button id="editQuestion" class="btn-sqr">Edit</button>
							<span id="editQuestionResponses">
								<span id="saveEditQuestionChanges" class="glyphicon glyphicon-ok"></span>
								<span id="cancelEditQuestionChanges" class="glyphicon glyphicon-remove"></span>
							</span>
							<div id="invalidAddQuestion" class="invalid-message">Please fill in all required fields</div>
						</div>
						<div id="addedQuestions"></div>
					</div>
				</div>			
				
				
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Question Format</p>
					</div>
					<div class="col-sm-9">							
						<select id="questionFormat" class="question-formats form-control">
						  <option class="answer-format-item" data-format-id="0">Yes or No</option>
						  <option class="answer-format-item" data-format-id="1">Short Answer</option>
						  <option class="answer-format-item" data-format-id="2">Single Answer</option>
						  <option class="answer-format-item" data-format-id="3">Multiple Answer</option>
						</select>					
					</div> 
				</div>	
				
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Question</p>
					</div>
					<div class="col-sm-9">							
						<textarea id="question" class="" rows="2"></textarea>				
					</div> 
				</div>		
				
				<div class="row" id="answerListContainer">
					<div class="label-text col-sm-3">
						<p>Answers</p>
					</div>
					<div class="col-sm-9">							
						<div id="answerList">
							<div class="answer-container">
								<span class="delete-answer glyphicon glyphicon-remove"></span>
								<input type="text" class="answer-option">
							</div>
							<div class="answer-container">
								<span class="delete-answer glyphicon glyphicon-remove"></span>
								<input type="text" class="answer-option">
							</div>
						</div>
						<span id="addAnswer" class="glyphicon glyphicon-plus"></span>			
					</div> 
				</div>										
				
			</div>					
			
	
			<div id="employeeSkillsContainer" class="section-container">
				<div class="header row">
					<p>Employee Skills</p>
				</div>		
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Required</p>
					</div>
					<div class="col-sm-9">							
						<div id="requiredSkillsContainer" class="list-items-container">
							<div class="list-item">
								<span class="delete-list-item glyphicon glyphicon-remove"></span>
								<span class="">
									<input type="text" >
								</span>
							</div>
						</div>
						<span class="add-list-item glyphicon glyphicon-plus"></span>				
					</div> 
				</div>		
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Desired</p>
					</div>
					<div class="col-sm-9">							
						<div id="desiredSkillsContainer" class="list-items-container">
							<div class="list-item">
								<span class="delete-list-item glyphicon glyphicon-remove"></span>
								<span class="answer-option-container">
									<input type="text">
								</span>
							</div>
						</div>	
						<span class="add-list-item glyphicon glyphicon-plus"></span>				
					</div> 
				</div>											
			</div>											
		</div>
	</div>
</div>

<script>

	$(document).ready(function(){
		initMultiDayCalendar($("#datesContainer #calendar-multi-day"));
	})
	
	
</script>