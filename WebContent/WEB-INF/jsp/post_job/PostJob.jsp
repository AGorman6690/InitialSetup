<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/PageContentManager.jsp" %> --%>
<%@ include file="../includes/resources/DatePicker.jsp" %>
<%@ include file="../includes/resources/JobInformation.jsp" %>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />				
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/sideBar.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
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

	<div class="postSections">
		<span class="post-section">General</span>
		<span>/</span>
		<span class="post-section">Dates</span>
		<span>/</span>
		<span class="post-section">Times</span>
		<span>/</span>
		<span class="post-section">Location</span>
		<span>/</span>
		<span class="post-section">Compensation</span>
		<span>/</span>
		<span class="post-section">Categories</span>
		<span>/</span>
		<span class="post-section">Questions</span>
		<span>/</span>
		<span class="post-section">Employee Skill</span>							
	</div>

	<div class="row">
		<div id="headerLinksContainer" class="col-sm-12">
			<div id="submitPosting_preview_container">
				<c:if test="${!empty postedJobs }">
					<div id="postedJobsContainer">
						<span id="copyPreviousPost1" class="page-content-link selected"
							 data-toggle-id="postedJobs">Copy a previous posting</span>
						<div id="postedJobs" class="dropdown-style">
							<c:forEach items="${postedJobs }" var="job">
								<div data-posted-job-id="${job.id }">${job.jobName }</div>
							</c:forEach>
						</div>
					</div>
					<span>/</span>
				</c:if>
				
				<span id="submitPosting_preview" class="page-content-link"
					 data-section-id="employmentContainer">Preview job posting</span>
			</div>
			<div id="submitPosting_final_container">
				<span id="previewJobPosting_Label" class=""
					 >Preview Job Posting</span>
				<span id="editPosting" class="page-content-link"
					 data-section-id="employmentContainer">Edit posting</span>
				<span>/</span>
				<span id="submitPosting_final" class="page-content-link"
					 data-section-id="employmentContainer">Submit posting</span>
			</div>
		</div>
	</div>
	<div class="row">
		<div id="displayExample_jobInfo"  class="col-sm-12">
		
		</div>
	</div>
	<div id="postJobInfoContainer" class="row first">
		<div class="col-sm-2">
			
<!-- 			<div id="copyPreviousPost"><span class="accent">Copy a previous job post</span></div> -->
			<div id="general" class="side-bar selected-blue" data-section-id="generalContainer">General
			</div>
			<div id="date" class="side-bar" data-section-id="datesContainer">Dates</div>
			<div id="times" class="side-bar" data-section-id="timesContainer">Times</div>
			<div id="location" class="side-bar" data-section-id="locationContainer">Location</div>
			<div id="compensation" class="side-bar" data-section-id="compensationContainer">Compensation</div>
			<div id="categories" class="side-bar" data-section-id="categoriesContainer">Categories</div>
			<div id="questions" class="side-bar" data-section-id="questionsContainer">Questions</div>
			<div id="employeeSkills" class="side-bar" data-section-id="employeeSkillsContainer">Employee Skills</div>					
<!-- 			<div id="submitJobContainer" class="">Submit Job</div> -->
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
					<p>Dates</p>
				</div>
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Work Days</p>
						<label><input id="partialAvailabilityAllowed" type="checkbox">Partial Availability Allowed</label>
					</div>
					<div class="col-sm-9">							
						<div class="calendar-container wide">
							<div id="workDaysCalendar_postJob" class="calendar" data-is-showing-job="0">
							</div>											
							<button class="square-button" id="clearCalendar">Clear</button>
						</div>
					</div> 
				</div>			
			</div>		
			
			<div id="timesContainer" class="section-container">
				<div class="header row">
					<p>Times</p>
				</div>
				<div id="noDatesSelected">
					<span class="accent">Please select one or more dates</span>
				</div>
				<div id="timesTableContainer">
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
				
<!-- 				<div class="row"> -->
<!-- 					<div class="label-text col-sm-3"> -->
<!-- 						<p>Start Time</p> -->
<!-- 					</div> -->
<!-- 					<div class="col-sm-9">							 -->
<!-- 						<select id="startTime-singleDate"></select> -->
<!-- 					</div>  -->
<!-- 				</div> -->
<!-- 				<div class="row"> -->
<!-- 					<div class="label-text col-sm-3"> -->
<!-- 						<p>End Time</p> -->
<!-- 					</div> -->
<!-- 					<div class="col-sm-9">							 -->
<!-- 						<select id="endTime-singleDate"></select> -->
<!-- 					</div>  -->
<!-- 				</div>				 -->
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
						<div id="addedQuestions"></div>
					</div>
				</div>			
				
				
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Question Format</p>
					</div>
					<div class="col-sm-9">							
						<select id="questionFormat" class="question-formats">
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
						<textarea id="question" class="" rows="3"></textarea>				
					</div> 
				</div>		
				
				<div class="row" id="answerListContainer">
					<div class="label-text col-sm-3">
						<p>Answers</p>
					</div>
					<div class="col-sm-9">							
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
				</div>		
				<div class="row">
					<div class="label-text col-sm-3">
						<p>Desired</p>
					</div>
					<div class="col-sm-9">							
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
	</div>
</div>


<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp">
</script>