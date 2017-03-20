<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/PageContentManager.jsp" %> --%>
<%@ include file="../includes/resources/DatePicker.jsp" %>
<%@ include file="../includes/resources/JobInformation.jsp" %>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />				
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob.css" />
<!-- <link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" /> -->
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />

<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>	
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>		

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/post_job/post_job_new.css" />				
			
			
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/post_job/questions.css" />			
<script	src="<c:url value="/static/javascript/post_job/Questions.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/PostJob.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/SubmitValidation.js" />"></script>
<script	src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>



	<div id="postActions">
		<c:if test="${!empty postedJobs }">
			<div id="postedJobsContainer">
				<button id="copyPreviousPost" class="sqr-btn"
					 data-toggle-id="postedJobs">Copy a previous job posting</button>
				<button id="startNewJob" class="sqr-btn">Start a new job posting</button>
				<div id="postedJobs" class="dropdown-style">
					<c:forEach items="${postedJobs }" var="job">
						<div data-posted-job-id="${job.id }">${job.jobName }</div>
					</c:forEach>
				</div>
			
			</div>
			
		</c:if>	
<!-- 		<span id="submitPosting_preview" class="post-section">Preview Job Posting</span> -->
		<span id="editPosting" class="post-section" data-section-id="employmentContainer">Edit posting</span>
		<span id="submitPosting_final" class="post-section" data-section-id="employmentContainer">Submit posting</span>
	</div>
	<div id="postSections" class="${!empty postedJobs ? 'hide-on-load' : '' }">
		<span class="post-section selected-section" data-section-id="generalContainer">General</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="datesContainer">Dates</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="timesContainer">Times</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="locationContainer">Location</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="compensationContainer">Compensation</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="categoriesContainer">Categories</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="questionsContainer">Questions</span>
<!-- 		<span>/</span> -->
		<span class="post-section" data-section-id="employeeSkillsContainer">Employee Skills</span>							
	</div>


<div class="container ${!empty postedJobs ? 'hide-on-load' : '' }">


	<div class="row">
		<div id="displayExample_jobInfo"  class="col-sm-12">
		
		</div>
	</div>
	<div id="postJobInfoContainer">
		<div class="header-container">
			<div><button id="submitPosting_preview" class="sqr-btn">Preview Job Posting</button></div>
			<span id="previousSection">Previous</span>
			<span id="nextSection">Next</span>
		</div>
	
		<div id="generalContainer" class="first section-container">
			<div class="item">
				<p>Employment Type</p>
				<div class="radio-container">
					<div class="radio">
					  <label><input value="0" id="employmentType_employee" type="radio" name="employmentType">Employee</label>
					</div>
					<div class="radio">
					  <label><input value="1" id="employmentType_contractor" type="radio" name="employmentType">Contractor</label>
					</div>
				</div>
			</div>
			<div class="item">
				<p>Job Name</p>
				<input id="name" name="name" type="text" class=""	value=""></input>						
			</div>
			<div class="item">
				<p>Description</p>
				<textarea id="description" name="description" class="" rows="6"></textarea>							
			</div>
		</div>
		
		<div id="datesContainer" class="section-container">

			<div class="row">
				<div class="item">
					<p>Partial Availability</p>
					<label><input id="partialAvailabilityAllowed" type="checkbox">Allowed</label>
				</div>					
				<div class="item calendar-container">
					<p>Dates</p>
					<div id="workDaysCalendar_postJob" class="calendar" data-is-showing-job="0">
					</div>											
					<button class="square-button" id="clearCalendar">Clear</button>
				</div>
			</div>			
		</div>		
		
		<div id="timesContainer" class="section-container">

			<p id="noDatesSelected" class="">Please select one or more dates</p>
			<div id="timesTableContainer" >
				<table id="timesTable" class="main-table-style">
					<thead>
						<tr>
							<th>Dates</th>
							<th>Selection</th>
							<th>Start Time</th>
							<th>End Time</th>
						</tr>
<!-- 							These elements are only to be cloned -->
<!-- 					************************************ -->
						<tr class="master-row-multi-select">
							<td></td>
							<td><label><input class="select-all" type="checkbox" name="time">Select All</label></td>
							<td><select   class="time start-time select-all"></select></td>
							<td><select  class="time end-time select-all"></select></td>							
						</tr>
						<tr class="master-row work-day-row">
							<td class="date"></td>
							<td><input type="checkbox" name="time"></td>
							<td><select class="time start-time"></select></td>
							<td><select class="time end-time"></select></td>
						</tr>
<!-- 					************************************ -->							
					</thead>
					
					<tbody>

					</tbody>				
				</table>
			
			</div>
		</div>	
					
		<div id="locationContainer" class="section-container">
			<div class="item">
				<p>Street Address</p>						
				<input id="street" type="text" class=""	value="2217 Bonnie Lane"></input>						
			</div>
			<div class="item">
				<p>City</p>					
				<input id="city" type="text" class=""	value="St. Paul"></input>						
			</div>
			<div class="item">
				<p>State</p>						
				<select id="state" class=""	></select>						
			</div>	
			<div class="item">
				<p>Zip Code</p>						
				<input id="zipCode" type="text" value="55119"></input>						
			</div>						
		</div>	
		
		
		<div id="compensationContainer" class="section-container">
			<div class="item">
				<p>Method</p>
				<div class="radio-container">							
					<div class="radio">
					  <label><input type="radio" name="pay-method">By the hour</label>
					</div>
					<div class="radio">
					  <label><input type="radio" name="pay-method">By the job</label>
					</div>					
				</div> 
			</div>	
			<div class="item">
				<p>Range</p>	
				<div class="radio-container">					
					<div class="radio">
					  <label><input value="0" type="radio" name="pay-range">No, accept all offers</label>
					</div>
					<div class="radio">
					  <label><input value = "1" type="radio" name="pay-range">Yes, only accept offers between:</label>
					</div>
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
						
		<div id="categoriesContainer" class="section-container">
			<div class="header row">
				<p>Categories</p>
			</div>		
			<div class="row">

			</div>								
		</div>	
		
		<div id="questionsContainer" class="section-container">
			<div id="addedQuestions" class="item">
				<p>Added Questions</p>
			</div>		
			<div class="">
				<div id="questionActions">
					<button id="newQuestion" class="clickable btn-sqr">Clear</button>
					<button id="addQuestion" class="clickable btn-sqr">Add</button>
					<button id="deleteQuestion" class="btn-sqr">Delete</button>
					<button id="editQuestion" class="btn-sqr">Edit</button>							
					<span id="editQuestionResponses">
						<span id="saveEditQuestionChanges" class="glyphicon glyphicon-ok"></span>
						<span id="cancelEditQuestionChanges" class="glyphicon glyphicon-remove"></span>
					</span>
					<c:if test="${!empty postedQuestions }">
						<div id="postedQuestionsContainer">
							<span id="copyPreviousQuestion" data-toggle-id="postedQuestions">
								Copy a previous question</span>
							<div id="postedQuestions" class="dropdown-style">
								<c:forEach items="${postedQuestions }" var="question">
									<div data-question-id="${question.questionId }">${question.text }</div>
								</c:forEach>
							</div>
						</div>
					</c:if>
					<div id="invalidAddQuestion" class="invalid-message">Please fill in all required fields</div>
				</div>
<!-- 				<div class="item"> -->
<!-- 					<p>Added Questions</p> -->

<!-- 				</div> -->
			</div>			
			
			
			<div class="item">
				<p>Question Format</p>						
				<select id="questionFormat" class="question-formats">
				  <option class="answer-format-item" data-format-id="0">Yes or No</option>
				  <option class="answer-format-item" data-format-id="1">Short Answer</option>
				  <option class="answer-format-item" data-format-id="2">Single Answer</option>
				  <option class="answer-format-item" data-format-id="3">Multiple Answer</option>
				</select>					
			</div>	
			
			<div class="item">
				<p>Question</p>						
				<textarea id="question" class="" rows="3"></textarea>				
			</div>		
			
			<div class="item" id="answerListContainer">
					<p>Answers</p>						
					<div id="answerList">
						<div class="list-item answer-container">
							<span class="delete-list-item delete-answer glyphicon glyphicon-remove"></span>
							<input type="text" class="answer-option">
						</div>
						<div class="list-item answer-container">
							<span class="delete-list-item delete-answer glyphicon glyphicon-remove"></span>
							<input type="text" class="answer-option">
						</div>
					</div>
					<span id="addAnswer" class="add-list-item glyphicon glyphicon-plus"></span>			
			</div>										
			
		</div>					
		

		<div id="employeeSkillsContainer" class="section-container">
			<div class="item">
					<p>Required</p>					
					<div id="requiredSkillsContainer" class="list-items-container skills-container">
						<div class="list-item">
							<span class="delete-list-item glyphicon glyphicon-remove"></span>
							<span class="">
								<input type="text" >
							</span>
						</div>							
					</div>		
					<span class="add-list-item glyphicon glyphicon-plus"></span>								
			</div>		
			<div class="item">
				<p>Desired</p>						
				<div id="desiredSkillsContainer" class="list-items-container skills-container">
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


<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp">
</script>